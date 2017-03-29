package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.UserSort;

/**
 * @author arijit dutta
 * @modified Faisal A. Siddiqui 
 *	{@link} http://jira.360training.com/browse/LMS-5155
 */
public class ManageOraganizationGroupsController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageOraganizationGroupsController.class.getName());

	private String manageOrganisationGroupTemplate = null;
	private String viewOrganisationGroupMembersTemplate = null;
	private String changeGroupMembersTemplate=null;
	private String orgredirectTemplate;
	private String searchLearnerTemplate = null;
	private String updateOrgGroupTemplate = null;

	private static final String MANAGE_USER_DEFAULT_ACTION = "default";
	private static final String MANAGE_USER_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_USER_PAGE_SEARCH_ACTION = "pageSearch";
	private static final String MANAGE_USER_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String MANAGE_USER_DELETE_LEARNER_ACTION = "delete";
	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String MANAGE_USER_SORT_LASTTNAME = "lastName";
	private static final String MANAGE_USER_SORT_EMAIL = "emailAddress";
	private static final String MANAGE_USER_SORT_LEARNER_ACTION = "sort";
	private static final String MANAGE_ORGGROUP_UPDATELEARNER_ACTION = "update";
	private static final String MANAGE_GROUP_DELETE_LEARNERGROUP_ACTION = "deleteLearnerGroup";
	private static final String MANAGE_GROUP_DELETE_ORGGROUP_ACTION = "deleteOrgGroup";
	private static final String MANAGE_GROUP_ADD_LEARNERROUP_ACTION = "addLearner";
	private static final String MANAGE_USER_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_USER_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_USER_PAGE_SIZE =10;

	private static final String CHANGE_GROUP_DEFAULT_ACTION = "default";
	private static final String CHANGE_GROUP_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String CHANGE_GROUP_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String CHANGE_GROUP_PREVPAGE_DIRECTION = "prev";
	private static final String CHANGE_GROUP_NEXTPAGE_DIRECTION = "next";
	private static final int CHANGE_GROUP_PAGE_SIZE = 10;
	private static final String CHANGE_GROUP_ALL_SEARCH_ACTION = "allSearch";
	private static final String CHANGE_GROUP_SORT_FIRSTNAME = "firstName";
	private static final String CHANGE_GROUP_SORT_LEARNER_ACTION = "sort";

	private LearnerService learnerService;
	private EntitlementService entitlementService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VU360UserService vu360UserService;

	/**
	 * added by arijit
	 * this method manages OrganisationGroup
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView manageOrganizationGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			//SecurityContextHolder.getContext().getAuthentication().
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			//ManageOrganizationalGroups mngOrzGroup = new ManageOrganizationalGroups();
			//List<VU360User> listVU360User=learnerService.findLearner("", user);
			String action = request.getParameter("action");
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			OrganizationalGroup rootOrgGroup=orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)){
					String[] selectedOrgGroupValues = request.getParameterValues("groups");
					if( selectedOrgGroupValues != null ){
						for( int i=0; i<selectedOrgGroupValues.length; i++ ) {
							String orgGroupID = selectedOrgGroupValues[i];
							if( StringUtils.isNotBlank(orgGroupID)) {
								//List<Learner> listLearners = new ArrayList <Learner>();
								//OrganizationalGroup orgGroup = learnerService.loadForUpdateOrganizationalGroup(Long.valueOf(orgGroupID));
								//listLearners = mngOrzGroup.getMembersbyOrgGroup(orgGroup, listVU360User);
								//log.debug("orgGroup"+orgGroup.getName());
								//learnerService.deleteOrgGroup(listLearners,orgGroup);
							}	
						}		
					}
				}else if(action.equalsIgnoreCase(MANAGE_GROUP_DELETE_LEARNERGROUP_ACTION)){
					String[] selectedLearnerGroupValues = request.getParameterValues("learnerGroupCheck");
					long learnerGroupIdArray[] = new long[selectedLearnerGroupValues.length];

					log.debug("LEARNERGroup "+selectedLearnerGroupValues.length);
					if( selectedLearnerGroupValues != null ){
						for( int i=0; i<selectedLearnerGroupValues.length; i++ ) {
							String learnerGroupID = selectedLearnerGroupValues[i];

							if( StringUtils.isNotBlank(learnerGroupID) ) {
								learnerGroupIdArray[i]=Long.parseLong(selectedLearnerGroupValues[i]);
							}	
						}		
						orgGroupLearnerGroupService.deleteLearnerGroups(learnerGroupIdArray);
						rootOrgGroup=orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
					}
				}else if(action.equalsIgnoreCase(MANAGE_GROUP_DELETE_ORGGROUP_ACTION)){
					String[] selectedOrgGroupValues = request.getParameterValues("orgGroups");
					if( selectedOrgGroupValues != null ){

						long orgGroupIdArray[] = new long[selectedOrgGroupValues.length];
						log.debug("OrgGroup "+selectedOrgGroupValues.length);

						if (validateDeleteData(selectedOrgGroupValues,rootOrgGroup,context)){

							for( int i=0; i<selectedOrgGroupValues.length; i++ ) {
								String orgGroupID = selectedOrgGroupValues[i];

								if( orgGroupID != null ) {
									orgGroupIdArray[i]=Long.parseLong(selectedOrgGroupValues[i]);
								}	
							}		
							orgGroupLearnerGroupService.deleteOrgGroups(orgGroupIdArray);
							rootOrgGroup=orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
						}
					}else{
						context.put("validateOrgGroup", "error.orgGroup.NotSelected");
					}
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,  null,user);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			Map<Long ,Long> orgGroupLearnerCountMap = orgGroupLearnerGroupService.getLearnerCountOfOrgGroups(customerId);
			context.put("orgGroupTreeAsList", treeAsList);
			context.put("orgGroupLearnerCountMap", orgGroupLearnerCountMap);
			return new ModelAndView(manageOrganisationGroupTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageOrganisationGroupTemplate);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private boolean validateData(List<String> orgGroupNames,Customer customer,Map context){

		String orgGroupNameArray[] = new String[orgGroupNames.size()];
		String existingOrgGroupNames="";
		for(int index=0;index<orgGroupNames.size();index++){

			orgGroupNameArray[index]=orgGroupNames.get(index);
		}
		List<OrganizationalGroup> existingOrgGroups=orgGroupLearnerGroupService.getOrgGroupByNames(orgGroupNameArray, customer);
		if(existingOrgGroups !=null)
			if (existingOrgGroups.size()>0 ){

				for(OrganizationalGroup orgGroup:existingOrgGroups){
					if(!existingOrgGroupNames.contains(orgGroup.getName()))
						existingOrgGroupNames = existingOrgGroupNames  + orgGroup.getName() + " , ";
				}
				existingOrgGroupNames=existingOrgGroupNames.substring(0, existingOrgGroupNames.length()-2);
				context.put("validateOrgGroup", "error.OrgGroup.exists");
				context.put("duplicateOrgGroups", existingOrgGroupNames);
				return false;
			}
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean validateDeleteData(String orgGroupIdArray[],OrganizationalGroup rootOrgGroup,Map context){

		boolean bRet=true;
		for(int index =0; index<orgGroupIdArray.length;index++){

			if(rootOrgGroup.getId()==Long.parseLong(orgGroupIdArray[index])){
				context.put("validateOrgGroup", "error.rootOrgGroup.delete");
				bRet= false;
				break;
			}
		}
		List<OrganizationalGroup> organizationalGroups = orgGroupLearnerGroupService.getOrgGroupsById(orgGroupIdArray);
		List<LearnerGroup> learnerGroups = null;
		List<Learner> learners = null;
		for(OrganizationalGroup organizationalGroup: organizationalGroups){
			learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(organizationalGroup.getId());
			//if(organizationalGroup.getLearnerGroups() !=null && organizationalGroup.getLearnerGroups().size()>0){
			if(CollectionUtils.isNotEmpty(learnerGroups)){
				context.put("validateOrgGroup", "error.orgGroup.learnerGroup.exists");
				bRet= false;
				break;
			}
			if(organizationalGroup.getChildrenOrgGroups() !=null && organizationalGroup.getChildrenOrgGroups().size()>0){
				context.put("validateOrgGroup", "error.orgGroup.childGroup.exists");
				bRet= false;
				break;
			}
			learners = orgGroupLearnerGroupService.getLearnersByOrgGroupId(organizationalGroup.getId());
			if(CollectionUtils.isNotEmpty(learners)){
				context.put("validateOrgGroup", "error.orgGroup.learner.exists");
				bRet= false;
				break;
				
			}
			
			if( entitlementService.getOrgGroupEntitlementByOrgGroupId(organizationalGroup.getId()) != null){
				context.put("validateOrgGroup", "error.orgGroup.orgGroupEntitlements.exists");
				bRet= false;
				break;
			}

		}
		return bRet;
	}


	public ModelAndView updateOrgGroup(HttpServletRequest request,
			HttpServletResponse response){
		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<Object, Object> context = new HashMap<Object, Object>();

			String orgGroupId = request.getParameter("orgGroupId");
			String orgGroupName=request.getParameter("orgGroupName");
			String action = request.getParameter("action");
			Long customerId = null;
			if (loggedInUser.isLMSAdministrator())
				customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			else
				customerId = loggedInUser.getLearner().getCustomer().getId();
			if( orgGroupId == null ){
				return new ModelAndView(orgredirectTemplate);
			}
			if(action == null){
				OrganizationalGroup organizationalGroup =learnerService.getOrganizationalGroupById(Long.parseLong(orgGroupId));
				if(StringUtils.isNotBlank(orgGroupId)){
					if(organizationalGroup.getCustomer().getId().equals(customerId)){
						context.put("organizationalGroup", organizationalGroup);
						return new ModelAndView(this.updateOrgGroupTemplate, "context", context);
					}
					else{
						return new ModelAndView(orgredirectTemplate);
					}
				}
			}else{
				OrganizationalGroup orgGroup =learnerService.loadForUpdateOrganizationalGroup(Long.parseLong(orgGroupId));
				if(StringUtils.isNotBlank(orgGroupId)){
					if(orgGroup.getCustomer().getId().equals(customerId)){
						orgGroup.setName(orgGroupName);
						if(validateEditData(orgGroup,context)){

							//organizationalGroup.setName(orgGroupName);
							orgGroup=orgGroupLearnerGroupService.saveOrganizationalGroup(orgGroup);
							return new ModelAndView(orgredirectTemplate);
						}
						else{
							context.put("organizationalGroup", orgGroup);
							//return new ModelAndView(learnerGroupRedirectTemplate);
							return new ModelAndView(this.updateOrgGroupTemplate, "context", context);
						}
					}
				}
				return new ModelAndView(orgredirectTemplate);
			}
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(orgredirectTemplate);
	}

	private boolean validateEditData(OrganizationalGroup organizationalGroup, Map<Object, Object> context  ){

		boolean isValid = true;

		if (StringUtils.isBlank(organizationalGroup.getName()))
		{
			//errors.rejectValue("emailAddress", "error.email.required");
			context.put("validateOrgGroup", "error.orgGroupName.required");
			isValid = false;
		}
		/*String orgGroupNameArray[] = new String[1];
		orgGroupNameArray[0]=organizationalGroup.getName();
		List<OrganizationalGroup> newOrgGroup=orgGroupLearnerGroupService.getOrgGroupByNames(orgGroupNameArray, customer);
		if(newOrgGroup !=null){
			if (newOrgGroup.size()>0 || newOrgGroup.size()>1){
				context.put("validateOrgGroup", "error.OrgGroup.exists");
				return false;
			}else if(newOrgGroup.size()==1){
				if(newOrgGroup.get(0).getId() != organizationalGroup.getId()){
					context.put("validateOrgGroup", "error.OrgGroup.exists");
					isValid= false;
				}
			}
		}*/
		return isValid;
	}


	/**
	 * @added by arijit
	 * this method views OrganisationGroup members
	 * @modified by Dyutiman 29 oct
	 * 
	 */
	public ModelAndView viewOrganizationGroupMembers(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession();

			String orgGroupId = request.getParameter("orgGroupId");
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");
			String direction = request.getParameter("direction");
			String searchType = request.getParameter("searchType");
			String pageIndex = request.getParameter("pageIndex");
			String prevAct = request.getParameter("prevAct");
			if( prevAct == null ) prevAct = "";

			session.setAttribute("orgGroupId",orgGroupId);
			int pageNo = 0;
			int recordShowing = 0;

			if( StringUtils.isBlank(sortDirection) ) {
				if( session.getAttribute("orgGroupSortDirection") == null ) {
					sortDirection = "0";
				}
				else{
					sortDirection=session.getAttribute("orgGroupSortDirection").toString();
				}
			}
			searchType = (searchType == null) ? MANAGE_USER_PAGE_SEARCH_ACTION : searchType;
			sortBy = (sortBy == null) ? "firstName" : sortBy;
			pageIndex = (pageIndex==null) ? "0" : pageIndex;
			direction = (direction == null) ? "prev" : direction;

			if( StringUtils.isNotBlank(orgGroupId) ) {

				if( session.getAttribute("prevSortBy") != null && session.getAttribute("prevAct") != null && 
						session.getAttribute("prevSortBy").toString().equalsIgnoreCase(sortBy) &&
							( session.getAttribute("prevAct").toString().equalsIgnoreCase("PrevNext") || 
								session.getAttribute("prevAct").toString().equalsIgnoreCase("All") ) )
				{
					if (sortDirection.equalsIgnoreCase("0"))
						sortDirection = "1";
					else
						sortDirection="0";
				}

				String action = request.getParameter("action");
				if (action == null) action = "default";
				
				if( action.equalsIgnoreCase("default") && session.getAttribute("orgGroupSortDirection") != null ) {
					sortDirection = session.getAttribute("orgGroupSortDirection").toString();
				}
				// not loadForUpdate is not required after LMS-6744 refactoring
				//OrganizationalGroup orgGroup = learnerService.loadForUpdateOrganizationalGroup(Long.valueOf(orgGroupId));//getOrganizationalGroupById(Long.valueOf(orgGroupId));
				//List<Learner> learnerList = orgGroup.getMembers();
				OrganizationalGroup orgGroup = orgGroupLearnerGroupService.getOrganizationalGroupById(Long.valueOf(orgGroupId));
				List<Learner> learnerList = orgGroupLearnerGroupService.getLearnersByOrgGroupId(Long.valueOf(orgGroupId));
				UserSort userSort = new UserSort();
				userSort.setSortBy(sortBy);
				userSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(learnerList,userSort);
				List<Learner> finalLearnerList=null;

				if( StringUtils.isNotBlank(action)) {
					
					if( action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) ) {

						String[] selectedLearnerValues = request.getParameterValues("learner");
						Long learnerIdArray[] = new Long[selectedLearnerValues.length];

						if( selectedLearnerValues != null ){
							for( int i=0 ; i<selectedLearnerValues.length ; i++ ) {
								String learnerID = selectedLearnerValues[i];

								if( StringUtils.isNotBlank(learnerID)) {
									learnerIdArray[i]=Long.parseLong(selectedLearnerValues[i]);
								}	
							}		
							orgGroupLearnerGroupService.deleteLearnersFromOrgGroup(learnerIdArray, orgGroup);
						}
						//orgGroup = learnerService.loadForUpdateOrganizationalGroup(Long.valueOf(orgGroupId));
						//learnerList = orgGroup.getMembers();
						orgGroup = orgGroupLearnerGroupService.getOrganizationalGroupById(Long.valueOf(orgGroupId));
						learnerList = orgGroupLearnerGroupService.getLearnersByOrgGroupId(Long.valueOf(orgGroupId));
						userSort.setSortBy(sortBy);
						userSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(learnerList,userSort);
						pageNo = 0;
						if( learnerList.size() > MANAGE_USER_PAGE_SIZE )
							finalLearnerList=learnerList.subList(0, learnerList.size());

					}else if( action.equalsIgnoreCase(MANAGE_GROUP_ADD_LEARNERROUP_ACTION) ) {
						// do nothing
					}

					if( searchType.equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION) ) {

						recordShowing = learnerList.size();

						if( action.equalsIgnoreCase("sort") ) {
							sortDirection= request.getParameter("sortDirection");
							session.setAttribute("orgGroupSortDirection", sortDirection);

							if( sortDirection.equalsIgnoreCase("0") )
								sortDirection = "1";
							else
								sortDirection="0";
							session.setAttribute("previousAction", "sort");
							
						}
						finalLearnerList = learnerList.subList(0, learnerList.size());
						context.put("orgGroup",orgGroup);
						context.put("totalRecord",recordShowing);
						context.put("recordShowing",recordShowing);
						context.put("pageNo",pageNo);
						context.put("listUsers",finalLearnerList);
						context.put("sortDirection", sortDirection );
						context.put("sortBy", sortBy);
						context.put("direction", direction);
						context.put("searchType", searchType);
						session.setAttribute("prevSortBy", sortBy);
						session.setAttribute("prevAct", prevAct);
						
						return new ModelAndView(viewOrganisationGroupMembersTemplate, "context", context);
					}
					if( action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) ) {

						String[] selectedLearnerValues = request.getParameterValues("learner");
						Long learnerIdArray[] = new Long[selectedLearnerValues.length];

						if( selectedLearnerValues != null ){
							for( int i=0 ; i<selectedLearnerValues.length ; i++ ) {
								String learnerID = selectedLearnerValues[i];

								if( StringUtils.isNotBlank(learnerID)) {
									learnerIdArray[i]=Long.parseLong(selectedLearnerValues[i]);
								}	
							}		
							orgGroupLearnerGroupService.deleteLearnersFromOrgGroup(learnerIdArray, orgGroup);
						}
						//orgGroup = learnerService.loadForUpdateOrganizationalGroup(Long.valueOf(orgGroupId));
						//learnerList = orgGroup.getMembers();
						orgGroup = orgGroupLearnerGroupService.getOrganizationalGroupById(Long.valueOf(orgGroupId));
						learnerList = orgGroupLearnerGroupService.getLearnersByOrgGroupId(Long.valueOf(orgGroupId));
						userSort.setSortBy(sortBy);
						userSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(learnerList,userSort);
						pageNo = 0;
						if( learnerList.size() > MANAGE_USER_PAGE_SIZE )
							finalLearnerList=learnerList.subList(0, learnerList.size());

					}else if( action.equalsIgnoreCase(MANAGE_GROUP_ADD_LEARNERROUP_ACTION) ) {
						// do nothing
					}

					if( searchType.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {
						pageNo = 0;
						if( learnerList.size() > MANAGE_USER_PAGE_SIZE )
							finalLearnerList = learnerList.subList(0, learnerList.size());

					} else {
						//pagination serach
						if(direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION)){

							if( session.getAttribute("previousAction") != null &&
									session.getAttribute("previousAction").toString().equalsIgnoreCase("sort") ) {
								session.setAttribute("previousAction", "paging");
							}
							pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
							pageNo = (pageNo <= 0)?0:pageNo-1;

						}else if(direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION)){

							if( session.getAttribute("previousAction") != null &&
									session.getAttribute("previousAction").toString().equalsIgnoreCase("sort") ) {
								session.setAttribute("previousAction", "paging");
							}
							pageIndex = request.getParameter("pageIndex");
							pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
							if( action.equalsIgnoreCase("sort") ) {
								pageNo = (pageNo<=0)?0:pageNo-1;
							}
							pageNo = (pageNo<0)?0:pageNo+1;
						}
					}
					int totalRecord = 0;

					if( learnerList != null && learnerList.size() > 0 ) {
						if( learnerList.size() < (pageNo+1)*MANAGE_USER_PAGE_SIZE ) {
							finalLearnerList = learnerList.subList(pageNo*MANAGE_USER_PAGE_SIZE ,learnerList.size());
							recordShowing = learnerList.size();
							
						}else if( learnerList.size() < MANAGE_USER_PAGE_SIZE ) {
							recordShowing = learnerList.size();
							finalLearnerList = learnerList.subList(0 ,learnerList.size());

						}else{
							recordShowing = (pageNo+1)*MANAGE_USER_PAGE_SIZE;
							finalLearnerList = learnerList.subList(pageNo*MANAGE_USER_PAGE_SIZE ,( pageNo+1) * MANAGE_USER_PAGE_SIZE);

						}
						totalRecord = learnerList.size();
					}

					if (searchType.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION))
						recordShowing=totalRecord;
					if(action.equalsIgnoreCase("sort") ){
						session.setAttribute("orgGroupSortDirection", sortDirection);

						if (sortDirection.equalsIgnoreCase("0"))
							sortDirection = "1";
						else
							sortDirection="0";
						session.setAttribute("previousAction", "sort");
					}
					context.put("orgGroup",orgGroup);
					context.put("totalRecord",totalRecord);
					context.put("recordShowing",recordShowing);
					context.put("pageNo",pageNo);
					context.put("listUsers",finalLearnerList);
					context.put("sortDirection", sortDirection );
					context.put("sortBy", sortBy);
					context.put("direction", direction);
					context.put("searchType", searchType);
					session.setAttribute("prevSortBy", sortBy);
					session.setAttribute("prevAct", prevAct);
				}
			}
			return new ModelAndView(viewOrganisationGroupMembersTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewOrganisationGroupMembersTemplate);
	}


	@SuppressWarnings("unchecked")
	public ModelAndView searchLearner(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<VU360User> resultList = new ArrayList<VU360User>();

			/*
			 * Get logged in user
			 */
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			
			boolean hasAdministratorRole = vu360UserService.hasAdministratorRole(loggedInUser);
			boolean hasTrainingAdministratorRole = vu360UserService.hasTrainingAdministratorRole(loggedInUser);
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession();
			String action = null;
			if(request.getParameter("action") != null) {
				action = request.getParameter("action").trim();
			}
			action = (action==null)?MANAGE_USER_DEFAULT_ACTION:action;
			String sortBy = MANAGE_USER_SORT_FIRSTNAME;
			int sortDirection = 1; //ascending
			Map<String,Integer> fieldSortDirections = new HashMap<String,Integer>();
			fieldSortDirections.put(MANAGE_USER_SORT_FIRSTNAME, 1);
			fieldSortDirections.put(MANAGE_USER_SORT_LASTTNAME, 1);
			fieldSortDirections.put(MANAGE_USER_SORT_EMAIL, 1);
			log.debug("action--------"+action);
			if(action.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION)){
				String searchKey = request.getParameter("searchkey").trim();
				resultList = learnerService.findLearner(searchKey,
						hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId());
			}else if(action.equalsIgnoreCase(MANAGE_USER_ADVANCED_SEARCH_ACTION)){
				String firstName = request.getParameter("firstname").trim();
				String lastName = request.getParameter("lastname").trim();
				String emailAddress= request.getParameter("emailaddress").trim();
				resultList = learnerService.findLearner(firstName,lastName,emailAddress,
						hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId());
			}else if(action.equalsIgnoreCase(MANAGE_USER_SORT_LEARNER_ACTION)){
//				resultList=(List<VU360User>)session.getAttribute("searchedLearnerList");
				if(request.getParameter("direction")!=null){
					try{
						sortDirection = Integer.parseInt(request.getParameter("direction"));
					}catch(NumberFormatException nex){}
					sortDirection = (sortDirection>0)?1:0;
				}
				if(request.getParameter("sortby").equalsIgnoreCase(MANAGE_USER_SORT_FIRSTNAME)){
					sortBy = MANAGE_USER_SORT_FIRSTNAME;
					fieldSortDirections.put(MANAGE_USER_SORT_FIRSTNAME, (1-sortDirection));
				}else if(request.getParameter("sortby").equalsIgnoreCase(MANAGE_USER_SORT_LASTTNAME)){
					sortBy = MANAGE_USER_SORT_LASTTNAME;
					fieldSortDirections.put(MANAGE_USER_SORT_LASTTNAME, (1-sortDirection));
				}else if(request.getParameter("sortby").equalsIgnoreCase(MANAGE_USER_SORT_EMAIL)){
					sortBy = MANAGE_USER_SORT_EMAIL;
					fieldSortDirections.put(MANAGE_USER_SORT_EMAIL, (1-sortDirection));
				}
			}else if(action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)){

				//List<VU360User> searchedlearners=(List<VU360User>)session.getAttribute("searchedLearnerList");
				//TODO need to revisit
				/*for(VU360User user : searchedlearners){
					String userId = user.getId().toString();
					String selectedLearnerId = request.getParameter(userId);
					if(selectedLearnerId!=null){
						learnerService.deleteLearner(user.getLearner());
					}else{
						resultList.add(user);
					}
				}*/
			}else if(action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION)){
				//resultList = null;
			}else if(action.equalsIgnoreCase(MANAGE_ORGGROUP_UPDATELEARNER_ACTION)){
				log.debug("request"+request);
				String[] selectedLearnerValues = request.getParameterValues("selectedLearner");
				log.debug("arijit-action"+selectedLearnerValues.length);
				List<Learner> listSelectedLearner = new ArrayList();
				if( selectedLearnerValues != null ){
					for( int i=0; i<selectedLearnerValues.length; i++ ) {
						String LearnerID = selectedLearnerValues[i];
						if( StringUtils.isNotBlank(LearnerID)) {
							Learner learner = learnerService.getLearnerByID(Long.valueOf(LearnerID));
							listSelectedLearner.add(learner);
						}	
					}		
				}
				if(session.getAttribute("learnerGroupId") != null){
					learnerService.addLearnersInLearnerGroup(listSelectedLearner,learnerService.getLearnerGroupById(Long.valueOf((String)session.getAttribute("learnerGroupId"))));
				}else if(session.getAttribute("OrgGroupId") != null){
					log.debug("arijit"+listSelectedLearner);
					// no more required after LMS-6744
					//learnerService.addLearnerInOrgGroup(listSelectedLearner, learnerService.loadForUpdateOrganizationalGroup(Long.valueOf((String)session.getAttribute("OrgGroupId"))));
					learnerService.addLearnersInOrgGroup(selectedLearnerValues, Long.valueOf((String)session.getAttribute("OrgGroupId")));
				}
			}

//			session.setAttribute("searchedLearnerList", resultList);
			context.put("members", resultList);
			context.put("totalRecord", resultList.size());
			context.put("sortby", sortBy);
			context.put("sortdirection", sortDirection==1?"asc":"desc");
			context.put("sortdirectionmap", fieldSortDirections);

			return new ModelAndView(searchLearnerTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchLearnerTemplate);
	}

	@SuppressWarnings("unused")
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<OrganizationalGroup> selectedLearnerOrgGroups, List<OrganizationalGroup> managableOrgGroups){

		if( orgGroup != null ) {
			TreeNode node = new TreeNode(orgGroup);

			if( selectedLearnerOrgGroups != null ) {
				for( int orgGroupNum = 0; orgGroupNum < selectedLearnerOrgGroups.size(); orgGroupNum ++ ) {
					if(selectedLearnerOrgGroups.get(orgGroupNum).getId().equals(orgGroup.getId())) {
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
			for( int i = 0; i < childGroups.size(); i++ ) {
				node = getOrgGroupTree(node, childGroups.get(i), selectedLearnerOrgGroups, managableOrgGroups);
			}
			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView changeGroup(HttpServletRequest request,	HttpServletResponse response) {
		try {
			//List<VU360User> resultList = new ArrayList<VU360User>();
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			// Get logged in user
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			
			boolean hasAdministratorRole = vu360UserService.hasAdministratorRole(loggedInUser);
			boolean hasTrainingAdministratorRole = vu360UserService.hasTrainingAdministratorRole(loggedInUser);
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("userData", loggedInUser);

			String orgGroupId = request.getParameter("orgGroupId");
			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress = request.getParameter("emailaddress");
			String searchKey = request.getParameter("searchkey");
			String action = request.getParameter("action");
			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");

			log.debug("First sortDirection  " + sortDirection);
			int pageNo = 0;
			int recordShowing = 0;
			action = (action==null)?CHANGE_GROUP_DEFAULT_ACTION:action;
			firstName = (firstName==null)?"":firstName;
			lastName = (lastName==null)?"":lastName;
			emailAddress = (emailAddress==null)?"":emailAddress;
			searchKey = (searchKey==null)?"":searchKey;

			HttpSession session = request.getSession();

			orgGroupId = session.getAttribute("orgGroupId").toString();

			if (sortBy == null && action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) == false ){

				session.setAttribute("searchedFirstName", firstName);
				session.setAttribute("searchedLastName", lastName);
				session.setAttribute("searchedEmailAddress", emailAddress);
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo",pageNo);
			}
			direction = (direction==null)?CHANGE_GROUP_PREVPAGE_DIRECTION:direction;
			pageIndex = (pageIndex==null)?"0":pageIndex;
			sortBy = (sortBy==null)?CHANGE_GROUP_SORT_FIRSTNAME:sortBy;

			if( sortDirection == null ) {
				sortDirection = "0";
			} else if(!action.equalsIgnoreCase(CHANGE_GROUP_SORT_LEARNER_ACTION) && session.getAttribute("prevSortDirection") != null) {
				sortDirection = session.getAttribute("prevSortDirection").toString();
			} else {
				//do nothing... sortDirection = sortDirection;
			}

			/*
			 * simple search is no more used...
			 */
			if( action.equalsIgnoreCase(CHANGE_GROUP_SIMPLE_SEARCH_ACTION) ) {
				session.setAttribute("searchType", CHANGE_GROUP_SIMPLE_SEARCH_ACTION);
				if(direction.equalsIgnoreCase(CHANGE_GROUP_PREVPAGE_DIRECTION)){
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);
				}else if(direction.equalsIgnoreCase(CHANGE_GROUP_NEXTPAGE_DIRECTION)){
					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);
				}
				log.debug("searchType="+action + " searchedSearchKey " + session.getAttribute("searchedSearchKey").toString() + "  direction " + direction + " pageIndex " + pageIndex + "   sortBy "+ firstName + "  sortDirection " + sortDirection);
				session.setAttribute("pageNo",pageNo);
				if( !hasAdministratorRole &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
						results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
								hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
						userList=(List<VU360User>)results.get("list");
					}
				} else {
					results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");
				}

				// advanced search...
			} else if( action.equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION) ) {

				log.debug("Result Size = " + userList.size());
				session.setAttribute("searchType", CHANGE_GROUP_ADVANCED_SEARCH_ACTION);
				if( direction.equalsIgnoreCase(CHANGE_GROUP_PREVPAGE_DIRECTION) ) {

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);

				}else if( direction.equalsIgnoreCase(CHANGE_GROUP_NEXTPAGE_DIRECTION) ) {

					pageIndex = request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);
				}
				session.setAttribute("pageNo",pageNo);	

				results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
						hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
				userList = (List<VU360User>)results.get("list");
				log.debug("Result Size = " + userList.size());

			} else if( action.equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION) ) {

				session.setAttribute("searchType", CHANGE_GROUP_ALL_SEARCH_ACTION);
				log.debug("searchType " + session.getAttribute("searchType" ));
				pageNo = 0;
				session.setAttribute("pageNo",pageNo);	
				results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
						hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						sortBy, Integer.parseInt(sortDirection));
				userList = (List<VU360User>)results.get("list");
				log.debug("Result Size = " + userList.size());

			} else if( action.equalsIgnoreCase(CHANGE_GROUP_SORT_LEARNER_ACTION) ) {

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION)){

					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				} else if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION)){

					results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							sortBy, Integer.parseInt(sortDirection));
					// results = learnerService.findAllLearners("",loggedInUser,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				} else {
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
				}
				session.setAttribute("prevSortDirection",sortDirection);

			} else if( action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION) ) {

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION)){

					results = learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				} else if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION)){

					results = learnerService.findAllLearnersWithCriteria(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(), 
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							sortBy, Integer.parseInt(sortDirection));
					//results = learnerService.findAllLearners("",loggedInUser,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");

				} else {
					results = learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							hasAdministratorRole, hasTrainingAdministratorRole, loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList = (List<VU360User>)results.get("list");
				}
				if (sortDirection.equalsIgnoreCase("0"))
					sortDirection = "1";
				else
					sortDirection = "0";

			}else if( action.equalsIgnoreCase(MANAGE_ORGGROUP_UPDATELEARNER_ACTION) ) {

				String[] selectedLearnerValues = request.getParameterValues("selectedLearner");
				// no more required after LMS-6744
				//OrganizationalGroup organizationalGroup = learnerService.loadForUpdateOrganizationalGroup(Long.parseLong(orgGroupId));//getOrganizationalGroupById(Long.parseLong(orgGroupId));
//				OrganizationalGroup organizationalGroup = learnerService.getOrganizationalGroupById(Long.parseLong(orgGroupId));
//				log.debug("arijit-action"+selectedLearnerValues.length);
//				List<Learner> listSelectedLearner = new ArrayList();
//				if( selectedLearnerValues != null ){
//					for( int i=0; i<selectedLearnerValues.length; i++ ) {
//						String LearnerID = selectedLearnerValues[i];
//						if( StringUtils.isNotBlank(LearnerID) ) {
//							boolean learnerFound=false;
//							for (int index=0;index<organizationalGroup.getMembers().size();index++){
//								if(organizationalGroup.getMembers().get(index).getKey().equalsIgnoreCase(LearnerID) ){
//									learnerFound=true;
//								}
//							}
//							if(learnerFound==false){
//								Learner learner = learnerService.getLearnerByID(Long.valueOf(LearnerID));
//								listSelectedLearner.add(learner);
//							}
//						}	
//					}		
//				}
				learnerService.addLearnersInOrgGroup(selectedLearnerValues,Long.valueOf(orgGroupId));

				return new ModelAndView(orgredirectTemplate);

			}
			else if(action.equalsIgnoreCase(CHANGE_GROUP_DEFAULT_ACTION)){
				//resultList = null;
				log.debug("Default sortDirection " + sortDirection);
				sortDirection="0";
				log.debug("default sortDirection " + sortDirection);
				pageNo=0;
				session.setAttribute("searchType","");
			}
			//if (pageNo>0)
			//recordShowing = ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(pageNo)*CHANGE_GROUP_PAGE_SIZE;
			//else
			log.debug("before record showing " + results.isEmpty());
			if ( !results.isEmpty() )

				recordShowing = ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(session.getAttribute("pageNo").toString())+1)*CHANGE_GROUP_PAGE_SIZE;		

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION))
					recordShowing=userList.size();
				if (sortDirection.equalsIgnoreCase("0"))
					sortDirection = "1";
				else
					sortDirection="0";
				String totalRecord=	(results.isEmpty())?"0":results.get("recordSize").toString();
				context.put("firstName", session.getAttribute("searchedFirstName"));
				context.put("lastName", session.getAttribute("searchedLastName"));
				context.put("emailAddress", session.getAttribute("searchedEmailAddress"));
				context.put("searchKey", session.getAttribute("searchedSearchKey"));
				context.put("searchType", session.getAttribute("searchType"));
				context.put("direction", direction);
// 				session.setAttribute("searchedLearnerList",userList);
				context.put("members", userList);
				context.put("totalRecord", Integer.parseInt(totalRecord));
				context.put("recordShowing", recordShowing);
				context.put("pageNo", session.getAttribute("pageNo"));
				context.put("sortDirection", sortDirection );
				context.put("sortBy", sortBy);

				return new ModelAndView(changeGroupMembersTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(changeGroupMembersTemplate);
	}



	public String getSearchLearnerTemplate() {
		return searchLearnerTemplate;
	}

	public void setSearchLearnerTemplate(String searchLearnerTemplate) {
		this.searchLearnerTemplate = searchLearnerTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getViewOrganisationGroupMembersTemplate() {
		return viewOrganisationGroupMembersTemplate;
	}

	public void setViewOrganisationGroupMembersTemplate(
			String viewOrganisationGroupMembersTemplate) {
		this.viewOrganisationGroupMembersTemplate = viewOrganisationGroupMembersTemplate;
	}

	public String getManageOrganisationGroupTemplate() {
		return manageOrganisationGroupTemplate;
	}

	public void setManageOrganisationGroupTemplate(String manageOrganisationGroupTemplate) {
		this.manageOrganisationGroupTemplate = manageOrganisationGroupTemplate;
	}

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getChangeGroupMembersTemplate() {
		return changeGroupMembersTemplate;
	}

	public void setChangeGroupMembersTemplate(String changeGroupMembersTemplate) {
		this.changeGroupMembersTemplate = changeGroupMembersTemplate;
	}

	public String getOrgredirectTemplate() {
		return orgredirectTemplate;
	}

	public void setOrgredirectTemplate(String orgredirectTemplate) {
		this.orgredirectTemplate = orgredirectTemplate;
	}

	public String getUpdateOrgGroupTemplate() {
		return updateOrgGroupTemplate;
	}

	public void setUpdateOrgGroupTemplate(String updateOrgGroupTemplate) {
		this.updateOrgGroupTemplate = updateOrgGroupTemplate;
	}

	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
}	