/*
 * Main.java
 *
 * Created on April 5, 2007, 10:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package swdemo;

import GuiMgr.*;
import ComMgr.*;

/**
 *
 * @author Howard
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main(String s) {
        LogTrace.getLog().setLevel(LogTrace.NORMAL + LogTrace.INFO);
        GuiMgr.SwUtil.setContext(this);
        // start load xml file and let system handle all
        boolean b = GuiMgr.SwUtil.startXML(s);
        if (b == false)
            System.out.println("Sorry, please check your input file " + s);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String s = "XmlData/myDemo.xml";        
        
        if (args.length != 0) s = args[0];
        new Main(s);
    }
    
}
