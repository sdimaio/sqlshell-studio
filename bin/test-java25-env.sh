#!/usr/bin/env bash

set -euo pipefail

JAVA_BIN="${JAVA_HOME:-/usr/lib/jvm/jdk-25.0.1}/bin/java"
MVN_BIN="$(command -v mvn || true)"

if [[ ! -x "${JAVA_BIN}" ]]; then
  echo "[env] Java binary not found at ${JAVA_BIN}" >&2
  exit 1
fi

"${JAVA_BIN}" -version
if [[ -n "${MVN_BIN}" ]]; then
  "${MVN_BIN}" -version | sed -n '1,6p'
fi
