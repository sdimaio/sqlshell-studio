package com.sdimaio.sqlshell.dialect.generic;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Safe fallback dialect for unsupported or not-yet-specialized databases.
 */
public final class GenericDialect implements DatabaseDialect {

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.OTHER;
    }

    @Override
    public boolean supportsExplain() {
        return false;
    }

    @Override
    public String testQuery() {
        return "SELECT 1";
    }

    @Override
    public String normalizeIdentifier(final String identifier) {
        return identifier == null ? null : identifier.trim();
    }
}
