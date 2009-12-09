package iwa1.datasources;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

public class RdfProducer {
	public static String XmlToRdf(String xml_data, String xsl_filename)
	{
		try {
			// Create a DOM factory and builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document docXMLString = builder.parse(new InputSource(new StringReader(xml_data)));
			
			// Load StreamSource objects with XML and XSLT files
			DOMSource xmlSource = new DOMSource(docXMLString);
			StreamSource xsltSource = new StreamSource( new File(xsl_filename) );
			//Serialize DOMSource to a String
			StringWriter strWriter = new StringWriter();
			// Load a Transformer object and perform the transformation
			TransformerFactory tfFactory =
			TransformerFactory.newInstance();
			Transformer tf = tfFactory.newTransformer(xsltSource);
			tf.transform(xmlSource, new StreamResult(strWriter));
			return strWriter.toString();
			}
			catch (Exception e) 
			{
			 e.printStackTrace();
			 return null;
			}	
	        	
	}

}
