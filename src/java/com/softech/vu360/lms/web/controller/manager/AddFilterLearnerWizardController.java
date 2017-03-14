
package com.softech.vu360.lms.web.controller.manager;



import java.util.ArrayList;
import java.util.Arrays;
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

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertFilter;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.FilterLearnerForm;



public class AddFilterLearnerWizardController  extends AbstractWizardFormController{
	
	
	
	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddTriggerWizardController.class.getName());

	private String finishTemplate = null;



	
	private VU360UserService vu360UserService;

		
	private LearnerService learnerService;

	public AddFilterLearnerWizardController(){

		super();

		setCommandName("addFilterLearnerForm");

		setCommandClass(FilterLearnerForm.class);

		setSessionForm(true);

		this.setBindOnNewForm(true);

		setPages(new String[] {

				"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addLearner/add1"
								
			});

	}



	

	protected Object formBackingObject(HttpServletRequest request) throws Exception {



		log.debug("IN formBackingObject");



		Object command = super.formBackingObject(request);

		
		try{

			FilterLearnerForm form = (FilterLearnerForm)command;

			String filterId = request.getParameter("filterId");
			form.setFilterId(Long.parseLong(filterId));
					
			}

		catch (Exception e) {

			log.debug("exception", e);

		}

		return command;

	}



	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		
		FilterLearnerForm form = (FilterLearnerForm) command;
		LearnerAlertFilter learnerAlertFilter = (LearnerAlertFilter) surveyService.getAlertTriggerFilterByID(form.getFilterId());

		Set<Learner> selectedLearners = form.getLearnerListFromDB().stream()
				.filter(vu360User -> Arrays.asList(form.getSelectedLearner()).contains(vu360User.getId().toString()))
				.map(vu360User -> vu360User.getLearner())
				.collect(Collectors.toSet());
		
		selectedLearners.addAll(learnerAlertFilter.getLearners());
		learnerAlertFilter.setLearners(selectedLearners.stream().collect(Collectors.toList()));
		form.setLearnerss(selectedLearners.stream().collect(Collectors.toList()));
		learnerAlertFilter.setId(form.getFilterId());
		surveyService.addAlertTriggerFilter(learnerAlertFilter);

		return new ModelAndView(
				"redirect:mgr_manageFilter.do?filterId=" + form.getFilterId() + "&method=searchEditFilterPageLearners");
	}

	



	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		FilterLearnerForm form =(FilterLearnerForm)command;
		return new ModelAndView("redirect:mgr_manageFilter.do?filterId="+form.getFilterId()+"&method=searchEditFilterPageLearners");
		//return super.processCancel(request, response, command, error);

	}


	protected void onBindAndValidate(HttpServletRequest request,

			Object command, BindException errors, int page) throws Exception {

		super.onBindAndValidate(request, command, errors, page);

	}





	protected void postProcessPage(HttpServletRequest request, Object command,

			Errors errors,int currentPage) throws Exception {
		
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


		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FilterLearnerForm form =(FilterLearnerForm)command;
		
		log.debug("in GET TARGET PAGE...."+currentPage);

		

		if(currentPage==0 && this.getTargetPage(request, currentPage) == 0
				&& request.getParameter("search") != null){

			
				form.setPageIndex("0");	
				String firstName=request.getParameter("firstName");
				String lastName=request.getParameter("lastName");
				String email=request.getParameter("email");
				
				
				List<VU360User> users = null;
				if(request.getParameter("search") != null){
					if(request.getParameter("search").equalsIgnoreCase("doSearch")){
						 //VU360User vu360UserModel = vu360UserService.getUserById(loggedInUser.getId());
						List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
						 users = learnerService.findLearner(firstName, lastName, email, 
								 loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId());
						form.setLearnerListFromDB(users);
					}
				}
			
				else{
					form.setLearnerListFromDB(users);
				}
				
				return 0;

		}
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


	public LearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}


	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}



}

