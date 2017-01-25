package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author arijit dutta
 *
 */
public class ManageCourseGroupController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageCourseGroupController.class.getName());
	private AuthorService authorService = null;
	private CustomerService customerService = null;
	
	private String manageOrganisationGroupTemplate = null;
	private String viewOrganisationGroupMembersTemplate = null;
	private String changeGroupMembersTemplate=null;
	private String searchLearnerTemplate = null;
	private String updateCourseGroupTemplate = null;
	private String deleteCourseGroupTemplate = null ;
	
	private static final String MANAGE_GROUP_DELETE_COURSEGROUP_ACTION = "deleteCourseGroup";
	private static final String MANAGE_GROUP_UPDATE_COURSEGROUP_ACTION = "update";
	
	private LearnerService learnerService;
	private CourseAndCourseGroupService courseAndCourseGroupService = null ;
	private EntitlementService entitlementService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	public ModelAndView updateCourseGroup(HttpServletRequest request,HttpServletResponse response){
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			
			Long courseGroupId = new Long (request.getParameter("courseGroupId"));
			String courseGroupName = request.getParameter("courseGroupName");
			String action = request.getParameter("action");
			
			CourseGroup courseGroup = null;
			
			if (StringUtils.isNotBlank(action) && action.equalsIgnoreCase(MANAGE_GROUP_UPDATE_COURSEGROUP_ACTION)) {
				courseGroup = courseAndCourseGroupService.loadForUpdateCourseGroup(courseGroupId);
				courseGroup.setName(courseGroupName);
				if(validateEditData(request,courseGroup,context)){					
					Long parentCourseGroupId =null;
					if(request.getParameter("courseGroups") != null){
					   parentCourseGroupId = new Long( request.getParameter("courseGroups") );
					}
					
					CourseGroup parentCourseGroup = this.courseAndCourseGroupService.getCourseGroupById(parentCourseGroupId);
					courseGroup.setParentCourseGroup(parentCourseGroup);
					courseGroup = courseAndCourseGroupService.updateCourseGroup(courseGroup);
					
					return new ModelAndView("redirect:ins_viewCourseGroups.do");
				}				
			}			
		 
			if (courseGroup == null) {
				courseGroup = courseAndCourseGroupService.getCourseGroupById(courseGroupId);
			}			 
			context.put("courseGroupName", courseGroup.getName() );
			context.put("courseGroupId", courseGroup.getId() );
			context.put("parentCourseGroupId", (courseGroup.getParentCourseGroup() != null) ? courseGroup.getParentCourseGroup().getId() : -1 );
			
			// [LMS-7106] Get Content Owner			
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			com.softech.vu360.lms.vo.ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
			}
			
			List<CourseGroup> courseGroupList = courseAndCourseGroupService.getAllCourseGroupsByContentOwnerId(contentOwner.getId());
			
			// Create Tree for display
			List<List<TreeNode>> courseGroupTreeList = this.courseAndCourseGroupService.getCourseGroupTreeList( courseGroupList, true );				
						
			context.put("courseGroupTreeList", courseGroupTreeList);
			return new ModelAndView(this.updateCourseGroupTemplate, "context", context);		
			
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView("redirect:ins_viewCourseGroups.do");
	}

	public ModelAndView deleteCourseGroups(HttpServletRequest request,
			HttpServletResponse response) {
			
		Map<Object,Object> context =null;
			
		try {
			List<CourseGroup> courseGroupList = new ArrayList<CourseGroup>();  	
			String[] selectedCourseGroupValues = request.getParameterValues("courseGroups");
			context = new HashMap<Object,Object>();
			List<Map<Object,Object>> courseGroupDeletionStatus = new ArrayList<Map<Object,Object>>();
			
			if( selectedCourseGroupValues != null ){

				long courseGroupIdArray[] = new long[selectedCourseGroupValues.length];
				log.debug("Course Groups "+selectedCourseGroupValues.length);

				//if (validateDeleteData(selectedOrgGroupValues,rootOrgGroup,context)){

					for( int i=0; i<selectedCourseGroupValues.length; i++ ) {
						String orgGroupID = selectedCourseGroupValues[i];

						if( orgGroupID != null ) {
							courseGroupIdArray[i]=Long.parseLong(selectedCourseGroupValues[i]);
							log.debug (" - "+ Long.parseLong(selectedCourseGroupValues[i])+ " - ");
							
						}	
						
					}
					if(courseGroupIdArray.length > 0 ) {
						courseGroupList = courseAndCourseGroupService.getCourseGroupsByIds(courseGroupIdArray);
					}
					
					if(courseGroupList != null){
						if(courseGroupList.size() > 0){
							Map<Object,Object> cgDeletionStatus = null;
							for(CourseGroup cg :courseGroupList){
								cgDeletionStatus = new HashMap<Object,Object>();
								if(cg.getChildrenCourseGroups().size() > 0 ){
									cgDeletionStatus.put("status","lms.instructor.CourseGroup.deleteError.childCourseGroupsExists"); 
									cgDeletionStatus.put("name",cg.getName());
									 
								}else if(cg.getCourses().size() > 0   ){
									cgDeletionStatus.put("status","lms.instructor.CourseGroup.deleteError.CoursesExists"); 
									cgDeletionStatus.put("name",cg.getName());

								}else{
									cgDeletionStatus.put("status","lms.instructor.CourseGroup.deleteMessage.deleted"); 
									cgDeletionStatus.put("name",cg.getName()); 
									
									courseAndCourseGroupService.deleteCourseGroup(cg) ; 
								}
								courseGroupDeletionStatus.add(cgDeletionStatus);
							}
							
						}else{
							
						}						
					}
					 
			}
			context.put("courseGroupDeletetionStatus", courseGroupDeletionStatus);
			
			
			return new ModelAndView(deleteCourseGroupTemplate, "context", context);

		}catch(Exception e){}
		
		return new ModelAndView("redirect:ins_viewCourseGroups.do");
		}

	/**
	 * added by arijit
	 * this method manages OrganisationGroup
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView manageCourseGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String action = request.getParameter("action");
			if(action != null){
				if(action.equalsIgnoreCase(MANAGE_GROUP_DELETE_COURSEGROUP_ACTION)){			
					return deleteCourseGroups( request,	response); 
				}
			}			
			
			Map<Object, Object> context = new HashMap<Object, Object>();
		
			// [LMS-7106] Get Content Owner			
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
			}
			
			if(contentOwner == null){
				contentOwner = authorService.addContentOwnerIfNotExist(user.getLearner().getCustomer(), user.getId());
				Customer newCustomer = this.getCustomerService().loadForUpdateCustomer(user.getLearner().getCustomer().getId());
				newCustomer.setContentOwner(contentOwner);
				this.getCustomerService().updateCustomer(newCustomer);
			}
			
			List<CourseGroup> courseGroupList = courseAndCourseGroupService.getAllCourseGroupsByContentOwnerId(contentOwner.getId());
			
			// Create Tree for display
			List<List<TreeNode>> courseGroupTreeList = this.courseAndCourseGroupService.getCourseGroupTreeList( courseGroupList, true );				
						
			context.put("courseGroupTreeList", courseGroupTreeList);
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


	/**
	 * @added by arijit
	 * this method views OrganisationGroup members
	 * @modified by Dyutiman 29 oct
	 * 
	 */

	/**	public ModelAndView viewCourseGroups(HttpServletRequest request,
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

			if( sortDirection == null ) {
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

			if( orgGroupId != null ) {

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
				orgGroupId = "1932";
				//OrganizationalGroup  orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupId));
				CourseGroup  orgGroup = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(orgGroupId) );
				List<Learner> learnerList = orgGroup.getMembers();
				UserSort userSort = new UserSort();
				userSort.setSortBy(sortBy);
				userSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(learnerList,userSort);
				List<Learner> finalLearnerList= new ArrayList<Learner>();

				if( action != null ) {

					if( searchType.equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION) ) {

						recordShowing = 100;//learnerList.size();

						if( action.equalsIgnoreCase("sort") ) {
							sortDirection= request.getParameter("sortDirection");
							session.setAttribute("orgGroupSortDirection", sortDirection);

							if( sortDirection.equalsIgnoreCase("0") )
								sortDirection = "1";
							else
								sortDirection="0";
							session.setAttribute("previousAction", "sort");
							
						}
						//finalLearnerList = learnerList.subList(0, learnerList.size());
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
					int totalRecord = 0;

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
	**/



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

	private boolean validateEditData(HttpServletRequest request,CourseGroup courseGroup,Map<Object, Object> context  ){

		boolean isValid = true;

		if (StringUtils.isBlank(courseGroup.getName()))
		{			
			context.put("validateCourseGroup", "error.instructor.manageCourseGroup.addCourse.error.groupNameRequired");
			isValid = false;
		}
		Long parentCourseGroupId =null;
		if(request.getParameter("courseGroups") != null){
		   parentCourseGroupId = new Long( request.getParameter("courseGroups") );
		}							
		CourseGroup tempCG= this.courseAndCourseGroupService.getCourseGroupById(parentCourseGroupId);
		String parentIds="";
		boolean keepLooping= true;
		while(keepLooping){
			if(tempCG.getParentCourseGroup() == null){
				keepLooping= false;
			}else{
				parentIds+= tempCG.getParentCourseGroup().getId() + ",";
				tempCG= tempCG.getParentCourseGroup();							
			}
		}
		if(parentIds.indexOf(courseGroup.getId()+"")>=0){
			context.put("validateCourseGroup", "error.instructor.manageCourseGroup.error.illegalParent");
			isValid = false;
		}		
		
		return isValid;
	}
	
	void prepareCourseGrouListsByRoot(List<List<CourseGroup>> courseGroupListByRoot , List<CourseGroup> modifiedCourseGroups ){
		List<CourseGroup> rootCourseList = new ArrayList<CourseGroup>();  
		
		for( CourseGroup cg : modifiedCourseGroups ) {
			if( cg.getParentCourseGroup() == null ){
				rootCourseList.add(cg);
			}
		}
		
		for( CourseGroup rootCourseGroup : rootCourseList ) {
			 List<CourseGroup> cgList = new ArrayList<CourseGroup>();
			 getRootCourseGroupsChilds( rootCourseGroup ,modifiedCourseGroups,cgList);
			 cgList.add(rootCourseGroup);
			 courseGroupListByRoot.add(cgList);
		}		
	}

	void getRootCourseGroupsChilds( CourseGroup parentCourseGroup , List<CourseGroup> modifiedCourseGroups, List<CourseGroup> childCourseGroups){
	
		for( CourseGroup cg : modifiedCourseGroups ) {
			if(cg.getParentCourseGroup() != null){
				if(cg.getParentCourseGroup().getId().equals(parentCourseGroup.getId())){
					childCourseGroups.add(cg);
					getRootCourseGroupsChilds( cg ,  modifiedCourseGroups, childCourseGroups);
				}
			}
		}
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

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(	CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @param deleteCourseGroupTemplate the deleteCourseGroupTemplate to set
	 */
	public void setDeleteCourseGroupTemplate(String deleteCourseGroupTemplate) {
		this.deleteCourseGroupTemplate = deleteCourseGroupTemplate;
	}

	/**
	 * @return the deleteCourseGroupTemplate
	 */
	public String getDeleteCourseGroupTemplate() {
		return deleteCourseGroupTemplate;
	}

	/**
	 * @param updateCourseGroupTemplate the updateCourseGroupTemplate to set
	 */
	public void setUpdateCourseGroupTemplate(String updateCourseGroupTemplate) {
		this.updateCourseGroupTemplate = updateCourseGroupTemplate;
	}

	/**
	 * @return the updateCourseGroupTemplate
	 */
	public String getUpdateCourseGroupTemplate() {
		return updateCourseGroupTemplate;
	}

	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}	