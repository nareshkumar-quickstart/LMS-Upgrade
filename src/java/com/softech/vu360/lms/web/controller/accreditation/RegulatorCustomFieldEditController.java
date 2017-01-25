package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldContext;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CustomFieldValidator;

public class RegulatorCustomFieldEditController extends VU360BaseMultiActionController {

	private AccreditationService accreditationService = null;

	private String defaultCustomFieldTemplate;
	private String closeCredentialTemplate;
	private String closeCredentialRequirementTemplate;
	private	String closeRegulatorTemplate=null;
	private	String closeApprovalTemplate=null;
	private	String closeProviderTemplate=null;
	private	String closeInstructorTemplate=null;
	private	String closeGlobalCustomFieldTemplate=null;
	
	public static final String CUSTOMFIELD_ENTITY_REGULATOR = "Regulator";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "Credentials";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT = "CredentialRequirement";
	public static final String CUSTOMFIELD_ENTITY_PROVIDERS = "Providers";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTORS = "Instructors";
	public static final String CUSTOMFIELD_ENTITY_COURSE_APPROVALS = "Course Approvals";
	public static final String CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS = "Provider Approvals";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS = "Instructor Approvals";
	

	public RegulatorCustomFieldEditController() {
		super();
	}

	public RegulatorCustomFieldEditController(Object delegate) {
		super(delegate);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String)
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if(command instanceof CustomFieldForm){
			CustomFieldForm form = (CustomFieldForm)command;
			if(methodName.equals("displayCustomFieldForEdit")){
				CustomField customField = accreditationService.loadForUpdateCustomField(form.getCustomFieldId());
				form.setCustomField(customField);
				form.setEntity(this.getEntity(customField));
				/*set entity type for  custom fields that are not global*/
				if(form.getEntity().length() == 0 && request.getParameter("entity")!= null){
					form.setEntity(request.getParameter("entity"));	
				}

			}
		}
 
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#validate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, java.lang.String)
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		if(methodName.equals("saveCustomField")){
			CustomFieldValidator validator = (CustomFieldValidator)this.getValidator();
			CustomFieldForm form = (CustomFieldForm)command;
			form.getCustomField().setFieldLabel(form.getFieldLabel());
			if (form.getCustomField() instanceof SingleSelectCustomField || form.getCustomField() instanceof MultiSelectCustomField) {
				this.readOptions(form);
			}
			validator.validateAddCustomFieldPage(form, errors);
		}
	}

	@SuppressWarnings("static-access")
	public ModelAndView displayCustomFieldForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = form.getCustomField();
		if (customField instanceof SingleLineTextCustomFiled) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)form.getCustomField();
			form.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
			form.setFieldLabel(singleLineTextCustomFiled.getFieldLabel());
			form.setFieldEncrypted(singleLineTextCustomFiled.getFieldEncrypted());
			form.setFieldRequired(singleLineTextCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(singleLineTextCustomFiled.getCustomFieldDescription());
		} else if (customField instanceof DateTimeCustomField) {
			DateTimeCustomField dateTimeCustomField = (DateTimeCustomField)form.getCustomField();
			form.setFieldLabel(dateTimeCustomField.getFieldLabel());
			form.setFieldEncrypted(dateTimeCustomField.getFieldEncrypted());
			form.setFieldRequired(dateTimeCustomField.getFieldRequired());
			form.setCustomFieldDescription(dateTimeCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_DATE);
		} else if (customField instanceof MultipleLineTextCustomfield) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)form.getCustomField();
			form.setFieldLabel(multipleLineTextCustomfield.getFieldLabel());
			form.setFieldEncrypted(multipleLineTextCustomfield.getFieldEncrypted());
			form.setFieldRequired(multipleLineTextCustomfield.getFieldRequired());
			form.setCustomFieldDescription(multipleLineTextCustomfield.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
		} else if (customField instanceof NumericCusomField) {
			NumericCusomField numericCusomField = (NumericCusomField)form.getCustomField();
			form.setFieldLabel(numericCusomField.getFieldLabel());
			form.setFieldEncrypted(numericCusomField.getFieldEncrypted());
			form.setFieldRequired(numericCusomField.getFieldRequired());
			form.setCustomFieldDescription(numericCusomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_NUMBER);
		} else if (customField instanceof SSNCustomFiled) {
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)form.getCustomField();
			form.setFieldLabel(sSNCustomFiled.getFieldLabel());
			form.setFieldEncrypted(sSNCustomFiled.getFieldEncrypted());
			form.setFieldRequired(sSNCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(sSNCustomFiled.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
		} else if (customField instanceof SingleSelectCustomField) {
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)form.getCustomField();
			form.setFieldLabel(singleSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(singleSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(singleSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(singleSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(singleSelectCustomField.getAlignment())){
				if (singleSelectCustomField.getAlignment().equalsIgnoreCase(singleSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(singleSelectCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
		} else if (customField instanceof MultiSelectCustomField) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)form.getCustomField();
			form.setFieldLabel(multiSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(multiSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(multiSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(multiSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(multiSelectCustomField.getAlignment())){
				if (multiSelectCustomField.getAlignment().equalsIgnoreCase(multiSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(multiSelectCustomField.getCustomFieldDescription());
			if (multiSelectCustomField.getCheckBox()){
				form.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
			}else{
				form.setFieldType(form.CUSTOMFIELD_CHOOSE);
			}
		} 
		return new ModelAndView(defaultCustomFieldTemplate);
	}

	@SuppressWarnings("static-access")
	public ModelAndView saveCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if(errors.hasErrors()){
			return new ModelAndView(defaultCustomFieldTemplate);
		}
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = accreditationService.getCustomFieldById(form.getCustomField().getId());
		if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)customField;
			singleLineTextCustomFiled.setCustomFieldDescription(form.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(form.isFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(form.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(singleLineTextCustomFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_DATE)) {
			DateTimeCustomField dateTimeCustomField =  (DateTimeCustomField)form.getCustomField();
			dateTimeCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(form.isFieldEncrypted());
			dateTimeCustomField.setFieldLabel(form.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(dateTimeCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)customField;
			multipleLineTextCustomfield.setCustomFieldDescription(form.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(form.isFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(form.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(multipleLineTextCustomfield);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_NUMBER)) {
			NumericCusomField numericCusomField = (NumericCusomField)form.getCustomField();
			numericCusomField.setCustomFieldDescription(form.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(form.isFieldEncrypted());
			numericCusomField.setFieldLabel(form.getFieldLabel().trim());
			numericCusomField.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(numericCusomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)customField;
			sSNCustomFiled.setCustomFieldDescription(form.getCustomFieldDescription());
			sSNCustomFiled.setFieldEncrypted(form.isFieldEncrypted());
			sSNCustomFiled.setFieldLabel(form.getFieldLabel().trim());
			sSNCustomFiled.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(sSNCustomFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_RADIO_BUTTON)) {
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)customField;
			singleSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			singleSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			singleSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			singleSelectCustomField.setFieldRequired(form.isFieldRequired());
			if (form.isAlignment())
				singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
			else
				singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(singleSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(singleSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(singleSelectCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_CHOOSE)) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			multiSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			multiSelectCustomField.setFieldRequired(form.isFieldRequired());
			multiSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(multiSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(multiSelectCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_CHECK_BOX)) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			multiSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			multiSelectCustomField.setFieldRequired(form.isFieldRequired());
			multiSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(multiSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(multiSelectCustomField);
		}
		return new ModelAndView("redirect:acc_manageRegulator.do?method=editRegulatorCustomField");
	}

	@SuppressWarnings("static-access")
	public ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		return new ModelAndView("redirect:acc_manageRegulator.do?method=editRegulatorCustomField");
	}
  
	private String getOption(List<CustomFieldValueChoice> optionList) {
		String optionString="";
		//Collections.sort(optionList);
		for (CustomFieldValueChoice option : optionList) {
			optionString = optionString + option.getLabel() + "\n";
		}
		return optionString;
	}

	private void readOptions(CustomFieldForm form){
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

		try {
			List<String> optionList = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						optionList.add(str);
					}
				}
			}
			form.setOptionList(optionList);
		} catch(IOException e) {
			e.printStackTrace();
		}		
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
	 * @return the closeCredentialTemplate
	 */
	public String getCloseCredentialTemplate() {
		return closeCredentialTemplate;
	}

	/**
	 * @param closeCredentialTemplate the closeCredentialTemplate to set
	 */
	public void setCloseCredentialTemplate(String closeCredentialTemplate) {
		this.closeCredentialTemplate = closeCredentialTemplate;
	}

	/**
	 * @return the defaultCustomFieldTemplate
	 */
	public String getDefaultCustomFieldTemplate() {
		return defaultCustomFieldTemplate;
	}

	/**
	 * @param defaultCustomFieldTemplate the defaultCustomFieldTemplate to set
	 */
	public void setDefaultCustomFieldTemplate(String defaultCustomFieldTemplate) {
		this.defaultCustomFieldTemplate = defaultCustomFieldTemplate;
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
	
	/**
	 * @return the closeRegulatorTemplate
	 */
	public String getCloseRegulatorTemplate() {
		return closeRegulatorTemplate;
	}

	/**
	 * @param closeRegulatorTemplate the closeRegulatorTemplate to set
	 */
	public void setCloseRegulatorTemplate(String closeRegulatorTemplate) {
		this.closeRegulatorTemplate = closeRegulatorTemplate;
	}

	/**
	 * @return the closeGlobalCustomFieldTemplate
	 */
	public String getCloseGlobalCustomFieldTemplate() {
		return closeGlobalCustomFieldTemplate;
	}

	/**
	 * @param closeGlobalCustomFieldTemplate the closeGlobalCustomFieldTemplate to set
	 */
	public void setCloseGlobalCustomFieldTemplate(
			String closeGlobalCustomFieldTemplate) {
		this.closeGlobalCustomFieldTemplate = closeGlobalCustomFieldTemplate;
	}

	/**
	 * @return the closeProviderTemplate
	 */
	public String getCloseProviderTemplate() {
		return closeProviderTemplate;
	}

	/**
	 * @param closeProviderTemplate the closeProviderTemplate to set
	 */
	public void setCloseProviderTemplate(String closeProviderTemplate) {
		this.closeProviderTemplate = closeProviderTemplate;
	}

	/**
	 * @return the closeInstructorTemplate
	 */
	public String getCloseInstructorTemplate() {
		return closeInstructorTemplate;
	}

	/**
	 * @param closeInstructorTemplate the closeInstructorTemplate to set
	 */
	public void setCloseInstructorTemplate(String closeInstructorTemplate) {
		this.closeInstructorTemplate = closeInstructorTemplate;
	}
	
	public String getCloseCredentialRequirementTemplate() {
		return closeCredentialRequirementTemplate;
	}

	public void setCloseCredentialRequirementTemplate(
			String closeCredentialRequirementTemplate) {
		this.closeCredentialRequirementTemplate = closeCredentialRequirementTemplate;
	}

	private String getEntity(CustomField customField){
		
		CustomFieldContext customFieldContext = customField.getCustomFieldContext();
		if(customFieldContext !=null){
			if(customFieldContext.isGlobalRegulator() ){//Regulator
				return CUSTOMFIELD_ENTITY_REGULATOR;
			}else if(customFieldContext.isGlobalCredential()){//Credentials
				return CUSTOMFIELD_ENTITY_CREDENTIALS;
			}else if(customFieldContext.isGlobalProvider()){//Providers
				return CUSTOMFIELD_ENTITY_PROVIDERS;
			}else if(customFieldContext.isGlobalInstructor()){//Instructors
				return CUSTOMFIELD_ENTITY_INSTRUCTORS;
			}else if(customFieldContext.isGlobalCourseApproval()){//Course Approvals
				return CUSTOMFIELD_ENTITY_COURSE_APPROVALS;
			}else if(customFieldContext.isGlobalProviderApproval()){//Provider Approvals
				return CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS;
			}else if(customFieldContext.isGlobalInstructorApproval()){//Instructor Approvals
				return CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS;
			}
		}
		return "";
	}

}