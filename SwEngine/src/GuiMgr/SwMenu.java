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
 * SwMenu is a class of JMenu which configured by XML 
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
public class SwMenu extends JMenu
{
	public SwMenu(readXML rd, Node pnode) {
		super();
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			ButtonGroup group = new ButtonGroup();
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
				else if (nm.equalsIgnoreCase("Menu")) {
					SwMenu menu = new SwMenu(rd, node);
					this.add(menu);
				}
				else if (nm.equalsIgnoreCase("MenuItem")) {
					SwMenuItem mi = new SwMenuItem(rd, node);					this.add(mi);
				}				else if (nm.equalsIgnoreCase("CheckBoxMenuItem")) {
					SwCheckBoxMenuItem mi = new SwCheckBoxMenuItem(rd, node);
					this.add(mi);
				}
				else if (nm.equalsIgnoreCase("ButtonGroup")) {
					group = SwHelper.getGroup(rd.getAttrValue(node, "Name"));	// Maybe named group				}
				else if (nm.equalsIgnoreCase("RadioButtonMenuItem")) {
					SwRadioButtonMenuItem mi = new SwRadioButtonMenuItem(rd, node);
					this.add(mi);					group.add(mi);
				}
				else if (nm.equalsIgnoreCase("Separator")) {
					this.addSeparator();
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("Mnemonic")) {
					if (value.length() == 1) this.setMnemonic(value.charAt(0));					else if (value.length() > 0) 					try { this.setMnemonic(Integer.parseInt(value));		 					} catch (Exception ie) {}				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);	
				}
				else	
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in Menu");				
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Menu Exception: " + e.getStackTrace());
		}	
	}
}
