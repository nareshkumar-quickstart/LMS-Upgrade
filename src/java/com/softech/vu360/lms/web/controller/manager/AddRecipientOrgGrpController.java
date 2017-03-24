package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.OrgGroupAlertRecipient;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.RecipientOrgGrpForm;
import com.softech.vu360.lms.web.controller.validator.AddRecipientOrgGrpValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;

public class AddRecipientOrgGrpController  extends AbstractWizardFormController {
	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddRecipientOrgGrpController.class.getName());
	private String finishTemplate = null;
	private static final String ORGANIZATION_GROUPS = "organizationgroups";
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VU360UserService vu360UserService;

	public AddRecipientOrgGrpController(){
		super();
		setCommandName("addRecipientOrgGroupForm");
		setCommandClass(RecipientOrgGrpForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageRecipient/addOrgGroup/add1"
		});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject");

		Object command = super.formBackingObject(request);
		try{
			RecipientOrgGrpForm form = (RecipientOrgGrpForm)command;
			String recipientId = request.getParameter("recipientId");
			form.setrecipientId(Long.parseLong(recipientId));
			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<OrganizationalGroup> orgGroup = orgGroupLearnerGroupService.getAllOrganizationalGroups(loggedInUserVO.getLearner().getCustomer().getId());
			form.setOrgGroupListFromDB(orgGroup);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		RecipientOrgGrpForm form = (RecipientOrgGrpForm)command;

		int i=0;
		int j=0;
		OrganizationalGroup orgitem;
		String orgselecteditem;
		List<OrganizationalGroup> selectedOrgGroupList =  new ArrayList<OrganizationalGroup>();
		if(form.getOrgGroup()!=null)
			for(;i<form.getOrgGroupListFromDB().size();){
				orgitem = form.getOrgGroupListFromDB().get(i);
				for(;j<form.getOrgGroup().length;){
					orgselecteditem = (form.getOrgGroup())[j];
					if(orgitem != null){
						if(orgitem.getId()==(Long.parseLong(orgselecteditem))){
							selectedOrgGroupList.add(orgitem);
							break;
						}
					}
					j++;
				}
				j=0;
				i++;
			}
		form.setSelectedOrgGroupList(selectedOrgGroupList);
		Long recipientId = form.getrecipientId();
		AlertRecipient recipient = surveyService.loadAlertRecipientForUpdate(recipientId);
		OrgGroupAlertRecipient orgGroupAlertRecipient=(OrgGroupAlertRecipient)recipient;
		List<OrganizationalGroup>orgGroups=form.getSelectedOrgGroupList();
		for(OrganizationalGroup orgGroup:orgGroups){
			orgGroupAlertRecipient.getOrganizationalGroups().add(orgGroup);
		}

		surveyService.addAlertRecipient(orgGroupAlertRecipient);

		// TODO Auto-generated method stub
		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageOrganisationGroups");
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		RecipientOrgGrpForm form = (RecipientOrgGrpForm)command;
		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageOrganisationGroups");
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {

		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors,int currentPage) throws Exception {

		log.debug("in GET TARGET PAGE...."+currentPage);
		super.postProcessPage(request, command, errors, currentPage);
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		Map model = new HashMap();
		RecipientOrgGrpForm form = (RecipientOrgGrpForm)command;
		switch(page){
		case 0:
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Long customerId = null;
			if (vu360UserService.hasAdministratorRole(loggedInUser))
				customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			else
				customerId = loggedInUser.getLearner().getCustomer().getId();

			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
			String[] orgGroupList = form.getOrgGroup();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(orgGroupList!=null && orgGroupList.length>0){
				for(String orgGroup:orgGroupList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			model.put("orgGroupTreeAsList", treeAsList);
			return model;
		default:
			break;
		}

		return super.referenceData(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddRecipientOrgGrpValidator validator = (AddRecipientOrgGrpValidator) this.getValidator();
		RecipientOrgGrpForm form = (RecipientOrgGrpForm) command;
		switch(page){
		case 0:
			validator.validateOrgGroupSelected(form, errors);
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
}

