/**
*******************************************************************************
*
*   (myComboBoxDemo) Generated on  Fri Dec 13 11:06:42 GMT-08:00 2002
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

public class myComboBoxDemo implements IcallBack
{
   private JPanel panel_myComboBoxDemo;
   private JComboBox combo_HairEyesMouth;
   private JComboBox combo_ComboHair;
   private JComboBox combo_ComboEyes;
   private JComboBox combo_ComboMouth;
   private JLabel label_Hair;
   private JLabel label_Eyes;
   private JLabel label_Mouth;

/****************************************
*         constructor
*****************************************/
   public myComboBoxDemo () {
      ComMgr.getComMgr().addListener(ComMgr.USER03, this);
      // user's special init (before loading)
      setMyModel(); 
   }

/****************************************
*         init run-time variables
*****************************************/
   private void init() {
      panel_myComboBoxDemo = (JPanel)GuiMgr.SwUtil.getObject("myComboBoxDemo");
      combo_HairEyesMouth = (JComboBox)GuiMgr.SwUtil.getObject("HairEyesMouth");
      combo_ComboHair = (JComboBox)GuiMgr.SwUtil.getObject("ComboHair");
      combo_ComboEyes = (JComboBox)GuiMgr.SwUtil.getObject("ComboEyes");
      combo_ComboMouth = (JComboBox)GuiMgr.SwUtil.getObject("ComboMouth");
      label_Hair = (JLabel)GuiMgr.SwUtil.getObject("Hair");
      label_Eyes = (JLabel)GuiMgr.SwUtil.getObject("Eyes");
      label_Mouth = (JLabel)GuiMgr.SwUtil.getObject("Mouth");
      // user's variable init (after loading)
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
   public void chgHairEyesMouth_click(JComboBox obj, String value, ActionEvent e) {
      int i = value.indexOf(",");
      if (i != -1) {
         chgIamge(label_Hair, value.substring(0,i).trim() + "hair");              
         int j = value.indexOf(",", i+1);
         if (j != -1) {
            chgIamge(label_Eyes, value.substring(i+1, j).trim() + "eyes");              
            chgIamge(label_Mouth, value.substring(j+1).trim() + "mouth");              
         }
      }
   }
   public void chgHair_click(JComboBox obj, String value, ActionEvent e) {
      chgIamge(label_Hair, value + "hair");
   }
   public void chgEyes_click(JComboBox obj, String value, ActionEvent e) {
      chgIamge(label_Eyes, value + "eyes");
   }
   public void chgMouth_click(JComboBox obj, String value, ActionEvent e) {
      chgIamge(label_Mouth, value + "mouth");
   }
   public void Save_click(JButton obj, String value, ActionEvent e) {
      addList();
   }
   public void Delete_click(JButton obj, String value, ActionEvent e) {
      delList();
   }
   public void Random_click(JButton obj, String value, ActionEvent e) {
      generate();
   }

/****************************************
*         user's methods
*****************************************/
   private void generate() {
   	  int n = combo_ComboHair.getItemCount();
      combo_ComboHair.setSelectedIndex((int)(n*Math.random()));
      combo_ComboEyes.setSelectedIndex((int)(n*Math.random()));
      combo_ComboMouth.setSelectedIndex((int)(n*Math.random()));
   }
   private void delList() {
      int i = combo_HairEyesMouth.getSelectedIndex();
      if (combo_HairEyesMouth.getItemCount() > 1)
         combo_HairEyesMouth.removeItemAt(i);
   }
   
   private void addList() {
      String s = (String)(combo_ComboHair.getSelectedItem());
      s += ", " +(String)(combo_ComboEyes.getSelectedItem());
      s += ", " +(String)(combo_ComboMouth.getSelectedItem());
      int i = 0;
      for ( ; i < combo_HairEyesMouth.getItemCount(); i++) {
         if (s.equals( combo_HairEyesMouth.getItemAt(i).toString())) {
            System.out.println(s + " was in the list");
            break;
         }
      }
      if (i == combo_HairEyesMouth.getItemCount()) {
         combo_HairEyesMouth.addItem(s);
         System.out.println(s + " is saved in the list");
         combo_HairEyesMouth.setSelectedItem(s);
      }
   }
   
   private void chgIamge(Object l, String s) {
      if (l != null) {
         ImageIcon II = null;
         String f = "resources/images/combobox/" + s.toLowerCase() + ".jpg";
         II = GuiMgr.SwUtil.getImageIcon(f);
         if (II != null) ((JLabel)l).setIcon(II);
      }
      else System.out.println("Can't get saved lable name for change to " + s);
   } 
   
   private void setMyModel() {
      DefaultComboBoxModel cbm = new DefaultComboBoxModel(names);
      cbm.setSelectedItem("Howard");
      GuiMgr.SwUtil.saveObject("myComboModel", (Object)cbm);  
      DefaultComboBoxModel cbm1 = new DefaultComboBoxModel(names);
      cbm1.setSelectedItem("Jeff");
      GuiMgr.SwUtil.saveObject("myComboModel01", (Object)cbm1);  
   }
   
   private final String [] names = new String [] {
	"Brent",
	"Georges",
	"Hans",
	"Howard",
	"James",
	"Jeff",
	"Jon",
	"Lara",
	"Larry",
	"Lisa",
	"Michael",
	"Philip",
	"Scott"
   };
}
