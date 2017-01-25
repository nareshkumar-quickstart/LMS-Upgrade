package com.softech.vu360.lms.web.controller.instructor;

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
import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.InstructorCustomFieldValidator;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddCustomFieldWizardController extends AbstractWizardFormController {

	private AccreditationService accreditationService = null;
	private ResourceService resourceService = null;

	private String closeResourceTemplate = null;

	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	//public static final String CUSTOMFIELD_DATE_OF_BIRTH = "Date of Birth";
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

	public AddCustomFieldWizardController() {
		super();
		setCommandName("customFieldForm");
		setCommandClass(CustomFieldForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageResources/addCustomField/add_customfield_credentials"
				, "instructor/manageResources/addCustomField/add_customfield_details_container"
				, "instructor/manageResources/addCustomField/add_customfield_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("static-access")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = new CustomField();
		form.setCustomField(customField);
		form.setFieldType(CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
			long resourceID = Long.parseLong(request.getParameter("resourceID"));
			Resource resource = resourceService.getResourceById(resourceID);
			form.setResource(resource);
		} 
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		if(this.getTargetPage(request, page) == 2) {
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

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("static-access")
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
			return new ModelAndView(closeResourceTemplate);
		} 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("static-access")
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = form.getCustomField();

		ContentOwner contentOwner= form.getResource().getContentowner();
		CustomFieldContext customFieldContext = new CustomFieldContext();
		customFieldContext.setContentOwner(contentOwner);
		if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = new SingleLineTextCustomFiled();
			singleLineTextCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(customField.getFieldRequired());
			singleLineTextCustomFiled.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(singleLineTextCustomFiled);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_DATE)) {
			DateTimeCustomField dateTimeCustomField =  new DateTimeCustomField();
			dateTimeCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			dateTimeCustomField.setFieldLabel(customField.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(customField.getFieldRequired());
			dateTimeCustomField.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(dateTimeCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = new MultipleLineTextCustomfield();
			multipleLineTextCustomfield.setCustomFieldDescription(customField.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(customField.getFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(customField.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(customField.getFieldRequired());
			multipleLineTextCustomfield.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(multipleLineTextCustomfield);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
			NumericCusomField numericCusomField = new NumericCusomField();
			numericCusomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(customField.getFieldEncrypted());
			numericCusomField.setFieldLabel(customField.getFieldLabel().trim());
			numericCusomField.setFieldRequired(customField.getFieldRequired());
			numericCusomField.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(numericCusomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCustomFiled ssnCustomFiled = new SSNCustomFiled();
			ssnCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			ssnCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			ssnCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			ssnCustomFiled.setFieldRequired(customField.getFieldRequired());
			ssnCustomFiled.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(ssnCustomFiled);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
			SingleSelectCustomField singleSelectCustomField = new SingleSelectCustomField();
			singleSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleSelectCustomField.setFieldRequired(customField.getFieldRequired());
			singleSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			singleSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			singleSelectCustomField.setCustomFieldContext(customFieldContext);
			if (form.isAlignment())
				singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
			else
				singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(singleSelectCustomField);
				accreditationService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(singleSelectCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
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
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(multiSelectCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
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
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				form.getResource().getCustomfields().add(multiSelectCustomField);
			}
		}

		if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
			resourceService.saveResource(form.getResource());
			return new ModelAndView(closeResourceTemplate);
		} 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map<Object,Object> model = new HashMap<Object,Object>();
		switch(page){
		case 0:
			model.put("customFieldTypes", CUSTOMFIELD_TYPES);
			return model;
		case 1:
			break;
		case 2:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		InstructorCustomFieldValidator validator = (InstructorCustomFieldValidator)this.getValidator();
		CustomFieldForm form = (CustomFieldForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			break;
		case 1:
			validator.validateAddCustomFieldPage(form, errors);
			break;
		case 2:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	/**
	 * @return the closeResourceTemplate
	 */
	public String getCloseResourceTemplate() {
		return closeResourceTemplate;
	}

	/**
	 * @param closeResourceTemplate the closeResourceTemplate to set
	 */
	public void setCloseResourceTemplate(String closeResourceTemplate) {
		this.closeResourceTemplate = closeResourceTemplate;
	}

	/**
	 * @return the resourceService
	 */
	public ResourceService getResourceService() {
		return resourceService;
	}

	/**
	 * @param resourceService the resourceService to set
	 */
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
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

}