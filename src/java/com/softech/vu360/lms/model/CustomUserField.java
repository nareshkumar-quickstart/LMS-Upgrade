package com.softech.vu360.lms.model;

/**
 * 
 * @author muhammad.saleem
 *
 */
public abstract class CustomUserField {

	private Long id;
	private String label = null;
	private String customUserFieldType = null;
	private  Boolean required = false;
	private  Boolean encrypted = false;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the required
	 */
	public  Boolean getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the id to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the encrypted
	 */
	public  Boolean isEncrypted() {
		return encrypted;
	}

	/**
	 * @param encrypted
	 *            the encrypted to set
	 */
	public void setEncrypted(Boolean encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * @return the customUserFieldType
	 */
	public String getCustomUserFieldType() {
		return customUserFieldType;
	}

	/**
	 * @param customUserFieldType
	 *            the customUserFieldType to set
	 */
	public void setCustomUserFieldType(String customUserFieldType) {
		this.customUserFieldType = customUserFieldType;
	}
}