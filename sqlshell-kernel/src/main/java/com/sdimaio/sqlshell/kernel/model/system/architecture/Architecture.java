package com.sdimaio.sqlshell.kernel.model.system.architecture;

import java.util.Locale;

/**
 * Normalized CPU architecture families.
 *
 * <p>The JVM reports architecture through vendor strings such as {@code amd64},
 * {@code x86_64}, or {@code aarch64}. A delivery-grade bootstrap layer should
 * not leak those aliases into downstream code. This enum collapses the common
 * variants into a compact, stable vocabulary suitable for startup checks,
 * diagnostics, and platform-specific resource selection.
 *
 * @author sdimaio
 */
public enum Architecture {
    X86_64,
    X86_32,
    ARM64,
    ARM32,
    PPC64,
    S390X,
    SPARC,
    UNKNOWN;

    /**
     * Normalizes a raw JVM architecture string.
     *
     * @param value raw {@code os.arch} value.
     * @return normalized architecture family.
     */
    public static Architecture from(final String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "amd64", "x86_64" -> X86_64;
            case "x86", "i386", "i486", "i586", "i686" -> X86_32;
            case "aarch64", "arm64" -> ARM64;
            case "arm", "arm32" -> ARM32;
            case "ppc64", "ppc64le" -> PPC64;
            case "s390x" -> S390X;
            case "sparc", "sparcv9" -> SPARC;
            default -> UNKNOWN;
        };
    }
}
