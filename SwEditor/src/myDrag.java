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

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * myDrag is a class to provide drag behavour 
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
class myDrag implements DragSourceListener, DragGestureListener {
 	public myDrag(Component cmp) { 		DragSource dragSource = DragSource.getDefaultDragSource();
 		dragSource.createDefaultDragGestureRecognizer(cmp,
 		   DnDConstants.ACTION_COPY_OR_MOVE, this);
 	}

	public Transferable getTransferable() {
		return new myTransfer(draggedValues);
	}
	
	// this must be on ahead of getDraggedValues for overwriting use
	public void setDraggedValues(Object [] objs) {
		draggedValues = objs;	
	}
	
	// should overwrite these two methods
	public void getDraggedValues() {}
	public void processDrop(Object obj) {}
	
 	// DragGestureListener method

 	public void dragGestureRecognized(DragGestureEvent event)
 	{  
 	    getDraggedValues();
 	    Transferable transferable = getTransferable();
 	    event.startDrag(null, transferable, this);
 	}

 	// DragSourceListener methods

 	public void dragEnter(DragSourceDragEvent event)
 	{
 	}

 	public void dragOver(DragSourceDragEvent event)
 	{
 	}

 	public void dragExit(DragSourceEvent event)
 	{
 	}

 	public void dropActionChanged(DragSourceDragEvent event)
 	{
 	}

 	public void dragDropEnd(DragSourceDropEvent event)
 	{  if (event.getDropSuccess())
 	   {  int action = event.getDropAction();
 	      if (action == DnDConstants.ACTION_MOVE){  
 			  for (int i = 0; i < draggedValues.length; i++)
 	             processDrop(draggedValues[i]);
 	      }
 	   }
 	}
	private Object[] draggedValues = new Object [] {null};
}
