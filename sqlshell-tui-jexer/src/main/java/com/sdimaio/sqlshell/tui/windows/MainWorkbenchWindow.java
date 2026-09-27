package com.sdimaio.sqlshell.tui.windows;

import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

/**
 * Initial workbench shell for SQLShell Studio.
 *
 * <p>This first version is intentionally modest: it anchors the application
 * layout and user workflow before the real JDBC-backed panels are connected.
 */
public final class MainWorkbenchWindow extends TWindow {

    public MainWorkbenchWindow(final TApplication application) {
        super(application, "SQLShell Studio", 1, 1, 86, 26, RESIZABLE);

        addLabel("Shell-native JDBC workbench", 2, 1);
        addLabel("This is the initial workbench placeholder.", 2, 2);

        addButton("Connections", 2, 5, new TAction() {
            @Override
            public void DO() {
                messageBox("Connections", "Connection manager is not implemented yet.");
            }
        });

        addButton("Editor", 16, 5, new TAction() {
            @Override
            public void DO() {
                messageBox("Editor", "SQL editor is not implemented yet.");
            }
        });

        addButton("Results", 26, 5, new TAction() {
            @Override
            public void DO() {
                messageBox("Results", "Result grid is not implemented yet.");
            }
        });

        addButton("Exit", 2, 9, new TAction() {
            @Override
            public void DO() {
                getApplication().exit();
            }
        });

        statusBar = newStatusBar("SQLShell Studio workbench shell - development branch");
    }
}
