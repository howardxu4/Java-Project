import GuiMgr.*;
import ComMgr.*;

public class hello
{
	public hello(String s) {
		// set code base for all file reference		
		GuiMgr.SwUtil.setContext(this);
		LogTrace.getLog().setLevel(-1); //LogTrace.DETAIL + LogTrace.DEBUG);
		// start load xml file and let system handle all		
		boolean b = GuiMgr.SwUtil.startXML(s);	
		if (b == false) 
			System.out.println("Sorry, please check your input file " + s);
	}
	public static void main(String args[])
	{
		String s = "SP.xml";
		if (args.length != 0) s = args[0];
		new hello(s);
	}	
}
