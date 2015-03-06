//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.util.Hashtable;
import java.util.Enumeration;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * Argument is a class of hashtable to hold arguments 
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
public class Argument extends Hashtable
{
	
	/**
	 * Argument - Argument class constructor. 
	 * @param None.
	 * @return None.
	 */	
	public Argument()
	{
		super();
	}
	
	/**
	 * getArgument - get argument by key. 
	 * @param String - key.
	 * @return Object - value of key.
	 */	
	public Object getArgument(String key)
	{
		return this.get(key);		
	}
	
	/**
	 * getArgumentNames - get argument names. 
	 * @param - None.
	 * @return Enumeration - all keys.
	 */	
    public Enumeration getArgumentNames()
    {
 		return this.keys();
    }
	
	/**
	 * setArgument - set argument method. 
	 * @param String - key.
	 * @param Object - value.
	 * @return boolean - success or fail.
	 */	
	public boolean setArgument(String key, Object argv)
	{
		if ( key == null || argv == null )
		{
			System.out.println("Error: Tried to set a argument with null key or null object.");
			return false;
		}
		Object o = this.put(key, argv);
		return ( o == null );
	}
	
	/**
	 * dump - dump all method. 
	 * @param - None.
	 * @return String - current contains.
	 */	
	public String dump()
	{
		return ("Arguments: " + this.toString());
	}
}
