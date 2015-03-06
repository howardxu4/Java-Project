//******************************************************************************
// Package Declaration
//******************************************************************************
package XmlMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
 
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Vector;
import java.util.Enumeration;

/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * readXML class is a xml handler class for read information from xml file. 
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


public class readXML
{	
	private Document	doc;
    private DocumentBuilder	builder;
	
	/**
	 * readXML - readXML class constructor. 
	 * @param None.
	 * @return None.
	 */	
	public readXML(){
		try {
			DocumentBuilderFactory factory =
			    DocumentBuilderFactory.newInstance();
			//factory.setValidating(true);   
			//factory.setNamespaceAware(true);
		   builder = factory.newDocumentBuilder();
	    }
		catch(Exception e){
			System.out.println("error: while creating the Doc builder");
			System.out.println(e.toString());
			e.printStackTrace();
	    }
	}
	
	/**
	 * setDocument - set document. 
	 * @param Document - doc.
	 * @return none.
	 */	
	public void setDocument(Document d) {
		doc = d;
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
	 * getDocRoot - get document root. 
	 * @param None.
	 * @return Document root - root of doc.
	 */	
	public Element getDocRoot() {
		try {
			return doc.getDocumentElement();
		}
		catch (Exception e) {}
		return null;
	}
	
	/**
	 * setDocument - set document. 
	 * @param String xmlfile.
	 * @return boolean - true for succeed.
	 */	
	public boolean setDocument(String xmlFile) {
		try {
			if (builder == null) return false;
 		    doc = builder.parse( "file:///"+xmlFile );			
		}
		catch ( Exception e ){
			System.out.println("XML file read fails: " + e.getMessage() );
			return false;
		}
		return true;
	}
	
	public boolean setDocument(URL xmlUrl) {
		try {
			if (builder == null) return false;
 		    doc = builder.parse(new org.xml.sax.InputSource( xmlUrl.openStream()));			
		}
		catch ( Exception e ){
			System.out.println("XML URL read fails: " + e.getMessage() );
			return false;
		}
		return true;
	}

	public boolean setDocument(StringBufferInputStream stream) {
		try {
			if (builder == null) return false;
 		    doc = builder.parse( stream );			
		}
		catch ( Exception e ){
			System.out.println("XML file read fails: " + e.getMessage() );
			return false;
		}
		return true;
	}
	
	/**
	 * getXMLtype - get XML document type. 
	 * @param None.
	 * @return String - XMLtype.
	 */	
	public String getXMLtype()
	{
		try {
			Element root = doc.getDocumentElement();
			return root.getNodeName();		
		}
		catch (Exception e) {}
		return new String();
	}

	public String [] getSubItems(Object pnode, String [] types, int n)
	{		
		ArrayList sa = new ArrayList();
//		System.out.println("name =>>>>>>> " + ((Node)pnode).getFirstChild().getNodeValue());
		if (pnode == null) pnode = this.getDocRoot();
		int k = 0;
		try {
			NodeList nodeList = ((Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase(types[n])) {
					if (n ==  types.length - 1) {
//			System.out.println("cv = " + node.getFirstChild().getNodeValue().trim());
						sa.add( node.getFirstChild().getNodeValue().trim());
					}
					else
						return getSubItems(node, types, n+1);
				}
			}
		}
		catch (Exception e) {
		System.out.println(e.getStackTrace());
		}
		String [] ssa = new String [sa.size()];
		for(int i = 0; i < ssa.length; i++)
			ssa[i] = (String)sa.get(i);
		return ssa;
	}
	/**
	 * getTypeValue - get value of type in document. 
	 * @param Node - certain node in document.
	 * @param String [] - array of types.
	 * @param int - current level of type.
	 * @return String - value of type.
	 */	
	public String getTypeValue(Object pnode, String [] types, int n)
	{		
		if (pnode == null) pnode = getDocRoot();
		try {
			NodeList nodeList = ((Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase(types[n])) {
					if (n ==  types.length - 1)
						return node.getFirstChild().getNodeValue().trim();
					else
						return getTypeValue(node, types, n+1);
				}
			}
		}
		catch (Exception e) {}
		return new String();
	}
	
	/**
	 * getTypePNode - get parent node of type in document. 
	 * @param Node - certain node in document.
	 * @param String [] - array of types.
	 * @param int - current level of type.
	 * @return Node - pnode of type.
	 */	
	public Object getTypePNode(Object pnode, String [] types, int n, String value)
	{														
		if (pnode == null) pnode = getDocRoot();
		try {
			NodeList nodeList = ((Element)pnode).getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++ )
			{
				Node node = nodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase(types[n])) {
					if (n == types.length - 1) {
						if (value == null || value.equalsIgnoreCase(node.getFirstChild().getNodeValue().trim()))
							return node;
					}
					else {
						return getTypePNode(node, types, n+1, value);
					}
				}
			}
		}
		catch (Exception e) {System.out.println("ReadXML Exception: " + e.getStackTrace());}
		return null;
	}
	
	/**
	 * getAttrValue - get the attribute value of specific type in the node. 
	 * @param Node - certain node in document.
	 * @param String - attribute of type.
	 * @return String - value of type.
	 */	
	public String getAttrValue(Node node, String type) {
		if (node.hasAttributes()) {
		   NamedNodeMap  attrs = node.getAttributes();
		   for(int j = 0; j < attrs.getLength(); j++) {
				Node attrNode = attrs.item(j);
				if (attrNode.getNodeName().equalsIgnoreCase(type))
					return attrNode.getNodeValue().trim();
		   }
		}
		return new String("");
	}
	
	/**
	 * get1stNodeValue - get the value of the First Child of node. 
	 * @param Node - certain node in document.
	 * @return String - value of type.
	 */	
	public String get1stNodeValue(Node node) {
		try {
			return node.getFirstChild().getNodeValue().trim();
		}
		catch (Exception e) {};
		return new String("");
	}
	/**
	 * dump - dump document information. 
	 * @param None.
	 * @return String - information.
	 */	
	protected String dump() {
		String s = new String();
		Element root = doc.getDocumentElement();
		s += "Name: " + root.getNodeName() + "\n\n";		
		return s + recDump(root.getChildNodes(), true);
	}
	
	/**
	 * recDump - recursive call dump. 
	 * @param Nodelist -  xml nodelist.
	 * @param boolean - isDump
	 * @return String - information.
	 */	
	private String recDump(NodeList nodeList, boolean f)
	{
		String s = new String();
		String t = new String();
		Vector trash = new Vector();
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			Node childNode = nodeList.item(i);
			t = nodeDump(childNode, f);
			if (t.length() < 1) 
				trash.addElement(childNode);
			else
				s += t;	// debug
			if (f && childNode.hasAttributes()) {
				s += dumpAttr(childNode);
/*				
				NamedNodeMap  attrs = childNode.getAttributes();
				int jj = attrs.getLength();
				for(int j = 0; j < attrs.getLength(); j++)
				{
					Node attrNode = attrs.item(j);
//					s += "ATTR " + j + " in " + jj + " : \n";
					s += nodeDump(attrNode, f);
				}
*/
			}
			if (childNode.hasChildNodes())
				s += recDump(childNode.getChildNodes(), f);
		}	
		Node parentNode = nodeList.item(0).getParentNode();
		for (Enumeration e=trash.elements(); e.hasMoreElements();)
		{
			parentNode.removeChild((Node)e.nextElement());
		}
		return s;
		
	}

	/**
	 * dumpAttr - dump node attributes information. 
	 * @param Node - xml node.
	 * @return String - information.
	 */	
	public String dumpAttr(	Node childNode ) { 
		String s = new String();
		NamedNodeMap  attrs = childNode.getAttributes();
		int jj = attrs.getLength();
		for(int j = 0; j < attrs.getLength(); j++)
		{
			Node attrNode = attrs.item(j);
//					s += "ATTR " + j + " in " + jj + " : \n";
			s += nodeDump(attrNode, true);
		}
		return s;
	}
	
	/**
	 * nodeDump - dump node information. 
	 * @param Node - xml node.
	 * @param boolean - isDump
	 * @return String - information.
	 */	
	private String nodeDump(Node node, boolean f)
	{
		String s = new String();
		if (node.getNodeType() == 3 && node.getNodeValue().trim().length() == 0) return s;
		if (node.getNodeName().charAt(0) != '#')
			s += "Name: " + node.getNodeName() + "\n";
		else s += "  ";
		if (f) {
			if (node.getNodeValue() != null)
				s += "Value: " + node.getNodeValue() + "\n";
			s += "Type: " + node.getNodeType() + "\n";
			if (node.hasAttributes())
				s += "Attribute: yes \n";
			if (node.hasChildNodes())
				s += "has Child: true \n";
			else
				s += "is leaf node \n";
		}
		return s;
	}
	
	/**
	 * trimDocument - trim document. 
	 * @param None.
	 * @return None.
	 */	
	public void trimDocument()
	{
		Element root = doc.getDocumentElement();
		recDump(root.getChildNodes(), false);
	}
	
	public String dumpAll() {
		return dump();	
	}

	/**
	 * main test entry
	 */
	public static void main(String args[])
	{
		try {
			readXML rd = new readXML();
			rd.setDocument(System.getProperty("user.dir") + "\\" + args[0]);
			rd.trimDocument();
			String s = rd.dump();
			System.out.println(s);
		}
		catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
	}
}
