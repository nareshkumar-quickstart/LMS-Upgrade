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
@Table(name = "REGULATORCATEGORY_CREDITREPORTINGFIELD_AUDIT")
public class RegulatorCategoryCreditReportingFieldAudit implements Serializable{

	private static final long serialVersionUID = 1L;
	
    @Id
	@javax.persistence.TableGenerator(name = "REGULATORCATEGORY_CREDITREPORTINGFIELD_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORCATEGORY_CREDITREPORTINGFIELD_AUDIT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORCATEGORY_CREDITREPORTINGFIELD_AUDIT_ID")
	private Long id = null;
    
    @Column(name = "REGULATORCATEGORY_ID")
	private Long regulatorCategoryId = null;
	
    @Column(name = "CREDITREPORTINGFIELD_ID")
	private Long CreditReportingFieldId = null;
	
    @Column(name = "OPERATION")
	private String operation = null;
	
    @Column(name = "CREATEDON")
	private Date createdOn = null;
	
    @Column(name = "CREATEDBY")
	private Long createdBy = null;
	
    @Column(name = "UPDATEDON")
	private Date updatedOn = null;
	
    @Column(name = "UPDATEDBY")
	private Long updatedBy = null;
	
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegulatorCategoryId() {
		return regulatorCategoryId;
	}
	public void setRegulatorCategoryId(Long regulatorCategoryId) {
		this.regulatorCategoryId = regulatorCategoryId;
	}
	public Long getCreditReportingFieldId() {
		return CreditReportingFieldId;
	}
	public void setCreditReportingFieldId(Long creditReportingFieldId) {
		CreditReportingFieldId = creditReportingFieldId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
