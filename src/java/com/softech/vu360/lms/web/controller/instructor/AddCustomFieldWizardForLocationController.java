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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldContext;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.CustomFieldLocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.AddCustomLocationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddCustomFieldWizardForLocationController extends AbstractWizardFormController{
	
	
	
	private String closeCustomFieldTemplate = null;
	private ResourceService resourceService = null;
	private AccreditationService accreditationService = null;
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

	public AddCustomFieldWizardForLocationController() {
		super();
		setCommandName("customFieldForm");
		setCommandClass(CustomFieldLocationForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"instructor/manageResources/Location/customField/addCustomField/add_customfield_credentials"
			, "instructor/manageResources/Location/customField/addCustomField/add_customfield_details_container"
			, "instructor/manageResources/Location/customField/addCustomField/add_customfield_confirm"
		});
	}

	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
		CustomField customField = new CustomField();
		form.setCustomField(customField);
		form.setFieldType(CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
		form.setEntity(request.getParameter("Location"));
		long locationID = Long.parseLong(request.getParameter("locationID"));
		form.setLocID(locationID);
		Location location = resourceService.loadForUpdateLocation(locationID);
		form.setLocation(location);
		return command;

	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
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

	
	
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		super.postProcessPage(request, command, errors, page);
	}
	private void readOptions(CustomFieldLocationForm form){
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

	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		 if (form.getEntity().equalsIgnoreCase(form.LOCATION)) {
				return new ModelAndView(closeCustomFieldTemplate);
		 }
		return null;
	}		
			
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
		CustomField customField = form.getCustomField();

		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		ContentOwner contentOwner= loggedInUser.getLearner().getCustomer().getContentOwner(); //accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		CustomFieldContext customFieldContext = new CustomFieldContext();
		customFieldContext.setContentOwner(contentOwner);		
		
		if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = new SingleLineTextCustomFiled();
			singleLineTextCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(customField.getFieldRequired());
			singleLineTextCustomFiled.setCustomFieldContext(customFieldContext);
			form.getLocation().getCustomfields().add(singleLineTextCustomFiled);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_DATE)) {
			DateTimeCustomField dateTimeCustomField =  new DateTimeCustomField();
			dateTimeCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			dateTimeCustomField.setFieldLabel(customField.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(customField.getFieldRequired());
			dateTimeCustomField.setCustomFieldContext(customFieldContext);
			form.getLocation().getCustomfields().add(dateTimeCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = new MultipleLineTextCustomfield();
			multipleLineTextCustomfield.setCustomFieldDescription(customField.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(customField.getFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(customField.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(customField.getFieldRequired());
			multipleLineTextCustomfield.setCustomFieldContext(customFieldContext);
			form.getLocation().getCustomfields().add(multipleLineTextCustomfield);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
			NumericCusomField numericCusomField = new NumericCusomField();
			numericCusomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(customField.getFieldEncrypted());
			numericCusomField.setFieldLabel(customField.getFieldLabel().trim());
			numericCusomField.setFieldRequired(customField.getFieldRequired());
			numericCusomField.setCustomFieldContext(customFieldContext);
			form.getLocation().getCustomfields().add(numericCusomField);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCustomFiled ssnCustomFiled = new SSNCustomFiled();
			ssnCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			ssnCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			ssnCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			ssnCustomFiled.setFieldRequired(customField.getFieldRequired());
			ssnCustomFiled.setCustomFieldContext(customFieldContext);
			form.getLocation().getCustomfields().add(ssnCustomFiled);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
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
			form.getLocation().getCustomfields().add(singleSelectCustomField);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
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
			form.getLocation().getCustomfields().add(multiSelectCustomField);
		}else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
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
			form.getLocation().getCustomfields().add(multiSelectCustomField);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(form.getEntity().equalsIgnoreCase(form.LOCATION)){
		resourceService.saveLocation(form.getLocation());
		return new ModelAndView("redirect:ins_mngLocations.do?method=showCustomField&id=" + form.getLocID());
		}
		
		return null;
	}
	
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
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddCustomLocationValidator validator = (AddCustomLocationValidator)this.getValidator();
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
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
	
	public ResourceService getResourceService() {
		return resourceService;
	}


	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}


	public String getCloseCustomFieldTemplate() {
		return closeCustomFieldTemplate;
	}


	public void setCloseCustomFieldTemplate(String closeCustomFieldTemplate) {
		this.closeCustomFieldTemplate = closeCustomFieldTemplate;
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}


	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
}
