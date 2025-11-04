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
     * Searches the in-memory database for a password based on the title parameter.
     * @param title - title to search for. Only complete match is returned.
     * @return optional containing first password whose title parameter matches input or empty optional if password not found
     */
    public Optional<PasswordEntry> findEntryByTitle(String title) {
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
}