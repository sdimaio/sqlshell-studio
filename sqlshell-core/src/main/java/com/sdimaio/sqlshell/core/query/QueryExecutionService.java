package com.sdimaio.sqlshell.core.query;

import com.sdimaio.sqlshell.core.session.DatabaseSession;

/**
 * Query execution contract decoupled from transport, UI, and JDBC details.
 */
public interface QueryExecutionService {

    QueryResult execute(DatabaseSession session, QueryRequest request);
}
