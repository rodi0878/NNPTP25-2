/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.List;

/**
 *
 * @author Roman
 */
public class PasswordDatabase {
    private File file;
    private String passwd;
    
    private List<Password> passwords;

    public PasswordDatabase(File file, String passwd) {
        this.file = file;
        this.passwd = passwd;
    }
    
    public void load() {
        // TODO: use JSON and CryptoFile to load
        // TODO: throw exceptions when error
    }
    
    public void save() {
        try {
            JSON json = new JSON();
            String out;
            if (passwords == null) {
                out = "[]";
            } else {
                out = json.toJson(passwords);
            }

            CryptoFile.writeFile(file, password, out);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to save password database", ex);
        }
    }
    
    public void add(Password password) {
        passwords.add(password);
    }
    
    public Password findEntryByTitle(String title) {
        for (Password password : passwords) {
            
            if (password.hasParameter(Parameter.StandardizedParameters.TITLE)) {
                Parameter.TextParameter titleParam;
                titleParam = (Parameter.TextParameter)password.getParameter(Parameter.StandardizedParameters.TITLE);
                if (titleParam.getValue().equals(titleParam)) {
                    return password;
                }
            }
        }
        return null;
    }
    
}
