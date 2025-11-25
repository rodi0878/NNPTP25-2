package cz.upce.fei.nnptp.zz.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for encrypting and decrypting files using a symmetric cipher (DES).
 * <p>
 * Provides methods to read and write encrypted content to a file with a given password.
 * </p>
 */
public class CryptoFile {

    private static final String ENCRYPTION_ALGORITHM = "DES";
    private static final String CIPHER_TRANSFORMATION = "DES/ECB/PKCS5Padding";

    private CryptoFile() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    private static Cipher initCipher(int mode, String password) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        Objects.requireNonNull(password, "Password must not be null");

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        SecretKey secretKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8),
                ENCRYPTION_ALGORITHM);
        cipher.init(mode, secretKey);
        return cipher;
    }

    /**
     * Reads and decrypts the content of a file using the provided password.
     *
     * @param file     the file to read from
     * @param password the password used for decryption
     * @return the decrypted content as a String, or null if an error occurs
     */
    public static String readFile(File file, String password) {
        Objects.requireNonNull(file, "file must not be null");
        Objects.requireNonNull(password, "password must not be null");

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // Build and INIT the cipher BEFORE wrapping it in CipherInputStream.
            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, password);

            try (CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
                 DataInputStream dataInputStream = new DataInputStream(cipherInputStream)) {
                // Matches write side: DataOutputStream#writeUTF
                return dataInputStream.readUTF();
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IOException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, "Decrypt failed", ex);
            return null;
        }
    }
    /**
     * Encrypts and writes the given content to a file using the provided password.
     *
     * @param file     the file to write to
     * @param password the password used for encryption
     * @param content  the content to encrypt and write
     */
    public static void  writeFile(File file, String password, String content) {
        Objects.requireNonNull(file, "file must not be null");
        Objects.requireNonNull(password, "password must not be null");
        if (content == null) content = "";

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            // Build and INIT the cipher BEFORE wrapping it in CipherOutputStream.
            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, password);

            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
                 DataOutputStream dataOutputStream = new DataOutputStream(cipherOutputStream)) {
                // Length-prefixed UTF format, pairs with readUTF()
                dataOutputStream.writeUTF(content);
                dataOutputStream.flush();
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IOException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, "Encrypt failed", ex);
        }
    }
    
}
