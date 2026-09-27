package com.sdimaio.sqlshell.jdbc.connection;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;
import com.sdimaio.sqlshell.core.dialect.DialectResolver;
import com.sdimaio.sqlshell.core.session.DatabaseSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC implementation of the connection lifecycle contract.
 *
 * <p>The first responsibility of this class is intentionally narrow: validate a
 * profile, open a live connection, and wrap it in a logical session. Query
 * execution belongs elsewhere. Keeping the responsibilities separated prevents
 * the connection layer from becoming a kitchen-sink adapter too early.
 *
 * @author sdimaio
 */
public final class JdbcConnectionService implements ConnectionService {

    /**
     * Registry used to resolve vendor-specific behavior for each session.
     */
    private final DialectResolver dialectResolver;

    /**
     * Creates the service with the default built-in dialect registry.
     */
    public JdbcConnectionService(final DialectResolver dialectResolver) {
        this.dialectResolver = dialectResolver;
    }

    @Override
    public boolean testConnection(final ConnectionProfile profile) {
        requireProfile(profile);
        loadDriverIfPresent(profile);
        try (Connection connection = openJdbcConnection(profile)) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public DatabaseSession openSession(final ConnectionProfile profile) {
        requireProfile(profile);
        loadDriverIfPresent(profile);
        try {
            Connection connection = openJdbcConnection(profile);
            DatabaseDialect dialect = dialectResolver.resolve(profile.databaseType());
            return new JdbcDatabaseSession(profile, dialect, connection);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to open JDBC session for profile '"
                + profile.name() + "'.", e);
        }
    }

    /**
     * Performs minimal profile validation before any JDBC interaction.
     *
     * <p>The checks are intentionally basic. The goal is to reject malformed
     * inputs early so lower-level failures carry operational meaning instead of
     * avoidable null/blank noise.
     *
     * @param profile profile to validate.
     */
    private void requireProfile(final ConnectionProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Connection profile must not be null.");
        }
        if (profile.jdbcUrl() == null || profile.jdbcUrl().isBlank()) {
            throw new IllegalArgumentException("JDBC URL must not be blank.");
        }
    }

    /**
     * Loads the configured JDBC driver class when explicitly supplied.
     *
     * <p>Why this is optional: modern JDBC drivers frequently self-register via
     * the service provider mechanism. The explicit load remains useful for
     * older driver packaging layouts and for environments where operational
     * predictability is preferred over implicit registration.
     *
     * @param profile connection profile that may declare a driver class.
     */
    private void loadDriverIfPresent(final ConnectionProfile profile) {
        if (profile.driverClass() == null || profile.driverClass().isBlank()) {
            return;
        }
        try {
            Class.forName(profile.driverClass());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Configured JDBC driver class was not found: "
                + profile.driverClass(), e);
        }
    }

    /**
     * Opens a raw JDBC connection from a profile.
     *
     * <p>Properties beyond username and password are preserved because many
     * enterprise JDBC stacks rely on driver-specific flags for SSL, networking,
     * tracing, or schema defaults.
     *
     * @param profile connection profile.
     * @return open JDBC connection.
     * @throws SQLException when the driver rejects or fails the connection.
     */
    private Connection openJdbcConnection(final ConnectionProfile profile) throws SQLException {
        Properties properties = new Properties();
        if (profile.username() != null) {
            properties.setProperty("user", profile.username());
        }
        if (profile.passwordRef() != null) {
            properties.setProperty("password", profile.passwordRef());
        }
        if (profile.properties() != null) {
            profile.properties().forEach(properties::setProperty);
        }
        return DriverManager.getConnection(profile.jdbcUrl(), properties);
    }
}
