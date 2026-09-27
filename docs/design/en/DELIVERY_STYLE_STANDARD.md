# Delivery Style Standard

**Official language for code, design, tests, and implementation artifacts: English**  
**Documentation policy: bilingual when useful for delivery, handoff, or stakeholder communication**

This document defines the engineering standard distilled from the delivery practices observed in the DRM1 and CommandHUB repositories.

---

## 1. Scope of This Standard

This standard applies to:

- source code
- Javadoc and inline comments
- test code
- build logic
- module naming
- branch and PR workflow
- shell scripts and operational tooling
- architecture and design documents
- migration and delivery reports

It is intended for repositories that want to follow the same engineering discipline.

---

## 2. Language Policy

## 2.1 Code-facing artifacts

The following must be written in **English**:

- Java source code
- class names
- method names
- package names
- Javadoc
- inline comments
- test names
- test descriptions
- architecture/design documents intended for engineers
- build scripts comments when the repository standard is English-first
- issue/PR technical summaries when possible

## 2.2 Documentation-facing artifacts

The following may be **bilingual** when delivery context requires it:

- executive summaries
- architecture reports for mixed audiences
- runbooks
- deployment instructions
- certification/validation documents
- customer-facing project notes

### Rule

English remains the canonical engineering language.  
Localized documentation is a delivery service layer, not the primary source of truth.

---

## 3. Code Documentation Standard: Sanfilippo Mode

The documentation style for code is not descriptive fluff. It is engineering memory.

### Mandatory principles

Javadoc and meaningful comments must:

1. explain **why**, not merely **what**
2. document invariants
3. expose complexity or performance implications where relevant
4. record tradeoffs
5. describe failure modes and lifecycle constraints
6. make concurrency and thread-safety explicit
7. help future senior engineers understand non-obvious decisions

### Public type Javadoc should cover

- responsibility and scope
- invariants
- thread-safety
- failure behavior
- lifecycle constraints
- complexity/performance notes when meaningful
- usage examples for reusable APIs
- operational impact where relevant

### Method Javadoc is required when

- the algorithm is not obvious
- side effects are important
- concurrency exists
- performance matters
- contracts are easy to misuse
- external resources are involved

### Style guidance

- be sober
- be precise
- avoid paraphrasing the implementation line by line
- optimize for maintainability and transfer of intent

---

## 4. Java Platform Standard

## 4.1 Runtime contract

The Java version must be treated as a contract, not a suggestion.

Recommended baseline pattern:

- declare Java version once at root
- use Maven Enforcer to reject unsupported runtimes
- align source/target/release consistently
- keep runtime scripts version-aware

## 4.2 Modernization policy

Prefer:

- stable Java features first
- measurable gains over fashionable syntax
- migration documents that separate completed work from remaining work

Avoid:

- preview-feature enthusiasm without operational need
- silent runtime drift
- multiple hidden Java baselines inside the same reactor

---

## 5. Repository Structure Standard

A serious repository should show architecture through structure.

### Recommended top-level pattern

```text
repo/
  docs/
  scripts/
  kernel/
  springboot-ms/ or app/
  lib-*/
  native/
  lib-jni/
  benchmarks/        (if needed)
  webgui/            (if needed)
  pom.xml
  mvnw
```

### Structural principles

- root repository is an orchestrator
- cross-cutting foundations live in `kernel`
- separate libraries represent real architectural concerns
- native and JNI boundaries stay explicit
- operational scripts live in the repository, not in people’s memory

---

## 6. Maven Standard

## 6.1 Root POM responsibilities

The root POM should define:

- Java baseline
- Maven minimum version
- plugin versions
- dependency management where appropriate
- module list
- shared quality/security profiles

## 6.2 Preferred build traits

- Maven Wrapper present
- `maven-enforcer-plugin` enabled
- `maven-compiler-plugin` configured with `release`
- test execution behavior explicit
- named profiles for cross-cutting concerns

### Recommended profiles

- `quality`
- `security`
- `sbom`
- `coverage`
- `distribution`

Profiles should exist only if they correspond to real delivery activities.

---

## 7. Testing Standard

## 7.1 Tests are delivery assets

Tests should not be treated as optional polish.

Tests should cover:

- business behavior
- configuration binding
- resilience boundaries
- failure-path behavior
- operational invariants where possible

## 7.2 Naming standard

Test names should read like executable engineering statements.

Example:
- `retriesTransientTransportFailuresThenSucceeds`
- `neverPropagatesFailuresIntoArchiveLifecycle`
- `platformRequirementsSatisfied`

## 7.3 Test taxonomy

When necessary, distinguish:

- unit tests
- integration tests
- non-functional tests
- load/performance tests
- smoke tests

This taxonomy should be visible in naming, grouping, or Maven profile usage.

---

## 8. Operational Tooling Standard

A delivered system is not complete without runnable operational tooling.

### Required operational artifacts where applicable

- start script
- stop script
- status script
- smoke test script
- environment template
- service unit or equivalent
- troubleshooting helpers

### Operational shell standards

Shell scripts should:

- use `set -euo pipefail` where sensible
- validate prerequisites early
- print meaningful diagnostics
- avoid hidden assumptions
- be readable by operators, not only by developers

### Rule

If an operation matters in production, it should exist as a repository-native procedure.

---

## 9. Architecture Documentation Standard

A software architecture document must explain:

- what the system does
- why the architecture is shaped that way
- module boundaries
- deployment assumptions
- state and persistence model
- security model
- resilience model
- observability model
- operational failure modes

### Preferred style

- full sentences
- explicit assumptions
- diagrams only when backed by written explanation
- tables for invariants, dependencies, deployment, and risk

### Recommended documentation folders

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

---

## 10. Migration and Modernization Standard

Modernization should be documented as an engineering program.

### Required artifacts for major migrations

- scope definition
- target stack statement
- branch under analysis
- verification commands executed
- outcome summary
- known gaps
- next actions

### Good migration practice

- make evidence explicit
- separate “done” from “ready for production”
- keep branch scope visible
- publish risks, not just successes

This is one of the strongest observed practices in the studied repositories.

---

## 11. Observability and Resilience Standard

These concerns must be visible in both code and delivery.

### Expected patterns

- health endpoints
- actuator or equivalent runtime introspection
- metrics publication
- circuit breakers/retries where justified
- structured logs
- operational documentation for telemetry

### Rule

Observability is not a post-deployment add-on. It is part of the application contract.

---

## 12. Native and Hybrid Boundaries

Use native code only when it earns its cost.

### If native/JNI is used

The repository must contain:

- explicit source tree
- build integration
- headers and mapping clarity
- runtime usage explanation
- portability assumptions
- fallbacks or failure strategy

### Rule

The native boundary must never be mystical. It must be inspectable, buildable, and explainable.

---

## 13. Branching and Delivery Workflow

### Required working model

- `main` is protected and merge-only
- daily engineering happens on `development` or topic branches from it
- work is integrated through pull requests
- the branch name should reflect scope

### Typical branch categories

- `development`
- `feature/...`
- `fix/...`
- `enhancement/...`
- `release/...`
- `archive/...` (if needed)

### Rule

Never normalize direct, undocumented work on `main`.

---

## 14. Delivery Artifact Checklist

A repository following this style should usually contain, where relevant:

- source code
- tests
- build wrapper
- root build logic
- operational scripts
- docs for architecture
- docs for operations/runbook
- modernization/delivery notes
- deployment descriptors
- quality/security automation

Not every repository needs every artifact. But every repository should make its delivery surface explicit.

---

## 15. What This Style Optimizes For

This delivery style is optimized for:

- maintainability under team turnover
- infrastructure realism
- migration under constraints
- engineering traceability
- operational credibility
- long-lived enterprise software

It is less optimized for:

- minimalism for its own sake
- purely aesthetic code golf
- undocumented “move fast” experimentation

That tradeoff is intentional.

---

## 16. Final Rule Set

If you want a concise operational summary, use this.

1. Write code in English.
2. Document rationale, not just mechanics.
3. Enforce Java and build baselines at the root.
4. Use Maven modules to reflect architecture.
5. Ship shell tooling as part of delivery.
6. Keep architecture, runbook, and migration notes in-repo.
7. Treat observability and resilience as first-class concerns.
8. Accept native code only with explicit discipline.
9. Work through branches and pull requests, not informal mainline edits.
10. Make the repository explain how to build, run, verify, and evolve the system.

That is the delivery standard.
