package com.sdimaio.sqlshell.core.session;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Active logical database session.
 *
 * <p>The core contract intentionally exposes only stable, implementation-free
 * information. JDBC handles and vendor-specific resources belong to adapter
 * layers, not to the domain-facing API.
 */
public interface DatabaseSession extends AutoCloseable {

    ConnectionProfile profile();

    DatabaseDialect dialect();

    SessionInfo sessionInfo();

    @Override
    void close();
}
