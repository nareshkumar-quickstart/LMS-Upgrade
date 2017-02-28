
package com.softech.vu360.lms.webservice.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiCustomerService;
import com.softech.vu360.lms.service.lmsapi.LmsApiDistributorService;
import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.SecurityAndRolesServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.UserGroupServiceLmsApi;
import com.softech.vu360.lms.util.LmsApiEmailAsyncTask;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.webservice.lmsapi.LMSAPIWS;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByHierarchyRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByHierarchyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByParentIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByParentIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.DeleteOrganizationGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.DeleteOrganizationGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupByIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupByIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupIdByNameRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupIdByNameResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.UpdateOrganizationGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.UpdateOrganizationGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.AddUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.AddUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.DeleteUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.DeleteUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupByIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupByIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupIdByNameRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupIdByNameResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.UpdateUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.UpdateUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Company;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Manager;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.RegisterCustomer;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.RegisterCustomers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.EnrolledCourse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.EnrolledCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.FailedCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrollCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrolledCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.Learners;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.AddOrgGroupByParentIdOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.AddOrgGroupByParentIdOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.DeleteOrganizationGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.DeletedOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.GetOrgGroupById;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.GetOrgGroupIdByName;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.InvalidOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.NewOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrgGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ResponseOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.UpdateOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.ResponseUserSecurityRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.ResponseUserSecurityRoles;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.UserSecurityRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.UserSecurityRoles;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Course;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Courses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.ResponseTrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.ResponseTrainingPlans;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResp;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResponsed;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanLearner;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanResponseCourse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanResponseCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanToLearner;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlans;
import com.softech.vu360.lms.webservice.message.lmsapi.types.transactionresult.TransactionResultType;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.Users;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.DeleteUserGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.DeletedUserGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.GetUserGroupById;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.GetUserGroupIdByName;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.NewUserGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.ResponseUserGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.UpdatedUserGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.UserGroup;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * The LMSAPIWSImpl class use as a endpoint that provide various methods through which you can perform  coreLMS 
 * operations. Like creation of Training plan, Adding Customer, Adding and updating user etc.  
 * 
 * @Enpoint is a specialised version of the standard Spring @Component annotation and allows this class to get picked 
 * up and registered by Springs component scanning.
 * 
 * @author      basit.ahmed
 * @version     %I%, %G%
 * @since       1.0
 */
@Endpoint
public class LMSAPIWSImpl implements LMSAPIWS {
	
	private static final Logger log = Logger.getLogger(LMSAPIWSImpl.class.getName());
	
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	private static final String SUCCESS = "Success";
	
	private static final String NO_LEARNER_FOUND_FOR_ENROLLMENT_ERROR = "No learner found for enrollment";
	private static final String NO_TRAINING_PLAN_FOUND_ERROR  = "No training Plan found";
	private static final String NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR  = "No manager found for customer";
	
	private static final String GROUP_SPLITTER = ">";
	
	private CustomerService customerService;
	private TrainingPlanService trainingPlanService;
	private CourseAndCourseGroupService courseCourseGrpService;
	private EntitlementService entitlementService;
	private SynchronousClassService synchronousClassService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VU360UserService vu360UserService;
	private EnrollmentService enrollmentService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private StatisticsService statsService;
	//private LmsApiService lmsApiService;
	//private DistributorService distributorService;
	private ActiveDirectoryService activeDirectoryService;
	private BrandService brandService;
	private LmsApiDistributorService lmsApiDistributorService;
	private LmsApiCustomerService lmsApiCustomerService;
	
	private SecurityAndRolesServiceLmsApi securityAndRolesServiceLmsApi;
	private OrgGroupServiceLmsApi orgGroupServiceLmsApi;
	private UserGroupServiceLmsApi userGroupServiceLmsApi;
	
	// Shahil Prasla
	//private OrgGroupLearnerGroupService orgGroupService;
	private VU360UserService userService;
	private SecurityAndRolesService securityService;
	private LearnerService learnerService;
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}
	
	public CourseAndCourseGroupService getCourseCourseGrpService() {
		return courseCourseGrpService;
	}

	public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}
	
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}
	
	public StatisticsService getStatsService() {
		return statsService;
	}

	public void setStatsService(StatisticsService statsService) {
		this.statsService = statsService;
	}
	
	//public LmsApiService getLmsApiService() {
		//return lmsApiService;
	//}

	//public void setLmsApiService(LmsApiService lmsApiService) {
		//this.lmsApiService = lmsApiService;
	//}
	
	/*public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}*/
	
	public LmsApiDistributorService getLmsApiDistributorService() {
		return lmsApiDistributorService;
	}

	public void setLmsApiDistributorService(LmsApiDistributorService lmsApiDistributorService) {
		this.lmsApiDistributorService = lmsApiDistributorService;
	}
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	public LmsApiCustomerService getLmsApiCustomerService() {
		return lmsApiCustomerService;
	}

	public void setLmsApiCustomerService(LmsApiCustomerService lmsApiCustomerService) {
		this.lmsApiCustomerService = lmsApiCustomerService;
	}
	
	public SecurityAndRolesServiceLmsApi getSecurityAndRolesServiceLmsApi() {
		return securityAndRolesServiceLmsApi;
	}

	public void setSecurityAndRolesServiceLmsApi(
			SecurityAndRolesServiceLmsApi securityAndRolesServiceLmsApi) {
		this.securityAndRolesServiceLmsApi = securityAndRolesServiceLmsApi;
	}

	//Shahil
	public VU360UserService getUserService() {
		return userService;
	}

	public void setUserService(VU360UserService userService) {
		this.userService = userService;
	}

	public SecurityAndRolesService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityAndRolesService securityService) {
		this.securityService = securityService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

	public void setOrgGroupServiceLmsApi(OrgGroupServiceLmsApi orgGroupServiceLmsApi) {
		this.orgGroupServiceLmsApi = orgGroupServiceLmsApi;
	}

	public void setUserGroupServiceLmsApi(UserGroupServiceLmsApi userGroupServiceLmsApi) {
		this.userGroupServiceLmsApi = userGroupServiceLmsApi;
	}

	@Override
	@PayloadRoot(localPart = CREATE_TRAININGPLAN_EVENT, namespace = TRAINING_PLAN_TARGET_NAMESPACE)
	
	 /**
     * This method creates a new training plan if the provided request is valid. The request argument must specify a 
     * valid request {@link CreateTrainingPlanRequest}. 
     * <p>
     * This method returns immediately if the request is not valid with the appropriate error message. Response 
     * ({@link CreateTrainingPlanResponse}) contains error message in case of any error or status related to newly 
     * created training plan.
     * 
     * @param  request  represent CreateTrainingPlanRequest object. This object contains information necessary to 
     *                  create new raining plan
     * @return          status related to newly created training plan.    
     * @see             CreateTrainingPlanRequest
     * @see             CreateTrainingPlanResponse             
     * @since           1.0
     */
	public CreateTrainingPlanResponse createTrainingPlan(CreateTrainingPlanRequest request) {
		
		log.debug("Create TrainingPlan Request start");
		final String REQUEST_END_MESSAGE = "Create TrainingPlan Request end";
		CreateTrainingPlanResponse response = new CreateTrainingPlanResponse();
		List<ResponseTrainingPlan> responseTrainingPlanList = new ArrayList<ResponseTrainingPlan>();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		try {
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
				TrainingPlans trainingPlans = request.getTrainingPlans();
				List<TrainingPlan> trainingPlanList = trainingPlans.getTrainingPlan();
				//commonValidation(customer, key);
				for (TrainingPlan trainingPlan: trainingPlanList) {
					
					List<TrainingPlanResponseCourse> trainingPlanResponseCourseList = new ArrayList<TrainingPlanResponseCourse>();
					String trainingPlanName = trainingPlan.getName();
					String trainingPlanDescription = trainingPlan.getDescription();
					ResponseTrainingPlan responseTrainingPlan = new ResponseTrainingPlan();
					Courses courses = trainingPlan.getCourses();
					List<Course> courseList = courses.getCourse();
					
					if (StringUtils.isNotEmpty(trainingPlanName) && StringUtils.isNotBlank(trainingPlanName)){
						trainingPlanName = trainingPlanName.trim();
					} else {
						String errorMessage = "Training Plan name can not be empty or blank";
						log.debug(errorMessage);
						ResponseTrainingPlan responseTrainingPlanError = getResponseTrainingPlanForError(responseTrainingPlan, 
								trainingPlanName, trainingPlanDescription, ERROR_CODE_ONE, errorMessage);
						responseTrainingPlanList.add(responseTrainingPlanError);
						continue;
					}
					if (StringUtils.isNotEmpty(trainingPlanDescription)){
						trainingPlanDescription = trainingPlanDescription.trim();
					}
					
					updateCoursesCache(courseList);
					List<com.softech.vu360.lms.model.Course> validCourseList = validateCreateTrainingPlanRequest(customer, courseList, trainingPlanResponseCourseList, responseTrainingPlan);
					if (validCourseList.isEmpty()) {
						String errorMessage = "No course found to add in training Plan.";
						log.debug(errorMessage);
						ResponseTrainingPlan responseTrainingPlanError = getResponseTrainingPlanForError(responseTrainingPlan, 
								trainingPlanName, trainingPlanDescription, ERROR_CODE_ONE, errorMessage);
						responseTrainingPlanList.add(responseTrainingPlanError);
						continue;
					}
					
					com.softech.vu360.lms.model.TrainingPlan newTrainingPlan = createTrainingPlan(trainingPlanName, trainingPlanDescription, customer);
					if (newTrainingPlan != null) {
						long trainingPlanId = newTrainingPlan.getId();
						BigInteger newTrainingPlanId = BigInteger.valueOf(trainingPlanId);
						responseTrainingPlan.setId(newTrainingPlanId);
						responseTrainingPlan.setName(newTrainingPlan.getName());
						responseTrainingPlan.setDescription(trainingPlanDescription);
						responseTrainingPlan.setErrorCode(ERROR_CODE_ZERO);
						responseTrainingPlan.setErrorMessage("");
					} else {
						String errorMessage = "Error In Creating training plan For customer: "  + customer.getName();
						log.debug(errorMessage);
						ResponseTrainingPlan responseTrainingPlanError = getResponseTrainingPlanForError(responseTrainingPlan, 
								trainingPlanName, trainingPlanDescription, ERROR_CODE_ONE, errorMessage);
						responseTrainingPlanList.add(responseTrainingPlanError);
						continue;
					}
					
					boolean result = addCoursesToTrainingPlan(newTrainingPlan, validCourseList, trainingPlanResponseCourseList, responseTrainingPlan);
					if (result) {
						responseTrainingPlanList.add(responseTrainingPlan);
					} else {
						String errorMessage = "No course found to add in training Plan";
						log.debug(errorMessage);
						ResponseTrainingPlan responseTrainingPlanError = getResponseTrainingPlanForError(responseTrainingPlan, 
								trainingPlanName, trainingPlanDescription, ERROR_CODE_ONE, errorMessage);
						responseTrainingPlanList.add(responseTrainingPlanError);
						continue;
					}
				} //end of for (TrainingPlan trainingPlan: trainingPlanList)
			
		} catch(Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
		ResponseTrainingPlans responseTrainingPlans = new ResponseTrainingPlans();
		responseTrainingPlans.setResponseTrainingPlan(responseTrainingPlanList);
		response.setResponseTrainingPlans(responseTrainingPlans);
		log.debug(REQUEST_END_MESSAGE);
		return response;
		
	} //end of createTrainingPlan()
	
	
/*	
	private boolean refreshCoursesCache(List<String> courseGuidList) throws Exception {
		try {
			if (courseGuidList != null && !courseGuidList.isEmpty()) {
				String[] courseGuidArray = courseGuidList.toArray(new String[courseGuidList.size()]);
				refreshCoursesCache(courseGuidArray);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
  
  private boolean refreshCoursesCache(String[] courseGuidArray) throws Exception {
		try {
				List<com.softech.vu360.lms.model.Course> refreshCoursesList = courseCourseGrpService.refreshCoursesCache(courseGuidArray);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
*/	
	private boolean refreshCoursesInCache(List<com.softech.vu360.lms.model.Course> courseList) throws Exception {
		try {
			if (courseList != null && !courseList.isEmpty() ) {
				List<String> courseGuidList = new ArrayList<String>();
				for (com.softech.vu360.lms.model.Course course : courseList) {
					String courseGUID = course.getCourseGUID();
					if (!StringUtils.isEmpty(courseGUID) && !StringUtils.isBlank(courseGUID)) {
						courseGuidList.add(courseGUID);
					}
				}
				//refreshCoursesCache(courseGuidList);
			}	
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	private boolean updateCoursesCache(List<Course> courseList) throws Exception {
		try {
			if (courseList != null && !courseList.isEmpty() ) {
				List<String> courseGuidList = new ArrayList<String>();
				for (Course course : courseList) {
					String courseGUID = course.getCourseID();
					if (!StringUtils.isEmpty(courseGUID) && !StringUtils.isBlank(courseGUID)) {
						courseGuidList.add(courseGUID);
					}
				}
				//refreshCoursesCache(courseGuidList);
			}	
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	private boolean addCoursesToTrainingPlan(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
			List<com.softech.vu360.lms.model.Course> validCourseList, 
			List<TrainingPlanResponseCourse> trainingPlanResponseCourseList, ResponseTrainingPlan responseTrainingPlan) throws Exception {
		
		log.debug("addCoursesToTrainingPlan() start");
		List <TrainingPlanCourse> trainingPlanCoursesList = new ArrayList <TrainingPlanCourse>();
		for (com.softech.vu360.lms.model.Course newCourse : validCourseList) {
			String courseGUID = newCourse.getCourseGUID();
			TrainingPlanCourse trainingPlanCourse = getTrainingPlanCourse(trainingPlan, newCourse);
			if (trainingPlanCourse != null) {
				trainingPlanCoursesList.add(trainingPlanCourse);
			} else {
				if (responseTrainingPlan != null) {
					TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ONE, "No course found to add in this training plan");
					trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);	
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}
			}
		} //end of for (com.softech.vu360.lms.model.Course newCourse ...)
			
		if (!trainingPlanCoursesList.isEmpty()) {
			List<TrainingPlanCourse> trainingPlanCoursesAddedList = addCoursesToTrainingPlan(trainingPlanCoursesList);	
			if (!trainingPlanCoursesAddedList.isEmpty()) {	
				trainingPlan.setCourses(trainingPlanCoursesList);			
			} else {
				return false;
			}
					
			trainingPlan= trainingPlanService.addTrainingPlan(trainingPlan);
			if (responseTrainingPlan != null) {
				for (TrainingPlanCourse tpCourse: trainingPlanCoursesAddedList) {
					com.softech.vu360.lms.model.Course course = tpCourse.getCourse();
					String courseGUID = course.getCourseGUID();
					TrainingPlanResponseCourse trainingPlanResponseCourse = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ZERO, "");
					trainingPlanResponseCourseList.add(trainingPlanResponseCourse);	
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}
			}
		} else {
			return false;	
		}
		log.debug("addCoursesToTrainingPlan() end");
		return true;
	}
	
	private List<com.softech.vu360.lms.model.Course> validateCreateTrainingPlanRequest(Customer customer, 
			List<Course> courseList, List<TrainingPlanResponseCourse> trainingPlanResponseCourseList, 
			ResponseTrainingPlan responseTrainingPlan) throws Exception {
		
		log.debug("validateCreateTrainingPlanRequest() start");
		List<com.softech.vu360.lms.model.Course> newCourseList = new ArrayList<com.softech.vu360.lms.model.Course>();
		GregorianCalendar todayDate = getTodayDate();
		Date enrollmentStartDate = todayDate.getTime();
		for (Course course : courseList) {
			String courseGUID = course.getCourseID();
			if (StringUtils.isEmpty(courseGUID) && StringUtils.isBlank(courseGUID)) {
				if (responseTrainingPlan != null) {
					TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ONE, "Course Id can not be empty");
					trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}
				continue;
			} 
			com.softech.vu360.lms.model.Course newCourse = courseCourseGrpService.getCourseByGUID(courseGUID);
			if (newCourse == null) {
				if (responseTrainingPlan != null) {
					String errorMessage = "No course found for " + courseGUID;
					log.debug(errorMessage);
					
					TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ONE, errorMessage);
					trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}
				continue;
			}
			
			String error = courseLevelEnrollmentValidations(newCourse);
			if (error != null) {
				if (responseTrainingPlan != null) {
					String errorMessage = error;
					TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ONE, errorMessage);
							
					trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);	
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}	
				continue;
			}
			
			Object entitlementError = entitlementLevelEnrollmentValidations(customer, newCourse, enrollmentStartDate, null);
			if (entitlementError instanceof String) {
				if (responseTrainingPlan != null) {
					String entError = (String)entitlementError;
					log.debug(entError);
					TrainingPlanResponseCourse trainingPlanResponseCourseError = getTrainingPlanResponseCourse(courseGUID, ERROR_CODE_ONE, entError);
							
					trainingPlanResponseCourseList.add(trainingPlanResponseCourseError);	
					responseTrainingPlan.setTrainingPlanCourse(trainingPlanResponseCourseList);
				}
				continue;	
			}
			newCourseList.add(newCourse);
		} //end of for (Course course : courseList)
		log.debug("validateCreateTrainingPlanRequest() end");
		return newCourseList;
	}
	
	  /**
     * This method assign training plan to users if the provided request is valid. The request argument must specify 
     * a valid request {@link AssignTrainingPlanToLearnerRequest}.
     * <p>
     * This method returns immediately if the request is not valid with the appropriate error message. Response 
     * ({@link AssignTrainingPlanToLearnerResponse}) contains error message in case of any error or user status 
     * related to training plan. 
     * 
     * @param  request  represent AssignTrainingPlanToLearnerRequest object. This object contains information 
     *                  necessary to  assign training plan to user
     * @return          status related to training plan assigned to user.    
     * @see             AssignTrainingPlanToLearnerRequest
     * @see             AssignTrainingPlanToLearnerResponse             
     * @since           1.0
     */
	@Override
	@PayloadRoot(localPart = ASSIGN_TRAININGPLAN_TO_LEARNER_EVENT, namespace = TRAINING_PLAN_TARGET_NAMESPACE)
	public AssignTrainingPlanToLearnerResponse assignTrainingPlantoLearner(AssignTrainingPlanToLearnerRequest request) {
		
		log.debug("assignTrainingPlantoLearner() Request start");
		final String REQUEST_END_MESSAGE = "assignTrainingPlantoLearner() Request end";
		AssignTrainingPlanToLearnerResponse response = new AssignTrainingPlanToLearnerResponse();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		try {
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
				TrainingPlanLearner learnerList = request.getLearner();
				TrainingPlanToLearner trainingPlan = request.getTrainingPlan();
				boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
				List<String> userNameList = learnerList.getUserId();
				BigInteger trainingPlanId = trainingPlan.getTrainingPlanId();
				String startDate = trainingPlan.getStartDate().toString();
				String endDate = trainingPlan.getEndDate().toString();
				
				List<TrainingPlanAssignResp> trainingPlanEnrollmentList = new ArrayList<TrainingPlanAssignResp>();
				String trainingPlanValidationError = assignTrainingPlanValidations(trainingPlanId, startDate, endDate);
				if (trainingPlanValidationError != null) {
					setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, trainingPlanValidationError, 
							trainingPlanValidationError, trainingPlanEnrollmentList, response, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				TrainingPlanAssignResponsed trainingPlanAssignResponsed = new TrainingPlanAssignResponsed();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				startDate = startDate + " 00:00:00.000";
				endDate = endDate + " 23:59:59.000";
				Date enrollmentStartDate = formatter.parse(startDate);
				Date enrollmentEndDate = formatter.parse(endDate);
				com.softech.vu360.lms.model.TrainingPlan currentTrainingPlan = null;
				try {
					currentTrainingPlan = findTrainingPlanById(trainingPlanId.longValue());
				} catch (Exception e) {
					String errorMessage = "No training Plan found for id: " + trainingPlanId + " --> Exception error: " + e.getMessage();
					setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
							trainingPlanEnrollmentList, response, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				if (currentTrainingPlan == null) {
					setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, NO_TRAINING_PLAN_FOUND_ERROR, 
							NO_TRAINING_PLAN_FOUND_ERROR, trainingPlanEnrollmentList, response, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				if (!currentTrainingPlan.getCustomer().getCustomerCode().equals(customerCode)) {
					String errorMessage = "Training plan does not belong to Customer: " + customerCode;
					setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
							trainingPlanEnrollmentList, response, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				VU360User manager = null;
				if (notifyLearnersByEmail) {
					manager = getVU360UserByCustomerGUID(customer);
					if (manager == null) {
						String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customerCode ;
						setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
								trainingPlanEnrollmentList, response, true);
						log.debug(REQUEST_END_MESSAGE);
						return response;
					}
				}
				
				List<Learner> learnersToBeEnrolledList = getValidLearners(userNameList, trainingPlanEnrollmentList, customerCode);
				if (learnersToBeEnrolledList.isEmpty()) {	
					setAssignTrainingPlanToLearnerErrorResponse(trainingPlanId, ERROR_CODE_ONE, NO_LEARNER_FOUND_FOR_ENROLLMENT_ERROR, 
							NO_LEARNER_FOUND_FOR_ENROLLMENT_ERROR, trainingPlanEnrollmentList, response, true);
					log.debug(REQUEST_END_MESSAGE);
					return response;		
				}
					
				Map<Learner, Map<String, Map<String, ?>>> learnerCourseEnrollMap = new HashMap<Learner, Map<String,Map<String,?>>>();
				
				/**
				 * Here we are getting courses that are in customer entitlement. By doing this we can enroll only those courses
				 * to learner for which we find entitlement. If we check customer entitlement when we assign courses to learner
				 * then its a performance issue, Because for each learner it needs to check whether courses are in cutomer
				 * entitlement or not. And if we found in the first learner that these courses are not in customer 
				 * entitlement then for each learner this check will again run, which is a performance issue.
				 */
				Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = getCourseEntitlementMap(currentTrainingPlan, enrollmentStartDate, enrollmentEndDate);
				
				for (Learner learnerToBeEnroll : learnersToBeEnrolledList) {
					
					/**
					 * We are getting customer entitlement again in this method because after enrollment seats are updated in Database. 
					 * If we use the entitlements that we get using the getCourseEntitlementMap() method above, then we have to update 
					 * the seats on object level which is cumbersome and more error prone.
					 * 
					 * This method will give you a map that contain enrollment error if error occur in enrollment and LearnerEnrollment object
					 * if enrollment is successfull for each learner.
					 */
					Map<String, Map<String, ?>> courseMap = doEnrollment(customer, learnerToBeEnroll, courseEntitlementMap,
							enrollmentStartDate, enrollmentEndDate);	
					learnerCourseEnrollMap.put(learnerToBeEnroll, courseMap);
				}		
			
				if (!learnerCourseEnrollMap.isEmpty()) {
					trainingPlanAssignResponsed = getTrainingPlanAssignResponsed(learnerCourseEnrollMap, 
							trainingPlanAssignResponsed, trainingPlanEnrollmentList, trainingPlanId, currentTrainingPlan, 
							manager, notifyLearnersByEmail);
				}
				response.setTransactionResult(TransactionResultType.SUCCESS);
				response.setTransactionResultMessage("");
				response.setTrainingPlan(trainingPlanAssignResponsed);
		
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
		}
		log.debug(REQUEST_END_MESSAGE);
		return response;
	} //end of assignTrainingPlantoLearner()
	
	private List<Learner> getValidLearners(List<String> userNameList, 
			List<TrainingPlanAssignResp> trainingPlanEnrollmentList, String customerCode) {
		
		log.debug("getValidLearners() start");
		List<Learner> learnersToBeEnrolledList = new ArrayList<Learner>();
		
		// get Learners to be enrolled list
		for (String userName : userNameList) {	
			if (StringUtils.isEmpty(userName) && StringUtils.isBlank(userName)) {
				String errorMessage =  "UserId can not be empty or blank" ;
				log.debug(errorMessage);
				TrainingPlanAssignResp trainingPlanEnrollment = getTrainingPlanEnrollmentForError(ERROR_CODE_ONE, errorMessage, userName);
				trainingPlanEnrollmentList.add(trainingPlanEnrollment);
				continue;
			}
				
			VU360User vu360User = vu360UserService.findUserByUserName(userName);
			if (vu360User == null) {
				if (trainingPlanEnrollmentList != null) {
					String errorMessage =  userName + " not found for customer: " + customerCode ;
					log.debug(errorMessage);
					TrainingPlanAssignResp trainingPlanEnrollment = getTrainingPlanEnrollmentForError(ERROR_CODE_ONE, errorMessage, userName);
					trainingPlanEnrollmentList.add(trainingPlanEnrollment);
				}	
				continue;	
			}
				
			Learner vu360Learner = vu360User.getLearner();
			String learnerCustomerCode = vu360Learner.getCustomer().getCustomerCode();
				
			// Check whether customer has these learners or not
			if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
				if (trainingPlanEnrollmentList != null) {
					String errorMessage = "UserId: " + userName + " not found for customer: " + customerCode ;
					log.debug(errorMessage);
					TrainingPlanAssignResp trainingPlanEnrollment = getTrainingPlanEnrollmentForError(ERROR_CODE_ONE, errorMessage, userName);
					trainingPlanEnrollmentList.add(trainingPlanEnrollment);
				}
				continue;		
			}	 
			learnersToBeEnrolledList.add(vu360Learner);		
		} //end of for()
		log.debug("getValidLearners() end");
		return learnersToBeEnrolledList;	
	}
	
	private void setAssignTrainingPlanToLearnerErrorResponse(BigInteger trainingPlanId, String errorCode, String errorMessage, 
			String transactionResultMessage, List<TrainingPlanAssignResp> trainingPlanEnrollmentList, 
			AssignTrainingPlanToLearnerResponse response, boolean setList ) {
		
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, errorCode, errorMessage);
		if (setList) {
			trainingPlanAssignResponsedError.setTrainingPlanEnrollment(trainingPlanEnrollmentList);
		}
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(transactionResultMessage);
		response.setTrainingPlan(trainingPlanAssignResponsedError);
		log.debug(errorMessage);
	}
	
	@Override
	@PayloadRoot(localPart = ASSIGN_TRAININGPLAN_TO_USERGROUP_EVENT, namespace = TRAINING_PLAN_TARGET_NAMESPACE)
	public AssignTrainingPlanToUserGroupResponse assignTrainingPlantoUserGroup(AssignTrainingPlanToUserGroupRequest request) {
		
		log.debug("assignTrainingPlantoUserGroup() Request start");
		final String REQUEST_END_MESSAGE = "assignTrainingPlantoUserGroup() Request end";
		AssignTrainingPlanToUserGroupResponse response = new AssignTrainingPlanToUserGroupResponse();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		try {
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
				List<BigInteger> userGroupIdList = request.getUserGroupId();   // must be userGruod Ids
				TrainingPlanToLearner trainingPlan = request.getTrainingPlan();
				boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
				BigInteger trainingPlanId = trainingPlan.getTrainingPlanId();
				String startDate = trainingPlan.getStartDate().toString();
				String endDate = trainingPlan.getEndDate().toString();
				List<TrainingPlanAssignResp> trainingPlanEnrollmentList = new ArrayList<TrainingPlanAssignResp>();
				String trainingPlanValidationError = assignTrainingPlanValidations(trainingPlanId, startDate, endDate);
				if (trainingPlanValidationError != null) {
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, trainingPlanValidationError, 
							trainingPlanValidationError, trainingPlanEnrollmentList, response, userGroupIdList, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				TrainingPlanAssignResponsed trainingPlanAssignResponsed = new TrainingPlanAssignResponsed();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				startDate = startDate + " 00:00:00.000";
				endDate = endDate + " 23:59:59.000";
				Date enrollmentStartDate = formatter.parse(startDate);
				Date enrollmentEndDate = formatter.parse(endDate);
				com.softech.vu360.lms.model.TrainingPlan currentTrainingPlan = findTrainingPlanById(trainingPlanId.longValue());
				if (currentTrainingPlan == null) {
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, NO_TRAINING_PLAN_FOUND_ERROR, 
							NO_TRAINING_PLAN_FOUND_ERROR, trainingPlanEnrollmentList, response, userGroupIdList, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				if (!currentTrainingPlan.getCustomer().getCustomerCode().equals(customerCode)) {
					String errorMessage = "Training plan does not belong to Customer: " + customer.getCustomerCode();
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
							trainingPlanEnrollmentList, response, userGroupIdList, false);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				VU360User manager = null;
				if (notifyLearnersByEmail) {
					manager = getVU360UserByCustomerGUID(customer);
					if (manager == null) {
						String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customerCode;
						setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
								trainingPlanEnrollmentList, response, userGroupIdList, true);
						log.debug(REQUEST_END_MESSAGE);
						return response;
					}
				}
				
				// get Learners to be enrolled list
				List<Long> learnerGroupIdList = getLearnerGroupIdList(userGroupIdList, trainingPlanAssignResponsed);	
				if (learnerGroupIdList.isEmpty()) {
					String errorMessage = "No valid UserGroupId is found";	
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, errorMessage, errorMessage, 
							trainingPlanEnrollmentList, response, userGroupIdList, true);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
					
				Long learnerGroupIdArray[] = new Long[learnerGroupIdList.size()];				
				learnerGroupIdArray = learnerGroupIdList.toArray( learnerGroupIdArray);
				List<Learner> learnersToBeEnrolledList = orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
				if (learnersToBeEnrolledList == null || learnersToBeEnrolledList.isEmpty()) {	
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, NO_LEARNER_FOUND_FOR_ENROLLMENT_ERROR,
							"", trainingPlanEnrollmentList, response, userGroupIdList, true);
					log.debug(REQUEST_END_MESSAGE);
					return response;			
				}
				
				learnersToBeEnrolledList = getValidUserGroupLearners(learnersToBeEnrolledList, trainingPlanEnrollmentList, customerCode);
				if (learnersToBeEnrolledList.isEmpty()) {	
					setAssignTrainingPlanToUserGroupErrorResponse(trainingPlanId, ERROR_CODE_ONE, NO_LEARNER_FOUND_FOR_ENROLLMENT_ERROR,
							"", trainingPlanEnrollmentList, response, userGroupIdList, true);
					log.debug(REQUEST_END_MESSAGE);
					return response;		
				}
							
				Map<Learner, Map<String, Map<String, ?>>> learnerCourseEnrollMap = new HashMap<Learner, Map<String,Map<String,?>>>();
				
				/**
				 * Here we are getting courses that are in customer entitlement. By doing this we can enroll only those courses
				 * to learner for which we find entitlement. If we check customer entitlement when we assign courses to learner
				 * then its a performance issue, Because for each learner it needs to check whether courses are in cutomer
				 * entitlement or not. And if we found in the first learner that these courses are not in customer 
				 * entitlement then for each learner this check will again run, which is a performance issue.
				 */
				Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = getCourseEntitlementMap(currentTrainingPlan, enrollmentStartDate, enrollmentEndDate);
				for (Learner learnerToBeEnroll : learnersToBeEnrolledList) {
					
					/**
					 * We are getting customer entitlement again in this method because after enrollment seats are updated in Database. 
					 * If we use the entitlements that we get using the getCourseEntitlementMap() method above, then we have to update 
					 * the seats on object level which is cumbersome and more error prone.
					 * 
					 * This method will give you a map that contain enrollment error if error occur in enrollment and LearnerEnrollment object
					 * if enrollment is successfull for each learner.
					 */
					Map<String, Map<String, ?>> courseMap = doEnrollment(customer, learnerToBeEnroll, courseEntitlementMap,
							enrollmentStartDate, enrollmentEndDate);	
					learnerCourseEnrollMap.put(learnerToBeEnroll, courseMap);
				}		
			
				if (!learnerCourseEnrollMap.isEmpty()) {
					trainingPlanAssignResponsed = getTrainingPlanAssignResponsed(learnerCourseEnrollMap, 
							trainingPlanAssignResponsed, trainingPlanEnrollmentList, trainingPlanId, currentTrainingPlan, 
							manager, notifyLearnersByEmail);
				}
				response.setTransactionResult(TransactionResultType.SUCCESS);
				response.setTransactionResultMessage("");
				response.setTrainingPlan(trainingPlanAssignResponsed);
		
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());	
		}
		log.debug(REQUEST_END_MESSAGE);
		return response;
	} //end of assignTrainingPlantoUserGroup()
	
	private List<Long> getLearnerGroupIdList(List<BigInteger> userGroupIdList, TrainingPlanAssignResponsed trainingPlanAssignResponsed) {
		
		List<BigInteger> responseUserGroupIdList = new ArrayList<BigInteger>();
		List<Long> learnerGroupIdList = new ArrayList<Long>();
		for (BigInteger userGroupId : userGroupIdList) {
			if (userGroupId == null) {
				continue;
			}
			responseUserGroupIdList.add(userGroupId);
			
			// Converting BigInteger to long
			learnerGroupIdList.add((userGroupId.longValue()));
		} //end of for()
		trainingPlanAssignResponsed.setUserGroupId(responseUserGroupIdList);
		return learnerGroupIdList;
	}
	
	private List<Learner> getValidUserGroupLearners(List<Learner> learnersToBeEnrolledList, 
			List<TrainingPlanAssignResp> trainingPlanEnrollmentList, String customerCode){
		
		for (Learner learner : learnersToBeEnrolledList) {
			String learnerCustomerCode = learner.getCustomer().getCustomerCode();
			
			// Check whether customer has these learners or not
			if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
				String userName = learner.getVu360User().getUsername();
				String errorMessage = userName + " not found";
				TrainingPlanAssignResp trainingPlanEnrollmentError = getTrainingPlanEnrollmentForError(ERROR_CODE_ONE, errorMessage, userName);
				trainingPlanEnrollmentList.add(trainingPlanEnrollmentError);
				learnersToBeEnrolledList.remove(learner);
				continue;
			}
		} //end of for()
		return learnersToBeEnrolledList;
	}
	
	private void setAssignTrainingPlanToUserGroupErrorResponse(BigInteger trainingPlanId, String errorCode, String errorMessage, 
			String transactionResultMessage, List<TrainingPlanAssignResp> trainingPlanEnrollmentList, 
			AssignTrainingPlanToUserGroupResponse response, List<BigInteger> userGroupIdList, boolean setList ) {
		
		List<BigInteger> responseUserGroupIdList = new ArrayList<BigInteger>();
		TrainingPlanAssignResponsed trainingPlanAssignResponsedError = getTrainingPlanAssignResponsed(trainingPlanId, errorCode, errorMessage);
		if (setList) {
			trainingPlanAssignResponsedError.setTrainingPlanEnrollment(trainingPlanEnrollmentList);
		}
		
		for (BigInteger userGroupId : userGroupIdList) {
			if (userGroupId == null) {
				continue;
			}
			responseUserGroupIdList.add(userGroupId);
		}
		trainingPlanAssignResponsedError.setUserGroupId(responseUserGroupIdList);
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(transactionResultMessage);
		response.setTrainingPlan(trainingPlanAssignResponsedError);
		log.debug(errorMessage);
	}
	
	@Override
	@PayloadRoot(localPart = ADD_CUSTOMER_EVENT, namespace = CUSTOMER_TARGET_NAMESPACE)
	public AddCustomerResponse addCustomer(AddCustomerRequest request) {
		
		log.debug("Add Customer Request start");
		final String REQUEST_END_MESSAGE = "Add Customer Request end";
		String apiKey = request.getKey();
		AddCustomerResponse response = new AddCustomerResponse();
		RegisterCustomers registerCustomers = new RegisterCustomers();
		List<RegisterCustomer> registerCustomerList = new ArrayList<RegisterCustomer>();
		Customers customers = request.getCustomers();
		BigInteger resellerId = request.getResellerId();
		List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList = customers.getCustomer();
		try {
			
			LmsApiDistributor lmsApiDistributor = findLmsApiDistributorByKey(apiKey);
			resellerApiValidation(lmsApiDistributor, resellerId.longValue());
			Distributor distributor = lmsApiDistributor.getDistributor();
			
			for (com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer: customerList) {
				Customer newCustomer = null;
				try {
					if (addCustomerValidation(customer)) {
						newCustomer = createNewCustomer(distributor, customer);	
					}
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					String userName = customer.getUserName();
					RegisterCustomer registerCustomerError = getRegisterCustomerError(ERROR_CODE_ONE, errorMessage, userName);
					registerCustomerList.add(registerCustomerError);
					continue;
				}
				
				if (newCustomer != null) {
					
					String generatedApiKey = null;
					boolean apiEnabled = customer.isApiEnabled();
					if (apiEnabled) {
						String environment = "Development";
						String privilegeType = null;
						Map<String, String> privilegeMap = new HashMap<String, String>();
						privilegeMap.put("type", privilegeType);
					   
					    JSONObject json = new JSONObject();
					    json.accumulateAll(privilegeMap);
					     
					    generatedApiKey = generateApiKey();
					    //String privilege = json.toString();
					     
					    String privilege = null;
						
						//LmsApiCustomer lmsApiCustomer = addLmsApiCustomer(newCustomer, environment, privilegeType);
					    try {
					    	LmsApiCustomer lmsApiCustomer = addLmsApiCustomer(newCustomer, generatedApiKey, environment, privilege); 
					    } catch (Exception e) {
					    	String errorMessage = e.getMessage();
							String userName = customer.getUserName();
							RegisterCustomer registerCustomerError = getRegisterCustomerError(ERROR_CODE_ONE, errorMessage, userName);
							registerCustomerList.add(registerCustomerError);
							continue;
					    }
					    
					}
					
					String customerCode = newCustomer.getCustomerCode();
					RegisterCustomer registerCustomer = getRegisterCustomer(customer, customerCode);
					if (generatedApiKey != null) {
						registerCustomer.setApiKey(generatedApiKey);
					}
					registerCustomerList.add(registerCustomer);
				}
				
			}	//end of for()
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
		registerCustomers.setRegisterCustomer(registerCustomerList);
		response.setRegisterCustomers(registerCustomers);
		log.debug(REQUEST_END_MESSAGE);
		return response;
	}
	
	private LmsApiDistributor findLmsApiDistributorByKey(String apiKey) throws Exception {
		
		LmsApiDistributor lmsApiDistributor = lmsApiDistributorService.findApiKey(apiKey);
		return lmsApiDistributor;
	}
	
	private LmsApiCustomer findLmsApiCustomerByKey(String apiKey) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = lmsApiCustomerService.findApiKey(apiKey);
		return lmsApiCustomer;
	}
	
	private boolean resellerApiValidation(LmsApiDistributor lmsApiDistributor, Long resellerId) throws Exception {
		
		String errorMessage = null;
		
		if (lmsApiDistributor == null) {
			errorMessage = "No Api Key found. Unauthorized Access";
			throwException(errorMessage);
		}
		
		Distributor distributor = lmsApiDistributor.getDistributor();
		if (distributor == null) {
			errorMessage = "No Reseller found for reseller Id: " + resellerId;
			throwException(errorMessage);
		}
		
		if (!(distributor.getId().equals(resellerId))){
			errorMessage = "Invalid reseller Id: " + resellerId;
			throwException(errorMessage);
		}
		
		if (!distributor.getLmsApiEnabledTF().booleanValue()) {
			errorMessage = "LMS API is not enable for resellerId: " + distributor.getId();
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}
	
	private boolean customerApiValidation(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception {
		
		String errorMessage = null;
		
		if (lmsApiCustomer == null) {
			errorMessage = "No Api Key found. Unauthorized Access";
			throwException(errorMessage);
		}
		
		Customer customer = lmsApiCustomer.getCustomer();
		
		if (customer == null) {
			errorMessage = "No Customer found: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!(customer.getCustomerCode().equals(customerCode))){
			errorMessage = "Invalid customer code: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!customer.getLmsApiEnabledTF().booleanValue()) {
			errorMessage = "LMS API is not enable for customer: " + customerCode;
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}
	
	private RegisterCustomer getRegisterCustomerError(String errorCode, String errorMessage, String userName) {
		log.debug(errorMessage);
		RegisterCustomer registerCustomerError = new RegisterCustomer();
		registerCustomerError.setErrorCode(errorCode);
		registerCustomerError.setErrorMessage(errorMessage);
		registerCustomerError.setUserName(userName);
		return registerCustomerError;	
	}
	
	private void setResellerPreferencesToAddCustomerForm(AddCustomerForm addcustomerForm, Distributor distributor) throws Exception {
		DistributorPreferences dp = distributor.getDistributorPreferences();
		if( dp != null ) {
			addcustomerForm.setAudio(dp.isAudioEnabled());
			addcustomerForm.setAudioLocked(dp.isAudioLocked());
			addcustomerForm.setCaptioning(dp.isCaptioningEnabled());
			addcustomerForm.setCaptioningLocked(dp.isCaptioningLocked());
			if( dp.getBandwidth().equalsIgnoreCase("high") ) {
				addcustomerForm.setBandwidth(true);
			}	
			if( dp.getBandwidth().equalsIgnoreCase("low") ) {
				addcustomerForm.setBandwidth(false);
			}
			addcustomerForm.setBandwidthLocked(dp.isBandwidthLocked());
			addcustomerForm.setVideo(dp.isVedioEnabled());
			addcustomerForm.setVideoLocked(dp.isVideoLocked());
			addcustomerForm.setRegistrationEmails(dp.isEnableRegistrationEmailsForNewCustomers());
			addcustomerForm.setRegistrationEmailsLocked(dp.isRegistrationEmailLocked());
			addcustomerForm.setEnrollmentEmails(dp.isEnableEnrollmentEmailsForNewCustomers());
			addcustomerForm.setEnrollmentEmailsLocked(dp.isEnrollmentEmailLocked());
			addcustomerForm.setCourseCompCertificateEmails(dp.isCourseCompletionCertificateEmailEnabled() );
			addcustomerForm.setCourseCompCertificateEmailsLocked(dp.isCourseCompletionCertificateEmailLocked());
		}
	}
	
	private boolean addCustomerValidation(com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer) throws Exception {
		
		String error = null;
		String accountExpiryDate = null;
		//Brander brander = getBrander(null, null);
		Company company = customer.getCompany();
		Manager manager = customer.getManager();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address  address1 = customer.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = customer.getAlternateAddress();
		String userName = customer.getUserName();
		String password = customer.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = customer.getAccountExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		if (company == null) {
			error = "company element is required";
			throwException(error);
		}
	
		if (manager == null) {
			error = "manager element is required";
			throwException(error);
		}
	
		String companyName = company.getName();
		String managerFirstName = manager.getManagerFirstName();
		String managerLastName = manager.getManagerLastName();
		String extension = manager.getExtension();
		
		if (StringUtils.isEmpty(companyName) || StringUtils.isBlank(companyName)){
			error = "Company name required";
			throwException(error);
		} else if (FieldsValidation.isInValidCustomerName(companyName)){
			error = "Invalid company name";
			throwException(error);
		}
		
		nameValidation(managerFirstName, null, managerLastName);
		emailValidation(userName);
		
		VU360User existingUser = vu360UserService.findUserByUserName(userName);
		if (existingUser != null) {
			error = "user name already exists";
			throwException(error);
		}
		
		passwordValidation(password);
		extensionValidation(extension);
		addressValidation(address1);
		addressValidation(alternateAddress);
		accountExpirationDateValidation(accountExpiryDate);
		
		 return true;
	}
	
	private Customer createNewCustomer(Distributor distributor, com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer) throws Exception {
		
		AddCustomerForm addCustomerForm = new AddCustomerForm();
		
		// By default customer preferences are copied from re seller's.
		setResellerPreferencesToAddCustomerForm(addCustomerForm, distributor);
		
		boolean accountStatus = false;
		Company company = customer.getCompany();
		Manager manager = customer.getManager();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address  address1 = customer.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address  alternateAddress = customer.getAlternateAddress();
		String companyName = company.getName();
		String managerFirstName = manager.getManagerFirstName();
		String managerLastName = manager.getManagerLastName();
		String companyWebSite = company.getWebsite();
		String emailAddress = company.getEmail();
		String userName = customer.getUserName();
		String managerPhone = manager.getManagerPhone();
		String extension = manager.getExtension();
		String customerType = customer.getCustomerType().value();
		String companyAccountStatus = company.getAccountStatus().value();
		String password = customer.getPassword();
		boolean isAccountExpired = customer.isAccountExpired();
		boolean isAccountLocked = customer.isAccountLocked();
		boolean isChangePasswordOnNextLogin = customer.isChangePasswordOnNextLogin();
		boolean isAccountDisabled = customer.isAccountDisabled();
		boolean isVisibleOnReport = customer.isVisibleOnReport();
		
		BigInteger brandId = customer.getBrandId();
		if (brandId != null) {
			long branderId = brandId.longValue();
			Brand brand = brandService.getBrandById(branderId);
			if (brand == null) {
				addCustomerForm.setBrandId(0);
			} else {
				addCustomerForm.setBrandId(branderId);
			}
			
		} else {
			brandId = new BigInteger("0");
			long branderId = brandId.longValue();
			addCustomerForm.setBrandId(branderId);
		}
		
		if (address1 != null) {
			setAddressToAddCustomerForm(addCustomerForm, address1, true);
		} else {
			address1 = getEmptyAddress();
			setAddressToAddCustomerForm(addCustomerForm, address1, true);
		}
		
		if (alternateAddress != null) {
			setAddressToAddCustomerForm(addCustomerForm, alternateAddress, false);
		} else {
			alternateAddress = getEmptyAddress();
			setAddressToAddCustomerForm(addCustomerForm, alternateAddress, false);
		}
		
		String accountExpiryDate = null;
		/**
		 * We have date in ("yyyy-MM-dd") format but in the CustomerServiceImpl.java it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = customer.getAccountExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
			}
		}
		
		if (companyAccountStatus != null && companyAccountStatus.equalsIgnoreCase("Active")) {
			accountStatus = true;
		}
		//LMS-15930
		if (emailAddress == null) {
			emailAddress= "";
		}
		
		addCustomerForm.setCustomerName(companyName);
		addCustomerForm.setFirstName(managerFirstName);
		addCustomerForm.setLastName(managerLastName);
		addCustomerForm.setWesiteURL(companyWebSite);
		addCustomerForm.setEmailAdd(emailAddress);
		addCustomerForm.setPhone(managerPhone);
		addCustomerForm.setExt(extension);
		addCustomerForm.setCustomerType(customerType);
		addCustomerForm.setStatus(accountStatus);
	
		// In API we are not currently offering self authoring so it is false. If we offer self authoring then we must provide
		// VU360User that has admin rights
		addCustomerForm.setSelfAuthor(false);
		addCustomerForm.setLoginEmailID(userName);
		addCustomerForm.setPassword(password);
		addCustomerForm.setExpired(isAccountExpired);
		addCustomerForm.setLocked(isAccountLocked);
		addCustomerForm.setChangePassword(isChangePasswordOnNextLogin);
		addCustomerForm.setDisabled(isAccountDisabled);
		addCustomerForm.setReport(isVisibleOnReport);
		addCustomerForm.setExpirationDate(accountExpiryDate);
		
		//VU360User user = null;
		Long userId=null;
		Customer registeredCustomer = customerService.addCustomer(userId, distributor, addCustomerForm);
		
		boolean apiEnabled = customer.isApiEnabled();
		
		if (apiEnabled) {
			registeredCustomer.setLmsApiEnabledTF(apiEnabled);
			registeredCustomer = customerService.updateCustomer(registeredCustomer);
		}
	
		return registeredCustomer;
		
	}
	
	private LmsApiCustomer addLmsApiCustomer(Customer customer, String apiKey, String environment, String privilege) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = new LmsApiCustomer();
		lmsApiCustomer.setCustomer(customer);
		lmsApiCustomer.setApiKey(apiKey);
		lmsApiCustomer.setEnvironment(environment);
		lmsApiCustomer.setPrivilege(privilege);
		
		LmsApiCustomer newLmsApiCustomer = lmsApiCustomerService.addLmsApiCustomer(lmsApiCustomer);
		return newLmsApiCustomer;
	}
	
	private String generateApiKey() {
		return GUIDGeneratorUtil.generateGUID();
		//return "1234";
	}
	
	private void setAddressToAddCustomerForm(AddCustomerForm addCustomerForm, com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address, boolean isAddress1) {
		String streetAddress1 = address.getStreetAddress1();
		String streetAddress2 = address.getStreetAddress2();
		String city = address.getCity();
		String country = address.getCountry();
		String state = address.getState();
		String zipCode = address.getZipCode();
		
		if (isAddress1) {
			addCustomerForm.setAddress1(streetAddress1);
			addCustomerForm.setAddress1a(streetAddress2);
			addCustomerForm.setCity1(city);
			addCustomerForm.setCountry1(country);
			addCustomerForm.setState1(state);
			addCustomerForm.setZip1(zipCode);
			addCustomerForm.setCountryLabel1(country);
			
		} else {
			addCustomerForm.setAddress2(streetAddress1);
			addCustomerForm.setAddress2a(streetAddress2);
			addCustomerForm.setCity2(city);
			addCustomerForm.setCountry2(country);
			addCustomerForm.setState2(state);
			addCustomerForm.setZip2(zipCode);
			addCustomerForm.setCountryLabel2(country);
		}
	}
	

	private com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address getEmptyAddress() {
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address = new com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address();
		address.setCity("");
		address.setCountry("");
		address.setState("");
		address.setStreetAddress1("");
		address.setStreetAddress2("");
		address.setZipCode("");
		return address;
	}
	
	private RegisterCustomer getRegisterCustomer(com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer, String customerCode) {
		Company company = customer.getCompany();
		String companyName = company.getName();
		String userName = customer.getUserName();
		
		RegisterCustomer registerCustomer = new RegisterCustomer();
		registerCustomer.setErrorCode(ERROR_CODE_ZERO);
		registerCustomer.setErrorMessage("");
		registerCustomer.setCustomerCode(customerCode);
		registerCustomer.setUserName(userName);
		registerCustomer.setCompany(company);
		registerCustomer.setOrganizationGroupName(companyName);
		return registerCustomer;
	}
	
	/**
	 * @PayloadRoot indicates that this method will process service requests with the XML root element matching that defined by 
	 * the localPart attribute. Our method will process incoming requests of type AddLearnerRequest with
	 * namespace http://com/softech/vu360/lms/webservice/message/lmsapi/serviceoperations.
	 */
	@Override
	//@PayloadRoot(localPart = ADD_USER_EVENT, namespace = USER_TARGET_NAMESPACE)
	public AddUserResponse addUser(AddUserRequest request) {
		
		log.debug("Add User Request start");
		final String REQUEST_END_MESSAGE = "Add User Request end";
		AddUserResponse response = new AddUserResponse();
		RegisterUsers registerUsers = new RegisterUsers();
		List<RegisterUser> registerUserList = new ArrayList<RegisterUser>();
		Users users = request.getUsers();
		List<User> userList = users.getUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {	
			Customer customer = getApiCustomer(apiKey, customerCode);
		
			for (User user: userList) {
				try {
					RegisterUser registerUser = null;
					List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorList = null;
					String userName = user.getUserName();
					if (userValidation(user)) {
						OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
						if (organizationalGroupsValidation(organizationalGroups)){
							List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();
							if (isUserExist(userName)) {   // check in lms only
								String errorMessage = "Unable to add. User Already Exist: " + userName;
								addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
								continue;
							}
							
							if (findUserInAD(userName)) {   // check in AD
								String errorMessage = "Unable to add. User Already Exist: " + userName;
								addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
								continue;
							}
								
							/**
							 * First we will make validOrgGroupList because user will create only if valid org group
							 * is present. We will create user only after we will found valid organizational group from
							 * valid organization group list. Because user have to create only once and then all
							 * organizational group assigned to it.
							 */
							Map<String, List<?>> resultMap = getvalidOrganizationalGroupMap(customer, orgGroupHierarchyList);
							Object validOrganizationalGroup = resultMap.get("validOrganizationalGroupList");
							registerOrganizationalGroupErrorList = (List<RegisterOrganizationalGroup>)resultMap.get("registerOrganizationalGroupErrorList");
							if (validOrganizationalGroup == null) {
								String errorMessage = "Unable to add user";
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
								registerUserList.add(registerUser);
								continue;	
							}
								
							List<OrganizationalGroup> validOrganizationalGroupList = (List<OrganizationalGroup>)validOrganizationalGroup;
							if (validOrganizationalGroupList == null || validOrganizationalGroupList.isEmpty()) {
								String errorMessage = "Unable to add user";
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
							} else {
								Learner learner = getNewLearner(customer, user);
								updateLearnerAssociationOfOrgGroups(learner, validOrganizationalGroupList);
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ZERO, "");
							}
							registerUserList.add(registerUser);	
						} 
					} 
						
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
					continue;
				}
			} //end of for (User user ..)
		
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
		registerUsers.setRegisterUser(registerUserList);
		response.setRegisterUsers(registerUsers);
		log.debug(REQUEST_END_MESSAGE);
		return response;
		
	} //end of addUser()
	
	@Override
	//@PayloadRoot(localPart = ADD_USER_LMS_ONLY_EVENT, namespace = USER_TARGET_NAMESPACE)
	public AddUserLmsOnlyResponse addUserLmsOnly(AddUserLmsOnlyRequest request) {
		
		log.debug("Add User Request start");
		final String REQUEST_END_MESSAGE = "Add User Request end";
		AddUserLmsOnlyResponse response = new AddUserLmsOnlyResponse();
		RegisterUsers registerUsers = new RegisterUsers();
		List<RegisterUser> registerUserList = new ArrayList<RegisterUser>();
		Users users = request.getUsers();
		List<User> userList = users.getUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {	
			Customer customer = getApiCustomer(apiKey, customerCode);
		
			for (User user: userList) {
				try {
					RegisterUser registerUser = null;
					List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorList = null;
					String userName = user.getUserName();
					if (userValidation(user)) {
						OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
						if (organizationalGroupsValidation(organizationalGroups)){
							List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();
							if (isUserExist(userName)) {
								String errorMessage = "Unable to add. User Already Exist: " + userName;
								addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
								continue;
							}
								
							/**
							 * First we will make validOrgGroupList because user will create only if valid org group
							 * is present. We will create user only after we will found valid organizational group from
							 * valid organization group list. Because user have to create only once and then all
							 * organizational group assigned to it.
							 */
							Map<String, List<?>> resultMap = getvalidOrganizationalGroupMap(customer, orgGroupHierarchyList);
							Object validOrganizationalGroup = resultMap.get("validOrganizationalGroupList");
							registerOrganizationalGroupErrorList = (List<RegisterOrganizationalGroup>)resultMap.get("registerOrganizationalGroupErrorList");
							if (validOrganizationalGroup == null) {
								String errorMessage = "Unable to add user";
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
								registerUserList.add(registerUser);
								continue;	
							}
								
							List<OrganizationalGroup> validOrganizationalGroupList = (List<OrganizationalGroup>)validOrganizationalGroup;
							if (validOrganizationalGroupList == null || validOrganizationalGroupList.isEmpty()) {
								String errorMessage = "Unable to add user";
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
							} else {
								Learner learner = getNewLearner(customer, user);
								updateLearnerAssociationOfOrgGroups(learner, validOrganizationalGroupList);
								registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ZERO, "");
							}
							registerUserList.add(registerUser);	
						} 
					} 
						
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
					continue;
				}
			} //end of for (User user ..)
		
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
		registerUsers.setRegisterUser(registerUserList);
		response.setRegisterUsers(registerUsers);
		log.debug(REQUEST_END_MESSAGE);
		return response;
		
	} //end of addUserLmsOnly()
	
	/**
	 * @PayloadRoot indicates that this method will process service requests with the XML root element matching that defined by 
	 * the localPart attribute. Our method will process incoming requests of type AddLearnerRequest with
	 * namespace http://com/softech/vu360/lms/webservice/message/lmsapi/serviceoperations.
	 */
	@Override
	//@PayloadRoot(localPart = UPDATE_USER_EVENT, namespace = USER_TARGET_NAMESPACE)
	public UpdateUserResponse updateUser(UpdateUserRequest request) {
		
		log.debug("Update User Request start");
		final String REQUEST_END_MESSAGE = "Update User Request end";
		UpdateUserResponse response = new UpdateUserResponse();
		RegisterUsers registerUsers = new RegisterUsers();
		List<RegisterUser> registerUserList = new ArrayList<RegisterUser>();
		UpdateableUsers users = request.getUsers();
		List<UpdateableUser> userList = users.getUpdateableUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {	
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
				for (UpdateableUser user: userList) {
					try {
						RegisterUser registerUser = null;
						List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorList = null;
						String userName = user.getUserName();
						if (updateUserValidation(user)) {
							OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
							if (organizationalGroupsValidation(organizationalGroups)){
								List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();
								if (!isUserExist(userName)) {
									String errorMessage = "Unable to Update. Invalid user name: " + userName;
									addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
									continue;
								}
								
								/**
								 * First we will make validOrgGroupList because user will update only if valid org group
								 * is present. We will update user only after we will found valid organizational group from
								 * valid organization group list. Because user have to create only once and then all
								 * organizational group assigned to it.
								 */
								Map<String, List<?>> resultMap = getvalidOrganizationalGroupMap(customer, orgGroupHierarchyList);
								Object validOrganizationalGroup = resultMap.get("validOrganizationalGroupList");
								registerOrganizationalGroupErrorList = (List<RegisterOrganizationalGroup>)resultMap.get("registerOrganizationalGroupErrorList");
								if (validOrganizationalGroup == null) {
									String errorMessage = "Unable to update user";
									registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
									registerUserList.add(registerUser);
									continue;	
								}
								
								List<OrganizationalGroup> validOrganizationalGroupList = (List<OrganizationalGroup>)validOrganizationalGroup;
								if (validOrganizationalGroupList == null || validOrganizationalGroupList.isEmpty()) {
									String errorMessage = "Unable to update user";
									registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ONE, errorMessage);
								} else {
									Learner learner = getUpdatedLearner(customer, user);
									updateLearnerAssociationOfOrgGroups(learner, validOrganizationalGroupList);
									registerUser = getResponseRegisterUser(user, registerOrganizationalGroupErrorList, ERROR_CODE_ZERO, "");
								}
								registerUserList.add(registerUser);	
							} else {
								String errorMessage = "Organization group validation not passed";
								addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
								continue;
							}
						} else {
							String errorMessage = "User validation not passed";
							addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
							continue;
						}
						
					} catch (Exception e) {
						String errorMessage = e.getMessage();
						addRegisterUserErrorToRegisterUserList(user, ERROR_CODE_ONE, errorMessage, registerUserList);
						continue;
					}
				} //end of for (User user ..)
		
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
		registerUsers.setRegisterUser(registerUserList);
		response.setRegisterUsers(registerUsers);
		log.debug(REQUEST_END_MESSAGE);
		return response;
		
	} //end of UpdateUser()
	
	private Map<String, List<?>> getvalidOrganizationalGroupMap(Customer customer, List<String> orgGroupHierarchyList) throws Exception {
		
		Map<String, List<?>> OrganizationalGroupMap = null;
		Map<String, List<?>> OrgGroupHierarchyMap = null;
		Map<String, List<?>> resultMap = new HashMap<String, List<?>>();
		List<String> validOrgGroupHierarchyList = null;
		List<OrganizationalGroup> validOrganizationalGroupList = null;
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrgGroupHierarchy = null;
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrganizationalGroup = null;
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorList = new ArrayList<RegisterOrganizationalGroup>();
		
		try {
			OrgGroupHierarchyMap = getOrgGroupHierarchyMapForRegisterUser(customer, orgGroupHierarchyList);
			Object validOrgGroupHierarchy = OrgGroupHierarchyMap.get("validOrgGroupHierarchyList");
			if (validOrgGroupHierarchy == null) {
				validOrgGroupHierarchyList = null;
			} else {
				validOrgGroupHierarchyList = (List<String>)validOrgGroupHierarchy;
			}
			registerOrganizationalGroupErrorListForOrgGroupHierarchy = (List<RegisterOrganizationalGroup>)OrgGroupHierarchyMap.get("registerOrganizationalGroupErrorListForOrgGroupHierarchy");
				
		} catch(Exception e) {
			String errorMessage = e.getMessage();
			throw new Exception(errorMessage);
		}	
		
		if (registerOrganizationalGroupErrorListForOrgGroupHierarchy != null) {
			registerOrganizationalGroupErrorList.addAll(registerOrganizationalGroupErrorListForOrgGroupHierarchy);
		}
		
		if (validOrgGroupHierarchyList != null && !validOrgGroupHierarchyList.isEmpty()) {
			OrganizationalGroupMap = getOrganizationalGroupMap(customer, validOrgGroupHierarchyList);
			validOrganizationalGroupList = (List<OrganizationalGroup>)OrganizationalGroupMap.get("validOrganizationalGroupList");
			registerOrganizationalGroupErrorListForOrganizationalGroup = (List<RegisterOrganizationalGroup>)OrganizationalGroupMap.get("registerOrganizationalGroupErrorListForOrganizationalGroup");
		} else {
			resultMap.put("validOrganizationalGroupList", null);
			resultMap.put("registerOrganizationalGroupErrorList", registerOrganizationalGroupErrorList);
			return resultMap;
		}
		
		if (registerOrganizationalGroupErrorListForOrganizationalGroup != null) {
			registerOrganizationalGroupErrorList.addAll(registerOrganizationalGroupErrorListForOrganizationalGroup);
		}
		
		resultMap.put("validOrganizationalGroupList", validOrganizationalGroupList);
		resultMap.put("registerOrganizationalGroupErrorList", registerOrganizationalGroupErrorList);
		
		return resultMap;
	}
	
	private Map<String, List<?>> getOrgGroupHierarchyMapForRegisterUser(Customer customer,List<String> orgGroupHierarchyList) throws Exception {
		
		Map<String, List<?>> OrgGroupHierarchyMap = new HashMap<String, List<?>>();
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrgGroupHierarchy = new ArrayList<RegisterOrganizationalGroup>();
		List<String> validOrgGroupHierarchyList = new ArrayList<String>();
		
		for (String orgGroupHierarchy: orgGroupHierarchyList) {
			try {
				String validOrgGroupHierarchy = getValidOrgGroupHierarchy(customer, orgGroupHierarchy);
				validOrgGroupHierarchyList.add(validOrgGroupHierarchy);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				RegisterOrganizationalGroup registerOrgGroupError = getRegisterOrganizationalGroup(ERROR_CODE_ONE, errorMessage, orgGroupHierarchy);
				registerOrganizationalGroupErrorListForOrgGroupHierarchy.add(registerOrgGroupError);
				//continue;
			}
		} //end of for()
		
		OrgGroupHierarchyMap.put("validOrgGroupHierarchyList", validOrgGroupHierarchyList);
		OrgGroupHierarchyMap.put("registerOrganizationalGroupErrorListForOrgGroupHierarchy", registerOrganizationalGroupErrorListForOrgGroupHierarchy);
		return OrgGroupHierarchyMap;
	}
	
	private RegisterUser getResponseRegisterUser(User user, List<RegisterOrganizationalGroup> registerOrganizationalGroupList, String errorCode, String errorMessage) {
		
		RegisterOrganizationalGroups registerOrganizationalGroups = new RegisterOrganizationalGroups();
		registerOrganizationalGroups.setRegisterOrganizationalGroup(registerOrganizationalGroupList);
		
		RegisterUser registerUser = getRegisterUser(errorCode, errorMessage, user);
		//if (registerOrganizationalGroupList != null && !registerOrganizationalGroupList.isEmpty()) {
		registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
		//}
		
		return registerUser;
	}
	
	private RegisterUser getResponseRegisterUser(UpdateableUser user, List<RegisterOrganizationalGroup> registerOrganizationalGroupList, String errorCode, String errorMessage) {
		
		RegisterOrganizationalGroups registerOrganizationalGroups = new RegisterOrganizationalGroups();
		registerOrganizationalGroups.setRegisterOrganizationalGroup(registerOrganizationalGroupList);
		
		RegisterUser registerUser = getRegisterUser(errorCode, errorMessage, user);
		//if (registerOrganizationalGroupList != null && !registerOrganizationalGroupList.isEmpty()) {
		registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
		//}
		
		return registerUser;
	}
	
	private void addRegisterUserErrorToRegisterUserList(User user, String errorCode, String errorMessage, List<RegisterUser> registerUserList) {
		RegisterUser registerUserError = getRegisterUser(errorCode, errorMessage, user);
		registerUserList.add(registerUserError);
	}
	
	private void addRegisterUserErrorToRegisterUserList(UpdateableUser user, String errorCode, String errorMessage, List<RegisterUser> registerUserList) {
		RegisterUser registerUserError = getRegisterUser(errorCode, errorMessage, user);
		registerUserList.add(registerUserError);
	}
	
	private boolean updateLearnerAssociationOfOrgGroups (Learner learner, List<OrganizationalGroup> organizationalGroupList) 
			throws Exception {
		
		if (organizationalGroupList == null || organizationalGroupList.isEmpty()) {
			String errorMessage = "Organizational group list is null or empty. No organizationalGroup is found to associate to learner";
			throw new Exception(errorMessage);
		}
		
		Set<OrganizationalGroup> setOfOrgGroups = new HashSet<OrganizationalGroup>();
		for (OrganizationalGroup organizationalGroup : organizationalGroupList) {
			setOfOrgGroups.add(organizationalGroup);
		}
		
        updateAssociationOfOrgGroups(learner, setOfOrgGroups);
        return true;
		
	}
	
	private Learner getNewLearner(Customer customer, User user) throws Exception {
		
		Learner newLearner = createNewUser(customer, user);;
		return newLearner;
		
	}
	
	private Learner getUpdatedLearner(Customer customer, UpdateableUser updateableUser) throws Exception {
		
		String userName = updateableUser.getUserName();
		Learner updatedLearner = null;
		VU360User existingUser = vu360UserService.findUserByUserName(userName);
		if (existingUser == null) {
			String errorMessage = "No existing user found for: " + userName;
			throwException(errorMessage);
		} 
		
		updatedLearner = updateUser(customer, existingUser, updateableUser);
		return updatedLearner;
		
	}
	
	private Map<String, List<?>> getOrganizationalGroupMap(Customer customer, List<String> validOrgGroupHierarchyList) {
		
		Map<String, List<?>> OrganizationalGroupMap = new HashMap<String, List<?>>();
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrganizationalGroup = new ArrayList<RegisterOrganizationalGroup>();
		List<OrganizationalGroup> validOrganizationalGroupList = new ArrayList<OrganizationalGroup>();
		for (String orgGroupHierarchy: validOrgGroupHierarchyList) {
			try {
				OrganizationalGroup organizationalGroup = getOrganizationalGroup(customer, orgGroupHierarchy);
				validOrganizationalGroupList.add(organizationalGroup);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				RegisterOrganizationalGroup registerOrgGroupError = getRegisterOrganizationalGroup(ERROR_CODE_ONE, errorMessage, orgGroupHierarchy);
				registerOrganizationalGroupErrorListForOrganizationalGroup.add(registerOrgGroupError);
				continue;
			}
			
			String errorMessage = "";
			RegisterOrganizationalGroup registerOrgGroup = getRegisterOrganizationalGroup(ERROR_CODE_ZERO, errorMessage, orgGroupHierarchy);
			registerOrganizationalGroupErrorListForOrganizationalGroup.add(registerOrgGroup);
			continue;
		}
		
		OrganizationalGroupMap.put("validOrganizationalGroupList", validOrganizationalGroupList);
		OrganizationalGroupMap.put("registerOrganizationalGroupErrorListForOrganizationalGroup", registerOrganizationalGroupErrorListForOrganizationalGroup);
		
		return OrganizationalGroupMap;
	}
	
	private String getValidOrgGroupHierarchy(Customer customer, String orgGroupHierarchy) throws Exception {
		
		if (StringUtils.isEmpty(orgGroupHierarchy) || StringUtils.isBlank(orgGroupHierarchy)) {
			String errorMessage = "OrgGroupHierarchy can not be empty or blank";
			throw new Exception(errorMessage);
		} else {
			//check hierarchy is seperated with correct delimiter i.e., <
			if(hasManagerSecurityRightsForOrganization(customer,orgGroupHierarchy)){
				String errorMessage = "Manager for customer: " + customer.getCustomerCode() + " do not have rigths to create organizational group: " + orgGroupHierarchy;
				throw new Exception(errorMessage);
			}
		} 
		return orgGroupHierarchy;
	}
	
	private RegisterOrganizationalGroup getRegisterOrganizationalGroup(String errorCode, String errorMessage, String orgGroupHierarchy) {
		RegisterOrganizationalGroup registerOrgGroup = new RegisterOrganizationalGroup();
		registerOrgGroup.setErrorCode(errorCode);
		registerOrgGroup.setErrorMessage(errorMessage);
		registerOrgGroup.setOrgGroupHierarchy(orgGroupHierarchy);
		return registerOrgGroup;
	}
	
	private Learner updateUser(Customer customer, VU360User existingUser, UpdateableUser userToBeUpdate) throws Exception {
		
		VU360User updateableUser = vu360UserService.loadForUpdateVU360User(existingUser.getId());
        VU360User updatedUser = getUpdatedUser(userToBeUpdate, updateableUser);
        LearnerProfile updatedLearnerProfile = getUpdatedLearnerProfile(userToBeUpdate, updatedUser);
        updatedUser.getLearner().setLearnerProfile(updatedLearnerProfile);
        
        //Update Profile before Remove Training Manger since it generate exception after removal. 
        learnerService.updateLearnerProfile(updatedUser.getLearner().getLearnerProfile());
        
        if(updatedUser.getTrainingAdministrator()!= null){
    		// this means.. this is existing learner and has got administrator .. really need demotion :)
    		removeTrainingManager(updatedUser);
    	}
        
        log.debug("updating learner having id = " + updatedUser.getLearner().getId() +" profile id = " +
        		updatedUser.getLearner().getLearnerProfile().getId() +" address id = " +
        		updatedUser.getLearner().getLearnerProfile().getLearnerAddress().getId() +" user id = " + updatedUser.getId());
        
        learnerService.updateUserFromBatchFile(updatedUser);
        log.debug("learner updated.");
        
        return updatedUser.getLearner();
           
	}
	
	private VU360User getUpdatedUser(UpdateableUser userToBeUpdate, VU360User existingUser) throws Exception {
		
		String middleName = null;
		
		Date updatedUserExpirationDate = null;
		String accountExpiryDate = null;
		String firstName = userToBeUpdate.getFirstName();
		middleName = userToBeUpdate.getMiddleName();
		if (middleName == null) {
			middleName = existingUser.getMiddleName();
		}
		String lastName = userToBeUpdate.getLastName();
		String emailAddress = userToBeUpdate.getEmailAddress();
		String userName = userToBeUpdate.getUserName();
		String password = userToBeUpdate.getPassword();
		boolean isAccountExpired = userToBeUpdate.isAccountExpired();
		boolean isAccountLocked = userToBeUpdate.isAccountLocked();
		boolean isChangePasswordOnNextLogin = userToBeUpdate.isChangePasswordOnNextLogin();
		boolean isVisibleOnReport = userToBeUpdate.isVisibleOnReport();
		
		existingUser.setFirstName(firstName);
		existingUser.setMiddleName(middleName);
		existingUser.setLastName(lastName);
		existingUser.setEmailAddress(emailAddress);
		
		if (password != null) {
			isCorrectPassword(password);
			existingUser.setPassWordChanged(true);
			existingUser.setPassword(password);
		} 
		
		existingUser.setUsername(userName);
		existingUser.setAccountNonExpired(!isAccountExpired);
		existingUser.setAccountNonLocked(!isAccountLocked);
		existingUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		existingUser.setVissibleOnReport(isVisibleOnReport);
		existingUser.setCredentialsNonExpired(!isAccountExpired);
		existingUser.setEnabled(true);
       
		/**
		 * We have date in ("yyyy-MM-dd") format but in the it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = userToBeUpdate.getExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
				
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				updatedUserExpirationDate = formatter.parse(accountExpiryDate);
			}
		} else {
			updatedUserExpirationDate = existingUser.getExpirationDate();
		}

		existingUser.setExpirationDate(updatedUserExpirationDate);
		return existingUser;	
		
	}
	
	private LearnerProfile getUpdatedLearnerProfile(UpdateableUser userToBeUpdate, VU360User existingUser) {
		String phone = userToBeUpdate.getPhone() == null ? existingUser.getLearner().getLearnerProfile().getOfficePhone() : userToBeUpdate.getPhone();
		String mobilePhone = userToBeUpdate.getMobilePhone() == null ? existingUser.getLearner().getLearnerProfile().getMobilePhone() : userToBeUpdate.getMobilePhone();
		String extension = userToBeUpdate.getExtension() == null ? existingUser.getLearner().getLearnerProfile().getOfficePhoneExtn() : userToBeUpdate.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = userToBeUpdate.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = userToBeUpdate.getAlternateAddress();
		
		LearnerProfile updatedLearnerProfile = existingUser.getLearner().getLearnerProfile();
		updatedLearnerProfile = learnerService.loadForUpdateLearnerProfile(updatedLearnerProfile.getId());
		updatedLearnerProfile.setMobilePhone(mobilePhone);
		updatedLearnerProfile.setOfficePhone(phone);
		updatedLearnerProfile.setOfficePhoneExtn(extension);
		updatedLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			updatedLearnerProfile.setLearnerAddress(newAddress);
		} else {
			Address newAddress = existingUser.getLearner().getLearnerProfile().getLearnerAddress();
			updatedLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			Address newAddress2 = existingUser.getLearner().getLearnerProfile().getLearnerAddress2();
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		}
		
		return updatedLearnerProfile;
	}
	
	private void removeTrainingManager(VU360User user){
		TrainingAdministrator trainingAdministrator = user.getTrainingAdministrator();
		trainingAdministrator.setVu360User(null);
		trainingAdministrator.setCustomer(null);
		vu360UserService.deleteLMSTrainingAdministrator(trainingAdministrator);
		
		// set training manager null
		user.setTrainingAdministrator(null);

		//delete manager role from this user
		Set<LMSRole> roles = user.getLmsRoles();
    	Set<LMSRole> roles2Remove = new HashSet<LMSRole>();
    	for(LMSRole role : roles){
    		if(role.getRoleType().equals(LMSRole.ROLE_TRAININGMANAGER)){
    			roles2Remove.add(role);
    		}
    	}
    	if(CollectionUtils.isNotEmpty(roles2Remove)){
    		roles.removeAll(roles2Remove);
    	}
    	user.setLmsRoles(roles);
    }
	
	private boolean findUserInAD(String userName) {
		return activeDirectoryService.findADUser(userName);	
	}
	
	private Learner createNewUser(Customer customer, User user) throws Exception {
		VU360User newUser = getNewUser(customer, user);
		LearnerProfile newLearnerProfile = getNewLearnerProfile(user);
		Learner newLearner = new Learner();
		newLearner.setVu360User(newUser);
		
		newLearnerProfile.setLearner(newLearner);
		
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);
		
		newUser.setLearner(newLearner);
		
		log.debug("adding learner...");
		newLearner = learnerService.addLearner(newLearner);
		log.debug("learner added.");
		
		return newLearner;
		
	} //end of createNewUser()
	
	private void updateAssociationOfOrgGroups(Learner learner, Set<OrganizationalGroup> setOfOrgGroups) {
        log.debug("updating associations of Org groups...");
        orgGroupLearnerGroupService.addRemoveOrgGroupsForLearner(learner, setOfOrgGroups);
    }
	
	private OrganizationalGroup getOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		OrganizationalGroup organizationalGroup = null;
        boolean isOrgGroupExist = isOrganizationalGroupExist(customer, orgGroupHierarchy);
    	if (!isOrgGroupExist) {
    		organizationalGroup = createOrganizationalGroup(customer, orgGroupHierarchy);
    		if (organizationalGroup != null) {
                log.debug("org group(s) created !");
    	    } else {
    			log.error("ERROR >> invalid  Org group or not permitted");
    			String errorMessage = "invalid Org group or not permitted";
    			throw new Exception(errorMessage); 
    		}
    	} else {
    		organizationalGroup = getExistingOrganizationalGroup(customer, orgGroupHierarchy);
    		if (organizationalGroup == null) {
    			log.debug("orgGroup not found");
    			String errorMessage = "Organizational Group exist but no organizational group is returning";
   				 throw new Exception(errorMessage);
    		}      
    	}	
     
		return organizationalGroup;
	}
	
	private boolean isOrganizationalGroupExist(Customer customer, String orgGroupHierarchy) throws Exception {
		//True if Organization exists
		boolean isOrgExists = false;
		OrganizationalGroup organizationalGroup = getExistingOrganizationalGroup(customer, orgGroupHierarchy);
		if (organizationalGroup != null) {
			isOrgExists = true;
		}
		return isOrgExists;
	}
	
	private OrganizationalGroup getExistingOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		VU360User manager = getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
        orgGroupHierarchy = getOrgGrouptHierarchy(orgGroupHierarchy);
		Map<String,OrganizationalGroup> managedGroupsMap = new HashMap<String, OrganizationalGroup>();
		if(manager.getTrainingAdministrator()!= null && !manager.getTrainingAdministrator().getManagedGroups().isEmpty()){
			for(OrganizationalGroup managedGroups : manager.getTrainingAdministrator().getManagedGroups()){
				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups);
			}
		}
		
		OrganizationalGroup organizationalGroup = null;
		String[] orgInBatchFile = orgGroupHierarchy.split(">");
		for(int i=0;i <orgInBatchFile.length ; i++) {
			if(managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())) {
				organizationalGroup = managedGroupsMap.get(orgInBatchFile[i].toUpperCase());
			}	
		}
		return organizationalGroup;
	}
	
	private String getOrgGrouptHierarchy(String orgGroupHierarchy) {
		String hierarchy = "";
        String[] groupNames = orgGroupHierarchy.split(GROUP_SPLITTER);
        for(String orgGroupName : groupNames) {
        	if(hierarchy == "") {
        		hierarchy = orgGroupName.trim();
        	} else {
        		hierarchy = hierarchy+ ">" + orgGroupName.trim();
        	}		
        } //end of for()
        
        return hierarchy;
	}
	 
	private OrganizationalGroup createOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		log.info("orgGroupHierarchy = " + orgGroupHierarchy);
        OrganizationalGroup organizationalGroup = null;
        boolean isRootGroupExist = isRootOrganizationalGroupExist(customer, orgGroupHierarchy);
        if (isRootGroupExist) { 
        	organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
        	if(organizationalGroup != null && !organizationalGroup.toString().equalsIgnoreCase(orgGroupHierarchy)) {
            	organizationalGroup = createMissingOrgGroup(customer, organizationalGroup, orgGroupHierarchy);
            } else {
            	log.info("No new groups/subgroups to create, group already exists");
            }   
        } else {  //end of if (isRootGroupExist)
        	String rootGroup =  getRootOrgGroup(orgGroupHierarchy);
        	log.error("ERROR >> root Orgnization Group does not exists: " + rootGroup);
        	String errorMessage = "Root Orgnization Group does not exists: " + rootGroup;
        	throw new Exception(errorMessage);   
        }  
        return organizationalGroup;
	}
	
	private OrganizationalGroup getOrganizationalGroupFromAllOrganizationalGroups(Customer customer, String orgGroupHierarchy) {
		OrganizationalGroup organizationalGroup = null;
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		Map<String, OrganizationalGroup> allOrganizationalGroups = getAllOrganizationalGroups(customer);
		
        log.info("rootOrgGroup.getName() = " + rootOrganizationalGroup.getName());
        log.info("allOrganizationalGroups() = " + allOrganizationalGroups);
		
        String hierarchy = orgGroupHierarchy;
		do {
            log.info("lookup group '" + hierarchy + "'...");
            organizationalGroup = allOrganizationalGroups.get(hierarchy.trim().toUpperCase());
            if(organizationalGroup == null) {
                log.info("not found");
                if(hierarchy.indexOf(GROUP_SPLITTER) > 0) {
             	   hierarchy = hierarchy.substring(0, hierarchy.lastIndexOf(GROUP_SPLITTER));
                } else {
             	   hierarchy = ""; 
                }    
                log.info("hierarchy = " + hierarchy);
            }
            else {
         	   log.info("found"); 
            }                   
            log.info("hierarchy.length() = "  + hierarchy.length());
            log.info("organizationalGroup = " + organizationalGroup);
            
        } while(organizationalGroup == null && hierarchy.length() > 0);
		
		return organizationalGroup;
		
	} //end of getOrganizationalGroupFromAllOrganizationalGroups()
	
	private OrganizationalGroup createMissingOrgGroup(Customer customer, OrganizationalGroup organizationalGroup, 
			String orgGroupHierarchy) throws Exception {
		VU360User manager = getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		log.info("creating missing groups...");
        orgGroupHierarchy = orgGroupHierarchy.substring(organizationalGroup.toString().length() + GROUP_SPLITTER.length());
        log.info("missing orgGroupHierarchy = " + orgGroupHierarchy);
        String[] orgGroupNames = orgGroupHierarchy.split(GROUP_SPLITTER);
        OrganizationalGroup newOrgGroup = null;
        
        for(String orgGroupName : orgGroupNames) {
        	try {
        		newOrgGroup = createNewOrgGroup(customer, manager, orgGroupName, organizationalGroup);
        	} catch (Exception e) {
        		String errorMessage = e.getMessage();
        		throw new Exception(errorMessage);
        	}
        	
            log.info("new org group created");
       	 	organizationalGroup.getChildrenOrgGroups().add(newOrgGroup);
            newOrgGroup.setRootOrgGroup(rootOrganizationalGroup);
            newOrgGroup.setParentOrgGroup(organizationalGroup);
            log.info("new org group added");
            organizationalGroup = newOrgGroup;
            
        } //end of for()
        
        return newOrgGroup;
		
	} //end of createMissingOrgGroup()
	
	private OrganizationalGroup createNewOrgGroup (Customer customer, VU360User manager, String orgGroupName, OrganizationalGroup organizationalGroup) throws Exception {
		if (manager == null) {
			manager = getVU360UserByCustomerGUID(customer);
			if (manager == null) {
				String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
				throw new Exception(errorMessage);
			}
		}
	
		log.info("orgGroupName = " + orgGroupName);
        log.info("loggedInUser.isLMSAdministrator() = " + vu360UserService.hasAdministratorRole(manager));
        log.info("manager.getTrainingAdministrator().isManagesAllOrganizationalGroups() = " + manager.getTrainingAdministrator().isManagesAllOrganizationalGroups());
        
        OrganizationalGroup newOrgGroup = null;
        
        if(vu360UserService.hasAdministratorRole(manager) || manager.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
        	log.info("creating org group '" + orgGroupName + "'...");
        	newOrgGroup = createNewOrganizationalGroup(customer, orgGroupName, organizationalGroup);
        } else {
        	boolean hasPermission = hasPermissionToCreateOrgGroup(customer, organizationalGroup);
        	if(hasPermission) {
        		log.info("creating org group '" + orgGroupName + "'...");
                newOrgGroup = createNewOrganizationalGroup(customer, orgGroupName, organizationalGroup);
        	} else {
        		log.error("ERROR >> No permissions to create org group.");
        		String errorMessage = "No permissions to create org group";
        		throw new Exception(errorMessage);
        	}
        }
        return newOrgGroup;
	}
	
	private OrganizationalGroup createNewOrganizationalGroup(Customer customer, String orgGroupName, 
			OrganizationalGroup organizationalGroup) {
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		OrganizationalGroup newOrgGroup = orgGroupLearnerGroupService.createOrgGroup2(customer,orgGroupName, rootOrganizationalGroup, organizationalGroup);
		return newOrgGroup;
	}
	
	private boolean hasPermissionToCreateOrgGroup(Customer customer, OrganizationalGroup organizationalGroup) throws Exception{
		log.info("checking permission...");
		VU360User manager = getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
        boolean hasPermission = false;
        List<OrganizationalGroup> managedGroups = manager.getTrainingAdministrator().getManagedGroups();
        log.info("managedGroups = " + managedGroups);
        for(OrganizationalGroup permitedOrgGroup : manager.getTrainingAdministrator().getManagedGroups()) {
        	log.info("\tchecking group '" + permitedOrgGroup.getName() + "'...");
            if(permitedOrgGroup.getName().equals(organizationalGroup.getName())) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
	}
	
	private boolean isRootOrganizationalGroupExist(Customer customer, String orgGroupHierarchy) throws Exception {
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		String rootGroup = getRootOrgGroup(orgGroupHierarchy);
        log.info("rootGroup = " + rootGroup);
        if(!rootOrganizationalGroup.getName().trim().equalsIgnoreCase(rootGroup)) {
            log.error("ERROR >> rootGroup does not exists");
            return false;
        }
        return true;
	}
	
	private String getRootOrgGroup(String orgGroupHierarchy) throws Exception {
		String rootGroup = null;
        if (orgGroupHierarchy.indexOf(GROUP_SPLITTER) > 0) {
        	rootGroup = orgGroupHierarchy.substring(0, orgGroupHierarchy.indexOf(GROUP_SPLITTER)).trim();
        } else {
        	rootGroup = orgGroupHierarchy.trim();
        }
        return rootGroup;
	}
	
	private VU360User getNewUser(Customer customer, User user) throws Exception {
		Date newUserExpirationDate = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		boolean isAccountExpired = user.isAccountExpired();
		boolean isAccountLocked = user.isAccountLocked();
		boolean isChangePasswordOnNextLogin = user.isChangePasswordOnNextLogin();
		boolean isVisibleOnReport = user.isVisibleOnReport();
		
		Calendar calender=Calendar.getInstance();
		Date createdDate=calender.getTime();
		LMSRole systemRole = getDefaultSystemRole(customer);
		
		VU360User newUser = new VU360User();
		newUser.setFirstName(firstName);
		newUser.setMiddleName(middleName);
		newUser.setLastName(lastName);
		newUser.setEmailAddress(emailAddress);
		newUser.setPassword(password);
		newUser.setUsername(userName);
		newUser.setAccountNonExpired(!isAccountExpired);
		newUser.setAccountNonLocked(!isAccountLocked);
		newUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		newUser.setVissibleOnReport(isVisibleOnReport);
		newUser.setAcceptedEULA(false);
		newUser.setNewUser(true);
		newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
		newUser.setCreatedDate(createdDate);
		newUser.addLmsRole(systemRole);
	
		/**
		 * We have date in ("yyyy-MM-dd") format but in the it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				newUserExpirationDate = formatter.parse(accountExpiryDate);
			}
		}

		newUser.setExpirationDate(newUserExpirationDate);
		return newUser;	
	}
	
	private LMSRole getDefaultSystemRole(Customer customer) {
		LMSRole systemRole = vu360UserService.getDefaultRole(customer);
		if(systemRole == null) {
			List<LMSRole> systemRoles=vu360UserService.getSystemRolesByCustomer(customer);
			for(LMSRole role:systemRoles){
				if(role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
				{
					systemRole=role;
					break;
				}
			}
		}
		return systemRole;
	}
	
	private LearnerProfile getNewLearnerProfile(User user) {
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		//List<CustomFieldValue> myCustomFieldValues = new ArrayList<CustomFieldValue>();
		
		LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setMobilePhone(mobilePhone);
		newLearnerProfile.setOfficePhone(phone);
		newLearnerProfile.setOfficePhoneExtn(extension);
		newLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		} else {
			address1 = getEmptyAddress();
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			alternateAddress = getEmptyAddress();
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		}
		return newLearnerProfile;
	}
	
	private Address getNewAddress(com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address) {
		String streetAddress1 = address.getStreetAddress1();
		String streetAddress2 = address.getStreetAddress2();
		String city = address.getCity();
		String country = address.getCountry();
		String state = address.getState();
		String zipCode = address.getZipCode();
		
		Address newAddress = new Address();
		newAddress.setStreetAddress(streetAddress1);
		newAddress.setStreetAddress2(streetAddress2);
		newAddress.setCity(city);
		newAddress.setState(state);
		newAddress.setZipcode(zipCode);
		newAddress.setCountry(country);
		
		return newAddress;
	}
	
	private OrganizationalGroup getRootOrganizationalGroup(Customer customer) {
		 OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
         rootOrgGroup = orgGroupLearnerGroupService.loadForUpdateOrganizationalGroup(rootOrgGroup.getId());
         return rootOrgGroup;   
	}
	
	private Map<String, OrganizationalGroup> getAllOrganizationalGroups(Customer customer) {
		Map<String, OrganizationalGroup> allOrganizationalGroups = new HashMap<String, OrganizationalGroup>();
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
        allOrganizationalGroups = this.getOrganizationalGroupMap(allOrganizationalGroups, rootOrgGroup);
        return allOrganizationalGroups;
	}
	
	private Boolean organizationalGroupsValidation(OrganizationalGroups organizationalGroups) throws Exception {
		
		String errorMessage = null;
		if (organizationalGroups != null) {
			List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();	
			if (!orgGroupHierarchyList.isEmpty()) {
				
			} else {
				errorMessage = "Atleast one OrgGroupHierarchy is required";
				throw new Exception(errorMessage);
			}
		} else {
			errorMessage = "OrganizationalGroups elemnt is required";
			throw new Exception(errorMessage);
		}
		return true;
	}
	
	private Boolean hasManagerSecurityRightsForOrganization(Customer customer,	String orgGroupHierarchy) throws Exception {
		VU360User manager = getVU360UserByCustomerGUID(customer);
		
		if (manager == null) {
			String errorMessage = "No manager found for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		Map<String,String> managedGroupsMap=new HashMap<String, String>();
		if(manager.getTrainingAdministrator()!= null && !manager.getTrainingAdministrator().getManagedGroups().isEmpty()){
			for(OrganizationalGroup managedGroups : manager.getTrainingAdministrator().getManagedGroups()){
				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups.getName().toUpperCase());
			}
			String[] orgInBatchFile=orgGroupHierarchy.split(">");
			for(int i=0;i <orgInBatchFile.length ; i++){
				if(!managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	private RegisterUser getRegisterUser(String errorCode, String errorMessage, User user) {
		log.debug(errorMessage);
		
		String userName = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		
		RegisterUser registerUser = new RegisterUser();
		registerUser.setErrorCode(errorCode);
		registerUser.setErrorMessage(errorMessage);
		registerUser.setUserName(userName);
		registerUser.setFirstName(firstName);
		registerUser.setLastName(lastName);
		registerUser.setEmailAddress(emailAddress);
		return registerUser;
	}
	
	private RegisterUser getRegisterUser(String errorCode, String errorMessage, UpdateableUser user) {
		log.debug(errorMessage);
		
		String userName = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		
		RegisterUser registerUser = new RegisterUser();
		registerUser.setErrorCode(errorCode);
		registerUser.setErrorMessage(errorMessage);
		registerUser.setUserName(userName);
		registerUser.setFirstName(firstName);
		registerUser.setLastName(lastName);
		registerUser.setEmailAddress(emailAddress);
		return registerUser;
	}
	
	private boolean userValidation(User user) throws Exception {
		String error = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		userNameValidation(userName);
		passwordValidation(password);
		emailValidation(emailAddress);
		nameValidation(firstName, middleName, lastName);
		
		if (StringUtils.isNotEmpty(phone) || StringUtils.isNotBlank(phone)){
			if (FieldsValidation.isInValidOffPhone((phone))){
				error = "Bad characters not allowed (Office Phone)";
				throwException(error);
			}
		}
		
		extensionValidation(extension);
		
		if (StringUtils.isNotEmpty(mobilePhone) || StringUtils.isNotBlank(mobilePhone)){
			if (FieldsValidation.isInValidMobPhone((mobilePhone))){
				error = "Bad characters not allowed (Mobile Phone)";
				throwException(error);
			}
		}
		
		addressValidation(address1);
		addressValidation(alternateAddress);
		accountExpirationDateValidation(accountExpiryDate);
		
		return true;
	} //end of addUserValidation()
	
	private boolean updateUserValidation(UpdateableUser user) throws Exception {
		String error = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		userNameValidation(userName);
		isCorrectPassword(password);
		emailValidation(emailAddress);
		nameValidation(firstName, middleName, lastName);
		
		if (StringUtils.isNotEmpty(phone) || StringUtils.isNotBlank(phone)){
			if (FieldsValidation.isInValidOffPhone((phone))){
				error = "Bad characters not allowed (Office Phone)";
				throwException(error);
			}
		}
		
		extensionValidation(extension);
		
		if (StringUtils.isNotEmpty(mobilePhone) || StringUtils.isNotBlank(mobilePhone)){
			if (FieldsValidation.isInValidMobPhone((mobilePhone))){
				error = "Bad characters not allowed (Mobile Phone)";
				throwException(error);
			}
		}
		
		addressValidation(address1);
		addressValidation(alternateAddress);
		accountExpirationDateValidation(accountExpiryDate);
		
		return true;
	} //end of addUserValidation()
	
	private boolean userNameValidation(String userName) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(userName) || StringUtils.isBlank(userName)) {
			error = "User name required";
			throwException(error);
		} else if (FieldsValidation.isInValidGlobalName(userName)){
			error = "Bad characters not allowed (Username)";
			throwException(error);	
		} 
		return true;
	}
	
	private boolean isUserExist(String userName){
		boolean userExist = false;
		VU360User vu360User = vu360UserService.findUserByUserName(userName); 
		if (vu360User != null) {
			userExist = true;
		}
		return userExist;
	}
	
	private boolean addressValidation(com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address) throws Exception {
		if (address != null) {
			String error = null;
			Brander brander = getBrander(null, null);
			String country = address.getCountry();
			String zipCode = address.getZipCode();
			String streetAddress1 = address.getStreetAddress1();
			String streetAddress2 = address.getStreetAddress2();
			String city = address.getCity();
			
			if (StringUtils.isNotEmpty(streetAddress1) && StringUtils.isNotBlank(streetAddress1)){
				if ((FieldsValidation.isInValidAddress((streetAddress1)))){
					error = "Bad characters not allowed (Street Address1)";
					throwException(error);
				}
			}
			
			if (StringUtils.isNotEmpty(streetAddress2) && StringUtils.isNotBlank(streetAddress2)){
				if ((FieldsValidation.isInValidAddress((streetAddress2)))){
					error = "Bad characters not allowed (Street Address2)";
					throwException(error);
				}
			}
			
			if (StringUtils.isNotEmpty(country) && StringUtils.isNotBlank(country) && StringUtils.isNotEmpty(zipCode) && StringUtils.isNotBlank(zipCode)) {
				//	for learner address 1 Zip Code
				if (!ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
		        	log.debug("ZIP CODE FAILED" );
		        	error = ZipCodeValidator.getCountryZipCodeError(country, brander);
		        	if (error == "") {
		        		error = "ZIP CODE FAILED";
		        	}
		        	throwException(error);
		        }	
			}
			
			if (StringUtils.isNotEmpty(city) && StringUtils.isNotBlank(city)){
				if (FieldsValidation.isInValidGlobalName(city)){
					error = "Bad characters not allowed (City)";
					throwException(error);
				}
			}	
		} //end of if (address != null)
		return true;
	}
	
	private boolean accountExpirationDateValidation(String accountExpiryDate) throws Exception {
		if (StringUtils.isNotEmpty(accountExpiryDate) && StringUtils.isNotBlank(accountExpiryDate)){
			String error = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate = null;
			Date todayDate = new Date();
			try {
				expirationDate = formatter.parse(accountExpiryDate);
				if (!formatter.format(expirationDate).equals(accountExpiryDate)) {
					error = "Invalid Date";
					throwException(error);
				}else {
					if( expirationDate.before(todayDate)  ) {
						error = "Invalid Date";
						throwException(error);
					}
				}	
			} catch (ParseException e) {
				e.printStackTrace();
				error = e.getMessage() + ". Provide date in yyyy-MM-dd format";
				throwException(error);
			}	
		}
		return true;
	}
	
	private boolean emailValidation(String emailAddress) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(emailAddress) || StringUtils.isBlank(emailAddress)) {
			error = "Email address required";
			throwException(error);
		} else if (!FieldsValidation.isEmailValid(emailAddress)){
			error = "Invalid email address";
			throwException(error);
		}
		return true;
	}
	
	private boolean passwordValidation(String password) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(password) || StringUtils.isBlank(password)) {
			error = "Password required";
			throwException(error);
        }
		
		isCorrectPassword(password);
		return true;
	}
	
	private boolean isCorrectPassword(String password) throws Exception {
		if (password != null) {
			if ( !FieldsValidation.isPasswordCorrect(password) ) {
				String error = "Password must contain alphabets and numbers and must be at least 8 characters long";
	        	throwException(error);
	        }
		}
		return true;
	}
	
	private boolean nameValidation(String firstName, String middleName, String lastName) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(firstName) || StringUtils.isBlank(firstName)) {
			error = "First Name required";
			throwException(error);
		} 
		else if (FieldsValidation.isInValidGlobalName(firstName)){
			error = "Bad characters not allowed (First name)";
			throwException(error);
		}
		
		if (StringUtils.isNotEmpty(middleName) || StringUtils.isNotBlank(middleName)){
			if (FieldsValidation.isInValidGlobalName(middleName)){
				error = "Bad characters not allowed (Middle name)";
				throwException(error);
			}
		}

		if (StringUtils.isEmpty(lastName) || StringUtils.isBlank(lastName)){
			error = "Last Name required";
			throwException(error);
		}
		else if (FieldsValidation.isInValidGlobalName(lastName)){
			error = "Bad characters not allowed (Last name)";
			throwException(error);
		}
		return true;
	}
	
	private boolean extensionValidation(String phoneExtension) throws Exception {
		String error = null;
		if (StringUtils.isNotEmpty(phoneExtension) || StringUtils.isNotBlank(phoneExtension)){
			if (FieldsValidation.isInValidMobPhone((phoneExtension))){
				error = "Bad characters not allowed (Phone Extension)";
				throwException(error);
			}
		}
		return true;
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
	/**
	 * 
	
	@Override
	@PayloadRoot(localPart = LEARNER_ENROLLED_COURSES_EVENT, namespace = TARGET_NAMESPACE)
	public LearnerEnrolledCoursesResponse getLearnerEnrolledCourses(LearnerEnrolledCoursesRequest learnerEnrolledCoursesRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PayloadRoot(localPart = STATISTICS_EVENT, namespace = TARGET_NAMESPACE)
	public StatisticsResponse getLearnerCoursesStatistics(StatisticsRequest request) {
	
		StatisticsResponse response = new StatisticsResponse();
		try {
			String customerId = request.getCustomerCode();
			Customer customer = customerService.getCustomerByCustomerCode(customerId);
			if (customer != null) {
				String userName = request.getUserId();
				Courses courses = request.getCourses();
				List<Course> courseList = courses.getCourse();
				VU360User vu360User = vu360UserService.findUserByUserName(userName);
				Learner learner = vu360User.getLearner();
				
				String customerCode = learner.getCustomer().getCustomerCode();
				if (!customerId.equalsIgnoreCase(customerCode)) {
					String errorMessage = userName + " not found";
					response.setUserId(userName);
					response.setTransactionResult(TransactionResultType.FAILURE);
					response.setTransactionResultMessage(errorMessage);
					return response;
				}
				
				Brander brand = getBrander(null, null);
				//Statistics courseStatistics = new Statistics();
				CourseStatistics courseStatistics = new CourseStatistics();
				List<CourseStats> courseStatsList = new ArrayList<CourseStats>();
				for (Course course : courseList) {
					CourseStats courseStats = new CourseStats();
					String courseGUID = course.getCourseID();
					com.softech.vu360.lms.model.Course newCourse = courseCourseGrpService.getCourseByGUID(courseGUID);
					if (newCourse == null) {
						String errorMessage = "No Course found of id " + courseGUID + " for " + userName;
						courseStats.setErrorCode(ERROR_CODE_ONE);
						courseStats.setErrorMessage(errorMessage);
						courseStatsList.add(courseStats);
						continue;
					}
					LearnerEnrollment learnerEnrollment = entitlementService.getLearnerEnrollmentsForLearner(learner, newCourse);
					if (learnerEnrollment == null) {
						String errorMessage = "No Enrollment found of " + userName + " for course " + newCourse.getName();
						courseStats.setErrorCode(ERROR_CODE_ONE);
						courseStats.setErrorMessage(errorMessage);
						courseStatsList.add(courseStats);
						continue;
					}
					
					String learnerEnrollmentId = Long.toString(learnerEnrollment.getId());
					//String courseId = null ;
					String viewType = "enrolled";
					String activeTab = "crnt";
					String crntEnrollmentId = null;
					String selEnrollmentPeriod = null;
					
					Map<Object, Object> context = enrollmentService.displayCourseDetailsPage(learnerEnrollmentId, crntEnrollmentId, 
							vu360User, activeTab, viewType, selEnrollmentPeriod, brand);
					LearnerCourseStatistics learnerCourseStatistics = (LearnerCourseStatistics)context.get("courseStatistics");
					//String status = learnerCourseStatistics.getStatusDisplayText();
					 if( (!context.get("courseType").equals("External Course")) && (!context.get("courseType").equals("DFC")) ) {
						 String courseProgress = String.valueOf(learnerCourseStatistics.getPercentComplete());
						 courseStats.setCourseProgress(courseProgress);
					 }
					
					Date firstAccessDate = learnerCourseStatistics.getFirstAccessDate();
					Date lastAccessDate = learnerCourseStatistics.getLastAccessDate();
					String totalNumberOfAccesses = null;
					if (learnerCourseStatistics.getLaunchesAccrued() > 0 ) {
						totalNumberOfAccesses = String.valueOf(learnerCourseStatistics.getLaunchesAccrued());
					} else {
						totalNumberOfAccesses = "--";
					}
					courseStats.setCourseID(courseGUID);
					courseStats.setCourseName(newCourse.getName());
					courseStats.setFirstAccessDate(getDateInXMLGregorianCalendarFormat(firstAccessDate));
					courseStats.setLastAccessDate(getDateInXMLGregorianCalendarFormat(lastAccessDate));
					courseStats.setTotalNumberofAccesses(Integer.parseInt(totalNumberOfAccesses));
					
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					if( (!context.get("courseType").equals("Synchronous Course"))  && (! context.get("courseType").equals("DFC"))) {
						
						if( (!context.get("courseType").equals("Scorm Course")) && (!context.get("courseType").equals("External Course"))) {
							
							String preAssessmentScore = null;
							String preTestScore = String.valueOf(learnerCourseStatistics.getPretestScore());
							String preTestDate = df.format(learnerCourseStatistics.getPreTestDate());
							
							if(preTestDate != null &&(!preTestScore.equals("-1.0"))) {
								
								preAssessmentScore = preTestScore;
							} else {
								preAssessmentScore = "--";
							}
							
							String averageQuizScore = null;
							String lastQuizDate = df.format(learnerCourseStatistics.getLastQuizDate());
							if(lastQuizDate != null) {
								averageQuizScore = String.valueOf(learnerCourseStatistics.getAverageQuizScore());
							} else {
								averageQuizScore = "--";
							}
							
							String lowestQuizScore = null;
							double lwQuizScore = learnerCourseStatistics.getLowestQuizScore();
							String lwstQuizScore = String.valueOf(lwQuizScore);
						
							if(lastQuizDate != null &&(!lwstQuizScore.equals("-1.0"))) {
								lowestQuizScore = lwstQuizScore;
							} else {
								lowestQuizScore = "--";
							}
							
							String highestQuizScore = null;
							if(lastQuizDate != null) {
								highestQuizScore = String.valueOf(learnerCourseStatistics.getHighestQuizScore());
							} else {
								highestQuizScore = "--";
							}
							
							String totalNumberOfQuizesAttempted = null;
							if (learnerCourseStatistics.getNumberQuizesTaken() > 0) {
								totalNumberOfQuizesAttempted = String.valueOf(learnerCourseStatistics.getNumberQuizesTaken());
							} else {
								totalNumberOfQuizesAttempted = "--";
							}
							courseStats.setPreAssessmentScore(new BigDecimal(preAssessmentScore));
							courseStats.setAverageQuizScore(new BigDecimal(averageQuizScore));
							courseStats.setLowestQuizScore(new BigDecimal(lowestQuizScore));
							courseStats.setHighestQuizScore(new BigDecimal(highestQuizScore));
							courseStats.setTotalNumberOfQuizzesAttempted(Integer.parseInt(totalNumberOfQuizesAttempted));
						}
						
						String averagePostTestScore = null;
						Date lastPostTestDate = learnerCourseStatistics.getLastPostTestDate();
						
						if (lastPostTestDate != null) {
							averagePostTestScore = String.valueOf(learnerCourseStatistics.getAveragePostTestScore());
						} else {
							averagePostTestScore = "--";
						}
						
						String lowestPostTestScore = null;
						String lwPostTestScore = String.valueOf(learnerCourseStatistics.getLowestPostTestScore());
						if(lastPostTestDate != null &&(!lwPostTestScore.equals("-1.0"))) {
							lowestPostTestScore = lwPostTestScore;
						} else {
							lowestPostTestScore = "--";
						}
						
						String highestPostTestScore = null;
						if(lastPostTestDate != null) {
							highestPostTestScore = String.valueOf(learnerCourseStatistics.getHighestPostTestScore());
						} else {
							highestPostTestScore = "--";
						}
						
						String totalNumberOfPostTestsAttempted = null;
						int numberPostTestsTaken = learnerCourseStatistics.getNumberPostTestsTaken();
						if (numberPostTestsTaken > 0) {
							totalNumberOfPostTestsAttempted = String.valueOf(numberPostTestsTaken);
						} else {
							totalNumberOfPostTestsAttempted = "--";
						}
						courseStats.setAveragePostTestScore(new BigDecimal(averagePostTestScore));
						courseStats.setLowestPostTestScore(new BigDecimal(lowestPostTestScore));
						courseStats.setHighestPostTestScore(new BigDecimal(highestPostTestScore));
						courseStats.setTotalNumberOfPostTestsAttempted(Integer.parseInt(totalNumberOfPostTestsAttempted));
					}
					
					String completionDate  = null;
					boolean isCourseCompleted = (Boolean)context.get("courseCompleted");
					Date courseCompletionDate = learnerCourseStatistics.getCompletionDate();
					if ( isCourseCompleted && courseCompletionDate != null ) {
						completionDate = df.format(courseCompletionDate);
					} else {
						completionDate = "Incomplete";
					}
					
					//courseStats.setCompletionDate(value)
					if( (!context.get("courseType").equals("Synchronous Course")) 
							&& (!context.get("courseType").equals("Scorm Course")) 
							&& (!context.get("courseType").equals("External Course")) 
							&& (!context.get("courseType").equals("DFC"))) {
						
						String totalTimeSpentInCourse_hours_minutes = null;
						Integer totalTimeInSeconds = learnerCourseStatistics.getTotalTimeInSeconds();
						if(totalTimeInSeconds != null) {
							totalTimeSpentInCourse_hours_minutes = String.valueOf(totalTimeInSeconds);
						} else {
							totalTimeSpentInCourse_hours_minutes = "00:00";
						}
						courseStats.setTotalTimeSpentInCourse(totalTimeSpentInCourse_hours_minutes);
					}
					courseStatsList.add(courseStats);
				} //end of for (Course course ..)
			
				courseStatistics.setCourseStats(courseStatsList);
				response.setUserId(userName);
				response.setCourseStatistics(courseStatistics);
				response.setTransactionResult(TransactionResultType.SUCCESS);
				response.setTransactionResultMessage("");
			} else {
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage("Customer not found");
			}
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
		}
		return response;
	} //end of getLearnerCoursesStatistics()
	 */
	private ResponseTrainingPlan getResponseTrainingPlanForError(ResponseTrainingPlan responseTrainingPlan, String trainingPlanName, String trainingPlanDescription, 
			String errorCode, String errorMessage){
		
		//ResponseTrainingPlan responseTrainingPlanError = new ResponseTrainingPlan();
		responseTrainingPlan.setName(trainingPlanName);
		responseTrainingPlan.setDescription(trainingPlanDescription);
		responseTrainingPlan.setErrorCode(errorCode);
		responseTrainingPlan.setErrorMessage(errorMessage);
		
		return responseTrainingPlan;
	
	}
	
	private TrainingPlanAssignResponsed getTrainingPlanAssignResponsed(Map<Learner, Map<String, Map<String, ?>>> learnerCourseEnrollMap, 
			TrainingPlanAssignResponsed trainingPlanAssignResponsed, List<TrainingPlanAssignResp> trainingPlanEnrollmentList,
			BigInteger trainingPlanId, com.softech.vu360.lms.model.TrainingPlan trainingPlan, VU360User user, 
			boolean notifyLearnersByEmail) throws Exception {
		
		Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap = new HashMap<Learner, List<LearnerEnrollment>>();
		for (Map.Entry<Learner, Map<String, Map<String, ?>>> entry : learnerCourseEnrollMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			
			TrainingPlanAssignResp trainingPlanEnrollment = new TrainingPlanAssignResp();
			TrainingPlanResponseCourses trainingPlanResponseCourses = new TrainingPlanResponseCourses();
			List<TrainingPlanResponseCourse> trainingPlanResponseCourseList = new ArrayList<TrainingPlanResponseCourse>();
			
			Learner learner = entry.getKey();
			Map<String, Map<String, ?>> courseMap = entry.getValue();
			
			Map<String, List<LearnerEnrollment>> courseSuccessMap = (Map<String, List<LearnerEnrollment>>)courseMap.get("successfulCoursesMap");
			Map<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorMap = (Map<String, Map<com.softech.vu360.lms.model.Course, String>>)courseMap.get("unSuccessfulCoursesMap");
			
			List<LearnerEnrollment> successfullCoursesEnrolledList = courseSuccessMap.get(learner.getVu360User().getUsername());
			
			if (!successfullCoursesEnrolledList.isEmpty()) {
				learnerEnrollmentEmailMap.put(learner, successfullCoursesEnrolledList);
				setTrainingPlanAssignment(trainingPlan, successfullCoursesEnrolledList);
			}
			
			for (LearnerEnrollment learnerEnrollment : successfullCoursesEnrolledList) {
				
				TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
				String courseGUID = learnerEnrollment.getCourse().getCourseGUID();
				trainingPlanResponseCourse.setErrorCode(ERROR_CODE_ZERO);
				trainingPlanResponseCourse.setErrorMessage("");
				trainingPlanResponseCourse.setCourseID(courseGUID);
				trainingPlanResponseCourseList.add(trainingPlanResponseCourse);
			}
			
			for (Map.Entry<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorEntry : courseErrorMap.entrySet()) {
				//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				String userName = courseErrorEntry.getKey();
				if (userName.equals(learner.getVu360User().getUsername())) {
					Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = courseErrorEntry.getValue();
					for (Map.Entry<com.softech.vu360.lms.model.Course, String> enrollErrorEntry : enrollErrorMap.entrySet()) {
						//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						com.softech.vu360.lms.model.Course course = enrollErrorEntry.getKey();
						String errorMessage = enrollErrorEntry.getValue();
						String courseGUID = course.getCourseGUID();
						TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
						trainingPlanResponseCourse.setErrorCode(ERROR_CODE_ONE);
						trainingPlanResponseCourse.setErrorMessage(errorMessage);
						trainingPlanResponseCourse.setCourseID(courseGUID);
						
						trainingPlanResponseCourseList.add(trainingPlanResponseCourse);
					}
				}	
			}
			
			trainingPlanResponseCourses.setTrainingPlanCourse(trainingPlanResponseCourseList);
			
			trainingPlanEnrollment.setUserId(learner.getVu360User().getUsername());
			trainingPlanEnrollment.setTrainingPlanCourses(trainingPlanResponseCourses);
			trainingPlanEnrollment.setErrorCode(ERROR_CODE_ZERO);
			trainingPlanEnrollment.setErrorMessage("");
			
			trainingPlanEnrollmentList.add(trainingPlanEnrollment);
			
		} //end of for()
		
		if (notifyLearnersByEmail && user != null) {
			if (!learnerEnrollmentEmailMap.isEmpty()) {
				Brander brander = getBrander(null, null);
				String loginURL = getLoginUrl();
				sendEmailToLearners(learnerEnrollmentEmailMap, loginURL, user, brander);
			}
		}
		
		trainingPlanAssignResponsed.setTrainingPlanId(trainingPlanId);
		trainingPlanAssignResponsed.setTrainingPlanEnrollment(trainingPlanEnrollmentList);
		trainingPlanAssignResponsed.setErrorCode(ERROR_CODE_ZERO);
		trainingPlanAssignResponsed.setErrorMessage("");
		
		return trainingPlanAssignResponsed;
	} //end of getTrainingPlanAssignResponsed()
	
	private void sendEmailToLearners(Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap, String loginURL, VU360User user,
			Brander brander ) {
		LmsApiEmailAsyncTask emailTask = new LmsApiEmailAsyncTask(brander, loginURL, user, learnerEnrollmentEmailMap, 
				learnersToBeMailedService);
		asyncTaskExecutorWrapper.execute(emailTask);
	}
	
	private void setTrainingPlanAssignment(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
			List<LearnerEnrollment> learnerEnrollmentList) {
		TrainingPlanAssignment tpa = new TrainingPlanAssignment();
		tpa.setTrainingPlan(trainingPlan);
		tpa.setLearnerEnrollments(learnerEnrollmentList);
		trainingPlanService.addTrainingPlanAssignments(tpa);
	}
	
	private static Brander getBrander(String brandName, com.softech.vu360.lms.vo.Language language ) throws Exception {
		if (StringUtils.isEmpty(brandName) && StringUtils.isBlank(brandName)) {
			brandName = "default";
		}
		
		if (language == null) {
			language = new com.softech.vu360.lms.vo.Language();
			language.setLanguage(Language.DEFAULT_LANG);
		}
		Brander brander = VU360Branding.getInstance().getBrander(brandName, new com.softech.vu360.lms.vo.Language());
		return brander;
	}
	
	private String getLoginUrl() {
		String loginURL = VU360Properties.getVU360Property("lms.loginURL") + "login.do";
		return loginURL;
	}
	
	private VU360User getVU360UserByCustomerGUID(Customer customer) {
		if (customer == null) {
			return null;
		}
		String customerGUID = customer.getCustomerGUID();
		return getVU360UserByCustomerGUID(customerGUID);
	}
	
	private VU360User getVU360UserByCustomerGUID(String customerGUID) {
		VU360User manager = vu360UserService.getUserByGUID(customerGUID);
		return manager;
	}
	
	private String isTrainingPlanIdProvided(BigInteger trainingPlanId) {
		String errorMessage = null;
		if( trainingPlanId == null) {
			errorMessage = "TrainingPlan Id can not be empty";
			return errorMessage;
		}
		return errorMessage;
	} //end if isTrainingPlanIdProvided()
	
	private TrainingPlanAssignResp getTrainingPlanEnrollmentForError(String errorCode, String errorMessage, String userName) {
		TrainingPlanAssignResp trainingPlanEnrollment = new TrainingPlanAssignResp();
		trainingPlanEnrollment.setErrorCode(errorCode);
		trainingPlanEnrollment.setErrorMessage(errorMessage);
		trainingPlanEnrollment.setUserId(userName);
		
		return trainingPlanEnrollment;
	}
	
	private Object entitlementLevelEnrollmentValidations(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		log.debug("entitlementLevelEnrollmentValidations() start for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
			
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();
	    List<CustomerEntitlement> customerEntitlementList = getCustomerEntitlementListForCourse(customer, course);
			
		if ( customerEntitlementList.isEmpty() ) { //if no contract found then exit
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not exist for customer: " + customer.getCustomerCode();
			log.debug(errorMessage);
			return errorMessage;
		} 
			
		List<Date> contractEndDatesList = new ArrayList <Date>();
		List<Date> contractStartDatesList = new ArrayList <Date>();
		
		for( CustomerEntitlement ent : customerEntitlementList ) {	
			boolean isSeatAvailable = false;
			if (!ent.isAllowUnlimitedEnrollments()) {
				if (ent.hasAvailableSeats(1)) {
					isSeatAvailable = true;		
				}
			} else {
				isSeatAvailable = true;
			}
			if (isSeatAvailable) {
				if ( ent.getEndDate() != null && !ent.getEndDate().before(todayDate.getTime())) {																
					   contractEndDatesList.add(ent.getEndDate());
					   contractStartDatesList.add(ent.getStartDate());								
				} else{
					Calendar c = Calendar.getInstance();
					c.setTime(ent.getStartDate()); // Now use entitlement start date.
					c.add(Calendar.DATE, ent.getDefaultTermOfServiceInDays()); // Adding days
					Date entEndDate = c.getTime();
					
					if(!entEndDate.before(todayDate.getTime())){ 
					   contractEndDatesList.add(entEndDate);
					   contractStartDatesList.add(ent.getStartDate());
					}
				}	
			}
		} //end of for for( CustomerEntitlement ent : ...)
		
		if (contractStartDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			log.debug(errorMessage);
			return errorMessage;
		}
		
		if (contractEndDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			log.debug(errorMessage);
			return errorMessage;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		Date courseStartDate = enrollmentStartDate;
		Date courseEndDate = enrollmentEndDate;
		
		if ( courseStartDate.before(todayDate.getTime()) ) {							
			
			Date minEndDate = Collections.min(contractEndDatesList);
			String minEnrollmentEndDate = sdf.format(minEndDate);
			String todaysDate = sdf.format(todayDate.getTime());
			
			errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			log.debug(errorMessage);
			return errorMessage;
		} 
		
		if( contractEndDatesList != null && !contractEndDatesList.isEmpty() ) {
			Date maxEndDate = Collections.max(contractEndDatesList); 
			if( courseStartDate.after(maxEndDate) ) {
				errorMessage = "Start Date cannot be after any Entitlement's End Date.";
				log.debug(errorMessage);
				return errorMessage;
			}
		}
		
		if( contractStartDatesList != null && !contractStartDatesList.isEmpty() ) {
			Date minStartDate = Collections.min(contractStartDatesList);
			if( courseStartDate.before(minStartDate) ) {
				errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
				log.debug(errorMessage);
				return errorMessage;
			}
		}
		
		if (enrollmentEndDate != null) {
			
			//LMS-15933
			if (courseEndDate.before(courseStartDate))
			{
				errorMessage = "Start date can not be after End Date";
				log.debug(errorMessage);
				return errorMessage;
			}
			//LMS-15933	
			if ( courseEndDate.before(todayDate.getTime()) ) {							
				
				Date minEndDate = Collections.min(contractEndDatesList);
				String minEnrollmentEndDate = sdf.format(minEndDate);
				String todaysDate = sdf.format(todayDate.getTime());
				
				errorMessage = "End date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
				log.debug(errorMessage);
				return errorMessage;
			} 
			
			if( contractEndDatesList != null && !contractEndDatesList.isEmpty() ) {
				Date maxEndDate = Collections.max(contractEndDatesList);
				if( courseEndDate.after(maxEndDate) ) {
					
					String minEnrollmentEndDate = sdf.format(maxEndDate);
					errorMessage = "End Date must not be later than " + minEnrollmentEndDate;
					log.debug(errorMessage);
					return errorMessage;
					
				}
			}
		}
	
		log.debug("entitlementLevelEnrollmentValidations() end for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
		return customerEntitlementList;
	}
	
	private String entitlementValidation(CustomerEntitlement customerEntitlement, Date enrollmentStartDate, Date enrollmentEndDate) 
			throws Exception {
		log.debug("entitlementValidation() start for CustomerEntitlement: " + customerEntitlement.getId() + ": " + 
		customerEntitlement.getName());	
		
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();	
		Date contractEndDate = null;
		Date contractStartDate = null;
								
		if( customerEntitlement.getEndDate() != null && !customerEntitlement.getEndDate().before(todayDate.getTime())) {																
			contractEndDate = customerEntitlement.getEndDate();
			contractStartDate = customerEntitlement.getStartDate();								
		}else{	
			Calendar c = Calendar.getInstance();
			c.setTime(customerEntitlement.getStartDate()); // Now use entitlement start date.
			c.add(Calendar.DATE, customerEntitlement.getDefaultTermOfServiceInDays()); // Adding days
			Date entEndDate = c.getTime();
					
			if(!entEndDate.before(todayDate.getTime())){ 
				contractEndDate = entEndDate;
				contractStartDate= customerEntitlement.getStartDate();
			}
		}													
			
		if (contractStartDate == null) {
			errorMessage = "Entitlement has expired";
			log.debug(errorMessage);
			return errorMessage;
		}
			
		if (contractEndDate == null) {
			errorMessage = "Entitlement has expired";
			log.debug(errorMessage);
			return errorMessage;
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");	
		if ( enrollmentStartDate.before(todayDate.getTime()) ) {
			String minEnrollmentEndDate = sdf.format(contractEndDate);
			String todaysDate = sdf.format(todayDate.getTime());
			errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			log.debug(errorMessage);
			return errorMessage;
		} 
							
		if( enrollmentStartDate.after(contractEndDate) ) {
			errorMessage = "Start Date cannot be after any Entitlement's End Date.";
			log.debug(errorMessage);
			return errorMessage;
		}
			
		if( enrollmentStartDate.before(contractStartDate) ) {
			errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
			log.debug(errorMessage);
			return errorMessage;
		}
		
		if (enrollmentEndDate != null) {
			
			if (enrollmentEndDate.before(enrollmentStartDate))
			{
				errorMessage = "Start date can not be after End Date";
				log.debug(errorMessage);
				return errorMessage;
			}
			//LMS-15933	
			if ( enrollmentEndDate.before(todayDate.getTime()) ) {							
				
				Date minEndDate = enrollmentEndDate;
				String minEnrollmentEndDate = sdf.format(minEndDate);
				String todaysDate = sdf.format(todayDate.getTime());
				
				errorMessage = "End date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
				log.debug(errorMessage);
				return errorMessage;
			} 
			
			if( enrollmentEndDate.after(contractEndDate) ) {	
				String minEnrollmentEndDate = sdf.format(contractEndDate);
				errorMessage = "End Date must not be later than " + minEnrollmentEndDate;
				log.debug(errorMessage);
				return errorMessage;		
			}	
		}
		
		boolean isSeatAvailable = false;
		if (!customerEntitlement.isAllowUnlimitedEnrollments()) {
			if (customerEntitlement.hasAvailableSeats(1)) {
				isSeatAvailable = true;		
			}
		} else {
			isSeatAvailable = true;
		}
					
		if (!isSeatAvailable) {
			errorMessage = "Seats not available in Customer Entitlement: " + customerEntitlement.getId() + ": " + customerEntitlement.getName();
			log.debug(errorMessage);
			return errorMessage;
		}
			
		log.debug("entitlementValidation() end for CustomerEntitlement: " + customerEntitlement.getId() + ": " + 
				customerEntitlement.getName());	
		return errorMessage;
	}

	private String courseLevelEnrollmentValidations(com.softech.vu360.lms.model.Course course){
		log.debug("courseLevelEnrollmentValidations() start for course: " + course.getCourseGUID());
		String errorMessage = null;
		
		//If not active enrollment exists and course is retired, then exit. 
		if(course.isRetired()) {
			errorMessage = "Course is retired";
			log.debug(errorMessage);
			return errorMessage;
		}
				
		if(!course.getCourseStatus().equalsIgnoreCase("Published")) {
			errorMessage = "Course is not published.";
			log.debug(errorMessage);
			return errorMessage;
		}
			
		log.debug("courseLevelEnrollmentValidations() end for course: " + course.getCourseGUID());
		return errorMessage;
	}
	
	private Object getCourseEntitlementList(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		Object result = null; 
		result = courseLevelEnrollmentValidations(course);
		if (result != null) {
			return result;
		}
		
		result = entitlementLevelEnrollmentValidations(customer, course, enrollmentStartDate, enrollmentEndDate);
		if (result != null) {
			return result;
		}
		return result;
	}
	
	private TrainingPlanResponseCourse getTrainingPlanResponseCourse(String courseGUID, String errorCode, String errorMessage) {
		TrainingPlanResponseCourse trainingPlanResponseCourse = new TrainingPlanResponseCourse();
		trainingPlanResponseCourse.setCourseID(courseGUID);
		trainingPlanResponseCourse.setErrorCode(errorCode);
		trainingPlanResponseCourse.setErrorMessage(errorMessage);
		return trainingPlanResponseCourse;
	} //end of setCreateTrainingPlanResponseForCourse()
	
	private Map<String, Map<String, ?>> doEnrollment(Customer customer, Learner learner, 
			Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		log.debug("doEnrollment() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		
		Map<String, Map<String, ?>> courseMap = new HashMap<String, Map<String, ?>>();
		Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = new HashMap<com.softech.vu360.lms.model.Course, String>();
		Map<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorMap = new HashMap<String, Map<com.softech.vu360.lms.model.Course, String>>();
		Map<String, List<LearnerEnrollment>> courseSuccessMap = new HashMap<String, List<LearnerEnrollment>>();
		List<LearnerEnrollment> learnerEnrollmentList = new ArrayList<LearnerEnrollment>();
		for (Map.Entry<com.softech.vu360.lms.model.Course, Object> entry : courseEntitlementMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			com.softech.vu360.lms.model.Course course = entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof String) {
				String errorMessage = (String)value;
				enrollErrorMap.put(course, errorMessage);
			} else if (value instanceof List<?>) {
				List<CustomerEntitlement> customerEntitlementList = (List<CustomerEntitlement>)value;
				Object result = getEnrollmentForCourse(customer, learner, course, enrollmentStartDate, enrollmentEndDate);
				if (result instanceof LearnerEnrollment) {
					LearnerEnrollment newEnrollment = (LearnerEnrollment)result;
					learnerEnrollmentList.add(newEnrollment);
				} else if (result instanceof String) { 
					String errorMessage = (String)result;
					enrollErrorMap.put(course, errorMessage);
				}
			}	
		} //end of for()
		
		courseSuccessMap.put(learner.getVu360User().getUsername(), learnerEnrollmentList);
		courseErrorMap.put(learner.getVu360User().getUsername(), enrollErrorMap);
		courseMap.put("successfulCoursesMap", courseSuccessMap);
		courseMap.put("unSuccessfulCoursesMap", courseErrorMap);
		
		log.debug("doEnrollment() end for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		return courseMap;
	
	} //end of doEnrollment()
	
	/**
	 * Gets existing enrollments or creates a new enrollment for user 
	 * @param course_id
	 * @param customer_id
	 * @param context
	 * @param cust
	 * @param course
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private Object getEnrollmentForCourse(Customer customer,  Learner learner, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate)throws Exception {
		log.debug("getEnrollmentForCourse() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		String errorMessage = null;
		CustomerEntitlement custEntitlement = null;
		List<CustomerEntitlement> customerEntitlementList = getCustomerEntitlementListForCourse(customer, course);
		if ( customerEntitlementList.isEmpty() ) { //if no contract found then exit
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not exist for customer: " + customer.getName();
			log.debug(errorMessage);
			return errorMessage;
		} 
		
		Date maxEntitlementEndDate = null;
		
		//Get that entitlement that has maximum entitlement end date
		for( CustomerEntitlement ent : customerEntitlementList ) {
			String error = entitlementValidation(ent, enrollmentStartDate, enrollmentEndDate);
			if (error == null) {
				if (maxEntitlementEndDate == null) {
					maxEntitlementEndDate = ent.getEntitlementEndDate();
					custEntitlement = ent;
				} else {
					if (ent.getEntitlementEndDate().after(maxEntitlementEndDate)) {
						maxEntitlementEndDate = ent.getEntitlementEndDate();
						custEntitlement = ent;
					}
			 	}
			}
		} //end of for()
		
		// catter Available seats are less than the no. of users to be enrolled.
		if (custEntitlement == null) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			log.debug(errorMessage);
			return errorMessage;
		}
		Object result = getEnrollmentForCourse(customer, learner, course, custEntitlement, enrollmentStartDate, enrollmentEndDate);
		return result;
	}
	
	private Object getEnrollmentForCourse(Customer customer,  Learner learner, com.softech.vu360.lms.model.Course course,
			CustomerEntitlement customerEntitlement, Date enrollmentStartDate, Date enrollmentEndDate)throws Exception {
		
		log.debug("getEnrollmentForCourse() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		
		//Get active enrollment for course. 
		LearnerEnrollment enrollment = getLearnerEnrollment(learner, course);
		
		//If no active enrollment exists, enroll into a valid contract
		if (enrollment == null ) {
			enrollment = addEnrollment(learner, customerEntitlement, course, enrollmentStartDate, enrollmentEndDate);
		} else {
			String enrollmentError = learnerEnrollmentValidations(enrollment);
			if (enrollmentError != null) {
				return enrollmentError;
			}
			enrollment = addEnrollment(learner, customerEntitlement, course, enrollmentStartDate, enrollmentEndDate);
		}
		log.debug("getEnrollmentForCourse() end for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		return enrollment;
	}
	
	private Map<com.softech.vu360.lms.model.Course, Object> getCourseEntitlementMap(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
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
		
		refreshCoursesInCache(courseList);
		Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = getCourseEntitlementMap(customer, courseList, enrollmentStartDate, enrollmentEndDate);
		return courseEntitlementMap;
	}
	
	private Map<com.softech.vu360.lms.model.Course, Object> getCourseEntitlementMap(Customer customer, 
			List<com.softech.vu360.lms.model.Course> courseList, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		log.debug("getCourseEntitlementMap() start");
		Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = new HashMap<com.softech.vu360.lms.model.Course, Object>();
		for (com.softech.vu360.lms.model.Course course : courseList) {
			Object result = getCourseEntitlementList(customer, course, enrollmentStartDate, enrollmentEndDate);
			if (result instanceof String) {
				String error = (String)result;
				courseEntitlementMap.put(course, error);
			} else if (result instanceof List<?>) {
				List<CustomerEntitlement> customerEntitlementList = (List<CustomerEntitlement>)result;
				courseEntitlementMap.put(course, customerEntitlementList);
			}
		}
		log.debug("getCourseEntitlementMap() end");
		return courseEntitlementMap;
	}
	
	private String learnerEnrollmentValidations(LearnerEnrollment enrollment) {
		
		log.debug("learnerEnrollmentValidations() start for learner: " + enrollment.getLearner().getId() + ", " + enrollment.getLearner().getVu360User().getUsername());
		boolean isExpired = false;
		String error = null;
		if (!(enrollment.getEnrollmentEndDate().compareTo(new Date(System.currentTimeMillis()))<0)) {    // date1.compareTo(date2)<0 --> Date1 is before Date2
			error = "Already Enrolled";
			log.debug(error);
			return error;
		} else {
			isExpired = true;
		}
		
		if (isExpired) {
			return null;
		}
		
		String enrollmentStatus = enrollment.getEnrollmentStatus();
		if (enrollmentStatus.equalsIgnoreCase("active")) {
			error = "Already Enrolled";
			log.debug(error);
			return error;
		}
		
		LearnerCourseStatistics courseStats= statsService.loadForUpdateLearnerCourseStatistics(enrollment.getCourseStatistics().getId());
		if (!courseStats.isCourseCompleted()) {
			error = "Already Enrolled";
			log.debug(error);
			return error;
		}
		log.debug("learnerEnrollmentValidations() start for learner: " + enrollment.getLearner().getId() + ", " + enrollment.getLearner().getVu360User().getUsername());
		return error;
	}
	
	private LearnerEnrollment getLearnerEnrollment(Learner learner, com.softech.vu360.lms.model.Course course) {
		
		//Get active enrollment for course. 
		LearnerEnrollment learnerEnrollment = enrollmentService.getAICCLearnerEnrollment(null, learner.getId(),course.getId());
		return learnerEnrollment;
	}
	
	private LearnerEnrollment addEnrollment(Learner learner, CustomerEntitlement custEntitlement, 
			com.softech.vu360.lms.model.Course course, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception{
		
		// create new enrollment
		log.debug("creating new enrollment");
		LearnerEnrollment newEnrollment = new LearnerEnrollment();
		newEnrollment.setCourse(course);
		newEnrollment.setCustomerEntitlement(custEntitlement);
		newEnrollment.setLearner(learner);
		//newEnrollment.setEnrollmentStartDate(new Date(System.currentTimeMillis()));
		newEnrollment.setEnrollmentStartDate(enrollmentStartDate);
		newEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		//newEnrollment.setEnrollmentEndDate(custEntitlement.getEntitlementEndDate());
		newEnrollment.setEnrollmentEndDate(enrollmentEndDate);
		log.debug("calling service to create enrollment");
		//LearnerEnrollment enrollment = enrollmentService.addEnrollment(learner, custEntitlement, null, newEnrollment);
		
		OrgGroupEntitlement orgGroupContract = null;
		LearnerEnrollment enrollment = enrollmentService.addEnrollment(newEnrollment, custEntitlement, orgGroupContract);
		log.debug("creating new enrollment complete");
		return enrollment;
	}
	
	private String assignTrainingPlanValidations(BigInteger trainingPlanId, String startDate, 
			String endDate) throws Exception {
		
		log.debug("assignTrainingPlanValidations() start");
		String errorMessage = null;
		if (StringUtils.isEmpty(startDate) && StringUtils.isNotBlank(startDate)){
			errorMessage = "Training Plan Start Date cannot be blank or empty";
			log.debug(errorMessage);
			return errorMessage;
		}
		
		if (StringUtils.isEmpty(endDate) && StringUtils.isNotBlank(endDate)){
			errorMessage = "Training Plan End Date cannot be blank or empty";
			log.debug(errorMessage);
			return errorMessage;
		}
		
		String trainingPlanErrorMessage = validateTrainingPlanDate(startDate, endDate);
		if ( trainingPlanErrorMessage != null) {
			log.debug(trainingPlanErrorMessage);
			return trainingPlanErrorMessage;	
		} 
		 
	    String trainingPlanIdError = isTrainingPlanIdProvided(trainingPlanId);
		if( trainingPlanIdError != null) {
			log.debug(trainingPlanIdError);
			return trainingPlanIdError;
		} 
		log.debug("assignTrainingPlanValidations() end");
		return errorMessage;
	} //end of assignTrainingPlan()
	
	private GregorianCalendar getTodayDate() {
		
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
		
		return todayDate;
		
	} //end of getTodayDate()

	private TrainingPlanAssignResponsed getTrainingPlanAssignResponsed(BigInteger trainingPlanId, String errorCode, String errorMessage) {
		
		TrainingPlanAssignResponsed trainingPlanAssignResponsed = new TrainingPlanAssignResponsed();
		trainingPlanAssignResponsed.setTrainingPlanId(trainingPlanId);
		trainingPlanAssignResponsed.setErrorCode(errorCode);
		trainingPlanAssignResponsed.setErrorMessage(errorMessage);
		
		return trainingPlanAssignResponsed;
		
	} //end of getTrainingPlanAssignResponsed()
	
	private List<CustomerEntitlement> getCustomerEntitlementListForCourse(Customer customer, com.softech.vu360.lms.model.Course course) {
		List<CustomerEntitlement> customerEntitlementList = entitlementService.getCustomerEntitlementsByCourseId(customer, course.getId());
		return customerEntitlementList;
	} //end of getCustomerEntitlementListForCourse()
	
	private TrainingPlanCourse getTrainingPlanCourse(com.softech.vu360.lms.model.TrainingPlan newTrainingPlan, com.softech.vu360.lms.model.Course newCourse) throws Exception {
		
		try {
			TrainingPlanCourse tpcourse = null;
			if (newCourse != null) {
				tpcourse = new TrainingPlanCourse();
				tpcourse.setCourse(newCourse);
				tpcourse.setTrainingPlan(newTrainingPlan);
			}
			return tpcourse;
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	} //end of getTrainingPlanCoursesList()
	
	private com.softech.vu360.lms.model.TrainingPlan findTrainingPlanById(long trainingPlanId) {
		com.softech.vu360.lms.model.TrainingPlan trainingPlan= trainingPlanService.getTrainingPlanByID(trainingPlanId);
		return trainingPlan;
	}
	
	private String validateTrainingPlanDate(String startDate, String endDate) throws Exception {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date tpStartDate = formatter.parse(startDate);
			Date tpEndDate = formatter.parse(endDate);
			if( tpEndDate.before(tpStartDate) ) {
				return "Training Plan End Date cannot be before Training Plan's Start Date.";
			}
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			throw new Exception(errorMessage);
		}
		return null;
	} //end of trainingPlanDateValidations()
	
	private com.softech.vu360.lms.model.TrainingPlan createTrainingPlan(String trainingPlanName, String trainingPlanDescription, Customer customer) throws Exception {
		 	
		com.softech.vu360.lms.model.TrainingPlan newTrainingPlan = new com.softech.vu360.lms.model.TrainingPlan();
		newTrainingPlan.setName(trainingPlanName);
		newTrainingPlan.setDescription(trainingPlanDescription);
		newTrainingPlan.setCustomer(customer);
				
		newTrainingPlan= trainingPlanService.addTrainingPlan(newTrainingPlan);
				
		return newTrainingPlan;
		    		
	} //end of createTrainingPlan()
	
	private List<TrainingPlanCourse> addCoursesToTrainingPlan(List <TrainingPlanCourse> trainingPlanCoursesList) throws Exception {
		List<TrainingPlanCourse> trainingPlanCoursesAddedList = trainingPlanService.addTrainingPlanCourses(trainingPlanCoursesList);
		return 	trainingPlanCoursesAddedList;
	} //end of addCoursesToTrainingPlan()
	
	private Map<String, OrganizationalGroup> getOrganizationalGroupMap (Map<String, OrganizationalGroup> orgGroupMap, OrganizationalGroup rootOrgGroup){
    	orgGroupMap.put(rootOrgGroup.toString().toUpperCase(), rootOrgGroup);
        List<OrganizationalGroup> childrenOrgGroups = rootOrgGroup.getChildrenOrgGroups();
        if(childrenOrgGroups != null && childrenOrgGroups.size() > 0) {
        	 for (OrganizationalGroup childOrgGroup : childrenOrgGroups) {
             	orgGroupMap = this.getOrganizationalGroupMap(orgGroupMap, childOrgGroup);
             }
        }        
        return orgGroupMap;
    }
	
	@Override
	//@PayloadRoot(localPart = LEARNER_COURSES_ENROLL_REQUEST, namespace = ENROLLMENT_TARGET_NAMESPACE)
	public LearnerCoursesEnrollResponse learnerCoursesEnroll(LearnerCoursesEnrollRequest request) {
		
		log.debug("Learner Courses Enroll Request start");
		final String REQUEST_END_MESSAGE = "Learner Courses Enroll Request end";
		
		LearnerCoursesEnrollResponse response = new LearnerCoursesEnrollResponse();
		List<LearnerEnrolledCourses> learnerEnrolledCoursesResponseList = new ArrayList<LearnerEnrolledCourses>();
		
		boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
				VU360User manager = null;
				if (notifyLearnersByEmail) {
					manager = getVU360UserByCustomerGUID(customer);
					if (manager == null) {
						String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customerCode ;
						response.setTransactionResult(TransactionResultType.FAILURE);
						response.setTransactionResultMessage(errorMessage);
						log.debug(REQUEST_END_MESSAGE);
						return response;
					}
				}
				
				List<LearnerCourses> learnerCoursesList = request.getLearnerCourses();
				if (learnerCoursesList == null || learnerCoursesList.isEmpty()) {
					String errorMessage = "LearnerCourses element is required and must not be empty";
					response.setTransactionResult(TransactionResultType.FAILURE);
					response.setTransactionResultMessage(errorMessage);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
				
				Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap = new HashMap<Learner, List<LearnerEnrollment>>();
				
				for (LearnerCourses learnerCourses : learnerCoursesList) {
					String userName = learnerCourses.getUserId();
					LearnerEnrollCourses courses = learnerCourses.getCourses();
					List<com.softech.vu360.lms.model.Course> validCourseList = new ArrayList<com.softech.vu360.lms.model.Course>();
					EnrolledCourses enrolledCourses = new EnrolledCourses();
					
					List<EnrolledCourse> enrolledCourseList = new ArrayList<EnrolledCourse>();
					try {
						Learner validLearner = getValidLearner(userName, customerCode);
						if (learnerCoursesValidation(courses)) {
							List<String> courseGuidList = courses.getCourseId();
							//refreshCoursesCache(courseGuidList);
								for (String courseGuid : courseGuidList) {
									try {
										com.softech.vu360.lms.model.Course validCourse = getValidCourse(customer, courseGuid);
										validCourseList.add(validCourse);
									} catch (Exception e) {
										String errorMessage = e.getMessage();
										EnrolledCourse enrolledCourseError = getEnrolledCourse(ERROR_CODE_ONE, errorMessage, courseGuid);
										enrolledCourseList.add(enrolledCourseError);
									}
									
								} //end of for (String courseGuid ..)
								
								if (validCourseList.isEmpty()) {
									enrolledCourses.setCourse(enrolledCourseList);
									String errorMessage = "No course found for enrollment";
									LearnerEnrolledCourses learnerEnrolledCoursesError = getLearnerEnrolledCourses(userName, ERROR_CODE_ONE, errorMessage);
									learnerEnrolledCoursesError.setCourses(enrolledCourses);
									learnerEnrolledCoursesResponseList.add(learnerEnrolledCoursesError);
									continue;
								}
								
								String startDate = courses.getEnrollmentStartDate().toString();
								String endDate = courses.getEnrollmentEndDate().toString();
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
								startDate = startDate + " 00:00:00.000";
								endDate = endDate + " 23:59:59.000";
								Date enrollmentStartDate = formatter.parse(startDate);
								Date enrollmentEndDate = formatter.parse(endDate);
								
								Map<String, Map<String, ?>> learnerCourseMap = processEnrollments(customer, validLearner, validCourseList, enrollmentStartDate, enrollmentEndDate);
								List<LearnerEnrollment> learnerSuccessfullCoursesEnrolledList = getLearnerSuccessfullCoursesEnrolledList(validLearner, learnerCourseMap);
								Map<com.softech.vu360.lms.model.Course, String> learnerUnsuccessfullCoursesErrorMap = getLearnerUnsuccessfullCoursesErrorMap(validLearner, learnerCourseMap);
								
								if (learnerSuccessfullCoursesEnrolledList != null && !learnerSuccessfullCoursesEnrolledList.isEmpty()) {
									learnerEnrollmentEmailMap.put(validLearner, learnerSuccessfullCoursesEnrolledList);
									for (LearnerEnrollment learnerEnrollment : learnerSuccessfullCoursesEnrolledList) {
										String courseGuid = learnerEnrollment.getCourse().getCourseGUID();
										EnrolledCourse enrolledCourse = getEnrolledCourse(ERROR_CODE_ZERO, "", courseGuid);
										enrolledCourseList.add(enrolledCourse);
									}	
								} 
								
								if (learnerUnsuccessfullCoursesErrorMap != null && !learnerUnsuccessfullCoursesErrorMap.isEmpty()) {
									for (Map.Entry<com.softech.vu360.lms.model.Course, String> entry : learnerUnsuccessfullCoursesErrorMap.entrySet()) {
										//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
										com.softech.vu360.lms.model.Course course = entry.getKey();
										String errorMessage = entry.getValue();
										String courseGuid = course.getCourseGUID();
										EnrolledCourse enrolledCourseError = getEnrolledCourse(ERROR_CODE_ONE, errorMessage, courseGuid);
										enrolledCourseList.add(enrolledCourseError);
									}
								}
								
								
								enrolledCourses.setCourse(enrolledCourseList);
								LearnerEnrolledCourses learnerEnrolledCourses = getLearnerEnrolledCourses(userName, ERROR_CODE_ZERO, "");
								learnerEnrolledCourses.setCourses(enrolledCourses);
								learnerEnrolledCoursesResponseList.add(learnerEnrolledCourses);			
						}
						
					} catch (Exception e) {
						String errorMessage = e.getMessage();
						LearnerEnrolledCourses learnerEnrolledCoursesError = getLearnerEnrolledCourses(userName, ERROR_CODE_ONE, errorMessage);
						learnerEnrolledCoursesResponseList.add(learnerEnrolledCoursesError);	
					}
						
				} //end of for()
				
				if (notifyLearnersByEmail && manager != null) {
					if (learnerEnrollmentEmailMap != null && !learnerEnrollmentEmailMap.isEmpty()) {
						Brander brander = getBrander(null, null);
						String loginURL = getLoginUrl();
						sendEmailToLearners(learnerEnrollmentEmailMap, loginURL, manager, brander);
					}
				}
					
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage("");
		response.setLearnerEnrolledCourses(learnerEnrolledCoursesResponseList);
		log.debug(REQUEST_END_MESSAGE);
		
		return response;
		
	} //end of bulkEnrollment()
	
	private EnrolledCourse getEnrolledCourse(String errorCode, String errorMessage, String courseGuid) {
		
		EnrolledCourse enrolledCourseError = new EnrolledCourse();
		enrolledCourseError.setErrorCode(errorCode);
		enrolledCourseError.setErrorMessage(errorMessage);
		enrolledCourseError.setCourseId(courseGuid);
		return enrolledCourseError;
		
	}
	
	private Map<String, Map<String, ?>> processEnrollments(Customer customer, Learner learner, List<com.softech.vu360.lms.model.Course> courseList, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		String errrorMessage = null;
		
		if (customer == null) {
			errrorMessage = "Customer is null";
			throwException(errrorMessage);
		} // end of if (learnerCoursesValidation...)
		
		if (learner == null) {
			errrorMessage = "Learner is null";
			throwException(errrorMessage);
		}
		
		if (courseList == null || courseList.isEmpty()) {
			errrorMessage = "Course list is null or empty";
			throwException(errrorMessage);
		}
		
		/**
		 * Here we are getting courses that are in customer entitlement. By doing this we can enroll only those courses to 
		 * learner for which we find entitlement. If we check customer entitlement when we assign courses to learner then its 
		 * a performance issue, Because for each learner it needs to check whether courses are in cutomer entitlement or not. 
		 * And if we found in the first learner that these courses are not in customer entitlement then for each learner this 
		 * check will again run, which is a performance issue.
		 */
		Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap = getCourseEntitlementMap(customer, courseList, enrollmentStartDate, enrollmentEndDate);
		
		/**
		 * We are getting customer entitlement again in this method because after enrollment seats are updated in Database. 
		 * If we use the entitlements that we get using the getCourseEntitlementMap() method above, then we have to update 
		 * the seats on object level which is cumbersome and more error prone.
		 * 
		 * This method will give you a map that contain enrollment error if error occur in enrollment and LearnerEnrollment 
		 * object if enrollment is successfull for provided learner.
		 */
		Map<String, Map<String, ?>> learnerCourseMap = doEnrollment(customer, learner, courseEntitlementMap, enrollmentStartDate, 
				enrollmentEndDate);
		
		return learnerCourseMap;
		
	} //end of processEnrollments()
	
	private List<LearnerEnrollment> getLearnerSuccessfullCoursesEnrolledList(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception {
		
		String errrorMessage = null;
		
		if (learner == null) {
			errrorMessage = "Learner is null";
			throwException(errrorMessage);
		}
		
		if (courseMap == null || courseMap.isEmpty()) {
			errrorMessage = "Course map is null or empty";
			throwException(errrorMessage);
		}
		
		Object successMap = courseMap.get("successfulCoursesMap");
		if (successMap == null) {
			return null;
		}
		
		Map<String, List<LearnerEnrollment>> courseSuccessMap = (Map<String, List<LearnerEnrollment>>)successMap;
		List<LearnerEnrollment> successfullCoursesEnrolledList = courseSuccessMap.get(learner.getVu360User().getUsername());
		
		if (successfullCoursesEnrolledList != null && !successfullCoursesEnrolledList.isEmpty()) {
			return successfullCoursesEnrolledList;
		}
		
		return null;
		
	} //end of getLearnerSuccessfullCoursesEnrolledList
	
	private Map<com.softech.vu360.lms.model.Course, String> getLearnerUnsuccessfullCoursesErrorMap(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception {
		
		String errrorMessage = null;
		
		if (learner == null) {
			errrorMessage = "Learner is null";
			throwException(errrorMessage);
		}
		
		if (courseMap == null || courseMap.isEmpty()) {
			errrorMessage = "Course map is null or empty";
			throwException(errrorMessage);
		}
		
		Object errorMap = courseMap.get("unSuccessfulCoursesMap");
		if (errorMap == null) {
			return null;
		}
		
		Map<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorMap = (Map<String, Map<com.softech.vu360.lms.model.Course, String>>)errorMap;
		for (Map.Entry<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorEntry : courseErrorMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			String userName = courseErrorEntry.getKey();
			if (userName.equals(learner.getVu360User().getUsername())) {
				Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = courseErrorEntry.getValue();
				return enrollErrorMap;	
			}	
		}
		
		return null;
		
	} //end of getLearnerUnsuccessfullCoursesErrorMap()
	
	private LearnerEnrolledCourses getLearnerEnrolledCourses(String userName, String errorCode, String errorMessage) {
		LearnerEnrolledCourses learnerEnrolledCoursesError = new LearnerEnrolledCourses();
		learnerEnrolledCoursesError.setUserId(userName);
		learnerEnrolledCoursesError.setErrorCode(errorCode);
		learnerEnrolledCoursesError.setErrorMessage(errorMessage);
		return learnerEnrolledCoursesError;
	}
	
	private boolean learnerCoursesValidation(LearnerEnrollCourses courses) throws Exception {
		
		String errorMessage = null;
		
		if (courses == null) {
			errorMessage = "Courses element is required";
			throwException(errorMessage);
		}
		
		XMLGregorianCalendar enrollmentStartDate = courses.getEnrollmentStartDate();
		XMLGregorianCalendar enrollmentEndDate = courses.getEnrollmentEndDate();
		List<String> courseGuidList = courses.getCourseId();
		
		if (enrollmentStartDate == null) {
			errorMessage = "enrolmentStartDate is mandotory ";
			throwException(errorMessage);
		}
		
		if (enrollmentEndDate == null) {
			errorMessage = "enrolmentEndDate is mandotory";
			throwException(errorMessage);
		}
		
		if (courseGuidList == null || courseGuidList.isEmpty()) {
			errorMessage = "Courses element is required and must not be empty";
			throwException(errorMessage);
		}
		
		return true;
	}
	
	private Learner getValidLearner(String userName, String customerCode) throws Exception {
		
		log.debug("getValidLearner() start");
		
		String errorMessage = null;
		
		if (StringUtils.isEmpty(userName) && StringUtils.isBlank(userName)) {
			errorMessage =  "UserId can not be empty or blank" ;
			throwException(errorMessage);
		}
				
		VU360User vu360User = vu360UserService.findUserByUserName(userName);
		if (vu360User == null) {
			errorMessage =  userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);
		}
				
		Learner vu360Learner = vu360User.getLearner();
		String learnerCustomerCode = vu360Learner.getCustomer().getCustomerCode();
				
		// Check whether customer has these learners or not
		if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
			errorMessage = "UserId: " + userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);			
		}	 
		
		log.debug("getValidLearner() end");
		
	    return vu360Learner;		
			
	}
	
	private com.softech.vu360.lms.model.Course getValidCourse(Customer customer, String courseGuid) throws Exception {
		
		log.debug("getValidCourse() start");
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();
		Date enrollmentStartDate = todayDate.getTime();
		
		if (StringUtils.isEmpty(courseGuid) && StringUtils.isBlank(courseGuid)) {
			errorMessage = "Course Id can not be empty";
		} 
		
		com.softech.vu360.lms.model.Course newCourse = courseCourseGrpService.getCourseByGUID(courseGuid);
		if (newCourse == null) {
			errorMessage = "No course found for " + courseGuid;
			throwException(errorMessage);
		}
			
		String error = courseLevelEnrollmentValidations(newCourse);
		if (error != null) {
			errorMessage = error;
		    throwException(errorMessage);
		}
			
		if (customer != null) {
			Object entitlementError = entitlementLevelEnrollmentValidations(customer, newCourse, enrollmentStartDate, null);
			if (entitlementError instanceof String) {
				String entError = (String)entitlementError;
				throwException(entError);	
			}
		}
		
		log.debug("getValidCourse() end");
		return newCourse;
	}
	
	@Override
	@PayloadRoot(localPart = BULK_ENROLLMENT_REQUEST, namespace = ENROLLMENT_TARGET_NAMESPACE)
	public BulkEnrollmentResponse bulkEnrollment(BulkEnrollmentRequest request) {
		
		log.debug("Bulk Enrollment Request start");
		final String REQUEST_END_MESSAGE = "Bulk Enrollment Request end";
		
		BulkEnrollmentResponse response = new BulkEnrollmentResponse();
		List<LearnerEnrolledCourses> learnerEnrolledCoursesResponseList = new ArrayList<LearnerEnrolledCourses>();
		boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			VU360User manager = null;
			if (notifyLearnersByEmail) {
				manager = getVU360UserByCustomerGUID(customer);
				if (manager == null) {
					String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customerCode ;
					response.setTransactionResult(TransactionResultType.FAILURE);
					response.setTransactionResultMessage(errorMessage);
					log.debug(REQUEST_END_MESSAGE);
					return response;
				}
			}
				
			Learners learners = request.getLearners();
			LearnerEnrollCourses courses = request.getCourses();
				
			if (learners == null) {
				String errorMessage = "Learners element is required and must not be empty";
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage(errorMessage);
				log.debug(REQUEST_END_MESSAGE);
				return response;
			}
				
			if (courses == null) {
				String errorMessage = "Courses element is required and must not be empty";
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage(errorMessage);
				log.debug(REQUEST_END_MESSAGE);
				return response;
			}
				
			List<com.softech.vu360.lms.model.Course> validCourseList = new ArrayList<com.softech.vu360.lms.model.Course>();
			List<EnrolledCourse> enrolledCourseErrorList = new ArrayList<EnrolledCourse>();
				
				/**
				 * In this request all the courses are same for every user. So we first make courses validation and
				 * make valid course list. If no valid course found then for every user this thing is correct. If we
				 * validate course for each learner then it's a performance issue because for each user same thing will
				 * run in each iteration.
				 */
			if (learnerCoursesValidation(courses)) {
				List<String> courseGuidList = courses.getCourseId();
				//refreshCoursesCache(courseGuidList);
				for (String courseGuid : courseGuidList) {
					try {
						com.softech.vu360.lms.model.Course validCourse = getValidCourse(customer, courseGuid);
						validCourseList.add(validCourse);
					} catch (Exception e) {
						String errorMessage = e.getMessage();
						EnrolledCourse enrolledCourseError = getEnrolledCourse(ERROR_CODE_ONE, errorMessage, courseGuid);
						enrolledCourseErrorList.add(enrolledCourseError);
					}	
				} //end of for (String courseGuid ..)			
			}
				
			if (!enrolledCourseErrorList.isEmpty()) {
				FailedCourses failedCourses = new FailedCourses();
				EnrolledCourses enrolledCoursesError = new EnrolledCourses();
				enrolledCoursesError.setCourse(enrolledCourseErrorList);
				failedCourses.setCourses(enrolledCoursesError);
				response.setFailedCourses(failedCourses);
			}
				
			if (validCourseList.isEmpty()) {
				String errorMessage = "No course found for enrollment";
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage(errorMessage);
				log.debug(REQUEST_END_MESSAGE);
				return response;
			}
				
			String startDate = courses.getEnrollmentStartDate().toString();
			String endDate = courses.getEnrollmentEndDate().toString();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			startDate = startDate + " 00:00:00.000";
			endDate = endDate + " 23:59:59.000";
			Date enrollmentStartDate = formatter.parse(startDate);
			Date enrollmentEndDate = formatter.parse(endDate);
				
			Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap = new HashMap<Learner, List<LearnerEnrollment>>();
			List<String> userIdList = learners.getUserId();
			for (String userName : userIdList) {
				try {
					EnrolledCourses enrolledCourses = new EnrolledCourses();
					List<EnrolledCourse> enrolledCourseList = new ArrayList<EnrolledCourse>();
						
					Learner validLearner = getValidLearner(userName, customerCode);
					Map<String, Map<String, ?>> learnerCourseMap = processEnrollments(customer, validLearner, validCourseList, enrollmentStartDate, enrollmentEndDate);
					List<LearnerEnrollment> learnerSuccessfullCoursesEnrolledList = getLearnerSuccessfullCoursesEnrolledList(validLearner, learnerCourseMap);
					Map<com.softech.vu360.lms.model.Course, String> learnerUnsuccessfullCoursesErrorMap = getLearnerUnsuccessfullCoursesErrorMap(validLearner, learnerCourseMap);
								
					if (learnerSuccessfullCoursesEnrolledList != null && !learnerSuccessfullCoursesEnrolledList.isEmpty()) {
						learnerEnrollmentEmailMap.put(validLearner, learnerSuccessfullCoursesEnrolledList);
						for (LearnerEnrollment learnerEnrollment : learnerSuccessfullCoursesEnrolledList) {
							String courseGuid = learnerEnrollment.getCourse().getCourseGUID();
							EnrolledCourse enrolledCourse = getEnrolledCourse(ERROR_CODE_ZERO, "", courseGuid);
							enrolledCourseList.add(enrolledCourse);
						}	
					} 
								
					if (learnerUnsuccessfullCoursesErrorMap != null && !learnerUnsuccessfullCoursesErrorMap.isEmpty()) {
						for (Map.Entry<com.softech.vu360.lms.model.Course, String> entry : learnerUnsuccessfullCoursesErrorMap.entrySet()) {
							//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							com.softech.vu360.lms.model.Course course = entry.getKey();
							String errorMessage = entry.getValue();
							String courseGuid = course.getCourseGUID();
							EnrolledCourse enrolledCourseError = getEnrolledCourse(ERROR_CODE_ONE, errorMessage, courseGuid);
							enrolledCourseList.add(enrolledCourseError);
						}
					}
								
					enrolledCourses.setCourse(enrolledCourseList);
					LearnerEnrolledCourses learnerEnrolledCourses = getLearnerEnrolledCourses(userName, ERROR_CODE_ZERO, "");
					learnerEnrolledCourses.setCourses(enrolledCourses);
					learnerEnrolledCoursesResponseList.add(learnerEnrolledCourses);			
						
				} catch(Exception e) {
					String errorMessage = e.getMessage();
					LearnerEnrolledCourses learnerEnrolledCoursesError = getLearnerEnrolledCourses(userName, ERROR_CODE_ONE, errorMessage);
					learnerEnrolledCoursesResponseList.add(learnerEnrolledCoursesError);
				}
					
			} //end of for()
				
			if (notifyLearnersByEmail && manager != null) {
				if (learnerEnrollmentEmailMap != null && !learnerEnrollmentEmailMap.isEmpty()) {
					Brander brander = getBrander(null, null);
					String loginURL = getLoginUrl();
					sendEmailToLearners(learnerEnrollmentEmailMap, loginURL, manager, brander);
				}
			}
				
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage("");
		response.setLearnerEnrolledCourses(learnerEnrolledCoursesResponseList);
		log.debug(REQUEST_END_MESSAGE);
		
		return response;
		
	} //end of bulkEnrollment()
	
	@Override
	@PayloadRoot(localPart = ASSIGN_SECURITY_ROLE_TO_LEARNERS_REQUEST, namespace = SECURITY_ROLES_TARGET_NAMESPACE)
	public AssignSecurityRoleToUsersResponse assignSecurityRoleToLearners(AssignSecurityRoleToUsersRequest request) {
		
		log.debug("Assign Security Role To Learners Request start");
		final String REQUEST_END_MESSAGE = "Assign Security Role To Learners Request end";
		AssignSecurityRoleToUsersResponse response = new AssignSecurityRoleToUsersResponse();
		ResponseUserSecurityRoles userSecurityRolesResponse = new ResponseUserSecurityRoles();
		//List<UserSecurityRoleResponse> responseSecurityRoleList = new ArrayList<UserSecurityRoleResponse>();
		UserSecurityRoles securityRoles = request.getUserSecurityRoles();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {	
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			Map<UserSecurityRole, Object> securityRolesMap = new HashMap<UserSecurityRole, Object>();
			//SecurityAndRolesServiceLmsApi securityAndRolesServiceLmsApi = new SecurityAndRolesServiceImplLmsApi();
			List<UserSecurityRole> userSecurityRoleList = securityRoles.getUserSecurityRole();
			for (UserSecurityRole userSecurityRole : userSecurityRoleList) {
				try {
					String securityRoleName = userSecurityRole.getSecurityRoleName();
					if (StringUtils.isEmpty(securityRoleName) || StringUtils.isBlank(securityRoleName)) {
						throwException("Security role name is mandotory.");
					}
					LMSRole lmsRole = vu360UserService.getRoleByName(securityRoleName, customer);
					securityAndRolesServiceLmsApi.securityRoleRequestValidation(userSecurityRole, customer, lmsRole);
					Map<Object, Object> securityRoleMap = securityAndRolesServiceLmsApi.getSecurityRoleMap(userSecurityRole, customer, lmsRole);
					securityRolesMap.put(userSecurityRole, securityRoleMap);
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					securityRolesMap.put(userSecurityRole, errorMessage);	
				}
			}
			
			Map<UserSecurityRole, Object> processedSeurityRoles = securityAndRolesServiceLmsApi.processSeurityRoles(securityRolesMap);
			List<ResponseUserSecurityRole> responseSecurityRoleList = securityAndRolesServiceLmsApi.getAssignSeurityRoleToUsersResponse(processedSeurityRoles);
			userSecurityRolesResponse.setUserSecurityRoleResponse(responseSecurityRoleList);
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage(SUCCESS);
        response.setUserSecurityRolesResponse(userSecurityRolesResponse);
		log.debug(REQUEST_END_MESSAGE);
		return response;
		
	}

	/*********** Organization Group***********/
	@PayloadRoot(localPart = ADD_ORG_GROUP_BY_HIERARCHY_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public AddOrgGroupByHierarchyResponse addOrgGroupByHierarchy(AddOrgGroupByHierarchyRequest request) {
		
		log.debug("AddOrgGroupByHierarchyRequest start");
		final String REQUEST_END_MESSAGE = "AddOrgGroupByHierarchyRequest end";
		AddOrgGroupByHierarchyResponse response = new AddOrgGroupByHierarchyResponse();
		String parentOrgGroupHierarchy = request.getParentOrgGroupHierarchy();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		OrgGroup orgGroup = request.getOrgGroup();
		List<String> orgGroupNames = orgGroup.getName();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			orgGroupServiceLmsApi.isValidParentOrgGroupHierarchy(customer, parentOrgGroupHierarchy);
			Map<String, Object> orgGroupMap = orgGroupServiceLmsApi.isValidOrgGroupNames(customer, orgGroupNames);
			Map<String, Object> processedOrgGroupMap= orgGroupServiceLmsApi.processOrgGroupMap(customer, parentOrgGroupHierarchy, orgGroupMap);
			NewOrganizationalGroups newOrganizationalGroups = orgGroupServiceLmsApi.processOrgGroupMapForResponse(parentOrgGroupHierarchy, processedOrgGroupMap);
			response.setNewOrganizationalGroups(newOrganizationalGroups);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		return response;
	}

	@PayloadRoot(localPart = ADD_ORG_GROUP_BY_PARENT_ID_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public AddOrgGroupByParentIdResponse addOrgGroupByParentId(AddOrgGroupByParentIdRequest request) {
		log.debug("AddOrgGroupByParentIdRequest start");
		final String REQUEST_END_MESSAGE = "AddOrgGroupByParentIdRequest end";
		AddOrgGroupByParentIdResponse response = new AddOrgGroupByParentIdResponse();
		
		AddOrgGroupByParentIdOrganizationalGroups organizationGroups = request.getOrganizationalGroups();
		List<AddOrgGroupByParentIdOrganizationalGroup>  organizationGroupsList = organizationGroups.getOrganizationalGroup();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			Map<String, Object> orgGroupMap = orgGroupServiceLmsApi.validateAddOrgGroupByParentIdRequest(customer, organizationGroupsList);
			Map<String, Object> processedOrgGroupMap= orgGroupServiceLmsApi.processOrgGroupMap(customer, orgGroupMap);
			NewOrganizationalGroups newOrganizationalGroups = orgGroupServiceLmsApi.processOrgGroupMapForResponse(processedOrgGroupMap);
			response.setNewOrganizationalGroups(newOrganizationalGroups);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = UPDATE_ORGANIZATION_GROUP_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public UpdateOrganizationGroupResponse updateOrganizationGroup(UpdateOrganizationGroupRequest request) {
		
		log.debug("UpdateOrganizationGroupRequest start");
		final String REQUEST_END_MESSAGE = "UpdateOrganizationGroupRequest end";
		UpdateOrganizationGroupResponse response = new UpdateOrganizationGroupResponse();
		
		UpdateOrganizationalGroup updateOrganizationalGroup = request.getOrganizationalGroup();
		BigInteger orgGroupId =  updateOrganizationalGroup.getId();
		String newOrgGroupName = updateOrganizationalGroup.getNewOrgGroupName();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.validateUpdateOrganizationRequest(customer, orgGroupId.longValue(), newOrgGroupName);
			OrganizationalGroup updatedOrganizationalGroup = orgGroupServiceLmsApi.updateOrganizationGroup(organizationalGroup, newOrgGroupName);
			ResponseOrganizationalGroup responseOrganizationalGroup = orgGroupServiceLmsApi.getUpdatedOrganizationGroupResponse(updatedOrganizationalGroup);
			response.setUpdatedOrganizationalGroup(responseOrganizationalGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = DELETE_ORGANIZATION_GROUP_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public DeleteOrganizationGroupResponse deleteOrganizationGroup(DeleteOrganizationGroupRequest request) {
		log.debug("DeleteOrganizationGroupRequest start");
		final String REQUEST_END_MESSAGE = "DeleteOrganizationGroupRequest end";
		DeleteOrganizationGroupResponse response = new DeleteOrganizationGroupResponse();
		
		DeleteOrganizationGroups organizationGroups =  request.getOrganizationalGroups();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		List<BigInteger> idsList = organizationGroups.getId();
		String orgGroupIdArray[] = new String[idsList.size()];
		for (int i=0; i<idsList.size(); i++) {
			orgGroupIdArray[i] = idsList.get(i).toString();
		}
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			Map<String, Object> deleteIdsMap = orgGroupServiceLmsApi.validateDeleteOrganizationGroupRequest(orgGroupIdArray, customer);
			Map<String, Object> processedDeleteIdsMap = orgGroupServiceLmsApi.processDeleteIdsMap(deleteIdsMap);
			Map<String, Object> responseMap = orgGroupServiceLmsApi.processDeleteIdsMapForResponse(processedDeleteIdsMap);
			
			DeletedOrganizationalGroup deletedOrganizationalGroup = (DeletedOrganizationalGroup)responseMap.get("deletedOrganizationalGroup");
			InvalidOrganizationalGroups invalidOrganizationalGroups = (InvalidOrganizationalGroups)responseMap.get("InvalidOrganizationalGroups");
			
			if (invalidOrganizationalGroups != null) {
				invalidOrganizationalGroups.setErrorCode("1");
			}
			response.setDeletedOrganizationalGroups(deletedOrganizationalGroup);
			response.setInvalidOrganizationalGroups(invalidOrganizationalGroups);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = GET_ORG_GROUP_BY_ID_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public GetOrgGroupByIdResponse getOrgGroupById(GetOrgGroupByIdRequest request) {
		
		log.debug("GetOrgGroupByIdRequest start");
		final String REQUEST_END_MESSAGE = "GetOrgGroupByIdRequest end";
		GetOrgGroupByIdResponse response = new GetOrgGroupByIdResponse();
		
		GetOrgGroupById orgGroup = request.getOrganizationalGroup();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		 BigInteger orgGroupId = orgGroup.getId();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.findOrganizationalGroupById(orgGroupId.longValue());
			ResponseOrganizationalGroup responseOrganizationalGroup = orgGroupServiceLmsApi.getOrgGroupByIdResponse(organizationalGroup);
			response.setOrganizationalGroup(responseOrganizationalGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = GET_ORG_GROUP_ID_BY_NAME_EVENT, namespace = ORG_GROUP_TARGET_NAMESPACE)
	@Override
	public GetOrgGroupIdByNameResponse getOrgGroupIdByName(GetOrgGroupIdByNameRequest request) {
		
		log.debug("GetOrgGroupIdByNameRequest start");
		final String REQUEST_END_MESSAGE = "GetOrgGroupIdByNameRequest end";
		GetOrgGroupIdByNameResponse response = new GetOrgGroupIdByNameResponse();
		
		GetOrgGroupIdByName orgGroup = request.getOrganizationalGroup();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		String parentOrgGroupHierarchy = orgGroup.getParentOrgGroupHierarchy();
		String orgGroupName = orgGroup.getName();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			orgGroupServiceLmsApi.validateGetOrgGroupIdByNameRequest(customer, parentOrgGroupHierarchy, orgGroupName);
			String orgGroupHierarchy = parentOrgGroupHierarchy + ">" + orgGroupName;
			OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
			if (organizationalGroup == null) {
				String errorMessage = "Invalid orgGroupHierarchy: " + orgGroupHierarchy;
				throwException(errorMessage);
			}
			ResponseOrganizationalGroup responseOrganizationalGroup = orgGroupServiceLmsApi.getOrgGroupIdByNameResponse(organizationalGroup);
			response.setOrganizationalGroup(responseOrganizationalGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	/******************** User Group *****************/
	
	@PayloadRoot(localPart = ADD_USER_GROUP_EVENT, namespace = USER_GROUP_TARGET_NAMESPACE)
	@Override
	public AddUserGroupResponse addUserGroup(AddUserGroupRequest request) {
		log.debug("AddUserGroupRequest start");
		final String REQUEST_END_MESSAGE = "AddUserGroupRequest end";
		AddUserGroupResponse response = new AddUserGroupResponse();
		String orgGroupHierarchy = request.getOrgGroupHierarchy();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		UserGroup userGroup = request.getUserGroup();
		List<String> userGroupNames = userGroup.getName();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			userGroupServiceLmsApi.isValidOrgGroupHierarchy(customer, orgGroupHierarchy);
			Map<String, Object> userGroupMap = userGroupServiceLmsApi.isValidUserGroupNames(customer, userGroupNames);
			Map<String, Object> processedUserGroupMap= userGroupServiceLmsApi.processUserGroupMap(customer, orgGroupHierarchy, userGroupMap);
			NewUserGroups newUserGroups = userGroupServiceLmsApi.processUserGroupMapForResponse(orgGroupHierarchy, processedUserGroupMap);
			response.setNewUserGroups(newUserGroups);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = UPDATE_USER_GROUP_EVENT, namespace = USER_GROUP_TARGET_NAMESPACE)
	@Override
	public UpdateUserGroupResponse updateUserGroup(UpdateUserGroupRequest request) {
		log.debug("UpdateUserGroupRequest start");
		final String REQUEST_END_MESSAGE = "UpdateUserGroupRequest end";
		UpdateUserGroupResponse response = new UpdateUserGroupResponse();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		UpdatedUserGroup updatedUserGroup = request.getUserGroup();
		BigInteger userGroupId =  updatedUserGroup.getId();
		String newUserGroupName = updatedUserGroup.getNewUserGroupName();
		BigInteger orgGroupGroupId = updatedUserGroup.getOrganizationGroupId();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			LearnerGroup learnerGroup = userGroupServiceLmsApi.validateUpdateUserGroupRequest(customer, userGroupId.longValue(), orgGroupGroupId.longValue(), newUserGroupName);
			LearnerGroup updatedLearnerGroup = userGroupServiceLmsApi.updateUserGroup(learnerGroup, newUserGroupName, orgGroupGroupId.longValue());
			ResponseUserGroup responseUserGroup = userGroupServiceLmsApi.getUpdatedOrganizationGroupResponse(updatedLearnerGroup);
			response.setUpdatedOrganizationalGroup(responseUserGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = DELETE_USER_GROUP_EVENT, namespace = USER_GROUP_TARGET_NAMESPACE)
	@Override
	public DeleteUserGroupResponse deleteUserGroup(DeleteUserGroupRequest request) {
		log.debug("DeleteUserGroupRequest start");
		final String REQUEST_END_MESSAGE = "DeleteUserGroupRequest end";
		DeleteUserGroupResponse response = new DeleteUserGroupResponse();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		DeleteUserGroups deleteUserGroups = request.getUserGroups();
		List<BigInteger> idsList = deleteUserGroups.getId();
		String userGroupIds[] = new String[idsList.size()];
		for (int i=0; i<idsList.size(); i++) {
			userGroupIds[i] = idsList.get(i).toString();
		}
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			userGroupServiceLmsApi.deleteUserGroups(userGroupIds);
			DeletedUserGroup deletedUserGroup = new DeletedUserGroup();
			deletedUserGroup.setId(idsList);
			deletedUserGroup.setErrorCode("0");
			deletedUserGroup.setErrorMessage("");
			response.setDeletedUserGroups(deletedUserGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = GET_USER_GROUP_BY_ID_EVENT, namespace = USER_GROUP_TARGET_NAMESPACE)
	@Override
	public GetUserGroupByIdResponse getUserGroupById(GetUserGroupByIdRequest request) {
		
		log.debug("GetUserGroupByIdRequest start");
		final String REQUEST_END_MESSAGE = "GetUserGroupByIdRequest end";
		GetUserGroupByIdResponse response = new GetUserGroupByIdResponse();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		GetUserGroupById userGroup = request.getUserGroup();
		BigInteger userGroupId = userGroup.getId();
		
		try {
			Customer customer = getApiCustomer(apiKey, customerCode);
			LearnerGroup userrGroup = userGroupServiceLmsApi.findUserGroupById(userGroupId.longValue());
			ResponseUserGroup responseUserGroup = userGroupServiceLmsApi.getUserGroupByIdResponse(userrGroup);
			response.setUserGroup(responseUserGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}

	@PayloadRoot(localPart = GET_USER_GROUP_ID_BY_NAME_EVENT, namespace = USER_GROUP_TARGET_NAMESPACE)
	@Override
	public GetUserGroupIdByNameResponse getUserGroupIdByName(GetUserGroupIdByNameRequest request) {
		
		log.debug("GetUserGroupIdByNameRequest start");
		final String REQUEST_END_MESSAGE = "GetUserGroupIdByNameRequest end";
		GetUserGroupIdByNameResponse response = new GetUserGroupIdByNameResponse();
		
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		GetUserGroupIdByName userGroupIdByName = request.getUserGroup();
		String userGroupName = userGroupIdByName.getName();
		String orgGroupHierarchy = userGroupIdByName.getParentOrgGroupHierarchy();
		
		String errorMessage = null;
		
		try {
		
			Customer customer = getApiCustomer(apiKey, customerCode);
			
			if (StringUtils.isEmpty(userGroupName)) {
				errorMessage = "UserGroupName can not be empty or blanck";
				throwException(errorMessage);
			}
			
			LearnerGroup userGroup = userGroupServiceLmsApi.findUserGroupByName(customer, userGroupName);
			if (userGroup == null){
				errorMessage = "Invalid UserGroup name";
				throwException(errorMessage);
			}
			
			ResponseUserGroup responseUserGroup = userGroupServiceLmsApi.getUserGroupIdByNameResponse(userGroup);
			response.setUserGroup(responseUserGroup);
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
		} catch (Exception e) {
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
			log.debug(REQUEST_END_MESSAGE);
			return response;
		}
		
		return response;
	}
	
	private Customer getApiCustomer(String apiKey, String customerCode) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = findLmsApiCustomerByKey(apiKey);
		customerApiValidation(lmsApiCustomer, customerCode);
		Customer customer = lmsApiCustomer.getCustomer();
		return customer;
		
	}
	
} //end of class LMSAPIWSImpl
