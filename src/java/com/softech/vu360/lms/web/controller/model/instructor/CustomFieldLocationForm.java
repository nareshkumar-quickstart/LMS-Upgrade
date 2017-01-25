package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CustomFieldLocationForm implements ILMSBaseInterface {
	
	
	public static final String CREDENTIAL = "Credentials";
	public static final String CREDENTIAL_REQUIREMENT = "CredentialRequirement";
	public static final String LOCATION = "Location";
	private static final long serialVersionUID = 1L;
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	
	private CustomField customField; 
	private Location location;
	private String fieldType;
	private String entity;
	private String option;
	private List<String> optionList = new ArrayList<String>();
	private boolean alignment = true;
	
	//FOR EDIT CUSTOMFIELD
	private long LocID = 0;  
	private long customFieldId = 0;  
	private boolean fieldEncrypted = false;
	private String fieldLabel = null;
	private boolean fieldRequired = false;
	private String customFieldDescription = null;
	private List<CustomFieldValueChoice> options = new ArrayList<CustomFieldValueChoice>();
	
	public CustomField getCustomField() {
		return customField;
	}
	/**
	 * @param customField the customField to set
	 */
	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the alignment
	 */
	public boolean isAlignment() {
		return alignment;
	}
	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(boolean alignment) {
		this.alignment = alignment;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return the optionList
	 */
	public List<String> getOptionList() {
		return optionList;
	}
	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}
	/**
	 * @return the credential
	 */
	
	/**
	 * @return the customFieldId
	 */
	public long getCustomFieldId() {
		return customFieldId;
	}
	/**
	 * @param customFieldId the customFieldId to set
	 */
	public void setCustomFieldId(long customFieldId) {
		this.customFieldId = customFieldId;
	}
	/**
	 * @return the fieldEncrypted
	 */
	public boolean isFieldEncrypted() {
		return fieldEncrypted;
	}
	/**
	 * @param fieldEncrypted the fieldEncrypted to set
	 */
	public void setFieldEncrypted(boolean fieldEncrypted) {
		this.fieldEncrypted = fieldEncrypted;
	}
	/**
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
	/**
	 * @param fieldLabel the fieldLabel to set
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	/**
	 * @return the fieldRequired
	 */
	public boolean isFieldRequired() {
		return fieldRequired;
	}
	/**
	 * @param fieldRequired the fieldRequired to set
	 */
	public void setFieldRequired(boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
	}
	/**
	 * @return the customFieldDescription
	 */
	public String getCustomFieldDescription() {
		return customFieldDescription;
	}
	/**
	 * @param customFieldDescription the customFieldDescription to set
	 */
	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}
	/**
	 * @return the options
	 */
	public List<CustomFieldValueChoice> getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(List<CustomFieldValueChoice> options) {
		this.options = options;
	}
	/**
	 * @return the regulator
	 */
	public long getLocID() {
		return LocID;
	}
	public void setLocID(long locID) {
		LocID = locID;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
