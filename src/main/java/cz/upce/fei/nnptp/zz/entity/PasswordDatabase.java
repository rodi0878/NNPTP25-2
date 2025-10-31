package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.*;

public class PasswordDatabase {
    private File file;
    private String password;
    
    private List<PasswordEntry> passwords;

    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;
        this.passwords = new ArrayList<>();
    }
    
    public void load() {
        try {
            String jsonData = CryptoFile.readFile(file, password);

            if (jsonData == null || jsonData.isEmpty())
                return;

            JSON json = new JSON();
            List<PasswordEntry> loadedPasswords = json.fromJson(jsonData);
            passwords.clear();
            passwords.addAll(loadedPasswords);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Unable to load password database", e);
        }
    }
    
    public void save() {
        String contents = new JSON().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }
    
    public void add(PasswordEntry password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password is null");

        if (passwords.stream().anyMatch(p -> p.getId() == password.getId()))
            throw new IllegalStateException("Password with this ID already exists");

        passwords.add(password);
    }
    
    public Optional<PasswordEntry> findEntryByTitle(String title) {
        for (PasswordEntry password : passwords) {
            
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
