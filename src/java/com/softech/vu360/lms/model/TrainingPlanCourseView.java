package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TrainingPlanCourseView  implements Serializable, Comparable<TrainingPlanCourseView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String courseId;
	private long courseGroupId;
	private long entitlementId;
	private Long totalSeats;
	private Integer seatsUsed = null;
	private long seatsRemaining;
	private String businessKey  = null;
	private String courseName = null;
	private String entitlementName = null;
	private Integer termsOfService = null;
	private String courseType=null;
	
	private  Boolean selected = false;
	private Integer unlimitedEnrollments =null;
	
	private Date expirationDate =null;
	private Date entitlementStartDate = null;
	private Date entitlementEndDate = null;

	private String enrollmentStartDate =null;
	private String enrollmentEndDate =null;
	
	private List<SynchronousClass> syncClasses=null;
	public int compareTo(TrainingPlanCourseView enrollmentCourseView) {
	    Integer result = this.getCourseName().compareToIgnoreCase(enrollmentCourseView.getCourseName());
	    return result ;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public long getCourseGroupId() { 
		return courseGroupId;
	}

	public void setCourseGroupId(long courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public long getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(long entitlementId) {
		this.entitlementId = entitlementId;
	}

	public Long getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Long totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Integer getSeatsUsed() {
		return seatsUsed;
	}

	public void setSeatsUsed(Integer seatsUsed) {
		this.seatsUsed = seatsUsed;
	}

	public long getSeatsRemaining() {
		return (this.totalSeats-seatsUsed);
	}


	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getEntitlementName() {
		return entitlementName;
	}

	public void setEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate( Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public  Boolean isSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Integer isUnlimitedEnrollments() {
		return unlimitedEnrollments;
	}

	public Integer getUnlimitedEnrollments() {
		return unlimitedEnrollments;
	}
	
	public void setUnlimitedEnrollments(Integer unlimitedEnrollments) {
		this.unlimitedEnrollments = unlimitedEnrollments;
	}

	public Integer getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(Integer termsOfService) {
		this.termsOfService = termsOfService;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

 

	public Date getEntitlementStartDate() {
		return entitlementStartDate;
	}

	public void setEntitlementStartDate(Date entitlementStartDate) {
		this.entitlementStartDate = entitlementStartDate;
	}

	public Date getEntitlementEndDate() {
		return entitlementEndDate;
	}

	public void setEntitlementEndDate(Date entitlementEndDate) {
		this.entitlementEndDate = entitlementEndDate;
	}

	public void setSeatsRemaining(long seatsRemaining) {
		this.seatsRemaining = seatsRemaining;
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

	public List<SynchronousClass> getSyncClasses() {
		return syncClasses;
	}

	public void setSyncClasses(List<SynchronousClass> syncClasses) {
		this.syncClasses = syncClasses;
	}

}