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


    @Test
    void equalsReturnsTrueForSameInstance() {
        Parameter<String> param = new Parameter<>();
        param.setValue("same");

        assertEquals(param, param);
    }

    @Test
    void equalsReturnsTrueForSameValue() {
        Parameter<String> p1 = new Parameter<>();
        p1.setValue("value");
        Parameter<String> p2 = new Parameter<>();
        p2.setValue("value");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentValue() {
        Parameter<String> p1 = new Parameter<>();
        p1.setValue("value1");
        Parameter<String> p2 = new Parameter<>();
        p2.setValue("value2");

        assertNotEquals(p1, p2);
    }

    @Test
    void equalsReturnsFalseForNullAndDifferentType() {
        Parameter<String> param = new Parameter<>();
        param.setValue("value");

        assertNotEquals(param, null);
        assertNotEquals(param, "value");
    }

    @Test
    void toStringContainsClassNameAndValue() {
        Parameter<String> param = new Parameter<>();
        param.setValue("test-value");

        String text = param.toString();

        assertTrue(text.contains("Parameter"));
        assertTrue(text.contains("test-value"));
    }
}
