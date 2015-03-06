/**
*******************************************************************************
*
*   (myDemo) Generated on Fri Dec 13 11:46:07 GMT-08:00 2002
*
*******************************************************************************
*/
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
public class myDemo implements IcallBack
{
   private JTabbedPane tabp_TabbedPane;
   private JEditorPane editp_SunSource;
   private JPanel panel_mySplitPaneDemo;
   private JSplitPane split_SplitPane;
   private JLabel label_moon;
   private JLabel label_earth;
   private JEditorPane editp_MySource;
   private JEditorPane editp_MyData;

/****************************************
*            constructor
*****************************************/
   public myDemo () {
      ComMgr.getComMgr().addListener(ComMgr.USER01, this);
      // user's special init (before load)
   }

/****************************************
*            init run-time variables
*****************************************/
   private void init() {
      tabp_TabbedPane = (JTabbedPane)GuiMgr.SwUtil.getObject("TabbedPane");
      editp_SunSource = (JEditorPane)GuiMgr.SwUtil.getObject("SunSource");
      panel_mySplitPaneDemo = (JPanel)GuiMgr.SwUtil.getObject("mySplitPaneDemo");
      split_SplitPane = (JSplitPane)GuiMgr.SwUtil.getObject("SplitPane");
      label_moon = (JLabel)GuiMgr.SwUtil.getObject("moon");
      label_earth = (JLabel)GuiMgr.SwUtil.getObject("earth");
      editp_MySource = (JEditorPane)GuiMgr.SwUtil.getObject("MySource");
      editp_MyData = (JEditorPane)GuiMgr.SwUtil.getObject("MyData");
      // user's variable init (after load)
      chgDemo.getDemo().setPages(editp_SunSource, editp_MySource, editp_MyData);
      chgDemo.getDemo().chkDemo(tabp_TabbedPane);
   }

/****************************************
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
		 GuiMgr.SwUtil.runSafety(this, "process", 
            new Object[]{new Integer(type), info}, null);
      else {   // user defind message handler
      }
      return null;
   }

/****************************************
*         system generated methods
*****************************************/
   public void process(int type, Object info) {
      Argument argv = (Argument)info;
      String name = (String)argv.getArgument(NAME);

   // the following useful info maybe referred on some case
   // Object obj = argv.getArgument(OBJECT);
   // int eType = ((Integer)argv.getArgument(TYPE)).intValue();
   // Object event = argv.getArgument(EVENT);
   // String value = (String)argv.getArgument(VALUE);

      switch (type) {
      case BUTTON :
         if (name.equals("SwSplitPane")) {
             chgDemo.getDemo().chgNthDemo("SplitPaneDemo", tabp_TabbedPane, 0);
             chgDemo.getDemo().chgMyDemo("mySplitPaneDemo", tabp_TabbedPane, 2);
         }
         else if (name.equals("SwComboBox")) {
             chgDemo.getDemo().chgNthDemo("ComboBoxDemo", tabp_TabbedPane, 0);
             chgDemo.getDemo().chgMyDemo("myComboBoxDemo", tabp_TabbedPane, 2);
         }
         else if (name.equals("SwTabbedPane")) {
             chgDemo.getDemo().chgNthDemo("TabbedPaneDemo", tabp_TabbedPane, 0);
             chgDemo.getDemo().chgMyDemo("myTabbedPaneDemo", tabp_TabbedPane, 2);
         }
         else if (name.equals("SwTryPane")) {
             chgDemo.getDemo().chgNthDemo("HtmlDemo", tabp_TabbedPane, 0);
             chgDemo.getDemo().chgMyDemo("myHtmlDemo", tabp_TabbedPane, 2);
         }
         break;
      case TABBEDPANE :
         if (name.equals("TabControl")) {
         // TODO: add your code for handling the ActionEvent on JTabbedPan [TabControl]  
         }
         break;
      case MENUITEM :
         if (name.equals("About...")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Wellcome to Simple SwingEngine demo\nHoward Xu\nCopyright 2002-2007");
            System.out.println("About...");
         }
         else if (name.equals("Company")) {
            GuiMgr.SwUtil.generateCode();
         }
         break;
      default:
         break;
      }
   }

/****************************************
*            user's methods
*****************************************/

}
