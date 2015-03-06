/*
 * IforwardTest.java
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
public class IforwardTest extends TestCase {
    
    public IforwardTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(IforwardTest.class);
        
        return suite;
    }

    /**
     * Test of setMethodDecl method, of class ComMgr.Iforward.
     */
    public void testSetMethodDecl() {
        System.out.println("setMethodDecl");
        
        String[] decl = null;
        Iforward instance = new Iforward();
        
        instance.setMethodDecl(decl);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserType method, of class ComMgr.Iforward.
     */
    public void testSetUserType() {
        System.out.println("setUserType");
        
        int t = 0;
        Iforward instance = new Iforward();
        
        instance.setUserType(t);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserType method, of class ComMgr.Iforward.
     */
    public void testGetUserType() {
        System.out.println("getUserType");
        
        Iforward instance = new Iforward();
        
        int expResult = 0;
        int result = instance.getUserType();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethodDecl method, of class ComMgr.Iforward.
     */
    public void testGetMethodDecl() {
        System.out.println("getMethodDecl");
        
        Iforward instance = new Iforward();
        
        String[] expResult = null;
        String[] result = instance.getMethodDecl();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Generated implementation of abstract class ComMgr.Iforward. Please fill dummy bodies of generated methods.
     */
    private class IforwardImpl implements Iforward {

        public void setMethodDecl(java.lang.String[] decl) {
            // TODO fill the body in order to provide useful implementation
            
        }

        public void setUserType(int t) {
            // TODO fill the body in order to provide useful implementation
            
        }

        public int getUserType() {
            // TODO fill the body in order to provide useful implementation
            
            return 0;
        }

        public java.lang.String[] getMethodDecl() {
            // TODO fill the body in order to provide useful implementation
            
            return null;
        }
    }

    
}
