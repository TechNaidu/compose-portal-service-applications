<#
Deploy OIDC role & policies and optionally set GitHub secrets.

Usage (PowerShell):
  # interactive (tries to discover AccountId from AWS CLI)
  .\scripts\deploy-oidc.ps1

  # non-interactive
  .\scripts\deploy-oidc.ps1 -AccountId 857110241832 -Region us-west-2 -RepoPattern 'repo:TechNaidu/compose-portal-service-applications:ref:refs/heads/*' -SetGitHubSecrets

Prerequisites:
 - AWS CLI v2 is installed & configured (you can pass --profile if needed by setting AWS_PROFILE env var)
 - (Optional) GitHub CLI `gh` installed & authenticated if -SetGitHubSecrets is used
 - You must have permissions to create IAM OIDC provider + roles (CloudFormation with CAPABILITY_NAMED_IAM)

What it does:
 - Deploys CloudFormation template scripts/iam-policies/iam-setup.yaml to create OIDC provider and GitHubActionsDeployRole
 - Reads RoleArn from stack outputs
 - If -SetGitHubSecrets is specified and `gh` is available, sets repository secrets: AWS_ROLE_ARN, AWS_ACCOUNT_ID, AWS_REGION, COMPOSE_ALB_HOST
 - Prints next manual commands if gh not available
#>

param(
    [string]$AccountId = $(try { aws sts get-caller-identity --query Account --output text } catch { '' }),
    [string]$Region = 'us-west-2',
    [string]$RepoPattern = 'repo:TechNaidu/compose-portal-service-applications:ref:refs/heads/*',
    [switch]$SetGitHubSecrets
)

if (-not $AccountId -or $AccountId -eq '') {
    $AccountId = Read-Host 'AWS AccountId (not auto-detected)'
}

Write-Host "Deploying OIDC stack to region: $Region for account: $AccountId"

$stackName = 'compose-portal-github-oidc'
$templateFile = 'scripts/iam-policies/iam-setup.yaml'

# Detect existing OIDC provider for token.actions.githubusercontent.com
Write-Host "Checking for existing OIDC provider for token.actions.githubusercontent.com..." -ForegroundColor Cyan
$existingOidcArn = ''
try {
    $providers = aws iam list-open-id-connect-providers --query 'OpenIDConnectProviderList[].Arn' --output text 2>$null
    foreach ($p in $providers -split "`n") {
        if (-not $p) { continue }
        try {
            $details = aws iam get-open-id-connect-provider --open-id-connect-provider-arn $p --output json 2>$null | ConvertFrom-Json
            if ($details.Url -eq 'https://token.actions.githubusercontent.com') {
                $existingOidcArn = $p
                break
            }
        } catch {
            # ignore
        }
    }
} catch {
    # ignore list errors
}

if ($existingOidcArn) {
    Write-Host "Found existing OIDC provider: $existingOidcArn" -ForegroundColor Yellow
} else {
    Write-Host "No existing OIDC provider found; CloudFormation will create one." -ForegroundColor Yellow
}

Write-Host "Running CloudFormation deploy..." -ForegroundColor Cyan
if ($existingOidcArn) {
    $deployCmd = "aws cloudformation deploy --stack-name $stackName --template-file $templateFile --capabilities CAPABILITY_NAMED_IAM --region $Region --parameter-overrides AccountId=$AccountId RepoPattern=\"$RepoPattern\" ExistingOIDCProviderArn=\"$existingOidcArn\""
} else {
    $deployCmd = "aws cloudformation deploy --stack-name $stackName --template-file $templateFile --capabilities CAPABILITY_NAMED_IAM --region $Region --parameter-overrides AccountId=$AccountId RepoPattern=\"$RepoPattern\""
}
Write-Host $deployCmd -ForegroundColor Yellow

$deploy = Invoke-Expression $deployCmd
if ($LASTEXITCODE -ne 0) {
    Write-Error "CloudFormation deploy failed. Inspect output above."
    exit 1
}

Write-Host "CloudFormation deploy finished. Reading RoleArn..." -ForegroundColor Cyan
$roleArn = aws cloudformation describe-stacks --stack-name $stackName --query "Stacks[0].Outputs[?OutputKey=='RoleArn'].OutputValue" --output text --region $Region
if (-not $roleArn) {
    Write-Error "Failed to read RoleArn from CloudFormation outputs"
    exit 1
}

Write-Host "Role ARN: $roleArn" -ForegroundColor Green

# Default ALB host
$albHost = "compose-portal-alb.$Region.elb.amazonaws.com"

if ($SetGitHubSecrets) {
    # Check for gh
    $ghPath = Get-Command gh -ErrorAction SilentlyContinue
    if (-not $ghPath) {
        Write-Warning "GitHub CLI 'gh' not found; cannot set repository secrets automatically. Install GH or set secrets manually in GitHub settings."
    } else {
        Write-Host "Setting GitHub repository secrets via gh..." -ForegroundColor Cyan
        # repo is inferred from git remote
        $remoteUrl = git config --get remote.origin.url
        if ($remoteUrl -match 'github.com[:/](.+)\.git') {
            $repo = $Matches[1]
        } else {
            # try to get from gh repo view
            $repo = gh repo view --json nameWithOwner -q .nameWithOwner
        }
        if (-not $repo) {
            Write-Warning "Could not detect repo name automatically. Please set secrets manually or run gh from the repo directory."
        } else {
            Write-Host "Repo detected: $repo"
            gh secret set AWS_ROLE_ARN --repo $repo --body $roleArn
            gh secret set AWS_ACCOUNT_ID --repo $repo --body $AccountId
            gh secret set AWS_REGION --repo $repo --body $Region
            gh secret set COMPOSE_ALB_HOST --repo $repo --body $albHost
            Write-Host "GitHub secrets set (AWS_ROLE_ARN, AWS_ACCOUNT_ID, AWS_REGION, COMPOSE_ALB_HOST)" -ForegroundColor Green
        }
    }
}

Write-Host "Done. Next steps:" -ForegroundColor Cyan
Write-Host " - Run the verification script: .\scripts\verify-ci.ps1 (set env AWS_ROLE_ARN if needed)" -ForegroundColor Yellow
Write-Host " - Trigger a test workflow by creating a PR or pushing a test branch (e.g. git checkout -b test/ci-smoke; git commit --allow-empty -m 'ci test'; git push origin test/ci-smoke)" -ForegroundColor Yellow

if (-not $SetGitHubSecrets) {
    Write-Host "To set GitHub secrets manually, run these commands in repo settings or using GH CLI:" -ForegroundColor Yellow
    Write-Host "gh secret set AWS_ROLE_ARN --body '$roleArn'" -ForegroundColor Gray
    Write-Host "gh secret set AWS_ACCOUNT_ID --body '$AccountId'" -ForegroundColor Gray
    Write-Host "gh secret set AWS_REGION --body '$Region'" -ForegroundColor Gray
    Write-Host "gh secret set COMPOSE_ALB_HOST --body '$albHost'" -ForegroundColor Gray
}
