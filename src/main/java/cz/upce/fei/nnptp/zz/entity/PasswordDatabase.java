/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.*;

/**
 *
 * @author Roman
 */
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
        try {
            String jsonData = CryptoFile.readFile(file, password);

            if (jsonData == null || jsonData.isEmpty())
                return;

            JSON json = new JSON();
            List<Password> loadedPasswords = json.fromJson(jsonData);
            passwords.clear();
            passwords.addAll(loadedPasswords);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Unable to load password database");
        }
    }
    
    public void save() {
        String contents = new JSON().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }
    
    public void add(Password password) {
        passwords.add(password);
    }
    
    public Optional<Password> findEntryByTitle(String title) {
        for (Password password : passwords) {
            
            if (password.hasParameter(Parameter.StandardizedParameters.TITLE)) {
                Parameter.TextParameter titleParameter;
                titleParameter = (Parameter.TextParameter)password.getParameter(Parameter.StandardizedParameters.TITLE);

                if (titleParameter.getValue().equals(title)) {
                    return Optional.of(password);
                }
            }
        }
        return Optional.empty();
    }
    
}
