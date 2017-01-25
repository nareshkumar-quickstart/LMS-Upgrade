package com.softech.vu360.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class XmlDocument {

	public static Document getXmlDocument(String xmlResponse) throws Exception {
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documenBuilder = builderFactory.newDocumentBuilder();
		documenBuilder.setErrorHandler(new DomErrorHandler());
		Document xmlDocument = null;
    	try {
    		//xmlDocument = documenBuilder.parse(new InputSource(new ByteArrayInputStream(xmlResponse.getBytes("UTF-16"))));
    		xmlDocument = documenBuilder.parse(new InputSource(new ByteArrayInputStream(xmlResponse.getBytes())));
    		
    		/**
    		 * optional, but recommended
    		 * 
    		 *  the following XML element
    		 		
    		 		<foo>hello 
					wor
					ld</foo>
			 * 
			 * could be represented like this in a denormalized node:
			 * 
			 		Element foo
    					Text node: ""
    					Text node: "Hello "
    					Text node: "wor"
    					Text node: "ld"
    					
    		 * When normalized, the node will look like this
    		 * 
    		 		Element foo
    					Text node: "Hello world"
    					
    		 * And the same goes for attributes: <foo bar="Hello world"/>, comments, etc.
    		 */
    		xmlDocument.getDocumentElement().normalize();
    	} catch(IOException e) {
    		e.printStackTrace();
    	} catch(SAXException e) {
    		e.printStackTrace();
    	}
    	return xmlDocument;
	}
	
	public static Document getXmlDocumentFromInputStream(InputStream is) throws IOException, SAXException, ParserConfigurationException {
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documenBuilder = builderFactory.newDocumentBuilder();
		documenBuilder.setErrorHandler(new DomErrorHandler());
		Document xmlDocument = null;
    	try {
    		xmlDocument = documenBuilder.parse(is);
    		
    		/**
    		 *  the following XML element
    		 		
    		 		<foo>hello 
					wor
					ld</foo>
			 * 
			 * could be represented like this in a denormalized node:
			 * 
			 		Element foo
    					Text node: ""
    					Text node: "Hello "
    					Text node: "wor"
    					Text node: "ld"
    					
    		 * When normalized, the node will look like this
    		 * 
    		 		Element foo
    					Text node: "Hello world"
    					
    		 * And the same goes for attributes: <foo bar="Hello world"/>, comments, etc.
    		 */
    		xmlDocument.getDocumentElement().normalize();
    	} catch(IOException e) {
    		e.printStackTrace();
    	} catch(SAXException e) {
    		e.printStackTrace();
    	}
    	return xmlDocument;
	}
	
	private static class DomErrorHandler implements ErrorHandler {
		
		/**
		 * Called to notify conditions that are not errors or fatal errors. The exception object, spe, contains information to
		 * enable you to locate the error in the original document.
		 */
		public void warning(SAXParseException spe) {
		
			/**
			 * Returns the line number of the end of the document text where the error occurred. If this is not available, �1 
			 * is returned.
			 */
			System.out.println("Warning at line " + spe.getLineNumber());
			
			/**
			 * Returns the column number within the document that contains the end of the text where the error occurred. If this
			 * is not available, �1 is returned. The first column in a line is column 1.
			 */
			System.out.println("Column Number " + spe.getColumnNumber());
			
			/**
			 * Returns the public identifier of the entity where the error occurred, or null if no public identifier is 
			 * available.
			 */
			System.out.println("Public Id" + spe.getPublicId());
			
			/**
			 * Returns the system identifier of the entity where the error occurred, or null if no system identifier is 
			 * available.
			 */
			System.out.println("System Id" + spe.getSystemId());
		    System.out.println(spe.getMessage());
		
		} //end of warning()
		
		/**
		 * Here you just rethrow the SAXParseException after outputting an error message indicating the line number that caused 
		 * the error. The SAXParseException class is a subclass of SAXException, so you can rethrow spe as the superclass type. 
		 * Don't forget the import statements in the MySAXHandler source fi le for the SAXException and SAXParseException class 
		 * names from the org.xml.sax package.
		 */
		public void fatalError(SAXParseException spe) throws SAXException {
			System.out.println("Fatal error at line " + spe.getLineNumber());
		    System.out.println(spe.getMessage());
		    throw spe;
		} //end of fatalError()

		public void error(SAXParseException spe) throws SAXException {
			System.out.println("Error at line " + spe.getLineNumber());
			System.out.println(spe.getMessage());
		} //end of error()
		
	} //end of static inner class DomErrorHandler
	
}
