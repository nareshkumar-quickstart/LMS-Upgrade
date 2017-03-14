package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddSecurityRoleForm;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.validator.AddSecurityRoleValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;

public class AddSecurityRoleWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddOrganizationGroupController.class.getName());
	private LearnerService learnerService;
	private VU360UserService vu360UserService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private LMSProductPurchaseService lmsProductPurchaseService;
	private String finishTemplate = null;
	private String cancelTemplate = null;

	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
	private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
	private static final String MANAGE_USER_SORT_ACCOUNTLOCKED = "accountNonLocked";
	private static final String[] SORTABLE_COLUMNS = {MANAGE_USER_SORT_FIRSTNAME,
		MANAGE_USER_SORT_LASTNAME,MANAGE_USER_SORT_USERNAME,MANAGE_USER_SORT_ACCOUNTLOCKED};

	public AddSecurityRoleWizardController() {
		super();
		setCommandName("addSecurityRoleForm");
		setCommandClass(AddSecurityRoleForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/addSecurityRole/addSecurityRoleStep1"
				, "manager/addSecurityRole/addSecurityRoleStep2"
				, "manager/addSecurityRole/addSecurityRoleStep3"
				, "manager/addSecurityRole/addSecurityRoleStep4"});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData( HttpServletRequest request, Object command, 
			Errors errors, int page) throws Exception {

		AddSecurityRoleForm form = (AddSecurityRoleForm)command;
		Map<Object, Object> model = new HashMap<Object, Object>();
		List<OrganizationalGroup> selectedOrgGroups = null;
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		//com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = null;
		if( vu360UserService.hasAdministratorRole(loggedInUser) ) {
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomer();
		} else {
			customer = loggedInUser.getLearner().getCustomer();
		}
		switch(page) {

		case 0:
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();

			List<LMSRole> tempRoles = vu360UserService.getAllRoles(customer,user);
			//by OWS for LMS-4297 on 26th Jan 2010
			List<LMSRole> allUserRoles = new ArrayList<LMSRole>();//vu360UserService.getAllRoles(customer,loggedInUser);
			if(vu360UserService.hasAdministratorRole(loggedInUser) && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
				allUserRoles=tempRoles;
				model.put("roles", allUserRoles);
			}
			else{
				
				for(LMSRole lmsRole: tempRoles){
				
					if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER) || lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)){
						allUserRoles.add(lmsRole);
						
					}
					//To show Instructor Role for Freemium ILT Upgrade User (LMS-16018)
					if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)){
						if(!CustomerUtil.checkB2BCustomer(auth)){
							VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
							LMSProducts lmsProducts = lmsProductPurchaseService.findLMSProductsPurchasedByCustomer(LMSProducts.WEBINAR_AND_WEBLINK_COURSES, details.getCurrentCustomerId());
							if(lmsProducts!=null && lmsProducts.getId()!=null){
								allUserRoles.add(lmsRole);
							}
						}
					}
				}
				model.put("roles", allUserRoles);
			}
			return model;
		case 1:
			LMSRole r = learnerService.getLMSRoleById(Long.valueOf(form.getRoleId()));
			if( !r.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER) ) {
				model.put("to", "four");
			} else {
				model.put("to", "three");
			}
			return model;
		case 2:
			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(
					customer.getId());
			String[] orgGroupList = form.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if( orgGroupList != null && orgGroupList.length > 0 ) { 
				for( String orgGroup : orgGroupList ) {
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, orgGroupIdList, loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			model.put("orgGroupTreeAsList", treeAsList);
			selectedOrgGroups = loggedInUser.getTrainingAdministrator().getManagedGroups();
			form.setSelectedLearnersOrgGroups(selectedOrgGroups);
			if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				form.setManageAll("false");
			}else
				form.setManageAll("true");
			return model;
		case 3:
			break;
		default :
			break;
		}
		return super.referenceData(request, page);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, 
			int page) throws Exception {

		AddSecurityRoleForm form = (AddSecurityRoleForm)command;

		//sync the learner list with the selected learner list 
		if ((page == 1 
				&& !StringUtils.isBlank(form.getAction()) 
				&& form.getAction().equalsIgnoreCase("search"))
				|| !StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("finish")
				||(this.getTargetPage(request, command, errors, 1)==0)
				||(this.getTargetPage(request, command, errors, 1)==2)
		){

			List<LearnerItemForm> learnerList = form.getLearners();
			Collections.sort(learnerList);

			List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
			Collections.sort(selectedLearnerList);

			List<LearnerItemForm> mergedLearnerList = new ArrayList<LearnerItemForm>();
			int i=0, j=0;
			LearnerItemForm item, selecteditem;
			for(;i<learnerList.size()&&j<selectedLearnerList.size();){
				item = learnerList.get(i);
				selecteditem = selectedLearnerList.get(j);
				if(item.compareTo(selecteditem)<0){
					i++;
					if(item.isSelected())
						mergedLearnerList.add(item);
				}else if(item.compareTo(selecteditem)>0){
					j++;
					mergedLearnerList.add(selecteditem);
				}else{
					if(item.isSelected())
						mergedLearnerList.add(item);
					i++; j++;
				}
			}
			for(;i<learnerList.size();i++){
				item = learnerList.get(i);
				if(item.isSelected())
					mergedLearnerList.add(item);
			}
			for(;j<selectedLearnerList.size();j++){
				selecteditem = selectedLearnerList.get(j);
				mergedLearnerList.add(selecteditem);
			}
			form.setSelectedLearners(mergedLearnerList);
		}
		//if( page == 4 ) {
		//	validator.validateFifthPage(form, errors);
		//}
		AddSecurityRoleValidator validator = (AddSecurityRoleValidator)this.getValidator();
		if(page == 1 && this.getTargetPage(request, 1) == 2)
			validator.validateSecondPage(form, errors);
		super.onBindAndValidate(request, command, errors, page);

	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AddSecurityRoleValidator validator = (AddSecurityRoleValidator)this.getValidator();
		AddSecurityRoleForm form = (AddSecurityRoleForm)command;
		log.debug("Page num --- "+page);
		switch(page) {

		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			if(finish)
				validator.validateSecondPage(form, errors);
			break;
		case 2:
			break;
		case 3:
			break;			
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	
	@SuppressWarnings("unchecked")
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		AddSecurityRoleForm form = (AddSecurityRoleForm)command;
		//CourseStatistics newCourseStatistics = new CourseStatistics();

		if ((page == 1 
				&& ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))
						||(this.getTargetPage(request, command, errors, 1)==2) && errors.hasErrors()))
						||(page == 2 && this.getTargetPage(request, command, errors, 1)==1)
						||(page == 0 && this.getTargetPage(request, command, errors, 1)==1)	){

			if(!StringUtils.isBlank(form.getSearchType())){
				Map<Object,Object> results = new HashMap<Object,Object>();
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.
						DEFAULT_PAGE_SIZE;
				int sortColumnIndex = form.getSortColumnIndex();
				String sortBy = (sortColumnIndex>=0 && sortColumnIndex<SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]: MANAGE_USER_SORT_FIRSTNAME ;
				int sortDirection = form.getSortDirection()<0 ? 0:(form.getSortDirection()>1?1:
					form.getSortDirection());
				List<VU360User> userList = null;
				
				if(form.getSearchType().equalsIgnoreCase("simplesearch")) {
					//userList=learnerService.findLearner(form.getSearchKey(), loggedInUser);
					Integer totalResults = 0;
					if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (tempManagedGroups!=null && tempManagedGroups.size() > 0 ) {
							results=learnerService.findLearner1(form.getSearchKey(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, 
									VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}

					} else {
						results=learnerService.findLearner1(form.getSearchKey(), 
								loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}

					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if(form.getSearchType().equalsIgnoreCase("advancesearch")) {
					Integer totalResults = 0;
					if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {
							results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}

					} else {
						//	Integer totalResults = 0;
						if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
							if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {
								results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
										loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
										loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
										loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
										pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
								totalResults = (Integer)results.get("recordSize");
								userList = (List<VU360User>)results.get("list");
							}

						} else {
							results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					}
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if(form.getSearchType().equalsIgnoreCase("allsearch")) {
					//results = learnerService.findAllLearners("", loggedInUser, sortBy, sortDirection);
					results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									sortBy, sortDirection);
					userList = (List<VU360User>)results.get("list");
				}

				if( userList != null && userList.size() > 0 ) {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					for( VU360User auser : userList ) {
						LearnerItemForm learnerItem = new LearnerItemForm();
						learnerItem.setSelected(false);
						learnerItem.setUser(auser);
						learnerList.add(learnerItem);
					}
					for( LearnerItemForm learnerItem : learnerList ) {
						for(LearnerItemForm preSelectedItem:form.getSelectedLearners()){
							if(learnerItem.compareTo(preSelectedItem)==0){
								learnerItem.setSelected(true);
								break;
							}
						}
					}
					form.setLearners(learnerList);
				} else {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					form.setLearners(learnerList);
				}
			}else{
				request.setAttribute("newPage","true");
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException error) throws Exception {

		AddSecurityRoleForm form = (AddSecurityRoleForm)command;
		List<OrganizationalGroup> orgGroupsList = new ArrayList<OrganizationalGroup>();
		// ############################################
		LMSRole r = learnerService.loadForUpdateLMSRole(Long.valueOf(form.getRoleId()));
		String[] orgGroupsIdList = form.getGroups();
		if( orgGroupsIdList != null && orgGroupsIdList.length > 0 ) {
			orgGroupsList = orgGroupLearnerGroupService.getOrgGroupsById(orgGroupsIdList);
		}
		// ############################################
		Map<Object, Object> context = new HashMap<Object, Object>();
		String[] selectedUserValues = new String[form.getSelectedLearners().size()];
		for( int i = 0; i < form.getSelectedLearners().size(); i ++ ){
			LearnerItemForm l = form.getSelectedLearners().get(i);
			selectedUserValues[i] = l.getUser().getId().toString();
		}

		if (selectedUserValues != null) {
			long userIdArray[] = new long[selectedUserValues.length];
			for (int loop = 0; loop < selectedUserValues.length; loop++) {
				String userID = selectedUserValues[loop];
				if (userID != null)
					userIdArray[loop] = Long.parseLong(userID);
			}
			learnerService.unAssignUsersFromAllRolesOfType(selectedUserValues,r.getRoleType());
			learnerService.assignUserToRole(userIdArray, r, form.getManageAll(), orgGroupsList);
		}
		context.put("number", form.getSelectedLearners().size());
		context.put("learners", form.getSelectedLearners());
		context.put("roleName", r.getRoleName());
		return new ModelAndView(finishTemplate, "context", context);
	}


	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}

}