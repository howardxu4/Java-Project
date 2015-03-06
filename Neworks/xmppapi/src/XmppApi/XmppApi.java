package XmppApi;

import java.util.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jivesoftware.smack.util.Base64;
import Dispatch.*;

public class XmppApi extends XmppBase implements IEreturn
{
	private static XmppApi m_api; 
	private Map<String, bData> bdata;
	
	private XmppApi(){
		bdata = new HashMap<String, bData>();
	};
	
	void XMPP_CleanData(String Id) {
		long now = (new Date()).getTime();
		Iterator <String> itr = bdata.keySet().iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			if (!key.equals(Id)) {
				bData bd = bdata.get(key);
				if (now - bd.time > TLIMIT)
					bdata.remove(key);
			}
		}
	}
	byte[] XMPP_GetData(String Id){
		bData bd = bdata.get(Id);
		if (bd != null) {
			bdata.remove(Id);
			byte[] data = new byte[bd.size];
			int sz = 0;
//		It would be better to use a System.arrayCopy for each of the arrays here:
			for (int i=0; i<bd.darray.size(); i++) {
				byte[] da = bd.darray.get(i);
				System.arraycopy(da, 0, data, sz, da.length);
				sz += da.length;
			}			
			return data;
		}
		return null;
	}
	void XMPP_SetData(String Id, byte[]data) {
		if (bdata.size() > 10) XMPP_CleanData(Id);
		bData bd = bdata.get(Id);
		if (bd == null) {
			bd = new bData();
			bd.darray = new ArrayList<byte[]>();
			bd.size = 0;
			bd.time = (new Date()).getTime();
		}
		bd.size += data.length;
		bd.darray.add(data);
		bdata.put(Id, bd);
	}

	public static XmppApi getXmppApi() {
		if (m_api == null)
			m_api = new XmppApi();
		return m_api;
	}
	
	public String XMPP_B64_Encode(byte[] txt, int off, int size, int max)
	{
	    if (size > max) size = max;
		String p = Base64.encodeBytes(txt, off, size);
	    return F_DATA + p;
	}
	public byte[] XMPP_B64_Decode(String str)
	{
		byte[] bret = null;
		if (str.startsWith(F_DATA)){
			bret = Base64.decode(str.substring(F_DATA.length()));
		}
	    return bret;
	}
	public int XMPP_BData(String[] id, String conn,  byte[] data, int size)
	{
		int XMPP_BLOCK = 4096;
	    int st = 0;
	    int ret = E_PARA;
	    while( size > 0) {
	    						//	subset of data : st -- size
	    	String pmsg = XMPP_B64_Encode(data, st, size, XMPP_BLOCK);
	        st += XMPP_BLOCK;
	        size -= XMPP_BLOCK;
	        id[0] = XMPP_Message(conn, pmsg, id[0]);
	    }
	    return ret;
	}
	public String XMPP_Encode(String mp) {
		try {	// US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16
			String tp = URLEncoder.encode(mp, "UTF-8");
			mp = F_XTAG + tp;
		} 
		catch (Exception e){}
		return mp;
	}
	public String XMPP_Decode(String para) {
		try {	// US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16
			para = para.substring(para.indexOf(F_XTAG) + F_XTAG.length());
			para = URLDecoder.decode(para, "UTF-8");
		}
		catch (Exception e){}
		return para;
	}

	public int XMPP_Start(String User, String Pswd, String Resc, String Svcs)
	{
		SetFunc(new XmppRecv(), Svcs);
		return XMPP_Conn(User, Pswd, Resc);
	}
	public void XMPP_End() {		// for reflection getDeclaredMethods
		super.XMPP_End();
	}

}
