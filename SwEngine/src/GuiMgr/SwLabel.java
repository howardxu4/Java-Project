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
 * SwLabel is a class of JLabel which configured by XML 
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
public class SwLabel extends JLabel implements Iforward
{
	public SwLabel(readXML rd, Node pnode) {
	try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node); 
				if (nm.equalsIgnoreCase("Name")) {
					this.setName(value);
				}
				else if (nm.equalsIgnoreCase("Text")) {
					this.setText(value);
				}
				else if (nm.equalsIgnoreCase("Alignment")) {
					SwHelper.setAlignment(this, value);
				}
				else if (nm.equalsIgnoreCase("Border")) {
					this.setBorder( new SwBorder(rd, node).getBorder());
				}
				else if (nm.equalsIgnoreCase("Icon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) { 
						this.setIcon(II);						this.setHorizontalAlignment(JLabel.CENTER);					}				}
				else if (nm.equalsIgnoreCase("Foreground")) {
					try {this.setForeground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("Background")) {
					try {this.setBackground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(this, rd, node);					
				}
				else if (nm.equalsIgnoreCase("ToolTipText")) {
					this.setToolTipText(value);
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false); 
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("UserDef")) {					
					Object listener = SwHelper.procUserDef(this, rd, node);
					if (listener instanceof MouseListener)
						this.addMouseListener((MouseListener)listener);
				}
				else	
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in Label");				
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Label Exception:" + e.getStackTrace() + " " + e.toString());
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
