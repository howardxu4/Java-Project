import javax.swing.*;
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

public class TreeViewer  implements IcallBack
{
	private	JTree hiddenTree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode treeRoot = null;
	private int[] siblingCnt = new int[10];
	
	public TreeViewer() {
		treeModel = null;
		myHandle();
	}
	public TreeViewer(DefaultTreeModel model) {
		setViewer(model);	
	}
	public void setViewer(DefaultTreeModel model) {
		treeModel = model;
		hiddenTree.setModel(treeModel);
		treeRoot = (DefaultMutableTreeNode)treeModel.getRoot();
	}
	public void setTree(JTree tree) {
		hiddenTree = tree;	
	}
	public JTree getTree(){
		return hiddenTree;
	}
	private void showThis(DefaultMutableTreeNode node, int son, boolean expanded) {
		String s = node.toString();
		s += " has " + son;
		s += expanded? " is" : " is'nt";
		s += " expanded";
		System.out.println(s);
	}
	private void traversTree(DefaultMutableTreeNode node, int level) {
		if (node != null) {
			int k = node.getChildCount();
			siblingCnt[level] = k;
			boolean b = hiddenTree.isExpanded(new TreePath( node.getPath()));
			showThis(node, k, b);
			if (b) {
				for (int i = 0; i < k; i++)
					traversTree((DefaultMutableTreeNode)node.getChildAt(i), level+1);
			}
		}
	}
	public int getTreeDeepLevel(){
		if (treeRoot != null) return treeRoot.getDepth();
		return 0;
	}
	
	public void myHandle() {
		ComMgr.getComMgr().addListener(ComMgr.USER05, this);
	}
/****************************************
*            init run-time variables
*****************************************/
	public void init() {
		JTree t = (JTree)GuiMgr.SwUtil.getObject("myTest");
	}
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
		 GuiMgr.SwUtil.runSafety(this, "process", 
      else {   // user defind message handler
      return null;
   }
/****************************************
*         system generated methods
*****************************************/
      Argument argv = (Argument)info;
      String name = (String)argv.getArgument(NAME);

   // the following useful info maybe referred on some case
      Object obj = argv.getArgument(OBJECT);
   // int eType = ((Integer)argv.getArgument(TYPE)).intValue();
   // Object event = argv.getArgument(EVENT);
      String value = (String)argv.getArgument(VALUE);

	  traversTree(treeRoot, 0);
}