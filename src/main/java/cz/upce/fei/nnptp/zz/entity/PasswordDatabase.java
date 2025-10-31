package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.*;

public class PasswordDatabase {
    private File file;
    private String password;
    
    private List<PasswordEntry> passwords;

    /**
     * Creates PasswordDatabase object
     * @param file - file to store and load database from
     * @param password - string used to encrypt and decrypt database at rest
     */
    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;
        this.passwords = new ArrayList<>();
    }

    /**
     * Loads contents of a database from an encripted file. File and decription password is set druing Database creation.
     */
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

    /**
     * Saves contents of a database to an encripted file. File and encription password is set druing Database creation.
     */
    public void save() {
        String contents = new JSON().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }

    /**
     * Adds a password to database
     * @param password - Password to add
     */
    public void add(PasswordEntry password) {
        if (Objects.isNull(password))
            throw new NullPointerException("Password is null");

        if (passwords.stream().anyMatch(p -> p.getId() == password.getId()))
            throw new IllegalStateException("Password with this ID already exists");

        passwords.add(password);
    }

    /**
     * Searches the database for a password based on title parameter
     * @param title - title to search for. Only complete match is returned
     * @return optional containing first password which title parameter matches input or empty optional if password not found
     */
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
