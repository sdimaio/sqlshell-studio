package com.sdimaio.sqlshell.tui.windows;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.core.connection.DatabaseType;
import jexer.TAction;
import jexer.TApplication;
import jexer.TComboBox;
import jexer.TField;
import jexer.TList;
import jexer.TPasswordField;
import jexer.TWindow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Window that manages saved connection profiles and supports a first connection
 * test workflow.
 *
 * <p>This window deliberately combines profile persistence and connection
 * validation in one place for the first vertical slice. The goal is to make
 * the product useful quickly: a user can enter credentials, save a profile,
 * reload it later, and immediately test whether the JDBC path works.
 *
 * @author sdimaio
 */
public final class ConnectionManagerWindow extends TWindow {

    /**
     * Repository for persisted connection profiles.
     */
    private final ConnectionProfileRepository profileRepository;

    /**
     * Service used to validate connection profiles.
     */
    private final ConnectionService connectionService;

    /**
     * Visual list of stored profiles.
     */
    private final TList profileList;

    /**
     * Editable profile name.
     */
    private final TField nameField;

    /**
     * Database type selector.
     */
    private final TComboBox databaseTypeBox;

    /**
     * JDBC URL field.
     */
    private final TField jdbcUrlField;

    /**
     * Optional driver class field.
     */
    private final TField driverClassField;

    /**
     * Username field.
     */
    private final TField usernameField;

    /**
     * Password field.
     */
    private final TPasswordField passwordField;

    /**
     * Default schema field.
     */
    private final TField schemaField;

    /**
     * Current in-memory view of profiles in UI order.
     */
    private final List<ConnectionProfile> profiles = new ArrayList<>();

    /**
     * Creates the connection manager window.
     *
     * @param application parent application.
     * @param profileRepository profile persistence repository.
     * @param connectionService connection validation service.
     */
    public ConnectionManagerWindow(final TApplication application,
                                   final ConnectionProfileRepository profileRepository,
                                   final ConnectionService connectionService) {
        super(application, "Connection Manager", 2, 2, 92, 24, RESIZABLE);
        this.profileRepository = profileRepository;
        this.connectionService = connectionService;

        addLabel("Saved Profiles", 2, 1);
        addLabel("Name:", 30, 2);
        addLabel("DB Type:", 30, 4);
        addLabel("JDBC URL:", 30, 6);
        addLabel("Driver Class:", 30, 8);
        addLabel("Username:", 30, 10);
        addLabel("Password:", 30, 12);
        addLabel("Default Schema:", 30, 14);

        profileList = addList(List.of(), 2, 3, 22, 14,
            new TAction() {
                @Override
                public void DO() {
                    loadSelectedProfileIntoFields();
                }
            },
            new TAction() {
                @Override
                public void DO() {
                    loadSelectedProfileIntoFields();
                }
            }
        );

        nameField = addField(46, 2, 34, false, "");
        databaseTypeBox = addComboBox(46, 4, 18, databaseTypeValues(), 0, 6,
            new TAction() {
                @Override
                public void DO() {
                    // No-op in V1; kept as an explicit hook for future presets.
                }
            }
        );
        jdbcUrlField = addField(46, 6, 34, false, "");
        driverClassField = addField(46, 8, 34, false, "");
        usernameField = addField(46, 10, 24, false, "");
        passwordField = addPasswordField(46, 12, 24, false, "");
        schemaField = addField(46, 14, 20, false, "");

        addButton("New", 30, 18, new TAction() {
            @Override
            public void DO() {
                clearEditorFields();
            }
        });
        addButton("Save", 40, 18, new TAction() {
            @Override
            public void DO() {
                saveCurrentProfile();
            }
        });
        addButton("Test", 50, 18, new TAction() {
            @Override
            public void DO() {
                testCurrentProfile();
            }
        });
        addButton("Delete", 60, 18, new TAction() {
            @Override
            public void DO() {
                deleteSelectedProfile();
            }
        });
        addButton("Close", 72, 18, new TAction() {
            @Override
            public void DO() {
                getApplication().closeWindow(ConnectionManagerWindow.this);
            }
        });

        refreshProfileList();
        activate(profileList);
        statusBar = newStatusBar("Connections - save, reload, and validate JDBC profiles");
    }

    /**
     * Returns the database type labels used by the combo box.
     *
     * @return display labels matching enum names.
     */
    private List<String> databaseTypeValues() {
        List<String> values = new ArrayList<>();
        for (DatabaseType type : DatabaseType.values()) {
            values.add(type.name());
        }
        return values;
    }

    /**
     * Rebuilds the visible list of saved profiles from the repository.
     *
     * <p>The list is refreshed after every mutating operation so the UI never
     * depends on stale in-memory assumptions.
     */
    private void refreshProfileList() {
        profiles.clear();
        profiles.addAll(profileRepository.listAll());
        List<String> labels = new ArrayList<>();
        for (ConnectionProfile profile : profiles) {
            labels.add(profile.name() + " [" + profile.databaseType().name() + "]");
        }
        profileList.setList(labels);
        if (!profiles.isEmpty()) {
            profileList.setSelectedIndex(0);
            loadSelectedProfileIntoFields();
        } else {
            clearEditorFields();
        }
    }

    /**
     * Loads the selected profile into the editor fields.
     */
    private void loadSelectedProfileIntoFields() {
        int index = profileList.getSelectedIndex();
        if (index < 0 || index >= profiles.size()) {
            return;
        }
        ConnectionProfile profile = profiles.get(index);
        nameField.setText(profile.name());
        databaseTypeBox.setText(profile.databaseType().name());
        jdbcUrlField.setText(nullToEmpty(profile.jdbcUrl()));
        driverClassField.setText(nullToEmpty(profile.driverClass()));
        usernameField.setText(nullToEmpty(profile.username()));
        passwordField.setText(nullToEmpty(profile.passwordRef()));
        schemaField.setText(nullToEmpty(profile.defaultSchema()));
    }

    /**
     * Clears editor fields in preparation for a new profile.
     */
    private void clearEditorFields() {
        nameField.setText("");
        databaseTypeBox.setText(DatabaseType.POSTGRESQL.name());
        jdbcUrlField.setText("");
        driverClassField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        schemaField.setText("");
    }

    /**
     * Saves the current form state as a connection profile.
     */
    private void saveCurrentProfile() {
        try {
            ConnectionProfile profile = buildProfileFromFields();
            profileRepository.save(profile);
            refreshProfileList();
            messageBox("Connections", "Profile saved: " + profile.name());
        } catch (RuntimeException e) {
            messageBox("Connections", e.getMessage());
        }
    }

    /**
     * Validates the current form state by opening a test JDBC connection.
     */
    private void testCurrentProfile() {
        try {
            ConnectionProfile profile = buildProfileFromFields();
            boolean ok = connectionService.testConnection(profile);
            if (ok) {
                messageBox("Connection Test", "Connection successful.");
            } else {
                messageBox("Connection Test", "Connection failed. Check URL, driver, and credentials.");
            }
        } catch (RuntimeException e) {
            messageBox("Connection Test", e.getMessage());
        }
    }

    /**
     * Deletes the currently selected persisted profile.
     */
    private void deleteSelectedProfile() {
        int index = profileList.getSelectedIndex();
        if (index < 0 || index >= profiles.size()) {
            return;
        }
        try {
            ConnectionProfile profile = profiles.get(index);
            profileRepository.deleteById(profile.id());
            refreshProfileList();
            clearEditorFields();
            messageBox("Connections", "Profile deleted: " + profile.name());
        } catch (RuntimeException e) {
            messageBox("Connections", e.getMessage());
        }
    }

    /**
     * Builds an immutable profile from the editor fields.
     *
     * <p>The current implementation stores no advanced properties yet, but the
     * returned object already exposes the extension point via the properties
     * map so future tuning does not require redesigning the UI contract.
     *
     * @return connection profile derived from the UI state.
     */
    private ConnectionProfile buildProfileFromFields() {
        ConnectionProfile selected = currentSelectedProfile();
        String id = selected == null ? UUID.randomUUID().toString() : selected.id();
        return new ConnectionProfile(
            id,
            requiredField(nameField.getText(), "Profile name"),
            DatabaseType.valueOf(databaseTypeBox.getText().trim().toUpperCase()),
            requiredField(jdbcUrlField.getText(), "JDBC URL"),
            blankToNull(driverClassField.getText()),
            blankToNull(usernameField.getText()),
            blankToNull(passwordField.getText()),
            blankToNull(schemaField.getText()),
            new LinkedHashMap<>()
        );
    }

    /**
     * Returns the currently selected persisted profile when one exists.
     *
     * @return selected profile, or {@code null} when no row is selected.
     */
    private ConnectionProfile currentSelectedProfile() {
        int index = profileList.getSelectedIndex();
        if (index < 0 || index >= profiles.size()) {
            return null;
        }
        return profiles.get(index);
    }

    /**
     * Validates a required text field.
     *
     * @param value candidate value.
     * @param fieldName logical field name for diagnostics.
     * @return trimmed value.
     */
    private String requiredField(final String value, final String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    /**
     * Normalizes blank UI values back to {@code null}.
     *
     * @param value UI value.
     * @return null for blanks, otherwise the trimmed string.
     */
    private String blankToNull(final String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    /**
     * Converts nullable values into field-safe text.
     *
     * @param value nullable persisted value.
     * @return empty string for null, original string otherwise.
     */
    private String nullToEmpty(final String value) {
        return value == null ? "" : value;
    }
}
