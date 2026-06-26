#!/usr/bin/env bash
# ============================================================
# quality-gate.sh — Pre-merge quality gate
# ============================================================
# Runs all checks before a PR can be merged:
#   1. Backend compiles (mvn compile -q)
#   2. Frontend builds (npm run build)
#   3. Tests pass (vitest run)
#   4. Smoke routes pass
#   5. false-green check passes
#
# Failures are accumulated (not short-circuited) so every
# problem is visible in a single run.
# ============================================================

set -u

FAIL=0

# --- helpers --------------------------------------------------

pass()  { echo "  [PASS] $*"; }
fail()  { echo "  [FAIL] $*"; FAIL=$((FAIL + 1)); }

run_step() {
  local label="$1" ; shift
  echo ""
  echo "=== $label ==="
  if "$@" 2>&1; then
    pass "$label"
  else
    fail "$label"
  fi
}

# --- paths ----------------------------------------------------

PROJECT_ROOT="/f/yatushi-6-6"
BACKEND_DIR="$PROJECT_ROOT/yts_project"
FRONTEND_DIR="$PROJECT_ROOT/yts_project_vueai"

# --- 1. Backend compile ---------------------------------------

echo ""
echo "╔══════════════════════════════════════════════════════════╗"
echo "║   Quality Gate                                        ║"
echo "╚══════════════════════════════════════════════════════════╝"

run_step "Backend compile" \
  bash -c "cd '$BACKEND_DIR' && mvn compile -q"

# --- 2. Frontend build ----------------------------------------

run_step "Frontend build" \
  bash -c "cd '$FRONTEND_DIR' && npm run build 2>&1 | tail -3; exit \${PIPESTATUS[0]}"

# --- 3. Tests -------------------------------------------------

run_step "Tests" \
  bash -c "cd '$FRONTEND_DIR' && npm run test:run 2>&1 | tail -5; exit \${PIPESTATUS[0]}"

# --- 4. Smoke routes ------------------------------------------

run_step "Smoke routes" \
  bash -c "cd '$FRONTEND_DIR' && npx tsx scripts/smoke-routes.ts 2>&1"

# --- 5. False-green check -------------------------------------

run_step "False-green check" \
  bash -c "cd '$PROJECT_ROOT' && npx tsx scripts/check-false-green.ts 2>&1"

# --- Summary ---------------------------------------------------

echo ""
echo "╔══════════════════════════════════════════════════════════╗"
echo "║   Quality Gate Summary                                 ║"
echo "╚══════════════════════════════════════════════════════════╝"

if [ "$FAIL" -gt 0 ]; then
  echo "  FAILURES: $FAIL"
  echo ""
  echo "  One or more quality gates failed. Fix the issues above"
  echo "  and re-run: bash scripts/quality-gate.sh"
  exit 1
else
  echo "  All quality gates passed."
  exit 0
fi
