//******************************************************************************
// Package Declaration
//******************************************************************************
package ObjMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import ComMgr.*;
	
/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * DirChooser class is a Directory dialog box handler class for simple access  
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
public class DirChooser extends JPanel implements TreeSelectionListener, ActionListener
{
	static final public	String fsptr = System.getProperty("file.separator");
	private String base = "";
	private boolean dirOnly;
	private DefaultTreeModel model;
	private DefaultListModel lmodel;
	private DefaultComboBoxModel flm; 

	private JTree tree;
	private JLabel text;
	private JList list;
	private JComboBox flist;
	private JDialog dlg;
	private boolean rtn;
	
	public DirChooser(String path)
	{
		super();
		String s = "CodeBase";
		dirOnly = (path==null);
		if(dirOnly) s = "My Computer";
		this.setPreferredSize(new Dimension(200, 300));
		lmodel = new DefaultListModel();
		flm = new DefaultComboBoxModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(s);
		model = new DefaultTreeModel(root);
		setFilter();
		DirLayout();
		if  (dirOnly){
			showUnder(root, System.getProperty("user.dir") + fsptr);
			list.setVisible(false);
			flist.setVisible(false);
		}
		else {
			base = path;
			s = base.substring(base.lastIndexOf(fsptr)+1);
			base = base.substring(0, base.lastIndexOf(fsptr)+1);
			showUnder(root, s + fsptr );
		}
	}
	
	public void setFilter(String [] flt) {
		if (flt != null) {
		flm.removeAllElements();
		for (int i=0; i<flt.length; i++)
			flm.addElement(flt[i]);
		}
	}
	
	private void setFilter() {
		flm.addElement("(XML); *.xml");
		flm.addElement("(Java); *.java");
		flm.addElement("(Picture); *.gif,*.jpg,*.png");
		flm.addElement("(All); *.*");
	}
	
	private void DirLayout() {
		JScrollPane scroll = new JScrollPane();
		this.setLayout(new BorderLayout());

		text = new JLabel();
		this.add(text, "North");
		list = new JList();
		tree = new JTree();
		flist = new JComboBox(flm);
		flist.setEditable(true);
		JPanel tmp = new JPanel();
		this.add(tmp, "South");
		scroll.setViewportView(tree);
		tree.setModel(model);
		tree.addTreeSelectionListener(this);
		
		if (dirOnly) 
			this.add(scroll, "Center");
		else {
			JSplitPane split = new JSplitPane();
			this.add(split, "Center");
			split.setLeftComponent(scroll);
			split.setDividerSize(5);
			split.setDividerLocation(120);
			JScrollPane scrl = new JScrollPane();
			split.setRightComponent(scrl);
			scrl.setViewportView(list);
			list.setModel(lmodel);		
			tmp.add(flist);
			flist.addActionListener(this);
		}
		JButton btn = (JButton)tmp.add(new JButton("OK"));
		btn.addActionListener(this);
		btn = (JButton)tmp.add(new JButton("Cancel"));
		btn.addActionListener(this);
	}
	
	public String getFileName() {
		if (dirOnly) return null;
		String f = getResult();
		String fnm = f.substring(f.indexOf("   ") + 3);
		if (f.indexOf(fsptr) != -1) 
			f = f.substring(f.indexOf(fsptr)+1, f.indexOf("  "));
		else
			f = "";
		fnm = (f.length()==0?"":f+fsptr) + fnm;
		return fnm;
	}
	
	public String getResult() {
		String s = "";
		TreePath path = tree.getSelectionPath();
		if (path != null) {
			int k = path.getPathCount(); 
			for (int i=1; i<k-1; i++)
				s += path.getPathComponent(i).toString() + fsptr;
			if (k >= 2)
				s += path.getLastPathComponent().toString();
		}
		if (!dirOnly) s += "   " + (String)list.getSelectedValue();
		return s;
	}
	
	private void setCdir(String s) {
		if (s.length() > 30) {
			int i = s.indexOf(fsptr);
			i = s.indexOf(fsptr, i+1);
			int j = s.indexOf(fsptr, i+1);
			if (j != -1) {
				j = s.indexOf(fsptr, i+1);
				if (j != -1) {
				   j = s.lastIndexOf(fsptr);
				   if (s.length() - j < 2)
					  j = s.lastIndexOf(fsptr, j-2);
				   s = s.substring(0, i+1) + ".." + s.substring(j);
				}
			}
		}
		text.setText(s);
	}
	
	private String getFilter() {
		Object o = flm.getSelectedItem();
		if (o == null) return null;
		return o.toString();
	}
	private boolean chkExtension(String f) {
		String s = getFilter();
		if (s == null) return false;
		s = s.substring(s.indexOf(';') + 1);
		int i = s.indexOf("*.");
		f = f.toLowerCase();
		while (i != -1) {
			String t = s.substring(i+1);
			int j = t.indexOf(',');
			if (j != -1) t = t.substring(0, j);
			if (f.endsWith(t)) return true;
			if (s.length() > 4)
				s = s.substring(i+4);
			else if (s.indexOf("*.*")!= -1) return true;
			else s = s.substring(i+2);
			i = s.indexOf("*.");
		}
		return false;
	}
	private void showFile(String d) {
		lmodel.removeAllElements();
		File dir = new File(d);
		String ss[] = dir.list();
		if (ss != null)
		for (int i=0; i<ss.length; i++) {
			dir = new File( d + fsptr + ss[i]);
			dir = new File(dir.getAbsolutePath());
			if (!dir.isDirectory()){
				if (chkExtension(ss[i]))
				lmodel.addElement(ss[i]);
			}
		}
	}
	
	private void showDir(String d, String pad, DefaultMutableTreeNode parent) {
		File dir = new File(d);
//		System.out.println(d + pad + dir.toString());
		String ss[] = dir.list();
		if (ss != null)
		for (int i=0; i<ss.length; i++) {
			dir = new File( d + fsptr + ss[i]);
			dir = new File(dir.getAbsolutePath());
			if (dir.isDirectory()){
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(ss[i]);
				model.insertNodeInto(node, parent, parent.getChildCount());
//				System.out.println(pad + ss[i]);
//				showDir(dir.toString(), pad + "   ");
			}
		}
	}
	void showUnder(DefaultMutableTreeNode parent, String cdir)
	{
		setCdir(base + cdir);
//	System.out.println("Base = " + base + " cdir = " + cdir);
		int i = cdir.indexOf(fsptr);
		String pad = "   ";
		while (i != -1) {
			String s = cdir.substring(0, i);
			s = s.substring(s.lastIndexOf(fsptr) + 1);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
			parent.add(node);
			parent = node;
			if (cdir.indexOf(fsptr, i+1) == -1)
			showDir(base + cdir.substring(0, i+1), pad, node);
			i = cdir.indexOf(fsptr, i+1);
			pad += "   ";
		}
		tree.setSelectionPath(new TreePath(parent.getPath()));
		showFile(base + cdir);
	}
	// ActionListener
	public void actionPerformed(ActionEvent e) {		if (e.getSource() instanceof JButton) {			JButton btn = (JButton)e.getSource();
			if (btn.getText().equals("OK")) {				String s =  getResult();				if (s.length() == 0 || s.endsWith("null"))					JOptionPane.showMessageDialog(this, 
						"Please select " + (dirOnly?"directory":"file" ) + " first");				else {					rtn = true;					dlg.setVisible(false);
				}
			}
			else if (btn.getText().equals("Cancel"))
				dlg.setVisible(false);
					}		else valueChanged(null);
	}
	
	// TreeSelectionListener
	public void valueChanged(TreeSelectionEvent e) {
//		JTree tree = (JTree)e.getSource();
		TreePath path = tree.getSelectionPath();
		if (path == null) return;
//	System.out.println (path.toString()); 
		String s = "";
		for (int i=1; i<path.getPathCount()-1; i++)
			s += path.getPathComponent(i).toString() + fsptr;
		if (s.length() == 0)
		s += path.getLastPathComponent().toString();
//	System.out.println (s);	
		boolean isDriver = (path.getPathCount() == 2);
		if (path.getPathCount() > 1) {
			DefaultMutableTreeNode tnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if (tnode == null) return;
			DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)tnode.getParent();
			if (pnode == null) return;
			try {
//		System.out.println ("remove " + tnode.toString() + " from " + pnode.toString());
			if (tnode.isLeaf()) {
				for (int i=pnode.getChildCount()-1; i>=0; i--) {
					if (pnode.getChildAt(i) != tnode)
						model.removeNodeFromParent((DefaultMutableTreeNode)pnode.getChildAt(i));
				}
				if (!isDriver)
				s += path.getLastPathComponent().toString();
				pnode = tnode;
			} else {
				pnode = tnode;
				for (int i=pnode.getChildCount()-1; i>=0; i--) {
					model.removeNodeFromParent((DefaultMutableTreeNode)pnode.getChildAt(i));
				}
				if (!isDriver)
				s += path.getLastPathComponent().toString();			}
			} catch (Exception ee) {
				System.out.println(ee.getMessage());
			}
			if (isDriver) s += fsptr;
			
//		System.out.println ("Base " + base + " s: " + s);	
		
			showDir(base + s, "   ", pnode);
			showFile(base + s);
			tree.expandPath(path);
			tree.setSelectionPath(path);
			setCdir(base + s);
		}
	}
	
	public boolean callDialog(Component p1) {
		dlg = new JDialog();
		dlg.setModal(true);
		dlg.getContentPane().setLayout(new BorderLayout());
		dlg.getContentPane().add(this,"Center");
		dlg.setTitle( (dirOnly?"Select Directory":"Select File"));
		dlg.setSize(320,300);
		if (p1 != null) dlg.setLocationRelativeTo(p1);
		dlg.setVisible(true);
		return rtn;
	}
	
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		DirChooser cb = new DirChooser(null);
		if (cb.callDialog(null)) 
			JOptionPane.showMessageDialog(null, cb.getResult());
		else
			JOptionPane.showMessageDialog(null, "Canceled");
		
		String base = System.getProperty("user.dir");	
		cb = new DirChooser(base);	
		if (cb.callDialog(null)) 
			JOptionPane.showMessageDialog(null, cb.getResult());
		System.exit(0);
	}
}
