package com.sdimaio.sqlshell.tui.app;

import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.kernel.model.system.runtime.JavaPlatform;
import com.sdimaio.sqlshell.tui.windows.MainWorkbenchWindow;
import jexer.TApplication;
import jexer.TMessageBox;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;

import java.lang.reflect.Field;

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
     * Application-specific menu id for the About dialog.
     */
    private static final int MID_ABOUT_SQLSHELL_STUDIO = 2001;

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
        addFileMenu();
        addWindowMenu();
        createHelpMenu();
        disableMenuIcons();
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
    /**
     * Handles application-specific menu actions.
     *
     * <p>The standard Jexer menu machinery already knows how to process stock
     * file and window menu items. This override exists only to customize the
     * About dialog while leaving the default behavior untouched for everything
     * else.
     *
     * @param menu menu event raised by Jexer.
     * @return true when the event was fully handled here.
     */
    @Override
    protected boolean onMenu(final TMenuEvent menu) {
        if (menu.getId() == MID_ABOUT_SQLSHELL_STUDIO) {
            messageBox("About SQLShell Studio",
                "SQLShell Studio\n"
                    + "Shell-native JDBC workbench\n\n"
                    + "Copyright (c) 2026 sdimaio\n"
                    + "All rights reserved.",
                TMessageBox.Type.OK);
            return true;
        }
        return super.onMenu(menu);
    }

    /**
     * Adds a project-specific Help menu.
     *
     * <p>The stock Jexer Help menu contains a broader help-system surface that
     * is useful for demo applications, but not yet meaningful for this product.
     * A minimal custom Help menu keeps the UX clean until a real in-application
     * help system exists.
     */
    private void createHelpMenu() {
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(MID_ABOUT_SQLSHELL_STUDIO, "&About...");
    }

    /**
     * Disables Jexer menu icons through reflective access.
     *
     * <p>The current terminal target renders the built-in emoji/icon gutter in a
     * visually degraded way. Until the product adopts a fully controlled theme
     * and terminal capability policy, plain text menus provide a cleaner and
     * more professional baseline.
     */
    private void disableMenuIcons() {
        try {
            Field useIconsField = TMenu.class.getDeclaredField("useIcons");
            useIconsField.setAccessible(true);
            for (TMenu menu : getAllMenus()) {
                useIconsField.setBoolean(menu, false);
            }
        } catch (ReflectiveOperationException ignored) {
            // The reflective access is a presentation refinement. Failing to
            // toggle menu icons must never prevent the application from starting.
        }
    }

    /**
     * Selects the most appropriate Jexer backend for the current platform.
     *
     * <p>Terminal operation is the product identity, so XTERM remains the
     * default. Swing is only selected automatically for platforms where the
     * terminal path is historically less predictable, or when the operator
     * explicitly asks for it.
     *
     * @return backend selection for the current runtime.
     */
    private static BackendType selectBackend() {
        JavaPlatform platform = JavaPlatform.current();
        BackendType backendType = BackendType.XTERM;

        if (platform.operatingSystem().type() == com.sdimaio.sqlshell.kernel.model.system.os.OperatingSystemType.WINDOWS
            || platform.operatingSystem().type() == com.sdimaio.sqlshell.kernel.model.system.os.OperatingSystemType.MACOS) {
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
