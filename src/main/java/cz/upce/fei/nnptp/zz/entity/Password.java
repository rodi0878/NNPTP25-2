package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;

public class Password {

    private int id;
    private String saveSeriesOfCharacters;
    private HashMap<String, Parameter<?>> parameters;

    public Password() {
    }

    public Password(int id, String password) {
        this.id = id;
        this.saveSeriesOfCharacters = password;
    }

    public Password(int id, String password, HashMap<String, Parameter<?>> parameters) {
        this.id = id;
        this.saveSeriesOfCharacters = password;
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return saveSeriesOfCharacters;
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
    
}
