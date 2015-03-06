package XmppApi;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public interface IXmppMPQ {
	public void M_Func(Message message, XmppBase api);

	public void P_Func(Presence presence);

	public void Q_Func(IQ iq);

}
