//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;
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

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwTextField is a class of JTextField which configured by XML 
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
*/public class SwTextField extends JTextField implements Iforward
{
	public SwTextField(readXML rd, Node pnode) {
		super(5);									// default 
		procTextField(this, rd, pnode);
	}
	
	static public void procTextField(JTextField txf, readXML rd, Node pnode) {
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node); 
				if (nm.equalsIgnoreCase("Name")) {
					txf.setName(value);
				}
				else if (nm.equalsIgnoreCase("Columns")) {
					try { txf.setColumns(Integer.parseInt(value)); } 
					catch (Exception en) {}
				}
				else if (nm.equalsIgnoreCase("Text")) {
					txf.setText(value);
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, txf);
				}
				else if (nm.equalsIgnoreCase("Editable")) {
					txf.setEditable(false);
				}
				else if (nm.equalsIgnoreCase("ToolTipText")) {
					txf.setToolTipText(value);
				}
				else if (nm.equalsIgnoreCase("Border")) {
					txf.setBorder( new SwBorder(rd, node).getBorder());
				}
				else if (nm.equalsIgnoreCase("Foreground")) {
					try {txf.setForeground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("Background")) {
					try {txf.setBackground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(txf, rd, node);					
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					txf.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("UserDef")) {	
					Object listener = SwHelper.procUserDef(txf, rd, node);
					if (listener instanceof FocusListener)
						txf.addFocusListener((FocusListener)listener);
					String actn = rd.getAttrValue(node, "Alter");
					if (actn.indexOf('K') != -1 && listener instanceof KeyListener)
						txf.addKeyListener((KeyListener)listener);
					else if (listener instanceof ActionListener)
						txf.addActionListener((ActionListener)listener);					
				}
				else 
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in TextField");
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"TextField Exception:" + e.getStackTrace() + " " + e.toString());
		}
	}
	
	// Implements Iforward
	public void setMethodDecl(String [] decl) {
		this.methodDecl = decl;
	}
	public void setUserType( int t) {
		this.userType = t;
	}
	public String [] getMethodDecl() {
		return this.methodDecl;
	}
	public int getUserType() {
		return this.userType;
	}
	private String [] methodDecl = null;
	private int userType = CALLBK;
}
