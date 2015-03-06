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
 * myTabbedPaneDemoTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:42 PM
 */

/**
 *
 * @author Howard
 */
public class myTabbedPaneDemoTest extends TestCase {
    
    public myTabbedPaneDemoTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(myTabbedPaneDemoTest.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class myTabbedPaneDemo.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Top_click method, of class myTabbedPaneDemo.
     */
    public void testTop_click() {
        System.out.println("Top_click");
        
        JRadioButton obj = null;
        String value = "";
        ActionEvent e = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        instance.Top_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Left_click method, of class myTabbedPaneDemo.
     */
    public void testLeft_click() {
        System.out.println("Left_click");
        
        JRadioButton obj = null;
        String value = "";
        ActionEvent e = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        instance.Left_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Bottom_click method, of class myTabbedPaneDemo.
     */
    public void testBottom_click() {
        System.out.println("Bottom_click");
        
        JRadioButton obj = null;
        String value = "";
        ActionEvent e = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        instance.Bottom_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Right_click method, of class myTabbedPaneDemo.
     */
    public void testRight_click() {
        System.out.println("Right_click");
        
        JRadioButton obj = null;
        String value = "";
        ActionEvent e = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        instance.Right_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of TabDemo_stateChanged method, of class myTabbedPaneDemo.
     */
    public void testTabDemo_stateChanged() {
        System.out.println("TabDemo_stateChanged");
        
        JTabbedPane obj = null;
        String value = "";
        ChangeEvent e = null;
        myTabbedPaneDemo instance = new myTabbedPaneDemo();
        
        instance.TabDemo_stateChanged(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
