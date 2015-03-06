//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.io.*;
import java.util.Date;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * LogTrace class is a Log and Trace handler class for recording run time information 
 * into a file
 * This class has implemented the static method for convienent using.
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
public class LogTrace
{
	static public final int NONE = 0;
	static public final int INFO = 1;
	static public final int WARNING = 2;
	static public final int ERROR = 4;
	static public final int EXECPT = 8;
	static public final int DEBUG = 16;
	static public final int DEVELOP = 32;
	static public final int ALL = -1;
	static public final int NORMAL = EXECPT + ERROR;
	static public final int DETAIL = NORMAL + WARNING + INFO;
									
	private static final String [] header = new String [] {
		"INFORMATION: ",
		"WARNING: ",
		"ERROR: ",
		"EXCEPTION: ",
		"DEBUG: ",
		"DEVELOP: ",
		"UNKNOWN: "
	};
	
	private static LogTrace logtrace;
	private static String logfile;
	private static int level = NORMAL;
	private PrintWriter out;
	
	/**
	 * LogTrace - LogTrace class constructor. 
	 * @param None.
	 * @return None.
	 */	
	private LogTrace()
	{		
		try {
			File outfile = new File (logfile);
			FileOutputStream fs = new FileOutputStream(outfile); 
			this.out = new PrintWriter(fs, true);      // auto-flush
		}
		catch (Exception e) {
			this.out = new PrintWriter(System.out, true);	// screen
		}
	}
	
	/**
	 * Log - log method. 
	 * @param String - loging information.
	 * @return None.
	 */	
	public void Log(String s)
	{
		this.out.println("[" + new Date().toString() + "] " + s);
	}
	
	/**
	 * chkLevel - check trace level method.
	 * @param int - trace level 
	 * @return true if the level is set.
	 */	
	public boolean chkLevel(int l)
	{
		return ((level & l) != 0);
	}
	
	/**
	 * Trace - trace method.
	 * @param int - trace level 
	 * @param String - trace information.
	 * @return None.
	 */	
	public void Trace(int l, String s)
	{
		if ((level & l) != 0) {
			int i = 0;
			while ((l>>=1) > 0) i++;
			if (i >= header.length) i = header.length -1;
			this.out.println(header[i] + s);
		}
	}
		
	/**
	 * setLevel - set trace level static method. 
	 * @param int - trace level.
	 * @return None.
	 */	
	public static void setLevel(int l)
	{
		level = l;
	}
	
	/**
	 * setLog - set log method. 
	 * @param String - loging file name.
	 * @return None.
	 */	
	public static void setLog(String s)
	{
		logfile = s;
	}
	
	/**
	 * getLogTrace - get LogTrace helper static method. 
	 * @param None.
	 * @return LogTrace instance.
	 */	
	public static LogTrace getLogTrace()
	{
		if (logtrace == null)
			logtrace = new LogTrace();
		return logtrace;
	}
	public static LogTrace getLog() {
		return getLogTrace();
	}
}
