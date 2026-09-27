package com.sdimaio.sqlshell.core.metadata;

/**
 * Logical database object categories exposed by the schema browser.
 */
public enum DbObjectType {
    CATALOG,
    SCHEMA,
    TABLE,
    VIEW,
    COLUMN,
    INDEX,
    SEQUENCE,
    FUNCTION,
    PROCEDURE,
    PACKAGE,
    OTHER
}
