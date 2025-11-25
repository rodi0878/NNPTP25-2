package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a single password entry containing an identifier, password,
 * and an optional collection of additional parameters.
 * <p>
 * The parameters map may include metadata such as title, description,
 * website, expiration date, or any other custom information represented
 * by {@link Parameter} objects.
 * </p>
 */
public class PasswordEntry {

    /** Unique identifier for the password entry. */
    private int id;

    /** The password value associated with this entry. */
    private String password;

    /**
     * A map of named parameters containing additional information related to the password.
     * Keys represent parameter names, and values are {@link Parameter} objects.
     */
    private HashMap<String, Parameter<?>> parameters;

    /**
     * Default constructor.
     * <p>
     * Creates an empty password entry with no initial values.
     * </p>
     */
    public PasswordEntry() {
    }

    /**
     * Constructs a new password entry using the provided id and password.
     *
     * @param id       unique identifier of this entry
     * @param password password value to store
     */
    public PasswordEntry(int id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Constructs a new password entry using the provided id, password,
     * and a map of parameters.
     *
     * @param id         unique identifier of this entry
     * @param password   password value to store
     * @param parameters additional parameters related to this entry
     */
    public PasswordEntry(int id, String password, HashMap<String, Parameter<?>> parameters) {
        this.id = id;
        this.password = password;
        this.parameters = parameters;
    }

    /**
     * Returns the unique identifier of this password entry.
     *
     * @return the ID of the entry
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the password stored in this entry.
     *
     * @return the password value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the map of parameters associated with this entry.
     *
     * @return a map of parameter names to {@link Parameter} objects,
     *         or {@code null} if no parameters are set
     */
    public HashMap<String, Parameter<?>> getParameters() {
        return parameters;
    }

    /**
     * Checks whether a parameter with the given name exists in this entry.
     *
     * @param title the name of the parameter to check
     * @return {@code true} if the parameter exists; {@code false} otherwise
     */
    boolean hasParameter(String title) {
        return parameters != null && parameters.containsKey(title);
    }

    /**
     * Returns a parameter associated with the given key.
     *
     * @param key the name of the parameter to retrieve
     * @return the {@link Parameter} object, or {@code null} if no such parameter exists
     */
    public Parameter<?> getParameter(String key) {
        return (parameters != null) ? parameters.get(key) : null;
    }

    /**
     * Returns a string representation of this password entry.
     *
     * @return a string describing the entry's ID, password, and parameters
     */
    @Override
    public String toString() {
        return "PasswordEntry{" + "id=" + id + ", password=" + password + ", parameters=" + parameters + '}';
    }

    /**
     * Computes a hash code for this password entry.
     *
     * @return the computed hash code
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
     * Compares this password entry to another object for equality.
     * <p>
     * Two password entries are considered equal if they have the same id,
     * password value, and parameter map.
     * </p>
     *
     * @param obj the object to compare with
     * @return {@code true} if both objects represent the same entry;
     *         {@code false} otherwise
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
