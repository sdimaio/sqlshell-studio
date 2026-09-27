# Standard dello Stile di Delivery

**Lingua ufficiale per codice, design, test e implementazione: inglese**  
**Politica documentale: bilingue quando utile a delivery, handoff o stakeholder communication**

Questo documento definisce lo standard ingegneristico distillato dalle pratiche osservate nei repository DRM1 e CommandHUB.

---

## 1. Ambito dello Standard

Questo standard si applica a:

- codice sorgente
- Javadoc e commenti inline
- codice di test
- logica di build
- naming dei moduli
- workflow di branch e PR
- script shell e tooling operativo
- documenti di architettura e design
- report di migrazione e delivery

---

## 2. Politica Linguistica

### 2.1 Artefatti code-facing

I seguenti artefatti devono essere in **inglese**:

- codice Java
- nomi di classi
- nomi di metodi
- package
- Javadoc
- commenti inline
- nomi dei test
- descrizioni dei test
- documenti architetturali/design destinati agli ingegneri
- commenti negli script di build quando il repository è English-first

### 2.2 Artefatti documentation-facing

Possono essere **bilingui** quando il contesto di delivery lo richiede:

- executive summary
- report architetturali per audience miste
- runbook
- guide di deployment
- documenti di validazione/certificazione
- note progettuali customer-facing

### Regola

L’inglese resta la lingua canonica dell’ingegneria.  
La documentazione localizzata è un layer di servizio, non la fonte primaria di verità.

---

## 3. Standard di Documentazione del Codice: Sanfilippo Mode

La documentazione del codice non deve essere decorativa. Deve essere memoria ingegneristica.

### Principi obbligatori

Javadoc e commenti significativi devono:

1. spiegare il **perché**, non solo il **cosa**
2. documentare invarianti
3. esporre implicazioni di complessità o performance quando rilevanti
4. registrare tradeoff
5. descrivere failure mode e vincoli di ciclo di vita
6. rendere espliciti concorrenza e thread-safety
7. aiutare futuri ingegneri senior a capire decisioni non ovvie

### Cosa deve coprire il Javadoc dei tipi pubblici

- responsabilità e scopo
- invarianti
- thread-safety
- comportamento in caso di errore
- vincoli di lifecycle
- note di complessità/performance quando significative
- esempi d’uso per API riusabili
- impatto operativo se rilevante

### Il Javadoc di un metodo è richiesto quando

- l’algoritmo non è ovvio
- gli effetti collaterali sono importanti
- c’è concorrenza
- la performance conta
- i contratti sono facili da violare
- sono coinvolte risorse esterne

### Linee di stile

- essere sobri
- essere precisi
- evitare parafrasi riga per riga dell’implementazione
- ottimizzare per manutenibilità e trasferimento del razionale

---

## 4. Standard di Piattaforma Java

### 4.1 Contratto runtime

La versione Java deve essere trattata come contratto, non come suggerimento.

Pattern raccomandato:

- dichiarare la versione Java una volta sola al root
- usare Maven Enforcer per rifiutare runtime non supportati
- allineare source/target/release in modo consistente
- rendere runtime script e build script consapevoli della versione

### 4.2 Politica di modernizzazione

Preferire:

- feature Java stabili prima di tutto
- guadagni misurabili invece di sintassi “alla moda”
- documenti di migrazione che separino chiaramente il completato dal restante

Evitare:

- entusiasmo per preview feature senza bisogno operativo
- drift silenzioso del runtime
- baseline Java multiple e nascoste nello stesso reactor

---

## 5. Standard di Struttura del Repository

Un repository serio deve mostrare l’architettura attraverso la sua struttura.

### Pattern top-level raccomandato

```text
repo/
  docs/
  scripts/
  kernel/
  app/ o springboot-ms/
  lib-*/
  native/
  lib-jni/
  benchmarks/
  webgui/
  pom.xml
  mvnw
```

### Principi strutturali

- il root orchestrates the system
- le fondamenta cross-cutting vivono in `kernel`
- le librerie separate rappresentano veri concern architetturali
- i confini native e JNI restano espliciti
- gli script operativi vivono nel repository, non solo nella memoria delle persone

---

## 6. Standard Maven

### 6.1 Responsabilità del POM root

Il `pom.xml` root dovrebbe definire:

- coordinate progetto
- baseline Java 25
- versione minima Maven
- versioni plugin in un solo posto
- dependency management quando utile
- lista moduli
- policy di build condivisa

### 6.2 Tratti di build preferiti

- Maven Wrapper presente
- `maven-enforcer-plugin` attivo
- `maven-compiler-plugin` con `release=25`
- comportamento dei test esplicito
- profili nominati per concern cross-cutting

### Profili raccomandati

- `quality`
- `security`
- `sbom`
- `coverage`
- `distribution`

I profili devono esistere solo se corrispondono a reali attività di delivery.

---

## 7. Standard dei Test

### 7.1 I test sono asset di delivery

I test non devono essere trattati come rifinitura opzionale.

Dovrebbero coprire:

- comportamento business
- binding di configurazione
- confini di resilienza
- failure path
- invarianti operative quando possibile

### 7.2 Convenzione di naming

I nomi dei test devono leggersi come enunciati ingegneristici eseguibili.

Esempi:
- `retriesTransientTransportFailuresThenSucceeds`
- `neverPropagatesFailuresIntoArchiveLifecycle`
- `platformRequirementsSatisfied`

### 7.3 Tassonomia dei test

Quando necessario, distinguere:

- unit test
- integration test
- non-functional test
- load/performance test
- smoke test

---

## 8. Standard del Tooling Operativo

Un sistema consegnato non è completo senza tooling operativo eseguibile.

### Artefatti richiesti, quando applicabili

- start script
- stop script
- status script
- smoke script
- environment template
- service unit o equivalente
- helper di troubleshooting

### Standard shell

Gli script dovrebbero:

- usare `set -euo pipefail` quando sensato
- validare i prerequisiti presto
- stampare diagnostica utile
- evitare fallback magici
- essere leggibili dal personale di esercizio

### Regola

Se un’operazione conta in produzione, deve esistere come procedura nativa del repository.

---

## 9. Standard della Documentazione Architetturale

Un documento di architettura deve spiegare:

- cosa fa il sistema
- perché l’architettura è fatta così
- confini modulari
- assunzioni di deployment
- modello di stato e persistenza
- modello di sicurezza
- modello di resilienza
- modello di observability
- failure mode operativi

### Stile preferito

- frasi complete
- assunzioni esplicite
- diagrammi solo se supportati da spiegazione scritta
- tabelle per invarianti, dipendenze, deployment e rischio

### Cartelle documentali raccomandate

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

## 10. Standard di Migrazione e Modernizzazione

La modernizzazione deve essere documentata come programma ingegneristico.

### Artefatti richiesti per migrazioni importanti

- definizione dello scope
- dichiarazione dello stack target
- branch analizzato
- comandi di verifica eseguiti
- outcome summary
- known gaps
- next actions

### Buona pratica

- rendere le evidenze esplicite
- separare “done” da “ready for production”
- tenere visibile il perimetro del branch
- pubblicare i rischi, non solo i successi

---

## 11. Standard di Observability e Resilience

Questi concern devono essere visibili sia nel codice sia nella delivery.

### Pattern attesi

- health endpoint
- actuator o equivalente di introspezione runtime
- pubblicazione metriche
- circuit breaker / retry dove giustificati
- log strutturati
- documentazione operativa della telemetria

### Regola

L’observability non è un add-on post-deployment. È parte del contratto applicativo.

---

## 12. Confini Nativi e Ibridi

Usare codice nativo solo quando ne vale davvero il costo.

### Se si usa native/JNI

Il repository deve contenere:

- source tree esplicito
- integrazione nel build
- chiarezza su header e mapping
- spiegazione del runtime usage
- assunzioni di portabilità
- fallback o failure strategy

### Regola

Il confine nativo non deve mai essere misterioso. Deve essere ispezionabile, buildabile e spiegabile.

---

## 13. Workflow di Branch e Delivery

### Modello richiesto

- `main` protetto e aggiornato solo via merge/PR
- il lavoro quotidiano avviene su `development` o su topic branch derivati da esso
- l’integrazione passa tramite pull request
- il nome del branch deve riflettere il perimetro

### Categorie tipiche di branch

- `development`
- `feature/...`
- `fix/...`
- `enhancement/...`
- `release/...`
- `archive/...` se giustificato

### Regola

Non normalizzare lavoro diretto e informale su `main`.

---

## 14. Checklist degli Artefatti di Delivery

Un repository che segue questo stile dovrebbe normalmente contenere, quando rilevanti:

- codice sorgente
- test
- wrapper di build
- logica di build root
- script operativi
- documenti di architettura
- documenti runbook/operations
- note di modernizzazione/delivery
- descrittori di deployment
- automazione quality/security

---

## 15. Cosa Ottimizza Questo Stile

Questo stile di delivery ottimizza per:

- manutenibilità sotto turnover del team
- realismo infrastrutturale
- migrazione sotto vincoli
- tracciabilità ingegneristica
- credibilità operativa
- software enterprise di lunga durata

È meno ottimizzato per:

- minimalismo fine a sé stesso
- code golf estetico
- sperimentazione veloce e non documentata

Questo tradeoff è intenzionale.

---

## 16. Regole Finali

Se vuoi un riassunto operativo, usa questo:

1. Scrivere codice in inglese.
2. Documentare il razionale, non solo la meccanica.
3. Enforzare baseline Java e build al root.
4. Usare moduli Maven per riflettere l’architettura.
5. Consegnare gli script shell come parte del prodotto.
6. Tenere architettura, runbook e note di migrazione nel repository.
7. Trattare observability e resilience come concern di primo livello.
8. Accettare codice nativo solo con disciplina esplicita.
9. Lavorare tramite branch e pull request, non con modifiche informali su `main`.
10. Fare in modo che il repository spieghi come buildare, eseguire, verificare ed evolvere il sistema.

Questo è lo standard di delivery.
