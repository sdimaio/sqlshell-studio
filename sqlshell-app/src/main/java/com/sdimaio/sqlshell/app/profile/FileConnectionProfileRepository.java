package com.sdimaio.sqlshell.app.profile;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.connection.ConnectionProfileRepository;
import com.sdimaio.sqlshell.core.connection.DatabaseType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Local file-backed repository for connection profiles.
 *
 * <p>The first persistence strategy deliberately favors transparency over
 * sophistication. Plain properties files are easy to inspect, back up, and
 * repair manually during early product development. This keeps the first
 * vertical slice simple while preserving the repository abstraction for later
 * evolution toward stronger secret management or richer workspace storage.
 *
 * @author sdimaio
 */
public final class FileConnectionProfileRepository implements ConnectionProfileRepository {

    /**
     * Root directory that contains one properties file per profile.
     */
    private final Path profilesDirectory;

    /**
     * Creates the repository under the default user-local configuration path.
     */
    public FileConnectionProfileRepository() {
        this(defaultProfilesDirectory());
    }

    /**
     * Creates the repository under an explicit directory.
     *
     * @param profilesDirectory directory used to store profile files.
     */
    public FileConnectionProfileRepository(final Path profilesDirectory) {
        this.profilesDirectory = profilesDirectory;
        ensureDirectoryExists();
    }

    @Override
    public List<ConnectionProfile> listAll() {
        ensureDirectoryExists();
        try {
            List<ConnectionProfile> profiles = new ArrayList<>();
            try (var stream = Files.list(profilesDirectory)) {
                stream.filter(path -> path.getFileName().toString().endsWith(".properties"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> loadProfile(path).ifPresent(profiles::add));
            }
            profiles.sort(Comparator.comparing(ConnectionProfile::name, String.CASE_INSENSITIVE_ORDER));
            return profiles;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to enumerate saved connection profiles.", e);
        }
    }

    @Override
    public Optional<ConnectionProfile> findById(final String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return loadProfile(profileFile(id));
    }

    @Override
    public ConnectionProfile save(final ConnectionProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile must not be null.");
        }
        ensureDirectoryExists();

        Properties properties = new Properties();
        properties.setProperty("id", profile.id());
        properties.setProperty("name", safe(profile.name()));
        properties.setProperty("databaseType", profile.databaseType().name());
        properties.setProperty("jdbcUrl", safe(profile.jdbcUrl()));
        properties.setProperty("driverClass", safe(profile.driverClass()));
        properties.setProperty("username", safe(profile.username()));
        properties.setProperty("passwordRef", safe(profile.passwordRef()));
        properties.setProperty("defaultSchema", safe(profile.defaultSchema()));
        if (profile.properties() != null) {
            profile.properties().forEach((key, value) ->
                properties.setProperty("property." + key, value == null ? "" : value));
        }

        Path file = profileFile(profile.id());
        try (OutputStream output = Files.newOutputStream(file)) {
            properties.store(output, "SQLShell Studio connection profile");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to persist connection profile '" + profile.name() + "'.", e);
        }
        return profile;
    }

    @Override
    public void deleteById(final String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(profileFile(id));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete connection profile '" + id + "'.", e);
        }
    }

    /**
     * Ensures that the profile directory exists before file operations begin.
     *
     * <p>The repository chooses fail-fast semantics because configuration
     * storage problems are operational issues, not user-input mistakes.
     */
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(profilesDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to prepare the local profile directory: "
                + profilesDirectory, e);
        }
    }

    /**
     * Loads one profile file if it is present and parseable.
     *
     * @param file profile file path.
     * @return parsed profile, or empty when the file does not exist.
     */
    private Optional<ConnectionProfile> loadProfile(final Path file) {
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
            Map<String, String> extra = new LinkedHashMap<>();
            for (String key : properties.stringPropertyNames()) {
                if (key.startsWith("property.")) {
                    extra.put(key.substring("property.".length()), properties.getProperty(key));
                }
            }
            return Optional.of(new ConnectionProfile(
                properties.getProperty("id"),
                properties.getProperty("name"),
                DatabaseType.valueOf(properties.getProperty("databaseType", DatabaseType.OTHER.name())),
                properties.getProperty("jdbcUrl"),
                emptyToNull(properties.getProperty("driverClass")),
                emptyToNull(properties.getProperty("username")),
                emptyToNull(properties.getProperty("passwordRef")),
                emptyToNull(properties.getProperty("defaultSchema")),
                extra
            ));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load connection profile file: " + file, e);
        }
    }

    /**
     * Computes the on-disk file for one profile id.
     *
     * @param id stable profile identifier.
     * @return properties file path.
     */
    private Path profileFile(final String id) {
        return profilesDirectory.resolve(id + ".properties");
    }

    /**
     * Normalizes null references into empty strings for property persistence.
     *
     * @param value raw value.
     * @return value safe for {@link Properties} storage.
     */
    private String safe(final String value) {
        return value == null ? "" : value;
    }

    /**
     * Normalizes empty persisted values back to null.
     *
     * @param value persisted value.
     * @return null for blank values, otherwise the original string.
     */
    private String emptyToNull(final String value) {
        return value == null || value.isBlank() ? null : value;
    }

    /**
     * Resolves the default local profile directory.
     *
     * <p>The path is intentionally predictable and shell-friendly so operators
     * can inspect or back up profiles without bespoke tooling.
     *
     * @return default profile directory.
     */
    private static Path defaultProfilesDirectory() {
        return Path.of(System.getProperty("user.home"), ".config", "sqlshell-studio", "profiles");
    }
}
