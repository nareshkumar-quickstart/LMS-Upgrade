package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CredentialValidator;
import com.softech.vu360.util.CustomFieldEntityType;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddNewCredentialWizardController extends AbstractWizardFormController{


	private AccreditationService accreditationService = null;

	private String closeTemplate = null;
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "CUSTOMFIELD_CREDENTIAL";

	public AddNewCredentialWizardController() {
		super();
		setCommandName("credentialForm");
		setCommandClass(CredentialForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/Credential/addCredential/add_credential"
				, "accreditation/Credential/addCredential/add_credential_confirmation"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CredentialForm form = (CredentialForm)command;
		Credential credential = new Credential();
		form.setCredential(credential);
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		// Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		// Auto-generated method stub
		super.postProcessPage(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException arg3)	throws Exception {
		
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CredentialForm form = (CredentialForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Credential credential = form.getCredential();
		
		if(!StringUtils.isBlank(form.getTotalNumberOfLicense()))
			credential.setTotalNumberOfLicense(Integer.parseInt(form.getTotalNumberOfLicense()));
		
		if ( !StringUtils.isBlank(form.getInformationLastVerifiedDate()) )
			credential.setInformationLastVerifiedDate(formatter.parse(form.getInformationLastVerifiedDate()));
		ContentOwner contentOwner = null;
		if( loggedInUser.getRegulatoryAnalyst() != null ) {
			contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		}
		
		if (form.getCustomFields().size()>0){
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){
				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){
					
					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (multiSelectCustomField.getCheckBox()){
						
						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){

							if (customFieldValueChoice.isSelected()){
								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
								customFieldValueChoices.add(customFieldValueChoiceRef);
							}

						}
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
						
					} else {
						
						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						if(customField.getSelectedChoices()!=null){
							Map<Long,CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
								customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
							}

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								if(customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))){
									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
									customFieldValueChoices.add(customFieldValueChoiceRef);
								}
							}
						}

						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
						
					}
				} else {
					CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
					Object value = customFieldValue.getValue();
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					/*
					 *  for Encryption...
					 */
					if( customField.getCustomFieldRef().getFieldEncrypted() ) {
						customFieldValue.setValue(value);
					}
					customFieldValues.add(customFieldValue);
				}
			}
			if (customFieldValues.size()>0){
				credential.setCustomfieldValues(customFieldValues);
			}
		}
		if(credential.getRenewalDeadlineType().equalsIgnoreCase(credential.HARD)){
			credential.setStaggeredBy(null);
			credential.setStaggeredTo(null);
		}else if(credential.getRenewalDeadlineType().equalsIgnoreCase(credential.STAGGERED)){
			credential.setHardDeadlineMonth(null);
			credential.setHardDeadlineDay(null);
			credential.setHardDeadlineYear(null);
		}else{
			credential.setStaggeredBy(null);
			credential.setStaggeredTo(null);
			credential.setHardDeadlineMonth(null);
			credential.setHardDeadlineDay(null);
			credential.setHardDeadlineYear(null);
		}		
		if( contentOwner != null ) {
			credential.setContentOwner(contentOwner);
			if(credential.getJurisdiction().equalsIgnoreCase("other")){
				credential.setJurisdiction(credential.getOtherJurisdiction());
			}
			accreditationService.saveCredential(credential);
		}

		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		CredentialForm form = (CredentialForm)command;
		switch(page){
		case 0:
			if (!errors.hasErrors() && form.getCustomFields().size()==0){
				List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALS), "", "");
				CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
				
				for(CustomField customField : globalCustomFieldList){
					if (customField instanceof SingleSelectCustomField || 
							customField instanceof MultiSelectCustomField ){
						
						List<CustomFieldValueChoice> customFieldValueChoices=this.getAccreditationService().getOptionsByCustomField(customField);
						fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);
						
					}else {
						fieldBuilder.buildCustomField(customField,0,customFieldValues);
					}
				}
				
				List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
				form.setCustomFields(customFieldList);
			}
			break;
		case 1:
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (!multiSelectCustomField.getCheckBox()){
						
						if(customField.getSelectedChoices()!=null){

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									if(selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())){
										customFieldValueChoice.setSelected(true);
									}
								}
							}
						}
					}
				}
			}
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		CredentialValidator validator = (CredentialValidator)this.getValidator();
		CredentialForm form = (CredentialForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateAddCredential(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
			break;
		case 1:
			break;
		}
		super.validatePage(command, errors, page, finish);
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