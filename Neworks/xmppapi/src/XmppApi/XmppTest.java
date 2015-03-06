package XmppApi;

import java.io.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class XmppTest implements IXmppMPQ {

private String To;
private String Id;

public String getTo() { return To; }
public String getId() { return Id; }

public XmppTest(String T) {
	To = T;
	Id = null;
}
public void M_Func(Message message, XmppBase api)
{
	 if(message.getType().equals(Message.Type.chat) && message.getBody() != null) {
 //           System.out.println("MSG--: " + message.toXML());
		 To = message.getFrom();
		 Id = message.getPacketID();
		 System.out.println("From -- " + To + " : " +  message.getBody());
         System.out.println("input your reply:");
 
     } else 
            System.out.println("I got a message I didn''t understand\n" + message.toXML());	
}

public void P_Func(Presence presence) {
//	System.out.println("PRE--:" + presence.toXML());
	if (presence.toXML().contains("svcs")) 
		System.out.println("Check Services!!");
}

public void Q_Func(IQ iq){
	System.out.println("IQ--:" + iq.toXML());
}

public static void main( String[] args ) {
	// Create a connection to the jabber.org server.
	String U = 	"myppp@jabber.org"; 
	String P = 	"password";
	String R = "CHAT";
	String T = "target@jabber.org";
	if (args.length > 0)
		T= args[0];
	if (args.length > 2) {
		U= args[1];
		P= args[2];
	}
	XmppApi test = XmppApi.getXmppApi();
	XmppTest xt = new XmppTest(T);
	test.SetFunc(xt, "");
	if (test.XMPP_Conn(U, P, R) != 0)
//	if (test.XMPP_Start(U, P, R, "")!=0)
		System.out.println("Sorry, Try again");
	else {
		test.setTo(T);
		test.XMPP_Presence("",1);
//	    test.XMPP_Message(null, "Test", "abc");
		test.XMPP_Chat(null,"hello" , "abc123");
	    test.XMPP_Query(T, "http://jabber.org/protocol/disco#info");
        System.out.println("Waiting for chat...");
        while (true) {
            String reply = "";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            	System.out.println("Input your message:");
            	reply = br.readLine();
                test.XMPP_Chat(xt.getTo() ,reply , xt.getId() );
            	if (reply.contains("bye")) break;
            }
            catch(Exception e) { System.err.println(e.getMessage()); }
        }
        test.XMPP_End();
	}
}

}
