/**
*******************************************************************************
*
*   (myDemo1) Generated on Tue on Dec 17 17:30:21 GMT-08:00 2002
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
public class myDemo1 implements IcallBack
{
   private JTabbedPane tabp_TabbedPane;
   private JPanel panel_mySplitPaneDemo;
   private JSplitPane split_SplitPane;
   private JLabel label_moon;
   private JLabel label_earth;
   private JEditorPane editp_MySource;
   private JEditorPane editp_MyData;

/****************************************
*            constructor
*****************************************/
   public myDemo1 () {
      ComMgr.getComMgr().addListener(ComMgr.USER01, this);
      // user's special init (before load)
   }

/****************************************
*            init run-time variables
*****************************************/
   private void init() {
      tabp_TabbedPane = (JTabbedPane)GuiMgr.SwUtil.getObject("TabbedPane");
      panel_mySplitPaneDemo = (JPanel)GuiMgr.SwUtil.getObject("mySplitPaneDemo");
      split_SplitPane = (JSplitPane)GuiMgr.SwUtil.getObject("SplitPane");
      label_moon = (JLabel)GuiMgr.SwUtil.getObject("moon");
      label_earth = (JLabel)GuiMgr.SwUtil.getObject("earth");
      editp_MySource = (JEditorPane)GuiMgr.SwUtil.getObject("MySource");
      editp_MyData = (JEditorPane)GuiMgr.SwUtil.getObject("MyData");
      // user's variable init (after load)
      chgDemo.getDemo().setPages(null, editp_MySource, editp_MyData);   }

/****************************************
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
		 GuiMgr.SwUtil.runSafety(this, "process",             new Object[]{new Integer(type), info}, null);
      else {   // user defind message handler
      }
      return null;
   }

/****************************************
*         system generated methods
*****************************************/   public void process(int type, Object info) {
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
            chgDemo.getDemo().chgMyDemo("mySplitPaneDemo", tabp_TabbedPane, 0);
         }
         else if (name.equals("SwComboBox")) {
            chgDemo.getDemo().chgMyDemo("myComboBoxDemo", tabp_TabbedPane, 0);
         }
         else if (name.equals("SwTabbedPane")) {
            chgDemo.getDemo().chgMyDemo("myTabbedPaneDemo", tabp_TabbedPane, 0);
         }
         else if (name.equals("SwTryPane")) {
            chgDemo.getDemo().chgMyDemo("myHtmlDemo", tabp_TabbedPane, 0);
         }
         break;
      case TABBEDPANE :
         if (name.equals("TabControl")) {
         // TODO: add your code for handling the ChangeEvent on JTabbedPane [TabControl]
         }
         break;
      case MENUITEM :
         if (name.equals("About...")) {
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
