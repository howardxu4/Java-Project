package FileMgr;

import java.io.*;
public class FileMgr {
	
	/** GetFileSizeByName - Get file size
	 * @parameter:  Fname - file name
	 * @return:     -1 fails, otherwise size of the file
	 */
	static public int GetFileSizeByName(String fname) {
		int ret = -1;
		File f = new File(fname);
		if (f.exists())
			ret = (int)f.length();
		return ret;
	}
	/**
	 * ReadFromFile - Get the partial content from file
	 * @parameter:  Data - pointer of data holder
	 * @parameter:  Fname - file name
	 * @parameter:  From - start poistion
	 * @parameter:  Size - data size
	 * @return:     the read data size
	 */
	static public int ReadFromFile(Object[] data, String fname, int from, int size) {
		int ret = 0;
		try {
			File f = new File(fname);
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			byte[] b = new byte[size];
			raf.seek(from);
			ret = raf.read(b);
			raf.close();
			data[0] = b;
		}
		catch(Exception e){}
		return ret;
	}
	/**
	 * GetFromFile - Get the content from file
	 * @parameter:  Fname - file name
	 *              Size - file size
	 * @return:     NULL - fails, otherwise the pointer of memory of file content
	 */
	static public byte[] GetFromFile(String fname, int size) {
		Object[] data = new Object[1];
		data[0] = null;
		ReadFromFile(data, fname, 0, size);
		return (byte[])data[0];
	}
	/**
	 * RemoveFile - Delete the file
	 * @parameter:  Fname - file name
	 * @return:     true if success
	 */
	static public boolean RemoveFile(String fname) {
		boolean ret = false;
		File f = new File(fname);
		if(f.exists())
			ret = f.delete();
		return ret;
	}
	/**
	 * WriteToFile - Write the information in memory to a file
	 * @parameter:  Fname - file name
	 *              Info - the poiter of memory
	 *              Size - the size of information in memory
	 * @return:     true - success, otherwise fails
	 */
	static public boolean WriteToFile(String fname, byte[] info, int size ) {
		boolean ret = false;
		try {
			File f = new File(fname);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.seek(f.length());
			raf.write(info, 0, size);
			raf.close();
			ret = true;
		}
		catch (Exception e) {}
		return ret;
	}
	/**
	 * SaveToFile - Save the information in memory to a new file
	 * @parameter:  Fname - file name
	 *              Info - the poiter of memory
	 *              Size - the size of information in memory
	 * @return:     true - success, otherwise fails
	 */
	static public boolean SaveToFile(String fname, byte[] info, int size) {
		RemoveFile(fname);
		return WriteToFile(fname, info, size);
	}
	
	static public boolean SaveToFile(String fname, String info) {
		byte[] data = info.getBytes();
		return SaveToFile(fname, data, data.length);
	}
/*	
	static public void main(String[] args) {
		String fname = "/home/hxu/hs_err_pid17869.log";
		int l = GetFileSizeByName(fname);
		System.out.println("File " + fname + " exist " + l);
		byte[] o = GetFromFile(fname, l);
		if (o != null)
			System.out.println(new String(o));
		fname = "/home/hxu/test.log";
		RemoveFile(fname);
		int blk = 4096;
		int ll = blk;
		byte[] oo = new byte[blk];
		for (int i=0; i<l; i+=blk) {
			if (l-i < blk) ll = l-i; 
			System.out.println("Write " + ll);
			System.arraycopy(o, i, oo, 0, ll);
			WriteToFile(fname, oo, ll);
		}
		fname = "/home/hxu/test1.log";
		SaveToFile(fname, o, l);
		Object[] a = new Object[1];
		ll = ReadFromFile(a, fname, 60000, 10000);
		System.out.println("return " + ll);
		System.out.println(new String((byte[])a[0]));		
	}
*/
}
