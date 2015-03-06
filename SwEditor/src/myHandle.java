//******************************************************************************
// Package Declaration
//******************************************************************************

//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

import org.w3c.dom.*;

import XmlMgr.*;
import ComMgr.*;
import GuiMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * myHandle is a class of Handler which process event from tree 
 *
 * </pre>
 *******************************************************************************
 * <B> Author: </B><p><pre>
 *
 *  Howard Xu
 *
 * </pre>
 *******************************************************************************
 * <B> Resources: </B><ul>
 *
 * </ul>
 *******************************************************************************
 * <B> Notes: </B><ul>
 *
 * </ul>
 *******************************************************************************
*/
public class myHandle implements IcallBack
{
	private JTextArea myPad;
	private JEditorPane myPane;
	private JInternalFrame myWindow;
	private JScrollPane myScrlPane;
	private JTree curTree;
	private myTree mt;
	private JPanel myProperty;
	private JPanel myContainer;
	private myProps myBox;
	private Box dummy;
	private JPanel myRefresh;
	private JScrollPane myScrlBox;
	
/****************************************
*            constructor
*****************************************/
	public myHandle() {
		ComMgr.getComMgr().addListener(ComMgr.USER05, this);
		GuiMgr.SwUtil.saveObject("myHandle", this);
	}
/****************************************
*            init run-time variables
*****************************************/
   public void init() { 	  myPad = (JTextArea)SwUtil.getObject("myPad");
	  myPane = (JEditorPane)SwUtil.getObject("myPane");
	  myWindow = (JInternalFrame)SwUtil.getObject("myWindow");
	  myScrlPane = (JScrollPane)SwUtil.getObject("myScrlPane");
	  mt = myTree.getMyTree(null);
	  curTree = (JTree)SwUtil.getObject("curTree");
	  mt.setRender(curTree);
	  myRefresh = (JPanel)SwUtil.getObject("refresh");
	  myProperty = (JPanel)SwUtil.getObject("putHere");
	  myContainer = (JPanel)SwUtil.getObject("setHere");
	  if (myProperty != null) initProperty("Frame");
   }
/****************************************
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
		 GuiMgr.SwUtil.runSafety(this, "process",             new Object[]{new Integer(type), info}, null);
      else {   // user defind message handler		  Object obj = ((Argument)info).getArgument(OBJECT);		  if (type == 123 || type == 124){
			  setProperty(obj, type==124);		  }
      }
      return null;
   }
/****************************************
*         system generated methods
*****************************************/   public void process(int type, Object info) {
      Argument argv = (Argument)info;
      String name = (String)argv.getArgument(NAME);

   // the following useful info maybe referred on some case
      Object obj = argv.getArgument(OBJECT);
   // int eType = ((Integer)argv.getArgument(TYPE)).intValue();
   // Object event = argv.getArgument(EVENT);
      String value = (String)argv.getArgument(VALUE);	  if (value == null) value = "null";

      switch (type) {
//		case TREE:
//		   System.out.println(name + " ==> " + value);
//		   setProperty(obj);
//		   break;
//		case MENUITEM:
//           System.out.println(name + "**>" + value);	
//		   if (name.equalsIgnoreCase("Support"))
//			   doThis();
//		break;
//	    case COLORCHOOSER:
//		  changeColor(value);
//		break;
		default:
		   System.out.println(name + " --> " + value);
		break;
		}
   }
   
   public void changeColor(String value) {
   		JLabel l = (JLabel)SwUtil.getObject("myColor");
		l.setText(value);
		l.setForeground(new Color(Integer.parseInt(value)));
   }

   private void initProperty(String s){
	   if (myBox == null) {
			myBox = new myProps();	
			dummy = Box.createVerticalBox();
			myContainer.add(myBox.getHelper(), "South");
//			myScrlBox = new JScrollPane();
//			myContainer.add(myScrlBox, "Center");
	   }
	   dummy.removeAll(); 
	   myBox.init();
	   myBox.buildProperty(s, dummy);	   
	   myProperty.add(myBox, "North");
	   myContainer.add(dummy, "Center");
//	   ((JScrollPane)myScrlBox).setViewportView(dummy); 
   }
   
   public void setProperty(Object obj) {
		setProperty(obj, true); 
   }
   private void setProperty(Object obj, boolean b) {
	   if (myBox == null) 
		   init();
	   else if (b){ // if compare changes update tree
		   String t = myBox.getProperties(); 
		   myPane.setText( "\n-----\n" + t);
		   if (t.indexOf("!!! Changed") != -1)
				mt.updtTree(myBox.getNameList());
		   if (t.indexOf("!!! Component") != -1)
				mt.updtChanges(myBox.getDelList(), myBox.getCurList());
	   }
	   String s = mt.getInfo(obj);
	   initProperty(s);
   	   myPad.setText("----\n" + s);
	   myRefresh.invalidate();
//	   myRefresh.repaint();
   }
   
   public void doThis() {
	   try {
		FileOps fop = new FileOps();
		File myFile = fop.fileOpen("hello.java", "", false);
		if (myFile != null) {
			int n = fop.askConf("Handle only one file, press Yes\nAuto expand to all, press No\n");
			if (n==2) return;
			myTree.setOnlyOne(n==0);
			String s = myFile.toString();
			s = s.substring(s.lastIndexOf('\\')+1);
			mt = myTree.getMyTree(s);
			mt.saveTree("myTree");
			JTree jt = (JTree)SwUtil.getObject("curTree");
			jt.setModel((TreeModel)SwUtil.getObject("myTree"));
	System.out.println(myFile.toString() + " myTree is set");
			myPad.setText(myFile.toString());
	System.out.println(s + " myPad is set");
			myPane.setPage(SwUtil.getUrl(s));
	System.out.println(s + " myPane is set");
			Component cp = SwUtil.chkXmlFile(s);
			((JScrollPane)myScrlPane).setViewportView(cp);
			myWindow.validate();
			myWindow.repaint();
			
	System.out.println(s + " myPanel is updated");
		}
	}
	catch (Exception e) { System.out.println("Outer " + e.getMessage()); }
   }
}
