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
 * SwButton is a class of JButton which configured by XML 
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
public class SwButton extends JButton implements Iforward
{
	public SwButton(readXML rd, Node pnode) {
		super();
		ProcAbstractButton(this, rd, pnode);
	}
	
	static protected void ProcAbstractButton(AbstractButton btn, readXML rd, Node pnode) {
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node); 
				if (nm.equalsIgnoreCase("Name")) {
					btn.setName(value);
				}
				else if (nm.equalsIgnoreCase("Text")) {
					btn.setText(value);
				}
				else if (nm.equalsIgnoreCase("Selected")) {
					btn.setSelected(true);
				}				else if (nm.equalsIgnoreCase("ToolTipText")) {
					btn.setToolTipText(value);
				}
				else if (nm.equalsIgnoreCase("Foreground")) {
					try {btn.setForeground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("Background")) {
					try {btn.setBackground(new Color(Integer.parseInt(value)));}
					catch (Exception ec) {}
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, btn);
				}
				else if (nm.equalsIgnoreCase("Icon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setIcon(II);				}
				else if (nm.equalsIgnoreCase("PressedIcon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setPressedIcon(II);				}
				else if (nm.equalsIgnoreCase("RolloverIcon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setRolloverIcon(II);				}
				else if (nm.equalsIgnoreCase("DisabledIcon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setDisabledIcon(II);				}
				else if (nm.equalsIgnoreCase("RolloverSelectedIcon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setRolloverSelectedIcon(II);				}
				else if (nm.equalsIgnoreCase("SelectedIcon")) {
					ImageIcon II = SwUtil.getImageIcon(value);
					if (II != null) btn.setSelectedIcon(II);				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(btn, rd, node);					
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					btn.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("Border")) {
					btn.setBorder( new SwBorder(rd, node).getBorder());
				}
				else if (nm.equalsIgnoreCase("Margin")) {
					btn.setMargin(new Insets(0,0,0,0));				}
				else if (nm.equalsIgnoreCase("Mnemonic")) {
					if (value.length() == 1)btn.setMnemonic(value.charAt(0));					else if (value.length() > 0) 					try { btn.setMnemonic(Integer.parseInt(value));		 					} catch (Exception ie) {}				}
				else if (nm.equalsIgnoreCase("UserDef")) {
					Object listener = SwHelper.procUserDef(btn, rd, node);
					if (listener instanceof ActionListener)
						btn.addActionListener((ActionListener)listener);
				}
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Button Exception:" + e.getStackTrace() + " " + e.toString());
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
