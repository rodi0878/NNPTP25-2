package cz.upce.fei.nnptp.zz.entity;

/**
 * Exception thrown when encryption operations fail.
 * <p>
 * This exception wraps underlying cryptographic or I/O errors that occur
 * during the encryption process.
 * </p>
 */
public class EncryptionException extends RuntimeException {

    /**
     * Creates a new {@code EncryptionException} with a detail message and cause.
     *
     * @param message the detail message describing the encryption failure
     * @param cause   the underlying exception that triggered this error
     */
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code EncryptionException} with a detail message.
     *
     * @param message the detail message describing the encryption failure
     */
    public EncryptionException(String message) {
        super(message);
    }
}

