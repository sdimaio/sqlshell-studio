#!/usr/bin/env bash

set -euo pipefail

JAVA_CMD="$(command -v java || true)"
MVN_BIN="$(command -v mvn || true)"

if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
  JAVA_BIN="${JAVA_HOME}/bin/java"
elif [[ -n "${JAVA_CMD}" ]]; then
  JAVA_BIN="${JAVA_CMD}"
else
  echo "[env] No java executable found in JAVA_HOME or PATH" >&2
  exit 1
fi

"${JAVA_BIN}" -version
if [[ -n "${MVN_BIN}" ]]; then
  JAVA_HOME="$(cd "$(dirname "${JAVA_BIN}")/.." && pwd)" PATH="$(dirname "${JAVA_BIN}"):${PATH}" "${MVN_BIN}" -version | sed -n '1,6p'
fi
