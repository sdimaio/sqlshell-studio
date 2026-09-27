package com.sdimaio.sqlshell.core.connection;

import com.sdimaio.sqlshell.core.session.DatabaseSession;

/**
 * High-level connection lifecycle contract.
 *
 * <p>This abstraction isolates the rest of the application from the mechanics
 * of driver loading, low-level JDBC exceptions, and vendor-specific session
 * bootstrap. The UI should only ask whether a profile is usable and whether a
 * logical session can be opened.
 *
 * @author sdimaio
 */
public interface ConnectionService {

    /**
     * Validates that the supplied profile can establish a database connection.
     *
     * <p>The method is intentionally boolean in the first revision to simplify
     * the first UI slice. Richer diagnostics can later be modeled explicitly
     * without changing the repository or dialect boundaries.
     *
     * @param profile profile to validate.
     * @return {@code true} when the connection succeeds.
     */
    boolean testConnection(ConnectionProfile profile);

    /**
     * Opens a logical database session.
     *
     * @param profile profile to open.
     * @return opened session.
     */
    DatabaseSession openSession(ConnectionProfile profile);
}
