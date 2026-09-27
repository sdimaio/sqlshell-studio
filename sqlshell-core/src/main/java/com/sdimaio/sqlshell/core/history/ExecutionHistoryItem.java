package com.sdimaio.sqlshell.core.history;

import java.time.Instant;

/**
 * Immutable execution history entry.
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
