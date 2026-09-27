package com.sdimaio.sqlshell.jdbc.metadata;

import com.sdimaio.sqlshell.core.metadata.DbObjectRef;
import com.sdimaio.sqlshell.core.metadata.MetadataService;
import com.sdimaio.sqlshell.core.session.DatabaseSession;

import java.util.List;

/**
 * Placeholder metadata service.
 *
 * <p>The metadata browser is not part of the first connection-management slice,
 * but the service boundary is introduced now so the future schema browser has a
 * stable contract to depend on.
 *
 * @author sdimaio
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
