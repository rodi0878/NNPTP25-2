package cz.upce.fei.nnptp.zz.entity;

/**
 * Exception thrown when decryption operations fail.
 * <p>
 * This exception wraps underlying cryptographic or I/O errors that occur
 * during the decryption process.
 * </p>
 */
public class DecryptionException extends RuntimeException {

    /**
     * Creates a new {@code DecryptionException} with a detail message and cause.
     *
     * @param message the detail message describing the decryption failure
     * @param cause   the underlying exception that triggered this error
     */
    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code DecryptionException} with a detail message.
     *
     * @param message the detail message describing the decryption failure
     */
    public DecryptionException(String message) {
        super(message);
    }
}

