//******************************************************************************
// Package Declaration
//******************************************************************************

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
import GuiMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * myXML is a class of XML converter to translate tree to XML Node 
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
public class myXML
{
	private final String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private final String nl = "\n";
	private final String pad = "   ";
//	private writeXML wr;
	private String buf;
	
	public myXML () {
	}
	
	public void convertTree(DefaultMutableTreeNode tnode) {
//		wr = new writeXML();
		String s = "";
		int n = 0;
		myNode mnode = ((myNode)tnode.getUserObject());
		boolean isRoot = (mnode.toString().equals("UIConfig")) ;
		buf = xmlstr + nl;
		if (!isRoot) {
//			wr.setElement(n++, "UIConfig", null, true);
			buf += "<UIConfig>" + nl;
			s = pad;
		}
		traverseTree(tnode, s);
		if (!isRoot) 
			buf += "</UIConfig>" + nl;
		outFile(buf);

//		traverseTree(tnode, n);
//		readXML rd = new readXML();
//		rd.setDocument(wr.getDocument());
//		System.out.println(rd.dumpAll());
//		wr.createXML(System.getProperty("user.dir") + "\\tmp1.xml");
	}
	
// Temperary comment these to using writeXML method due to output format	
/*	
	private void traverseTree(DefaultMutableTreeNode tnode, int n) {
		myNode mnode = ((myNode)tnode.getUserObject());
		Node pnode = mnode.node; 
		String t = null;
		if (pnode.getFirstChild() != null)
			if (pnode.getFirstChild().getNodeValue() != null) 
				t = pnode.getFirstChild().getNodeValue().trim();
		wr.setElement(n, mnode.toString(), t, true);
		if (pnode.hasAttributes())
			traverseAttr(pnode, n);
		for (int i=0; i<tnode.getChildCount(); i++) {	
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			traverseTree(node, n+1);
		}
	}
	private void traverseAttr(Node pnode, int n) {
		NamedNodeMap  attrs = pnode.getAttributes();
		for(int i = 0; i < attrs.getLength(); i++) {
			Node node = attrs.item(i);
			wr.setAttribute(n, node.getNodeName(), node.getNodeValue());
		}
	}	
*/	
	private void traverseTree(DefaultMutableTreeNode tnode, String s) {
		myNode mnode = ((myNode)tnode.getUserObject());
		Node pnode = mnode.node; 
		String t = "<" + mnode.toString();
		if (pnode.hasAttributes())
			t += traverseAttr(pnode);
		t +=">";
		if (pnode.getFirstChild() != null)
			if (pnode.getFirstChild().getNodeValue() != null)
				t += getEncode(pnode.getFirstChild().getNodeValue().trim());
		int k = tnode.getChildCount();
		if (k > 0) buf += s + t + nl;
		for (int i=0; i<k; i++) {	
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			traverseTree(node, s+pad);
		}
		if (k == 0) buf += s + t + "</" + mnode.toString() + ">" + nl;
		else buf += s + "</" + mnode.toString() + ">" + nl;
	}
	private String traverseAttr(Node pnode) {
		NamedNodeMap  attrs = pnode.getAttributes();
		String s = "";
		for(int i = 0; i < attrs.getLength(); i++) {
			Node node = attrs.item(i);
			s += " " + node.getNodeName() + "=\"" + node.getNodeValue()+"\"";
		}
		return s;	
	}
	
	private String getEncode(String s) {
		String t = "";
		for (int i=0; i< s.length(); i++) {
			char c = s.charAt(i);
			if( c == '<') t += "&lt;";
			else if (c == '>') t += "&gt;";
			else t += c;
		}
		return t;	
	}
	private void outFile(String s) {
		try {
		File file = new File(System.getProperty("user.dir") + "\\tmp.xml");
		FileWriter out = new FileWriter (file);
		out.write(s);
		out.close();
		}
		catch (Exception e) { System.out.println("File IO: " + e.getMessage()); }
	}
}
