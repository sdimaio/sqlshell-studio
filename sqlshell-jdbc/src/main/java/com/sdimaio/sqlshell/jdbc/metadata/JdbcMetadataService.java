package com.sdimaio.sqlshell.jdbc.metadata;

import com.sdimaio.sqlshell.core.metadata.DbObjectRef;
import com.sdimaio.sqlshell.core.metadata.MetadataService;
import com.sdimaio.sqlshell.core.session.DatabaseSession;
import java.util.List;

/**
 * Placeholder metadata service.
 */
public final class JdbcMetadataService implements MetadataService {

    @Override
    public List<String> listSchemas(final DatabaseSession session) {
        return List.of();
    }

    @Override
    public List<DbObjectRef> listObjects(final DatabaseSession session, final String schema) {
        return List.of();
    }
}
