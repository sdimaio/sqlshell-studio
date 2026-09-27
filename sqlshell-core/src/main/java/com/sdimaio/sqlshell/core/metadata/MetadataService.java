package com.sdimaio.sqlshell.core.metadata;

import com.sdimaio.sqlshell.core.session.DatabaseSession;
import java.util.List;

/**
 * Metadata browsing contract for schemas and objects.
 */
public interface MetadataService {

    List<String> listSchemas(DatabaseSession session);

    List<DbObjectRef> listObjects(DatabaseSession session, String schema);
}
