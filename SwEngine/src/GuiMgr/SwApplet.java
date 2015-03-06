//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;import java.net.*;

import GuiMgr.*;
import ComMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwApplet is a class of JApplet to support running on internet 
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
public class SwApplet extends JApplet implements Runnable
{
	static String xmlfile = "XmlData/myComboBoxDemo.xml";

	private JPanel defPanel(String s) {
		JPanel panel = new JPanel();		JLabel label = new JLabel("The file " + s + " is not configured as Panel");		panel.add(label);		return panel;
	}
		public JPanel getPanel(String s) {			Object cp = SwUtil.chkXmlFile(s);
        if (cp instanceof JPanel)
			return (JPanel)cp;
		return defPanel(s);	}	
    public void init() {
		String s = null;
		try { s = getParameter("DATAFILE"); }		catch (Exception e) {}
		if (s == null) s = xmlfile;		try {
			String t = getParameter("TRACELEVEL"); 			if (t != null) LogTrace.getLog().setLevel(Integer.parseInt(t)); 
		}		catch (Exception e) {}

 		// set context first for URL resolve
		SwUtil.setContext(this);	
       //Add the panel to this applet.
		getContentPane().add(getPanel(s));
    }

    public void start() {		if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
			LogTrace.getLog().Trace(LogTrace.INFO, 
				"Applet start");
	}
    public void stop() {		if (LogTrace.getLog().chkLevel(LogTrace.INFO)) 
			LogTrace.getLog().Trace(LogTrace.INFO, 
				"Applet stop");
	}

    public synchronized void run() {
        while (true) {
			try {				wait();
				break;			}			catch (InterruptedException e) { break; }
        } //end thread loop
    }
}