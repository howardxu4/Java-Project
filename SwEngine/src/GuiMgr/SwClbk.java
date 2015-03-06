//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
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

import XmlMgr.*;
import ComMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwClbk is a class of Callback which implements all listener interfaces and
 * distributes the event message to the proper real listener
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
public class SwClbk extends WindowAdapter implements ActionListener, ComponentListener,
	TreeSelectionListener, ChangeListener, MouseListener, ListSelectionListener, 
	FocusListener, HyperlinkListener, AdjustmentListener, KeyListener, Itypes
{
	private static SwClbk clbk;
	private static Hashtable Clstbl;
	
	static public SwClbk getCallBack() {
		if (clbk == null) new SwClbk();
		return clbk;
	}
	
	public SwClbk() {
		clbk = this;
		this.Clstbl = new Hashtable();
		for (int i=0; i < ComMgr.ObjName.length; i++) {
			String s = "GuiMgr.Sw" + ComMgr.ObjName[i];
			this.Clstbl.put((Object)s, (Object)new Integer(i));
		}
	}
	
	public int getClsNum(String s) {
		Object obj = this.Clstbl.get("GuiMgr.Sw" + s);
		if (obj != null)
			return ((Integer)obj).intValue();
		return UNKNOWN;
	}
	// WindowAdapter -- WindowListener 
	public void windowClosing(WindowEvent e) {		System.exit(0);	}
	public void windowDeactivated(WindowEvent e) {  
	   SwHelper.closePopup();
    }         
	
	// ComponentListener
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
	   SwHelper.closePopup();
    }

    public void componentResized(ComponentEvent e) {
 	   SwHelper.closePopup();
    }

    public void componentShown(ComponentEvent e) {
    }
	
	// Distribute event
	private void eventProcess(Object obj, int eType, Object evt) {
		SwHelper.closePopup();		// to support popup ?
		Argument argv = new Argument();
		int type = UNKNOWN;
		try { type = ((Integer)Clstbl.get(obj.getClass().getName())).intValue();} 
		catch (Exception ex) { 
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Can't find " + obj.getClass().getName() + "!!!");
		}
		if (type >= UNKNOWN) return;
		
		String name = ((Component)obj).getName();
		switch (type) {
			case BUTTON:
			case CHECKBOX:
			case CHECKBOXMENUITEM:
			case MENUITEM:
			case RADIOBUTTON:
			case RADIOBUTTONMENUITEM:
			case TOGGLEBUTTON:				
				if (name == null || name.length() == 0) 
					name = ((AbstractButton)obj).getText();
				argv.setArgument(VALUE, new Boolean(((AbstractButton)obj).isSelected()).toString());
				break;
			case COLORCHOOSER:
				argv.setArgument(VALUE, 
					new Integer(((JColorChooser)obj).getColor().getRGB()).toString());
				break;
			case COMBOBOX:
				argv.setArgument(VALUE, ((JComboBox)obj).getSelectedItem().toString());
				break;
			case TREE:
				if (((JTree)obj).getSelectionPath() == null) return;
				argv.setArgument(VALUE, ((JTree)obj).getSelectionPath().toString());
				break;
			case LIST:
				if (((SwList)obj).getValueIsAdjusting()) 
					argv.setArgument(VALUE, ((SwList)obj).getSelectedValue().toString());
				else return;
				break;
			case TABBEDPANE:
				argv.setArgument(VALUE, new Integer(((SwTabbedPane)obj).getSelectedIndex()).toString());
				break;
			case SLIDER:
				argv.setArgument(VALUE,  new Integer(((SwSlider)obj).getValue()).toString());
				break;		
			case SPINER:
				try {argv.setArgument(VALUE, ((SwSpiner)obj).getValue().toString());} 
				catch (Exception ex) {}
				break;
			case SCROLLBAR:
				argv.setArgument(VALUE,  new Integer(((SwScrollBar)obj).getValue()).toString());
				break;
			case TOOLBAR:	// ToolBar process is in SwUtil.actionPerformed
			break;
			case PANEL:
			case LABEL:
			break;			// mouselistener only
			default:
		}
			
		if (name == null) {
			if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					type+" No name found on "+eType+", use default name --> NoName");
			name = "NoName";
		}
				
		argv.setArgument(NAME, name);
		argv.setArgument(OBJECT, obj);
		argv.setArgument(TYPE, new Integer(eType));
		argv.setArgument(EVENT, evt);
		
		int sndType = CALLBK;
		try { sndType = ((Iforward)obj).getUserType(); }
		catch (Exception ey) {}
		
		boolean b = false;
		if ( sndType == SYSTEM && obj instanceof Iforward) {
			String [] s = ((Iforward)obj).getMethodDecl();
			if (s != null) {
				Object [] sc = new Object[4];
				for(int i=0; i<4; i++) sc[i] = s[i];
				if (s[3] != null) {
					if(s[3].equalsIgnoreCase("_value")) 
						sc[3] = argv.getArgument(VALUE);
					else if(s[3].equals("_component"))
						sc[3] = obj;
					else if(s[3].equals("_event"))
						sc[3] = evt;
				}
				b = SwHelper.callUserMethod(obj, sc);
			}
		}
		if (!b)
		ComMgr.getComMgr().postMsg(sndType, type, argv);	// using post no hang system
	}
	
	// ActionListener
	public void actionPerformed(ActionEvent e) {
		eventProcess(e.getSource(), ComMgr.ACTION, e);
	}
		
	// TreeSelectionListener
	public void valueChanged(TreeSelectionEvent e) {
		eventProcess(e.getSource(), ComMgr.TREESELECTION, e);
	}
	
	// ListSelectionListener
	public void valueChanged(ListSelectionEvent e) {
		eventProcess(e.getSource(), ComMgr.LISTSELECTION, e);
	}
	
	// ChangeListener
	public void stateChanged(ChangeEvent e) {
		eventProcess(e.getSource(), ComMgr.CHANGE, e);
	}

	// AdjustmentListener
	public void adjustmentValueChanged(AdjustmentEvent e) {
		eventProcess(e.getSource(), ComMgr.ADJUSTMENT, e);
	}

	// MouseListener
	public void mouseClicked( MouseEvent e ) {
		eventProcess(e.getSource(), ComMgr.MOUSECLICKED, e);
	}
	public void mouseEntered( MouseEvent e ) {
		eventProcess(e.getSource(), ComMgr.MOUSEENTERED, e);
	}
	public void mouseExited( MouseEvent e ) {
		eventProcess(e.getSource(), ComMgr.MOUSEEXIT, e);
	}
	public void mousePressed( MouseEvent e ) {
		eventProcess(e.getSource(), ComMgr.MOUSEPRESSED, e);
	}
	public void mouseReleased( MouseEvent e ) {
		eventProcess(e.getSource(), ComMgr.MOUSERELEASED, e);
	}
		
	// FocusListener
	public void focusGained( FocusEvent e ) {
		eventProcess(e.getSource(), ComMgr.FOCUSGAINED, e);
	}
	public void focusLost( FocusEvent e ) {
		eventProcess(e.getSource(), ComMgr.FOCUSLOST, e);
	}
	
	// KeyListener
	public void keyPressed( KeyEvent e ) {
		eventProcess(e.getSource(), ComMgr.KEYPRESSED, e);
	}
	public void keyReleased( KeyEvent e ) {
		eventProcess(e.getSource(), ComMgr.KEYRELEASED, e);
	}
	public void keyTyped( KeyEvent e ) {
		eventProcess(e.getSource(), ComMgr.KEYTYPED, e);
	}
	
	// HyperlinkListener
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			SwEditorPane html = (SwEditorPane)e.getSource();
		    if (e instanceof HTMLFrameHyperlinkEvent) {				((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
				    (HTMLFrameHyperlinkEvent)e);
		    } else {
				try {
				    html.setPage(e.getURL());
				} catch (IOException ioe) {
					if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
						LogTrace.getLog().Trace(LogTrace.EXECPT, 
							"IOE: " + ioe);
				}
		    }
		}	}}
