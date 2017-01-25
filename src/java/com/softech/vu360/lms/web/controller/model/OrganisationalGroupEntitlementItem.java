/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author tathya
 *
 */
public class OrganisationalGroupEntitlementItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long organisationalGroupId=null;
	private String maxEnrollments=null;
	private String organisationalGroupName=null;
	private boolean selected = false;
	
	public OrganisationalGroupEntitlementItem() {
		super();
	}
	
	
	public OrganisationalGroupEntitlementItem(Long organisationalGroupId,
			String organisationalGroupName) {
		super();
		this.organisationalGroupId = organisationalGroupId;
		this.organisationalGroupName = organisationalGroupName;
	}

	public Long getOrganisationalGroupId() {
		return organisationalGroupId;
	}
	
	public void setOrganisationalGroupId(Long organisationalGroupId) {
		this.organisationalGroupId = organisationalGroupId;
	}
	
	public String getMaxEnrollments() {
		return maxEnrollments;
	}
	
	public void setMaxEnrollments(String maxEnrollments) {
		this.maxEnrollments = maxEnrollments;
	}

	public String getOrganisationalGroupName() {
		return organisationalGroupName;
	}

	public void setOrganisationalGroupName(String organisationalGroupName) {
		this.organisationalGroupName = organisationalGroupName;
	}


	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
