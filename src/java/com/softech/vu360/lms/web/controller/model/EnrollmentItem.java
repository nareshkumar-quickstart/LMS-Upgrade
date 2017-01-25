/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author tapas
 *
 */
public class EnrollmentItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String courseName=null;
	private String enrollmentEndDate=null;
	
	
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the enrollmentEndDate
	 */
	public String getEnrollmentEndDate() {
		return enrollmentEndDate;
	}
	/**
	 * @param enrollmentEndDate the enrollmentEndDate to set
	 */
	public void setEnrollmentEndDate(String enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}
}
