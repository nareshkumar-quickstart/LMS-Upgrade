
package com.softech.vu360.lms.web.controller.manager;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.OrgGroupAlertFilterTrigger;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.FilterLearnerGroupForm;
import com.softech.vu360.lms.web.controller.model.FilterOrgGroupForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;



public class AddFilterOrgGroupController extends AbstractWizardFormController{
	
	
	
	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddFilterOrgGroupController.class.getName());

	private String finishTemplate = null;

	private static final String ORGANIZATION_GROUPS = "organizationgroups";

	
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	private VU360UserService vu360UserService;

	
	public AddFilterOrgGroupController(){

		super();

		setCommandName("addFilterOrgGroupForm");

		setCommandClass(FilterOrgGroupForm.class);

		setSessionForm(true);

		this.setBindOnNewForm(true);

		setPages(new String[] {

				"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addOrgGroup/add1"

				
			});

	}


	protected Object formBackingObject(HttpServletRequest request) throws Exception {



		log.debug("IN formBackingObject");



		Object command = super.formBackingObject(request);

		
		try{

			FilterOrgGroupForm form = (FilterOrgGroupForm)command;

			String filterId = request.getParameter("filterId");
			form.setFilterId(Long.parseLong(filterId));
			
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<OrganizationalGroup> orgGroup = orgGroupLearnerGroupService.getAllOrganizationalGroups(loggedInUser.getLearner().getCustomer().getId());
			form.setOrgGroupListFromDB(orgGroup);
			
			
		}

		catch (Exception e) {

			log.debug("exception", e);

		}

		return command;

	}


	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		
		FilterOrgGroupForm form = (FilterOrgGroupForm) command;

		OrgGroupAlertFilterTrigger orgGroupAlertFilter = (OrgGroupAlertFilterTrigger) surveyService
				.getAlertTriggerFilterByID(form.getFilterId());

		Set<OrganizationalGroup> selectedOrgGroup = form
				.getOrgGroupListFromDB().stream().filter(orgGroup -> Arrays
						.asList(form.getOrgGroup()).contains(orgGroup.getId().toString()))
				.collect(Collectors.toSet());

		selectedOrgGroup.addAll(orgGroupAlertFilter.getOrgGroups());
		orgGroupAlertFilter.setOrggroup(selectedOrgGroup.stream().collect(Collectors.toList()));
		orgGroupAlertFilter.setId(form.getFilterId());
		surveyService.addAlertTriggerFilter(orgGroupAlertFilter);

		return new ModelAndView("redirect:mgr_manageFilter.do?filterId=" + form.getFilterId()
				+ "&method=searchEditFilterPageOrganisationGroups");

	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		FilterOrgGroupForm form = (FilterOrgGroupForm)command;
		return new ModelAndView("redirect:mgr_manageFilter.do?filterId="+form.getFilterId()+"&method=searchEditFilterPageOrganisationGroups");

		//return super.processCancel(request, response, command, error);

	}


	protected void onBindAndValidate(HttpServletRequest request,

			Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub

		super.onBindAndValidate(request, command, errors, page);

	}

	protected void postProcessPage(HttpServletRequest request, Object command,

			Errors errors,int currentPage) throws Exception {
		
		
		FilterOrgGroupForm form = (FilterOrgGroupForm)command;
		
		
		log.debug("in GET TARGET PAGE...."+currentPage);
		
		
		super.postProcessPage(request, command, errors, currentPage);

	}

	protected Map referenceData(HttpServletRequest request, Object command,

			Errors errors, int page) throws Exception {
		
		log.debug("IN referenceData");
		Map<String, Object> model = new HashMap<>();
		FilterOrgGroupForm form = (FilterOrgGroupForm) command;
		switch (page) {
		case 0:
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			Long customerId = 0L;
			if (loggedInUser.isAdminMode())
				customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication()
						.getDetails()).getCurrentCustomerId();
			else {
				customerId=loggedInUser.getLearner().getCustomer().getId();
			}

			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
			String[] orgGroupList = form.getOrgGroup();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if (orgGroupList != null && orgGroupList.length > 0) {
				for (String orgGroup : orgGroupList) {
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			
			TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, orgGroupIdList,
					vu360UserService.getUserById(loggedInUser.getId()));
			List<TreeNode> treeAsList = orgGroupRoot.bfs();

			model.put("orgGroupTreeAsList", treeAsList);
			return model;
		default:
			break;
		}

		return super.referenceData(request, command, errors, page);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		switch(page){

		case 0:

			break;

		default:

			break;

		}

		super.validatePage(command, errors, page, finish);

	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {


			

		
		
	
		return super.getTargetPage(request, command, errors, currentPage);

	}
	
	 

	public String getFinishTemplate() {

		return finishTemplate;

	}



	public void setFinishTemplate(String finishTemplate) {

		this.finishTemplate = finishTemplate;

	}


	public static String getORGANIZATION_GROUPS() {

		return ORGANIZATION_GROUPS;

	}


	public SurveyService getSurveyService() {
		return surveyService;
	}





	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}



	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}


	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}





	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}


	public Logger getLog() {
		return log;
	}


	public void setLog(Logger log) {
		this.log = log;
	}


}

