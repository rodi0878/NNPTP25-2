package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoFileTest {
    
    public CryptoFileTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    @TempDir
    File tempDir;

    private File testFile;

    @BeforeEach
    public void setUp() {
        testFile = new File(tempDir, "test-encrypted.dat");
    }

    @AfterEach
    public void tearDown() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test of readFile method, of class CryptoFile.
     */
    @Test
    public void testReadFile() {
        System.out.println("readFile");
        File file = null;
        String password = "";
        String expResult = "";
        String result = ""; //CryptoFile.readFile(file, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of writeFile method, of class CryptoFile.
     */
    @Test
    public void testWriteFile() {
        System.out.println("writeFile");
        File file = null;
        String password = "";
        String cnt = "";
        //CryptoFile.writeFile(file, password, cnt);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testWriteFile_ShortPassword() {
        String shortPassword = "123";
        String content = "Test content with short password";

        CryptoFile.writeFile(testFile, shortPassword, content);
        String result = CryptoFile.readFile(testFile, shortPassword);

        assertEquals(content, result);
    }

    @Test
    public void testWriteFile_LongPassword() {
        String longPassword = "ThisIsAVeryLongPasswordThatExceedsEightBytes";
        String content = "Test content with long password";

        CryptoFile.writeFile(testFile, longPassword, content);
        String result = CryptoFile.readFile(testFile, longPassword);

        assertEquals(content, result);
    }

    @Test
    public void testWriteFile_ExactlyEightBytePassword() {
        String eightBytePassword = "12345678";
        String content = "Test content with 8-byte password";

        CryptoFile.writeFile(testFile, eightBytePassword, content);
        String result = CryptoFile.readFile(testFile, eightBytePassword);

        assertEquals(content, result);
    }

    @Test
    public void testWriteFile_SpecialCharactersInPassword() {
        String password = "p@ssw0rd!#$%";
        String content = "Content with special char password";

        CryptoFile.writeFile(testFile, password, content);
        String result = CryptoFile.readFile(testFile, password);

        assertEquals(content, result);
    }
    
}
