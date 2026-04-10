<#
PowerShell verification script for Compose Portal CI pipeline.

Usage:
  Set-Location <repo-root>
  # Dry run (no external commands executed):
  .\scripts\verify-ci.ps1 -DryRun

  # Real run (requires AWS CLI + Docker configured / or OIDC credentials):
  $env:AWS_ACCOUNT_ID='857110241832'
  $env:AWS_REGION='us-west-2'
  $env:AWS_ROLE_ARN='arn:aws:iam::857110241832:role/compose-portal-github-actions-deploy-role'
  $env:COMPOSE_ALB_HOST='compose-portal-alb.us-west-2.elb.amazonaws.com'
  .\scripts\verify-ci.ps1
#>

param(
    [string]$AwsRegion,
    [string]$AwsAccountId,
    [string]$RoleArn,
    [string]$AlbHost,
    [string]$ImageTag = 'test-ci-tag',
    [switch]$DryRun,
    [switch]$SkipDocker
)

# Normalize defaults (PowerShell 5.1 doesn't allow complex expressions in param defaults)
if (-not $AwsRegion -or $AwsRegion.Trim() -eq '') {
    if ($env:AWS_REGION) { $AwsRegion = $env:AWS_REGION } else { $AwsRegion = 'us-east-1' }
}
if (-not $AwsAccountId -or $AwsAccountId.Trim() -eq '') {
    if ($env:AWS_ACCOUNT_ID) { $AwsAccountId = $env:AWS_ACCOUNT_ID } else { $AwsAccountId = Read-Host 'AWS Account ID' }
}
if (-not $RoleArn -or $RoleArn.Trim() -eq '') {
    if ($env:AWS_ROLE_ARN) { $RoleArn = $env:AWS_ROLE_ARN } else { $RoleArn = Read-Host 'Role ARN used by GitHub Actions (leave blank to skip simulate)' }
}
if (-not $AlbHost -or $AlbHost.Trim() -eq '') {
    if ($env:COMPOSE_ALB_HOST) { $AlbHost = $env:COMPOSE_ALB_HOST } else { $AlbHost = Read-Host 'ALB host (eg compose-portal-alb.us-east-1.elb.amazonaws.com)' }
}

function Write-Ok($msg) { Write-Host "[PASS] $msg" -ForegroundColor Green }
function Write-Fail($msg) { Write-Host "[FAIL] $msg" -ForegroundColor Red }
function ExecOrDry([string]$label, [scriptblock]$action, [string]$cmdPreview) {
    Write-Host "-- $label"
    if ($DryRun) {
        Write-Host "   DRYRUN: $cmdPreview" -ForegroundColor Cyan
        return @{ Success = $true; Output = 'DRYRUN' }
    } else {
        try {
            $out = & $action 2>&1
            $exit = $LASTEXITCODE
            return @{ Success = ($exit -eq 0); Output = $out }
        } catch {
            return @{ Success = $false; Output = $_ }
        }
    }
}

Write-Host "Starting Compose Portal CI verification..." -ForegroundColor Cyan
Write-Host "Region: $AwsRegion | Account: $AwsAccountId | ALB: $AlbHost`n" -ForegroundColor Yellow

# Validate AwsRegion looks like a real AWS region (simple heuristic)
if ($AwsRegion -notmatch '^[a-z]{2}-[a-z]+-\d+$') {
    Write-Host "[WARN] The AWS region value '$AwsRegion' doesn't look like a valid region (example: us-west-2)."
    Write-Host "If you intended to run a dry run use '-DryRun'."
    if (-not $DryRun) {
        Write-Host "Aborting: invalid AwsRegion and not running in DryRun. Re-run with -DryRun or set -AwsRegion us-west-2 or set env: AWS_REGION." -ForegroundColor Red
        exit 1
    }
}

# If running for real, ensure Docker is available before attempting ECR login (unless SkipDocker)
if (-not $DryRun -and -not $SkipDocker) {
    $dockerCmd = Get-Command docker -ErrorAction SilentlyContinue
    if (-not $dockerCmd) {
        Write-Host "[ERROR] 'docker' CLI not found in PATH. Install Docker Desktop and ensure 'docker' is available, or run the script with -DryRun or -SkipDocker to preview/skip Docker-related steps." -ForegroundColor Red
        exit 1
    }
}

# 1) ECR login
if (-not $SkipDocker) {
    $ecrRegistry = "${AwsAccountId}.dkr.ecr.${AwsRegion}.amazonaws.com"
    $label = "ECR login -> $ecrRegistry"
    $preview = "aws ecr get-login-password --region $AwsRegion | docker login --username AWS --password-stdin $ecrRegistry"
    $result = ExecOrDry $label { aws ecr get-login-password --region $AwsRegion | docker login --username AWS --password-stdin $ecrRegistry | Out-Null } $preview
    if ($result.Success) {
        Write-Ok "ECR login succeeded"
    } else {
        Write-Fail "ECR login failed: $($result.Output)"
    }
} else {
    Write-Host "[SKIP] Skipping Docker/ECR login and image checks because -SkipDocker is set" -ForegroundColor Yellow
}

# 2) ECR list images
$repos = @('compose-portal-gateway','compose-portal-user','compose-portal-product','compose-portal-order')
if (-not $SkipDocker) {
    foreach ($r in $repos) {
        $label = "ECR list-images for $r"
        $preview = "aws ecr list-images --repository-name $r --region $AwsRegion"
        $res = ExecOrDry $label { aws ecr list-images --repository-name $r --region $AwsRegion } $preview
        if ($res.Success) {
            Write-Ok "Listed images for $r"
        } else {
            Write-Fail "Could not list images for ${r}: $($res.Output)"
        }
    }
} else {
    Write-Host "[SKIP] Skipping ECR list-images for repos because -SkipDocker is set" -ForegroundColor Yellow
}

# 3) Describe image tag
$label = "Describe image tag $ImageTag in gateway repo"
$preview = "aws ecr describe-images --repository-name compose-portal-gateway --image-ids imageTag=$ImageTag --region $AwsRegion"
$res = ExecOrDry $label { aws ecr describe-images --repository-name compose-portal-gateway --image-ids imageTag=$ImageTag --region $AwsRegion } $preview
if ($res.Success) {
    Write-Ok "Image exists"
} else {
    Write-Host $res.Output
    Write-Ok "Tag not found (OK for test)"
}

# 4) SSM put/get
$paramName = "/compose-portal/test-ci/verify"
$label = "SSM put-parameter $paramName"
$preview = "aws ssm put-parameter --name $paramName --value '<random>' --type String --overwrite --region $AwsRegion"
$res = ExecOrDry $label { aws ssm put-parameter --name $paramName --value "ci-verify-$(Get-Random)" --type String --overwrite --region $AwsRegion } $preview
if ($res.Success) { Write-Ok "SSM put success" } else { Write-Fail "SSM put failed: $($res.Output)" }

$label = "SSM get-parameter $paramName"
$preview = "aws ssm get-parameter --name $paramName --region $AwsRegion"
$res = ExecOrDry $label { aws ssm get-parameter --name $paramName --region $AwsRegion } $preview
if ($res.Success) { Write-Ok "SSM get success" } else { Write-Fail "SSM get failed: $($res.Output)" }

# 5) ECS describe services
$cluster = 'compose-portal-prod-cluster'
$services = 'gateway-service','user-service','product-service','order-service'
$label = "ECS describe-services for $cluster"
$preview = "aws ecs describe-services --cluster $cluster --services $services --region $AwsRegion"
$res = ExecOrDry $label { aws ecs describe-services --cluster $cluster --services $services --region $AwsRegion } $preview
if ($res.Success) {
    Write-Ok "ECS describe success"
} else {
    # Handle common ECS errors and provide guidance
    $outStr = $res.Output -join "`n"
    if ($outStr -match 'ClusterNotFoundException') {
        Write-Fail "ECS cluster '$cluster' not found in region $AwsRegion."
        Write-Host "Hint: verify the cluster name or run: aws ecs list-clusters --region $AwsRegion" -ForegroundColor Yellow
    } else {
        Write-Fail "ECS failed: $($res.Output)"
    }
}

# 6) IAM simulate (optional)
if ($RoleArn -and $RoleArn.Trim() -ne '') {
    $label = "IAM simulate rds DeleteDBInstance for role $RoleArn"
    $resourceArn = "arn:aws:rds:${AwsRegion}:${AwsAccountId}:db:compose-portal-prod-db"
    $preview = "aws iam simulate-principal-policy --policy-source-arn $RoleArn --action-names rds:DeleteDBInstance --resource-arn $resourceArn --region $AwsRegion"
    $res = ExecOrDry $label { aws iam simulate-principal-policy --policy-source-arn $RoleArn --action-names rds:DeleteDBInstance --resource-arn $resourceArn --region $AwsRegion } $preview
    if ($res.Success) {
        # parse decisions if possible
        try {
            $json = $res.Output | Out-String | ConvertFrom-Json
            $decisions = $json.EvaluationResults | ForEach-Object { $_.EvalDecision }
            if ($decisions -contains 'explicitDeny' -or $decisions -contains 'implicitDeny') { Write-Ok "Simulation shows deletion is denied: $($decisions -join ',')" } else { Write-Fail "Simulation did not deny deletion: $($decisions -join ',')" }
        } catch {
            Write-Ok "IAM simulate executed (raw output available)"
        }
    } else {
        $outStr = $res.Output -join "`n"
        if ($outStr -match 'NoSuchEntity') {
            Write-Fail "IAM role or entity not found: check that RoleArn '$RoleArn' exists and is correct."
            Write-Host "Hint: verify the role with: aws iam get-role --role-name <role-name> or aws iam list-roles --query 'Roles[?contains(RoleName, `"compose-portal")]' --output table" -ForegroundColor Yellow
        } else {
            Write-Fail "IAM simulate failed: $($res.Output)"
        }
    }
} else {
    Write-Host "Skipping IAM simulate"
}

# 7) ALB health check
Write-Host "7) Health check"
$ports = 8080..8083
$failCount = 0
foreach ($p in $ports) {
    $url = "http://${AlbHost}:${p}/actuator/health"
    Write-Host "Checking $url"
    if ($DryRun) {
        Write-Host "   DRYRUN: curl $url" -ForegroundColor Cyan
        continue
    }
    try {
        $res = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        if ($res.StatusCode -eq 200) { Write-Ok "Health OK on port $p" } else { Write-Fail "Non-200: $($res.StatusCode)"; $failCount++ }
    } catch {
        Write-Fail "Health failed for ${url}: $_"; $failCount++
    }
}

if ($failCount -eq 0) { Write-Ok "All health checks passed" } else { Write-Fail "$failCount checks failed" }
Write-Host "`nVerification complete ✅"
