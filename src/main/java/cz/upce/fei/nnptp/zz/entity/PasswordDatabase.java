package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.*;

public class PasswordDatabase {
    private File file;
    private String password;
    
    private List<Password> passwords;

    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;
        this.passwords = new ArrayList<>();
    }
    
    public void load() {
        // TODO: use JSON and CryptoFile to load
        // TODO: throw exceptions when error
    }
    
    public void save() {
        String contents = new JSON().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }
    
    public void add(Password password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password is null");

        if (passwords.stream().anyMatch(p -> p.getId() == password.getId()))
            throw new IllegalStateException("Password with this ID already exists");

        passwords.add(password);
    }
    
    public Optional<Password> findEntryByTitle(String title) {
        for (Password password : passwords) {
            
            if (password.hasParameter(Parameter.StandardizedParameters.TITLE)) {
                Parameter<?> titleParameter;
                titleParameter = password.getParameter(Parameter.StandardizedParameters.TITLE);

                if (titleParameter.getValue().equals(title)) {
                    return Optional.of(password);
                }
            }
        }
        return Optional.empty();
    }
    
}
