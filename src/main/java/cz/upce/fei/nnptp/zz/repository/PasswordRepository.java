package cz.upce.fei.nnptp.zz.repository;

import cz.upce.fei.nnptp.zz.entity.PasswordEntry;

import java.util.List;
import java.util.Optional;

/**
 * Abstraction for password storage.
 * Implementations can store password entries in different ways (file, memory, database, etc.).
 */
public interface PasswordRepository {

    /**
     * Retrieves all password entries from the underlying storage.
     *
     * @return list of all stored password entries
     */
    List<PasswordEntry> findAll();

    /**
     * Finds a password entry by its title (or name).
     *
     * @param name the title of the password entry to find
     * @return an {@link Optional} containing the matching entry if found, otherwise {@link Optional#empty()}
     */
    Optional<PasswordEntry> findByName(String name);

    /**
     * Saves all given password entries to the underlying storage.
     * Existing data will be replaced with the provided entries.
     *
     * @param entries list of password entries to save
     */
    void saveAll(List<PasswordEntry> entries);
}