package com.sdimaio.sqlshell.core.dialect;

import com.sdimaio.sqlshell.core.connection.DatabaseType;

/**
 * Resolves a concrete dialect for a logical database family.
 *
 * <p>The resolver exists as a tiny abstraction so infrastructure adapters can
 * depend on a stable contract without importing implementation-side registry
 * classes from the dialect module.
 *
 * @author sdimaio
 */
public interface DialectResolver {

    /**
     * Resolves a dialect for the requested database family.
     *
     * @param databaseType requested family.
     * @return matching dialect.
     */
    DatabaseDialect resolve(DatabaseType databaseType);
}
