package cz.upce.fei.nnptp.zz.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Reads and decrypts the content of a file using the provided password.
     *
     * @param file     the file to read from
     * @param password the password used for decryption
     * @return the decrypted content as a String, or null if an error occurs
     */
    public static String readFile(File file, String password) {
        try (final var fileInputStream = new FileInputStream(file)){
            // TODO...
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
            SecretKey secretKey = new SecretKeySpec(password.getBytes(), "DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            DataInputStream dataInputStream = new DataInputStream(cipherInputStream);
            String result = dataInputStream.readUTF();
            dataInputStream.close();
            cipher.doFinal();
            
            return result;        
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException |
                 IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * Encrypts and writes the given content to a file using the provided password.
     *
     * @param file     the file to write to
     * @param password the password used for encryption
     * @param content  the content to encrypt and write
     */
    public static void  writeFile(File file, String password, String content) {
        try (final var fileOutputStream = new FileOutputStream(file)) {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
            SecretKey secretKey = new SecretKeySpec(password.getBytes(), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            DataOutputStream dataOutputStream = new DataOutputStream(cipherOutputStream);
            dataOutputStream.writeUTF(content);
            dataOutputStream.close();
            cipher.doFinal();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException |
                 IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
