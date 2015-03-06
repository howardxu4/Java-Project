package XmppApi;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;

public class XmppBase implements PacketListener { 
	private XMPPConnection conn;
	private String To;
	private String svcs;
	private boolean isPinged;
	private IXmppMPQ func;

	public void SetFunc(IXmppMPQ Func, String Svcs) {
		func = Func;
		svcs = Svcs;
	}
	private void M_Func(Message message)
	{
		if (func != null)
			func.M_Func(message, this);
		else 
			System.out.println("Received: " + message.toXML());
	}
	private void P_Func(Presence presence)
	{
		if (func != null) {
			if (!presence.getFrom().equals(XMPP_GetURI()))  // check is self
				func.P_Func(presence);
		}
		else 
			System.out.println("Precense:" + presence.toXML());
	}
	private void Q_Func(IQ iq)
	{
		if (func != null)
			func.Q_Func(iq);
		else 
			System.out.println("IQ:" + iq.toXML());
	}

	protected class myIQ extends IQ
	{
		public myIQ(String To, int Resp){
			if (Resp != 0) 
				setType(IQ.Type.RESULT);
			else
				setType(IQ.Type.GET);
			if (To != null && To !="")
			setTo(To);
		}
		public void setMsg(String Msg)
		{
				msg = Msg;
		}
	    public String getChildElementXML() {
	            return msg;
	    }
	    private String msg;
	}
	private void handlePing(IQ iq)
	{
		String s = iq.getPacketID();
		if (s != null && s.equals("ping")) {
			if (iq.getType() == IQ.Type.RESULT)
				isPinged = true;
			else if (iq.getType() == IQ.Type.GET) 
				XMPP_Ping (iq.getTo(), 1);
		}
	}
	public void processPacket(Packet packet) 
	{
		String msg = packet.toXML();

		if (msg.startsWith("<message")) 
			M_Func((Message)packet);
		else if (msg.startsWith("<presence"))
			P_Func((Presence)packet);
		else if (msg.startsWith("<iq")) {
			handlePing((IQ)packet);
			Q_Func((IQ)packet);
		}
		else
			System.out.println("All: " + msg);
	}
	public int XMPP_Presence(String To, int N)
	{            	
		Presence presence = new Presence(Presence.Type.available);
		if (To != null && To != "")
			presence.setTo(To);
		if (N == 0)
			presence.setType(Presence.Type.unavailable);
		presence.setPriority(1);
		if (N != 0 && svcs != null && !svcs.isEmpty()) {
			DefaultPacketExtension extension = new DefaultPacketExtension("x", null);
			extension.setValue("svcs", svcs);
			presence.addExtension(extension);
		}
		conn.sendPacket(presence);
	    return 0;
	}
	public String XMPP_Message(String To, String Msg, String Id)
	{
	    if (To == null || To == "") To = this.To;
	    Message msg = new Message(To);
	    msg.setBody(Msg);
	    if (Id == null || Id.isEmpty())
	    	Id = Message.nextID();
	    msg.setPacketID(Id);
//	System.out.println("Send: " + Id + "  " + Msg)	 ;   
	    conn.sendPacket(msg);
	    return Id;
	}
	public String XMPP_Chat(String To, String Msg, String Id)
	{
	    if (To == null || To == "") To = this.To;
	    Message msg = new Message(To, Message.Type.chat);
	    msg.setBody(Msg);
	    if (Id == null || Id.isEmpty())
	    	Id = Message.nextID();
	    msg.setPacketID(Id);
	    conn.sendPacket(msg);
	    return Id;
	}
	int XMPP_Query(String To, String Msg)
	{
		myIQ iq = new myIQ(To, 0); 
		iq.setMsg("<query xmlns=\"" + Msg + "\"></query>");
        conn.sendPacket(iq);	
//	System.out.println("Send:" + iq.toXML());
		return 0;
	}
	int XMPP_Ping (String To, int Resp) 
	{
		myIQ ping = new myIQ(To, Resp);
		if (Resp == 0)
			ping.setMsg("<ping xmlns=\"urn:xmpp:ping\"></ping>");
		isPinged = false;
		conn.sendPacket(ping);
		return 0;
	}
	boolean XMPP_IsPinged()
	{
		return isPinged;
	}
	void setTo(String To)
	{
	    this.To = To;
	}
	private String getResc(String Resc) {
		Resc = String.format("%s_%02X", Resc, new Random().nextInt(255));
		return Resc;
	}

	public int XMPP_Conn(String User, String Pswd, String Resc)
	{
		int rtn = 0;
		String S = "jabber.org";
		int i = User.indexOf('@');
		if (i > 0) S = User.substring(i+1);
		conn = new XMPPConnection(S);
		try {
    		conn.connect();
//    		System.out.println("Connected to " + conn.getHost());
            try {
            	conn.login(User, Pswd, getResc(Resc));	
//            	System.out.println("Logged in as " + conn.getUser());
            	conn.addPacketListener(this, new NotFilter(new OrFilter()) );
            	XMPP_Presence("",1);
            } catch (XMPPException ex) {
                //ex.printStackTrace();
                System.out.println("Failed to log in as " + conn.getUser());
                rtn = 2;
            }
        } catch (XMPPException ex) {
            //ex.printStackTrace();
            System.out.println("Failed to connect to " + conn.getHost());
            rtn = 1;
        }
		return rtn;
	}

	public void XMPP_End()
	{
		if (conn != null)
			conn.disconnect();
	}
	public String XMPP_GetURI() {
		if (conn != null)
			return conn.getUser();
		return "";
	}
	
}

