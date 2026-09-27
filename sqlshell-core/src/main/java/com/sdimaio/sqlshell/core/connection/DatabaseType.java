package com.sdimaio.sqlshell.core.connection;

/**
 * Supported database families.
 *
 * <p>The enum is intentionally broader than the first implementation scope.
 * This avoids redesigning the core contract when additional JDBC dialects are
 * introduced later. A caller may therefore persist a type that is not yet
 * fully optimized by the dialect layer while still remaining inside a stable
 * domain vocabulary.
 *
 * @author sdimaio
 */
public enum DatabaseType {
    ORACLE,
    POSTGRESQL,
    MYSQL,
    SQLSERVER,
    SQLITE,
    OTHER
}
