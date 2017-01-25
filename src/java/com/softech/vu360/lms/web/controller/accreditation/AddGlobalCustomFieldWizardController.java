package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CustomFieldValidator;

public class AddGlobalCustomFieldWizardController extends AbstractWizardFormController{

	private AccreditationService accreditationService = null;
	private String closeGlobalCustomFieldTemplate = null;

	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	private static final String[] CUSTOMFIELD_TYPES = {
		CUSTOMFIELD_SINGLE_LINE_OF_TEXT
		, CUSTOMFIELD_DATE
		, CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT
		, CUSTOMFIELD_NUMBER
		, CUSTOMFIELD_RADIO_BUTTON
		, CUSTOMFIELD_CHOOSE
		, CUSTOMFIELD_CHECK_BOX
		, CUSTOMFIELD_SOCIAL_SECURITY_NUMBER
	};

	public static final String CUSTOMFIELD_ENTITY_REGULATOR = "Regulator";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "Credentials";
	public static final String CUSTOMFIELD_ENTITY_PROVIDERS = "Providers";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTORS = "Instructors";
	public static final String CUSTOMFIELD_ENTITY_COURSE_APPROVALS = "Course Approvals";
	public static final String CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS = "Provider Approvals";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS = "Instructor Approvals";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENTS = "Credential Requirements";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY = "Credential Category";
	private static final String[] CUSTOMFIELD_ENTITIES = {
		CUSTOMFIELD_ENTITY_REGULATOR
		, CUSTOMFIELD_ENTITY_CREDENTIALS
		, CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY
		, CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENTS
		, CUSTOMFIELD_ENTITY_PROVIDERS
		, CUSTOMFIELD_ENTITY_INSTRUCTORS
		, CUSTOMFIELD_ENTITY_COURSE_APPROVALS
		, CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS
		, CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS
	};

	public AddGlobalCustomFieldWizardController() {
		super();
		setCommandName("customFieldForm");
		setCommandClass(CustomFieldForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/GlobalCustomField/addCustomField/step1"
				, "accreditation/GlobalCustomField/addCustomField/step2"
				, "accreditation/GlobalCustomField/addCustomField/step3"
				, "accreditation/GlobalCustomField/addCustomField/step4"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = new CustomField();
		form.setCustomField(customField);
		form.setEntity(CUSTOMFIELD_ENTITY_REGULATOR);
		form.setFieldType(CUSTOMFIELD_SINGLE_LINE_OF_TEXT);

		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map<Object,Object> model = new HashMap<Object,Object>();
		switch(page){
		case 0:
			model.put("customFieldEntities", CUSTOMFIELD_ENTITIES);
			return model;
		case 1:
			model.put("customFieldTypes", CUSTOMFIELD_TYPES);
			return model;
		case 2:
			break;
		case 3:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		if(this.getTargetPage(request, page) == 3) {
			if(form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)
					||form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)
					||form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
				if( !StringUtils.isBlank(form.getOption()) ) {
					this.readOptions(form);
				}
			}
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	private void readOptions(CustomFieldForm form){
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

		try {
			List<String> optionList = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						if(str.length()>250)
							str = str.substring(0,249);
						optionList.add(str);
					}
				}
			}
			form.setOptionList(optionList);
		} catch(IOException e) {
			e.printStackTrace();
		}		
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
		super.postProcessPage(request, command, errors, page);
	}

	@SuppressWarnings("static-access")
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = form.getCustomField();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner= this.getAccreditationService().findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		CustomFieldContext customFieldContext = new CustomFieldContext();
		customFieldContext.setContentOwner(contentOwner);
		
		if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_REGULATOR)){//Regulator
			customFieldContext.setGlobalRegulator(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALS)){//Credentials
			customFieldContext.setGlobalCredential(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_PROVIDERS)){//Providers
			customFieldContext.setGlobalProvider(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_INSTRUCTORS)){//Instructors
			customFieldContext.setGlobalInstructor(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_COURSE_APPROVALS)){//Course Approvals
			customFieldContext.setGlobalCourseApproval(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS)){//Provider Approvals
			customFieldContext.setGlobalProviderApproval(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS)){//Instructor Approvals
			customFieldContext.setGlobalInstructorApproval(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENTS)){//Credential Requirement
			customFieldContext.setGlobalCredentialRequirement(true);
		}else if(form.getEntity().equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY)){ // LMS-8314 :: Credential Category
			customFieldContext.setGlobalCredentialCategory(true);
		}
		
		if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = new SingleLineTextCustomFiled();
			singleLineTextCustomFiled.setGlobal(true);
			singleLineTextCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(customField.getFieldRequired());
			singleLineTextCustomFiled.setCustomFieldContext(customFieldContext);
			
			this.getAccreditationService().saveGlobalCustomField(singleLineTextCustomFiled);
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_DATE)) {
			DateTimeCustomField dateTimeCustomField =  new DateTimeCustomField();
			dateTimeCustomField.setGlobal(true);
			dateTimeCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			dateTimeCustomField.setFieldLabel(customField.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(customField.getFieldRequired());
			dateTimeCustomField.setCustomFieldContext(customFieldContext);
			
			this.getAccreditationService().saveGlobalCustomField(dateTimeCustomField);
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = new MultipleLineTextCustomfield();
			multipleLineTextCustomfield.setGlobal(true);
			multipleLineTextCustomfield.setCustomFieldDescription(customField.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(customField.getFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(customField.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(customField.getFieldRequired());
			multipleLineTextCustomfield.setCustomFieldContext(customFieldContext);
			
			this.getAccreditationService().saveGlobalCustomField(multipleLineTextCustomfield);
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
			NumericCusomField numericCusomField = new NumericCusomField();
			numericCusomField.setGlobal(true);
			numericCusomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(customField.getFieldEncrypted());
			numericCusomField.setFieldLabel(customField.getFieldLabel().trim());
			numericCusomField.setFieldRequired(customField.getFieldRequired());
			numericCusomField.setCustomFieldContext(customFieldContext);
			
			this.getAccreditationService().saveGlobalCustomField(numericCusomField);
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCustomFiled sSNCustomFiled = new SSNCustomFiled();
			sSNCustomFiled.setGlobal(true);
			sSNCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			sSNCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			sSNCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			sSNCustomFiled.setFieldRequired(customField.getFieldRequired());
			sSNCustomFiled.setCustomFieldContext(customFieldContext);
			
			this.getAccreditationService().saveGlobalCustomField(sSNCustomFiled);
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
			SingleSelectCustomField singleSelectCustomField = new SingleSelectCustomField();
			singleSelectCustomField.setGlobal(true);
			singleSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			singleSelectCustomField.setFieldRequired(customField.getFieldRequired());
			singleSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			singleSelectCustomField.setCustomFieldContext(customFieldContext);
			if (form.isAlignment())
				singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
			else
				singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
			
			singleSelectCustomField=(SingleSelectCustomField) this.getAccreditationService().saveGlobalCustomField(singleSelectCustomField);
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(singleSelectCustomField);
				this.getAccreditationService().saveOption(option);
			}
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
			multiSelectCustomField.setGlobal(true);
			multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
			multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			multiSelectCustomField.setCustomFieldContext(customFieldContext);
			multiSelectCustomField.setCheckBox(false);
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			
			multiSelectCustomField=(MultiSelectCustomField) this.getAccreditationService().saveGlobalCustomField(multiSelectCustomField);
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				this.getAccreditationService().saveOption(option);
			}
			
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
			multiSelectCustomField.setGlobal(true);
			multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
			multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			multiSelectCustomField.setCustomFieldContext(customFieldContext);
			multiSelectCustomField.setCheckBox(true);
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			
			multiSelectCustomField=(MultiSelectCustomField) this.getAccreditationService().saveGlobalCustomField(multiSelectCustomField);
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				this.getAccreditationService().saveOption(option);
			}
			
		}
		
		return new ModelAndView(closeGlobalCustomFieldTemplate);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		CustomFieldValidator validator = (CustomFieldValidator)this.getValidator();
		CustomFieldForm form = (CustomFieldForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			validator.validateAddCustomFieldPage(form, errors);
			break;
		case 3:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(closeGlobalCustomFieldTemplate);
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

}