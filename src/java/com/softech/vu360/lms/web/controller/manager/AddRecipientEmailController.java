package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.model.EmailAddressAlertRecipient;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.RecipientEmailForm;
import com.softech.vu360.lms.web.controller.validator.EmailValidaTOR;

public class AddRecipientEmailController extends AbstractWizardFormController {

private int NUMBER_OF_EMAILS_TOBE_ADDED = 1;
	
	private SurveyService surveyService = null;
	private static transient Logger log = Logger.getLogger(AddRecipientEmailController.class.getName());

	private String finishTemplate = null;

	
	public AddRecipientEmailController(){
		super();
		setCommandName("recipientEmailForm");
		setCommandClass(RecipientEmailForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageRecipient/addEmail/add1"
				, "manager/userGroups/survey/manageAlert/manageRecipient/addEmail/add2"
			});
	}


	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);

		try{
			RecipientEmailForm form = (RecipientEmailForm)command;
			String recipientId = request.getParameter("recipientId");
			form.setrecipientId(Long.parseLong(recipientId));
			ArrayList<String> emailAddresses = new ArrayList <String>();
			emailAddresses.add("");
			form.setEmailAddresses(emailAddresses);
		}

		catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}


	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {
		
		RecipientEmailForm form = (RecipientEmailForm)command;
		AlertRecipient recipient = surveyService.loadAlertRecipientForUpdate(form.getrecipientId());
		
		EmailAddressAlertRecipient emailAddressAlertRecipient = (EmailAddressAlertRecipient)recipient;
		if (form.getEmailAddresses() != null && form.getEmailAddresses().size() > 0) {
			List<EmailAddress> emails = new ArrayList<EmailAddress>();
			for ( String email : form.getEmailAddresses()) {
				EmailAddress emailAdd = new EmailAddress();
				emailAdd.setEmail(email);
				emails.add(emailAdd);
			}
			emailAddressAlertRecipient.getEmailAddress().addAll(emails);
		}
		
		surveyService.addAlertRecipient(emailAddressAlertRecipient);

		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageEmailAddress");

	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		RecipientEmailForm form = (RecipientEmailForm)command;

		return new ModelAndView("redirect:mgr_manageRecipient.do?recipientId="+form.getrecipientId()+"&method=SearchEditRecipientPageEmailAddress");

		//return super.processCancel(request, response, command, error);

	}


	protected void onBindAndValidate(HttpServletRequest request,

			Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub

		super.onBindAndValidate(request, command, errors, page);

	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors,int page) throws Exception {
		RecipientEmailForm form = (RecipientEmailForm)command;
		if( page == 0 ) {
			String action = form.getAction();
			if( !StringUtils.isBlank(action) && action.equalsIgnoreCase("add") ) {
				NUMBER_OF_EMAILS_TOBE_ADDED++;
				ArrayList<String> emailAddresses = form.getEmailAddresses();
				emailAddresses.add("");
				form.setEmailAddresses(emailAddresses);
				form.setNumberOfEmails(NUMBER_OF_EMAILS_TOBE_ADDED);
			} else if( !StringUtils.isBlank(action) && action.equalsIgnoreCase("delete") ) {
				NUMBER_OF_EMAILS_TOBE_ADDED--;
				ArrayList<String> emailAddresses = form.getEmailAddresses();
				emailAddresses.remove(emailAddresses.size()-1);
				form.setEmailAddresses(emailAddresses);
				form.setNumberOfEmails(NUMBER_OF_EMAILS_TOBE_ADDED);
			}
		}
		
		super.postProcessPage(request, command, errors, page);
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		// TODO Auto-generated method stub
		return super.referenceData(request, command, errors, page);

	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		EmailValidaTOR validator = (EmailValidaTOR)this.getValidator();
		RecipientEmailForm form = (RecipientEmailForm)command;
		switch(page){
		/*case 0:
			validator.validateFirstPage(form, errors);
			break;*/
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			validator.validateEmailPage(form, errors);
			break;
		case 6:
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


	public Logger getLog() {
		return log;
	}


	public void setLog(Logger log) {
		this.log = log;
	}


}