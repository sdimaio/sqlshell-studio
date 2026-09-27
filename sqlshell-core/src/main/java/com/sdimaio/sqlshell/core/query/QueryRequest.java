package com.sdimaio.sqlshell.core.query;

import java.util.Map;

/**
 * Immutable request describing a unit of SQL work.
 */
public record QueryRequest(
    String sqlText,
    QueryMode mode,
    Integer limit,
    Integer fetchSize,
    Integer timeoutSeconds,
    Map<String, Object> parameters
) {
}
