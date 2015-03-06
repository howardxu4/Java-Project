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
 * myTransfer is a class of transferable 
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
public class myTransfer implements Transferable
{  public myTransfer(Object[] files)
   {  fileList = new ArrayList(Arrays.asList(files));
   }

   public DataFlavor[] getTransferDataFlavors()
   {  return flavors;
   }

   public boolean isDataFlavorSupported(DataFlavor flavor)
   {  return Arrays.asList(flavors).contains(flavor);
   }

   public synchronized Object getTransferData
      (DataFlavor flavor)
      throws UnsupportedFlavorException
   {  if(flavor.equals(DataFlavor.javaFileListFlavor))
      {  return fileList;
      }
      else if(flavor.equals(DataFlavor.stringFlavor))
      {  return fileList.toString();
      }
      else
      {  throw new UnsupportedFlavorException(flavor);
      }
   }

   private static DataFlavor[] flavors =
      {  DataFlavor.javaFileListFlavor,
         DataFlavor.stringFlavor
      };

   private java.util.List fileList;
}
