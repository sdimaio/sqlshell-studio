package com.sdimaio.sqlshell.core.query;

import com.sdimaio.sqlshell.core.session.DatabaseSession;

/**
 * Query execution contract decoupled from transport, UI, and JDBC details.
 *
 * <p>This boundary makes it possible to test the UI against fake execution
 * services and to evolve statement execution policy without rewriting the TUI.
 *
 * @author sdimaio
 */
public interface QueryExecutionService {

    /**
     * Executes a request in the context of an open session.
     *
     * @param session open database session.
     * @param request query request.
     * @return execution result.
     */
    QueryResult execute(DatabaseSession session, QueryRequest request);
}
