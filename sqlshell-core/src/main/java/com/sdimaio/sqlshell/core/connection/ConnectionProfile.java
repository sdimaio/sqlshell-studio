package com.sdimaio.sqlshell.core.connection;

import java.util.Map;

/**
 * Immutable description of a saved database connection profile.
 *
 * <p>The profile is intentionally UI-free and transport-free. It represents a
 * durable user choice that can be consumed by the JDBC adapter, by a future
 * configuration synchronizer, or by a terminal UI without any presentation
 * leakage.
 *
 * <p>Why a record: profile state is value-like, naturally immutable at runtime,
 * and benefits from structural equality during tests and persistence
 * round-trips.
 *
 * <p>Security note: {@code passwordRef} is not defined as the clear-text secret
 * itself. The field name deliberately leaves room for future indirection such
 * as a keyring handle, encrypted blob identifier, or external secret alias.
 * The first local repository implementation may still store plain text for
 * pragmatism, but the core model avoids hard-coding that assumption.
 *
 * @param id stable profile identifier.
 * @param name human-readable profile name.
 * @param databaseType logical database family.
 * @param jdbcUrl JDBC connection URL.
 * @param driverClass optional explicit driver class name.
 * @param username login principal.
 * @param passwordRef secret reference or current password payload.
 * @param defaultSchema schema to select after login when applicable.
 * @param properties extra driver/session properties.
 *
 * @author sdimaio
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
