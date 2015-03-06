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
 * SwInternalFrame is a class of JInternalFrame which configured by XML 
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
public class SwInternalFrame extends JInternalFrame
{
	public SwInternalFrame(readXML rd, Node pnode) {
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
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("Title")) {
					this.setTitle(value);
				}
				else if (nm.equalsIgnoreCase("Closable")) {
					this.setClosable(true); //Boolean.getBoolean(value));
				}
				else if (nm.equalsIgnoreCase("Maximizable")) {
					this.setMaximizable(true); //Boolean.getBoolean(value));
				}
				else if (nm.equalsIgnoreCase("Iconifiable")) {
					this.setIconifiable(true); //Boolean.getBoolean(value));
				}
				else if (nm.equalsIgnoreCase("Resizable")) {
					this.setResizable(true); //Boolean.getBoolean(value));
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					this.setSize(new SwDimension(rd, node));
				}
				else if (nm.equalsIgnoreCase("Rectangle")) {
					this.setBounds(new SwRectangle(rd, node));
				}
				else if (nm.equalsIgnoreCase("Panel")) {
					Component cp = SwUtil.chkXmlFile(rd.getAttrValue(node, "XmlFile"));
					if (cp == null) cp = new SwPanel(rd, node);
					this.setContentPane((SwPanel)cp);				}
				else 
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in InternalFrame");
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"InternalPane Exception:" + e.getStackTrace() + " " + e.toString());
		}
	}
}
