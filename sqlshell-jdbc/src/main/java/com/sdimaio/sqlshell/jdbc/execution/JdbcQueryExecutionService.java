package com.sdimaio.sqlshell.jdbc.execution;

import com.sdimaio.sqlshell.core.query.QueryExecutionService;
import com.sdimaio.sqlshell.core.query.QueryRequest;
import com.sdimaio.sqlshell.core.query.QueryResult;
import com.sdimaio.sqlshell.core.session.DatabaseSession;
import java.util.List;

/**
 * Placeholder query execution service.
 */
public final class JdbcQueryExecutionService implements QueryExecutionService {

    @Override
    public QueryResult execute(final DatabaseSession session, final QueryRequest request) {
        return new QueryResult(
            request.mode(),
            List.of(),
            List.of(),
            0L,
            0L,
            List.of("Execution engine not implemented yet."),
            0L,
            false,
            null
        );
    }
}
