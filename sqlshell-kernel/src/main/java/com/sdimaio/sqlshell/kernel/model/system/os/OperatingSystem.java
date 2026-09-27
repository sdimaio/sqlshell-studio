package com.sdimaio.sqlshell.kernel.model.system.os;

import com.sdimaio.sqlshell.kernel.model.system.architecture.Architecture;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable operating-system snapshot with semantic capabilities.
 *
 * <p>Downstream code should branch on capabilities whenever possible instead of
 * spreading ad-hoc vendor-string checks across the codebase. This improves
 * portability, keeps startup behavior auditable, and makes unit tests easier to
 * write because the platform becomes a plain immutable value object.
 *
 * @author sdimaio
 */
public final class OperatingSystem {

    /**
     * Stable semantic capabilities exposed by the host platform.
     */
    public enum Capability {
        POSIX,
        PROCFS,
        SYMLINKS,
        CASE_SENSITIVE_FS,
        SYSTEMD,
        SMF
    }

    /**
     * Normalized OS family.
     */
    private final OperatingSystemType type;

    /**
     * Raw JVM-reported OS name.
     */
    private final String name;

    /**
     * Raw JVM-reported OS version.
     */
    private final String version;

    /**
     * Normalized processor architecture.
     */
    private final Architecture architecture;

    /**
     * Best-effort Linux distribution label.
     */
    private final Optional<String> distribution;

    /**
     * Semantic capability set for the host platform.
     */
    private final EnumSet<Capability> capabilities;

    /**
     * Creates an immutable operating-system snapshot.
     *
     * @param type normalized operating-system family.
     * @param name raw JVM-reported name.
     * @param version raw JVM-reported version.
     * @param architecture normalized architecture.
     * @param distribution best-effort Linux distribution label.
     * @param capabilities semantic capability set.
     */
    private OperatingSystem(final OperatingSystemType type,
                            final String name,
                            final String version,
                            final Architecture architecture,
                            final Optional<String> distribution,
                            final EnumSet<Capability> capabilities) {
        this.type = Objects.requireNonNull(type, "type");
        this.name = Objects.requireNonNullElse(name, "");
        this.version = Objects.requireNonNullElse(version, "");
        this.architecture = Objects.requireNonNullElse(architecture, Architecture.UNKNOWN);
        this.distribution = distribution == null ? Optional.empty() : distribution;
        this.capabilities = capabilities == null
            ? EnumSet.noneOf(Capability.class)
            : EnumSet.copyOf(capabilities);
    }

    /**
     * Captures the current host operating system.
     *
     * <p>The implementation intentionally uses cheap file probes and JVM system
     * properties only. Bootstrap code should remain predictable and should not
     * depend on spawning helper processes just to understand the environment it
     * is starting in.
     *
     * @return immutable host operating-system snapshot.
     */
    public static OperatingSystem current() {
        String osName = System.getProperty("os.name", "");
        String osVersion = System.getProperty("os.version", "");
        OperatingSystemType type = OperatingSystemType.fromOsName(osName);
        Architecture architecture = Architecture.from(System.getProperty("os.arch", ""));
        return new OperatingSystem(
            type,
            osName,
            osVersion,
            architecture,
            detectDistribution(type),
            detectCapabilities(type)
        );
    }

    /**
     * Returns the normalized operating-system family.
     *
     * @return OS family.
     */
    public OperatingSystemType type() {
        return type;
    }

    /**
     * Returns the raw JVM-reported operating-system name.
     *
     * @return OS name.
     */
    public String name() {
        return name;
    }

    /**
     * Returns the raw JVM-reported operating-system version.
     *
     * @return OS version.
     */
    public String version() {
        return version;
    }

    /**
     * Returns the normalized processor architecture.
     *
     * @return processor architecture.
     */
    public Architecture arch() {
        return architecture;
    }

    /**
     * Returns the best-effort Linux distribution label.
     *
     * @return optional distribution label.
     */
    public Optional<String> distribution() {
        return distribution;
    }

    /**
     * States whether a capability is present.
     *
     * @param capability capability to check.
     * @return true when present.
     */
    public boolean has(final Capability capability) {
        return capabilities.contains(capability);
    }

    /**
     * Returns a defensive copy of the capability set.
     *
     * @return host capability set.
     */
    public EnumSet<Capability> capabilities() {
        return EnumSet.copyOf(capabilities);
    }

    /**
     * Returns whether the host platform is Unix-like.
     *
     * @return true for Unix-like families.
     */
    public boolean isUnixLike() {
        return type.isUnixLike();
    }

    @Override
    public String toString() {
        return "OperatingSystem{" + type + ", version='" + version + "', arch=" + architecture
            + distribution.map(value -> ", distribution='" + value + "'").orElse("") + "}";
    }

    /**
     * Derives capabilities from a coarse OS family using stable file-system
     * probes where useful.
     *
     * @param type OS family.
     * @return capability set.
     */
    private static EnumSet<Capability> detectCapabilities(final OperatingSystemType type) {
        EnumSet<Capability> result = EnumSet.noneOf(Capability.class);
        if (type.isUnixLike()) {
            result.add(Capability.POSIX);
            result.add(Capability.SYMLINKS);
            result.add(Capability.CASE_SENSITIVE_FS);
        }
        if (Files.isDirectory(Path.of("/proc"))) {
            result.add(Capability.PROCFS);
        }
        if (type == OperatingSystemType.LINUX && Files.exists(Path.of("/run/systemd/system"))) {
            result.add(Capability.SYSTEMD);
        }
        if (type == OperatingSystemType.SOLARIS) {
            result.add(Capability.SMF);
        }
        if (type == OperatingSystemType.WINDOWS) {
            result.remove(Capability.CASE_SENSITIVE_FS);
            result.remove(Capability.POSIX);
            result.remove(Capability.SYMLINKS);
        }
        return result;
    }

    /**
     * Attempts to derive a friendly Linux distribution label.
     *
     * @param type OS family.
     * @return distribution label when available.
     */
    private static Optional<String> detectDistribution(final OperatingSystemType type) {
        if (type != OperatingSystemType.LINUX) {
            return Optional.empty();
        }
        Path osRelease = Path.of("/etc/os-release");
        if (!Files.isRegularFile(osRelease)) {
            return Optional.empty();
        }
        try {
            for (String line : Files.readAllLines(osRelease)) {
                if (line.startsWith("PRETTY_NAME=")) {
                    String value = line.substring("PRETTY_NAME=".length()).trim();
                    value = value.replaceAll("^\"|\"$", "");
                    return value.isBlank() ? Optional.empty() : Optional.of(value);
                }
            }
        } catch (Exception ignored) {
            // Best-effort detection only. Bootstrap must not fail because the
            // distribution label could not be read.
        }
        return Optional.empty();
    }
}
