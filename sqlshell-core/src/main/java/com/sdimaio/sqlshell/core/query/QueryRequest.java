package com.sdimaio.sqlshell.core.query;

import java.util.Map;

/**
 * Immutable request describing a unit of SQL work.
 *
 * <p>The request bundles the text, the intended execution mode, and optional
 * control knobs such as limit, fetch size, timeout, and parameters. It is kept
 * immutable so the same request can be logged, replayed, tested, or handed to
 * multiple layers without aliasing surprises.
 *
 * @param sqlText SQL payload to execute.
 * @param mode execution mode.
 * @param limit optional row limit hint.
 * @param fetchSize optional fetch size hint.
 * @param timeoutSeconds optional statement timeout.
 * @param parameters optional parameter map for future prepared/bound modes.
 *
 * @author sdimaio
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
