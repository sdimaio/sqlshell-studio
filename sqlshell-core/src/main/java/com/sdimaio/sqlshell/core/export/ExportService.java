package com.sdimaio.sqlshell.core.export;

import com.sdimaio.sqlshell.core.query.QueryResult;
import java.nio.file.Path;

/**
 * Result export contract.
 *
 * @author sdimaio
 */
public interface ExportService {

    /**
     * Exports a query result into the requested format.
     *
     * @param result result to export.
     * @param format target format.
     * @param outputPath target file.
     */
    void export(QueryResult result, ExportFormat format, Path outputPath);
}
