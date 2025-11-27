package cz.upce.fei.nnptp.zz.entity;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class JSON {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final Type PASSWORD_ENTRY_LIST = new TypeToken<List<PasswordEntry>>() {}.getType();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(DATE_FORMAT.format(src))
            )
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                            LocalDateTime.parse(json.getAsString(), DATE_FORMAT))
            .setPrettyPrinting()
            .create();

    public String toJson(List<PasswordEntry> passwords)  {
        Objects.requireNonNull(passwords, "Password list cannot be null");

        // validate entries
        passwords.forEach(p ->
                Objects.requireNonNull(p, "Cannot serialize null Password object")
        );

        return gson.toJson(passwords);
    }
    
    public List<PasswordEntry> fromJson(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        return gson.fromJson(json, PASSWORD_ENTRY_LIST);
    }
}
