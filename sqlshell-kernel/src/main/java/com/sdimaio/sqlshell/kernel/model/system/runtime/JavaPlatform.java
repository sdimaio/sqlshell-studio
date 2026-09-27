package com.sdimaio.sqlshell.kernel.model.system.runtime;

import com.sdimaio.sqlshell.kernel.model.system.architecture.Architecture;
import com.sdimaio.sqlshell.kernel.model.system.os.OperatingSystem;

/**
 * Immutable composite snapshot of host operating system, architecture, and JVM.
 *
 * <p>This object exists so bootstrap and diagnostics can reason about the
 * runtime platform from one place instead of stitching together ad-hoc calls to
 * system properties and MXBeans throughout the codebase.
 *
 * @author sdimaio
 */
public final class JavaPlatform {

    /**
     * Host operating-system snapshot.
     */
    private final OperatingSystem operatingSystem;

    /**
     * Normalized architecture, repeated here for ergonomic access.
     */
    private final Architecture architecture;

    /**
     * JVM runtime snapshot.
     */
    private final JvmEnvironment jvmEnvironment;

    /**
     * Creates the composite runtime snapshot.
     *
     * @param operatingSystem host operating system.
     * @param jvmEnvironment JVM runtime snapshot.
     */
    private JavaPlatform(final OperatingSystem operatingSystem,
                         final JvmEnvironment jvmEnvironment) {
        this.operatingSystem = operatingSystem;
        this.architecture = operatingSystem.arch();
        this.jvmEnvironment = jvmEnvironment;
    }

    /**
     * Captures the current runtime platform.
     *
     * @return immutable platform snapshot.
     */
    public static JavaPlatform current() {
        return new JavaPlatform(OperatingSystem.current(), JvmEnvironment.current());
    }

    /**
     * Returns the host operating-system snapshot.
     *
     * @return operating-system snapshot.
     */
    public OperatingSystem operatingSystem() {
        return operatingSystem;
    }

    /**
     * Returns the normalized processor architecture.
     *
     * @return processor architecture.
     */
    public Architecture architecture() {
        return architecture;
    }

    /**
     * Returns the JVM runtime snapshot.
     *
     * @return JVM snapshot.
     */
    public JvmEnvironment jvmEnvironment() {
        return jvmEnvironment;
    }

    @Override
    public String toString() {
        return "JavaPlatform{" + operatingSystem + ", arch=" + architecture + ", jvm=" + jvmEnvironment.vmVersion() + "}";
    }
}
