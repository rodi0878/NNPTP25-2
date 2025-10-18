/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author st69851
 */
public class PasswordDatabaseTest {

    public PasswordDatabaseTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of load method, of class PasswordDatabase.
     */
    @Disabled("not implemented")
    @Test
    public void testLoad() {
        System.out.println("load");
        // test not implemented
    }

    /**
     * Test of save method, of class PasswordDatabase.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        try {
            java.io.File tmp = java.io.File.createTempFile("pwdb", ".dat");
            tmp.deleteOnExit();

            String pwd = "password";
            PasswordDatabase instance = new PasswordDatabase(tmp, pwd);

            instance.save();

            assertTrue(tmp.exists(), "Saved file should exist");

            String decrypted = CryptoFile.readFile(tmp, pwd);
            assertEquals("[]", decrypted, "Saved JSON for empty passwords should be []");
        } catch (Exception ex) {
            fail("Exception during testSave: " + ex.getMessage());
        }
    }

    /**
     * Test of add method, of class PasswordDatabase.
     */
    @Test
    @Disabled("not implemented")
    public void testAdd() {
        System.out.println("add");
        // test not implemented
    }

    /**
     * Test of findEntryByTitle method, of class PasswordDatabase.
     */
    @Test
    @Disabled("not implemented")
    public void testFindEntryByTitle() {
        System.out.println("findEntryByTitle");
        // test not implemented
    }

}
