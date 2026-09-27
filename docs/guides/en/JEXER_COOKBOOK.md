# Jexer Cookbook

**Practical recipes for Jexer 2.0**  
**Based on the repository:** https://codeberg.org/AutumnMeowMeow/jexer

> This collection contains ready-to-copy recipes.
> It does not replace the API documentation: it exists to show how components are actually used.

---

## Contents

1. [Hello world with stock menus](#1-hello-world-with-stock-menus)
2. [Window with exit button](#2-window-with-exit-button)
3. [Simple form with text fields](#3-simple-form-with-text-fields)
4. [Login with password field](#4-login-with-password-field)
5. [Checkbox, radio group, and combo box](#5-checkbox-radio-group-and-combo-box)
6. [Message and input dialogs](#6-message-and-input-dialogs)
7. [List with preview](#7-list-with-preview)
8. [Filesystem tree view](#8-filesystem-tree-view)
9. [Text editor](#9-text-editor)
10. [Progress bar with timer](#10-progress-bar-with-timer)
11. [File open box](#11-file-open-box)
12. [Table with text cells](#12-table-with-text-cells)
13. [Panel and BoxLayoutManager](#13-panel-and-boxlayoutmanager)
14. [Open an embedded terminal](#14-open-an-embedded-terminal)
15. [Minimal image viewer](#15-minimal-image-viewer)
16. [Resizable window with onResize](#16-resizable-window-with-onresize)
17. [Real application skeleton](#17-real-application-skeleton)

---

## 1. Hello world with stock menus

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

---

## 2. Window with exit button

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

public class ExitWindowApp extends TApplication {

    public ExitWindowApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Exit Demo", 2, 2, 40, 10);
        window.addLabel("Press the button to exit", 2, 2);
        window.addButton("Exit", 2, 4, new TAction() {
            @Override
            public void DO() {
                exit();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ExitWindowApp().run();
    }
}
```

---

## 3. Simple form with text fields

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TMessageBox;
import jexer.TWindow;

public class FormApp extends TApplication {

    private TField nameField;
    private TField emailField;

    public FormApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Customer", 2, 2, 60, 12);
        window.addLabel("Name:", 2, 2);
        window.addLabel("Email:", 2, 4);

        nameField = window.addField(15, 2, 30, false, "Mario Rossi");
        emailField = window.addField(15, 4, 30, false, "mario@example.com");

        window.addButton("Save", 2, 7, new TAction() {
            @Override
            public void DO() {
                String text = "Name=" + nameField.getText() + "\n"
                    + "Email=" + emailField.getText();
                messageBox("Form", text, TMessageBox.Type.OK);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new FormApp().run();
    }
}
```

---

## 4. Login with password field

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TPasswordField;
import jexer.TMessageBox;
import jexer.TWindow;

public class LoginApp extends TApplication {

    private TField userField;
    private TPasswordField passwordField;

    public LoginApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Login", 5, 3, 50, 12);
        window.addLabel("User:", 2, 2);
        window.addLabel("Password:", 2, 4);

        userField = window.addField(15, 2, 20, false, "admin");
        passwordField = window.addPasswordField(15, 4, 20, false);

        window.addButton("Login", 2, 7, new TAction() {
            @Override
            public void DO() {
                if ("admin".equals(userField.getText())
                    && "secret".equals(passwordField.getText())) {
                    messageBox("Login", "Access granted", TMessageBox.Type.OK);
                } else {
                    messageBox("Login", "Wrong credentials", TMessageBox.Type.OK);
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new LoginApp().run();
    }
}
```

---

## 5. Checkbox, radio group, and combo box

```java
import java.util.ArrayList;
import java.util.List;

import jexer.TAction;
import jexer.TApplication;
import jexer.TCheckBox;
import jexer.TComboBox;
import jexer.TMessageBox;
import jexer.TRadioGroup;
import jexer.TWindow;

public class OptionsApp extends TApplication {

    private TCheckBox active;
    private TRadioGroup mode;
    private TComboBox theme;

    public OptionsApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Options", 2, 2, 60, 18);

        active = window.addCheckBox(2, 2, "Active", true);

        mode = window.addRadioGroup(2, 4, "Mode");
        mode.addRadioButton("Auto");
        mode.addRadioButton("Manual", true);
        mode.addRadioButton("Off");
        mode.setRequiresSelection(true);

        List<String> values = new ArrayList<>();
        values.add("Light");
        values.add("Dark");
        values.add("Retro");

        theme = window.addComboBox(30, 4, 12, values, 2, 6,
            new TAction() {
                @Override
                public void DO() {
                    // on theme change
                }
            }
        );

        window.addButton("Show", 2, 12, new TAction() {
            @Override
            public void DO() {
                String msg = "active=" + active.isChecked()
                    + "\nmodeIndex=" + mode.getSelected()
                    + "\ntheme=" + theme.getText();
                messageBox("Values", msg, TMessageBox.Type.OK);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new OptionsApp().run();
    }
}
```

---

## 6. Message and input dialogs

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TInputBox;
import jexer.TMessageBox;
import jexer.TWindow;

public class DialogApp extends TApplication {

    public DialogApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Dialogs", 2, 2, 50, 12);

        window.addButton("Message", 2, 2, new TAction() {
            @Override
            public void DO() {
                messageBox("Info", "Operation completed", TMessageBox.Type.OK);
            }
        });

        window.addButton("Input", 2, 4, new TAction() {
            @Override
            public void DO() {
                TInputBox in = inputBox("Rename", "New name:",
                    "default.txt", TInputBox.Type.OKCANCEL);
                messageBox("Result", in.getText() + " / " + in.getResult(),
                    TMessageBox.Type.OK);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new DialogApp().run();
    }
}
```

---

## 7. List with preview

```java
import java.util.Arrays;
import java.util.List;

import jexer.TAction;
import jexer.TApplication;
import jexer.TList;
import jexer.TStatusBar;
import jexer.TWindow;

public class ListApp extends TApplication {

    private TList list;
    private TStatusBar bar;

    public ListApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "List Demo", 2, 2, 50, 15);

        List<String> items = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");

        list = window.addList(items, 2, 2, 20, 8,
            new TAction() {
                @Override
                public void DO() {
                    messageBox("Selected", list.getSelected());
                }
            },
            new TAction() {
                @Override
                public void DO() {
                    bar.setText("Current: " + list.getSelected());
                }
            }
        );

        bar = window.newStatusBar("Ready");
    }

    public static void main(String[] args) throws Exception {
        new ListApp().run();
    }
}
```

---

## 8. Filesystem tree view

```java
import java.io.IOException;

import jexer.TAction;
import jexer.TApplication;
import jexer.TDirectoryTreeItem;
import jexer.TTreeItem;
import jexer.TTreeViewScrollable;
import jexer.TWindow;

public class TreeApp extends TApplication {

    private TTreeViewScrollable tree;

    public TreeApp() throws Exception {
        super(BackendType.XTERM);

        final TWindow window = new TWindow(this, "Tree Demo", 2, 2, 50, 18);

        tree = window.addTreeViewWidget(1, 1, 40, 12,
            new TAction() {
                @Override
                public void DO() {
                    TTreeItem item = tree.getSelected();
                    messageBox("Selected", item.getText());
                }
            }
        );

        new TDirectoryTreeItem(tree, ".", true);
    }

    public static void main(String[] args) throws Exception {
        new TreeApp().run();
    }
}
```

---

## 9. Text editor

```java
import jexer.TApplication;
import jexer.TEditor;
import jexer.TWindow;

public class InlineEditorApp extends TApplication {

    public InlineEditorApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Editor", 2, 2, 60, 20);
        TEditor editor = window.addEditor("First line\nSecond line", 1, 1, 56, 14);
        editor.setText(editor.getText() + "\nThird line");
    }

    public static void main(String[] args) throws Exception {
        new InlineEditorApp().run();
    }
}
```

---

## 10. Progress bar with timer

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TProgressBar;
import jexer.TTimer;
import jexer.TWindow;

public class ProgressApp extends TApplication {

    private TProgressBar bar;
    private int value = 0;
    private TTimer timer;

    public ProgressApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Progress", 2, 2, 40, 10);
        bar = window.addProgressBar(2, 2, 20, 0, true);

        timer = addTimer(100, true, new TAction() {
            @Override
            public void DO() {
                if (value < 100) {
                    value++;
                    bar.setValue(value);
                } else {
                    timer.setRecurring(false);
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ProgressApp().run();
    }
}
```

---

## 11. File open box

```java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jexer.TAction;
import jexer.TApplication;
import jexer.TFileOpenBox;
import jexer.TWindow;

public class FileOpenApp extends TApplication {

    public FileOpenApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Open File", 2, 2, 50, 10);
        window.addButton("Open Any", 2, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    String filename = fileOpenBox(".");
                    messageBox("File", String.valueOf(filename));
                } catch (IOException e) {
                    messageBox("Error", e.getMessage());
                }
            }
        });

        window.addButton("Open Text", 2, 4, new TAction() {
            @Override
            public void DO() {
                try {
                    List<String> filters = new ArrayList<>();
                    filters.add("^.*\\.[Tt][Xx][Tt]$");
                    String filename = fileOpenBox(".", TFileOpenBox.Type.OPEN, filters);
                    messageBox("File", String.valueOf(filename));
                } catch (IOException e) {
                    messageBox("Error", e.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new FileOpenApp().run();
    }
}
```

---

## 12. Table with text cells

```java
import jexer.TApplication;
import jexer.TTable;
import jexer.TWindow;

public class TableApp extends TApplication {

    public TableApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Table", 2, 2, 60, 18, TWindow.RESIZABLE);
        TTable table = new TTable(window, 0, 0, 56, 14);

        table.setShowRowLabels(true);
        table.setShowColumnLabels(true);
        table.setColumnLabel(0, "ID");
        table.setColumnLabel(1, "Name");

        table.setCellText(0, 0, "1");
        table.setCellText(1, 0, "Mario");
        table.setCellText(0, 1, "2");
        table.setCellText(1, 1, "Luigi");
    }

    public static void main(String[] args) throws Exception {
        new TableApp().run();
    }
}
```

---

## 13. Panel and BoxLayoutManager

```java
import jexer.TApplication;
import jexer.TPanel;
import jexer.TWindow;
import jexer.layout.BoxLayoutManager;

public class PanelApp {
    public static void main(String[] args) throws Exception {
        TApplication app = new TApplication(TApplication.BackendType.XTERM);
        TWindow window = new TWindow(app, "Panels", 60, 22);
        window.setLayoutManager(new BoxLayoutManager(
            window.getWidth() - 2,
            window.getHeight() - 2,
            false
        ));

        TPanel right = window.addPanel(0, 0, 10, 10);
        right.setBorderStyle("round");
        right.setTitle("Right");

        TPanel left = window.addPanel(0, 0, 10, 10);
        left.setBorderStyle("singleVdoubleH");
        left.setTitle("Left");
        left.setTitleDirection(TPanel.Direction.BOTTOM_RIGHT);

        right.setLayoutManager(new BoxLayoutManager(right.getWidth(), right.getHeight(), true));
        left.setLayoutManager(new BoxLayoutManager(left.getWidth(), left.getHeight(), true));

        left.addText("C1", 0, 0, left.getWidth(), left.getHeight());
        left.addText("C2", 0, 0, left.getWidth(), left.getHeight());
        right.addText("C3", 0, 0, right.getWidth(), right.getHeight());

        app.run();
    }
}
```

---

## 14. Open an embedded terminal

```java
import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

public class TerminalApp extends TApplication {

    public TerminalApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Terminal Launcher", 2, 2, 50, 12);

        window.addButton("Shell", 2, 2, new TAction() {
            @Override
            public void DO() {
                openTerminal(0, 0);
            }
        });

        window.addButton("Run ls -la", 2, 4, new TAction() {
            @Override
            public void DO() {
                String[] cmd = {"bash", "-lc", "ls -la"};
                openTerminal(2, 2, TWindow.RESIZABLE, cmd, true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new TerminalApp().run();
    }
}
```

---

## 15. Minimal image viewer

```java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import jexer.TApplication;
import jexer.TWindow;

public class ImageApp extends TApplication {

    public ImageApp() throws Exception {
        super(BackendType.XTERM);

        TWindow window = new TWindow(this, "Image", 2, 2, 50, 20);
        BufferedImage image = ImageIO.read(new File("resources/logo_128.png"));
        window.addImage(0, 0, 30, 10, image, 0, 0);
    }

    public static void main(String[] args) throws Exception {
        new ImageApp().run();
    }
}
```

---

## 16. Resizable window with onResize

```java
import jexer.TApplication;
import jexer.TTable;
import jexer.TWidget;
import jexer.TWindow;
import jexer.event.TResizeEvent;

public class ResizableTableWindow extends TWindow {

    private TTable table;

    public ResizableTableWindow(TApplication app) {
        super(app, "Resizable", 0, 0, 44, 22, RESIZABLE);
        table = new TTable(this, 0, 0, 42, 20);
    }

    @Override
    public void onResize(final TResizeEvent event) {
        if (event.getType() == TResizeEvent.Type.WIDGET) {
            TResizeEvent tableSize = new TResizeEvent(event.getBackend(),
                TResizeEvent.Type.WIDGET,
                event.getWidth() - 2,
                event.getHeight() - 2);
            table.onResize(tableSize);
            return;
        }

        for (TWidget widget: getChildren()) {
            widget.onResize(event);
        }
    }
}
```

---

## 17. Real application skeleton

```java
package app;

import app.ui.MainWindow;
import jexer.TApplication;

public class MainApp extends TApplication {

    public MainApp() throws Exception {
        super(BackendType.XTERM);
        addToolMenu();
        addFileMenu();
        addWindowMenu();
        new MainWindow(this);
    }

    public static void main(String[] args) throws Exception {
        new MainApp().run();
    }
}
```

```java
package app.ui;

import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

public class MainWindow extends TWindow {

    public MainWindow(TApplication app) {
        super(app, "Main", 1, 1, 70, 20);

        addLabel("Demo app", 2, 2);
        addButton("About", 2, 4, new TAction() {
            @Override
            public void DO() {
                messageBox("About", "Jexer application skeleton");
            }
        });
        addButton("Exit", 2, 6, new TAction() {
            @Override
            public void DO() {
                getApplication().exit();
            }
        });
    }
}
```

---

# How to use this cookbook

Recommended method:

1. copy one recipe into a `.java` file
2. make it compile by itself
3. modify it gradually
4. isolate one component per study session
5. keep `JEXER_COMPONENT_REFERENCE.md` open in parallel
