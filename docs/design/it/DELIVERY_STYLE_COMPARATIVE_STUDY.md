# Studio Comparativo dello Stile di Delivery Sanfilippo

**Repository studiati**

1. `eda.digitalrepository.m1`  
   Path: `/home/sdimaio/projects/unina/ecosistema/source/EcosistemaDiAteneo/eda.digitalrepository.m1/`
2. `ttg2c.uip.commandhub`  
   Path: `/home/sdimaio/projects/costa_crociera/2026/move2cloud/sources/project-am-ttg2c-m2c.commandhub/`

**Scopo del documento**

Questo studio distilla ciò che è riusabile, presentabile e tecnicamente significativo nell’approccio di delivery mostrato dai due repository. L’obiettivo non è lodare i progetti in astratto, ma identificare abitudini ingegneristiche, scelte strutturali e meccaniche di delivery che hanno prodotto risultati consistenti.

---

## 1. Sintesi Esecutiva

I due repository mostrano uno stile di delivery riconoscibile e riusabile, caratterizzato da:

- **Java 25 come baseline di piattaforma esplicita**, non come semplice impostazione del compilatore.
- **Struttura Maven multi-modulo** per separare runtime, dominio, bridge nativi e tooling operativo.
- **Architettura come artefatto di primo livello**, documentata con prosa tecnica, diagrammi, runbook e note operative.
- **Commenti e Javadoc come documentazione ingegneristica**, soprattutto in DRM1 dove il “Sanfilippo mode” è esplicitamente imposto.
- **Tooling shell consegnato insieme all’applicazione**, non lasciato alla memoria del team.
- **Qualità, sicurezza e supply chain rappresentate nel build**, in particolare in DRM1 con profili quality, security e sbom.
- **Modernizzazione svolta in modo incrementale e con evidenze**, soprattutto in CommandHUB tramite report di migrazione e branch di lavoro controllati.
- **Mentalità ibrida**: Java, codice nativo, shell, deployment asset, runbook e analisi sono tutti parte dello stesso sistema da consegnare.

Questo non è solo uno stile di codifica. È uno **stile di delivery**.

---

## 2. Evidenze Quantitative

| Dimensione | DRM1 | CommandHUB | Interpretazione |
|---|---:|---:|---|
| Moduli Maven | 18 | 6 | Entrambi sono modulari; DRM1 è più ampio, CommandHUB più focalizzato. |
| File Java main | 374 | 163 | Entrambi sono codebase non banali. |
| File Java test | 111 | 20 | DRM1 mostra un investimento più forte sui test; CommandHUB ha un’impronta più orientata alla migrazione. |
| File di documentazione in `docs/` | 282 | 23 | DRM1 trasforma la documentazione in asset tecnico; CommandHUB documenta architettura e operations in modo mirato. |
| Script shell (`.sh` / `.ksh`) | 55 | 40 | Entrambi trattano l’automazione shell come parte della delivery. |
| File C / header nativi | 78 | 16 | Entrambi accettano estensioni native quando giustificate dal contesto. |

---

## 3. Cosa Hanno in Comune i Due Progetti

### 3.1 Disciplina di piattaforma

Entrambi trattano la versione Java come **contratto**:

- DRM1 richiede Java 25 tramite Maven Enforcer e proprietà di build.
- CommandHUB richiede Java 25 tramite Maven Enforcer e script di startup.

### Lezione pratica

Non dire “dovrebbe andare su Java 25”.  
Dì: **questo sistema builda, testa, pacchettizza e gira su Java 25, e il build fallisce altrimenti**.

---

### 3.2 Pensiero multi-modulo

Nessuno dei due repository è un monolite travestito da build Maven.

Il pattern comune è:

- POM aggregatore root
- modulo runtime/applicativo
- modulo kernel/common
- librerie ausiliarie per capability
- moduli native/JNI quando necessari
- script e documentazione operativa intorno ad essi

### Lezione pratica

Un repository enterprise serio dovrebbe rendere visibili i confini architetturali nel filesystem e nel grafo di build.

---

### 3.3 La delivery include le operations

In entrambi i progetti la delivery non finisce al JAR.

Le evidenze includono:

- script di start
- script di stop/status
- file environment
- wrapper di build
- descrittori di servizio
- runbook
- note HA
- smoke script
- helper di monitoring

### Lezione pratica

Se l’operatore non può avviare, validare, monitorare e fare troubleshooting del sistema con strumenti nativi del repository, la delivery è incompleta.

---

### 3.4 Pragmatismo nativo

Entrambi i repository includono layer native/JNI.

Questo è importante perché mostra un approccio non ideologico:

- usare puro Java dove possibile
- usare codice nativo dove necessario
- mantenere il confine nativo esplicito e governato dal build

### Lezione pratica

Lo stile di delivery non è guidato dall’ideologia, ma dall’esito tecnico.

---

### 3.5 La documentazione è parte del prodotto

Entrambi i progetti trattano architettura e operations come deliverable.

DRM1 è particolarmente forte con:
- volumi architetturali
- report di delivery
- note implementative
- ADR
- piani
- documentazione di test
- materiale di observability
- prompt di validazione/certificazione e handoff

CommandHUB mostra una disciplina più mirata ma comunque preziosa con:
- documenti architetturali
- note HA
- runbook
- checklist di release
- analisi e report di migrazione

### Lezione pratica

La documentazione non è “dopo il codice”. È una stream parallela della delivery.

---

## 4. Punti di Forza Distintivi di DRM1

### 4.1 Evidenza più forte di repository piattaforma

DRM1 non è soltanto un’applicazione. Si comporta come un repository di piattaforma:

- decomposizione modulare ampia
- integrazioni storage multiple
- stack di osservabilità
- stack di resilienza
- sottomoduli AI
- web GUI
- layer nativo
- modulo benchmark
- corpus documentale molto ricco

### 4.2 Profili espliciti quality/security/supply-chain

Il POM root include profili e script distinti per:

- coverage
- quality
- security
- generazione SBOM

Questo è un segnale molto forte perché le preoccupazioni di pipeline sono nominate ed eseguibili, non solo auspicate.

### 4.3 Sanfilippo mode come standard esplicito

DRM1 contiene la dichiarazione più chiara della filosofia di delivery in `AGENTS.md`:

- spiegare il **perché**, non solo il **cosa**
- documentare invarianti, complessità, tradeoff, failure mode, contratti, razionale
- scrivere per futuri ingegneri senior
- Javadoc in inglese per il codice
- documentazione usata come memoria operativa e di handoff

### 4.4 Mentalità di modernizzazione controllata

Le note di migrazione a Java 25 di DRM1 rifiutano l’uso casuale delle preview e preferiscono:

- feature stabili
- miglioramenti misurabili a runtime
- rollout controllato di opzioni come AOT cache, compact headers o profili GC

### Lezione pratica

La modernizzazione è trattata come programma ingegneristico, non come turismo del linguaggio.

---

## 5. Punti di Forza Distintivi di CommandHUB

### 5.1 Disciplina upgrade-in-place

L’identità più forte di CommandHUB è la **migrazione sotto vincoli operativi**.

Il repository mostra come modernizzare un sistema legacy o vincolato senza fingere di essere in un greenfield.

### 5.2 Evidenza esplicita di migrazione

La cartella `ai/` contiene report ingegneristici molto pratici:

- migration delivery report
- modernization analysis
- scope checks
- executive one-pagers
- patch alignment notes

Questo è prezioso perché mostra uno stile di delivery in cui i cambiamenti sono spiegati in termini di:

- perimetro
- rischio
- ciò che è stato cambiato
- ciò che resta da fare
- ciò che è stato verificato

### 5.3 Professionalità negli script di build

`build.sh` non è solo uno script comodo. È un wrapper professionale con:

- parsing degli argomenti
- rilevamento Java
- logging
- directory log di build
- output colorato
- exit code espliciti
- diagnostica leggibile operativamente

### 5.4 Focalizzazione architetturale operativa

La documentazione di CommandHUB è particolarmente forte su:

- scenari HA
- tradeoff Solaris/Linux
- strategia health
- modelli di failover
- runbook e checklist di release

Questo dà al repository un carattere molto infrastrutturale.

---

## 6. Lo Stile di Delivery che Emerge

Combinando i due repository, lo stile di delivery può essere sintetizzato così.

### 6.1 La delivery è di sistema, non di soli sorgenti

Il deliverable non è “il codice Java”. È:

- codice sorgente
- logica di build
- logica di packaging
- script di startup/runtime
- documentazione operativa
- documentazione architetturale
- note di migrazione
- procedure di verifica

### 6.2 La comunicazione ingegneristica è parte della correttezza

Lo stile assume che un sistema non sia veramente consegnato finché un altro ingegnere non è in grado di:

- capirne il razionale
- buildarlo in modo consistente
- farlo partire correttamente
- osservarlo in produzione
- fare troubleshooting sotto stress
- evolverlo senza perdere invarianti

### 6.3 La modularità serve a mantenere il controllo

I repository evitano strutture blob. La modularità è usata per mantenere il controllo su:

- confini di dipendenza
- perimetro di migrazione
- perimetro di test
- ownership operativa
- feature opzionali

### 6.4 La delivery privilegia esplicitazione rispetto alla magia

Pattern osservati:

- enforcement della versione Java
- profili Maven nominati
- script di startup espliciti
- caricamento environment esplicito
- documentazione architetturale esplicita
- report di migrazione espliciti

Questo riduce le assunzioni nascoste.

---

## 7. Lezioni da Esportare in Nuovi Progetti

### 7.1 Iniziare con un vero root build

Il POM root dovrebbe definire:
- contratto di versione Java
- versioni plugin
- profili quality/security comuni
- grafo moduli

### 7.2 Separare codice piattaforma e codice applicativo

Tenere distinti:
- codice kernel/runtime
- librerie di dominio/capability
- servizio applicativo
- bridge native/JNI
- script operativi

### 7.3 Trattare gli script come codice

Gli script shell devono essere:
- versionati
- nominati per scopo
- robusti al fallimento
- environment-aware
- leggibili per chi opera

### 7.4 Scrivere l’architettura in frasi complete

Un buon documento architetturale spiega:
- cos’è il sistema
- perché è fatto così
- quali sono le invarianti
- quali failure mode esistono
- come si esegue in pratica

### 7.5 Rendere auditabile la modernizzazione

Quando si migra un sistema:
- creare documenti di analisi espliciti
- separare il “fatto” dal “production-ready”
- registrare comandi ed evidenze
- rendere visibili branch e perimetro

---

## 8. Debolezze e Cautele

### 8.1 La documentazione può superare la discoverability

La forza documentale di DRM1 può diventare anche un rischio se indicizzazione e navigazione non sono curate.

### Lezione

Corpus documentali grandi richiedono indice e tassonomia.

### 8.2 Divergenza di tooling tra repository

I due repository sono allineati nello spirito ma non ancora completamente standardizzati in ogni dettaglio.

Esempi:
- DRM1 codifica fortemente il Sanfilippo mode.
- CommandHUB mescola note di modernizzazione molto forti con alcune convenzioni legacy.
- La policy su Lombok non è identica nei due contesti.

### Lezione

I repository futuri dovrebbero dichiarare una style guide canonica fin dall’inizio.

### 8.3 Il rigore di build va bilanciato con la manutenibilità

Un build root molto potente è un bene, ma deve restare comprensibile.

### Lezione

Ogni profilo e ogni wrapper shell dovrebbero avere un motivo d’esistere esplicitamente dichiarato.

---

## 9. Valutazione Finale

Questi due repository dimostrano uno stile di delivery con i seguenti punti di forza pubblicamente presentabili:

- ingegneria Java di piattaforma seria
- struttura Maven modulare
- forte attenzione operativa
- disponibilità a combinare Java, shell e codice nativo in modo responsabile
- enfasi rara sulla documentazione ricca di razionale
- modernizzazione e migrazione trattate come lavoro ingegneristico tracciabile

Se presentato bene, questo non è soltanto “il nostro modo di programmare”.  
È un modello replicabile per:

- modernizzazione enterprise
- delivery Java platform-conscious
- software engineering credibile dal punto di vista operativo
- implementazione guidata dall’architettura

Questa è la parte che vale la pena presentare al mondo.
