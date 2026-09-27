package com.sdimaio.sqlshell.core.history;

import java.util.List;

/**
 * Query execution history contract.
 */
public interface HistoryService {

    void record(ExecutionHistoryItem item);

    List<ExecutionHistoryItem> recent(int limit);
}
