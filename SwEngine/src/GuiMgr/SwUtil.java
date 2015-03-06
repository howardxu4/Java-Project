//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;
import javax.swing.tree.*;
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
 * SwUtil is a class of Utility which provides the methods for common interesting 
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
*/public class SwUtil 
{
	private static Object thisObj = null;			// URL codebase
	private static Component thisRoot = null;		// Look and Fell root
	private static String title = "Swing Engine Demo";	// frame title
	
	// Set the frame title
	public static void setTitle(String s) {
		title = s;
	}
		 
    // Possible Look & Feels
	private static final String [] lafs = new String [] {
            "javax.swing.plaf.metal.MetalLookAndFeel",
            "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
			"javax.swing.plaf.basic.BasicLookAndFeel",
			"javax.swing.plaf.multi.MultiLookAndFeel",
            "com.sun.java.swing.plaf.mac.MacLookAndFeel"
		};

	// Common methods
	static public void setContext(Object obj) {
		if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
			LogTrace.getLog().Trace(LogTrace.INFO, 
				"Set to " + obj.getClass().getName() + " *****");
		thisObj = obj;
		if (obj instanceof Component) thisRoot = (Component)obj;
	}
	
	static public boolean isApplet() {
		return (thisObj instanceof JApplet);
	}

	static public void exit(int e) {
		System.exit(e);	
	}
	
	static public void setLookAndFeel(int m) {
		final int n = m % lafs.length;
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
			try {
				UIManager.setLookAndFeel(lafs[n]);
				javax.swing.SwingUtilities.updateComponentTreeUI(thisRoot);	
				if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
					LogTrace.getLog().Trace(LogTrace.INFO, 
						"Set look and feel to " + lafs[n]);
			}
			catch (Exception e) {
				if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
					LogTrace.getLog().Trace(LogTrace.EXECPT, 
						" on Look and Feel: " + e.getMessage()); }
//				}
//			});
	}

	static public void swichToolTip(boolean b) {
		ToolTipManager.sharedInstance().setEnabled(b);	
	}
	
	static public void runSafety(final Object cs, final String ms, final Object [] obj, final String s) 
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					if (obj == null) ObjMgr.getObjMgr().callMethod( cs, ms);
					else ObjMgr.getObjMgr().callMethod( cs, ms, obj);
				}
				catch (Exception e) { 
				if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
					LogTrace.getLog().Trace(LogTrace.EXECPT, 
						((s != null)? s:" runSafety on method : "  + e.getMessage())); 
				}
			}
		});
	}
	
	static public void genMethod(Object takeThis, int type, Object info) {
		FuncGen.genFunc(takeThis, type, info);
	}
		
	static public URL getUrl(String f) {
		String fl = "/";
		if (f.charAt(0) != '/') fl += f;
		else fl = f;
		if (thisObj != null) {
			try {
				URL url = thisObj.getClass().getResource(fl);
				if (url != null ) {
					if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"URL: " + f);
					return url;
				}
                                else if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"NO-URL: " + fl);
			}
			catch (Exception e) {
                            if(LogTrace.getLog().chkLevel(LogTrace.EXECPT))
                                LogTrace.getLog().Trace(LogTrace.EXECPT, e.getMessage());
                        }
			try {
				if (thisObj instanceof JApplet) {
					SwApplet applet = (SwApplet)thisObj;
					if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"CodeBase: " + applet.getCodeBase().toString());
					if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"my URL: " + f);
					URL url = new URL(applet.getCodeBase(), fl);
					if (url != null) return url;
					String s = applet.getCodeBase().toString() + f;		//??
					if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"new URL fails on " + s);
					return new URL(s);
				}
				else {
					String s = "file:///" + System.getProperty("user.dir") + fl;
					if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
						LogTrace.getLog().Trace(LogTrace.DEVELOP, 
							"my File: " + f);
					return new URL(s);
				}
			}
			catch (Exception ee) {
				if (LogTrace.getLog().chkLevel(LogTrace.EXECPT+LogTrace.DEVELOP)) 
					LogTrace.getLog().Trace(LogTrace.EXECPT, 
						f + " fails -> " + ee.getMessage());
					return null;
			}
		}
		if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
			LogTrace.getLog().Trace(LogTrace.ERROR, 
				"The root object need set first!");			
		return null;	
	}
	
	static public ImageIcon getImageIcon(String f) {
		try {
			URL url = getUrl(f);
			if (url != null){
				if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
					LogTrace.getLog().Trace(LogTrace.DEVELOP, 
						"URL = " + url.toString());				
				return new ImageIcon(url);
			}
			else if (new File(f).exists()) 
				return new ImageIcon(f);
		}
		catch (Exception e) { 
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Check: " + e.getMessage());
			try {
				if (thisObj instanceof JApplet) {
					JApplet applet = (JApplet)thisObj;
					return new ImageIcon(applet.getCodeBase(), f);
				}
				else {
					String s = System.getProperty("user.dir") + "/" + f;
					return new ImageIcon(f);
				}
			}
			catch (Exception ee) {
				if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
					LogTrace.getLog().Trace(LogTrace.EXECPT, 
						"Check again: " + ee.getMessage());
			}
			if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
				LogTrace.getLog().Trace(LogTrace.ERROR, 
					" on image file " + f);
		}
		return null;
	}
	
	static public void showComp(Object cp) {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		JFrame frame = new JFrame(title);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(SwClbk.getCallBack());
	    frame.addComponentListener(SwClbk.getCallBack());
		frame.setContentPane((JComponent)cp);
		saveObject("_CurrWindow", frame);
			
		//Display the window.
		frame.pack();
		frame.setVisible(true);	
	}
	
	static public boolean chkXmlType(String f, String s) {
		boolean b = s.equalsIgnoreCase("UIConfig");
		if (!b) 
			if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
				LogTrace.getLog().Trace(LogTrace.ERROR, 
					"The wrong XML format, check File: " + f);
		return b;
	}
	
	static public boolean startXML(String s) {
		Object cp = chkXmlFile(s);
		if (cp != null) {
			if (cp instanceof Component) thisRoot = (Component)cp;
			if(cp instanceof SwFrame) 
				((SwFrame)cp).show();
			else if(cp instanceof SwWindow)
				((SwWindow)cp).show();
			else if(cp instanceof SwDialog) {
				((SwDialog)cp).setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				((SwDialog)cp).setVisible(true);
			}
			else 
				showComp(cp);
			return true;
		}
		return false;
	}
	
	static public void saveObject(String s, Object o) {
		if (s != null && s.length() > 0) {
			if (!ObjMgr.getObjMgr().saveGC(s, o))
				if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
					LogTrace.getLog().Trace(LogTrace.ERROR, 
						"Save object failure, check name " + s);
		}
	}
	
	static public Object getObject(String s) {
		return ObjMgr.getObjMgr().getGC(s);		
	}

	static public boolean delObject(String s) {
		return ObjMgr.getObjMgr().delGC(s);
	}
	
	static public Component chkXmlFile(String xml) {
		if (xml.length() > 0) {							// ignore the node itself
			SwEngine scp = new SwEngine(xml);
			if (scp.getLength() != 1) {
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"XML Should has only one component, but has " + scp.getLength());
			}
			return scp.getComponent();
		}
		return null;
	}

	static public void generateCode() {
		FileOps fop = new FileOps();
		File myFile = fop.fileOpen("hello.java", "", false);
		if (myFile != null) {
			int n = fop.askConf("Output format likes VB style, press Yes\nUsing one big switch style, press No\n");
			new CodeGen().genCode(myFile.getPath(), n); 
		}
	}	
	
}
