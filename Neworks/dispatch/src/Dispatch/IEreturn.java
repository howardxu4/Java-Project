package Dispatch;

import java.util.*;

public interface IEreturn {
	
	// Global Constance
		static final int E_OK = 0;
		static final int E_PARA = -1;
		static final int E_CONN = -2;
		static final int E_CALL = -3;
		static final int E_DATA = -4;
		static final int E_TOUT = -5;
		
		static final String F_CALL = "call:";
		static final String F_DATA = "Bdata:";
		static final String F_REPLY = "Reply:";
		static final String F_XTAG = "Xtags:";

		static final String F_SIZE = ":;";
		static final String F_FUNC = "::";
		static final String F_FMT = ";:";
		static final String F_PARA = ";;";
				
	// Queue Structure
		static final long TLIMIT = 10000;
		public class qMesg {
			public String m_id;
			public long time;
			public String data;
		}
		
	// Binary Data Structure
		public class bData {
			public ArrayList<byte[]> darray;
			public int size;
			public long time;
		}
		
	// Parameter Structure
		public class uInfo {

		    public String 			conn;     	// IN:  connection string               
		    public char             type;       // ['I'|'P'|'S'|'G'|'Q'] 
		    public String          	func;       // module.method 
		    public String          	fmt;        // %s.. 
		    public String          	paras;      // combined parameters 
		    public byte[]          	data;       // binary data pointer IN 
		    public int    			size;       // data size 

		    public int 				value;      // OUT: return int value                
		    public Object 			point;    	// OUT: return info pointer 
		                                    
		    public int    			status;     // [E_OK|E_CONN|E_CALL|E_DATA|E_TOUT]

		}
		interface ICallback0 {
			public void callback0(cInfo info);
		}
		public class cInfo extends uInfo
		{
	        public Object   uData;          /* user data */
	        ICallback0		clbkFunc; 		/* callback as ClbkFunc pointer*/
	        public Object[]	info;           // binary info pointer OUT 
	        public Object	Return;         /* Return from uCALL */
		}

}
