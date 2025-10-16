/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Roman
 */
public class JSON {
    
    
    public String toJson(List<Password> passwords)  {
        // TODO: support all parameters!!!
        String output = "[";
        for (Password password : passwords) {
            if (!output.isEmpty() && !output.equals("["))
                output += ",";
            output += "{";
            output += "id:" + password.getId() + ",";
            output += "password:\"" + password.getPassword()+"\"";
            
            output += "}";
        }
        output += "]";
        
        return output;
    }
    
    public List<Password> fromJson(String json) {
        String[] v = json.replace("[","").replace("]","").split("}");
        return Arrays.stream(v).map(f ->
                f.replace("{id:","")
                .replace(",{id:","")
                .replace(",password:\"","°°°°")
                .replace("\"",""))
            .map(
                    s -> {
                        var tmp = s.split("°°°°");
                        int id = Integer.parseInt(tmp[0].replace(",",""));
                        return new Password(id,tmp[1]);
                    }
            ).toList();
    }
}
