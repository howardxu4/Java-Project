package XmlrpcApi;

import Dispatch.IEreturn.*;

import java.net.URL;
import java.util.Vector;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class XmlrpcApi {
	static private XmlrpcApi m_xa;
	private boolean m_running;
	private WebServer m_server;
	private XmlrpcApi() {
		m_running = false;
	}
	static public XmlrpcApi getXmlrpcApi() {
		if (m_xa == null) 
			m_xa = new XmlrpcApi();
		return m_xa;
	}
	boolean isRunning() {
		return m_running;
	}
	public boolean client(String servername) {
		try {
			ssdp sp = new ssdp(servername);
			sp.start();
			return true;
		}
		catch(Exception e){};
		return false;
	}
	public boolean servers (String nameserver, int port, String svcs) {
		if (m_running) return m_running;
		try {
			ssdp sp = new ssdp(nameserver, port, svcs);
			sp.start();
		}
		catch (Exception e) { return false;}
		return server( port);
	}
//	public void server (int port, String cfgfile) {
	public boolean server (int port) {
		if (m_running == false)
		try {
		    m_server = new WebServer(port);
		    XmlRpcServer xmlRpcServer = m_server.getXmlRpcServer();
		    PropertyHandlerMapping phm = new PropertyHandlerMapping();
	     /* Load handler definitions from a property file.
	      * The property file might look like:
	      *   Calculator=org.apache.xmlrpc.demo.Calculator
	      *   org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
	      */
//		    System.out.println("Set handler");
//		    phm.addHandler("sv", XmlrpcMgr.class);
		    
		    String cfgfile = "MyHandlers.properties";
		    if (cfgfile != null)
		    	try {
		    		phm.load(Thread.currentThread().getContextClassLoader(), cfgfile);
		    	}
		    	catch(Exception e) {}
	     
	     /* You may also provide the handler classes directly,
	      * like this:
	      * phm.addHandler("Calculator",
	      *     org.apache.xmlrpc.demo.Calculator.class);
	      * phm.addHandler(org.apache.xmlrpc.demo.proxy.Adder.class.getName(),
	      *     org.apache.xmlrpc.demo.proxy.AdderImpl.class);
	      */
		    xmlRpcServer.setHandlerMapping(phm);
		    XmlRpcServerConfigImpl serverConfig =
		    	(XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
		    serverConfig.setEnabledForExtensions(true);
		    serverConfig.setContentLengthOptional(false);
	
		    m_server.start();
//		    System.out.println("Started successfully.");
		    m_running = true;
		} 
		catch (Exception exception) {
			System.err.println("XmlRpc Server: " + exception );
			exception.printStackTrace();
			m_running = false;
		}
		return m_running;
	}

	public void shutdown() {
		if (m_running) {
			m_server.shutdown();
			m_running = false;
		}
	}
	public XmlRpcClient getClient(String uri) {
		XmlRpcClient client = null;
		try {
		    XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		    client = new XmlRpcClient();      
		    pConfig.setServerURL(new URL(uri));
		    client.setConfig(pConfig);	
		}
		catch (Exception e) {
			System.out.println("XmlRpc Client: " + e.getMessage());
			client = null;
		}
		return client;
	}
	public Object execute(XmlRpcClient client, String func, Object[] para) {
		Object result = null;
		try {
			result = client.execute(func, para); 
		}
		catch(Exception e) { 
			System.out.println("XmlRpc Call: " + e.getMessage());
		}
		return result;
	}
	
	public static void main(String[] args) {
		
//		String cfg = "MyHandlers.properties";
//		cfg = null;
		XmlrpcApi xa = getXmlrpcApi();
//		xa.server(8080, cfg);
		xa.server(8080);
		
    	XmlRpcClient client = xa.getClient("http://127.0.0.1:8080/xmlrpc");
	     
        // start building the parameter list
        Vector<Object> params = new Vector<Object>();

        if(args.length>0) params.add(new Integer(args[0]));
        else params.add(new Integer(20));
        if(args.length>1) params.add(new Integer(args[1]));
        else params.add(new Integer(30));
		Object result = xa.execute(client,"sv.sum", params.toArray());
		System.out.println( result + "  " + result.getClass().getName());
		result = xa.execute(client,"sv.sub", params.toArray());
		System.out.println( result + "  " + result.getClass().getName());
		
		params.clear();
		params.add("Hello");
		result = xa.execute(client,"sv.myecho", params.toArray());
		System.out.println( result + "  " + result.getClass().getName() );

		XmlrpcMgr xm = new XmlrpcMgr();
		String b = "this is a test for binary data";
		Object [] r;

		Object[] ob = new Object[2];
		ob[0] = 0;
		ob[1] = 1;
		
		System.out.println("-------binary call--------[No ob change]");
		params.clear();
		params.add(ob);
		params.add(b.getBytes());
		params.add(b.length());
		result = xa.execute(client,"sv.binary", params.toArray());
		System.out.println( result + "  " + result.getClass().getName() );
		System.out.println(ob[0] + " --  " + ob[0].getClass().getName());
		System.out.println(xm.mkString(ob[0]));
		System.out.println(ob[1] + " --  " + ob[1].getClass().getName());

		System.out.println("-------Gbinary call--------");
		result = xm.Gbinary(ob, b.getBytes(), b.length());
		System.out.println( result + "  " + result.getClass().getName() );
		r = (Object[])result;
//		System.out.println(ob[0] + " --  " + ob[0].getClass().getName());
		System.out.println(r[0] + " --  " + r[0].getClass().getName());
		System.out.println(xm.mkString(r[0]));
		System.out.println(r[1] + " --  " + r[1].getClass().getName());
			
		System.out.println("----- Qcall------");
		params.clear();
		params.add("XmlrpcApi.XmlrpcMgr.binary");
		params.add(b.getBytes());
		params.add(b.length());
		params.add(new String());
		params.add("");
		result = xa.execute(client,"sv.Qcall", params.toArray());
		System.out.println( result + "  " + result.getClass().getName() );
		r = (Object[])result;
		System.out.println(r[0] + " --  " + r[0].getClass().getName());
		System.out.println(xm.mkString(r[0]));
//		System.out.println(r[1] + " --  " + r[1].getClass().getName() + "\n");
	
/*		
		client = xa.getClient("http://127.0.0.1:8089/RPC2");
		params.clear();
	    params.add(new String("utility.GetFileSizeByName"));
	    params.add("%s");
	    params.add(new String("/opt/bubble/bin/restart"));
	    result = xa.execute(client, "sv.Icall", params.toArray() );
	    System.out.println( result + "  " + result.getClass().getName() );
	    
	    params.clear();
	    params.add(new String("usersapi.USER_Login"));
	    params.add("%s,%s,%s");
	    params.add("howard,pioneer,10.0.2.138");
	    result = xa.execute(client, "sv.Icall", params.toArray() );
	    int n = ((Integer)result).intValue();
	    System.out.println( result + "  ==  " + n + "  " + result.getClass().getName());
	    
	    params.clear();
	    params.add("usersapi.USER_Name_Get");
	    params.add("%d");
	    String p = "" + n;
	    params.add(p);
	    result = xa.execute(client, "sv.Pcall", params.toArray() );
	    System.out.println( result + " ! " + result.getClass().getName() + " !");   
	    if (Object[].class.isInstance(result)) {
	    	String s = new String();
	    	Object[] a = (Object[])result;
	    	for(int i=0; i < a.length; i++ )
		    	s += a[i];
	    	System.out.println(s);
	    }
	    else System.out.println(Object[].class.getName());

*/	    
	
	    params.clear();
	    uInfo vret = new uInfo();
	    vret.conn = "http://127.0.0.1:8080/xmlrpc";
	    vret.func = "XmlrpcApi.XmlrpcMgr.sum";
	    vret.fmt = "%d,%d";
	    vret.paras = "11,99";
	    vret.type = 'I';
	    vret.value = 0;
	    result = xm.Ucall(vret);
	    System.out.println( result + "  " + vret.value );
	    
	    vret.type = 'Q';
	    vret.value = 0;
	    vret.point = null;
	    vret.func = "XmlrpcApi.XmlrpcMgr.binary";
//	    vret.fmt = "%P,%S,%I";
	    vret.fmt = "";
	    vret.data = b.getBytes();
		vret.size = b.length();
		vret.paras = new String();
		System.out.println("--------- ");
		
		result = xm.Ucall(vret); 
		System.out.println( result + "  " + result.getClass().getName() );
		
		System.out.println(vret.value);
		System.out.println(vret.point + " --  " + vret.point.getClass().getName());
		System.out.println(xm.mkString(vret.point));
		
		int sz = xm.Get_Icon(ob, "hello world");
		System.out.println("size = " + sz);
		
	    vret.func = "XmlrpcApi.XmlrpcMgr.Get_Icon";
	    vret.fmt = "%s";
	    vret.paras = "This is my test";
	    vret.type = 'G';
	    vret.value = 0;
	    result = xm.Ucall(vret);
	    System.out.println( result + "  " + vret.value );

		
	    System.out.println("DONE");
	    xa.shutdown();
	}
}
