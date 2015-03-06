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
public class myProp extends JPanel implements IComm, ActionListener, MouseListener, KeyListener
{
	private int id;
	private int type;
	private Object func;
	private String name;
	private int kind;	private String orig;
		private JTextField label;
	private JTextField value;
	private JComboBox list;
	private Box cbox;
	private JTextField file;
	private JButton btn;
	private JComponent comp;	private Color myColor;
	private boolean isSelf;
    private myPopup pop;

	//		  Id,  Type,     Name,     Orig,      Array,     Func,  Kind 
	myProp(int k, int t, String n, String v, Object[] a, Object f, int m) {
		super();
		setLayout(new GridLayout(0, 2));
		Border border = BorderFactory.createLineBorder(Color.lightGray);
		Dimension dimension = new Dimension(100,16);
		id = k;
		name = n.substring(1+n.lastIndexOf(' ')).trim();
		type = t%HIDE;
		func = f;
		kind = m;
		orig = v;
		myColor = new Color(-6697729); 
		
		if (t >= SUBT) { 
			if(n.startsWith("   ")) n = "       !-- " + n.trim();
			else n = "    !-- " + n;
		}
		label = (JTextField)add(new JTextField(n));
		label.setBackground(Color.white);
		label.setBorder(border);
		label.setEditable(false);
		label.setPreferredSize(dimension);
		if (type != SUBT) {
			label.addMouseListener(this);
			label.addKeyListener(this); 
		}
		
		Box rbox = (Box)add(Box.createHorizontalBox());
		value = (JTextField)rbox.add(new JTextField(v));
		value.setBackground(Color.white);
		value.setBorder(border);
		value.setEditable(type == STR || type == INT || type == SUBT);
		value.setPreferredSize(dimension);
		if (type != SUBT) {
			value.addMouseListener(this);
			value.addKeyListener(this);
		}
		
		if (type == BOOL || type == COMB) {
		if (a == null) a = new Object [] { "", "true", "false" };
		list = (JComboBox)rbox.add(new JComboBox(a));
		list.setEditable(true);
		list.setSelectedItem(getValue());
		list.setPreferredSize(dimension);
		list.setVisible(false);
		if (func != null) list.addActionListener(this);
		}
				
		if (type == COLR || type == ICON || type == DIAL) {
		cbox = (Box)rbox.add(new Box(BoxLayout.X_AXIS));
		file = (JTextField)cbox.add(new JTextField(getValue()));
		file.setHorizontalAlignment(JLabel.LEFT);
		file.setBackground(Color.white);
		file.setBorder(border);
		file.addKeyListener(this);
		btn = (JButton)cbox.add(new JButton(" ..."));
		btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setMargin(new Insets(4,4,4,4));
		btn.setMinimumSize(new Dimension(20,20));
		btn.setMaximumSize(new Dimension(20,20));
		btn.addActionListener(this);
		cbox.setPreferredSize(dimension);
		cbox.setVisible(false);
		}
		if (type == DIAL) pop = new myPopup(this, name.equals("Position"));
		
		if (type == COLR || type == ICON || type == DIAL) comp = cbox;
		else if(type == BOOL || type == COMB) comp = list;
		else comp = null;
		
		this.setVisible(t < SUBT);
		if (type != SUBT) myComm.getComm().addListener(new Integer(id), this);
		if (type != t) type = t;		// HIDE
	}
	
	public void swapObj(final boolean ack) {
//		SwingUtilities.invokeLater(new Runnable() {//		public void run() {
			try {			if (ack) {				// unselected
				if (type == COLR || type == ICON || type == DIAL)
					value.setText(file.getText());
				else if (type == BOOL || type == COMB)
					value.setText((String)list.getSelectedItem());
			    value.setVisible(true);				comp.setVisible(false);
			}
			else {					// selected
				if (type == COLR) 
					try {btn.setBackground(new Color(Integer.parseInt(getValue())));}
					catch (Exception e) {btn.setBackground(new Color(-3355444));}
				if (type == COLR || type == ICON || type == DIAL) 
					file.setText(value.getText());
				comp.setVisible(true);
				value.setVisible(false);			}
			}catch (Exception e) {System.out.println("==>" + e.toString());}
//		}
//		});
	}
	// IComm Listener
	public void Acknowledge(int from, int info){
		switch (info) {
		case TURNOFF:
			if (comp != null && comp.isVisible()) 
				swapObj(true);
			if (label.getBackground() != Color.white) {
				label.setBackground(Color.white);				if (type == TREE)
					myComm.getComm().notify(id, PARENT, UNSELECT);
			}
			break;
		case TURNON:
			if (comp != null) swapObj(false);
			label.setBackground(myColor);			if (type == TREE)
				myComm.getComm().notify(id, PARENT, SELECTED);			label.grabFocus();
		}
	}
	// MouseListener
	public void mouseClicked( MouseEvent e ) {
	}
	public void mouseEntered( MouseEvent e ) {
	}
	public void mouseExited( MouseEvent e ) {
	}
	public void mousePressed( MouseEvent e ) {
		isSelf = label.getBackground() == myColor;
		if (!isSelf) label.setBackground(myColor);	}
	public void mouseReleased( MouseEvent e ) {
		if(!isSelf) 
			myComm.getComm().notify(id, PARENT, MSSELECT);
	}
	// KeyListener
	public void keyPressed( KeyEvent e ) {
		int k = e.getKeyCode();
		if( k == KeyEvent.VK_UP || k == KeyEvent.VK_KP_UP) 
			myComm.getComm().notify(id, PARENT, KEYUP);
		else if ( k == KeyEvent.VK_DOWN || k == KeyEvent.VK_KP_DOWN) 
			myComm.getComm().notify(id, PARENT,KEYDOWN);
	}
	public void keyReleased( KeyEvent e ) {
	}
	public void keyTyped( KeyEvent e ) {
	}
	// ActionListener
	public void actionPerformed(ActionEvent e) {
		if (type == COLR) {
			Color c = JColorChooser.showDialog(
				this, "ColorChooser ", btn.getBackground());			if(c != null) {
				btn.setBackground(c);
				file.setText(Integer.toString(c.getRGB()));
			}
		}
		else if (type == ICON) {
			DirChooser cb = new DirChooser(null);
			if (cb.callDialog(this)) {
				String base = cb.getResult();
				cb = new DirChooser(base);	
				if (name.indexOf("Icon")!=-1)
					cb.setFilter(
						new String [] {"(Picture); *.gif,*.jpg,*.png",
								 "(All); *.*" });
				if (cb.callDialog(this)) 
					file.setText( cb.getFileName());
			}
		}
		else if (type == DIAL) {
			Point p = new Point(0,cbox.getSize().height);
			pop.showPopup(cbox, p); 
		}
		else if (type == COMB) {
			value.setText((String)list.getSelectedItem());
			if (func != null) myComm.getComm().notify(id, PARENT, FUNCHECK);			
		}
	}
	public String getName() {
		return name;
	}
	public int getType() {
		return type;
	}
	public void setType(int t) {
		type = t;
	}
	public String getValue() {
		return value.getText();
	}
	public void setValue(String s) {
		value.setText(s);
		if (type == BOOL || type == COMB)
			list.setSelectedItem(s);
		else if (type == DIAL)
			file.setText(s);
		else if (type == SUBT) 
			myComm.getComm().notify(id, PARENT, UPDATED);
	}
	public Object getFunc() {
		return func;
	}
	public int getKind() {
		return kind;
	}
	public void setOrig(String s) {
		orig = s;
		setValue(s);
	}
	public String getOrig() {
		return orig;
	}
}
