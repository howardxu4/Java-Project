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
	public static final int USER00 = 10;
	public static final int USER01 = 11;
	public static final int USER02 = 12;
	public static final int USER03 = 13;
	public static final int USER04 = 14;
	public static final int USER05 = 15;
	public static final int USER06 = 16;
	public static final int USER07 = 17;
	public static final int USER08 = 18;
	public static final int USER09 = 19;

	public static final int MAXHOLD = 20;
	
	// Message Types
	
	public static final int UTIL = 0;
	public static final int DISPATCH = 1;
	public static final int SVCLIST = 2;
	public static final int SSDP = 3;
	public static final int XMLRPCAPI = 4;	
	public static final int XMPPAPI = 5;	
	
	// Object Names
	
	public static String [] ObjName = new String [] {
							"Util",
							"Dispatch",
							"SvcList",
							"Ssdp",
							"XmlrpcApi",
							"XmppApi"
			// UNKNOWN				
	};
	
	// Event Types
	
	public static final int IDLE = 0;
	public static final int NOTIFY = 1;
	public static final int UPDATE = 2;
	public static final int ALERT = 3;
	public static final int RETURN = 4;

	// Argument Names	
	
	public static final String NAME = new String("NAME");
	public static final String OBJECT = new String("OBJECT");
	public static final String TYPE = new String("TYPE");
	public static final String EVENT = new String("EVENT");
	public static final String VALUE = new String("VALUE");
	public static final String URI = new String ("URI");
	
}
