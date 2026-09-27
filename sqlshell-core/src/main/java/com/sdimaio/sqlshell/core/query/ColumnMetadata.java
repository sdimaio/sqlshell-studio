package com.sdimaio.sqlshell.core.query;

/**
 * Column description for tabular query results.
 */
public record ColumnMetadata(
    String name,
    int jdbcType,
    String databaseTypeName,
    boolean nullable,
    Integer size,
    Integer scale
) {
}
