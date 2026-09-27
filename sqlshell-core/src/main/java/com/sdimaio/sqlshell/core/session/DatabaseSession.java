package com.sdimaio.sqlshell.core.session;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Active logical database session.
 *
 * <p>This contract is intentionally smaller than the eventual JDBC reality.
 * The application needs a stable object representing an open session, but it
 * should not become dependent on raw {@code java.sql.Connection} handles. That
 * indirection keeps the core reusable and makes it easier to evolve execution
 * mechanics later.
 *
 * @author sdimaio
 */
public interface DatabaseSession extends AutoCloseable {

    /**
     * Returns the profile that originated this session.
     *
     * @return connection profile backing the session.
     */
    ConnectionProfile profile();

    /**
     * Returns the dialect assigned to the session.
     *
     * @return vendor dialect.
     */
    DatabaseDialect dialect();

    /**
     * Returns a current metadata snapshot for the session.
     *
     * @return session information.
     */
    SessionInfo sessionInfo();

    /**
     * Closes the session and releases underlying resources.
     */
    @Override
    void close();
}
