package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class PasswordTest {
    
    public PasswordTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        PasswordEntry ppwd = new PasswordEntry(0, "pass");
        assertEquals("pass", ppwd.getPassword());
    }

    @Test
    public void methods_are_null_safe_when_parameters_is_null() {
        // Arrange
        PasswordEntry p = new PasswordEntry(0, "pass");

        // Act
        boolean hasTitle = p.hasParameter(Parameter.StandardizedParameters.TITLE);
        Parameter rawParam = p.getParameter(Parameter.StandardizedParameters.TITLE);

        // Assert
        assertFalse(hasTitle, "With no parameter map, hasParameter should return false");
        assertNull(rawParam, "With no parameter map, getParameter should return null");
    }

    @Test
    public void methods_work_when_parameters_provided() {
        // Arrange
        HashMap<String, Parameter<?>> map = new HashMap<>();
        Parameter<?> titleParam = new Parameter<>("MyTitle");
        map.put(Parameter.StandardizedParameters.TITLE, titleParam);

        PasswordEntry p = new PasswordEntry(1, "secret", map);

        // Act
        boolean hasTitle = p.hasParameter(Parameter.StandardizedParameters.TITLE);
        Parameter<?> rawParam = p.getParameter(Parameter.StandardizedParameters.TITLE);

        // Assert
        assertTrue(hasTitle, "TITLE should exist");
        assertNotNull(rawParam, "getParameter should not return null, when TITLE is in map");
        Parameter<?> typed = assertInstanceOf(Parameter.class, rawParam);
        assertEquals("MyTitle", typed.getValue(), "Title value should be MyTitle");
    }
    
}
