package com.softech.vu360.lms.web.restful.request.customer.contract.responsible;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.web.controller.model.AddCustomerContractForm;
import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.AddCustomerContractRequest;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.ContractDetail;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.CourseDetail;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.CourseGroupDetail;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.CourseGroups;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.Courses;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.LmsAddCustomerContractRequest;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.OrganizationGroupDetail;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.OrganizationGroupEnrollment;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.OrganizationGroupsEnrollments;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.AddCustomerContractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.ContractDetailResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseDetailResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroupDetailResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.InvalidCourseGroups;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.InvalidCourses;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.InvalidOrganizationGroupsEnrollments;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.LmsAddCustomerContractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationGroupDetailResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationalGroups;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;
import com.softech.vu360.util.FormUtil;

@Component(value="addCustomerContractRequestResponsible")
@Scope("request")
public class AddCustomerContractRequestResponsible extends AbstractRequestResponsible {
	
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	private static final String ERROR_MESSAGE_SEPERATOR = ">";
	
	private static final String CONTRACT_DETAIL_ERROR_MK = "contractDetailError";
	
	private static final String COURSE_GROUP_DETAIL_MAP_MK = "courseGroupDetailMap";
	private static final String COURSE_GROUP_DETAIL_ERROR_MK = "courseGroupDetailError";
	private static final String COURSE_GROUPS_ERROR_MK = "courseGroupsError";
	private static final String COURSE_GROUPS_MAP_MK = "courseGroupsMap";
	private static final String INVALID_COURSE_GROUP_GUID_LIST_MK = "invalidCourseGroupGuidList";
	private static final String INVALID_DISTRIBUTOR_COURSE_GROUP_GUID_LIST_MK = "invalidDistributorCourseGroupGuidList";
	private static final String DISTRIBUTOR_COURSE_GROUP_MAP_MK = "distributorCourseGroupMap";
	private static final String VALID_DISTRIBUTOR_COURSE_GROUP_LIST_MK = "validDistributorCourseGroupList";
	
	private static final String COURSE_DETAIL_MAP_MK = "courseDetailMap";
	private static final String COURSE_DETAIL_ERROR_MK = "courseDetailError";
	private static final String COURSES_ERROR_MK = "coursesError";
	private static final String COURSES_MAP_MK = "coursesMap";
	private static final String INVALID_DISTRIBUTOR_COURSE_GUID_LIST_MK = "invalidDistributorCourseGuidList";
	private static final String VALID_DISTRIBUTOR_COURSE_GROUP_AND_COURSE_IDs_LIST_MK = "validDistributorCourseGroupAndCourseIdsList";
	private static final String INVALID_COURSES_GUID_LIST_MK = "invalidCoursesGuidList";
	private static final String DISTRIBUTOR_COURSE_MAP_MK = "distributorCourseMap";
	
	private static final String ORG_GROUP_DETAIL_MAP_MK = "organizationGroupDetailMap";
	private static final String ORG_GROUP_DETAIL_ERROR_MK = "organizationGroupDetailError";
	private static final String ORG_GROUPS_ENROLLMENTS_ERROR_MK = "organizationGroupsEnrollmentsError";
	private static final String ORG_GROUP_ENROLLMENTS_MAP_MK = "organizationGroupEnrollmentsMap";
	private static final String ORG_GROUP_ENROLLMENT_ERROR_MAP_MK = "organizationGroupEnrollmentErrorMap";
	private static final String ORG_GROUP_ENROLLMENT_MAP_MK = "orgGroupEnrollmentMap";
	private static final String ORG_GROUP_ENROLLMENTS_EXCEED_MAX_ENROLLMENTS_ERROR_MK = "organizationGroupEnrollmentsExceedMaximumEnrollmentsError";
	
	/**
	 * Instead of the @Autowired, we are using @Resource(name="messageProvider") to achieve the same result. The @Resource
	 * is one of the annotations in the JSR-250 standard that defines a common set of Java annotations for use on both JSE and JEE
	 * platforms. Different from @Autowired, the @Resource annotation supports the name parameter for more fine-grained DI 
	 * requirements.
	 */
	@Resource(name="orgGroupServiceLmsApi")
	private OrgGroupServiceLmsApi orgGroupServiceLmsApi;
	
	@Resource(name="courseAndCourseGroupService")
	private CourseAndCourseGroupService courseAndCourseGroupService;
	
	@Resource(name="customerService")
	private CustomerService customerService;
	
	@Resource(name="entitlementService")
	private EntitlementService entitlementService;
	
	public void setOrgGroupServiceLmsApi(OrgGroupServiceLmsApi orgGroupServiceLmsApi) {
		this.orgGroupServiceLmsApi = orgGroupServiceLmsApi;
	}
	
	public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public AbstractResponse handleRequest(AbstractRequest request) throws Exception {
		
		LmsAddCustomerContractResponse response = null;
		
		if (request instanceof LmsAddCustomerContractRequest) {
			LmsAddCustomerContractRequest lmsAddCustomerContractRequest = (LmsAddCustomerContractRequest)request;
			AddCustomerContractRequest addCustomerContractRequest = lmsAddCustomerContractRequest.getAddCustomerContractRequest();
			response = addCustomerContract(addCustomerContractRequest);
		}
		
		return response;
	}
	
	public LmsAddCustomerContractResponse addCustomerContract(AddCustomerContractRequest addCustomerContractRequest) throws Exception {
		
		LmsAddCustomerContractResponse response = null;
	
		Map<String, Object> credentialsMap = validateAddCustomerContractRequestCredentials(addCustomerContractRequest);
		
		response = getErrorResponse(credentialsMap);
		if (response == null) {
			ContractDetail contractDetail = addCustomerContractRequest.getContractDetail();
			response = createNewCustomerContract(contractDetail, credentialsMap);
		}
		
		return response;
		
	}
	
	private Map<String, Object> validateAddCustomerContractRequestCredentials(AddCustomerContractRequest addCustomerContractRequest) throws Exception {
		
		Map<String, Object> credentialsMap = new LinkedHashMap<>();
		
		ContractDetail contractDetail = addCustomerContractRequest.getContractDetail();
		CourseGroupDetail courseGroupDetail = addCustomerContractRequest.getCourseGroupDetail();
		CourseDetail courseDetail = addCustomerContractRequest.getCourseDetail();
		OrganizationGroupDetail organizationGroupDetail = addCustomerContractRequest.getOrganizationGroupDetail();
		
		String customerGuid = contractDetail.getCustomerGuid();
		Customer customer = customerService.findCustomerByCustomerGUID(customerGuid);
		
		String contractDetailError = validateContractDetail(customer, contractDetail);
		credentialsMap.put(CONTRACT_DETAIL_ERROR_MK, contractDetailError);
		
		String contractType = contractDetail.getContractType();
		if (StringUtils.isNotBlank(contractType)) {
			if(contractType.equals(AddCustomerContractForm.COURSEGROUPTYPE)) {
				Map<String, Object> courseGroupDetailMap = validateCourseGroupDetail(courseGroupDetail);
				credentialsMap.put(COURSE_GROUP_DETAIL_MAP_MK, courseGroupDetailMap);
			} else if(contractType.equals(AddCustomerContractForm.COURSETYPE)) {
				Map<String, Object> courseDetailMap = validateCourseDetail(courseDetail);
				credentialsMap.put(COURSE_DETAIL_MAP_MK, courseDetailMap);
			}
		}
		
		if (customer != null) {
			Map<String, Object> organizationGroupDetailMap = validateOrganizationGroupDetail(customer, contractDetail, organizationGroupDetail);
			credentialsMap.put(ORG_GROUP_DETAIL_MAP_MK, organizationGroupDetailMap);
		}
		
		return credentialsMap;
	}
	
	private String validateContractDetail(Customer customer, ContractDetail contractDetail) throws Exception {
		
		StringBuilder contractDetailError = new StringBuilder();
		
		String contractEndType = contractDetail.getContractEndType();
		Boolean allowUnlimitedEnrollments = contractDetail.getAllowUnlimitedEnrollments();
		String customerGuid = contractDetail.getCustomerGuid();
		String contractName = contractDetail.getContractName();
		Integer maximumEnrollments = contractDetail.getMaximumEnrollments();
		Boolean allowSelfEnrollment = contractDetail.getAllowSelfEnrollment();
		String contractType = contractDetail.getContractType();
		Date contractStartDate = contractDetail.getStartDate();
		Integer termOfServices = contractDetail.getTermOfServices();
		Date contractEndDate = contractDetail.getEndDate();
	
		if (customer == null) {
			String customerError = "Invalid CustomerGuid. No Customer found for guid: ";
			contractDetailError.append(customerError + customerGuid + ERROR_MESSAGE_SEPERATOR);
		} else {
			List<CustomerEntitlement> entitlements = entitlementService.getActiveCustomerEntitlementsForCustomer(customer);
			for(CustomerEntitlement ent : entitlements) {
				if(ent.getName().equalsIgnoreCase(contractName)) {
					String contractNameError = "A Contract with similar name (%s) already exists. Please choose another name";
					contractDetailError.append(String.format(contractNameError, contractName) + ERROR_MESSAGE_SEPERATOR);
					break;
				}
			}
		}
	
		Date expirationDate= null;
	
		if (contractStartDate == null) {
			String contractStartDateError = "StartDate can not be empty or blank";
			contractDetailError.append(contractStartDateError + ERROR_MESSAGE_SEPERATOR);
		}
		
		if (StringUtils.isBlank(contractEndType)) {
			String contractEndTypeMandotoryError = "contractEndType is mandotory and should be either \"NumberOfDays\" or \"EndDate\")";
			contractDetailError.append(contractEndTypeMandotoryError + ERROR_MESSAGE_SEPERATOR);
		} else if (contractEndType.equalsIgnoreCase(ContractEndType.TERM_OF_SERVICES.toString())) {
			String termOfServicesEmptyError = "If contractEndType is NumberOfDays then TermOfServices can not be empty or blank";
			String termOfServicesLessThanZeroError = termOfServicesEmptyError + " and must be greater than or equal to zero";
			if (termOfServices == null) {
				contractDetailError.append(termOfServicesEmptyError + ERROR_MESSAGE_SEPERATOR);
			} else if (termOfServices < 0) {
				contractDetailError.append(termOfServicesLessThanZeroError + ERROR_MESSAGE_SEPERATOR);
			} else {
				if (contractStartDate != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(contractStartDate);
					cal.add(Calendar.DATE, termOfServices);
					expirationDate = cal.getTime();
				}
			}
		} else if (contractEndType.equalsIgnoreCase(ContractEndType.END_DATE.toString())) {
			if (contractEndDate == null) {
				contractDetailError.append("If contractEndType is EndDate then EndDate can not be empty or blank" + ERROR_MESSAGE_SEPERATOR);
			} else {
				expirationDate = contractEndDate;
				if (contractStartDate != null) {
					if( contractStartDate.equals(contractEndDate) || contractStartDate.after(contractEndDate)) {
						contractDetailError.append("End Date should strictly be after Start Date" + ERROR_MESSAGE_SEPERATOR);
					}
				} 
			}
		} 
		
		if (customer != null && expirationDate != null) {
			Distributor distributor = customer.getDistributor();
			Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(distributor);
			
			if (expirationDate.after(maxEndDate)) {
				contractDetailError.append("Customer entitlement end date should not exceed max distributor entitlement end date." + ERROR_MESSAGE_SEPERATOR);
			}
		}
		
		if (StringUtils.isBlank(contractName)) {
			contractDetailError.append("ContractName can not be empty or blank" + ERROR_MESSAGE_SEPERATOR);
		}
		
		
		if (!allowUnlimitedEnrollments) {
			if (maximumEnrollments == null) {
				contractDetailError.append("If allowUnlimitedEnrollments is false then MaximumEnrollments is mandotory. MaximumEnrollments can be any non negative integer (1,2,....)" + ERROR_MESSAGE_SEPERATOR);
			} 
		}
		
		if (StringUtils.isBlank(contractType)) {
			contractDetailError.append("contractType is mandotory and should be either \"CourseGroup\" or \"Course\")" + ERROR_MESSAGE_SEPERATOR);
		} else if ((!(contractType.equalsIgnoreCase("CourseGroup"))) && (!(contractType.equalsIgnoreCase("Course")))) {
			contractDetailError.append("contractType is mandotory and should be either \"CourseGroup\" or \"Course\")" + ERROR_MESSAGE_SEPERATOR);
		}
		
		if (allowSelfEnrollment == null) {
			contractDetailError.append("AllowSelfEnrollment is mandotory and should be either \"true\" or \"false\")" + ERROR_MESSAGE_SEPERATOR);
		}
		
		if (contractDetailError.length() > 0) {
			contractDetailError.deleteCharAt(contractDetailError.length() - 1); 
		} else {
			return null;
		}
		
		return contractDetailError.toString();
		
	}
	
	private Map<String, Object> validateCourseGroupDetail(CourseGroupDetail courseGroupDetail) throws Exception {
		
		Map<String, Object> courseGroupDetailMap = null;
		
		if (courseGroupDetail != null) {
			courseGroupDetailMap = new HashMap<>();
			CourseGroups courseGroups = courseGroupDetail.getCourseGroups();
			
			if (courseGroups == null) {
				String errorMessage = "CourseGroups is necessary under CourseGroupDetail. Either remove CourseGroupDetail or provide necessary details for CourseGroupDetail.";
				courseGroupDetailMap.put(COURSE_GROUP_DETAIL_ERROR_MK, errorMessage);
			} else {
				
				List<String> courseGroupsGuidList = courseGroups.getGuid();
				if (courseGroupsGuidList.isEmpty()) {
					String errorMessage = "Atleat one guid should be provided under CourseGroups";
					courseGroupDetailMap.put(COURSE_GROUPS_ERROR_MK, errorMessage);
				} else {
					Map<String, Object> courseGroupsMap = validateCourseGroups(courseGroups);
					courseGroupDetailMap.put(COURSE_GROUPS_MAP_MK, courseGroupsMap);
				}
			}
		}
		
		return courseGroupDetailMap;
	}
	
	private Map<String, Object> validateCourseGroups(CourseGroups courseGroups) throws Exception {
		
		Map<String, Object> courseGroupMap = new LinkedHashMap<>();
		List<String> invalidCourseGroupGuidList = new ArrayList<>();
		List<CourseGroup> dbCourseGroupsList = new ArrayList<>();
		
		List<String> courseGroupsGuidList = courseGroups.getGuid();
		for (String courseGroupGuid : courseGroupsGuidList) {
			CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupByguid(courseGroupGuid);
			if (courseGroup == null) {
				invalidCourseGroupGuidList.add(courseGroupGuid);
			} else {
				dbCourseGroupsList.add(courseGroup);
			}
		}
		
		Map<String, List<?>> distributorCourseGroupMap = findDistributorCourseGroups(dbCourseGroupsList);
		
		if (!CollectionUtils.isEmpty(invalidCourseGroupGuidList)) {
			courseGroupMap.put(INVALID_COURSE_GROUP_GUID_LIST_MK, invalidCourseGroupGuidList);
		}
		
		if (!CollectionUtils.isEmpty(distributorCourseGroupMap)) {
			courseGroupMap.put(DISTRIBUTOR_COURSE_GROUP_MAP_MK, distributorCourseGroupMap);
		}
		
		return courseGroupMap;
		
	}
	
	private Map<String, List<?>> findDistributorCourseGroups(List<CourseGroup> courseGroupsList) {
		
		List<Long> courseGroupIdList = new ArrayList<>();
		Map<String, List<?>> distributorCourseGroupMap = new LinkedHashMap<>();
		List<CourseGroup> validDistributorCourseGroupList = new ArrayList<>();
		List<String> invalidDistributorCourseGroupGuidList = new ArrayList<>();
		
		for (CourseGroup courseGroup : courseGroupsList) {
			courseGroupIdList.add(courseGroup.getId());
		}
		
		Distributor distributor = lmsApiDistributor.getDistributor();
		HashMap<CourseGroup, List<Course>> resultMap = entitlementService.findAvailableCourseGroups(distributor, courseGroupIdList);
		
		boolean courseGroupGuidMatched = false;
		
		for (CourseGroup courseGroup : courseGroupsList) {
			String courseGroupGuid = courseGroup.getGuid();
			for (Map.Entry<CourseGroup, List<Course>> entry : resultMap.entrySet()) {
				CourseGroup distributorCourseGroup = entry.getKey();
				String distributorCourseGroupGuid = distributorCourseGroup.getGuid();
				if (distributorCourseGroupGuid.equals(courseGroupGuid)) {
					validDistributorCourseGroupList.add(distributorCourseGroup);
					courseGroupGuidMatched = true;
					break;
				} 
			}
			
			if (!courseGroupGuidMatched) {
				invalidDistributorCourseGroupGuidList.add(courseGroupGuid);
			} else {
				courseGroupGuidMatched = false;
			}
		}
		
		if (!CollectionUtils.isEmpty(invalidDistributorCourseGroupGuidList)) {
			distributorCourseGroupMap.put(INVALID_DISTRIBUTOR_COURSE_GROUP_GUID_LIST_MK, invalidDistributorCourseGroupGuidList);
		}
		
		if (!CollectionUtils.isEmpty(validDistributorCourseGroupList)) {
			distributorCourseGroupMap.put(VALID_DISTRIBUTOR_COURSE_GROUP_LIST_MK, validDistributorCourseGroupList);
		}
		
		return distributorCourseGroupMap;
		
	}
	
	private Map<String, Object> validateCourseDetail(CourseDetail courseDetail) throws Exception {
		
		Map<String, Object> courseDetailMap = null;
		
		if (courseDetail != null) {
			courseDetailMap = new HashMap<>();
			Courses courses = courseDetail.getCourses();
			
			if (courses == null) {
				String errorMessage = "Courses is necessary under CourseDetail. Either remove CourseDetail or provide necessary details for CourseDetail.";
				courseDetailMap.put(COURSE_DETAIL_ERROR_MK, errorMessage);
			} else {
				
				List<String> coursesGuidList = courses.getGuid();
				if (coursesGuidList.isEmpty()) {
					String errorMessage = "Atleat one guid should be provided under Courses";
					courseDetailMap.put(COURSES_ERROR_MK, errorMessage);
				} else {
					Map<String, Object> coursesMap = validateCourses(courses);
					courseDetailMap.put(COURSES_MAP_MK, coursesMap);
				}
			}
		}
		
		return courseDetailMap;
	}
	
	private Map<String, Object> validateCourses(Courses courses) throws Exception {
		
		Map<String, Object> coursesMap = new LinkedHashMap<>();
		List<String> invalidCoursesGuidList = new ArrayList<>();
		List<Course> dbCoursesList = new ArrayList<>();
	
		List<String> coursesGuidList = courses.getGuid();
		for (String courseGuid : coursesGuidList) {
			Course course = courseAndCourseGroupService.getCourseByGUID(courseGuid);
			if (course == null) {
				invalidCoursesGuidList.add(courseGuid);
			} else {
				dbCoursesList.add(course);
			}
		}
		
		Map<String, Object> distributorCourseMap = findDistributorCourses(dbCoursesList);
		
		if (!CollectionUtils.isEmpty(invalidCoursesGuidList)) {
			coursesMap.put(INVALID_COURSES_GUID_LIST_MK, invalidCoursesGuidList);
		}
		
		if (!CollectionUtils.isEmpty(distributorCourseMap)) {
			coursesMap.put(DISTRIBUTOR_COURSE_MAP_MK, distributorCourseMap);
		}
		
		return coursesMap;
		
	}
	
	private Map<String, Object> findDistributorCourses(List<Course> coursesList) {
		
		List<Long> coursesIdList = new ArrayList<>();
		Map<String, Object> distributorCourseMap = new LinkedHashMap<>();
		List<String> invalidDistributorCourseGuidList = new ArrayList<>();
		List<String> validDistributorCourseGroupAndCourseIdsList = new ArrayList<>();
		
		for (Course course : coursesList) {
			coursesIdList.add(course.getId());
		}
		
		Distributor distributor = lmsApiDistributor.getDistributor();
		HashMap<CourseGroup, List<Course>> resultMap = entitlementService.findAvailableCourses(distributor, coursesIdList);
		
		boolean courseIdMatched = false;
		
		for (Course course : coursesList) {
			Long courseId = course.getId();
			String courseGuid = course.getCourseGUID();
			
			for (Map.Entry<CourseGroup, List<Course>> entry : resultMap.entrySet()) {
				CourseGroup courseGroup = entry.getKey();
				List<Course> distributorCoursesList = entry.getValue();
				Long courseGroupId = courseGroup.getId();
				for (Course distributorCourse : distributorCoursesList) {
					Long distributorCourseId = distributorCourse.getId();
					//String distributorCourseGuid = distributorCourse.getCourseGUID();;
					if (distributorCourseId.equals(courseId)) {
						validDistributorCourseGroupAndCourseIdsList.add(courseGroupId + ":" + distributorCourseId);
						courseIdMatched = true;
						break;
					}		
				}				
				
			} //end of for (Map.Entry<CourseGroup
			
			if (!courseIdMatched) {
				invalidDistributorCourseGuidList.add(courseGuid);
			} else {
				courseIdMatched = false;
			}
			
		} // end of for (Course course
		
		if (!CollectionUtils.isEmpty(invalidDistributorCourseGuidList)) {
			distributorCourseMap.put(INVALID_DISTRIBUTOR_COURSE_GUID_LIST_MK, invalidDistributorCourseGuidList);
		}
		
		if (!CollectionUtils.isEmpty(validDistributorCourseGroupAndCourseIdsList)) {
			distributorCourseMap.put(VALID_DISTRIBUTOR_COURSE_GROUP_AND_COURSE_IDs_LIST_MK, validDistributorCourseGroupAndCourseIdsList);
		}
		
		return distributorCourseMap;
		
	}
	
	private Map<String, Object> validateOrganizationGroupDetail(Customer customer, ContractDetail contractDetail, OrganizationGroupDetail organizationGroupDetail) throws Exception {
		
		Map<String, Object> organizationGroupDetailMap = null;
		
		if (organizationGroupDetail != null) {
			organizationGroupDetailMap = new HashMap<>();
			OrganizationGroupsEnrollments organizationGroupEnrollments = organizationGroupDetail.getOrganizationGroupsEnrollments();
			if (organizationGroupEnrollments == null) {
				String errorMessage = "OrganizationGroupsEnrollments is necessary under OrganizationGroupDetail. Either remove OrganizationGroupDetail or provide necessary details for OrganizationGroupDetail.";
				organizationGroupDetailMap.put(ORG_GROUP_DETAIL_ERROR_MK, errorMessage);
			} else {
				List<OrganizationGroupEnrollment> organizationGroupEnrollmentList = organizationGroupEnrollments.getOrganizationGroupEnrollment();
				if (organizationGroupEnrollmentList.isEmpty()) {
					String errorMessage = "Atleat one OrganizationGroupEnrollment should be provided under OrganizationGroupsEnrollments";
					organizationGroupDetailMap.put(ORG_GROUPS_ENROLLMENTS_ERROR_MK, errorMessage);
				} else {
					Map<String, Object> organizationGroupEnrollmentsMap = validateOrganizationGroupEnrollments(customer, contractDetail, organizationGroupEnrollments);
					organizationGroupDetailMap.put(ORG_GROUP_ENROLLMENTS_MAP_MK, organizationGroupEnrollmentsMap);
				}	
			}
		}
		
		return organizationGroupDetailMap;
	}
	
	private Map<String, Object> validateOrganizationGroupEnrollments(Customer customer, ContractDetail contractDetail, OrganizationGroupsEnrollments organizationGroupEnrollments) throws Exception {
		
		Map<String, OrganizationalGroup> allOrganizationalGroups = orgGroupServiceLmsApi.getAllOrganizationalGroups(customer);
		OrganizationalGroup rootOrganizationalGroup = orgGroupServiceLmsApi.getRootOrganizationalGroup(customer);
		String rootOrganizationalGroupName = rootOrganizationalGroup.getName();
		Map<OrganizationGroupEnrollment, String> organizationGroupEnrollmentErrorMap = new LinkedHashMap<>();
		Map<String, Object> organizationGroupEnrollmentMap = new HashMap<>();
		StringBuilder organizationGroupEnrollmentError = new StringBuilder();
		List<OrganizationGroupEnrollment> organizationGroupEnrollmentList = organizationGroupEnrollments.getOrganizationGroupEnrollment();
		Map<OrganizationalGroup, Integer> orgGroupEnrollmentMap = new LinkedHashMap<>();
		Long totalOrganizationGroupEnrollments = 0L;
		
		for (Object childNode : organizationGroupEnrollmentList) {
			
			JSONObject fromObject = JSONObject.fromObject(childNode);
			OrganizationGroupEnrollment organizationGroupEnrollment = (OrganizationGroupEnrollment) JSONObject.toBean(fromObject, OrganizationGroupEnrollment.class);
			organizationGroupEnrollmentError.setLength(0);
			String orgGroupHierarchy = organizationGroupEnrollment.getOrgGroupHierarchy();
			Integer orgGroupMaximumEnrollments = organizationGroupEnrollment.getMaximumEnrollments();
			
			if (StringUtils.isEmpty(orgGroupHierarchy)) {
				organizationGroupEnrollmentError.append("OrgGroupHierarchy can not be empty or blank" +  ERROR_MESSAGE_SEPERATOR);
			}
			
			if (orgGroupMaximumEnrollments < 1) {
				organizationGroupEnrollmentError.append("Atleast 1 or more than one enrollments shuold be given for OrgGroupHierarchy: " + orgGroupHierarchy  +  " " + ERROR_MESSAGE_SEPERATOR);
			} else {
				totalOrganizationGroupEnrollments += orgGroupMaximumEnrollments;
			}
			
			if (organizationGroupEnrollmentError.length() > 0) {
				organizationGroupEnrollmentError.deleteCharAt(organizationGroupEnrollmentError.length() - 1);
				organizationGroupEnrollmentErrorMap.put(organizationGroupEnrollment, organizationGroupEnrollmentError.toString());
			} else {
				String rootGroupName = orgGroupServiceLmsApi.getRootOrgGroupName(orgGroupHierarchy);
				if(!rootOrganizationalGroupName.equalsIgnoreCase(rootGroupName)) {
					organizationGroupEnrollmentError.append("Invalid orgGroupHierarchy: " + orgGroupHierarchy + ". Root organization group must exist and must be the first in the hierarchy. Correct root organization group is: " + rootOrganizationalGroupName);
					organizationGroupEnrollmentErrorMap.put(organizationGroupEnrollment, organizationGroupEnrollmentError.toString());
				} else {
				    OrganizationalGroup existingOrganizationalGroup = allOrganizationalGroups.get(orgGroupHierarchy.trim().toUpperCase());
					if (existingOrganizationalGroup == null) {
						organizationGroupEnrollmentError.append("Invalid orgGroupHierarchy. No organization group found for " + orgGroupHierarchy);
						organizationGroupEnrollmentErrorMap.put(organizationGroupEnrollment, organizationGroupEnrollmentError.toString());
					} else {
						orgGroupEnrollmentMap.put(existingOrganizationalGroup, orgGroupMaximumEnrollments);
					}
				}	
			} 
		}
		
		Boolean allowUnlimitedEnrollments = contractDetail.getAllowUnlimitedEnrollments();
		Integer maximumEnrollments = contractDetail.getMaximumEnrollments();
		
		if (!allowUnlimitedEnrollments && maximumEnrollments != null ) {
			if (totalOrganizationGroupEnrollments > Long.valueOf(maximumEnrollments)) {
				String errorMessage = "Maximum org group enrollment should not be greater than MaximumEnrollments";
				organizationGroupEnrollmentMap.put(ORG_GROUP_ENROLLMENTS_EXCEED_MAX_ENROLLMENTS_ERROR_MK, errorMessage);
			}
		}
		
		if (!CollectionUtils.isEmpty(organizationGroupEnrollmentErrorMap)) {
			organizationGroupEnrollmentMap.put(ORG_GROUP_ENROLLMENT_ERROR_MAP_MK, organizationGroupEnrollmentErrorMap);
		}
		
		if (!CollectionUtils.isEmpty(orgGroupEnrollmentMap)) {
			organizationGroupEnrollmentMap.put(ORG_GROUP_ENROLLMENT_MAP_MK, orgGroupEnrollmentMap);
		}
		
		return organizationGroupEnrollmentMap;
		
	}
	
	private LmsAddCustomerContractResponse createNewCustomerContract(ContractDetail contractDetail, Map<String, Object> credentialsMap) {
		
		LmsAddCustomerContractResponse response = new LmsAddCustomerContractResponse();
		AddCustomerContractResponse addCustomerContractResponse = new AddCustomerContractResponse();
		
		Map<String, Object> courseGroupDetailMap = getCourseGroupDetailMap(credentialsMap);
		Map<String, Object> courseDetailMap = getCourseDetailMap(credentialsMap);
		Map<String, Object> organizationGroupDetailMap = getOrganizationGroupDetailMap(credentialsMap);
		
		String customerGuid = contractDetail.getCustomerGuid();
		Customer customer = customerService.findCustomerByCustomerGUID(customerGuid);
		
		CustomerEntitlement newCustomerEntitlement = getNewCustomerEntitlement(contractDetail, customer);
		
		List<OrgGroupEntitlement> newOrgGroupEntitlementList = getOrgGroupEntitlementList(organizationGroupDetailMap, customer, newCustomerEntitlement, contractDetail);
		OrganizationGroupDetailResponse organizationGroupDetailResponse = getOrganizationGroupDetailResponse(newOrgGroupEntitlementList);
		addCustomerContractResponse.setOrganizationGroupDetailResponse(organizationGroupDetailResponse);
		
		CustomerEntitlement newDbCustomerEntitlement = entitlementService.saveCustomerEntitlement(newCustomerEntitlement, newOrgGroupEntitlementList);
		
		List<CourseGroup> validDistributorCourseGroupList = getValidDistributorCourseGroupList(courseGroupDetailMap);
		List<CourseGroup> contractCourseGroupsList = addCourseGroupsInContract(customer, newDbCustomerEntitlement, validDistributorCourseGroupList);
		CourseGroupDetailResponse courseGroupDetailResponse = getCourseGroupDetailResponse(courseGroupDetailMap, contractCourseGroupsList);
		addCustomerContractResponse.setCourseGroupDetailResponse(courseGroupDetailResponse);
		
		List<String> validDistributorCourseGroupAndCourseIdsList = getValidDistributorCourseGroupAndCourseIdsList(courseDetailMap);
		List<Course> contractCoursesList = addCoursesInContract(customer, newDbCustomerEntitlement, validDistributorCourseGroupAndCourseIdsList);
		CourseDetailResponse courseDetailResponse = getCourseDetailResponse(courseDetailMap, contractCoursesList);
		addCustomerContractResponse.setCourseDetailResponse(courseDetailResponse);
		
		ContractDetailResponse contractDetailResponse = getContractDetailResponse(newDbCustomerEntitlement);
		addCustomerContractResponse.setContractDetailResponse(contractDetailResponse);
		
		response.setAddCustomerContractResponse(addCustomerContractResponse);
		return response;
		
	}
	
	private CustomerEntitlement getNewCustomerEntitlement(ContractDetail contractDetail, Customer customer) {
		
		String contractEndType = contractDetail.getContractEndType();
		Boolean allowUnlimitedEnrollments = contractDetail.getAllowUnlimitedEnrollments();
		String contractName = contractDetail.getContractName();
		Integer maximumEnrollments = contractDetail.getMaximumEnrollments();
		Boolean allowSelfEnrollment = contractDetail.getAllowSelfEnrollment();
		String contractType = contractDetail.getContractType();
		Date contractStartDate = contractDetail.getStartDate();
		Integer termOfServices = contractDetail.getTermOfServices();
		Date contractEndDate = contractDetail.getEndDate();
		BigDecimal transactionAmount = contractDetail.getTransactionAmount();
		if (transactionAmount == null) {
			transactionAmount = new BigDecimal(0.0);
		}
		
		Double tAmt = transactionAmount.doubleValue();
		
		CustomerEntitlement newCustomerEntitlement = getNewCustomerEntitlement(customer, contractName, contractType, contractEndType, contractStartDate, contractEndDate, allowSelfEnrollment, termOfServices, allowUnlimitedEnrollments, maximumEnrollments, tAmt);
		return newCustomerEntitlement;
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getOrganizationGroupEnrollmentsMap(Map<String, Object> organizationGroupDetailMap) {
		
		Map<String, Object> organizationGroupEnrollmentsMap = null;
		if (!CollectionUtils.isEmpty(organizationGroupDetailMap)) {
			organizationGroupEnrollmentsMap = (Map<String, Object>)organizationGroupDetailMap.get(ORG_GROUP_ENROLLMENTS_MAP_MK);
		}
		
		return organizationGroupEnrollmentsMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<OrganizationalGroup, Integer> getOrgGroupEnrollmentMap(Map<String, Object> organizationGroupDetailMap) {
		
		Map<OrganizationalGroup, Integer> orgGroupEnrollmentMap = null;
		Map<String, Object> organizationGroupEnrollmentsMap = getOrganizationGroupEnrollmentsMap(organizationGroupDetailMap);
		if (!CollectionUtils.isEmpty(organizationGroupEnrollmentsMap)) {
			orgGroupEnrollmentMap = (Map<OrganizationalGroup, Integer>)organizationGroupEnrollmentsMap.get(ORG_GROUP_ENROLLMENT_MAP_MK);
		}
		return orgGroupEnrollmentMap;
	}
	
	private List<OrgGroupEntitlement> getOrgGroupEntitlementList(Map<String, Object> organizationGroupDetailMap, 
			Customer customer, CustomerEntitlement customerEntitlement, ContractDetail contractDetail) {
		
		List<OrgGroupEntitlement> newOrgGroupEntitlementList = null;
		
		Map<OrganizationalGroup, Integer> orgGroupEnrollmentMap = getOrgGroupEnrollmentMap(organizationGroupDetailMap);
		if (!CollectionUtils.isEmpty(orgGroupEnrollmentMap)) {
			newOrgGroupEntitlementList = new ArrayList<OrgGroupEntitlement>();
			String contractEndType = contractDetail.getContractEndType();
			Date contractStartDate = contractDetail.getStartDate();
			Date contractEndDate = contractDetail.getEndDate();
			Boolean allowSelfEnrollment = contractDetail.getAllowSelfEnrollment();
			Integer termOfServices = contractDetail.getTermOfServices();
			for (Map.Entry<OrganizationalGroup, Integer> entry : orgGroupEnrollmentMap.entrySet()) {
				OrganizationalGroup orgGroup = entry.getKey();
				Integer orgGroupMaximumEnrollments = entry.getValue();
				OrgGroupEntitlement newOrgGroupEntitlement = getNewOrgGroupEntitlement(orgGroup, 
						orgGroupMaximumEnrollments, customerEntitlement, contractEndType, termOfServices, 
						contractEndDate, allowSelfEnrollment, contractStartDate); 
				newOrgGroupEntitlementList.add(newOrgGroupEntitlement);
						
			}
		}
			
		return newOrgGroupEntitlementList;
	}
	
	private CourseGroupDetailResponse getCourseGroupDetailResponse(Map<String, Object> courseGroupDetailMap, 
			List<CourseGroup> contractCourseGroupsList) {
		
		CourseGroupDetailResponse courseGroupDetailResponse = null;
		if (!CollectionUtils.isEmpty(contractCourseGroupsList)) {
			courseGroupDetailResponse = new CourseGroupDetailResponse();
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups responseCourseGroups = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups();
			List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroup> responseCourseGroupList = getCourseGroupDetailResponseList(contractCourseGroupsList);
			responseCourseGroups.setCourseGroup(responseCourseGroupList);
			List<String> invalidGuidsList = getAllInvalidCourseGroupGuidList(courseGroupDetailMap);
			if (!CollectionUtils.isEmpty(invalidGuidsList)) {
				InvalidCourseGroups invalidCourseGroups = getInvalidCourseGroups(invalidGuidsList);
				responseCourseGroups.setInvalidCourseGroups(invalidCourseGroups);
			}
			courseGroupDetailResponse.setCourseGroups(responseCourseGroups);
			courseGroupDetailResponse.setErrorCode(ERROR_CODE_ZERO);
			courseGroupDetailResponse.setErrorMessage("");
		}
		return courseGroupDetailResponse;
	}
	
	private CourseDetailResponse getCourseDetailResponse(Map<String, Object> courseDetailMap, 
			List<Course> contractCoursesList) {
		
		CourseDetailResponse courseDetailResponse = null;
		if (!CollectionUtils.isEmpty(contractCoursesList)) {
		    courseDetailResponse = new CourseDetailResponse();
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses responseCourses = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses();
			List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Course> responseCourseList = getCourseDetailResponseList(contractCoursesList);
			responseCourses.setCourse(responseCourseList);
			List<String> invalidGuidsList = getAllInvalidCourseGuidList(courseDetailMap);
			if (!CollectionUtils.isEmpty(invalidGuidsList)) {
				InvalidCourses invalidCourses = getInvalidCourses(invalidGuidsList);
				responseCourses.setInvalidCourses(invalidCourses);
			}
			courseDetailResponse.setCourses(responseCourses);
			courseDetailResponse.setErrorCode(ERROR_CODE_ZERO);
			courseDetailResponse.setErrorMessage("");
		}
		return courseDetailResponse;
	}
	
	private ContractDetailResponse getContractDetailResponse(CustomerEntitlement customerEntitlement) {
		
		ContractDetailResponse contractDetailResponse = new ContractDetailResponse();
		contractDetailResponse.setContractName(customerEntitlement.getName());
		contractDetailResponse.setMaximumEnrollments(customerEntitlement.getMaxNumberSeats());
		contractDetailResponse.setAllowSelfEnrollment(customerEntitlement.isAllowSelfEnrollment());
		contractDetailResponse.setContractType(customerEntitlement.getEnrollmentType());
		contractDetailResponse.setStartDate(customerEntitlement.getStartDate());
		contractDetailResponse.setTermOfServices(customerEntitlement.getDefaultTermOfServiceInDays());
		contractDetailResponse.setEndDate(customerEntitlement.getEndDate());
		contractDetailResponse.setTransactionAmount(new BigDecimal(customerEntitlement.getTransactionAmount()));
		contractDetailResponse.setErrorCode(ERROR_CODE_ZERO);
		contractDetailResponse.setErrorMessage("");
		
		return contractDetailResponse;
		
	}
	
	private List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroup> getCourseGroupDetailResponseList(List<CourseGroup> contractCourseGroupsList) {
		
		List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroup> responseCourseGroupList = new ArrayList<>();
		
		for (CourseGroup courseGroup : contractCourseGroupsList) {
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroup responseCourseGroup = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroup();
			responseCourseGroup.setName(courseGroup.getName());
			responseCourseGroup.setGuid(courseGroup.getGuid());
			responseCourseGroupList.add(responseCourseGroup);
		}
		
		return responseCourseGroupList;
	
	}
	
	private List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Course> getCourseDetailResponseList(List<Course> contractCoursesList) {
		
		List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Course> responseCourseList = new ArrayList<>();
		
		for(Course course : contractCoursesList) {
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Course responseCourse = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Course();
			responseCourse.setName(course.getName());
			responseCourse.setGuid(course.getCourseGUID());
			responseCourseList.add(responseCourse);
		}
		
		return responseCourseList;
		
	}
	
	private OrganizationGroupDetailResponse getOrganizationGroupDetailResponse(List<OrgGroupEntitlement> orgGroupEntitlementList) {
		
		OrganizationGroupDetailResponse organizationGroupDetailResponse = null;
		
		if (!CollectionUtils.isEmpty(orgGroupEntitlementList)) {
			
			organizationGroupDetailResponse = new OrganizationGroupDetailResponse();
			OrganizationalGroups responseOrganizationalGroups = new OrganizationalGroups();
			List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationalGroup> responseOrganizationalGroupList = new ArrayList<>();
			for (OrgGroupEntitlement orgGroupEntitlement : orgGroupEntitlementList) {
				OrganizationalGroup orgGroup = orgGroupEntitlement.getOrganizationalGroup();
				String orgGroupHierarchy = orgGroup.toString();
				//String orgGroupName = orgGroup.getName();
				com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationalGroup responseOrganizationalGroup = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationalGroup();
				responseOrganizationalGroup.setId(orgGroup.getId());
				responseOrganizationalGroup.setName(orgGroupHierarchy);
				responseOrganizationalGroup.setSeats(orgGroupEntitlement.getMaxNumberSeats());
				responseOrganizationalGroup.setSeatsUsed(orgGroupEntitlement.getNumberSeatsUsed());
				
				responseOrganizationalGroupList.add(responseOrganizationalGroup);
			}
			
			responseOrganizationalGroups.setOrganizationalGroup(responseOrganizationalGroupList);
			organizationGroupDetailResponse.setOrganizationalGroups(responseOrganizationalGroups);
			organizationGroupDetailResponse.setErrorCode(ERROR_CODE_ZERO);
			organizationGroupDetailResponse.setErrorMessage("");
		}
		
		return organizationGroupDetailResponse;
	}
	
	private CustomerEntitlement getNewCustomerEntitlement(Customer customer, String contractName, String contractType, 
			String contractEndType, Date contractStartDate, Date contractEndDate, Boolean allowSelfEnrollment, 
			Integer termOfServices, Boolean allowUnlimitedEnrollments, Integer maximumEnrollments, 
			Double transactionAmount) {
		
		CustomerEntitlement customerEntitlement = null;
		
		if(contractType.equals(AddCustomerContractForm.COURSEGROUPTYPE)) {
			customerEntitlement = new CourseGroupCustomerEntitlement();
		} else if(contractType.equals(AddCustomerContractForm.COURSETYPE)) {
			customerEntitlement = new CourseCustomerEntitlement();
		} 
			
		customerEntitlement.setName(contractName);
		customerEntitlement.setCustomer(customer);
		customerEntitlement.setStartDate(contractStartDate);
		customerEntitlement.setAllowSelfEnrollment(allowSelfEnrollment);
		customerEntitlement.setContractCreationDate(new Date());
		
		if (contractEndType.equalsIgnoreCase(ContractEndType.TERM_OF_SERVICES.toString())) {
			customerEntitlement.setDefaultTermOfServiceInDays(termOfServices);
		} else if (contractEndType.equalsIgnoreCase(ContractEndType.END_DATE.toString())) {
			customerEntitlement.setEndDate(FormUtil.formatToDayEnd(contractEndDate));
		}
		
		if (allowUnlimitedEnrollments) {
			customerEntitlement.setAllowUnlimitedEnrollments(true);
		} else {
			customerEntitlement.setMaxNumberSeats(maximumEnrollments);
		}
		
		customerEntitlement.setTransactionAmount(transactionAmount);
		
		return customerEntitlement;
	}
	
	private OrgGroupEntitlement getNewOrgGroupEntitlement(OrganizationalGroup orgGroup, Integer orgGroupMaximumEnrollments, 
			CustomerEntitlement customerEntitlement, String contractEndType, Integer termOfServices, Date contractEndDate, 
			Boolean allowSelfEnrollment, Date contractStartDate ) {
		
		//OrganizationalGroup newOrgGroup = new OrganizationalGroup();
		//newOrgGroup.setId(existingOrgGroupId);
		
		OrgGroupEntitlement newOrgGroupEntitlement = new OrgGroupEntitlement();
		newOrgGroupEntitlement.setOrganizationalGroup(orgGroup);
		newOrgGroupEntitlement.setMaxNumberSeats(orgGroupMaximumEnrollments);
		newOrgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
		
		if (contractEndType.equalsIgnoreCase(ContractEndType.TERM_OF_SERVICES.toString())) {
			newOrgGroupEntitlement.setDefaultTermOfServiceInDays(termOfServices);
		} else if (contractEndType.equalsIgnoreCase(ContractEndType.END_DATE.toString())) {
			newOrgGroupEntitlement.setEndDate(contractEndDate);
		}
		
		if (allowSelfEnrollment) {
			newOrgGroupEntitlement.setAllowSelfEnrollment(true);
		} else {
			newOrgGroupEntitlement.setAllowSelfEnrollment(false);
		}
		
		newOrgGroupEntitlement.setStartDate(contractStartDate);
		
		return newOrgGroupEntitlement;
	}
	
	private List<Course> addCoursesInContract(Customer customer, CustomerEntitlement customerEntitlement, List<String> distributorCourseGroupAndCourseIdsList) {
		
		List<Course> contractCoursesList = null;
		
		if (!CollectionUtils.isEmpty(distributorCourseGroupAndCourseIdsList)) {
			contractCoursesList = new ArrayList<>();
			List<CourseCustomerEntitlementItem> courseCustomerEntitlementList = new ArrayList<CourseCustomerEntitlementItem>();
			CourseCustomerEntitlement ent = (CourseCustomerEntitlement)customerEntitlement;
			
			for (String value : distributorCourseGroupAndCourseIdsList) {
				
				String []strArray = value.split(":");

				CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupById(Long.valueOf(strArray[0]));
				Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(strArray[1]));
				
				CourseCustomerEntitlementItem courseCustomerEntitlement = new CourseCustomerEntitlementItem();
				courseCustomerEntitlement.setCustomerEntitlement(ent);
				courseCustomerEntitlement.setCourse(course);
				courseCustomerEntitlement.setCourseGroup(courseGroup);
				courseCustomerEntitlementList.add(courseCustomerEntitlement);
				contractCoursesList.add(course);
				
			}
			
			//Set<CourseCustomerEntitlementItem> courseSet =new HashSet<CourseCustomerEntitlementItem>(entitlementService.getItemsByEntitlement(ent));
			//courseSet.addAll(courseCustomerEntitlementList);
			/*for(CourseCustomerEntitlementItem courseCustomerEntitlementItem : courseCustomerEntitlementList) {
				entitlementService.addCourseEntitlementItem(courseCustomerEntitlementItem);
			}*/
			entitlementService.addCourseEntitlementItems(courseCustomerEntitlementList);
			entitlementService.saveCustomerEntitlement(customerEntitlement ,null);
		}
		
		return contractCoursesList;
	}
	
	private List<CourseGroup> addCourseGroupsInContract(Customer customer, CustomerEntitlement customerEntitlement, List<CourseGroup> validCourseGroupsList) {
		
		List<CourseGroup> contractCourseGroupsList = null;
		
		if (!CollectionUtils.isEmpty(validCourseGroupsList)) {
			List<CourseGroupCustomerEntitlementItem> courseGroupCustomerEntitlementList = new ArrayList<CourseGroupCustomerEntitlementItem>();
			contractCourseGroupsList = new ArrayList<>();
			CourseGroupCustomerEntitlement ent = (CourseGroupCustomerEntitlement)customerEntitlement;
			
			for (CourseGroup courseGroup : validCourseGroupsList) {
				CourseGroupCustomerEntitlementItem courseGroupCustomerEntitlement = new CourseGroupCustomerEntitlementItem();
				courseGroupCustomerEntitlement.setCourseGroup(courseGroup);
				courseGroupCustomerEntitlement.setCourseGroupCustomerEntitlement(ent);
				courseGroupCustomerEntitlementList.add(courseGroupCustomerEntitlement);
				contractCourseGroupsList.add(courseGroup);
			}
			
			entitlementService.addCourseGroupEntitlementItem(courseGroupCustomerEntitlementList);
			entitlementService.saveCustomerEntitlement(customerEntitlement, null);
			
		}
		
		return contractCourseGroupsList;
	}
	
	
	private String getContractDetailError(Map<String, Object> credentialsMap) {
		String contractDetailError = (String)credentialsMap.get(CONTRACT_DETAIL_ERROR_MK);
		return contractDetailError;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCourseGroupDetailMap(Map<String, Object> credentialsMap) {
		Map<String, Object> courseGroupDetailMap = (Map<String, Object>)credentialsMap.get(COURSE_GROUP_DETAIL_MAP_MK);
		return courseGroupDetailMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCourseDetailMap(Map<String, Object> credentialsMap) {
		Map<String, Object> courseDetailMap = (Map<String, Object>)credentialsMap.get(COURSE_DETAIL_MAP_MK);
		return courseDetailMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getOrganizationGroupDetailMap(Map<String, Object> credentialsMap) {
		Map<String, Object> organizationGroupDetailMap = (Map<String, Object>)credentialsMap.get(ORG_GROUP_DETAIL_MAP_MK);
		return organizationGroupDetailMap;
	}
	
	private LmsAddCustomerContractResponse getErrorResponse(Map<String, Object> credentialsMap) {
		
		LmsAddCustomerContractResponse response = null;
		AddCustomerContractResponse addCustomerContractResponse = null;
		
		String contractDetailError = getContractDetailError(credentialsMap);
		Map<String, Object> courseGroupDetailMap = getCourseGroupDetailMap(credentialsMap);
		Map<String, Object> courseDetailMap = getCourseDetailMap(credentialsMap);
		Map<String, Object> organizationGroupDetailMap = getOrganizationGroupDetailMap(credentialsMap);
		
		if (contractDetailError != null) {
			if (addCustomerContractResponse == null) {
				addCustomerContractResponse = new AddCustomerContractResponse();
			}
			ContractDetailResponse contractDetailResponse = new ContractDetailResponse();
			contractDetailResponse.setErrorCode(ERROR_CODE_ONE);
			contractDetailResponse.setErrorMessage(contractDetailError);
			addCustomerContractResponse.setContractDetailResponse(contractDetailResponse);
		}
		
		if (!CollectionUtils.isEmpty(courseGroupDetailMap)) {
			CourseGroupDetailResponse courseGroupDetailResponse = getCourseGroupDetailError(courseGroupDetailMap);
			if (courseGroupDetailResponse != null) {
				if (addCustomerContractResponse == null) {
					addCustomerContractResponse = new AddCustomerContractResponse();
				}
				addCustomerContractResponse.setCourseGroupDetailResponse(courseGroupDetailResponse);
			}
			
		}
		
		if (!CollectionUtils.isEmpty(courseDetailMap)) {
			CourseDetailResponse courseDetailResponse = getCourseDetailError(courseDetailMap);
			if (courseDetailResponse != null) {
				if (addCustomerContractResponse == null) {
					addCustomerContractResponse = new AddCustomerContractResponse();
				}
				addCustomerContractResponse.setCourseDetailResponse(courseDetailResponse);
			}
			
		}
		
		if (!CollectionUtils.isEmpty(organizationGroupDetailMap)) {
			OrganizationGroupDetailResponse organizationGroupDetailResponse = getOrganizationGroupDetailError(organizationGroupDetailMap);
			if (organizationGroupDetailResponse != null) {
				if (addCustomerContractResponse == null) {
					addCustomerContractResponse = new AddCustomerContractResponse();
				}
				addCustomerContractResponse.setOrganizationGroupDetailResponse(organizationGroupDetailResponse);
			}
			
		}
		
		if (addCustomerContractResponse != null ) {
			response = new LmsAddCustomerContractResponse();
			response.setAddCustomerContractResponse(addCustomerContractResponse);
		}
		
		return response;
		
	}
	
	private CourseGroupDetailResponse getCourseGroupDetailError(Map<String, Object> courseGroupDetailMap) {
		
		CourseGroupDetailResponse courseGroupDetailResponse = null;
		
		String courseGroupDetailError = (String)courseGroupDetailMap.get(COURSE_GROUP_DETAIL_ERROR_MK);
		String courseGroupsError = (String)courseGroupDetailMap.get(COURSE_GROUPS_ERROR_MK);
		
		if (courseGroupDetailError != null) {
			courseGroupDetailResponse = new CourseGroupDetailResponse();
			courseGroupDetailResponse.setErrorCode(ERROR_CODE_ONE);
			courseGroupDetailResponse.setErrorMessage(courseGroupDetailError);
		} else if (courseGroupsError != null) {
			courseGroupDetailResponse = new CourseGroupDetailResponse();
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups responseCourseGroups = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups();
			responseCourseGroups.setErrorCode(ERROR_CODE_ONE);
			responseCourseGroups.setErrorMessage(courseGroupsError);
			courseGroupDetailResponse.setCourseGroups(responseCourseGroups);
		} else {
			List<CourseGroup> validDistributorCourseGroupList = getValidDistributorCourseGroupList(courseGroupDetailMap);
			if (CollectionUtils.isEmpty(validDistributorCourseGroupList)) {
				List<String> invalidGuidsList = getAllInvalidCourseGroupGuidList(courseGroupDetailMap);
				if (!CollectionUtils.isEmpty(invalidGuidsList)) {
					courseGroupDetailResponse = new CourseGroupDetailResponse();
					com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups responseCourseGroups = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.CourseGroups();
					InvalidCourseGroups invalidCourseGroups = getInvalidCourseGroups(invalidGuidsList);
					responseCourseGroups.setInvalidCourseGroups(invalidCourseGroups);
					courseGroupDetailResponse.setCourseGroups(responseCourseGroups);
				}
			}
		}
		
		return courseGroupDetailResponse;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> getCourseGroupsMap(Map<String, Object> courseGroupDetailMap) {
		
		Map<String, List<?>> courseGroupsMap = null;
		
		if (!CollectionUtils.isEmpty(courseGroupDetailMap)) {
			courseGroupsMap = (Map<String, List<?>>)courseGroupDetailMap.get(COURSE_GROUPS_MAP_MK);
		}
		
		return courseGroupsMap;
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> getDistributorCourseGroupMap(Map<String, Object> courseGroupDetailMap) {
		
		Map<String, List<?>> distributorCourseGroupMap = null;
		Map<String, List<?>> courseGroupsMap = getCourseGroupsMap(courseGroupDetailMap);
		if (!CollectionUtils.isEmpty(courseGroupsMap)) {
			distributorCourseGroupMap = (Map<String, List<?>>)courseGroupsMap.get(DISTRIBUTOR_COURSE_GROUP_MAP_MK);
		}
		return distributorCourseGroupMap;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<CourseGroup> getValidDistributorCourseGroupList(Map<String, Object> courseGroupDetailMap) {
		
		List<CourseGroup> validDistributorCourseGroupList = null;
		Map<String, List<?>> distributorCourseGroupMap = getDistributorCourseGroupMap(courseGroupDetailMap);
		if (!CollectionUtils.isEmpty(distributorCourseGroupMap)) {
			validDistributorCourseGroupList = (List<CourseGroup>)distributorCourseGroupMap.get(VALID_DISTRIBUTOR_COURSE_GROUP_LIST_MK);
		}
		
		return validDistributorCourseGroupList;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getInvalidCourseGroupGuidList(Map<String, Object> courseGroupDetailMap) {
		
		List<String> invalidCourseGroupGuidList = null;
		Map<String, List<?>> courseGroupsMap = getCourseGroupsMap(courseGroupDetailMap);
		if (!CollectionUtils.isEmpty(courseGroupsMap)) {
			invalidCourseGroupGuidList = (List<String>)courseGroupsMap.get(INVALID_COURSE_GROUP_GUID_LIST_MK);
		}
		
		return invalidCourseGroupGuidList;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getInvalidDistributorCourseGroupGuidList(Map<String, Object> courseGroupDetailMap) {
		
		List<String> invalidDistributorCourseGroupGuidList = null;
		Map<String, List<?>> distributorCourseGroupMap = getDistributorCourseGroupMap(courseGroupDetailMap);
		if (!CollectionUtils.isEmpty(distributorCourseGroupMap)) {
			invalidDistributorCourseGroupGuidList = (List<String>)distributorCourseGroupMap.get(INVALID_DISTRIBUTOR_COURSE_GROUP_GUID_LIST_MK);
		}
		
		return invalidDistributorCourseGroupGuidList;
			
	}
	
	private List<String> getAllInvalidCourseGroupGuidList(Map<String, Object> courseGroupDetailMap) {
		
		List<String> invalidCourseGroupGuidList = getInvalidCourseGroupGuidList(courseGroupDetailMap);
		List<String> invalidDistributorCourseGroupGuidList = getInvalidDistributorCourseGroupGuidList(courseGroupDetailMap);
		List<String> invalidGuidsList = new ArrayList<String>();
		
		if (!CollectionUtils.isEmpty(invalidCourseGroupGuidList)) {
			invalidGuidsList.addAll(invalidCourseGroupGuidList);
		}
		
		if (!CollectionUtils.isEmpty(invalidDistributorCourseGroupGuidList)) {
			invalidGuidsList.addAll(invalidDistributorCourseGroupGuidList);
		}
		
		return invalidGuidsList;
	}
	
	private InvalidCourseGroups getInvalidCourseGroups(List<String> invalidGuidsList) {
		
		String errorMessage = "No CourseGroup found for the guids or guids not belong to distributor";
		InvalidCourseGroups invalidCourseGroups = new InvalidCourseGroups();
		invalidCourseGroups.setErrorCode(ERROR_CODE_ONE);
		invalidCourseGroups.setErrorMessage(errorMessage);
		invalidCourseGroups.setGuid(invalidGuidsList);
		return invalidCourseGroups;
		
	}
	
	private CourseDetailResponse getCourseDetailError(Map<String, Object> courseDetailMap) {
		
		CourseDetailResponse courseDetailResponse = null;
		
		String courseDetailError = (String)courseDetailMap.get(COURSE_DETAIL_ERROR_MK);
		String coursesError = (String)courseDetailMap.get(COURSES_ERROR_MK);
		if (courseDetailError != null) {
			courseDetailResponse = new CourseDetailResponse();
			courseDetailResponse.setErrorCode(ERROR_CODE_ONE);
			courseDetailResponse.setErrorMessage(courseDetailError);
		} else if (coursesError != null) {
			courseDetailResponse = new CourseDetailResponse();
			com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses responseCourses = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses();
			responseCourses.setErrorCode(ERROR_CODE_ONE);
			responseCourses.setErrorMessage(coursesError);
			courseDetailResponse.setCourses(responseCourses);
		} else {
			List<String> validDistributorCourseGroupAndCourseIdsList = getValidDistributorCourseGroupAndCourseIdsList(courseDetailMap);
			if (CollectionUtils.isEmpty(validDistributorCourseGroupAndCourseIdsList)) {
				List<String> invalidGuidsList = getAllInvalidCourseGuidList(courseDetailMap);
				if (!CollectionUtils.isEmpty(invalidGuidsList)) {
					courseDetailResponse = new CourseDetailResponse();
					com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses responseCourses = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.Courses();
					InvalidCourses invalidCourses = getInvalidCourses(invalidGuidsList);
					responseCourses.setInvalidCourses(invalidCourses);
					courseDetailResponse.setCourses(responseCourses);
				}
			}		
		}
		
		return courseDetailResponse;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> getCoursesMap(Map<String, Object> courseDetailMap) {
		
		Map<String, List<?>> coursesMap = null;
		if (!CollectionUtils.isEmpty(courseDetailMap)) {
			coursesMap = (Map<String, List<?>>)courseDetailMap.get(COURSES_MAP_MK);
		}
		
		return coursesMap;
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> getDistributorCourseMap(Map<String, Object> courseDetailMap) {
		
		Map<String, List<?>> distributorCourseMap = null;
		Map<String, List<?>> coursesMap = getCoursesMap(courseDetailMap);
		if (!CollectionUtils.isEmpty(coursesMap)) {
			distributorCourseMap  = (Map<String, List<?>>)coursesMap.get(DISTRIBUTOR_COURSE_MAP_MK);
		}
		
		return distributorCourseMap;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getValidDistributorCourseGroupAndCourseIdsList(Map<String, Object> courseDetailMap) {
		
		List<String> validDistributorCourseGroupAndCourseIdsList = null;
		
		Map<String, List<?>> distributorCourseMap = getDistributorCourseMap(courseDetailMap);
		if (!CollectionUtils.isEmpty(distributorCourseMap)) {
			validDistributorCourseGroupAndCourseIdsList = (List<String>)distributorCourseMap.get(VALID_DISTRIBUTOR_COURSE_GROUP_AND_COURSE_IDs_LIST_MK);
		}
		
		return validDistributorCourseGroupAndCourseIdsList;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getInvalidCourseGuidList(Map<String, Object> courseDetailMap) {
		
		List<String> invalidCourseGuidList = null;
		Map<String, List<?>> coursesMap = getCoursesMap(courseDetailMap);
		if (!CollectionUtils.isEmpty(coursesMap)) {
			invalidCourseGuidList = (List<String>)coursesMap.get(INVALID_COURSES_GUID_LIST_MK);
		}
		
		return invalidCourseGuidList;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getInvalidDistributorCourseGuidList(Map<String, Object> courseDetailMap) {
		
		List<String> invalidDistributorCourseGuidList = null;
		Map<String, List<?>> distributorCourseMap = getDistributorCourseMap(courseDetailMap);
		if (!CollectionUtils.isEmpty(distributorCourseMap)) {
			invalidDistributorCourseGuidList = (List<String>)distributorCourseMap.get(INVALID_DISTRIBUTOR_COURSE_GUID_LIST_MK);
		}
		
		return invalidDistributorCourseGuidList;
	}
	
	private List<String> getAllInvalidCourseGuidList(Map<String, Object> courseDetailMap) {
		
		List<String> invalidCourseGuidList = getInvalidCourseGuidList(courseDetailMap);
		List<String> invalidDistributorCourseGuidList = getInvalidDistributorCourseGuidList(courseDetailMap);
		
		List<String> invalidGuidsList = new ArrayList<String>();
		
		if (!CollectionUtils.isEmpty(invalidCourseGuidList)) {
			invalidGuidsList.addAll(invalidCourseGuidList);
		}
		if (!CollectionUtils.isEmpty(invalidDistributorCourseGuidList)) {
			invalidGuidsList.addAll(invalidDistributorCourseGuidList);
		}
		return invalidGuidsList;
	}
	
	private InvalidCourses getInvalidCourses(List<String> invalidGuidsList) {
		
		String errorMessage = "No Course found for the guids or guids are not belong to distributor";
		InvalidCourses invalidCourses = new InvalidCourses();
		invalidCourses.setErrorCode(ERROR_CODE_ONE);
		invalidCourses.setErrorMessage(errorMessage);
		invalidCourses.setGuid(invalidGuidsList);
		return invalidCourses;
		
	}
	
	@SuppressWarnings("unchecked")
	private OrganizationGroupDetailResponse getOrganizationGroupDetailError( Map<String, Object> organizationGroupDetailMap) {
		
		OrganizationGroupDetailResponse organizationGroupDetailResponse = null;
		
		String organizationGroupDetailError = (String)organizationGroupDetailMap.get(ORG_GROUP_DETAIL_ERROR_MK);
		String organizationGroupsEnrollmentsError = (String)organizationGroupDetailMap.get(ORG_GROUPS_ENROLLMENTS_ERROR_MK);
		Map<String, Object> organizationGroupEnrollmentsMap = getOrganizationGroupEnrollmentsMap(organizationGroupDetailMap);
		
		if (organizationGroupDetailError != null) {
			organizationGroupDetailResponse = new OrganizationGroupDetailResponse();
			organizationGroupDetailResponse.setErrorCode(ERROR_CODE_ONE);
			organizationGroupDetailResponse.setErrorMessage(organizationGroupDetailError);
		} else if (organizationGroupsEnrollmentsError != null) {
			organizationGroupDetailResponse = new OrganizationGroupDetailResponse();
			organizationGroupDetailResponse.setErrorCode(ERROR_CODE_ONE);
			organizationGroupDetailResponse.setErrorMessage(organizationGroupsEnrollmentsError);
		} else {
			
			String organizationGroupEnrollmentsExceedMaximumEnrollmentsError = (String)organizationGroupEnrollmentsMap.get(ORG_GROUP_ENROLLMENTS_EXCEED_MAX_ENROLLMENTS_ERROR_MK);
			Map<OrganizationGroupEnrollment, String> organizationGroupEnrollmentErrorMap = (Map<OrganizationGroupEnrollment, String>)organizationGroupEnrollmentsMap.get(ORG_GROUP_ENROLLMENT_ERROR_MAP_MK);
			
			if (!CollectionUtils.isEmpty(organizationGroupEnrollmentErrorMap) || organizationGroupEnrollmentsExceedMaximumEnrollmentsError != null) {
				organizationGroupDetailResponse = new OrganizationGroupDetailResponse();
				if (!CollectionUtils.isEmpty(organizationGroupEnrollmentErrorMap)) {
					InvalidOrganizationGroupsEnrollments invalidOrganizationGroupsEnrollments = new InvalidOrganizationGroupsEnrollments();
					List<com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationGroupEnrollment> invalidOrganizationGroupEnrollmentList = new ArrayList<>();
					for (Map.Entry<OrganizationGroupEnrollment, String> entry : organizationGroupEnrollmentErrorMap.entrySet()) {
						
						OrganizationGroupEnrollment key = entry.getKey();
						String error = entry.getValue();
						
						com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationGroupEnrollment responseOrganizationGroupEnrollment = new com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.OrganizationGroupEnrollment();
						responseOrganizationGroupEnrollment.setErrorCode(ERROR_CODE_ONE);
						responseOrganizationGroupEnrollment.setErrorMessage(error);
						responseOrganizationGroupEnrollment.setOrgGroupHierarchy(key.getOrgGroupHierarchy());
						responseOrganizationGroupEnrollment.setMaximumEnrollments(key.getMaximumEnrollments());
						invalidOrganizationGroupEnrollmentList.add(responseOrganizationGroupEnrollment);
						
					} //end of for()
					
					invalidOrganizationGroupsEnrollments.setOrganizationGroupEnrollment(invalidOrganizationGroupEnrollmentList);
					organizationGroupDetailResponse.setInvalidOrganizationGroupsEnrollments(invalidOrganizationGroupsEnrollments);
				
				} //end of if()
				
				if (organizationGroupEnrollmentsExceedMaximumEnrollmentsError != null) {
					organizationGroupDetailResponse.setErrorCode(ERROR_CODE_ONE);
					organizationGroupDetailResponse.setErrorMessage(organizationGroupEnrollmentsExceedMaximumEnrollmentsError);
				}
				
			} //end of if()
			
		}
	
		return organizationGroupDetailResponse;
		
	}
	
	@Override
	public AbstractResponse handleRequestError(AbstractRequest request, Exception e) throws Exception {
		
		LmsAddCustomerContractResponse response = new LmsAddCustomerContractResponse();
		AddCustomerContractResponse addCustomerContractResponse = new AddCustomerContractResponse();
		String errorMessage = e.getMessage();
		addCustomerContractResponse.setErrorCode(ERROR_CODE_ONE);
		addCustomerContractResponse.setErrorMessage(errorMessage);
		response.setAddCustomerContractResponse(addCustomerContractResponse);
		return response;
			
	}
	
	enum ContractEndType {
		/**
		 * You can specify values of enum constants at the creation time as shown in below:
				
				public enum CourseStatus {NOT_STARTED(0, "notstarted"), COMPLETD(1, "completed")..};

		 * But for this to work you need to define a member variables and a constructor because 
		 * NOT_STARTED(0, "notstarted") is actually calling a constructor which accepts int and string value
		 * 
		 * Enum constants are implicitly static and final and can not be changed once created.


		 */
		TERM_OF_SERVICES(0, "NumberOfDays"), 
		END_DATE(1, "EndDate");
		
		private Integer id;
		private String description;
		
		// Constructor of enum in java must be private any other access modifier will result in compilation error.
		private ContractEndType(final Integer id, final String description) {
			this.id = id;
		    this.description = description;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		// Override the toString() method in the Enum class to return description
		@Override
		public String toString() {
			return this.description;
		}
		
	}
	
}
