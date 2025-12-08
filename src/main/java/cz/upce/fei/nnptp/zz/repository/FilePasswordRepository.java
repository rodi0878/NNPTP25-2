package cz.upce.fei.nnptp.zz.repository;

import cz.upce.fei.nnptp.zz.entity.CryptoFile;
import cz.upce.fei.nnptp.zz.entity.DecryptionException;
import cz.upce.fei.nnptp.zz.entity.EncryptionException;
import cz.upce.fei.nnptp.zz.entity.JSON;
import cz.upce.fei.nnptp.zz.entity.Parameter;
import cz.upce.fei.nnptp.zz.entity.PasswordEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     * @throws IllegalArgumentException if any of the arguments is {@code null}
     */
    public FilePasswordRepository(File file, String password, JSON json) {
        if (file == null) throw new IllegalArgumentException("file must not be null");
        if (password == null) throw new IllegalArgumentException("password must not be null");
        if (json == null) throw new IllegalArgumentException("json must not be null");
        this.file = file;
        this.password = password;
        this.json = json;
    }

    /**
     * Loads all password entries from the encrypted file.
     * If the file is empty or cannot be read, an empty list is returned.
     *
     * @return a mutable list of all persisted password entries
     * @throws PasswordStorageException if the file cannot be decrypted or the stored data cannot be deserialized
     */
    @Override
    public List<PasswordEntry> findAll() {
        String data = null;
        try {
            data = readEncryptedJson();
        } catch (DecryptionException e) {
            return new ArrayList<>();
        }
        
        try {
            // JSON.fromJson returns raw List, so we must cast it.
            @SuppressWarnings("unchecked")
            List<PasswordEntry> entries = json.fromJson(data);
            if (entries == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(entries);
        } catch (RuntimeException e) {
            throw new PasswordStorageException("Failed to deserialize password entries", e);
        }
    }

    /**
     * Finds the first password entry whose TITLE parameter matches the given name.
     *
     * @param name the title value to search for (may be {@code null})
     * @return an {@link Optional} with the matching entry, or {@code Optional.empty()} if not found or name is {@code null}
     */

    @Override
    public Optional<PasswordEntry> findByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(entry -> {
                    Parameter<?> p = entry.getParameter(Parameter.StandardizedParameters.TITLE);
                    return p != null && name.equals(String.valueOf(p.getValue()));
                })
                .findFirst();
    }


    /**
     * Persists all provided password entries into the encrypted file, replacing any previous content.
     *
     * @param entries the entries to be stored; may be {@code null}, in which case an empty list is stored
     * @throws PasswordStorageException if the data cannot be serialized, encrypted, or written to the file
     */
    @Override
    public void saveAll(List<PasswordEntry> entries) {
        List<PasswordEntry> safeList =
                entries == null ? Collections.emptyList() : new ArrayList<>(entries);

        try {
            // JSON uses toJson(...) to serialize the list.
            String data = json.toJson(safeList);
            writeEncryptedJson(data);
        } catch (RuntimeException e) {
            throw new PasswordStorageException("Failed to save password entries", e);
        }
    }

    // Helpers for encrypted I/O
    /**
     * Reads encrypted JSON string from the underlying file.
     *
     * @return decrypted JSON string, or {@code null} if reading/decryption failed
     * @throws DecryptionException if decryption fails
     * @throws IllegalArgumentException if file or password is null
     */
    private String readEncryptedJson() throws DecryptionException, IllegalArgumentException {
        return CryptoFile.readFile(file, password);
    }

    /**
     * Writes the given JSON string to the underlying file using encryption.
     *
     * @param jsonData JSON content to encrypt and persist; never {@code null}
     * @throws EncryptionException if encryption fails
     * @throws IllegalArgumentException if file or password is null
     */
    private void writeEncryptedJson(String jsonData) throws EncryptionException, IllegalArgumentException {
        CryptoFile.writeFile(file, password, jsonData);
    }
}