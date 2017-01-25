package com.softech.vu360.lms.model;

import java.io.Serializable;

public class CourseGroupCustomerEntitlementItemPK implements Serializable{

	private static final long serialVersionUID = -5896374225434897887L;
	
	private CourseGroup courseGroup ;
	private CourseGroupCustomerEntitlement customerEntitlement ;
	
	public CourseGroupCustomerEntitlementItemPK() {
		
	}

	public CourseGroupCustomerEntitlementItemPK(CourseGroup courseGroup, CourseGroupCustomerEntitlement customerEntitlement) {
		this.courseGroup = courseGroup;
		this.customerEntitlement = customerEntitlement;
	}

	public CourseGroup getCourseGroup() {
		return courseGroup;
	}

	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}

	public CourseGroupCustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}

	public void setCustomerEntitlement(CourseGroupCustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseGroup == null) ? 0 : courseGroup.hashCode());
		result = prime * result
				+ ((customerEntitlement == null) ? 0 : customerEntitlement.hashCode());
		result = prime
				* result
				+ ((customerEntitlement == null) ? 0 : customerEntitlement
						.hashCode());
		return result;
	}
	
	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseGroupCustomerEntitlementItemPK other = (CourseGroupCustomerEntitlementItemPK) obj;
		if (courseGroup == null) {
			if (other.courseGroup != null)
				return false;
		} else if (!courseGroup.equals(other.courseGroup))
			return false;
		if (customerEntitlement == null) {
			if (other.customerEntitlement != null)
				return false;
		} else if (!customerEntitlement.equals(other.customerEntitlement))
			return false;
		
		return true;
	}

}
