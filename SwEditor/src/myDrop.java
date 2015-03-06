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
 * myDrop is a class to provide drop behavour 
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
//   new DropTarget(obj, new myDrop(obj));

public class myDrop implements DropTargetListener
{  
   public myDrop(Component cmp)
   {  
	   super();
	   new DropTarget(cmp, this);
   }

   public boolean isDragAcceptable(DropTargetDragEvent event)
   {  // usually, you check the available data flavors here
      // in this program, we accept all flavors
      return (event.getDropAction()
         & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
   }

   public boolean isDropAcceptable(DropTargetDropEvent event)
   {  // usually, you check the available data flavors here
      // in this program, we accept all flavors
      return (event.getDropAction()
         & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
   }

   public void dragEnter(DropTargetDragEvent event)
   {  int a = event.getDropAction();
      if (!isDragAcceptable(event))
      {  event.rejectDrag();
         return;
      }
   }

   public void dragExit(DropTargetEvent event)
   {
   }

   public void dragOver(DropTargetDragEvent event)
   {  // you can provide visual feedback here
   }

   public void dropActionChanged(DropTargetDragEvent event)
   {  if (!isDragAcceptable(event))
      {  event.rejectDrag();
         return;
      }
   }

   public void drop(DropTargetDropEvent event)
   {  if (!isDropAcceptable(event))
      {  event.rejectDrop();
         return;
      }

      event.acceptDrop(DnDConstants.ACTION_COPY);
	  processDrop( event.getTransferable());
	  
      event.dropComplete(true);
   }

   // should overwrite this method
   public void processDrop(Object obj) {
   	  System.out.println(" ==>  process... ");
   }
}
