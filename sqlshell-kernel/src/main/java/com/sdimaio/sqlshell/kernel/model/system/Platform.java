package com.sdimaio.sqlshell.kernel.model.system;

import com.sdimaio.sqlshell.kernel.model.system.architecture.Architecture;
import com.sdimaio.sqlshell.kernel.model.system.os.OperatingSystem;

import java.util.Objects;

/**
 * Compact immutable platform descriptor composed of operating system and
 * architecture.
 *
 * <p>This smaller view intentionally omits JVM runtime metrics. It is useful
 * when the caller only needs deployment-asset or compatibility classification
 * without the heavier runtime snapshot.
 *
 * @author sdimaio
 */
public final class Platform {

    /**
     * Host operating-system snapshot.
     */
    private final OperatingSystem operatingSystem;

    /**
     * Creates the compact platform descriptor.
     *
     * @param operatingSystem operating-system snapshot.
     */
    private Platform(final OperatingSystem operatingSystem) {
        this.operatingSystem = Objects.requireNonNull(operatingSystem, "operatingSystem");
    }

    /**
     * Captures the current host platform.
     *
     * @return compact platform descriptor.
     */
    public static Platform current() {
        return new Platform(OperatingSystem.current());
    }

    /**
     * Returns the operating-system snapshot.
     *
     * @return operating-system snapshot.
     */
    public OperatingSystem operatingSystem() {
        return operatingSystem;
    }

    /**
     * Returns the normalized architecture.
     *
     * @return processor architecture.
     */
    public Architecture architecture() {
        return operatingSystem.arch();
    }

    /**
     * Returns a stable subdirectory key of the form {@code os/arch}.
     *
     * <p>This mirrors the kind of resource layout often used for platform-
     * specific scripts or native binaries.
     *
     * @return lower-case resource subdirectory key.
     */
    public String subdirectoryKey() {
        return operatingSystem.type().name().toLowerCase() + "/"
            + operatingSystem.arch().name().toLowerCase();
    }

    @Override
    public String toString() {
        return "Platform{" + operatingSystem.type() + ", arch=" + operatingSystem.arch() + "}";
    }
}
