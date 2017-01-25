/**
 * 
 */
package com.softech.vu360.lms.model;

/**
 * @author Ashis
 *
 */
public class CustomReportingField implements SearchableKey {

	private static final long serialVersionUID = 1L;
	private Long id;
	private  Boolean fieldEncrypted = false;
	private String fieldLabel = null;
	private  Boolean fieldRequired = false;
	private String fieldType = null;
	private ContentOwner contentOwner = null;
	private String customFieldDescription = null;
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
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
	 * @return the fieldEncrypted
	 */
	public  Boolean isFieldEncrypted() {
		return fieldEncrypted;
	}
	/**
	 * @param fieldEncrypted the fieldEncrypted to set
	 */
	public void setFieldEncrypted(Boolean fieldEncrypted) {
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
	public  Boolean isFieldRequired() {
		return fieldRequired;
	}
	/**
	 * @param fieldRequired the fieldRequired to set
	 */
	public void setFieldRequired(Boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
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
	 * @return the contentOwner
	 */
	public ContentOwner getContentOwner() {
		return contentOwner;
	}
	/**
	 * @param contentOwner the contentOwner to set
	 */
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
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

}
