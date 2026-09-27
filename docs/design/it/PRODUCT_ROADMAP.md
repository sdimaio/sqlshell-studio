# Roadmap di Prodotto di SQLShell Studio

**Progetto:** SQLShell Studio  
**Visione:** workbench database JDBC shell-native con TUI professionale per ambienti headless e SSH-first.

---

## 1. Direzione del Prodotto

SQLShell Studio vuole occupare lo spazio tra:

- shell SQL low-level (`sqlplus`, `psql`, `sqlcmd`, ecc.)
- strumenti GUI desktop pesanti (SQL Developer, Toad, DBeaver, DataGrip)

Il valore distintivo è:

- uso nativo in terminale
- workflow keyboard-first
- supporto multi-database via JDBC
- schema/query/result workflow professionale
- ottima aderenza ad ambienti remoti/server senza X11

---

## 2. Principi di Prodotto

1. **Shell-first**  
   Il prodotto deve restare pienamente utile via SSH.

2. **Keyboard-first**  
   Ogni workflow importante deve essere efficiente senza mouse.

3. **Database-core prima della UI**  
   Il motore JDBC e i dialetti devono restare indipendenti dalla UI Jexer.

4. **Multi-database ma con disciplina**  
   Oracle e PostgreSQL vengono prima. Gli altri dialetti sono estensioni successive.

5. **MVP prima dell’ambizione**  
   Consegnare un workbench forte e usabile prima di allargare troppo lo scope.

---

## 3. Strategia Generale di Release

### Phase 0 — Foundation

Obiettivo: fissare repository, architettura, policy linguistiche e confini modulari.

Deliverable:
- struttura repository
- baseline Java/Maven
- branching model
- documenti architetturali
- documenti sullo stile di delivery
- prime guide Jexer

---

### Phase 1 — MVP Workbench

Obiettivo: creare un client database terminale realmente usabile.

#### Scope

- gestione connection profile
- apertura/chiusura sessioni JDBC
- SQL editor (single editor o tab minimi)
- esecuzione statement/script
- result grid
- error panel / messages
- query history
- schema browser (schema, tabelle, viste, colonne)
- open/save file SQL
- export CSV

#### Database supportati inizialmente

- Oracle
- PostgreSQL

#### Criterio di uscita

Un utente deve poter:
- salvare un profilo di connessione
- connettersi al database
- browsare gli oggetti di schema
- scrivere ed eseguire SQL
- ispezionare risultati ed errori
- salvare query su disco
- esportare un result set in CSV

---

### Phase 2 — Workflow professionale

Obiettivo: rendere il prodotto produttivo per uso tecnico regolare.

#### Scope

- multi-tab editor
- multi-result tabs
- supporto explain plan
- object inspector
- migliore navigazione dei risultati
- filtering e sorting
- copy/export più ricchi
- snippets / saved queries
- recent files e recent connections
- status bar più ricca con metadata di esecuzione

---

### Phase 3 — Feature DBA/Operator avanzate

Obiettivo: differenziare il prodotto oltre il semplice “query runner con browser”.

#### Feature candidate

- session monitor
- estrazione DDL
- ricerca object schema-wide
- object jump / quick-open palette
- bookmark
- controlli transazionali e session state indicator
- watchdog / cancellation per query lunghe
- export più ricchi (TSV, JSON)

---

### Phase 4 — Estensioni di piattaforma

Obiettivo: ampliare la copertura senza compromettere l’architettura.

#### Feature candidate

- dialetto MySQL/MariaDB
- dialetto SQL Server
- supporto SQLite
- hook plugin/extension se davvero giustificati
- terminal integration embedded
- integrazione con tool esterni

---

## 4. Roadmap Funzionale per Area

### 4.1 Connection Management

#### MVP
- create/edit/delete connection profiles
- test connection
- open/close session

#### Dopo
- tag ambiente (dev/test/prod)
- miglioramento secret handling
- default schema e fetch settings per profilo

---

### 4.2 SQL Editing

#### MVP
- una superficie editor
- load/save file
- execute del testo visibile

#### Dopo
- tabs
- snippets
- statement boundary detection
- execute current statement
- bookmark

---

### 4.3 Result Viewing

#### MVP
- visualizzazione tabellare
- scroll verticale e orizzontale
- ispezione celle e righe
- export CSV

#### Dopo
- sorting
- filtering
- copy cell/row/result set
- lazy paging
- formatting per tipo dato

---

### 4.4 Schema Navigation

#### MVP
- schema
- tabelle
- viste
- colonne

#### Dopo
- indici
- constraint
- procedure/function/package
- object search
- DDL preview

---

### 4.5 Execution Diagnostics

#### MVP
- error message panel
- SQLState e vendor code
- timing di esecuzione basilare

#### Dopo
- execution log strutturato
- explain plan tab
- warnings panel
- statistics history

---

### 4.6 Persistenza e Workspace

#### MVP
- connection profiles
- query history
- recent files

#### Dopo
- workspace/session restore
- layout salvati
- bookmark e preferiti

---

## 5. Roadmap Non Funzionale

### 5.1 Performance

#### Requisiti iniziali
- niente freeze evidenti della UI in condizioni normali
- gestione sicura dei result set grandi
- fetch size configurabile

#### Migliorie successive
- lazy result loading
- metadata retrieval ottimizzato
- task in background con progress reporting

---

### 5.2 Reliability

#### Requisiti iniziali
- failure reporting chiaro
- cleanup sicuro delle sessioni
- lifecycle delle connessioni deterministico

#### Migliorie successive
- query cancellation model
- fault isolation per dialect/service
- persistenza workspace resistente ai crash

---

### 5.3 Operability

#### Requisiti iniziali
- esecuzione pulita via SSH
- startup prevedibile
- log e status feedback

#### Migliorie successive
- diagnostica più ricca
- export più semplice dei dati di supporto
- generazione di troubleshooting report

---

## 6. Milestone di Release

### Milestone A — Repository Ready
- docs e architettura baseline committate
- branching model in place
- implementazione può partire con sicurezza

### Milestone B — First Running UI Shell
- l’app si avvia
- menu e launcher esistono
- finestre placeholder in place

### Milestone C — First Real DB Session
- connect/disconnect JDBC funziona
- prima query eseguibile

### Milestone D — MVP Usable
- browsing/editing/execution/export path completo

### Milestone E — Professional Daily Driver
- multi-tab, explain, object inspection, history più ricca

---

## 7. Scope Guardrails

Queste feature **non** devono essere prioritarie all’inizio:

- visual query builder
- modellazione ER
- debugger PL/SQL
- tooling drag-and-drop
- larghezza funzionale completa di SQL Developer
- ecosistema plugin troppo presto

Motivo:
sono costose e non definiscono la value proposition iniziale.

---

## 8. Definizione di Successo

SQLShell Studio ha successo se diventa:

- il client database preferito da terminale per lavoro remoto/headless
- un workbench JDBC produttivo per Oracle e PostgreSQL
- uno strumento serio e affidabile, non improvvisato
- un repository che dimostra forte disciplina di delivery Java 25 multi-modulo

Questo è il target della roadmap.
