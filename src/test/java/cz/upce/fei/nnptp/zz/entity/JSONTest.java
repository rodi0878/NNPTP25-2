package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JSONTest {
    private final JSON jsonUtil = new JSON();

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
