package com.sdimaio.sqlshell.core.query;

/**
 * Stable query failure description.
 *
 * <p>This type exists to shield the UI and higher layers from raw JDBC
 * exceptions. The application still needs SQLState, vendor code, and optional
 * position information, but it should not be forced to parse arbitrary
 * exception hierarchies in order to present a useful failure.
 *
 * @author sdimaio
 */
public record QueryError(
    String message,
    String sqlState,
    Integer vendorCode,
    Integer position,
    String exceptionClass
) {
}
