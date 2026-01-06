package cz.upce.fei.nnptp.zz.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import cz.upce.fei.nnptp.zz.repository.PasswordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

public class PasswordDatabaseTest {
    
    public PasswordDatabaseTest() {
    }
    
    private Path tempDirectory;
    private PasswordDatabase database;

    @BeforeEach
    public void setUp() throws IOException {
        tempDirectory = Files.createTempDirectory("PasswordDatabaseTest");
        assertTrue(Files.exists(tempDirectory));
        database = new PasswordDatabase(new File("test.db"), "testPassword");
        database.add(new PasswordEntry(1, "password1"));
        database.add(new PasswordEntry(2, "password2"));
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        if (tempDirectory != null) {
            Files.walk(tempDirectory).map(Path::toFile).forEach(File::delete);
            Files.deleteIfExists(tempDirectory);
        }
    }

    @Test
    public void testSaveEmptyDatabase() {
        PasswordDatabase database = new PasswordDatabase(tempDirectory.resolve("TestEmpty.txt").toFile(), "password");
        database.save();

        assertTrue(Files.exists(tempDirectory.resolve("TestEmpty.txt")));
    }

    @Test
    public void testSaveValidMultiplePasswords() {
        PasswordDatabase database = new PasswordDatabase(tempDirectory.resolve("TestValid.txt").toFile(), "password");
        database.add(new PasswordEntry(1, "password1"));
        database.add(new PasswordEntry(2, "password2"));

        database.save();

        assertTrue(Files.exists(tempDirectory.resolve("TestValid.txt")));
    }
    
    @Test
    public void testJsonLoadEmpty(){
        JSON json = new JSON();
        List<PasswordEntry> v = json.fromJson("[]");
        assertTrue(v.isEmpty());
    }
    
    @Test
    public void testJsonLoadValue(){
        JSON json = new JSON();
        List<PasswordEntry> v = json.fromJson("[{username:\"user\",password:\"pswd\"}]");
        assertEquals(1, v.stream().count());
    }    
    
    @Test
    public void testJsonLoadValuePasswordData(){
        JSON json = new JSON();
        List<PasswordEntry> v = json.fromJson("[{username:\"user\",password:\"pswd\"}]");
        assertEquals("pswd", v.getFirst().getPassword());
    }
    
    @Test
    public void testJsonLoadValueMultiple(){
        JSON json = new JSON();
        List<PasswordEntry> v = json.fromJson("[{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"}]");
        assertEquals(5, v.size());
    }

    @Test
    void testAddNullPassword() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");
        NullPointerException exception = assertThrows(NullPointerException.class, () -> database.add(null));
        assertEquals("Password is null", exception.getMessage());
    }

    @Test
    void testAddDuplicatePasswordId() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");
        PasswordEntry password = new PasswordEntry(1, "password1");
        PasswordEntry password2 = new PasswordEntry(1, "password2");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            database.add(password);
            database.add(password2);
        });
        assertEquals("Password with this ID already exists", exception.getMessage());
    }

    //# region update tests
    @Test
    void testUpdateExistingEntry() {
        PasswordEntry updated = new PasswordEntry(1, "updatedPassword");

        database.update(updated);

        var result = database.findEntryById(1);
        assertTrue(result.isPresent());
        assertEquals("updatedPassword", result.get().getPassword());
    }

    @Test
    void testUpdateWithNullThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> database.update(null)
        );

        assertEquals("Password is null", exception.getMessage());
    }

    @Test
    void testUpdateNonExistingIdThrowsException() {
        PasswordEntry nonExisting = new PasswordEntry(999, "password");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> database.update(nonExisting)
        );

        assertEquals("Password with this ID does not exist", exception.getMessage());
    }
    //#endregion

    @Test
    void testFindEntryByTitle() {
        PasswordDatabase database = new PasswordDatabase(new File("testDatabase.txt"), "password");

        HashMap<String, Parameter<?>> password1Parameters = new HashMap<>();
        var password1TitleParam = new Parameter<>("PW1 title");
        password1Parameters.put(Parameter.StandardizedParameters.TITLE, password1TitleParam);
        PasswordEntry password1 = new PasswordEntry(1, "password1",password1Parameters);

        HashMap<String, Parameter<?>> password2Parameters = new HashMap<>();
        var password2TitleParam = new Parameter<>("PW2 title");
        password2Parameters.put(Parameter.StandardizedParameters.TITLE, password2TitleParam);
        PasswordEntry password2 = new PasswordEntry(2, "password2",password2Parameters);

        database.add(password1);
        database.add(password2);

        var foundEntry = database.findEntryByTitle("PW2 title");

        assertTrue(foundEntry.isPresent());

        assertEquals(foundEntry.get(), password2);
    }

    @Test
    public void testLoadWithMockedCryptoFile() {
        try (MockedStatic<CryptoFile> mockedCryptoFile = mockStatic(CryptoFile.class)) {
            File testFile = tempDirectory.resolve("test.txt").toFile();
            String testPassword = "password";
            String mockJsonData = "[{\"password\":\"testPass\",\"parameters\":{\"title\":{\"type\":\"text\",\"value\":\"Test Entry Title\"}}}]";
            mockedCryptoFile.when(() -> CryptoFile.readFile(testFile, testPassword)).thenReturn(mockJsonData);

            PasswordDatabase database = new PasswordDatabase(testFile, testPassword);
            database.load();

            mockedCryptoFile.verify(() -> CryptoFile.readFile(testFile, testPassword));
        }
    }

    @Test
    public void testLoadWithNullFileContent() {
        try (MockedStatic<CryptoFile> mockedCryptoFile = mockStatic(CryptoFile.class)) {
            File testFile = tempDirectory.resolve("test.txt").toFile();
            String testPassword = "password";

            mockedCryptoFile.when(() -> CryptoFile.readFile(testFile, testPassword)).thenReturn(null);

            PasswordDatabase database = new PasswordDatabase(testFile, testPassword);

            assertDoesNotThrow(database::load);
        }
    }

    @Test
    void testFindEntryByTitleWithNull() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                database.findEntryByTitle(null)
        );
        assertEquals("Title must not be null or empty.", exception.getMessage());
    }

    @Test
    void testFindEntryByTitleWithEmptyString() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                database.findEntryByTitle("")
        );
        assertEquals("Title must not be null or empty.", exception.getMessage());
    }

    @Test
    void testFindEntryByTitleWithWhitespace() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                database.findEntryByTitle("  \t  ")
        );
        assertEquals("Title must not be null or empty.", exception.getMessage());
    }

    @Test
    void testRemoveByIdRemovesExistingEntry() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");

        PasswordEntry entry = new PasswordEntry(1, "password1");
        database.add(entry);

        boolean removed = database.removeById(1);

        assertTrue(removed);

        assertDoesNotThrow(() -> database.add(new PasswordEntry(1, "password2")));
    }

    @Test
    public void testConstructorWithInjectedRepository() {
        // Mock repository and its behavior
        PasswordRepository repo = mock(PasswordRepository.class);
        PasswordEntry existing = new PasswordEntry(1, "existing");
        when(repo.findAll()).thenReturn(List.of(existing));

        // Create database with injected repository
        PasswordDatabase database = new PasswordDatabase(repo);

        // load() should call repository.findAll()
        database.load();
        verify(repo).findAll();

        // Add a new entry and call save() which should call repository.saveAll(...)
        PasswordEntry newEntry = new PasswordEntry(2, "newpass");
        database.add(newEntry);
        database.save();

        // Capture the list passed to saveAll and assert it contains both entries
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(repo).saveAll(captor.capture());
        List captured = captor.getValue();

        assertEquals(2, captured.size());
        assertTrue(captured.contains(existing));
        assertTrue(captured.contains(newEntry));
    }

    @Test
    void testGetAllEntriesReturnCorrectSize() {
        List<PasswordEntry> allEntries = database.getAllEntries();
        assertEquals(2, allEntries.size());
    }

    @Test
    void testGetAllEntriesReturnCopyNotReference() {
        List<PasswordEntry> allEntries = database.getAllEntries();
        allEntries.clear();
        assertEquals(2, database.getAllEntries().size());
    }

    @Test
    void testFindEntryByIdReturnsMatchingEntry() {
        var result = database.findEntryById(2);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getId());
        assertEquals("password2", result.get().getPassword());
    }

    @Test
    void testFindEntryByIdReturnsEmptyWhenNotFound() {
        var result = database.findEntryById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindEntryByIdWithNegativeIdThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> database.findEntryById(-1)
        );

        assertEquals("Id must not be negative.", exception.getMessage());
    }
}

