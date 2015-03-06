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
 * SwFrame is a class of JFrame which configured by XML 
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
public class SwFrame extends JFrame
{
		
	public SwFrame(readXML rd, Object pnode)
	{
		super(); 
		this.setSize(500,400);			    getContentPane().setLayout(new BorderLayout());		addWindowListener(SwClbk.getCallBack());		
		if (pnode == null) pnode = rd.getDocRoot();		
	
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node);
				String xml = rd.getAttrValue(node, "XmlFile");
				if (nm.equalsIgnoreCase("Title")) {
					setTitle(value);
				}
				if (nm.equalsIgnoreCase("Name")) {
					this.setName(value);	
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					try {this.setSize(new SwDimension(rd, node)); }
					catch (Exception e) {}				}
				else if (nm.equalsIgnoreCase("MenuBar")) {
					Component cp = SwUtil.chkXmlFile(xml);
					if (cp == null)cp =	new SwMenuBar(rd, node);
					if (cp instanceof GuiMgr.SwMenuBar)
						setJMenuBar((SwMenuBar)cp);
					else if (cp != null)
					if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
						LogTrace.getLog().Trace(LogTrace.ERROR, 
							"Must be MenuBar. but now it is " + cp.getClass().getName());
				}
				else if (nm.equalsIgnoreCase("ToolBar")) {
					Component cp = SwUtil.chkXmlFile(xml);
					if (cp == null)cp = new SwToolBar(rd, node);
					getContentPane().add(cp, BorderLayout.NORTH);
				}
				else if (nm.equalsIgnoreCase("Panel")) {
					Component cp = SwUtil.chkXmlFile(xml);
					if (cp == null) cp = new SwPanel(rd, node);
					getContentPane().add(cp, BorderLayout.CENTER);				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in Frame");
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Frame Exception: " + e.getStackTrace());
		}
		
		pack();
	}

}
