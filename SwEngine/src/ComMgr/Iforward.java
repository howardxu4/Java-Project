//******************************************************************************
// Package Declaration
//******************************************************************************
package ComMgr;

/**
 *******************************************************************************
 * <B> interface Description: </B><p><pre>
 *
 * Iforward is an interface of set user type method 
 * This interface need implement the method on using class.
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

public interface Iforward extends Itypes
{	
	public void setMethodDecl(String [] decl);
	public void setUserType( int t);
	public int getUserType();
	public String [] getMethodDecl();
}
