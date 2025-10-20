/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author st69851
 */
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
    public void testSaveNullPassword() {
        PasswordDatabase database = new PasswordDatabase(tempDirectory.resolve("TestNull.txt").toFile(), "password");
        database.add(null);
        assertThrows(NullPointerException.class, database::save);
        assertFalse(Files.exists(tempDirectory.resolve("TestNull.txt")));
    }

    @Test
    public void testSaveValidMultiplePasswords() {
        PasswordDatabase database = new PasswordDatabase(tempDirectory.resolve("TestValid.txt").toFile(), "password");
        database.add(new Password(1, "password1"));
        database.add(new Password(2, "password2"));

        database.save();

        assertTrue(Files.exists(tempDirectory.resolve("TestValid.txt")));
    }
}
