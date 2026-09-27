package com.sdimaio.sqlshell.dialect.oracle;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;

/**
 * Minimal Oracle dialect scaffold.
 *
 * <p>Oracle deserves an explicit dialect even in the first revision because its
 * baseline test query and identifier conventions differ from the generic SQL
 * assumptions used by PostgreSQL-style engines.
 *
 * @author sdimaio
 */
public final class OracleDialect implements DatabaseDialect {

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.ORACLE;
    }

    @Override
    public boolean supportsExplain() {
        return true;
    }

    @Override
    public String testQuery() {
        return "SELECT 1 FROM DUAL";
    }

    @Override
    public String normalizeIdentifier(final String identifier) {
        return identifier == null ? null : identifier.trim().toUpperCase();
    }
}
