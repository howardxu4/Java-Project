import XmlMgr.*;

public class DemoXML {

	/**
	 * main test entry
	 */
	public static void main(String args[])
	{
		try {
			readXML rd = new readXML();
			rd.setDocument(System.getProperty("user.dir") + "\\" + args[0]);
			rd.trimDocument();
			String s = rd.dumpAll();
			System.out.println(s);
			writeXML wr = new writeXML();
			// generate new XML
			int n = 0;
			wr.setElement(n++, "MyTest", null, true);			
			wr.setElement(n, "First", "1st", true);
			wr.setAttribute(n, "Attr", "Value1");
			wr.setElement(n, "Second", "2nd", true);
			wr.setAttribute(n, "Attr", "Value2");
			wr.setElement(++n, "SubSecond", "2nd_two", true);
			wr.setAttribute(n, "SubAttr", "Value2.2");
			wr.setElement(--n, "Third", "3rd", true);
			wr.setAttribute(n, "Attr", "Value3");
			wr.createXML(System.getProperty("user.dir") + "\\" + "newOutput.xml");
			System.out.println("\n--------------");
			System.out.println("The output file is: " + System.getProperty("user.dir")+ "\\" + "newOutput.xml");
			System.out.println("\n--------------");
			// copy read XML
			wr.setDocument(rd.getDocument());
			wr.createXML(System.getProperty("user.dir") + "\\" + "myOutput.xml");
			System.out.println("\n--------------");
			System.out.println("The output file is: " + System.getProperty("user.dir")+ "\\" + "myOutput.xml");
			System.out.println("\n--------------");
		}
		catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
	}

}