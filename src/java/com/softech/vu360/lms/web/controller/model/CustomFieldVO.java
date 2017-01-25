package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


/**
 * 
 * @author khurram.shehzad
 *
 */
public class CustomFieldVO  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String RESELLER = "Reseller";
	public static final String CUSTOMER = "Customer";

	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	private Long id;
	private String fieldLabel = null;
	private String fieldType = null;
	private String entity = null;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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

	
}
