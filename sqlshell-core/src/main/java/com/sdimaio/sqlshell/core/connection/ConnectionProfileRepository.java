package com.sdimaio.sqlshell.core.connection;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for persisted connection profiles.
 *
 * <p>The profile store is modeled as a repository rather than as a simple
 * utility because the persistence technology is intentionally left open. A
 * local file store is sufficient for the first release, but the rest of the
 * application should not care whether profiles later move to encrypted local
 * storage, a shared workspace, or a synchronized settings backend.
 *
 * <p>Implementations are expected to preserve profile identity by {@code id}
 * and to return profiles in a stable, user-friendly order.
 *
 * @author sdimaio
 */
public interface ConnectionProfileRepository {

    /**
     * Returns all known profiles.
     *
     * @return profiles currently available to the application.
     */
    List<ConnectionProfile> listAll();

    /**
     * Locates one profile by identifier.
     *
     * @param id stable profile identifier.
     * @return matching profile when present.
     */
    Optional<ConnectionProfile> findById(String id);

    /**
     * Persists or replaces a profile.
     *
     * @param profile profile to persist.
     * @return the persisted profile, potentially normalized by the store.
     */
    ConnectionProfile save(ConnectionProfile profile);

    /**
     * Deletes a profile by identifier.
     *
     * @param id stable profile identifier.
     */
    void deleteById(String id);
}
