/*
 * SwdemoSuite.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:42 PM
 */

package swdemo;

import junit.framework.*;

/**
 *
 * @author Howard
 */
public class SwdemoSuite extends TestCase {
    
    public SwdemoSuite(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }


    /**
     * suite method automatically generated by JUnit module
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("SwdemoSuite");
        suite.addTest(swdemo.MainTest.suite());
        return suite;
    }
    
}
