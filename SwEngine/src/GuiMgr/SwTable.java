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
import javax.swing.table.*;

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
 * SwTable is a class of JTable which configured by XML 
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
*/public class SwTable extends JTable
{
	public SwTable(readXML rd, Node pnode) {
		super();
		
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node);
				if (nm.equalsIgnoreCase("Model")) {
					Object myModel = SwUtil.getObject(value);					if (myModel instanceof TableModel)
						this.setModel( (TableModel)myModel);	
				}
				else if (nm.equalsIgnoreCase("Name")) {
					this.setName(value);	
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					if (true) {
						try {this.setPreferredScrollableViewportSize(new SwDimension(rd, node)); }
						catch (Exception e) {}
					}
					else
						SwHelper.setCompSize(this, rd, node);											}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in Table");				
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Table Exception: " + e.getStackTrace());
		}	
	}}
