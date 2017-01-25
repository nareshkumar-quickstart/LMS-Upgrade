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
@Table(name = "REGULATORYAPPROVAL_AUDIT")
public class RegulatoryApprovalAudit implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "REGULATORYAPPROVAL_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORYAPPROVAL_AUDIT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORYAPPROVAL_AUDIT_ID")
	private Long id;
	
    @Column(name = "CONTENTOWNER_ID") 
	private Long containOwnerId;
	
    @Column(name = "APPROVALTYPE") 
	private String approvalType;
	
    @Column(name = "deleted") 
	private Boolean delete;
	
    @Column(name = "REGULATORCATEGORY_ID") 
	private Long regulatoryCategoryId;
	
    @Column(name = "REGULATORYAPPROVAL_ID") 
	private Long regulatorApprovaId = null;
	
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
	public Long getContainOwnerId() {
		return containOwnerId;
	}
	public void setContainOwnerId(Long containOwnerId) {
		this.containOwnerId = containOwnerId;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	public Boolean isDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Long getRegulatoryCategoryId() {
		return regulatoryCategoryId;
	}
	public void setRegulatoryCategoryId(Long regulatoryCategoryId) {
		this.regulatoryCategoryId = regulatoryCategoryId;
	}
	public Long getRegulatorApprovaId() {
		return regulatorApprovaId;
	}
	public void setRegulatorApprovaId(Long regulatorApprovaId) {
		this.regulatorApprovaId = regulatorApprovaId;
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
