package XmppApi;

import java.util.*;

import Dispatch.*;
import MsgMgr.*;

public class XmppMgr implements IEreturn{

	static private int m_wait;
	static public int TIMEWAIT = 500;
	static private Map <String, qMesg> mesg;
	
	static void Xmpp_reWait() {
		m_wait = 0;
	}
	private void clearMesg() {
		long now = (new Date()).getTime();
		Iterator <String> itr = mesg.keySet().iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			qMesg qmsg = mesg.get(key);
			if (now - qmsg.time > TLIMIT)
				mesg.remove(key);
		}
	}
	private void setMesg(qMesg qmsg) {
		if (mesg == null) mesg = new HashMap<String, qMesg>();
		mesg.put(qmsg.m_id, qmsg);
	}
	private qMesg getMesg(String Id) {
		qMesg qmsg = null;
		if (mesg != null) {
			qmsg = mesg.get(Id);
			if (qmsg != null)
				mesg.remove(Id);
			if (mesg.size() > 10) 
				clearMesg();
		}
		return qmsg;
	}
	
	int Xmpp_Wait(uInfo vret, String id) {
		if (vret.value == -1) return E_OK;     // no wait answer
		m_wait = 0;
		Object qmsg;
		while(true) {
			qmsg = MsgMgr.getMsgMgr().recvMsg(MsgMgr.R_QUEUE, false);
			if (qmsg == null) qmsg = getMesg(id);
			if (qmsg != null) {
				if (qMesg.class.isInstance(qmsg)) {
					qMesg qm = (qMesg)qmsg;
					if (qm.m_id.equals(id)) {
						switch(vret.type) {
						case 'I':
							vret.value = (new Integer(qm.data)).intValue();
							break;
						case 'P':
						case 'S':
							vret.point = qm.data;
							break;
						case 'G':
						case 'Q':
							vret.value = (new Integer(qm.data)).intValue();
							vret.point = XmppApi.getXmppApi().XMPP_GetData(id);
							break;
						}
						return E_OK;
					}
					else { 
						setMesg((qMesg)qmsg);
						System.out.println("ID no matched: " + id);
					}
				}
			}
			else {
				try { 
					Thread.sleep(TIMEWAIT);
					if (m_wait++ > 50) break;
				}
				catch (Exception e) {}
			}
		}
		System.out.println("Time out!!");
		return E_TOUT;
	}
	int Xmpp_Call(String[] id, uInfo vret) {
		String para = vret.paras; // Xmpp_Encode
		String msg = vret.type + "call" + F_FUNC +  vret.func + F_FMT + vret.fmt + F_PARA + para;
		id[0] = XmppApi.getXmppApi().XMPP_Message( vret.conn, msg, id[0]);
		return E_OK;
	}
	int Xmpp_Bcall(String[] id, uInfo vret) {
		String para = vret.paras; // Xmpp_Encode
		String msg = vret.type + "call" + F_SIZE + vret.size + F_FUNC +  vret.func + F_FMT + vret.fmt + F_PARA + para;
		XmppApi.getXmppApi().XMPP_BData(id, vret.conn, vret.data, vret.size);
		if (id[0].isEmpty()) return E_DATA;
		XmppApi.getXmppApi().XMPP_Message( vret.conn, msg, id[0]);
		return E_OK;
	}

	/**
	 * Ucall - Interface of universal call function
	 * @parameter:  vret - structure of IO buffer
	 * @return      0 success, negative error code [PARA, CONN, CALL, DATA, TOUT...]
	 */
	public int Ucall (uInfo vret){
		int ret = E_CALL;
		// ping first ??
		try {
			String[] id = new String[] { "" };
			switch(vret.type) {
			case 'I':
			case 'P':
			case 'G':
				ret = Xmpp_Call(id, vret);
				if (ret == E_OK) 
					ret = Xmpp_Wait(vret , (String)id[0]);
				break;
			case 'S':
			case 'Q':
				//   data size
				ret = Xmpp_Bcall(id, vret);
				if (ret == E_OK)
					ret = Xmpp_Wait(vret, (String)id[0]);
				break;
			default:
				ret = E_PARA;
				break;
			}
		}
		catch(Exception e) { 
			ret = E_DATA;
			System.out.println(e.getMessage());
		}
		return ret;
	}
}
