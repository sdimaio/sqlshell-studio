package com.sdimaio.sqlshell.core.connection;

import com.sdimaio.sqlshell.core.session.DatabaseSession;

/**
 * High-level connection lifecycle contract.
 */
public interface ConnectionService {

    boolean testConnection(ConnectionProfile profile);

    DatabaseSession openSession(ConnectionProfile profile);
}
