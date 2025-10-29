package cz.upce.fei.nnptp.zz.controller;

import cz.upce.fei.nnptp.zz.entity.CryptoFile;
import cz.upce.fei.nnptp.zz.entity.JSON;
import cz.upce.fei.nnptp.zz.entity.Password;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise password manager - in console
 * - uses strong industry-based encryption algorithm
 * - stores your passwords and relevent information (website, login, notes, ...)
 * - allows you to simply manage all stored informations
 * 
 * 
 * 
 */
public class Main {
    private static final String FILENAME = "test.txt";
    private static final String ENCRYPTION_PASSWORD = "password";
    
    public static void main(String[] args) {
        // This is only temporary demo for existing API
        // Main should not be primarily updated
        // Main is currently not in focus for development
        // most development should focus on application APIs
        List<Password> passwords = new ArrayList<>();
        passwords.add(new Password(0, "sdfghjkl"));
        passwords.add(new Password(1, "ASDSAFafasdasdasdas"));
        passwords.add(new Password(2, "aaa-aaaa-"));
        String contents = new JSON().toJson(passwords);
        
        CryptoFile.writeFile(new File(FILENAME), ENCRYPTION_PASSWORD,  contents);
        
        String read = CryptoFile.readFile(new File(FILENAME), ENCRYPTION_PASSWORD);
        System.out.println(read);
        
    }
   
}
