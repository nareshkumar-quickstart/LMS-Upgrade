/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author tapas
 *
 */
public class CourseGroupItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String courseGroupName=null;
	private List<EnrollmentItem> enrollmentItems=new ArrayList<EnrollmentItem>();
	
	
	/**
	 * @return the courseGroupName
	 */
	public String getCourseGroupName() {
		return courseGroupName;
	}
	/**
	 * @param courseGroupName the courseGroupName to set
	 */
	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}
	/**
	 * @return the enrollmentItem
	 */
	public List<EnrollmentItem> getEnrollmentItems() {
		return enrollmentItems;
	}
	/**
	 * @param enrollmentItem the enrollmentItem to set
	 */
	public void setEnrollmentItem(List<EnrollmentItem> enrollmentItems) {
		this.enrollmentItems = enrollmentItems;
	}
	public void addEnrollmentItem(EnrollmentItem enrollmentItem) {
		enrollmentItems.add(enrollmentItem);
	}
}
