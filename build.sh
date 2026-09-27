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

java_major_of() {
  local java_bin="$1"
  local raw=""
  local major=""

  if [[ ! -x "${java_bin}" ]]; then
    echo ""
    return 0
  fi

  raw="$(${java_bin} -XshowSettings:properties -version 2>&1 \
    | awk -F'= ' '/java\.specification\.version =/ {gsub(/^[[:space:]]+|[[:space:]]+$/, "", $2); print $2; exit}')"

  if [[ -z "${raw}" ]]; then
    raw="$(${java_bin} -version 2>&1 \
      | awk -F '"' '/version/ {print $2; exit}')"
  fi

  if [[ -z "${raw}" ]]; then
    raw="$(${java_bin} -version 2>&1 | grep -Eo '[0-9]+(\.[0-9]+)?' | head -1 || true)"
  fi

  if [[ -z "${raw}" ]]; then
    echo ""
    return 0
  fi

  if [[ "${raw}" == 1.* ]]; then
    major="${raw#1.}"
    major="${major%%.*}"
  else
    major="${raw%%.*}"
  fi

  echo "${major}"
}

select_java_home() {
  local candidate major java_cmd

  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    major="$(java_major_of "${JAVA_HOME}/bin/java")"
    if [[ "${major}" == "${REQUIRED_JAVA_MAJOR}" ]]; then
      echo "${JAVA_HOME}"
      return 0
    fi
    log "Ignoring JAVA_HOME=${JAVA_HOME} because it points to Java ${major:-unknown}, not Java ${REQUIRED_JAVA_MAJOR}."
  fi

  if [[ -x "${DEFAULT_JAVA_HOME}/bin/java" ]]; then
    major="$(java_major_of "${DEFAULT_JAVA_HOME}/bin/java")"
    if [[ "${major}" == "${REQUIRED_JAVA_MAJOR}" ]]; then
      echo "${DEFAULT_JAVA_HOME}"
      return 0
    fi
  fi

  java_cmd="$(command -v java || true)"
  if [[ -n "${java_cmd}" ]]; then
    major="$(java_major_of "${java_cmd}")"
    if [[ "${major}" == "${REQUIRED_JAVA_MAJOR}" ]]; then
      candidate="$(cd "$(dirname "${java_cmd}")/.." && pwd)"
      echo "${candidate}"
      return 0
    fi
  fi

  for candidate in /usr/lib/jvm/* /usr/local/java/* /opt/java/*; do
    if [[ -x "${candidate}/bin/java" ]]; then
      major="$(java_major_of "${candidate}/bin/java")"
      if [[ "${major}" == "${REQUIRED_JAVA_MAJOR}" ]]; then
        echo "${candidate}"
        return 0
      fi
    fi
  done

  return 1
}

JAVA_HOME="$(select_java_home)" || fail "Unable to locate a Java ${REQUIRED_JAVA_MAJOR} installation."
export JAVA_HOME
export PATH="${JAVA_HOME}/bin:${PATH}"
hash -r

JAVA_MAJOR="$(java_major_of "${JAVA_HOME}/bin/java")"
if [[ "${JAVA_MAJOR}" != "${REQUIRED_JAVA_MAJOR}" ]]; then
  fail "Detected Java ${JAVA_MAJOR:-unknown}, but Java ${REQUIRED_JAVA_MAJOR} is required."
fi

log "Using JAVA_HOME=${JAVA_HOME}"
log "Running Maven build"

cd "${SCRIPT_DIR}"
if [[ -x "${SCRIPT_DIR}/mvnw" ]]; then
  ./mvnw clean install "$@"
else
  mvn clean install "$@"
fi
