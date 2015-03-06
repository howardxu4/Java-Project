package MsgMgr;

public class MsgTest implements Runnable {
	MsgMgr m ;
	public MsgTest() {
		m = MsgMgr.getMsgMgr();
	}
	public static void main (String[] args){
		
		MsgTest t = new MsgTest();
		Thread  tr = new Thread(t);
		tr.start();
		try {
			Thread.sleep(500);
		}
		catch(Exception e){}
		for (int i=0; i<5; i++) {
			t.m.sendMsg(1,"Hello " + i);
			try {
			Thread.sleep(100);
			System.out.println("send " + i + " queue num: " + t.m.getNumMsg(1) );
			}
			catch(Exception e){}
		}
		try {
			tr.join();
		}
		catch (Exception e){}
		System.out.println("Done!");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		do {
			Object o = m.recvMsg(1, true);
			System.out.println("recv: " + o);
		} while (++i < 4);
	}
}
