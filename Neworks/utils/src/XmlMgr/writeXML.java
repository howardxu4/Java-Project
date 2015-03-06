//******************************************************************************
// Package Declaration
//******************************************************************************
package XmlMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * writeXML class is a xml handler class for write information to xml file. 
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


public class writeXML
{
	private final int	MAXLEVEL = 18;
    private Document	doc;
	private Element []	level;
	
	/**
	 * writeXML - writeXML class constructor. 
	 * @param None.
	 * @return None.
	 */	
	public writeXML()
	{
		try{
			level = new Element [MAXLEVEL];
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument ();
		}catch(Exception e){
		    System.out.println("writeXML Exception : "+ e);
		}
	}
	
	/**
	 * getDocument - get document. 
	 * @param None.
	 * @return Document - doc.
	 */	
	public Document getDocument() {
		return doc;
	}
	
	/**
	 * setDocument - set document. 
	 * @param String xmlfile.
	 * @return None.
	 */	
	public void setDocument(Document Doc) {
		doc = Doc;
	}
	
	/**
	 * getElement - get Element. 
	 * @param int - index.
	 * @return Element - element on level.
	 */	
	public Element getElement(int index)
	{
		if (index < MAXLEVEL) 
			return level[index];
		return null;
	}
	
	/**
	 * setElement - set Element. 
	 * @param int - index.
	 * @param String - type.
	 * @param String - value.
	 * @param boolean - add on.
	 * @return None.
	 */	
	public void setElement(int index, String type, String value, boolean addOn)
	{
		if (index < MAXLEVEL) {
			level[index] = doc.createElement(type);
			if (value != null)
				level[index].appendChild(doc.createTextNode(value));	// TEXT
			if (index == 0) doc.appendChild(level[index]); 
			else if(level[index-1] != null && addOn)
				level[index-1].appendChild(level[index]);
		}
	}
	
	/**
	 * setElement - set Element. 
	 * @param int - index.
	 * @param Element - element.
	 * @return None.
	 */	
	public void setElement(int index, Element element, boolean addOn)	// ELEM
	{
		if (index < MAXLEVEL) {
			level[index] = element;
			if (index == 0) doc.appendChild(level[index]); 
			else if(level[index-1] != null && addOn)
				level[index-1].appendChild(level[index]);
		}
	}
	
	/**
	 * setAttribute - set attribute. 
	 * @param int - index.
	 * @param String - type.
	 * @param String - value.
	 * @return None.
	 */	
	public void setAttribute(int index, String type, String value)		// ATTR
	{
		if (index < MAXLEVEL && level[index] != null)		
		    level[index].setAttribute(type, value);		
	}
	
	/**
	 * setCDATASection - set CDATA section. 
	 * @param int - index.
	 * @param String - cdata.
	 * @return None.
	 */	
	public void setCDATASection(int index, String cdata)				// CDATA
	{
		if (index < MAXLEVEL && level[index] != null)		
			level[index].appendChild(doc.createCDATASection(cdata));	
	}
	
	/**
	 * createXML - create xml file. 
	 * @param String - absLocation.
	 * @return None.
	 */	
	public void createXML(String absLocation){
		try{
		    FileWriter out = new FileWriter(absLocation);
            // Use a Transformer for output
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
 
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);			
		    if(doc.getDocumentElement() == null)
				System.out.println("doc element is null");
			
            transformer.transform(source, result);
			
		}
		catch(Exception e){
		    System.out.println("Error while writing XML: " + e.toString());
		}
	}
}
