package SvcMgr;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class SvcMgr {
	
	class Module {
		private Object instance;
		private Map <String, Method > methods;
		Module(Object inst) {
			instance = inst;
			Class <?> cls =inst.getClass();
			setMethods(cls);
		}
		Module(String clsname, ClassLoader cl) throws Exception{
			try {
			Class<?> cls = (cl==null) ?Class.forName(clsname):
				cl.loadClass(clsname);

			instance = cls.newInstance();
			setMethods(cls);
			}
			catch(Exception e) {
//				System.out.println("No default constructor in " + clsname);
				try {
					int i = clsname.lastIndexOf('.');
					Class<?> cls = (cl==null) ?Class.forName(clsname):
						cl.loadClass(clsname);
					Method m = cls.getDeclaredMethod("get"+clsname.substring(i+1), new Class[0]);
					instance = m.invoke(null, new Object[0]);
					setMethods(cls);
				}
				catch(Exception ee) {
					System.out.println("No get" + clsname + " factory method");
					throw ee;
				}
			}
		}
		private void setMethods(Class<?> cls) {
//			System.out.println(" ****** " + cls.getName());
			Method[] mtds = cls.getDeclaredMethods();
			methods = new HashMap<String, Method> ();
			for (Method mtd: mtds)
				methods.put(mtd.getName(), mtd);
		}
		public Method getMethod(String name) {
			return methods.get(name);
		}
		public Object getInstance() {
			return instance;
		}
	}
	
	private Map <String, ClassLoader> packages;
	private Map <String, Module> modules;
	private static SvcMgr sm;
	private SvcMgr() {
		modules = new HashMap <String, Module> ();
		packages = new HashMap <String, ClassLoader> ();
	}
	public static SvcMgr getSvcMgr() {
		if (sm == null) 
			sm = new SvcMgr();
		return sm;
	}
	
	public ClassLoader getClsLoader(String jarfile) {
		try {
			File file = new File(jarfile); 
			URL url = file.toURI().toURL();
			URL[] urls = new URL[]{url}; 
			return new URLClassLoader(urls);
		}
		catch(Exception e){}
		return null;
	}
	public void setClsLoader(String name){
		try {
			String pf = System.getProperty("user.dir") + "/" + "myProperties.txt";
			FileInputStream propFile = new FileInputStream(pf);
			Properties p = new Properties();
			p.load(propFile);
			String jarfile = p.getProperty(name);
			ClassLoader cl = getClsLoader(jarfile);
			if (cl != null)
				packages.put(name, cl);
			System.out.println("Load Jar: " + jarfile);
		}
		catch (Exception e){}
	}
	public boolean setModule(String name) {
		int i = name.indexOf(':');
		if (i!= -1) {
			String jar = name.substring(0, i);
			name = name.substring(i+1);
			if(packages.containsKey(jar) == false)
				setClsLoader(jar);
			ClassLoader cl = packages.get(jar);
			if (cl == null) return false;
			if (modules.containsKey(name) == false) {
				try {
					Module m = new Module(name, cl);
					modules.put(name, m);
				}
				catch(Exception e) { 
					System.out.println("No module " + name + " is found");
					return false; 
				}
			}
			return true;
		}
		return setModule(name, null); 
	}
	public boolean setModule(String name, Object inst) {
		if (modules.containsKey(name) == false) {
			try {
				Module m = null;
				if (inst == null)
					m = new Module(name, null);
				else
					m = new Module(inst);
				modules.put(name, m);
			}
			catch(Exception e) { 
				System.out.println("No module " + name + " is found");
				return false; 
			}
		}
		return true;
	}
	private void dumpModule(Module m) {
		Set <String> mtds = m.methods.keySet();
		Iterator <String> i = mtds.iterator();
		while (i.hasNext()) 
			System.out.println(" --  " + i.next());
	}
	void dump() {
		Set <Map.Entry<String, Module> > entrys = modules.entrySet();
		Iterator <Map.Entry<String, Module>> i = entrys.iterator();
		while (i.hasNext()) {
			Map.Entry<String, Module> entry = i.next();
			System.out.println(entry.getKey());
			dumpModule(entry.getValue());
		}
	}
	private Object[] getMethod(String cls, String name) {
		Module m = modules.get(cls);
		Method mtd = m.getMethod(name);
		if (mtd != null) {
			Object[] ob = new Object[2];
			ob[0] = mtd;
			ob[1] = m.getInstance();
			return ob;
		}
		System.out.println("No name " + name + " in " + cls + " is found!");
		return null;
	}
	public Object[] getSvcFunc(String func) {
		int i = func.lastIndexOf('.');
		if (i != -1) {
			String cls = func.substring(0, i);
			String name = func.substring(i+1);
//	System.out.println(cls + " -- " + name);		
			if (setModule(cls)) {
				i = cls.indexOf(':');
				if (i != -1) cls = cls.substring(i+1);
				return getMethod(cls, name);
			}
		}
		return null;
	}	
	public Object callSvcFunc(String func, Object[] para) {
		Object[] o = getSvcFunc(func);
		if (o != null) {
			try {
				return ((Method)o[0]).invoke(o[1], para);
			}
			catch(Exception e) { e.printStackTrace(); }
		}
		return null;
	}
	
	public static void main(String[] args) { 
		SvcMgr sm = SvcMgr.getSvcMgr();
		sm.setModule(sm.getClass().getName(), sm);
		sm.setModule("ComMgr.ComMgr", null);
		sm.setModule("MsgMgr.MsgMgr", null);
		sm.setModule("SwEngine:XmlMgr.writeXML");
		sm.setModule("SwEngine:ObjMgr.ObjMgr");
		Object[] o = sm.getSvcFunc("ComMgr.LogTrace.Log");
		Object[] ob = new Object[1];
		ob[0] = "hello world";
		if (o != null) { System.out.println("Get the method!");
			try {
				((Method)o[0]).invoke(o[1], ob);
			}
			catch(Exception e) {}
		}
//		sm.dump();
		ob[0] = "Good bye";
		sm.callSvcFunc("ComMgr.LogTrace.Log", ob);
		String [] ss = new String[1];
		ss[0] = "demoapi.xml";
		ob[0] = ss;
//		sm.callSvcFunc("XmlMgr.readXML.main", ob);
		sm.callSvcFunc("MsgMgr.MsgTest.main", ob);
		Object[] para = new Object[2];
		para[0] = 1;
		para[1] = "Hi";
		sm.callSvcFunc("MsgMgr.MsgMgr.sendMsg",para);
		para[1] = false;
		Object r = sm.callSvcFunc("MsgMgr.MsgMgr.recvMsg", para);
		System.out.println(r);
		sm.callSvcFunc("ObjMgr.ObjMgr.main", ob);
		/*
		ob[0] = new Integer(8081);
		sm.setModule("XmlrpcApi");
		sm.callSvcFunc("XmlrpcApi.XmlrpcApi.server", ob);
		*/
	}
}
