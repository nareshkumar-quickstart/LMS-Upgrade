package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class CustomField implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean fieldEncrypted = Boolean.FALSE;
	private String fieldLabel = null;
	private Boolean fieldRequired = Boolean.FALSE;
	private String fieldType = null;
	private String customFieldDescription = null;
	private String alignment = HORIZONTAL;
	private Boolean global = Boolean.FALSE;
	private Boolean active = Boolean.TRUE;

	public static final String HORIZONTAL = "horizonatl";
	public static final String VERTICAL = "vertical";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isFieldEncrypted() {
		return fieldEncrypted;
	}

	public void setFieldEncrypted(Boolean fieldEncrypted) {
		this.fieldEncrypted = fieldEncrypted;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public Boolean isFieldRequired() {
		return fieldRequired;
	}

	public void setFieldRequired(Boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getCustomFieldDescription() {
		return customFieldDescription;
	}

	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public Boolean isGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}