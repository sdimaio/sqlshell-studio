# SQLShell Studio Architecture Guide

**Project:** `sqlshell-studio`  
**Mission:** shell-native JDBC database client with a professional TUI, inspired by SQL Developer and Toad  
**Primary UI stack:** Jexer 2.0  
**Primary execution context:** terminal / SSH / headless servers / tmux / screen

---

## 1. Product Vision

SQLShell Studio is a **professional TUI database client** for environments where:

- X11 is unavailable
- GUI desktops are inconvenient or impossible
- shell access is the primary mode of operation
- developers, DBAs, and operators still need a rich client experience

The goal is **not** to clone SQL Developer feature-by-feature.
The goal is to build a:

> **keyboard-first, shell-native, multi-database JDBC workbench**

with a serious user experience and a clear architecture.

---

## 2. Product Positioning

### 2.1 What SQLShell Studio should be

- a professional TUI database client
- fast to use over SSH
- strong on keyboard navigation
- useful for Oracle, PostgreSQL, and other JDBC targets
- lighter than a desktop GUI
- richer than plain SQL CLI tools

### 2.2 What it should not try to be at first

- a full clone of SQL Developer
- a visual modeling suite
- a PL/SQL debugger
- a drag-and-drop GUI builder
- a huge plugin platform in V1

---

## 3. Design Principles

1. **Shell-first**  
   The application must feel native in a terminal.

2. **Keyboard-first**  
   Mouse support is welcome, but the main workflow must be optimized for keyboard.

3. **Database-core first, UI second**  
   The JDBC and metadata logic must live outside the Jexer layer.

4. **Multi-dialect aware**  
   JDBC is not enough. Each DBMS has its own behavior and metadata quirks.

5. **Professional result viewing**  
   Result grids, errors, query history, and object browser are core features.

6. **Incremental scope**  
   Deliver a strong MVP before adding advanced tooling.

---

## 4. Recommended Development Roadmap

## Phase 1 — MVP

Deliver a usable first version with:

- connection profiles
- JDBC connection test
- SQL editor (single tab or minimal multi-tab)
- execute SQL statement / script
- result table view
- error panel
- query history
- schema browser (schemas/tables/views/columns)
- open/save SQL files
- CSV export

### Success criteria for MVP

A real user can:
- connect to Oracle or PostgreSQL
- browse objects
- run queries
- inspect results
- save and reopen scripts
- export data

---

## Phase 2 — Professional Workflow

Add:

- multi-tab editor
- multi-result tabs
- explain plan support
- object inspector
- better filtering and sorting in results
- large-result pagination or lazy loading
- status panel with timing / row count
- improved connection/session management
- SQL snippets and favorites

---

## Phase 3 — Differentiating Features

Add selected higher-value features:

- Oracle-focused extras
- PostgreSQL-focused extras
- session monitor
- DDL extraction
- bookmarks / workspace persistence
- reusable query library
- schema search / object jump
- optional embedded terminal integration

---

## 5. High-Level Architecture

The architecture should be modular.

```text
sqlshell-studio/
  docs/
    guides/
  sqlshell-core/
  sqlshell-jdbc/
  sqlshell-dialects/
  sqlshell-tui-jexer/
  sqlshell-app/
```

### 5.1 Module responsibilities

#### `sqlshell-core`
Domain model and use-case contracts.

Contains:
- connection profile model
- query request/result model
- metadata model
- service interfaces
- history model
- export abstractions

#### `sqlshell-jdbc`
Low-level JDBC execution and metadata retrieval.

Contains:
- connection factory
- JDBC session implementation
- statement execution
- transaction handling
- result streaming / pagination helpers

#### `sqlshell-dialects`
Database-specific behavior abstraction.

Contains:
- Oracle dialect
- PostgreSQL dialect
- generic ANSI dialect
- optional MySQL / SQL Server later

#### `sqlshell-tui-jexer`
Jexer UI components and windows.

Contains:
- main application shell
- connection manager windows
- SQL editor windows
- result viewers
- schema browser
- dialogs
- table/tree integration adapters

#### `sqlshell-app`
Composition root / startup module.

Contains:
- main entry point
- configuration bootstrap
- dependency wiring
- environment setup

---

## 6. Suggested Java Package Structure

A practical package structure could be:

```text
com.sdimaio.sqlshell
  app/
  core/
    connection/
    query/
    metadata/
    history/
    export/
    session/
  jdbc/
    connection/
    execution/
    metadata/
  dialect/
    api/
    oracle/
    postgres/
    generic/
  tui/
    app/
    windows/
    dialogs/
    widgets/
    models/
    actions/
  config/
  util/
```

---

## 7. Core Domain Model

The core should start with a small but solid model.

### 7.1 Connection model

```text
ConnectionProfile
- id
- name
- jdbcUrl
- driverClass
- username
- passwordRef / secretKey
- dbType
- defaultSchema
- properties
```

```text
DatabaseType
- ORACLE
- POSTGRESQL
- MYSQL
- SQLSERVER
- SQLITE
- OTHER
```

### 7.2 Session model

```text
DatabaseSession
- profile
- live JDBC connection handle
- dialect
- connectedSince
- autoCommit
- currentSchema
- sessionInfo
```

### 7.3 Query execution model

```text
QueryRequest
- sqlText
- executionMode (statement / script / explain)
- limit
- fetchSize
- timeoutSeconds
- parameterMap
```

```text
QueryResult
- executionType
- columnMeta
- rows
- rowCount
- updateCount
- warnings
- executionTimeMillis
- truncated
```

```text
QueryError
- message
- sqlState
- vendorCode
- position
- exceptionClass
```

### 7.4 Metadata model

```text
DbObjectRef
- type
- catalog
- schema
- name
```

```text
TableMetadata
- schema
- tableName
- columns
- primaryKeys
- foreignKeys
- indexes
- ddlPreview
```

```text
ColumnMetadata
- name
- jdbcType
- databaseTypeName
- nullable
- size
- scale
- defaultValue
```

### 7.5 History model

```text
ExecutionHistoryItem
- timestamp
- connectionProfileId
- sqlSnippet
- success
- executionTimeMillis
- rowCount
- errorSummary
```

---

## 8. Service Layer

The service layer should hide JDBC and dialect complexity from the TUI.

### 8.1 Connection services

```text
ConnectionProfileRepository
- loadAll()
- save(profile)
- delete(profileId)
```

```text
ConnectionService
- testConnection(profile)
- openSession(profile)
- closeSession(session)
```

### 8.2 Query services

```text
QueryExecutionService
- execute(session, request)
- executeScript(session, request)
- explain(session, request)
```

### 8.3 Metadata services

```text
MetadataService
- listSchemas(session)
- listTables(session, schema)
- listViews(session, schema)
- describeTable(session, objectRef)
- listProcedures(session, schema)
```

### 8.4 Export services

```text
ExportService
- exportCsv(result, path)
- exportTsv(result, path)
- exportJson(result, path)
```

### 8.5 History services

```text
HistoryService
- add(item)
- recent(limit)
- search(text)
- clear()
```

---

## 9. Dialect Layer

The dialect layer is essential.

### 9.1 Why it exists

JDBC does not normalize everything that matters for a rich client:

- schema discovery differs by vendor
- explain plan differs
- DDL retrieval differs
- object categories differ
- pagination may differ
- session info may differ

### 9.2 Dialect interface

```text
DatabaseDialect
- getType()
- testQuery()
- listSchemasSql()
- listSessionInfoSql()
- buildExplainRequest(sql)
- supportsExplain()
- supportsPackages()
- supportsStoredProcedures()
- normalizeIdentifier(name)
- getTableDdl(...)
```

### 9.3 Initial dialects

#### OracleDialect
Focus on:
- schemas/users
- tables/views/indexes
- packages/procedures
- explain plan integration
- DDL extraction where feasible

#### PostgresDialect
Focus on:
- schemas
- tables/views/functions
- explain support
- current search_path / schema awareness

#### GenericDialect
Fallback for unsupported DBs.

---

## 10. JDBC Execution Strategy

### 10.1 Separate statement modes

Do not treat all SQL as identical.

You need at least:
- single statement execution
- script execution
- explain execution
- metadata lookup execution

### 10.2 Result size strategy

Avoid loading unbounded huge results immediately if possible.

Recommended approach:
- configurable fetch size
- result truncation threshold for safety
- future support for lazy page loading

### 10.3 Transactions

Need explicit support for:
- autocommit on/off
- commit
- rollback

This should be visible in the UI status area.

---

## 11. TUI Information Architecture

The main screen should feel like a text IDE.

## Main layout proposal

```text
+--------------------------------------------------------------+
| Menu Bar                                                     |
+----------------------+---------------------------------------+
| Connections /        | SQL Editor Tabs                       |
| Schema Browser       |                                       |
|                      |                                       |
|                      |                                       |
+----------------------+---------------------------------------+
| Result Tabs / Messages / Explain / History                   |
+--------------------------------------------------------------+
| Status Bar                                                   |
+--------------------------------------------------------------+
```

### 11.1 Left pane

Contains:
- saved connections
- active session info
- schema/object tree

### 11.2 Center pane

Contains:
- SQL editor tabs
- current statement marker
- execution context details

### 11.3 Bottom pane

Contains tabs like:
- Results
- Messages
- Errors
- Explain
- History

### 11.4 Status bar

Show:
- active connection name
- DB type
- current schema
- autocommit state
- rows returned
- execution time

---

## 12. Jexer UI Modules

The Jexer layer should be split into windows and reusable widgets.

### 12.1 Suggested windows

```text
MainWorkbenchWindow
ConnectionManagerWindow
ConnectionEditDialog
SqlEditorWindow
ResultTableWindow / ResultPanel
SchemaBrowserWindow / SchemaPanel
ObjectInspectorWindow / ObjectInspectorPanel
HistoryWindow
ExportDialog
SessionInfoWindow
AboutWindow
```

### 12.2 Suggested reusable widgets

```text
ConnectionTreeWidget
SchemaTreeWidget
SqlEditorPanel
ResultGridPanel
MessageLogPanel
ExplainPlanPanel
StatusSummaryWidget
```

### 12.3 Jexer-specific concerns

Because Jexer is terminal-first, pay extra attention to:
- focus movement
- keyboard shortcuts
- scrolling large results
- resize behavior
- responsiveness under SSH latency

---

## 13. Result Grid Strategy

This is one of the most important design decisions.

### 13.1 V1 grid requirements

- rows and columns viewable
- keyboard navigation
- cell copy
- row copy
- horizontal and vertical scroll
- column labels
- simple width management

### 13.2 V2 improvements

- sorting
- filtering
- freeze columns
- better numeric/date formatting
- export visible or all rows

### 13.3 Technical warning

A result grid can become the hardest widget in the project. Keep V1 modest.

---

## 14. SQL Editor Strategy

### 14.1 V1

- plain editor based on Jexer editor facilities
- save / open files
- execute all text
- execute current selection or current statement later if easy

### 14.2 V2

- multiple tabs
- statement boundary detection
- query snippets
- recent files
- bookmarks

### 14.3 Scope warning

A full SQL editor is a project by itself. Keep the first version practical, not ambitious.

---

## 15. Persistence Strategy

### 15.1 Profiles and settings

Persist locally:
- connection profiles
- recent files
- query history
- UI preferences
- saved snippets

### 15.2 Recommended formats

- JSON for profiles and settings
- plain text or structured JSON for history

### 15.3 Secret management

Passwords should not remain in plain text if avoidable.

For V1 you may allow:
- no password save
- or a simple local secret strategy

But mark this area clearly as security-sensitive.

---

## 16. Non-Functional Requirements

### Performance
- smooth enough on remote shell
- safe behavior on large result sets
- non-blocking UI while query executes where practical

### Reliability
- strong exception handling around JDBC
- no silent failures
- always surface SQLState and vendor code

### Usability
- full keyboard navigation
- predictable shortcuts
- clean status messages

### Portability
- Linux first
- Swing fallback possible
- avoid OS-specific assumptions in core logic

---

## 17. Initial Keyboard Strategy

Suggested shortcuts:

- `Ctrl+N` new SQL tab
- `Ctrl+O` open SQL file
- `Ctrl+S` save SQL file
- `F5` execute current SQL
- `F6` explain current SQL
- `Ctrl+L` focus schema browser
- `Ctrl+E` focus editor
- `Ctrl+R` focus results
- `Ctrl+H` history
- `Ctrl+W` close tab/window
- `Alt+X` exit

Keep them consistent across the app.

---

## 18. Risks and Technical Challenges

### 18.1 Multi-DB support
Risk: complexity grows quickly.  
Mitigation: strong dialect layer from day one.

### 18.2 Large result sets
Risk: UI freezes or memory blowups.  
Mitigation: row limits, fetch size, staged loading.

### 18.3 Editor scope creep
Risk: too much time spent on editing features.  
Mitigation: keep V1 plain and robust.

### 18.4 Metadata inconsistency
Risk: object browser behaves differently per DB.  
Mitigation: normalize through metadata services and dialect helpers.

---

## 19. First Development Milestones

## Milestone 0 — Project skeleton
- repository structure
- build files
- docs
- startup app shell

## Milestone 1 — Connection manager
- profile CRUD
- test connection
- session open/close

## Milestone 2 — SQL editor + execute
- editor pane
- run statement
- messages pane

## Milestone 3 — Result viewer
- table rendering
- copy/export basics

## Milestone 4 — Schema browser
- schema tree
- table/view browser
- object detail pane

## Milestone 5 — History and persistence
- recent queries
- recent files
- profiles persisted

At milestone 5, the project becomes truly useful.

---

## 20. Recommended Next Deliverables

The best next project documents to produce are:

1. `PRODUCT_ROADMAP.md`
2. `MODULE_LAYOUT.md`
3. `UI_LAYOUT_GUIDE.md`
4. `DIALECT_API.md`
5. `MVP_BACKLOG.md`

And then code skeletons for:

- app bootstrap
- core models
- JDBC session layer
- connection manager window
- main workbench shell

---

## Conclusion

SQLShell Studio should be built as a **serious shell-native database workbench**, not as a rushed clone of a desktop giant.

If the architecture stays modular and the scope stays disciplined, this can become a very strong and genuinely useful tool:

- ideal for headless environments
- efficient over SSH
- productive from keyboard
- valuable for DBAs and developers alike

That is the right target.
