# Jexer Development Guide

**Manuale pratico per sviluppare applicazioni TUI in Java con Jexer**  
**Stile didattico ispirato ai manuali Borland Turbo Vision**

> Versione di lavoro: 1.2  
> Questa guida ora usa come riferimento il repository: `https://codeberg.org/AutumnMeowMeow/jexer`  
> Il complemento pratico di questo file è in:
>
> - `JEXER_COMPONENT_REFERENCE.md`
> - `JEXER_COOKBOOK.md`
>
> Nota: alcuni nomi di metodi o costruttori possono variare leggermente tra versioni di Jexer. Gli esempi seguono la API Jexer 2.0 letta dal repository di riferimento e sono pensati per restare il più possibile aderenti al codice reale.

---

## Indice

- [Prefazione](#prefazione)
- [1. Cos'è Jexer](#1-cosè-jexer)
- [2. Modello mentale](#2-modello-mentale)
- [3. Impostazione di un progetto](#3-impostazione-di-un-progetto)
- [4. Prima applicazione](#4-prima-applicazione)
- [5. Anatomia di una app Jexer](#5-anatomia-di-una-app-jexer)
- [6. Finestre](#6-finestre)
- [7. Widget standard](#7-widget-standard)
- [8. Menu, shortcut e status bar](#8-menu-shortcut-e-status-bar)
- [9. Dialog box](#9-dialog-box)
- [10. Eventi](#10-eventi)
- [11. Dati e stato applicativo](#11-dati-e-stato-applicativo)
- [12. Validazione](#12-validazione)
- [13. Layout e resize](#13-layout-e-resize)
- [14. Widget personalizzati](#14-widget-personalizzati)
- [15. Colori e tema visivo](#15-colori-e-tema-visivo)
- [16. Task lunghi e concorrenza](#16-task-lunghi-e-concorrenza)
- [17. Architetture consigliate](#17-architetture-consigliate)
- [18. Applicazione esempio](#18-applicazione-esempio)
- [19. Mappa Turbo Vision → Jexer](#19-mappa-turbo-vision--jexer)
- [20. Checklist finale](#20-checklist-finale)

---

## Prefazione

Jexer è, per molti sviluppatori Java, la strada più naturale per costruire una **interfaccia testuale ricca**: finestre, menu, dialog, campi di input, liste, pulsanti, scrollbar e controlli custom in un terminale.

Questa guida non è una semplice lista di classi. Il suo scopo è insegnarti:

- come pensare un programma Jexer
- come strutturarlo bene
- come farlo crescere senza trasformarlo in un groviglio di callback
- come ottenere un risultato gradevole e stabile

L'approccio è volutamente pratico e progressivo, come i vecchi manuali Borland.

---

## 1. Cos'è Jexer

Jexer è una libreria Java per applicazioni **TUI** (Text User Interface). Non è una CLI tradizionale a `println()`, ma un ambiente con:

- desktop logico
- finestre sovrapponibili
- focus tra i controlli
- eventi da tastiera e mouse
- ridisegno automatico della schermata

### Quando usarlo

Usa Jexer quando vuoi:

- tool gestionali da terminale
- browser di dati
- editor testuali strutturati
- dashboard operative
- strumenti interni rapidi da distribuire in Java

### Quando non usarlo

Evitalo se vuoi:

- grafica pixel-based
- componenti web
- interfacce touch-first
- layout responsive da browser

---

## 2. Modello mentale

Il modello corretto è questo:

- **TApplication**: l'applicazione
- **TWindow**: una finestra
- **Widget**: i controlli dentro una finestra
- **Eventi**: input tastiera, mouse, comandi
- **Model/Service**: dati e logica fuori dalla UI

### Regola d'oro

La UI **non deve essere** il tuo modello dati.

La UI serve a:
- visualizzare
- raccogliere input
- invocare azioni

I dati reali devono vivere in classi dedicate.

---

## 3. Impostazione di un progetto

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.miguel-luis</groupId>
        <artifactId>jexer</artifactId>
        <version>VERSIONE</version>
    </dependency>
</dependencies>
```

### Struttura consigliata

```text
src/main/java/
  app/
    Main.java
    ui/
      MainApp.java
      windows/
      dialogs/
      widgets/
    model/
    service/
    controller/
```

### Convenzione utile

- `model`: classi dominio
- `service`: salvataggio, file, rete, logica
- `ui/windows`: finestre principali
- `ui/dialogs`: dialog modali
- `ui/widgets`: controlli custom

---

## 4. Prima applicazione

Questo è il classico “hello world con finestra”.

```java
package app;

import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;
import jexer.backend.BackendType;

public class HelloApp extends TApplication {

    public HelloApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = addWindow("Hello Jexer", 2, 2, 50, 12);
        window.addLabel("Benvenuto in Jexer", 2, 2);
        window.addLabel("Questa è una vera finestra TUI.", 2, 3);
        window.addButton("Esci", 2, 5, new TAction() {
            @Override
            public void DO() {
                exit();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new HelloApp().run();
    }
}
```

### Osservazioni

- l'app deriva da `TApplication`
- il backend qui è `XTERM`
- la finestra viene aggiunta al desktop
- il bottone esegue un'azione tramite `TAction`

### Esercizio

Modifica l'esempio aggiungendo:
- un secondo bottone
- una seconda finestra
- una shortcut per uscire

---

## 5. Anatomia di una app Jexer

Una app Jexer seria ha almeno tre livelli.

### 5.1 Livello visuale

Contiene:
- finestre
- dialog
- controlli
- menu

### 5.2 Livello logico

Contiene:
- servizi
- use case
- trasformazioni dati
- accesso a file o risorse

### 5.3 Livello dati

Contiene:
- entità
- record
- configurazioni
- stato persistente

### Esempio

```java
public class Customer {
    private String name;
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

```java
public class CustomerService {
    public void save(Customer customer) {
        // logica di persistenza
    }
}
```

La finestra non dovrebbe salvare direttamente file o database: dovrebbe delegare.

---

## 6. Finestre

Le finestre sono il contenitore naturale dell'interazione.

### 6.1 Finestra semplice

```java
TWindow customerWindow = addWindow("Customers", 1, 1, 70, 20);
```

### 6.2 Uso corretto delle finestre

Una finestra dovrebbe rappresentare:

- una vista principale
- uno strumento dedicato
- un editor
- un pannello di informazioni

### 6.3 Finestra come classe dedicata

```java
package app.ui.windows;

import jexer.TApplication;
import jexer.TWindow;

public class AboutWindow extends TWindow {
    public AboutWindow(TApplication app) {
        super(app, "About", 5, 3, 40, 10);
        addLabel("Demo application in Jexer", 2, 2);
        addLabel("Version 1.0", 2, 3);
    }
}
```

### Nota di progetto

Se una finestra supera ~200 righe di logica applicativa, spesso va spezzata.

---

## 7. Widget standard

### 7.1 Label

Per testo statico.

```java
window.addLabel("Nome:", 2, 2);
```

### 7.2 Button

```java
window.addButton("Salva", 2, 10, new TAction() {
    @Override
    public void DO() {
        saveRecord();
    }
});
```

### 7.3 Field / input

Esempio tipico di form:

```java
window.addLabel("Nome:", 2, 2);
window.addLabel("Email:", 2, 4);

// La firma esatta può variare per versione.
// In molte versioni è disponibile un addField simile a questo.
nameField = window.addField(15, 2, 30, false);
emailField = window.addField(15, 4, 30, false);
```

### 7.4 Checkbox

```java
activeBox = window.addCheckBox(2, 6, "Attivo", true);
```

### 7.5 Radio group

Usalo per scelte esclusive.

### 7.6 List widget

Usalo per:
- elenco record
- file
- elementi selezionabili

### 7.7 Text area / editor

Per note e testo libero multi-linea.

### Regola pratica

Ogni finestra deve contenere pochi widget ben organizzati. Troppi controlli distruggono la leggibilità.

---

## 8. Menu, shortcut e status bar

Le app TUI ricche devono essere forti da tastiera.

### 8.1 Struttura menu classica

- File
- Edit
- View
- Tools
- Help

### 8.2 Convenzioni utili

- `Alt+X` o `Ctrl+Q`: uscita
- `F1`: help
- `F2`: salva
- `F3`: cerca/apri
- `F10`: menu

### 8.3 Esempio concettuale

```java
// Le API di menu possono differire tra versioni.
// Usa questo come modello organizzativo.
addFileMenu();
addEditMenu();
addWindowMenu();
addHelpMenu();
```

### 8.4 Status bar

Una status bar ben fatta mostra:
- scorciatoie principali
- contesto corrente
- messaggi brevi di stato

---

## 9. Dialog box

I dialog servono per interazioni brevi e mirate.

### 9.1 Dialog di conferma

```java
boolean confirmed = askToDelete();
if (confirmed) {
    deleteSelected();
}
```

### 9.2 Dialog di input

Usali per:
- nome file
- stringa di ricerca
- nuovo elemento

### 9.3 Dialog come classe separata

```java
public class CustomerDialog extends TWindow {
    public CustomerDialog(TApplication app) {
        super(app, "Customer", 10, 5, 50, 15, MODAL);
        addLabel("Nome:", 2, 2);
        addLabel("Email:", 2, 4);
    }
}
```

### Regola

Se un dialog ha troppa logica, smette di essere un dialog e diventa una finestra funzionale.

---

## 10. Eventi

Un programma Jexer vive di eventi.

### 10.1 Tipi comuni

- click su un bottone
- pressione tasto
- cambio focus
- selezione lista
- resize terminale

### 10.2 Callback piccoli

**Cattivo:**

```java
window.addButton("Salva", 2, 10, new TAction() {
    @Override
    public void DO() {
        // 100 righe di logica
    }
});
```

**Buono:**

```java
window.addButton("Salva", 2, 10, new TAction() {
    @Override
    public void DO() {
        onSave();
    }
});

private void onSave() {
    Customer customer = readForm();
    validate(customer);
    service.save(customer);
    showSavedMessage();
}
```

### 10.3 Tastiera prima del mouse

In Jexer, la tastiera va trattata come canale principale.

---

## 11. Dati e stato applicativo

Non leggere lo stato dai widget in modo anarchico.

### 11.1 Pattern corretto

```java
private Customer readForm() {
    Customer c = new Customer();
    c.setName(nameField.getText());
    c.setEmail(emailField.getText());
    return c;
}
```

```java
private void writeForm(Customer c) {
    nameField.setText(c.getName());
    emailField.setText(c.getEmail());
}
```

### 11.2 Stato minimo globale

Mantieni globale solo:
- configurazione
- sessione utente
- services condivisi

Evita singleton enormi.

---

## 12. Validazione

La validazione deve esistere a due livelli.

### 12.1 Livello UI

Controlli semplici:
- campo vuoto
- lunghezza massima
- formato basilare

### 12.2 Livello business

Regole reali:
- email valida
- codice univoco
- range ammesso
- relazioni coerenti tra campi

### Esempio

```java
private void validate(Customer c) {
    if (c.getName() == null || c.getName().isBlank()) {
        throw new IllegalArgumentException("Il nome è obbligatorio");
    }
    if (c.getEmail() == null || !c.getEmail().contains("@")) {
        throw new IllegalArgumentException("Email non valida");
    }
}
```

---

## 13. Layout e resize

Jexer lavora in coordinate testuali.

### 13.1 Buone pratiche

- margini costanti
- allineamenti netti
- gruppi di controlli omogenei
- bottoni sempre in posizioni prevedibili

### 13.2 Resize

Una TUI seria deve reggere il ridimensionamento del terminale.

Strategie:
- dimensione minima supportata
- controlli che si allargano con criterio
- pannelli che si riducono senza sovrapporsi

### 13.3 Regola visiva

Non cercare di riempire ogni cella. Lascia respirare la schermata.

---

## 14. Widget personalizzati

Quando i widget standard non bastano, costruiscine uno.

### 14.1 Quando conviene

- mini-grafici
- progress monitor avanzati
- tabelle speciali
- pannelli di stato
- viewer custom

### 14.2 Esempio di widget concettuale

```java
public class StatusPanel extends TWidget {
    private String title = "";
    private String value = "";

    public StatusPanel(TWindow parent, int x, int y, int width) {
        super(parent, x, y, width, 3);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void draw() {
        // Disegno semplificato: bordo, titolo, valore
        putStringXY(0, 0, "+------------------+");
        putStringXY(0, 1, "| " + title + ": " + value);
        putStringXY(0, 2, "+------------------+");
    }
}
```

### Nota

Le primitive di drawing possono cambiare tra versioni. Il punto importante è il pattern:

- stato interno piccolo
- metodi setter chiari
- rendering separato dalla logica

---

## 15. Colori e tema visivo

Un buon tema TUI migliora drasticamente l'usabilità.

### Usa il colore per:

- focus
- errore
- selezione
- stato attivo/inattivo
- gerarchia

### Evita:

- troppi colori saturi
- testo con contrasto scarso
- significati cromatici incoerenti

### Tema classico consigliato

Se vuoi un look “Turbo-like”:
- sfondo blu o scuro
- finestre chiare o ben delineate
- evidenziazione netta del focus
- status line ben leggibile

---

## 16. Task lunghi e concorrenza

Mai bloccare la UI per operazioni lunghe.

### Esempi di task da fare in background

- scansione directory
- import/export file
- sincronizzazione rete
- elaborazioni grandi

### Pattern consigliato

1. mostra progress dialog
2. avvia task in thread separato
3. aggiorna stato/progresso
4. chiudi dialog al termine

### Regola pratica

Se un'operazione dura più di mezzo secondo, dai sempre feedback visivo.

---

## 17. Architetture consigliate

### 17.1 App piccola

- 1 classe app
- 1 finestra principale
- 1 service

### 17.2 App media

- `MainApp`
- finestre dedicate
- dialog separati
- model e service separati

### 17.3 Pattern consigliato

**MV-ish leggero:**
- Model: dati
- View: Jexer UI
- Controller/Presenter: collega eventi e servizi

### Da evitare

- una sola classe con tutto
- logica business dentro i bottoni
- I/O file sparso ovunque nella UI

---

## 18. Applicazione esempio

Qui sotto trovi uno scheletro realistico.

### 18.1 Modello

```java
package app.model;

public class Note {
    private String title;
    private String body;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
```

### 18.2 Service

```java
package app.service;

import app.model.Note;
import java.util.ArrayList;
import java.util.List;

public class NoteService {
    private final List<Note> notes = new ArrayList<>();

    public List<Note> all() {
        return notes;
    }

    public void add(Note note) {
        notes.add(note);
    }

    public void remove(Note note) {
        notes.remove(note);
    }
}
```

### 18.3 App principale

```java
package app;

import app.service.NoteService;
import app.ui.windows.MainWindow;
import jexer.TApplication;
import jexer.backend.BackendType;

public class MainApp extends TApplication {
    public MainApp() throws Exception {
        super(BackendType.XTERM);

        NoteService service = new NoteService();
        new MainWindow(this, service);
    }

    public static void main(String[] args) throws Exception {
        new MainApp().run();
    }
}
```

### 18.4 Finestra principale

```java
package app.ui.windows;

import app.service.NoteService;
import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

public class MainWindow extends TWindow {
    private final NoteService service;

    public MainWindow(TApplication app, NoteService service) {
        super(app, "Notes", 1, 1, 80, 24);
        this.service = service;

        addLabel("Notes manager", 2, 1);
        addButton("Nuova", 2, 20, new TAction() {
            @Override
            public void DO() {
                onNewNote();
            }
        });
        addButton("Esci", 14, 20, new TAction() {
            @Override
            public void DO() {
                app.exit();
            }
        });
    }

    private void onNewNote() {
        // Apri dialog di editing o form dedicata
    }
}
```

Questo esempio non è un prodotto finito, ma mostra la struttura giusta.

---

## 19. Mappa Turbo Vision → Jexer

| Turbo Vision | Jexer |
|---|---|
| TApplication | TApplication |
| Desktop | desktop interno gestito dall'app |
| TWindow | TWindow |
| Dialog | dialog/finestra modale |
| StatusLine | status bar |
| MenuBar | menu bar |
| InputLine | field/input text |
| Button | button |
| ListBox/ListViewer | list widget |
| Event loop | dispatch eventi Jexer |
| Custom draw | widget custom |

### Nota importante

Jexer non è un clone 1:1 di Turbo Vision. È una libreria Java con lo stesso spirito generale.

---

## 20. Checklist finale

Prima di considerare “buona” una app Jexer, controlla:

### Architettura
- [ ] UI separata da dati e servizi
- [ ] callback piccoli
- [ ] niente I/O sparso nella UI

### Usabilità
- [ ] app usabile solo da tastiera
- [ ] shortcut coerenti
- [ ] focus chiaro
- [ ] messaggi leggibili

### Robustezza
- [ ] validazione presente
- [ ] resize tollerato
- [ ] errori gestiti con messaggi chiari

### Aspetto
- [ ] colori coerenti
- [ ] layout ordinato
- [ ] controlli ben allineati

---

## Conclusione

Se usato bene, Jexer ti permette di creare applicazioni testuali molto ricche, piacevoli e professionali. Il segreto non è solo conoscere i widget: è progettare bene l'applicazione.

Le regole chiave sono semplici:

1. separa sempre UI e logica
2. privilegia la tastiera
3. tratta finestre e dialog come oggetti veri
4. valida i dati
5. mantieni i callback piccoli
6. costruisci componenti custom solo quando servono davvero

Con questo approccio, Jexer diventa una piattaforma eccellente per utility, strumenti operativi, mini-gestionali e applicazioni da terminale con un vero sapore desktop classico.
