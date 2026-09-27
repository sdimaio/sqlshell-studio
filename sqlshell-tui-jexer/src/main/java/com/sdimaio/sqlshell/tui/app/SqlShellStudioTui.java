package com.sdimaio.sqlshell.tui.app;

import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.tui.windows.MainWorkbenchWindow;
import jexer.TApplication;

/**
 * Top-level Jexer application shell.
 *
 * <p>The application object receives its core services from the outside rather
 * than creating them internally. This keeps the UI module honest: it consumes
 * services, it does not own infrastructure assembly.
 *
 * @author sdimaio
 */
public final class SqlShellStudioTui extends TApplication {

    /**
     * Repository used by the connection-management slice.
     */
    private final ConnectionProfileRepository profileRepository;

    /**
     * Service used for JDBC profile validation and session creation.
     */
    private final ConnectionService connectionService;

    /**
     * Creates the TUI shell.
     *
     * @param profileRepository profile repository.
     * @param connectionService connection service.
     * @throws Exception when the backend cannot be initialized.
     */
    public SqlShellStudioTui(final ConnectionProfileRepository profileRepository,
                             final ConnectionService connectionService) throws Exception {
        super(selectBackend());
        this.profileRepository = profileRepository;
        this.connectionService = connectionService;
        addToolMenu();
        addFileMenu();
        addWindowMenu();
        new MainWorkbenchWindow(this, this.profileRepository, this.connectionService);
    }

    /**
     * Selects the backend using a conservative platform-first policy.
     *
     * <p>Why this policy: terminal operation is the product identity, so XTERM
     * remains the default. Swing is used as a pragmatic compatibility fallback
     * on desktop-centric platforms or when explicitly requested.
     *
     * @return backend selection for the current runtime.
     */
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
