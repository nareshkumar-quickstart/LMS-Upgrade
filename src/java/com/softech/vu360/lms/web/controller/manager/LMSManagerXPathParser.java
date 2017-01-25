package com.softech.vu360.lms.web.controller.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
public class LMSManagerXPathParser {



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
				
				NodeList resourcesElements = currentElement.getElementsByTagName("resource");
				System.out.println("resourcesElements: " + resourcesElements.getLength());
				int j=0;
				List<String>  scos = new ArrayList<String>();
				for(int i=0;i<resourcesElements.getLength();i++)
				{
					Element firstResourceElement = (Element) resourcesElements.item(i);
					if(firstResourceElement.getAttribute("adlcp:scormtype").contains("sco"))
					{
						j++;
						String href = firstResourceElement.getAttribute("href");
						System.out.println("SCO LAUNCH URI: " + href);
						scos.add(href);
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
				
				if(descAttributes != null && descAttributes.getLength() > 0)
				{
					Element descLangstringElement = (Element) descAttributes.item(0);    
					String description = (descLangstringElement.getFirstChild().getTextContent());
					System.out.println("description: " + description);
					courseDetails.put("description", description);					
				}
				else
				{
					courseDetails.put("description", " ");
				}
				

				


			}//if ended

		}//external loop ended./.. traversing loop



		return courseDetails;
	}
}
