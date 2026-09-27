# SQLShell Studio Module Layout

This document defines the initial modular structure for SQLShell Studio.

The primary rule is simple:

> the database engine, dialect logic, and persistence model must not depend on Jexer.

---

## 1. Initial Module Set

```text
sqlshell-studio/
  docs/
  sqlshell-kernel/
  sqlshell-core/
  sqlshell-jdbc/
  sqlshell-dialects/
  sqlshell-tui-jexer/
  sqlshell-app/
  pom.xml
```

---

## 2. Module Responsibilities

## 2.1 `sqlshell-kernel`

Purpose:
- low-level runtime and platform model
- OS and JVM detection
- bootstrap-oriented environment diagnostics
- future home for delivery-grade startup guards

Should contain:
- no UI classes
- no JDBC logic
- minimal dependencies

Typical packages:

```text
com.sdimaio.sqlshell.kernel.model.system
com.sdimaio.sqlshell.kernel.model.system.os
com.sdimaio.sqlshell.kernel.model.system.runtime
com.sdimaio.sqlshell.kernel.model.system.architecture
```

---

## 2.2 `sqlshell-core`

Purpose:
- stable domain model
- core service contracts
- query/result abstractions
- metadata abstractions
- history/export/session contracts

Should contain:
- no Jexer classes
- no direct JDBC driver wiring
- minimal third-party coupling

Typical packages:

```text
com.sdimaio.sqlshell.core.connection
com.sdimaio.sqlshell.core.session
com.sdimaio.sqlshell.core.query
com.sdimaio.sqlshell.core.metadata
com.sdimaio.sqlshell.core.history
com.sdimaio.sqlshell.core.export
```

---

## 2.3 `sqlshell-jdbc`

Purpose:
- JDBC implementation of session and query services
- connection opening/closing
- statement execution
- result set mapping
- metadata retrieval using JDBC metadata and SQL helpers

Dependencies:
- depends on `sqlshell-core`
- should not depend on Jexer

Typical packages:

```text
com.sdimaio.sqlshell.jdbc.connection
com.sdimaio.sqlshell.jdbc.execution
com.sdimaio.sqlshell.jdbc.metadata
com.sdimaio.sqlshell.jdbc.mapping
```

---

## 2.4 `sqlshell-dialects`

Purpose:
- vendor-specific SQL and metadata logic
- Oracle dialect
- PostgreSQL dialect
- generic fallback dialect

Dependencies:
- depends on `sqlshell-core`
- may optionally depend on `sqlshell-jdbc` abstractions if carefully designed
- must not depend on Jexer

Typical packages:

```text
com.sdimaio.sqlshell.dialect.api
com.sdimaio.sqlshell.dialect.oracle
com.sdimaio.sqlshell.dialect.postgres
com.sdimaio.sqlshell.dialect.generic
```

---

## 2.5 `sqlshell-tui-jexer`

Purpose:
- all Jexer UI code
- windows, dialogs, widgets, actions
- focus management and keyboard workflows
- visual adapters to core data models

Dependencies:
- depends on `sqlshell-core`
- depends on `sqlshell-jdbc`
- depends on `sqlshell-dialects`
- depends on Jexer

Typical packages:

```text
com.sdimaio.sqlshell.tui.app
com.sdimaio.sqlshell.tui.windows
com.sdimaio.sqlshell.tui.dialogs
com.sdimaio.sqlshell.tui.widgets
com.sdimaio.sqlshell.tui.actions
com.sdimaio.sqlshell.tui.models
```

---

## 2.6 `sqlshell-app`

Purpose:
- startup entry point
- dependency wiring
- configuration loading
- environment bootstrap

Dependencies:
- depends on `sqlshell-core`
- depends on `sqlshell-jdbc`
- depends on `sqlshell-dialects`
- depends on `sqlshell-tui-jexer`

Typical packages:

```text
com.sdimaio.sqlshell.app
com.sdimaio.sqlshell.config
```

---

## 3. Dependency Direction

Expected dependency graph:

```text
sqlshell-app
  -> sqlshell-tui-jexer
  -> sqlshell-jdbc
  -> sqlshell-dialects
  -> sqlshell-core
  -> sqlshell-kernel

sqlshell-tui-jexer
  -> sqlshell-jdbc
  -> sqlshell-dialects
  -> sqlshell-core
  -> sqlshell-kernel

sqlshell-jdbc
  -> sqlshell-core

sqlshell-dialects
  -> sqlshell-core

sqlshell-core
  -> (nothing project-local)

sqlshell-kernel
  -> (nothing project-local)
```

### Architectural rule

`sqlshell-core` must remain the least coupled module in the system.

---

## 4. Core Types That Should Live in `sqlshell-core`

Suggested initial types:

### Connection/session
- `ConnectionProfile`
- `DatabaseType`
- `DatabaseSession`
- `SessionInfo`

### Query execution
- `QueryRequest`
- `QueryMode`
- `QueryResult`
- `QueryError`
- `ColumnMetadata`

### Metadata
- `DbObjectRef`
- `DbObjectType`
- `TableMetadata`
- `IndexMetadata`
- `ForeignKeyMetadata`

### Workspace/history/export
- `ExecutionHistoryItem`
- `ExportFormat`
- `SavedQuery`

### Service contracts
- `ConnectionService`
- `QueryExecutionService`
- `MetadataService`
- `HistoryService`
- `ExportService`
- `DatabaseDialect`

---

## 5. Types That Should Live in `sqlshell-jdbc`

Suggested initial implementation classes:

- `JdbcConnectionService`
- `JdbcDatabaseSession`
- `JdbcQueryExecutionService`
- `JdbcMetadataService`
- `JdbcResultMapper`
- `JdbcResultPage`
- `JdbcExceptionTranslator`

### Responsibility boundary

This module should know JDBC deeply.
The rest of the project should know JDBC only through abstractions.

---

## 6. Types That Should Live in `sqlshell-dialects`

Suggested initial classes:

- `GenericDialect`
- `OracleDialect`
- `PostgresDialect`
- `DialectRegistry`

Possible supporting types:
- `ExplainRequest`
- `ExplainResult`
- `DdlRenderRequest`

### Responsibility boundary

Dialect modules should express differences such as:
- explain syntax
- schema discovery strategy
- object discovery behavior
- DDL extraction logic
- session info queries
- identifier normalization rules

---

## 7. Types That Should Live in `sqlshell-tui-jexer`

Suggested windows:

- `MainWorkbenchWindow`
- `ConnectionManagerWindow`
- `ConnectionEditDialog`
- `SqlEditorWindow`
- `ResultsWindow`
- `SchemaBrowserWindow`
- `ObjectInspectorWindow`
- `HistoryWindow`
- `ExportDialog`
- `AboutWindow`

Suggested reusable widgets/panels:

- `ConnectionTreeWidget`
- `SchemaTreeWidget`
- `SqlEditorPanel`
- `ResultGridPanel`
- `MessagePanel`
- `ExplainPanel`
- `StatusSummaryWidget`

### UI rule

Widgets should not perform JDBC logic directly. They should invoke services.

---

## 8. Types That Should Live in `sqlshell-app`

Suggested classes:

- `Main`
- `SqlShellStudioApplication`
- `AppConfiguration`
- `ServiceFactory`
- `EnvironmentBootstrap`

### Responsibility boundary

This module should mostly wire everything together and remain thin.

---

## 9. Maven Root Module Plan

The root `pom.xml` should:

- define Java 25 baseline
- define project version
- list modules
- centralize plugin versions
- provide shared profiles where needed

Suggested module order:

```xml
<modules>
    <module>sqlshell-kernel</module>
    <module>sqlshell-core</module>
    <module>sqlshell-jdbc</module>
    <module>sqlshell-dialects</module>
    <module>sqlshell-tui-jexer</module>
    <module>sqlshell-app</module>
</modules>
```

---

## 10. Early Packaging Decision

For the first implementation cycle, the recommended packaging is:

- all modules produce standard jars
- `sqlshell-app` produces the runnable artifact

This keeps implementation simple while preserving architecture.

---

## 11. Future Optional Modules

These should be deferred until truly needed:

- `sqlshell-benchmarks`
- `sqlshell-telemetry`
- `sqlshell-webapi`
- `sqlshell-native`
- `sqlshell-plugins`

### Rule

Do not create modules only because they “might be useful later”.
Create them when architectural pressure is real.

---

## 12. Initial Delivery Sequence

Recommended implementation sequence:

1. create root build
2. create `sqlshell-core`
3. create `sqlshell-jdbc`
4. create `sqlshell-dialects`
5. create `sqlshell-tui-jexer`
6. create `sqlshell-app`
7. wire first working flow: connect -> run query -> display result

This sequence ensures architecture remains intentional from the start.

---

## 13. Final Rule

If a class depends on Jexer, it belongs in the UI side.
If a class depends on JDBC specifics, it belongs in the JDBC side.
If a class expresses vendor database differences, it belongs in the dialect side.
If a class is pure domain or API contract, it belongs in core.

That separation is the foundation of maintainability for SQLShell Studio.
