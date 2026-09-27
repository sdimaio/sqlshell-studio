package com.sdimaio.sqlshell.dialect.api;

import com.sdimaio.sqlshell.core.connection.DatabaseType;
import com.sdimaio.sqlshell.core.dialect.DatabaseDialect;
import com.sdimaio.sqlshell.core.dialect.DialectResolver;
import com.sdimaio.sqlshell.dialect.generic.GenericDialect;
import com.sdimaio.sqlshell.dialect.oracle.OracleDialect;
import com.sdimaio.sqlshell.dialect.postgres.PostgresDialect;

import java.util.Map;

/**
 * Small in-memory registry for built-in dialects.
 *
 * <p>The registry is intentionally simple in the first revision because the
 * initial goal is deterministic dialect selection, not dynamic plugin loading.
 * A plain map is enough until extension mechanics become a real product need.
 *
 * @author sdimaio
 */
public final class DialectRegistry implements DialectResolver {

    /**
     * Built-in dialect set indexed by database family.
     */
    private final Map<DatabaseType, DatabaseDialect> dialects = Map.of(
        DatabaseType.ORACLE, new OracleDialect(),
        DatabaseType.POSTGRESQL, new PostgresDialect(),
        DatabaseType.OTHER, new GenericDialect()
    );

    /**
     * Resolves a dialect for the requested database family.
     *
     * @param databaseType requested family.
     * @return matching dialect, or the generic fallback dialect.
     */
    @Override
    public DatabaseDialect resolve(final DatabaseType databaseType) {
        return dialects.getOrDefault(databaseType, dialects.get(DatabaseType.OTHER));
    }
}
