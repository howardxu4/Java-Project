package ObjMgr;

import javax.swing.*;import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

import GuiMgr.*;

public class chgDemo
{
	static final private String [] demos = new String [] {
				"ButtonDemo",
				"ColorChooserDemo", 
				"ComboBoxDemo",
				"FileChooserDemo",
				"HtmlDemo",
				"InternalFrameDemo",
				"ListDemo",
				"OptionPaneDemo",
				"ProgressBarDemo",
				"ScrollPaneDemo",
				"SliderDemo",
				"SplitPaneDemo",
				"TabbedPaneDemo",
				"TableDemo",
				"ToolTipDemo",
				"TreeDemo"
	};
	
	static private int indx = 1;
	static private chgDemo sd = null;
	
	public static chgDemo getDemo() {
		if (sd == null) new chgDemo();
		return sd;
	}
	
	private chgDemo() {
		sd = this;	
	}
	
	private void setDemoPanel(String s) {
		int i = 0;
		for(; i<demos.length; i++) 
			if (demos[i].equalsIgnoreCase(s)) {
				indx = i;
				break;
			}
	}	
	
	private JPanel getDemoPanel() {
		return getDemoPanel(indx++);
	}
	
	private JPanel getDemoPanel(int n) {
		JPanel p = null;
		try {
			n = n % demos.length;
			String csob = demos[n];
			Class c = Class.forName("SwingSet2");
			Object demo = ObjMgr.getObjMgr().getObject(csob, new Class[] {c}, new Object [] {null});
			p = (JPanel)ObjMgr.getObjMgr().callMethod(demo, "getDemoPanel");
		} catch (Exception ee) {
			System.err.println(ee.getMessage());
			p = new JPanel();
			p.add (new JLabel("Can't find Sun's SwingSet2.jar"));
		}
		return p;
	}
	
	// Object is TabbedPane, n is nth Tab	-- to next
	public void changeDemo(Object obj, int n) {	
		JTabbedPane o = (JTabbedPane)obj;
		if (o != null) {  	
			n = n % o.getTabCount();
			String t = o.getTitleAt(n);
			o.remove(n);
			o.add( getDemoPanel(),t, n);
			o.setSelectedIndex(n);
		}
	}
	
	// String is demo name, Object is TabbedPane, n is nth Tab
	public void chgNthDemo(String nm, Object obj, int n) {
		setDemoPanel(nm);
		changeDemo(obj, n);
	}
	
	private JPanel getMyDemoPanel(String nm) {
		JPanel DemoPanel = (JPanel)ObjMgr.getObjMgr().getGC(nm);
		if (DemoPanel == null) {
			SwEngine scp = new SwEngine("XmlData/" + nm + ".xml");
			if (scp.getLength() != 1) 
				System.out.println("Should be only one component, but has " + scp.getLength());	
			DemoPanel = (JPanel)scp.getComponent();
		}	
		return DemoPanel;
	}
	
	private void chgMyPage(String nm) {
		String s = "";
		if (p0 != null)		// demo1 no p0
		try {
			s = p0.getPage().toString();
			p0.setPage(s.substring(0,s.lastIndexOf("/")+1) + nm.substring(2) + ".java");
		}
		catch (Exception e) { System.out.println("Change page fails :" + s ); }
		try {	
			s = p1.getPage().toString();
			p1.setPage(s.substring(0,s.lastIndexOf("/")+1) + nm + ".java");
		}
		catch (Exception e) { System.out.println("Change page fails :" + s ); }
		try {
			s = p2.getPage().toString();
			p2.setPage(s.substring(0,s.lastIndexOf("/")+1) + nm + ".xml");
		}
		catch (Exception e) { System.out.println("Change page fails :" + s ); }
	}
	
	public void chgMyDemo(String nm, Object obj, int n) {
		JTabbedPane o = (JTabbedPane)obj;
		if (o != null) { 
			String t = o.getTitleAt(n);
			o.remove(n);
			o.add( getMyDemoPanel(nm),t, n);
			chgMyPage(nm);
		}
	}
	
	public void chkDemo(JTabbedPane tbp) {
		if (tbp != null) 
			if (tbp.getTitleAt(0).compareToIgnoreCase("Sun Demo") != 0) 
				tbp.add( getDemoPanel(0), "Sun Demo", 0);
	}
	
	public void setPages(JEditorPane p00, JEditorPane p01, JEditorPane p02) {
		p0 = p00;
		p1 = p01;
		p2 = p02;
	}
	
	private JEditorPane p0, p1, p2;
}
