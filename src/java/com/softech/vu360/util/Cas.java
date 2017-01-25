package com.softech.vu360.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.security.sasl.AuthenticationException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class Cas {
	
	private static final Logger LOGGER = Logger.getLogger(Cas.class.getName());
	
	private String username;
	private String password;
	private String casUrl;
	private RestClient restClient;
	
	private static Document getResponseFromHttpsURLConnection(HttpsURLConnection conn) throws IOException, SAXException, ParserConfigurationException {
		return XmlDocument.getXmlDocumentFromInputStream(conn.getInputStream());
	}
		
	public static String validate(final String casUrl, final String ticket, final String serviceUrl) throws Exception {

		String casValidatingUrl = casUrl;
		String requestParameters;
		if(serviceUrl == null || serviceUrl == StringUtils.EMPTY)
			requestParameters = String.format("?ticket=%s", serviceUrl, ticket);
		else
			requestParameters = String.format("?service=%s&ticket=%s", serviceUrl, ticket);
		String casValidationUrl = casValidatingUrl + requestParameters;
		
        Document responseDoc = getResponseFromHttpsURLConnection(
				WebProxyNetworkUtil.getHttpsConnection(casValidationUrl)
				);
        
        LOGGER.info(getStringFromDoc(responseDoc));
        
        return getUser(responseDoc);
	}
	
	private static boolean isValidUser(Document xmlDocument) throws DOMException, CASAuthenticationFailed {
		
		Node authenticationFailureNode = getNode(xmlDocument, "cas:authenticationFailure");
		if (authenticationFailureNode != null) {
			if (authenticationFailureNode == null || authenticationFailureNode.getFirstChild() == null) {
				throw new NullPointerException("cas:authenticationFailure is either empty or doesn't exists in CAS Response");
			}
			throw new CASAuthenticationFailed(authenticationFailureNode.getFirstChild().getNodeValue()); 
		}
		return true;
	}
	
	private static String getUser(Document xmlDocument) throws NullPointerException, CASAuthenticationFailed {
		if(isValidUser(xmlDocument)) {
			Node userNode = getNode(xmlDocument, "cas:user");
			if (userNode == null || userNode.getFirstChild() == null) {
				throw new NullPointerException("cas:user is either empty or doesn't exists in CAS Response");
			}
			return userNode.getFirstChild().getNodeValue();
		}
		
		return StringUtils.EMPTY;
	}
	
	private static Node getNode(Document xmlDocument, String nodeName) {
		
		Node node = null;
		NodeList nodeList = xmlDocument.getElementsByTagName(nodeName);
		int nodeListLength = nodeList.getLength();
		if (nodeListLength > 0) {
			node = nodeList.item(0);
		}
		return node;
	}
	
	
	public Cas(String username, String password, String casUrl) {
		this.username = username;
		this.password = password;
		this.casUrl = casUrl;
		restClient = new RestClient();
	}
	
	public String getServiceTicket(String serviceUrl) throws IOException {
		// get TGT
        String location = getTicketGrantingTicket(username, password);
        
        // get SGT
        return getServiceGrantingTicket(location, serviceUrl);
		
	}
	
	/**
	 * With the TGT location and service url this will get the SGT
	 * @param tgtLocation
	 * @param serviceUrl
	 * @return
	 * @throws IOException
	 */
	private String getServiceGrantingTicket(String tgtLocation, String serviceUrl) throws IOException {
		Map<String,Object> params = new HashMap<String, Object>();
        params.put("service", serviceUrl);
        params.put("method", "POST");
        
        
        HttpURLConnection conn = restClient.post(tgtLocation, params);
        StringBuilder responseBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String input;
		while ((input = in.readLine()) != null) {
			responseBuilder.append(input);
		}
		in.close();
		
		String response = responseBuilder.toString();
		LOGGER.info("SGT -> " + response);
		 
		return response;
	}
	
	/**
	 * Gets the TGT for the given username and password
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private String getTicketGrantingTicket(String username, String password) throws IOException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", password);
		HttpURLConnection conn = restClient.post(casUrl, params);
		
        if(conn.getResponseCode() == 400) {
        	throw new AuthenticationException("bad username or password");
        }
        String location = conn.getHeaderField("Location");
        LOGGER.info("TGT LOCATION -> " + location);
        return location;
	}
	
	private static String getStringFromDoc(org.w3c.dom.Document doc)    {
	    DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
	    LSSerializer lsSerializer = domImplementation.createLSSerializer();
	    return lsSerializer.writeToString(doc);   
	}
	
}