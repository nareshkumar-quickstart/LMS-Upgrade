package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.model.ManageOrganizationalGroups;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeCourseGroupTree;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.UserSort;
import com.softech.vu360.util.VU360Branding;

/**
 * @author sourav
 *
 */
public class ManageLearnerGroupsController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageLearnerGroupsController.class.getName());

	private static final String ADD_LEARNERGROUP__NEXT_ACTION = "next";
	private static final String ADD_LEARNERGROUP__FINISH_ACTION = "finish";
	private static final String MANAGE_GROUP_DELETE_LEARNERGROUP_ACTION = "deleteLearnerGroup";
	private static final String MANAGE_GROUP_SEARCH_LEARNERGROUP_ACTION = "searchLearnerGroup";
	private static final String CHANGE_GROUP_DEFAULT_ACTION = "default";
	private static final String CHANGE_GROUP_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String CHANGE_GROUP_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String CHANGE_GROUP_PREVPAGE_DIRECTION = "prev";
	private static final String CHANGE_GROUP_NEXTPAGE_DIRECTION = "next";
	private static final int CHANGE_GROUP_PAGE_SIZE = 10;
	private static final String CHANGE_GROUP_ALL_SEARCH_ACTION = "allSearch";
	private static final String CHANGE_GROUP_SORT_FIRSTNAME = "firstName";
	private static final String CHANGE_GROUP_SORT_LEARNER_ACTION = "sort";
	private static final String MANAGE_USER_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_USER_NEXTPAGE_DIRECTION = "next";
	private static final String MANAGE_LEARNERGROUP_UPDATELEARNER_ACTION = "update";
	private static final String MANAGE_USER_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_USER_PAGE_SEARCH_ACTION = "pageSearch";
	private static final String MANAGE_USER_DELETE_LEARNER_ACTION = "delete";
	private static final String MANAGE_GROUP_ADD_LEARNERROUP_ACTION = "addLearner";
	private static final String MANAGE_LEARNERGROUP_PAGING_ACTION = "paging";

	
	
	private String manageLearnerGroupsTemplate=null;
	private String addLearnerGroupTemplate=null;
	private String addLearnerRedirectTemplate=null;
	private String addLearnerGroupsFinishTemplate = null;
	private String saveLearnerGroupTemplate = null;
	private String viewLearnerGroupMembersTemplate = null;
	private String searchLearnerTemplate = null;
	private String addMembersTemplate = null;
	private String learnerGroupRedirectTemplate = null;
	private String updateLearnerGroupTemplate = null;
	private String viewLearnerGroupCoursesTemplate = null;
	private String addCourseTemplate = null;
	
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private LearnerService learnerService;
	private EnrollmentService enrollmentService;
	private VelocityEngine velocityEngine;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private OrganizationalGroup orgGroup = null;
	private VU360UserService vu360UserService;
	
	//Added By Noman
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private EntitlementService entitlementService = null;
	//--------------
	
	@SuppressWarnings("unchecked")
	public ModelAndView displayLearnerGroups(HttpServletRequest request, HttpServletResponse response) {

		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		//loggedInUser =  vu360UserService.getUpdatedUserById(loggedInUser.getId()); //(VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		Customer customer = null;
		String action = request.getParameter("action");
		String sortDirection = request.getParameter("sortDirection");
		String pageIndex = request.getParameter("pageCurrIndex");
		String showAll = request.getParameter("showAll");
		if( showAll == null ) showAll = "false";
		if( pageIndex == null ) pageIndex = "0";
		log.debug("pageIndex = " + pageIndex);
		Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
		PagerAttributeMap.put("pageIndex",pageIndex);
		PagerAttributeMap.put("showAll",showAll);
		String visited = request.getParameter("visited");//checking whether its the first time or not
//		if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
//			sortDirection = session.getAttribute("sortDirection").toString();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
//			 sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		
		if(StringUtils.isBlank(visited)){
				sortDirection ="1";
				sortColumnIndex ="0";
			}	
		
		if( action == null )
			action = MANAGE_GROUP_SEARCH_LEARNERGROUP_ACTION;
		context.put("showAll", showAll);
		context.put("sortDirection", 0);
		context.put("sortColumnIndex", 0);
		if (loggedInUser.isLMSAdministrator())
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		else
			customer = loggedInUser.getLearner().getCustomer();
		//List<OrganizationalGroup> modifiedOrgGroups=orgGroupLearnerGroupService.getAllOrganizationalGroups(customer);

		if( action.equalsIgnoreCase(MANAGE_GROUP_DELETE_LEARNERGROUP_ACTION) ) {

			String[] selectedLearnerGroupValues = request.getParameterValues("learnerGroupCheck");
			if( selectedLearnerGroupValues != null ){
				long learnerGroupIdArray[] = new long[selectedLearnerGroupValues.length];

				for( int i=0; i<selectedLearnerGroupValues.length; i++ ) {
					String learnerGroupID = selectedLearnerGroupValues[i];

					if( StringUtils.isNotBlank(learnerGroupID) ) {
						learnerGroupIdArray[i]=Long.parseLong(selectedLearnerGroupValues[i]);
					}	
				}		
				orgGroupLearnerGroupService.deleteLearnerGroups(learnerGroupIdArray);
			}
			return showLearnerGroups(request, context, PagerAttributeMap, customer, loggedInUser, sortColumnIndex, sortDirection);
		} else if ( action.equalsIgnoreCase(MANAGE_GROUP_SEARCH_LEARNERGROUP_ACTION) ) {
			return showLearnerGroups(request, context, PagerAttributeMap, customer, loggedInUser, sortColumnIndex, sortDirection);
		} else if(action.equalsIgnoreCase(MANAGE_LEARNERGROUP_PAGING_ACTION)) {
			return showLearnerGroups(request, context, PagerAttributeMap, customer, loggedInUser, sortColumnIndex, sortDirection);
		} 
		context.put("userData", loggedInUser);
		return new ModelAndView(manageLearnerGroupsTemplate, "context", context);
	}



	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}



	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}



	private ModelAndView showLearnerGroups(HttpServletRequest request, Map<Object, Object> context, 
			Map<String, Object> PagerAttributeMap, Customer customer, VU360User loggedInUser, 
			String sortColumnIndex, String sortDirection){
//		HttpSession session = request.getSession();
		String name = request.getParameter("name");
		context.put("name", name);
		context.put("userData", loggedInUser);
		if( !loggedInUser.isLMSAdministrator() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
			if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()==0 ) {				
				return new ModelAndView(manageLearnerGroupsTemplate, "context", context);
			}
		}
		List<LearnerGroup> lnrGroups = orgGroupLearnerGroupService.findLearnerGroupByName("%%", customer, loggedInUser);
		Map<Long,Long> learnerCountMap = orgGroupLearnerGroupService.getLearnerCountOfLearnerGroups(lnrGroups); // LMS-6764
		if( lnrGroups.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", 0);
		}
		request.setAttribute("PagerAttributeMap", PagerAttributeMap);
		// manually sorting
		if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex)) {
			// sorting against learner group name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if( lnrGrp1.getName().toUpperCase().compareTo
										(lnrGrp2.getName().toUpperCase()) > 0 ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if( lnrGrp1.getName().toUpperCase().compareTo
										(lnrGrp2.getName().toUpperCase()) < 0 ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against number of learners	
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if(learnerCountMap.get(lnrGrp1.getId())< learnerCountMap.get(lnrGrp2.getId())) {
								//if( lnrGrp1.getLearners().size() > lnrGrp2.getLearners().size() ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if(learnerCountMap.get(lnrGrp1.getId())< learnerCountMap.get(lnrGrp2.getId())) {
								//if( lnrGrp1.getLearners().size() < lnrGrp2.getLearners().size() ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
				// sorting against organizational group name	
			} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if( lnrGrp1.getOrganizationalGroup().getName().compareTo(lnrGrp2.getOrganizationalGroup().getName()) > 0 ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 2);
				} else {
					for( int i = 0 ; i < lnrGroups.size() ; i ++ ) {
						for( int j = 0 ; j < lnrGroups.size() ; j ++ ) {
							if( i != j ) {
								LearnerGroup lnrGrp1 =  (LearnerGroup) lnrGroups.get(i);
								LearnerGroup lnrGrp2 =  (LearnerGroup) lnrGroups.get(j);
								if( lnrGrp1.getOrganizationalGroup().getName().compareTo(lnrGrp2.getOrganizationalGroup().getName()) < 0 ) {
									LearnerGroup tempLG = lnrGroups.get(i);
									lnrGroups.set(i, lnrGroups.get(j));
									lnrGroups.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 2);
				}
			}
		}
		context.put("learnerGroups", lnrGroups);
		context.put("learnerGroupCount", learnerCountMap); // LMS-6764
		
		return new ModelAndView(manageLearnerGroupsTemplate, "context", context);
	}
	

	@SuppressWarnings("unchecked")
	public ModelAndView addLearnerGroup(HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(); 
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		Map<Object, Object> context = new HashMap<Object, Object>();

		Customer customer = null;
		if (loggedInUser.isLMSAdministrator())
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		else
			customer = loggedInUser.getLearner().getCustomer();

		List<OrganizationalGroup> modifiedOrgGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customer.getId());
		OrganizationalGroup rootGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		context.put("rootGroup", rootGroup);
		ManageOrganizationalGroups mngOrzGroup = new ManageOrganizationalGroups();
		List<Map> groupsInTree = new ArrayList<Map>();
		groupsInTree = mngOrzGroup.createOrgGroupView(rootGroup, null,null);
		if(session.getAttribute("addLGgroupName") != null) {
			context.put("groupName", session.getAttribute("addLGgroupName"));
			for (int i=0; i<groupsInTree.size(); i++) {
				Map orgGroupMap = groupsInTree.get(i);
				orgGroup = (OrganizationalGroup)orgGroupMap.get("value");
				orgGroupMap.put("isChecked", false);
				if(session.getAttribute("addLGgroupName").equals(orgGroup.getName())) {
					orgGroupMap.put("isChecked", true);
					groupsInTree.set(i, orgGroupMap);
				}
			}
		}
		String actionType = request.getParameter("action");
		OrganizationalGroup organizationalGroup = null;

		if( actionType == null && session.getAttribute("addLGorganizationalGroup") != null) {
			organizationalGroup = (OrganizationalGroup) session.getAttribute("addLGorganizationalGroup");
		}

		if( StringUtils.isNotBlank(actionType) ){
			if(actionType.equalsIgnoreCase(ADD_LEARNERGROUP__NEXT_ACTION)){
				String id = request.getParameter("groups");
				if (id != null)
					organizationalGroup = learnerService.getOrganizationalGroupById(Long.valueOf(id));
				String gruopNameText = request.getParameter("groupName");
				if(validateData(gruopNameText,organizationalGroup,context,customer)){
					session.setAttribute("addLGgroupName", gruopNameText);
					session.setAttribute("addLGorganizationalGroup", organizationalGroup);
					return new ModelAndView(addLearnerRedirectTemplate);
				}
			}
		}

		// storing ids of all org groups.
		List<Long> orgGroupIdList = new ArrayList<Long>();

		for( OrganizationalGroup orgGroup : modifiedOrgGroups ){
			Long orgGroupId = orgGroup.getId();
			orgGroupIdList.add(orgGroupId);
		}

		// creating the tree as list
		TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootGroup, orgGroupIdList,loggedInUser);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();
		for( TreeNode node : treeAsList ) {
			if( organizationalGroup != null ) {
				OrganizationalGroup o = (OrganizationalGroup)node.getValue();
				if( o.getId().compareTo(organizationalGroup.getId()) == 0 )
					node.setSelected(true);
			}
		}
		//model.put("orgGroupTreeAsList", treeAsList);
		context.put("OrgGroupView", treeAsList);
		context.put("userData", loggedInUser);
		return new ModelAndView(addLearnerGroupTemplate, "context", context);
	}

	public ModelAndView updateLearnerGroup(HttpServletRequest request,
			HttpServletResponse response){
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();

			String learnerGroupId = request.getParameter("learnerGroupId");
						
			String action = request.getParameter("action");
			
			if(learnerGroupId==null){
				return new ModelAndView(learnerGroupRedirectTemplate);
			}
			
			Customer customer = null;
			if (loggedInUser.isLMSAdministrator())
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = loggedInUser.getLearner().getCustomer();
						
			//Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();			
			//List<OrganizationalGroup> orgGroups = orgGroupLearnerGroupService.getOrgGroupsByCustomer(customer, loggedInUser);
			OrganizationalGroup rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
			
			//Noman, Get the Organization Group Hierarchy
			//TODO
			log.debug("updateLearnerGroup");
						
//			String[] orgGroupList = form.getGroups();
//			
//			if( orgGroupList != null && orgGroupList.length > 0 ){
//				for( String orgGroup : orgGroupList ){
//					Long orgGroupId = Long.parseLong(orgGroup);
//					orgGroupIdList.add(orgGroupId);
//				}
//			}
			
			if( action == null ) {
				LearnerGroup learnerGroup =learnerService.getLearnerGroupById(Long.valueOf(learnerGroupId));//learnerService.loadForUpdateLearnerGroup(Long.parseLong(learnerGroupId)); no need to load for update to display only 
				if( learnerGroup != null ) {
					if(learnerGroup.getCustomer().getId().equals(customer.getId())){
						//Page load mode
						List<Long> selectedOrgGroupIdList = new ArrayList<Long>();
						
						selectedOrgGroupIdList.add(learnerGroup.getOrganizationalGroup().getId());
						
						TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, selectedOrgGroupIdList,loggedInUser);
						List<TreeNode> treeAsList = orgGroupRoot.bfs();
//						for( TreeNode node : treeAsList )
//							log.debug(node.toString());						
						context.put("orgGroupTreeAsList", treeAsList);
						
						context.put("learnerGroup", learnerGroup);
						return new ModelAndView(this.updateLearnerGroupTemplate, "context", context);
					}
					else{
						return new ModelAndView(learnerGroupRedirectTemplate);
					}
				}
			} else {
				LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(Long.parseLong(learnerGroupId));
				if(learnerGroup != null){
					if(learnerGroup.getCustomer().getId().longValue()==customer.getId().longValue()){
						//Saving Mode
						String learnerGroupName=request.getParameter("learnerGroupName");
						//Get the selected Organization Group ID By request Parameter;
						String selectedOrgGroupID = request.getParameter("groups");
						
						OrganizationalGroup selectedOrgGroup = null;
						if(selectedOrgGroupID!=null){
							Long orgGroupId = Long.parseLong(selectedOrgGroupID);
							selectedOrgGroup = learnerService.loadForUpdateOrganizationalGroup(orgGroupId);							
						}
						learnerGroup.setOrganizationalGroup(selectedOrgGroup);
						learnerGroup.setName(learnerGroupName);
						if(validateEditData(learnerGroup,customer,context)){
							//learnerGroup.setName(learnerGroupName);
							

							orgGroupLearnerGroupService.saveLearnerGroup(learnerGroup);
							return new ModelAndView(learnerGroupRedirectTemplate);
						}else{
							//--LMS-9672-----Start
							List<Long> selectedOrgGroupIdList = new ArrayList<Long>();
							selectedOrgGroupIdList.add(learnerGroup.getOrganizationalGroup().getId());
							TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, selectedOrgGroupIdList,loggedInUser);
							List<TreeNode> treeAsList = orgGroupRoot.bfs();
							context.put("orgGroupTreeAsList", treeAsList);
							//--LMS-9672-----END
							
							context.put("learnerGroup", learnerGroup);
							//return new ModelAndView(learnerGroupRedirectTemplate);
							return new ModelAndView(this.updateLearnerGroupTemplate, "context", context);
						}
					}
				}
				return new ModelAndView(learnerGroupRedirectTemplate);
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(updateLearnerGroupTemplate);

	}

	private boolean validateEditData(LearnerGroup learnerGroup,Customer customer,Map<Object, Object> context  ){

		boolean isValid = true;

		if (StringUtils.isBlank(learnerGroup.getName()))
		{
			//errors.rejectValue("emailAddress", "error.email.required");
			context.put("validateLearnerGroupName", "error.learnerGroupName.required");
			isValid= false;
		}
		LearnerGroup newLearnerGroup=learnerService.getLearnerGroupByName(learnerGroup.getName(), customer);
		if (newLearnerGroup !=null){
			if(newLearnerGroup.getId().longValue() != learnerGroup.getId().longValue()){
				context.put("validateDuplicateGroupName", "error.learnerGroupName.existLearnerGroupName");
				isValid= false;
			}
		}
		return isValid;
	}


	public ModelAndView finishAddLearnerGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession(); 
			context.put("groupName", session.getAttribute("addLGgroupName"));
			context.put("selectedGroup",((OrganizationalGroup)session.getAttribute("addLGorganizationalGroup")).getName() );

			String actionType = request.getParameter("action");
			Customer customer = null;
			if (loggedInUser.isLMSAdministrator())
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = loggedInUser.getLearner().getCustomer();
			if( StringUtils.isNotBlank(actionType)){
				if( actionType.equalsIgnoreCase(ADD_LEARNERGROUP__FINISH_ACTION) ){

					LearnerGroup learnerGroup = new LearnerGroup();
					OrganizationalGroup organizationalGroup = (OrganizationalGroup)session.getAttribute("addLGorganizationalGroup");
					learnerGroup.setName((String)session.getAttribute("addLGgroupName"));
					learnerGroup.setOrganizationalGroup(organizationalGroup);
					learnerGroup.setCustomer(customer);
					//organizationalGroup.getLearnerGroups().add(learnerGroup);
					orgGroupLearnerGroupService.saveLearnerGroup(learnerGroup);

					session.setAttribute("addLGorganizationalGroup",null);
					session.setAttribute("addLGgroupName",null);

					return new ModelAndView(learnerGroupRedirectTemplate);
				}
			}
			return new ModelAndView(addLearnerGroupsFinishTemplate, "context", context);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(addLearnerGroupsFinishTemplate);
	}

	private boolean validateData(String learnerGroupName,OrganizationalGroup organizationalGroup,	Map<Object, Object> context  ,Customer customer ){

		boolean isValid=true;
		if (organizationalGroup ==null)
		{
			//errors.rejectValue("emailAddress", "error.email.required");
			context.put("validateOrgGroupName", "error.orgGroupName.required");
			isValid= false;
		}
		if (StringUtils.isBlank(learnerGroupName))
		{
			//errors.rejectValue("emailAddress", "error.email.required");
			context.put("validateLearnerGroupName", "error.learnerGroupName.required");
			isValid= false;
		}
		LearnerGroup learnerGroup=learnerService.getLearnerGroupByName(learnerGroupName, customer);
		if (learnerGroup != null){
			context.put("validateDuplicateGroupName", "error.learnerGroupName.existLearnerGroupName");
			isValid= false;
		}
		return isValid;
	}

	/**
	 * @author Saptarshi
	 */
	public void saveLearnerGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			LearnerGroup learnerGroup = new LearnerGroup();

			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			HttpSession session = request.getSession();
			OrganizationalGroup organizationalGroup = (OrganizationalGroup)session.getAttribute("organizationalGroup");
			learnerGroup.setName((String)session.getAttribute("groupName"));
			learnerGroup.setOrganizationalGroup(organizationalGroup);

			Customer customer = null;
			if (loggedInUser.isLMSAdministrator())
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = loggedInUser.getLearner().getCustomer();

			learnerGroup.setCustomer(customer);
			learnerService.saveLearnerGroup(learnerGroup);

			//customer.getLearnerGroups().add(learnerGroup);
			/*	List<OrganizationalGroup> modifiedOrgGroups=orgGroupLearnerGroupService.getAllOrganizationalGroups(customer);
			Map<Object, Object> context = new HashMap<Object, Object>();
			TreeNode orgGroupRoot  = getOrgGroupTree(null, modifiedOrgGroups.get(0).getRootOrgGroup(), null, null);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			for(TreeNode node:treeAsList )
				log.debug(node.toString());
			context.put("orgGroupTreeAsList", treeAsList);

			//////////////////////////////////

			List<LearnerGroup> learnerGroups=orgGroupLearnerGroupService.getAllLearnerGroups(customer);
			context.put("learnerGroups",learnerGroups);
			//Map<Object, Object> map2 = mngOrzGroup.getNumberofMembersbyLearnerGroup(listVU360User);
			//context.put("countmap2", map2);
			return new ModelAndView(manageOrganisationGroupTemplate, "context", context);*/
			//	return new ModelAndView(manageOrganisationGroupTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
	}

	//Noman Azeem
	//Filters the learner by search call
	private List<Learner> filterLearner(String firstName, String lastName, String emailAddress, List<Learner> allLearners) {
		List<Learner> filteredLearner= new ArrayList<Learner>();
		
		if ((firstName == null || firstName.equals("")) 
			&& (lastName == null || lastName.equals(""))
			&& (emailAddress == null || emailAddress.equals(""))){
		
			//If no search entity is found then return original list
			return allLearners;
		}
		
		for (Learner learner : allLearners) {
			if ( learner.getVu360User().getFirstName().toLowerCase().indexOf(firstName.toLowerCase())>=0 
				&& learner.getVu360User().getLastName().toLowerCase().indexOf(lastName.toLowerCase())>=0  
				&& learner.getVu360User().getEmailAddress().indexOf(emailAddress.toLowerCase())>=0 ) {
				
				//if it is found
				
				filteredLearner.add(learner);
			}
			
		}
		return filteredLearner;
		
	}
	public ModelAndView viewLearnerGroupMembers(HttpServletRequest request,	HttpServletResponse response) {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession();

			session.setAttribute("learnerGroupId",null);
			
			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress= request.getParameter("emailaddress");
			
			String learnerGroupId = request.getParameter("learnerGroupId");
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");
			String direction = request.getParameter("direction");
			String searchType = request.getParameter("searchType");
			String pageIndex = request.getParameter("pageIndex");
			session.setAttribute("learnerGroupId",learnerGroupId);
			
			
			context.put("firstName",firstName);
			context.put("lastName",lastName);
			context.put("emailAddress",emailAddress);
			
			
			int pageNo = 0;
			int recordShowing = 0;

			searchType = (searchType == null) ? MANAGE_USER_PAGE_SEARCH_ACTION : searchType;
			sortBy = (sortBy == null) ? "firstName" : sortBy;
			pageIndex = (pageIndex == null) ? "0" : pageIndex;
			direction = (direction == null) ? "prev" : direction;

			if( StringUtils.isNotBlank(learnerGroupId) ) {
				String action = request.getParameter("action");
				if (StringUtils.isBlank(action)) action = "default";
				if( StringUtils.isNotBlank(action) ) {
					List<Learner> listSelectedLearner = new ArrayList<Learner>();
					if(action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)){
						
						LearnerGroup learnerGroup = null;
						String[] selectedLearnerValues = request.getParameterValues("learner");
						if( selectedLearnerValues != null ){
							Long learnerIdArray[] = new Long[selectedLearnerValues.length];
							//learnerGroup=learnerService.loadForUpdateLearnerGroup(Long.parseLong(learnerGroupId));
							learnerGroup=learnerService.getLearnerGroupById(Long.parseLong(learnerGroupId));

							for( int i=0; i<selectedLearnerValues.length; i++ ) {
								String learnerID = selectedLearnerValues[i];
								if( learnerID != null ) {
									learnerIdArray[i] = Long.parseLong(selectedLearnerValues[i]);
								}	
							}		
							orgGroupLearnerGroupService.deleteLearnersFromLearnerGroup(learnerIdArray, learnerGroup);
						}
						
					}else if(action.equalsIgnoreCase(MANAGE_GROUP_ADD_LEARNERROUP_ACTION)){
						String[] selectedLearnerValues = request.getParameterValues("learner");
						if( selectedLearnerValues != null ){
							for( int i=0; i<selectedLearnerValues.length; i++ ) {
								String LearnerID = selectedLearnerValues[i];
								if( LearnerID != null ) {
									Learner learner = learnerService.getLearnerByID(Long.valueOf(LearnerID));
									listSelectedLearner.add(learner);
								}	
							}		
						}
					}
				}
				//LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(Long.valueOf(learnerGroupId));
				LearnerGroup learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(learnerGroupId));
				if (learnerGroup == null){
					return new ModelAndView(viewLearnerGroupMembersTemplate, "context", context);
				}
				//LMS-6744
				// Modified By MariumSaud : Replace method orgGroupLearnerGroupService.getLearnersByLearnerGroupId(learnerGroup.getId()); with orgGroupLearnerGroupService.getAllLearnersByLearnerGroupId(learnerGroup.getId())
				// Reason : Previous method is making Bulk queries for Learner and then for each learner's user this is causing Proxy Error
				// Resolution : New method will now Used Overlaoded Constructor to populate Learner with only required attributes. 
				List<Learner> allLearners = orgGroupLearnerGroupService.getAllLearnersByLearnerGroupId(learnerGroup.getId());
				List<Learner> learnerList = filterLearner(firstName, lastName, emailAddress, allLearners);
				List<Learner> allLearnerList = learnerList;
				UserSort userSort = new UserSort();
				userSort.setSortBy(sortBy);
				int totalRecord = 0;
				totalRecord = learnerList.size();
				
				if ( searchType.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {
					pageNo = 0;
					if(learnerList.size()<CHANGE_GROUP_PAGE_SIZE)
						learnerList = learnerList.subList(0, learnerList.size());
				} else {
					// pagination search
					if( direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION) ) {
						pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
						pageNo = (pageNo <= 0)?0:pageNo-1;

					} else if( direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION) ) {
						pageIndex = request.getParameter("pageIndex");
						pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
						if( action.equalsIgnoreCase("sort") ) {
							pageNo = (pageNo<=0)?0:pageNo-1;
						}
						pageNo = (pageNo<0)?0:pageNo+1;
						log.debug("page no " + pageNo);
					}
				}
				if( searchType.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {
					recordShowing = totalRecord;
					learnerList = allLearnerList;
				}
				if( action.equalsIgnoreCase("sort") ) {
					//pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					session.setAttribute("learnerGroupSortDirection", sortDirection);
					if (sortDirection.equalsIgnoreCase("0"))
						sortDirection = "1";
					else
						sortDirection = "0";
					userSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(learnerList,userSort);
					session.setAttribute("prevAction", "sort");	
				}
				if( action.equalsIgnoreCase("default") ) {
					if( session.getAttribute("prevAction") != null && 
						session.getAttribute("prevAction").toString().equalsIgnoreCase("sort") ) {
						if( StringUtils.isNotBlank(sortDirection) && !sortDirection.isEmpty() ) {
							userSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(learnerList,userSort);
						}
					}
				}
				if( learnerList != null && learnerList.size() > 0 ) {
					if(learnerList.size()<(pageNo+1)*CHANGE_GROUP_PAGE_SIZE){
						recordShowing = learnerList.size();
						learnerList = learnerList.subList(pageNo*CHANGE_GROUP_PAGE_SIZE ,learnerList.size());
						//learnerList = learnerList.subList(0 ,learnerList.size());						
					}else if(learnerList.size()<CHANGE_GROUP_PAGE_SIZE){
						recordShowing = learnerList.size();
						learnerList = learnerList.subList(0 ,learnerList.size());
					}else{
						if(! searchType.equalsIgnoreCase(MANAGE_USER_SIMPLE_SEARCH_ACTION) ) {
							recordShowing = (pageNo+1)*CHANGE_GROUP_PAGE_SIZE;
							learnerList = learnerList.subList(pageNo*CHANGE_GROUP_PAGE_SIZE ,( pageNo+1 ) * CHANGE_GROUP_PAGE_SIZE);
							//learnerList = learnerList.subList(0 ,learnerList.size());
						}
					}
					//totalRecord = learnerList.size();
				}
				Boolean hasNextPage = false;
				if( ( pageNo + 1 ) * CHANGE_GROUP_PAGE_SIZE < totalRecord )
					hasNextPage = true;

				context.put("learnerGroup",learnerGroup);
				context.put("totalRecord",totalRecord);
				context.put("recordShowing",recordShowing);
				context.put("pageNo",pageNo);
				context.put("hasNextPage",hasNextPage);
				context.put("listUsers",learnerList);
				context.put("sortDirection", sortDirection );
				if( session.getAttribute("learnerGroupSortDirection") == null ) {
					context.put("lastSortDirection", "0" );
				} else {
					context.put("lastSortDirection", session.getAttribute("learnerGroupSortDirection").toString() );
				}
				context.put("lastSortDirection", sortDirection );
				context.put("sortBy", sortBy);
				context.put("direction", direction);
				context.put("searchType", searchType);
			}
			return new ModelAndView(viewLearnerGroupMembersTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewLearnerGroupMembersTemplate);
	}
	
	/**
	 * This method will be shown Learner Group Courses association
	 * Noman Azeem 
	 */
	@SuppressWarnings("unchecked")
	/*public ModelAndView viewLearnerGroupCourses(HttpServletRequest request,	HttpServletResponse response)  {
		
		try
		{
			Map<String, Object> context = new HashMap<String, Object>();
			String learnerGroupId = request.getParameter("learnerGroupId");
			context.put("learnerGroupId", learnerGroupId);			
			LearnerGroup learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(learnerGroupId));
			
			LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(Long.valueOf(learnerGroupId));
			if (learnerGroup == null){
				return new ModelAndView(viewLearnerGroupMembersTemplate, "context", context);
			}
			
			Long entId = Long.parseLong(request.getParameter("Id"));
			CustomerEntitlement customerEntitlement = entitlementService.loadForUpdateCustomerEntitlement(entId);
			
			List<TreeNode> courseGroupTreeNodeList = new ArrayList<TreeNode>(); 
			
			if(customerEntitlement instanceof CourseGroupCustomerEntitlement) {
				List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();			
				CourseGroupCustomerEntitlement courseGroupCustomerEntitlement = (CourseGroupCustomerEntitlement)customerEntitlement;
				List<CourseGroup> courseGroups = courseGroupCustomerEntitlement.getCourseGroups();
				for(CourseGroup courseGroup : courseGroups){
					CourseSort courseSort = new CourseSort();
					List<Course> cgCourses = courseGroup.getCourses();
					List<Course> publishedCourses = new ArrayList<Course>(); 
					if (cgCourses != null && cgCourses.size() > 0) {
						for (Course course : cgCourses) {
							if (course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED)) {
								publishedCourses.add(course);
							}
						}
						
						if (!publishedCourses.isEmpty()) {							
							Collections.sort(publishedCourses, courseSort);
							courseGroup.setCourses(publishedCourses);
						}
					}
					TreeNode courseGroupCourseTreeNode = null;
					boolean courseGroupAdded = false;
					for (TreeNode rootTreeNode : rootNodesReferences) {
						List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
						if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
							courseGroupAdded = true;
							break;
						}
						
						CourseGroup currentCourseGroup = courseGroup;
						while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
							CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
							childCourseGroups.add(currentCourseGroup);
							if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
								courseGroupAdded = true;
								break;
							}
							currentCourseGroup = parentCourseGroup;
						}

						if (courseGroupAdded)
							break;
					}
					if (!courseGroupAdded) {
						courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null);
						rootNodesReferences.add(courseGroupCourseTreeNode);
					}						
				}
				
				boolean courseAdded = false;				
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
						courseAdded = true;
						break;
					}
					
					CourseGroup currentCourseGroup = courseGroup;
					while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}
					if (courseAdded)
						break;
				}
				if (!courseAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}
			}
			
			for (TreeNode rootTreeNode : rootNodesReferences) {
				courseGroupTreeNodeList.addAll(rootTreeNode.bfs());
			}
			context.put("contractType", CustomerEntitlement.COURSE_ENROLLMENTTYPE);
			context.put("treeAsList", courseGroupTreeNodeList);
			context.put("learnerGroup",learnerGroup);
			
			return new ModelAndView(viewLearnerGroupCoursesTemplate, "context", context);
		}
		catch (Exception e) {
			log.debug("exception", e);
		}	
		return new ModelAndView(viewLearnerGroupCoursesTemplate);		
	}*/
	
	public ModelAndView viewLearnerGroupCourses(HttpServletRequest request, 
			HttpServletResponse response){
		
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			List<TreeNode> treeAsList = null;//getEntitlementCourseGroupTreeNode(null);
			String learnerGroupId = request.getParameter("learnerGroupId");
			context.put("learnerGroupId", learnerGroupId);			
			LearnerGroup learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(learnerGroupId));
			
			if (learnerGroup == null){
				return new ModelAndView(viewLearnerGroupMembersTemplate, "context", context);
			}
				
			treeAsList=this.getTreeForContract(learnerGroup);
										
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", CustomerEntitlement.COURSE_ENROLLMENTTYPE);
			context.put("learnerGroupId", learnerGroupId);
			
			return new ModelAndView(viewLearnerGroupCoursesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewLearnerGroupCoursesTemplate);
	}
	
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup ) {

		if( orgGroup != null ) {

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for( int i=0; i<childGroups.size(); i++ ){
				node = getOrgGroupTree(node, childGroups.get(i));
			}
			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			} else
				return node;
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView changeLearnerGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//List<VU360User> resultList = new ArrayList<VU360User>();
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();

			/*
			 * Get logged in user
			 */
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("userData", loggedInUser);

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String emailAddress= request.getParameter("emailaddress");
			String searchKey = request.getParameter("searchkey");
			String action = request.getParameter("action");
			String direction=request.getParameter("direction");
			String pageIndex=request.getParameter("pageIndex");
			String sortDirection=request.getParameter("sortDirection");
			String sortBy=request.getParameter("sortBy");

			log.debug("First sortDirection  " + sortDirection);
			int pageNo=0;
			int recordShowing=0;
			action = (action==null)?CHANGE_GROUP_DEFAULT_ACTION:action;
			firstName = (firstName==null)?"":firstName;
			lastName = (lastName==null)?"":lastName;
			emailAddress = (emailAddress==null)?"":emailAddress;
			searchKey = (searchKey==null)?"":searchKey;
			if(sortDirection==null)
				sortDirection="0";

			HttpSession session = request.getSession();
			//long[] groupId = new long[1];

			String learnerGroupId=session.getAttribute("learnerGroupId").toString();

			LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(Long.valueOf(learnerGroupId));

			if (sortBy == null && action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)== false){

				log.debug("DYUTIMAN in FIRTS ");
				session.setAttribute("searchedFirstName", firstName);
				session.setAttribute("searchedLastName", lastName);
				session.setAttribute("searchedEmailAddress", emailAddress);
				//if(session.getAttribute("searchedSearchKey")!=null && session.getAttribute("searchedSearchKey").toString()!="")
				//session.setAttribute("searchedSearchKey", session.getAttribute("searchedSearchKey"));
				//else
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo",pageNo);

			}
			direction = (direction==null)?CHANGE_GROUP_PREVPAGE_DIRECTION:direction;
			pageIndex = (pageIndex==null)?"0":pageIndex;
			sortDirection = (sortDirection==null)?"0":sortDirection;
			sortBy = (sortBy==null)?CHANGE_GROUP_SORT_FIRSTNAME:sortBy;
			log.debug("2nd sortDirection  " + sortDirection);


			log.debug("action="+action + "  direction " + direction + " pageIndex " + pageIndex + "   sortBy "+ firstName + "  sortDirection " + sortDirection);
			if(action.equalsIgnoreCase(CHANGE_GROUP_SIMPLE_SEARCH_ACTION)){

				session.setAttribute("searchType", CHANGE_GROUP_SIMPLE_SEARCH_ACTION);

				if(direction.equalsIgnoreCase(CHANGE_GROUP_PREVPAGE_DIRECTION)){

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);
					//resultList = learnerService.findLearner(searchKey,loggedInUser);
					//session.setAttribute("searchedSearchKey", session.getAttribute("searchedSearchKey"));

				}else if(direction.equalsIgnoreCase(CHANGE_GROUP_NEXTPAGE_DIRECTION)){

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);
					//resultList = learnerService.findLearner(searchKey,loggedInUser);
					//session.setAttribute("searchedSearchKey", session.getAttribute("searchedSearchKey"));
				}//else {
				//session.setAttribute("searchedSearchKey", searchKey);
				//}
				log.debug("searchType="+action + " searchedSearchKey " + session.getAttribute("searchedSearchKey").toString() + "  direction " + direction + " pageIndex " + pageIndex + "   sortBy "+ firstName + "  sortDirection " + sortDirection);
				session.setAttribute("pageNo",pageNo);
				if( !loggedInUser.isLMSAdministrator() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
						
						results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
								loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));

						userList=(List<VU360User>)results.get("list");
					}
						
				}else {
					results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));

					userList=(List<VU360User>)results.get("list");
				}
				

			}else if(action.equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION)){
				//resultList = learnerService.findLearner(firstName,lastName,emailAddress,loggedInUser);
				//results = learnerService.findLearner1(firstName,lastName,emailAddress,loggedInUser,0);
				log.debug("Result Size = " + userList.size());
				session.setAttribute("searchType", CHANGE_GROUP_ADVANCED_SEARCH_ACTION);
				if(direction.equalsIgnoreCase(CHANGE_GROUP_PREVPAGE_DIRECTION)){

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);
					//resultList = learnerService.findLearner(searchKey,loggedInUser);

				}else if(direction.equalsIgnoreCase(CHANGE_GROUP_NEXTPAGE_DIRECTION)){

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);
					//resultList = learnerService.findLearner(searchKey,loggedInUser);

				}
				session.setAttribute("pageNo",pageNo);	
				/*results=orgGroupLearnerGroupService.findLearnerNotInLearnerGroup(objGroup,session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString()
						,loggedInUser,pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));*/

				results=learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
						pageNo,CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
				//recordShowing = ((Integer)resultList1.get("recordSize")>=CHANGE_GROUP_PAGE_SIZE*(pageNo+1))?
				//		(pageNo+1)*CHANGE_GROUP_PAGE_SIZE:(Integer)resultList1.get("recordSize");
				userList=(List<VU360User>)results.get("list");
				log.debug("Result Size = " + userList.size());

				//recordShowing = ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(pageIndex)+1)*CHANGE_GROUP_PAGE_SIZE;
				//(pageNo)*CHANGE_GROUP_PAGE_SIZE:(Integer)userList.size();

			}else if(action.equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION)){

				session.setAttribute("searchType", CHANGE_GROUP_ALL_SEARCH_ACTION);
				log.debug("searchType " + session.getAttribute("searchType" ));
				pageNo=0;
				session.setAttribute("pageNo",pageNo);	
				//	results=orgGroupLearnerGroupService.findAllLearnersNotInLearnerGroup(objGroup,"",loggedInUser,sortBy,Integer.parseInt(sortDirection));
				results=learnerService.findAllLearners("",
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),sortBy,Integer.parseInt(sortDirection));


				userList=(List<VU360User>)results.get("list");
				log.debug("Result Size = " + userList.size());

				//recordShowing = ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(pageIndex)+1)*CHANGE_GROUP_PAGE_SIZE;
				//(pageNo)*CHANGE_GROUP_PAGE_SIZE:(Integer)userList.size();

			}else if(action.equalsIgnoreCase(CHANGE_GROUP_SORT_LEARNER_ACTION)){
				log.debug("searchType " + session.getAttribute("searchType" ));

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION)){

					/*results=orgGroupLearnerGroupService.findLearnerNotInLearnerGroup(objGroup,session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString()
							,loggedInUser,Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					 */

					results=learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));

					userList=(List<VU360User>)results.get("list");

				}else if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION)){

					//results=orgGroupLearnerGroupService.findAllLearnersNotInLearnerGroup(objGroup,"",loggedInUser,sortBy,Integer.parseInt(sortDirection));
					results=learnerService.findAllLearners("",
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");
				}
				else{
					results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");

				}
				//sortDirection = (sortDirection =="0")?"1":"0";

			}else if(action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)){

				//session.setAttribute("searchType", CHANGE_GROUP_DELETE_LEARNER_ACTION);
				//List<VU360User> searchedlearners = (List<VU360User>)session.getAttribute("searchedLearnerList");

				log.debug("delete " + pageNo + "search type " + session.getAttribute("searchType").toString());
				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ADVANCED_SEARCH_ACTION)){

					results=learnerService.findLearner1(session.getAttribute("searchedFirstName").toString(),session.getAttribute("searchedLastName").toString(),session.getAttribute("searchedEmailAddress").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");

				}else if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION)){

					results=learnerService.findAllLearners("",
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");
				}
				else{
					results=learnerService.findLearner1(session.getAttribute("searchedSearchKey").toString(),
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							Integer.parseInt(session.getAttribute("pageNo").toString()),CHANGE_GROUP_PAGE_SIZE,sortBy,Integer.parseInt(sortDirection));
					userList=(List<VU360User>)results.get("list");

				}
				//sortDirection = (sortDirection =="0")?"1":"0";
				log.debug("Delete sortDirection " + sortDirection);
				if (sortDirection.equalsIgnoreCase("0"))
					sortDirection = "1";
				else
					sortDirection="0";

			}else if(action.equalsIgnoreCase(MANAGE_LEARNERGROUP_UPDATELEARNER_ACTION)){
				String[] selectedLearnerValues = request.getParameterValues("selectedLearner");
				Long selectedLearners[] = new Long[selectedLearnerValues.length];
				List<Long> learnerIds = new ArrayList<Long>(selectedLearnerValues.length);
				
				for(int i=0;i<selectedLearnerValues.length;i++){
					if(StringUtils.isNotBlank(selectedLearnerValues[i])){
						learnerIds.add(Long.valueOf(selectedLearnerValues[i]));
					}
				}
				selectedLearners = learnerIds.toArray(selectedLearners);
				List<Learner> listSelectedLearner = learnerService.getLearnersByIDs(selectedLearners);
				learnerService.addLearnersInLearnerGroup(listSelectedLearner,learnerGroup);
				context.put("learnerGroupId", learnerGroupId);
				return new ModelAndView(learnerGroupRedirectTemplate);

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
			if (!results.isEmpty())
				recordShowing = ((Integer)userList.size()<CHANGE_GROUP_PAGE_SIZE)?Integer.parseInt(results.get("recordSize").toString()):(Integer.parseInt(session.getAttribute("pageNo").toString())+1)*CHANGE_GROUP_PAGE_SIZE;		
				//sortDirection = (sortDirection =="0")?"1":"0";

				if (session.getAttribute("searchType").toString().equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION))
					recordShowing=userList.size();
				if (sortDirection.equalsIgnoreCase("0"))
					sortDirection = "1";
				else
					sortDirection="0";
				String totalRecord =	(results.isEmpty())?"0":results.get("recordSize").toString();
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

				return new ModelAndView(addMembersTemplate, "context", context);
				
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(addMembersTemplate);
	}

	/**
	 * Added by Dyutiman 
	 * method required to create org group tree
	 * 
	 */
	@SuppressWarnings("unused")
	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup,
			List<Long> selectedOrgGroups ){

		if( orgGroup != null ) {
			TreeNode node = new TreeNode(orgGroup);

			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for( int i = 0 ; i < childGroups.size() ; i++ ) {
				node = getOrgGroupTree(node, childGroups.get(i), selectedOrgGroups);
			}
			node.setEnabled(true);

			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	
	public ModelAndView showAddCourses( HttpServletRequest request, 
			HttpServletResponse response) {
		
		Map<String, Object> context = new HashMap<String, Object>();
		String learnerGroupId = request.getParameter("learnerGroupId");
		context.put("learnerGroupId", learnerGroupId);
		context.put("pageNo",0);
		context.put("totalRecord", 0);
		context.put("recordShowing", 0);
		context.put("contractType", CustomerEntitlement.COURSE_ENROLLMENTTYPE);
		return new ModelAndView(addCourseTemplate, "context", context);
	}

	
	public ModelAndView searchCourses( HttpServletRequest request, 
			HttpServletResponse response) {
		
		String learnerGroupId = request.getParameter("learnerGroupId");
		String keywords=request.getParameter("keywords");
		String title=request.getParameter("title");
		String id=request.getParameter("courseId");
		
		Map<String, Object> context = new HashMap<String, Object>();
		List<TreeNode> courseGroupTree = getEntitlementCourseGroupTreeNode(null, title.trim(), id, keywords.trim(), context);		
		context.put("courseGroupTree", courseGroupTree);
		if (courseGroupTree == null) {
			String[] error = {"error.admin.customerEnt.course.errorMsg1"
					, "error.admin.customerEnt.course.errorMsg2"
					, "error.admin.customerEnt.course.errorMsg3"};
			context.put("error", error);
		}
		context.put("contractType", CustomerEntitlement.COURSE_ENROLLMENTTYPE);
		context.put("learnerGroupId", learnerGroupId);
		context.put("pageNo",0);
		context.put("totalRecord", courseGroupTree.size());
		context.put("recordShowing", courseGroupTree.size());
		
		return new ModelAndView(addCourseTemplate, "context", context);
		
	}	

	
	private List<TreeNode> getEntitlementCourseGroupTreeNode(Object object,
			String title, String id, String keywords, Map<String, Object> context) {		
		
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		if (customer != null) {			
			title = !StringUtils.isBlank(title) ? title.trim() : "";
			id = !StringUtils.isBlank(id) ? id.trim() : "";
			keywords = !StringUtils.isBlank(keywords) ? keywords.trim() : "";
			
			HashMap<CourseGroup, List<Course>> resultMap = entitlementService.findCourseInCustomerEntitlementsBySearchCriteria(customer, title, id, keywords);
						
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			Set<CourseGroup> courseGroups = resultMap.keySet();
			for (CourseGroup courseGroup : courseGroups) {		
				List<Course> courses = resultMap.get(courseGroup);
				if(courses!=null) { //i.e. its not a blank search
					CourseSort courseSort = new CourseSort();
					
					if (courses != null && courses.size() > 0) {
						Collections.sort(courses, courseSort);
					}
					
					for (Course course : courses) {
							
						boolean courseAdded = false;					
						for (TreeNode rootTreeNode : rootNodesReferences) {
							List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
							if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
								courseAdded = true;
								break;
							}
							
							CourseGroup currentCourseGroup = courseGroup;
							while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
								CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
								childCourseGroups.add(currentCourseGroup);
								if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
									courseAdded = true;
									break;
								}
								currentCourseGroup = parentCourseGroup;
							}
	
							if (courseAdded)
								break;
						}
						if (!courseAdded) {
							courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
							rootNodesReferences.add(courseGroupCourseTreeNode);
						}
					}
					context.put("callMacroForChildren", "false");
				}
			}
			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}
		}
		
		return treeNodesList;
	}	
	
	/**
	 * Courses which are not associated with any course group there course group will be null
	 * Courses which are associated with Course Group should represent as Course Group items  
	 */
	public ModelAndView addCoursesInLearnerGroup( HttpServletRequest request, 
			HttpServletResponse response) {
		
		String learnerGroupId = request.getParameter("learnerGroupId");
		LearnerGroup learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(learnerGroupId));
		String []strCourseIds = request.getParameterValues("courses");
		
		Set<LearnerGroupItem> newLearnerGroupItems = new HashSet<LearnerGroupItem>();
		if(strCourseIds!=null) {
			LearnerGroupItem item;

			for(int i=0;i<strCourseIds.length;i++) {
				String []strArray=strCourseIds[i].split(":");
				CourseGroup courseGroupSearch = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(strArray[0])); //courseAndCourseGroupService.loadForUpdateCourseGroup(Long.parseLong(strArray[0]));//TODO why we need this loadForUpdate here?
				
				Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(strArray[1]));
//				Course course=new Course();
//				course.setId(Long.valueOf(strArray[1]));
				
				item = new LearnerGroupItem();
				item.setCourse(course);
				
				if(courseGroupSearch!=null) {
					item.setCourseGroup(courseGroupSearch);
				}else{
					item.setCourseGroup(null);
				}
				item.setLearnerGroup(learnerGroup);
				newLearnerGroupItems.add(item);
			}
		}
		
		if(!newLearnerGroupItems.isEmpty()) {
			learnerService.saveLearnerGroupItems(newLearnerGroupItems);
			learnerGroup.getLearnerGroupItems().addAll(newLearnerGroupItems);
			Brander brand =  VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			List<Learner> learners = orgGroupLearnerGroupService.getLearnersByLearnerGroupId(learnerGroup.getId());
			enrollmentService.enrollLearnersInLearnerGroupCourses(learners, new ArrayList<LearnerGroupItem>(newLearnerGroupItems), brand);
		}
		
		return viewLearnerGroupCourses(request, response);	
	}
	
	public ModelAndView removeCourseGroup( HttpServletRequest request, 
			HttpServletResponse response) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<TreeNode> treeAsList = null;
		
		String learnerGroupId = request.getParameter("learnerGroupId");
		LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(Long.valueOf(learnerGroupId));
		String []strCourseIds = request.getParameterValues("courses");
		List<LearnerGroupItem> items=learnerService.getLearnerGroupItemsByLearnerGroupId(learnerGroup.getId());
		learnerGroup.setLearnerGroupItems(items);
		List<LearnerGroupItem> itemsToBeRemoved=new ArrayList<LearnerGroupItem>();
		LearnerGroupItem item;		
		for(int i=0;i<strCourseIds.length;i++){			
			String []strArray=strCourseIds[i].split(":");
			item = learnerGroup.findLearnerGroupItem (new Long(strArray[0]),new Long(strArray[1]));
			
			if(items.contains(item)){
				items.remove(item);
				itemsToBeRemoved.add(item);
			}
		}
		
		orgGroupLearnerGroupService.deleteLearnerGroupItems(itemsToBeRemoved);
		learnerGroup = orgGroupLearnerGroupService.saveLearnerGroup(learnerGroup);
		

		treeAsList=this.getTreeForContract(learnerGroup);
		
		context.put("coursesTreeAsList", treeAsList);
		context.put("contractType", CustomerEntitlement.COURSE_ENROLLMENTTYPE);
		context.put("learnerGroupId", learnerGroupId);
		
		return new ModelAndView(viewLearnerGroupCoursesTemplate, "context", context);
	}
	
	private List<TreeNode> getTreeForContract(LearnerGroup learnerGroup) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();		
		
		List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
		TreeNode courseGroupCourseTreeNode = null;
		
		List<LearnerGroupItem> learnerGroupItems=learnerService.getLearnerGroupItemsByLearnerGroupId(learnerGroup.getId());
		for(LearnerGroupItem learnerGroupItem : learnerGroupItems ) {
			Course course = learnerGroupItem.getCourse();
			if (course.isActive()) { // [1/27/2011] LMS-7183 :: Retired Course Functionality II
				CourseGroup courseGroup = learnerGroupItem.getCourseGroup();
				if(courseGroup==null){
					courseGroup = new CourseGroup();
					java.util.Date date = new java.util.Date();
					courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
					courseGroup.setName("Miscellaneous");
				}
					
				boolean courseAdded = false;					
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
						courseAdded = true;
						break;
					}
					
					CourseGroup currentCourseGroup = courseGroup;
					while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}
	
					if (courseAdded)
						break;
				}
				if (!courseAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}
			}
		}

		for (TreeNode rootTreeNode : rootNodesReferences) {
			treeNodesList.addAll(rootTreeNode.bfs());
		}
		
		return treeNodesList;
	}
	
	public String getAddCourseTemplate() {
		return addCourseTemplate;
	}

	public void setAddCourseTemplate(String addCourseTemplate) {
		this.addCourseTemplate = addCourseTemplate;
	}

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public String getManageLearnerGroupsTemplate() {
		return manageLearnerGroupsTemplate;
	}

	public void setManageLearnerGroupsTemplate(String manageLearnerGroupsTemplate) {
		this.manageLearnerGroupsTemplate = manageLearnerGroupsTemplate;
	}

	public String getAddLearnerGroupTemplate() {
		return addLearnerGroupTemplate;
	}

	public void setAddLearnerGroupTemplate(String addLearnerGroupTemplate) {
		this.addLearnerGroupTemplate = addLearnerGroupTemplate;
	}

	public String getAddLearnerRedirectTemplate() {
		return addLearnerRedirectTemplate;
	}

	public void setAddLearnerRedirectTemplate(String addLearnerRedirectTemplate) {
		this.addLearnerRedirectTemplate = addLearnerRedirectTemplate;
	}

	public String getAddLearnerGroupsFinishTemplate() {
		return addLearnerGroupsFinishTemplate;
	}

	public void setAddLearnerGroupsFinishTemplate(
			String addLearnerGroupsFinishTemplate) {
		this.addLearnerGroupsFinishTemplate = addLearnerGroupsFinishTemplate;
	}

	public String getSaveLearnerGroupTemplate() {
		return saveLearnerGroupTemplate;
	}

	public void setSaveLearnerGroupTemplate(String saveLearnerGroupTemplate) {
		this.saveLearnerGroupTemplate = saveLearnerGroupTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getViewLearnerGroupMembersTemplate() {
		return viewLearnerGroupMembersTemplate;
	}

	public void setViewLearnerGroupMembersTemplate(
			String viewLearnerGroupMembersTemplate) {
		this.viewLearnerGroupMembersTemplate = viewLearnerGroupMembersTemplate;
	}

	public String getSearchLearnerTemplate() {
		return searchLearnerTemplate;
	}

	public void setSearchLearnerTemplate(String searchLearnerTemplate) {
		this.searchLearnerTemplate = searchLearnerTemplate;
	}

	public String getAddMembersTemplate() {
		return addMembersTemplate;
	}

	public void setAddMembersTemplate(String addMembersTemplate) {
		this.addMembersTemplate = addMembersTemplate;
	}

	public String getLearnerGroupRedirectTemplate() {
		return learnerGroupRedirectTemplate;
	}

	public void setLearnerGroupRedirectTemplate(String learnerGroupRedirectTemplate) {
		this.learnerGroupRedirectTemplate = learnerGroupRedirectTemplate;
	}

	public String getUpdateLearnerGroupTemplate() {
		return updateLearnerGroupTemplate;
	}

	public void setUpdateLearnerGroupTemplate(String updateLearnerGroupTemplate) {
		this.updateLearnerGroupTemplate = updateLearnerGroupTemplate;
	}
	public String getViewLearnerGroupCoursesTemplate() {
		return viewLearnerGroupCoursesTemplate;
	}

	public void setViewLearnerGroupCoursesTemplate(
			String viewLearnerGroupCoursesTemplate) {
		this.viewLearnerGroupCoursesTemplate = viewLearnerGroupCoursesTemplate;
	}
	
	
	//Added By Noman
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
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
	
}