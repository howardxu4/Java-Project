package XmlrpcApi;

import java.io.*;
import java.net.*;
import java.util.*;

import ComMgr.*;

public class ssdp implements Runnable{

	private static final int PORT = 1900;
	private static final String ADDRESS = "239.255.255.250";
	
//	private static final int DEFAULT_MSEARCH_MX = 3;
	private static final int RECV_MESSAGE_BUFSIZE = 1024;
	
	private static final String SSDP_QUERY_STR =
  		"M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nMAN: \"ssdp:discover\"\r\nST: %s\r\nMX: 3\r\n\r\n" ;
	private static final String SSDP_NOTIFY_STR =
        "NOTIFY * HTTP/1.1\r\nDate: %s\r\nHOST: 239.255.255.250:1900\r\nNTS: ssdp:%s\r\nCACHE-CONTROL: max-age = 1830\r\nLOCATION: %s\r\nST: %s\r\nUSN:uuid:%s\r\n\r\n";
	private boolean m_debug;
	
	private void my_print(String s) {
		if (m_debug)
			System.out.println("SSDP Debug Print: " + s);
	}
	/**
	 * SSDP discover constructor 
	 * @param sn
	 * @throws IOException
	 */
	public ssdp(String sn) throws IOException{
		address = InetAddress.getByName(ADDRESS);
		socket = new DatagramSocket();
        sname = sn;
        svr = null;
        uuid = null;
        m_waiting = 50000;
        m_running = false;
        m_debug = false;
	}
	/**
	 * SSDP server constructor
	 * @param sn
	 * @param port
	 * @param svcs
	 * @throws IOException
	 */
	public ssdp(String sn, int port, String svcs) throws IOException{
        address = InetAddress.getByName(ADDRESS);
		MulticastSocket ms = new MulticastSocket(PORT);
        ms.joinGroup(address);
        ms.setLoopbackMode(false);
        socket = ms;
        sname = sn;
		svr = svcs;
        uuid = UUID.randomUUID().toString();
        seturi();	
        uri = "http://" + uri + ":" + port + "/xmlrpc";
        m_waiting = 300000;
        m_running = false;
	}
	/**
	 * start method
	 */
	public void start() throws IOException{
		if (!m_running)
			new Thread(this).start();
	}
	/**
	 * Callback interface implementation
	 * @param msg
	 */
	private void passMsg(String uri, String s)
	{
		String ss[] = s.split(":");
		my_print("Set Service --> " + uri + " " + ss[0] + "  " + ss[1]);
		Argument info = new Argument();
		info.put(ComMgr.URI, uri);	// URI
		info.put(ComMgr.NAME, ss[0]);	// NAME
		info.put(ComMgr.TYPE, ss[1]);	// TYPE
		info.put(ComMgr.VALUE, "XmlrpcApi.XmlrpcMgr.Ucall");	// VALUE
		ComMgr.getComMgr().sendMsg(ComMgr.SVCLIST, ComMgr.UPDATE, info);
	}
	/**
	 * setWait set waiting time
	 * @param sec
	 */
	public void setWait(int sec) {
		m_waiting = sec * 1000;
	}
	private void seturi() throws IOException{
		 Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets)) {
	        	if (netint.getDisplayName().matches("lo") == false) {
	        		my_print("---------" + netint.getDisplayName());
	        		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	        		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        			uri = inetAddress.getHostAddress();
	        			if (uri.contains(".")) 
	        				return;
	        		}
	        	}
	        }
	        uri = InetAddress.getLocalHost().getHostAddress();
	}
	/**
	 * query method for discovery
	 */
	public void query() {
		try {
			String s = String.format(SSDP_QUERY_STR, sname);
			DatagramPacket p = new DatagramPacket(s.getBytes(), s.length(), address, PORT);
			socket.send(p);
		}
		catch (IOException e){
			System.err.println(e.getMessage());
		}
	}
	private void notify(String msg, InetAddress addr, int port) {
		try {
			String date = new Date().toString();
			String s = String.format(SSDP_NOTIFY_STR, date, msg, uri, svr, uuid);
			DatagramPacket p = new DatagramPacket(s.getBytes(), s.length(), addr, port);
			socket.send(p);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	private void processMsg(String msg)
    {
    	if (msg.contains("ssdp:alive")) {
    		int i = msg.indexOf("LOCATION: ");
    		if (i != -1) {
    			String s = msg.substring(i+10);
    			i = s.indexOf('\r');
    			String ss = s.substring(0, i);
    			i = s.indexOf("\nST: ");
    			if (i != -1) {
    				s = s.substring(i+5);
    				i = s.indexOf('\r');
    				s = s.substring(0, i);
    				passMsg(ss, s);
    			}	
    		}
    		else
    			my_print(msg);
    	}
    }
    private void processMsg(String msg, InetAddress addr, int port)
    {
    	if (msg.contains("M-SEARCH")) {
    		if (msg.contains("ssdp:discover")) {
    			int i = msg.indexOf("\nST: ");
    			if (i != -1) {
    				String s = msg.substring(i+5);
    				i = s.indexOf('\r');
    				s = s.substring(0, i);
    				if (sname.contains(s)) {
        				my_print("--- Services [" + svr + "] response to " + addr);
    					notify("alive", addr, port);
    				}
    			}
    		}
    		else
    			my_print("Recv: " + msg);
    	}
    }
    /**
     * Runnable interface implementation 
     */
	public void run() {
		m_running = true;
		byte [] buf = new byte[RECV_MESSAGE_BUFSIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		if (uuid != null) {
			notify("ByeBye", address, PORT);
			notify("alive", address, PORT);
		}
		else
			query();
		
		while(m_running) {
			try {
	    		socket.setSoTimeout(m_waiting);
				socket.receive(packet);
				String s = new String(packet.getData());
				if (uuid != null) 
					processMsg(s, packet.getAddress(), packet.getPort());
				else 
					processMsg(s);
	    		packet.setLength(buf.length); // must reset length field!
			}
        	catch(InterruptedIOException t){
        		if (uuid != null)
        			notify("alive", address, PORT);
        		else
        			query();
        	}
        	catch(IOException e) {
        		e.printStackTrace();
        		break;
        	}
		}
		my_print("out of loop\n"); 
	}
	/**
	 * end method
	 */
	public void end() {
		m_running = false;
	}
	protected DatagramSocket socket;
    private InetAddress address;
    private String uuid;
    private String uri;
    private String svr;
    private String sname;
    private int m_waiting;
    private boolean m_running;
    protected Thread m_thread;
   
	/**
	 * main testing program
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		ssdp sv = new ssdp("Pioneer:XMBP", 8969, "ABCD");
		ssdp sd = new ssdp("Pioneer");
		sv.start();
		sd.start();
		
		int msg;
		System.out.print("Enter any message to discover\n");
		do {
			msg = System.in.read();
			if (msg == '\n')
				sd.query();
		}while (msg != 32);
		
		sv.end();
		sd.end();
		sd.query();

		System.out.print("Finish\n");
	}
}
