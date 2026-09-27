package com.sdimaio.sqlshell.core.metadata;

/**
 * Logical database object categories exposed by the schema browser.
 *
 * <p>The enum is intentionally broader than the first implementation so the UI
 * and service contracts do not need structural changes when more metadata is
 * introduced later.
 *
 * @author sdimaio
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
