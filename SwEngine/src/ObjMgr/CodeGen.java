//******************************************************************************
// Package Declaration
//******************************************************************************
package ObjMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.util.*;
import java.io.*;

import GuiMgr.*;
import ComMgr.*;
import XmlMgr.*;

import org.w3c.dom.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * CodeGen class is code parser class for generating code from XML data 
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
public class CodeGen implements Itypes
{
	private static int evtType [] = {
		Itypes.UNKNOWN,						// "Applet",
		Itypes.UNKNOWN,						// "Border",
		Itypes.UNKNOWN,						// "Box",
		Itypes.ACTION,						// "Button",				
		Itypes.ACTION,						// "CheckBox",				
		Itypes.ACTION,						// "CheckBoxMenuItem",		
		Itypes.UNKNOWN,						// "ColorChooser",
		Itypes.ACTION,						// "ComboBox",				
		Itypes.UNKNOWN,						// "Component",
		Itypes.UNKNOWN,						// "DesktopPane",
		Itypes.UNKNOWN,						// "Dialog",
		Itypes.UNKNOWN,						// "Dimension",
		Itypes.UNKNOWN,						// "EditorPane",
		Itypes.UNKNOWN,						// "FileChooser",
		Itypes.KEYPRESSED,	//[FOCUSGAINED]	// "FormattedTextField",
		Itypes.UNKNOWN,						// "Frame",
		Itypes.UNKNOWN,						// "InternalPane",
		Itypes.MOUSECLICKED,				// "Label",			
		Itypes.LISTSELECTION,				// "List",					
		Itypes.UNKNOWN,						// "Menu",
		Itypes.UNKNOWN,						// "MenuBar",
		Itypes.ACTION,						// "MenuItem",				
		Itypes.MOUSECLICKED,				// "Panel",				
		Itypes.KEYPRESSED,	//[FOCUSGAINED]	// "PasswordField",		
		Itypes.UNKNOWN,						// "PopupMenu",
		Itypes.CHANGE,						// "ProgressBar",
		Itypes.ACTION,						// "RadioButton",			
		Itypes.ACTION,						// "RadioButtonMenuItem",	 
		Itypes.UNKNOWN,						// "Rectangle",
		Itypes.ADJUSTMENT,					// "ScrollBar",			
		Itypes.UNKNOWN,						// "ScrollPane",
		Itypes.CHANGE,						// "Slider",				 
		Itypes.CHANGE,						// "Spiner",				
		Itypes.UNKNOWN,						// "SplitPane",
		Itypes.CHANGE,						// "TabbedPane",			
		Itypes.UNKNOWN,						// "Table",
		Itypes.FOCUSGAINED,					// "TextArea",	
		Itypes.KEYPRESSED,	//[FOCUSGAINED]	// "TextField",	
		Itypes.FOCUSGAINED,					// "TextPane",				
		Itypes.ACTION,						// "ToggleButton",
		Itypes.UNKNOWN,						// "ToolBar",				
		Itypes.TREESELECTION,				// "Tree",					 
		Itypes.UNKNOWN,						// "Window"
		};
	
	private Hashtable hType;
	private Hashtable hDef;
	private ArrayList aDev;
	private ArrayList aSav;
	private boolean isOne;
	private String fName;
	private String fPath;
	
	public CodeGen () {
		hDef = new Hashtable();
		aSav = new ArrayList();
	}

	class holder {
		public String type;
		public String name;
		public String help;
		holder(String t, String n, String h) {
			type = t;
			name = n;
			help = h;
		}
	}

	private void setAdef(String s) {
		aDev = (ArrayList)hDef.get(s);
		if (aDev == null) {
			aDev = new ArrayList();
			hDef.put(s, aDev);
			int i = fName.lastIndexOf('/');
			if (i == -1) i = fName.lastIndexOf('\\');
			if (i != -1)
				aDev.add(new holder(s, fName.substring(i+1), fName.substring(0, i))); 
			else 
				aDev.add(new holder(s, fName.substring(i+1), fName)); 
		}		
	}
	public boolean genCode(String s, int n) {
		int i = s.lastIndexOf('/');
		if (i == -1) i = s.lastIndexOf('\\');
		isOne = (n==0);
//		if (s.indexOf(':') != -1) fPath = s.substring(0, i+1); else
		fPath = System.getProperty("user.dir") + "\\";
		
		if (rdXmlFile(s)) {
			if (n==2) {
				display(s.substring(i+1));
			}
			else {
				output(n==0);
			}
			return true;
		}
		return false;
	}
	private void display(String s) {
		if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
			LogTrace.getLog().Trace(LogTrace.DEVELOP, 
				"#######...Generated File: " + s + ".JAVA...#######"); 
		for (Enumeration h = hDef.elements() ; h.hasMoreElements() ;) {
			aDev = (ArrayList)h.nextElement();
		if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
			LogTrace.getLog().Trace(LogTrace.DEVELOP, 
				"-------..............---------\n");
			for (Iterator e = aDev.iterator() ; e.hasNext() ;) {
				holder hd = (holder)e.next();
				if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
					LogTrace.getLog().Trace(LogTrace.DEVELOP, 
						hd.type + "  " + hd.name + "  " + hd.help);
			}
		}
		if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
			LogTrace.getLog().Trace(LogTrace.DEVELOP, 
				"-------xxxxxxxxxxxxxx---------\n");
		for (Iterator e = aSav.iterator(); e.hasNext(); ) {
			holder hd = (holder)e.next();
			if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
				LogTrace.getLog().Trace(LogTrace.DEVELOP, 
					hd.type + "  " + hd.name + "  " + hd.help);
		}
		if (LogTrace.getLog().chkLevel(LogTrace.DEVELOP)) 
			LogTrace.getLog().Trace(LogTrace.DEVELOP, 
				"-------------------------------------------------------------"); 
		
	}
	
	private void reSort(holder hd) {
		ArrayList aType = (ArrayList)hType.get(hd.type);
		if (aType == null) {
			aType = new ArrayList();
			hType.put(hd.type, aType);
		}
		aType.add(hd);
	}
	
	private void output(boolean vb) {
		String pad = null;
		try {
		for (Enumeration h = hDef.elements() ; h.hasMoreElements() ;) {
			aDev = (ArrayList)h.nextElement();
			if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
				LogTrace.getLog().Trace(LogTrace.INFO, 
					"-------..............---------\n");
			String s = "";
			String CodeType = null;
			String ClsName = null;
			String PathName = null;
			hType = new Hashtable();
			for (Iterator e = aDev.iterator() ; e.hasNext() ;) {
				holder hd = (holder)e.next();
				if (ClsName == null) {
					int k = GuiMgr.SwHelper.getUserDef(hd.type);
					if (k == -1) break;
					CodeType = hd.type;
					PathName = hd.help;
					ClsName = hd.name;
					if (ClsName.indexOf('.') != -1)
						ClsName = ClsName.substring(0,ClsName.indexOf('.'));
					s += FuncGen.getClassHdr(ClsName, pad);			// 1
					for (Iterator v = aSav.iterator(); v.hasNext(); ) {
						holder vhd = (holder)v.next();
						s += FuncGen.getVarDecl( vhd.type, vhd.name, pad);	// 2
					}
					s += FuncGen.getClsCnst(ClsName, k, pad);	// 3
					for (Iterator v = aSav.iterator(); v.hasNext(); ) {
						holder vhd = (holder)v.next();
						s += FuncGen.getInitVar( vhd.type, vhd.name, pad);	// 4
					}
					s += FuncGen.getClsClbk(pad, vb);			// 5
				}
				else {
					int eType = Integer.parseInt(hd.help);
					if (vb) {
						s += FuncGen.getFuncDef(hd.name, hd.type, eType, pad);	// 6
						if (eType == Itypes.MOUSECLICKED) {
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.MOUSEENTERED, pad);	// 6
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.MOUSEEXIT, pad);		// 6
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.MOUSEPRESSED, pad);	// 6
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.MOUSERELEASED, pad);	// 6							
						}
						else if (eType == Itypes.FOCUSGAINED) {
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.FOCUSLOST, pad);	// 6
							if (hd.type.equals(Itypes.ObjName[Itypes.TEXTFIELD]) ||
								hd.type.equals(Itypes.ObjName[Itypes.PASSWORDFIELD]))		
								s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.ACTION, pad);	// 6
						}
						else if (eType == Itypes.KEYPRESSED) {
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.KEYRELEASED, pad);	// 6							
							s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.KEYTYPED, pad);	// 6							
							if (hd.type.equals(Itypes.ObjName[Itypes.TEXTFIELD]) ||
								hd.type.equals(Itypes.ObjName[Itypes.PASSWORDFIELD])){	
								s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.FOCUSGAINED, pad);	// 6
								s += FuncGen.getFuncDef(hd.name, hd.type, Itypes.FOCUSLOST, pad);	// 6
							}
						}
					}
					else {
						reSort(hd);
					}
				}
			}
			if (ClsName != null) {	
				if (!vb) {
					for (Enumeration ht = hType.elements() ; ht.hasMoreElements() ;) {
						ArrayList aType = (ArrayList)ht.nextElement();
						for (int k = 0; k<aType.size(); k++ ) {
							holder hd = (holder)aType.get(k);
							s += FuncGen.getCaseDef(hd.name, hd.type, Integer.parseInt(hd.help),
								pad, k==0, k+1==aType.size());
						}
					}
					s += FuncGen.getClsClbk(pad);
				}
				s += "\n";
				s += "/****************************************\n";
				s += "*            user's methods\n";
				s += "*****************************************/\n";
				s += "\n}\n";
				FileOps fop = new FileOps();
				if (fop.askConf( "This code will generate for " + CodeType + " based on " + ClsName
					+ ",\n do you want to continue?") == 0)
						fop.fileSave(PathName, ClsName, s);
			}
		}	
		} catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					" on output: " + e.getMessage()); }
	}
	
	private boolean rdXmlFile(String s) {
		if (s.length() > 0) {
			try {		
				readXML rd = new readXML();
				if (s.indexOf(':') == -1) {
					if (new File(fPath + s).exists()== false) {		// reset
						if (LogTrace.getLog().chkLevel(LogTrace.ERROR)) 
							LogTrace.getLog().Trace(LogTrace.ERROR, 
								fPath + s);
						fPath = System.getProperty("user.dir") + "\\";
					}
					rd.setDocument(fPath + s);
				}
				else 
					rd.setDocument(s);
				rd.trimDocument();
	
				if (SwUtil.chkXmlType(s, rd.getXMLtype())) {
				
				fName = s;
				parseDoc(rd, rd.getDocRoot());
				return true;
				}
			}
			catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"CodeGen Exception: (rdXmlFile) " + e.getStackTrace());}
		}
		return false;
	}
	private void parseDoc(readXML rd, Node pnode) {
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			String pName = pnode.getNodeName();
			if (pName.startsWith("ToolBar")) pName = "Button";	// changed here !!!
			for ( int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				if (nm.charAt(0) != '#') {
					boolean isXmlFile = rd.getAttrValue(node, "XmlFile").length() > 0;
					if (isXmlFile) {
//						if (isOne)
							rdXmlFile(rd.getAttrValue(node, "XmlFile"));
//						else 
//							new CodeGen().genCode(rd.getAttrValue(node, "XmlFile"), isOne);
					}
					else if (nm.equalsIgnoreCase("SaveId")) {
						aSav.add(new holder(pName,rd.get1stNodeValue(node),getName(pName, rd, pnode, false)));
					}
					else if (nm.equalsIgnoreCase("UserDef")) {
						setAdef(rd.get1stNodeValue(node));
						aDev.add(new holder(pName, getName(pName, rd, pnode, true), getEType(pName, rd, node)));
					}
					else {
						int t = GuiMgr.SwClbk.getCallBack().getClsNum(nm);
						if ( t >= Itypes.UNKNOWN );			// ignore
						else if (rd.getTypeValue(node, new String[] {"UserDef"}, 0).length() == 0) {
							if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
								LogTrace.getLog().Trace(LogTrace.INFO, 
									nm + " (" + getName(pName, rd, node, false) +
										") default at ........>> CALLBK ");
						}
					}
					if (!isXmlFile)
						parseDoc(rd, node);
				}
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"CodeGen Exception: (parseDoc) " + e.getMessage());}
	}
	private String getName(String pname, readXML rd, Node node, boolean f) {
		try {
		String name = rd.getTypeValue(node, new String[] {"Name"}, 0);
		if (name.length() == 0)
			name = rd.getTypeValue(node, new String[] {"Text"}, 0);
		if (f && name.length() == 0) {
			if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					"The class  (" + pname + ") is no name!!!");
			name = "NoName";
		}
		return name;
		}
		catch (Exception e1) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					" on getName " + pname);
			return "NoName";
		}
	}	
	private String getEType(String name, readXML rd, Node node) {
		try {
		int i = GuiMgr.SwClbk.getCallBack().getClsNum(name);
		if (i < Itypes.UNKNOWN) {
			i = evtType[i];
			if (i == Itypes.KEYPRESSED) {
				String value = rd.getAttrValue(node, "Alter");
				if (value.indexOf('K') == -1) i = Itypes.FOCUSGAINED;
			}
		}
		else {
			if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
				LogTrace.getLog().Trace(LogTrace.WARNING, 
					"The class name (" + name + ") is unknown!!!");
			i = Itypes.ACTION;
		}
		return new Integer(i).toString();
		}
		catch (Exception e1) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					" on getEType " + name);
			return "3";		// button type
		}
	}	
}
