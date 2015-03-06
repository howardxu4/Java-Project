/*
 * LogTraceTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:37 PM
 */

package ComMgr;

import junit.framework.*;
import java.io.*;
import java.util.Date;

/**
 *
 * @author Howard
 */
public class LogTraceTest extends TestCase {
    
    public LogTraceTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LogTraceTest.class);
        
        return suite;
    }

    /**
     * Test of Log method, of class ComMgr.LogTrace.
     */
    public void testLog() {
        System.out.println("Log");
        
        String s = "";
        LogTrace instance = new LogTrace();
        
        instance.Log(s);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chkLevel method, of class ComMgr.LogTrace.
     */
    public void testChkLevel() {
        System.out.println("chkLevel");
        
        int l = 0;
        LogTrace instance = new LogTrace();
        
        boolean expResult = true;
        boolean result = instance.chkLevel(l);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Trace method, of class ComMgr.LogTrace.
     */
    public void testTrace() {
        System.out.println("Trace");
        
        int l = 0;
        String s = "";
        LogTrace instance = new LogTrace();
        
        instance.Trace(l, s);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLevel method, of class ComMgr.LogTrace.
     */
    public void testSetLevel() {
        System.out.println("setLevel");
        
        int l = 0;
        
        LogTrace.setLevel(l);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLog method, of class ComMgr.LogTrace.
     */
    public void testSetLog() {
        System.out.println("setLog");
        
        String s = "";
        
        LogTrace.setLog(s);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLog method, of class ComMgr.LogTrace.
     */
    public void testGetLog() {
        System.out.println("getLog");
        
        LogTrace expResult = null;
        LogTrace result = LogTrace.getLog();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
