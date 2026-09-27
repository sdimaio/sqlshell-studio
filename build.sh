#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REQUIRED_JAVA_MAJOR="25"
DEFAULT_JAVA_HOME="/usr/lib/jvm/jdk-25.0.1"

log() {
  printf '[build] %s\n' "$*"
}

fail() {
  printf '[build][error] %s\n' "$*" >&2
  exit 1
}

select_java_home() {
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    echo "${JAVA_HOME}"
    return 0
  fi

  if [[ -x "${DEFAULT_JAVA_HOME}/bin/java" ]]; then
    echo "${DEFAULT_JAVA_HOME}"
    return 0
  fi

  for candidate in /usr/lib/jvm/* /usr/local/java/* /opt/java/*; do
    if [[ -x "${candidate}/bin/java" ]]; then
      if "${candidate}/bin/java" -version 2>&1 | grep -q 'version "25'; then
        echo "${candidate}"
        return 0
      fi
    fi
  done

  return 1
}

JAVA_HOME="$(select_java_home)" || fail "Unable to locate a Java 25 installation."
export JAVA_HOME
export PATH="${JAVA_HOME}/bin:${PATH}"
hash -r

JAVA_MAJOR="$(java -version 2>&1 | sed -n 's/.*version "\([0-9][0-9]*\).*/\1/p' | head -1)"
if [[ "${JAVA_MAJOR}" != "${REQUIRED_JAVA_MAJOR}" ]]; then
  fail "Detected Java ${JAVA_MAJOR:-unknown}, but Java ${REQUIRED_JAVA_MAJOR} is required."
fi

log "Using JAVA_HOME=${JAVA_HOME}"
log "Running Maven build"

cd "${SCRIPT_DIR}"
mvn clean install "$@"
