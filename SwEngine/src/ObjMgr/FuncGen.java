//******************************************************************************
// Package Declaration
//******************************************************************************
package ObjMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import GuiMgr.*;
import ComMgr.*;
import ObjMgr.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * FuncGen class is code generation class for different parts of code 
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
public class FuncGen implements Itypes 
{	
	private static final String [] eventType = {
							"ActionEvent",
							"TreeSelectionEvent",
							"ListSelectionEvevnt",
							"ChangeEvent",
							"AdjustmentEvent",
							"MouseEvent",
							"MouseEvent",
							"MouseEvent",
							"MouseEvent",
							"MouseEvent",
							"FocusEvent",
							"FocusEvent",
							"KeyEvent",
							"KeyEvent",
							"KeyEvent"
	};	
	private static final String [] evtType = {
							"click",
							"afterSelect",
							"valueChanged",
							"stateChanged",
							"valueAdjusted",
							"mouseClick",
							"mouseEntered",
							"mouseExit",
							"mouseDown",
							"mouseUp",
							"focusGained",
							"focusLost",
							"keyDown",
							"keyUp",
							"keyTyped"
	};	
	private static final String [] prefix = { 
							"apllet",			// never
							"border",			// never
							"box",				// study...
						 	"btn",
							"ckbx",	
							"ckmnitm",
							"colorchooser",		// special model handle
							"combo",
							"comp",				// self handle
							"desktop",			// never
							"dialog",			// never
							"dimension",		// never
							"editpane",			// default handle
							"filechooser",		// study...
							"fmtfld",
							"frame",			// never
							"internalpane",		// never
							"label",
							"list",
							"menu",				// never
							"menubar",			// never
							"mnitm",
							"panel",
							"pswd",
							"popup",			// auto handle
							"progbar",
							"radio",
							"rdmnitm",
							"rectangle",		// never
							"scrlb",
							"scrollpane",		// never
							"slid",
							"spin",
							"splitpane",		// never
							"tabp",
							"table",			// study...
							"txta",
							"txtfld",
							"txtp",
							"toggle",
							"toolbar",			// never
							"tree",
							"window"			// never
					//	"UNKNOWN = 43;
							
	};
	
	// call back handler
	public static void genFunc(Object takeThis, int type, Object info) {
		String s = null;
		try {
		// received information 
			Argument argv = (Argument)info;
			Object obj = argv.getArgument(OBJECT);
			String name = (String)argv.getArgument(NAME);
			int eType = ((Integer)argv.getArgument(TYPE)).intValue();
			String value = (String)argv.getArgument(VALUE);
			if (value == null) value = "";
			name = name.replace(' ', '_');
			
			s = "Error: missing --> " + name + "_"  + evtType[eType] + "(J"	
			+ Itypes.ObjName[type] + " obj, String value, " + eventType[eType] + " e)"; 
			 
			GuiMgr.SwUtil.runSafety(takeThis, name + "_"  + evtType[eType],
				 new Object[] {obj, value, argv.getArgument(EVENT)}, s);
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT,
					s);
		}
	}
	
	// method declaration
	public static String getFuncDef(String name, String type, int eType, String pad) {
		if (pad == null) pad = "   ";
		String s = pad + "public void " + name.replace(' ', '_') + "_"  + evtType[eType]
			+ "(J" + type + " obj, String value, " + eventType[eType] 
			+ " e) {\n";
		s += pad + pad + "// TODO: add your code for handling the " + eventType[eType]
			+ " on J" + type + " [" + name + "]\n";
		s += pad + "}\n"; 
		return s;
	}
	
	private static String getEvtCase(String name, String type, int eType, String sType, String pad, boolean b, boolean f) {
		if (pad == null) pad = "   ";
		String s = pad + pad + pad + pad;
		if (b) s += "switch (((Integer)argv.getArgument(TYPE)).intValue()) {\n" + pad + pad + pad + pad;
		s += "case " + sType + ":\n";
		s += pad + pad + pad + pad + "// TODO: add your code for handling the ";
		s += eventType[eType] + " on J" + type + " [" + name + "]\n";
		s += pad + pad + pad + pad + pad + "break;\n";
		if (f) s += pad + pad + pad + pad + "}\n";
		return s;
	}
	
	
	// switch case content
	public static String getCaseDef(String name, String type, int eType, String pad, boolean b, boolean f) {
		if (pad == null) pad = "   ";
		String s = pad + pad;
		if (b) s += "case " + type.toUpperCase() + " :\n" + pad + pad;
		s += pad;
		if (!b) s += "else ";
		s += "if (name.equals(\"" + name + "\")) {\n";
		if (eType == Itypes.FOCUSGAINED) { 
			boolean tb = (type.equals(Itypes.ObjName[Itypes.TEXTFIELD]) ||
				type.equals(Itypes.ObjName[Itypes.PASSWORDFIELD]));
			s += getEvtCase(name, type, Itypes.FOCUSGAINED, "FOCUSGAINED", pad, true, false);
			s += getEvtCase(name, type, Itypes.FOCUSLOST, "FOCUSLOST", pad, false, !tb);
			if (tb) s += getEvtCase(name, type, Itypes.ACTION, "ACTION", pad, false, true);
		}
		else if (eType == Itypes.KEYPRESSED) {
			boolean tb = (type.equals(Itypes.ObjName[Itypes.TEXTFIELD]) ||
				type.equals(Itypes.ObjName[Itypes.PASSWORDFIELD]));
			s += getEvtCase(name, type, Itypes.KEYPRESSED, "KEYPRESSED", pad, true, false);
			s += getEvtCase(name, type, Itypes.KEYRELEASED, "KEYRELEASED", pad, false, false);
			s += getEvtCase(name, type, Itypes.KEYTYPED, "KEYTYPED", pad, false, !tb);
			if (tb) s += getEvtCase(name, type, Itypes.FOCUSGAINED, "FOCUSGAINED", pad, false, false);
			if (tb) s += getEvtCase(name, type, Itypes.FOCUSLOST, "FOCUSLOST", pad, false, true);
		}
		else if (eType == Itypes.MOUSECLICKED) {
			s += getEvtCase(name, type, Itypes.MOUSECLICKED, "MOUSECLICKED", pad, true, false);
			s += getEvtCase(name, type, Itypes.MOUSEENTERED, "MOUSEENTERED", pad, false, false);
			s += getEvtCase(name, type, Itypes.MOUSEEXIT, "MOUSEEXIT", pad, false, false);
			s += getEvtCase(name, type, Itypes.MOUSEPRESSED, "MOUSEPRESSED", pad, false, false);
			s += getEvtCase(name, type, Itypes.MOUSERELEASED, "MOUSERELEASED", pad, false, true);			
		}
		else {
			s += pad + pad + pad + "// TODO: add your code for handling the ";
			s += eventType[eType] + " on J" + type + " [" + name + "]\n";
		}
		s += pad + pad + pad + "}\n";
		if (f) s += pad + pad + pad + "break;\n";
		return s;
	}
	
	// class heaher
	public static String getClassHdr(String name, String pad) {
		if (pad == null) pad = "   ";
		String s = "/**\n";		//(Calendar.getInstance().getTime()).toString()
		s += "*******************************************************************************\n";
		s += "*\n";
		s += "*   (" + name + ") Generated on " + new java.util.Date().toString() + "\n";
		s += "*\n";
		s += "*******************************************************************************\n";
		s += "*/\n";		
		s += "import javax.swing.*;\n";		s += "import javax.swing.event.*;\n";
		s += "import javax.swing.text.*;\n\n";
		s += "import java.awt.*;\n";
		s += "import java.awt.event.*;\n";
		s += "import java.util.*;\n";
		s += "import java.io.*;\n";
		s += "import java.net.*;\n\n";

		s += "import GuiMgr.SwUtil.*;\n";
		s += "import ComMgr.*;\n\n";
		
		s += "public class " + name + " implements IcallBack\n{\n"; 
		return s;
	}
	
	// variable declaration
	public static String getVarDecl(String type, String name, String pad) {
		if (pad == null) pad = "   ";
		return pad + "private J" + type + " " 
			+ prefix[GuiMgr.SwClbk.getCallBack().getClsNum(type)] 
			   + "_" + name.replace(' ', '_') + ";\n";	
	}
	
	// class constructor
	public static String getClsCnst(String name, int un, String pad) {
		if (pad == null) pad = "   ";
		String s = "\n";		s += "/****************************************\n";
		s += "*            constructor\n";
		s += "*****************************************/\n";
		s += pad + "public " + name + " () {\n";
		s += pad + pad + "ComMgr.getComMgr().addListener(ComMgr.USER0" + un 
			 + ", this);\n";
		s += pad + pad + "// user's special init (before load)\n";
		s += pad + "}\n\n";
		s += "/****************************************\n";
		s += "*            init run-time variables\n";
		s += "*****************************************/\n";
		s += pad + "private void init() {\n";
		return 	s;
	}
	
	// init variables
	public static String getInitVar(String type, String name, String pad) {
		if (pad == null) pad = "   ";
		return pad + pad + prefix[GuiMgr.SwClbk.getCallBack().getClsNum(type)] 
			 + "_" + name + " = (J" + type + ")GuiMgr.SwUtil.getObject(\""
			 + name + "\");\n";
	}
	
	// class callback
	public static String getClsClbk(String pad, boolean vb) {
		if (pad == null) pad = "   ";
		String s = pad + pad + "// user's variable init (after load)\n";
		s += pad + "}\n\n";
		s += "/****************************************\n";
		s += "*         implements call back handler \n";
		s += "*****************************************/\n";
		s += pad + "public Object callBack(int type, Object info) {\n";
		s += pad + pad + "if (type == NOTIFY) init();\n";
		s += pad + pad + "else if (type < UNKNOWN) \n";
		if (vb) 
			s += pad + pad + "   GuiMgr.SwUtil.genMethod(this, type, info);\n";
		else {
			s += pad + pad + "   GuiMgr.SwUtil.runSafety(this, \"process\",\n";			s += pad + pad + "      new Object[]{new Integer(type), info}, null);\n";		}
		s += pad + pad + "else {   // user definded message handler\n";
		s += pad + pad + "}\n";
		s += pad + pad + "return null;\n";
		s += pad + "}\n\n";
		s += "/****************************************\n";
		s += "*         system generated methods\n";
		s += "*****************************************/\n";		if (!vb) {
		s += pad + "public void process(int type, Object info) {\n";		s += pad + pad + "Argument argv = (Argument)info;\n";
		s += pad + pad + "String name = (String)argv.getArgument(NAME);\n\n";
		s += pad + "// the following useful info maybe referred on some case\n";
		s += pad + "// Object obj = argv.getArgument(OBJECT);\n";
		s += pad + "// int eType = ((Integer)argv.getArgument(TYPE)).intValue();\n";
		s += pad + "// Object event = argv.getArgument(EVENT);\n";
		s += pad + "// String value = (String)argv.getArgument(VALUE);\n\n";
		s += pad + pad + "switch (type) {\n";
		}
		return s;
	}
	
	public static String getClsClbk(String pad) {
		if (pad == null) pad = "   ";
		String s = pad + pad + "default:\n";
		s += pad + pad + pad + "break;\n";
		s += pad + pad + "}\n";
		s += pad + "}\n";
		return s;
	}
}
