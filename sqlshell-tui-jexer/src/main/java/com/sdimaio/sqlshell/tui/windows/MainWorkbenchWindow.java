package com.sdimaio.sqlshell.tui.windows;

import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import jexer.TAction;
import jexer.TApplication;
import jexer.TWindow;

/**
 * Initial workbench shell for SQLShell Studio.
 *
 * <p>This window intentionally starts small. The first objective is to expose a
 * coherent workbench entry point and one complete vertical slice, not to fake a
 * full IDE before the underlying services exist.
 *
 * @author sdimaio
 */
public final class MainWorkbenchWindow extends TWindow {

    /**
     * Repository used by the connection manager workflow.
     */
    private final ConnectionProfileRepository profileRepository;

    /**
     * Service used to validate and open JDBC sessions.
     */
    private final ConnectionService connectionService;

    /**
     * Creates the main workbench window.
     *
     * @param application parent Jexer application.
     * @param profileRepository profile repository.
     * @param connectionService connection service.
     */
    public MainWorkbenchWindow(final TApplication application,
                               final ConnectionProfileRepository profileRepository,
                               final ConnectionService connectionService) {
        super(application, "SQLShell Studio", 1, 1, 86, 26, RESIZABLE);
        this.profileRepository = profileRepository;
        this.connectionService = connectionService;

        addLabel("Shell-native JDBC workbench", 2, 1);
        addLabel("The first implementation slice focuses on connection management.", 2, 2);

        addButton("Connections", 2, 5, new TAction() {
            @Override
            public void DO() {
                new ConnectionManagerWindow(getApplication(),
                    MainWorkbenchWindow.this.profileRepository,
                    MainWorkbenchWindow.this.connectionService);
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
