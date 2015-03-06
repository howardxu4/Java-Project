//******************************************************************************
// Package Declaration
//******************************************************************************
package XmlMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import java.io.*;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * transXML class is a xml handler class for transform a xml file through
 * a xsl file. 
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


public class transXML
{
	private Transformer transformer;
		
	/**
	 * transXML - transXML class constructor. 
	 * @param None.
	 * @return None.
	 */	
	transXML(String xslFile)
	{
		initTransform(xslFile);
	}
	
	/**
	 * initTransform - initialize transform. 
	 * @param String - template xslfile.
	 * @return None.
	 */	
	void initTransform(String xslFile)
	{
		try {
			this.transformer = null;
			TransformerFactory factory = TransformerFactory.newInstance();
			this.transformer = factory.newTransformer(new StreamSource(new File(xslFile)));
	    }catch(Exception e){
			System.out.println("TransXML Exception: " + e);
	    }
	}
	
	/**
	 * convertXML - transform xml file. 
	 * @param String - input xmlfile.
	 * @param String - output file.
	 * @return boolean - true for succeed.
	 */	
	boolean convertXML(String input, String out)
	{   
		boolean rtn = false;
		if (transformer != null) {
			try{
				transformer.transform(new StreamSource("file:///"+input), 
					new StreamResult(new FileOutputStream(out)));
				rtn = true;
			}catch(Exception e){
				System.out.println("TransXML Exception: " + e);
			}
		}
		else
			System.out.println("initTransform failed.");
		return rtn;
	}
	
	/**
	 * convertXML - transform xml file. 
	 * @param String - template xslfile.
	 * @param String - input xmlfile.
	 * @param String - output file.
	 * @return  boolean - true for succeed.
	 */	
	public static boolean convertXML(String xslFile, String input, String out)
	{
		transXML trans = new transXML(xslFile);
		return trans.convertXML(input, out);
	}
}
