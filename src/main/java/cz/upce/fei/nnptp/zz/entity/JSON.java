/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.util.List;

/**
 *
 * @author Roman
 */
public class JSON {
    
    
    public String toJson(List<Password> passwords)  {
        // TODO: support all parameters!!!
        StringBuilder output = new StringBuilder("[");
        for (Password password : passwords) {
            if ((output.length() > 0) && !output.toString().equals("["))
                output.append(",");
            output.append("{");
            output.append("id:").append(password.getId()).append(",");
            output.append("password:\"").append(password.getPassword()).append("\"");
            
            output.append("}");
        }
        output.append("]");
        
        return output.toString();
    }
    
    public List<Password> fromJson(String json) {
        throw new RuntimeException("NYI");
    }
}
