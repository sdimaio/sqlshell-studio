# Java 25 Multi-Module Delivery Blueprint

This blueprint captures the reusable repository and build shape implied by the studied projects. It is intended as a practical template for future systems that want the same delivery posture.

---

## 1. Objectives

A repository following this blueprint should make it easy to:

- enforce Java 25 as a build/runtime contract
- separate architectural concerns into modules
- run quality, security, and packaging flows from the root
- ship application code together with operational scripts and documentation
- support incremental modernization without collapsing into a monolith

---

## 2. Reference Repository Shape

```text
project-root/
  docs/
    architecture/
    design/
    implementation/
    operations/
    runbook/
    release/
    tests/
    adr/
  scripts/
  kernel/
  app/ or springboot-ms/
  lib-foo/
  lib-bar/
  native/
  lib-jni/
  benchmarks/
  webgui/
  pom.xml
  mvnw
  mvnw.cmd
```

Not every project needs every directory, but the structure should communicate intent.

---

## 3. Root POM Blueprint

The root `pom.xml` should be an aggregator and policy container.

### It should define

- project coordinates
- Java 25 baseline
- Maven minimum version
- plugin versions in one place
- dependency management where useful
- module list
- shared build policy

### Example policy elements

- `maven-enforcer-plugin` to reject wrong Java/Maven versions
- `maven-compiler-plugin` with `release=25`
- `maven-surefire-plugin` with explicit behavior
- named profiles for `quality`, `security`, `sbom`, `coverage`, `distribution`

### Why this matters

A root build is not just for convenience. It is where the repository declares what it is willing to build and support.

---

## 4. Module Taxonomy

### 4.1 Kernel

Purpose:
- low-level runtime abstractions
- shared utility primitives
- logging/runtime/platform helpers

Keep `kernel` focused. It should not become an accidental dumping ground.

### 4.2 Application service module

Typical names:
- `springboot-ms`
- `app`
- `service`

Purpose:
- application wiring
- Spring Boot entry point
- controllers/endpoints
- configuration binding
- orchestration

### 4.3 Domain or capability libraries

Examples:
- `libfs4j`
- `libclust4j`
- `libdoc4j`
- `libs3store4j`
- `libtelemetry`
- `libsecurity-*`

Purpose:
- isolate optional or specialized capabilities
- keep the application layer thin
- enable focused tests and migrations

### 4.4 Native / JNI modules

Typical split:
- `native` for C/C++ build outputs
- `lib-jni` for Java wrappers and native bindings

Purpose:
- keep portability boundaries explicit
- make native integration inspectable and build-managed

### 4.5 Benchmarks module

Purpose:
- JMH or custom performance verification
- repeatable measurements for architecture tradeoffs

Use it when the project has true performance-sensitive paths.

---

## 5. Java 25 Baseline Policy

### Mandatory policy

- set `java.version=25`
- set `maven.compiler.release=25`
- reject unsupported runtimes through Enforcer
- avoid hidden per-module downgrades

### Recommended policy

- prefer stable Java 25 features first
- document optional runtime features separately
- treat preview features as an exception, not the default

### Good example of controlled modernization

A migration note should explicitly distinguish:

- stable language/runtime features adopted
- optional runtime flags
- features intentionally deferred

This is superior to “we upgraded to Java 25” with no detail.

---

## 6. Build Profiles Blueprint

A professional reactor should expose named profiles for delivery concerns.

## 6.1 `quality`

Typical contents:
- SpotBugs
- static analysis checks
- stricter verification gates

## 6.2 `security`

Typical contents:
- dependency vulnerability checks
- dependency inventory validation
- rate-limit aware configuration for external feeds if required

## 6.3 `sbom`

Typical contents:
- CycloneDX or equivalent SBOM generation

## 6.4 `coverage`

Typical contents:
- per-module JaCoCo
- optional aggregate coverage report

## 6.5 `distribution`

Typical contents:
- assembly packaging
- final runtime layout
- tar/zip/distribution output

### Rule

A profile should correspond to a real delivery concern, not a random convenience flag.

---

## 7. Test Stratification Blueprint

A strong repository distinguishes test categories.

### Suggested classes of tests

- unit tests
- integration tests
- operational smoke tests
- non-functional tests
- benchmarks

### Possible implementation mechanisms

- Maven groups/tags
- dedicated profiles
- naming conventions
- shell wrappers for smoke or environment-dependent tests

### Why this matters

Not all tests belong in the default inner loop.  
But all important test categories should be visible and executable.

---

## 8. Scripts Blueprint

A Java system is not fully delivered if it cannot be operated from the repository itself.

### Expected script families

- build scripts
- start scripts
- stop scripts
- status scripts
- smoke test scripts
- quality/security wrappers
- environment/bootstrap helpers
- monitoring and diagnostics helpers

### Script design principles

- fail fast
- explicit environment loading
- readable logs
- understandable error messages
- no silent fallback magic

### Recommended shell baseline

Use, when appropriate:

```bash
set -euo pipefail
```

and central shared shell libraries for repeated logic.

---

## 9. Documentation Blueprint

Treat documentation as part of the architecture.

### Recommended docs taxonomy

```text
docs/
  architecture/
  design/
  implementation/
  operations/
  runbook/
  release/
  tests/
  adr/
```

### Design rule

Do not let architecture live only in people’s heads or in diagrams without prose.  
The repository should explain itself.

---

## 10. Operational Readiness Blueprint

A Java service should usually ship with:

- startup profile strategy
- health endpoints
- metrics strategy
- logging strategy
- service descriptor or equivalent
- runtime configuration template
- deployment and rollback notes

### Why

The build is not complete when the jar exists.  
It is complete when the service can be started, inspected, and recovered consistently.

---

## 11. Modernization Blueprint

When modernizing a legacy or constrained system, use this pattern.

### Step 1 — baseline
- make the build reproducible
- add wrapper
- add version enforcement
- identify target branch

### Step 2 — platform move
- align Java release level
- align build plugins
- remove legacy import/runtime assumptions

### Step 3 — application framework move
- Spring Boot migration
- Jakarta migration if needed
- runtime config alignment

### Step 4 — operations
- startup script alignment
- service unit alignment
- environment variable rationalization

### Step 5 — hardening
- tests
- smoke checks
- observability
- release readiness

### Rule

Never present a modernization branch as “done” if only the build is green and operational validation is missing.

---

## 12. Recommended Engineering Conventions

### Code
- English only
- Javadoc for public and non-trivial types
- rationale-rich comments
- explicit failure behavior

### Build
- root-first policy
- no hidden module drift
- wrapper committed
- reproducible commands documented

### Repository
- architecture visible in modules
- shell and docs versioned
- native boundaries explicit

### Delivery
- branch-driven
- PR-driven
- evidence-driven

---

## 13. Minimal Blueprint Example

A small but serious Java 25 project might look like this:

```text
my-system/
  docs/
    architecture/
    design/
    runbook/
  scripts/
    start.sh
    stop.sh
    status.sh
    verify-all.sh
  kernel/
  app/
  lib-storage/
  lib-telemetry/
  pom.xml
  mvnw
```

And the root build should already know:
- which Java version is legal
- how to run quality checks
- how to produce security artifacts
- how to package the runtime

---

## 14. Anti-Patterns This Blueprint Avoids

- single massive application module with mixed responsibilities
- build logic duplicated in every child module
- undocumented shell deployment knowledge
- “works on my Java version” ambiguity
- migration notes hidden in chat history instead of repository documents
- architecture only visible through package accidents

---

## 15. Final Guidance

This blueprint is not about making repositories bigger.  
It is about making them **more truthful**.

A truthful repository shows:

- what it is
- how it builds
- how it runs
- how it fails
- how it is modernized
- how it is operated

That is the real value of the studied delivery style.
