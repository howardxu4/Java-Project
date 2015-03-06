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
 * SwSplitPane is a class of JSplitPane which configured by XML 
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
*/public class SwSplitPane extends JSplitPane
{
	public SwSplitPane(readXML rd, Node pnode){
		super();
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				if (nm.equalsIgnoreCase("ContinuousLayout")) {
					this.setContinuousLayout(true);
				}
				else if (nm.equalsIgnoreCase("OneTouchExpandable")) {
					this.setOneTouchExpandable(true);				}
				else if (nm.equalsIgnoreCase("DividerLocation")) {
					try { 
						int n = Integer.parseInt(rd.get1stNodeValue(node));
						this.setDividerLocation(n);					}
					catch (Exception en ) {}
				}				else if (nm.equalsIgnoreCase("Orientation")) {
					if(rd.get1stNodeValue(node).indexOf("V") != -1)
						this.setOrientation(JSplitPane.VERTICAL_SPLIT);					else						this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
				}
				else if (nm.equalsIgnoreCase("LeftComponent") ||
						 nm.equalsIgnoreCase("TopComponent")) {
					SwEngine scp = new SwEngine(rd, node);
					if (scp.getLength() != 1) 
						if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
							LogTrace.getLog().Trace(LogTrace.WARNING, 
								"Should be only one component, but has " + scp.getLength());
					Component c = scp.getComponent(); 
					this.setLeftComponent(c);
				}
				else if (nm.equalsIgnoreCase("RightComponent") ||
						 nm.equalsIgnoreCase("BottomComponent")) {
					SwEngine scp = new SwEngine(rd, node);
					if (scp.getLength() != 1) 
						if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
							LogTrace.getLog().Trace(LogTrace.WARNING, 
								"Should be only one component, but has " + scp.getLength());
					Component c = scp.getComponent();
					this.setRightComponent(c); 
				}
				else if (nm.equalsIgnoreCase("Name")) {
					this.setName(rd.get1stNodeValue(node));	
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(this, rd, node);					
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
						"Name " + node.getNodeName() + " is not processed in SplitPane");				
			}			
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"SplitPane Exception: " + e.getStackTrace());}
	}
}
