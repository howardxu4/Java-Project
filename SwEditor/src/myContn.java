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
 * myContn is a class of container which configured by dynamic 
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
public class myContn extends Box implements MouseListener
{
	private ArrayList cmps = new ArrayList();
	private Box cbox = null;
	private String IconPath = "Resources/images/icons/";	private MouseListener listener;
	private Box vbox = null;		// current visual location 
	private Box sbox = null;		// current selected
								   
	myContn(ArrayList nameList, int start)  {
		super(BoxLayout.Y_AXIS);
		int i = start;
		this.listener = (MouseListener)GuiMgr.SwUtil.getObject("popMenu");
		do {
			myProp mp = (myProp)nameList.get(i);
			if (i > start && mp.getType() == IComm.CONT) break;
//System.out.println("Add container " + i);			
			this.add(mp);
		} while(++i < nameList.size());
//System.out.println("? the container " + i + "reach " + nameList.size());			
		cbox = Box.createVerticalBox();
		if (i < nameList.size()) {
			cbox.setPreferredSize(new Dimension(240, 32));
			cbox.setForeground(new Color(-1));
		}
		this.add(cbox);
	}
	
	public ArrayList getCmps() {
		return cmps;
	}
	
	public void procComponent(int op) {
		int k = cbox.getComponentCount();
		int i=0;
		for( ;i<k;i++)
			if(cbox.getComponent(i) == sbox) {
//				System.out.print("@@@@@@@ ----> " + i);
				break;
			}
		switch (op) {
		case 0:	// UP
			if (i > 0) {
				cbox.remove(i);	Object o = cmps.remove(i);
				cbox.add(sbox,i-1);		cmps.add(i-1, o);
			}
			break;
		case 1: // DOWN
			if (i < k-1) {
				cbox.remove(i);	Object o = cmps.remove(i);
				cbox.add(sbox, i+1);	cmps.add(i+1, o);
			}
			break;
		case 2: // DEL
			cbox.remove(i); cmps.remove(i);
			break;
		default:	// HELP
		System.out.println(" Operator " + op);
		}
	}
	
	// tag      attributes   value  name;text;saveid   
	// name [ n = v , n = v ] { v } ( n : v ; n : v )
	public void addComponent(String ctrl, String name, Integer seq) {
		try {
			vbox = Box.createHorizontalBox(); 
			String s = ctrl;
			int i = name.indexOf('{');
			int j = name.indexOf("Title");
			if (j != -1) {
				s = name.substring(j);
				i = s.indexOf('=');
				j = s.indexOf(',');
				if (j == -1) j = s.indexOf(']');
				s = s.substring(i+1,j);
			}
			else if (i != -1) {
				s = name.substring(i+1, name.indexOf('}'));
			}
			else {
				i = name.indexOf(':');
				if (i != -1) {
					j = name.indexOf(';');
					if (j == -1) j = name.indexOf(')');
					s = name.substring(i+1,j);
				}
			}
			cmps.add( new Object [] {seq, s});
			JLabel lb = new JLabel(s);
			ImageIcon icon = null;			try {icon = new ImageIcon(IconPath + ctrl + ".png");}			catch (Exception e) {}
			lb.setIcon(icon);
			i = name.indexOf('[');
			if (i != -1) 
				lb.setToolTipText( name.substring(i, name.indexOf(']')+1));
			
			lb.addMouseListener(this); 
			//	System.out.println("Set icon ==>" + IconPath + ctrl + ".png");
			vbox.add(lb);
			vbox.add(Box.createHorizontalGlue());
			vbox.setBorder(BorderFactory.createLineBorder(Color.lightGray));
			vbox.addMouseListener(this);
			cbox.add(vbox);
		}
		catch (Exception e) {System.out.println(e.getMessage());}
	}
	// MouseListener
	public void mouseClicked( MouseEvent e ) {
		if (sbox != vbox){
			if (sbox != null)
			sbox.setBorder(BorderFactory.createLineBorder(Color.lightGray));
			sbox = vbox;
//			for(int i=0; i<cbox.getComponentCount();i++)
//				if(cbox.getComponent(i) == sbox)
//					System.out.println("@@@@@@@ ----> " + i);
		}
		sbox.setBorder(BorderFactory.createLineBorder(Color.red));
	}
	public void mouseEntered( MouseEvent e ) {
		Object obj = e.getSource();
		vbox = (obj instanceof JLabel)?(Box)((JLabel)obj).getParent():(Box)obj;
		if (sbox != vbox)
		vbox.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	public void mouseExited( MouseEvent e ) {
		if (sbox != vbox)
		vbox.setBorder(BorderFactory.createLineBorder(Color.lightGray));
	}
	public void mousePressed( MouseEvent e ) {
		mouseClicked(e);
		listener.mousePressed(e);
	}
	public void mouseReleased( MouseEvent e ) {
		listener.mouseReleased(e);
	}
}
