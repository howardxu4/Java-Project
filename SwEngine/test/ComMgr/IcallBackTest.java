/*
 * IcallBackTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:37 PM
 */

package ComMgr;

import junit.framework.*;

/**
 *
 * @author Howard
 */
public class IcallBackTest extends TestCase {
    
    public IcallBackTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(IcallBackTest.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class ComMgr.IcallBack.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        IcallBack instance = new IcallBack();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Generated implementation of abstract class ComMgr.IcallBack. Please fill dummy bodies of generated methods.
     */
    private class IcallBackImpl implements IcallBack {

        public java.lang.Object callBack(int type, java.lang.Object info) {
            // TODO fill the body in order to provide useful implementation
            
            return null;
        }
    }

    
}
