package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CredentialValidator;

public class AddRequirementWizardController extends AbstractWizardFormController{


	private AccreditationService accreditationService = null;

	private String closeTemplate = null;

	public AddRequirementWizardController() {
		super();
		setCommandName("credentialForm");
		setCommandClass(CredentialForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {				
				"accreditation/Credential/addRequirement/selectCategory",
				"accreditation/Credential/addRequirement/add_requirement",
				"accreditation/Credential/addRequirement/add_requirement_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CredentialForm form = (CredentialForm)command;
		
		long credentialID = Long.parseLong(request.getParameter("credentialID"));
		form.setCid(credentialID);
		
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		
		CredentialValidator validator = (CredentialValidator)this.getValidator();
		CredentialForm form = (CredentialForm) command;
		
		if (page == 0 && this.getTargetPage(request, page) == 1) {
			Long selCategoryId = Long.valueOf( request.getParameter("categoryId") ); 
			
			CredentialCategory category = this.accreditationService.getCredentialCategoryById(selCategoryId);						
			form.setCategory(category);
			
			if (form.getRequirement() == null) {
				CredentialCategoryRequirement requirement = new CredentialCategoryRequirement();
				form.setRequirement(requirement);
			}			
		}
		else if (page == 1 && this.getTargetPage(request, page) == 2) {
			validator.validateAddRequirement(form, errors);
		}
		
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
				
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		// TODO Auto-generated method stub
		super.postProcessPage(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		CredentialForm form = (CredentialForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		CredentialCategoryRequirement requirement = form.getRequirement();
                requirement.setNumberOfHours( Double.valueOf(form.getCreditHours()));
                
		if ( !StringUtils.isBlank(form.getLicenseRenewalDate()) )
			requirement.setLicenseRenewalDate(formatter.parse(form.getLicenseRenewalDate()));
		if ( !StringUtils.isBlank(form.getCERequirementDeadline()) )
			requirement.setCERequirementDeadline(formatter.parse(form.getCERequirementDeadline()));
		if ( !StringUtils.isBlank(form.getReportingPeriod()) )
			requirement.setReportingPeriod(formatter.parse(form.getReportingPeriod()));
		
		requirement.setCredentialCategory(form.getCategory());
		accreditationService.saveCredentialCategoryRequirement(requirement);
		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		switch(page){
		case 0:
			CredentialForm form = (CredentialForm) command;			
			List<CredentialCategory> categoryList = this.accreditationService.getCredentialCategoryByCredential( form.getCid(), form.getSortDirection() );
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("categoryList", categoryList);
			return context;
			
		case 1:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		
		errors.setNestedPath("");
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}
}
