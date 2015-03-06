package Dispatch;

import java.lang.reflect.Method;
import java.util.*;

import MsgMgr.*;
import SvcMgr.*;
	
public class Dispatch implements IEreturn, Runnable{
	
	private int e_status;
	private boolean m_running;	
	static private Dispatch m_dp;
	private SvcList m_svclst;
	private StrPara m_sp;
	public SvcMgr m_sm;
	
	private Dispatch() {
		e_status = E_OK;
		m_running = false;
		m_svclst = SvcList.getSvcList();
		m_sp = new StrPara();
		m_sm = SvcMgr.getSvcMgr();
	}
	public static Dispatch getDispatch() {
		if (m_dp == null)
			m_dp = new Dispatch();
		return m_dp;
	}
	int Ecall() {
		return e_status;
	}
	/**
	 * Icall - Interger return universal call function
	 * @parameter:  func - function name
	 * @parameter:  fmt  - printf format for parameters
	 * @parameter:  ...  - optional parameters
	 * @return      integer value of function returned
	 */
	int Icalls(String func, String fmt, String para) {
		int ret = -1;
        int index = m_svclst.getSvcIndex(func);
        String p = m_svclst.getSvcUfunc(index);
        if (p != null) {
            uInfo vret = new uInfo();
            vret.type = 'I';
            vret.func = func;
            vret.fmt = fmt;
            vret.paras = para;
            vret.data = null;
            vret.size = 0;
            vret.value = 0;
            vret.point = null;
            vret.conn = m_svclst.getSvcConn(index);
            e_status = Vcall(p, vret);
         // e_status = sm.callSvcFunc(p, vret);
            if (e_status >= E_OK)
                    ret = vret.value;
        }
//        else
//                DebugPrintf(DispGetHdl(), T_ERROR, "service not available for method: %s\n", func);
		return ret;
	} 
	public int Icall(String func, String fmt, Object... objects) {
		String para = "";
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		else fmt = "";
		return Icalls(func, fmt, para);
	}
	/**
	 * Pcall - pointer return universal call function
	 * @parameter:  func - function name
	 * @parameter:  fmt  - printf format for parameters
	 * @parameter:  ...  - optional parameters
	 * @return      pointer of string of function returned
	 */
	String Pcalls(String func, String fmt, String para) {
		String ret = new String();
        int index = m_svclst.getSvcIndex(func);
        String p = m_svclst.getSvcUfunc(index);
        if (p != null) {
            uInfo vret = new uInfo();
            vret.type = 'P';
            vret.func = func;
            vret.fmt = fmt;
            vret.paras = para;
            vret.data = null;
            vret.size = 0;
            vret.value = 0;
            vret.point = null;
            vret.conn = m_svclst.getSvcConn(index);
            e_status = Vcall(p, vret);
         // e_status = sm.callSvcFunc(p, vret);
            if (e_status >= E_OK)
            	ret = String.valueOf(vret.point);
        }
		return ret;
	}
	public String Pcall(String func, String fmt, Object... objects) {
		String para = "";
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		else fmt = "";
		return Pcalls(func, fmt, para);
	}
	/**
	 * Scall - Set cdata universal call function
	 * @parameter:  func - function name
	 * @parameter:  data  - cdata
	 * @parameter:  size -  cdata size
	 * @parameter:  fmt  - printf format for parameters
	 * @parameter:  ...  - optional parameters
	 * @return      pointer string of function returned
	 */
	String Scalls(String func, byte[]data, int size, String fmt, String para) {
		String ret = null;
        int index = m_svclst.getSvcIndex(func);
        String p = m_svclst.getSvcUfunc(index);
        if (p != null) {
            uInfo vret = new uInfo();
            vret.type = 'S';
            vret.func = func;
            vret.fmt = fmt;
            vret.paras = para;
            vret.data = data;
            vret.size = size;
            vret.value = 0;
            vret.point = null;
            vret.conn = m_svclst.getSvcConn(index);
            e_status = Vcall(p, vret);
         // e_status = sm.callSvcFunc(p, vret);    
            if (e_status >= E_OK)
            	ret = String.valueOf(vret.point);
        }		
		return ret;
	}
	public String Scall(String func, byte[]data, int size, String fmt, Object... objects) {
		String para = "";
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		else fmt = "";
		return Scalls(func, data, size, fmt, para);
	}
	/**
	 * Gcall - Get cdata universal call function
	 * @parameter:  func - function name
	 * @parameter:  info - hold pointer for cdata returned
	 * @parameter:  fmt  - printf format for parameters
	 * @parameter:  ...  - optional parameters
	 * @return      size of cdata of function returned
	 */
	int Gcalls(String func, Object[]info, String fmt, String para) {
		int ret = -1;
        int index = m_svclst.getSvcIndex(func);
        String p = m_svclst.getSvcUfunc(index);
        if (p != null) {
            uInfo vret = new uInfo();
            vret.type = 'G';
            vret.func = func;
            vret.fmt = fmt;
            vret.paras = para;
            vret.data = null;
            vret.size = 0;
            vret.value = 0;
            vret.point = null;
            vret.conn = m_svclst.getSvcConn(index);
            e_status = Vcall(p, vret);
         // e_status = sm.callSvcFunc(p, vret);
            if (e_status >= E_OK)
            	ret = vret.value;
          		info[0] = vret.point;
        }
		return ret;
	}
	public int Gcall(String func, Object[]info, String fmt, Object... objects) {
		String para = "";
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		else fmt = "";
		return Gcalls(func, info, fmt, para);
	}
	/**
	 * Qcall - Get cdata with input cdata universal call function
	 * @parameter:  func - function name
	 * @parameter:  info - hold pointer for cdata returned
	 * @parameter:  data - cdata
	 * @parameter:  size - cdata size
	 * @parameter:  fmt  - printf format for parameters
	 * @parameter:  ...  - optional parameters
	 * @return      size of info of function returned
	 */
	int Qcalls(String func, Object[]info, byte[]data, int size, String fmt, String para) {
		int ret = 0;
        int index = m_svclst.getSvcIndex(func);
        String p = m_svclst.getSvcUfunc(index);
        if (p != null) {
            uInfo vret = new uInfo();
            vret.type = 'Q';
            vret.func = func;
            vret.fmt = fmt;
            vret.paras = para;
            vret.data = data;
            vret.size = size;
            vret.value = 0;
            vret.point = null;
            vret.conn = m_svclst.getSvcConn(index);
            e_status = Vcall(p, vret);
         // e_status = sm.callSvcFunc(p, vret);
            if (e_status >= E_OK)
            	ret = vret.value;
          		info[0] = vret.point;
        }
		return ret;
	}
	public int Qcall(String func, Object[]info, byte[]data, int size, String fmt, Object... objects) {
		String para = "";
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		else fmt = "";
		return Qcalls(func, info, data, size, fmt, para); 
	}
	private int Mucall(cInfo info) {
		switch(info.type){
		case 'I':
			info.Return = Icalls(info.func, info.fmt, info.paras);
			break;
		case 'P':
			info.Return = Pcalls(info.func, info.fmt, info.paras);
			break;
		case 'S':
			info.Return = Scalls(info.func, info.data, info.size, info.fmt, info.paras);
			break;
		case 'G':
			info.Return = Gcalls(info.func, info.info, info.fmt, info.paras);
			break;
		case 'Q':
			info.Return = Qcalls(info.func, info.info, info.data, info.size, info.fmt, info.paras);
			break;
		}
		return (info.status = Ecall());
	}
	public void run() {
		m_running = true;
		cInfo info = null;
		while(m_running) {
			// receive para from queue
			info = (cInfo)(MsgMgr.getMsgMgr().recvMsg(MsgMgr.I_QUEUE, true));	
			Mucall(info);
			info.clbkFunc.callback0(info);
		}
	}
	private int checkPara(cInfo para) {
		if (para.func == null) return E_CALL;
		switch(para.type) {
		case 'I':
		case 'P':
		case 'G':
			break;
		case 'S':
		case 'Q':
			if (para.data == null) return E_DATA;
			break;
		default:
			return E_PARA;
		}
		if (para.fmt == null) para.fmt = ""; 
		para.Return = 0;
		return E_OK;
	}
	/**
	 * Mcalls - using message to invoke calls make it asynchronized
	 * @prarmeter:  info - structure of CINFO carrying all parameters
	 * @return:   0 success; otherwise fails
	 */
	int Mcall(cInfo info) {
		int ret = checkPara(info);
		if (ret == E_OK) {
			if (m_running == false)
				new Thread(this).start();
			// send info to queue
			if (info.clbkFunc == null)
				ret = Mucall(info);
			else 
				MsgMgr.getMsgMgr().sendMsg(MsgMgr.I_QUEUE, info);
		}
		return ret;
	}
	public cInfo Mcalls(char type, String func, String fmt, String paras, byte[] data, int size, ICallback0 clbk, Object uData) {
		cInfo info = new cInfo();
		info.type = type;
		info.func = func;
		info.fmt = fmt;
		info.paras = paras;
		info.data = data;
		info.size = size;
		info.info = new Object[] {0};
		info.clbkFunc = clbk;			
		info.uData = uData;		
		info.status = Mcall(info);
		return info;
	}
	public cInfo Mcalls(char type, String func, String fmt, String paras, byte[] data, int size) {
		return Mcalls(type, func, fmt, paras, data, size, null, null);
	}
	public cInfo Mcalls(char type, String func, String fmt, String paras, ICallback0 clbk, Object uData) {
		return Mcalls(type, func, fmt, paras, null, 0, clbk, uData);
	}
	public cInfo Mcalls(char type, String func, String fmt, String paras) {
		return Mcalls(type, func, fmt, paras, null, 0, null, null);
	}
	public String getParas(String fmt, Object... objects) {
		String para = null;
		if (fmt != null && fmt.charAt(0) != 0) {
	          ArrayList <Object> a = new ArrayList<Object> ();
	          for (Object d : objects )
	        	  a.add(d);
	          para = m_sp.para2Str(a.toArray(), fmt);
		}
		return para; 
	}
	
	/**
	 * Vcall - virturl function call routine
	 * @Parameter:  func - function name
	 * @paramter:   ...  - optional parameters
	 * @return:     negative fails, otherwise method designed
	 * @note:       the last parameter mus be NULL
	 */
	public int Vcall(String func, Object...objects ) {
		int ret = 0;
		try {
			ArrayList <Object> a = new ArrayList<Object> ();
	        for (Object d : objects )
	        	if(d == null) break;
	        	else a.add(d);
	        m_sm.callSvcFunc(func, a.toArray());
		}
		catch (Exception e) { 
			System.out.println("Vcall Exception: " + e.getMessage()); 
		}
		return ret;
	}
	/**
	 * Ucall - Interface of universal call function
	 * @parameter:  vret - structure of IO buffer
	 * @return      0 success, negative error code [PARA, CONN, CALL, DATA, TOUT...]
	 */
	public int Ucall (uInfo vret) {
		int ret = E_CALL;
		Object[] o = m_sm.getSvcFunc(vret.func);
		if (o != null) {			
			try {
				Object[] ob = m_sp.str2Para(vret.paras, vret.fmt);
				Object Iret = null;
				Object[] info = new Object[1];
				ret = E_OK;
				switch(vret.type) {
				case 'I':
					Iret = ((Method)o[0]).invoke(o[1], ob);
					if (Iret != null) 
						vret.value = ((Integer)Iret).intValue();
					break;
				case 'P':
					vret.point = ((Method)o[0]).invoke(o[1], ob);
					break;
				case 'S':
					// ob  data size
					ob = m_sp.merger(ob, vret.data, vret.size);
					vret.point = ((Method)o[0]).invoke(o[1], ob);
					break;
				case 'G':
					// ob  info
				{	Object k = info;
					ob = m_sp.merger(ob, k);
					Iret = ((Method)o[0]).invoke(o[1], ob);
					if (Iret != null) {
						vret.value = ((Integer)Iret).intValue();
						vret.point = info[0];
					}
				}
					break;
				case 'Q':
					// ob  info data size
					ob = m_sp.merger(ob, info, vret.data, vret.size);
					Iret = ((Method)o[0]).invoke(o[1], ob);
					if (Iret != null) {
						vret.value = ((Integer)Iret).intValue();
						vret.point = info[0];
					}
					break;
				default:
					ret = E_PARA;
					break;
				}
			}
			catch(Exception e) { e.printStackTrace(); ret = E_DATA; }
		}
		return ret;
	}
	public static void main(String[] args) {
		Dispatch dp = getDispatch();
		System.out.println("OK, Java World");
		Object[] ob = new Object[1];
		ob[0] = "Good bye";
		dp.m_sm.callSvcFunc("SwEngine:ComMgr.LogTrace.Log", ob);
		String [] ss = new String[1];
		ss[0] = "demoapi.xml";
		ob[0] = ss;
		dp.m_sm.setModule("SvcMgr.SvcMgr", dp.m_sm);
		dp.m_sm.callSvcFunc("SvcMgr.SvcMgr.main", ob);
	}
}
