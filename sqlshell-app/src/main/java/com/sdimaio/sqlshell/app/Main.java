package com.sdimaio.sqlshell.app;

import com.sdimaio.sqlshell.tui.app.SqlShellStudioTui;

/**
 * Bootstrap entry point for SQLShell Studio.
 */
public final class Main {

    private Main() {
    }

    public static void main(final String[] args) throws Exception {
        new SqlShellStudioTui().run();
    }
}
