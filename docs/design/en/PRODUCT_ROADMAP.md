# SQLShell Studio Product Roadmap

**Project:** SQLShell Studio  
**Vision:** a shell-native JDBC database workbench with a professional TUI for headless and SSH-first environments.

---

## 1. Product Direction

SQLShell Studio is intended to occupy the space between:

- low-level SQL shells (`sqlplus`, `psql`, `sqlcmd`, etc.)
- heavyweight desktop GUI tools (SQL Developer, Toad, DBeaver, DataGrip)

The distinguishing value is:

- terminal-native operation
- keyboard-first workflow
- JDBC-based multi-database support
- professional schema/query/result workflow
- strong fit for remote/server environments without X11

---

## 2. Product Principles

1. **Shell-first**  
   The product must remain fully useful over SSH.

2. **Keyboard-first**  
   Every important workflow must be efficient without a mouse.

3. **Database-core first**  
   The JDBC and dialect engine must be independent from the Jexer UI.

4. **Multi-database, but disciplined**  
   Oracle and PostgreSQL come first. Other dialects are optional follow-ons.

5. **MVP before ambition**  
   Build a strong, usable workbench before attempting broad IDE-like scope.

---

## 3. Release Strategy Overview

## Phase 0 — Foundation

Goal: establish repository, architecture, language rules, and module boundaries.

Deliverables:
- repository structure
- Java/Maven baseline
- branching model
- architecture documents
- delivery style documents
- initial Jexer guides

Exit criteria:
- repository is structurally ready for implementation
- documentation reflects product intent and delivery discipline

---

## Phase 1 — MVP Workbench

Goal: create a usable terminal database client.

### Scope

- connection profile management
- JDBC session open/close
- SQL editor (single editor or minimal tab model)
- execute SQL statement / script
- result grid view
- error panel / execution messages
- query history
- schema browser (schemas, tables, views, columns)
- open/save SQL file
- CSV export

### Primary supported databases

- Oracle
- PostgreSQL

### Exit criteria

A user can:
- store a connection profile
- connect to a database
- browse schema objects
- write and run SQL
- inspect results and errors
- save a query to disk
- export a result set to CSV

---

## Phase 2 — Professional Query Workflow

Goal: make the product productive enough for regular technical use.

### Scope

- multi-tab editor
- multi-result tabs
- explain plan support
- object inspector
- improved result navigation
- result filtering and sorting
- better copy/export options
- snippets / saved queries
- recent files and recent connections
- status bar with richer execution metadata

### Exit criteria

The product supports normal day-to-day analysis workflows without feeling like a toy.

---

## Phase 3 — Advanced DBA/Operator Features

Goal: differentiate the product beyond “query runner with browser”.

### Candidate features

- session monitor
- DDL extraction
- schema-wide object search
- object jump / quick-open palette
- bookmarks
- transaction controls and session state indicator
- long-running query watchdog / cancellation
- richer export formats (TSV, JSON)

### Exit criteria

The product starts to feel like a serious operational workbench rather than only a development helper.

---

## Phase 4 — Platform Extensions

Goal: expand reach while preserving architecture.

### Candidate features

- MySQL/MariaDB dialect
- SQL Server dialect
- SQLite support
- plugin or extension hooks (if truly justified)
- embedded terminal workflows
- external tool integration

### Exit criteria

Platform growth does not compromise maintainability or product identity.

---

## 4. Functional Roadmap by Area

## 4.1 Connection Management

### MVP
- create/edit/delete connection profiles
- test connection
- open/close session

### Later
- environment tags (dev/test/prod)
- secret handling improvements
- per-profile default schema and fetch settings

---

## 4.2 SQL Editing

### MVP
- one editor surface
- load/save file
- execute visible SQL

### Later
- tabs
- snippets
- statement boundary detection
- execute current statement
- bookmarks

---

## 4.3 Result Viewing

### MVP
- tabular result display
- scroll vertically and horizontally
- cell and row inspection
- CSV export

### Later
- sorting
- filtering
- copy cell/row/result set
- lazy paging
- formatting rules per data type

---

## 4.4 Schema Navigation

### MVP
- schemas
- tables
- views
- columns

### Later
- indexes
- constraints
- procedures/functions/packages
- object search
- DDL preview

---

## 4.5 Execution Diagnostics

### MVP
- error message panel
- SQLState and vendor code
- basic execution timing

### Later
- structured execution log
- explain plan tab
- warnings panel
- execution statistics history

---

## 4.6 Persistence and Workspace

### MVP
- connection profiles
- query history
- recent files

### Later
- workspace/session restore
- saved layouts
- bookmarks and favorites

---

## 5. Non-Functional Roadmap

## 5.1 Performance

### Early requirements
- no obvious UI freezes during normal operations
- safe handling of large result sets
- configurable fetch size

### Later improvements
- lazy result loading
- tuned metadata retrieval
- background tasks with progress reporting

---

## 5.2 Reliability

### Early requirements
- clear failure reporting
- safe session cleanup
- deterministic connection lifecycle

### Later improvements
- query cancellation model
- fault isolation by dialect/service
- crash-resilient workspace persistence

---

## 5.3 Operability

### Early requirements
- runs cleanly over SSH
- predictable startup
- logs and status feedback

### Later improvements
- richer diagnostics
- easier support data export
- troubleshooting report generation

---

## 6. Release Milestones

## Milestone A — Repository Ready
- docs and architecture baseline committed
- branching model in place
- implementation can start safely

## Milestone B — First Running UI Shell
- app boots
- menus and launcher exist
- placeholder windows in place

## Milestone C — First Real DB Session
- JDBC connect/disconnect works
- first query can be executed

## Milestone D — MVP Usable
- core browsing/editing/execution/export path complete

## Milestone E — Professional Daily Driver
- multi-tab, explain, object inspection, richer history

---

## 7. Scope Guardrails

The following should **not** be prioritized early:

- visual query builder
- ER diagram modeling
- PL/SQL debugger
- drag-and-drop tooling
- full desktop-clone breadth of SQL Developer
- plugin ecosystem too early

Reason:
These features are expensive and do not define the initial value proposition.

---

## 8. Success Definition

SQLShell Studio succeeds if it becomes:

- the preferred terminal database client for remote/headless work
- a productive JDBC workbench for Oracle and PostgreSQL
- a tool that feels serious and trustworthy, not improvised
- a repository that demonstrates strong Java 25 multi-module delivery discipline

That is the roadmap target.
