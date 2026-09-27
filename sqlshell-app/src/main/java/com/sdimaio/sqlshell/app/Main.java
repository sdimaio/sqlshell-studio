package com.sdimaio.sqlshell.app;

import com.sdimaio.sqlshell.app.bootstrap.StartupDiagnostics;
import com.sdimaio.sqlshell.app.bootstrap.StartupRequirements;
import com.sdimaio.sqlshell.app.profile.FileConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.dialect.api.DialectRegistry;
import com.sdimaio.sqlshell.jdbc.connection.JdbcConnectionService;
import com.sdimaio.sqlshell.kernel.model.system.runtime.JavaPlatform;
import com.sdimaio.sqlshell.tui.app.SqlShellStudioTui;

/**
 * Bootstrap entry point for SQLShell Studio.
 *
 * <p>The entry point currently performs explicit wiring rather than relying on
 * a dependency-injection framework. That choice keeps the initial runtime small
 * and makes the first architecture slice easier to reason about while the
 * module boundaries settle.
 *
 * @author sdimaio
 */
public final class Main {

    /**
     * Hidden constructor because this type is an entry-point holder, not a
     * runtime object.
     */
    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments.
     * @throws Exception when the TUI backend cannot be initialized.
     */
    public static void main(final String[] args) throws Exception {
        JavaPlatform platform = JavaPlatform.current();
        StartupRequirements.validate(platform);
        StartupDiagnostics.printBanner(platform);

        ConnectionProfileRepository profileRepository = new FileConnectionProfileRepository();
        ConnectionService connectionService = new JdbcConnectionService(new DialectRegistry());
        new SqlShellStudioTui(profileRepository, connectionService).run();
    }
}
