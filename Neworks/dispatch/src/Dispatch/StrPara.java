package Dispatch;

import java.util.*;
public class StrPara {
	enum paraType { NIL,
		INT ,
		STR ,
		FLT ,
		DBL 
	}
	String m_dilimeter;
	paraType m_type;
	String m_format;
	private void setFmt(String fmt) {
		m_format = fmt;
		m_dilimeter = "";
		m_type = paraType.NIL;
	}
	private paraType getType() {
		m_type = paraType.NIL;
		if (m_format != null ) {
			int l = m_format.length();
			for (int i = 0; i < l; i++) {
				switch(m_format.charAt(i)) {
				case 'u':
				case 'd':
				case 'i':
					m_type = paraType.INT;
					break;
				case 's':
					m_type = paraType.STR;
					break;
				case 'f':
					m_type = paraType.FLT;
				case 'e':
				case 'E':
				case 'g':
				case 'G':
					m_type = paraType.DBL;
					break;
				case '%':
				case ' ':
					break;
				default:
					m_dilimeter = m_format.substring(i,i+1);
					m_format = m_format.substring(i+1);
					l = 0;
					break;
				}
			}
			if (l != 0) m_format = null;
		}
		return m_type;
	}
	public String para2Str(Object[] para, String fmt){
		String s = new String();
		setFmt(fmt);
		for(int i=0; i<para.length; i++) {
			s+= m_dilimeter;
			if (getType() == paraType.NIL) break;
			s+= para[i];	
		}
		return s;
	}
	public Object[] str2Para(String spara, String fmt) {
		ArrayList<Object> a = new ArrayList<Object>();
		setFmt(fmt);
		while (spara != null && getType() != paraType.NIL) {
			Object o = null;
			String s = spara;
			int i = spara.indexOf(m_dilimeter);
			if (i > 0)
				s = spara.substring(0, i);
			if(m_type == paraType.DBL) {
				o = new Double(s);
			}
			else if (m_type == paraType.FLT) {
				o = new Float(s);
			}
			else if (m_type == paraType.INT) {
				o = new Integer(s);
			}
			else if (m_type == paraType.STR) {
				o = new String(s);
			}
			a.add(o);
			if (i == -1) 
				spara = null;
			else 
				spara = spara.substring(i+1);
		}
		return a.toArray();
	}
	public Object[] merger(Object[] para, Object...objects ){
		ArrayList<Object> a = new ArrayList<Object>();
		for(Object o: objects) 
			a.add(o);
		if (para != null)
			for (Object o: para) 
				a.add(o);
		return (a.toArray());
	}
	
	public static void main(String[] args) {
		String fmt = "%d,%s;%d";
		Object[] o = new Object[3];
		o[0] = new Integer(123);
		o[1] = "ABC,XYZ";
		o[2] = new Integer(789);
		StrPara sp = new StrPara();
		String s = sp.para2Str(o, fmt);
		System.out.println(s);
		Object [] a = sp.str2Para(s, fmt);
		for(Object aa: a) 
			System.out.println(aa);
		a = sp.merger(a, "Data", 4, o);
		for(Object aa: a) 
			System.out.println(aa);
	}
	
}
