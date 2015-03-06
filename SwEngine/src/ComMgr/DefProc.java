//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.util.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * DefProc class is a Default process handler class for the GUI object response  
 * This class has implemented the callback method for internal uses only.
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
public class DefProc implements IcallBack
{
	private static final String [] eventType = {
							"ACTION",
							"TREESELECTION",
							"LISTSELECTION",
							"CHANGE",
							"ADJUSTMENT",
							"MOUSECLICKED",
							"MOUSEENTERED",
							"MOUSEEXIT",
							"MOUSEPRESSED",
							"MOUSERELEASED",
							"FOCUSGAINED",
							"FOCUSLOST",
							"KEYPRESSED",
							"KEYRELEASED",
							"KEYTYPED"
	};	
	/**
	 * DefProc - DefProc class constructor. 
	 * @param None.
	 * @return None.
	 */	
	public DefProc() {
		ComMgr.getComMgr().addListener(ComMgr.CALLBK, this);		
	}
	
	/**
	 * callBack - implement interface method to response GUI object. 
	 * @param int - type of message.
	 * @param Object - information of message.
	 * @return null.
	 */	
	public Object callBack(int type, Object info) {
		Argument argv = (Argument)info;
// 		System.out.println(argv.dump());

		Object obj = argv.getArgument(OBJECT);
		String name = (String)argv.getArgument(NAME);
		int event = UNKNOWN;
		try { event = ((Integer)argv.getArgument(TYPE)).intValue(); } 
		catch(Exception e) {}
		Object value = argv.getArgument(VALUE);
		
		if (value == null) value = "";
		
		switch (type) {
		case TREE:
		case MENUITEM:
		case CHECKBOXMENUITEM:
		case RADIOBUTTONMENUITEM:
		case BUTTON:
		case RADIOBUTTON:
		case CHECKBOX:
		case TOOLBAR:
		case COMBOBOX:
		case LIST:
		case SLIDER:
		case PANEL:
		case PASSWORDFIELD: 
		case TEXTFIELD:
		case TABBEDPANE:
		case SPINER:
		case SCROLLBAR:
		case TEXTAREA:
		case TEXTPANE:
		case LABEL:
			System.out.println(obj.getClass().getName() + " response --> " 
				+ eventType[event] + " on (" +  name + ") value = " + value.toString());			
			break;
		case NOTIFY:
			System.out.println("CALLBK received Notified from " + name + " " + obj.toString());			
			break;
		case UNKNOWN:
		default:
			System.out.println(obj.getClass().getName() + " response --> " 
				+  eventType[event] + " on (" +  name + ") (object type decide by youself )" + type);
			break;
		}
		return null;
	}
}
