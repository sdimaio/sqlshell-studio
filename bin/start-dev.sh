#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

cd "${REPO_ROOT}"
"${REPO_ROOT}/build.sh" -DskipTests compile
JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/jdk-25.0.1}" PATH="${JAVA_HOME}/bin:${PATH}" mvn -q -pl sqlshell-app -am exec:java
