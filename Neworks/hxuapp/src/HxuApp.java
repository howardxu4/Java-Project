

import java.util.*;
import Dispatch.*;
import Dispatch.IEreturn.*;

public class HxuApp {
	
	public class myfunc implements ICallback0 {
		public void callback0(cInfo info) {
			System.out.println(info.func + " Returned: " + (String)info.Return);
			if (info.uData != null)  {
				System.out.println(" AppFinish() ");
				// AppFinish();
			}
		}
	}
	
	public ICallback0 getCLBK() {
		return new myfunc();
	}

	public String AppStart(String config ) {
		if (config == null)
			config = "app_config.xml";
		Dispatch dp = Dispatch.getDispatch();
		SvcList sl = SvcList.getSvcList();
		LoadConf cfg = LoadConf.getLoadConf();
		cfg.initConf(config);
		cfg.loadDictionary(sl);  // load service dictionary
		String p = cfg.getFromConf("<useservices>");
		sl.setUsingSvc(p);
		p = cfg.getFromConf("<localservice>");
		if (p == null) p = "";
		sl.UpdateSvcList(p, "local", 1000, "Dispach.Ucall");
		p = cfg.getFromConf("<xmlrpc>");
		if (p != null) {
			int portnum = 9040;
			String nameserver = cfg.getTagValue(p, "<nameserver>");
			dp.Vcall("XmlrpcApi.XmlrpcApi.client", nameserver);
			p = cfg.getTagValue(p, "<port>");
			if (p != null) portnum = new Integer(p).intValue();
			if (portnum < 8000) portnum = 9040;
			portnum += new Random().nextInt(100);
			dp.Vcall("XmlrpcApi.XmlrpcApi.server", portnum);
		}
		p = cfg.getFromConf("<xmpp>");
        if (p != null) {
            String username = cfg.getTagValue(p, "<username>");
            String password = cfg.getTagValue(p, "<password>");
            String resource = cfg.getTagValue(p, "<resource>");
            dp.Vcall("XmppApi.XmppApi.XMPP_Start", username, password, resource, "");
        }
        return getAvailSvc(500);
	}
	public String getAvailSvc(int t) { 
     // wait a while for connection of services
		String svc = new String();
        int i = t/5;
        do {
        	try {
        		Thread.sleep(t);
        	} catch (Exception e) {}
        	t-=i;
        	svc = SvcList.getSvcList().getAvailSvs();
        	System.out.print(".");
        } while (svc.isEmpty() && t >0 );
        if (svc.isEmpty() && i > 99)
        	System.out.println("\nNo service available!");
        return svc;
	}
	public void AppFinish() {
		Dispatch.getDispatch().Vcall("XmlrpcApi.XmlrpcApi.shutdown");
		Dispatch.getDispatch().Vcall("XmppApi.XmppApi.XMPP_End");
		System.exit(0);
	}
	
	public void Server(String config) {
		Dispatch dp = Dispatch.getDispatch();
		SvcList sl = SvcList.getSvcList();
		LoadConf cfg = LoadConf.getLoadConf();
		cfg.initConf(config);
		cfg.loadDictionary(sl);  // load service dictionary
		String svcsupport = sl.getSvcSupported();
		if (svcsupport == null) {
			System.out.println("**** No service supported in this server ****\n");
			System.exit(0);
		}
			System.out.println("-----Supported services: " + svcsupport);
		String p = cfg.getFromConf("<useservices>");
		if (p != null) {
			sl.setUsingSvc(p);
			for (int i=0; i<p.length();i++)
				if (svcsupport.indexOf(p.charAt(i)) != -1) {
					String svc = "" + p.charAt(i);
                    sl.UpdateSvcList(svc, "local", 1000, "Dispatch.Ucall");
             }
		}
		p = cfg.getFromConf("<xmlrpc>");
		if (p != null) {
			int portnum = 8080;
			String nameserver = cfg.getTagValue(p, "<nameserver>");
			dp.Vcall("XmlrpcApi.XmlrpcApi.client", nameserver);
			p = cfg.getTagValue(p, "<port>");
			if (p != null) portnum = new Integer(p).intValue();
			dp.Vcall("XmlrpcApi.XmlrpcApi.servers", nameserver, portnum, svcsupport);
		}	
		p = cfg.getFromConf("<xmpp>");
        if (p != null) {
            String username = cfg.getTagValue(p, "<username>");
            String password = cfg.getTagValue(p, "<password>");
            String resource = cfg.getTagValue(p, "<resource>");
            dp.Vcall("XmppApi.XmppApi.XMPP_Start", username, password, resource, svcsupport);
        }
        p = cfg.getFromConf("<start_init>");
        if (p != null) {
        	// dp.Vcall(p);
        }
	}
	
	static public void cmain(String[] args) {
		HxuApp hxu = new HxuApp();
		String p = hxu.AppStart(null);
		if(!p.isEmpty()) {
		
			if (p.indexOf('L') == -1)
				System.out.println("Service is not found");
			else {
			System.out.println("testing...");
			Dispatch dp = Dispatch.getDispatch();
	/*	
			p = dp.Pcall("musicapi.MUSIC_Artist_Get", "%s;%s", "Mcdonna", "");
			System.out.println("Returned: " + (String)p);
	*/	
			int n = dp.Icall("utility.GetFileSizeByName","%s","/opt/bubble/bin/restart");
		    System.out.println("return : " + n);
		    
		    n = dp.Icall("usersapi.USER_Login", "%s,%s,%s", "howard", "pioneer", "10.0.2.138");
		    System.out.println("return : " + n);
		    
		    String sp = "" + n;
		    ICallback0 me = hxu.getCLBK();
		    
		    cInfo c = dp.Mcalls('P', "usersapi.USER_Name_Get", "%d", sp, null, 0, me, null);
		    System.out.println("Returned: " + c.status);
		    
	//		String fmt = "ID; GROUP; ALBUM;YEAR; DURATION; TITLE; URL";
	//	    String text = "<ARTIST>Cher</ARTIST><TRACK>Believe</TRACK>";
	//	    String paras = dp.getParas("%s,%s,%d,%d,%d", text, fmt, 0, 3, n);
	//	    c =  dp.Mcalls('P',"musicapi.MUSIC_Ask", "%s,%s,%d,%d,%d", paras, null, 0, me, 1);
	//	    System.out.println("Returned: " + c.status);
	    
	//	    p = dp.Pcall("musicapi.MUSIC_Ask", "%s,%s,%d,%d,%d", text, fmt, 0, 3, n);
	//	    System.out.println("Returned: " + (String)p);
				 
		    p = dp.Pcall("usersapi.USER_Name_Get", "%d", n);
		    System.out.println("Returned: " + (String)p);
		    
		    n = dp.Icall("usersapi.USER_Logout", "%d", n);
	        System.out.printf(" USER_Logout return : %d, (%s)\n", n, p);    
			}
		}
	    hxu.AppFinish();
	}
	static public void main(String[] argv) {
		if (argv.length == 0) 
			cmain(argv);
		else {
			HxuApp hxu = new HxuApp();
			hxu.Server(argv[0]);
//			hxu.Server(null);
			System.out.println("Server start...");
	        do {
	        	try {
	        		Thread.sleep(1000000);
	        	} catch (Exception e) {}
	        } while (true);

			
		}
	}
}
