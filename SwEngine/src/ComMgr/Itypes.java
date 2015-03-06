//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;

/**
 *******************************************************************************
 * <B> interface Description: </B><p><pre>
 *
 * Types is an interface of constance 
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

public interface Itypes
{
	// Object Modules
	public static final int SYSTEM = 0;
	public static final int USER01 = 1;
	public static final int USER02 = 2;
	public static final int USER03 = 3;
	public static final int USER04 = 4;
	public static final int USER05 = 5;
	public static final int USER06 = 6;
	public static final int USER07 = 7;
	public static final int USER08 = 8;
	public static final int USER09 = 9;
	public static final int CALLBK = 15;
	public static final int MAXHOLD = 16;
	
	// Message Types
	
	public static final int APPLET = 0;
	public static final int BORDER = 1;
	public static final int BOX = 2;
	public static final int BUTTON = 3;
	public static final int CHECKBOX = 4;	
	public static final int CHECKBOXMENUITEM = 5;	
	public static final int COLORCHOOSER = 6;
	public static final int COMBOBOX = 7;
	public static final int COMPONENT = 8;
	public static final int DESKTOPPANE = 9;
	public static final int DIALOG = 10;
	public static final int DIMENSION = 11;
	public static final int EDITORPANE = 12;
	public static final int FILECHOOSER = 13;
	public static final int FORMATTEDTEXTFIELD = 14;
	public static final int FRAME = 15;
	public static final int INTERNALFRAME = 16;
	public static final int LABEL = 17;
	public static final int LIST = 18;
	public static final int MENU = 19;
	public static final int MENUBAR = 20;
	public static final int MENUITEM  = 21;
	public static final int PANEL = 22;
	public static final int PASSWORDFIELD = 23;
	public static final int POPUPMENU = 24;
	public static final int PROGRESSBAR = 25;
	public static final int RADIOBUTTON = 26;
	public static final int RADIOBUTTONMENUITEM = 27;
	public static final int RECTANGLE = 28;
	public static final int SCROLLBAR = 29;
	public static final int SCROLLPANE = 30;
	public static final int SLIDER = 31;
	public static final int SPINER = 32;
	public static final int SPLITPANE = 33;
	public static final int TABBEDPANE = 34;
	public static final int TABLE = 35;
	public static final int TEXTAREA = 36;
	public static final int TEXTFIELD = 37;
	public static final int TEXTPANE = 38;
	public static final int TOGGLEBUTTON = 39;
	public static final int TOOLBAR = 40;
	public static final int TREE = 41;
	public static final int WINDOW = 42;
	public static final int UNKNOWN = 43;

	public static final int SYSLOG = 100;
	public static final int TRACE = 101;
	public static final int NOTIFY = 102;
	
	// Object Names
	
	public static String [] ObjName = new String [] {
							"Applet",
							"Border",
							"Box",
							"Button",
							"CheckBox",
							"CheckBoxMenuItem",
							"ColorChooser",
							"ComboBox",
							"Component",
							"DesktopPane",
							"Dialog",
							"Dimension",
							"EditorPane",
							"FileChooser",
							"FormattedTextField",
							"Frame",
							"InternalFrame",
							"Label",
							"List",
							"Menu",
							"MenuBar",
							"MenuItem",
							"Panel",
							"PasswordField",
							"PopupMenu",
							"ProgressBar",
							"RadioButton",
							"RadioButtonMenuItem",
							"Rectangle",
							"ScrollBar",
							"ScrollPane",
							"Slider",
							"Spiner",
							"SplitPane",
							"TabbedPane",
							"Table",
							"TextArea",
							"TextField",
							"TextPane",
							"ToggleButton",
							"ToolBar",
							"Tree",
							"Window"
			// UNKNOWN				

	};
	
	// Event Types
	
	public static final int ACTION = 0;
	public static final int TREESELECTION = 1;
	public static final int LISTSELECTION = 2;
	public static final int CHANGE = 3;
	public static final int ADJUSTMENT = 4;
	public static final int MOUSECLICKED = 5;
	public static final int MOUSEENTERED = 6;
	public static final int MOUSEEXIT = 7;
	public static final int MOUSEPRESSED = 8;
	public static final int MOUSERELEASED = 9;
	public static final int FOCUSGAINED = 10;
	public static final int FOCUSLOST = 11;
	public static final int KEYPRESSED = 12;
	public static final int KEYRELEASED = 13;
	public static final int KEYTYPED = 14;
	
	// Argument Names	
	
	public static final String NAME = new String("NAME");
	public static final String OBJECT = new String("OBJECT");
	public static final String TYPE = new String("TYPE");
	public static final String EVENT = new String("EVENT");
	public static final String VALUE = new String("VALUE");
	
}
