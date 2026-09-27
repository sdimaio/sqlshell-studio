package com.sdimaio.sqlshell.core.query;

/**
 * Column description for tabular query results.
 *
 * <p>The metadata is deliberately simple in the first revision: enough to draw
 * result grids and reason about formatting without prematurely encoding every
 * vendor-specific detail.
 *
 * @author sdimaio
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
