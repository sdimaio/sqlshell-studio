package com.sdimaio.sqlshell.dialect.api;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;
import com.sdimaio.sqlshell.dialect.generic.GenericDialect;
import com.sdimaio.sqlshell.dialect.oracle.OracleDialect;
import com.sdimaio.sqlshell.dialect.postgres.PostgresDialect;
import java.util.Map;

/**
 * Small in-memory registry for built-in dialects.
 */
public final class DialectRegistry {

    private final Map<DatabaseType, DatabaseDialect> dialects = Map.of(
        DatabaseType.ORACLE, new OracleDialect(),
        DatabaseType.POSTGRESQL, new PostgresDialect(),
        DatabaseType.OTHER, new GenericDialect()
    );

    public DatabaseDialect resolve(final DatabaseType databaseType) {
        return dialects.getOrDefault(databaseType, dialects.get(DatabaseType.OTHER));
    }
}
