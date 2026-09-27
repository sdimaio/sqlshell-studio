package com.sdimaio.sqlshell.core.connection;

import java.util.Map;

/**
 * Immutable description of a saved database connection profile.
 *
 * <p>The profile is kept free of UI concerns so it can be reused by the JDBC,
 * dialect, persistence, and TUI layers without dragging presentation details
 * into the core model.
 */
public record ConnectionProfile(
    String id,
    String name,
    DatabaseType databaseType,
    String jdbcUrl,
    String driverClass,
    String username,
    String passwordRef,
    String defaultSchema,
    Map<String, String> properties
) {
}
