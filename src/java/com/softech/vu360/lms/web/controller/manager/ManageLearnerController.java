package com.softech.vu360.lms.web.controller.manager;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.LearningSessionService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.model.ManageOrganizationalGroups;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Ashis
 *
 */
public class ManageLearnerController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());

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
	private static final String EDIT_USER_CHANGEGROUP_SAVE = "save";
	
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
	private String redirectToOtherPageIfPermisionRevokedTemplate = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private EnrollmentService enrollmentService;
	private EntitlementService entitlementService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;	
    private LearningSessionService learningSessionService;

	@SuppressWarnings("unchecked")
	public ModelAndView searchLearner(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if( auth.getDetails() != null && auth.getDetails() instanceof VU360UserAuthenticationDetails ) {
				/* VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
					if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
						return new ModelAndView(failureTemplate,"isRedirect","c");
					}
				} */
			}
			//learningSessionService.emailCertificate("6fa869a2-3d7f-4d5a-a77d-edc58fc37712");
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
			//VU360User loggedInUser = vu360UserService.getUserById(principalUser.getId());
			context.put("userData", loggedInUser);

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress= request.getParameter("emailaddress");
			String searchKey = request.getParameter("searchkey");
			String action = request.getParameter("action");
			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String sortDirection = request.getParameter("sortDirection");
			String prevSortDirection = "";
			String newId = "";
			String sortBy = request.getParameter("sortBy");

			log.debug("First sortDirection  " + sortDirection);
			int pageNo = 0;
			int recordShowing = 0;
			action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;
			firstName = (firstName == null) ? "" : firstName;
			lastName = (lastName == null) ? "" : lastName;
			emailAddress = (emailAddress == null) ? "" : emailAddress;
			searchKey = (searchKey == null) ? "" : searchKey;
			if( sortDirection == null )
				sortDirection = "0";

			HttpSession session = request.getSession();
			
			if(action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION))request.setAttribute("newPage","true");

			if( action.isEmpty() && session.getAttribute("action") != null && 
					!session.getAttribute("action").toString().isEmpty() )
				action = session.getAttribute("action").toString();

			session.setAttribute("featureGroup","Users & Groups");
			session.setAttribute("feature","LMS-MGR-0001");

			if( sortBy == null && action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)== false ) {

				session.setAttribute("searchedFirstName", firstName);
				session.setAttribute("searchedLastName", lastName);
				session.setAttribute("searchedEmailAddress", emailAddress);
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo",pageNo);
			}
			direction = ( direction == null ) ? MANAGE_USER_PREVPAGE_DIRECTION : direction;
			pageIndex = ( pageIndex == null ) ? "0" : pageIndex;
			sortDirection = ( sortDirection == null ) ? "0" : sortDirection;
			sortBy = ( sortBy == null ) ? MANAGE_USER_SORT_FIRSTNAME : sortBy;

			if( sortBy.equalsIgnoreCase("${context.sortBy}") && session.getAttribute("sortBy") != null )
				sortBy = session.getAttribute("sortBy").toString();
			if( sortDirection.equalsIgnoreCase("${context.sortDirection}") && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();

			session.setAttribute("sortBy", sortBy);
			session.setAttribute("direction", direction);
			session.setAttribute("sortDirection", sortDirection);
			
			/*
			 * @added by Dyutiman...
			 * this will be not null only if control comes here after adding a new learner. 
			 */
			if( session.getAttribute("createdLearnerId") != null ) {
				newId = session.getAttribute("createdLearnerId").toString();
			}
			log.debug(" new-id -- "+newId);
			
			/*
			 * simple search is no more used
			 */
			if( action.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {
				
				session.setAttribute("searchType", MANAGE_USER_SIMPLE_SEARCH_ACTION);
				if( session.getAttribute("prevSortDirection") != null )
					prevSortDirection = session.getAttribute("prevSortDirection").toString();
				else
					prevSortDirection = "0";
				if( direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION) ) {
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
				}else if( direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION) ) {
					pageIndex = request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
				}
				session.setAttribute("pageNo", pageNo);
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(), 
						loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(prevSortDirection));
				userList = (List<VU360User>)results.get("list");

			}else if( action.equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION) ) {

				session.setAttribute("searchType", MANAGE_USER_ADVANCED_SEARCH_ACTION);
				if( session.getAttribute("prevSortDirection") != null )
					prevSortDirection = session.getAttribute("prevSortDirection").toString();
				else
					prevSortDirection = "0";
				sortDirection = prevSortDirection;

				if( direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION) ) {

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					session.setAttribute("prevAction","paging");

				}else if( direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION) ) {

					pageIndex = request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					session.setAttribute("prevAction","paging");
				}
				session.setAttribute("pageNo", pageNo);
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				if( !loggedInUser.isAdminMode() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					if( tempManagedGroups==null || (tempManagedGroups!=null &&  tempManagedGroups.size() == 0) ) {
						return new ModelAndView(redirectToSearchPageTemplate, "context", context);
					}
				}
				results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
						session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
						loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(prevSortDirection));

				userList = (List<VU360User>)results.get("list");
				log.debug("Result Size = " + userList.size());

			}else if( action.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION) ) {

				session.setAttribute("searchType", MANAGE_USER_ALL_SEARCH_ACTION);
				if( session.getAttribute("prevSortDirection") != null )
					prevSortDirection = session.getAttribute("prevSortDirection").toString();
				else
					prevSortDirection = "0";
				sortDirection = prevSortDirection;
				pageNo = 0;
				session.setAttribute("pageNo",pageNo);
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				
				results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),
						session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
						loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						sortBy, Integer.parseInt(prevSortDirection));
				//results = learnerService.findAllLearners("",loggedInUser,sortBy,Integer.parseInt(sortDirection));
				userList = (List<VU360User>)results.get("list");
				session.setAttribute("prevAction","all");

			}else if( action.equalsIgnoreCase(MANAGE_USER_SORT_LEARNER_ACTION) ) {
				
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());

				if( session.getAttribute("prevAction") != null && ( session.getAttribute("prevAction").toString().equalsIgnoreCase("paging")
						|| session.getAttribute("prevAction").toString().equalsIgnoreCase("all") )) {
					if( sortDirection.equalsIgnoreCase("1") ) {
						sortDirection = "0";
					} else {
						sortDirection = "1";
					}
					session.setAttribute("prevAction","sort");
				}
				
				if( session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION) ) {

					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
							session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else if( session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION) ) {

					results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),
							session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							sortBy, Integer.parseInt(sortDirection));
					//results = learnerService.findAllLearners("",loggedInUser,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
					
				}else{
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}
				session.setAttribute("prevSortDirection",sortDirection);
				if(sortDirection.equalsIgnoreCase("0")) sortDirection = "1";
				else sortDirection = "0";

			}else if( action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) ) {

				String[] selectedUserValues = request.getParameterValues("selectedLearners");
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				
				if( selectedUserValues != null ) {
					long userIdArray[] = new long[selectedUserValues.length];
					for( int loop = 0 ; loop < selectedUserValues.length ; loop++ ) {
						String userID = selectedUserValues[loop];
						if( userID != null )
							userIdArray[loop] = Long.parseLong(userID);
					}
					learnerService.InactiveUsers(userIdArray);
				}

				if( session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION) ) {
					if( !loggedInUser.isAdminMode() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
						if( tempManagedGroups==null || (tempManagedGroups!=null &&  tempManagedGroups.size() == 0) ) {
							return new ModelAndView(redirectToSearchPageTemplate, "context", context);
						}
					}
					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				}else if( session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION) ) {
					if( !loggedInUser.isAdminMode() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
						if( tempManagedGroups==null || (tempManagedGroups!=null &&  tempManagedGroups.size() == 0) ) {
							return new ModelAndView(redirectToSearchPageTemplate, "context", context);
						}
					}
					results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),
							session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							sortBy, Integer.parseInt(prevSortDirection));
					//results = learnerService.findAllLearners("",loggedInUser,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
					
				}else{
					if( !loggedInUser.isAdminMode() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
						if( tempManagedGroups==null || (tempManagedGroups!=null &&  tempManagedGroups.size() == 0) ) {
							return new ModelAndView(redirectToSearchPageTemplate, "context", context);
						}
					}
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),MANAGE_USER_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
				}
				if( sortDirection.equalsIgnoreCase("0") )
					sortDirection = "1";
				else
					sortDirection="0";

			}else if( action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION) ) {
				
				List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				
				sortDirection = "1";
				pageNo = 0;
				session.setAttribute("searchType","");
				sortBy = null;
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
					VU360UserAuthenticationDetails det = (VU360UserAuthenticationDetails)auth.getDetails();
					if(det.getCurrentCustomer().getCustomerType().equalsIgnoreCase("b2c")){
						results =learnerService.findAllLearners("", 
								loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								"firstName",5);
						userList = (List<VU360User>)results.get("list");
						context.put("b2cRecord", userList.size());
						userList.clear();
					}
				}
			}
			if( !results.isEmpty() )
				recordShowing = ((Integer)userList.size()<MANAGE_USER_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(session.getAttribute("pageNo").toString())+1)*MANAGE_USER_PAGE_SIZE;		

			if( session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION) )
				recordShowing = userList.size();
			
			String totalRecord =	(results.isEmpty())?"-1":results.get("recordSize").toString();
			session.setAttribute("searchedFirstName" ,HtmlEncoder.escapeHtmlFull(session.getAttribute("searchedFirstName").toString()));
			context.put("firstName", session.getAttribute("searchedFirstName"));
			session.setAttribute("searchedLastName" ,HtmlEncoder.escapeHtmlFull(session.getAttribute("searchedLastName").toString()));
			context.put("lastName", session.getAttribute("searchedLastName"));
			session.setAttribute("searchedEmailAddress" ,HtmlEncoder.escapeHtmlFull(session.getAttribute("searchedEmailAddress").toString()));
			context.put("emailAddress", session.getAttribute("searchedEmailAddress"));
			context.put("searchKey", session.getAttribute("searchedSearchKey"));
			context.put("searchType", session.getAttribute("searchType"));
			context.put("direction", direction);
			session.setAttribute("action", action);
			session.setAttribute("sortBy", sortBy);
			
			if( !newId.isEmpty() ) {
				Learner justAddedLearner = learnerService.getLearnerByID(Long.parseLong(newId));
				boolean isPresent = false;
				for( VU360User pUser : userList ) {
					if( pUser.getId().compareTo(justAddedLearner.getVu360User().getId()) == 0 ) {
						isPresent = true;
						break;
					}
				}
				if( !isPresent )
					userList.add(0, justAddedLearner.getVu360User());
				if( action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION) ) {
					recordShowing = 1;
					totalRecord = "1";
				} else if( userList.size() == 11 ) {
					userList.remove(10);
				}
			}
			
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			if(customer.getDistributor().getName().equalsIgnoreCase("360Factors")){
				context.put("hideAddUserBtn", true);
			}else{
				context.put("hideAddUserBtn", false);
			}
			
			context.put("members", userList);
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("recordShowing", recordShowing);
			context.put("pageNo", session.getAttribute("pageNo"));
			context.put("sortDirection", sortDirection );
			context.put("sortBy", sortBy);
			session.removeAttribute("createdLearnerId");
			
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
	public ModelAndView displayEditLearner(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String userId=request.getParameter("Id");
			VU360User vu360User = null;
			if(StringUtils.isNotBlank(userId)){
				vu360User=vu360UserService.loadForUpdateVU360User(Long.valueOf(userId));
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
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			if(vu360User !=null && vu360User.getLearner().getCustomer().getId().longValue() == customer.getId().longValue()){

				
				context.put("userData", loggedInUser);
				String actionType=request.getParameter("action");
				log.debug("Action" + actionType);

				if(StringUtils.isNotBlank(actionType)){
					if(actionType.equalsIgnoreCase(EDIT_USER_LEARNER_UPDATE_ACTION)){

						if(this.validateData(request,context,vu360User)){
							context.put("vu360User", vu360User);
							return new ModelAndView(editUserTemplate, "context", context);
						}
						else {
							this.updateLearner(request, response);
							Map<Object, Object> newcontext = new HashMap<Object, Object>();
							HttpSession session = request.getSession();
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

					}else if(actionType.equalsIgnoreCase(EDIT_USER_LEARNER_RESETPASSWORD_ACTION )){
						String newPassword=GUIDGeneratorUtil.generatePassword();
						vu360User.setPassword(newPassword);
						vu360UserService.changeUserPassword(vu360User.getId(), vu360User);
						Map<String, Object> model = new HashMap<>();
						//request.
						vu360User.setPassword(newPassword);
						model.put("user", vu360User);
						//commented by Faisal A. Siddiqui July 21 09 for new branding
						//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
						Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());


						String resetPassWordTemplatePath =  brander.getBrandElement("lms.email.resetPassWord.body");
						String fromAddress =  brander.getBrandElement("lms.email.resetPassWord.fromAddress");
						String fromCommonName =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						String subject =  brander.getBrandElement("lms.email.resetPassWord.subject");
						String support =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
						model.put("support", support);
						String lmsDomain=VU360Properties.getVU360Property("lms.domain");
						model.put("lmsDomain",lmsDomain);
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
								fromAddress, 
								fromCommonName, 
								subject, 
								text); 
						context.put("vu360User", vu360User);						
						return new ModelAndView(editUserTemplate, "context", context);
					}else if(actionType.equalsIgnoreCase("cancel")){
						return new ModelAndView(managerTemplate);
					}
				}else{ 
					context.put("vu360User", vu360User);
					return new ModelAndView(editUserTemplate, "context", context);
				}
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(loginTemplate);
	}

	@SuppressWarnings("unchecked")
	private boolean validateData(HttpServletRequest request,Map context,VU360User vu360User){

		boolean check = false;
		if (StringUtils.isBlank(request.getParameter("emailAddress")))
		{
			context.put("validateEmailAddress", "error.addNewLearner.email.required");
			vu360User.setEmailAddress(null);
			check=true;
		}
		else if
		(!FieldsValidation.isEmailValid(request.getParameter("emailAddress"))){
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
		//19.01.2009
		if (!StringUtils.isBlank(request.getParameter("zipcode"))){

			if (StringUtils.length(request.getParameter("zipcode"))>5){
				context.put("validateZipcode", "error.addNewLearner.zip1.invalidlength");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(null);
				check=true;

			}
			else if (!StringUtils.isNumeric(request.getParameter("zipcode"))){
				context.put("validateZipcode", "error.addNewLearner.zipcode1.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(null);
				check=true;
			}else{
				vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(request.getParameter("zipcode"));
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
		if (!StringUtils.isBlank(request.getParameter("zipcode2"))){

			if (StringUtils.length(request.getParameter("zipcode2"))>5){
				context.put("validateZipcode2", "error.addNewLearner.zip2.invalidlength");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(null);
				check=true;

			}
			else if (!StringUtils.isNumeric(request.getParameter("zipcode2"))){
				context.put("validateZipcode2", "error.addNewLearner.zipcode2.all.invalidText");
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(null);
				check=true;

			}else{
				vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2"));
			}
		}
		/*
		if (StringUtils.isBlank(request.getParameter("userName")))
		{
			context.put("validateUsername", "error.addNewLearner.userName.required");
			vu360User.setUsername(null);
			check=true;
		}
		else if	(!FieldsValidation.isEmailValid(request.getParameter("userName"))){
			context.put("validateUsername", "error.addNewLearner.userName.invalidformat");
			vu360User.setUsername(null);
			check=true;
		}else{
			vu360User.setUsername(request.getParameter("userName"));
		}*/
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
				check=true;
			}
		}
		return check;
	}


	public ModelAndView updateLearner(HttpServletRequest request, HttpServletResponse response) {

		try {
			String userId = request.getParameter("Id");
			//log.debug("userId " + userId);
			VU360User vu360User=null;
			if( userId.isEmpty() ) {
				vu360User  = vu360UserService.loadForUpdateVU360User(Long.valueOf(userId));
			}
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			if(vu360User !=null && vu360User.getLearner().getCustomer().getId().equals(customerId)){
				Learner learner=vu360User.getLearner(); 
				vu360User.setFirstName(request.getParameter("firstName"));
				vu360User.setLastName(request.getParameter("lastName"));
				vu360User.setMiddleName(request.getParameter("middleName"));
				vu360User.setEmailAddress(request.getParameter("emailAddress"));
				//vu360User.setUsername(request.getParameter("userName"));
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
				//vu360User.setAcceptedEULA(new Boolean(request.getParameter("accountNonExpired")));
				vu360User.setAccountNonLocked(new Boolean(request.getParameter("accountNonLocked")));
				vu360User.setChangePasswordOnLogin(new Boolean(request.getParameter("exsisting")));
				vu360User.setCredentialsNonExpired(new Boolean(request.getParameter("accountNonExpired")));
				vu360User.setEnabled(new Boolean(request.getParameter("enabled")));
				vu360User.setVissibleOnReport(new Boolean(request.getParameter("vissibleOnReport")));

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				//Date currentTime_1 = new Date();
				// Date for 13 Nov 2001
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
				//log.debug("cmbCountry");
				learner.getLearnerProfile().getLearnerAddress().setCountry(request.getParameter("cmbCountry").trim());

				learner.getLearnerProfile().getLearnerAddress2().setCity(request.getParameter("city2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setState(request.getParameter("cmbState2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setZipcode(request.getParameter("zipcode2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setCountry(request.getParameter("cmbCountry2").trim());
				learner.getLearnerProfile().getLearnerAddress2().setStreetAddress(request.getParameter("streetAddress2a").trim());
				learner.getLearnerProfile().getLearnerAddress2().setStreetAddress2(request.getParameter("streetAddress2b").trim());

				learner.setVu360User(vu360User);
				//learnerService.saveLearner(learner);
				learnerService.saveUser(vu360User);

				//Update it on StoreFront
				try {
					Customer cust=vu360User.getLearner().getCustomer();
					if(cust!=null && cust.getCustomerType().equals(Customer.B2C)){
						transformAndUpdateProfile(cust, vu360User, request.getParameter("password"));
					}
				}
				catch(Exception ex){
					log.debug("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
					log.error("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
				}
			}
			return new ModelAndView(managerTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editUserTemplate);
	}


	/**
	 * author baishakhi
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView displayEditLearnerChangeOfLearnerAndOrgGroups(HttpServletRequest request,
			HttpServletResponse response){
		try {

			String actionType=request.getParameter("action");
			Map<Object, Object> context = new HashMap<Object, Object>();
			//HttpSession session = request.getSession();
			Learner newLearner=null;
			String userId=request.getParameter("learnerId");
			//if (session.getAttribute("editLearnerSession")==null) {
			newLearner=learnerService.loadForUpdateLearner(Long.valueOf(userId));
			//newLearner = saveNewLearner(request);
			//} else {
			//newLearner = (Learner)session.getAttribute("editLearnerSession");
			//}

			//VU360User loggedInUser = VU360UserAuthenticationDetails.getProxyUser();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();//Above statement not enabling org groups
			

			Customer customer = null;
			if (vu360UserService.hasAdministratorRole(loggedInUser))
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = loggedInUser.getLearner().getCustomer();

			List<OrganizationalGroup> selectedLearnerOrgGroups =(List<OrganizationalGroup>)orgGroupLearnerGroupService.getOrgGroupsByLearner(newLearner);;

			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(selectedLearnerOrgGroups!=null && selectedLearnerOrgGroups.size()>0){
				for(OrganizationalGroup orgGroup:selectedLearnerOrgGroups){
					//Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroup.getId());
				}
			}
			// List<OrganizationalGroup> organizationalGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customer);
			List<OrganizationalGroup> organizationalGroupsByLearner = orgGroupLearnerGroupService.getOrgGroupsByCustomer(customer);
			List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getAllLearnerGroups(customer.getId(),loggedInUser);
			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
			//	TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup, selectedLearnerOrgGroups, loggedInUser);

			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			//	for(TreeNode node:treeAsList )
			//	log.debug(node.toString());
			context.put("orgGroupTreeAsList", treeAsList);

			//Create OrgGroups list
			ManageOrganizationalGroups arrangedOrgGroup = new ManageOrganizationalGroups();
			//List<Map>OrgGroupView = arrangedOrgGroup.createOrgGroupView(rootOrgGroup,selectedLearnerOrgGroups,null);
			context.put("userData", loggedInUser);
			List <LearnerGroup> learnerGroupsByLearer=orgGroupLearnerGroupService.getLearnerGroupsByLearner(newLearner);
			if (actionType==null) {
				// Getting learner groups selected by the learner.

				context.put("selectedLearnerGroups", learnerGroupsByLearer);

				//session.setAttribute("selectedLearnerGroups",selectedLearnerOrgGroups);

				// Getting learner groups associated with checked organization groups.
				List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList<LearnerGroup>();
				List<LearnerGroup> lgs = null;
				for(int orgGroupNo=0; orgGroupNo<selectedLearnerOrgGroups.size(); orgGroupNo++) {
					lgs = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(selectedLearnerOrgGroups.get(orgGroupNo).getId());
					selectedLearnerGroupsAssociatedWithOrgGroup.addAll(lgs);
					//selectedLearnerGroupsAssociatedWithOrgGroup.addAll(selectedLearnerOrgGroups.get(orgGroupNo).getLearnerGroups());
				}

				selectedLearnerGroupsAssociatedWithOrgGroup = 
					arrangedOrgGroup.compareLearnerGroups(selectedLearnerGroupsAssociatedWithOrgGroup,learnerGroupsByLearer);


				//session.setAttribute("LearnerGroupView", selectedLearnerGroupsAssociatedWithOrgGroup);
				//session.setAttribute("editLearnerSession", newLearner);
				context.put("editLearnerSession", newLearner);
				//	context.put("OrgGroupView",OrgGroupView);
				context.put("LearnerGroupView",selectedLearnerGroupsAssociatedWithOrgGroup);

			} else if(actionType.equalsIgnoreCase(EDIT_USER_CHANGEGROUP_SAVE)) {

				//newLearner= (Learner)session.getAttribute("editLearnerSession");

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
						if( StringUtils.isNotBlank(orgGroupID)) {
							if (organizationalGroupsByLearner.get(index).getId()== Long.parseLong(orgGroupID)){
								selectedOrganizationalGroups.add(organizationalGroupsByLearner.get(index));
								break;

							}
						}
					}
				}

				//Work for Auto-Learner Enrollment
				List<LearnerGroup> existingLearnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByLearner(newLearner);				
				/*
				List<Course> removedLearnerGroupsCourses = new ArrayList<Course>();
				List<Course> remainingLearnerGroupsCourses = new ArrayList<Course>();
				if(!existingLearnerGroups.isEmpty()) {
					//Check the removed Learner Group List									
					for (LearnerGroup lg: existingLearnerGroups) {
						boolean removed = true;
						List<LearnerGroupItem> lgItems = lg.getLearnerGroupItems();
						if( selectedGroups!=null ){
							for( int j=0; j<selectedGroups.length; j++ ) {
								Long existingLgId = lg.getId();
								Long learnerGroupID = Long.valueOf(selectedGroups[j]);
								if (existingLgId.equals(learnerGroupID)) {
									removed = false;
									break;
								}
							}
						}
						for(LearnerGroupItem lgItem: lgItems){
							Course course = lgItem.getCourse();
							if(removed){
								if(!removedLearnerGroupsCourses.contains(course)) {
									removedLearnerGroupsCourses.add(course);
								}
							}
							else {
								if(!remainingLearnerGroupsCourses.contains(course)) {
									remainingLearnerGroupsCourses.add(course);
								}
							}
						}
					}
					
					//Remove enrollment only if the course of learner group doesn't exist in any other learner group of this user.
					for(Course course : removedLearnerGroupsCourses) {
						if(!remainingLearnerGroupsCourses.contains(course)) {
							enrollmentService.deleteLearnerEnrollment(newLearner, course);
						}
					}
				}
				*/
				
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
				HttpSession session = request.getSession();
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
				//	return new ModelAndView(managerTemplate);
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


	@SuppressWarnings("unused")
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<OrganizationalGroup> selectedLearnerOrgGroups,VU360User loggedInUser){

		boolean isEnabled = true;
		if( orgGroup != null ) {

			TreeNode node = new TreeNode(orgGroup);

			if( selectedLearnerOrgGroups != null ) {
				for(int orgGroupNum = 0; orgGroupNum < selectedLearnerOrgGroups.size(); orgGroupNum ++ ) {
					if(selectedLearnerOrgGroups.get(orgGroupNum).getId().equals(orgGroup.getId())) {
						node.setSelected(true);
					}
				}
			}
			/*if( managableOrgGroups != null ) {
				for(int orgGroupNum = 0; orgGroupNum < managableOrgGroups.size(); orgGroupNum ++ ) {
					if(managableOrgGroups.get(orgGroupNum).getId() == orgGroup.getId()) {
						node.setEnabled(true);
					}
				}
			} else {
				node.setEnabled(true);
			}*/
			if(!vu360UserService.hasAdministratorRole(loggedInUser) && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {

				for(OrganizationalGroup og : loggedInUser.getTrainingAdministrator().getManagedGroups()) {
					if(	og.getId().longValue() == orgGroup.getId().longValue()) {
						isEnabled=true;
						break;
					}
					else
						isEnabled=false;
				}
			}
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i), selectedLearnerOrgGroups, loggedInUser);
			}
			if(isEnabled)
				node.setEnabled(true);
			else
				node.setEnabled(false);

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}


	/**
	 * @return the manageLearnerTemplate
	 */
	public String getManageLearnerTemplate() {
		return manageLearnerTemplate;
	}
	/**
	 * @param manageLearnerTemplate the manageLearnerTemplate to set
	 */
	public void setManageLearnerTemplate(String manageLearnerTemplate) {
		this.manageLearnerTemplate = manageLearnerTemplate;
	}
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public String getEditUserTemplate() {
		return editUserTemplate;
	}

	public void setEditUserTemplate(String editUserTemplate) {
		this.editUserTemplate = editUserTemplate;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the loginTemplate
	 */
	public String getLoginTemplate() {
		return loginTemplate;
	}

	/**
	 * @param loginTemplate the loginTemplate to set
	 */
	public void setLoginTemplate(String loginTemplate) {
		this.loginTemplate = loginTemplate;
	}

	/**
	 * @return the editDisplayLearnerChangeGroupTemplate
	 */
	public String getEditDisplayLearnerChangeGroupTemplate() {
		return editDisplayLearnerChangeGroupTemplate;
	}

	/**
	 * @param editDisplayLearnerChangeGroupTemplate the editDisplayLearnerChangeGroupTemplate to set
	 */
	public void setEditDisplayLearnerChangeGroupTemplate(
			String editDisplayLearnerChangeGroupTemplate) {
		this.editDisplayLearnerChangeGroupTemplate = editDisplayLearnerChangeGroupTemplate;
	}

	public String getManagerTemplate() {
		return managerTemplate;
	}

	public void setManagerTemplate(String managerTemplate) {
		this.managerTemplate = managerTemplate;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	/**
	 * @return the redirectToSearchPageTemplate
	 */
	public String getRedirectToSearchPageTemplate() {
		return redirectToSearchPageTemplate;
	}

	/**
	 * @param redirectToSearchPageTemplate the redirectToSearchPageTemplate to set
	 */
	public void setRedirectToSearchPageTemplate(String redirectToSearchPageTemplate) {
		this.redirectToSearchPageTemplate = redirectToSearchPageTemplate;
	}

	/**
	 * @return the failureTemplate
	 */
	public String getFailureTemplate() {
		return failureTemplate;
	}

	/**
	 * @param failureTemplate the failureTemplate to set
	 */
	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	/**
	 * @return the redirectToOtherPageIfPermisionRevokedTemplate
	 */
	public String getRedirectToOtherPageIfPermisionRevokedTemplate() {
		return redirectToOtherPageIfPermisionRevokedTemplate;
	}

	/**
	 * @param redirectToOtherPageIfPermisionRevokedTemplate the redirectToOtherPageIfPermisionRevokedTemplate to set
	 */
	public void setRedirectToOtherPageIfPermisionRevokedTemplate(
			String redirectToOtherPageIfPermisionRevokedTemplate) {
		this.redirectToOtherPageIfPermisionRevokedTemplate = redirectToOtherPageIfPermisionRevokedTemplate;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}


	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}


	public EntitlementService getEntitlementService() {
		return entitlementService;
	}


	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}


	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}


	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}


	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}


	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}


	private void transformAndUpdateProfile(Customer customer, VU360User user, String strPwd){

		CustomerData customerData=new CustomerData();

		customerData.setCustomerID(customer.getCustomerCode());
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


	/**
	 * @return the learningSessionService
	 */
	public LearningSessionService getLearningSessionService() {
		return learningSessionService;
	}


	/**
	 * @param learningSessionService the learningSessionService to set
	 */
	public void setLearningSessionService(
			LearningSessionService learningSessionService) {
		this.learningSessionService = learningSessionService;
	}

}