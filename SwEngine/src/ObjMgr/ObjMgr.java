//******************************************************************************
// Package Declaration
//******************************************************************************
package ObjMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.lang.reflect.Constructor;import java.lang.reflect.Method;
import java.util.*;
import java.io.*;

import ComMgr.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * ObjMgr class is a object handler class for instantiate and store new object
 * at run time. 
 * This class has implemented the static method for convienent using.
 *
 * </pre>
 *******************************************************************************
 * <B> Author: </B><p><pre>
 *
 *  Howard Xu
 *
 * </pre>
 *******************************************************************************
 * <B> Resources: </B><ul>
 *
 * </ul>
 *******************************************************************************
 * <B> Notes: </B><ul>
 *
 * </ul>
 *******************************************************************************
*/
public class ObjMgr extends Hashtable
{
	private static ObjMgr objmgr;
	private Hashtable gc;
	
	// Class help handle
	public static ObjMgr getObjMgr() {
		if (objmgr == null)
			objmgr = new ObjMgr();
		return objmgr;
	}
	
	private ObjMgr() {
		super();
		gc = new Hashtable();
		gc.put("_SYSTEM", GuiMgr.SwClbk.getCallBack());
		gc.put("_SWUTIL", new GuiMgr.SwUtil());
	}

	// Graphic Control Object storage
	public boolean saveGC(String nm, Object ctl) {
		if (gc.containsKey(nm)) return false;
		gc.put(nm, ctl);
		return true;
	}
	public Object getGC(String nm) {
		if (nm == null || nm.length() == 0) return null;
		if (gc.containsKey(nm)) return gc.get(nm);	
		if (LogTrace.getLog().chkLevel(LogTrace.DEBUG)) 
			LogTrace.getLog().Trace(LogTrace.DEBUG, 
				"Can't find " + nm);
		return null;
	}
	public boolean delGC(String nm) {
		Object o = gc.remove(nm);
		return o != null;
	}
	
	// crtClass from class name to get a new object instance  
	public Object crtObject(String cs) {
		return crtObject(cs, new Class[] {}, new Object[] {});
	}
	
	public Object crtObject(String cs, Class [] argc, Object [] argv) {
		Object o = InstanceClass(cs, argc, argv);
		if (o != null) 
			if (LogTrace.getLog().chkLevel(LogTrace.DEBUG)) 
				LogTrace.getLog().Trace(LogTrace.DEBUG, 
					"Object " + cs + " is created &&&&&&&&");
		return o;
	}
	
	public boolean putObject(String cs, Object o) {
		if (this.contains(cs)) return false;
		this.put(cs, o);
		return true;
	}
	
	public boolean delObject(String cs) {
		Object o = this.remove(cs);
		return o != null;
	}
	
	// getClass from class name to get an object instance  
	public Object getObject(String cs) {
		return getObject(cs, new Class[] {}, new Object[] {});
	}
	
	public Object getObject(String cs, Class [] argc, Object [] argv) {
		Object o = null;
		try {
			o = this.get(cs);
			if (o == null) {
				o = InstanceClass(cs, argc, argv);
				this.put(cs, o);
				if (LogTrace.getLog().chkLevel(LogTrace.DEBUG)) 
					LogTrace.getLog().Trace(LogTrace.DEBUG, 
						"Object " + cs + " is added ++++++++");
			}
			else 
			if (LogTrace.getLog().chkLevel(LogTrace.DEBUG)) 
				LogTrace.getLog().Trace(LogTrace.DEBUG, 
					"Object " + cs + " is exist!!!!!!!!");
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					" GetObject " + e.getMessage());
			o = null;
		}
		return o;
	}
	
	// InstanceClass instantiate class
	private Object InstanceClass(String cs, Class [] argc, Object [] argv) {
		try {
//		System.out.println("get Class " + cs);
			Class c = Class.forName(cs);
//		System.out.println("ClassForName " + c.toString() + " is OK");
			Constructor s = null;
			try {
				s = c.getDeclaredConstructor (argc);
			} catch (Exception ee) {
//				System.out.println(cs +" getDeclaredConstructor error on " + ee.getMessage());
				try {
				Constructor [] ss = c.getConstructors();
				s = ss[0];
				}
				catch (Exception eee) {
					if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
						LogTrace.getLog().Trace(LogTrace.EXECPT, 
							cs +" getConstructors error on " + eee.getMessage());
				}
			}
//		System.out.println("getDeclaredConstructor " + s.toString() + " is OK");
			return s.newInstance(argv);	
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					cs + " InstanceClass " + e.getMessage());
			return null;
		}
	}

	// callMethod to invoke the method in the class
	public Object callMethod(Object cs, String ms, Object[] obj) throws Exception
	{
		try
		{
			Class c = cs.getClass();
//			System.out.println("get class " + c.getName() + " method = " + ms);
			Method mm [] = c.getMethods();
			int k = -1;
			for (int i=0; i<mm.length; i++) {
				if(mm[i].getName().equals(ms)){
//					System.out.println("method [ " +i + "] " + mm[i].getName());
					k = i;
				}
			}
//			Method m = c.getMethod(ms.trim(), new Class[] {null});
			Method m = null;
			if (k != -1) m = mm[k];
			else {
				Class[] cls = new Class[obj.length];
				for (int i=0; i<obj.length; i++)
					cls[i] = obj[i].getClass();
				m = c.getDeclaredMethod(ms, cls);
			}
//			System.out.println("get method " + m.getName());
			Class cc[] = m.getParameterTypes();
//			for(int i=0; i<cc.length; i++)
//				System.out.println("Type of Class [" +i + "] " + cc[i].getName());
			if (cc.length == 0)
				return m.invoke(cs, null);
			return m.invoke(cs, obj);	
		}
		catch (Exception e1){throw e1;}
	}

	// callMethod to invoke the method in the class
	public Object callMethod(Object cs, String ms)
	{
		try
		{
			Class c = cs.getClass();
//			System.out.println("get class " + c.getName() + " method = " + ms);
			Method mm [] = c.getMethods();
			int k = -1;
			for (int i=0; i<mm.length; i++)
				if(mm[i].getName().equals(ms)){
//					System.out.println("method [ " +i + "] " + mm[i].getName());
					k = i;
				}
//			Method m = c.getMethod(ms.trim(), new Class[] {null});
			Method m = null;
			if (k != -1) m = mm[k];
			else m = c.getDeclaredMethod(ms, null);
//			System.out.println("get method " + m.getName());
//			Class cc[] = m.getParameterTypes();
//			for(int i=0; i<cc.length; i++)
//				System.out.println("Type of Class [" +i + "] " + cc[i].getName());
			return m.invoke(cs, null);
//			return m.invoke(cs, new Object[] {null});	
		}
		catch (Exception e1)
		{
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"callMethod -->" + e1.getMessage());
			return null;
		}
	}
	
	public Object callMethod(Object cs, String ms, String acs, String aob)
	{
		try
		{
			Class t = Class.forName(acs);
			Class c = cs.getClass();
			Method m = c.getDeclaredMethod(ms, new Class[] {t});
			Object o = this.getGC(aob);
			return m.invoke(cs, new Object[] {o});
		}
		catch (Exception e1)
		{
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"callMethod -->" + e1.getMessage());
			return null;
		}
	}
	/**
	 * main test entry
	 */
	public static void main (String [] args) {
		try {
					
			ObjMgr oM = getObjMgr();		
			System.out.println("I got it -> " + oM.toString());
			
			String cn = "TestMain";
			Class ct = Class.forName("java.lang.String");
			String s = "test.xml";
			Object osf = oM.crtObject(cn, new Class[] {ct}, new Object[] {s});
			
			System.out.println("Sleeping ....");
			Thread.sleep(10000);
			System.out.println ("Wake up and finish !!!");
		}
		catch (Exception e) {
			System.err.println("**** " + e.getMessage());
			e.printStackTrace();
		}
	}
}
