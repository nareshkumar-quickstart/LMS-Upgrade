package com.softech.vu360.lms.web.controller.administrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageOrganizationalGroups;
import com.softech.vu360.lms.web.controller.model.Menu;
import com.softech.vu360.lms.web.controller.model.UserForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.UserValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Ashis
 *
 */
public class ManageUserController extends VU360BaseMultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageUserController.class.getName());
	private static final String MANAGE_USER_DEFAULT_ACTION = "default";
	private static final String MANAGE_USER_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_USER_ALL_SEARCH_ACTION = "allSearch";
	private static final String MANAGE_USER_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_USER_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_USER_PAGE_SIZE = 10;
	private static final String MANAGE_USER_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String EDIT_USER_LEARNER_UPDATE_ACTION = "update";
	private static final String EDIT_USER_LEARNER_RESETPASSWORD_ACTION = "resetpassword";
	private static final String MANAGE_USER_DELETE_LEARNER_ACTION = "delete";
	private static final String MANAGE_USER_SORT_LEARNER_ACTION = "sort";
	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String EDIT_USER_CHANGEGROUP_SAVE="save";
	private String failureTemplate = null;
	private JavaMailSenderImpl mailSender;
	private VelocityEngine velocityEngine;
	private String manageLearnerTemplate = null;
	private String editUserTemplate = null;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private String loginTemplate = null;
	private String editDisplayLearnerChangeGroupTemplate = null;
	private String managerTemplate = null;
	private String redirectToSearchPageTemplate = null;
	private String closeTemplate = null;

	//private String errorMessage = null;
	private String redirectToOtherPageIfPermisionRevokedTemplate = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private CustomFieldService customFieldService=null;
	private EnrollmentService enrollmentService;
	private EntitlementService entitlementService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private ActiveDirectoryService activeDirectoryService;
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	public ManageUserController() {
		super();
	}

	public ManageUserController(Object delegate) {
		super(delegate);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView searchLearner(HttpServletRequest request,
			HttpServletResponse response,Object command, BindException errors )throws Exception {
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			HttpSession session = request.getSession(true);
			session.setAttribute("featureGroup", "Users");

			//List<VU360User> resultList = new ArrayList<VU360User>();
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			String error = request.getParameter("error");
			error = (error==null)?null:error;
			String action = request.getParameter("action");
			action = (action==null)?MANAGE_USER_DEFAULT_ACTION:action;
			@SuppressWarnings("unused")
			boolean customerSelected = false ;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if( error != null ) {
				if( error.equalsIgnoreCase("customerSelection") ){
					context.put("customerSelection", "NOT SET");
					context.put("totalRecord", 0);
					customerSelected = false ;
					return new ModelAndView(manageLearnerTemplate, "context", context);
				}
			}
			if( ! action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION) ) { 
				//if its a not a search , then go ahead ,else check for customer selection

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();

					if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
						if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
							context.put("customerSelection", "NOT SET");
							context.put("totalRecord", 0);
							customerSelected = false ;
							return new ModelAndView(manageLearnerTemplate, "context", context);
						}
						else {
							context.put("customerSelection", "SET");
							customerSelected = true ;
						}	
					}
				}
			} else {
				context.put("customerSelection", "SET");
				customerSelected = true ;
			}
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//VU360User loggedInUserObj = (VU360User) vu360UserService.loadForUpdateVU360User(loggedInUser.getId());
			List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
			
			if( !loggedInUser.isLMSAdministrator() ) {
				if(!UserPermissionChecker.hasAccessToFeature("LMS-MGR-0001", loggedInUser, request.getSession(true))){
					Menu menu = new Menu(loggedInUser,request);
					context.put("menu", menu);
					return new ModelAndView(redirectToOtherPageIfPermisionRevokedTemplate,"context",context);
				}
			}
			context.put("userData", loggedInUser);

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress= request.getParameter("emailaddress");
			String searchKey = request.getParameter("searchkey");
			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String sortDirection = request.getParameter("sortDirection");
			String prevSortDirection = "";
			String sortBy = request.getParameter("sortBy");

			int pageNo = 0;
			int recordShowing = 0;

			firstName = (firstName==null)?"":firstName;
			lastName = (lastName==null)?"":lastName;
			emailAddress = (emailAddress==null)?"":emailAddress;
			searchKey = (searchKey==null)?"":searchKey;
			if( sortDirection == null )
				sortDirection="0";

			session.setAttribute("feature","LMS-ADM-0002");

			if( sortBy == null  && action.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION)== false && action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)== false ){

				session.setAttribute("searchedFirstName", firstName);
				session.setAttribute("searchedLastName", lastName);
				session.setAttribute("searchedEmailAddress", emailAddress);
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo",pageNo);
			}
			direction = (direction==null)?MANAGE_USER_PREVPAGE_DIRECTION:direction;
			pageIndex = (pageIndex==null)?"0":pageIndex;
			sortDirection = (sortDirection==null)?"0":sortDirection;
			sortBy = (sortBy==null)?MANAGE_USER_SORT_FIRSTNAME:sortBy;

			session.setAttribute("sortBy", sortBy);
			session.setAttribute("direction", direction);
			session.setAttribute("sortDirection", sortDirection);

			if( action.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {

				session.setAttribute("searchType", MANAGE_USER_SIMPLE_SEARCH_ACTION);

				if( session.getAttribute("prevSortDirection") != null )
					prevSortDirection = session.getAttribute("prevSortDirection").toString();
				else
					prevSortDirection = "1";

				if( direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION) ) {

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;

				}else if( direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION) ) {

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
				}
				session.setAttribute("pageNo",pageNo);
				results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(prevSortDirection));
				userList=(List<VU360User>)results.get("list");

			}else if( action.equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION) ) {

				session.setAttribute("searchType", MANAGE_USER_ADVANCED_SEARCH_ACTION);
				if( session.getAttribute("prevSortDirection") != null )
					prevSortDirection = session.getAttribute("prevSortDirection").toString();
				else
					prevSortDirection = "1";
				sortDirection = prevSortDirection;

				if(direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION)){

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					session.setAttribute("prevAction", "nextPrev");

				}else if(direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION)){

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					session.setAttribute("prevAction", "nextPrev");
				}
				session.setAttribute("pageNo",pageNo);	
				results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
						session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(prevSortDirection));
				userList = (List<VU360User>)results.get("list");

			}else if( action.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION) ) {

				session.setAttribute("searchType", MANAGE_USER_ALL_SEARCH_ACTION);
				pageNo = 0;
				session.setAttribute("pageNo",pageNo);	
				Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				String showAllLimit = brander.getBrandElement("lms.resultSet.showAll.Limit") ;
				int intShowAllLimit = Integer.parseInt(showAllLimit.trim());

				results = learnerService.findAllLearnersByCustomer(session.getAttribute("searchedFirstName").toString(),
						session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						sortBy,Integer.parseInt(sortDirection) , intShowAllLimit);
				userList = (List<VU360User>)results.get("list");

			}else if( action.equalsIgnoreCase(MANAGE_USER_SORT_LEARNER_ACTION) ) {

				if( session.getAttribute("prevAction")!= null && session.getAttribute("prevAction").toString().equalsIgnoreCase("nextPrev") ) {

					if( sortDirection.equalsIgnoreCase("0") ) sortDirection = "1";
					else sortDirection = "0";
					session.setAttribute("prevAction", "sort");
				}

				if(session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION)){

					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
							session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else if(session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION)){

					results = learnerService.findAllLearners("",
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else{
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
				}
				session.setAttribute("prevSortDirection",sortDirection);
				if( sortDirection.equalsIgnoreCase("0") ) sortDirection = "1";
				else sortDirection = "0";

			}else if( action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) ) {

				String[] selectedUserValues = request.getParameterValues("selectedLearners");

				if( selectedUserValues != null ) {
					long userIdArray[] = new long[selectedUserValues.length];
					for (int loop = 0; loop < selectedUserValues.length; loop++) {
						String userID = selectedUserValues[loop];
						if (userID != null)
							userIdArray[loop] = Long.parseLong(userID);
					}
					learnerService.InactiveUsers(userIdArray);
				}
				if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION)){

					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION)){

					results = learnerService.findAllLearners("",
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else{
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
				}
				if( sortDirection.equalsIgnoreCase("0") )
					sortDirection = "1";
				else
					sortDirection="0";

			}
			else if(action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION)){
				sortDirection = "1";
				pageNo = 0;
				session.setAttribute("searchType","");
				request.setAttribute("newPage","true");
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
					VU360UserAuthenticationDetails det = (VU360UserAuthenticationDetails)auth.getDetails();
					if(det.getCurrentCustomer().getCustomerType().equalsIgnoreCase("b2c")){
						results =learnerService.findAllLearners("", 
								loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								"firstName",5);
						userList = (List<VU360User>)results.get("list");
						context.put("b2cRecord", userList.size());
						userList.clear();
					}
				}
			}else{
				request.setAttribute("newPage","true");
			}
			if( !results.isEmpty() )

				recordShowing = ((Integer)userList.size()<MANAGE_USER_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(session.getAttribute("pageNo").toString())+1)*MANAGE_USER_PAGE_SIZE;		
				if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
					recordShowing=userList.size();
				String totalRecord=	(results.isEmpty())?"-1":results.get("recordSize").toString();
				context.put("firstName", session.getAttribute("searchedFirstName"));
				context.put("lastName", session.getAttribute("searchedLastName"));
				context.put("emailAddress", session.getAttribute("searchedEmailAddress"));
				context.put("searchKey", session.getAttribute("searchedSearchKey"));
				context.put("searchType", session.getAttribute("searchType"));
				context.put("direction", direction);
				context.put("members", userList);
				context.put("totalRecord", Integer.parseInt(totalRecord));
				context.put("recordShowing", recordShowing);
				context.put("pageNo", session.getAttribute("pageNo"));
				context.put("sortDirection", sortDirection );
				context.put("sortBy", sortBy);

				return new ModelAndView(manageLearnerTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageLearnerTemplate);
	}

	/*
	 * Change name to ViewLearnerInfo
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView displayEditLearner(HttpServletRequest request, HttpServletResponse response ,Object command, BindException errors )throws Exception {
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			HttpSession session = request.getSession(true);
			session.setAttribute("featureGroup", "Users");

			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String userId = request.getParameter("Id");
			VU360User vu360User = null;
			if( StringUtils.isNotBlank(userId) ) {
				vu360User = vu360UserService.loadForUpdateVU360User(Long.valueOf(userId));
			}
			else
				return new ModelAndView(manageLearnerTemplate);
			/*Map<String,String> roleTypeForCombo=new HashMap<String,String>();
			for(LMSRole userRole :loggedInUser.getLmsRoles()){
				//roleType.put(userRole.getRoleType(), userRole.getRoleType());
				roleTypeForCombo.put(userRole.getKey(), userRole.getRoleType());
			}
			--Edit role should noty be allowed from learner edit interface as there is a chance user may have multiple role

			context.put("roleTypeForCombo", roleTypeForCombo);
			 */

			UserForm form=(UserForm)command;
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			if(vu360User !=null && vu360User.getLearner().getCustomer().getId().longValue() == customer.getId().longValue()){

				context.put("userData", vu360User);
				String actionType=request.getParameter("action");
				log.debug("Action" + actionType);

				if(StringUtils.isNotBlank(actionType)){
					if(actionType.equalsIgnoreCase(EDIT_USER_LEARNER_UPDATE_ACTION)){

						//Validate Custom Fields

						if(form.getCustomFields().size()>0){
							UserValidator validate=new UserValidator();
							validate.validateCustomFieldsList(form.getCustomFields(), errors);
						}

						if(this.validateData(request,context,vu360User ) || errors.getAllErrors().size()>0) {
							context.put("vu360User", vu360User);
							context.put("errors", errors);
							return new ModelAndView(editUserTemplate, "context", context);
						}
						else {
							this.updateLearner(request, response, command, errors);
							Map<Object, Object> newcontext = new HashMap<Object, Object>();
							newcontext.put("firstName", session.getAttribute("searchedFirstName"));
							newcontext.put("lastName", session.getAttribute("searchedLastName"));
							newcontext.put("emailAddress", session.getAttribute("searchedEmailAddress"));
							newcontext.put("searchKey", session.getAttribute("searchedSearchKey"));
							newcontext.put("searchType", session.getAttribute("searchType"));
							newcontext.put("direction", session.getAttribute("direction"));

							if(session.getAttribute("direction").toString().equalsIgnoreCase("next")){
								newcontext.put("pageNo", Integer.parseInt(session.getAttribute("pageNo").toString()) -1);
							}else{
								newcontext.put("pageNo", session.getAttribute("pageNo"));
							}

							newcontext.put("sortDirection", session.getAttribute("sortDirection"));
							newcontext.put("sortBy", session.getAttribute("sortBy"));
							
							/*--------------------START: ADD ABILITY TO SAVE REPORTING FILED VALUE--------------------------*/		  
							if(form.getCreditReportingFields().size()>0){
								for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : form.getCreditReportingFields()){

									if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){

										List<CreditReportingFieldValueChoice> selectedChoiceList = new ArrayList<CreditReportingFieldValueChoice>();

										if (((MultiSelectCreditReportingField) field.getCreditReportingFieldRef()).isCheckBox()){

											for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice  customFieldValueChoice : field.getCreditReportingFieldValueChoices()){
												if(customFieldValueChoice.isSelected()){
													selectedChoiceList.add(customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
												}
											}

										}else{

											if(field.getSelectedChoices()!=null){

												Map<Long,CreditReportingFieldValueChoice> totalChoiceMap = new HashMap<Long,CreditReportingFieldValueChoice>();

												for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice  customFieldValueChoice : field.getCreditReportingFieldValueChoices()){
													totalChoiceMap.put(customFieldValueChoice.getCreditReportingFieldValueChoiceRef().getId(), customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
												}

												for(String selectedChoiceIdString : field.getSelectedChoices()){
													if(totalChoiceMap.containsKey(new Long(selectedChoiceIdString))){														
														selectedChoiceList.add((totalChoiceMap.get(new Long(selectedChoiceIdString))));
													}
												}

											}
										}

										CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
										creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
										creditReportingFieldValue.setCreditReportingValueChoices(selectedChoiceList);
										creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());

										learnerService.saveCreditReportingfieldValue(creditReportingFieldValue);

									}else{
										CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
										creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
										creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());

										learnerService.saveCreditReportingfieldValue(creditReportingFieldValue);

									}

								}
							}
					/*--------------------END: ADD ABILITY TO SAVE REPORTING FILED VALUE-------------------------*/

							return new ModelAndView(redirectToSearchPageTemplate, "context", newcontext);
						}

					}else if(actionType.equalsIgnoreCase(EDIT_USER_LEARNER_RESETPASSWORD_ACTION )){
						String newPassword=GUIDGeneratorUtil.generatePassword();
						vu360User.setPassword(newPassword);
						vu360UserService.changeUserPassword(vu360User.getId(), vu360User);
						Map<String, Object> model = new HashMap<>();
						vu360User.setPassword(newPassword);
						model.put("user", vu360User);
						Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
						
						VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
						if(adminDetails.getCurrentSearchType().equals(AdminSearchType.CUSTOMER)){
							brander=VU360Branding.getInstance().getBranderByUser(request,loggedInUserVO);
						}
						
						String resetPassWordTemplatePath =  brander.getBrandElement("lms.email.resetPassWord.body");
						String fromAddress =  brander.getBrandElement("lms.email.resetPassWord.fromAddress");
						String fromCommonName =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						String subject =  brander.getBrandElement("lms.email.resetPassWord.subject");
						String support =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						model.put("support", support);
						String lmsDomain=VU360Properties.getVU360Property("lms.domain");
						model.put("lmsDomain",lmsDomain);
						model.put("brander", brander);
						
						/*START- BRANDNG EMAIL WORK*/
						String templateText =  brander.getBrandElement("lms.branding.email.passwordUpdated.templateText");						
						String loginurl= lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());				
						templateText= templateText.replaceAll("&lt;firstname&gt;", vu360User.getFirstName());
						templateText= templateText.replaceAll("&lt;lastname&gt;", vu360User.getLastName());						 						
						templateText= templateText.replaceAll("&lt;loginurl&gt;", loginurl);
						templateText= templateText.replaceAll("&lt;phone&gt;", "");
						templateText= templateText.replaceAll("&lt;support&gt;", support);
						templateText= templateText.replaceAll("&lt;customername&gt;", vu360User.getLearner().getCustomer().getName());
						model.put("templateText", templateText);			
						/*END-BRANDING EMAIL WORK*/
						String text = VelocityEngineUtils.mergeTemplateIntoString(
								velocityEngine, resetPassWordTemplatePath, model);

						SendMailService.sendSMTPMessage(vu360User.getEmailAddress(), 
								fromAddress, fromCommonName, subject, text); 
						context.put("vu360User", vu360User);						
						return new ModelAndView(editUserTemplate, "context", context);
					}else if(actionType.equalsIgnoreCase("cancel")){
						return new ModelAndView(managerTemplate);
					}

				}
				else{ 

					//For Custom Fields
					List<CustomField> distCustomFieldList=vu360User.getLearner().getCustomer().getDistributor().getCustomFields();
					List<CustomField> myCustomFieldList=vu360User.getLearner().getCustomer().getCustomFields();
					if(distCustomFieldList!=null)
						myCustomFieldList.addAll(vu360User.getLearner().getCustomer().getDistributor().getCustomFields());

					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					Distributor reseller = vu360User.getLearner().getCustomer().getDistributor();
					CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
					customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
					totalCustomFieldList.addAll(customer.getCustomFields());
					totalCustomFieldList.addAll(reseller.getCustomFields());

					Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

					for(CustomField customField:totalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> customFieldValueChoices=customFieldService.getOptionsByCustomField(customField);
							fieldBuilder2.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

							if (customField instanceof MultiSelectCustomField){
								CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
								existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
							}

						}else {
							fieldBuilder2.buildCustomField(customField,0,customFieldValues);
						}
					}

					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList2 =fieldBuilder2.getCustomFieldList();

					for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList2){
						if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
							Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

							for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
								tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
							}

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
								if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(true);
								}
							}
						}
					}

					form.setVu360User(vu360User);
					form.setCustomFields(customFieldList2);
					/*------------------------------START: DISPLAY REPORTING FIELDS------------------*/
					CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
					List<CreditReportingField> customFieldList=learnerService.getCreditReportingFieldsByLearner(vu360User.getLearner());
					List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(vu360User.getLearner());

					Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();

					for(CreditReportingField creditReportingField : customFieldList){
					  if(!(creditReportingField instanceof StaticCreditReportingField)){	
						if (creditReportingField instanceof SingleSelectCreditReportingField || 
								creditReportingField instanceof MultiSelectCreditReportingField) {

							List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = learnerService.getChoicesByCreditReportingField(creditReportingField);
							fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList,creditReportingFieldValueOptionList);

							if(creditReportingField instanceof MultiSelectCreditReportingField){
								CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
								existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
							}

						} else {
							fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList);
						}						
					 }
					}

					List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder.getCreditReportingFieldList();

					for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields){
						if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
							List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field.getCreditReportingFieldRef().getId());
							Map<Long,CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long,CreditReportingFieldValueChoice>();

							for (CreditReportingFieldValueChoice choice : existingChoices){
								existingChoicesMap.put(choice.getId(), choice);
							}

							for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field.getCreditReportingFieldValueChoices()){
								if(existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())){
									tempChoice.setSelected(true);
								}
							}
						}
					}
					form.setCreditReportingFields(creditReportingFields);
					/*------------------------------END: DISPLAY REPORTING FIELDS-------------------*/
					context.put("vu360User", vu360User);
					return new ModelAndView(editUserTemplate, "context", context);
				}
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(loginTemplate);
	}
	
	private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingField,
			List<CreditReportingFieldValue> creditReportingFieldValues){
		if (creditReportingFieldValues != null){
			for (CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues){
				if (creditReportingFieldValue.getReportingCustomField()!=null){
					if (creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId())==0){
						return creditReportingFieldValue;
					}
				}
			}
		}
		return new CreditReportingFieldValue();
	}
	
	private boolean validateData(HttpServletRequest request,Map<Object, Object> context,VU360User vu360User){

		if(request.getParameter("validate").equalsIgnoreCase("donotValidate") ) {
			// no need to validate
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCountry(request.getParameter("cmbCountry2"));
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCountry(request.getParameter("cmbCountry"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(request.getParameter("zipcode"));
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2"));

			vu360User.setEmailAddress(request.getParameter("emailAddress"));
			vu360User.setFirstName(request.getParameter("firstName"));
			vu360User.setMiddleName(request.getParameter("middleName"));
			vu360User.setLastName(request.getParameter("lastName"));
			vu360User.getLearner().getLearnerProfile().setOfficePhone(request.getParameter("officePhone"));
			vu360User.getLearner().getLearnerProfile().setOfficePhoneExtn(request.getParameter("officePhoneExtn").trim());
			vu360User.getLearner().getLearnerProfile().setMobilePhone(request.getParameter("mobilePhone"));
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress(request.getParameter("streetAddress1"));
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress2(request.getParameter("streetAddress2"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCity(request.getParameter("city"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(request.getParameter("zipcode"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCity(request.getParameter("city2"));
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2"));

			vu360User.setUsername(request.getParameter("userName"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress(request.getParameter("streetAddress2a"));

			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress2(request.getParameter("streetAddress2b"));

			vu360User.setPassword(request.getParameter("password"));

			return true ;
		}
		boolean check = false;
		// get the brand object 
		Brander brand = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		// for country value 
		vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCountry(request.getParameter("cmbCountry2"));
		vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCountry(request.getParameter("cmbCountry"));

		vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(request.getParameter("zipcode"));
		vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2"));


		if(StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCountry()) && StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCountry()) ) {
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCountry(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCountry().trim());
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCountry(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCountry().trim());
		}	
		if(StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode()) && StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode()) ) {
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode().trim());
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode().trim());
		}

		if( brand != null)	{
			String country = null ;
			String zipCode = null ;
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------

			country = vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCountry();
			zipCode = vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode();

			if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brand, log) ) {
				log.debug("ZIP CODE FAILED" );
				check=true;
				context.put("validateZipcode", ZipCodeValidator.getCountryZipCodeError(country, brand));
			}				

			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCountry();
			zipCode = vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode();

			if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brand , log) ) {
				log.debug("ZIP CODE FAILED" );
				check=true;
				context.put("validateZipcode2", ZipCodeValidator.getCountryZipCodeError(country, brand));
			}	
		}
		if (StringUtils.isBlank(request.getParameter("emailAddress")))
		{
			context.put("validateEmailAddress", "error.addNewLearner.email.required");
			vu360User.setEmailAddress(null);
			check=true;
		}
		else if(!FieldsValidation.isEmailValid(request.getParameter("emailAddress"))){
			context.put("validateEmailAddress", "error.addNewLearner.email.invalidformat");
			vu360User.setEmailAddress(null);
			check=true;
		}
		else{
			vu360User.setEmailAddress(request.getParameter("emailAddress"));
		}

		if (StringUtils.isBlank(request.getParameter("firstName"))){
			context.put("validateFirstName", "error.addNewLearner.firstName.required");
			vu360User.setFirstName(null);
			check=true;
		}
		else if (FieldsValidation.isInValidGlobalName(request.getParameter("firstName"))){
			context.put("validateFirstName", "error.addNewLearner.firstName.all.invalidText");
			vu360User.setFirstName(null);
			check=true;
		}
		else{
			vu360User.setFirstName(request.getParameter("firstName"));
		}

		/*if (StringUtils.isBlank(request.getParameter("middleName"))){
			context.put("validateMiddleName", "error.manageUser.editLearner.middleName.required");
			vu360User.setMiddleName(null);
			check=true;
		}
		else*/
		if (FieldsValidation.isInValidGlobalName(request.getParameter("middleName"))){
			context.put("validateMiddleName", "error.addNewLearner.middleName.all.invalidText");
			vu360User.setMiddleName(null);
			check=true;
		}
		else{
			vu360User.setMiddleName(request.getParameter("middleName"));
		}


		if (StringUtils.isBlank(request.getParameter("lastName"))){
			context.put("validateLastName", "error.addNewLearner.lastName.required");
			vu360User.setLastName(null);
			check=true;
		}
		else if (FieldsValidation.isInValidGlobalName(request.getParameter("lastName"))){
			context.put("validateLastName", "error.addNewLearner.lastName.all.invalidText");
			vu360User.setLastName(null);
			check=true;
		}
		else{
			vu360User.setLastName(request.getParameter("lastName"));
		}


		if (!StringUtils.isBlank(request.getParameter("officePhone").trim())){
			if (FieldsValidation.isInValidOffPhone(request.getParameter("officePhone").trim())){
				context.put("validateOfficePhone", "error.addNewLearner.officePhone.all.invalidText");
				vu360User.getLearner().getLearnerProfile().setOfficePhone(null);
				check=true;
			}else{
				vu360User.getLearner().getLearnerProfile().setOfficePhone(request.getParameter("officePhone"));
			}
		}
		if (!StringUtils.isBlank(request.getParameter("officePhoneExtn").trim())){
			if (FieldsValidation.isInValidMobPhone((request.getParameter("officePhoneExtn").trim()))){
				context.put("validateOfficePhoneExtn", "error.addNewLearner.officePhoneExtn.all.invalidText");
				vu360User.getLearner().getLearnerProfile().setOfficePhoneExtn(null);
				check=true;

			}
			else
				vu360User.getLearner().getLearnerProfile().setOfficePhoneExtn(request.getParameter("officePhoneExtn"));
		}
		if (!StringUtils.isBlank(request.getParameter("mobilePhone"))){
			if (FieldsValidation.isInValidMobPhone(request.getParameter("mobilePhone"))){
				context.put("validateMobilePhone", "error.addNewLearner.mobilePhone.all.invalidText");
				vu360User.getLearner().getLearnerProfile().setMobilePhone(null);
				check=true;
			}
			else
				vu360User.getLearner().getLearnerProfile().setMobilePhone(request.getParameter("mobilePhone"));
		}


		if (!StringUtils.isBlank(request.getParameter("streetAddress1"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("streetAddress1"))){
				context.put("validateStreetAddress1", "error.addNewLearner.streetAddress1.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress(null);
				check=true;
			}
			else
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress(request.getParameter("streetAddress1"));

		}
		if (!StringUtils.isBlank(request.getParameter("streetAddress2"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("streetAddress2"))){
				context.put("validateStreetAddress2", "error.addNewLearner.streetAddress1.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress2(null);
				check=true;
			}
			else
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress2(request.getParameter("streetAddress2"));

		}
		if (!StringUtils.isBlank(request.getParameter("city"))){
			if (FieldsValidation.isInValidGlobalName(request.getParameter("city"))){
				context.put("validateCity", "error.addNewLearner.city1.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCity(null);
				check=true;
			}
			else{
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCity(request.getParameter("city"));
			}
		}
		//for address2 validation 04/03/2009

		if (!StringUtils.isBlank(request.getParameter("city2"))){
			if (FieldsValidation.isInValidGlobalName(request.getParameter("city2"))){
				context.put("validateCity2", "error.addNewLearner.city2.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCity(null);
				check=true;
			}
			else{
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCity(request.getParameter("city2"));
			}
		}

		if (StringUtils.isBlank(request.getParameter("userName")))
		{
			context.put("validateUsername", "error.addNewLearner.userName.required");
			vu360User.setUsername(null);
			check=true;
		}else if(vu360User.getUsername().compareToIgnoreCase(request.getParameter("userName"))!=0){

			if (activeDirectoryService.findADUser(request.getParameter("userName"))) {
				context.put("validateUsername", "error.addNewUser.AD.existUsername");
				vu360User.setUsername(null);
				check=true;
			}
			
			else if (vu360UserService.findUserByUserName(request.getParameter("userName"))!=null) {
				log.debug("Size of list");
				context.put("validateUsername", "error.addNewLearner.username.all.existUsername");
				vu360User.setUsername(null);
				check=true;
			}

		}else{
			vu360User.setUsername(request.getParameter("userName"));
		}
		if (!StringUtils.isBlank(request.getParameter("streetAddress2a"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("streetAddress2a"))){
				context.put("validateStreetAddress2a", "error.addNewLearner.streetAddress2.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress(null);
				check=true;
			}
			else
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress(request.getParameter("streetAddress2a"));

		}
		if (!StringUtils.isBlank(request.getParameter("streetAddress2b"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("streetAddress2b"))){
				context.put("validateStreetAddress2b", "error.addNewLearner.streetAddress2.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress2(null);
				check=true;
			}
			else
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress2(request.getParameter("streetAddress2b"));

		}
		String newPassword=request.getParameter("password");
		if (newPassword !=null)
			if (!newPassword.trim().isEmpty())
			{
				if (!StringUtils.equals(request.getParameter("password"), request.getParameter("confirmpassword"))){
					context.put("validatePassword", "error.password.matchPassword");
					check=true;
				}
				if (!FieldsValidation.isPasswordCorrect(request.getParameter("password")))
				{

					context.put("validatePassword", "error.password.invalidlength");
					vu360User.setPassword(null);
					check=true;
				}
				else{
					vu360User.setPassword(request.getParameter("password"));
				}

			}
		String dateString = request.getParameter("expirationDate").trim();
		if (dateString==null)dateString="";
		if (!StringUtils.isBlank(dateString)){

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date =null;

			try {
				String TodaysDate=formatter.format(new Date());
				date=formatter.parse(TodaysDate);
				expirationDate = formatter.parse(dateString);
				if (!formatter.format(expirationDate).equals(dateString)) {

					context.put("validateExpirationDate", "error.addNewLearner.expDate.invalidDate");
					check=true;
				}else {
					if( expirationDate.before(date)  ) {
						context.put("validateExpirationDate", "error.addNewLearner.expDate.invalidDate");
						check=true;
					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
				context.put("validateExpirationDate", "error.addNewLearner.expDate.invalidDate");
				check = true;
			}
		}
		return check;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView updateLearner(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors )throws Exception {

		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("featureGroup", "Users");

			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String userId=request.getParameter("Id");
			VU360User vu360User = null;
			if( !userId.isEmpty() ) {
				vu360User = vu360UserService.loadForUpdateVU360User(Long.valueOf(userId));
			}
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			if(vu360User !=null && vu360User.getLearner().getCustomer().getId().equals(customer.getId())){
				Learner learner=vu360User.getLearner(); 
				vu360User.setFirstName(request.getParameter("firstName"));
				vu360User.setLastName(request.getParameter("lastName"));
				vu360User.setMiddleName(request.getParameter("middleName"));
				vu360User.setEmailAddress(request.getParameter("emailAddress"));
				if(vu360User.getUsername().compareToIgnoreCase(request.getParameter("userName"))!=0)
				{
					log.debug("vu360User.getUsername() " + vu360User.getUsername()+"userName"+request.getParameter("userName"));
					vu360User.setUsername(request.getParameter("userName"));				   
				}
				/*
				 * Password should not be dited in this way. It has a chance to encrypted again
				 */
				String newPassword=request.getParameter("password");
				if (newPassword !=null)
					if (!newPassword.trim().isEmpty())
					{
						vu360User.setPassWordChanged(true);
						vu360User.setPassword(request.getParameter("password")); 

					}

				vu360User.setAccountNonExpired(new Boolean(request.getParameter("accountNonExpired")));
				vu360User.setAccountNonLocked(new Boolean(request.getParameter("accountNonLocked")));
				vu360User.setChangePasswordOnLogin(new Boolean(request.getParameter("exsisting")));
				vu360User.setCredentialsNonExpired(new Boolean(request.getParameter("accountNonExpired")));
				vu360User.setEnabled(new Boolean(request.getParameter("enabled")));
				vu360User.setVissibleOnReport(new Boolean(request.getParameter("vissibleOnReport")));

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String dateString = request.getParameter("expirationDate").trim();
				log.debug("dateString " + dateString);
				// Parse the string into a Date.
				if (!dateString.isEmpty()){
					Date myDate = formatter.parse(dateString);
					vu360User.setExpirationDate(myDate);
				}
				else
					vu360User.setExpirationDate(null);

				learner.getLearnerProfile().setOfficePhone(request.getParameter("officePhone").trim());
				learner.getLearnerProfile().setOfficePhoneExtn(request.getParameter("officePhoneExtn").trim());
				learner.getLearnerProfile().setMobilePhone(request.getParameter("mobilePhone").trim());

				learner.getLearnerProfile().getLearnerAddress().setStreetAddress(request.getParameter("streetAddress1").trim());
				learner.getLearnerProfile().getLearnerAddress().setStreetAddress2(request.getParameter("streetAddress2").trim());
				learner.getLearnerProfile().getLearnerAddress().setCity(request.getParameter("city").trim());
				learner.getLearnerProfile().getLearnerAddress().setState(request.getParameter("cmbState").trim());
				learner.getLearnerProfile().getLearnerAddress().setZipcode(request.getParameter("zipcode").trim());
				learner.getLearnerProfile().getLearnerAddress().setCountry(request.getParameter("cmbCountry").trim());
				learner.getLearnerProfile().getLearnerAddress2().setCity(request.getParameter("city2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setState(request.getParameter("cmbState2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setCountry(request.getParameter("cmbCountry2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setStreetAddress(request.getParameter("streetAddress2a").trim());
				learner.getLearnerProfile().getLearnerAddress2().setStreetAddress2(request.getParameter("streetAddress2b").trim());
				learner.setVu360User(vu360User);
				vu360User = learnerService.saveUser(vu360User);

				/////// Send mail to learner for new password
				if(newPassword !=null && !newPassword.isEmpty())
				{
					try {
						Map model = new HashMap();
						VU360User user=vu360User;
						model.put("loggedInUser", vu360User);
						model.put("customerName", vu360User.getLearner().getCustomer().getName());
						model.put("user", user);

						StringBuilder loginURL=new StringBuilder();
						loginURL.append(request.getScheme());
						loginURL.append("://");
						loginURL.append(request.getServerName());
						if(request.getServerPort()!=80){
							loginURL.append(":");
							loginURL.append(request.getServerPort());
						}
						loginURL.append(request.getContextPath());
						model.put("url",loginURL.toString());

						Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
						VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
						if(adminDetails.getCurrentSearchType().equals(AdminSearchType.CUSTOMER)){
							brander=VU360Branding.getInstance().getBranderByUser(request,loggedInUserVO);
						}
						String batchImportTemplatePath =  brander.getBrandElement("lms.email.resetPassWord.body");
						String fromAddress =  brander.getBrandElement("lms.email.resetPassWord.fromAddress");
						String fromCommonName =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						String subject =  brander.getBrandElement("lms.emil.resetPassWord.subject");//+ customer.getName();
						String support =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						String phone =  brander.getBrandElement("lms.footerLinks.contactus.contactPhone");
						String lmsDomain=VU360Properties.getVU360Property("lms.domain");
						model.put("lmsDomain",lmsDomain);
						model.put("support", support);
						model.put("phone", phone);
						model.put("brander", brander);
						
						/*START- BRANDNG EMAIL WORK*/
						String templateText =  brander.getBrandElement("lms.branding.email.passwordUpdated.templateText");						
						String loginurl= lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());				
						templateText= templateText.replaceAll("&lt;firstname&gt;", vu360User.getFirstName());
						templateText= templateText.replaceAll("&lt;lastname&gt;", vu360User.getLastName());						 						
						templateText= templateText.replaceAll("&lt;loginurl&gt;", loginurl);
						templateText= templateText.replaceAll("&lt;phone&gt;", phone);
						templateText= templateText.replaceAll("&lt;support&gt;", support);	
						templateText= templateText.replaceAll("&lt;customername&gt;", vu360User.getLearner().getCustomer().getName());
						model.put("templateText", templateText);			
						/*END-BRANDING EMAIL WORK*/
						
						model.put("learnerPassword", newPassword);					
						String text = VelocityEngineUtils.mergeTemplateIntoString(
								velocityEngine, batchImportTemplatePath, model);
						SendMailService.sendSMTPMessage(user.getEmailAddress(), 
								fromAddress, 
								fromCommonName, 
								subject, 
								text);					 

					} catch( Exception e ) {
						e.printStackTrace();
					}
				}
				/////// Send mail to learner for new password
				//Update it on StoreFront
				try
				{
					Customer cust=vu360User.getLearner().getCustomer();
					if(cust!=null && cust.getCustomerType().equals(Customer.B2C) && (CustomerUtil.isUserProfileUpdateOnSF(customer))){
						transformAndUpdateProfile(cust, vu360User, request.getParameter("password"));
					}
				}
				catch(Exception ex){
					log.debug("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
					log.error("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
				}

				//Save Custom Fields

				UserForm form=(UserForm) command;
				List<CustomFieldValue> myCustomFieldValues=new ArrayList<CustomFieldValue>();
				if(form.getCustomFields().size()>0){
					for (com.softech.vu360.lms.web.controller.model.customfield.CustomField field : form.getCustomFields()){

						if(field.getCustomFieldRef() instanceof MultiSelectCustomField){

							List<CustomFieldValueChoice> selectedChoiceList = new ArrayList<CustomFieldValueChoice>();

							if (((MultiSelectCustomField) field.getCustomFieldRef()).getCheckBox()){

								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
									if(customFieldValueChoice.isSelected()){
										selectedChoiceList.add(customFieldValueChoice.getCustomFieldValueChoiceRef());
									}
								}

							}else{

								if(field.getSelectedChoices()!=null){

									Map<Long,CustomFieldValueChoice> totalChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

									for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
										totalChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
									}

									for(String selectedChoiceIdString : field.getSelectedChoices()){
										if(totalChoiceMap.containsKey(new Long(selectedChoiceIdString))){
											selectedChoiceList.add(totalChoiceMap.get(new Long(selectedChoiceIdString)));
										}
									}

								}
							}

							CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
							customFieldValue.setCustomField(field.getCustomFieldRef());
							customFieldValue.setValueItems(selectedChoiceList);
							myCustomFieldValues.add(customFieldValue);

						}else{
							CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
							customFieldValue.setCustomField(field.getCustomFieldRef());
							myCustomFieldValues.add(customFieldValue);
						}
					}
				}
				vu360User.getLearner().getLearnerProfile().setCustomFieldValues(myCustomFieldValues);
				learnerService.updateLearnerProfile(vu360User.getLearner().getLearnerProfile());
				// TEMP logic should be removed after R&D why Reseller Custom Fields being inserted on customer_customfield table
				customFieldService.deleteCustomerCustomFieldsRelation(customer, this.getDistributorCustomFieldIds(customer));
			}
			return new ModelAndView(managerTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editUserTemplate);
	}


	private Long[] getDistributorCustomFieldIds(Customer customer)
	{
		List<CustomField> list=customer.getDistributor().getCustomFields();
		Long[] ids=new Long[list.size()];
		int counter=0;
		for(CustomField field:list)
		{
			ids[counter]=field.getId().longValue();
			counter++;
		}
		return ids;
	}


	/**
	 * author baishakhi
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView displayEditLearnerChangeOfLearnerAndOrgGroups(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors ) throws Exception{
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			HttpSession session = request.getSession(true);
			session.setAttribute("featureGroup", "Users");

			String actionType = request.getParameter("action");
			Map<Object, Object> context = new HashMap<Object, Object>();
			Learner newLearner = null;
			String userId = request.getParameter("learnerId");
			newLearner = learnerService.loadForUpdateLearner(Long.valueOf(userId));

			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

			Customer customer = null;
			if (loggedInUser.isLMSAdministrator())
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = loggedInUser.getLearner().getCustomer();

			List<OrganizationalGroup> selectedLearnerOrgGroups =(List<OrganizationalGroup>)orgGroupLearnerGroupService.getOrgGroupsByLearner(newLearner);;

			//will have to think latter if the training admin manage all group
			List<OrganizationalGroup> organizationalGroupsByLearner = orgGroupLearnerGroupService.getOrgGroupsByCustomer(customer);
			List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getAllLearnerGroups(customer.getId(),loggedInUser);
			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup, selectedLearnerOrgGroups, null);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			context.put("orgGroupTreeAsList", treeAsList);

			//Create OrgGroups list
			ManageOrganizationalGroups arrangedOrgGroup = new ManageOrganizationalGroups();
			List<Map>OrgGroupView = arrangedOrgGroup.createOrgGroupView(rootOrgGroup,selectedLearnerOrgGroups,null);
			context.put("userData", loggedInUser);
			List <LearnerGroup> learnerGroupsByLearer=orgGroupLearnerGroupService.getLearnerGroupsByLearner(newLearner);
			if (actionType==null) {
				// Getting learner groups selected by the learner.
				context.put("selectedLearnerGroups", learnerGroupsByLearer);

				// Getting learner groups associated with checked organization groups.
				List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList<LearnerGroup>();
				List<LearnerGroup> ogLearnerGroups = null;
				for(int orgGroupNo=0; orgGroupNo<selectedLearnerOrgGroups.size(); orgGroupNo++) {
					ogLearnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(selectedLearnerOrgGroups.get(orgGroupNo).getId());
					selectedLearnerGroupsAssociatedWithOrgGroup.addAll(ogLearnerGroups);
				}
				selectedLearnerGroupsAssociatedWithOrgGroup = 
					arrangedOrgGroup.compareLearnerGroups(selectedLearnerGroupsAssociatedWithOrgGroup,learnerGroupsByLearer);

				context.put("editLearnerSession", newLearner);
				context.put("OrgGroupView",OrgGroupView);
				context.put("LearnerGroupView",selectedLearnerGroupsAssociatedWithOrgGroup);

			} else if(actionType.equalsIgnoreCase(EDIT_USER_CHANGEGROUP_SAVE)) {
				//=============================================================================
				String[] selectedOrgGroupValues = request.getParameterValues("groups");
				if(selectedOrgGroupValues == null){
					context.put("validateSelectionOfOrgGrp","error.addEnrollment.orgGroupRequired");
					return new ModelAndView(editDisplayLearnerChangeGroupTemplate, "context", context);
				}
				String[] selectedGroups = request.getParameterValues("deSelectedLearnerGroups");
				List<OrganizationalGroup> selectedOrganizationalGroups= new ArrayList<OrganizationalGroup>();
				List<LearnerGroup> selectedLearnerGroups= new ArrayList<LearnerGroup>();

				for (int index=0;index<organizationalGroupsByLearner.size();index++){

					for( int i=0; i<selectedOrgGroupValues.length; i++ ) {
						String orgGroupID = selectedOrgGroupValues[i];
						if( StringUtils.isNotBlank(orgGroupID) ) {
							if (organizationalGroupsByLearner.get(index).getId()== Long.parseLong(orgGroupID)){
								selectedOrganizationalGroups.add(organizationalGroupsByLearner.get(index));
								break;
							}
						}
					}
				}				
				
				//Work for Auto-Learner Enrollment
				List<LearnerGroup> existingLearnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByLearner(newLearner);				
				List<LearnerGroup> newlySelectedLearnerGroups = new ArrayList<LearnerGroup>();
				for(int index1=0;index1<learnerGroups.size();index1++){					
					if( selectedGroups!=null ){
						for( int j=0; j<selectedGroups.length; j++ ) {
							String learnerGroupID = selectedGroups[j];
							if(StringUtils.isNotBlank(learnerGroupID)){
								if(learnerGroups.get(index1).getId()==Long.parseLong(learnerGroupID)){
									LearnerGroup lg = learnerGroups.get(index1);
									if(!existingLearnerGroups.contains(lg)){
										newlySelectedLearnerGroups.add(lg);
									}
									selectedLearnerGroups.add(lg);
									break;
								}
							}
						}
					}
				}
				
				orgGroupLearnerGroupService.ManageOrgAndLearnerGroup(selectedOrganizationalGroups,selectedLearnerGroups,newLearner);
				
				if (!newlySelectedLearnerGroups.isEmpty()) {
					Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
					enrollmentService.enrollLearnerInLearnerGroupsCourses(loggedInUser, customer, newLearner, newlySelectedLearnerGroups, brander);
				}
				
				Map<Object, Object> newcontext = new HashMap<Object, Object>();
				newcontext.put("firstName", session.getAttribute("searchedFirstName"));
				newcontext.put("lastName", session.getAttribute("searchedLastName"));
				newcontext.put("emailAddress", session.getAttribute("searchedEmailAddress"));
				newcontext.put("searchKey", session.getAttribute("searchedSearchKey"));
				newcontext.put("searchType", session.getAttribute("searchType"));
				newcontext.put("direction", session.getAttribute("direction"));

				if(session.getAttribute("direction").toString().equalsIgnoreCase("next")){
					newcontext.put("pageNo", Integer.parseInt(session.getAttribute("pageNo").toString()) -1);
				}else{
					newcontext.put("pageNo", session.getAttribute("pageNo"));
				}

				newcontext.put("sortDirection", session.getAttribute("sortDirection"));
				newcontext.put("sortBy", session.getAttribute("sortBy"));
				return new ModelAndView(redirectToSearchPageTemplate, "context", newcontext);
			}
			return new ModelAndView(editDisplayLearnerChangeGroupTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageLearnerTemplate);
	}

	public void sendMail(String from,String to,String subject,String msgText){
		log.debug("send mail");
		SimpleMailMessage message = new SimpleMailMessage() ;
		message.setTo(to);
		message.setText(msgText) ;
		message.setFrom(from) ;
		message.setSubject(subject) ;

		mailSender.send(message) ;
	}


	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<OrganizationalGroup> selectedLearnerOrgGroups, List<OrganizationalGroup> managableOrgGroups){

		if(orgGroup!=null){

			TreeNode node = new TreeNode(orgGroup);

			if( selectedLearnerOrgGroups != null ) {
				for (OrganizationalGroup selectedLearnerOrgGroup : selectedLearnerOrgGroups) {
					if (selectedLearnerOrgGroup.getId().equals(orgGroup.getId())) {
						node.setSelected(true);
					}
				}
			}
			if( managableOrgGroups != null ) {
				for(int orgGroupNum = 0; orgGroupNum < managableOrgGroups.size(); orgGroupNum ++ ) {
					if(managableOrgGroups.get(orgGroupNum).getId() == orgGroup.getId()) {
						node.setEnabled(true);
					}
				}
			} else {
				node.setEnabled(true);
			}
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i), selectedLearnerOrgGroups, managableOrgGroups);
			}

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	public void setManageLearnerTemplate(String manageLearnerTemplate) {
		this.manageLearnerTemplate = manageLearnerTemplate;
	}
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public void setEditUserTemplate(String editUserTemplate) {
		this.editUserTemplate = editUserTemplate;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public void setLoginTemplate(String loginTemplate) {
		this.loginTemplate = loginTemplate;
	}

	public void setEditDisplayLearnerChangeGroupTemplate(
			String editDisplayLearnerChangeGroupTemplate) {
		this.editDisplayLearnerChangeGroupTemplate = editDisplayLearnerChangeGroupTemplate;
	}

	public void setManagerTemplate(String managerTemplate) {
		this.managerTemplate = managerTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public void setRedirectToSearchPageTemplate(String redirectToSearchPageTemplate) {
		this.redirectToSearchPageTemplate = redirectToSearchPageTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public void setRedirectToOtherPageIfPermisionRevokedTemplate(
			String redirectToOtherPageIfPermisionRevokedTemplate) {
		this.redirectToOtherPageIfPermisionRevokedTemplate = redirectToOtherPageIfPermisionRevokedTemplate;
	}

	private void transformAndUpdateProfile(Customer customer, VU360User user, String strPwd){
		CustomerData customerData=new CustomerData();
		customerData.setCustomerID(customer.getCustomerGUID());
		customerData.setBillingAddress(getBillingAddress(user));
		customerData.setShippingAddress(getShippingAddress(user));
		UpdateUserAuthenticationCredential auth=new UpdateUserAuthenticationCredential();
		auth.setUserLogonID(user.getUsername());
		auth.setUserPassword(strPwd);

		new StorefrontClientWSImpl().updateProfileOnStorefront(customerData, auth);
	}
	/**
	 * Setting the Address info
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getBillingAddress( VU360User user) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile().getLearnerAddress();

		msgAddress.setStreetAddress1(learnerAddress.getStreetAddress() );
		msgAddress.setStreetAddress2( learnerAddress.getStreetAddress2() );
		msgAddress.setCity( learnerAddress.getCity() );
		msgAddress.setCountry( learnerAddress.getCountry());
		msgAddress.setState( learnerAddress.getState() );
		msgAddress.setZipCode(learnerAddress.getZipcode() );
		msgAddress.setFirstName( user.getFirstName() );
		msgAddress.setLastName( user.getLastName() );
		msgAddress.setPhone( user.getLearner().getLearnerProfile().getOfficePhone() );

		return msgAddress;
	}  
	/**
	 * Setting the Address info
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getShippingAddress( VU360User user ) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile().getLearnerAddress2();

		msgAddress.setStreetAddress1( learnerAddress.getStreetAddress() );
		msgAddress.setStreetAddress2(learnerAddress.getStreetAddress2() );
		msgAddress.setCity( learnerAddress.getCity() );
		msgAddress.setCountry( learnerAddress.getCountry());
		msgAddress.setState( learnerAddress.getState() );
		msgAddress.setZipCode( learnerAddress.getZipcode() );
		msgAddress.setFirstName( user.getFirstName() );
		msgAddress.setLastName( user.getLastName() );
		msgAddress.setPhone( user.getLearner().getLearnerProfile().getOfficePhone() );
		return msgAddress;
	}

	private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,List<CustomFieldValue> customFieldValues){
		if (customFieldValues != null){
			for (CustomFieldValue customFieldValue : customFieldValues){
				if (customFieldValue.getCustomField()!=null){
					if (customFieldValue.getCustomField().getId().compareTo(customField.getId())==0){
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}
	
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
	}

}