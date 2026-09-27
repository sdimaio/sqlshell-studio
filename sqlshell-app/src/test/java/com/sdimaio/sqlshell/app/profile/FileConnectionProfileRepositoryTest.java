package com.sdimaio.sqlshell.app.profile;

import com.sdimaio.sqlshell.core.connection.ConnectionProfile;
import com.sdimaio.sqlshell.core.connection.DatabaseType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the first local connection-profile repository implementation.
 *
 * @author sdimaio
 */
class FileConnectionProfileRepositoryTest {

    /**
     * Temporary directory injected by JUnit for isolated repository testing.
     */
    @TempDir
    Path tempDir;

    /**
     * Confirms that a saved profile can be reloaded without structural loss.
     */
    @Test
    void saveAndReloadRoundTripPreservesProfileShape() {
        FileConnectionProfileRepository repository = new FileConnectionProfileRepository(tempDir);
        ConnectionProfile profile = new ConnectionProfile(
            "local-postgres",
            "Local Postgres",
            DatabaseType.POSTGRESQL,
            "jdbc:postgresql://localhost:5432/postgres",
            null,
            "postgres",
            "secret",
            "public",
            Map.of("sslmode", "disable")
        );

        repository.save(profile);

        assertTrue(repository.findById("local-postgres").isPresent());
        ConnectionProfile loaded = repository.findById("local-postgres").orElseThrow();
        assertEquals(profile, loaded);
        assertEquals(1, repository.listAll().size());
    }

    /**
     * Confirms that deletion removes the stored profile cleanly.
     */
    @Test
    void deleteRemovesStoredProfile() {
        FileConnectionProfileRepository repository = new FileConnectionProfileRepository(tempDir);
        repository.save(new ConnectionProfile(
            "demo",
            "Demo",
            DatabaseType.OTHER,
            "jdbc:test",
            null,
            null,
            null,
            null,
            Map.of()
        ));

        repository.deleteById("demo");

        assertFalse(repository.findById("demo").isPresent());
    }
}
