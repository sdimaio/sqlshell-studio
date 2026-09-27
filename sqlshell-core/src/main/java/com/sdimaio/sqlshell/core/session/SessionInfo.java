package com.sdimaio.sqlshell.core.session;

import java.time.Instant;
import java.util.Map;

/**
 * Immutable snapshot of observable session metadata.
 *
 * <p>The snapshot deliberately captures only information that is meaningful to
 * higher layers: time of connection, auto-commit state, current schema, and a
 * vendor-neutral attribute map. Low-level resources such as JDBC connections
 * remain outside the record because they are lifecycle-bearing implementation
 * details, not stable domain state.
 *
 * @param connectedSince session creation timestamp.
 * @param autoCommit effective auto-commit state.
 * @param currentSchema current database schema when available.
 * @param attributes additional implementation-defined session attributes.
 *
 * @author sdimaio
 */
public record SessionInfo(
    Instant connectedSince,
    boolean autoCommit,
    String currentSchema,
    Map<String, String> attributes
) {
}
