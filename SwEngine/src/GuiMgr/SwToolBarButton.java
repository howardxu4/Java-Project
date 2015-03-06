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
 * SwToolBarButton is a class of AbstractButton which configured by XML 
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

public class SwToolBarButton extends AbstractAction implements Iforward
{
	public SwToolBarButton (String text, Icon ic, String tooltip, String userdef) {		if (tooltip == null) tooltip = "Click " + text + " button to action";
//		this.putValue(Action.NAME, text);		this.putValue(Action.SMALL_ICON, ic);		this.putValue(Action.SHORT_DESCRIPTION, tooltip);
		this.putValue("myID", text);
		int k = SwHelper.getUserDef(userdef);
		if (k != -1) this.userType = k;		// ...
	}
		public SwToolBarButton (readXML rd, Node pnode){		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				String value = rd.get1stNodeValue(node);
				if (nm.equalsIgnoreCase("#text")) ;
				else if (nm.equalsIgnoreCase("Name")) {
					this.putValue("myID", value);
				}
				else if (nm.equalsIgnoreCase("Icon")) {
					this.putValue(Action.SMALL_ICON, SwUtil.getImageIcon(value));				}
				else if (nm.equalsIgnoreCase("SaveId")) {
					SwUtil.saveObject(value, this);
				}
				else if (nm.equalsIgnoreCase("UserDef")) {					
					listener = SwHelper.procUserDef(this, rd, node);
				}
				else if (nm.equalsIgnoreCase("ToolTipText")) {					
					this.putValue(Action.SHORT_DESCRIPTION, value);
				}
			}		}		catch (Exception e) {			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"ToolBarButton Exception: " + e.getStackTrace());		}			
	}	
	public void actionPerformed (ActionEvent e) {		boolean b = false;
		if ( userType == SYSTEM) {
			if (listener != null && !listener.equals(SwClbk.getCallBack())){
				((ActionListener)listener).actionPerformed(e);
				return;
			}
			String [] s = getMethodDecl();
			if (s != null) {
				Object [] sc = new Object[4];
				for(int i=0; i<4; i++) sc[i] = s[i];
				if (s[3] != null) {
					if(s[3].equalsIgnoreCase("_value")) 
						sc[3] = "true";
					else if(s[3].equals("_component"))
						sc[3] =  e.getSource();
					else if(s[3].equals("_event"))
						sc[3] = e;
				}
				b = SwHelper.callUserMethod(e.getSource(), sc);
			}
		}
		if (!b) {
		Argument argv = new Argument();
		argv.setArgument(Itypes.NAME, this.getValue("myID").toString());
		argv.setArgument(Itypes.OBJECT, e.getSource());
		argv.setArgument(Itypes.TYPE, new Integer(Itypes.ACTION));
		argv.setArgument(Itypes.EVENT, e);
		ComMgr.getComMgr().sendMsg(this.userType, Itypes.BUTTON, argv);
		}
	}
	
	private Object listener;
	// Implements Iforward
	public void setMethodDecl(String [] decl) {
		this.methodDecl = decl;
	}
	public void setUserType( int t) {
		this.userType = t;
	}
	public String [] getMethodDecl() {
		return this.methodDecl;
	}
	public int getUserType() {
		return this.userType;
	}
	private String [] methodDecl = null;
	private int userType = CALLBK;
	
}
