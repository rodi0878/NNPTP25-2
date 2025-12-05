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

    private final File file;
    private final String password;

    // In-memory cache of password entries
    private final List<PasswordEntry> passwords;

    // Repository handling persistence logic
    private final PasswordRepository repository;

    /**
     * Primary constructor that enables injecting any repository implementation.
     * Great for unit testing and future storage backends.
     */
    public PasswordDatabase(PasswordRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.file = null;
        this.password = null;
        this.passwords = new ArrayList<>();
    }

    /**
     * Creates PasswordDatabase object
     * @param file - file to store and load database from
     * @param password - string used to encrypt and decrypt database at rest
     */
    public PasswordDatabase(File file, String password) {
        this.file = Objects.requireNonNull(file, "file must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
        this.repository = new FilePasswordRepository(this.file, this.password, new JSON());
        this.passwords = new ArrayList<>();
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
     * Finds a password entry by its ID in the in-memory database.
     *
     * @param id ID to search for, must not be negative
     * @return optional containing the first entry with the given ID,
     *         or {@link Optional#empty()} if not found
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
        return passwords.removeIf(entry -> entry.getId() == id);
    }

    /**
     * Searches the in-memory database for a password based on the title parameter.
     * @param title - title to search for. Only complete match is returned.
     * @return optional containing first password whose title parameter matches input or empty optional if password not found
     */
    public Optional<PasswordEntry> findEntryByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty.");
        }

        return passwords.stream()
                .filter(p -> p.hasParameter(Parameter.StandardizedParameters.TITLE))
                .map(p -> new Object[]{p, p.getParameter(Parameter.StandardizedParameters.TITLE)})
                .filter(arr -> arr[1] != null && arr[1] instanceof Parameter<?> param &&
                        param.getValue() != null && param.getValue().equals(title))
                .map(arr -> (PasswordEntry) arr[0])
                .findFirst();
    }





    /**
     * Returns all password entries currently in memory.
     * @return list of password entries
     */
    public List<PasswordEntry> getAllEntries() {
        return new ArrayList<>(passwords);
    }
}