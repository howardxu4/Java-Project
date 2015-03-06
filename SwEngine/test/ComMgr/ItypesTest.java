/*
 * ItypesTest.java
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
public class ItypesTest extends TestCase {
    
    public ItypesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ItypesTest.class);
        
        return suite;
    }

    /**
     * Generated implementation of abstract class ComMgr.Itypes. Please fill dummy bodies of generated methods.
     */
    private class ItypesImpl implements Itypes {
    }

    
}
