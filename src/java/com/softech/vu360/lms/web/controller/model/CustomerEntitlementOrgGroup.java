package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class CustomerEntitlementOrgGroup  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private OrganizationalGroup orgGroup;
	private String maxEnrollments;
	
	
	public CustomerEntitlementOrgGroup() {
		super();
	}
	
	/**
	 * @return the orgGroup
	 */
	public OrganizationalGroup getOrgGroup() {
		return orgGroup;
	}
	/**
	 * @param orgGroup the orgGroup to set
	 */
	public void setOrgGroup(OrganizationalGroup orgGroup) {
		this.orgGroup = orgGroup;
	}
	/**
	 * @return the maxEnrollments
	 */
	public String getMaxEnrollments() {
		return maxEnrollments;
	}
	/**
	 * @param maxEnrollments the maxEnrollments to set
	 */
	public void setMaxEnrollments(String maxEnrollments) {
		this.maxEnrollments = maxEnrollments;
	}

}
