package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ORGANIZATIONALGROUPENTITLEMENT")
public class OrgGroupEntitlement implements SearchableKey {

	private static final long serialVersionUID = -563587260272312637L;

	@Id
    @javax.persistence.TableGenerator(name = "ORGANIZATIONALGROUPENTITLEMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORGANIZATIONALGROUPENTITLEMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORGANIZATIONALGROUPENTITLEMENT_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="ORGANIZATIONALGROUP_ID")
	private OrganizationalGroup organizationalGroup ;
	
	@OneToOne (cascade = { CascadeType.MERGE})
    @JoinColumn(name="CUSTOMERENTITLEMENT_ID")
	private CustomerEntitlement customerEntitlement ;
	
	@Column(name = "SEATS")
	private Integer maxNumberSeats = 0;
	
	@Transient
	private Integer defaultTermOfServiceInDays = 0;
	
	@Transient
	private Date endDate;
	
	@Transient
	private Date startDate;
	
	@Column(name = "ALLOWUNLIMITEDENROLLMENTSTF")
	private Boolean allowUnlimitedEnrollments = Boolean.FALSE;
	
	@Column(name = "ALLOWSELFENROLLMENTTF")
	private Boolean allowSelfEnrollment = Boolean.FALSE;
	
	@Column(name = "NUMBERSEATSUSED")
	private Integer numberSeatsUsed = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return id.toString();
	}

	public OrganizationalGroup getOrganizationalGroup() {
		return this.organizationalGroup;
	}

	public void setOrganizationalGroup(OrganizationalGroup organizationalGroup) {
		this.organizationalGroup = organizationalGroup;
	}

	public CustomerEntitlement getCustomerEntitlement() {
		return this.customerEntitlement;
	}

	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}

	public Integer getMaxNumberSeats() {
		return maxNumberSeats;
	}

	public void setMaxNumberSeats(Integer maxNumberSeats) {
		this.maxNumberSeats = maxNumberSeats;
	}

	public Integer getDefaultTermOfServiceInDays() {
		return defaultTermOfServiceInDays;
	}

	public void setDefaultTermOfServiceInDays(Integer defaultTermOfServiceInDays) {
		this.defaultTermOfServiceInDays = defaultTermOfServiceInDays;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public  Boolean isAllowUnlimitedEnrollments() {
		return allowUnlimitedEnrollments;
	}

	public void setAllowUnlimitedEnrollments(Boolean allowUnlimitedEnrollments) {
		this.allowUnlimitedEnrollments = allowUnlimitedEnrollments;
	}

	public  Boolean isAllowSelfEnrollment() {
		return allowSelfEnrollment;
	}

	public void setAllowSelfEnrollment(Boolean allowSelfEnrollment) {
		this.allowSelfEnrollment = allowSelfEnrollment;
	}

	public Integer getNumberSeatsUsed() {
		return numberSeatsUsed;
	}

	public void setNumberSeatsUsed(Integer numberSeatsUsed) {
		this.numberSeatsUsed = numberSeatsUsed;
	}

	public  Boolean hasAvailableSeats(Integer numSeatsRequesting) {
		if(getCustomerEntitlement().hasAvailableSeats(numSeatsRequesting)){
			if ( allowUnlimitedEnrollments ){
				return true;
			}else if((this.maxNumberSeats - this.numberSeatsUsed) >= numSeatsRequesting){
				return true;
			}
			return false;
		}
		return false;
	}
}
