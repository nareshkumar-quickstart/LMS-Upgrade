package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CourseEntitlementItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private CustomerEntitlement entitlement = null;
	private Long entitlementId = null;
	private int maximumEnrollments = 0;
	private int enrollments = 0;
	private Date initiationDate = null;
	private Date expirationDate = null;
	private boolean unlimited = false;
	private String enrollmentStartDate = null; 
	private String enrollmentEndDate = null; 
	private boolean selected = false;
	private int termsOfServicesInDays = 0;
	private boolean expired = false;
	private boolean isOrgGroupSpecificEntitlement = false;
	
	private List<EnrollmentCourse> courses = new ArrayList <EnrollmentCourse>();

	public CourseEntitlementItem() {
		super();
	}

	public int getMaximumEnrollments() {
		return maximumEnrollments;
	}
	public void setMaximumEnrollments(int maximumEnrollments) {
		this.maximumEnrollments = maximumEnrollments;
	}
	public int getEnrollments() {
		return enrollments;
	}
	public void setEnrollments(int enrollments) {
		this.enrollments = enrollments;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Date getInitiationDate() {
		return initiationDate;
	}
	public void setInitiationDate(Date initiationDate) {
		this.initiationDate = initiationDate;
	}

	public Long getEntitlementId() {
		return entitlementId;
	}
	public void setEntitlementId(Long entitlementId) {
		this.entitlementId = entitlementId;
	}
	public String getEnrollmentStartDate() {
		return enrollmentStartDate;
	}
	public void setEnrollmentStartDate(String enrollmentStartDate) {
		this.enrollmentStartDate = enrollmentStartDate;
	}
	public String getEnrollmentEndDate() {
		return enrollmentEndDate;
	}
	public void setEnrollmentEndDate(String enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}

	/*public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseEntitlementItem other = (CourseEntitlementItem) obj;
		if (course == null) {
			if (other.course != null)
				return false;
			else
				return true;
		} else {
			if(this.course.getCourseId().equals(other.getCourse().getCourseId()))
				return true;
			else 
				return false;
		}
	}*/

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isUnlimited() {
		return unlimited;
	}

	public void setUnlimited(boolean isUnlimited) {
		this.unlimited = isUnlimited;
	}

	/**
	 * @return the entitlement
	 */
	public CustomerEntitlement getEntitlement() {
		return entitlement;
	}

	/**
	 * @param entitlement the entitlement to set
	 */
	public void setEntitlement(CustomerEntitlement entitlement) {
		this.entitlement = entitlement;
	}

	public int getTermsOfServicesInDays() {
		return termsOfServicesInDays;
	}

	public void setTermsOfServicesInDays(int termsOfServicesInDays) {
		this.termsOfServicesInDays = termsOfServicesInDays;
	}

	public List<EnrollmentCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<EnrollmentCourse> courses) {
		this.courses = courses;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return the isOrgGroupSpecificEntitlement
	 */
	public boolean isOrgGroupSpecificEntitlement() {
		return isOrgGroupSpecificEntitlement;
	}

	/**
	 * @param isOrgGroupSpecificEntitlement the isOrgGroupSpecificEntitlement to set
	 */
	public void setOrgGroupSpecificEntitlement(boolean isOrgGroupSpecificEntitlement) {
		this.isOrgGroupSpecificEntitlement = isOrgGroupSpecificEntitlement;
	}

}