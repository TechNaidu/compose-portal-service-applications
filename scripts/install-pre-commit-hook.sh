#!/usr/bin/env bash
# Installs a pre-commit hook that scans for AWS creds before committing
set -euo pipefail
ROOT_DIR=$(git rev-parse --show-toplevel 2>/dev/null || echo "${PWD}")
HOOK_DIR="$ROOT_DIR/.git/hooks"
HOOK_FILE="$HOOK_DIR/pre-commit"

cat > "$HOOK_FILE" <<'HOOK'
#!/usr/bin/env bash
# pre-commit hook: detect AWS credentials
set -euo pipefail
ROOT_DIR=$(git rev-parse --show-toplevel 2>/dev/null || echo "${PWD}")
SCRIPTS_DIR="$ROOT_DIR/scripts"

# Prefer the local scripts if present
if [ -x "$SCRIPTS_DIR/detect-aws-creds.sh" ]; then
  "$SCRIPTS_DIR/detect-aws-creds.sh" || {
    echo "Pre-commit: AWS credential scan failed. Commit aborted."
    exit 1
  }
fi

exit 0
HOOK

chmod +x "$HOOK_FILE"

echo "Installed pre-commit hook at $HOOK_FILE"

