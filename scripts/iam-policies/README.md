This folder contains recommended IAM policy JSON files and example commands to attach them to the GitHub Actions / CI role.

IMPORTANT:
- Replace 123456789012 with your AWS account ID.
- Replace <GITHUB_OIDC_ROLE_NAME> with the role name you create for GitHub Actions (if using OIDC).
- Replace <JENKINS_ROLE_NAME> or other role names as appropriate.

Files and purpose:
- ecr-policy.json           → least-privilege ECR push/pull for the four repositories
- ecs-policy.json           → permissions to register task defs & update ECS services
- ssm-policy.json           → allow read/write to parameters under /compose-portal/* but deny deletes for prod
- deny-db-delete-policy.json→ explicit deny for destructive RDS/DynamoDB actions in production
- cloudwatch-policy.json    → allow writing deployment logs to CloudWatch
- github-oidc-trust.json    → example trust policy for GitHub OIDC role

Example commands (replace placeholders):

# Create role (example) with GitHub OIDC trust and attach the policies below
aws iam create-role --role-name GitHubActionsDeployRole --assume-role-policy-document file://github-oidc-trust.json --description "Role for GitHub Actions to deploy compose-portal"

# Attach inline policies (or create managed policies and attach)
aws iam put-role-policy --role-name GitHubActionsDeployRole --policy-name ComposePortalECRPolicy --policy-document file://ecr-policy.json
aws iam put-role-policy --role-name GitHubActionsDeployRole --policy-name ComposePortalECSPolicy --policy-document file://ecs-policy.json
aws iam put-role-policy --role-name GitHubActionsDeployRole --policy-name ComposePortalSSMPolicy --policy-document file://ssm-policy.json
aws iam put-role-policy --role-name GitHubActionsDeployRole --policy-name DenyProductionDBDeletion --policy-document file://deny-db-delete-policy.json
aws iam put-role-policy --role-name GitHubActionsDeployRole --policy-name ComposePortalCloudWatchPolicy --policy-document file://cloudwatch-policy.json

# NOTE: For organization-level protection consider creating an SCP via AWS Organizations (not covered by these commands here).

Replace ARNs and resource names as needed.

