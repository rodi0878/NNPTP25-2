package cz.upce.fei.nnptp.zz.repository;

import cz.upce.fei.nnptp.zz.entity.DecryptionException;
import cz.upce.fei.nnptp.zz.entity.JSON;
import cz.upce.fei.nnptp.zz.entity.Parameter;
import cz.upce.fei.nnptp.zz.entity.PasswordEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePasswordRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAll_shouldPersistEntriesToFile() {
        File file = tempDir.resolve("repo.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("My title"));
        PasswordEntry entry = new PasswordEntry(1, "secret", params);

        repository.saveAll(List.of(entry));

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    void findAll_shouldLoadEntriesFromFile() {
        File file = tempDir.resolve("repo_load.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("My title"));
        PasswordEntry entry = new PasswordEntry(1, "secret", params);

        repository.saveAll(List.of(entry));

        FilePasswordRepository repository2 = new FilePasswordRepository(file, masterPassword, json);
        List<PasswordEntry> loaded = repository2.findAll();

        assertEquals(1, loaded.size());
        assertEquals(1, loaded.get(0).getId());
        assertEquals("secret", loaded.get(0).getPassword());
    }

    @Test
    void findByName_shouldReturnMatchingEntry() {
        File file = tempDir.resolve("repo_find.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        HashMap<String, Parameter<?>> params1 = new HashMap<>();
        params1.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("First"));
        PasswordEntry first = new PasswordEntry(1, "pw1", params1);

        HashMap<String, Parameter<?>> params2 = new HashMap<>();
        params2.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("Second"));
        PasswordEntry second = new PasswordEntry(2, "pw2", params2);

        repository.saveAll(List.of(first, second));

        var result = repository.findByName("Second");

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getId());
    }

    @Test
    void findByName_shouldReturnEmptyForUnknownTitle() {
        File file = tempDir.resolve("repo_find_unknown.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("Existing"));
        PasswordEntry entry = new PasswordEntry(1, "pw1", params);

        repository.saveAll(List.of(entry));

        assertTrue(repository.findByName("Unknown").isEmpty());
    }

    @Test
    void findAll_shouldCreateEmptyList_whenDecryptionFails() throws IOException {
        File file = tempDir.resolve("corrupted.txt").toFile();
        String masterPassword = "password";

        // Create a corrupted/non-encrypted file
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write("corrupted content");
        }

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        List<PasswordEntry> result = repository.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenFileIsEmpty() {
        File file = tempDir.resolve("empty.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        // File doesn't exist, should return empty list (not throw exception)
        List<PasswordEntry> result = assertDoesNotThrow(repository::findAll);
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAll_shouldHandleNullEntries() {
        File file = tempDir.resolve("null_entries.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        // Should not throw exception when entries is null
        assertDoesNotThrow(() -> repository.saveAll(null));

        // Should create an empty file
        assertTrue(file.exists());
    }
}
