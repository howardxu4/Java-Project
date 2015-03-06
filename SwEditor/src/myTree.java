//******************************************************************************
// Package Declaration
//******************************************************************************

//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;
import javax.swing.tree.*;
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
 * myTree is a class to build Tree from XML 
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
public class myTree  implements ActionListener
{	
	private readXML rd = null;
	private DefaultTreeModel myModel;
	private JTree myJTree;
	private DefaultTreeModel delModel;
	private DefaultMutableTreeNode delRoot;
	private	JTree delTree;
	private	org.w3c.dom.Document doc; 
	
	private String base = null;
	static boolean onlyOne;
	static private myTree mt = null;
	
	static public myTree getMyTree(String s) {
		if (s != null) new myTree(s);
		return mt;
	}
	
	static void setOnlyOne(boolean b) {
		onlyOne = b;
	}
	
	public myTree() {
		this("XmlData/sp.xml");
		this.saveTree("myTree");
	}
	
	public myTree(String s) {
		super();
		SwUtil.setContext(this);
		rd = rdXmlFile(s);
		doc = rd.getDocument();
		mt = this;
		delTree = (JTree)GuiMgr.SwUtil.getObject("delTree");
	}
	
	public readXML getRd() {
		return rd;
	}
	
	public void saveTree(String s) {
		if (rd != null) {
			Node pnode = rd.getDocRoot();
			parseTree(rd, pnode, s);
		}
	}
	
	public void setRender(JTree tree) {
		if (tree != null) {
			myJTree = tree;
			tree.setCellRenderer(new myRenderer(tree));
	//	    new theDrag(tree);
	//		new theDrop(tree);
			new myPopMenu(popMenu, this, tree);
			new myPopMenu(delPopMenu, this, delTree);
		}
	}
	
	private readXML rdXmlFile(String s) {
		try {		
			rd = new readXML();
			if (s.indexOf(':') == -1) {
				if (base != null) {
					s = base + File.separator + s; 
					rd.setDocument(s);
				}
				else
					rd.setDocument(SwUtil.getUrl(s));
			}
			else
				rd.setDocument(s);	
			rd.trimDocument();
			return rd;
		}
		catch (Exception e) {}
		return null;
	}
	
	private void parseTree(readXML rd, Node pnode, String s) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myNode(pnode));
		buildTree(root, pnode, rd);
		myModel = new DefaultTreeModel(root);
		GuiMgr.SwUtil.delObject(s);
		GuiMgr.SwUtil.saveObject(s, myModel);
		if (delRoot==null) {
			delRoot = new DefaultMutableTreeNode("Recycle Bin");
			delModel = new DefaultTreeModel(delRoot);
			delTree.setModel(delModel);
		}
	}

	private void buildTree(DefaultMutableTreeNode tn, Node pnode, readXML rd) {
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				if (nm.charAt(0) != '#') {
					if( rd.getAttrValue(node, "XmlFile").length() > 0 && !onlyOne) {
						readXML srd = rdXmlFile(rd.getAttrValue(node, "XmlFile"));
						if (srd != null) {
							buildTree(tn, srd.getDocRoot(), srd);
						}
					}
					else {
					DefaultMutableTreeNode snd = new DefaultMutableTreeNode(new myNode(node));
					tn.add(snd);
					buildTree(snd, node, rd);
					}
				}
			}	
		}
		catch (Exception e) {}		
	}	
	
	public static void main(String [] args) {
		String s = "sp.xml";
		if (args.length > 0) s = args[0];
		new myTree(s).saveTree("myTree");
	}

	private DefaultMutableTreeNode getTagRoot(DefaultMutableTreeNode tnode) {
		String s = tnode.toString();
		while (GuiMgr.SwClbk.getCallBack().getClsNum(s) == Itypes.UNKNOWN){
			if (s.equals("Tab") || s.equals("LeftComponent") ||	s.equals("RightComponent")
				|| s.equals("ToolBarButton") || s.equals("UIConfig") || s.equals("ButtonGroup")) break;	// Root Node ?
			tnode = (DefaultMutableTreeNode)tnode.getParent();
			if(tnode == null) break;
			s = tnode.toString();
		}
		if(s.equals("Dimension") || s.equals("Rectangle") ||  s.equals("Border")){ 
			tnode = (DefaultMutableTreeNode)tnode.getParent();
		}
		return tnode;		
	}
	
	// prepare property information from tree
	public String getInfo(Object obj) {
		DefaultMutableTreeNode tnode = (DefaultMutableTreeNode)
            ((JTree)obj).getLastSelectedPathComponent();
		tnode = getTagRoot(tnode);
		curNode = tnode;					// save for update
		curKeepList = new ArrayList();
		return getDetail(tnode);
	}
	
	private String getNodeValue(DefaultMutableTreeNode tnode) {
		Node pnode = ((myNode)tnode.getUserObject()).node;
		return rd.get1stNodeValue(pnode);
	}
	
	private String getId(DefaultMutableTreeNode tnode) {
		String s="";
		for (int i = 0; i < tnode.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			String t = node.toString();
			if (t.equals("Name") || t.equals("Text") || t.equals("SaveId")) {
				s += (s.length()>0?"; ":"") + t + " : " + getNodeValue(node);
			}
		}
		return "   (" + s +  ")";
	}

	private String getAttrs(Node pnode) {
		String s = "";
		NamedNodeMap  attrs = pnode.getAttributes();
		for(int i = 0; i < attrs.getLength(); i++) {
			Node node = attrs.item(i);
			if (s.length()==0) s = " [";
			else s += ", ";
			s += node.getNodeName() + " = " + node.getNodeValue();
		}
		if ( s.length() > 0) s += "]";
		return s;
	}

	private String getBasic(DefaultMutableTreeNode tnode) {
		String s = tnode.toString();
		Node pnode = ((myNode)tnode.getUserObject()).node;
		if (pnode.hasAttributes()) s+= getAttrs(pnode);
		String t = rd.get1stNodeValue(pnode);
		if (t.length() > 0) s += "   { " + t + " }";
		return s;
	}

	private String getAsAttrs(DefaultMutableTreeNode tnode) {
		String s="";
		for (int i = 0; i < tnode.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			String t = node.toString();
			if (s.length()==0) s = " [";
			else s += ", ";
			s += node.toString() + " = " + getNodeValue(node);
		}
		if ( s.length() > 0) s += "]";
		return s;
	}
	
	private String getAsSibling(DefaultMutableTreeNode tnode) {
		String s="";
		for (int i = 0; i < tnode.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			curKeepList.add(node);
			String t = node.toString();
			s += "\n   " + t;
			Node pnode = ((myNode)node.getUserObject()).node;
			if (pnode.hasAttributes()) s += getAttrs(pnode);
			String tt = rd.get1stNodeValue(pnode);
			if( tt.length() > 0) s += "   { " + tt + " }";
			if (!node.isLeaf()) s += getId(node);
		}
		return s;
	}

	private String getDetail(DefaultMutableTreeNode tnode) {
		String s = getBasic(tnode);
		curKeepList.add(tnode);
		s += "\n----";
		for (int i = 0; i < tnode.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tnode.getChildAt(i);
			curKeepList.add(node);
			String t = node.toString();
			s += "\n   " + t;
			Node pnode = ((myNode)node.getUserObject()).node;
			if (pnode.hasAttributes()) s += getAttrs(pnode);
			String tt = rd.get1stNodeValue(pnode);
			if( tt.length() > 0) s += "   { " + tt + " }"; 
			if (!node.isLeaf()) {
				if (t.equals("Dimension") || t.equals("Rectangle") ) 
					s += getAsAttrs(node);
				else if(t.equals("Layout") || t.equals("UserDef"))
					s += getAsSibling(node);
				else s += getId(node);	
			}
		}
		return s;
	}	
	
	// update tree information depending on property changes
	private static final String [] subNames = new String [] {
			"Height",
			"Width",
			"Y",
			"X"
		};
	private void changeNode(DefaultMutableTreeNode tnode, String nm, String value) {
		if (nm.equals("Layout") || nm.equals("Border")) 
			addAttr(tnode, nm, value);
		else if (nm.equals("Dimension") || nm.equals("Rectangle")) { 
			try {
			value = value.substring(value.indexOf('(')+1);
			for (int i=0; i<tnode.getChildCount(); i++) {
				DefaultMutableTreeNode node = findNode(tnode, subNames[i]);
				Node pnode = ((myNode)node.getUserObject()).node;
				int j = value.indexOf(',');
				if (j == -1) j = value.indexOf(')');
				if (pnode.getFirstChild() != null)
					pnode.getFirstChild().setNodeValue(value.substring(0,j));
				else
					pnode.appendChild(doc.createTextNode(value.substring(0,j)));
				if (value.length() > j)
					value = value.substring(j+1);
			}
			} catch (Exception e) {System.out.println(nm + " unchageable");}
		}
		else {
			try {
			Node pnode = ((myNode)tnode.getUserObject()).node;	
			if (pnode.getFirstChild() != null)
				pnode.getFirstChild().setNodeValue(value);
			else 
				pnode.appendChild(doc.createTextNode(value));	
			}
			catch (Exception e) {
				System.out.println("changeNode: " + e.getMessage());
			}
		}
	}
	
	private void addNode(DefaultMutableTreeNode tnode, String nm, String value, boolean b) {					
		try {
			org.w3c.dom.Element child = doc.createElement(nm);
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new myNode(child));
			 
			DefaultMutableTreeNode [] kidNodes = null;
			if (value.length() > 0) {
				if (nm.equals("Dimension") || nm.equals("Rectangle")) {	
					int k = 2;
					if (nm.equals("Rectangle")) k = 4;
					kidNodes = new DefaultMutableTreeNode [k];
					int j = value.indexOf('(');
					value = value.substring(j + 1);
					for (int i = 0; i < k; i++) {
						org.w3c.dom.Element grandChild = doc.createElement(subNames[i]);
						j = value.indexOf(',');
						if (j == -1) j = value.indexOf(')');
						grandChild.appendChild(doc.createTextNode(value.substring(0, j)));
						kidNodes[i] = new DefaultMutableTreeNode(new myNode(grandChild));
						if (value.length() > j)
							value = value.substring(j+1);
					}
				}
				else if (nm.equals("Layout") || nm.equals("Border")) 
					addAttr(childNode, nm, value);
				else
					child.appendChild(doc.createTextNode(value));
			}										// Attr -> Front : Cmpt -> back
			myModel.insertNodeInto(childNode, tnode, b?tnode.getChildCount():0);
			if (kidNodes != null ) {
				for (int i=0; i < kidNodes.length; i++)
					myModel.insertNodeInto(kidNodes[i], childNode, i);
			}

//	System.out.println(tnode.toString() + " .......added....... " + nm + " with value " + value);
		} catch (Exception e) {System.out.println("Error:" + e.getMessage()); }
	}

	private void addNode(String nm, String value) {					
		DefaultMutableTreeNode tnode = findNode(curNode, nm);
		if (tnode != null) 
			changeNode(tnode, nm, value);
		else 						
			addNode(curNode, nm, value, false); 
	}
	
	private void removeNode(DefaultMutableTreeNode tnode, String nm) {
		if (tnode != null) {
			if (nm.equals("Layout") || nm.equals("Border")) 
				removeAttr(tnode, nm, true);
			else {
				Node pnode = ((myNode)tnode.getUserObject()).node;
				if (nm.equals("UserDef")&& pnode.getAttributes().getLength() > 0) {
					pnode.removeChild(pnode.getFirstChild());
				}
				else if (tnode.getParent() != null) 
					myModel.removeNodeFromParent(tnode);
			}
		}	
	}
		
	private void addAttr(String pnm, String nm, String value) {
		DefaultMutableTreeNode tnode = findNode(curNode, pnm);
		if (tnode == null) {
			tnode = curNode;
			org.w3c.dom.Element child = doc.createElement(pnm);
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new myNode(child));						
			myModel.insertNodeInto(childNode, tnode, 0); // tnode.getChildCount());
			tnode = childNode;	
		}
		addAttr(tnode, nm, value);
	}
	
	private void addAttr(DefaultMutableTreeNode tnode, String nm, String value) {
		Node pnode = ((myNode)tnode.getUserObject()).node;
		if (pnode.getNodeName().equals(nm)) nm = "Type";
		org.w3c.dom.Element elmt = (org.w3c.dom.Element)pnode; 
		elmt.setAttribute(nm, value);
//	System.out.println("Set Attribute: " + nm + " = " + value);		
	}
	
	private void removeAttr(DefaultMutableTreeNode tnode, String nm, boolean f) {
		Node pnode = ((myNode)tnode.getUserObject()).node;
		if (pnode.getNodeName().equals(nm)) nm = "Type";
		org.w3c.dom.Element elmt = (org.w3c.dom.Element)pnode; 
		Attr attr = elmt.getAttributeNode(nm);
		if (attr != null) elmt.removeAttributeNode(attr);
		if (nm.equals("Action")) {								// Userdef 
			for (int i=0; i<tnode.getChildCount(); i++)
				myModel.removeNodeFromParent((DefaultMutableTreeNode)tnode.getChildAt(i));
		}
		if (f && isEmptyNode(tnode))
			myModel.removeNodeFromParent(tnode);
	}
	
	private DefaultMutableTreeNode findNode(DefaultMutableTreeNode pnode, String nm) {
		if (pnode != null)
		for (int i = 0; i < pnode.getChildCount(); i++) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)pnode.getChildAt(i);
			if (nm.equals(tn.toString())) {
//	System.out.println("[" + i + "] " + tn.toString() + " is found");
				return tn;
			}
		}
		return null;
	}
	
	private boolean isEmptyNode(DefaultMutableTreeNode tnode) {
		if (tnode.isLeaf()) {
			Node pnode = ((myNode)tnode.getUserObject()).node;
			if (pnode.getAttributes().getLength() == 0) {
				pnode = pnode.getFirstChild();
				if (pnode == null) return true;
				String s = pnode.getNodeValue();
				if (s == null || s.length() == 0) return true;
			}
		}
		return false;
	}

	public void updtChanges(ArrayList delList, ArrayList curList) {
		if (curKeepList != null) {
		try {	
		for(int i=0; i<this.curKeepList.size(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)curKeepList.get(i);
			System.out.println("I = " + i + " Name = " + node.toString());
		}
		// first index is TAG, second is ----
		if (curList != null)
		for(int i=0; i<curList.size(); i++) {
			Object [] cmpts = (Object [])curList.get(i);
			int k = ((Integer)cmpts[0]).intValue();
			if (k < 2) {
				System.out.println("Add -->  " + i + "  " + cmpts[0] + "  " + cmpts[1]);
				addNode(curNode, (String)cmpts[1], "", true);
			}
			else {
				System.out.println("Move -->  " + i + " ==> " + (k-2) + "  " + cmpts[1] + " : " 
				+ ((DefaultMutableTreeNode)curKeepList.get(k-2)).toString() );
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)curKeepList.get(k-2);
				removeNode(node, node.toString());
				myModel.insertNodeInto(node, curNode, curNode.getChildCount());
			}
		}
		if (delList != null)
		for(int i=0; i<delList.size(); i++) {
			int k = ((Integer)delList.get(i)).intValue();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)curKeepList.get(k-2);
			System.out.println("Delete --> " + i + " ==> " + (k-2) + "  " + node.toString());
			removeNode(node, node.toString());		// maybe save it to deleted tree
			delModel.insertNodeInto(node, delRoot, delRoot.getChildCount());
		}
		} catch (Exception e) {System.out.println("updtChanges :" + e.getMessage()); }
		}
	}
	
	public void updtTree(ArrayList nameList) {
		int k = nameList.size();
		for(int i=0; i<k; i++) {
			myProp mp = (myProp)nameList.get(i);
			String nm = mp.getName();
			String v = mp.getValue();
			String o = mp.getOrig();
			int kind = mp.getKind();
			if (!v.equals(o)) {
				if (v.length()==0) {
				   if (kind==IComm.ATTR && !nm.equals("Layout")) {
				   		int j = i-1;
				   		myProp pmp = (myProp)nameList.get(j);
				   		do {
				   			if (pmp.getKind()!=IComm.ATTR) break;
				   			pmp = (myProp)nameList.get(--j);
				   		}while (j > 0);
				   		DefaultMutableTreeNode tnode = curNode;
				   		if (j > 0) tnode = findNode(curNode, pmp.getName());
				   		if (tnode != null) 
				   			removeAttr(tnode, nm, j>0);
				   }
				   else if(kind!=IComm.KIDS)
						removeNode(	findNode(curNode, nm), nm);
				   else if(nm.equals("Object") || nm.equals("Method")  
						|| nm.equals("ParType") || nm.equals("ParValue")) {
						if (mp.getType() < IComm.HIDE){
							DefaultMutableTreeNode tnode = findNode(curNode, "UserDef");	
							if (tnode != null) 
								removeNode(	findNode(tnode, nm), nm);
						}
				    }
					v = " <Removed>"; 
				}
				else { 
					if (kind==IComm.ATTR && !nm.equals("Layout")) {
						int j = i-1;
						myProp pmp = (myProp)nameList.get(j);
						do {
							if (pmp.getKind()!=IComm.ATTR) break;
							pmp = (myProp)nameList.get(--j);
						}while (j > 0);
						if (j > 0) addAttr(pmp.getName(), nm, v);
						else addAttr(curNode, nm, v);
					} 
					else if(kind!=IComm.KIDS) 
						addNode(nm, v);
					else if(nm.equals("Object") || nm.equals("Method")  
						|| nm.equals("ParType") || nm.equals("ParValue")) {
						if (mp.getType() < IComm.HIDE){
							DefaultMutableTreeNode tnode = findNode(curNode, "UserDef");	
							if (tnode != null) {
								DefaultMutableTreeNode snode = findNode(tnode, nm);
								if (snode != null) changeNode(snode, nm, v);
								else
								addNode(tnode, nm, v, false);
							}
						}
					}
					else {  // if (kind==IComm.KIDS) 
						int j = i-1;
						myProp pmp = (myProp)nameList.get(j);
						do {
							if (pmp.getKind()!=IComm.KIDS) break;
							pmp = (myProp)nameList.get(--j);
						}while (j > 0);
						if (j > 0) addAttr(pmp.getName(), nm, v);
						else addAttr(curNode, nm, v);
					}
					v = (o.length()==0) ? " <Added>":" <Changed>"; 
				}
		System.out.println( "[ " + mp.getKind() + " ] : (" + i + ") " + mp.getName() + " =  " 
					+ mp.getValue() + " [" + mp.getOrig() + "]" + v);
			}
		}
	}
	
	private void setStatus(String s) {
		JTextField status = (JTextField)GuiMgr.SwUtil.getObject("Status");	
		status.setText(s);	
	}

	private void reloadTree(int n) {
		Argument argv = new Argument();
		argv.setArgument(Itypes.OBJECT, myJTree);
		ComMgr.getComMgr().sendMsg(Itypes.USER05, 123+n, argv);	// update tree
	}
	
	private void deleteCurNode() {
		if (curNode != null && curNode.getParent() != null) {
			DefaultMutableTreeNode tnode = curNode;
			curNode = (DefaultMutableTreeNode)curNode.getParent();
			myModel.removeNodeFromParent(tnode);
			delModel.insertNodeInto(tnode, delRoot, delRoot.getChildCount());	
			myJTree.setSelectionPath( new TreePath(curNode.getPath()));
			reloadTree(0);
		}
	}
	
	private void appendFromRecycle() {
		DefaultMutableTreeNode dnode = (DefaultMutableTreeNode)
            delTree.getLastSelectedPathComponent();
		String s = "";
		if (curNode != null && dnode != null) {
			if (dnode.getParent() == null)
				s = "You can not insert recycle bin, please select others!";
			else {
				s = dnode.toString() + " had appended at " + curNode.toString();
				delModel.removeNodeFromParent(dnode);
				myModel.insertNodeInto(dnode, curNode, curNode.getChildCount());
				myJTree.setSelectionPath( new TreePath(curNode.getPath()));
				reloadTree(0);
			}
		}
		else
			s = "Please select nodes in tree and recycle bin tree first";
		setStatus(s);
	}
	
	private void clearRecycle(boolean f) {
		if (f) {
			delRoot.removeAllChildren();
			delModel.reload();
		}
		else {
			DefaultMutableTreeNode dnode = (DefaultMutableTreeNode)
			    delTree.getLastSelectedPathComponent();
			if (dnode != null && dnode.getParent() != null) {
				delModel.removeNodeFromParent(dnode);					
			}
			else setStatus("Select a node except the root to delete");
		}
	}
	
	private void importFile() {
		DirChooser cb = new DirChooser(null);
		if (cb.callDialog(this.delTree)) {
			base = cb.getResult();	
			cb = new DirChooser(base);	
			if (cb.callDialog(this.delTree)) {
				String fnm = base + cb.fsptr + cb.getFileName();				
	//			JOptionPane.showMessageDialog(null, fnm);
				FileOps fop = new FileOps();
				int n = fop.askConf("Handle only one file, press Yes\nAuto expand to all, press No\n");
				if (n==2) return;
				setOnlyOne(n==0);
				rdXmlFile(fnm);
				Node pnode = rd.getDocRoot();
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myNode(pnode));
				buildTree(root, pnode, rd);
				delModel.insertNodeInto(root, delRoot, delRoot.getChildCount());	
			}

		}
	}
	
	private void traversNode(DefaultMutableTreeNode tnode) {
		if (tnode != null) {
			myXML myXml = new myXML();
			myXml.convertTree(tnode);
			Component cp = SwUtil.chkXmlFile("tmp.xml");
			Object myScrlPane = SwUtil.getObject("myScrlPane");
			((JScrollPane)myScrlPane).setViewportView(cp);
		}
		else setStatus("Please select node first");
	}
	
	private void procOperator(int n, String s) {
		if(curNode != null || n > 2) 
			s += " is processing...";
		else s += " need select node first";
		setStatus(s);
		
		switch (n) {
		case 0: // Refresh:
			reloadTree(1);
			break;
		case 1: // Delete
			deleteCurNode();
			break;
		case 2: // Insert
			appendFromRecycle();
			break;
		case 4: // Help
			traversNode(curNode);
			break;
		case 5: // Empty
			clearRecycle(true);
			break;
		case 6: // Clear
			clearRecycle(false);
			break;
		case 7:	// Import
			importFile();
			break;
		default: // Help.
			traversNode(getTagRoot((DefaultMutableTreeNode) delTree.getLastSelectedPathComponent()));
			break;
		}
	}
	
	private DefaultMutableTreeNode curNode = null;
	private ArrayList curKeepList = null;
	
	private Object [] popMenu = 
	new Object [] {
		new Object [] { null,
			new Object [] {"Refresh", "Delete", "Insert", "---", "Help"}
		}
	};
	private Object [] delPopMenu = 
	new Object [] {
		new Object [] { null,
			new Object [] {"EmptyAll", "Clear", "Import", "---", "Help."}
		}
	};
	
	// ActionListener
	public void actionPerformed(ActionEvent e) {
		String n = ((JMenuItem)e.getSource()).getText();
		Object [] cmpStrs = (Object [])((Object [])popMenu[0])[1];
		for (int i=0; i<cmpStrs.length; i++) {
			if (n.equals((String)cmpStrs[i])) {
				procOperator(i, n);
				break;
			}
		}
		cmpStrs = (Object [])((Object [])delPopMenu[0])[1];
		for (int i=0; i<cmpStrs.length; i++) {
			if (n.equals((String)cmpStrs[i])) {
				procOperator(i+cmpStrs.length, n);
				break;
			}
		}
	}
	
    private class myRenderer extends DefaultTreeCellRenderer {
        ImageIcon leafIcon;
        ImageIcon nodeIcon;
		String IconPath = "Resources/images/icons/";
		
        public myRenderer(JTree tree) {
            leafIcon = new ImageIcon(IconPath + "button.png");
            nodeIcon = new ImageIcon(IconPath + "image.gif");
			
	        //Enable tool tips.
	        ToolTipManager.sharedInstance().registerComponent(tree);
		
        }

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
			
			myNode mn = (myNode)((DefaultMutableTreeNode)value).getUserObject();
			setToolTipText("(" + getBasic((DefaultMutableTreeNode)value) + ")");
			ImageIcon icon = null;
			try {icon = new ImageIcon(IconPath + mn.toString() + ".png");}
			catch (Exception e) {}
			if (leaf) { 
				 if (icon == null) setIcon(leafIcon);
				 else setIcon(icon);
			}
			else {
				 if (icon == null) setIcon(nodeIcon);
				 else setIcon(icon);
			}

            return this;
        }
    }
	
	class theDrag extends myDrag {
		theDrag(Component cmp) {
			super(cmp);
			tree = (JTree)cmp;
		}
		// overwrite
		public void getDraggedValues() {
			super.setDraggedValues(new Object [] {tree.getLastSelectedPathComponent()});
		}
		public void processDrop(Object obj) {
			System.out.println(" ==> " + obj.toString());
		}
		JTree tree;
	}

	class theDrop extends myDrop {
		theDrop(Component cmp) {
			super(cmp);
			tree = (JTree)cmp;
		}
		public void dragOver(DropTargetDragEvent event)
		{  // you can provide visual feedback here
			Point pt = event.getLocation();
			TreePath tp = tree.getPathForLocation(pt.x, pt.y);
			if (tp != null) tree.setSelectionPath(tp);
		}
		// should overwrite this method
		public void processDrop(Object obj) {
			Transferable transferable = (Transferable)obj;
			DataFlavor[] flavors
			   = transferable.getTransferDataFlavors();
		  System.out.println("Total number of data flavors are " + flavors.length);
	
		}
		JTree tree;
	}
}
