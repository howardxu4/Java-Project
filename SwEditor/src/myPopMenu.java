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
 * myPopMenu is a class of popup menu which configured by dynamic 
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

public class myPopMenu extends JPopupMenu implements MouseListener
{
	private	JPopupMenu popup;	private ActionListener listener;

	public myPopMenu(Object [] menus, ActionListener l, JComponent cmpt)
	{
		super();
		listener = l;
		popup = this;
		try {
		if (menus != null) {
			for (int i=0; i<menus.length; i++) {
				Object [] md = (Object [])menus[i];
				if (md[0] != null && ((String)md[0]).length() > 0)
					buildSubMenu((String)md[0], (Object [])md[1]);
				else
					buildMenu((Object [])md[1]);
			}
			if (cmpt != null)
				cmpt.addMouseListener(this);
		}
		}catch (Exception e) {System.out.println("?? " + e.getMessage()); }
	}
	
	private void buildMenu( Object [] mnitems ) {
		for (int i=0; i<mnitems.length; i++) {
			if (((String)mnitems[i]).equals("---"))	// Separator
				this.addSeparator();
			else {
			JMenuItem menuitem = (JMenuItem)this.add( new JMenuItem((String)mnitems[i]));
			menuitem.addActionListener(listener);
			}
		}
	}
	
	private void buildSubMenu(String mn, Object [] mnitems ) {
		JMenu menu = new JMenu(mn);
		for (int i=0; i<mnitems.length; i++) {
			if (((String)mnitems[i]).equals("---"))	// Separator
				menu.addSeparator();
			else {
			JMenuItem menuitem = (JMenuItem)menu.add( new JMenuItem((String)mnitems[i]));
			menuitem.addActionListener(listener);
			}
		}
		this.add(menu);
	}
	
	// MouseListener
	public void mouseClicked( MouseEvent e ) {}
	
	public void mouseEntered( MouseEvent e ) {}
	
	public void mouseExited( MouseEvent e ) {}
		
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) 
            popup.show(e.getComponent(), e.getX(), e.getY());
    } 	
	
}	

