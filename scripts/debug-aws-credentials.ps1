<#
PowerShell helper to diagnose AWS SignatureDoesNotMatch and credential issues on Windows.

Usage:
  # Dry run diagnostics (no AWS calls):
  .\scripts\debug-aws-credentials.ps1 -DryRun

  # Real run (runs aws sts get-caller-identity --debug and saves output):
  .\scripts\debug-aws-credentials.ps1

What it does:
  - Prints and masks AWS-related environment variables
  - Displays contents of %USERPROFILE%\.aws\credentials and config (masked)
  - Detects malformed or duplicated secrets
  - Optionally clears AWS_* env vars in current session
  - Runs 'aws sts get-caller-identity --debug' and saves debug output to aws-sts-debug.txt

Important: This script will mask secrets when displaying them to the console. It does not upload anything.
#>
param(
    [switch]$DryRun,
    [switch]$ClearEnv
)

function Mask([string]$s) {
    if (-not $s) { return '' }
    if ($s.Length -le 6) { return ('*' * $s.Length) }
    return $s.Substring(0,3) + ('*' * ([Math]::Max(3, $s.Length - 6))) + $s.Substring($s.Length-3)
}

function Show-EnvVar([string]$name) {
    $val = (Get-ChildItem Env:$name -ErrorAction SilentlyContinue).Value
    if ($null -eq $val -or $val -eq '') { Write-Host "  $name = <not set>" -ForegroundColor DarkYellow; return $null }
    Write-Host "  $name = $(Mask $val)"
    return $val
}

Write-Host "AWS credential debug helper" -ForegroundColor Cyan
Write-Host "DryRun: $DryRun | ClearEnv: $ClearEnv`n"

# 1) Show environment variables
Write-Host "Environment variables:" -ForegroundColor Yellow
$envAccessKey = Show-EnvVar 'AWS_ACCESS_KEY_ID'
$envSecretKey = Show-EnvVar 'AWS_SECRET_ACCESS_KEY'
$envSession = Show-EnvVar 'AWS_SESSION_TOKEN'
$envProfile = Show-EnvVar 'AWS_PROFILE'
$envRegion = Show-EnvVar 'AWS_REGION'

# 2) Show credentials file contents (masked)
$credsPath = Join-Path $env:USERPROFILE '.aws\credentials'
$configPath = Join-Path $env:USERPROFILE '.aws\config'

function Show-CredsFile([string]$path) {
    if (Test-Path $path) {
        Write-Host "\n$path:" -ForegroundColor Yellow
        $lines = Get-Content $path -ErrorAction SilentlyContinue
        $currentSection = ''
        foreach ($l in $lines) {
            $t = $l.Trim()
            if ($t -match '^\[.+\]') { $currentSection = $t; Write-Host "  $t" -ForegroundColor Cyan; continue }
            if ($t -match '^(aws_access_key_id|aws_secret_access_key|aws_session_token)\s*=\s*(.+)$') {
                $k = $matches[1]; $v = $matches[2].Trim()
                Write-Host "    $k = $(Mask $v)"
            } else {
                Write-Host "    $t"
            }
        }
    } else {
        Write-Host "\n$path: <not found>" -ForegroundColor DarkYellow
    }
}

Show-CredsFile $credsPath
Show-CredsFile $configPath

# 3) Heuristics: look for duplicated secret or accidental double-paste
Write-Host "\nHeuristic checks:" -ForegroundColor Yellow

function Heuristic-Check-Secret([string]$s, [string]$which) {
    if (-not $s) { return }
    if ($s.Length -ge 40) {
        Write-Host "  [$which] looks long (length=$($s.Length))." -ForegroundColor Cyan
        # check for repeated halves
        if ($s.Length % 2 -eq 0) {
            $half = $s.Substring(0, $s.Length/2)
            if ($half -eq $s.Substring($s.Length/2)) {
                Write-Host "    -> Value appears to be duplicated (same first/second half). This causes SignatureDoesNotMatch." -ForegroundColor Red
            }
        }
        # check for obvious repetition of a substring
        if ($s -match '(.{8,})\1') {
            Write-Host "    -> Value contains repeated substring pattern (possible accidental paste)." -ForegroundColor Red
        }
    }
}

Heuristic-Check-Secret $envSecretKey 'ENV:AWS_SECRET_ACCESS_KEY'

# Also scan credentials file raw values for secrets (not printing them fully)
if (Test-Path $credsPath) {
    $raw = Get-Content $credsPath -ErrorAction SilentlyContinue | Select-String -Pattern 'aws_secret_access_key\s*=\s*(.+)' -AllMatches
    foreach ($m in $raw.Matches) {
        $val = $m.Groups[1].Value.Trim()
        Heuristic-Check-Secret $val "FILE:${credsPath}"
    }
}

# 4) Option to clear env vars in current session
if ($ClearEnv) {
    Write-Host "\nClearing AWS_* environment variables in this session..." -ForegroundColor Yellow
    Remove-Item Env:AWS_ACCESS_KEY_ID -ErrorAction SilentlyContinue
    Remove-Item Env:AWS_SECRET_ACCESS_KEY -ErrorAction SilentlyContinue
    Remove-Item Env:AWS_SESSION_TOKEN -ErrorAction SilentlyContinue
    Remove-Item Env:AWS_PROFILE -ErrorAction SilentlyContinue
    Write-Host "Cleared." -ForegroundColor Green
}

# 5) Attempt to run aws sts get-caller-identity --debug (if not DryRun)
if ($DryRun) {
    Write-Host "\nDryRun enabled: skipping AWS CLI calls. To run live diagnostics, re-run without -DryRun." -ForegroundColor Yellow
    exit 0
}

Write-Host "\nRunning: aws sts get-caller-identity --debug" -ForegroundColor Yellow
try {
    $outFile = Join-Path (Get-Location) 'aws-sts-debug.txt'
    aws sts get-caller-identity --debug 2>&1 | Tee-Object -FilePath $outFile
    Write-Host "Debug output saved to $outFile" -ForegroundColor Green
    Write-Host "Look in the debug output for lines that show which credentials were used and any signing headers. Common clues: 'Found credentials in shared credentials file', 'Found credentials in environment', or 'SignatureDoesNotMatch' block with canonical request details."
} catch {
    Write-Host "AWS CLI call failed: $_" -ForegroundColor Red
    Write-Host "Ensure AWS CLI is installed and on PATH. You can run 'aws --version' to check." -ForegroundColor Yellow
}

Write-Host "\nSuggestions if you still see SignatureDoesNotMatch:" -ForegroundColor Cyan
Write-Host "  1) Ensure you rotated the exposed keys in the AWS Console and are using the new keys." -ForegroundColor White
Write-Host "  2) Remove any accidental duplication: if your secret looks exactly like two copies of a shorter string concatenated, replace it with the correct single-secret value." -ForegroundColor White
Write-Host "  3) Remove surrounding quotes or trailing spaces in credentials file or env vars. Quotes become part of the secret and will break signing." -ForegroundColor White
Write-Host "  4) Check for an AWS_SESSION_TOKEN requirement (temporary credentials) — if you used temporary creds, set AWS_SESSION_TOKEN as well." -ForegroundColor White
Write-Host "  5) For clock skew: run PowerShell as Administrator and run: w32tm /resync" -ForegroundColor White
Write-Host "\nIf you want, run: .\scripts\debug-aws-credentials.ps1 -ClearEnv to clear AWS env vars in this session and then re-run 'aws configure'." -ForegroundColor Cyan

