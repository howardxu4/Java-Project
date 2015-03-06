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

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import org.w3c.dom.*;

import XmlMgr.*;
import ComMgr.*;
import GuiMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * myNode is a class to Node for Tree and XML 
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
public class myNode {
	public Node node;
	myNode (Node nd) {
		node = nd;
	}	
	public String toString() {
		return node.getNodeName();
	}
}