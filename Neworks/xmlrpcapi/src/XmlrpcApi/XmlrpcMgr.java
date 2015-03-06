package XmlrpcApi;

import org.apache.xmlrpc.client.XmlRpcClient;
import Dispatch.*;

public class XmlrpcMgr implements IEreturn{
	
//	Test methods:
	public String myecho(String h) {
		return "Echo:" + h;
	}
	public Integer sum(int x, int y) {
		 return new Integer(x+y);
	}
	public int sub(int x, int y) {
		return x-y;
	}
	public int Get_Icon( Object[] holder, String name ){
        int ret = name.length();
        holder[0] = name;
        System.out.println("Method: Get_Icon be called.");
        return ret;
	}
	public int binary(Object[] info, byte[] data, int size) {
		String s =  new String(data);
		// System.out.println(s + " !!");
		s = "Echo: " + s;
		info[0] = s.getBytes();
		if (info.length > 1)		// for Gbinary call
			info[1] = s.length();
		// System.out.println(s + " size= " + s.length());
		int sz = s.length();
		return sz;
	}
	public Object Gbinary(Object[] info, byte[] data, int size) {
		binary(info, data, size);
		return info;
	}
	
	public int Icall(String func, String fmt, String para) {
		int ret = -1;
		uInfo vret = new uInfo();
        vret.type = 'I';
        vret.func = func;
        vret.fmt = fmt;
        vret.paras = para;
        vret.data = null;
        vret.size = 0;
        vret.value = 0;
        vret.point = null;
        if ( Dispatch.getDispatch().Ucall(vret) >= E_OK)
        	ret = vret.value;		
		return ret;
	}
	public String Pcall(String func, String fmt, String para) {
		String ret = new String();
        uInfo vret = new uInfo();
        vret.type = 'P';
        vret.func = func;
        vret.fmt = fmt;
        vret.paras = para;
        vret.data = null;
        vret.size = 0;
        vret.value = 0;
        vret.point = null;
        if( Dispatch.getDispatch().Ucall(vret) >= E_OK)
        	ret = String.valueOf(vret.point);
		return ret;
	}
	public String Scall(String func, byte[]data, int size, String fmt, String para) {
		String ret = null;
        uInfo vret = new uInfo();
        vret.type = 'S';
        vret.func = func;
        vret.fmt = fmt;
        vret.paras = para;
        vret.data = data;
        vret.size = size;
        vret.value = 0;
        vret.point = null;
        if( Dispatch.getDispatch().Ucall(vret) >= E_OK)
        	ret = String.valueOf(vret.point);
		return ret;		
	}
	public Object[] Gcall(String func, String fmt, String para) {
		Object[] info = null;
        uInfo vret = new uInfo();
        vret.type = 'G';
        vret.func = func;
        vret.fmt = fmt;
        vret.paras = para;
        vret.data = null;
        vret.size = 0;
        vret.value = 0;
        vret.point = null;
        if( Dispatch.getDispatch().Ucall(vret) >= E_OK) {
        	info = new Object[2];
        	info[1] = vret.value;
      		info[0] = vret.point;
        }
		return info;
	}
	public Object[] Qcall(String func, byte[]data, int size, String fmt, String para) {
		Object[] info = null;
		uInfo vret = new uInfo();
        vret.type = 'Q';
        vret.func = func;
        vret.fmt = fmt;
        vret.paras = para;
        vret.data = data;
        vret.size = size;
        vret.value = 0;
        vret.point = null;    
   		int i =  Dispatch.getDispatch().Ucall(vret);
        if( i>= E_OK) {
        	info = new Object[2];
        	info[1] = vret.value;
      		info[0] = vret.point;
        }
		return info; 
	}
	public String mkString(Object o) {
		String s = new String();
	    if (Object[].class.isInstance(o)) {
	    	Object[] a = (Object[])o;
	    	for(int i=0; i < a.length; i++ )
		    	s += a[i];
	    }
	    else if (String.class.isInstance(o)) 
	    	s = (String)o;
	    else if (byte[].class.isInstance(o))
	    	s = new String((byte[])o);
		return s;
	}
	/**
	 * Ucall - Interface of universal call function
	 * @parameter:  vret - structure of IO buffer
	 * @return      0 success, negative error code [PARA, CONN, CALL, DATA, TOUT...]
	 */
	public int Ucall (uInfo vret){
		int ret = E_CALL;
		XmlRpcClient client = XmlrpcApi.getXmlrpcApi().getClient(vret.conn);
		if (client != null) {
			try {
				Object[] ob = null;
				Object Iret = null;
				Object[] info;
				ret = E_OK;				
				switch(vret.type) {
				case 'I':
					ob = new Object[] {vret.func, vret.fmt, vret.paras};
					Iret = client.execute("sv.Icall", ob);
					if (Iret != null) 
						vret.value = ((Integer)Iret).intValue();
					break;
				case 'P':
					ob = new Object[] {vret.func, vret.fmt, vret.paras};
					Iret = client.execute("sv.Pcall", ob);
					vret.point = mkString(Iret);
					break;
				case 'S':
					// ob  data size
					ob = new Object[] {vret.func, vret.data, vret.size, vret.fmt, vret.paras};
					Iret = client.execute("sv.Scall", ob);
					vret.point = mkString(Iret);
					break;
				case 'G':
					// ob  info
					ob = new Object[] {vret.func, vret.fmt, vret.paras};
					Iret = client.execute("sv.Gcall", ob);
					if (Iret != null) {
						info = (Object[])Iret;
						vret.value = ((Integer)info[1]).intValue();
						vret.point = info[0];
					}
					else ret = E_DATA;
					break;
				case 'Q':
					// ob  info data size
					ob = new Object[] {vret.func, vret.data, vret.size, vret.fmt, vret.paras};
					Iret = client.execute("sv.Qcall", ob);
					if (Iret != null) {
						info = (Object[])Iret;
						vret.value = ((Integer)info[1]).intValue();
						vret.point = info[0];
					}
					else ret = E_DATA;
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
		}
		return ret;
	}	
}
