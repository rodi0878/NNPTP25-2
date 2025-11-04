package cz.upce.fei.nnptp.zz.repository;

import cz.upce.fei.nnptp.zz.entity.JSON;
import cz.upce.fei.nnptp.zz.entity.Parameter;
import cz.upce.fei.nnptp.zz.entity.PasswordEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePasswordRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAllAndFindAll_shouldPersistAndLoadEntries() {
        File file = tempDir.resolve("repo.txt").toFile();
        String masterPassword = "password";

        JSON json = new JSON();
        FilePasswordRepository repository = new FilePasswordRepository(file, masterPassword, json);

        // připravíme jeden záznam
        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put(Parameter.StandardizedParameters.TITLE, new Parameter<>("My title"));
        PasswordEntry entry = new PasswordEntry(1, "secret", params);

        repository.saveAll(List.of(entry));

        // nový repository nad stejným souborem – musí načíst to samé
        FilePasswordRepository repository2 = new FilePasswordRepository(file, masterPassword, json);
        List<PasswordEntry> loaded = repository2.findAll();

        assertEquals(1, loaded.size());
        assertEquals(1, loaded.get(0).getId());
        assertEquals("secret", loaded.get(0).getPassword());
    }

    @Test
    void findByName_shouldReturnEntryByTitleParameter() {
        File file = tempDir.resolve("repo2.txt").toFile();
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

        assertTrue(repository.findByName("Second").isPresent());
        assertEquals(2, repository.findByName("Second").get().getId());
        assertTrue(repository.findByName("Unknown").isEmpty());
    }
}