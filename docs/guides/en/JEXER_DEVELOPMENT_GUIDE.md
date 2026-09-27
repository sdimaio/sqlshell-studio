# Jexer Development Guide

**Practical manual for building TUI applications in Java with Jexer**  
**Teaching style inspired by classic Borland Turbo Vision manuals**

> Working version: 1.2  
> Reference repository: `https://codeberg.org/AutumnMeowMeow/jexer`  
> Companion documents:
>
> - `JEXER_COMPONENT_REFERENCE.md`
> - `JEXER_COOKBOOK.md`
>
> The examples follow the Jexer 2.0 API as read from the reference repository and aim to stay close to real code.

---

## Table of Contents

- [Preface](#preface)
- [1. What Jexer Is](#1-what-jexer-is)
- [2. Mental Model](#2-mental-model)
- [3. Project Setup](#3-project-setup)
- [4. First Application](#4-first-application)
- [5. Anatomy of a Jexer App](#5-anatomy-of-a-jexer-app)
- [6. Windows](#6-windows)
- [7. Standard Widgets](#7-standard-widgets)
- [8. Menus, Shortcuts, and Status Bar](#8-menus-shortcuts-and-status-bar)
- [9. Dialog Boxes](#9-dialog-boxes)
- [10. Events](#10-events)
- [11. Data and Application State](#11-data-and-application-state)
- [12. Validation](#12-validation)
- [13. Layout and Resize](#13-layout-and-resize)
- [14. Custom Widgets](#14-custom-widgets)
- [15. Colors and Visual Theme](#15-colors-and-visual-theme)
- [16. Long Tasks and Concurrency](#16-long-tasks-and-concurrency)
- [17. Recommended Architectures](#17-recommended-architectures)
- [18. Example Application](#18-example-application)
- [19. Turbo Vision → Jexer Map](#19-turbo-vision--jexer-map)
- [20. Final Checklist](#20-final-checklist)

---

## Preface

Jexer is a Java library for building rich text user interfaces: windows, menus, dialogs, input fields, lists, buttons, and custom controls inside a terminal.

This guide is not just API documentation. Its goal is to teach you how to:

- think in terms of Jexer applications
- structure a Jexer project well
- grow a codebase without turning it into callback spaghetti
- build interfaces that are both usable and maintainable

The approach is practical and progressive, deliberately similar to classic Borland manuals.

---

## 1. What Jexer Is

Jexer is a **TUI** library, not just a `println()`-style CLI toolkit. It manages:

- logical desktop
- overlapping windows
- widget focus
- keyboard and mouse events
- screen redraw

### When to use it

Use Jexer when you want:

- terminal-based business tools
- data browsers
- structured text editors
- operational dashboards
- internal tools distributed in Java

### When not to use it

Avoid it if you need:

- pixel-based graphical UI
- web frontends
- touch-first interaction
- browser-style responsive layouts

---

## 2. Mental Model

The correct mental map is:

- **TApplication**: the application
- **TWindow**: a window
- **Widget**: a UI control
- **Events**: keyboard and mouse input
- **Model/Service**: data and business logic outside the UI

### Golden rule

The UI should **not** be your data model.

The UI should:
- display state
- gather user input
- trigger actions

The real data should live in dedicated classes.

---

## 3. Project Setup

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.gitlab.autumnmeowmeow</groupId>
        <artifactId>jexer</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### Recommended structure

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

### Useful convention

- `model`: domain data
- `service`: business logic, persistence, I/O
- `windows`: primary windows
- `dialogs`: modal interactions
- `widgets`: custom controls

---

## 4. First Application

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
        window.addLabel("Welcome to Jexer", 2, 2);
        window.addLabel("This is a real TUI window.", 2, 3);
        window.addButton("Exit", 2, 5, new TAction() {
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

### What this example shows

- the application extends `TApplication`
- a window is created with `addWindow(...)`
- widgets are added directly to the window
- a button triggers logic through a `TAction`

---

## 5. Anatomy of a Jexer App

A serious Jexer app should have at least three layers.

### 5.1 UI layer

Contains:
- windows
- dialogs
- controls
- menus

### 5.2 Logic layer

Contains:
- services
- use cases
- data transformations
- file/network/database access

### 5.3 Data layer

Contains:
- entities
- records
- configurations
- persistent state

### Example

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
        // persistence logic
    }
}
```

The window should not write files or SQL directly. It should delegate.

---

## 6. Windows

Windows are the natural container for interaction.

### 6.1 Simple window

```java
TWindow customerWindow = addWindow("Customers", 1, 1, 70, 20);
```

### 6.2 Proper use of windows

A window should represent:

- a main view
- a dedicated tool
- an editor
- an information panel

### 6.3 Dedicated window class

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

### Design note

If a window grows beyond ~200 lines of application logic, it is often time to split responsibilities.

---

## 7. Standard Widgets

### 7.1 Label

Used for static text.

```java
window.addLabel("Name:", 2, 2);
```

### 7.2 Button

```java
window.addButton("Save", 2, 10, new TAction() {
    @Override
    public void DO() {
        saveRecord();
    }
});
```

### 7.3 Field / input

```java
window.addLabel("Name:", 2, 2);
window.addLabel("Email:", 2, 4);
nameField = window.addField(15, 2, 30, false);
emailField = window.addField(15, 4, 30, false);
```

### 7.4 Checkbox

```java
activeBox = window.addCheckBox(2, 6, "Active", true);
```

### 7.5 Radio group

Use it for mutually exclusive choices.

### 7.6 List widget

Use it for:
- record lists
- file lists
- selectable items

### 7.7 Text area / editor

Use it for free multi-line text.

### Practical rule

Each window should contain a manageable number of clearly organized widgets. Too many controls destroy readability.

---

## 8. Menus, Shortcuts, and Status Bar

Rich TUI applications should be strong from the keyboard.

### 8.1 Classic menu structure

- File
- Edit
- View
- Tools
- Help

### 8.2 Useful conventions

- `Alt+X` or `Ctrl+Q`: exit
- `F1`: help
- `F2`: save
- `F3`: search/open
- `F10`: menu

### 8.3 Conceptual example

```java
addFileMenu();
addEditMenu();
addWindowMenu();
addHelpMenu();
```

### 8.4 Status bar

A well-designed status bar can show:
- key shortcuts
- current context
- short status messages

---

## 9. Dialog Boxes

Dialogs are for short, focused interactions.

### 9.1 Confirmation dialog

```java
boolean confirmed = askToDelete();
if (confirmed) {
    deleteSelected();
}
```

### 9.2 Input dialog

Use it for:
- file name
- search text
- quick rename

### 9.3 Dialog as a dedicated class

```java
public class CustomerDialog extends TWindow {
    public CustomerDialog(TApplication app) {
        super(app, "Customer", 10, 5, 50, 15, MODAL);
        addLabel("Name:", 2, 2);
        addLabel("Email:", 2, 4);
    }
}
```

### Rule

If a dialog grows too much logic, it stops being a dialog and starts becoming a real application window.

---

## 10. Events

A Jexer program lives through events.

### 10.1 Common event sources

- button click
- keypress
- focus change
- list selection
- terminal resize

### 10.2 Keep callbacks small

**Bad:**

```java
window.addButton("Save", 2, 10, new TAction() {
    @Override
    public void DO() {
        // 100 lines of logic
    }
});
```

**Better:**

```java
window.addButton("Save", 2, 10, new TAction() {
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

### 10.3 Keyboard before mouse

In Jexer, keyboard should be treated as the primary interaction channel.

---

## 11. Data and Application State

Do not read application state from widgets in a chaotic way.

### 11.1 Proper pattern

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

### 11.2 Minimal global state

Keep global only what is truly shared:
- configuration
- user session
- shared services

Avoid giant singletons.

---

## 12. Validation

Validation should exist at two levels.

### 12.1 UI level

Simple checks:
- empty field
- max length
- basic format

### 12.2 Business level

Real rules:
- valid email
- unique code
- accepted ranges
- consistency between fields

### Example

```java
private void validate(Customer c) {
    if (c.getName() == null || c.getName().isBlank()) {
        throw new IllegalArgumentException("Name is required");
    }
    if (c.getEmail() == null || !c.getEmail().contains("@")) {
        throw new IllegalArgumentException("Invalid email");
    }
}
```

---

## 13. Layout and Resize

Jexer works in text-cell coordinates.

### 13.1 Good practices

- consistent margins
- clear alignment
- homogeneous control groups
- predictable button placement

### 13.2 Resize

A serious TUI should tolerate terminal resizing.

Strategies:
- define a minimum supported size
- make some controls stretch sensibly
- avoid overlapping content when shrinking

### 13.3 Visual rule

Do not try to fill every screen cell. In a TUI, whitespace is structure.

---

## 14. Custom Widgets

When standard widgets are not enough, build one.

### 14.1 When it makes sense

- mini charts
- advanced progress monitor
- custom tables
- status panels
- special viewers

### 14.2 Conceptual widget example

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
        putStringXY(0, 0, "+------------------+");
        putStringXY(0, 1, "| " + title + ": " + value);
        putStringXY(0, 2, "+------------------+");
    }
}
```

The exact drawing primitives may vary slightly by version. The important part is the pattern:

- small internal state
- clear setter methods
- rendering separate from business logic

---

## 15. Colors and Visual Theme

A good TUI theme improves usability dramatically.

### Use color to indicate:

- focus
- error
- selection
- active/inactive state
- hierarchy

### Avoid:

- too many saturated colors
- poor contrast
- inconsistent color meaning across screens

### Recommended classic theme

If you want a Turbo-like look:
- dark or blue desktop background
- clearly marked windows
- strong focus highlight
- readable status line

---

## 16. Long Tasks and Concurrency

Never block the UI thread with long operations.

### Examples of background tasks

- directory scanning
- import/export
- remote synchronization
- large data processing

### Recommended pattern

1. show progress dialog
2. start work in background
3. update progress/status
4. close progress dialog when done

### Practical rule

If an operation lasts more than half a second, always show visible feedback.

---

## 17. Recommended Architectures

### 17.1 Small application

- 1 application class
- 1 main window
- 1 service layer

### 17.2 Medium application

- `MainApp`
- dedicated windows
- separate dialogs
- separate model and services

### 17.3 Recommended pattern

**Light MV-style:**
- Model: data
- View: Jexer UI
- Controller/Presenter: wires events to services

### Avoid:

- one giant class for everything
- business logic inside button callbacks
- file or SQL I/O spread all over the UI layer

---

## 18. Example Application

Below is a realistic skeleton.

### 18.1 Model

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

    public List<Note> all() { return notes; }
    public void add(Note note) { notes.add(note); }
    public void remove(Note note) { notes.remove(note); }
}
```

### 18.3 Main app

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

### 18.4 Main window

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
        addButton("New", 2, 20, new TAction() {
            @Override
            public void DO() {
                onNewNote();
            }
        });
        addButton("Exit", 14, 20, new TAction() {
            @Override
            public void DO() {
                app.exit();
            }
        });
    }

    private void onNewNote() {
        // open editor dialog or form
    }
}
```

This is not a finished product, but it shows the right layering.

---

## 19. Turbo Vision → Jexer Map

| Turbo Vision | Jexer |
|---|---|
| TApplication | TApplication |
| Desktop | internal desktop managed by the app |
| TWindow | TWindow |
| Dialog | dialog/modal window |
| StatusLine | status bar |
| MenuBar | menu bar |
| InputLine | field/input text |
| Button | button |
| ListBox/ListViewer | list widget |
| Event loop | Jexer event dispatch |
| Custom draw | custom widget |

### Important difference

Jexer is not a 1:1 Turbo Vision clone. It is a Java library with a similar spirit and workflow.

---

## 20. Final Checklist

Before calling a Jexer application “good”, check:

### Architecture
- [ ] UI separated from data and services
- [ ] callbacks kept small
- [ ] no file/SQL I/O scattered through UI

### Usability
- [ ] app usable entirely from keyboard
- [ ] shortcuts consistent
- [ ] focus clear
- [ ] messages readable

### Robustness
- [ ] validation exists
- [ ] resize is tolerated
- [ ] errors are shown clearly

### Appearance
- [ ] color usage is coherent
- [ ] layout is clean
- [ ] controls are aligned well

---

## Conclusion

Used correctly, Jexer lets you build rich, pleasant, and professional terminal applications. The secret is not just knowing the widgets — it is designing the application well.

The key rules are simple:

1. always separate UI and logic
2. prioritize keyboard workflow
3. treat windows and dialogs as real objects
4. validate data
5. keep callbacks small
6. build custom widgets only when truly needed

With this approach, Jexer becomes an excellent platform for utilities, internal tools, dashboards, and terminal-native applications with a classic desktop feel.
