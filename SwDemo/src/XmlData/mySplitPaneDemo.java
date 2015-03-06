/**
*******************************************************************************
*
*   (mySplitPaneDemo) Generated on Fri Dec 13 11:03:58 GMT-08:00 2002
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

public class mySplitPaneDemo implements IcallBack
{
   private JPanel panel_mySplitPaneDemo;
   private JSplitPane split_SplitPane;
   private JLabel label_moon;
   private JLabel label_earth;

/****************************************
*         constructor
*****************************************/
   public mySplitPaneDemo () {
      ComMgr.getComMgr().addListener(ComMgr.USER02, this);
      // user's special init (before load)
   }

/****************************************
*         init run-time variables
*****************************************/
   private void init() {
      panel_mySplitPaneDemo = (JPanel)GuiMgr.SwUtil.getObject("mySplitPaneDemo");
      split_SplitPane = (JSplitPane)GuiMgr.SwUtil.getObject("SplitPane");
      label_moon = (JLabel)GuiMgr.SwUtil.getObject("moon");
      label_earth = (JLabel)GuiMgr.SwUtil.getObject("earth");
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
   public void DividerSize_focusGained(JTextField obj, String value, FocusEvent e) {
      // TODO: add your code for handling the FocusEvent on JTextField [DividerSize]
   }
   public void DividerSize_focusLost(JTextField obj, String value, FocusEvent e) {
      split_SplitPane.setDividerSize(checkinput(obj));
   }
   public void DividerSize_click(JTextField obj, String value, ActionEvent e) {
      split_SplitPane.setDividerSize(checkinput(obj));
   }
   public void FirstMinimumSize_focusGained(JTextField obj, String value, FocusEvent e) {
      // TODO: add your code for handling the FocusEvent on JTextField [FirstMinimumSize]
   }
   public void FirstMinimumSize_focusLost(JTextField obj, String value, FocusEvent e) {
      int n = checkinput(obj);
      label_earth.setMinimumSize(new Dimension(n,n));
   }
   public void FirstMinimumSize_click(JTextField obj, String value, ActionEvent e) {
      int n = checkinput(obj);
      label_earth.setMinimumSize(new Dimension(n,n));   }
   public void SecondMinimumSize_focusGained(JTextField obj, String value, FocusEvent e) {
      // TODO: add your code for handling the FocusEvent on JTextField [SecondMinimumSize]
   }
   public void SecondMinimumSize_focusLost(JTextField obj, String value, FocusEvent e) {
      int n = checkinput(obj);
      label_moon.setMinimumSize(new Dimension(n,n));   }
   public void SecondMinimumSize_click(JTextField obj, String value, ActionEvent e) {
      int n = checkinput(obj);
      label_moon.setMinimumSize(new Dimension(n,n));   }

/****************************************
*         user's methods
*****************************************/
   private int checkinput(JTextField obj) {      int n = 10;
      try { n = Integer.parseInt(obj.getText());} catch (Exception ee) {
         JOptionPane.showMessageDialog((Component)split_SplitPane, (Object)"Invalid input number", 
            "Error", JOptionPane.ERROR_MESSAGE); }
      if (n < 5) { n = 5; obj.setText("5"); }
      return n;   }
}
