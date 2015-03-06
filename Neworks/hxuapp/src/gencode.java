
import org.w3c.dom.*;

import java.util.*;
import java.text.*;

import FileMgr.*;
import XmlMgr.*;

public class gencode {
	public class paras {
		String para;
		String attr;
		paras(String value) {
			attr = value;
		}
	}
	class record {
		String mtd;
		String fmt;
		String rtn;
		String rta;
		String mdl;
		String dsc;
		ArrayList<paras> argv;

		public record(String name){
			mtd = name;
			fmt = rtn = rta = dsc = null;
			argv = new ArrayList<paras>();
		}

	}
	
	private String lib;
	private String cls;
	private String mdl;
	private int m_mode;
	private String m_name;					// current name [type]
	private paras m_para;					// current parameter
	private record m_record;				// current method
	private ArrayList<record> m_alist;
	private boolean m_debug;
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	
	public gencode() {
		m_alist = new ArrayList<record>();
		m_record = null;
	}
	private void my_print(String s) {
		if (m_debug)
			System.out.println("My Debug Print: " + s);
	}
	private void dumpAttr(NamedNodeMap attrs, String name) {
		   for(int j = 0; j < attrs.getLength(); j++) {
				Node attrNode = attrs.item(j);
				String aname = attrNode.getNodeName();
				String value = attrNode.getNodeValue();
				if (aname.equals("name")) {
					if (name.equals("method")) {
						m_record = new record(value);
						m_alist.add(m_record);
					}
					else if (name.equals("format"))
						m_record.fmt = value;
					else if (name.equals("class"))
						cls = value;
					else if (name.equals("library")) {
						lib = value;
						mdl = lib.substring(3, lib.indexOf('.'));
					}
					else	
						my_print(name + "  ==>> " + value);
				}
				else if (aname.equals("attr")) {
					if (name.equals("param")) {
						m_para = new paras(value);
						m_record.argv.add(m_para);
					}
					else if (name.equals("return"))
						m_record.rta = value;
					else
						my_print(name + "  ATTR == " + attrNode.getNodeValue());
				}
				else if (aname.equals("version")); // pass
				else 
					my_print(name + " ATTR " + aname + " ???? " + attrNode.getNodeValue());
		   }
	}
	private String dumpNode(Node node){
		String name = node.getNodeName();
//		System.out.println(name);
		if(node.hasAttributes())
			dumpAttr(node.getAttributes(), name);
		return name;
	}
	private void dumpNodes(NodeList nodeList) {
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			Node node = nodeList.item(i);
			int type = node.getNodeType();
			if (type == 1) {
				m_name = dumpNode(node);
			}
			else if (type == 3) {
				if (m_name.equals("format"))
					m_record.fmt = node.getNodeValue();
				else if (m_name.equals("param"))
					m_para.para = node.getNodeValue();
				else if (m_name.equals("return"))
					m_record.rtn = node.getNodeValue();
				else if (m_name.equals("description"))
					m_record.dsc = node.getNodeValue();
				else
					my_print(m_name + " => " + node.getNodeValue());
			}
			else
				my_print( " type = " + type);
			if (node.hasChildNodes())
				dumpNodes(node.getChildNodes());
		}		
	}

	public void start(String fn) {
		readXML rd = new readXML();
		if (fn.charAt(0) == '/')
			rd.setDocument(fn);
		else
			rd.setDocument(System.getProperty("user.dir") + "/" + fn);
		rd.trimDocument();
		Node node = rd.getDocRoot();
		dumpNode(node);
		dumpNodes(node.getChildNodes());
	}
	private int getFmt(String f, int n) {
		int ret = 0;
		do {
			int i = f.indexOf('%');
			f = f.substring(i+1);
		} while (n-- > 0);
		switch(f.charAt(0)) {
		case 'd':
		case 'u':
		case 'i':
			m_name = " int ";
			ret = 1;		// Integer
			break;
		case 's':
			m_name = " String ";
			ret = 2;		// String [char*]
			break;
		case 'P':	 
			m_name = " Object[] ";
			ret = 3;		// pointer ** data
			break;
		case 'S':
			m_name = " byte[] ";
			ret = 4;		// void *info
			break;
		case 'I':
			m_name = " int ";
			ret = 5;		// int size
			break;
		case 'f':
			m_name = " float ";
			ret = 6;		// float
			break;
		case 'e':
		case 'g':
			m_name = " double ";
			ret = 7;		// double
			break;
		}
		return ret;
	}
	private String genDisc(record r) {
		String s = "/** " + r.mtd + " - " + r.dsc + "\n";
		for (paras pa : r.argv) 
			s += " * @param:\t" +  pa.para + " - " + pa.attr + "\n";
		s += " * @return:\t" + r.rta + "\n";
		s += "**/\n";
		return s;
	}
	private String genDecl(record r) {
		int n = 0;
		String s = new String();
		if (r.rtn.indexOf('*') == -1) m_name = "int ";
		else m_name = "String ";
		s += "public " + m_name + r.mtd + "(";
		for (paras pa: r.argv) {
			int t = getFmt(r.fmt, n++);
			if (t > 0) {
				if (n > 1) s += ",";
				s += m_name + pa.para;
			}
			else System.out.println("Error: check type");
		}
		s += " )";
		return s;
	}

	private String clientCode(record r) {
		String s = "\treturn m_dp.";
		String fmt = r.fmt;
		int i = 0;
		if (r.fmt.startsWith("%P,%S,%I")) { 
			s += "Qcall" + "(\"" + mdl + "." + r.mtd + "\"";
			s += ", " + r.argv.get(0).para + ", " + r.argv.get(1).para; 
			s += ", " + r.argv.get(2).para;
			i = 3;
			fmt = fmt.substring(8);
		}
		else if (r.fmt.startsWith("%S,%I")) {
			s += "Scall" + "(\"" + mdl + "." + r.mtd + "\"";
			s += ", " + r.argv.get(0).para + ", " + r.argv.get(1).para; 
			i = 2;
			fmt = fmt.substring(5);
		}
		else if (r.fmt.startsWith("%P")) {
			s +="Gcall" + "(\"" + mdl + "." + r.mtd + "\"";
			s += ", " + r.argv.get(0).para; 
			i = 1;
			fmt = fmt.substring(2);
		}
		else if (r.rtn.indexOf('*') == -1) {
			s += "Icall" + "(\"" + mdl + "." + r.mtd + "\"";
		}
		else {
			s += "Pcall" + "(\"" + mdl + "." + r.mtd + "\"";
		}		
		if ( i > 0  && !fmt.isEmpty()) {
			if (fmt.charAt(0) == ',')
				fmt = fmt.substring(1);
		}
		if (fmt.isEmpty())
			s += ", null";
		else {
			s += ", \"" + fmt + "\"";
			for (paras pa : r.argv) 
				if (i-- <= 0)
					s += ", " + pa.para;
		}
		s += ");\n";
		return s;
	}

	private String genBody(record r) {
		String s = new String();
		if (m_mode == 0) s+=";";	// interface
		else if (m_mode < 3){		// client server
			s += "{\n";
			if (m_mode == 1) {		// client
				s += clientCode(r);
			}
			else {					// server
				if (r.rtn.indexOf('*') == -1)
					s += "\tint ret = 0;\n";
				else
					s += "\tString ret = new String();\n";
				s += "\tSystem.out.println(\"Method: " + mdl + "." + r.mtd + " be called.\");\n";
				s += "\treturn ret;\n";
			}
			s += "}";
		}
		else {						// test
			if (r.rtn.indexOf("*") == -1) m_name = "ret";
			else m_name = "pret";
			s += "\t\t" + m_name + " = mytest." + r.mtd + "(";
			int n = 0;
			for (paras pa: r.argv) {
				int t = getFmt(r.fmt, n++);
				if (n > 1) s += ",";
				switch (t) {
				case 1:			// d
					s += "0";
					break;
				case 2:			// s
					s += "\"" + pa.para + "\"";
					break;
				case 3:			// P
					s += "oret";
					break;
				case 4:			// S
					s += "\"Binay\".getBytes()";
					break;
				case 5:			// I
					s += "6";
					break;
				default:
				}
			}
			s += ");\n";
			if (r.rtn.indexOf("*") == -1) m_name = "ret";
			else m_name = "pret";
			s += "\t\tSystem.out.println( \"" + r.mtd + " return: \" + " + m_name + ");";
		}
		return s + "\n";		
	}
	private String genMethod(record r) { 
		String s = new String();
		if (m_mode < 3) {
			s += genDisc(r);
			s += genDecl(r);
		}
		s += genBody(r);
		return s + "\n";
	}
	private String genMethods() {
		String s = new String();
		for ( record r : m_alist) {
			s += genMethod(r);
		}
		return s;
	}
	private String getFname(int n) {
		String fn = new String();
		switch(n) {
		case 1:
			fn = "client_" + mdl + ".java";
			break;
		case 2:
			fn = mdl + ".java";
			break;
		case 3:
			fn = "test_" + mdl + ".java";
			break;
		case 0:
		default:
			fn = "I" + mdl + ".java";
			break;
		}
		return fn;
	}
	private String getTitle(int n) {
		String s = "/**\n *\t\t" + getFname(n);
		s += "\n *\tGenerated on ";
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    s += sdf.format(cal.getTime());
		s += "\n**/\n\n";
		return s;
	}
	public String codegen(int n) {
		m_mode = n;
		String s = getTitle(n);
		cls = mdl;				// cls: Java  mdl : C++
		switch(m_mode) {
		case 0:		//Interface
			s += "public interface I" + cls +  "\n{\n";
			s += genMethods();
			break;
		case 1:		// client
			s += "import Dispatch.*;\n\n";
			s += "public class client_" + cls + " implements I" + cls + "\n{\n";
			s += "/** Constructor of client_" + cls + "     */\n"; 
			s += "Dispatch m_dp;\n";
			s += "public client_" + cls + "(){\n";
			s += "\tm_dp = Dispatch.getDispatch();\n}\n";
			s += genMethods();
			break;
		case 2:		// server
			s += "public class " + cls + " implements I" + cls + "\n{\n";
			s += genMethods();
			break;
		case 3:		// test
			s += "public class test_" + cls + "\n{\n";
			s += "\tpublic static void main(String[] argv)\n\t{\n";
			s += "\t\tint ret=0;\n";
			s += "\t\tString pret=new String();\n";
			s += "\t\tObject[] oret = new Object[1];\n";
			s += "\t\tclient_" + cls + " mytest = new client_" + cls + "();\n\n";  
			s += "\t\tHxuApp hxu = new HxuApp();\n";
			s += "\t\tpret = hxu.AppStart(null);\n";
			s += "\t\tif (pret.isEmpty())\n";
			s += "\t\t\tSystem.out.println(hxu.getAvailSvc(1000));\n\n";
			s += genMethods();
			s += "\t\thxu.AppFinish();\n\t}\n";
			break;
		}
		if (m_mode < 4) s += "}";
		return s;
	}
	public void output(String s, boolean screen, int n){
		if (screen) System.out.println(s);
		else {
			String fn = getFname(n);
			System.out.println("check out put file: " + fn);
			FileMgr.SaveToFile(fn, s);
		}
	}
	static public void main(String[] argv) {
		gencode gd = new gencode();
		String fn = "demoapi.xml";
		int i = 0;						// I:0 C:1 S:2 T:3
		boolean screen = true;
		if (argv.length == 1) {
			System.out.println("Usage: gencode {xmlfile [[-]i|I|c|C|s|S|t|T|a|A]}\n");
			System.exit(0);
		}
		if (argv.length>1) {
			fn = argv[0];
			char c = argv[1].charAt(0);
			if (c == '-') c = argv[1].charAt(1);
			switch(c) {
			case 'T':				screen = false;			
			case 't':				// Test
				i = 3;
				break;
			case 'S':				screen = false;
			case 's':				// server
				i = 2;
				break;
			case 'C':				screen = false;
			case 'c':				// Client
				i = 1;
				break;
			case 'A':				screen = false;
			case 'a':				// All 
				i = 4;
				break;
			case 'I':				screen = false;
			case 'i':
			default:				// Interface
				break;
			}
		}
		gd.start(fn);
		String s = new String();
		if (i == 4) {
			while(--i >=0) {
				s = gd.codegen(i);
				gd.output(s, screen, i);
			}
		}
		else {
			s = gd.codegen(i);
			gd.output(s, screen, i);
		}
	}
}
