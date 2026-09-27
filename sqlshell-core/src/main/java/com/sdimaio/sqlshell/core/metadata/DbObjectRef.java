package com.sdimaio.sqlshell.core.metadata;

/**
 * Stable reference to a database object independent from the UI layer.
 *
 * @author sdimaio
 */
public record DbObjectRef(
    DbObjectType type,
    String catalog,
    String schema,
    String name
) {
}
