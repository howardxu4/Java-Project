import junit.framework.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import GuiMgr.SwUtil.*;
import ComMgr.*;
import ObjMgr.*;
/*
 * myDemo1Test.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:42 PM
 */

/**
 *
 * @author Howard
 */
public class myDemo1Test extends TestCase {
    
    public myDemo1Test(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(myDemo1Test.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class myDemo1.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        myDemo1 instance = new myDemo1();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of process method, of class myDemo1.
     */
    public void testProcess() {
        System.out.println("process");
        
        int type = 0;
        Object info = null;
        myDemo1 instance = new myDemo1();
        
        instance.process(type, info);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
