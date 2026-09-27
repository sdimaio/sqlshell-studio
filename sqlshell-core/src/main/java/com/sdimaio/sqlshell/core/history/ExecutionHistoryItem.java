package com.sdimaio.sqlshell.core.history;

import java.time.Instant;

/**
 * Immutable execution history entry.
 *
 * <p>The history model stores enough information for recall, support, and UI
 * display without turning the history log into a full query archive. The full
 * SQL text can be stored elsewhere later if governance and persistence policy
 * require it.
 *
 * @author sdimaio
 */
public record ExecutionHistoryItem(
    Instant timestamp,
    String connectionProfileId,
    String sqlSnippet,
    boolean success,
    Long executionTimeMillis,
    Long rowCount,
    String errorSummary
) {
}
