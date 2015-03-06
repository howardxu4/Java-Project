//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.io.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * SysProc class is a System process handler class for the run time requirement 
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
public class SysProc implements IcallBack
{
	/**
	 * SysProc - SysProc class constructor. 
	 * @param None.
	 * @return None.
	 */	
	SysProc() {		
		ComMgr.getComMgr().addListener(ComMgr.SYSTEM, this);		
	}
	
	/**
	 * callBack - implement interface method. 
	 * @param int - type of message.
	 * @param Object - information of message.
	 * @return Object.
	 */	
	public Object callBack(int type, Object info) {
		Object rtn = null;
		Argument argv = (Argument)info;
// 		System.out.println(argv.dump());

		Object obj = argv.getArgument(OBJECT);
		String name = (String)argv.getArgument(NAME);
		
		switch (type) {
		case SYSLOG:
			break;
		case TRACE:
			LogTrace.getLog().Log("SYSTEM received trace from " + name + " " + obj.toString());
			break;
		case NOTIFY:
			LogTrace.getLog().Log("SYSTEM received Notification from " + name + " " + obj.toString());
			break;
		default:
			LogTrace.getLog().Log("SYSTEM received Message from " + name + " " + obj.toString());
		}	
		return rtn;
	}
}
