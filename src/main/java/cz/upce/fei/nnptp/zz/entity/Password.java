package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Roman
 */
public class Password {

    private int id;
    private String password;
    private HashMap<String, Parameter> parameters;

    public Password() {
    }

    public Password(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public Password(int id, String password, HashMap<String, Parameter> parameters) {
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

    public HashMap<String, Parameter> getParameters() {
        return parameters;
    }

    boolean hasParameter(String TITLE) {
        return parameters != null && parameters.containsKey(TITLE);
    }

    public Parameter getParameter(String key) {
        return (parameters != null) ? parameters.get(key) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Password otherPassword))
            return false;

        return this.id == otherPassword.id &&
                Objects.equals(this.password, otherPassword.password) &&
                Objects.equals(this.parameters, otherPassword.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, parameters);
    }

}
