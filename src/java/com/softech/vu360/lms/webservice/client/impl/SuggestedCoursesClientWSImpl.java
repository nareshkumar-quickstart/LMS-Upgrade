package com.softech.vu360.lms.webservice.client.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesDisplayRequest;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesDisplayRequestType;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesDisplayResponseType;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesDisplayService;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesList;
import com.softech.vu360.util.VU360Properties;

/**
 * This class implements all the interface methods for example,
 * updateProfile which will call stroefront web service to update the details
 * about a user. Little more work still needed.
 * 
 * @author abdullah mashood
**/

public class SuggestedCoursesClientWSImpl {
	
	private static final Logger log = Logger.getLogger(SuggestedCoursesClientWSImpl.class.getName());
	//public static final String STOREFRONT_SUGGESTEDCOURSES_ENDPOINT = "http://10.0.101.88/webapp/wcs/services/SuggestedCoursesDisplayService"; //http://10.0.101.88/webapp/wcs/services/SuggestedCoursesDisplayService

	public static final String STOREFRONT_SUGGESTEDCOURSES_ENDPOINT = VU360Properties.getVU360Property("storeFrontSuggestedCoursesEndPoint");
	
	public SuggestedCoursesList getSuggestedCourses_Marshal(List<String> courseGUID,String courseGroupGUID, String storeId) {
		
		log.info("\\n\\n\\n ---------- START : getSuggestedCourses() : " + this.getClass().getName() + " ---------- ");
		SuggestedCoursesList coursesList = null;

        try
		{
			//Request object used for communication between client and webservice
			Map<Object, Object> context = new HashMap<Object, Object>();

        
			WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
			webServiceTemplate.setDefaultUri(STOREFRONT_SUGGESTEDCOURSES_ENDPOINT);
			
			SuggestedCoursesDisplayRequest lsRequest = new SuggestedCoursesDisplayRequest();
			SuggestedCoursesDisplayRequestType lsRequestType = new SuggestedCoursesDisplayRequestType();
			SuggestedCoursesDisplayResponseType SuggestedCoursesResponse = null;


			if(storeId!=null && !storeId.isEmpty())
				lsRequestType.setStoreId(storeId);
			else
				lsRequestType.setStoreId("-1");
			
			
			if(courseGUID!=null && courseGUID.size()>0)
				lsRequestType.setCourseGuids(courseGUID);
			else{
				courseGUID.add("-1");
				lsRequestType.setCourseGuids(courseGUID);
			}			
			
			lsRequest.setIn(lsRequestType);


			//set the marshallers/unmarshallars
			org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
			org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
			webServiceTemplate.setMarshaller( marshaller );
			webServiceTemplate.setUnmarshaller( unmarshaller );
			
			//Need to set the contextpath which is just the complete package name used from ObjectFactory class
			marshaller.setContextPath("com.softech.vu360.lms.webservice.message.SuggestedCourse");
			unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.SuggestedCourse");
			
			//Need to call following methods to make sure the properties are properly set
			marshaller.afterPropertiesSet();
			unmarshaller.afterPropertiesSet();

            //SuggestedCoursesDisplayResponseType object = (SuggestedCoursesDisplayResponseType) webServiceTemplate.marshalSendAndReceive(lsRequest);
            //coursesList = object.getCourseList();

            JAXBElement element = (JAXBElement)webServiceTemplate.marshalSendAndReceive(lsRequest);
            if(element != null && element.getValue() != null)
            {
                //SuggestedCoursesDisplayResponseType responseType = (SuggestedCoursesDisplayResponseType)element.getValue();
                coursesList = ((SuggestedCoursesDisplayResponseType)element.getValue()).getCourseList();

            }
		} 
		catch( Exception e ) 
		{
			log.error(" ########## >>> ERROR <<< ########## ");
			log.error(e, e);
		}
		finally
		{
			log.info(" ---------- END : getSuggestedCourses() : " + this.getClass().getName() + " ---------- \\n\\n\\n");
		}
		return coursesList;
	}

    public SuggestedCoursesList getSuggestedCourses(List<String> courseGUID,String courseGroupGUID, String storeId) {

        log.info("\\n\\n\\n ---------- START : getSuggestedCourses() : " + this.getClass().getName() + " ---------- ");
        //List<SuggestedCoursesList> coursesList = null;
        SuggestedCoursesList coursesList = null;

        SuggestedCoursesDisplayService displayService = null;


        try
        {
            //Request object used for communication between client and webservice
            Map<Object, Object> context = new HashMap<Object, Object>();
            log.info("Now In construtor");
            displayService = new SuggestedCoursesDisplayService();
            log.info("Object Created");

            SuggestedCoursesDisplayRequest lsRequest = new SuggestedCoursesDisplayRequest();
            SuggestedCoursesDisplayRequestType lsRequestType = new SuggestedCoursesDisplayRequestType();
            SuggestedCoursesDisplayResponseType SuggestedCoursesResponse = null;

            courseGUID = new ArrayList<String>();

            courseGUID.add("aa");
            courseGUID.add("bb");

            lsRequestType.setStoreId("aa");
            lsRequestType.setCourseGuids(courseGUID);

            lsRequest.setIn(lsRequestType);

            log.info("Thats the result");

            SuggestedCoursesDisplayResponseType r  = displayService.getSuggestedCoursesDisplayService().getSuggestedCourses(lsRequest);
            log.info(r);
        }
        catch( Exception e )
        {
            log.error(" ########## >>> ERROR <<< ########## ");
            log.error(e, e);
        }
        finally
        {
            log.info(" ---------- END : getSuggestedCourses() : " + this.getClass().getName() + " ---------- \\n\\n\\n");
        }
        return coursesList;
    }

    public void WSClient()
    {
        try
        {
            URL url = new URL("http://10.0.101.88/webapp/wcs/services/SuggestedCoursesDisplayService/wsdl/wsdl/ThreeSixtyTrainingService/ThreeSixtyTraining-SuggestedCoursesDisplayService.wsdl");



            // Qualified name of the service:
            //   1st arg is the service URI
            //   2nd is the service name published in the WSDL
            QName qname = new QName("http://10.0.101.88/webapp/wcs/services/SuggestedCoursesDisplayService/wsdl/wsdl/ThreeSixtyTrainingService/ThreeSixtyTraining-SuggestedCoursesDisplayService.wsdl", "SuggestedCoursesDisplayService");

            // Create, in effect, a factory for the service.
            Service service = Service.create(url, qname);

            // Extract the endpoint interface, the service "port".
            //ThreeSixtyTrainingSuggestedCoursesDisplayServicePortType type  = service.getPort(ThreeSixtyTrainingSuggestedCoursesDisplayServicePortType.class);

        }
        catch (Exception e) {
            log.debug("Failed to create URL for the wsdl Location: 'http://10.0.101.88/webapp/wcs/services/SuggestedCoursesDisplayService/wsdl/wsdl/ThreeSixtyTrainingService/ThreeSixtyTraining-SuggestedCoursesDisplayService.wsdl', retrying as a local file");
            log.debug(e.getMessage());
        }

    }

}
