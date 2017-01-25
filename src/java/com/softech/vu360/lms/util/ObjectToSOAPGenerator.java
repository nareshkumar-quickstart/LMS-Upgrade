/**
 * 
 */
package com.softech.vu360.lms.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * @author Zeeshan Hanif
 * 
 * This class will be used to generate SOAP Envelop from Object Graph
 *
 */
public class ObjectToSOAPGenerator {

	private static Logger log = Logger.getLogger(ObjectToSOAPGenerator.class.getName());
	
	public static <T> String generateSOAPEnvelop(T T)throws Exception{
		log.debug("SOAP Generator Start");		
		
		JAXBContext jc = JAXBContext.newInstance( T.getClass());
		
		// marshal to OutputStream
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStream os = bos;
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal( T, os );
		os.close();
		
		//m.marshal(st, System.out);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		
		//Populate the body
        //Create the main element and namespace
		MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        
        //Create objects for the message parts            
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();        

        //envelope.addNamespaceDeclaration("soapenv","http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
        envelope.addNamespaceDeclaration("xsd","http://www.w3.org/2001/XMLSchema" );
        envelope.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
        SOAPBody body = envelope.getBody();
        
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(true);
        
        DocumentBuilder bul = fac.newDocumentBuilder();
        Document doc = bul.parse(bis);
        body.addDocument(doc);

        message.saveChanges();
 
        // Create Byte output stream to hold soap envelop data
        ByteArrayOutputStream bosSOAP = new ByteArrayOutputStream();
        
        message.writeTo(bosSOAP);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bosSOAP.toByteArray())));
        
        String line = null;
        StringBuffer sb = new StringBuffer();
		while((line=br.readLine()) != null) {
			sb.append(line+"\n");	
		}
		log.debug("SOAP Packet:");
		log.debug(sb.toString());		
		
		return sb.toString();
	}
}
