package com.sdimaio.sqlshell.jdbc.connection;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.connection.ConnectionService;
import com.sdimaio.sqlshell.core.session.DatabaseSession;

/**
 * Placeholder JDBC connection service.
 *
 * <p>This first scaffold exists to freeze the dependency direction and the
 * public API shape before real connection lifecycle code is implemented.
 */
public final class JdbcConnectionService implements ConnectionService {

    @Override
    public boolean testConnection(final ConnectionProfile profile) {
        return false;
    }

    @Override
    public DatabaseSession openSession(final ConnectionProfile profile) {
        throw new UnsupportedOperationException("JDBC session opening is not implemented yet.");
    }
}
