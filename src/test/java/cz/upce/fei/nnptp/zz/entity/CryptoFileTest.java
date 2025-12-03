package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoFileTest {

    @TempDir
    Path tempDir;

    @Test
    void readFile_shouldThrowDecryptionException_whenFileDoesNotExist() {
        File nonExistentFile = tempDir.resolve("nonexistent.txt").toFile();
        String password = "password";

        DecryptionException exception = assertThrows(
                DecryptionException.class,
                () -> CryptoFile.readFile(nonExistentFile, password)
        );

        assertTrue(exception.getMessage().contains("Failed to read or decrypt file"));
        assertNotNull(exception.getCause());
    }

    @Test
    void readFile_shouldThrowDecryptionException_whenPasswordIsWrong() throws IOException {
        File file = tempDir.resolve("test.txt").toFile();
        String correctPassword = "password";
        String content = "test content";
        CryptoFile.writeFile(file, correctPassword, content);
        String wrongPassword = "wrongPassword";
        DecryptionException exception = assertThrows(
                DecryptionException.class,
                () -> CryptoFile.readFile(file, wrongPassword)
        );
        assertTrue(exception.getMessage().contains("Failed to initialize decryption cipher"));
    }

    @Test
    void readFile_shouldThrowIllegalArgumentException_whenFileIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoFile.readFile(null, "password")
        );

        assertTrue(exception.getMessage().contains("file must not be null"));
    }

    @Test
    void readFile_shouldThrowIllegalArgumentException_whenPasswordIsNull() {
        File file = tempDir.resolve("test.txt").toFile();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoFile.readFile(file, null)
        );

        assertTrue(exception.getMessage().contains("password must not be null"));
    }

    @Test
    void writeFile_shouldThrowIllegalArgumentException_whenFileIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoFile.writeFile(null, "password", "content")
        );

        assertTrue(exception.getMessage().contains("file must not be null"));
    }

    @Test
    void writeFile_shouldThrowIllegalArgumentException_whenPasswordIsNull() {
        File file = tempDir.resolve("test.txt").toFile();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoFile.writeFile(file, null, "content")
        );

        assertTrue(exception.getMessage().contains("password must not be null"));
    }

    @Test
    void writeFile_shouldThrowIllegalArgumentException_whenContentIsNull() {
        File file = tempDir.resolve("test.txt").toFile();
        String password = "password";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoFile.writeFile(file, password, null)
        );
        assertTrue(exception.getMessage().contains("content must not be null"));
    }

    @Test
    void writeFileAndReadFile_shouldWorkCorrectly() throws IOException {
        File file = tempDir.resolve("test.txt").toFile();
        String password = "password";
        String content = "test content";

        CryptoFile.writeFile(file, password, content);
        String read = CryptoFile.readFile(file, password);

        assertEquals(content, read);
    }
}
