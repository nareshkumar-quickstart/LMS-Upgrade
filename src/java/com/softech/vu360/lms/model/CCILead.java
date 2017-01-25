package com.softech.vu360.lms.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "CCILead")
public class CCILead {

	@Id
	@AttributeOverrides({
	    @AttributeOverride(name = "customerOrder", column = @Column(name="OrderID")),
	    @AttributeOverride(name = "cciLeadId",  column = @Column(name="cciLeadId"))
	    })
	
    @Column(name = "CCILeadID")
	private String cciLeadId;
	
    @OneToOne
    @JoinColumn(name="OrderID")
	private CustomerOrder customerOrder ;
	
	/**
	 * @return the cciLeadId
	 */
	public String getCciLeadId() {
		return cciLeadId;
	}
	/**
	 * @param cciLeadId the cciLeadId to set
	 */
	public void setCciLeadId(String cciLeadId) {
		this.cciLeadId = cciLeadId;
	}
	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}
	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}
	
}
