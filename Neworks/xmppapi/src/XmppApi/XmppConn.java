package XmppApi;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.*;

public class XmppConn  {
	
	class chat extends XmppBase implements IXmppMPQ{
		String URI; 	// callback function
		int	   Qnum; 	// queue number
		chat () {
			SetFunc(this, "");
			URI = "";
			Qnum = -1;
		}
		public void M_Func(Message message, XmppBase api)
		{
			 if(message.getType().equals(Message.Type.chat) && message.getBody() != null) {
		            System.out.println("MSG--: " + message.toXML());
		            if (!message.getFrom().equals(api.XMPP_GetURI()))  // check is self
		            {
		            	//	api.XMPP_Ping(message.getFrom(), 0);
		            	if (this.URI.isEmpty())
			            api.XMPP_Chat(message.getFrom()  ,"I am a Java bot. You said: " + 
			            		message.getBody(), message.getPacketID() );
		            }
		     } else 
		            System.out.println("I got a message I didn''t understand\n" + message.toXML());	
		}
		public void P_Func(Presence presence) {
			System.out.println("PRE--:" + presence.toXML());
		}
		public void Q_Func(IQ iq){
			System.out.println("IQ--:" + iq.toXML());
		}
	}
	
	private Map <Integer, chat> chats;
	
	public XmppConn() {
		chats = new HashMap <Integer, chat>();
	}
	
	public int XMPP_Create(String User, String Pswd, String Resc) {
		int i = 1;
		chat c = new chat();
//		c.SetFunc(null, "");
		c.XMPP_Conn(User, Pswd, Resc);
		while (chats.containsKey(new Integer(i))) i++;
		chats.put(new Integer(i), c);
		return i;
	}
	public void XMPP_UserSet(int index, String URI, int Qnum) {
		chat c = chats.get(new Integer(index));
		if (c != null) {
			c.URI = URI;
			c.Qnum = Qnum;
		}
	}
	public int XMPP_Close(int index) {
		chat c = chats.get(new Integer(index));
		if (c != null) {
			chats.remove(new Integer(index));
			c.XMPP_End();
			return index;
		}
		return -1;
	}
	public String XMPP_Send(int index, String To, String Msg, String Id) {
		chat c = chats.get(new Integer(index));
		if (c != null) 
			return c.XMPP_Chat(To, Msg, Id);
		return null;
	}
	public void XMPP_IQuery(int index, String To, String Msg) {
		chat c = chats.get(new Integer(index));
		if (c != null)
			c.XMPP_Query(To, Msg);
	}
	public void XMPP_IPresence(int index, String To, int N) {
		chat c = chats.get(new Integer(index));
		if (c != null)
			c.XMPP_Presence(To, N);
	}
	public void XMPP_IPing(int index, String To) {
		chat c = chats.get(new Integer(index));
		if (c != null)
			c.XMPP_Ping(To, 0);
	}
	public boolean XMPP_CheckPing(int index) {
		chat c = chats.get(new Integer(index));
		if (c != null)
			return c.XMPP_IsPinged();
		return false;
	}
	public static void main( String[] args ) {
		// Create a connection to the jabber.org server.
		String U = 	"howareyou@jabber.org"; // "myppp@jabber.org"; 
		String P = 	"iamfine";	// "password";
		String R = "CHAT";
		String T = "target@jabber.org";
		if (args.length > 1) {
			U= args[0];
			P= args[1];
			
		}
		if (args.length > 2)
			T= args[2];
		XmppConn test = new XmppConn();
		int i = test.XMPP_Create(U, P, R);
		if (i == 0)
			System.out.println("Sorry, Try again");
		else {
			test.XMPP_IPresence(i, "",1);
		    test.XMPP_Send(i, T, "Test", "abc_ID");
		    test.XMPP_IQuery(i, "howareyou@jabber.org", "http://jabber.org/protocol/disco#info");
	        System.out.println("Press enter to disconnect");
	        try {
	            System.in.read();
	        } catch (IOException ex) {
	            //ex.printStackTrace();
	        }
	        test.XMPP_Close(i);
		}
	}

}
