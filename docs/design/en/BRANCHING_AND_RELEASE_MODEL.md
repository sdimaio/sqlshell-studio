# Branching and Release Model

This document defines the branching and release workflow to be used in repositories that follow this delivery style.

It is based on a simple principle:

> daily work must happen away from `main`, and integration must happen through pull requests.

---

## 1. Core Branches

### `main`

Purpose:
- protected integration branch
- production-ready or release-grade state
- only updated through reviewed merge/pull requests

Rules:
- no direct daily development on `main`
- no ad-hoc “small emergency edits” without review path
- tags for official releases should come from `main`

### `development`

Purpose:
- primary working branch
- integration baseline for ongoing engineering work
- default target for new work unless a stricter flow is required

Rules:
- daily engineering happens here
- feature/fix branches can be cut from `development`
- work is merged back into `development` first
- `development` is the branch used for normal collaboration

---

## 2. Supporting Branch Categories

### `feature/...`

Use for:
- new functional work
- architectural increments
- modernization steps
- major UI or API additions

Examples:
- `feature/java25-sb3-porting`
- `feature/opensearch-integration`
- `feature/telemetry-platform`

### `fix/...`

Use for:
- defect correction
- operational remediation
- small scope repair work

Example:
- `fix/commandhub-mail-postdrop-nnp`

### `enhancement/...`

Use for:
- improvements that are not pure bug fixes and not full new features
- hardening work
- technical refinement

### `release/...`

Use for:
- stabilization for a release line
- packaging or controlled deployment branches
- final integration and release-specific adjustments

### `archive/...`

Use only when there is a clear reason to preserve a historical experimental or delivery state with a visible branch marker.

---

## 3. Standard Workflow

## 3.1 Daily work

The standard workflow is:

1. update local `development`
2. create a topic branch if the change deserves isolation
3. implement and commit in small logical units
4. push branch to remote
5. open pull request
6. merge into `development`
7. when ready, open PR from `development` to `main`

---

## 3.2 Minimum expected flow

For small repositories or solo work, the minimum acceptable model is:

- always work on `development`
- never work directly on `main`
- merge `development -> main` via PR only

This is the baseline model currently adopted for `sqlshell-studio`.

---

## 4. Pull Request Policy

Every PR should answer these questions clearly:

- what changed
- why it changed
- what was verified
- what remains intentionally out of scope
- whether there are operational or migration implications

### PR target rules

- normal feature/fix work: target `development`
- release promotion: target `main`

### PR style rules

Prefer:
- small or medium cohesive PRs
- explicit verification notes
- architecture or runbook update if behavior changed

Avoid:
- giant mixed-purpose PRs
- hidden unrelated refactors
- undocumented operational behavior changes

---

## 5. Commit Policy

Commit messages should describe meaningful engineering units.

Prefer:
- `Add architecture and Jexer documentation guides`
- `Introduce JDBC dialect abstraction for Oracle and PostgreSQL`
- `Harden startup validation for missing runtime dependencies`

Avoid:
- `misc`
- `fix stuff`
- timestamp-only commit messages in collaborative repositories unless operating in a very constrained emergency workflow

### Ideal commit characteristics

- one idea per commit where feasible
- code + docs updated together when tightly coupled
- build remains healthy across the branch lifecycle

---

## 6. Release Promotion Model

The expected release promotion model is:

1. work lands in `development`
2. integration stabilizes there
3. release PR is opened from `development` to `main`
4. after merge, release tag is created from `main`

### Why this model works

It keeps:
- `main` clean
- review discipline visible
- release promotion explicit
- unfinished work away from the release line

---

## 7. Hotfix Strategy

If a hotfix is required, choose one of the following depending on urgency.

### Preferred path

- branch from `main`
- create `fix/...`
- patch
- PR into `main`
- then back-merge or replay into `development`

### Emergency path

Only if governance demands it:
- direct emergency branch from `main`
- minimal patch
- mandatory follow-up synchronization into `development`

### Rule

A fix applied only to `main` and not reconciled into `development` creates future drift and is considered incomplete.

---

## 8. Documentation Synchronization Rule

When a change affects architecture, operations, or delivery mechanics, the same branch or PR should update the relevant documentation.

Typical examples:
- architecture docs
- runbooks
- startup procedures
- migration notes
- quality/security instructions

This avoids the common failure mode where the code evolves but the delivery knowledge does not.

---

## 9. Branch Naming Guidance

Branch names should communicate scope quickly.

Good examples:
- `feature/sql-editor-tabs`
- `feature/jdbc-postgres-dialect`
- `fix/result-grid-null-rendering`
- `enhancement/query-history-retention`
- `release/2026-10-m1`

Bad examples:
- `test`
- `misc`
- `branch1`
- `newstuff`

---

## 10. What This Model Optimizes For

This branching model is optimized for:

- controlled integration
- traceable delivery
- easier review
- explicit release promotion
- lower risk on the protected branch

It is not optimized for:
- chaotic direct edits on `main`
- undocumented “quick pushes”
- branchless solo coding habits on long-lived repositories

That tradeoff is intentional.

---

## 11. Current Policy for SQLShell Studio

For this repository specifically:

- active branch for day-to-day work: `development`
- remote branch: `origin/development`
- release target branch: `main`
- all merges to `main`: via pull request

### Working rule

We work **always on `development`** unless a separate topic branch is explicitly requested.

---

## 12. Final Rule Set

1. `main` is protected.
2. `development` is the normal working branch.
3. Topic work may branch from `development` when useful.
4. Integration happens through pull requests.
5. Promotion to `main` happens only through reviewed PRs.
6. Docs are updated in the same delivery flow when behavior changes.
7. Hotfixes must be reconciled back into `development`.

That is the branching and release model.
