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
 * myProp is a class of property which configured by dynamic 
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
public class myComm
{
	private static Hashtable listener;	
	private static myComm Comm;
	
	public static myComm getComm()
	{
		if(Comm == null) {
			Comm = new myComm();
			listener = new Hashtable();
		}
		return Comm;
	}
	
	public void addListener(Object n, Object l)
	{
		listener.put(n, l);
	}
	
	public void removeListener(Object n)
	{
		if (n == null) listener.clear();
		else listener.remove(n);
	}
		
	public void notify(int from, int to, int info)
	{
		SwHelper.closePopup();		// to support popup ?		
		IComm a = (IComm)listener.get(new Integer(to));
		if (a != null) a.Acknowledge(from, info);
	}
}
