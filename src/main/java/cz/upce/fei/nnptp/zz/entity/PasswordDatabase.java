package cz.upce.fei.nnptp.zz.entity;

import cz.upce.fei.nnptp.zz.repository.FilePasswordRepository;
import cz.upce.fei.nnptp.zz.repository.PasswordRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a password database that manages password entries using a repository.
 */
public class PasswordDatabase {

    // In-memory cache of password entries
    private final List<PasswordEntry> passwords = new ArrayList<>();

    // Repository handling persistence logic
    private final PasswordRepository repository;

    /**
     * Primary constructor that enables injecting any repository implementation.
     * Great for unit testing and future storage backends.
     */
    public PasswordDatabase(PasswordRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    /**
     * Creates PasswordDatabase object
     *
     * @param file     - file to store and load database from
     * @param password - string used to encrypt and decrypt database at rest
     */
    public PasswordDatabase(File file, String password) {
        this(new FilePasswordRepository(
                Objects.requireNonNull(file, "file must not be null"),
                Objects.requireNonNull(password, "password must not be null"),
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
            passwords.addAll(repository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Unable to load password database", e);
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
            throw new RuntimeException("Unable to save password database", e);
        }
    }

    /**
     * Adds a password to the in-memory database.
     *
     * @param password - Password to add
     */
    public void add(PasswordEntry password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password is null");

        if (passwords.stream().anyMatch(p -> p.getId() == password.getId()))
            throw new IllegalStateException("Password with this ID already exists");

        passwords.add(password);
        // Testy nevyžadují automatický persist — proto save() není volán zde.
    }

    /**
     * Updates an existing password entry in the in-memory database
     *
     * @param updated password entry with updated data
     * @throws NullPointerException if updated is null
     * @throws IllegalStateException if no entry with the same ID exists
     */
    public void update(PasswordEntry updated) {
        if (updated == null) {
            throw new NullPointerException("Password is null");
        }

        for (int i = 0; i < passwords.size(); i++) {
            if (passwords.get(i).getId() == updated.getId()) {
                passwords.set(i, updated);
                return;
            }
        }

        throw new IllegalStateException("Password with this ID does not exist");
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
        if (id < 0) {
            throw new IllegalArgumentException("Id must not be negative.");
        }

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
        if (id < 0) {
            throw new IllegalArgumentException("Id must not be negative.");
        }
        return passwords.removeIf(entry -> entry.getId() == id);
    }

    /**
     * Searches the in-memory database for a password based on the title parameter.
     *
     * @param title - title to search for. Only complete match is returned.
     * @return optional containing first password whose title parameter matches input or empty optional if password not found
     */
    public Optional<PasswordEntry> findEntryByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty.");
        }
        for (PasswordEntry password : passwords) {
            if (password.hasParameter(Parameter.StandardizedParameters.TITLE)) {
                Parameter<?> titleParameter = password.getParameter(Parameter.StandardizedParameters.TITLE);
                if (titleParameter != null &&
                        titleParameter.getValue() != null &&
                        titleParameter.getValue().equals(title)) {
                    return Optional.of(password);
                }
            }
        }
        return Optional.empty();
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