public interface IComm
{
	// data type
	public static final int STR = 0;
	public static final int INT = 1;
	public static final int LABL = 2;
	public static final int BOOL = 3;
	public static final int COMB = 4;
	public static final int COLR = 5;
	public static final int ICON = 6;
	public static final int DIAL = 7;
	public static final int TREE = 8;
	public static final int CONT = 9;
	public static final int SUBT = 10;		// hiden
	public static final int TAG = 20;
	public static final int VTAG = 30;
	public static final int MENU = 40;
	public static final int MENUBAR = 50;
	
	public static final int HIDE = 100;
	
	// data kind
	public static final int NAME = 0;
	public static final int PROP = 1;
	public static final int ATTR = 2;
	public static final int KIDS = 3;
	public static final int DYNM = 4;
	
	// id
	public static final int PARENT = -1;
	
	// message to child
	public static final int TURNON = 1;
	public static final int TURNOFF = 0;
	// message to parent
	public static final int KEYUP = -1;
	public static final int MSSELECT = 0;
	public static final int KEYDOWN = 1;
	public static final int SELECTED = 2;
	public static final int UNSELECT = 3;
	public static final int FUNCHECK = 4;
	public static final int UPDATED = 5;
	
	public void Acknowledge(int from, int info);
}
