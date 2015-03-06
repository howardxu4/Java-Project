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
/*
 * mySplitPaneDemoTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:41 PM
 */

/**
 *
 * @author Howard
 */
public class mySplitPaneDemoTest extends TestCase {
    
    public mySplitPaneDemoTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(mySplitPaneDemoTest.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class mySplitPaneDemo.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of DividerSize_focusGained method, of class mySplitPaneDemo.
     */
    public void testDividerSize_focusGained() {
        System.out.println("DividerSize_focusGained");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.DividerSize_focusGained(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of DividerSize_focusLost method, of class mySplitPaneDemo.
     */
    public void testDividerSize_focusLost() {
        System.out.println("DividerSize_focusLost");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.DividerSize_focusLost(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of DividerSize_click method, of class mySplitPaneDemo.
     */
    public void testDividerSize_click() {
        System.out.println("DividerSize_click");
        
        JTextField obj = null;
        String value = "";
        ActionEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.DividerSize_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of FirstMinimumSize_focusGained method, of class mySplitPaneDemo.
     */
    public void testFirstMinimumSize_focusGained() {
        System.out.println("FirstMinimumSize_focusGained");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.FirstMinimumSize_focusGained(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of FirstMinimumSize_focusLost method, of class mySplitPaneDemo.
     */
    public void testFirstMinimumSize_focusLost() {
        System.out.println("FirstMinimumSize_focusLost");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.FirstMinimumSize_focusLost(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of FirstMinimumSize_click method, of class mySplitPaneDemo.
     */
    public void testFirstMinimumSize_click() {
        System.out.println("FirstMinimumSize_click");
        
        JTextField obj = null;
        String value = "";
        ActionEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.FirstMinimumSize_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SecondMinimumSize_focusGained method, of class mySplitPaneDemo.
     */
    public void testSecondMinimumSize_focusGained() {
        System.out.println("SecondMinimumSize_focusGained");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.SecondMinimumSize_focusGained(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SecondMinimumSize_focusLost method, of class mySplitPaneDemo.
     */
    public void testSecondMinimumSize_focusLost() {
        System.out.println("SecondMinimumSize_focusLost");
        
        JTextField obj = null;
        String value = "";
        FocusEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.SecondMinimumSize_focusLost(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SecondMinimumSize_click method, of class mySplitPaneDemo.
     */
    public void testSecondMinimumSize_click() {
        System.out.println("SecondMinimumSize_click");
        
        JTextField obj = null;
        String value = "";
        ActionEvent e = null;
        mySplitPaneDemo instance = new mySplitPaneDemo();
        
        instance.SecondMinimumSize_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
