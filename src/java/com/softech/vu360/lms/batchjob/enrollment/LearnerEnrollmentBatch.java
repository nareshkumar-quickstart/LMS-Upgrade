package com.softech.vu360.lms.batchjob.enrollment;

import java.util.Date;

import com.softech.vu360.lms.model.LearnerEnrollment;

/**
 * this class is used with the new learner enrollment
 * service for creating new enrollments.  As such this
 * class is not used in the normal application and should 
 * never be read as an object - it is used to optimize writes
 * as the domain model is too complex
 * 
 * @author jason
 * 
 */
public class LearnerEnrollmentBatch {

	private Long id;
	private String enrollmentStatus = LearnerEnrollment.ACTIVE;
	private Long courseId;
	private Long learnerId;
	private Date enrollmentDate = null;
	private Date enrollmentStartDate = null;
	private Date enrollmentEndDate = null;
	private Date enrollmentRequiredCompletionDate = null;
	private Long customerEntitlementId;
	private Long orgGroupEntitlementId;
	private LearnerCourseStatisticsBatch courseStatistics;
	

	private Long synchronousClassId;


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the enrollmentStatus
	 */
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}


	/**
	 * @param enrollmentStatus the enrollmentStatus to set
	 */
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}


	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}


	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}


	/**
	 * @return the learnerId
	 */
	public Long getLearnerId() {
		return learnerId;
	}


	/**
	 * @param learnerId the learnerId to set
	 */
	public void setLearnerId(Long learnerId) {
		this.learnerId = learnerId;
	}


	/**
	 * @return the enrollmentDate
	 */
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}


	/**
	 * @param enrollmentDate the enrollmentDate to set
	 */
	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}


	/**
	 * @return the enrollmentStartDate
	 */
	public Date getEnrollmentStartDate() {
		return enrollmentStartDate;
	}


	/**
	 * @param enrollmentStartDate the enrollmentStartDate to set
	 */
	public void setEnrollmentStartDate(Date enrollmentStartDate) {
		this.enrollmentStartDate = enrollmentStartDate;
	}


	/**
	 * @return the enrollmentEndDate
	 */
	public Date getEnrollmentEndDate() {
		return enrollmentEndDate;
	}


	/**
	 * @param enrollmentEndDate the enrollmentEndDate to set
	 */
	public void setEnrollmentEndDate(Date enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}


	/**
	 * @return the enrollmentRequiredCompletionDate
	 */
	public Date getEnrollmentRequiredCompletionDate() {
		return enrollmentRequiredCompletionDate;
	}


	/**
	 * @param enrollmentRequiredCompletionDate the enrollmentRequiredCompletionDate to set
	 */
	public void setEnrollmentRequiredCompletionDate(
			Date enrollmentRequiredCompletionDate) {
		this.enrollmentRequiredCompletionDate = enrollmentRequiredCompletionDate;
	}


	/**
	 * @return the customerEntitlementId
	 */
	public Long getCustomerEntitlementId() {
		return customerEntitlementId;
	}


	/**
	 * @param customerEntitlementId the customerEntitlementId to set
	 */
	public void setCustomerEntitlementId(Long customerEntitlementId) {
		this.customerEntitlementId = customerEntitlementId;
	}


	/**
	 * @return the orgGroupEntitlementId
	 */
	public Long getOrgGroupEntitlementId() {
		return orgGroupEntitlementId;
	}


	/**
	 * @param orgGroupEntitlementId the orgGroupEntitlementId to set
	 */
	public void setOrgGroupEntitlementId(Long orgGroupEntitlementId) {
		this.orgGroupEntitlementId = orgGroupEntitlementId;
	}


	/**
	 * @return the synchronousClassId
	 */
	public Long getSynchronousClassId() {
		return synchronousClassId;
	}


	/**
	 * @param synchronousClassId the synchronousClassId to set
	 */
	public void setSynchronousClassId(Long synchronousClassId) {
		this.synchronousClassId = synchronousClassId;
	}


	/**
	 * @return the courseStatistics
	 */
	public LearnerCourseStatisticsBatch getCourseStatistics() {
		return courseStatistics;
	}


	/**
	 * @param courseStatistics the courseStatistics to set
	 */
	public void setCourseStatistics(LearnerCourseStatisticsBatch courseStatistics) {
		this.courseStatistics = courseStatistics;
	}
}
