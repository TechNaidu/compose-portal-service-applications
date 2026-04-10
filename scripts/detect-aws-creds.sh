#!/usr/bin/env bash
# Detect potential AWS credentials in the repository (POSIX shell)
set -euo pipefail

ROOT_DIR=$(git rev-parse --show-toplevel 2>/dev/null || echo "${PWD}")
cd "${ROOT_DIR}"

echo "Scanning ${ROOT_DIR} for AWS access keys and likely secrets..."
FOUND=0

# AKIA pattern
if grep -RIn --exclude-dir=.git -E "AKIA[0-9A-Z]{16}" . >/dev/null 2>&1; then
  echo "Found AKIA access key pattern:";
  grep -RIn --exclude-dir=.git -E "AKIA[0-9A-Z]{16}" || true
  FOUND=1
fi

# aws secret or var names
if grep -RIn --exclude-dir=.git -E "aws_secret_access_key|AWS_SECRET_ACCESS_KEY|aws_access_key_id|AWS_ACCESS_KEY_ID" . >/dev/null 2>&1; then
  echo "Found AWS credential variable names or possible hard-coded values:";
  grep -RIn --exclude-dir=.git -E "aws_secret_access_key|AWS_SECRET_ACCESS_KEY|aws_access_key_id|AWS_ACCESS_KEY_ID" || true
  FOUND=1
fi

# Long base64-like strings heuristic
if grep -RIn --exclude-dir=.git -E "[A-Za-z0-9+/=]{40,}" . | head -n 1 >/dev/null 2>&1; then
  echo "Found long base64-like strings (heuristic):";
  grep -RIn --exclude-dir=.git -E "[A-Za-z0-9+/=]{40,}" | head -n 50 || true
  FOUND=1
fi

if [ "$FOUND" -ne 0 ]; then
  echo "\nERROR: Potential credentials were found. Rotate any exposed keys and remove them from Git history."
  exit 1
fi

echo "No obvious AWS creds found (scan is heuristic)."

