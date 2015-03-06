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
 * SwScrollPane is a class of JScrollPane which configured by XML 
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
*/public class SwScrollPane extends JScrollPane
{
	public SwScrollPane(readXML rd, Node pnode) {
		super();
		SwEngine scp = new SwEngine(this, rd, pnode, false);
		if (scp.getLength() != 1) 
			if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					"ScrollPane should has only one component, but has " + scp.getLength());
		Component c = scp.getComponent(); 
		if ( c != null ) this.setViewportView(c);
	}
}
