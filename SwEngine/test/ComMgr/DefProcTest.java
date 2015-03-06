/*
 * DefProcTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:37 PM
 */

package ComMgr;

import junit.framework.*;
import java.util.*;

/**
 *
 * @author Howard
 */
public class DefProcTest extends TestCase {
    
    public DefProcTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DefProcTest.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class ComMgr.DefProc.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        DefProc instance = new DefProc();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
