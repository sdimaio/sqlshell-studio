package com.sdimaio.sqlshell.core.export;

import com.sdimaio.sqlshell.core.query.QueryResult;
import java.nio.file.Path;

/**
 * Result export contract.
 */
public interface ExportService {

    void export(QueryResult result, ExportFormat format, Path outputPath);
}
