package com.softech.vu360.lms.web.controller.manager;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserNewService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddAlertForm;
import com.softech.vu360.lms.web.controller.validator.AddAlertValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class AddAlertController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddAlertController.class.getName());

	private String finishTemplate = null;
	private String redirectTemplate=null;
	private SurveyService surveyService = null;
	private LearnerService learnerService = null;
	
	@Inject
	VU360UserNewService vu360UserNewService;
	
	@Autowired
	VU360UserService vu360UserService;

	public SurveyService getSurveyService() {
		return surveyService;
	}


	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}


	public AddAlertController(){
		super();
		setCommandName("addAlertForm");
		setCommandClass(AddAlertForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/addAlert1"
				,"manager/userGroups/survey/manageAlert/addAlert2"});
	}
	
	
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		
		AddAlertForm form = (AddAlertForm) command;
		VU360UserNew vu360UserModel = null;

		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext()
				.getAuthentication()).getDetails();

		if (details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {

			com.softech.vu360.lms.vo.Customer customer = (com.softech.vu360.lms.vo.Customer) request.getSession(true)
					.getAttribute("adminSelectedCustomer");

			com.softech.vu360.lms.vo.Distributor distributorvo = (com.softech.vu360.lms.vo.Distributor) request
					.getSession(true).getAttribute("adminSelectedDistributor");

			if (customer != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(customer.getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId.longValue());

			} else if (distributorvo != null) {

				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorvo.getMyCustomer().getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId.longValue());

			} else {

				vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();

			}
		} else if (details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {

			vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();

		}

		form.getAlert().setCreatedBy(vu360UserModel);
		form.getAlert().setFromName(form.getFromName());
		form.getAlert().setAlertSubject(form.getAlertSubject());
		form.getAlert().setAlertMessageBody(
				form.getAlertMessageBody().substring(0, Math.min(form.getAlertMessageBody().length(), 2000)));
		form.getAlert().setIsDefault(Boolean.parseBoolean(form.getIsDefaultMessage()));
		form.getAlert().setCreatedDate(new Date());
		surveyService.addAlert(form.getAlert());
		return new ModelAndView(redirectTemplate);
	}
	
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(redirectTemplate);
	}


	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		
		AddAlertForm form = (AddAlertForm) command;
		if(request.getParameter("chkValue")!=null && !request.getParameter("chkValue").equals("")) {
			form.setIsDefaultMessage(request.getParameter("chkValue"));
		}
		
		super.onBindAndValidate(request, command, errors, page);
	}


	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		
		super.postProcessPage(request, command, errors, page);
	}


	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		AddAlertForm form = (AddAlertForm) command;
		
		
			com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
			lang.setLanguage(Language.DEFAULT_LANG);
			Brander brand = VU360Branding.getInstance().getBrander(VU360Branding.DEFAULT_BRAND, lang);
			if(form.getIsDefaultMessage()!=null && form.getIsDefaultMessage().equalsIgnoreCase("true")){
				form.setAlertMessageBody(brand.getBrandElement("lms.manager.manageAlert.addAlert.caption.detaultMessage"));
				form.setFromName(VU360Properties.getVU360Property("lms.email.alert.default.fromName"));
				form.setAlertSubject(VU360Properties.getVU360Property("lms.email.alert.default.subject"));
			}
			
		
		
		return super.referenceData(request, form, errors, page);
		
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddAlertValidator validator = (AddAlertValidator)this.getValidator();
		AddAlertForm form = (AddAlertForm)command;
		switch(page){
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}
	
	public String getFinishTemplate() {
		return finishTemplate;
	}
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}


	public String getRedirectTemplate() {
		return redirectTemplate;
	}


	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}


	public LearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}
