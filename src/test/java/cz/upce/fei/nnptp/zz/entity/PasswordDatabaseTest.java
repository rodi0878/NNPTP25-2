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
}
