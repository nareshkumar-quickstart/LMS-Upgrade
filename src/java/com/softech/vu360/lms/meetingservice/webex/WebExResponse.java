package com.softech.vu360.lms.meetingservice.webex;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class WebExResponse {
	private int status;
	private String errorCode;
	private String errorReason;
	public static final int ERROR=1;
	public static final int OK=2;
	private static final Logger log = Logger.getLogger(WebExResponse.class.getName());
	
	private static DocumentBuilderFactory factory = null;
	private static DocumentBuilder builder = null;

	public WebExResponse deserialize(String strResponse) throws IOException
	{
		WebExResponse response=null;
		try 
		{
			
			//javax.xml.parsers.DocumentBuilder db = factory.newDocumentBuilder();
		    org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
		 
		    inStream.setCharacterStream(new java.io.StringReader(strResponse));
		    //org.xml.sax.Document doc = db.parse(inStream);
		    initDocumentBuilder();
		    // File f = new File(argv[0]);
		    Document document = builder.parse(inStream);
			NodeList list = document.getElementsByTagName("result");
			Node node = list.item(0);
			String nodeValue = node.getTextContent();
			if(nodeValue.equalsIgnoreCase("FAILED"))
			{
				this.setStatus(ERROR);
				this.setErrorCode("302");
				//this.setErrorReason(nodeValue);
			}
			else if(nodeValue.equalsIgnoreCase("SUCCESS")) 
			{ 
				NodeList bodContentList=document.getElementsByTagName("bodyContent");
				String requestType=bodContentList.item(0).getAttributes().getNamedItem("xsi:type").getTextContent();
				if(StringUtils.isNotBlank(requestType))
				{
					if(requestType.equalsIgnoreCase("meet:getjoinurlMeetingResponse"))
					{
						response=new JoinMeetingURLResponse();
						this.setStatus(OK);
					}
				}
				handlePostDeserialization(document);
			}
			
		}catch (ParserConfigurationException e) 
		{
			this.setStatus(ERROR);
			this.setErrorCode("303");
			log.error("Error Occured while parsing XML in desrialization process:"+e.getMessage());
			
		} 
		catch (SAXException e) 
		{
			this.setStatus(ERROR);
			this.setErrorCode("304");
			this.setErrorReason(e.getMessage());
			log.error("Error Occured in desrialization process :"+e.getMessage());
		}
		return response;
	}
	public abstract void handlePostDeserialization(Document doc);
	public int getStatus() {
		return status;
	}
	private  static final void initDocumentBuilder() throws ParserConfigurationException{ 
		if(factory==null){
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		}
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
}
