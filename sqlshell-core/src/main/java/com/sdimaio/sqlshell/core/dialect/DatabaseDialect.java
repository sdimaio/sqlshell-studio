package com.sdimaio.sqlshell.core.dialect;

import com.sdimaio.sqlshell.core.connection.DatabaseType;

/**
 * Contract for vendor-specific SQL and metadata behavior.
 */
public interface DatabaseDialect {

    DatabaseType databaseType();

    boolean supportsExplain();

    String testQuery();

    String normalizeIdentifier(String identifier);
}
