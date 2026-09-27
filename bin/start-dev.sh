#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

# Build the full reactor first so all module artifacts are installed locally.
# The application is then launched from the sqlshell-app module directly,
# because running exec:java from the reactor root would target the aggregator
# project, which intentionally has no main class.
cd "${REPO_ROOT}"
"${REPO_ROOT}/build.sh" -DskipTests compile
JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/jdk-25.0.1}" PATH="${JAVA_HOME}/bin:${PATH}" \
  mvn -q -f sqlshell-app/pom.xml exec:java
