package cz.upce.fei.nnptp.zz.entity;

import cz.upce.fei.nnptp.zz.repository.FilePasswordRepository;
import cz.upce.fei.nnptp.zz.repository.PasswordRepository;

import java.io.File;
import java.util.*;

/**
 * Represents a password database that manages password entries using a repository.
 */
public class PasswordDatabase {

    // In-memory cache of password entries
    private final List<PasswordEntry> passwords = new ArrayList<>();

    // Repository handling persistence logic
    private final PasswordRepository repository;

    private void validateId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must not be negative.");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty.");
        }
    }

    /**
     * Primary constructor that enables injecting any repository implementation.
     * Great for unit testing and future storage backends.
     */
    public PasswordDatabase(PasswordRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository must not be null");
    }

    /**
     * Creates PasswordDatabase object
     *
     * @param file     - file to store and load database from
     * @param password - string used to encrypt and decrypt database at rest
     */
    public PasswordDatabase(File file, String password) {
        this(new FilePasswordRepository(
                Objects.requireNonNull(file, "File must not be null"),
                Objects.requireNonNull(password, "Password must not be null"),
                new JSON()
        ));
    }

    /**
     * Loads contents of a database from an encrypted file.
     * File and decryption password are set during Database creation.
     */
    public void load() {
        try {
            passwords.clear();
            List<PasswordEntry> loaded = repository.findAll();
            if (loaded != null) {
                passwords.addAll(loaded);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to load password database", e);
        }
    }

    /**
     * Saves contents of a database to an encrypted file.
     * File and encryption password are set during Database creation.
     */
    public void save() {
        try {
            repository.saveAll(passwords);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to save password database", e);
        }
    }

    /**
     * Adds a password to the in-memory database.
     *
     * @param entry password entry to add
     */
    public void add(PasswordEntry entry) {
        Objects.requireNonNull(entry, "Password entry must not be null");

        boolean idExists = passwords.stream()
                .anyMatch(p -> p.getId() == entry.getId());

        if (idExists) {
            throw new IllegalStateException("Password with this ID already exists");
        }

        passwords.add(entry);
    }

    /**
     * Finds a password entry by its ID in the in-memory database.
     *
     * @param id ID to search for, must not be negative
     * @return optional containing the first entry with the given ID,
     * or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code id} is negative
     */
    public Optional<PasswordEntry> findEntryById(int id) {
        validateId(id);

        return passwords.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst();
    }

    /**
     * Removes a password entry with the given ID from the in-memory database.
     *
     * @param id ID of the password entry to remove
     * @return {@code true}, if at least one entry was removed, {@code false} otherwise
     */
    public boolean removeById(int id) {
        validateId(id);
        return passwords.removeIf(entry -> entry.getId() == id);
    }

    /**
     * Searches the in-memory database for a password based on the title parameter.
     *
     * @param title - title to search for. Only complete match is returned.
     * @return optional containing first password whose title parameter matches input or empty optional if password not found
     */
    public Optional<PasswordEntry> findEntryByTitle(String title) {
        validateTitle(title);

        return passwords.stream()
                .filter(entry -> entry.hasParameter(Parameter.StandardizedParameters.TITLE))
                .map(entry -> Map.entry(entry,
                        entry.getParameter(Parameter.StandardizedParameters.TITLE)))
                .filter(e -> Objects.equals(e.getValue().getValue(), title))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /**
     * Returns all password entries currently in memory.
     *
     * @return list of password entries
     */
    public List<PasswordEntry> getAllEntries() {
        return new ArrayList<>(passwords);
    }
}