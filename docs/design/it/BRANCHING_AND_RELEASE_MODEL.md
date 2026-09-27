# Modello di Branching e Release

Questo documento definisce il workflow di branching e release da usare nei repository che seguono questo stile di delivery.

Si basa su un principio semplice:

> il lavoro quotidiano deve avvenire fuori da `main`, e l’integrazione deve avvenire tramite pull request.

---

## 1. Branch Principali

### `main`

Scopo:
- branch di integrazione protetto
- stato production-ready o release-grade
- aggiornato solo tramite merge/pull request revisionate

Regole:
- niente sviluppo quotidiano diretto su `main`
- niente “piccole modifiche veloci” senza percorso di review
- i tag ufficiali di release devono provenire da `main`

### `development`

Scopo:
- branch principale di lavoro
- baseline di integrazione per l’ingegneria in corso
- target di default per il lavoro nuovo salvo casi particolari

Regole:
- il lavoro quotidiano avviene qui
- feature/fix branch possono nascere da `development`
- il lavoro rientra prima in `development`
- `development` è il branch usato per la collaborazione normale

---

## 2. Categorie di Branch di Supporto

### `feature/...`

Usare per:
- nuove funzionalità
- incrementi architetturali
- passi di modernizzazione
- estensioni importanti di UI o API

### `fix/...`

Usare per:
- correzione difetti
- remediation operative
- repair work a perimetro piccolo

### `enhancement/...`

Usare per:
- miglioramenti che non sono né pure bugfix né nuove feature complete
- hardening
- raffinamento tecnico

### `release/...`

Usare per:
- stabilizzazione di una release line
- branch di packaging o deployment controllato
- ultimi aggiustamenti specifici di release

### `archive/...`

Usare solo quando serve preservare in modo visibile uno stato storico sperimentale o di delivery.

---

## 3. Workflow Standard

### 3.1 Lavoro quotidiano

Flusso standard:

1. aggiornare `development`
2. creare un topic branch se il lavoro merita isolamento
3. implementare e committare per unità logiche piccole
4. push del branch remoto
5. apertura pull request
6. merge in `development`
7. quando pronto, PR da `development` a `main`

---

### 3.2 Flusso minimo atteso

Per repository piccoli o lavoro solo, il modello minimo accettabile è:

- lavorare sempre su `development`
- non lavorare mai direttamente su `main`
- fare merge `development -> main` solo via PR

Questo è il modello baseline attualmente adottato per `sqlshell-studio`.

---

## 4. Politica delle Pull Request

Ogni PR dovrebbe rispondere chiaramente a queste domande:

- cosa è cambiato
- perché è cambiato
- cosa è stato verificato
- cosa resta volutamente fuori scope
- se ci sono implicazioni operative o di migrazione

### Regole di target

- feature/fix normali: target `development`
- promozione di release: target `main`

### Regole di stile per le PR

Preferire:
- PR piccole o medie ma coese
- note di verifica esplicite
- aggiornamento di architettura/runbook se il comportamento cambia

Evitare:
- PR enormi e multi-scopo
- refactor non correlati nascosti dentro la stessa PR
- cambiamenti operativi non documentati

---

## 5. Politica dei Commit

I messaggi di commit devono descrivere unità ingegneristiche significative.

Preferire:
- `Add architecture and Jexer documentation guides`
- `Introduce JDBC dialect abstraction for Oracle and PostgreSQL`
- `Harden startup validation for missing runtime dependencies`

Evitare:
- `misc`
- `fix stuff`
- messaggi solo temporali in repository collaborativi, salvo casi molto particolari

### Caratteristiche ideali di un commit

- un’idea per commit, quando possibile
- codice + documentazione aggiornati insieme se strettamente accoppiati
- build sano lungo il ciclo di vita del branch

---

## 6. Modello di Promozione Release

Il modello atteso è:

1. il lavoro arriva in `development`
2. lì si stabilizza l’integrazione
3. si apre una PR di release da `development` a `main`
4. dopo il merge, il tag di release nasce da `main`

### Perché funziona

Mantiene:
- `main` pulito
- disciplina di review visibile
- promozione di release esplicita
- lavoro incompleto lontano dalla release line

---

## 7. Strategia Hotfix

Se serve un hotfix, scegliere uno dei seguenti percorsi a seconda dell’urgenza.

### Percorso preferito

- branch da `main`
- creazione `fix/...`
- patch
- PR verso `main`
- poi back-merge o replay in `development`

### Percorso emergenziale

Solo se la governance lo richiede:
- branch di emergenza da `main`
- patch minima
- obbligo di riallineamento successivo in `development`

### Regola

Una fix applicata solo a `main` e non riconciliata in `development` è incompleta e genera drift futuro.

---

## 8. Regola di Sincronizzazione Documentale

Quando una modifica impatta architettura, operations o meccaniche di delivery, lo stesso branch o la stessa PR devono aggiornare la documentazione rilevante.

Esempi tipici:
- documenti architetturali
- runbook
- procedure di startup
- note di migrazione
- istruzioni quality/security

Questo evita il failure mode in cui il codice evolve ma la conoscenza di delivery resta ferma.

---

## 9. Guida al Naming dei Branch

I nomi dei branch devono comunicare rapidamente il perimetro.

Buoni esempi:
- `feature/sql-editor-tabs`
- `feature/jdbc-postgres-dialect`
- `fix/result-grid-null-rendering`
- `enhancement/query-history-retention`
- `release/2026-10-m1`

Cattivi esempi:
- `test`
- `misc`
- `branch1`
- `newstuff`

---

## 10. Cosa Ottimizza Questo Modello

Questo modello di branching ottimizza per:

- integrazione controllata
- delivery tracciabile
- review più semplice
- promozione esplicita della release
- rischio più basso sul branch protetto

Non ottimizza per:
- editing diretto e caotico su `main`
- quick push non documentati
- abitudini di sviluppo senza branch in repository di lunga durata

Questo tradeoff è intenzionale.

---

## 11. Policy Corrente per SQLShell Studio

Per questo repository specifico:

- branch attivo per il lavoro quotidiano: `development`
- branch remoto: `origin/development`
- branch target di release: `main`
- tutti i merge verso `main`: solo via pull request

### Regola di lavoro

Si lavora **sempre su `development`**, salvo richiesta esplicita di un topic branch separato.

---

## 12. Regole Finali

1. `main` è protetto.
2. `development` è il branch normale di lavoro.
3. Il lavoro tematico può nascere da `development` quando utile.
4. L’integrazione avviene tramite pull request.
5. La promozione a `main` avviene solo tramite PR revisionate.
6. La documentazione viene aggiornata nello stesso flusso di delivery quando cambia il comportamento.
7. Gli hotfix devono essere riconciliati in `development`.

Questo è il modello di branching e release.
