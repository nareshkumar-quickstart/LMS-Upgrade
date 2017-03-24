package com.softech.vu360.lms.web.controller.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ProctorStatusEnum;
import com.softech.vu360.util.SendMailService;
/**
 * 
 * @author arijit
 * 
 */
public class ChangeMemberRoleController extends MultiActionController implements
		InitializingBean {

	private static final Logger log = Logger.getLogger(ChangeMemberRoleController.class
			.getName());

	private static final String CHANGE_MEMBER_PREVPAGE_DIRECTION = "prev";
	private static final String CHANGE_MEMBER_NEXTPAGE_DIRECTION = "next";


	private static final int CHANGE_MEMBER_PAGE_SIZE = 10;
	private static final String CHANGE_MEMBER_SORT_LEARNER_ACTION = "sort";
	private static final String MANAGE_ORGGROUP_UPDATELEARNER_ACTION = "update";

	private static final String ADD_ROLE_DEFAULT_ACTION = "default";
	private static final String ADD_ROLE_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String ADD_ROLE_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String ADD_ROLE_PREVPAGE_DIRECTION = "prev";
	private static final String ADD_ROLE_NEXTPAGE_DIRECTION = "next";
	private static final int ADD_ROLE_PAGE_SIZE = 10;
	private static final String ADD_ROLE_ALL_SEARCH_ACTION = "allSearch";
	private static final String ADD_ROLE_SORT_FIRSTNAME = "firstName";
	private static final String ADD_ROLE_SORT_LEARNER_ACTION = "sort";
	private static final String CHANGE_MEMBER_ROLE_DELETE_MEMBER_ACTION = "delete";
	private static final String DISABLE_PROCTOR_ACTION = "disableProctor";
	private static final String DISABLE_SINGLE_PROCTOR_ACTION = "disableSingleProctor";
	private static final String CHANGE_MEMBER_ROLE_PAGE_SEARCH_ACTION = "pageSearch";

	private String changeMemberRoleTemplate = null;
	private String changeMemberRoleForProctorTemplate = null;
	private String addMemberRoleTemplate = null;
	private String closeTemplate = null;
	private String addMemberRoleToChangeMemberRedirectTemplate = null;
	
	private LearnerService learnerService = null;
	private ProctorService proctorService = null;
	private VU360UserService vu360UserService = null;

	HttpSession session = null;

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public ModelAndView changeMemberRole(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LMSRole lmsRole = null;
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");
			String direction = request.getParameter("direction");
			String searchType = request.getParameter("searchType");
			String pageIndex = request.getParameter("pageIndex");

			String prevAct = request.getParameter("prevAct");
			if( prevAct == null ) prevAct = "";

			int pageNo = 0;
			int recordShowing = 0;

			sortDirection = (sortDirection == null) ? "0" : sortDirection;
			searchType = (searchType == null) ? CHANGE_MEMBER_ROLE_PAGE_SEARCH_ACTION : searchType;
			sortBy = (sortBy == null) ? "firstName" : sortBy;
			pageIndex = (pageIndex == null) ? "0" : pageIndex;
			direction = (direction == null) ? "" : direction;
			String roleID = request.getParameter("roleId");
			String roleType = request.getParameter("roleType");
			String action = request.getParameter("action");
			action = (action == null) ? ADD_ROLE_DEFAULT_ACTION: action;

			if ( roleID == null )
				return new ModelAndView(changeMemberRoleTemplate);
			else if ( StringUtils.isNotBlank(roleID)) {

				session.setAttribute("roleId", roleID);
				lmsRole = learnerService.getLMSRoleById(Long.valueOf(roleID));

				if ( StringUtils.isNotBlank(action) ) {
					if ( action.equalsIgnoreCase(CHANGE_MEMBER_ROLE_DELETE_MEMBER_ACTION) ) {

						String[] selectedUserValues = request
						.getParameterValues("selectedUsers");
						long userIdArray[] = new long[selectedUserValues.length];

						if (selectedUserValues != null) {
							for (int loop = 0; loop < selectedUserValues.length; loop++) {
								String userID = selectedUserValues[loop];
								if (StringUtils.isNotBlank(userID))
									userIdArray[loop] = Long.parseLong(userID);
							}
						}
						/**
						 * Noman
						 * calling unAssignUserFromRole, to revoke roles from particular user, passing userids and roleids
						 * */
						learnerService.unAssignUserFromRole(userIdArray,
								learnerService.getLMSRoleById(Long.valueOf(roleID)));
					}else if ( action.equalsIgnoreCase(DISABLE_PROCTOR_ACTION) ) {
						Proctor proctor=null;
						String[] selectedUserValues = request.getParameterValues("selectedUsers");
						
						if (selectedUserValues != null) {
							for (int loop = 0; loop < selectedUserValues.length; loop++) {
								String userID = selectedUserValues[loop];
								if (StringUtils.isNotBlank(userID) && Long.parseLong(userID)>0){
									proctor = proctorService.getProctorByUserId(Long.parseLong(userID));
									proctor.setStatus(ProctorStatusEnum.Disable.toString());
									proctorService.updateProctor(proctor);
									
									// send Proctor disable email to 360training staff
									sendEmailToProctorSuperviser(proctor);
									
								}
							}
						}
					}
					else if ( action.equalsIgnoreCase(DISABLE_SINGLE_PROCTOR_ACTION) ) {
						Proctor proctor=null;
						String userID = request.getParameter("disableSingleProctorId");
						
						if (userID != null && StringUtils.isNotBlank(userID) && Long.parseLong(userID)>0) {
							proctor = proctorService.getProctorByUserId(Long.parseLong(userID));
							proctor.setStatus(ProctorStatusEnum.Disable.toString());
							proctorService.updateProctor(proctor);
							
							
							//CODE USE TO DELETE PROCTOR ROLE OF USER
							//String userIdArray[] = new String[1];
							//userIdArray[0] = proctor.getUser().getId().toString();
							//learnerService.unAssignUsersFromAllRolesOfType(userIdArray, LMSRole.ROLE_PROCTOR);
							
							// send Proctor disable email to 360training staff
							sendEmailToProctorSuperviser(proctor);
						}
					}
				}
				Map<Object, Object> results = null;
				List<VU360User> userList = null;

				if( session.getAttribute("prevSortBy") != null && session.getAttribute("prevAct") != null && 
						session.getAttribute("prevSortBy").toString().equalsIgnoreCase(sortBy) &&
							( session.getAttribute("prevAct").toString().equalsIgnoreCase("PrevNext") || 
								session.getAttribute("prevAct").toString().equalsIgnoreCase("All") ) &&
								action.equalsIgnoreCase("sort") )
				{
					if( sortDirection.equalsIgnoreCase("0") )
						sortDirection = "1";
					else
						sortDirection = "0";
				}

				// this actually done the show all thing...
				if ( searchType.equalsIgnoreCase(ADD_ROLE_SIMPLE_SEARCH_ACTION) ) {
					if( session.getAttribute("prevAction") != null && 
							session.getAttribute("prevAction").toString().equalsIgnoreCase("sort") ) {
						if (sortDirection.equalsIgnoreCase("0"))
							sortDirection = "1";
						else
							sortDirection = "0";
						session.setAttribute("prevAction", "showall");
					}
					pageNo = 0;
					
					List<OrganizationalGroup> tempManagedGroups = learnerService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());

					results = learnerService.getAllUsersInLmsRole(lmsRole,
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							sortBy, Integer.parseInt(sortDirection));
					userList = (List<VU360User>) results.get("list");
				} else {
					// pagination search
					if( !action.equalsIgnoreCase(CHANGE_MEMBER_SORT_LEARNER_ACTION) ) {
						if ( direction.equalsIgnoreCase(CHANGE_MEMBER_PREVPAGE_DIRECTION) ) {

							pageNo = (pageIndex.isEmpty()) ? 0 : Integer.parseInt(pageIndex);
							pageNo = (pageNo <= 0) ? 0 : pageNo - 1;
							log.debug("pageNo = " + pageNo);
							if( session.getAttribute("prevAction") != null && 
									session.getAttribute("prevAction").toString().equalsIgnoreCase("sort") ) {
								if (sortDirection.equalsIgnoreCase("0"))
									sortDirection = "1";
								else
									sortDirection = "0";
								session.setAttribute("prevAction", "paging");
							}
						} else if ( direction.equalsIgnoreCase(CHANGE_MEMBER_NEXTPAGE_DIRECTION) ) {

							pageIndex = request.getParameter("pageIndex");
							pageNo = (pageIndex.isEmpty()) ? 0 : Integer.parseInt(pageIndex);
							pageNo = (pageNo < 0) ? 0 : pageNo + 1;
							log.debug("page no " + pageNo);
							if( session.getAttribute("prevAction") != null && 
									session.getAttribute("prevAction").toString().equalsIgnoreCase("sort") ) {
								if (sortDirection.equalsIgnoreCase("0"))
									sortDirection = "1";
								else
									sortDirection = "0";
								session.setAttribute("prevAction", "paging");
							}
						}
					}
					if( action.equalsIgnoreCase(CHANGE_MEMBER_SORT_LEARNER_ACTION) )
						pageNo = Integer.parseInt(pageIndex);
					
					List<OrganizationalGroup> tempManagedGroups = learnerService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());

					results = learnerService.getAllUsersInLmsRole(lmsRole,
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							pageNo, CHANGE_MEMBER_PAGE_SIZE, sortBy, Integer.parseInt(sortDirection));
					userList = (List<VU360User>) results.get("list");
				}
				String totalRecord = "0";

				if ( !results.isEmpty() ) {
					recordShowing = ((Integer) userList.size() < CHANGE_MEMBER_PAGE_SIZE) ? Integer
							.parseInt(results.get("recordSize").toString())
							: (pageNo + 1) * CHANGE_MEMBER_PAGE_SIZE;
							totalRecord = results.get("recordSize").toString();
				}
				if ( searchType.equalsIgnoreCase(ADD_ROLE_SIMPLE_SEARCH_ACTION) )
					recordShowing = Integer.parseInt(totalRecord);

				if( action.equalsIgnoreCase(CHANGE_MEMBER_SORT_LEARNER_ACTION) ) {
					if( !searchType.equalsIgnoreCase(ADD_ROLE_SIMPLE_SEARCH_ACTION) )
					{
						if( sortDirection.equalsIgnoreCase("0") )
							sortDirection = "1";
						else
							sortDirection = "0";
					}
					session.setAttribute("prevAction", "sort");
				}
				context.put("roleid", roleID);
				context.put("lmsRole", lmsRole);
				context.put("roleName", lmsRole.getRoleName());
				context.put("totalRecord", Integer.parseInt(totalRecord));
				context.put("recordShowing", recordShowing);
				context.put("pageNo", pageNo);
				context.put("listUsers", results.get("list"));
				context.put("sortDirection", sortDirection);
				context.put("sortBy", sortBy);
				context.put("direction", direction);
				context.put("searchType", searchType);
				session.setAttribute("prevSortBy", sortBy);
				session.setAttribute("prevAct", prevAct);
				
				context.put("roleType", roleType);
			}
			if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR))
				return new ModelAndView(changeMemberRoleForProctorTemplate, "context", context);
			else
				return new ModelAndView(changeMemberRoleTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(changeMemberRoleTemplate);
	}

	private void sendEmailToProctorSuperviser(Proctor proctor){
		String credentialsName=""; 
		List<Credential> lstCredential = proctor.getCredentials();
		for( Credential regCred : lstCredential ) {
			credentialsName = credentialsName + "<br />" + regCred.getOfficialLicenseName();
		}
		
		try {
			Properties properties = new Properties();  
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("proctorEmail.properties");  
			// load the inputStream using the Properties 
			properties.load(inputStream);
			String propBody = properties.getProperty("email.body"); 
			propBody = propBody.replace("[NEWLINE]", "<br />");
			propBody = propBody.replace("[FirstName]", proctor.getUser().getFirstName());
			propBody = propBody.replace("[LastName]", proctor.getUser().getLastName());
			propBody = propBody.replace("[CompanyName]",  proctor.getUser().getLearner().getCustomer().getName());
			propBody = propBody.replace("[EmailAddress]", proctor.getUser().getEmailAddress());
			propBody = propBody.replace("[CredentialsName]", credentialsName);
			SendMailService.sendSMTPMessage(properties.getProperty("email.to"), properties.getProperty("email.from"), 
											properties.getProperty("email.title"), 
											properties.getProperty("email.subject"), 
											propBody);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		// get the value of the property  
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView addMemberInRole(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<Object, Object> results = new HashMap<Object, Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			//Get logged in user
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("userData", loggedInUser);

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress = request.getParameter("emailaddress");
			String searchKey = request.getParameter("searchkey");
			String action = request.getParameter("action");
			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");
			String roleID = request.getParameter("roleId");
			String roleType = request.getParameter("roleType");
			
			if (roleID == null)
				return new ModelAndView(addMemberRoleTemplate);

			session = request.getSession();

			int pageNo = 0;
			int recordShowing = 0;
			action = (action == null) ? ADD_ROLE_DEFAULT_ACTION : action;
			firstName = (firstName == null) ? "" : firstName;
			lastName = (lastName == null) ? "" : lastName;
			emailAddress = (emailAddress == null) ? "" : emailAddress;
			searchKey = (searchKey == null) ? "" : searchKey;
			if (sortDirection == null)
				sortDirection = "0";

			if ( sortBy == null && !action.equalsIgnoreCase(MANAGE_ORGGROUP_UPDATELEARNER_ACTION) 
					&& !action.equalsIgnoreCase(ADD_ROLE_ALL_SEARCH_ACTION) 
					&& !action.equalsIgnoreCase(ADD_ROLE_SORT_LEARNER_ACTION) ) {

				session.setAttribute("searchedFirstName", firstName);
				session.setAttribute("searchedLastName", lastName);
				session.setAttribute("searchedEmailAddress", emailAddress);
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo", pageNo);

			}
			direction = (direction == null) ? ADD_ROLE_PREVPAGE_DIRECTION: direction;
			pageIndex = (pageIndex == null) ? "0" : pageIndex;
			pageNo = (session.getAttribute("pageNo") == null) ? 0 : Integer.parseInt(session.getAttribute("pageNo").toString());
			sortDirection = (sortDirection == null) ? "0" : sortDirection;
			sortBy = (sortBy == null) ? ADD_ROLE_SORT_FIRSTNAME : sortBy;
			log.debug("2nd sortDirection  " + sortDirection);

			/*
			 * simple search is no more used...
			 */
			if (action.equalsIgnoreCase(ADD_ROLE_SIMPLE_SEARCH_ACTION)) {
				session.setAttribute("searchType", ADD_ROLE_SIMPLE_SEARCH_ACTION);
				if (direction.equalsIgnoreCase(ADD_ROLE_PREVPAGE_DIRECTION)) {
					pageNo = (pageIndex.isEmpty()) ? 0 : Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0) ? 0 : pageNo - 1;
				} else if (direction.equalsIgnoreCase(ADD_ROLE_NEXTPAGE_DIRECTION)) {
					pageIndex = request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty()) ? 0 : Integer.parseInt(pageIndex);
					pageNo = (pageNo < 0) ? 0 : pageNo + 1;
				}
				session.setAttribute("pageNo", pageNo);
				boolean hasAdministratorRole = vu360UserService.hasAdministratorRole(loggedInUser);
				boolean hasTrainingAdministratorRole = vu360UserService.hasTrainingAdministratorRole(loggedInUser);
				if( !hasAdministratorRole && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
						results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
								hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								pageNo,ADD_ROLE_PAGE_SIZE, sortBy, Integer
								.parseInt(session.getAttribute("rolesortDirection").toString()));
						userList = (List<VU360User>) results.get("list");
					}
				} else {
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							pageNo,ADD_ROLE_PAGE_SIZE, sortBy, Integer
							.parseInt(session.getAttribute("rolesortDirection").toString()));
					userList = (List<VU360User>) results.get("list");
				}

				// advanced search	
			} else if (action.equalsIgnoreCase(ADD_ROLE_ADVANCED_SEARCH_ACTION)) {
				results = addRoleAdvancedSearch(session, pageNo, loggedInUser, sortBy, sortDirection, direction, 
						pageIndex, request, roleType);
				userList = (List<VU360User>) results.get("list");
				List<ContentOwner> contentOwnerList = (List<ContentOwner>) results.get("contentOwnerList");
				context.put("contentOwnerList", contentOwnerList);
			} else if (action.equalsIgnoreCase(ADD_ROLE_ALL_SEARCH_ACTION)) {
				results = addRoleAllSearch(session, pageNo, loggedInUser, sortBy, sortDirection);
				userList = (List<VU360User>) results.get("list");
				log.debug("Result Size = " + userList.size());

			} else if (action.equalsIgnoreCase(ADD_ROLE_SORT_LEARNER_ACTION)) {
				results = addRoleSortLearner(session, sortDirection, pageNo, sortBy, loggedInUser);
				userList = (List<VU360User>) results.get("list");
			} else if (action.equalsIgnoreCase(MANAGE_ORGGROUP_UPDATELEARNER_ACTION)) {
				manageOrgGrpUpdateLearner(request, context, roleID, roleType);
				
			} else if (action.equalsIgnoreCase(ADD_ROLE_DEFAULT_ACTION)) {
				addRoleDefault(sortDirection, pageNo, session);
				request.setAttribute("newPage","true");
			}
			log.debug("before record showing " + results.isEmpty());
			if (!results.isEmpty())
				recordShowing = ((Integer) userList.size() < ADD_ROLE_PAGE_SIZE) ? Integer.parseInt(results.get("recordSize").toString())
						: (Integer.parseInt(session.getAttribute("pageNo").toString()) + 1) * ADD_ROLE_PAGE_SIZE;

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(ADD_ROLE_ALL_SEARCH_ACTION))
					recordShowing = userList.size();
				log.debug("after record showing " + results.isEmpty());

				if(action.equalsIgnoreCase(CHANGE_MEMBER_SORT_LEARNER_ACTION)) {
					if (sortDirection.equalsIgnoreCase("0"))
						sortDirection = "1";
					else
						sortDirection = "0";
					session.setAttribute("previousAction", "sort");
				}
				log.debug("final sortDirection " + sortDirection);
				String totalRecord = (results.isEmpty()) ? "0" : results.get("recordSize").toString();

				context.put("roleid", roleID);
				context.put("roleType", roleType);				
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
				context.put("sortDirection", sortDirection);
				context.put("sortBy", sortBy);

				return new ModelAndView(addMemberRoleTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(addMemberRoleTemplate);
	}
	
	public String getChangeMemberRoleTemplate() {
		return changeMemberRoleTemplate;
	}

	public void setChangeMemberRoleTemplate(String changeMemberRoleTemplate) {
		this.changeMemberRoleTemplate = changeMemberRoleTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getAddMemberRoleTemplate() {
		return addMemberRoleTemplate;
	}

	public void setAddMemberRoleTemplate(String addMemberRoleTemplate) {
		this.addMemberRoleTemplate = addMemberRoleTemplate;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public String getAddMemberRoleToChangeMemberRedirectTemplate() {
		return addMemberRoleToChangeMemberRedirectTemplate;
	}

	public void setAddMemberRoleToChangeMemberRedirectTemplate(
			String addMemberRoleToChangeMemberRedirectTemplate) {
		this.addMemberRoleToChangeMemberRedirectTemplate = addMemberRoleToChangeMemberRedirectTemplate;
	}

	public Map<Object, Object> addRoleSimpleSearch(int pageNo, String direction, HttpSession session, String pageIndex, 
			HttpServletRequest request, VU360User loggedInUser, String sortBy) {

		Map<Object, Object> results = new HashMap<Object, Object>();
		log.debug("action type - " + ADD_ROLE_SIMPLE_SEARCH_ACTION);
		session.setAttribute("searchType",
				ADD_ROLE_SIMPLE_SEARCH_ACTION);

		if (direction.equalsIgnoreCase(ADD_ROLE_PREVPAGE_DIRECTION)) {

			pageNo = (pageIndex.isEmpty()) ? 0 : Integer
					.parseInt(pageIndex);
			pageNo = (pageNo <= 0) ? 0 : pageNo - 1;
			log.debug("pageNo = " + pageNo);
			// resultList =
			// learnerService.findLearner(searchKey,loggedInUser);

		} else if (direction
				.equalsIgnoreCase(ADD_ROLE_NEXTPAGE_DIRECTION)) {

			pageIndex = request.getParameter("pageIndex");
			pageNo = (pageIndex.isEmpty()) ? 0 : Integer
					.parseInt(pageIndex);
			pageNo = (pageNo < 0) ? 0 : pageNo + 1;
			log.debug("page no " + pageNo);
			// resultList =
			// learnerService.findLearner(searchKey,loggedInUser);

		}

		session.setAttribute("pageNo", pageNo);
		if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
			if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
				
				results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
						vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,ADD_ROLE_PAGE_SIZE, sortBy, Integer
						.parseInt(session.getAttribute("rolesortDirection").toString()));
			}
				
		}else {
			results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
					vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
					pageNo,ADD_ROLE_PAGE_SIZE, sortBy, Integer
					.parseInt(session.getAttribute("rolesortDirection").toString()));
		}
		
		return results;
	}

	public Map<Object, Object> addRoleAdvancedSearch(HttpSession session, int pageNo, VU360User loggedInUser, String sortBy, 
			String sortDirection, String direction, 
			String pageIndex, HttpServletRequest request, String roleType) {
		log.debug("action type - " + ADD_ROLE_ADVANCED_SEARCH_ACTION);
		
		Map<Object, Object> results = new HashMap<Object, Object>();
		List<VU360User> userList = new ArrayList<VU360User>();
		/*Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			String[] paramValue = request.getParameterValues(paramName);
		}*/
		
		/*Enumeration sessionParams = session.getAttributeNames();
		while (sessionParams.hasMoreElements()) {
			String paramName = (String) sessionParams.nextElement();
			log.debug("paramName: " + paramName);
		}*/
		
		session.setAttribute("searchType",
				ADD_ROLE_ADVANCED_SEARCH_ACTION);
		if (direction.equalsIgnoreCase(ADD_ROLE_PREVPAGE_DIRECTION)) {

			pageNo = (pageIndex.isEmpty()) ? 0 : Integer
					.parseInt(pageIndex);

			pageNo = (pageNo <= 0) ? 0 : pageNo - 1;
			log.debug("pageNo = " + pageNo);
			// resultList =
			// learnerService.findLearner(searchKey,loggedInUser);

		} else if (direction
				.equalsIgnoreCase(ADD_ROLE_NEXTPAGE_DIRECTION)) {

			pageIndex = request.getParameter("pageIndex");
			pageNo = (pageIndex.isEmpty()) ? 0 : Integer
					.parseInt(pageIndex);
			pageNo = (pageNo < 0) ? 0 : pageNo + 1;
			log.debug("page no " + pageNo);
			// resultList =
			// learnerService.findLearner(searchKey,loggedInUser);

		}
		session.setAttribute("pageNo", pageNo);
		// results=orgGroupLearnerGroupService.findLearnerNotInLearnerGroup(objGroup,session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString()
		// ,loggedInUser,pageNo,ADD_ROLE_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
		boolean hasAdministratorRole = vu360UserService.hasAdministratorRole(loggedInUser);
		boolean hasTrainingAdministratorRole = vu360UserService.hasTrainingAdministratorRole(loggedInUser);
		if (StringUtils.isNotBlank(roleType) && roleType.equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)) {
			results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
					session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
					hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
					pageNo,	ADD_ROLE_PAGE_SIZE, sortBy, Integer.parseInt(sortDirection));

			/*results = learnerService.findLearner4AssigningInstRole(session.getAttribute("searchedFirstName").toString(),
					session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), loggedInUser,
					pageNo,	ADD_ROLE_PAGE_SIZE, sortBy, Integer.parseInt(sortDirection), roleId.intValue());*/
			
			/* in-case of assigning instructor role, we need to pass the contentowner list, 
			 * so admin can select the user for assigning instructor role and also select
			 * the contentowner for that particular instructor. As contentowner id is a foreign key
			 * in instructor table  
			 * */
			List<ContentOwner> contentOwnerList = learnerService.getContentOwnerList(loggedInUser);
			results.put("contentOwnerList", contentOwnerList);
			
		} else {
			results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
				session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
				hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
				loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
				loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
				pageNo,	ADD_ROLE_PAGE_SIZE, sortBy, Integer.parseInt(sortDirection));
		}

		userList = (List<VU360User>) results.get("list");
		log.debug("Result Size = " + userList.size());
		return results;
		// recordShowing =
		// ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(pageIndex)+1)*CHANGE_GROUP_PAGE_SIZE;
		// (pageNo)*CHANGE_GROUP_PAGE_SIZE:(Integer)userList.size();

	}
	
	public Map<Object, Object> addRoleAllSearch(HttpSession session, int pageNo, VU360User loggedInUser, String sortBy, String sortDirection) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		List<VU360User> userList = new ArrayList<VU360User>();

		log.debug("action type - " + ADD_ROLE_ALL_SEARCH_ACTION);
		
		session.setAttribute("searchType", ADD_ROLE_ALL_SEARCH_ACTION);
		log.debug("searchType " + session.getAttribute("searchType"));
		pageNo = 0;
		session.setAttribute("pageNo", pageNo);
		// results=orgGroupLearnerGroupService.findAllLearnersNotInLearnerGroup(objGroup,"",loggedInUser,sortBy,Integer.parseInt(sortDirection));
		results = learnerService.findAllLearners("", 
				vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
				loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
				loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
				sortBy, Integer.parseInt(sortDirection));
	/*	results = learnerService.getAllUsersNotInLmsRole(learnerService
				.getLMSRoleById(Long.parseLong(roleID)), loggedInUser,sortBy, Integer.parseInt(sortDirection));
*/
		userList = (List<VU360User>) results.get("list");
		log.debug("Result Size = " + userList.size());
		
		return results;

	}
	
	public Map<Object, Object> addRoleSortLearner(HttpSession session, String sortDirection, int pageNo, String sortBy, VU360User loggedInUser) {

		Map<Object, Object> results = new HashMap<Object, Object>();
		log.debug("action type - " + ADD_ROLE_SORT_LEARNER_ACTION);
		log.debug("searchType " + session.getAttribute("searchType"));

		if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(ADD_ROLE_ADVANCED_SEARCH_ACTION)) {

			
			session.setAttribute("rolesortDirection",sortDirection);
			results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),
					session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
					vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
					pageNo,	ADD_ROLE_PAGE_SIZE, sortBy, Integer.parseInt(sortDirection));

		} else if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(ADD_ROLE_ALL_SEARCH_ACTION)) {
			results = learnerService.findAllLearners("",
					vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
					sortBy, Integer.parseInt(sortDirection));
		} else {
			results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
					vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
					Integer.parseInt(session.getAttribute("pageNo").toString()),ADD_ROLE_PAGE_SIZE,sortBy, Integer.parseInt(session.getAttribute("rolesortDirection").toString()));
		}
		// sortDirection = (sortDirection =="0")?"1":"0";
		return results;
	}	
	
	public ModelAndView manageOrgGrpUpdateLearner(HttpServletRequest request, Map<Object, Object> context, String roleID, String roleType) {
		
		log.debug("action type - " + MANAGE_ORGGROUP_UPDATELEARNER_ACTION);
		
		log.debug("request" + request);
		/*String[] selectedLearnerValues = request
				.getParameterValues("selectedLearners");
		*/
		
		HashMap requestParameters = (HashMap) request.getParameterMap();
		for (Iterator iter=requestParameters.keySet().iterator(); iter.hasNext();) {
			String parameterName = (String) iter.next();
			log.debug("paramter name " + parameterName);
		}
			
			
		/* FAS: commented as its throwing exception and being used only for logging
		String[] selectedUserContentOwnerValues = request.getParameterValues("drpContentOwner");
		
		for (int i=0; i<selectedUserContentOwnerValues.length; i++) {
			log.debug("contentowner values " + i + " - " + selectedUserContentOwnerValues[i]);
		}
		*/
		String[] selectedUserValues = request.getParameterValues("selectedUsers");
		if (selectedUserValues != null) {
			Map<Long, Long> userIdContentOwnerMap = convertSelectedUserValue2Map(selectedUserValues);
			
			/**
			 * Noman
			 * calling assignUserToRole function to assign role, passing userid and roleid
			 * incase of assigning instructor role, we need to take care of the instructor entry
			 * if instructor doesn't exists then we have to create the instructor entry with contentowner id
			 * */
			LMSRole role= learnerService.getLMSRoleById(Long.valueOf(roleID));			
			learnerService.unAssignUsersFromAllRolesOfType(selectedUserValues,role.getRoleType());
			learnerService.assignUserToRole(userIdContentOwnerMap, role);
			context.put("roleid",roleID);
			context.put("roleType",roleType);
			return new ModelAndView(addMemberRoleToChangeMemberRedirectTemplate,"context",context);
		}else{
			context.put("errorNoSelection","error.addEnrollment.LearnerRequired");	
			context.put("roleid",roleID);
			context.put("roleType", roleType);
			return new ModelAndView(addMemberRoleTemplate,"context",context);
		}
	}

	private Map<Long, Long> convertSelectedUserValue2Map(String[] selectedUserValues) {
		Map<Long, Long> userContentOwnerMap = new HashMap<Long, Long>();
		
//		long userIdArray[] = new long[selectedUserValues.length];
		for (int loop = 0; loop < selectedUserValues.length; loop++) {
			String userInfo = selectedUserValues[loop];
			
			String[] userIdContentOwnerId = userInfo.split("-");  
			
			if (userIdContentOwnerId.length>1) {
				userContentOwnerMap.put(new Long(userIdContentOwnerId[0]), new Long(userIdContentOwnerId[1]));
			}
			else{
				userContentOwnerMap.put(new Long(userIdContentOwnerId[0]), new Long(userIdContentOwnerId[0]));
			}

		}
		return userContentOwnerMap;
	}
	public void addRoleDefault(String sortDirection, int pageNo, HttpSession session) {
	
		// resultList = null;
		log.debug("Default sortDirection " + sortDirection);
		sortDirection = "0";
		log.debug("default sortDirection " + sortDirection);
		pageNo = 0;
		session.setAttribute("searchType", "");

	}

	public String getChangeMemberRoleForProctorTemplate() {
		return changeMemberRoleForProctorTemplate;
	}

	public void setChangeMemberRoleForProctorTemplate(String changeMemberRoleForProctorTemplate) {
		this.changeMemberRoleForProctorTemplate = changeMemberRoleForProctorTemplate;
	}

	public ProctorService getProctorService() {
		return proctorService;
	}

	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	
}