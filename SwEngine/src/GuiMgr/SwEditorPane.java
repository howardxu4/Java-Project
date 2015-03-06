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
import javax.swing.text.html.*;
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
 * SwEditorPane is a class of JEditorPane which configured by XML 
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
public class SwEditorPane extends JEditorPane
{
	public SwEditorPane(readXML rd, Node pnode) {
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
				else if (nm.equalsIgnoreCase("Page")) {
					try { this.setPage(GuiMgr.SwUtil.getUrl(value)); }
					catch (Exception eu) {
					if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
						LogTrace.getLog().Trace(LogTrace.EXECPT, 
							value + " setPage Error : " + eu.getMessage()); 
					}
				}
				else if (nm.equalsIgnoreCase("Visible")) {
					this.setVisible(false);
				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(rd.get1stNodeValue(node), this);
				}
				else if (nm.equalsIgnoreCase("Text")) {
					this.setText(value);
				}
				else if (nm.equalsIgnoreCase("Editable")) {
					this.setEditable(false);
				}
				else if (nm.equalsIgnoreCase("Dimension")) {
					SwHelper.setCompSize(this, rd, node);					
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in EditorPane");				
			}
			this.addHyperlinkListener(SwClbk.getCallBack());	
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"EditorPane Exception:" + e.getStackTrace() + " " + e.toString());
		}
	}	
}
