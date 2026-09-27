package com.sdimaio.sqlshell.kernel.model.system.os;

import java.util.Locale;

/**
 * Coarse-grained operating system families.
 *
 * <p>The purpose of this enum is not to capture every distribution or desktop
 * variant. Its purpose is to support stable bootstrap decisions such as backend
 * defaults, filesystem assumptions, and capability checks.
 *
 * @author sdimaio
 */
public enum OperatingSystemType {
    LINUX,
    WINDOWS,
    MACOS,
    SOLARIS,
    BSD,
    OTHER;

    /**
     * Maps a raw JVM {@code os.name} string to a normalized family.
     *
     * @param osName raw operating system name.
     * @return normalized operating system family.
     */
    public static OperatingSystemType fromOsName(final String osName) {
        if (osName == null || osName.isBlank()) {
            return OTHER;
        }
        String normalized = osName.toLowerCase(Locale.ROOT);
        if (normalized.contains("linux")) {
            return LINUX;
        }
        if (normalized.contains("windows")) {
            return WINDOWS;
        }
        if (normalized.contains("mac") || normalized.contains("darwin")) {
            return MACOS;
        }
        if (normalized.contains("sunos") || normalized.contains("solaris")) {
            return SOLARIS;
        }
        if (normalized.contains("bsd")) {
            return BSD;
        }
        return OTHER;
    }

    /**
     * States whether the family should be treated as Unix-like for bootstrap
     * and operations decisions.
     *
     * @return true when the family is Unix-like.
     */
    public boolean isUnixLike() {
        return this == LINUX || this == MACOS || this == SOLARIS || this == BSD;
    }
}
