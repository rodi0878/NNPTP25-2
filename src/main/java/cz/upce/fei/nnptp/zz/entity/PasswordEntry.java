package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a password entry containing an identifier, a password value,
 * and an associated set of additional custom parameters.
 * <p>
 * Parameters are stored in a map where keys represent parameter names
 * and values are {@link Parameter} objects holding typed metadata.
 * These may include information such as titles, descriptions, websites,
 * expiration dates, or any other custom attributes.
 * </p>
 */
public class PasswordEntry {

    /** Unique identifier of this password entry. */
    private int id;

    /** Password value stored in this entry. */
    private String password;

    /**
     * A map of additional parameters bound to this entry.
     * <p>
     * Never {@code null}. If no parameters are provided, it is initialized as an empty map.
     * </p>
     */
    private HashMap<String, Parameter<?>> parameters = new HashMap<>();

    /**
     * Default constructor.
     * <p>
     * Creates a password entry with no predefined values.
     * The parameters map is always initialized.
     * </p>
     */
    public PasswordEntry() {
    }

    /**
     * Creates a password entry with the specified id and password.
     *
     * @param id       unique identifier of the entry
     * @param password password associated with this entry
     */
    public PasswordEntry(int id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Creates a password entry with the specified id, password,
     * and a provided map of parameters.
     * <p>
     * If {@code parameters} is {@code null}, an empty map is created to ensure
     * internal consistency.
     * </p>
     *
     * @param id         unique identifier of the entry
     * @param password   password associated with this entry
     * @param parameters parameter map, or {@code null} to use an empty map
     */
    public PasswordEntry(int id, String password, HashMap<String, Parameter<?>> parameters) {
        this.id = id;
        this.password = password;
        this.parameters = (parameters != null) ? parameters : new HashMap<>();
    }

    /**
     * Returns the unique identifier of this password entry.
     *
     * @return entry ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the password associated with this entry.
     *
     * @return stored password value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns all parameters assigned to this entry.
     * <p>
     * The returned map is never {@code null}.
     * </p>
     *
     * @return map of parameter names mapped to {@link Parameter} objects
     */
    public HashMap<String, Parameter<?>> getParameters() {
        return parameters;
    }

    /**
     * Checks whether a parameter with the given name exists.
     *
     * @param title name of the parameter
     * @return {@code true} if the parameter exists, otherwise {@code false}
     */
    boolean hasParameter(String title) {
        return parameters.containsKey(title);
    }

    /**
     * Retrieves a parameter by its name.
     *
     * @param key name of the parameter
     * @return the corresponding {@link Parameter}, or {@code null} if not found
     */
    public Parameter<?> getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * Returns a descriptive string representation of the password entry.
     *
     * @return string including id, password, and parameters
     */
    @Override
    public String toString() {
        return "PasswordEntry{" + "id=" + id + ", password=" + password + ", parameters=" + parameters + '}';
    }

    /**
     * Computes a hash code for this password entry
     * based on its id, password, and parameters.
     *
     * @return computed hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.password);
        hash = 29 * hash + Objects.hashCode(this.parameters);
        return hash;
    }

    /**
     * Compares this entry to another object for equality.
     * <p>
     * Two entries are equal if they share the same id, password
     * and parameter map.
     * </p>
     *
     * @param obj the object to compare to
     * @return {@code true} if entries are equal, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PasswordEntry other = (PasswordEntry) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return Objects.equals(this.parameters, other.parameters);
    }
}
