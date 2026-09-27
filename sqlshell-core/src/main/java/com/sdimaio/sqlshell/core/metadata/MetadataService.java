package com.sdimaio.sqlshell.core.metadata;

import com.sdimaio.sqlshell.core.session.DatabaseSession;
import java.util.List;

/**
 * Metadata browsing contract for schemas and objects.
 *
 * <p>The service stays intentionally compact in the first implementation.
 * Better to offer a small stable surface and grow it carefully than to freeze
 * a large metadata API before the TUI workflows prove what is actually needed.
 *
 * @author sdimaio
 */
public interface MetadataService {

    /**
     * Lists schemas visible from the current session.
     *
     * @param session open database session.
     * @return visible schemas.
     */
    List<String> listSchemas(DatabaseSession session);

    /**
     * Lists objects visible inside one schema.
     *
     * @param session open database session.
     * @param schema schema to inspect.
     * @return object references for the selected schema.
     */
    List<DbObjectRef> listObjects(DatabaseSession session, String schema);
}
