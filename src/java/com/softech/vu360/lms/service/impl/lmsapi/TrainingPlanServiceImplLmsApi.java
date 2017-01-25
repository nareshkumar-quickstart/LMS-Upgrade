package com.softech.vu360.lms.service.impl.lmsapi;


import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.lmsapi.CourseAndCourseGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.CustomerEntitlementServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.TrainingPlanServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.VU360UserServiceLmsApi;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Course;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Courses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.ResponseTrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResp;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResponsed;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanResponseCourse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanResponseCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.transactionresult.TransactionResultType;


public class TrainingPlanServiceImplLmsApi implements TrainingPlanServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(TrainingPlanServiceImplLmsApi.class.getName());
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	
	private TrainingPlanService trainingPlanService;
	private CourseAndCourseGroupService courseCourseGrpService;
	private CourseAndCourseGroupServiceLmsApi courseAndCourseGroupServiceLmsApi; 
	private VU360UserServiceLmsApi vu360UserServiceLmsApi;
	private CustomerEntitlementServiceLmsApi customerEntitlementServiceLmsApi;
	
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}
	
	public CourseAndCourseGroupServiceLmsApi getCourseAndCourseGroupServiceLmsApi() {
		return courseAndCourseGroupServiceLmsApi;
	}

	public void setCourseAndCourseGroupServiceLmsApi(CourseAndCourseGroupServiceLmsApi courseAndCourseGroupServiceLmsApi) {
		this.courseAndCourseGroupServiceLmsApi = courseAndCourseGroupServiceLmsApi;
	}
	
	
	public com.softech.vu360.lms.model.TrainingPlan createTrainingPlan(String trainingPlanName, String trainingPlanDescription, Customer customer) throws Exception {
	 	
		com.softech.vu360.lms.model.TrainingPlan newTrainingPlan = new com.softech.vu360.lms.model.TrainingPlan();
		newTrainingPlan.setName(trainingPlanName);
		newTrainingPlan.setDescription(trainingPlanDescription);
		newTrainingPlan.setCustomer(customer);
				
		newTrainingPlan= trainingPlanService.addTrainingPlan(newTrainingPlan);
				
		return newTrainingPlan;
		    		
	} //end of createTrainingPlan()
	
	public Map<String, Object> validateCreateTrainingPlanRequest(Customer customer, TrainingPlan trainingPlan) throws Exception {
		 
		String trainingPlanName = trainingPlan.getName();
		if (StringUtils.isEmpty(trainingPlanName) && StringUtils.isBlank(trainingPlanName)){
			String errorMessage = "Training Plan name can not be empty or blank";
			throwException(errorMessage);
		} 
		
		Courses courses = trainingPlan.getCourses();
		List<Course> courseList = courses.getCourse();
		courseAndCourseGroupServiceLmsApi.updateCoursesCache(courseList);
		Map<String, Object> coursesMap = validateRequestCourses(customer, courseList);
		return coursesMap;
		 
	 }
	
	private Map<String, Object> validateRequestCourses(Customer customer, List<Course> courseList) throws Exception {
		 
		List<String> courseGuidList = new ArrayList<String>();
		for (Course course : courseList) {
			String courseGuid = course.getCourseID();
			courseGuidList.add(courseGuid);
		} 
		
		Map<String, Object> coursesMap = courseAndCourseGroupServiceLmsApi.getCoursesMap(customer, courseGuidList);
		return coursesMap;
	 }
	
	public com.softech.vu360.lms.model.TrainingPlan processTrainingPlanCreation(Customer customer, String trainingPlanName, String trainingPlanDescription, List<com.softech.vu360.lms.model.Course> coursesList) throws Exception {
		
		com.softech.vu360.lms.model.TrainingPlan newTrainingPlan = createTrainingPlan(trainingPlanName, trainingPlanDescription, customer);
		if (newTrainingPlan == null) {
			String errorMessage = "Error In Creating training plan For customer: "  + customer.getName();
			throwException(errorMessage);
		} 
		
		List <TrainingPlanCourse> trainingPlanCoursesList = getTrainingPlanCoursesList(newTrainingPlan, coursesList);
		List<TrainingPlanCourse> addedTrainingPlanCoursesList = trainingPlanService.addTrainingPlanCourses(trainingPlanCoursesList);
		if (!addedTrainingPlanCoursesList.isEmpty()) {	
			newTrainingPlan.setCourses(trainingPlanCoursesList);			
		} else {
			String errorMessage = "No course found to add in training Plan.";
			throwException(errorMessage);
		}
		
		newTrainingPlan= trainingPlanService.addTrainingPlan(newTrainingPlan);
		return newTrainingPlan;
		 	
	}
	
	public List<ResponseTrainingPlan> processTrainingPlansMap(Customer customer, Map<TrainingPlan, Object> trainingPlansMap) throws Exception {
		
		List<ResponseTrainingPlan> responseTrainingPlanList = new ArrayList<ResponseTrainingPlan>();
		
		for (Map.Entry<TrainingPlan, Object> trainingPlansMapEntry : trainingPlansMap.entrySet()) {
			
			ResponseTrainingPlan responseTrainingPlan = new ResponseTrainingPlan();
			List<TrainingPlanResponseCourse> trainingPlanResponseCourseList = new ArrayList<TrainingPlanResponseCourse>();
			
			TrainingPlan trainingPlan = trainingPlansMapEntry.getKey();
			String trainingPlanName = trainingPlan.getName();
			String trainingPlanDescription = trainingPlan.getDescription();
			
			try {
				Object value = trainingPlansMapEntry.getValue();
				if (value instanceof Map<?, ?>) { 
					Map<String, Object> coursesMap = (Map<String, Object>) value;
					
					Object invalidCourses = coursesMap.get("invalidCoursesMap");
					if (invalidCourses != null) {
						Map<String, String> invalidCoursesMap = (Map<String, String>) invalidCourses;
						List<TrainingPlanResponseCourse> invalidCoursesErrorList = processCoursesMapForErrors(invalidCoursesMap);
						for (TrainingPlanResponseCourse trainingPlanResponseCourseError : invalidCoursesErrorList) {
							trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);
						}
					}
					
					Object coursesList = coursesMap.get("validCoursesList");
					if (coursesList != null) {
						List<com.softech.vu360.lms.model.Course> validCoursesList = (List<com.softech.vu360.lms.model.Course>)coursesList;
						com.softech.vu360.lms.model.TrainingPlan newTrainingPlan = processTrainingPlanCreation(customer, trainingPlanName, trainingPlanDescription, validCoursesList);
						setCreatedTrainingPlanResponse(newTrainingPlan, responseTrainingPlan, trainingPlanResponseCourseList);
						responseTrainingPlanList.add(responseTrainingPlan);	
					} else {
						String errorMessage = "No course found to add in training Plan.";
						throwException(errorMessage);
					}
					
				} else if (value instanceof String) {
					String errorMessage = (String) value;
					throwException(errorMessage);
				}
			} catch(Exception e) {
				String errorMessage = e.getMessage();
				log.debug(errorMessage);
				ResponseTrainingPlan responseTrainingPlanError = getResponseTrainingPlanForError(responseTrainingPlan, 
						trainingPlanName, trainingPlanDescription, ERROR_CODE_ONE, errorMessage);
				responseTrainingPlanList.add(responseTrainingPlanError);
			}
			
		} //end of for()
		
		return responseTrainingPlanList;
		
	}
	
	private List<TrainingPlanResponseCourse> processCoursesMapForErrors(Map<String, String> coursesMap) {
		
		List<TrainingPlanResponseCourse> trainingPlanResponseCoursesErrorList = new ArrayList<TrainingPlanResponseCourse>();
		for (Map.Entry<String, String> coursesMapEntry : coursesMap.entrySet()) {
			String courseGuid = coursesMapEntry.getKey();
			String errorMessage = coursesMapEntry.getValue();
			
			TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGuid, ERROR_CODE_ONE, errorMessage);
			trainingPlanResponseCoursesErrorList.add(trainingPlanResponseCourseError);
				
		}
		return trainingPlanResponseCoursesErrorList;
	}
	
	private List <TrainingPlanCourse> getTrainingPlanCoursesList(com.softech.vu360.lms.model.TrainingPlan trainingPlan, List<com.softech.vu360.lms.model.Course> coursesList) throws Exception {
		
		List <TrainingPlanCourse> trainingPlanCoursesList = new ArrayList <TrainingPlanCourse>();
		
		for (com.softech.vu360.lms.model.Course course : coursesList) {
			TrainingPlanCourse trainingPlanCourse = new TrainingPlanCourse();
			trainingPlanCourse.setCourse(course);
			trainingPlanCourse.setTrainingPlan(trainingPlan);
			trainingPlanCoursesList.add(trainingPlanCourse);
			
		} //end of for (com.softech.vu360.lms.model.Course newCourse ...)
		
		return trainingPlanCoursesList;
		
	}
	
	private TrainingPlanResponseCourse getTrainingPlanResponseCourse(String courseGUID, String errorCode, String errorMessage) {
		
		TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
		trainingPlanResponseCourse.setCourseID(courseGUID);
		trainingPlanResponseCourse.setErrorCode(errorCode);
		trainingPlanResponseCourse.setErrorMessage(errorMessage);
		return trainingPlanResponseCourse;
	} //end of setCreateTrainingPlanResponseForCourse()
	
	private ResponseTrainingPlan getResponseTrainingPlanForError(ResponseTrainingPlan responseTrainingPlan, String trainingPlanName, String trainingPlanDescription, 
			String errorCode, String errorMessage){
		
		//ResponseTrainingPlan responseTrainingPlanError = new ResponseTrainingPlan();
		responseTrainingPlan.setName(trainingPlanName);
		responseTrainingPlan.setDescription(trainingPlanDescription);
		responseTrainingPlan.setErrorCode(errorCode);
		responseTrainingPlan.setErrorMessage(errorMessage);
		
		return responseTrainingPlan;
	
	}
	
	private boolean setCreatedTrainingPlanResponse(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
			ResponseTrainingPlan responseTrainingPlan, List<TrainingPlanResponseCourse> trainingPlanResponseCourseList) {
		
		long trainingPlanId = trainingPlan.getId();
		BigInteger newTrainingPlanId = BigInteger.valueOf(trainingPlanId);
		responseTrainingPlan.setId(newTrainingPlanId);
		responseTrainingPlan.setName(trainingPlan.getName());
		responseTrainingPlan.setDescription(trainingPlan.getDescription());
		responseTrainingPlan.setErrorCode(ERROR_CODE_ZERO);
		responseTrainingPlan.setErrorMessage("");
		
		List<TrainingPlanCourse> trainingPlanCoursesList = trainingPlan.getCourses();
		
		for (TrainingPlanCourse tpCourse: trainingPlanCoursesList) {
			com.softech.vu360.lms.model.Course course = tpCourse.getCourse();
			String courseGUID = course.getCourseGUID();
			TrainingPlanResponseCourse trainingPlanResponseCourse = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ZERO, "");
			trainingPlanResponseCourseList.add(trainingPlanResponseCourse);	
			responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
		}
		
		return true;
	}
	
	//Assign Training Plan to Learner module
	
	public Map<String, Object> validateAssignTrainingPlanToLearnerRequest(BigInteger trainingPlanId, String startDate, String endDate, String customerCode, String customerGuid, boolean notifyLearnersByEmail) throws Exception {
			
		
		log.debug("validateAssignTrainingPlanRequest() start");
		
		Map<String, Object> trainingPlanValidationMap = new LinkedHashMap<String, Object>();
		String errorMessage = null;
			
		try {
			if (StringUtils.isEmpty(startDate) && StringUtils.isNotBlank(startDate)){
				errorMessage = "Training Plan Start Date cannot be blank or empty";
				throwException(errorMessage);
			}
				
			if (StringUtils.isEmpty(endDate) && StringUtils.isNotBlank(endDate)){
				errorMessage = "Training Plan End Date cannot be blank or empty";
				throwException(errorMessage);
			}
				
			validateTrainingPlanDate(startDate, endDate);
				
			if( trainingPlanId == null) {
				errorMessage = "TrainingPlan Id can not be empty";
				throwException(errorMessage);
			}
			
			com.softech.vu360.lms.model.TrainingPlan currentTrainingPlan = findTrainingPlanById(trainingPlanId.longValue());
			
			if (currentTrainingPlan == null) {
				errorMessage = "No training Plan found for id: " + trainingPlanId;
				throwException(errorMessage);
			}
			
			if (!isTrainingPlanBelongToCustomer(currentTrainingPlan, customerCode)) {
				errorMessage = "Training plan does not belong to Customer: " + customerCode;
				throwException(errorMessage);
			}
				
			if (notifyLearnersByEmail) {
				VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customerGuid);
				if (manager == null) {
					errorMessage = "Email can not send to learner(s). No manager found for customer : " + customerCode ;
					throwException(errorMessage);
				} else {
					trainingPlanValidationMap.put("manager", manager);
				}
			}
			
			trainingPlanValidationMap.put("trainingPlan", currentTrainingPlan);
				
		} catch (Exception e) {
			errorMessage = e.getMessage();
			trainingPlanValidationMap.put(trainingPlanId.toString(), errorMessage);
			return trainingPlanValidationMap;
		}
		
		log.debug("validateAssignTrainingPlanRequest() end");
		return trainingPlanValidationMap;
	}
	
	public Map<String, Object> validateAssignTrainingPlanToUserGroupRequest(BigInteger trainingPlanId, String startDate, String endDate, String customerCode, String customerGuid, boolean notifyLearnersByEmail) throws Exception {
			
		Map<String, Object> trainingPlanValidationMap = validateAssignTrainingPlanToLearnerRequest(trainingPlanId, startDate, endDate, customerCode, customerGuid, notifyLearnersByEmail);
		return trainingPlanValidationMap;
	}
		
	public boolean validateTrainingPlanDate(String startDate, String endDate) throws Exception {
			
		String errorMessage = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date tpStartDate = formatter.parse(startDate);
			Date tpEndDate = formatter.parse(endDate);
			if( tpEndDate.before(tpStartDate) ) {
				errorMessage = "Training Plan End Date cannot be before Training Plan's Start Date.";
				throwException(errorMessage);
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			throwException(errorMessage);
		}
		return true;
	} //end of trainingPlanDateValidations()
		
	public com.softech.vu360.lms.model.TrainingPlan findTrainingPlanById(long trainingPlanId) throws Exception {
			
		try {
			com.softech.vu360.lms.model.TrainingPlan trainingPlan= trainingPlanService.getTrainingPlanByID(trainingPlanId);
			return trainingPlan;
		} catch (Exception e) {
			String errorMessage = "No training Plan found for id: " + trainingPlanId + " --> Exception error: " + e.getMessage();
			throwException(errorMessage);
		}
			
		return null;
	}
		
	public boolean isTrainingPlanBelongToCustomer(com.softech.vu360.lms.model.TrainingPlan trainingPlan, Customer customer) throws Exception {
			
		String customerCode = customer.getCustomerCode();
		return isTrainingPlanBelongToCustomer(trainingPlan, customerCode);
			
	}
		
	public boolean isTrainingPlanBelongToCustomer(com.softech.vu360.lms.model.TrainingPlan trainingPlan, String customerCode) throws Exception {
			
		if (!trainingPlan.getCustomer().getCustomerCode().equals(customerCode)) {
			return false;
		}
			
		return true;
	}
	
	
	public AssignTrainingPlanToLearnerResponse getAssignTrainingPlanToLearnerResponse(Map<String, Object> trainingPlanValidationMap) {  
		
		AssignTrainingPlanToLearnerResponse response = new AssignTrainingPlanToLearnerResponse();
		
		for (Map.Entry<String, Object> trainingPlansMapEntry : trainingPlanValidationMap.entrySet()) {
			String key = trainingPlansMapEntry.getKey();
			Object value = trainingPlansMapEntry.getValue();
			BigInteger trainingPlanId = new BigInteger (key);
			if (value instanceof String) {
				String errorMessage = (String) value;
				log.debug(errorMessage);
				TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, ERROR_CODE_ONE, errorMessage);
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage(errorMessage);
				response.setTrainingPlan(trainingPlanAssignResponsedError);
				return response;
			}
		}
		
		return response;
	}
	
	public AssignTrainingPlanToLearnerResponse getAssignTrainingPlanToLearnerResponse(BigInteger trainingPlanId, Map<String, Object> learnersMap) {
		
		AssignTrainingPlanToLearnerResponse response = new AssignTrainingPlanToLearnerResponse();
		List<TrainingPlanAssignResp> trainingPlanEnrollmentList = getTrainingPlanAssignRespListForInvalidLearners(learnersMap);
		String errorMessage = "No learner found for enrollment";
		log.debug(errorMessage);
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, ERROR_CODE_ONE, errorMessage);
		trainingPlanAssignResponsedError.setTrainingPlanEnrollment(trainingPlanEnrollmentList);
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(errorMessage);
		response.setTrainingPlan(trainingPlanAssignResponsedError);
		
		return response;
	
	}
	
	public List<TrainingPlanAssignResp> getTrainingPlanAssignRespListForInvalidLearners(Map<String, Object> learnersMap) {  
		
		List<TrainingPlanAssignResp> trainingPlanAssignRespListForInvalidLearners = new ArrayList<TrainingPlanAssignResp>();
	
		Object learnersErrorMap = learnersMap.get("invalidLearnersMap");
		if (learnersErrorMap != null) {
			Map<String, String> invalidLearnersMap = (Map<String, String>) learnersErrorMap;
			for (Map.Entry<String, String> invalidLearnersMapEntry : invalidLearnersMap.entrySet()) {
				String userName = invalidLearnersMapEntry.getKey();
				String errorMessage = invalidLearnersMapEntry.getValue();
		        
				TrainingPlanAssignResp trainingPlanEnrollment = getTrainingPlanEnrollmentForError(ERROR_CODE_ONE, errorMessage, userName);
				trainingPlanAssignRespListForInvalidLearners.add(trainingPlanEnrollment);
				
			}
		}
		
		return trainingPlanAssignRespListForInvalidLearners;
	}
	
	
	private TrainingPlanAssignResp getTrainingPlanEnrollmentForError(String errorCode, String errorMessage, String userName) {
		
		TrainingPlanAssignResp trainingPlanEnrollment = new TrainingPlanAssignResp();
		trainingPlanEnrollment.setErrorCode(errorCode);
		trainingPlanEnrollment.setErrorMessage(errorMessage);
		trainingPlanEnrollment.setUserId(userName);
		
		return trainingPlanEnrollment;
		
	}
	
	private TrainingPlanAssignResponsed getTrainingPlanAssignResponsed(BigInteger trainingPlanId, String errorCode, String errorMessage) {
		
		TrainingPlanAssignResponsed trainingPlanAssignResponsed = new TrainingPlanAssignResponsed();
		trainingPlanAssignResponsed.setTrainingPlanId(trainingPlanId);
		trainingPlanAssignResponsed.setErrorCode(errorCode);
		trainingPlanAssignResponsed.setErrorMessage(errorMessage);
		
		return trainingPlanAssignResponsed;
		
	} //end of getTrainingPlanAssignResponsed()
	
	
	public Map<com.softech.vu360.lms.model.Course, Object> getCourseEntitlementMap(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		List<com.softech.vu360.lms.model.Course> courseList = new ArrayList<com.softech.vu360.lms.model.Course>();
		Customer customer = trainingPlan.getCustomer();
		List<TrainingPlanCourse> trainingPlanCourseList = trainingPlan.getCourses();
		for (TrainingPlanCourse trainingPlanCourse : trainingPlanCourseList) {
			if (trainingPlanCourse == null) {
				log.debug("TrainingPlanCourse found null in TrainingPlan: " + trainingPlan.getId() + ", " + trainingPlan.getName());
				continue;
			}
			courseList.add(trainingPlanCourse.getCourse());
		}
		
		courseAndCourseGroupServiceLmsApi.refreshCoursesInCache(courseList);
		Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = getCourseEntitlementMap(customer, courseList, enrollmentStartDate, enrollmentEndDate);
		return courseEntitlementMap;
	}
	
	private Map<com.softech.vu360.lms.model.Course, Object> getCourseEntitlementMap(Customer customer, 
			List<com.softech.vu360.lms.model.Course> courseList, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		log.debug("getCourseEntitlementMap() start");
		Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = new HashMap<com.softech.vu360.lms.model.Course, Object>();
		for (com.softech.vu360.lms.model.Course course : courseList) {
			try {
				List<CustomerEntitlement> customerEntitlementList = customerEntitlementServiceLmsApi.getValidCustomerEntitlementList(customer, course, enrollmentStartDate, enrollmentEndDate);
				courseEntitlementMap.put(course, customerEntitlementList);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				courseEntitlementMap.put(course, errorMessage);
			}
		}
		log.debug("getCourseEntitlementMap() end");
		return courseEntitlementMap;
	}
	
	
	public List<TrainingPlanAssignResp> processLearnerCoursesEnrollmentResultMapForResponse(Map<String, Object> learnersMap, Map<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMap) {
		
		
		List<TrainingPlanAssignResp> trainingPlanEnrollmentList = getTrainingPlanAssignRespListForInvalidLearners(learnersMap);
		
		for (Map.Entry<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMapEntry : learnerCoursesEnrollmentResultMap.entrySet()) {
			
			TrainingPlanAssignResp trainingPlanEnrollment = new TrainingPlanAssignResp();
			TrainingPlanResponseCourses trainingPlanResponseCourses = new TrainingPlanResponseCourses();
			List<TrainingPlanResponseCourse> trainingPlanResponseCourseList = new ArrayList<TrainingPlanResponseCourse>();
			
			Learner learner = learnerCoursesEnrollmentResultMapEntry.getKey();
			Map<String, Object> coursesEnrollmentResultMap = learnerCoursesEnrollmentResultMapEntry.getValue();
			
			Object successfullCoursesEnrolledResult = coursesEnrollmentResultMap.get("successfullCoursesEnrolledList");
			Object unsuccessfullCoursesResult = coursesEnrollmentResultMap.get("unSuccessfulCoursesMap");
			
			if (successfullCoursesEnrolledResult != null) {
				List<LearnerEnrollment> successfullCoursesEnrolledList = (List<LearnerEnrollment>)successfullCoursesEnrolledResult;
				for (LearnerEnrollment learnerEnrollment : successfullCoursesEnrolledList) {
					
					TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
					String courseGUID = learnerEnrollment.getCourse().getCourseGUID();
					trainingPlanResponseCourse.setErrorCode(ERROR_CODE_ZERO);
					trainingPlanResponseCourse.setErrorMessage("");
					trainingPlanResponseCourse.setCourseID(courseGUID);
					trainingPlanResponseCourseList.add(trainingPlanResponseCourse);
				}
			}
			
			if (unsuccessfullCoursesResult != null) {
				Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = (Map<com.softech.vu360.lms.model.Course, String>)unsuccessfullCoursesResult;
				for (Map.Entry<com.softech.vu360.lms.model.Course, String> enrollErrorMapEntry : enrollErrorMap.entrySet()) {
					//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					com.softech.vu360.lms.model.Course course = enrollErrorMapEntry.getKey();
					String errorMessage = enrollErrorMapEntry.getValue();
					String courseGUID = course.getCourseGUID();
					TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
					trainingPlanResponseCourse.setErrorCode(ERROR_CODE_ONE);
					trainingPlanResponseCourse.setErrorMessage(errorMessage);
					trainingPlanResponseCourse.setCourseID(courseGUID);
					
					trainingPlanResponseCourseList.add(trainingPlanResponseCourse);
				}
			}
			
			trainingPlanResponseCourses.setTrainingPlanCourse(trainingPlanResponseCourseList);
			
			trainingPlanEnrollment.setUserId(learner.getVu360User().getUsername());
			trainingPlanEnrollment.setTrainingPlanCourses(trainingPlanResponseCourses);
			trainingPlanEnrollment.setErrorCode(ERROR_CODE_ZERO);
			trainingPlanEnrollment.setErrorMessage("");
			
			trainingPlanEnrollmentList.add(trainingPlanEnrollment);
				
		} //end of for()
		
		return trainingPlanEnrollmentList;
	}
	
	public Map<Learner, List<LearnerEnrollment>> getLearnerEnrollmentEmailMap(Map<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMap) {
		
		Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap = new HashMap<Learner, List<LearnerEnrollment>>();
		
		for (Map.Entry<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMapEntry : learnerCoursesEnrollmentResultMap.entrySet()) {
			
			Learner learner = learnerCoursesEnrollmentResultMapEntry.getKey();
			Map<String, Object> coursesEnrollmentResultMap = learnerCoursesEnrollmentResultMapEntry.getValue();
			
			Object successfullCoursesEnrolledResult = coursesEnrollmentResultMap.get("successfullCoursesEnrolledList");
			
			if (successfullCoursesEnrolledResult != null) {
				List<LearnerEnrollment> successfullCoursesEnrolledList = (List<LearnerEnrollment>)successfullCoursesEnrolledResult;
				if (!successfullCoursesEnrolledList.isEmpty()) {
					learnerEnrollmentEmailMap.put(learner, successfullCoursesEnrolledList);
				}
			}
			
		} //end of for()
		
		return learnerEnrollmentEmailMap;
	}
	
	public void setTrainingPlanAssignment(com.softech.vu360.lms.model.TrainingPlan trainingPlan, List<LearnerEnrollment> learnerEnrollmentList) {
		
		TrainingPlanAssignment tpa = new TrainingPlanAssignment();
		tpa.setTrainingPlan(trainingPlan);
		tpa.setLearnerEnrollments(learnerEnrollmentList);
		trainingPlanService.addTrainingPlanAssignments(tpa);
	}
	
	// Assign Training plan to User Group
	
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(Map<String, Object> trainingPlanValidationMap) throws Exception {  
				
		AssignTrainingPlanToUserGroupResponse response = new AssignTrainingPlanToUserGroupResponse();
		List<BigInteger> userGroupIdList = null;
		
		Object userGroupList = trainingPlanValidationMap.get("userGroupIdList");
		if (userGroupList != null) {
			userGroupIdList = (List<BigInteger>)userGroupList;
		}
			
		for (Map.Entry<String, Object> trainingPlansMapEntry : trainingPlanValidationMap.entrySet()) {
			String key = trainingPlansMapEntry.getKey();
			Object value = trainingPlansMapEntry.getValue();
			BigInteger trainingPlanId = new BigInteger (key);
			if (value instanceof String) {
				String errorMessage = (String) value;
				log.debug(errorMessage);
				TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, errorMessage, userGroupIdList);
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage(errorMessage);
				response.setTrainingPlan(trainingPlanAssignResponsedError);
				return response;
			}
		}		
		return response;
	}
	
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(BigInteger trainingPlanId, List<BigInteger> userGroupIdList, Map<String, Object> learnersMap) throws Exception {
		
		List<TrainingPlanAssignResp> trainingPlanEnrollmentList = getTrainingPlanAssignRespListForInvalidLearners(learnersMap);
		String errorMessage = "No learner found for enrollment";
		log.debug(errorMessage);
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, errorMessage, userGroupIdList);
		trainingPlanAssignResponsedError.setTrainingPlanEnrollment(trainingPlanEnrollmentList);
		AssignTrainingPlanToUserGroupResponse response  = getAssignTrainingPlanToUserGroupResponse(trainingPlanAssignResponsedError, TransactionResultType.FAILURE, errorMessage);
		return response;
	
	}
	
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(BigInteger trainingPlanId, String errorMessage, List<BigInteger> userGroupIdList) throws Exception {
		
		log.debug(errorMessage);
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, errorMessage, userGroupIdList);
		AssignTrainingPlanToUserGroupResponse response  = getAssignTrainingPlanToUserGroupResponse(trainingPlanAssignResponsedError, TransactionResultType.FAILURE, errorMessage);
		return response;
	
	}
	
	public TrainingPlanAssignResponsed getTrainingPlanAssignResponsed(BigInteger trainingPlanId, String errorMessage, List<BigInteger> userGroupIdList) throws Exception {  
		
		AssignTrainingPlanToUserGroupResponse response = new AssignTrainingPlanToUserGroupResponse();
		List<BigInteger> responseUserGroupIdList = new ArrayList<BigInteger>();	
		
		for (BigInteger userGroupId : userGroupIdList) {
			if (userGroupId == null) {
				continue;
			}
			responseUserGroupIdList.add(userGroupId);
		}
		
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, ERROR_CODE_ONE, errorMessage);
		trainingPlanAssignResponsedError.setUserGroupId(responseUserGroupIdList);
		
		return trainingPlanAssignResponsedError;
		
	}
	
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(
			TrainingPlanAssignResponsed trainingPlanAssignResponsed, TransactionResultType transactionResult, 
			String transactionResultMessage) throws Exception {
		
		AssignTrainingPlanToUserGroupResponse response = new AssignTrainingPlanToUserGroupResponse();
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(transactionResultMessage);
		response.setTrainingPlan(trainingPlanAssignResponsed);
		return response;
		
	}
	
	private void throwException(String error) throws Exception {
			log.debug(error);
			throw new Exception(error);
	}
	
	
		
}
