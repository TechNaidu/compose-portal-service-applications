# PowerShell script to scan the repo for potential AWS credentials
param(
    [string]$Root = (git rev-parse --show-toplevel 2>$null | Out-String).Trim()
)
if (-not $Root) { $Root = Get-Location }
Write-Host "Scanning $Root for AWS creds..."
$found = $false

# AKIA pattern
Get-ChildItem -Recurse -File -Exclude .git | Select-String -Pattern 'AKIA[0-9A-Z]{16}' -SimpleMatch -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "Found AKIA pattern in: $($_.Path):$($_.LineNumber) -> $($_.Line.Trim())"
    $found = $true
}

# Variable names
Get-ChildItem -Recurse -File -Exclude .git | Select-String -Pattern 'aws_secret_access_key|AWS_SECRET_ACCESS_KEY|aws_access_key_id|AWS_ACCESS_KEY_ID' -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "Found credential variable name in: $($_.Path):$($_.LineNumber) -> $($_.Line.Trim())"
    $found = $true
}

# long base64-like strings heuristic
Get-ChildItem -Recurse -File -Exclude .git | Select-String -Pattern '[A-Za-z0-9+/=]{40,}' -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "Found long base64-like string in: $($_.Path):$($_.LineNumber) -> $($_.Line.Trim())"
    $found = $true
}

if ($found) {
    Write-Host "\nERROR: Potential credentials found. Rotate and remove them from git history." -ForegroundColor Red
    exit 1
}

Write-Host "No obvious AWS creds found (heuristic scan)." -ForegroundColor Green

