package com.sdimaio.sqlshell.dialect.postgres;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Minimal PostgreSQL dialect scaffold.
 */
public final class PostgresDialect implements DatabaseDialect {

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.POSTGRESQL;
    }

    @Override
    public boolean supportsExplain() {
        return true;
    }

    @Override
    public String testQuery() {
        return "SELECT 1";
    }

    @Override
    public String normalizeIdentifier(final String identifier) {
        return identifier == null ? null : identifier.trim().toLowerCase();
    }
}
