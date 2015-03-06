package ObjMgr;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

import GuiMgr.*;
import ComMgr.*;

public class ObjDemo implements IcallBack {
    
    static String [] imgfls = {
        "JButton", "JColorChooser", "JComboBox", "JFileChooser",
        "JEditorPane", "JDesktop", "JList", "JOptionPane",
        "JProgressBar", "JScrollPane", "JSlider", "JSplitPane",
        "JTabbedPane", "JTable", "ToolTip", "JTree"
                ,"JRadioButton", "JScrollBar",  "JDialog", "JMenu"
    };
    
    static final private String [] im = new String [] {"apple",
    "asparagus",
    "banana",
    "broccoli",
    "cantaloupe",
    "carrot",
    "corn",
    "grapefruit",
    "grapes",
    "kiwi",
    "onion",
    "peach",
    "pear",
    "pepper",
    "pickle",
    "pineapple",
    "raspberry",
    "strawberry",
    "tomato",
    "watermelon" };
    
    static private int idx = 0;
    
    public ObjDemo() {
        ComMgr.getComMgr().addListener(ComMgr.USER01, this);
        initTableModel();
        ObjMgr.getObjMgr().saveGC("ObjDemo", this);
    }
    
    public void init() {}
    
    public Object callBack(int type, Object info) {
        if (type == NOTIFY) init();
        else if (type < UNKNOWN)
            GuiMgr.SwUtil.runSafety(this, "process",
                    new Object[]{new Integer(type), info}, null);
        else {   // user defind message handler
            
        }
        return null;
    }
    /****************************************
     *         system generated methods
     *****************************************/
    public void process(int type, Object info) {
        Argument argv = (Argument)info;
        Object obj = argv.getArgument(OBJECT);
        String name = (String)argv.getArgument(NAME);
        
//		System.out.println(argv.dump());
        switch (type) {
            case TREE:
                String value = (String)argv.getArgument(VALUE);
                System.out.println("Hello, " + value);
                if ((value.indexOf("Berlin") != -1) || (value.indexOf("Kiel") != -1)) {
                    changeDemo("First", 0);
                } else if ((value.indexOf("Cupertino") != -1 ) || (value.indexOf("San Jose") != -1)) {
                    changeDemo("Six", 5);
                }
                break;
            case LABEL:
                Integer I = (Integer)(argv.getArgument(TYPE));
                if (I != null && I.intValue() == ComMgr.MOUSECLICKED) {	// click only
                    changePic( name, obj);
                }
                break;
            case LIST:
                String item = (String)argv.getArgument(VALUE);
                System.out.println(obj.getClass().getName() + " response --> " + name + " " + item);
                break;
            case SLIDER:
                try {
                    changeProgressBar(Integer.parseInt((String)argv.getArgument(VALUE)));
                } catch (Exception e){};
//			System.out.println(obj.getClass().getName() + " response --> " + name);
                break;
            case BUTTON:
                if (name.equalsIgnoreCase("PrevPage")) {
                    changePage(false);
                } else if (name.equalsIgnoreCase("NextPage")) {
                    changePage(true);
                } else if (name.equalsIgnoreCase("end")) {
                    System.exit(0);
                }
            case TOOLBAR:
                if (name.indexOf("Sw") == 0) {
                    System.out.println("changed on " + name + " at here @@@@@@@@ type = " + type);
                    String cmpname = name.substring(2);
                    chgDemoAlso("J"+cmpname, "Sun Demo");
                    chgMyDemo(cmpname);
                    break;
                } else if (name.equalsIgnoreCase("Hi")) {
                    changeToolBar(true);
                    break;
                } else if (name.equalsIgnoreCase("Bye")) {
                    changeToolBar(false);
                    break;
                } else chgDemoAlso(name, "First");
                break;
            case MENUITEM:
                if (name.equalsIgnoreCase("exit"))
                    System.exit(1);
                else if (name.equalsIgnoreCase("Company"))
                    GuiMgr.SwUtil.generateCode();
                else {
                    String s = name;
                    if (name.equalsIgnoreCase("Last")) s = "Six";
                    System.out.println("change TabbedPane to " + s);
                    JTabbedPane tbp = (JTabbedPane)SwUtil.getObject("TabbedPane");
                    if (tbp != null) {
                        for (int i=0; i<tbp.getTabCount(); i++)
                            if (tbp.getTitleAt(i).equalsIgnoreCase(s))
                                tbp.setSelectedIndex(i);
                    }
                }
            case RADIOBUTTONMENUITEM:
                if (name.equalsIgnoreCase("Metal"))
                    SwUtil.setLookAndFeel(0);
                else if (name.equalsIgnoreCase("Windows")) {
                    SwUtil.setLookAndFeel(2);
                } else if (name.equalsIgnoreCase("Macintosh"))
                    SwUtil.setLookAndFeel(3);
                else if (name.equalsIgnoreCase("Linux"))
                    SwUtil.setLookAndFeel(1);
                else if (name.equalsIgnoreCase("On"))
                    SwUtil.swichToolTip(true);
                else if (name.equalsIgnoreCase("Off"))
                    SwUtil.swichToolTip(false);
                break;
            case PANEL:
                System.out.println(name + " mouse=> " + argv.getArgument(TYPE).toString());
                break;
            case PASSWORDFIELD:
                System.out.println(name + " event " + argv.getArgument(TYPE).toString());
                break;
            case TEXTAREA:
                System.out.println(name + " event " + argv.getArgument(TYPE).toString());
                break;
            case NOTIFY:
                System.out.println("User01 received Notified from " + name + " " + obj.toString());
                break;
            default:
                System.out.println("He He, No handler on " + name + " " + obj.toString());
                break;
        }
    }
    
    private void chgMyDemo(String nm) {
        SwTabbedPane o = (SwTabbedPane)ObjMgr.getObjMgr().getGC("TabbedPane");
        chgDemo.getDemo().chgMyDemo(nm, o, 2);
    }
    private void changeDemo(String s, int n) {
        SwTabbedPane o = (SwTabbedPane)ObjMgr.getObjMgr().getGC("TabbedPane");
        chgDemo.getDemo().chgNthDemo(s, o, n);
    }
    private void chgDemoAlso(String nm, String tl) {
        for(int i = 0; i < imgfls.length; i++)
            if (nm.equalsIgnoreCase(imgfls[i])) {
            if (i < imgfls.length - 4) {
                if (nm.charAt(0) == 'J') {
                    nm = nm.substring(1) + "Demo";
                }
                changeDemo(nm, 0);
            } else ;	// do something
            break;
            }
    }
    private void changePic(String nm, Object obj) {
        String value = "resources/images/ImageClub/food/" + im[idx] + ".jpg";
        System.out.println(nm + "-->" + im[idx]);
        ImageIcon II = SwUtil.getImageIcon(value);
        if (II != null) {
            SwLabel l = (SwLabel)obj;
            l.setIcon(II);
            l.setToolTipText("This is " + im[idx]);
        }
        idx = (idx+1) % im.length;
    }
    private void changeProgressBar(int n) {
        SwProgressBar spg = (SwProgressBar)ObjMgr.getObjMgr().getGC("PrgBar");
        if (spg != null) {
            spg.setValue(n);
            spg.setString("%" + n/2);
        }
        SwScrollBar sclb = (SwScrollBar)ObjMgr.getObjMgr().getGC("ScrlBar");
        if (sclb != null) {
            sclb.setValue(n);
        }
    }
    private void changePage(boolean next) {
        SwEditorPane ep = (SwEditorPane)ObjMgr.getObjMgr().getGC("page");
        if (ep != null) {
            String s = ep.getPage().toString();
            int i = s.lastIndexOf(".html");
            if ( i != -1) {
                try {
                    int j = new Integer(s.substring(i-1, i)).intValue();
                    if (next) j = (j % 5) + 1;
                    else if (--j == 0) j = 5;
                    s = s.substring(0,i-1) + j + ".html";
                    System.out.println("change page to " + s);
                    
                    ep.setPage(s);
                } catch (Exception e) {System.out.println(e.getMessage()); }
            } else
                System.out.println( s + i);
        }
    }
    
    public void changeToolBar(boolean b) {
        SwToolBar stb = (SwToolBar)ObjMgr.getObjMgr().getGC("myTB");
        if (stb != null) {
            System.out.println("length - :" + stb.getComponents().length);
            int n = stb.getComponents().length;
            if ( !b && n > 2) {
                for (int i = n-1; i >= 2; i--)
                    stb.remove(i);
                stb.repaint();
            } else if (b && n == 2) {
                for (int i = 0; i < imgfls.length; i++) {
                    String imageFile = "resources/images/toolbar/" + imgfls[i] + ".gif";
                    ImageIcon II = SwUtil.getImageIcon(imageFile);
                    if (II != null) {
                        String tooltip = null;
                        if (i + 4 >= imgfls.length) tooltip = "Unavailabl to action on " + imgfls[i];
                        AbstractAction rtn = new SwToolBarButton(imgfls[i], II, tooltip, "USER01");
                        stb.add(rtn);
                    }
                }
            } else
                System.out.println("Please press another button");
        }
    }
    
    private void initTableModel() {
        ObjMgr.getObjMgr().saveGC("TableModel", new MyTableModel());
    }
    
}
