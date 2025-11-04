package cz.upce.fei.nnptp.zz.repository;

import cz.upce.fei.nnptp.zz.entity.CryptoFile;
import cz.upce.fei.nnptp.zz.entity.JSON;
import cz.upce.fei.nnptp.zz.entity.Parameter;
import cz.upce.fei.nnptp.zz.entity.PasswordEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link PasswordRepository} that stores all password entries
 * in a single encrypted file using {@link CryptoFile} for encryption and
 * {@link JSON} for serialization.
 */
public class FilePasswordRepository implements PasswordRepository {

    private final File file;
    private final String password;
    private final JSON json;

    /**
     * Creates a new file-based password repository.
     *
     * @param file     the underlying file that will be used to persist the encrypted data
     * @param password the password used to encrypt and decrypt the file contents
     * @param json     JSON helper used for serialization and deserialization of password entries
     * @throws NullPointerException if any of the arguments is {@code null}
     */
    public FilePasswordRepository(File file, String password, JSON json) {
        this.file = Objects.requireNonNull(file, "file must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
        this.json = Objects.requireNonNull(json, "json must not be null");
    }

    @Override
    /**
     * Loads all password entries from the encrypted file.
     * If the file is empty or cannot be read, an empty list is returned.
     *
     * @return a mutable list of all persisted password entries
     * @throws PasswordStorageException if the stored data cannot be deserialized
     */
    public List<PasswordEntry> findAll() {
        String data = CryptoFile.readFile(file, password);

        // CryptoFile vrací null při chybě, tady to raději interpretujeme jako "žádná data"
        if (data == null || data.isBlank()) {
            return new ArrayList<>();
        }

        try {
            // JSON.fromJson vrací raw List, tak to musíme přetypovat
            @SuppressWarnings("unchecked")
            List<PasswordEntry> entries = (List<PasswordEntry>) json.fromJson(data);
            if (entries == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(entries);
        } catch (RuntimeException e) {
            throw new PasswordStorageException("Failed to deserialize password entries", e);
        }
    }

    @Override
    /**
     * Finds the first password entry whose TITLE parameter matches the given name.
     *
     * @param name the title value to search for (may be {@code null})
     * @return an {@link Optional} with the matching entry, or {@code Optional.empty()} if not found or name is {@code null}
     */
    public Optional<PasswordEntry> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        List<PasswordEntry> entries = findAll();
        for (PasswordEntry entry : entries) {
            Parameter param = entry.getParameter(Parameter.StandardizedParameters.TITLE);
            if (param != null && name.equals(param.getValue().toString())) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    @Override
    /**
     * Persists all provided password entries into the encrypted file, replacing any previous content.
     *
     * @param entries the entries to be stored; may be {@code null}, in which case an empty list is stored
     * @throws PasswordStorageException if the data cannot be serialized or written to the file
     */
    public void saveAll(List<PasswordEntry> entries) {
        List<PasswordEntry> safeList =
                entries == null ? Collections.emptyList() : new ArrayList<>(entries);

        try {
            // JSON používá toJson(...)
            String data = json.toJson(safeList);

            // CryptoFile.writeFile už sám řeší šifrování + IO a chyby loguje.
            CryptoFile.writeFile(file, password, data);
        } catch (RuntimeException e) {
            throw new PasswordStorageException("Failed to save password entries", e);
        }
    }
}