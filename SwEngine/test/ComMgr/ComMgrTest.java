/*
 * ComMgrTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:33 PM
 */

package ComMgr;

import junit.framework.*;
import java.util.*;

/**
 *
 * @author Howard
 */
public class ComMgrTest extends TestCase {
    
    public ComMgrTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getComMgr method, of class ComMgr.ComMgr.
     */
    public void testGetComMgr() {
        System.out.println("getComMgr");
        
        ComMgr expResult = null;
        ComMgr result = ComMgr.getComMgr();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addListener method, of class ComMgr.ComMgr.
     */
    public void testAddListener() {
        System.out.println("addListener");
        
        int n = 0;
        Object l = null;
        ComMgr instance = new ComMgr();
        
        instance.addListener(n, l);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendMsg method, of class ComMgr.ComMgr.
     */
    public void testSendMsg() {
        System.out.println("sendMsg");
        
        int n = 0;
        int type = 0;
        Object info = null;
        ComMgr instance = new ComMgr();
        
        Object expResult = null;
        Object result = instance.sendMsg(n, type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postMsg method, of class ComMgr.ComMgr.
     */
    public void testPostMsg() {
        System.out.println("postMsg");
        
        int n = 0;
        int type = 0;
        Object info = null;
        ComMgr instance = new ComMgr();
        
        instance.postMsg(n, type, info);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of brcstMsg method, of class ComMgr.ComMgr.
     */
    public void testBrcstMsg() {
        System.out.println("brcstMsg");
        
        int type = 0;
        Object info = null;
        ComMgr instance = new ComMgr();
        
        instance.brcstMsg(type, info);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ComMgrTest.class);
        
        return suite;
    }
    
}
