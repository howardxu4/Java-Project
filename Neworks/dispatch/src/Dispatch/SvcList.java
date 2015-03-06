package Dispatch;

import java.util.*;
import ComMgr.*;

public class SvcList implements ICallback1, IcallBack{
	public class SvEntry {
		int m_id;
		String ufunction;
		String connection;
		SvEntry(String conn, String func, int id) {
			connection = conn;
			ufunction = func;
			this.m_id = id;
		}
	}
	public class SvcCont {
		char symbol;
		String sname;
		String name;
		int number; 
		Vector<SvEntry> svclist;
		SvcCont (char symbol, String sname, String name) {
			this.symbol = symbol;
			this.sname = sname;
			this.name = name;
			number = 0;
			svclist = new Vector<SvEntry> ();
		}
		boolean isSymbol(char s) { return symbol == s; }
		boolean isName(String nm) { return name.equals(nm); }
	}
	int svc_id;
	String svc_name;
	String svc_sprt;
	String svc_script;
	Vector<SvcCont> svc_list;
	static private SvcList m_sl;
	private SvcList(){
		svc_id = new Random().nextInt(99999999);
		svc_sprt = new String();
		svc_script = new String();
		svc_list = new Vector<SvcCont>();
		ComMgr.getComMgr().addListener(ComMgr.SVCLIST, this);
	}
	static public SvcList getSvcList() {
		if (m_sl == null)
			m_sl = new SvcList();
		return m_sl;
	}
	int add(char symbol, String module, String name){
		if (module.isEmpty() || name.isEmpty()) return -1;
		for (int i=0; i<svc_list.size(); i++) {
			if(svc_list.elementAt(i).isSymbol(symbol)) return 1;
			if(svc_list.elementAt(i).isName(name)) return 1;
		}
		svc_list.add(new SvcCont(symbol,module,name));
		return 0;
	}
	public String getAvailSvs() {
		String svc_ready = "";
		for (int i=0; i<svc_list.size(); i++) {
			if(svc_list.elementAt(i).number > 0)
				svc_ready += svc_list.elementAt(i).symbol; 
		}
		return svc_ready;
	}
	char getSymbol(String service) {
		for (int i=0; i<svc_list.size(); i++) {
			if(svc_list.elementAt(i).name.equals(service))
				return svc_list.elementAt(i).symbol;
		}
		return ' ';
	}
	void dump() {
		for (int i=0; i<svc_list.size(); i++) {
			SvcCont cont = svc_list.elementAt(i);
			System.out.print(" " + (i) + " " + cont.number + " " + cont.name );
			for(int j=0; j<cont.svclist.size(); j++) {
				SvEntry entry = cont.svclist.elementAt(j);
				 	System.out.print(", " + entry.connection + " " + entry.ufunction + " " +  entry.m_id);
			}
			System.out.println();
		}
	}
	public void setSvcScript() {
		svc_script = "";
		// update svc_script from svc_list
		for(int i=0; i<svc_list.size(); i++) {
			SvcCont cont = svc_list.elementAt(i);
			if (cont.symbol >= '0' && cont.symbol<= '9')
				svc_script += cont.name + ",";
		}	
	}
	public void setUsingSvc(String svcs){
		if (svcs == null || svcs.isEmpty()) {
			svc_name = "LBUSCPMTEYGOIN";			// inner dictionary
			for(int i=0; i<svc_sprt.length(); i++)
				if (svc_name.indexOf(svc_sprt.charAt(i)) == -1)
					svc_name += svc_sprt.charAt(i);
		}
		else svc_name = svcs;
	}
	String getSvcConn(int n) {
		if (n < svc_list.size()) {
				SvcCont cont = svc_list.elementAt(n);
				if (cont.svclist.size() > 0)
					return cont.svclist.elementAt(0).connection;
		}
		return null; 
	}
	public int getSvcIndex(String func) {
		String svc = getSvcName(func);
		for (int i=0; i<svc_list.size(); i++)
			if (svc_list.elementAt(i).name.equals(svc))
				return i;
		return -1;
	}
	String getSvcName(String func) {
		int i = func.indexOf('.');
		if (i != -1) 
			return func.substring(0,i);
		return "serviceapi";
	}
	String getSvcUfunc(int n){
		if (n < svc_list.size()) {
				SvcCont cont = svc_list.elementAt(n);
				if (cont.svclist.size() > 0)
					return cont.svclist.elementAt(0).ufunction;
		}
		return null;
	}
	boolean isScriptCall(String func){
		if (!svc_script.isEmpty()) {
			String svc = getSvcName(func);
			return (svc_script.contains(svc));
		}
		return false;
	}
	public int UpdateSvcList(String services, String connection, int id, String ufunc){
		int chg = 0;
//		System.out.println(services + " " + connection + " " + id + " " + ufunc);
		if (ufunc == null || ufunc.isEmpty()) services = svc_name;
		if (services != null) {
		    for(int n=0; n<services.length();n++) {
		    	char c = services.charAt(n);
		    	if (svc_name.indexOf(c) != -1) {
		    		for(int i=0; i<svc_list.size(); i++) {
		    			if(svc_list.elementAt(i).symbol == c) {
		    				SvcCont cont = svc_list.elementAt(i);
		    				if (ufunc != null && !ufunc.isEmpty()) {
		    					boolean needAdd = true;
		    					for(int j=0; j<cont.svclist.size(); j++) {
		    						SvEntry entry = cont.svclist.elementAt(j);
		    						if (entry.m_id == id && entry.connection.equals(connection)) {
		    							needAdd = false;
		    							break;
		    						}
		    					}
		    					if (needAdd) {
		    						cont.svclist.add(new SvEntry(connection, ufunc,id));
		    						cont.number++;
		    						chg++;
		    					}
		    				}
		    				else {
		    					for(int j=0; j<cont.svclist.size(); j++) {
		    						if(cont.svclist.elementAt(j).connection.equals(connection)) {
		    							cont.svclist.remove(j);
		    							cont.number--;
		    							chg++;
		    							break;
		    						}
		    					}
		    				}
		    			}
		    		}
		    	}
	        }
		}
		if (chg != 0) ; // dump();
//		System.out.println("Update : " + chg + " " + svc_name);
		return 0;
	}
	void setSvc(char symbol){
		if(!svc_sprt.isEmpty()) {
			if (svc_sprt.indexOf(symbol) == -1)
				svc_sprt += symbol;
		}
		else 
			svc_sprt += symbol;
	}
	// load service dictionary from loadconf
	public void callback1(String name, String module, char symbol){
//		System.out.println("Call " + symbol + " " + name + " " + module);
		this.add(symbol, module, name);
		this.setSvc(symbol);
	}
	// update service from ComMgr 
	public Object callBack(int type, Object info) {
		Class<?> x = Argument.class;
		if (x.isInstance(info)) {
			Argument arg = (Argument)info;
			String[] para = new String[4];
			para[0] = (String)arg.get(ComMgr.TYPE);
			para[1] = (String)arg.get(ComMgr.URI);
			para[2] = (String)arg.get(ComMgr.NAME);
			para[3] = (String)arg.get(ComMgr.VALUE);
			int id = new Integer(para[2]).intValue();
			this.UpdateSvcList(para[0], para[1], id, para[3]);
		}
		return null;
	}
	public String getSvcSupported() {
		if (svc_sprt.isEmpty()) return null;
		return "" + svc_id + ":" + svc_sprt;
	}
}
