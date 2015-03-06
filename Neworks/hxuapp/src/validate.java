import java.io.*;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;

public class validate {

     static public int verify(String xml, String sch) throws SAXException, IOException {
	
    	 int rtn = 0;

        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = 
            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        
        // 2. Compile the schema. 
        // Here the schema is loaded from a java.io.File, but you could use 
        // a java.net.URL or a javax.xml.transform.Source instead.
        File schemaLocation = new File(sch);
        Schema schema = factory.newSchema(schemaLocation);
    
        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();
        
        // 4. Parse the document you want to check.
        Source source = new StreamSource(xml);
        
        // 5. Check the document
        try {
            validator.validate(source);
        }
        catch (SAXException ex) {
            System.out.println(xml + " is not valid because ");
            System.out.println(ex.getMessage());
            rtn = -1;
        }  
        return rtn;
    }

    public static void main(String[] args) {

        if (args.length < 2) {
                System.out.println("Usage: validat input.xml verify.xsd");
        }
        else 
        try {
        	if(verify(args[0], args[1]) == 0)
        	System.out.println(args[0] + " is valid.");
        }
        catch( SAXException e) {}
        catch( IOException e) {}
        catch( Exception e){}
    }
}
