package com.softech.vu360.lms.web.controller.model.customfield;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

public class CustomFieldBuilder  implements ILMSBaseInterface{
	private static final String SINGLE_LINE_TEXT_CUSTOM_FILED_TEMPLATE="vm/accreditation/CustomFieldTemplate/singleLineTextCustomFiled.vm";
	private static final String SINGLE_LINE_NUMERIC_CUSTOM_FILED_TEMPLATE="vm/accreditation/CustomFieldTemplate/singleLineNumericCustomFiled.vm";
	private static final String SINGLE_LINE_SSN_CUSTOM_FILED_TEMPLATE="vm/accreditation/CustomFieldTemplate/singleLineSSNCustomFiled.vm";
	private static final String MULTIPLE_LINE_TEXT_CUSTOM_FIELD_TEMPLATE="vm/accreditation/CustomFieldTemplate/multipleLineTextCustomfield.vm";
	private static final String DATE_TIME_CUSTOM_FIELD_TEMPLATE="vm/accreditation/CustomFieldTemplate/dateTimeCustomField.vm";
	private static final String SINGLE_SELECT_CUSTOM_FIELD_TEMPLATE="vm/accreditation/CustomFieldTemplate/singleSelectCustomField.vm";
	private static final String MULTI_SELECT_CUSTOM_FIELD_TEMPLATE="vm/accreditation/CustomFieldTemplate/multiSelectCustomField.vm";
	private static final String MULTI_SELECT_COMBO_CUSTOM_FIELD_TEMPLATE="vm/accreditation/CustomFieldTemplate/multiSelectComboCustomField.vm";
	private static final long serialVersionUID = 1L;
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();

	public void buildCustomField(com.softech.vu360.lms.model.CustomField customField,int fieldStatus,List<CustomFieldValue> customFieldValues){
		this.buildCustomField(customField,fieldStatus, customFieldValues, null);
	}

	public void buildCustomField(com.softech.vu360.lms.model.CustomField customField,int fieldStatus,List<CustomFieldValue> customFieldValues,List<CustomFieldValueChoice> customFieldValueChoices){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserMode currentMode = null;
		
		// LMS-6353 :: Set mode to LEARNER by default if no Authentication details exists specially for Self Registrations
		if ( auth.getDetails() instanceof VU360UserAuthenticationDetails ) {
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
			currentMode = details.getCurrentMode();
		}
		
		if ( currentMode == null ) {
			currentMode = VU360UserMode.ROLE_LEARNER;
		}
		
		com.softech.vu360.lms.web.controller.model.customfield.CustomField field = null;

		CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);

		if(customField instanceof SingleLineTextCustomFiled){

			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			field.setTemplatePath(SINGLE_LINE_TEXT_CUSTOM_FILED_TEMPLATE);

		}else if(customField instanceof MultipleLineTextCustomfield){

			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			field.setTemplatePath(MULTIPLE_LINE_TEXT_CUSTOM_FIELD_TEMPLATE);

		}else if(customField instanceof NumericCusomField){

			NumericCusomField numericCusomField = (NumericCusomField)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			field.setTemplatePath(SINGLE_LINE_NUMERIC_CUSTOM_FILED_TEMPLATE);

		}else if(customField instanceof DateTimeCustomField){

			DateTimeCustomField dateTimeCustomField = (DateTimeCustomField)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			field.setTemplatePath(DATE_TIME_CUSTOM_FIELD_TEMPLATE);

		}else if (customField instanceof SingleSelectCustomField){
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			field.setTemplatePath(SINGLE_SELECT_CUSTOM_FIELD_TEMPLATE);
			if (customFieldValueChoices!=null){
				for (CustomFieldValueChoice customFieldValueChoice:customFieldValueChoices){
					com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoiceRef = new com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice(customFieldValueChoice);
					field.addCustomFieldValueChoice(customFieldValueChoiceRef);
				}
			}
		}else if (customField instanceof MultiSelectCustomField){
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			if (multiSelectCustomField.getCheckBox()){
				field.setTemplatePath(MULTI_SELECT_CUSTOM_FIELD_TEMPLATE);
			}else {
				field.setTemplatePath(MULTI_SELECT_COMBO_CUSTOM_FIELD_TEMPLATE);
			}
			if (customFieldValueChoices!=null){
				for (CustomFieldValueChoice customFieldValueChoice:customFieldValueChoices){
					com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoiceRef = new com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice(customFieldValueChoice);
					field.addCustomFieldValueChoice(customFieldValueChoiceRef);
				}
			}
		}else if (customField instanceof SSNCustomFiled){
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)customField;
			field = new com.softech.vu360.lms.web.controller.model.customfield.CustomField(customField,customFieldValue);
			field.setAdminCurrentMode( currentMode );
			field.setStatus(fieldStatus);
			field.setCustomFieldRef(customField);
			//field.getCustomFieldValueRef().setCustomField(customField);
			field.setTemplatePath(SINGLE_LINE_SSN_CUSTOM_FILED_TEMPLATE);
		}

		if(field!=null){
			customFieldList.add(field);
		}
	}

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

	/**
	 * @return the customFieldList
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldList() {
		return customFieldList;
	}

	/**
	 * @param customFieldList the customFieldList to set
	 */
	public void setCustomFieldList(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList) {
		this.customFieldList = customFieldList;
	}

}
