# Guida di Architettura di SQLShell Studio

**Progetto:** `sqlshell-studio`  
**Missione:** client database JDBC shell-native con TUI professionale, ispirato a SQL Developer e Toad  
**Stack UI principale:** Jexer 2.0  
**Contesto di esecuzione principale:** terminale / SSH / server headless / tmux / screen

---

## 1. Visione di Prodotto

SQLShell Studio è un **client database TUI professionale** pensato per ambienti in cui:

- X11 non è disponibile
- le GUI desktop sono scomode o impossibili
- l’accesso shell/SSH è la modalità principale
- DBA, sviluppatori e operatori hanno comunque bisogno di un’esperienza ricca lato client

L’obiettivo **non** è clonare SQL Developer feature-by-feature.
L’obiettivo è costruire un:

> **workbench JDBC multi-database, shell-native e keyboard-first**

con una buona esperienza d’uso e una architettura chiara.

---

## 2. Posizionamento del Prodotto

### 2.1 Cosa deve essere SQLShell Studio

- un client database TUI professionale
- rapido da usare via SSH
- molto forte sulla tastiera
- utile per Oracle, PostgreSQL e altri target JDBC
- più leggero di una GUI desktop
- più ricco delle sole shell SQL tradizionali

### 2.2 Cosa non deve provare a essere all’inizio

- un clone completo di SQL Developer
- una suite di modellazione visuale
- un debugger PL/SQL
- un drag-and-drop GUI builder
- una piattaforma plugin enorme già in V1

---

## 3. Principi di Design

1. **Shell-first**  
   L’applicazione deve risultare naturale dentro un terminale.

2. **Keyboard-first**  
   Il mouse può essere supportato, ma il workflow principale deve essere ottimizzato per la tastiera.

3. **Database-core prima della UI**  
   La logica JDBC e metadata deve vivere fuori dal layer Jexer.

4. **Consapevolezza dei dialetti DB**  
   JDBC non basta. Ogni DBMS ha differenze di comportamento e di metadati.

5. **Result viewing professionale**  
   Result grid, errori, history delle query e object browser sono feature centrali.

6. **Scope incrementale**  
   Consegnare un MVP forte prima di aggiungere tooling avanzato.

---

## 4. Roadmap di Sviluppo Raccomandata

### Fase 1 — MVP

Consegnare una prima versione utile con:

- connection profiles
- test connessione JDBC
- SQL editor (single tab o multi-tab minimale)
- esecuzione SQL statement / script
- result table view
- error panel
- query history
- schema browser (schema/tabelle/viste/colonne)
- open/save di file SQL
- export CSV

### Criterio di successo dell’MVP

Un utente reale deve poter:
- connettersi a Oracle o PostgreSQL
- browsare oggetti
- eseguire query
- ispezionare risultati
- salvare e riaprire script
- esportare dati

---

### Fase 2 — Workflow professionale

Aggiungere:

- multi-tab editor
- multi-result tabs
- explain plan
- object inspector
- migliore filtro/sort dei risultati
- pagination o lazy loading per risultati grandi
- status panel con timing / row count
- migliore session management
- snippet SQL e preferiti

---

### Fase 3 — Feature distintive

Aggiungere selettivamente:

- extra Oracle
- extra PostgreSQL
- session monitor
- estrazione DDL
- bookmark / persistenza workspace
- libreria query riusabili
- schema search / object jump
- eventuale terminal integration embedded

---

## 5. Architettura ad Alto Livello

L’architettura deve essere modulare.

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

### 5.1 Responsabilità dei moduli

#### `sqlshell-core`
Modello di dominio e contratti dei casi d’uso.

#### `sqlshell-jdbc`
Esecuzione JDBC di basso livello e lettura metadata.

#### `sqlshell-dialects`
Astrazione del comportamento DB-specifico.

#### `sqlshell-tui-jexer`
Componenti UI e finestre Jexer.

#### `sqlshell-app`
Composition root / startup module.

---

## 6. Package Java Suggeriti

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

## 7. Modello di Dominio Core

Il core dovrebbe partire con un modello piccolo ma solido.

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

---

## 8. Layer dei Servizi

Il layer dei servizi deve nascondere la complessità JDBC e dialect al layer TUI.

### Servizi iniziali consigliati

- `ConnectionProfileRepository`
- `ConnectionService`
- `QueryExecutionService`
- `MetadataService`
- `ExportService`
- `HistoryService`

---

## 9. Layer Dialect

Il layer dei dialetti è essenziale.

### Perché esiste

JDBC non normalizza tutto ciò che conta per un client ricco:

- schema discovery diversa per vendor
- explain plan differente
- DDL retrieval differente
- categorie oggetti differenti
- pagination differente
- session info differente

### Interfaccia suggerita

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

### Dialetti iniziali

- `OracleDialect`
- `PostgresDialect`
- `GenericDialect`

---

## 10. Strategia di Esecuzione JDBC

### 10.1 Modalità statement distinte

Non trattare tutto l’SQL come se fosse uguale.

Servono almeno:
- single statement execution
- script execution
- explain execution
- metadata lookup execution

### 10.2 Strategia sui result set grandi

Evitare di caricare subito dataset enormi senza limiti.

Approccio raccomandato:
- fetch size configurabile
- soglia di truncation per sicurezza
- futuro supporto a lazy page loading

### 10.3 Transazioni

Serve supporto esplicito a:
- autocommit on/off
- commit
- rollback

Questo stato deve essere visibile nella status area UI.

---

## 11. Information Architecture della TUI

La schermata principale dovrebbe assomigliare a un text IDE.

```text
+--------------------------------------------------------------+
| Menu Bar                                                     |
+----------------------+---------------------------------------+
| Connections /        | SQL Editor Tabs                       |
| Schema Browser       |                                       |
|                      |                                       |
+----------------------+---------------------------------------+
| Result Tabs / Messages / Explain / History                   |
+--------------------------------------------------------------+
| Status Bar                                                   |
+--------------------------------------------------------------+
```

### 11.1 Pannello sinistro

Contiene:
- saved connections
- session info attiva
- schema/object tree

### 11.2 Pannello centrale

Contiene:
- SQL editor tabs
- marker dello statement corrente
- contesto di esecuzione

### 11.3 Pannello inferiore

Contiene tab tipo:
- Results
- Messages
- Errors
- Explain
- History

### 11.4 Status bar

Mostra:
- nome connessione attiva
- DB type
- current schema
- autocommit state
- rows returned
- execution time

---

## 12. Moduli UI Jexer

Il layer Jexer dovrebbe essere diviso in finestre e widget riusabili.

### Finestre suggerite

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

### Widget/panel riusabili

```text
ConnectionTreeWidget
SchemaTreeWidget
SqlEditorPanel
ResultGridPanel
MessageLogPanel
ExplainPlanPanel
StatusSummaryWidget
```

---

## 13. Strategia della Result Grid

Questa è una delle decisioni più importanti.

### Requisiti V1

- righe e colonne visualizzabili
- navigazione da tastiera
- copy cell
- copy row
- scroll orizzontale e verticale
- label di colonna
- gestione semplice delle larghezze

### Migliorie V2

- sorting
- filtering
- freeze columns
- formattazione migliore di numeri/date
- export di tutte o solo delle righe visibili

### Avvertenza tecnica

La result grid può diventare il widget più difficile del progetto. La V1 deve essere ambiziosa ma sobria.

---

## 14. Strategia dell’SQL Editor

### V1

- editor semplice basato sulle facility Jexer
- save / open file
- execute all text
- execute selection o current statement in seguito, se semplice

### V2

- multiple tabs
- statement boundary detection
- snippets
- recent files
- bookmarks

### Avvertenza di scope

Un editor SQL completo è un progetto a sé. La prima versione deve essere pratica e robusta.

---

## 15. Strategia di Persistenza

### 15.1 Profili e impostazioni

Persistenza locale di:
- connection profiles
- recent files
- query history
- preferenze UI
- snippets salvati

### 15.2 Formati raccomandati

- JSON per profili e impostazioni
- plain text o JSON strutturato per la history

### 15.3 Secret management

Le password non dovrebbero restare in plain text se evitabile.

Per V1 è accettabile:
- non salvare la password
- oppure usare una strategia locale semplice ma chiaramente segnalata come area sensibile

---

## 16. Requisiti Non Funzionali

### Performance
- buona fluidità su shell remota
- comportamento sicuro su result set grandi
- UI non bloccante durante l’esecuzione query, dove praticabile

### Reliability
- gestione forte delle eccezioni JDBC
- niente failure silenziose
- esposizione di SQLState e vendor code

### Usabilità
- navigazione completa da tastiera
- shortcut coerenti
- messaggi di stato chiari

### Portabilità
- Linux first
- Swing fallback opzionale
- evitare assunzioni OS-specific nel core

---

## 17. Strategia Iniziale di Tastiera

Shortcut suggerite:

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

---

## 18. Rischi e Sfide Tecniche

### 18.1 Supporto multi-DB
Rischio: la complessità cresce rapidamente.  
Mitigazione: forte layer dialect fin dal giorno uno.

### 18.2 Result set grandi
Rischio: freeze UI o blowup di memoria.  
Mitigazione: row limits, fetch size, staged loading.

### 18.3 Scope creep sull’editor
Rischio: troppo tempo speso su feature da editor.  
Mitigazione: V1 sobria e robusta.

### 18.4 Incoerenza metadata
Rischio: object browser diverso per DB.  
Mitigazione: normalizzare via metadata service e dialect helper.

---

## 19. Prime Milestone di Sviluppo

### Milestone 0 — Project skeleton
- struttura repository
- build files
- docs
- startup app shell

### Milestone 1 — Connection manager
- profile CRUD
- test connection
- session open/close

### Milestone 2 — SQL editor + execute
- editor pane
- run statement
- messages pane

### Milestone 3 — Result viewer
- table rendering
- copy/export base

### Milestone 4 — Schema browser
- schema tree
- browser tabelle/viste
- object detail pane

### Milestone 5 — History and persistence
- recent queries
- recent files
- profili persistiti

A milestone 5 il progetto diventa già realmente utile.

---

## 20. Conclusione

SQLShell Studio va costruito come **workbench database serio e shell-native**, non come clone frettoloso di un gigante desktop.

Se l’architettura resta modulare e lo scope disciplinato, il progetto può diventare uno strumento davvero forte:

- ideale per ambienti headless
- efficiente via SSH
- produttivo da tastiera
- utile per DBA e sviluppatori

Questo è il target corretto.
