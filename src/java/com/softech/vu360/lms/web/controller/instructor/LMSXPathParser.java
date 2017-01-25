package com.softech.vu360.lms.web.controller.instructor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author tahir.mehmood
 *SCORM VERSION:
<schemaversion>1.2</schemaversion>

TITLE OF COURSE:
<title>
<langstring xml:lang="en-US">118-01 HAZWOPER Regulation Overview</langstring>
</title>

DESCRIPTION OF COURSE
<description>
<langstring xml:lang="en-US">118-01 HAZWOPER Regulation Overview</langstring>
</description>


SCO LAUNCH URI
<resource identifier="SCO0" type="webcontent" adlcp:scormtype="sco" href="player.html">
<file href="player.html"></file>
<dependency identifierref="ALLRESOURCES"></dependency>
</resource> 
 */
public class LMSXPathParser {



	public static Map getCourseDetailsFromXML(File XMLFile) throws Exception {

		Map courseDetails = null;

		File file = XMLFile;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		System.out.println("Root element " + doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("manifest");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);
			courseDetails = new HashMap();
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {			  
				Element currentElement = (Element) fstNode;			      
				NodeList titleElements = currentElement.getElementsByTagName("title");
				Element firstTitleElement = (Element) titleElements.item(0);
				NodeList titleAttributes = firstTitleElement.getElementsByTagName("langstring");
				if(titleAttributes.getLength() > 0)
				{
					Element langstringElement = (Element) titleAttributes.item(0);
					String title = (langstringElement.getFirstChild().getTextContent());
					System.out.println("Title : "  + title);
					courseDetails.put("title", title);					
				}else
				{
					Element langstringElement = (Element) titleAttributes.item(0);
					String title = firstTitleElement.getTextContent();
					System.out.println("Title : "  + title);
					courseDetails.put("title", title);	
					
				}
				NodeList lstNmElmntLst = currentElement.getElementsByTagName("schemaversion");
				Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
				NodeList lstNm = lstNmElmnt.getChildNodes();
				String schemaVersion = ((Node) lstNm.item(0)).getNodeValue();
				System.out.println("SCORM VERSION : " + schemaVersion);
				courseDetails.put("schemaversion", schemaVersion);
				
				//BEGIN: ENGSUP-31157
				NodeList datafromLMSElement = currentElement.getElementsByTagName("adlcp:datafromlms");
				if(datafromLMSElement.getLength() > 0)
				{
					Element firstDataFromLMSElement = (Element) datafromLMSElement.item(0);
					String dataFromLMS = firstDataFromLMSElement.getTextContent();
					courseDetails.put("datafromlms", dataFromLMS);
				}
				
				datafromLMSElement = currentElement.getElementsByTagName("adlcp:dataFromLMS");
				if(datafromLMSElement.getLength() > 0)
				{
					Element firstDataFromLMSElement = (Element) datafromLMSElement.item(0);
					String dataFromLMS = firstDataFromLMSElement.getTextContent();
					courseDetails.put("datafromlms", dataFromLMS);
				}
				
				
				NodeList timelimitactionElement = currentElement.getElementsByTagName("adlcp:timelimitaction");
				if(timelimitactionElement.getLength() > 0)
				{
					Element firsttimelimitactionElement = (Element) timelimitactionElement.item(0);
					String timelimitaction = firsttimelimitactionElement.getTextContent();
					courseDetails.put("timelimitaction", timelimitaction);
				}
				
				timelimitactionElement = currentElement.getElementsByTagName("adlcp:timeLimitAction");
				if(timelimitactionElement.getLength() > 0)
				{
					Element firsttimelimitactionElement = (Element) timelimitactionElement.item(0);
					String timelimitaction = firsttimelimitactionElement.getTextContent();
					courseDetails.put("timelimitaction", timelimitaction);
				}
				
				
				NodeList completionThresholdElement = currentElement.getElementsByTagName("adlcp:completionThreshold");
				if(completionThresholdElement.getLength() > 0)
				{
					Element firstcompletionThresholdElement = (Element) completionThresholdElement.item(0);
					String completionThreshold = firstcompletionThresholdElement.getTextContent();
					courseDetails.put("completionThreshold", completionThreshold);
				}
				
				NodeList masteryscoreElement = currentElement.getElementsByTagName("adlcp:masteryscore");
				if(masteryscoreElement.getLength() > 0)
				{
					Element firstmasteryscoreElement = (Element) masteryscoreElement.item(0);
					String masteryscore = firstmasteryscoreElement.getTextContent();
					courseDetails.put("masteryscore", masteryscore);
				}
				
				masteryscoreElement = currentElement.getElementsByTagName("imsss:minNormalizedMeasure");
				if(masteryscoreElement.getLength() > 0)
				{
					Element firstmasteryscoreElement = (Element) masteryscoreElement.item(0);
					String masteryscore = firstmasteryscoreElement.getTextContent();
					courseDetails.put("masteryscore", masteryscore);
				}
				
				NodeList maxtimeallowedElement = currentElement.getElementsByTagName("adlcp:maxtimeallowed");
				if(maxtimeallowedElement.getLength() > 0)
				{
					Element firstmaxtimeallowedElement = (Element) maxtimeallowedElement.item(0);
					String maxtimeallowed = firstmaxtimeallowedElement.getTextContent();
					courseDetails.put("maxtimeallowed", maxtimeallowed);
					
					if(maxtimeallowed !=null && maxtimeallowed.length() >0)
					{
						String[] timeArray = maxtimeallowed.split(":");
						
						if(timeArray!=null && timeArray.length >0 && timeArray.length == 3)
						{
								int hrs = (Integer.parseInt(timeArray[0]));
								
								if(hrs <0)
								{
									hrs =0;
								}
								
								int mns = (Integer.parseInt(timeArray[1]));
								if(mns <0)
								{
									mns =0;
								}
								
								double secs = (Double.parseDouble(timeArray[2]));
								if(secs <0)
								{
									secs =0;
								}
								
								
								int totalSecs = (hrs * (60*60))+(mns * 60)+ ((int)secs) ;
								courseDetails.put("maxtimeallowedValue", totalSecs);								
						}						
					}
				}
				
				maxtimeallowedElement = currentElement.getElementsByTagName("imsss:limitConditions");
				if(maxtimeallowedElement.getLength() > 0)
				{
					Element firstmaxtimeallowedElement = (Element) maxtimeallowedElement.item(0);					
					String firstmaxtimeallowedAttr = firstmaxtimeallowedElement.getAttribute("attemptAbsoluteDurationLimit");
					
					if(firstmaxtimeallowedAttr.length() > 0)
					{						
						String maxtimeallowed = firstmaxtimeallowedAttr;
						courseDetails.put("maxtimeallowed", maxtimeallowed);	
						
						
						if(maxtimeallowed !=null && maxtimeallowed.length() >0)
						{
							// format of incoming time:  P[yY][mM][dD][T[hH][mM][s[.s]S]]
							int timeIndex = maxtimeallowed.indexOf('T');
							if ( timeIndex >= 0 ) {
								int startIndex = 1;
								String hours = null;
								String minutes = null;
								String seconds = null;
								// parse time
								String timeStr = maxtimeallowed.substring(timeIndex, maxtimeallowed.length());
								// the time portion is broken into: T[hH][mM][s[.s]S]
								int hoursIndex = timeStr.indexOf('H');
								if ( hoursIndex >= 0 ) {
									// parse out hours
									hours = timeStr.substring(startIndex, hoursIndex);
									startIndex = hoursIndex+1;
								}
								
								int minutesIndex = timeStr.indexOf('M');
								if ( minutesIndex >= 0 ) {
									minutes = timeStr.substring(startIndex, minutesIndex);
									startIndex = minutesIndex + 1;
								}
								
								int secondsIndex = timeStr.indexOf('S');
								if ( secondsIndex >= 0 ) {
									seconds = timeStr.substring(startIndex, secondsIndex);
								}
								
								// construct the time in seconds
								long timeInSeconds = 0;
								if ( seconds != null ) {
									try {
										timeInSeconds += Double.parseDouble(seconds);
									}
									catch (NumberFormatException nfe) {
										
									}
								}
								
								if ( minutes != null ) {
									try {
										timeInSeconds = timeInSeconds + (Long.parseLong(minutes)*60);
									}
									catch (NumberFormatException nfe) {
										
									}						
								}
								
								if ( hours != null ) {
									try {
										timeInSeconds = timeInSeconds + (Long.parseLong(hours)*60*60);
									}
									catch (NumberFormatException nfe) {
										
									}						
								}
								
								courseDetails.put("maxtimeallowedValue", timeInSeconds);								
							}						
						}
					}					
				}
								
				//END: ENGSUP-31157
				
				
				NodeList resourcesElements = currentElement.getElementsByTagName("resource");
				System.out.println("resourcesElements: " + resourcesElements.getLength());
				int j=0;
				List<String>  scos = new ArrayList<String>();
				
				for(int i=0;i<resourcesElements.getLength();i++){														
					Element firstResourceElement = (Element) resourcesElements.item(i);										
					  NamedNodeMap namedNodeMap =firstResourceElement.getAttributes();
					  for(int index=0;index<namedNodeMap.getLength();index++){
					    Node node=namedNodeMap.item(index);
					    if(node.getNodeName().equalsIgnoreCase("adlcp:scormtype") && node.getNodeValue().contains("sco")){
					       j++;
					       String href = firstResourceElement.getAttribute("href");
					       System.out.println("SCO LAUNCH URI: " + href);
					       scos.add(href);
					    }					    						    
					   }
																				
				}//internal for loop end
				courseDetails.put("href", scos); 
				
				
				NodeList descElements = currentElement.getElementsByTagName("description");
				Element firstDescElement = (Element) descElements.item(0);
				
				NodeList descAttributes = null;
				if(firstDescElement != null && firstDescElement.getElementsByTagName("langstring") != null)
				{
					descAttributes = firstDescElement.getElementsByTagName("langstring");
				}
				
				if(descAttributes != null && descAttributes.getLength() > 0){				
					Element descLangstringElement = (Element) descAttributes.item(0);    
					String description ="";
					if(descLangstringElement.getFirstChild() != null)
					   description = (descLangstringElement.getFirstChild().getTextContent());
										
					courseDetails.put("description", description);					
				}
				else{				
					courseDetails.put("description", " ");
				}

			}//if ended

		}//external loop ended./.. traversing loop



		return courseDetails;
	}
}
