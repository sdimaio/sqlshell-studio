package com.sdimaio.sqlshell.core.query;

/**
 * Stable query failure description.
 */
public record QueryError(
    String message,
    String sqlState,
    Integer vendorCode,
    Integer position,
    String exceptionClass
) {
}
