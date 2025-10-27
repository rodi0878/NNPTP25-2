package cz.upce.fei.nnptp.zz.entity;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        Type passwordType = new TypeToken<List<Password>>() {}.getType();
        return gson.fromJson(json,passwordType);
    }
}
