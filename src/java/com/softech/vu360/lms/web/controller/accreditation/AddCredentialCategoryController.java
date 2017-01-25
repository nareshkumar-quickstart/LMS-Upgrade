/**
 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
 */
package com.softech.vu360.lms.web.controller.accreditation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CredentialValidator;
import com.softech.vu360.util.CustomFieldEntityType;

/**
 * @author sana.majeed
 * LMS-8314 :: Regulatory Module Phase II [CredentialCategory] 
 */
public class AddCredentialCategoryController extends AbstractWizardFormController {
	
	private static final Logger log = Logger.getLogger(AddCredentialCategoryController.class.getName());
	
	private AccreditationService accreditationService = null;
	private String closeTemplate = null;
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY = "CUSTOMFIELD_CREDENTIALCATEGORY";
	
	public AddCredentialCategoryController() {
		super();
		setCommandName("credentialForm");
		setCommandClass(CredentialForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {				
				"accreditation/Credential/addCategory/addCategory",
				"accreditation/Credential/addCategory/confirmation"	});		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {		
		
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		
		CredentialForm form = (CredentialForm) command;		
		form.setCategory( new CredentialCategory() );
		
		Long credentialID = Long.valueOf( request.getParameter("credentialId") );
		form.setCid( credentialID );
		
		form.setCredentialRequirementCustomFields(null);
		
		return command;
	}
	
	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		log.debug("IN referenceData > page: " + page);		
		Map<Object, Object> context = new HashMap<Object, Object>();
		CredentialForm form = (CredentialForm) command;
		
		switch (page) {
		case 0:
			// [1/27/2011] LMS-8725 :: Fetch only Global Custom Fields for CredentialCategroy
			if ( form.getCredentialRequirementCustomFields() == null ){
				List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY ), "", "");
				
				if (globalCustomFieldList != null && globalCustomFieldList.size() > 0) {			
					form.setCredentialRequirementCustomFields( this.accreditationService.getCustomFieldsAndValues(globalCustomFieldList, new ArrayList<CustomFieldValue>()) );	
				}
			}
						
			context.put("categoryTypes", CredentialCategory.CATEGORY_TYPES);
			return context;
			
		case 1:
			// [1/27/2011] LMS-8725 :: Fetch only Global Custom Fields for CredentialCategroy
			if (form.getCredentialRequirementCustomFields() != null) {
				// Set Custom Field Values 
				form.getCategory().setCustomFieldValues( this.accreditationService.getCustomFieldValues( form.getCredentialRequirementCustomFields(), false ) );
				List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY ), "", "");
				form.setCredentialRequirementCustomFields( this.accreditationService.getCustomFieldsAndValues(globalCustomFieldList, form.getCategory().getCustomFieldValues()) );
			}
			
		default:
			break;			
		}
		
		return super.referenceData(request, page);
	}
	
	protected void validatePage(Object command, Errors errors, int page) {
		
		log.debug("IN validatePage : page > " + page);
		
		CredentialValidator validator = (CredentialValidator) this.getValidator();
		CredentialForm form = (CredentialForm)command;
		errors.setNestedPath("");
		
		switch(page) {
		case 0:
			validator.validateAddCategory (form, errors);
			break;
		case 1:
			break;
		}
		super.validatePage(command, errors, page);
		
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		
		log.debug("IN onBindAndValidate");
		super.onBindAndValidate(request, command, error, page);
	}
	
	
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		
		log.debug("IN getTargetPage");		
		return super.getTargetPage(request, command, errors, currentPage);
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		
		log.debug("IN processCancel");		
		return new ModelAndView(closeTemplate);
	}
	
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		log.debug("IN processFinish");
		
		CredentialForm form = (CredentialForm) command;
		
		Credential credential = this.accreditationService.getCredentialById( form.getCid() );
		CredentialCategory category = form.getCategory();
		category.setCredential( credential );
		
		// [1/27/2011] LMS-8725 :: Add Category, Save Global Custom Fields values
		if (form.getCredentialRequirementCustomFields() != null && form.getCredentialRequirementCustomFields().size() > 0) {
			category.setCustomFieldValues( this.accreditationService.getCustomFieldValues(form.getCredentialRequirementCustomFields(), true) );
		}
		if(!category.getCategoryType().equalsIgnoreCase(CredentialCategory.POST_LICENSE)){
			category.setCompletionDeadlineMonths(new Float("-1"));
			category.setCompletionDeadlineFrom(null);
		}
		category = this.accreditationService.saveCredentialCategory( category );
		
		return new ModelAndView(closeTemplate);
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

}
