package com.sdimaio.sqlshell.kernel.model.system.runtime;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

/**
 * Immutable snapshot of JVM runtime characteristics relevant to bootstrap and
 * diagnostics.
 *
 * <p>This type is deliberately lighter than a full telemetry subsystem. It is
 * meant to answer practical startup questions such as: which JVM version is
 * running, how many processors are visible, and what are the initial memory
 * envelopes.
 *
 * @author sdimaio
 */
public record JvmEnvironment(
    String vmName,
    String vmVendor,
    String vmVersion,
    long startTimeEpochMs,
    long uptimeMs,
    int availableProcessors,
    long heapUsed,
    long heapCommitted,
    long heapMax,
    int threadCount,
    int daemonThreadCount
) {

    /**
     * Captures the current JVM runtime snapshot.
     *
     * @return immutable runtime snapshot.
     */
    public static JvmEnvironment current() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        return new JvmEnvironment(
            runtime.getVmName(),
            runtime.getVmVendor(),
            runtime.getVmVersion(),
            runtime.getStartTime(),
            runtime.getUptime(),
            Runtime.getRuntime().availableProcessors(),
            memory.getHeapMemoryUsage().getUsed(),
            memory.getHeapMemoryUsage().getCommitted(),
            memory.getHeapMemoryUsage().getMax(),
            threads.getThreadCount(),
            threads.getDaemonThreadCount()
        );
    }

    /**
     * Returns the major Java version parsed from the VM version string.
     *
     * <p>The parser supports both the legacy {@code 1.x} scheme and modern
     * major-version-first strings.
     *
     * @return parsed major version, or {@code 0} when parsing fails.
     */
    public int javaMajorVersion() {
        return parseJavaMajor(vmVersion);
    }

    /**
     * Parses the Java major version from a raw version string.
     *
     * @param version raw JVM version string.
     * @return parsed major version, or {@code 0} when parsing fails.
     */
    private int parseJavaMajor(final String version) {
        if (version == null || version.isBlank()) {
            return 0;
        }
        if (version.startsWith("1.")) {
            String[] parts = version.split("\\.");
            return parts.length > 1 ? safeInt(parts[1]) : 1;
        }
        String[] parts = version.split("\\.");
        return safeInt(parts[0]);
    }

    /**
     * Best-effort integer parsing helper.
     *
     * @param text numeric-looking text.
     * @return parsed integer, or {@code 0} when parsing fails.
     */
    private int safeInt(final String text) {
        try {
            return Integer.parseInt(text.replaceAll("[^0-9].*$", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
