package com.sdimaio.sqlshell.core.metadata;

/**
 * Stable reference to a database object independent from the UI layer.
 */
public record DbObjectRef(
    DbObjectType type,
    String catalog,
    String schema,
    String name
) {
}
