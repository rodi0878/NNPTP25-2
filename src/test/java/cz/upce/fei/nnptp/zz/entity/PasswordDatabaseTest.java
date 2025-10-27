package cz.upce.fei.nnptp.zz.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        database.add(new Password(1, "password1"));
        database.add(new Password(2, "password2"));

        database.save();

        assertTrue(Files.exists(tempDirectory.resolve("TestValid.txt")));
    }
    @Test
    public void testJsonLoadEmpty(){
        JSON json = new JSON();
        List<Password> v = json.fromJson("[]");
        assertTrue(v.isEmpty());
    }
    @Test
    public void testJsonLoadValue(){
        JSON json = new JSON();
        List<Password> v = json.fromJson("[{username:\"user\",password:\"pswd\"}]");
        assertEquals(1, v.stream().count());
    }    @Test
    public void testJsonLoadValuePasswordData(){
        JSON json = new JSON();
        List<Password> v = json.fromJson("[{username:\"user\",password:\"pswd\"}]");
        assertEquals("pswd", v.getFirst().getPassword());
    }
    @Test
    public void testJsonLoadValueMultiple(){
        JSON json = new JSON();
        List<Password> v = json.fromJson("[{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"},{username:\"user\",password:\"pswd\"}]");
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
        Password password = new Password(1, "password1");
        Password password2 = new Password(1, "password2");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            database.add(password);
            database.add(password2);
        });
        assertEquals("Password with this ID already exists", exception.getMessage());
    }
}
