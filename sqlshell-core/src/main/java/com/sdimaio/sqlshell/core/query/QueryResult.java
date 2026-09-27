package com.sdimaio.sqlshell.core.query;

import java.util.List;
import java.util.Map;

/**
 * Immutable outcome of a query execution request.
 *
 * <p>The result intentionally supports both row-returning and update-count
 * scenarios. The shape is broad enough for the first UI implementation while
 * remaining compact enough to serialize, log, or export without UI coupling.
 *
 * @author sdimaio
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
     * Returns whether the execution completed without a recorded error.
     *
     * @return true when the caller can treat the execution as successful.
     */
    public boolean successful() {
        return error == null;
    }
}
