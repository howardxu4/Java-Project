//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
		JPanel panel = new JPanel();
	}
	
        if (cp instanceof JPanel)
			return (JPanel)cp;
		return defPanel(s);
    public void init() {
		String s = null;
		try { s = getParameter("DATAFILE"); }
		if (s == null) s = xmlfile;
			String t = getParameter("TRACELEVEL"); 
		}

 		// set context first for URL resolve
		SwUtil.setContext(this);	
       //Add the panel to this applet.
		getContentPane().add(getPanel(s));
    }

    public void start() {
			LogTrace.getLog().Trace(LogTrace.INFO, 
				"Applet start");
	}
    public void stop() {
			LogTrace.getLog().Trace(LogTrace.INFO, 
				"Applet stop");
	}

    public synchronized void run() {
        while (true) {
			try {
				break;
        } //end thread loop
    }
}