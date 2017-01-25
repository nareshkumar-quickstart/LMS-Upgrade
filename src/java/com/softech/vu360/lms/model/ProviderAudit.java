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
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROVIDER_AUDIT")
public class ProviderAudit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "PROVIDER_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "PROVIDER_AUDIT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PROVIDER_AUDIT_ID")
	private Long id;
	
	@Column(name = "CONTENTOWNER_ID")
	private Long contentownerId;
	
	@Column(name = "ADDRESS_ID")
	private Long addressId;
	
	@Column(name = "REGULATOR_ID")
	private Long regulatorId;
	
	@Column(name = "PROVIDERNAME")
	private String name = null;
	
	@Column(name = "CONTACTNAME")
	private String contactName = null;
	
	@Column(name = "PHONE")
	private String phone = null;
	
	@Column(name = "FAX")
	private String fax = null;
	
	@Column(name = "WEBSITE")
	private String website = null;
	
	@Column(name = "EMAILADDRESS")
	private String emailAddress = null;
	
	@Column(name = "ACTIVE")
	private Boolean active = true;
	
	@Column(name = "PROVIDER_ID")
	private Long providerId = null;
	
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
	public Long getContentownerId() {
		return contentownerId;
	}
	public void setContentownerId(Long contentownerId) {
		this.contentownerId = contentownerId;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getRegulatorId() {
		return regulatorId;
	}
	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public  Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long getProviderId() {
		return providerId;
	}
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
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
