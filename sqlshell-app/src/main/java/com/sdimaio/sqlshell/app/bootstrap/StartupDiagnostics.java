package com.sdimaio.sqlshell.app.bootstrap;

import com.sdimaio.sqlshell.kernel.model.system.runtime.JavaPlatform;

/**
 * Writes a concise startup diagnostics banner.
 *
 * <p>The immediate value is operational clarity: when the tool starts from a
 * shell, the operator can see at once which OS family, architecture, and JVM
 * were detected before any higher-level workflow begins.
 *
 * @author sdimaio
 */
public final class StartupDiagnostics {

    /**
     * Hidden constructor because this type is a pure diagnostics utility.
     */
    private StartupDiagnostics() {
    }

    /**
     * Emits a human-readable startup summary to the standard output stream.
     *
     * @param platform captured runtime platform.
     */
    public static void printBanner(final JavaPlatform platform) {
        System.out.println("[SQLShell Studio] Starting on " + platform.operatingSystem().type()
            + " / " + platform.architecture()
            + " using JVM " + platform.jvmEnvironment().vmName()
            + " " + platform.jvmEnvironment().vmVersion());
    }
}
