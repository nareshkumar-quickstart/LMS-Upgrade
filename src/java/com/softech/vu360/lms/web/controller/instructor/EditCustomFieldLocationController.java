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
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
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
import com.softech.vu360.lms.web.controller.model.instructor.CustomFieldLocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.AddCustomLocationValidator;

public class EditCustomFieldLocationController extends VU360BaseMultiActionController{
	
	private AccreditationService accreditationService = null;

	private String defaultCustomFieldTemplate;
	private	String closeGlobalCustomFieldTemplate=null;
	
		
	public EditCustomFieldLocationController() {
		super();
	}

	public EditCustomFieldLocationController(Object delegate) {
		super(delegate);
	}
	
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if(command instanceof CustomFieldLocationForm){
			CustomFieldLocationForm form = (CustomFieldLocationForm)command;
			if(methodName.equals("displayCustomFieldForEdit")){
				CustomField customField = accreditationService.loadForUpdateCustomField(form.getCustomFieldId());
				form.setCustomField(customField);
				form.setEntity("Location");
				String locId=request.getParameter("locId");
				form.setLocID(Long.parseLong(locId));
				/*set entity type for  custom fields that are not global*/
				if(form.getEntity().length() == 0 && request.getParameter("entity")!= null){
					form.setEntity(request.getParameter("entity"));	
				}

			}
		}

	}
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		if(methodName.equals("saveCustomField")){
			AddCustomLocationValidator validator = (AddCustomLocationValidator)this.getValidator();
			CustomFieldLocationForm form = (CustomFieldLocationForm)command;
			form.getCustomField().setFieldLabel(form.getFieldLabel());
			if (form.getCustomField() instanceof SingleSelectCustomField || form.getCustomField() instanceof MultiSelectCustomField) {
				this.readOptions(form);
			}
			validator.validateAddCustomFieldPage(form, errors);
		}
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
	
	public ModelAndView displayCustomFieldForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
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
	
	public ModelAndView saveCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if(errors.hasErrors()){
			return new ModelAndView(defaultCustomFieldTemplate);
		}
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
		CustomField customField = form.getCustomField();
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
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "displayGlobalCustomFields");
		return new ModelAndView("redirect:ins_mngLocations.do?method=showCustomField&id=" + form.getLocID());
	}
	
	public ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldLocationForm form = (CustomFieldLocationForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		 if (form.getEntity().equalsIgnoreCase(form.LOCATION)) {
			 return new ModelAndView("redirect:ins_mngLocations.do?method=showCustomField&id=" + form.getLocID());
		 }
		return null;
	}		
	

	
	private String getOption(List<CustomFieldValueChoice> optionList) {
		String optionString="";
		//Collections.sort(optionList);
		for (CustomFieldValueChoice option : optionList) {
			optionString = optionString + option.getLabel() + "\n";
		}
		return optionString;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getCloseGlobalCustomFieldTemplate() {
		return closeGlobalCustomFieldTemplate;
	}

	public void setCloseGlobalCustomFieldTemplate(
			String closeGlobalCustomFieldTemplate) {
		this.closeGlobalCustomFieldTemplate = closeGlobalCustomFieldTemplate;
	}

	public String getDefaultCustomFieldTemplate() {
		return defaultCustomFieldTemplate;
	}

	public void setDefaultCustomFieldTemplate(String defaultCustomFieldTemplate) {
		this.defaultCustomFieldTemplate = defaultCustomFieldTemplate;
	}
}
