package com.sdimaio.sqlshell.core.history;

import java.util.List;

/**
 * Query execution history contract.
 *
 * @author sdimaio
 */
public interface HistoryService {

    /**
     * Records one execution event.
     *
     * @param item history item to persist.
     */
    void record(ExecutionHistoryItem item);

    /**
     * Returns the most recent execution items.
     *
     * @param limit maximum number of entries to return.
     * @return most recent items.
     */
    List<ExecutionHistoryItem> recent(int limit);
}
