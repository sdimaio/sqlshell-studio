package com.sdimaio.sqlshell.core.query;

/**
 * Execution mode requested by the caller.
 *
 * <p>The distinction is kept explicit because statement execution, script
 * execution, and explain-plan generation have different semantics, result
 * models, and future UI expectations.
 *
 * @author sdimaio
 */
public enum QueryMode {
    STATEMENT,
    SCRIPT,
    EXPLAIN
}
