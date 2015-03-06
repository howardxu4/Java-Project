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
 * myComboBoxDemoTest.java
 * JUnit based test
 *
 * Created on April 17, 2007, 5:42 PM
 */

/**
 *
 * @author Howard
 */
public class myComboBoxDemoTest extends TestCase {
    
    public myComboBoxDemoTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(myComboBoxDemoTest.class);
        
        return suite;
    }

    /**
     * Test of callBack method, of class myComboBoxDemo.
     */
    public void testCallBack() {
        System.out.println("callBack");
        
        int type = 0;
        Object info = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        Object expResult = null;
        Object result = instance.callBack(type, info);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chgHairEyesMouth_click method, of class myComboBoxDemo.
     */
    public void testChgHairEyesMouth_click() {
        System.out.println("chgHairEyesMouth_click");
        
        JComboBox obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.chgHairEyesMouth_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chgHair_click method, of class myComboBoxDemo.
     */
    public void testChgHair_click() {
        System.out.println("chgHair_click");
        
        JComboBox obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.chgHair_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chgEyes_click method, of class myComboBoxDemo.
     */
    public void testChgEyes_click() {
        System.out.println("chgEyes_click");
        
        JComboBox obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.chgEyes_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chgMouth_click method, of class myComboBoxDemo.
     */
    public void testChgMouth_click() {
        System.out.println("chgMouth_click");
        
        JComboBox obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.chgMouth_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Save_click method, of class myComboBoxDemo.
     */
    public void testSave_click() {
        System.out.println("Save_click");
        
        JButton obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.Save_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Delete_click method, of class myComboBoxDemo.
     */
    public void testDelete_click() {
        System.out.println("Delete_click");
        
        JButton obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.Delete_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Random_click method, of class myComboBoxDemo.
     */
    public void testRandom_click() {
        System.out.println("Random_click");
        
        JButton obj = null;
        String value = "";
        ActionEvent e = null;
        myComboBoxDemo instance = new myComboBoxDemo();
        
        instance.Random_click(obj, value, e);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
