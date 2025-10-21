package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterTest {

    @Test
    void testTextParameterDefaultValidation() {
        Parameter.TextParameter param = new Parameter.TextParameter("Hello");
        assertTrue(param.isValid());

        param.setValue(" ");
        assertFalse(param.isValid());
    }

    @Test
    void testDateTimeParameterDefaultValidation() {
        Parameter.DateTimeParameter future = new Parameter.DateTimeParameter(LocalDateTime.now().plusDays(1));
        assertTrue(future.isValid());

        Parameter.DateTimeParameter past = new Parameter.DateTimeParameter(LocalDateTime.now().minusDays(1));
        assertFalse(past.isValid());
    }

    @Test
    void testPasswordParameterDefaultValidation() {
        Parameter.PasswordParameter strong = new Parameter.PasswordParameter("Secure123");
        assertTrue(strong.isValid());

        Parameter.PasswordParameter weak = new Parameter.PasswordParameter("abc");
        assertFalse(weak.isValid());
    }

    @Test
    void testCustomValidator() {
        Parameter.TextParameter custom = new Parameter.TextParameter("short");
        custom.setValidator(p -> ((Parameter.TextParameter) p).getValue().length() > 10);
        assertFalse(custom.isValid());
    }
}
