package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiEnrollmentService;
import com.softech.vu360.lms.service.lmsapi.response.LmsApiEnrollmentResponseService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEnrollmentValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.DuplicatesEnrollment;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrolledCourses;

/**
 * Endpoints are the key concept of Spring Web Service’s server support. Unlike controllers, whose handler methods are directly tied
 * to HTTP requests and responses, Spring Web Service SOAP endpoints can serve SOAP requests made via HTTP, XMPP, SMTP, JMS, and 
 * more. Just like @Controller marks a controller whose @RequestMapping methods should be scanned and mapped to requests, @Endpoint 
 * marks an endpoint whose @org.springframework.ws.server.endpoint.annotation.PayloadRoot methods, 
 * @org.springframework.ws.soap.server.endpoint.annotation.SoapAction methods, and/or 
 * @org.springframework.ws.soap.addressing.server.annotation.Action methods are handlers for incoming SOAP requests on any protocol. 
 * Endpoint methods’ parameters correspond to elements of the request, whereas return types indicate response contents.
 * 
 * @author basit.ahmed
 *
 */
@Endpoint
public class EnrollmentSoapEndpoint {

	private static final Logger log = Logger.getLogger(EnrollmentSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our EnrollmentServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String ENROLLMENT_TARGET_NAMESPACE = "http://enrollment.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	private static final String LEARNER_COURSES_ENROLL_LOCAL_PART = "LearnerCoursesEnrollRequest";
	private static final String BULK_ENROLLMENT_LOCAL_PART = "BulkEnrollmentRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	@Autowired
	private LmsApiEnrollmentService lmsApiEnrollmentService;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	@Autowired
	private LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService;
	
	@Autowired
	private LmsApiEnrollmentResponseService lmsApiEnrollmentResponseService;
	
	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	public void setLmsApiEnrollmentService(LmsApiEnrollmentService lmsApiEnrollmentService) {
		this.lmsApiEnrollmentService = lmsApiEnrollmentService;
	}
	
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public void setLmsApiEnrollmentValidationService(LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService) {
		this.lmsApiEnrollmentValidationService = lmsApiEnrollmentValidationService;
	}
	
	public void setLmsApiEnrollmentResponseService(LmsApiEnrollmentResponseService lmsApiEnrollmentResponseService) {
		this.lmsApiEnrollmentResponseService = lmsApiEnrollmentResponseService;
	}

	@PayloadRoot(namespace = ENROLLMENT_TARGET_NAMESPACE, localPart = LEARNER_COURSES_ENROLL_LOCAL_PART)
	public LearnerCoursesEnrollResponse learnerCoursesEnroll(LearnerCoursesEnrollRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for learnerCoursesEnroll");
		
		LearnerCoursesEnrollResponse response = null;
		boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		DuplicatesEnrollment duplicatesEnrollment = request.getDuplicatesEnrollment();
		if (duplicatesEnrollment == null) {
			duplicatesEnrollment = DuplicatesEnrollment.UPDATE;
		}
		List<LearnerCourses> learnerCoursesList = request.getLearnerCourses();
		try {
			Customer customer = lmsApiAuthenticationService.authenticateByApiKeyAndCustomerCode(apiKey, customerCode);
			String customerGuid = customer.getCustomerGUID();
			VU360User manager = vu360UserService.getUserByGUID(customerGuid);
			lmsApiEnrollmentValidationService.validateLearnerCoursesEnrollRequest(request, manager, customerCode);
			Map<Boolean, List<LearnerCourses>> learnerCoursesMap = lmsApiEnrollmentValidationService.getLearnerCoursesMap(learnerCoursesList);
			List<LearnerEnrolledCourses> learnerEnrolledCoursesResponseList = lmsApiEnrollmentService.enrollLearnerCourses(learnerCoursesMap, customer, manager, notifyLearnersByEmail, duplicatesEnrollment);
			response = lmsApiEnrollmentResponseService.getLearnerCoursesEnrollResponse(learnerEnrolledCoursesResponseList);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			response = lmsApiEnrollmentResponseService.getLearnerCoursesEnrollResponse(errorMessage);
		}
		
		return response;
	}
	
	//@PayloadRoot(namespace = ENROLLMENT_TARGET_NAMESPACE, localPart = BULK_ENROLLMENT_LOCAL_PART)
	public BulkEnrollmentResponse bulkEnrollment(BulkEnrollmentRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for bulkEnrollment");
		
		BulkEnrollmentResponse response = new BulkEnrollmentResponse();
		return response;
	}
	
}
