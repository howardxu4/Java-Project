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
 * SwComboBox is a class of JComboBox which configured by XML 
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
public class SwComboBox extends JComboBox implements Iforward
{
	public SwComboBox() {
	}
	
	public SwComboBox(readXML rd, Node pnode) {
		super();
		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node);
				if (nm.equalsIgnoreCase("Item")) {
					cbm.addElement(value);
					if (rd.getAttrValue(node, "Selected").length() > 0)
						cbm.setSelectedItem(value);
				}
				else if (nm.equalsIgnoreCase("Model")) {
					Object myModel = SwUtil.getObject(value);					if (myModel instanceof DefaultComboBoxModel) 
						cbm = (DefaultComboBoxModel)myModel;
				}
				else if (nm.equalsIgnoreCase("Name")) {
					this.setName(value);					
				}
				else if (nm.equalsIgnoreCase("Editable")) {				
					if (value.equalsIgnoreCase("false")) this.setEditable(false);	
					else this.setEditable(true);	
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(this, rd, node);					
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);	
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("UserDef")) {					
					Object listener = SwHelper.procUserDef(this, rd, node);
					if (listener instanceof ActionListener)
						this.addActionListener((ActionListener)listener);
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in ComboBox");				
			}
			this.setModel(cbm);
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"ComboBox Exception: " + e.getStackTrace());
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
