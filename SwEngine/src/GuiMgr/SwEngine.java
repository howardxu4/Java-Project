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
import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Constructor;

import org.w3c.dom.*;

import XmlMgr.*;
import ObjMgr.*;
import ComMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwEngine is a class of dispatch kernel which build controls configured by XML 
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
public class SwEngine
{
	private Component cp = null;
	private Vector v = new Vector();
	private ButtonGroup group;
	
	public SwEngine(String s)  {				// SwUtil.chkXmlFile
		readXML rd = new readXML();
		try {	
			int uid = SwHelper.setUserIds(0);
			URL url = SwUtil.getUrl(s);
	System.out.println(s + " --------> " + url.toString());		
			rd.setDocument(SwUtil.getUrl(s));
			if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
				LogTrace.getLog().Trace(LogTrace.INFO, 
					"Input Xml file ==> " + s); 
			rd.trimDocument();
		
			if (SwUtil.chkXmlType(s, rd.getXMLtype())) {

			Node pnode = rd.getDocRoot();
			String ld = rd.getAttrValue(pnode, "Load");
			if (ld.length() > 1) {
				if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
					LogTrace.getLog().Trace(LogTrace.INFO, 
						"Loading ==> " + ld + " now..."); 
				if (ObjMgr.getObjMgr().getObject(ld)== null)
					if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
						LogTrace.getLog().Trace(LogTrace.ERROR, 
							"Class " + ld + " loading failure!!!"); 
			}
			processAll(null, rd, null, false);
			String init = rd.getAttrValue(pnode, "Init");
			if (init.length() > 1) {
				if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
					LogTrace.getLog().Trace(LogTrace.INFO, 
						"Initial ==> " + init + " now..."); 
				Object InitObj = ObjMgr.getObjMgr().getGC(init);
				if (InitObj != null) {
					SwUtil.runSafety(InitObj, "init", null, init + ".init calling");
				}
				else if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
					LogTrace.getLog().Trace(LogTrace.ERROR, 
						"Object " + init + " is undefined!!!"); 
			}
			int k = SwHelper.setUserIds(uid) + 1;
			SwHelper.notifyAll(k, "Loaded", s);
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Engine Exception: " + e.getStackTrace()); 
		}
	}
	
 	public SwEngine(readXML rd, Node pnode) {	// SwSplitPane SwTabbedPane SwBox
 		processAll(null, rd, pnode, false);
 	}
	
 	public SwEngine(JComponent p, readXML rd, Node pnode, boolean b) {	
 		processAll(p, rd, pnode, b);				// SwSrollPane	SwPanel
 	}
	
	private void processAll(JComponent p, readXML rd, Node pnode, boolean b) {
		try {
			if (pnode == null) pnode = rd.getDocRoot();		// can be from root
			group = new ButtonGroup();						// for radiobuttons
			boolean borderlayout = false;
			cp = SwUtil.chkXmlFile(rd.getAttrValue(pnode, "XmlFile"));
			if (cp != null) {
				v.addElement(cp);
				if (b && p != null) {
					try {p.add(cp, rd.getAttrValue(pnode, "Position"));}
					catch(Exception ep) {handleException(pnode.getNodeName(), ep);}
				}
			}
			else {
				NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++ ) {
					Node node = nodeList.item(i);
					String nm = node.getNodeName();
					if (nm.charAt(0) != '#') {
						Component c = SwUtil.chkXmlFile(rd.getAttrValue(node, "XmlFile"));
						if (c != null) {
							cp = c;
							v.addElement(cp);
							if (b && p != null) {
								if (borderlayout) {
									try {p.add(cp, rd.getAttrValue(node, "Position"));}
									catch(Exception ep) {handleException(node.getNodeName(), ep);}
								}
								else p.add(cp);
							}
						}
						else if (nm.equalsIgnoreCase("Name")) {
							if (p != null)
								p.setName(rd.get1stNodeValue(node));
						}
						else if (nm.equalsIgnoreCase("Alignment")) {
							if (b && p != null) 
								SwHelper.setAlignment(p, rd.get1stNodeValue(node));
						}
						else if (nm.equalsIgnoreCase("Foreground")) {
							if (b && p != null) {
							try {p.setForeground(new Color(Integer.parseInt(rd.get1stNodeValue(node))));}
							catch (Exception ec) {System.out.println("Bad Fore color");}
							}
						}
						else if (nm.equalsIgnoreCase("Background")) {
							if (b && p != null) {
							try {p.setBackground(new Color(Integer.parseInt(rd.get1stNodeValue(node))));}
							catch (Exception ec) {System.out.println("Bad Back color");}
							}
						}
						else if (nm.equalsIgnoreCase("Dimension")) {
							if (p != null) 
								SwHelper.setCompSize(p, rd, node);
						}
						else if (nm.equalsIgnoreCase("Visible")) {
							if (p != null)
								p.setVisible(false);
						}
						else if (nm.equalsIgnoreCase("ToolTipText")) {
							if (b && p != null)
								p.setToolTipText(rd.get1stNodeValue(node));
						}
						else if (nm.equalsIgnoreCase("Border")) {
							if (p != null) 
								p.setBorder(new SwBorder(rd, node).getBorder());
						}
						else if (nm.equalsIgnoreCase("SaveId")) {
							if (p != null)
								SwUtil.saveObject(rd.get1stNodeValue(node), p);
						}
						else if (nm.equalsIgnoreCase("UserDef")) {
							if (b && p != null) {
								Object listener = SwHelper.procUserDef(p, rd, node);
								if (listener instanceof MouseListener)
									p.addMouseListener((MouseListener)listener);
							}
						}
						else if (nm.equalsIgnoreCase("Layout")) {
							if (b && p != null) { 
								String layout = rd.getAttrValue(node, "Type");
								if (layout.indexOf("Box") != -1) {		// Box
									if (layout.indexOf("Y_AXIS") != -1)
										p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
									else
										p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
								}
								else if (layout.indexOf("Grid")!=-1) {	// Grid
									int row = 0, col = 2;
									try { row = Integer.parseInt(rd.getAttrValue(node, "Row"));} 
									catch (Exception en) {}
									try { col = Integer.parseInt(rd.getAttrValue(node, "Col"));} 
									catch (Exception en) {}
									p.setLayout(new GridLayout(row, col));
								}
								else if (layout.indexOf("Flow")!=-1) {	// Flow	
									if (layout.indexOf("LEFT") != -1) 
										p.setLayout(new FlowLayout(FlowLayout.LEFT));
									else if (layout.indexOf("RIGHT") != -1)
										p.setLayout(new FlowLayout(FlowLayout.RIGHT));
									else
										p.setLayout(new FlowLayout(FlowLayout.CENTER));
								}
								else if (layout.indexOf("Border")!=-1) {
									p.setLayout(new BorderLayout());	//Border 
									borderlayout = true;								}								else ;							// Default | Current
							}							NodeList CnodeList = ((org.w3c.dom.Element)node).getChildNodes();
							for (int j=0; j < CnodeList.getLength(); j++ ) {
								Node Cnode = CnodeList.item(j);
								boolean f = false;
								if (Cnode.getNodeName().charAt(0) != '#'){
								Component cc = SwUtil.chkXmlFile(rd.getAttrValue(Cnode, "XmlFile"));
								if (cc != null) {
									cp = cc;
									v.addElement(cp);
									f = true;
								}
								else f = processOne(rd, Cnode); 
								if (f && b && p != null) 
									if (borderlayout) {
										try {p.add(cp, rd.getAttrValue(Cnode, "Position"));}
										catch(Exception ep) {handleException(Cnode.getNodeName(), ep);}
									}
									else p.add(cp);
								}
							}						}
						else {
							if (processOne(rd, node)) 
								if (b && p != null) 
									if (borderlayout) {
										try {p.add(cp, rd.getAttrValue(node, "Position"));}
										catch(Exception ep) {handleException(nm, ep);}
									}
									else p.add(cp);
						}
					}
				}
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Engine Exception: (processAll) " + e.getStackTrace()); 
		}			
	}
	private void handleException(String nm, Exception e) {
		if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
			LogTrace.getLog().Trace(LogTrace.EXECPT, 
				nm + "with bad layout position. " + e.getStackTrace()); 
	}
	
	private boolean processOne(readXML rd, Node node) {
		boolean rtn = false;
		String nm = node.getNodeName();
		if (nm.equalsIgnoreCase("ButtonGroup")) {				
			group = SwHelper.getGroup( rd.getAttrValue(node, "Name"));	// Maybe named group		}
		else if ( chkSwName(nm, rd, node) ) {			// check Sw Class
			if (nm.equalsIgnoreCase("RadioButton"))
				group.add((SwRadioButton)cp);
			rtn = true;
		}	
		else {
			if (nm.equalsIgnoreCase("PopupMenu"));	
			else if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					"Name " + nm + " is not processed!!!!"); 
		}
		return rtn;
	}
	
	private boolean chkSwName(String nm, readXML rd, Node node) {
		String cls = "GuiMgr.Sw" + nm;
		try {
			Class [] argc = new Class [] {
				Class.forName("XmlMgr.readXML"),Class.forName("org.w3c.dom.Node")};
			Object [] argv = new Object [] { rd, node };
			Object obj = ObjMgr.getObjMgr().crtObject(cls, argc, argv);
			if (obj instanceof SwComponent) 
				obj = ((SwComponent)obj).getObject();		// Component
			if (obj instanceof SwBox)
				obj = ((SwBox)obj).getBox();				// Box
			if (obj != null && obj instanceof Component) {
				if (obj instanceof JPopupMenu); 	// no adding and return false
				else {
					cp = (Component)obj;
					v.addElement(cp);
					return true;
				}
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Attention !!! " + cls + " : " + e.getMessage()); 
		}
		return false;
	}
	public Component getComponent() {
		return cp;
	}
	public int getLength() {
		return v.size();	
	}
	public Vector getComponents() {
		return v;	
	}
}
