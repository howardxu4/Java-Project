//******************************************************************************
// Package Declaration
//******************************************************************************

//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;
import javax.swing.tree.*;
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
 * myPopup is a class of popup which configured by dynamic 
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

public class myPopup implements ActionListener
{
	private Component contents;
	private Object disp;
	myPopup(Object obj, boolean isPos) {
		disp = obj;
		if (isPos) {
		GuiMgr.SwUtil.delObject("myPos");
		GuiMgr.SwUtil.saveObject("myPos", this);
		contents = GuiMgr.SwUtil.chkXmlFile("XmlData/pos.xml");
		}
		else {
		GuiMgr.SwUtil.delObject("myPop");	
		GuiMgr.SwUtil.saveObject("myPop", this);
		contents = GuiMgr.SwUtil.chkXmlFile("XmlData/pop.xml");
		}
	}
	public void showPopup(Container owner, Point p) {
		GuiMgr.SwHelper.convertPoint(owner, p);
		GuiMgr.SwHelper.openPopup(owner, (JComponent)contents, p);
	}
	public void actionPerformed(ActionEvent e) {
		JComponent b = (JComponent)e.getSource();
		String text =  b.getName();
//		System.out.println("Selected => " + text);
		if (disp instanceof myProp)
			((myProp)disp).setValue(text);
		else if (disp instanceof JButton)
			((JButton)disp).setText(text);
		else if (disp instanceof JTextField) 
			((JTextField)disp).setText(text);
		GuiMgr.SwHelper.closePopup();
	}
}
