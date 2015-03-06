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
import java.lang.reflect.Constructor;

import org.w3c.dom.*;

import XmlMgr.*;
import ObjMgr.*;
import ComMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwComponent is a class of build JComponent which configured by XML 
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

public class SwComponent
{
	private Object obj = null;
	public SwComponent(readXML rd, Node pnode) {
		obj = buildComponent(rd, pnode);
	}
	
	public Object getObject() {
		return obj;
	}
	
	private Object buildComponent(readXML rd, Node pnode) {
		try {
			String csob = null;		// Class name
			String cstp = null;		// Class type
			String csmd = null;		// Class method
			Object argv = null;		// Argument value
			String text = "";
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node); 
				if (nm.equalsIgnoreCase("#text")) ;
				else if (nm.equalsIgnoreCase("Name")) {
					text = value;
				}
				else if (nm.equalsIgnoreCase("Class")) {
					csob = value;
				}
				else if (nm.equalsIgnoreCase("ArgType")) {
					cstp = value;
				}
				else if (nm.equalsIgnoreCase("ArgValue")) {
					if (value.length() > 0)
						argv = SwUtil.getObject(value);
				}
				else if (nm.equalsIgnoreCase("Method")) {
					csmd = value;
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING))
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in crtComponent"); 
			}
			if (csob != null && cstp != null && csmd != null) {
				Class c = Class.forName(cstp);
				Object demo = ObjMgr.getObjMgr().getObject(csob, new Class[] {c}, new Object [] {argv});
				Object p = ObjMgr.getObjMgr().callMethod(demo, csmd);
				return p;
			}
			else {
				Class cs = Class.forName("java.lang.String");
				Class c = Class.forName(csob);
				Constructor st = c.getDeclaredConstructor (new Class[] {cs});
				Object cpnt = st.newInstance(new Object[] {text});
				return cpnt;
			}	
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"crtComponent Exception: " + e.getStackTrace()); 
		}
		return null;
	}
	
}
