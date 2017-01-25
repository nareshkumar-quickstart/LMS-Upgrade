package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanResponse;

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
public class TrainingPlanSoapEndpoint {

	private static final Logger log = Logger.getLogger(TrainingPlanSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our TrainingPlanServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String TRAINING_PLAN_TARGET_NAMESPACE = "http://trainingplan.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	private static final String CREATE_TRAININGPLAN_LOCAL_PART = "CreateTrainingPlanRequest";
	private static final String ASSIGN_TRAININGPLAN_TO_LEARNER_LOCAL_PART  = "AssignTrainingPlanToLearnerRequest";
	private static final String ASSIGN_TRAININGPLAN_TO_USERGROUP_LOCAL_PART  = "AssignTrainingPlanToUserGroupRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	public LmsApiAuthenticationService getLmsApiAuthenticationService() {
		return lmsApiAuthenticationService;
	}

	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}

	//@PayloadRoot(namespace = TRAINING_PLAN_TARGET_NAMESPACE, localPart = CREATE_TRAININGPLAN_LOCAL_PART)
	public CreateTrainingPlanResponse createTrainingPlan(CreateTrainingPlanRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for createTrainingPlan");
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			
		} catch (Exception e) {
			
		}
		
		CreateTrainingPlanResponse response = new CreateTrainingPlanResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = TRAINING_PLAN_TARGET_NAMESPACE, localPart = ASSIGN_TRAININGPLAN_TO_LEARNER_LOCAL_PART)
	public AssignTrainingPlanToLearnerResponse assignTrainingPlantoLearner(AssignTrainingPlanToLearnerRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for assignTrainingPlantoLearner");
		
		AssignTrainingPlanToLearnerResponse response = new AssignTrainingPlanToLearnerResponse();
		return response;
		
	}
	
	//@PayloadRoot(namespace = TRAINING_PLAN_TARGET_NAMESPACE, localPart = ASSIGN_TRAININGPLAN_TO_USERGROUP_LOCAL_PART)
	public AssignTrainingPlanToUserGroupResponse assignTrainingPlantoUserGroup(AssignTrainingPlanToUserGroupRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for assignTrainingPlantoUserGroup");
		
		AssignTrainingPlanToUserGroupResponse response = new AssignTrainingPlanToUserGroupResponse();
		return response;
		
	}
	
}
