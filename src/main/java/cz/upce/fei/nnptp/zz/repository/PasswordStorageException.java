package cz.upce.fei.nnptp.zz.repository;

/**
 * Wraps IO or encryption-related problems that occur while storing or retrieving passwords.
 */
public class PasswordStorageException extends RuntimeException {

    /**
     * Creates a new {@code PasswordStorageException} with a detail message and cause.
     *
     * @param message the detail message describing the problem
     * @param cause   the underlying exception that triggered this error
     */
    public PasswordStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code PasswordStorageException} with a detail message.
     *
     * @param message the detail message describing the problem
     */
    public PasswordStorageException(String message) {
        super(message);
    }
}