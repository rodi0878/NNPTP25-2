package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JSONTest {
    private final JSON jsonUtil = new JSON();

    @Test
    void toJson_ThrowsException_WhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonUtil.toJson(null)
        );
        assertTrue(exception.getMessage().contains("Password list cannot be null"));
    }

    @Test
    void toJson_ThrowsException_WhenListContainsNullEntry() {
        List<PasswordEntry> list = new ArrayList<>();
        list.add(new PasswordEntry(1, "abc"));
        list.add(null); // allowed

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonUtil.toJson(list)
        );
        assertTrue(exception.getMessage().contains("Cannot serialize null Password object"));
    }

    @Test
    void toJson_ReturnsValidJson_WhenListIsEmpty() {
        List<PasswordEntry> empty = List.of();

        String result = jsonUtil.toJson(empty);

        assertNotNull(result, "Result should not be null");
        assertEquals("[]", result.trim(), "Empty list should serialize to []");
    }

    @Test
    void toJson_SerializesSimpleEntryCorrectly() {
        PasswordEntry entry = new PasswordEntry(1, "secret123");
        List<PasswordEntry> list = List.of(entry);

        String json = jsonUtil.toJson(list);

        assertNotNull(json);
        assertTrue(json.contains("\"id\": 1"), "Should contain id");
        assertTrue(json.contains("\"password\": \"secret123\""), "Should contain password");
    }

    @Test
    void fromJson_ReturnsEmptyList_WhenInputIsNull() {
        List<PasswordEntry> result = jsonUtil.fromJson(null);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty when input is null");
    }

    @Test
    void fromJson_ReturnsEmptyList_WhenInputIsEmptyString() {
        List<PasswordEntry> result = jsonUtil.fromJson("");

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty when input is empty string");
    }

    @Test
    void fromJson_ReturnsList_WhenValidJsonProvided() {
        String json = "[{\"id\":1,\"password\":\"secret123\"}]";

        List<PasswordEntry> result = jsonUtil.fromJson(json);

        assertEquals(1, result.size(), "Should parse one password entry");
        assertEquals(1, result.get(0).getId());
        assertEquals("secret123", result.get(0).getPassword());
    }
}
