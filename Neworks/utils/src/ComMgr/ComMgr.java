//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * ComMgr class is a communication handler class for transform message between
 * different class through indirect way. 
 * This class has implemented the static method for convenient using singleton pattern.
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
public class ComMgr implements Itypes
{
	
	private static Object [] listener = new Object [MAXHOLD];	
	private static ComMgr CMgr;

	/**
	 * getComMgr - get communication manager helper static method. 
	 * @param None.
	 * @return ComMgr instance.
	 */	
	public static ComMgr getComMgr()
	{
		if(ComMgr.CMgr == null) {
			ComMgr.CMgr = new ComMgr();
		}
		return CMgr;
	}
		
	/**
	 * addListener - add listener in method. (added null equal del)
	 * @param int - entry.
	 * @param Object - listener object.
	 * @return None.
	 */	
	public void addListener(int n, Object l)
	{
		if (n < MAXHOLD) listener[n] = l;
	}
	
	/**
	 * sendMsg - send message method. 
	 * @param int - target entry.
	 * @param int - message type.
	 * @param Object - information.
	 * @return Object - respond Object.
	 */	
	public Object sendMsg(int n, int type, Object info)
	{
		if (n >= 0 && n < MAXHOLD && listener[n] != null) {
			try {
				IcallBack target = (IcallBack)listener[n];	
				return target.callBack(type, info);
			}
			catch (Exception e) {}
		}
		return null;
	}
	/**
	 * postMsg - post message method. 
	 * @param int - target entry.
	 * @param int - message type.
	 * @param Object - information.
	 * @return None.
	 */	
	public void postMsg(int n, int type, Object info)
	{
		if (n >= 0 && n < MAXHOLD) {
			try {
				MyThread t = new MyThread(n, type, info);
				t.start();
			}
	 		catch(Exception e){};		
		}
	}
	/**
	 * brcstMsg - broadcast message method. 
	 * @param int - message type.
	 * @param Object - information.
	 * @return None.
	 */	
	public void brcstMsg(int type, Object info)
	{
		for (int i=0; i<MAXHOLD; i++)
			if (listener[i] != null)
				postMsg(i, type, info);
	}
	
	/**
	 * MyThread - inner class for posting message thread. 
	 */	
	private class MyThread extends Thread
 	{
		/**
		 * MyThread - MyThread class constructor. 
		 * @param int - target entry.
		 * @param int - message type.
		 * @param Object - information.
		 */	
		public MyThread(int indx, int type, Object info) 
		{
			super();
			this.indx = indx;
			this.type = type;
			this.info = info;
		}
		/**
		 * run - implement thread run method
		 */	
 		public void run() 
		{
			try {
				if (listener[indx] != null) {
					IcallBack target = (IcallBack)listener[indx];	
					target.callBack(type, info);
				}
			}
			catch (Exception e) {}
		}
		
		private int indx;
		private int type;
		private Object info;
	}
}
