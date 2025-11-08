package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import java.util.Objects;

public class PasswordEntry {

    private int id;
    private String password;
    private HashMap<String, Parameter<?>> parameters;

    public PasswordEntry() {
    }

    public PasswordEntry(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public PasswordEntry(int id, String password, HashMap<String, Parameter<?>> parameters) {
        this.id = id;
        this.password = password;
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public HashMap<String, Parameter<?>> getParameters() {
        return parameters;
    }

    boolean hasParameter(String title) {
        return parameters != null && parameters.containsKey(title);
    }

    public Parameter<?> getParameter(String key) {
        return (parameters != null) ? parameters.get(key) : null;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" + "id=" + id + ", password=" + password + ", parameters=" + parameters + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.password);
        hash = 29 * hash + Objects.hashCode(this.parameters);
        return hash;
    }

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
