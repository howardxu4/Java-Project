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

import XmlMgr.*;
import ComMgr.*;
import ObjMgr.*;

import org.w3c.dom.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwHelper is a class of Helper which provides the methods for internal using 
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

public class SwHelper
{
	static public void notifyAll(int eType, String msg, Object obj)
	{
		Argument argv = new Argument();
		argv.setArgument(Itypes.NAME, msg);
		argv.setArgument(Itypes.TYPE, new Integer(eType));
		argv.setArgument(Itypes.OBJECT, obj);
		for (int i=0; i<Itypes.MAXHOLD; i++)
			if (((1<<i) & eType) != 0)
				ComMgr.getComMgr().postMsg(i, Itypes.NOTIFY, argv);
	}
	
	static private int userIDs = 0;
	static public int setUserIds(int n) {
		int oldIDs = userIDs;
		userIDs = n;
		return oldIDs;
	}
	
	static public int getUserDef(String value) {				
		int k = -1;
		if (value != null) {
			k = value.indexOf("0");
			if (k != -1) {
				try {
					k = Integer.parseInt(value.substring(k));					if (k >= Itypes.MAXHOLD) k = -1;					else userIDs |= (1<<k);
				}
				catch (Exception en) {k = -1;}
			}		}
		return k;														}
	
	static private void procUserActn(Iforward p, readXML rd, Node pnode) {
		String [] s = new String[4];
		NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++ )
		{
			Node node = nodeList.item(i);
			String value = rd.get1stNodeValue(node);
			if (value.length() > 0) {
				String nm = node.getNodeName();
				if (nm.equalsIgnoreCase("Object")) s[0] =  value;
				else if (nm.equalsIgnoreCase("Method")) s[1] =  value;
				else if (nm.equalsIgnoreCase("ParType")) s[2] =  value;
				else if (nm.equalsIgnoreCase("ParValue")) s[3] =  value;
			}
		}
		if (s[0] != null && s[1] != null) p.setMethodDecl(s); 
	}
	
	static public Object procUserDef(Object p, readXML rd, Node pnode) {
		Object obj = SwUtil.getObject(rd.getAttrValue(pnode, "Action"));
		if (obj != null) {	
			((Iforward)p).setUserType(Itypes.SYSTEM);
			procUserActn((Iforward)p, rd, pnode);
		}
		else {
			int ud = SwHelper.getUserDef(rd.get1stNodeValue(pnode));
			if (ud != -1) ((Iforward)p).setUserType(ud);
			obj = SwClbk.getCallBack();
		}
		return obj;
	}
	
	static void setAlignment(JComponent p, String s) {
		String algn = s.toLowerCase();
		if ( algn.indexOf("top") != -1)			p.setAlignmentY(p.TOP_ALIGNMENT);		if ( algn.indexOf("left") != -1)
			p.setAlignmentX(p.LEFT_ALIGNMENT);		if ( algn.indexOf("righr") != -1)
			p.setAlignmentX(p.RIGHT_ALIGNMENT);		if ( algn.indexOf("center") != -1)
			p.setAlignmentX(p.CENTER_ALIGNMENT);		if ( algn.indexOf("bottom") != -1)
			p.setAlignmentY(p.BOTTOM_ALIGNMENT);	}
	
	static void setCompSize(JComponent p, readXML rd, Node node) {
		String sz = rd.getAttrValue(node, "Size");
		if (sz.indexOf("Minim") != -1)
			p.setMinimumSize(new SwDimension(rd, node));
		else if (sz.indexOf("Maxim") != -1)
			p.setMaximumSize(new SwDimension(rd, node));
		else
			p.setPreferredSize(new SwDimension(rd, node));
	}
	
	static ButtonGroup getGroup(String gp) {
		ButtonGroup group = new ButtonGroup();	
		if (gp.length() != 0)  {			Object o = SwUtil.getObject(gp);
			if (o instanceof ButtonGroup) group = (ButtonGroup)o;
			else SwUtil.saveObject(gp, group);		}		return group;
	}
   
	static boolean callUserMethod(Object obj, Object []s) {
		if (obj != null && s != null) {					
		try {
			Object par = null;
			String name = (String)s[0];
			if (name.equalsIgnoreCase("_this"));
			else obj = GuiMgr.SwUtil.getObject(name);
			if (obj != null) {	
				par = s[3];							// Object						
				if (s[2] != null && s[3] != null) {
					String type = (String)s[2];
					if (s[3] instanceof String) {
						String value = (String) s[3];
						if(value.length() > 0) {
						if (type.equalsIgnoreCase("_int"))
							par = new Integer(value);
						else if (type.equalsIgnoreCase("_boolean"))
							par = new Boolean(value);
						else if (type.equalsIgnoreCase("_byname"))
							par = GuiMgr.SwUtil.getObject(value);
						}
					}
				}
				String t =	"Name " + obj.getClass().getName() + "." + s[1]
					+ "(" + ((s[3]==null)? "":par.getClass().getName()) + ")"; 
				GuiMgr.SwUtil.runSafety(obj, (String)s[1], new Object [] {par}, t);
				
				Argument argv = new Argument();
				argv.setArgument(Itypes.NAME, name);
				argv.setArgument(Itypes.TYPE, new Integer(4));
				argv.setArgument(Itypes.OBJECT, t);
				ComMgr.getComMgr().postMsg(Itypes.SYSTEM, Itypes.TRACE, argv);
				return true;
			}
		}
		catch (Exception e) {
			System.out.println("Wrong :" + e.getMessage());}
		}
		return false;
	}
	
	static private Popup popup = null;
	static public void openPopup(Container ower, JComponent contents, Point p) {
		if (popup == null) {
			PopupFactory factory = PopupFactory.getSharedInstance();
			popup = factory.getPopup(ower, contents, p.x, p.y);
			popup.show();
		}
	}
	static public void closePopup() {
	   if (popup != null) {
		   popup.hide();
		   popup = null;
	   }
	}		
	static public void convertPoint(Container cp, Point p) {
 		if (cp != null)
	    try {
	 		Point lp = cp.getLocation(); 
	 		p.x += lp.x; 
	 		p.y += lp.y;
	 		convertPoint(cp.getParent(), p);
	    }
	    catch (Exception e) {}
	}
}
