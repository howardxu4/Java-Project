package Dispatch;


import org.w3c.dom.*;
import FileMgr.*;
import XmlMgr.*;

interface ICallback1 {
	public void callback1(String n, String m, char s);
}
public class LoadConf {
	static private final String SVR_CONFIG = "svr_config.xml";
	static private final String USR_CONFIG = "user_config.xml";
	
	static private LoadConf m_lc;
	private String Config;
	private LoadConf(){};
	
	public void initConf(String cfgfile){
		if (cfgfile == null) cfgfile = SVR_CONFIG;
		Config = loadFile(cfgfile);
		if (Config == null) fatal(cfgfile);
		cfgfile = getTagValue(Config, "<userconf>");
		if(cfgfile == null) fatal(USR_CONFIG);
		String user = loadFile(cfgfile);
		if (user == null) fatal(cfgfile);
		Config += user;
		// setDebug
		myrec = new record();
	}
	static public LoadConf getLoadConf() {
		if (m_lc == null)
			m_lc = new LoadConf();
		return m_lc;
	}
	private void fatal(String filename) {
		System.out.println("Can not open config file :" + filename);
		System.out.println("Please put config file " + filename + " in current directory");
		System.exit(-1);
	}
	public String getFromConf(String tag)
	{
		return getTagValue(Config, tag);
	}
	public String getTagValue(String xml, String tag){
		String value = null;
		int pos = xml.indexOf(tag);
		if (pos != -1) {
			xml = xml.substring(pos + tag.length());
			tag = tag.replace("<", "</");
			pos = xml.indexOf(tag);
			if (pos != -1) value = xml.substring(0, pos);
		}
		return value;
	}
	String loadFile(String fname){
		String cont = null;
		int l = FileMgr.GetFileSizeByName(fname);
		if (l > 0) {
			byte[] o = FileMgr.GetFromFile(fname, l);
			if (o != null) cont = new String(o);
		}
        return cont;
	}

	class record {
		String gname;
		String name;
		String module;
		char symbol;
		ICallback1 clbk;
		void setClbk(ICallback1 o) {
			clbk = o;
		}
		void recordtext(String v) {
			if (gname.equals("name"))
				name = v;
			else if (gname.equals("module"))
				module = v;
			else if (gname.equals("symbol"))
				symbol = v.charAt(0);
		}
		void recordName(String n) {
			if (n.equals("service")) {
				if (name != null && module != null && clbk != null)
					clbk.callback1(name, module, symbol);
				name = null;
				module = null;
			}
			else gname = n;
		}
	}
	private record myrec;
	void print_element_names(NodeList nodeList)
	{
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			Node node = nodeList.item(i);
			int type = node.getNodeType();
			if (type == 1) {
				myrec.recordName(node.getNodeName());
			}
			else if (type == 3) {
				myrec.recordtext(node.getNodeValue());
			}
			else
				System.out.println( " type = " + type);
			if (node.hasChildNodes())
				print_element_names(node.getChildNodes());
		}
	}
	private void loadSvc(Node node, ICallback1 o) {
		myrec.setClbk(o);
		print_element_names(node.getChildNodes());
		myrec.recordName("service");
	}
	public void loadDictionary(SvcList sl){
		String fn = getFromConf("<servicelist>");
		readXML rd = new readXML();
		if (FileMgr.GetFileSizeByName( System.getProperty("user.dir") + "/" + fn) > 0)
			fn = System.getProperty("user.dir") + "/" + fn;
		else 
			fn = System.getProperty("user.home") + "/" + fn;
		rd.setDocument(fn);
		rd.trimDocument();
//		String s = rd.dumpAll();
//		System.out.println(s);
		loadSvc(rd.getDocRoot(), sl);
//		sl.dump();	
		sl.setSvcScript();
	}
	public static void main(String[] args) {
		LoadConf cfg = getLoadConf();
		cfg.initConf(null);
		SvcList sl = SvcList.getSvcList();
		cfg.loadDictionary(sl);
		System.out.println("User Home Path: "+
				System.getProperty("user.home"));
	}
}
