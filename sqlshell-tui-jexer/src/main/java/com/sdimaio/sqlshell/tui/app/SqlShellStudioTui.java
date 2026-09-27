package com.sdimaio.sqlshell.tui.app;

import com.sdimaio.sqlshell.tui.windows.MainWorkbenchWindow;
import jexer.TApplication;

/**
 * Top-level Jexer application shell.
 */
public final class SqlShellStudioTui extends TApplication {

    public SqlShellStudioTui() throws Exception {
        super(selectBackend());
        addToolMenu();
        addFileMenu();
        addWindowMenu();
        new MainWorkbenchWindow(this);
    }

    private static BackendType selectBackend() {
        BackendType backendType = BackendType.XTERM;
        String os = System.getProperty("os.name", "");
        if (os.startsWith("Windows") || os.startsWith("Mac")) {
            backendType = BackendType.SWING;
        }
        if (System.getProperty("jexer.Swing") != null) {
            if (System.getProperty("jexer.Swing", "false").equals("true")) {
                backendType = BackendType.SWING;
            } else {
                backendType = BackendType.XTERM;
            }
        }
        return backendType;
    }
}
