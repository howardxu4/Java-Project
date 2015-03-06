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
 * SwBorder is a class of helper to support various border 
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
public class SwBorder 
{
	private Border border;
	public SwBorder(readXML rd, Node pnode) {
		String p = rd.getAttrValue(pnode, "Type");
		if (p.length() == 0) {
			String t = rd.getAttrValue(pnode, "Title");
			border = BorderFactory.createTitledBorder(t);
		}
		else if (p.equalsIgnoreCase("Etched")) {
			border = BorderFactory.createEtchedBorder();			
		}
		else if (p.equalsIgnoreCase("Line")) {
			border = BorderFactory.createLineBorder(Color.black);			
		}
		else if (p.equalsIgnoreCase("Raised")) {
			border = BorderFactory.createRaisedBevelBorder();			
		}
		else if (p.equalsIgnoreCase("Lowered")) {
			border = BorderFactory.createLoweredBevelBorder();			
		}
		else {
			int i = 10;
			try { i = Integer.parseInt(p); }
			catch (Exception e) { }
			if (i < 0)
				border = BorderFactory.createEmptyBorder(0,-i,-i,-i);
			else
				border = BorderFactory.createEmptyBorder(i,i,i,i);
		}		
	}
	public Border getBorder() {
		return border;
	}
}
