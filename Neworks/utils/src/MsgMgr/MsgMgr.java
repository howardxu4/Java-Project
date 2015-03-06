package MsgMgr;

import java.util.*;

public class MsgMgr {
	class MsgQueue {
		private Queue <Object> queue;
		
		MsgQueue(){
			queue = new LinkedList<Object> ();
		}
		protected void push(Object o) {
			synchronized(queue) {
				queue.add(o);
				queue.notify();
			}
		}
		protected Object pop(boolean w) {
			Object o = null;
			try {
			    synchronized(queue) {
			    	while (w && queue.isEmpty()) 
			    		queue.wait();
			    	if (!queue.isEmpty()) 
			    		o = queue.poll();
			    }
			}
			catch(Exception e) { System.out.println("Exception " + e.getMessage()); }
		    return o;
		}
		protected void clean() {
			synchronized(queue) {
				queue.clear();
				queue.notifyAll();
			}
		}
		protected int size() {
		    return queue.size();
		}
	}
	// Common Queue	
	// 0		SYSTEM P/S
	public static final int I_QUEUE = 1;		// IN dispatch
	public static final int C_QUEUE = 2;		// CHAT ims
	public static final int R_QUEUE = 3;		// RESP xmpp
	public static final int M_QUEUE = 4;		// MESG 

	private final int MAXQNUM = 16;
	private static MsgQueue[] mq ;
	private static MsgMgr mm;
	private MsgMgr() {
		mq = new MsgQueue[MAXQNUM];
	}
	static public synchronized MsgMgr getMsgMgr() {
		if (mm == null) 
			mm = new MsgMgr();
		return mm;
	}
	private synchronized int checkQID(int n) {
		n = n % MAXQNUM;
		if (mq[n] == null) 
			mq[n] = new MsgQueue();
		return n;
	}
	public void sendMsg(int n, Object o) {
		n = checkQID(n);
		mq[n].push(o);
	}
	public Object recvMsg(int n, boolean w) {
		n = checkQID(n);
		return mq[n].pop(w);
	}
	public void cleanMsg(int n){
		n = checkQID(n);
		mq[n].clean();
	}
	public int getNumMsg(int n) {
		n = checkQID(n);
		return mq[n].size();
	}

}

