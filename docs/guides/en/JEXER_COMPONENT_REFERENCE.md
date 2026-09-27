# Jexer Component Reference

**Practical reference for Jexer 2.0 components**  
**Reference repository:** https://codeberg.org/AutumnMeowMeow/jexer

> This guide was built by reading the Jexer 2.0 repository sources, especially `src/jexer`, `src/demo`, and `examples/`.
>
> Goal: learn the components through short, realistic snippets that are as close as possible to the real API.

---

## Contents

1. [Bootstrapping: TApplication](#1-bootstrapping-tapplication)
2. [TWindow](#2-twindow)
3. [TLabel](#3-tlabel)
4. [TButton and TAction](#4-tbutton-and-taction)
5. [TField](#5-tfield)
6. [TPasswordField](#6-tpasswordfield)
7. [TCheckBox](#7-tcheckbox)
8. [TRadioGroup and TRadioButton](#8-tradiogroup-and-tradiobutton)
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
20. [TTreeViewScrollable and TDirectoryTreeItem](#20-ttreeviewscrollable-and-tdirectorytreeitem)
21. [TTable](#21-ttable)
22. [TPanel](#22-tpanel)
23. [TSplitPane](#23-tsplitpane)
24. [TImage](#24-timage)
25. [TTerminalWindow](#25-tterminalwindow)
26. [TStatusBar](#26-tstatusbar)
27. [Layout managers](#27-layout-managers)
28. [Menus](#28-menus)

---

## 1. Bootstrapping: TApplication

The base class for an application is `jexer.TApplication`.

### Minimal example

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

### Available backends

From `TApplication` source:
- `TApplication.BackendType.SWING`
- `TApplication.BackendType.ECMA48`
- `TApplication.BackendType.XTERM`

### When to subclass it

If your app has startup logic and menus, create a subclass:

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

`TWindow` is the standard container for widgets and interactive content.

### Classic constructor

```java
import jexer.TWindow;

TWindow window = new TWindow(app, "Customers", 1, 1, 70, 20);
```

### Constructor with flags

```java
TWindow modal = new TWindow(app, "Settings", 0, 0, 60, 18,
    TWindow.MODAL | TWindow.CENTERED | TWindow.RESIZABLE);
```

### Dedicated class

```java
import jexer.TApplication;
import jexer.TWindow;

public class AboutWindow extends TWindow {
    public AboutWindow(TApplication app) {
        super(app, "About", 5, 3, 40, 10);
        addLabel("Demo application in Jexer", 2, 2);
    }
}
```

### Useful methods to know

- `setTitle(...)`
- `newStatusBar(...)`
- `setLayoutManager(...)`
- `activate(widget)`
- override `onResize(...)`

---

## 3. TLabel

`TLabel` is used for static text.

### Basic use

```java
window.addLabel("Name:", 2, 2);
```

### With explicit color key

```java
TLabel dayOfWeekLabel = addLabel("Wednesday-", 35, row - 1,
    "tmenu", false);
```

### Updating the label text

```java
dayOfWeekLabel.setLabel("Thursday ");
dayOfWeekLabel.setWidth(dayOfWeekLabel.getLabel().length());
```

---

## 4. TButton and TAction

Buttons use `TAction` callbacks.

### Basic button

```java
import jexer.TAction;

window.addButton("Save", 2, 10, new TAction() {
    @Override
    public void DO() {
        saveRecord();
    }
});
```

### Close window button

```java
window.addButton("Close", 2, 12, new TAction() {
    @Override
    public void DO() {
        getApplication().closeWindow(MyWindow.this);
    }
});
```

### Button that opens another window

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

`TField` is the standard text field.

### Main `addField()` overloads

```java
addField(int x, int y, int width, boolean fixed)
addField(int x, int y, int width, boolean fixed, String text)
addField(int x, int y, int width, boolean fixed, String text, TAction enterAction)
addField(int x, int y, int width, boolean fixed, String text, TAction enterAction, TAction updateAction)
```

### Simple example

```java
TField nameField = window.addField(15, 2, 30, false, "Mario Rossi");
```

### Field with enter action

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

### Read and write text

```java
String text = nameField.getText();
nameField.setText("New value");
```

---

## 6. TPasswordField

Masked version of a text field.

### Creation

```java
TPasswordField pwd = window.addPasswordField(15, 2, 20, false);
```

### With initial value

```java
TPasswordField pwd = window.addPasswordField(15, 4, 20, true, "hunter2");
```

### With callbacks

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

### Basic creation

```java
TCheckBox activeBox = window.addCheckBox(2, 2, "Active", true);
```

### With callback

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

### Read and write state

```java
boolean active = activeBox.isChecked();
activeBox.setChecked(true);
```

---

## 8. TRadioGroup and TRadioButton

`TRadioGroup` manages mutually exclusive options.

### Create a group

```java
TRadioGroup radioGroup = window.addRadioGroup(2, 2, "Mode");
radioGroup.addRadioButton("Automatic");
radioGroup.addRadioButton("Manual", true);
radioGroup.addRadioButton("Off");
radioGroup.setRequiresSelection(true);
```

### Group with explicit width

```java
TRadioGroup group = window.addRadioGroup(2, 8, 24, "Theme");
```

### Read current selection

```java
int selectedIndex = radioGroup.getSelected();
```

---

## 9. TComboBox

### Helper constructor

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

Parameters:
- `x, y`
- `width`
- `values`
- `valuesIndex`
- `maxValuesHeight`
- `updateAction`

### Get and set current text

```java
String value = comboBox.getText();
comboBox.setText("Two");
```

---

## 10. TSpinner

A spinner is simple: one callback for up and one for down.

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

---

## 11. TCalendar

### Creation

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

### Read current value

`getValue()` returns a `java.util.Calendar`.

```java
Calendar value = calendar.getValue();
Date date = new Date(value.getTimeInMillis());
```

---

## 12. TList

`TList` shows a list of strings and supports callbacks on enter, navigation, and click.

### Useful overloads

```java
addList(List<String> strings, int x, int y, int width, int height)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction, TAction moveAction)
addList(List<String> strings, int x, int y, int width, int height, TAction enterAction, TAction moveAction, TAction singleClickAction)
```

### Basic example

```java
List<String> items = Arrays.asList("Alpha", "Beta", "Gamma");
TList list = window.addList(items, 2, 2, 20, 8);
```

### Selected item

```java
String current = list.getSelected();
```

---

## 13. TText

`TText` shows multi-line scrollable text, but it is not editable.

### Creation

```java
String text = "Lorem ipsum\nsecond line\nthird line";
TText textView = window.addText(text, 1, 3, 40, 10);
```

### With explicit color key

```java
TText textView = window.addText(text, 1, 3, 40, 10, "ttext");
```

---

## 14. TEditor

`TEditor` is the editable text area.

### Creation

```java
TEditor editor = window.addEditor("Initial content", 0, 0, 42, 20);
```

### Read and write content

```java
String content = editor.getText();
editor.setText("New content");
```

### Ready-to-use editor window

```java
import jexer.TEditorWindow;

new TEditorWindow(getApplication());
```

or for a file:

```java
new TEditorWindow(getApplication(), new File(filename));
```

---

## 15. TProgressBar

### Creation

```java
TProgressBar progress = window.addProgressBar(20, 10, 12, 0);
```

### Variant with background matching

```java
TProgressBar progress = window.addProgressBar(20, 12, 12, 0, true);
```

### Update value

```java
progress.setValue(50);
int current = progress.getValue();
```

---

## 16. TTimer

`TApplication.addTimer(...)` is the standard way to schedule recurring UI work.

### Real signature

```java
TTimer addTimer(long duration, boolean recurring, TAction action)
```

### Example

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

### Stop it

```java
timer.setRecurring(false);
// or
getApplication().removeTimer(timer);
```

---

## 17. TMessageBox

### Supported types

- `TMessageBox.Type.OK`
- `TMessageBox.Type.OKCANCEL`
- `TMessageBox.Type.YESNO`
- `TMessageBox.Type.YESNOCANCEL`

### Basic example

```java
getApplication().messageBox("Info", "Operation completed",
    TMessageBox.Type.OK);
```

### Get result

```java
TMessageBox box = getApplication().messageBox("Delete",
    "Delete the selected record?", TMessageBox.Type.YESNOCANCEL);

if (box.isYes()) {
    deleteRecord();
}
```

---

## 18. TInputBox

`TInputBox` extends `TMessageBox` and adds a `TField`.

### Example

```java
TInputBox in = getApplication().inputBox("Search", "Text to find:");
String text = in.getText();
```

### With initial text

```java
TInputBox in = getApplication().inputBox("Rename",
    "New name:", "default.txt");
```

### With OK/CANCEL

```java
TInputBox in = getApplication().inputBox("Rename",
    "New name:", "default.txt", TInputBox.Type.OKCANCEL);
```

---

## 19. TFileOpenBox

### Supported types

- `TFileOpenBox.Type.OPEN`
- `TFileOpenBox.Type.SAVE`
- `TFileOpenBox.Type.SELECT`

### Basic example

```java
String filename = fileOpenBox(".");
```

### Save

```java
String filename = fileSaveBox(".");
```

### With regex filters

```java
List<String> filters = new ArrayList<>();
filters.add("^.*\\.[Tt][Xx][Tt]$");
filters.add("^.*\\.[Cc][Ss][Vv]$");

String filename = fileOpenBox(".", TFileOpenBox.Type.OPEN, filters);
```

---

## 20. TTreeViewScrollable and TDirectoryTreeItem

### Create the tree

```java
TTreeViewScrollable treeView = window.addTreeViewWidget(1, 1, 40, 12);
```

### Populate with filesystem

```java
new TDirectoryTreeItem(treeView, ".", true);
```

### Get selected item

```java
TTreeItem selected = treeView.getSelected();
String text = selected.getText();
```

---

## 21. TTable

`TTable` is one of the richest components in the library.

### Direct creation

```java
TTable table = new TTable(window, 0, 0, 42, 20);
```

### Work with cells

```java
table.setCellText(0, 0, "ID");
table.setCellText(1, 0, "Name");
table.setCellText(0, 1, "1");
table.setCellText(1, 1, "Mario");

String value = table.getCellText(1, 1);
```

### Show row/column labels

```java
table.setShowRowLabels(true);
table.setShowColumnLabels(true);
table.setColumnLabel(0, "Code");
table.setColumnLabel(1, "Description");
```

---

## 22. TPanel

`TPanel` is a container with border and title, ideal for layout composition.

### Creation

```java
TPanel left = window.addPanel(0, 0, 10, 10);
left.setBorderStyle("singleVdoubleH");
left.setTitle("Left");
```

### Title placement

```java
left.setTitleDirection(TPanel.Direction.BOTTOM_RIGHT);
```

---

## 23. TSplitPane

`TSplitPane` divides an area into two regions.

### Create

```java
TSplitPane split = window.addSplitPane(1, 1, 60, 18, true);
```

The final boolean means:
- `true`: vertical split
- `false`: horizontal split

### Assign widgets

```java
split.setLeft(leftPanel);
split.setRight(rightPanel);
```

or:

```java
split.setTop(topPanel);
split.setBottom(bottomPanel);
```

---

## 24. TImage

Jexer 2.0 supports images very well.

### Basic helper

```java
BufferedImage image = ImageIO.read(new File("logo.png"));
TImage imageWidget = window.addImage(0, 0, 30, 10, image, 0, 0);
```

---

## 25. TTerminalWindow

One of the most interesting components: a terminal embedded in a window.

### Open a shell terminal

```java
getApplication().openTerminal(0, 0);
```

### Run a command

```java
String[] cmd = {"bash", "-lc", "ls -la"};
getApplication().openTerminal(2, 2, TWindow.RESIZABLE, cmd, true);
```

---

## 26. TStatusBar

Almost every demo uses a status bar.

### Create one

```java
statusBar = newStatusBar("F1 Help  F10 Exit");
```

### Add shortcuts

```java
import static jexer.TCommand.*;
import static jexer.TKeypress.*;

statusBar.addShortcutKeypress(kbF1, cmHelp, "Help");
statusBar.addShortcutKeypress(kbF10, cmExit, "Exit");
```

---

## 27. Layout managers

The `jexer.layout` package includes:
- `StretchLayoutManager`
- `BoxLayoutManager`
- `AnchoredLayoutManager`

### StretchLayoutManager

```java
import jexer.layout.StretchLayoutManager;

setLayoutManager(new StretchLayoutManager(getWidth() - 2,
        getHeight() - 2));
```

### BoxLayoutManager

```java
import jexer.layout.BoxLayoutManager;

window.setLayoutManager(new BoxLayoutManager(
    window.getWidth() - 2,
    window.getHeight() - 2,
    false
));
```

---

## 28. Menus

Jexer provides stock menus and custom menus.

### Stock menus

```java
addToolMenu();
addFileMenu();
addWindowMenu();
```

### Custom menu

```java
import jexer.menu.TMenu;

TMenu fileMenu = addMenu("&File");
fileMenu.addDefaultItem(TMenu.MID_SHELL);
fileMenu.addSeparator();
fileMenu.addDefaultItem(TMenu.MID_EXIT);
```

---

# Recommended study order

1. `TApplication`, `TWindow`, `TButton`, `TLabel`
2. `TField`, `TPasswordField`, `TInputBox`
3. `TCheckBox`, `TRadioGroup`, `TComboBox`, `TSpinner`
4. `TList`, `TTreeViewScrollable`
5. `TText`, `TEditor`
6. `TProgressBar`, `TTimer`, `TStatusBar`
7. `TTable`, `TPanel`, `TSplitPane`
8. `TImage`, `TTerminalWindow`

---

# Most useful source files to study

### Demos
- `src/demo/DemoMainWindow.java`
- `src/demo/DemoTextFieldWindow.java`
- `src/demo/DemoCheckBoxWindow.java`
- `src/demo/DemoMsgBoxWindow.java`
- `src/demo/DemoTreeViewWindow.java`
- `src/demo/DemoTableWindow.java`
- `src/demo/Demo7.java`

### Examples
- `examples/HelloWorld.java`
- `examples/MyApplication.java`
- `examples/JexerImageViewer.java`
- `examples/JexerTilingWindowManager.java`
- `examples/JexerTilingWindowManager2.java`

---

# Conclusion

If you want to learn the components for real, the most effective path is:

- read this file
- open the original demo sources from the repository
- copy one snippet at a time into a small app
- run experiments on each widget in isolation

That is exactly the learning logic of the classic Borland manuals: small steps, concrete examples, then composition into larger applications.
