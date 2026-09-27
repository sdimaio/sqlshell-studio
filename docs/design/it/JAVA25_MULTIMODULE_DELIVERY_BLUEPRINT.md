# Blueprint di Delivery Java 25 Multi-Modulo

Questo blueprint cattura la forma di repository e build riusabile implicita nei progetti studiati. È pensato come template pratico per futuri sistemi che vogliono la stessa postura di delivery.

---

## 1. Obiettivi

Un repository che segue questo blueprint dovrebbe rendere facile:

- imporre Java 25 come contratto di build/runtime
- separare i concern architetturali in moduli
- eseguire quality, security e packaging flow dal root
- consegnare codice applicativo insieme a script operativi e documentazione
- supportare modernizzazioni incrementali senza ricadere in un monolite

---

## 2. Forma di Riferimento del Repository

```text
project-root/
  docs/
    architecture/
    design/
    implementation/
    operations/
    runbook/
    release/
    tests/
    adr/
  scripts/
  kernel/
  app/ or springboot-ms/
  lib-foo/
  lib-bar/
  native/
  lib-jni/
  benchmarks/
  webgui/
  pom.xml
  mvnw
  mvnw.cmd
```

Non ogni progetto richiede ogni directory, ma la struttura deve comunicare l’intento architetturale.

---

## 3. Blueprint del POM Root

Il `pom.xml` root deve essere insieme aggregatore e contenitore di policy.

### Deve definire

- coordinate progetto
- baseline Java 25
- versione minima Maven
- versioni plugin in un solo punto
- dependency management quando utile
- lista moduli
- policy di build condivisa

### Elementi di policy raccomandati

- `maven-enforcer-plugin` per rifiutare runtime errati
- `maven-compiler-plugin` con `release=25`
- `maven-surefire-plugin` con comportamento esplicito
- profili nominati per `quality`, `security`, `sbom`, `coverage`, `distribution`

### Perché conta

Il build root non serve solo alla comodità. È il luogo in cui il repository dichiara cosa è disposto a buildare e supportare.

---

## 4. Tassonomia dei Moduli

### 4.1 Kernel

Scopo:
- astrazioni runtime di basso livello
- primitive utility condivise
- helper di logging/runtime/platform

`kernel` deve restare focalizzato, non diventare un contenitore indiscriminato.

### 4.2 Modulo servizio applicativo

Nomi tipici:
- `springboot-ms`
- `app`
- `service`

Scopo:
- wiring applicativo
- entry point Spring Boot
- controller/endpoint
- binding configurazione
- orchestrazione

### 4.3 Librerie di dominio o capability

Esempi:
- `libfs4j`
- `libclust4j`
- `libdoc4j`
- `libs3store4j`
- `libtelemetry`
- `libsecurity-*`

Scopo:
- isolare capability opzionali o specializzate
- mantenere sottile il layer applicativo
- facilitare test e migrazione mirati

### 4.4 Moduli native / JNI

Split tipico:
- `native` per sorgenti/output C/C++
- `lib-jni` per wrapper Java e binding nativi

### 4.5 Modulo benchmark

Scopo:
- JMH o performance verification ripetibile
- misurazioni comparabili per tradeoff architetturali

---

## 5. Politica della Baseline Java 25

### Politica obbligatoria

- impostare `java.version=25`
- impostare `maven.compiler.release=25`
- rifiutare runtime non supportati con Enforcer
- evitare downgrade nascosti per modulo

### Politica raccomandata

- preferire feature Java 25 stabili
- documentare separatamente le opzioni runtime opzionali
- trattare le preview come eccezione, non come default

---

## 6. Blueprint dei Profili di Build

Un reactor professionale dovrebbe esporre profili nominati per i concern di delivery.

### `quality`

Contenuti tipici:
- SpotBugs
- controlli statici
- gate di verifica più severi

### `security`

Contenuti tipici:
- vulnerability scanning delle dipendenze
- validazione inventory dipendenze

### `sbom`

Contenuti tipici:
- generazione SBOM CycloneDX o equivalente

### `coverage`

Contenuti tipici:
- JaCoCo per modulo
- report aggregato opzionale

### `distribution`

Contenuti tipici:
- assembly packaging
- layout runtime finale
- output tar/zip/distribution

---

## 7. Blueprint della Stratificazione dei Test

Uno strong repository distingue le categorie di test.

### Classi suggerite

- unit test
- integration test
- operational smoke test
- non-functional test
- benchmark

### Meccanismi possibili

- group/tag Maven
- profili dedicati
- convenzioni di naming
- wrapper shell per smoke o test dipendenti dall’ambiente

---

## 8. Blueprint degli Script

Un sistema Java non è davvero consegnato se non può essere operato dal repository stesso.

### Famiglie di script attese

- build script
- start script
- stop script
- status script
- smoke test script
- quality/security wrapper
- helper di bootstrap environment
- helper di monitoring e diagnostica

### Principi di design

- fail fast
- caricamento environment esplicito
- log leggibili
- errori comprensibili
- niente magia silenziosa

---

## 9. Blueprint della Documentazione

La documentazione deve essere trattata come parte dell’architettura.

### Tassonomia raccomandata

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

### Regola di design

Non lasciare che l’architettura viva solo nella testa delle persone o in diagrammi senza prosa. Il repository deve spiegare sé stesso.

---

## 10. Blueprint dell’Operational Readiness

Un servizio Java dovrebbe normalmente consegnare:

- strategia di profili startup
- health endpoint
- strategia metriche
- strategia logging
- service descriptor o equivalente
- template di configurazione runtime
- note di deployment e rollback

### Perché

Il build non è completo quando esiste il jar.  
È completo quando il servizio può essere avviato, ispezionato e recuperato in modo consistente.

---

## 11. Blueprint della Modernizzazione

Quando si modernizza un sistema legacy o vincolato, usare questo pattern.

### Step 1 — baseline
- rendere riproducibile il build
- aggiungere wrapper
- aggiungere version enforcement
- identificare il branch target

### Step 2 — platform move
- allineare livello Java release
- allineare plugin di build
- rimuovere assunzioni legacy su import o runtime

### Step 3 — application framework move
- migrazione Spring Boot
- migrazione Jakarta se necessaria
- allineamento config runtime

### Step 4 — operations
- allineamento startup scripts
- allineamento service unit
- razionalizzazione environment variables

### Step 5 — hardening
- test
- smoke checks
- observability
- release readiness

---

## 12. Convenzioni Ingegneristiche Raccomandate

### Codice
- solo inglese
- Javadoc per tipi pubblici e non banali
- commenti ricchi di razionale
- comportamento in errore esplicito

### Build
- policy al root
- niente drift nascosti tra moduli
- wrapper committato
- comandi riproducibili documentati

### Repository
- architettura visibile nei moduli
- shell e docs versionati
- confini nativi espliciti

### Delivery
- branch-driven
- PR-driven
- evidence-driven

---

## 13. Esempio Minimo di Blueprint

```text
my-system/
  docs/
    architecture/
    design/
    runbook/
  scripts/
    start.sh
    stop.sh
    status.sh
    verify-all.sh
  kernel/
  app/
  lib-storage/
  lib-telemetry/
  pom.xml
  mvnw
```

E il build root dovrebbe già sapere:
- quale versione Java è ammessa
- come eseguire quality checks
- come produrre artefatti security
- come pacchettizzare il runtime

---

## 14. Anti-Pattern che Questo Blueprint Evita

- modulo applicativo unico e confuso
- logica di build duplicata in ogni child
- conoscenza di deployment tenuta fuori dal repository
- ambiguità del tipo “works on my Java version”
- note di migrazione perse in chat o mail
- architettura visibile solo per caso nei package

---

## 15. Guida Finale

Questo blueprint non serve a rendere i repository più grandi.  
Serve a renderli **più veritieri**.

Un repository veritiero mostra:

- cos’è
- come si builda
- come si esegue
- come fallisce
- come si modernizza
- come si opera

Questo è il vero valore dello stile di delivery studiato.
