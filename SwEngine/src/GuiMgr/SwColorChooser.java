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
 * SwColorChooser is a class of JColorChooser which configured by XML 
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
public class SwColorChooser extends JColorChooser implements Iforward
{
	private SwColorChooser clrChooser;
	public SwColorChooser(readXML rd, Node pnode) {
		super();
		clrChooser = this;
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
				else if (nm.equalsIgnoreCase("Color")) {
					try {this.setColor(Integer.parseInt(value)); }
					catch (Exception e){}
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(this, rd, node);					
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("UserDef")) {					
					Object listener = SwHelper.procUserDef(this, rd, node);
					this.getSelectionModel().addChangeListener(new ChangeListener() {
		                public void stateChanged(ChangeEvent e) {
							SwClbk.getCallBack().stateChanged( new ChangeEvent(clrChooser));						}
					});
				}
				else 
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in ColorChooser");
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"ColorChooser Exception:" + e.getStackTrace() + " " + e.toString());
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
