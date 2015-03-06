package XmppApi;

import java.util.Date;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import ComMgr.*;
import MsgMgr.*;
import Dispatch.*;

public class XmppRecv implements IXmppMPQ, IEreturn {

	private String M_Process(char type, String msg, String conn, String Id){
		String ret = null;
		int i = msg.indexOf(F_FUNC);
		int j = msg.indexOf(F_FMT);
		int k = msg.indexOf(F_PARA);
		if (i == -1 || j == -1 || k == -1)
			return ret;
		String para = msg.substring(k+2);
		if (para.indexOf(F_XTAG) != -1) 
			para = XmppApi.getXmppApi().XMPP_Decode(para);
		uInfo vret = new uInfo();
		vret.type = type;
		vret.func = msg.substring(i+2, j);
		vret.fmt = msg.substring(j+2, k);
		vret.paras = para;
	    vret.data = null;
	    vret.size = 0;
	    vret.value = 0;
	    vret.point = null;
		switch(type) {
		case 'I':
			if(Dispatch.getDispatch().Ucall(vret) == E_OK)
				ret = "" + vret.value; 
			break;
		case 'P':
			if(Dispatch.getDispatch().Ucall(vret) == E_OK)
				ret = (String)vret.point;
			break;			
		case 'S':
			k = msg.indexOf(F_SIZE);
			vret.size = new Integer(msg.substring(k+2,i)).intValue();
			vret.data = XmppApi.getXmppApi().XMPP_GetData(Id);
			if(Dispatch.getDispatch().Ucall(vret) == E_OK)
				ret = (String)vret.point;
			break;		
		case 'G':
			if(Dispatch.getDispatch().Ucall(vret) == E_OK) {
				String[] id = new String[] {Id};
				XmppApi.getXmppApi().XMPP_BData(id, conn, (byte[])vret.point, vret.value); 
				ret = "" + vret.value;
			}
			break;
		case 'Q':
			k = msg.indexOf(F_SIZE);
			vret.size = new Integer(msg.substring(k+2,i)).intValue();
			vret.data = XmppApi.getXmppApi().XMPP_GetData(Id);			
			if(Dispatch.getDispatch().Ucall(vret) == E_OK) {
				String[] id = new String[] {Id};
				XmppApi.getXmppApi().XMPP_BData(id, conn, (byte[])vret.point, vret.value); 
				ret = "" + vret.value;
			}
			break;
		default:
			break;
		}
		return ret;
	}
	
	public void M_Func(Message message, XmppBase api) {
//		System.out.println("Received: " + message.toXML());
		if(!message.getType().equals(Message.Type.chat) && message.getBody() != null) {
		    String Id = message.getPacketID();
		    String from = message.getFrom();
		    String p = message.getBody();
		    int i = p.indexOf(F_CALL);
		    if (i != -1) {
		    	StringBuffer buf = new StringBuffer();
		    	buf.append(F_REPLY);      	
//System.out.println("F_Call " + Id + " : " + p);
		    	String mp = M_Process(p.charAt(i-1), p,from, Id);
		    	if (mp != null) {
		    		mp = mp.trim();
		    		if(mp.indexOf('<') != -1 || mp.indexOf('\'') != -1) 
		    			mp = XmppApi.getXmppApi().XMPP_Encode(mp);
		    		buf.append(mp);
		    	}
		    	api.XMPP_Message(from, buf.toString(), Id);
		    }
			else if (p.startsWith(F_REPLY)) {
//System.out.println("F_REPLY " + Id + " : " + p);
	            qMesg m = new qMesg();
	            i = p.indexOf(F_REPLY);
	            p = p.substring(i + F_REPLY.length());
	            m.m_id = Id;
	            if (p.indexOf(F_XTAG) != -1) 
	            	p = XmppApi.getXmppApi().XMPP_Decode(p);
	            m.data = p;
	            m.time = (new Date()).getTime();
	            MsgMgr.getMsgMgr().sendMsg(MsgMgr.R_QUEUE, m);
		    }
		    else if (p.startsWith(F_DATA)) {
//System.out.println("F_DATA: " + Id + " " + p.length());		    	
                byte[] data = XmppApi.getXmppApi().XMPP_B64_Decode(p);
                XmppApi.getXmppApi().XMPP_SetData(Id, data);
                XmppMgr.Xmpp_reWait();
		    }
	    } 
		else 
			System.out.println("I got a message I didn''t understand\n" + message.toXML());			
	}
	private void passMsg(String uri, String s)
	{
		String conn = "XmppApi.XmppMgr.Ucall";
		String ss[] = null;
		try {
			ss = s.split(":");
		}
		catch (Exception e) {
			ss = new String[] {"0", ""};
			conn = "";
		}
	
//		System.out.println("Set Service --> " + uri + " " + ss[0] + "  " + ss[1]);
		Argument info = new Argument();
		info.put(ComMgr.URI, uri);	// URI
		info.put(ComMgr.NAME, ss[0]);	// NAME
		info.put(ComMgr.TYPE, ss[1]);	// TYPE
		info.put(ComMgr.VALUE, conn);	// VALUE
		ComMgr.getComMgr().sendMsg(ComMgr.SVCLIST, ComMgr.UPDATE, info);
	}

	public void P_Func(Presence presence) {
//		System.out.println("PRE--:" + presence.toXML());
		String fr = presence.getFrom();
		String st = presence.toXML();
		if (st.contains("svcs")) {
//			System.out.println("Check Services!!\n" + st);
			int i = st.lastIndexOf("<svcs>");
			int j = st.lastIndexOf("</svcs>");
			if (i != -1 && j != -1) {
				String s = st.substring(i+6, j);
				if (s.indexOf(':') != -1) 
					passMsg( fr,  s);
			}
		}
		else {
//			System.out.println("Check Presence!!\n" + st);
			passMsg( fr, null);
		}
	}
    
	public void Q_Func(IQ iq){
		// Nothing to do
	}
}
