package com.sdimaio.sqlshell.core.session;

import java.time.Instant;
import java.util.Map;

/**
 * Immutable snapshot of runtime session metadata.
 */
public record SessionInfo(
    Instant connectedSince,
    boolean autoCommit,
    String currentSchema,
    Map<String, String> attributes
) {
}
