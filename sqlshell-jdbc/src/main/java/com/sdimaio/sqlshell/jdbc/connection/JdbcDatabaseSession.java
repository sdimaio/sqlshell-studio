package com.sdimaio.sqlshell.jdbc.connection;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;
import com.sdimaio.sqlshell.core.session.DatabaseSession;
import com.sdimaio.sqlshell.core.session.SessionInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

/**
 * JDBC-backed implementation of {@link DatabaseSession}.
 *
 * <p>This type holds the live JDBC connection while still exposing only the
 * domain-facing session contract to higher layers. The raw connection remains
 * available to the JDBC module through {@link #jdbcConnection()} so adapters can
 * evolve without leaking JDBC into the core API.
 *
 * @author sdimaio
 */
public final class JdbcDatabaseSession implements DatabaseSession {

    /**
     * Profile that originated this session.
     */
    private final ConnectionProfile profile;

    /**
     * Dialect selected for the session.
     */
    private final DatabaseDialect dialect;

    /**
     * Live JDBC connection.
     */
    private final Connection connection;

    /**
     * Timestamp captured when the session object was created.
     */
    private final Instant connectedSince;

    /**
     * Creates a new session wrapper around an open JDBC connection.
     *
     * @param profile connection profile that opened the session.
     * @param dialect resolved database dialect.
     * @param connection live JDBC connection.
     */
    public JdbcDatabaseSession(final ConnectionProfile profile,
                               final DatabaseDialect dialect,
                               final Connection connection) {
        this.profile = profile;
        this.dialect = dialect;
        this.connection = connection;
        this.connectedSince = Instant.now();
    }

    @Override
    public ConnectionProfile profile() {
        return profile;
    }

    @Override
    public DatabaseDialect dialect() {
        return dialect;
    }

    @Override
    public SessionInfo sessionInfo() {
        return new SessionInfo(
            connectedSince,
            safeAutoCommit(),
            safeSchema(),
            Map.of("databaseType", dialect.databaseType().name())
        );
    }

    /**
     * Returns the underlying JDBC connection for adapter-side use.
     *
     * <p>This method intentionally lives only on the JDBC implementation type.
     * UI and core callers should not depend on it.
     *
     * @return live JDBC connection.
     */
    public Connection jdbcConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to close JDBC session cleanly.", e);
        }
    }

    /**
     * Reads the auto-commit flag while degrading safely when the driver fails.
     *
     * <p>The TUI benefits from a best-effort answer here. A metadata failure
     * should not collapse the whole session info panel.
     *
     * @return current auto-commit state, or {@code true} when unknown.
     */
    private boolean safeAutoCommit() {
        try {
            return connection.getAutoCommit();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Reads the current schema while tolerating drivers that do not expose it.
     *
     * @return current schema, or {@code null} when unavailable.
     */
    private String safeSchema() {
        try {
            return connection.getSchema();
        } catch (SQLException e) {
            return null;
        }
    }
}
