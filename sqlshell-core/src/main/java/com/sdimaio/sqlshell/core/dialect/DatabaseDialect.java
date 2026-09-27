package com.sdimaio.sqlshell.core.dialect;

import com.sdimaio.sqlshell.core.connection.DatabaseType;

/**
 * Contract for vendor-specific SQL and metadata behavior.
 *
 * <p>JDBC gives a common transport, not a common product model. Object naming,
 * explain-plan support, session discovery, and DDL extraction all diverge
 * across vendors. This abstraction prevents those differences from leaking
 * directly into the TUI layer or into generic JDBC execution code.
 *
 * @author sdimaio
 */
public interface DatabaseDialect {

    /**
     * Returns the database family served by this dialect.
     *
     * @return database family.
     */
    DatabaseType databaseType();

    /**
     * States whether this vendor offers a supported explain workflow.
     *
     * @return {@code true} when explain is supported by this dialect layer.
     */
    boolean supportsExplain();

    /**
     * Returns a minimal query suitable for connection testing.
     *
     * @return vendor-appropriate test query.
     */
    String testQuery();

    /**
     * Normalizes a logical identifier according to vendor rules.
     *
     * @param identifier raw identifier.
     * @return normalized identifier.
     */
    String normalizeIdentifier(String identifier);
}
