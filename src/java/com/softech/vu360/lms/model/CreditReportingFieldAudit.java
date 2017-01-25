package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "CREDITREPORTINGFIELD_AUDIT")
public class CreditReportingFieldAudit implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDITREPORTINGFIELD_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDITREPORTINGFIELD_AUDIT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDITREPORTINGFIELD_AUDIT_ID")
	private Long id;

	@Column(name = "CREDITREPORTINGFIELD_ID")
	private Long creditReportingFieldId;
	
	@Column(name = "FIELDENCRYPTEDTF")
	private Boolean fieldEncrypted = false;
	
	@Column(name = "FIELDLABEL")
	private String fieldLabel = null;
	
	@Column(name = "FIELDREQUIREDTF")
	private Boolean fieldRequired = false;
	
	@Column(name = "FIELDTYPE")
	private String fieldType = null;	
	
	@Column(name = "DESCRIPTION")
	private String customFieldDescription = null;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "UPDATED_BY")
	private Long updatedBy;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "OPERATION")
	private String operation;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCreditReportingFieldId() {
		return creditReportingFieldId;
	}
	public void setCreditReportingFieldId(Long creditReportingFieldId) {
		this.creditReportingFieldId = creditReportingFieldId;
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
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
