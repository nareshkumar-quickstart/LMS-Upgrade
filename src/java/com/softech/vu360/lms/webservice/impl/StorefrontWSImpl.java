package com.softech.vu360.lms.webservice.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.OrderCreatedEventService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.impl.OrderCreatedEventServiceImpl;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.webservice.StorefrontWS;
import com.softech.vu360.lms.webservice.message.storefront.CourseDetails;
import com.softech.vu360.lms.webservice.message.storefront.CourseDetailsRequest;
//application imports
import com.softech.vu360.lms.webservice.message.storefront.CourseDetailsResponse;
import com.softech.vu360.lms.webservice.message.storefront.InstructorInfo;
import com.softech.vu360.lms.webservice.message.storefront.Order;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedResponse;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;
import com.softech.vu360.lms.webservice.message.storefront.RefundResponse;
import com.softech.vu360.lms.webservice.message.storefront.SectionInfo;
import com.softech.vu360.lms.webservice.message.storefront.SessionInfo;
import com.softech.vu360.lms.webservice.message.storefront.TransactionResultType;
import com.softech.vu360.lms.webservice.message.storefront.UpdateProfileRequest;
import com.softech.vu360.lms.webservice.message.storefront.UpdateProfileResponse;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApprovalRequest;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApprovalResponse;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.RegulatoryCategory;



/**
 * StorefrontWSImpl defines the set of interfaces 
 * to control the interactions and business logic
 * of the integration between Storefront & LMS.
 * 
 * @author jason
 */
@Endpoint
public class StorefrontWSImpl implements StorefrontWS {
	
	private static final long serialVersionUID = -5609592438565324419L;

	private static final Logger log = Logger.getLogger(StorefrontWSImpl.class.getName());
	
	private static final String ORDER_CREATED_EVENT = "OrderCreatedRequest";
	private static final String UPDATE_PROFILE_EVENT = "UpdateProfileRequest";
	private static final String REFUND_EVENT = "RefundRequest";
	private static final String GET_COURSE_DETAIL_EVENT = "CourseDetailsRequest";
	
	private static final String GET_COURSE_APPROVAL_EVENT = "CourseApprovalRequest";
	
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/sfMessages";
	//public OrderCreatedEventDAO orderCreatedEventDAO2=null;
	public OrderCreatedEventService orderCreatedEventService;
	public SynchronousClassService synchronousClassService ;
	
	/*
	 * (non-Javadoc)
	 * @see com.softech.vu360.lms.webservice.StorefrontWS#getCourseDetailsEvent(com.softech.vu360.lms.webservice.message.storefront.CourseDetailsRequest)
	 * Course Approval Services
	 */
	public AccreditationService accreditationService;

	
	@PayloadRoot(localPart = GET_COURSE_DETAIL_EVENT, namespace = MESSAGES_NAMESPACE)
	public CourseDetailsResponse getCourseDetailsEvent(CourseDetailsRequest	 courseDetailsRequest) {
	/*
	 * TODO use SynchronousClassService and other related services to retrieved related info and transform it into
	 * web service objects and return in CourseDetailsResponse object.
	 */
		List<String> courseGUIDList= courseDetailsRequest.getCourseGUID() ;//new ArrayList<String>();
		//courseGUIDList.add(courseDetailsRequest.getCourseGUID() );
		List<CourseDetails> response= getCourseDetails(courseGUIDList) ; 
		CourseDetailsResponse courseDetailsResponse = new CourseDetailsResponse();
		if( response != null) {
			
			for(CourseDetails cd: response) {
				courseDetailsResponse.getCoursePublishInfo().add(cd);
			}
		}
		
		return courseDetailsResponse;
	}
	
	/**
	 * Following method will be mapped based on PayloadRoot annotation definition i.e. OrderCrteatedRequest.
	 * Client will be passing in all the relevant details pertaining to creating an order in the system
	 *  
	 */
	@PayloadRoot(localPart = ORDER_CREATED_EVENT, namespace = MESSAGES_NAMESPACE)
	public OrderCreatedResponse orderCreatedEvent(OrderCreatedRequest orderCreatedEvent) {
		log.debug("orderCreatedEvent was called.");
		
		//Need to retrieve the Order object and from it all the relevant objects. Once, all the info is there we 
		//should call the Business Logic to validate/insert the data in the db. fmk march-25-2009
		Order order = orderCreatedEvent.getOrder();
		log.debug( "CustomerName: " + order.getCustomer().getCustomerName() + "\n\n");
		log.debug( "Order ID: :" + order.getOrderId() + "\n\n" );
		
		//Response object should be created to send back to the client
		OrderCreatedResponse response = new OrderCreatedResponse();
		response.setEventDate( orderCreatedEvent.getEventDate() );
		response.setTransactionGUID( orderCreatedEvent.getTransactionGUID() );
		try{
			LmsSfOrderResultBuilder lmsSfOrderResultbuilder = orderCreatedEventService.processStoreFrontOrder(orderCreatedEvent);
			if(lmsSfOrderResultbuilder != null && lmsSfOrderResultbuilder.getOrderAuditList() != null && lmsSfOrderResultbuilder.getOrderAuditList().size() > 0 )
			{
				//Log auditing
				orderCreatedEventService.orderAuditLog(lmsSfOrderResultbuilder);
			}
			if(lmsSfOrderResultbuilder != null && lmsSfOrderResultbuilder.hasOneValidCourseGuid() )
			{
				log.debug("SUCCESS");
				response.getTransactionResult().add( TransactionResultType.SUCCESS );
				response.setTransactionResultMessage("Order Processed Successfully");
				if(((OrderCreatedEventServiceImpl)orderCreatedEventService).isCorporateAuthorVar()){
					//As per requirements we will place VU360User GUID in setCustomerGUID is isCorporateAuthorVar is true. 
					response.setCustomerGUID(lmsSfOrderResultbuilder.getVU360User().getUserGUID());
				}else{
					response.setCustomerGUID(lmsSfOrderResultbuilder.getCustomer().getCustomerGUID());	
				}				
			}
			else
			{
				response.getTransactionResult().add( TransactionResultType.FAILURE );
				response.setTransactionResultMessage("Order Processed Failed"+";vu360userID="+(lmsSfOrderResultbuilder.getVU360User() == null ? "-1": lmsSfOrderResultbuilder.getVU360User().getId().toString()));
				
				if (order.getOrderId().equalsIgnoreCase("-1")){//Flag used for VarMigration only
					if(((OrderCreatedEventServiceImpl)orderCreatedEventService).isCorporateAuthorVar()){
						//As per requirements we will place VU360User GUID in setCustomerGUID is isCorporateAuthorVar is true. 
						response.setCustomerGUID(lmsSfOrderResultbuilder.getVU360User().getUserGUID());
					}else{
						response.setCustomerGUID(lmsSfOrderResultbuilder.getCustomer().getCustomerGUID());	
					}
				}
				else{
					response.setTransactionResultMessage("Order Processed Failed");
					response.setCustomerGUID((lmsSfOrderResultbuilder.getCustomer() != null) ? lmsSfOrderResultbuilder.getCustomer().getCustomerGUID() : "");
					log.debug("FAILED");
				}
			}
		}
		catch(Exception ex){
			log.error("Exception occured in order processing "+ex);
		}
		return response;
	}

	 
	 
	 
	 
	/**
	 * Following method will be mapped based on PayloadRoot annotation definitation i.e. UpdateProfile.
	 * Client will be passing in all the relevant details pertaining to updating the profile of a user
	 *  
	 */
	@PayloadRoot(localPart = UPDATE_PROFILE_EVENT, namespace = MESSAGES_NAMESPACE)
	public UpdateProfileResponse updateProfileEvent(UpdateProfileRequest updateProfileEvent) {
		UpdateProfileResponse response = new UpdateProfileResponse();
		log.trace( "UpdateProfileRequest method called...." + "\n\n");
		log.trace( "CustomerName: " + updateProfileEvent.getCustomer().getCustomerName() + "\n\n");
		response.getTransactionResult().add( TransactionResultType.SUCCESS );
		return response;
	}
	
	/**
	 * Following method will be mapped based on PayloadRoot annotation definitation i.e. UpdateProfile.
	 * Client will be passing in all the relevant details pertaining to refunding an order
	 *  
	 */
	@PayloadRoot(localPart = REFUND_EVENT, namespace = MESSAGES_NAMESPACE)
	public RefundResponse refundEvent(RefundRequest refundEvent) {
		Order order = refundEvent.getOrder();
		log.trace( "CustomerName: " + order.getCustomer().getCustomerName() + "\n\n");
		log.trace( "Order ID: :" + order.getOrderId() + "\n\n" );
		
		//Response object should be created to send back to the client
		RefundResponse response = new RefundResponse();
		response.setEventDate( refundEvent.getEventDate() );
		response.setTransactionGUID( refundEvent.getTransactionGUID() );
		
		try
		{
		if(orderCreatedEventService.implementRefund(refundEvent)){
			response.getTransactionResult().add( TransactionResultType.SUCCESS );
			response.setTransactionResultMessage("Succeeded!");
			log.debug("SUCCESS");
		}
		else{
			response.getTransactionResult().add( TransactionResultType.FAILURE );
			response.setTransactionResultMessage("Failed!");
			log.debug("FAILURE");
		}
			
		}
		catch(Exception ex){
			log.debug("Exception Occured while implementing refund: " + ex.getMessage());
			response.getTransactionResult().add( TransactionResultType.FAILURE );
			response.setTransactionResultMessage("Failed with Exception: " + ex.getMessage());
		}
		
		return response;
	}	
	
	/**
	 * Following method will be mapped on PayloadRoot annotation definition i.e course approval
	 * Client will be passing in all the relevant details pertaining to course approval
	 */
	@PayloadRoot(localPart = GET_COURSE_APPROVAL_EVENT, namespace = MESSAGES_NAMESPACE)
	public CourseApprovalResponse getCourseApproval(CourseApprovalRequest courseApprovalReq) {
		CourseApprovalResponse response = new CourseApprovalResponse();
		try {
			Course course = accreditationService.getCourseByGUID(courseApprovalReq.getCourseGUID());
			if(course != null) {
				List<CourseApproval> courseApprovalList = accreditationService.getCourseApprovalListByCourse(course);
				
				response.getCourseApproval().addAll(getCourseApprovalsArray(courseApprovalList));
			}
			
			
		} catch(WebServiceException wse) {
			log.error("Course Approval Services Exception:" + wse.getMessage());
		} catch(NullPointerException npe) {
			log.error("Course Approval Null:" + npe.getMessage());
		} catch(Exception ex) {
			log.error("Coure Approval Service Exception:" + ex.getMessage());
		}
		
		return response;
	}
	
	/**
	 * Following method iterate course approvals list for Store front course approval response.
	 * @param courseApprovalList
	 * @return
	 */
	
	public List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApproval> 
			getCourseApprovalsArray(List<CourseApproval> courseApprovalList) {
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApproval> courseApprovalArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApproval>();
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApproval courseApproval = null;
		for(CourseApproval courseApprovalIter : courseApprovalList) {
			courseApproval = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApproval();
			courseApproval.setApprovedCourseName(avoidNullString(courseApprovalIter.getApprovedCourseName()));
			courseApproval.setCourseApprovalNumber(avoidNullString(courseApprovalIter.getCourseApprovalNumber()));
			courseApproval.setCourseApprovaltype(avoidNullString(courseApprovalIter.getCourseApprovaltype()));
			courseApproval.setApprovedCreditHours(courseApprovalIter.getApprovedCreditHours().equals("") ? new BigDecimal(0.0) : new BigDecimal(courseApprovalIter.getApprovedCreditHours()));
			courseApproval.setId(courseApprovalIter.getId());
			courseApproval.setCourse(getCourseforApprovals(courseApprovalIter.getCourse()));
			courseApproval.getRegulatorCategories().addAll((getRegulatoryCategoriesArrayForApprovals(courseApprovalIter)));
			courseApproval.getCredentialCategory().addAll(getCredentialCategoryForApproval(courseApprovalIter.getRequirements()));
//			crsApproval.setCourseGroup(getCourseGroupForApprovals(ca));
			courseApprovalArray.add(courseApproval);
		}
		return courseApprovalArray;
	}
	
	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> getCredentialCategoryForApproval(
			List<CredentialCategoryRequirement> requirements) {
		
		
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> credentialCategoryArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory>();
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory objCredentialCategory = null;
		com.softech.vu360.lms.model.CredentialCategory credentialCategory = null;

		if(requirements != null && requirements.size() > 0)
		{
			for(CredentialCategoryRequirement requirement : requirements) {
				objCredentialCategory = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory();
				credentialCategory = requirement.getCredentialCategory();
				
				if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.PRE_LICENSE)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				} 
				else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.POST_LICENSE)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				}
				else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.CONTINUING_EDUCATION)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				} 
				else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.DESIGNATION)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				} 
				else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.PROFESSIONAL_DEVELOPMENT)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				}
				else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.CERTIFICATION)) {
					if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,requirement))
					{
						credentialCategoryArray.add(addCredentialCategoryForApprovals(requirement, credentialCategory, requirement.getCredentialCategory().getCredential()));
					}	
				}
			}
			
		}
		return credentialCategoryArray;
	}
	

	/**
	 * 
	 * @param proxyCredentialCategoryList
	 * @param credentialCategory
	 * @param requirement
	 * @return
	 */
	private boolean isAlreadyExistInCredentialCategory(List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> proxyCredentialCategoryList, com.softech.vu360.lms.model.CredentialCategory credentialCategory,
			com.softech.vu360.lms.model.CredentialCategoryRequirement requirement)
	{
		boolean isExist = false;
		if(proxyCredentialCategoryList != null && proxyCredentialCategoryList.size() > 0 && credentialCategory != null)
		{
			for(CredentialCategory category : proxyCredentialCategoryList)
			{
				if(category.getCategoryType().equals(credentialCategory.getCategoryType()))
					{
						isExist = true;
						//Check to see if credential already exist in credential category
						if(!credentialAlreadyExist(category.getCredentials(), requirement)) {
							category.getCredentials().add(getCredentialsForCourseApproval(requirement));
						}
					}
					
			}
		}
		return isExist;
	}
	
	private com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential getCredentialsForCourseApproval(CredentialCategoryRequirement requirement) {
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential credential = null;
		if(requirement != null) {
			credential = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential();
			
			credential.setId(requirement.getCredentialCategory().getCredential().getId());
			credential.setOfficialLicenseName(avoidNullString(requirement.getCredentialCategory().getCredential().getOfficialLicenseName()));
			credential.setShortLicenseName(avoidNullString(requirement.getCredentialCategory().getCredential().getShortLicenseName()));
			credential.setTotalNumberOfLicense(requirement.getCredentialCategory().getCredential().getTotalNumberOfLicense() == 0 ? 0 : requirement.getCredentialCategory().getCredential().getTotalNumberOfLicense());
			credential.setOtherJurisdiction(avoidNullString(requirement.getCredentialCategory().getCredential().getOtherJurisdiction()));
			credential.setActive(requirement.getCredentialCategory().getCredential().isActive());
			credential.setCredentialType(avoidNullString(requirement.getCredentialCategory().getCredential().getCredentialType()));
			credential.setRenewalDeadlineType(avoidNullString(requirement.getCredentialCategory().getCredential().getRenewalDeadlineType()));
			credential.setStaggeredBy(avoidNullString(requirement.getCredentialCategory().getCredential().getStaggeredBy()));
			credential.setStaggeredTo(avoidNullString(requirement.getCredentialCategory().getCredential().getStaggeredTo()));
			credential.setRenewalFrequency(avoidNullString(credentialRenewalFrequencyYearLabel(requirement.getCredentialCategory().getCredential().getRenewalFrequency())));
			credential.setHardDeadlineMonth(avoidNullString(requirement.getCredentialCategory().getCredential().getHardDeadlineMonth()));
			credential.setHardDeadlineDay(avoidNullString(requirement.getCredentialCategory().getCredential().getHardDeadlineDay()));
			credential.setHardDeadlineYear(avoidNullString(requirement.getCredentialCategory().getCredential().getHardDeadlineYear()));
			credential.setDescription(avoidNullString(requirement.getCredentialCategory().getCredential().getDescription()));
			credential.setPreRequisite(avoidNullString(requirement.getCredentialCategory().getCredential().getPreRequisite()));
			credential.getRequirements().add(getRequirementProxy(requirement));
		}
		
		return credential;
	}
	
	private boolean credentialAlreadyExist(List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential> credentials, CredentialCategoryRequirement requirement) {
		boolean isExists = false;
		
		if(credentials != null && credentials.size() > 0 && requirement != null) {
			for(com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential credential : credentials) {
				if(credential.getId() == requirement.getCredentialCategory().getCredential().getId()) {
					credential.getRequirements().add(getRequirementProxy(requirement));
					isExists = true;
				}
			}
		}
		
		return isExists;
	}
	
	private com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory addCredentialCategoryForApprovals(CredentialCategoryRequirement requirement 
			,com.softech.vu360.lms.model.CredentialCategory credentialCategory, Credential credential) {
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory proxyCredentialCategory = 
				new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory();
		
		proxyCredentialCategory.getCredentials().add(getCredentialsForCourseApproval(requirement));
		proxyCredentialCategory.setId(credentialCategory.getId());
		proxyCredentialCategory.setName(avoidNullString(credentialCategory.getName()));
		proxyCredentialCategory.setHours(credentialCategory.getHours() == 0.0 ? new BigDecimal(0.0) : new BigDecimal(credentialCategory.getHours()));
		proxyCredentialCategory.setCompletionDeadlineMonths(credentialCategory.getCompletionDeadlineMonths() == 0.0 ? 0.0 : credentialCategory.getCompletionDeadlineMonths());
		proxyCredentialCategory.setCategoryType(avoidNullString(credentialCategory.getCategoryType()));
		
		return proxyCredentialCategory;
	}
	
	
	
	/**
	 * Following method create Course object for Course Approval.
	 * @param course
	 * @return
	 */
	
	public com.softech.vu360.lms.webservice.message.storefront.courseapproval.Course getCourseforApprovals(Course course) {
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.Course objCourse = null;
		if(course != null) {
			objCourse = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.Course();
			objCourse.setId(course.getId());
			objCourse.setApprovalNumber(avoidNullString(course.getApprovalNumber()));
			if(new BigDecimal(course.getApprovedcoursehours()) == null) {
				objCourse.setApprovedcoursehours(new BigDecimal(0.0));
			} else {
				objCourse.setApprovedcoursehours(BigDecimal.valueOf(course.getApprovedcoursehours()));
			}
			objCourse.setBussinesskey(avoidNullString(course.getBussinesskey()));
			if(new BigDecimal(course.getCeus()) == null) {
				objCourse.setCeus(new BigDecimal(0.0));
			} else {
				objCourse.setCeus(BigDecimal.valueOf(course.getCeus()));
			}
			objCourse.setCode(avoidNullString(course.getCode()));
			objCourse.setCourseGUID(avoidNullString(course.getCourseGUID()));
			objCourse.setCourseId(avoidNullString(course.getCourseId()));			
			objCourse.setCourseStatus(avoidNullString(course.getCourseStatus()));
			objCourse.setFinalExamInfo(avoidNullString(course.getFinalExamInfo()));
			objCourse.setName(avoidNullString(course.getName()));
			objCourse.setRetired(course.isRetired());
		}
		
		return objCourse;
	}
	
	/**
	 * Following method create address object.
	 * @param address
	 * @return
	 */
	
	public com.softech.vu360.lms.webservice.message.storefront.Address getAddressforApprovals(Address address) {
		com.softech.vu360.lms.webservice.message.storefront.Address objAddress = null;
		if(address != null) {
			objAddress = new com.softech.vu360.lms.webservice.message.storefront.Address();
			objAddress.setAddressLine1(avoidNullString(address.getStreetAddress()));
			objAddress.setAddressLine2(avoidNullString(address.getStreetAddress2()));
			objAddress.setAddressLine3(avoidNullString(address.getStreetAddress3()));
			objAddress.setCity(avoidNullString(address.getCity()));
			objAddress.setState(avoidNullString(address.getState()));
			objAddress.setZipCode(avoidNullString(address.getZipcode()));
			objAddress.setCountry(avoidNullString(address.getCountry()));
		}
		
		return objAddress;
	}
	
	/**
	 * Following method iterate credential category requirements for regulator requirements list
	 * @param courseApproval
	 * @return
	 */
	
	public List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> 
			getRegulatoryRequirementsArrayForApprovals(CourseApproval courseApproval) {
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> requirementArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement>();
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement credentialCategoryRequirement = null;
		if(courseApproval != null) {
			credentialCategoryRequirement = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement();
			List<CredentialCategoryRequirement> requirements = courseApproval.getRequirements();
			for(CredentialCategoryRequirement credentialCategoryRequirementIter : requirements) {
				requirementArray.add(getRequirementProxy(credentialCategoryRequirementIter));
			}
		}
		
		return requirementArray;
	}
	
	/**
	 * transforms model requirement to requirement proxy object
	 * @param requirement
	 * @return
	 */
	private com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement getRequirementProxy(CredentialCategoryRequirement requirement)
	{
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement requirementProxy = null;
		if(requirement != null)
		{
			requirementProxy = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement();
			requirementProxy.setId(requirement.getId());
			requirementProxy.setNumberOfHours(requirement.getNumberOfHours() == 0.0 ? 0.0 : requirement.getNumberOfHours());
			requirementProxy.setSeatTimeMeasurement(avoidNullString(requirement.getSeatTimeMeasurement()));
			requirementProxy.setReciprocity(avoidNullString(requirement.getReciprocity()));
			requirementProxy.setCourseValidation(avoidNullString(requirement.getCourseValidation()));
			requirementProxy.setCourseApprovalPeriod(avoidNullString(requirement.getCourseApprovalPeriod()));
			requirementProxy.setOnlineCEAllowed(requirement.isOnlineCEAllowed());
			requirementProxy.setCorrespondenceCEAllowed(requirement.isCorrespondenceCEAllowed());
			requirementProxy.setCourseAssessments(avoidNullString(requirement.getCourseAssessments()));
			requirementProxy.setCorrespondenceAllowedPreLicensing(requirement.isCorrespondenceAllowedPreLicensing());
			requirementProxy.setCorrespondenceAllowedPostLicensing(requirement.isCorrespondenceAllowedPostLicensing());
			requirementProxy.setReportingFees(requirement.getReportingFees() == 0.0 ? new BigDecimal(0.0) : new BigDecimal(requirement.getReportingFees()));
			requirementProxy.setCERequiresProviderApproval(requirement.isCERequiresProviderApproval());
			requirementProxy.setCredentialCategory(getCredentialCategoryForApprovals(requirement.getCredentialCategory()));
			requirementProxy.setName(avoidNullString(requirement.getName()));
			requirementProxy.setDescription(avoidNullString(requirement.getDescription()));
		}
		return requirementProxy;
	}
	
	/**
	 * Following method create Credential Category object model
	 * @param credentialCategory
	 * @return
	 */
	
	private CredentialCategory getCredentialCategoryForApprovals(
			com.softech.vu360.lms.model.CredentialCategory credentialCategory) {
		CredentialCategory objCredentialCatergory = null;
		if(credentialCategory != null) {
			objCredentialCatergory = new CredentialCategory();
			objCredentialCatergory.setId(credentialCategory.getId());
			objCredentialCatergory.setName(avoidNullString(credentialCategory.getName()));
			objCredentialCatergory.setCategoryType(avoidNullString(credentialCategory.getCategoryType()));
			objCredentialCatergory.setHours(credentialCategory.getHours() == 0.0 ? new BigDecimal(0.0) : new BigDecimal(credentialCategory.getHours()));
			objCredentialCatergory.setCompletionDeadlineMonths(credentialCategory.getCompletionDeadlineMonths());
		}
		
		return objCredentialCatergory;
	}
	
	/**
	 * Following method creates Regulatory Category list.
	 * @param courseApproval
	 * @return
	 */

	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.RegulatoryCategory> 
			getRegulatoryCategoriesArrayForApprovals(CourseApproval courseApproval) {
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.RegulatoryCategory> regulatoryCategoryArray = null;

		if(courseApproval != null) {
			regulatoryCategoryArray = new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.RegulatoryCategory>();
			List<RegulatorCategory> regCategoryList = courseApproval.getRegulatorCategories();
			RegulatoryCategory regulatorCategory = null;
			if((regCategoryList != null && regCategoryList.size() > 0)) {
				for(RegulatorCategory regulatorCategoryIter : regCategoryList) {
					regulatorCategory = new RegulatoryCategory();
					regulatorCategory.setId(regulatorCategoryIter.getId());
					regulatorCategory.getRegulator().add((getRegulatorForApprovals(regulatorCategoryIter.getRegulator())));
					regulatorCategory.setCategoryType(avoidNullString(regulatorCategoryIter.getCategoryType()));
					regulatorCategory.setCourseApprovalRequired(regulatorCategoryIter.getCourseApprovalRequired());
					regulatorCategory.setCourseApprovalPeriod(regulatorCategoryIter.getCourseApprovalPeriod());
					regulatorCategory.setCourseApprovalPeriodUnit(avoidNullString(regulatorCategoryIter.getCourseApprovalPeriodUnit()));
					regulatoryCategoryArray.add(regulatorCategory);
				}
			}
		}
		
		return regulatoryCategoryArray;
	}
	
	/**
	 * Following method creates regulator object for course approvals.
	 * @param regulator
	 * @return
	 */

	private com.softech.vu360.lms.webservice.message.storefront.courseapproval.Regulator getRegulatorForApprovals(
			Regulator regulator) {
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.Regulator objRegulator = null;
		if(regulator != null) {
			objRegulator = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.Regulator();
			objRegulator.setId(regulator.getId());
			objRegulator.setName(avoidNullString(regulator.getName()));
			objRegulator.setPhone(avoidNullString(regulator.getPhone()));
			objRegulator.setFax(avoidNullString(regulator.getFax()));
			objRegulator.setWebsite(avoidNullString(regulator.getWebsite()));
			objRegulator.setEmailAddress(avoidNullString(regulator.getEmailAddress()));
			objRegulator.setJurisdiction(avoidNullString(regulator.getJurisdiction()));
			objRegulator.setAddress(getAddressforApprovals(regulator.getAddress()));
			objRegulator.setActive(regulator.isActive());
			objRegulator.setCourseApprovalLink(avoidNullString(getCourseApprovalLink(regulator.getCustomfieldValues())));
			objRegulator.getCredentialCategory().addAll(getCredentialCategoryArray(regulator.getCredentials()));
		}
		return objRegulator;
	}
	
	/**
	 * Following method iterate course approval link for course approvals
	 * @param customFieldList
	 * @return Course Approval Link
	 */
	
	private String getCourseApprovalLink(List<CustomFieldValue> customFieldValueList) {
		
		String courseApprovalLink = null;
		if(customFieldValueList != null && customFieldValueList.size() > 0) {
			for(CustomFieldValue customFieldValue : customFieldValueList) {
				if(customFieldValue != null && customFieldValue.getCustomField() != null) {
					if(customFieldValue.getCustomField().getFieldLabel().equalsIgnoreCase("Course Approval Link")) {
						courseApprovalLink = (String) customFieldValue.getValue();
						avoidNullString(courseApprovalLink);
					}
				}
			}
		}
		
		return courseApprovalLink;
	}
	
	/**
	 * Following method iterate credential list for course approvals
	 * @param credentials
	 * @return Credential
	 */

	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential> getCredentialsArrayForApprovals(
			Credential credential) {
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential> credentialArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential>();
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential objCredential = null; 
		if(credential != null) {
			objCredential = new com.softech.vu360.lms.webservice.message.storefront.courseapproval.Credential();
			objCredential.setId(credential.getId());
			objCredential.setOfficialLicenseName(avoidNullString(credential.getOfficialLicenseName()));
			objCredential.setShortLicenseName(avoidNullString(credential.getShortLicenseName()));
			objCredential.setTotalNumberOfLicense(credential.getTotalNumberOfLicense() == 0 ? 0 : credential.getTotalNumberOfLicense());
			objCredential.setOtherJurisdiction(avoidNullString(credential.getOtherJurisdiction()));
			objCredential.setActive(credential.isActive());
			objCredential.setCredentialType(avoidNullString(credential.getCredentialType()));
			objCredential.setRenewalDeadlineType(avoidNullString(credential.getRenewalDeadlineType()));
			objCredential.setStaggeredBy(avoidNullString(credential.getStaggeredBy()));
			objCredential.setStaggeredTo(avoidNullString(credential.getStaggeredTo()));
			objCredential.setRenewalFrequency(avoidNullString(credentialRenewalFrequencyYearLabel(credential.getRenewalFrequency())));
			objCredential.setHardDeadlineMonth(avoidNullString(credential.getHardDeadlineMonth()));
			objCredential.setHardDeadlineDay(avoidNullString(credential.getHardDeadlineDay()));
			objCredential.setHardDeadlineYear(avoidNullString(credential.getHardDeadlineYear()));
			objCredential.setDescription(avoidNullString(credential.getDescription()));
			objCredential.setPreRequisite(avoidNullString(credential.getPreRequisite()));
			objCredential.getRequirements().addAll(getCredentialCategoryRequirementsByCredential(credential));
			credentialArray.add(objCredential);
		}
		return credentialArray;
	}
	
	private String credentialRenewalFrequencyYearLabel(String renewableFrequency) {
		String renewableFre = null;
		char[] renewable = renewableFrequency.toCharArray();
		if(renewableFrequency.equals("one time only")) {
			renewableFre =  renewableFrequency;
		} else if(renewableFrequency.equals("SelectRenewalFrequency")) {
			renewableFre = renewableFrequency;
		} else {
			for(int i = 0; i <= renewable.length; i++) {
				char ch = renewable[i];
				if(Character.isLetter(ch)) {
					renewableFre = renewable[0] + " Year(s)";
					break;
				}
			}
		}
		return renewableFre;
	}
	
	/**
	 * Gets credential category array against credential
	 * @param credentials
	 * @return
	 */
	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> getCredentialCategoryArray(
			List<Credential> credentials) {
		
		
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> credentialCategoryArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory>();
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory objCredentialCategory = 
				null; 
		List<com.softech.vu360.lms.model.CredentialCategory> credentialCategories = null;
		//Get credential categories from credential
		for(Credential credential : credentials) {
			credentialCategories = accreditationService.getCredentialCategoryByCredential(credential.getId());
			if(credentialCategories != null && credentialCategories.size() > 0) {
				for(com.softech.vu360.lms.model.CredentialCategory credentialCategory : credentialCategories) {
					objCredentialCategory = 
							new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory();
					
					if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.PRE_LICENSE)) {
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential))
						{
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}	
					} else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.POST_LICENSE)) {
						
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential))
						{
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}
					} else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.CONTINUING_EDUCATION)) {
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential)) {
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}
						
					} else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.CERTIFICATION)) {
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential)) {
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}
					} else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.DESIGNATION)) {
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential)) {
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}
						
					} else if(credentialCategory.getCategoryType().equals(com.softech.vu360.lms.model.CredentialCategory.PROFESSIONAL_DEVELOPMENT)) {
						if(!isAlreadyExistInCredentialCategory(credentialCategoryArray,credentialCategory,credential)) {
							credentialCategoryArray.add(addCredentialCategoryForApprovals(objCredentialCategory, credentialCategory, credential));
						}
						
					}
					
				
				}
			}
		}
		return credentialCategoryArray;
	}
	
	
	
	/**
	 * 
	 * @param proxyCredentialCategoryList
	 * @param credentialCategory
	 * @return
	 */
	private boolean isAlreadyExistInCredentialCategory(List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory> proxyCredentialCategoryList, com.softech.vu360.lms.model.CredentialCategory credentialCategory,
			com.softech.vu360.lms.model.Credential credential)
	{
		boolean isExist = false;
		if(proxyCredentialCategoryList != null && proxyCredentialCategoryList.size() > 0 && credentialCategory != null)
		{
			for(CredentialCategory category : proxyCredentialCategoryList)
			{
				if(category.getCategoryType().equals(credentialCategory.getCategoryType()))
					{
						category.getCredentials().addAll(getCredentialsArrayForApprovals(credential));
						isExist = true;
					}
					
			}
		}
		return isExist;
	}
	
	
	private com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory addCredentialCategoryForApprovals(com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategory 
			objCredentialCategory, com.softech.vu360.lms.model.CredentialCategory credentialCategory, Credential credential) {
		
		objCredentialCategory.getCredentials().addAll(getCredentialsArrayForApprovals(credential));
		objCredentialCategory.setId(credentialCategory.getId());
		objCredentialCategory.setName(avoidNullString(credentialCategory.getName()));
		if(new BigDecimal(credentialCategory.getHours()) == null) {
			objCredentialCategory.setHours(new BigDecimal(0.0));
		} else {
			objCredentialCategory.setHours(BigDecimal.valueOf(credentialCategory.getHours()));
		}
		
		if(credentialCategory.getCompletionDeadlineMonths() == 0.0) {
			objCredentialCategory.setCompletionDeadlineMonths(0.0);
		} else {
			objCredentialCategory.setCompletionDeadlineMonths(credentialCategory.getCompletionDeadlineMonths());
		}
		objCredentialCategory.setCategoryType(avoidNullString(credentialCategory.getCategoryType()));
		
		return objCredentialCategory;
	}
	
	
	
	/**
	 * Get credential category requirements for credential category
	 * @param credentialCategory
	 * @return
	 */
	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> getCredentialCategoryReqArray(
			com.softech.vu360.lms.model.CredentialCategory credentialCategory) {
		
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> credentialCategoryReqArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement>();
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement objCredentialCategoryReq = 
				new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement(); 
		
		//Get credential categories from credential
		List<com.softech.vu360.lms.model.CredentialCategoryRequirement> credentialCategoriesReq = accreditationService.getCredentialCategoryRequirementsByCategory(credentialCategory.getId());
		if(credentialCategoriesReq != null && credentialCategoriesReq.size() > 0)
		{
			for(com.softech.vu360.lms.model.CredentialCategoryRequirement credentialCategoryReq : credentialCategoriesReq) {
				credentialCategoryReqArray.add(getRequirementProxy(credentialCategoryReq));
			}
		}
		return credentialCategoryReqArray;
	}
	
	/**
	 * Get credential category requirements for credential category
	 * @param credential
	 * @return
	 */
	private List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> getCredentialCategoryRequirementsByCredential(
			com.softech.vu360.lms.model.Credential credential) {
		
		List<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement> credentialCategoryReqArray = 
				new ArrayList<com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement>();
		
		com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement objCredentialCategoryReq = 
				new com.softech.vu360.lms.webservice.message.storefront.courseapproval.CredentialCategoryRequirement(); 
		
		//Get credential categories from credential
		List<com.softech.vu360.lms.model.CredentialCategoryRequirement> credentialCategoriesReq = accreditationService.getCredentialCategoryRequirementsByCredential(credential.getId());
		if(credentialCategoriesReq != null && credentialCategoriesReq.size() > 0)
		{
			for(com.softech.vu360.lms.model.CredentialCategoryRequirement credentialCategoryReq : credentialCategoriesReq) {
				credentialCategoryReqArray.add(getRequirementProxy(credentialCategoryReq));
			}
		}
		return credentialCategoryReqArray;
	}

	/**
	 * @return the orderCreatedEventService
	 */
	public OrderCreatedEventService getOrderCreatedEventService() {
		return orderCreatedEventService;
	}

	/**
	 * @param orderCreatedEventService the orderCreatedEventService to set
	 */
	public void setOrderCreatedEventService(
			OrderCreatedEventService orderCreatedEventService) {
		this.orderCreatedEventService = orderCreatedEventService;
	}
	
	// [1/11/2011] LMS-8560 :: Synchronous Sessions are not publishing on StoreFront
	List<CourseDetails> getCourseDetails(List<String> courseGUIDList) {
		
		List<Course> courseList = getCourses(courseGUIDList);
		List<InstructorSynchronousClass> instructorList = null;
		SectionInfo sectionInfo = null ; 
		List<CourseDetails> coursePublishInfo = new ArrayList<CourseDetails>();
		CourseDetails courseDetails = null ;
		
		if(courseList !=null ) {
			for(Course crs : courseList){
				courseDetails = new CourseDetails() ;
				courseDetails.setCourseGUID( crs.getCourseGUID()); 
				
				List<SynchronousClass> synClassList = this.synchronousClassService.getSynchronousClassesForEnrollment(crs.getId(), 0, true);
				if (synClassList != null && synClassList.size() > 0) {					
					for (SynchronousClass synClass : synClassList) {
						
						// Set Class Information
						sectionInfo = new SectionInfo();
						sectionInfo.setSectionID(synClass.getGuid());
						sectionInfo.setSectionName(avoidNullString( synClass.getSectionName()));						
						sectionInfo.setAvailableSeats( BigInteger.valueOf((synClass.getMaxClassSize() == null) ? 0 : synClass.getMaxClassSize()) );
						sectionInfo.setSectionStatus( avoidNullString(synClass.getClassStatus()) );
						sectionInfo.setSectionMeetingType(avoidNullString(synClass.getMeetingType()));						
						sectionInfo.setSectionEnrollmentCloseDate( convertDateToXMLGregorianCalender( avoidNullDate(synClass.getEnrollmentCloseDate())).toString() );
						sectionInfo.setSectionStartDate( convertDateToXMLGregorianCalender( avoidNullDate(synClass.getClassStartDate()) ).toString());
						sectionInfo.setSectionEndDate( convertDateToXMLGregorianCalender( avoidNullDate(synClass.getClassEndDate()) ).toString());
						
						// Set Session Information
						for (SynchronousSession session : synClass.getSynchronousSessions()) {
							
							SessionInfo sessionInfo = new SessionInfo();
							sessionInfo.setSessionEndDateTime ( convertDateToXMLGregorianCalender(avoidNullDate(session.getEndDateTime())).toString());							sessionInfo.setSessionID(session.getId().toString());
							sessionInfo.setSessionStartDateTime( convertDateToXMLGregorianCalender( avoidNullDate(session.getStartDateTime())).toString());
							sectionInfo.getSessions().add(sessionInfo) ;
						}
						
						// Set Instructor Information
						instructorList = getInstructorList(synClass.getId());						
						for( InstructorSynchronousClass synchInst :  instructorList){							
							Instructor instructor = synchInst.getInstructor();							
							InstructorInfo instructorInfo = new InstructorInfo();
							instructorInfo.setInstructorFirstName(avoidNullString(instructor.getFirstName() ));
							instructorInfo.setInstructorID(avoidNullString(instructor.getId().toString()));
							instructorInfo.setInstructorLastName(avoidNullString( instructor.getLastName()));
							instructorInfo.setInstructorRole(avoidNullString(synchInst.getInstructorType()));							
							sectionInfo.getInstructors().add(instructorInfo) ;
						}
						courseDetails.getSynchronousClass().add(sectionInfo);
					}
				}
				coursePublishInfo.add(courseDetails) ;
			}
		}
		return coursePublishInfo;
	}

	List<Course> getCourses(List<String> courseGUIDList){
		List<Course> courseList = null ;
		
		if( courseGUIDList != null ){
			Vector guidList = new Vector( courseGUIDList );
		    courseList = synchronousClassService.getCourseByGUID(guidList);
		}
		return courseList ; 
	}
	
	List<SynchronousClass> getSynchrousClassess(Long courseId ) {
		List<SynchronousClass> synchronousClassList =null; 
		synchronousClassList = synchronousClassService.getAllSynchClassesOfCourse(courseId) ; 
		return synchronousClassList ; 
	}
	
	List<SynchronousSession> getClassSessions(Long classId) {
		List<SynchronousSession> synchronousSessionList =null; 
		synchronousSessionList = synchronousClassService.getAllClassSessionsOfSynchClass(classId) ; 
		return synchronousSessionList ; 
	}
	
	 List<InstructorSynchronousClass> getInstructorList(Long classId){
		List<InstructorSynchronousClass> instructorList =null; 
		instructorList = synchronousClassService.getAllInstructorsOfSynchClass(classId) ; 
		return instructorList ; 
	 }
	 
	 /**
	  * Following method get java.util.Date object and returns XMLGregorianCalenger object for xml parsing.
	  * @param dt
	  * @return
	  */

	 XMLGregorianCalendar  convertDateToXMLGregorianCalender(Date dt) {
			GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
			gc.setTime(dt); 
			
			DatatypeFactory dataTypeFactory = null;
			try {
			dataTypeFactory = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException ex) {
				log.debug("Date conversion error");
			//Logger.getLogger(InstallmentBean.class.getName()).log(Level.SEVERE, null, ex);
			}
			XMLGregorianCalendar value = dataTypeFactory.newXMLGregorianCalendar(gc);
			return value;
	 }
	 
	 /**
	  * Following method check string value is null, if it's null then set as an empty value.	 
	  * @param str
	  * @return
	  */
	 private String avoidNullString(String str) {
		 if( str == null)
			 return "" ;
		 else
			 return str ;
	 }
	 
	 /**
	  * Following method check java.util.Date object as a null, if it's null then create new date object.
	  * @param dt
	  * @return
	  */
	 private Date avoidNullDate(Date dt) {
	 	if( dt == null)
			return new Date();
		else
			return dt ;
	 }
	 
	/**
	 * @return the synchronousClassService
	 */
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	/**
	 * @param synchronousClassService the synchronousClassService to set
	 */
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
	
	
}
