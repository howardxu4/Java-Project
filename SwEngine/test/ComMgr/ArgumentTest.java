/*
 * ArgumentTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:37 PM
 */

package ComMgr;

import junit.framework.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author Howard
 */
public class ArgumentTest extends TestCase {
    
    public ArgumentTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ArgumentTest.class);
        
        return suite;
    }

    /**
     * Test of getArgument method, of class ComMgr.Argument.
     */
    public void testGetArgument() {
        System.out.println("getArgument");
        
        String key = "";
        Argument instance = new Argument();
        
        Object expResult = null;
        Object result = instance.getArgument(key);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getArgumentNames method, of class ComMgr.Argument.
     */
    public void testGetArgumentNames() {
        System.out.println("getArgumentNames");
        
        Argument instance = new Argument();
        
        Enumeration expResult = null;
        Enumeration result = instance.getArgumentNames();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setArgument method, of class ComMgr.Argument.
     */
    public void testSetArgument() {
        System.out.println("setArgument");
        
        String key = "";
        Object argv = null;
        Argument instance = new Argument();
        
        boolean expResult = true;
        boolean result = instance.setArgument(key, argv);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dump method, of class ComMgr.Argument.
     */
    public void testDump() {
        System.out.println("dump");
        
        Argument instance = new Argument();
        
        String expResult = "";
        String result = instance.dump();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
