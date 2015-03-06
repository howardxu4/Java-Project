/**
*******************************************************************************
*
*   (myTabbedPaneDemo) Generated on Fri Dec 13 11:06:27 GMT-08:00 2002
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

public class myTabbedPaneDemo implements IcallBack
{
   private JPanel panel_myTabbedPaneDemo;
   private JTabbedPane tabp_chgTabbedPane;

/****************************************
*         constructor
*****************************************/
   public myTabbedPaneDemo () {
      ComMgr.getComMgr().addListener(ComMgr.USER04, this);
      // user's special init (before load)
   }

/****************************************
*         init run-time variables
*****************************************/
   private void init() {
      panel_myTabbedPaneDemo = (JPanel)GuiMgr.SwUtil.getObject("myTabbedPaneDemo");
      tabp_chgTabbedPane = (JTabbedPane)GuiMgr.SwUtil.getObject("chgTabbedPane");
      // user's variable init (after load)
   }

/****************************************
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
         GuiMgr.SwUtil.genMethod(this, type, info);
      else {   // user defind message handler
      }
      return null;
   }

/****************************************
*         system generated methods
*****************************************/
   public void Top_click(JRadioButton obj, String value, ActionEvent e) {
      tabp_chgTabbedPane.setTabPlacement(JTabbedPane.TOP);   }
   public void Left_click(JRadioButton obj, String value, ActionEvent e) {
      tabp_chgTabbedPane.setTabPlacement(JTabbedPane.LEFT);   }
   public void Bottom_click(JRadioButton obj, String value, ActionEvent e) {
      tabp_chgTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);   }
   public void Right_click(JRadioButton obj, String value, ActionEvent e) {
      tabp_chgTabbedPane.setTabPlacement(JTabbedPane.RIGHT);   }
   public void TabDemo_stateChanged(JTabbedPane obj, String value, ChangeEvent e) {
      int i = Integer.parseInt(value);
	  if (i+1 == tabp_chgTabbedPane.getTabCount()){
          if (hdchg == null) hdchg = new headChange();          hdchg.go();  
      }   }

/****************************************
*         user's methods
*****************************************/
   class headChange implements ActionListener {
   javax.swing.Timer animator;    JComponent cp;
	     headChange() {      cp = (JComponent)GuiMgr.SwUtil.getObject("HeadSpinPanel");
   }   public void go() {
      animator = new javax.swing.Timer(1000, this);
      animator.start();
   }
   public void actionPerformed(ActionEvent e) {
      if(cp.isVisible()) 
         change();
      else 
         animator.stop();
   }
   void change() {      boolean f = false;      JLabel label = null;
      for (int i=0; i<6; i++) {
         String s = "img" + i;          label = (JLabel)(GuiMgr.SwUtil.getObject(s));         boolean b = Math.random() > 0.5;
         label.setVisible(b);         if (b) f = b;
      }
      if (f == false)label.setVisible(true);    }   }   headChange hdchg = null;
}
