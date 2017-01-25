/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author syed.mahmood
 *
 */
public class ProctorCertificateCompletionForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String proctorId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String startDate;
	private String endDate;
	private String courseTitle;
	
	private List<LearnerEnrollment> learnerEnrollmentList = new ArrayList<LearnerEnrollment>();
	/**
	 * @return the proctorId
	 */
	public String getProctorId() {
		return proctorId;
	}
	/**
	 * @param proctorId the proctorId to set
	 */
	public void setProctorId(String proctorId) {
		this.proctorId = proctorId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the learnerEnrollmentList
	 */
	public List<LearnerEnrollment> getLearnerEnrollmentList() {
		return learnerEnrollmentList;
	}
	/**
	 * @param learnerEnrollmentList the learnerEnrollmentList to set
	 */
	public void setLearnerEnrollmentList(
			List<LearnerEnrollment> learnerEnrollmentList) {
		this.learnerEnrollmentList = learnerEnrollmentList;
	}
	
	
	public String getCourseTitle() {
		return courseTitle;
	}
	
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	

}
