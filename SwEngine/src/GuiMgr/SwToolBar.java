//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
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

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwToolBar is a class of JToolBar which configured by XML 
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
public class SwToolBar extends JToolBar 
{
	public SwToolBar(readXML rd, Node pnode) {
		super();
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				if (nm.equalsIgnoreCase("ToolBarButton")) {
					
					AbstractAction an = getTlbAction(rd, node);
					if (an != null) this.add(an);
					else {
					SwEngine scp = new SwEngine(rd, node);
					if (scp.getLength() != 1) 
						if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
							LogTrace.getLog().Trace(LogTrace.WARNING, 
								"ToolBar Button Should be only one component, but has " + scp.getLength());
					Component c = scp.getComponent(); 
					if (c != null) this.add(c);
					}
				}
				else if (nm.equalsIgnoreCase("Name")) {
					this.setName(rd.get1stNodeValue(node));	
				}
				else if (nm.equalsIgnoreCase("Separator")) {
					this.addSeparator();
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(rd.get1stNodeValue(node), this);
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					"Name " + node.getNodeName() + " is not processed in ToolBar");				
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"ToolBar Exception: " + e.getStackTrace());
		}	
	}		private AbstractAction getTlbAction(readXML rd, Node pnode) {		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			boolean hasName = false;
			boolean hasIcon = false;
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				if (nm.equalsIgnoreCase("#text")) ;
				else if (nm.equalsIgnoreCase("Name")) {
					hasName = true;
				}
				else if (nm.equalsIgnoreCase("Icon")) {
					hasIcon = true;
				}
			}			if (hasName && hasIcon) 
				return (AbstractAction) new SwToolBarButton(rd, pnode);
		}		catch (Exception e) {			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"ToolBar Exception: " + e.getStackTrace());		}			
		return null;	}}
