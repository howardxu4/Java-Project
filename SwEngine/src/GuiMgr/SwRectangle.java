//******************************************************************************
// Package Declaration
//******************************************************************************
package GuiMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;

import java.awt.*;
import java.util.*;

import org.w3c.dom.*;

import XmlMgr.*;
import ComMgr.*;

/**
 *******************************************************************************
 * <B> class Description: </B><p><pre>
 *
 * SwRectangle is a class of Rectangle which configured by XML 
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
public class SwRectangle extends Rectangle
{
	public SwRectangle(readXML rd, Node pnode) {
		super();
		try {
			NodeList nodeList = ((org.w3c.dom.Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				String nm = node.getNodeName();
				int n = 40;
				try { n = new Integer(rd.get1stNodeValue(node)).intValue(); }
				catch (Exception en) {}
				if (nm.equalsIgnoreCase("Height")) {
					this.height = n;
				}
				else if (nm.equalsIgnoreCase("Width")) {
					this.width = n;
				}
				else if (nm.equalsIgnoreCase("X")) {
					this.x = n;
				}
				else if (nm.equalsIgnoreCase("Y")) {
					this.y = n;
				}
				else
				if (LogTrace.getLog().chkLevel(LogTrace.WARNING)) 
					LogTrace.getLog().Trace(LogTrace.WARNING, 
						"Name " + node.getNodeName() + " is not processed in Rectangle");				
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"Rectangle Exception: " + e.getStackTrace());}	
	}	
}
