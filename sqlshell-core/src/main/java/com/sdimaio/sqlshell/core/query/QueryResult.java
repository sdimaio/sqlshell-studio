package com.sdimaio.sqlshell.core.query;

import java.util.List;
import java.util.Map;

/**
 * Immutable outcome of a query execution request.
 */
public record QueryResult(
    QueryMode executionMode,
    List<ColumnMetadata> columns,
    List<Map<String, Object>> rows,
    Long rowCount,
    Long updateCount,
    List<String> warnings,
    Long executionTimeMillis,
    boolean truncated,
    QueryError error
) {

    /**
     * Returns true when the result completed without a recorded error.
     *
     * @return true when the execution succeeded from the caller point of view.
     */
    public boolean successful() {
        return error == null;
    }
}
