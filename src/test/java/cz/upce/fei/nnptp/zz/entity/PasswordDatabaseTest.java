package cz.upce.fei.nnptp.zz.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class PasswordDatabaseTest {
    
    public PasswordDatabaseTest() {
    }
    
    private Path tempDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        tempDirectory = Files.createTempDirectory("PasswordDatabaseTest");
        assertTrue(Files.exists(tempDirectory));
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

    /**
     * Helper method to create a PasswordEntry with the necessary TITLE parameter for searches.
     */
    private PasswordEntry createEntryWithTitle(long id, String passwordValue, String titleValue) {
        HashMap<String, Parameter<?>> params = new HashMap<>();
        Parameter<String> titleParam = new Parameter<>(titleValue);
        // The cast to String is safe here because the Parameter constructor checks for null.
        params.put(Parameter.StandardizedParameters.TITLE, titleParam);

        // Note: The PasswordEntry constructor takes an int for id, so we cast the long.
        return new PasswordEntry((int) id, passwordValue, params);
    }

    @Test
    void testRemoveExistingEntryById() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");
        // Create entries with proper TITLE parameters for successful searching
        PasswordEntry entry1 = createEntryWithTitle(10, "password10", "Entry 10 Title");
        PasswordEntry entry2 = createEntryWithTitle(20, "password20", "Entry 20 Title");

        database.add(entry1);
        database.add(entry2);

        // 1. Check removal of an existing entry
        assertTrue(database.remove(10), "Should return true when an entry is successfully removed.");

        // 2. Check that the remaining entry is still present
        var foundEntry = database.findEntryByTitle("Entry 20 Title");
        assertTrue(foundEntry.isPresent(), "Entry 20 should still be in the database.");

        // 3. Try to find the removed entry
        assertFalse(database.findEntryByTitle("Entry 10 Title").isPresent(), "Removed entry 10 should not be found.");
    }

    @Test
    void testRemoveNonExistingEntryById() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");
        // Create entry with proper TITLE parameter
        PasswordEntry entry1 = createEntryWithTitle(10, "password10", "Entry 10 Title");

        database.add(entry1);

        // Try to remove an ID that was never added
        assertFalse(database.remove(99), "Should return false when no entry with the ID is found.");

        // Check that the original entry is still present
        assertTrue(database.findEntryByTitle("Entry 10 Title").isPresent(), "Original entry 10 must remain in the database.");
    }

    @Test
    void testRemoveEntryFromEmptyDatabase() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");

        // Try to remove an ID from an empty database
        assertFalse(database.remove(50), "Should return false when removing from an empty database.");
    }

    @Test
    void testRemoveOnlyEntry() {
        PasswordDatabase database = new PasswordDatabase(new File(""), "password");
        // Create the single entry
        PasswordEntry entry1 = createEntryWithTitle(15, "singlePasswordValue", "Single Title");

        database.add(entry1);

        // Remove the single entry
        assertTrue(database.remove(15), "Should return true for removing the single entry.");

        // Check if the database is now empty for this entry
        assertFalse(database.findEntryByTitle("Single Title").isPresent(), "Database should be empty after removing the only entry.");
    }

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
}
