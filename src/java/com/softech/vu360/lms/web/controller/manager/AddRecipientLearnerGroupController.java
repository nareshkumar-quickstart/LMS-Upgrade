package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertRecipient;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.RecipientLearnerGroupForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddRecipientLearnerGroupController extends AbstractWizardFormController{
	
	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddRecipientLearnerGroupController.class.getName());
	private String finishTemplate = null;
	private VU360UserService vu360UserService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	
	public AddRecipientLearnerGroupController(){
		super();

		setCommandName("addRecipientLearnerGroupForm");

		setCommandClass(RecipientLearnerGroupForm.class);

		setSessionForm(true);

		this.setBindOnNewForm(true);

		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageRecipient/addLearnerGroup/add1"
		});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		try{
			RecipientLearnerGroupForm form = (RecipientLearnerGroupForm)command;
			String recipientId = request.getParameter("recipientId");
			form.setrecipientId(Long.parseLong(recipientId));
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getAllLearnerGroups(loggedInUser.getLearner().getCustomer().getId(), loggedInUser);
			form.setLearnerGroupListFromDB(learnerGroups);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		RecipientLearnerGroupForm form =(RecipientLearnerGroupForm)command;
		Long recipientId = form.getrecipientId();
		int i=0; 
		int j=0;

		LearnerGroup item;
		String selecteditem;
		List<LearnerGroup> selectedLearnerGroupList =  new ArrayList<LearnerGroup>();

		if(form.getLearnerGroup()!=null)
			for(;i<form.getLearnerGroupListFromDB().size();){
				item = form.getLearnerGroupListFromDB().get(i);
				for(;j<form.getLearnerGroup().length;){
					selecteditem = (form.getLearnerGroup())[j];
					if(item != null){
						if(item.getId()==(Long.parseLong(selecteditem))){
							selectedLearnerGroupList.add(item);
							break;
						}
					}
					j++;
				}
				j=0;
				i++;
			}
		form.setSelectedLearnerGroupList(selectedLearnerGroupList);
		AlertRecipient recipient = surveyService.loadAlertRecipientForUpdate(recipientId);
		LearnerGroupAlertRecipient learnerGroupAlertRecipient=(LearnerGroupAlertRecipient)recipient;
		List<LearnerGroup> learnerGroupList = form.getSelectedLearnerGroupList();
		for(LearnerGroup learnerGroup:learnerGroupList){
			learnerGroupAlertRecipient.getLearnerGroups().add(learnerGroup);
		}
		surveyService.addAlertRecipient(learnerGroupAlertRecipient);
		// TODO Auto-generated method stub
		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageLearnerGroups");
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		RecipientLearnerGroupForm form =(RecipientLearnerGroupForm)command;
		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageLearnerGroups");
		//return super.processCancel(request, response, command, error);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors,int currentPage) throws Exception {

		/*RecipientLearnerGroupForm form =(RecipientLearnerGroupForm)command;
		Map <Object, Object>model = new HashMap <Object, Object>();
		Map <Object, Object>surveyMethodMap = new LinkedHashMap <Object, Object>();*/
		// TODO Auto-generated method stub
		log.debug("in GET TARGET PAGE...."+currentPage);
		super.postProcessPage(request, command, errors, currentPage);
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		// TODO Auto-generated method stub
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

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
}
