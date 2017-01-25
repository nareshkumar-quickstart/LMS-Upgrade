package com.softech.vu360.lms.webservice.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.webservice.WebBasedCourseCreationWS;
import com.softech.vu360.lms.webservice.message.webBasedCourseCreation.CourseCreationResponse;
import com.softech.vu360.lms.webservice.message.webBasedCourseCreation.WebBasedCourseCreationRequest;
import com.softech.vu360.util.VU360Properties;

@Endpoint
public class WebBasedCourseCreationImpl implements WebBasedCourseCreationWS{

	private static final Logger log = Logger.getLogger(WebBasedCourseCreationImpl.class.getName());
	private static final String TARGET_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/webBasedCourseCreation";
	private static final String WEBBASED_COURSE_CREATED_EVENT = "WebBasedCourseCreationRequest";
	private static final String COURSETYPE_SYNCHRONOUS = SynchronousCourse.COURSE_TYPE;
	private static final String DEFAULT_DELIVERYMETHOD = "Online Interactive";
	private static final Long DEFAULT_DELIVERYMETHODID = 1l;
	private static final String CONTENT_OWNER_ID = VU360Properties.getVU360Property("ws.webinar.contentOwnerId");;
	private static final String RESULT_SUCCESS = "Success";
	private static final String RESULT_FAILURE = "Failure";
	
	private CourseAndCourseGroupService courseAndCourseGroupService;
	
	@Override
	@PayloadRoot(localPart = WEBBASED_COURSE_CREATED_EVENT, namespace = TARGET_NAMESPACE)
	public CourseCreationResponse WebBasedCourseCreationRequestCompleted(
			WebBasedCourseCreationRequest webBasedCourseCreationRequest)  {
		
		CourseCreationResponse courseCreationResponse = new CourseCreationResponse();
		
		log.debug("--------------Calling WS WebBasedCourseCreationImpl.WebBasedCourseCreationRequestCompleted()------------");
		try{
		
			if(webBasedCourseCreationRequest!=null && webBasedCourseCreationRequest.getCourseTitleName()!=null && webBasedCourseCreationRequest.getCourseTitleName().length()>0 
					&& webBasedCourseCreationRequest.getKeywords()!=null && webBasedCourseCreationRequest.getKeywords().length()>0){
				
				Course wsCourse = new SynchronousCourse();
				
				//Default values setting to Course
				wsCourse.setCredithour("");
				wsCourse.setBusinessUnitId(1);
				wsCourse.setBusinessUnitName("All");
				wsCourse.setVersion("1.0");
				wsCourse.setBussinesskey("");
				wsCourse.setCourseId("");
				wsCourse.setCourseStatus(Course.PUBLISHED);
				wsCourse.setCourseType(COURSETYPE_SYNCHRONOUS);
				wsCourse.setProductprice(1.0);
				wsCourse.setDeliveryMethod(DEFAULT_DELIVERYMETHOD);
				wsCourse.setDeliveryMethodId(DEFAULT_DELIVERYMETHODID);
				wsCourse.setCurrency("USD");
				wsCourse.setApprovalNumber("1");
				
				
				ContentOwner contentowner = new ContentOwner();
				contentowner.setId(Long.parseLong(CONTENT_OWNER_ID));
				//contentowner.setId(Long.parseLong("62978"));
				wsCourse.setContentOwner(contentowner);
				
				//Description
				if(webBasedCourseCreationRequest.getDescription()!=null){
					wsCourse.setDescription(webBasedCourseCreationRequest.getDescription());
				}
				else{
					wsCourse.setDescription("");	
				}
				
				//Course Title/Name
				wsCourse.setCourseTitle(webBasedCourseCreationRequest.getCourseTitleName());
				wsCourse.setName(webBasedCourseCreationRequest.getCourseTitleName());
				
				//Exam information
				if(webBasedCourseCreationRequest.getExamInformation()!=null)
					wsCourse.setFinalExamInfo(webBasedCourseCreationRequest.getExamInformation());
				else
					wsCourse.setFinalExamInfo("");
				
				//Keywords
				wsCourse.setKeywords(webBasedCourseCreationRequest.getKeywords());
				
				//Language
				Long langId = 1L;//set to default En
				
				try{
				List<Language> languages = courseAndCourseGroupService.getAllLanguages();
				if(languages!=null && !languages.isEmpty()){
					for (Iterator iterator = languages.iterator(); iterator.hasNext();) {
						Language language = (Language) iterator.next();
						
						if(language!=null && language.getDisplayName()!=null && webBasedCourseCreationRequest.getLanguage()!=null 
								&& language.getDisplayName().toUpperCase().equals(webBasedCourseCreationRequest.getLanguage().toUpperCase())){
							langId = language.getId();
							break;
						}
					}
				}
				}catch(Exception sube){
					// in case of any exception, default language will be set 'English'
				}
				
				Language lang = new Language();
				lang.setId(langId);
				wsCourse.setLanguage(lang);
				
				//Learning Objective
				if(webBasedCourseCreationRequest.getObjectives()!=null){
					wsCourse.setLearningObjectives(webBasedCourseCreationRequest.getObjectives());
				}
				else{
					wsCourse.setLearningObjectives("");
				}
				
				//Features (Field not in LMS but required in LCMS)
				if(webBasedCourseCreationRequest.getFeatures()!=null){
					wsCourse.setTopicsCovered(webBasedCourseCreationRequest.getFeatures());
				}
				
				Course retCourse = courseAndCourseGroupService.addCourse(wsCourse);
				
				if (retCourse == null) {
					log.debug("Save Course fail........");
					courseCreationResponse.setResponseMessage(RESULT_FAILURE);
					courseCreationResponse.setResponseDetail("Exception: Course could not be created.");
				}
				else{
					courseCreationResponse.setResponseMessage(RESULT_SUCCESS);
				}
			}
			else{
				courseCreationResponse.setResponseMessage(RESULT_FAILURE);
				courseCreationResponse.setResponseDetail("Validation: Mandatory data input required (Course Title/Name, Keywords).");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			courseCreationResponse.setResponseMessage(RESULT_FAILURE);
			courseCreationResponse.setResponseDetail("Exception: "+e.toString());
		}
		
		return courseCreationResponse;
	}


    public CourseAndCourseGroupService getCourseAndCourseGroupService() {
    	return courseAndCourseGroupService;
    }

    public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
    	this.courseAndCourseGroupService = courseAndCourseGroupService;
    }



}
