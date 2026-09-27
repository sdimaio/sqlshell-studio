package com.sdimaio.sqlshell.app.bootstrap;

import com.sdimaio.sqlshell.kernel.model.system.runtime.JavaPlatform;

/**
 * Minimal runtime startup guard.
 *
 * <p>The Maven build already enforces Java 25, but a distributed artifact may
 * still be launched manually with the wrong JVM. This guard fails early with a
 * clear message so operators do not enter the application with a misaligned
 * runtime and discover the problem only after undefined behavior.
 *
 * @author sdimaio
 */
public final class StartupRequirements {

    /**
     * Minimum supported Java major version.
     */
    private static final int REQUIRED_JAVA_MAJOR = 25;

    /**
     * Hidden constructor because this type is a static policy utility.
     */
    private StartupRequirements() {
    }

    /**
     * Validates the current runtime platform against mandatory bootstrap rules.
     *
     * @param platform captured runtime platform.
     */
    public static void validate(final JavaPlatform platform) {
        requireJava25(platform);
    }

    /**
     * Enforces the Java 25 runtime baseline.
     *
     * @param platform captured runtime platform.
     */
    private static void requireJava25(final JavaPlatform platform) {
        int actual = platform.jvmEnvironment().javaMajorVersion();
        if (actual < REQUIRED_JAVA_MAJOR) {
            throw new IllegalStateException(
                "SQLShell Studio requires Java 25 at runtime. Detected JVM major version: " + actual);
        }
    }
}
