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

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Gson gson  = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(DATE_FORMAT.format(src));
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString(), DATE_FORMAT);
                }
            })
            .setPrettyPrinting()
            .create();

    public String toJson(List<Password> passwords)  {
        if (passwords == null)
            throw new NullPointerException("Password list cannot be null");
        for (Password p : passwords) {
            if (p == null) {
                throw new NullPointerException("Cannot serialize null Password object");
            }
        }

        return gson.toJson(passwords);
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
