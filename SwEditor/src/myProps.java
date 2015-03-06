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
 * myProps is a class of properties which configured by dynamic 
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
public class myProps extends Box implements IComm, ActionListener {
	private ArrayList nameList;
	private ArrayList cmptList;
	private int [] contList = new int [10];
	private int contSize;
	private int curSelect = -1;
	private myContn [] mycontns;
	private myBasic mybasic;
	private Box helper;

	myProps() {
		super(BoxLayout.Y_AXIS);
		mybasic = new myBasic();
		mycontns = new myContn [3];
		helper = Box.createVerticalBox();
	    helper.setPreferredSize(new Dimension(240, 192));
	    helper.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		GuiMgr.SwUtil.saveObject("popMenu", new myPopMenu(popMenu, this, null));
	}
	public void init() {
		myComm.getComm().removeListener(null);
		nameList = new ArrayList();
		cmptList = new ArrayList();
		contSize = 0;
		myComm.getComm().addListener(new Integer(PARENT), this);
		this.removeAll();
		MouseListener [] ls = helper.getMouseListeners();
		if (ls != null) 
			for(int i=0; i<ls.length; i++)
				helper.removeMouseListener(ls[i]);
	}
	public Box getHelper() {
		return helper;
	}
	private void keyMove(int id, boolean up) {
		int i=id, k;
		if (up) {								//UP
			for( k=i-1; k>=0; k--)
				if(((myProp)nameList.get(k)).getType()<SUBT)
					break;
			if(k >=0) curSelect = k; 
			else return;
		}
		else {									//DOWN
			for( k=i+1; k<nameList.size(); k++)
				if(((myProp)nameList.get(k)).getType()<SUBT)
					break;
			if (k<nameList.size()) curSelect = k;
			else return;
		}
		if (id != curSelect) {
			myComm.getComm().notify( PARENT, id, TURNOFF);
			myComm.getComm().notify( PARENT, curSelect, TURNON);
		}
	}
	private void procSelect(int from, boolean unsel) {
		String v = "(";
		boolean b = true;
		for (int i=from+1; i<nameList.size(); i++) {
			myProp myp = (myProp)nameList.get(i);
			if(myp.getType()==SUBT) {
				if (unsel) {
				String vl = myp.getValue().trim();
				if (vl.length()==0) b = false;
				if (i > from+1) v += ", ";
				v += vl;
				}
				myp.setVisible(!unsel);
			}
			else break;
		}
		if (unsel)((myProp)nameList.get(from)).setValue((b?(v + ")"):""));
	}
	private void funcCheck(int from) {
		String val = ((myProp)nameList.get(from)).getValue();
		String func = (String)((myProp)nameList.get(from)).getFunc();
		int num = Integer.parseInt(func.substring(0,1));
		boolean b = (val.length()>0?(func.indexOf(val)!=-1):false);
		for (int i=1; i<=num; i++) {
			if (from+i < nameList.size()) {
				myProp myp = (myProp)nameList.get(from+i);
				int type =  myp.getType();
				if (b && type >= HIDE) {
					myp.setType(type - HIDE);
					myp.setVisible(b);
				}
				else if(!b && type<HIDE) {
					myp.setType(type + HIDE);
					myp.setVisible(b);
				}
			}
		}
	}
	private void updtValue(int from) {
		int i = from-1;
		int k = SUBT;
		for (; i>0; i--) {
			k =((myProp)nameList.get(i)).getType();
			if (k != SUBT) break;
		}
		if (i > 0 && k == TREE)
			procSelect(i, true);
	}
	// IComm Listener
	public void Acknowledge(int from, int info){
		switch (info) {
		case KEYUP:
			keyMove(from, true);
			break;
		case KEYDOWN:
			keyMove(from, false);
			break;
		case MSSELECT:
			if (from != curSelect) {
				myComm.getComm().notify( PARENT, curSelect, TURNOFF);
				curSelect = from;
				myComm.getComm().notify( PARENT, curSelect, TURNON);
			}
			break;
		case SELECTED: 
			procSelect(from, false);
			break;
		case UNSELECT: 
			procSelect(from, true);
			break;
		case FUNCHECK: 
			funcCheck(from);
			break;
		case UPDATED: 
			updtValue(from);
			break;
		default:
		}
	}

	private void procSUBT(int from) {
		String v = "(";
		boolean b = true;
		while(--from > 0) 
			if (((myProp)nameList.get(from)).getType()==TREE) break;
		for (int i=from+1; i<nameList.size(); i++) {
			myProp myp = (myProp)nameList.get(i);
			if(myp.getType()==SUBT) {
				String vl = myp.getValue().trim();
				if (vl.length()==0) b = false;
				if (i > from+1) v += ", ";
				v += vl;
			}
			else break;
		}
		if (b && ((myProp)nameList.get(from)).getType()==TREE)
			((myProp)nameList.get(from)).setOrig(v + ")");
	}
	
	private boolean setPropValue(String tag, String name, String value, int k) {
		boolean rtn = false;
		int i = 0;
		myProp mp = null;
		try {
		for(; i<nameList.size(); i++) {
			mp = (myProp)nameList.get(i); 
			String s = mp.getName();
			if (s.equals(tag)) {
				if (i == 0 && k > 1) return false;			// Contained
				if (name.equals("Type") || tag.equals(name))
					break;
				else tag = name;
			}
//			else System.out.println("(" + tag + ") != (" + s + ")");
		}
		if(i<nameList.size()) {
			int j = mp.getType();
			if (j < CONT || j == SUBT) {
				if (value == null && j == BOOL) value = "true"; // ??
				if (value != null) {
					mp.setOrig(value);
					if (j == SUBT) procSUBT(i);
				}
				System.out.println( name + " ==> " + value);
			}
			rtn = true;
		}
		else System.out.println(tag + " : " + name + " is  out of list");
		}catch (Exception e) {System.out.println(e.getMessage()); }
		return rtn;
	}
	
	private void putPropValue(int k, String s) {
		
		if (k != 2) {
			boolean f = false;
			String n = getPropName(s);
			String t = n;
			String v = null;
			int i = s.indexOf('{');		// { value }
 			if (i != -1) { 
				v = s.substring(i+1); 
				v = v.substring(0,v.indexOf('}')).trim();
				f = setPropValue(n, t, v, k);
			}
			i = s.indexOf('[');			// [ attributes ]
			if (i != -1) {
				v = s.substring(i+1);
				do {
					i = v.indexOf('=');
					t = v.substring(0,i).trim();
					v = v.substring(i+1);
					i = v.indexOf(',');
					String u;
					int j = i;
					if (j == -1) j = v.indexOf(']');
					u = v.substring(0,j).trim();
					if (setPropValue(n, t, u, k)) f = true;
					v = v.substring(j+1);
				} while (i != -1);
			}
			if (v == null)
				if (setPropValue(n, n, v, k)) f = true;
			if (!f)	{	// here is the potential control in container  
				Integer seq = new Integer(k);
				cmptList.add(seq);
				mycontns[0].addComponent(n, s, seq);
			}
		}
	}
	
	//	ScrollPane
	//	----
	//	   Dimension [Height = 300, Width = 100]
	//	   Tree   (Name : curTree; SaveId : curTree)
	private void findPropValue(String s) {
		int k = 0;
		int i=s.indexOf('\n');
		String t = ((i==-1)?s:s.substring(0,i));
		putPropValue(++k, t);
		while (i != -1) {
			s = s.substring(i+1);
			i = s.indexOf('\n');
			t = ((i==-1)?s:s.substring(0,i));
			putPropValue(++k, t);
		}
	}
	
	private String getPropName(String nm) {
		int i=0;
		nm = nm.trim();
		for (; i<nm.length(); i++)
			if(nm.charAt(i) == ' ' || nm.charAt(i) == '\n')
				break;
		return nm.substring(0,i);
	}
	
	public void buildProperty(String nm, Box box) {
		this.init();
		mybasic.buildProperty(getPropName(nm), nameList);
		boolean contStart = false;
		for(int i=0; i<nameList.size(); i++) {
			myProp mp = (myProp)nameList.get(i);
			if (!contStart) 
				contStart = (mp.getType() == CONT);
			if (contStart) {
				if (mp.getType() == CONT)
					if (contSize < 3)
						contList[contSize++] = i;
					else
						System.out.println("!! too many container are there !!");
			}
			else  add(mp);
		}
		for (int i=0; i<contSize; i++) {
			mycontns[i] = new myContn(nameList, contList[i]);
			box.add(mycontns[i]);
	//		System.out.println("Container index = " + contList[i]);
		}
		findPropValue(nm);
	// test listener	
		if (contSize > 0) {
			new myPopMenu(mybasic.getAllowd(((myProp)nameList.get(contList[contSize-1])).getName()), this, helper);
			System.out.println("--> " + ((myProp)nameList.get(contList[contSize-1])).getName());
		}
	}
		
	private Object [] popMenu = 
	new Object [] {
		new Object [] { null,
			new Object [] {"MoveUp", "MoveDown", "Delete", "---", "Help"}
		}
	};
	
	// ActionListener
	public void actionPerformed(ActionEvent e) {		String n = ((JMenuItem)e.getSource()).getText();		Object [] cmpStrs = (Object [])((Object [])popMenu[0])[1];		for (int i=0; i<cmpStrs.length; i++) {			if (n.equals((String)cmpStrs[i])) {
				this.mycontns[contSize-1].procComponent(i);		// operator				return;				}		}
		this.mycontns[contSize-1].addComponent(n, "", new Integer(1));	// new ----
	}
	
	private String [] Kind = new String [] {
			"NAME",
			"PROP",
			"ATTR",
			"KIDS",
			"DYNM"
		};
	public ArrayList getCurList() {
		ArrayList al = null;
		if (contSize > 0) al = mycontns[contSize-1].getCmps();
		return al;
	}
	
	public ArrayList getDelList() {
		return this.cmptList;
	}
	
	public ArrayList getNameList() {
		return this.nameList;
	}
	
	public String getProperties() {
		String s = ""; 
		int k = nameList.size(); 
		boolean changed = false;
		boolean cpchanged = false;
		if (curSelect != 0) Acknowledge(0, MSSELECT);	// make sure last is updated
		for(int i=0; i<k; i++) {
			myProp mp = (myProp)nameList.get(i);
			String v = mp.getValue();
			String o = mp.getOrig();
			if (!v.equals(o)) {
				changed = true;
				if (v.length()==0) v = " <Removed>";
				else if (o.length()==0) v = " <Added>";
				else v = " <Changed>";
				s += Kind[mp.getKind()] + " : (" + i + ") " + mp.getName() + " =  " 
					+ mp.getValue() + " [" + mp.getOrig() + "]" + v + "\n";
			}
			else v = "";
//			if (!changed) 
//				s += Kind[mp.getKind()] + " : " + mp.getName() + " =  " 
//					+ mp.getValue() + " [" + mp.getOrig() + "]" + v + "\n";
		}
		if (contSize > 0) {
			ArrayList cloneList = (ArrayList)this.cmptList.clone();
			try {
			for(k=0; k<contSize; k++) {
				ArrayList al = mycontns[k].getCmps();
				for(int i=0; i<al.size(); i++) {
					Object[] cmpts = (Object[])al.get(i);
					int j=this.cmptList.size()-1;
					for ( ; j>=0; j--) 
						if(cmpts[0].equals(cmptList.get(j))) break;	
					if (j>=0) {
						cmptList.remove(j);
						if(i>=cloneList.size() || !cmpts[0].equals(cloneList.get(i))) { 
							cpchanged = true;		
							s += "\nCtrl ID: " + cmpts[0] + "  " + cmpts[1] + " Order Changed";
						}
						else
						s += "\nCtrl ID: " + cmpts[0] + "  " + cmpts[1] + " No Change";
					}
					else {
						cpchanged = true;
						s += "\nCtrl ID: " + cmpts[0] + "  " + cmpts[1] + " New Added";
					}
				}
			}
			} catch (Exception e) {System.out.println ("getProperties :" + e.getMessage ());}
			for(int j=0; j<this.cmptList.size(); j++) {
				cpchanged = true;
				s += "\nCtrl ID: " + cmptList.get(j) + " Be removed";
			}
		}
		if (changed) s += "\n!!! Changed !!!";
		if (cpchanged) s += "\n!!! Component Changed !!!";
		if (!changed && !cpchanged) s += "\n--- No change ---";
		return s;
	}
		
	static public void main(String [] args) {
		String s = "Frame";
		if (args.length > 0) s = args[0];
		Box dummy = Box.createVerticalBox();
		dummy.setPreferredSize(new Dimension(220,100));
		myProps myBox = new myProps();
		myBox.buildProperty(s, dummy);
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(myBox, "North");
		jp.add(dummy, "Center");
		SwUtil.showComp(new JScrollPane(jp));
	}
}