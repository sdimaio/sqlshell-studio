package com.sdimaio.sqlshell.dialect.postgres;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Minimal PostgreSQL dialect scaffold.
 *
 * <p>PostgreSQL is a first-class target of the initial roadmap, so the dialect
 * is introduced immediately even though its current implementation remains
 * intentionally small.
 *
 * @author sdimaio
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
