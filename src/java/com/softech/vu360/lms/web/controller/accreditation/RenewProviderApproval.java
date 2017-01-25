package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditApprovalValidator;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.HtmlEncoder;

/**
 * 
 * @author Saptarshi
 *
 */
public class RenewProviderApproval extends AbstractWizardFormController {
	
	public static final String CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL = "CUSTOMFIELD_PROVIDERAPPROVAL";

	private AccreditationService accreditationService = null;

	private	String closeApprovalTemplate=null;


	public RenewProviderApproval() {
		super();
		setCommandName("approvalForm");
		setCommandClass(ApprovalForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/editApproval/renewProviderApproval/renew_approval_provider"
				, "accreditation/approvals/editApproval/renewProviderApproval/renew_approval_provider_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (request.getParameter("appID") != null) {
			long providerApprovallId = Long.parseLong(request.getParameter("appID"));
			ProviderApproval providerApproval = accreditationService.getProviderApprovalById(providerApprovallId);
			form.setProviderApproval(providerApproval);
			
			ProviderApproval renewProviderApproval = new ProviderApproval();
			renewProviderApproval.setApprovedProviderName(providerApproval.getApprovedProviderName());
			renewProviderApproval.setContentOwner(providerApproval.getContentOwner());
			renewProviderApproval.setCourseApprovalEffectivelyEndDate(providerApproval.getCourseApprovalEffectivelyEndDate());
			renewProviderApproval.setCustomFields(providerApproval.getCustomFields());
			renewProviderApproval.setCustomFieldValues(providerApproval.getCustomFieldValues());
			renewProviderApproval.setDocuments(providerApproval.getDocuments());
			renewProviderApproval.setOtherProviderRepresentative(providerApproval.getOtherProviderRepresentative());
			renewProviderApproval.setProvider(providerApproval.getProvider());
			renewProviderApproval.setProviderApprovalEffectivelyStartDate(providerApproval.getProviderApprovalEffectivelyStartDate());
			renewProviderApproval.setProviderApprovalNumber(providerApproval.getProviderApprovalNumber());
			renewProviderApproval.setProviderApprovalPeriod(providerApproval.getProviderApprovalPeriod());
			renewProviderApproval.setProviderApprovalStatus(providerApproval.getProviderApprovalStatus());
			renewProviderApproval.setProviderDirector(providerApproval.getProviderDirector());
			renewProviderApproval.setProviderInstructorOfRecord(providerApproval.getProviderInstructorOfRecord());
			renewProviderApproval.setProviderMostRecentlySubmittedForApprovalDate(providerApproval.getProviderMostRecentlySubmittedForApprovalDate());
			renewProviderApproval.setProviderOriginallyApprovedDate(providerApproval.getProviderOriginallyApprovedDate());
			renewProviderApproval.setProviderSubmissionReminderDate(providerApproval.getProviderSubmissionReminderDate());
			renewProviderApproval.setRegulatorCategories(providerApproval.getRegulatorCategories());
			renewProviderApproval.setRequirements(providerApproval.getRequirements());
			
			form.setRenewProviderApproval(renewProviderApproval);
			
			form.setApprovalEffectivelyStartDate(formatter.format(providerApproval.getProviderApprovalEffectivelyStartDate()));
			form.setApprovalEffectivelyEndDate(formatter.format(providerApproval.getCourseApprovalEffectivelyEndDate()));
			form.setMostRecentlySubmittedForApprovalDate(formatter.format(providerApproval.getProviderMostRecentlySubmittedForApprovalDate()));
			form.setSubmissionReminderDate(formatter.format(providerApproval.getProviderSubmissionReminderDate()));
			form.setOriginallyApprovedDate(formatter.format(providerApproval.getProviderOriginallyApprovedDate()));
			
			List<CustomFieldValue> customFieldValues = providerApproval.getCustomFieldValues();
			List<CustomField> courseApprovalCustomFieldList = providerApproval.getCustomFields();
			List<CustomField> globalCustomFieldList  = accreditationService.findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL), "", "");
			form.setCustomFields(this.arrangeCustomField(courseApprovalCustomFieldList, customFieldValues, globalCustomFieldList));
		}
		return command;
	}
	
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> arrangeCustomField(List<CustomField> courseApprovalCustomFieldList, List<CustomFieldValue> customFieldValues, List<CustomField> globalCustomFieldList){

		CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
		List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
		Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();
		List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();

		for (CustomField globalCustomField : globalCustomFieldList){
			totalCustomFieldList.add(globalCustomField);
		}
		for (CustomField customField : courseApprovalCustomFieldList){
			totalCustomFieldList.add(customField);
		}

		for(CustomField customField:totalCustomFieldList){
			if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
				List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
				fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

				if (customField instanceof MultiSelectCustomField){
					CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
					existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
				}

			}else {
				fieldBuilder.buildCustomField(customField,0,customFieldValues);
			}
		}

		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList){
			if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
				List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
				Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

				for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
					tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
				}

				for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
					if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
						customFieldValueChoice.setSelected(true);
					}
				}
			}
		}
		return customFieldList;
	}

	
	/**
	 * For getting custom field values by custom field.
	 * @param customField
	 * @param customFieldValues
	 * @return CustomFieldValue
	 */
	private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,List<CustomFieldValue> customFieldValues){
		if (customFieldValues != null){
			for (CustomFieldValue customFieldValue : customFieldValues){
				if (customFieldValue.getCustomField()!=null){
					if (customFieldValue.getCustomField().getId().compareTo(customField.getId())==0){
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// TODO Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		super.postProcessPage(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();

		context.put("target", "showProviderApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		ApprovalForm form = (ApprovalForm)command;

		ProviderApproval renewProviderApproval = form.getRenewProviderApproval();
		ProviderApproval providerApproval = form.getProviderApproval();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (renewProviderApproval != null) {
			
			if (form.getApprovalEffectivelyStartDate() != null )
				renewProviderApproval.setProviderApprovalEffectivelyStartDate(formatter.parse(form.getApprovalEffectivelyStartDate()));
			if (form.getApprovalEffectivelyEndDate() != null )
				renewProviderApproval.setCourseApprovalEffectivelyEndDate(formatter.parse(form.getApprovalEffectivelyEndDate()));
			if (form.getMostRecentlySubmittedForApprovalDate() != null )
				renewProviderApproval.setProviderMostRecentlySubmittedForApprovalDate(formatter.parse(form.getMostRecentlySubmittedForApprovalDate()));
			if (form.getSubmissionReminderDate() != null )
				renewProviderApproval.setProviderSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			if (form.getOriginallyApprovedDate() != null )
				renewProviderApproval.setProviderOriginallyApprovedDate(formatter.parse(form.getOriginallyApprovedDate()));
			
			renewProviderApproval.setDerivedFrom(form.getProviderApproval());
			renewProviderApproval.setCustomFieldValues(this.saveCustomField(form.getCustomFields()));
			accreditationService.saveProviderApproval(renewProviderApproval);
			
			
			providerApproval.setActive(false);
			accreditationService.saveProviderApproval(providerApproval);
			
		}
		
		
		Map<Object, Object> context = new HashMap<Object, Object>();

		context.put("target", "showProviderApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}
	
	public List<CustomFieldValue> saveCustomField ( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		if (customFields.size()>0){
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFields){

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
						CustomFieldValue customFieldValue = new CustomFieldValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);
					}else {
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

						CustomFieldValue customFieldValue = new CustomFieldValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);

					}

				}else {
					CustomFieldValue customFieldValue = new CustomFieldValue();
					CustomFieldValue customFieldValueRef= customField.getCustomFieldValueRef();
					
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					customFieldValue.setValue(HtmlEncoder.escapeHtmlFull(customFieldValueRef.getValue().toString()).toString());
					customFieldValues.add(customFieldValue);
				}
			}
			if (customFieldValues.size()>0){
				return customFieldValues;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		switch(page){
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		ApprovalForm form = (ApprovalForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateProviderApprovalRenew(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
			break;
		case 1:
			break;
		case 2:
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
	 * @return the closeApprovalTemplate
	 */
	public String getCloseApprovalTemplate() {
		return closeApprovalTemplate;
	}

	/**
	 * @param closeApprovalTemplate the closeApprovalTemplate to set
	 */
	public void setCloseApprovalTemplate(String closeApprovalTemplate) {
		this.closeApprovalTemplate = closeApprovalTemplate;
	}

}
