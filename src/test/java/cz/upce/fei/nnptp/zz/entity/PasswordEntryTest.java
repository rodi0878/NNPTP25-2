package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEntryTest {

    public PasswordEntryTest() {
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

    /**
     * Test of getId method.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        PasswordEntry instance = new PasswordEntry(42, "pass");
        int expResult = 42;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        PasswordEntry instance = new PasswordEntry(1, "mypassword");
        String expResult = "mypassword";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParameters method.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put("title", new Parameter<>("TestTitle"));
        PasswordEntry instance = new PasswordEntry(1, "pwd", params);
        HashMap<String, Parameter<?>> result = instance.getParameters();
        assertEquals(params, result);
    }

    /**
     * Test of hasParameter method.
     */
    @Test
    public void testHasParameter() {
        System.out.println("hasParameter");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put("title", new Parameter<>("TitleValue"));
        PasswordEntry instance = new PasswordEntry(1, "pwd", params);
        assertTrue(instance.hasParameter("title"));
        assertFalse(instance.hasParameter("nonexistent"));
    }

    /**
     * Test of getParameter method.
     */
    @Test
    public void testGetParameter() {
        System.out.println("getParameter");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        Parameter<String> titleParam = new Parameter<>("TitleValue");
        params.put("title", titleParam);
        PasswordEntry instance = new PasswordEntry(1, "pwd", params);
        assertEquals(titleParam, instance.getParameter("title"));
        assertNull(instance.getParameter("nonexistent"));
    }

    /**
     * Test of toString method.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        params.put("title", new Parameter<>("TitleValue"));
        PasswordEntry instance = new PasswordEntry(1, "pwd", params);
        String result = instance.toString();
        assertTrue(result.contains("1"));
        assertTrue(result.contains("pwd"));
        assertTrue(result.contains("parameters"));
    }

    /**
     * Test of hashCode method.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        PasswordEntry instance1 = new PasswordEntry(1, "pwd", params);
        PasswordEntry instance2 = new PasswordEntry(1, "pwd", params);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    /**
     * Test of equals method.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        HashMap<String, Parameter<?>> params = new HashMap<>();
        PasswordEntry instance1 = new PasswordEntry(1, "pwd", params);
        PasswordEntry instance2 = new PasswordEntry(1, "pwd", params);
        PasswordEntry instance3 = new PasswordEntry(2, "other", params);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, "string");
    }

    /**
     * Test that parameters map is never null, even with default constructor.
     * This verifies that redundant null checks are not needed.
     */
    @Test
    public void testParametersNeverNull() {
        System.out.println("parametersNeverNull");
        // Test default constructor
        PasswordEntry instance1 = new PasswordEntry();
        assertNotNull(instance1.getParameters());
        assertTrue(instance1.getParameters().isEmpty());
        PasswordEntry instance2 = new PasswordEntry(1, "password");
        assertNotNull(instance2.getParameters());
        assertTrue(instance2.getParameters().isEmpty());
        
        // Test constructor with null parameters (should default to empty map)
        PasswordEntry instance3 = new PasswordEntry(2, "password", null);
        assertNotNull(instance3.getParameters());
        assertTrue(instance3.getParameters().isEmpty());
        
        // Verify methods work without null checks on all instances
        assertFalse(instance2.hasParameter("title"));
        assertNull(instance2.getParameter("title"));
        assertFalse(instance3.hasParameter("title"));
        assertNull(instance3.getParameter("title"));
    }

}
