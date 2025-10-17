/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

/**
 *
 * @author Roman
 */
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
        Password ppwd = new Password(0, "pass");
        assertEquals("pass", ppwd.getPassword());
    }

    @Test
    public void methods_are_null_safe_when_parameters_is_null() {
        // Arrange
        Password p = new Password(0, "pass");

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
        HashMap<String, Parameter> map = new HashMap<>();
        Parameter.TextParameter titleParam = new Parameter.TextParameter("MyTitle");
        map.put(Parameter.StandardizedParameters.TITLE, titleParam);

        Password p = new Password(1, "secret", map);

        // Act
        boolean hasTitle = p.hasParameter(Parameter.StandardizedParameters.TITLE);
        Parameter rawParam = p.getParameter(Parameter.StandardizedParameters.TITLE);

        // Assert
        assertTrue(hasTitle, "TITLE should exist");
        assertNotNull(rawParam, "getParameter should not return null, when TITLE is in map");
        Parameter.TextParameter typed = assertInstanceOf(Parameter.TextParameter.class, rawParam);
        assertEquals("MyTitle", typed.getValue(), "Title value should be MyTitle");
    }
    
}
