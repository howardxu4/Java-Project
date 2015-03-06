//******************************************************************************
// Package Declaration
//******************************************************************************

//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;
import javax.swing.tree.*;
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
import GuiMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * myBasic is a class of basic properties which configured by dynamic 
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
public class myBasic implements IComm
{
	
	private readXML rd = null;
	private Hashtable myBaseProps;
	private int startId;

	myBasic() {
		myBaseProps = new Hashtable();
		loadXml("XmlData/props.xml");		
	}
	
	// IComm Listener
	public void Acknowledge(int from, int info){}
		
	class baseProp {
		public int type;
		public String name;
		public Object[] array;
		public Object func;
		public baseProp (String name, int type, Object[] array, Object func) {
			this.type = type;
			this.name = name;
			this.array = array;
			this.func = func;
		}
	}
	
	private readXML rdXmlFile(String s) {
		try {		
			rd = new readXML();
			if (s.indexOf(':') == -1)
				rd.setDocument(SwUtil.getUrl(s));
			else
				rd.setDocument(s);	
			rd.trimDocument();
			return rd;
		}
		catch (Exception e) {}
		return null;
	}
		
	// prepare structure from xml
	private int cvtType(String s) {
		int rtn = LABL;
		int hide = 0;
		if (s.startsWith("HIDE")) {
			s = s.substring(5);		//HIDE_XXXX HIDE.XXX
			hide = HIDE;
		}
		if (s.equals("STR")) rtn = STR;
		else if (s.equals("INT")) rtn = INT;
		else if (s.equals("BOOL")) rtn = BOOL;
		else if (s.equals("COMB")) rtn = COMB;
		else if (s.equals("COLR")) rtn = COLR;
		else if (s.equals("ICON")) rtn = ICON;
		else if (s.equals("DIAL")) rtn = DIAL;
		else if (s.equals("TREE")) rtn = TREE;
		else if (s.equals("SUBT")) rtn = SUBT;
		else if (s.equals("CONT")) rtn = CONT;
		else if (s.equals("TAG")) rtn = TAG;
		else if (s.equals("VTAG")) rtn = VTAG;
		else if (s.equals("MENU")) rtn = MENU;
		else if (s.equals("MENUBAR")) rtn = MENUBAR;
		return rtn + hide;
	}
	
	private void createProperty(String name, String type, ArrayList alist, Object func){
		Object [] obj = null;
		if (alist.size() != 0) {
			obj = alist.toArray();
		}
		myBaseProps.put(name, new baseProp(name, cvtType(type), obj, func));
	}

	private void loadXml(String s) {
		SwUtil.setContext(this);
		rd = rdXmlFile(s);
		if (rd != null) {
			Node pnode = rd.getDocRoot();
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String name = node.getNodeName();
				String type = "";
				String func = null;
				ArrayList alist = new ArrayList();
				NodeList CnodeList = ((org.w3c.dom.Element)node).getChildNodes();
				for (int j = 0; j < CnodeList.getLength(); j++ ) {
					Node cnode = CnodeList.item(j);
					String nm = cnode.getNodeName();
					if (nm.equalsIgnoreCase("Type")) type = rd.get1stNodeValue(cnode);
					else if (nm.equalsIgnoreCase("Func")) func = rd.get1stNodeValue(cnode);
					else if (nm.equalsIgnoreCase("Array")) {
						NodeList SnodeList = ((org.w3c.dom.Element)cnode).getChildNodes();
						for (int k = 0; k < SnodeList.getLength(); k++ ) {
							try {
							Node snode = SnodeList.item(k);
							alist.add(rd.get1stNodeValue(snode));
							}
							catch (Exception e) { System.out.println(e.getMessage());}
						}
					}
				}
				createProperty(name, type, alist, func);
			}
		}
	}
	
	// above is load data structure from xml 
	// bellow is build property list from dtat structure

	private void buildProperty(String name, boolean isAttr, ArrayList nameList){
		Object [] obj = null;
		int type = LABL;
		Object func = null;
		baseProp bp = (baseProp)myBaseProps.get(name);
		if (bp != null) {
			obj = bp.array;
			type = bp.type;
			func = bp.func;
		}
		if (type == MENU || type == MENUBAR) ;		// ignore
		else if (type == TAG || type == VTAG) {
			if (type == TAG) {
				nameList.add(new myProp(startId++, LABL, name, "", null, null, NAME));
			}
			if (obj != null)
				for(int i=0; i<obj.length; i++) {
					if (((String)obj[i]).equalsIgnoreCase("Container")) {
						nameList.add(new myProp(startId++, CONT, (String)obj[i+1], "", null, null, DYNM));
					}
					else {
						buildProperty((String)obj[i+1], ((String)obj[i]).equalsIgnoreCase("Attribute"), nameList);
					}
					i++;
				}
		}
		else {
			int m = PROP;
			if (isAttr) name = (type<HIDE?"    !-- ":"    ") + name;
			if (isAttr)
				if (type<HIDE) 	m = ATTR;
				else 		m = KIDS;
			nameList.add(new myProp(startId++, type, name, "", obj, func, m));
			if (type==TREE) {
				for(int i=0; i<obj.length; i++) {
					baseProp bps = (baseProp)myBaseProps.get(obj[i]);
					if (bps != null) 
						nameList.add(new myProp(startId++, bps.type, bps.name, "", bps.array, bps.func, KIDS));
				}
			}
			else if (func != null) {	//<Func>4:_SYSTEM;Object,Method,ParType,ParValue</Func>
				String s = (String)func;
				int j = Integer.parseInt(s.substring(0,1));
				int k = s.indexOf(';');
				if (k != -1) {
					s = s.substring(k+1);
					for(int i=0; i<j; i++) {
						k = s.indexOf(',');
						if (k != -1) {
							buildProperty(s.substring(0,k), true, nameList);
							s = s.substring(k+1);
						}
						else
							buildProperty(s, true, nameList);	
					}
				}
			}	 
		}
	}
	
	public void buildProperty(String nm, ArrayList nameList) {
		this.startId = 0;
		buildProperty(nm, false, nameList);
	}	
	
	public Object [] getAllowd(String nm) {
		Object [] objs = null;
		baseProp bp = (baseProp)myBaseProps.get(nm);
		if (bp != null) {
			if (bp.type == MENUBAR) {
				int k = bp.array.length;
				objs = new Object[k];
				for (int i=0; i<k; i++) {
					baseProp bps = (baseProp)myBaseProps.get((String)bp.array[i]);
					if (bps != null) 
						objs[i] = new Object [] {bps.name, bps.array};
				}
			}
			else if (bp.type == MENU) {
				objs = new Object[1];
				objs[0] = new Object [] {bp.name, bp.array}; 
			}
		}
		return objs;
	}

}
