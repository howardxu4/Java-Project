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
import javax.swing.Box.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;
import java.util.Vector;
import java.util.Enumeration;

import org.w3c.dom.*;

import XmlMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwBox is a class of helper to support various box 
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
public class SwBox 
{
	private Component box;
	public SwBox(readXML rd, Node pnode) {
		box = Box.createGlue();
		String nm = rd.getAttrValue(pnode, "Style");
		if (nm.equalsIgnoreCase("Horizontal")) {
			box = Box.createHorizontalBox();
			SwEngine scp = new SwEngine(rd, pnode);
			for (Enumeration e = scp.getComponents().elements(); e.hasMoreElements(); ){ 
				((Box)box).add((Component)e.nextElement());
			}			
		}
		else if (nm.equalsIgnoreCase("HorizontalGlue")) {
			box = Box.createHorizontalGlue();
		}
		else if (nm.equalsIgnoreCase("HorizontalStrut")) {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ ) {
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase("Size")) {
					try {
						int n = Integer.parseInt(rd.get1stNodeValue(node));
						box = Box.createHorizontalStrut(n);
					}
					catch (Exception e) {}
				}
			}
		}
		else if (nm.equalsIgnoreCase("Vertical")) {
			box = Box.createVerticalBox();
			SwEngine scp = new SwEngine(rd, pnode);
			for (Enumeration e = scp.getComponents().elements(); e.hasMoreElements(); ){ 
				((Box)box).add((Component)e.nextElement());
			}			
		}
		else if (nm.equalsIgnoreCase("VerticalGlue")) {
			box = Box.createVerticalGlue();
		}
		else if (nm.equalsIgnoreCase("VerticalStrut")) {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ ) {
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase("Size")) {
					try {
						int n = Integer.parseInt(rd.get1stNodeValue(node)); 
						box = Box.createVerticalStrut(n);
					}
					catch (Exception e) {}
				}
			}
		}
		else if (nm.equalsIgnoreCase("RigidArea")) {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ ) {
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase("Dimension")) 
					box = Box.createRigidArea(new SwDimension(rd, node));
			}
		}
	}
	public Component getBox() {
		return box;
	}
}
