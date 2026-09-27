#!/usr/bin/env bash

set -euo pipefail

JAVA_CMD="$(command -v java || true)"
MVN_BIN="$(command -v mvn || true)"

if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
  echo "[env] JAVA_HOME is set to: ${JAVA_HOME}"
  "${JAVA_HOME}/bin/java" -version
fi

if [[ -n "${JAVA_CMD}" ]]; then
  echo "[env] java found in PATH: ${JAVA_CMD}"
  "${JAVA_CMD}" -version
  JAVA_BIN="${JAVA_CMD}"
elif [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
  JAVA_BIN="${JAVA_HOME}/bin/java"
else
  echo "[env] No java executable found in JAVA_HOME or PATH" >&2
  exit 1
fi

if [[ -n "${MVN_BIN}" ]]; then
  echo "[env] mvn found in PATH: ${MVN_BIN}"
  JAVA_HOME="$(cd "$(dirname "${JAVA_BIN}")/.." && pwd)" PATH="$(dirname "${JAVA_BIN}"):${PATH}" "${MVN_BIN}" -version | sed -n '1,6p'
fi
