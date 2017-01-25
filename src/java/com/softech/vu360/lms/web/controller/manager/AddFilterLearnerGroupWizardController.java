
package com.softech.vu360.lms.web.controller.manager;



import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.FilterLearnerGroupForm;



public class AddFilterLearnerGroupWizardController   extends AbstractWizardFormController{



	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddFilterLearnerGroupWizardController.class.getName());

	private String finishTemplate = null;




	private VU360UserService vu360UserService;



	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	public AddFilterLearnerGroupWizardController(){

		super();

		setCommandName("addFilterLearnerGroupForm");

		setCommandClass(FilterLearnerGroupForm.class);

		setSessionForm(true);

		this.setBindOnNewForm(true);

		setPages(new String[] {

				"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addLearnerGroup/add1"

		});

	}


	protected Object formBackingObject(HttpServletRequest request) throws Exception {



		log.debug("IN formBackingObject");



		Object command = super.formBackingObject(request);


		try{

			FilterLearnerGroupForm form = (FilterLearnerGroupForm)command;

			String filterId = request.getParameter("filterId");
			form.setFilterId(Long.parseLong(filterId));

			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360User vu360UserModel = vu360UserService.getUserById(loggedInUser.getId());
			List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getAllLearnerGroups(vu360UserModel.getLearner().getCustomer().getId(), vu360UserModel);
			form.setLearnerGroupListFromDB(learnerGroups);

		}

		catch (Exception e) {

			log.debug("exception", e);

		}

		return command;

	}



	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {

		FilterLearnerGroupForm form = (FilterLearnerGroupForm) command;

		LearnerGroupAlertFilter learnerGroupAlertFilter = (LearnerGroupAlertFilter) surveyService
				.getAlertTriggerFilterByID(form.getFilterId());

		Set<LearnerGroup> selectedLearnerGroup = form
				.getLearnerGroupListFromDB().stream().filter(learnerGroup -> Arrays
						.asList(form.getLearnerGroup()).contains(learnerGroup.getId().toString()))
				.collect(Collectors.toSet());

		selectedLearnerGroup.addAll(learnerGroupAlertFilter.getLearnerGroup());
		learnerGroupAlertFilter.setLearnerGroup(selectedLearnerGroup.stream().collect(Collectors.toList()));
		learnerGroupAlertFilter.setId(form.getFilterId());
		surveyService.addAlertTriggerFilter(learnerGroupAlertFilter);

		return new ModelAndView("redirect:mgr_manageFilter.do?filterId=" + form.getFilterId()
				+ "&method=searchEditFilterPageLearnerGroups");

	}





	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		FilterLearnerGroupForm form =(FilterLearnerGroupForm)command;

		return new ModelAndView("redirect:mgr_manageFilter.do?filterId="+form.getFilterId()+"&method=searchEditFilterPageLearnerGroups");

		//return super.processCancel(request, response, command, error);

	}


	protected void onBindAndValidate(HttpServletRequest request,

			Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub

		super.onBindAndValidate(request, command, errors, page);

	}





	protected void postProcessPage(HttpServletRequest request, Object command,

			Errors errors,int currentPage) throws Exception {


		FilterLearnerGroupForm form =(FilterLearnerGroupForm)command;
		Map <Object, Object>model = new HashMap <Object, Object>();
		Map <Object, Object>surveyMethodMap = new LinkedHashMap <Object, Object>();
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



	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}





	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}





	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}



}

