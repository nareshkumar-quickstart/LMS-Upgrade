package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertRecipient;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.RecipientLearnerForm;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddRecipientLearnerController extends AbstractWizardFormController{

	private SurveyService surveyService = null;
	private static final Logger log = Logger.getLogger(AddRecipientLearnerController.class.getName());
	private String finishTemplate = null;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;

	public AddRecipientLearnerController(){
		super();

		setCommandName("addRecipientLearnerForm");

		setCommandClass(RecipientLearnerForm.class);

		setSessionForm(true);

		this.setBindOnNewForm(true);

		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageRecipient/addLearner/add1"
			});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		try{
			RecipientLearnerForm form = (RecipientLearnerForm)command;

			String recipientId = request.getParameter("recipientId");
			form.setRecipientId(Long.parseLong(recipientId));
			}

		catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		RecipientLearnerForm form =(RecipientLearnerForm)command;
		
		Long recipientId = form.getRecipientId();
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
		List<VU360User> users = learnerService.findLearner(firstName, lastName, email, 
				loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
				loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
				loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId());
		form.setLearnerListFromDB(users);
		
		int i=0, j=0;
		VU360User item0;
		String selecteditem0;
		
		List<Learner> learnerss = new ArrayList<Learner>();
		if(form.getSelectedLearner()!=null){
			for(;i<form.getLearnerListFromDB().size();){
				item0 = form.getLearnerListFromDB().get(i);
				for(;j<form.getSelectedLearner().length;){
					selecteditem0 = (form.getSelectedLearner())[j];
					if(item0 != null){
						if(item0.getId()==(Long.parseLong(selecteditem0))){
							//addRecipientForm.getSelectedLearnerGroupList().add(item);
							learnerss.add(item0.getLearner());
							break;
						}
					}
					j++;
				}
				j=0;
				i++;
			}
		}
		form.setLearnerss(learnerss);
		AlertRecipient recipient = surveyService.loadAlertRecipientForUpdate(recipientId);
		LearnerAlertRecipient learnerAlertRecipient=(LearnerAlertRecipient)recipient;
		List<Learner> learnersList=form.getLearnerss();
		for(Learner learner:learnersList){
			learnerAlertRecipient.getLearners().add(learner);
		}
		surveyService.addAlertRecipient(learnerAlertRecipient);
		// TODO Auto-generated method stub
		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getRecipientId()+"&method=SearchEditRecipientLearners");
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		RecipientLearnerForm form =(RecipientLearnerForm)command;
		log.debug("IN processCancel");
		return new ModelAndView("redirect:mgr_manageRecipient.do?method=SearchEditRecipientLearners&recipientId=" + form.getRecipientId());
		//return super.processCancel(request, response, command, error);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors,int currentPage) throws Exception {
		
/*		RecipientLearnerForm form =(RecipientLearnerForm)command;
		Map <Object, Object>model = new HashMap <Object, Object>();
		Map <Object, Object>surveyMethodMap = new LinkedHashMap <Object, Object>();*/
		// TODO Auto-generated method stub
		log.debug("in GET TARGET PAGE...."+currentPage);
		/*if(currentPage==3 && this.getTargetPage(request, currentPage) == 4)
		{
			String Learner[]=request.getParameterValues("learnerGroup");
			
		}*/
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
		//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		RecipientLearnerForm form =(RecipientLearnerForm)command;
		log.debug("in GET TARGET PAGE...."+currentPage);
		if(currentPage==0 && this.getTargetPage(request, currentPage) == 0){
				form.setPageIndex("0");	
				String firstName=request.getParameter("firstName");
				String lastName=request.getParameter("lastName");
				String email=request.getParameter("email");
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails(); 
				List<VU360User> users= null;				
								
				if(request.getParameter("search") != null){
					if(request.getParameter("search").equalsIgnoreCase("doSearch")){
						if( details.getCurrentVOCustomer() != null && details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR){		
							Map<Object,Object> results = new HashMap<Object,Object>();
							results = learnerService.findAllLearnersOfCustomersOfReseller(firstName, lastName, email, 1, -1, "firstName", 0, null);
							 users = (List<VU360User>)results.get("list");
						 }else{
							 List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
						     users = learnerService.findLearner(firstName, lastName, email, 
						    		 loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
										loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
										loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId());
						 }
						form.setLearnerListFromDB(users);
					}
				}else{
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

}
