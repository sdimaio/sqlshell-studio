# Jexer Component Reference

**Riferimento pratico ai componenti di Jexer 2.0**  
**Basato sul repository di riferimento:** https://codeberg.org/AutumnMeowMeow/jexer

> Questa guida è costruita leggendo il codice del repository Jexer 2.0 e i demo sotto `src/demo` ed `examples/`.
>
> Obiettivo: imparare i componenti con snippet brevi, realistici e il più possibile fedeli alla API reale.

---

## Indice

1. [Bootstrapping: TApplication](#1-bootstrapping-tapplication)
2. [TWindow](#2-twindow)
3. [TLabel](#3-tlabel)
4. [TButton e TAction](#4-tbutton-e-taction)
5. [TField](#5-tfield)
6. [TPasswordField](#6-tpasswordfield)
7. [TCheckBox](#7-tcheckbox)
8. [TRadioGroup e TRadioButton](#8-tradiogroup-e-tradiobutton)
9. [TComboBox](#9-tcombobox)
10. [TSpinner](#10-tspinner)
11. [TCalendar](#11-tcalendar)
12. [TList](#12-tlist)
13. [TText](#13-ttext)
14. [TEditor](#14-teditor)
15. [TProgressBar](#15-tprogressbar)
16. [TTimer](#16-ttimer)
17. [TMessageBox](#17-tmessagebox)
18. [TInputBox](#18-tinputbox)
19. [TFileOpenBox](#19-tfileopenbox)
20. [TTreeViewScrollable e TDirectoryTreeItem](#20-ttreeviewscrollable-e-tdirectorytreeitem)
21. [TTable](#21-ttable)
22. [TPanel](#22-tpanel)
23. [TSplitPane](#23-tsplitpane)
24. [TImage](#24-timage)
25. [TTerminalWindow](#25-tterminalwindow)
26. [TStatusBar](#26-tstatusbar)
27. [Layout managers](#27-layout-managers)
28. [Menu](#28-menu)

---

## 1. Bootstrapping: TApplication

La classe base dell'applicazione è `jexer.TApplication`.

### Esempio minimo

```java
import jexer.TApplication;

public class HelloWorld {
    public static void main(String [] args) throws Exception {
        TApplication app = new TApplication(TApplication.BackendType.XTERM);
        app.addToolMenu();
        app.addFileMenu();
        app.addWindowMenu();
        app.run();
    }
}
```

### Backend disponibili

Dal sorgente di `TApplication`:

- `TApplication.BackendType.SWING`
- `TApplication.BackendType.ECMA48`
- `TApplication.BackendType.XTERM`

### Quando usare una sottoclasse

Se la tua app ha menu, stato e logica iniziale, crea una sottoclasse:

```java
import jexer.TApplication;

public class MyApplication extends TApplication {

    public MyApplication() throws Exception {
        super(BackendType.XTERM);
        addToolMenu();
        addFileMenu();
        addWindowMenu();
    }

    public static void main(String [] args) throws Exception {
        MyApplication app = new MyApplication();
        app.run();
    }
}
```

---

## 2. TWindow

`TWindow` è il contenitore standard per widget e contenuti.

### Costruttore classico

```java
import jexer.TWindow;

TWindow window = new TWindow(app, "Customers", 1, 1, 70, 20);
```

### Costruttore con flag

```java
TWindow modal = new TWindow(app, "Settings", 0, 0, 60, 18,
    TWindow.MODAL | TWindow.CENTERED | TWindow.RESIZABLE);
```

### Classe dedicata

```java
import jexer.TApplication;
import jexer.TWindow;

public class AboutWindow extends TWindow {
    public AboutWindow(TApplication app) {
        super(app, "About", 5, 3, 40, 10);
        addLabel("Jexer demo", 2, 2);
    }
}
```

### Metodi utili da conoscere

- `setTitle(...)`
- `newStatusBar(...)`
- `setLayoutManager(...)`
- `activate(widget)`
- override di `onResize(...)`

---

## 3. TLabel

`TLabel` serve per testo statico.

### Uso base

```java
window.addLabel("Nome:", 2, 2);
```

### Con color key esplicita

Nel demo compare anche:

```java
TLabel dayOfWeekLabel = addLabel("Wednesday-", 35, row - 1,
    "tmenu", false);
```

### Aggiornare il testo

```java
dayOfWeekLabel.setLabel("Thursday ");
dayOfWeekLabel.setWidth(dayOfWeekLabel.getLabel().length());
```

---

## 4. TButton e TAction

`TButton` usa callback basati su `TAction`.

### Bottone base

```java
import jexer.TAction;

window.addButton("Salva", 2, 10, new TAction() {
    @Override
    public void DO() {
        saveRecord();
    }
});
```

### Bottone per chiudere finestra

```java
window.addButton("Close", 2, 12, new TAction() {
    @Override
    public void DO() {
        getApplication().closeWindow(MyWindow.this);
    }
});
```

### Bottone che apre una finestra

```java
window.addButton("About", 2, 14, new TAction() {
    @Override
    public void DO() {
        new AboutWindow(getApplication());
    }
});
```

---

## 5. TField

`TField` è il campo testo standard.

### Overload principali di `addField()`

Dal sorgente di `TWidget`:

```java
addField(int x, int y, int width, boolean fixed)
addField(int x, int y, int width, boolean fixed, String text)
addField(int x, int y, int width, boolean fixed, String text, TAction enterAction)
addField(int x, int y, int width, boolean fixed, String text, TAction enterAction, TAction updateAction)
```

### Esempio semplice

```java
TField nameField = window.addField(15, 2, 30, false, "Mario Rossi");
```

### Campo con enter action

```java
TField searchField = window.addField(15, 4, 30, false, "",
    new TAction() {
        @Override
        public void DO() {
            performSearch();
        }
    }
);
```

### Campo con enter + update action

```java
TField codeField = window.addField(15, 6, 12, true, "",
    new TAction() {
        @Override
        public void DO() {
            submitCode();
        }
    },
    new TAction() {
        @Override
        public void DO() {
            validateCodeWhileTyping();
        }
    }
);
```

### Leggere e scrivere il testo

```java
String text = nameField.getText();
nameField.setText("Nuovo valore");
```

---

## 6. TPasswordField

Versione masked del campo testo.

### Creazione

```java
TPasswordField pwd = window.addPasswordField(15, 2, 20, false);
```

### Con valore iniziale

```java
TPasswordField pwd = window.addPasswordField(15, 4, 20, true, "hunter2");
```

### Con callback

```java
TPasswordField pwd = window.addPasswordField(15, 6, 20, false, "",
    new TAction() {
        @Override
        public void DO() {
            login();
        }
    },
    new TAction() {
        @Override
        public void DO() {
            updatePasswordStrength();
        }
    }
);
```

---

## 7. TCheckBox

### Creazione base

```java
TCheckBox activeBox = window.addCheckBox(2, 2, "Attivo", true);
```

### Con callback

```java
TCheckBox debugBox = window.addCheckBox(2, 4, "Debug", false,
    new TAction() {
        @Override
        public void DO() {
            onDebugToggle();
        }
    }
);
```

### Lettura e scrittura stato

```java
boolean active = activeBox.isChecked();
activeBox.setChecked(true);
```

---

## 8. TRadioGroup e TRadioButton

`TRadioGroup` gestisce opzioni mutualmente esclusive.

### Creazione gruppo

```java
TRadioGroup radioGroup = window.addRadioGroup(2, 2, "Modalità");
radioGroup.addRadioButton("Automatico");
radioGroup.addRadioButton("Manuale", true);
radioGroup.addRadioButton("Off");
radioGroup.setRequiresSelection(true);
```

### Variante con larghezza esplicita

```java
TRadioGroup group = window.addRadioGroup(2, 8, 24, "Theme");
```

### Leggere la selezione

```java
int selectedIndex = radioGroup.getSelected();
```

---

## 9. TComboBox

### Costruttore helper

```java
List<String> values = new ArrayList<>();
values.add("One");
values.add("Two");
values.add("Three");

TComboBox comboBox = window.addComboBox(20, 2, 12, values, 1, 6,
    new TAction() {
        @Override
        public void DO() {
            onComboChanged();
        }
    }
);
```

Parametri:
- `x, y`
- `width`
- `values`
- `valuesIndex` iniziale
- `maxValuesHeight`
- `updateAction`

### Ottenere e impostare testo

```java
String value = comboBox.getText();
comboBox.setText("Two");
```

### Demo reale dal progetto

```java
comboBox = addComboBox(40, row, 12, comboValues, 2, 6,
    new TAction() {
        public void DO() {
            getApplication().messageBox("Chosen", comboBox.getText(),
                TMessageBox.Type.OK);
        }
    }
);
```

---

## 10. TSpinner

Lo spinner è molto semplice: due callback, su e giù.

```java
window.addSpinner(35, 10,
    new TAction() {
        @Override
        public void DO() {
            incrementValue();
        }
    },
    new TAction() {
        @Override
        public void DO() {
            decrementValue();
        }
    }
);
```

### Caso reale dal demo

```java
addSpinner(35 + dayOfWeekLabel.getWidth(), row - 1,
    new TAction() {
        public void DO() {
            dayOfWeekCalendar.add(Calendar.DAY_OF_WEEK, 1);
            refreshLabel();
        }
    },
    new TAction() {
        public void DO() {
            dayOfWeekCalendar.add(Calendar.DAY_OF_WEEK, -1);
            refreshLabel();
        }
    }
);
```

---

## 11. TCalendar

### Creazione

```java
TCalendar calendar = window.addCalendar(2, 2,
    new TAction() {
        @Override
        public void DO() {
            onDateChanged();
        }
    }
);
```

### Leggere il valore

`getValue()` restituisce un `java.util.Calendar`.

```java
Calendar value = calendar.getValue();
Date date = new Date(value.getTimeInMillis());
```

### Demo reale

```java
calendar = addCalendar(1, row++,
    new TAction() {
        public void DO() {
            getApplication().messageBox("Date",
                new Date(calendar.getValue().getTimeInMillis()).toString(),
                TMessageBox.Type.OK);
        }
    }
);
```

---

## 12. TList

`TList` visualizza una lista di stringhe e supporta callback su enter, movimento e click.

### Overload utili

```java
addList(List<String> strings, int x, int y, int width, int height)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction, TAction moveAction)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction, TAction moveAction, TAction singleClickAction)
```

### Esempio base

```java
List<String> items = Arrays.asList("Alpha", "Beta", "Gamma");
TList list = window.addList(items, 2, 2, 20, 8);
```

### Esempio con enter action

```java
TList list = window.addList(items, 2, 2, 20, 8,
    new TAction() {
        @Override
        public void DO() {
            openSelectedItem();
        }
    }
);
```

### Esempio con preview in movimento

```java
TList list = window.addList(items, 2, 2, 20, 8,
    new TAction() {
        @Override
        public void DO() {
            confirmSelection();
        }
    },
    new TAction() {
        @Override
        public void DO() {
            updatePreview();
        }
    }
);
```

### Elemento selezionato

```java
String current = list.getSelected();
```

---

## 13. TText

`TText` mostra testo multi-linea scrollabile, ma non editabile.

### Creazione

```java
String text = "Lorem ipsum\nseconda riga\nterza riga";
TText textView = window.addText(text, 1, 3, 40, 10);
```

### Con color key esplicita

```java
TText textView = window.addText(text, 1, 3, 40, 10, "ttext");
```

### Uso nei panel

Dal demo `Demo7`:

```java
left.addText("C1", 0, 0, left.getWidth(), left.getHeight());
right.addText("C4", 0, 0, right.getWidth(), right.getHeight());
```

---

## 14. TEditor

`TEditor` è l'area testo editabile.

### Creazione

```java
TEditor editor = window.addEditor("Contenuto iniziale", 0, 0, 42, 20);
```

### Leggere e scrivere il contenuto

```java
String content = editor.getText();
editor.setText("Nuovo contenuto");
```

### Finestra pronta all'uso

Jexer fornisce anche `TEditorWindow`:

```java
import jexer.TEditorWindow;

new TEditorWindow(getApplication());
```

oppure su file:

```java
new TEditorWindow(getApplication(), new File(filename));
```

---

## 15. TProgressBar

### Creazione

```java
TProgressBar progress = window.addProgressBar(20, 10, 12, 0);
```

### Variante con matchWindowBackground

```java
TProgressBar progress = window.addProgressBar(20, 12, 12, 0, true);
```

### Aggiornamento

```java
progress.setValue(50);
int current = progress.getValue();
```

### Uso reale con timer

```java
progressBar1 = addProgressBar(col + 13, row, 12, 0, true);
```

---

## 16. TTimer

`TApplication.addTimer(...)` è il modo standard per task periodici UI-safe.

### Firma reale

```java
TTimer addTimer(long duration, boolean recurring, TAction action)
```

### Esempio

```java
TTimer timer = getApplication().addTimer(250, true,
    new TAction() {
        @Override
        public void DO() {
            tick();
        }
    }
);
```

### Fermare il timer

```java
timer.setRecurring(false);
// oppure
getApplication().removeTimer(timer);
```

### Demo reale

```java
timer1 = getApplication().addTimer(250, true,
    new TAction() {
        public void DO() {
            if (timer1I < 100) {
                timer1I++;
            } else {
                timer1.setRecurring(false);
            }
            progressBar1.setValue(timer1I);
        }
    }
);
```

---

## 17. TMessageBox

### Tipi supportati

Dal sorgente:
- `TMessageBox.Type.OK`
- `TMessageBox.Type.OKCANCEL`
- `TMessageBox.Type.YESNO`
- `TMessageBox.Type.YESNOCANCEL`

### Esempio base

```java
getApplication().messageBox("Info", "Operazione completata",
    TMessageBox.Type.OK);
```

### Ottenere il risultato

```java
TMessageBox box = getApplication().messageBox("Delete",
    "Eliminare il record?", TMessageBox.Type.YESNOCANCEL);

if (box.isYes()) {
    deleteRecord();
}
```

### API utile

- `getResult()`
- `isYes()`
- `isNo()`
- `isOk()`
- `isCancel()`

---

## 18. TInputBox

`TInputBox` estende `TMessageBox` e aggiunge un `TField`.

### Esempio

```java
TInputBox in = getApplication().inputBox("Search", "Testo da cercare:");
String text = in.getText();
```

### Con testo iniziale

```java
TInputBox in = getApplication().inputBox("Rename",
    "Nuovo nome:", "default.txt");
```

### Con tipo OK/CANCEL

```java
TInputBox in = getApplication().inputBox("Rename",
    "Nuovo nome:", "default.txt", TInputBox.Type.OKCANCEL);

String text = in.getText();
Object result = in.getResult();
```

### Demo reale

```java
TInputBox in = getApplication().inputBox("Input",
    "Inserisci un valore", "seed", TInputBox.Type.OKCANCEL);

getApplication().messageBox("Result",
    in.getText() + " / " + in.getResult());
```

---

## 19. TFileOpenBox

### Tipi supportati

Dal sorgente:
- `TFileOpenBox.Type.OPEN`
- `TFileOpenBox.Type.SAVE`
- `TFileOpenBox.Type.SELECT`

### Esempio base

```java
String filename = fileOpenBox(".");
```

### Apertura esplicita

```java
String filename = fileOpenBox(".", TFileOpenBox.Type.OPEN);
```

### Salvataggio

```java
String filename = fileSaveBox(".");
```

### Con filtri regex

```java
List<String> filters = new ArrayList<>();
filters.add("^.*\\.[Tt][Xx][Tt]$");
filters.add("^.*\\.[Cc][Ss][Vv]$");

String filename = fileOpenBox(".", TFileOpenBox.Type.OPEN, filters);
```

---

## 20. TTreeViewScrollable e TDirectoryTreeItem

### Creazione tree view

```java
TTreeViewScrollable treeView = window.addTreeViewWidget(1, 1, 40, 12);
```

### Con callback su selezione

```java
treeView = addTreeViewWidget(0, 0, getWidth() / 2, getHeight(),
    new TAction() {
        @Override
        public void DO() {
            TTreeItem item = treeView.getSelected();
            onTreeSelection(item);
        }
    }
);
```

### Popolare con filesystem

```java
new TDirectoryTreeItem(treeView, ".", true);
```

### Ottenere elemento selezionato

```java
TTreeItem selected = treeView.getSelected();
String text = selected.getText();
```

### Resize esplicito

Dal demo:

```java
TResizeEvent treeSize = new TResizeEvent(resize.getBackend(),
    TResizeEvent.Type.WIDGET, resize.getWidth() - 4,
    resize.getHeight() - 4);

treeView.onResize(treeSize);
```

---

## 21. TTable

`TTable` è uno dei componenti più ricchi.

### Creazione diretta

```java
TTable table = new TTable(window, 0, 0, 42, 20);
```

### Oppure helper

```java
TTable table = window.addTable(0, 0, 42, 20);
```

### Lavorare con le celle

```java
table.setCellText(0, 0, "ID");
table.setCellText(1, 0, "Name");
table.setCellText(0, 1, "1");
table.setCellText(1, 1, "Mario");

String value = table.getCellText(1, 1);
```

### Selezione cella

```java
table.setSelectedCell(1, 1);
```

### Label di righe e colonne

```java
table.setShowRowLabels(true);
table.setShowColumnLabels(true);
table.setColumnLabel(0, "Code");
table.setColumnLabel(1, "Description");
table.setRowLabel(0, "Header");
```

### Modifiche strutturali

```java
table.insertRowBelow(1);
table.insertColumnRight(0);
table.deleteRow(2);
table.deleteColumn(1);
```

### Eventi per singola cella

```java
table.setCellEnterAction(1, 1, new TAction() {
    @Override
    public void DO() {
        openDetail();
    }
});

table.setCellUpdateAction(1, 1, new TAction() {
    @Override
    public void DO() {
        validateCell();
    }
});
```

---

## 22. TPanel

`TPanel` è un contenitore con bordo e titolo, ottimo per comporre layout.

### Creazione

```java
TPanel left = window.addPanel(0, 0, 10, 10);
left.setBorderStyle("singleVdoubleH");
left.setTitle("Left");
```

### Altro pannello

```java
TPanel right = window.addPanel(0, 0, 10, 10);
right.setBorderStyle("round");
right.setTitle("Right");
```

### Titolo posizionato diversamente

```java
left.setTitleDirection(TPanel.Direction.BOTTOM_RIGHT);
```

### Contenuti dentro il panel

```java
left.addText("C1", 0, 0, left.getWidth(), left.getHeight());
```

---

## 23. TSplitPane

`TSplitPane` divide un'area in due regioni.

### Creazione helper

```java
TSplitPane split = window.addSplitPane(1, 1, 60, 18, true);
```

Il parametro finale `vertical`:
- `true`: split verticale
- `false`: split orizzontale

### Assegnare widget ai lati

```java
TPanel leftPanel = new TPanel(split, 0, 0, 20, 18);
TPanel rightPanel = new TPanel(split, 0, 0, 40, 18);

split.setLeft(leftPanel);
split.setRight(rightPanel);
```

### Variante top/bottom

```java
TSplitPane split = window.addSplitPane(1, 1, 60, 18, false);
split.setTop(topPanel);
split.setBottom(bottomPanel);
```

---

## 24. TImage

Jexer 2.0 supporta immagini molto bene.

### Helper base

```java
BufferedImage image = ImageIO.read(new File("logo.png"));
TImage imageWidget = window.addImage(0, 0, 30, 10, image, 0, 0);
```

### Uso reale

Nel viewer di esempio, l'immagine è collocata in una porzione di desktop e ricreata al resize.

### Operazioni utili

Dal sorgente compaiono metodi come:
- `setTop(...)`
- `setLeft(...)`

utili per scroll o pan.

---

## 25. TTerminalWindow

Uno dei componenti più interessanti di Jexer: un terminale dentro una finestra.

### Aprire un terminale shell standard

```java
getApplication().openTerminal(0, 0);
```

### Con flag

```java
getApplication().openTerminal(0, 0, TWindow.RESIZABLE);
```

### Eseguire un comando

```java
getApplication().openTerminal(2, 2, "htop", true);
```

### Con array command

```java
String[] cmd = {"bash", "-lc", "ls -la"};
getApplication().openTerminal(2, 2, TWindow.RESIZABLE, cmd, true);
```

### Costruttore diretto

```java
new TTerminalWindow(getApplication(), 2, 2, "bash", true);
```

---

## 26. TStatusBar

Quasi tutti i demo Jexer usano una status bar.

### Creazione

```java
statusBar = newStatusBar("F1 Help  F10 Exit");
```

### Shortcut tipici

```java
import static jexer.TCommand.*;
import static jexer.TKeypress.*;

statusBar.addShortcutKeypress(kbF1, cmHelp, "Help");
statusBar.addShortcutKeypress(kbF10, cmExit, "Exit");
```

### Aggiornare il testo

```java
statusBar.setText("Loaded 12 records");
```

---

## 27. Layout managers

Jexer include un package `jexer.layout` con:

- `StretchLayoutManager`
- `BoxLayoutManager`
- `AnchoredLayoutManager`

### StretchLayoutManager

Molto usato nei demo classici.

```java
import jexer.layout.StretchLayoutManager;

setLayoutManager(new StretchLayoutManager(getWidth() - 2,
        getHeight() - 2));
```

### BoxLayoutManager

Dal demo `Demo7`:

```java
import jexer.layout.BoxLayoutManager;

window.setLayoutManager(new BoxLayoutManager(
    window.getWidth() - 2,
    window.getHeight() - 2,
    false
));
```

Per pannelli verticali:

```java
left.setLayoutManager(new BoxLayoutManager(
    left.getWidth(), left.getHeight(), true
));
```

---

## 28. Menu

Jexer fornisce menu stock e menu custom.

### Menu stock

```java
addToolMenu();
addFileMenu();
addWindowMenu();
```

### Menu custom

```java
import jexer.menu.TMenu;

TMenu fileMenu = addMenu("&File");
fileMenu.addDefaultItem(TMenu.MID_SHELL);
fileMenu.addSeparator();
fileMenu.addDefaultItem(TMenu.MID_EXIT);
```

### Leggere un menu item esistente

```java
TMenuItem menuItem = getApplication().getMenuItem(10010);
boolean checked = menuItem.isChecked();
menuItem.setChecked(true);
```

---

# Strategia di studio consigliata

Per imparare davvero i componenti Jexer, usa questo ordine:

1. `TApplication`, `TWindow`, `TButton`, `TLabel`
2. `TField`, `TPasswordField`, `TInputBox`
3. `TCheckBox`, `TRadioGroup`, `TComboBox`, `TSpinner`
4. `TList`, `TTreeViewScrollable`
5. `TText`, `TEditor`
6. `TProgressBar`, `TTimer`, `TStatusBar`
7. `TTable`, `TPanel`, `TSplitPane`
8. `TImage`, `TTerminalWindow`

---

# File sorgente da studiare nel repository

I più utili per imparare bene Jexer 2.0 sono:

### Demos
- `src/demo/DemoMainWindow.java`
- `src/demo/DemoTextFieldWindow.java`
- `src/demo/DemoCheckBoxWindow.java`
- `src/demo/DemoMsgBoxWindow.java`
- `src/demo/DemoTreeViewWindow.java`
- `src/demo/DemoTableWindow.java`
- `src/demo/Demo7.java`

### Esempi completi
- `examples/HelloWorld.java`
- `examples/MyApplication.java`
- `examples/JexerImageViewer.java`
- `examples/JexerTilingWindowManager.java`
- `examples/JexerTilingWindowManager2.java`

---

# Conclusione

Se vuoi imparare davvero i componenti, la strada più efficace è:

- leggere questo file
- aprire i demo originali del repository
- copiare uno snippet alla volta in una tua mini app
- fare micro-esperimenti su ciascun widget

Questa è esattamente la logica dei vecchi manuali Borland: piccoli passi, esempi concreti, poi composizione in applicazioni più grandi.
