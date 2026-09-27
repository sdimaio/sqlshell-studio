# Layout Modulare di SQLShell Studio

Questo documento definisce la struttura modulare iniziale per SQLShell Studio.

La regola primaria è semplice:

> il motore database, la logica dei dialetti e il modello di persistenza non devono dipendere da Jexer.

---

## 1. Insieme iniziale dei moduli

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

## 2. Responsabilità dei Moduli

### 2.1 `sqlshell-kernel`

Scopo:
- modello runtime e piattaforma di basso livello
- rilevamento OS e JVM
- diagnostica di bootstrap dell'ambiente
- futura casa per startup guards professionali

Deve contenere:
- nessuna classe UI
- nessuna logica JDBC
- dipendenze minime

Package tipici:

```text
com.sdimaio.sqlshell.kernel.model.system
com.sdimaio.sqlshell.kernel.model.system.os
com.sdimaio.sqlshell.kernel.model.system.runtime
com.sdimaio.sqlshell.kernel.model.system.architecture
```

---

## 2.2 `sqlshell-core`

Scopo:
- modello di dominio stabile
- contratti dei servizi core
- astrazioni query/result
- astrazioni metadata
- contratti history/export/session

Deve contenere:
- nessuna classe Jexer
- nessun wiring diretto di driver JDBC
- accoppiamento minimale con terze parti

Package tipici:

```text
com.sdimaio.sqlshell.core.connection
com.sdimaio.sqlshell.core.session
com.sdimaio.sqlshell.core.query
com.sdimaio.sqlshell.core.metadata
com.sdimaio.sqlshell.core.history
com.sdimaio.sqlshell.core.export
```

---

### 2.3 `sqlshell-jdbc`

Scopo:
- implementazione JDBC dei servizi di sessione e query
- apertura/chiusura connessioni
- esecuzione statement
- mapping dei result set
- lettura metadata via JDBC metadata e helper SQL

Dipendenze:
- dipende da `sqlshell-core`
- non deve dipendere da Jexer

Package tipici:

```text
com.sdimaio.sqlshell.jdbc.connection
com.sdimaio.sqlshell.jdbc.execution
com.sdimaio.sqlshell.jdbc.metadata
com.sdimaio.sqlshell.jdbc.mapping
```

---

### 2.4 `sqlshell-dialects`

Scopo:
- logica SQL e metadata specifica per vendor
- dialetto Oracle
- dialetto PostgreSQL
- dialetto generico fallback

Dipendenze:
- dipende da `sqlshell-core`
- può dipendere da astrazioni di `sqlshell-jdbc` solo se progettato con attenzione
- non deve dipendere da Jexer

Package tipici:

```text
com.sdimaio.sqlshell.dialect.api
com.sdimaio.sqlshell.dialect.oracle
com.sdimaio.sqlshell.dialect.postgres
com.sdimaio.sqlshell.dialect.generic
```

---

### 2.5 `sqlshell-tui-jexer`

Scopo:
- tutto il codice UI Jexer
- finestre, dialog, widget, action
- focus management e workflow da tastiera
- adapter visuali verso i modelli core

Dipendenze:
- dipende da `sqlshell-core`
- dipende da `sqlshell-jdbc`
- dipende da `sqlshell-dialects`
- dipende da Jexer

Package tipici:

```text
com.sdimaio.sqlshell.tui.app
com.sdimaio.sqlshell.tui.windows
com.sdimaio.sqlshell.tui.dialogs
com.sdimaio.sqlshell.tui.widgets
com.sdimaio.sqlshell.tui.actions
com.sdimaio.sqlshell.tui.models
```

---

### 2.6 `sqlshell-app`

Scopo:
- entry point di startup
- dependency wiring
- caricamento configurazione
- bootstrap dell’ambiente

Dipendenze:
- dipende da `sqlshell-core`
- dipende da `sqlshell-jdbc`
- dipende da `sqlshell-dialects`
- dipende da `sqlshell-tui-jexer`

Package tipici:

```text
com.sdimaio.sqlshell.app
com.sdimaio.sqlshell.config
```

---

## 3. Direzione delle Dipendenze

Grafo atteso:

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
  -> (nessun modulo locale)

sqlshell-kernel
  -> (nessun modulo locale)
```

### Regola architetturale

`sqlshell-core` deve restare il modulo meno accoppiato del sistema.

---

## 4. Tipi che Devono Vivere in `sqlshell-core`

Tipi iniziali suggeriti:

### Connessioni/sessioni
- `ConnectionProfile`
- `DatabaseType`
- `DatabaseSession`
- `SessionInfo`

### Esecuzione query
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

### Contratti di servizio
- `ConnectionService`
- `QueryExecutionService`
- `MetadataService`
- `HistoryService`
- `ExportService`
- `DatabaseDialect`

---

## 5. Tipi che Devono Vivere in `sqlshell-jdbc`

Classi iniziali suggerite:

- `JdbcConnectionService`
- `JdbcDatabaseSession`
- `JdbcQueryExecutionService`
- `JdbcMetadataService`
- `JdbcResultMapper`
- `JdbcResultPage`
- `JdbcExceptionTranslator`

### Confine di responsabilità

Questo modulo deve conoscere JDBC in profondità.  
Il resto del progetto deve conoscere JDBC solo tramite astrazioni.

---

## 6. Tipi che Devono Vivere in `sqlshell-dialects`

Classi iniziali suggerite:

- `GenericDialect`
- `OracleDialect`
- `PostgresDialect`
- `DialectRegistry`

Tipi di supporto possibili:
- `ExplainRequest`
- `ExplainResult`
- `DdlRenderRequest`

### Confine di responsabilità

I dialetti devono esprimere differenze come:
- sintassi explain
- strategia di schema discovery
- comportamento dell’object discovery
- logica di estrazione DDL
- query di session info
- normalizzazione identificatori

---

## 7. Tipi che Devono Vivere in `sqlshell-tui-jexer`

Finestre suggerite:

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

Widget/panel riusabili suggeriti:

- `ConnectionTreeWidget`
- `SchemaTreeWidget`
- `SqlEditorPanel`
- `ResultGridPanel`
- `MessagePanel`
- `ExplainPanel`
- `StatusSummaryWidget`

### Regola UI

I widget non devono eseguire logica JDBC direttamente. Devono invocare servizi.

---

## 8. Tipi che Devono Vivere in `sqlshell-app`

Classi suggerite:

- `Main`
- `SqlShellStudioApplication`
- `AppConfiguration`
- `ServiceFactory`
- `EnvironmentBootstrap`

### Confine di responsabilità

Questo modulo dovrebbe fare soprattutto wiring e restare sottile.

---

## 9. Piano del Modulo Root Maven

Il `pom.xml` root dovrebbe:

- definire baseline Java 25
- definire la versione progetto
- elencare i moduli
- centralizzare versioni plugin
- offrire profili condivisi quando necessari

Ordine moduli suggerito:

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

## 10. Decisione di Packaging Iniziale

Per il primo ciclo di implementazione, il packaging raccomandato è:

- tutti i moduli producono jar standard
- `sqlshell-app` produce l’artefatto eseguibile

---

## 11. Moduli Opzionali Futuri

Da rinviare finché non realmente necessari:

- `sqlshell-benchmarks`
- `sqlshell-telemetry`
- `sqlshell-webapi`
- `sqlshell-native`
- `sqlshell-plugins`

### Regola

Non creare moduli solo perché “potrebbero servire”.  
Crearli solo quando la pressione architetturale è reale.

---

## 12. Sequenza Iniziale di Delivery

Sequenza raccomandata:

1. creare il build root
2. creare `sqlshell-core`
3. creare `sqlshell-jdbc`
4. creare `sqlshell-dialects`
5. creare `sqlshell-tui-jexer`
6. creare `sqlshell-app`
7. cablare il primo flow funzionante: connect -> run query -> display result

---

## 13. Regola Finale

Se una classe dipende da Jexer, appartiene al lato UI.  
Se una classe dipende da specificità JDBC, appartiene al lato JDBC.  
Se una classe esprime differenze tra database vendor, appartiene al lato dialect.  
Se una classe è puro dominio o contratto API, appartiene al core.

Questa separazione è il fondamento della manutenibilità di SQLShell Studio.
