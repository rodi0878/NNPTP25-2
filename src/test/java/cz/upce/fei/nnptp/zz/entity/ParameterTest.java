package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParameterTest {

    @Test
    void defaultValidatorRejectsNullValue() {
        Parameter<String> param = new Parameter<>();
        assertThrows(IllegalArgumentException.class, () -> param.setValue(null));
    }

    @Test
    void acceptsNonNullValue() {
        Parameter<String> param = new Parameter<>();
        assertDoesNotThrow(() -> param.setValue("ok"));
        assertEquals("ok", param.getValue());
    }

    @Test
    void supportsMultipleCustomValidators() {
        Parameter<Integer> param = new Parameter<>();
        param.addValidator(v -> v > 0);
        param.addValidator(v -> v < 100);

        assertThrows(IllegalArgumentException.class, () -> param.setValue(-1));
        assertThrows(IllegalArgumentException.class, () -> param.setValue(200));
        assertDoesNotThrow(() -> param.setValue(50));
    }

    @Test
    void validatesInitialValueInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Parameter<>(null));
    }

    @Test
    void allowsUserDefinedValidationRules() {
        Parameter<String> param = new Parameter<>();
        param.addValidator(v -> v.length() >= 3);

        assertThrows(IllegalArgumentException.class, () -> param.setValue("ab"));
        assertDoesNotThrow(() -> param.setValue("abcd"));
    }
}
