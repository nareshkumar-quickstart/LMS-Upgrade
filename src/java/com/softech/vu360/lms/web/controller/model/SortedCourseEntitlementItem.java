package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

 
 


public class SortedCourseEntitlementItem  implements ILMSBaseInterface, Comparable<SortedCourseEntitlementItem>{
	private static final long serialVersionUID = 1L;
	private String courseName;
	private String courseBusinesskey;
	private int entitlementNumber ; 
	private int entitlementCourseNumber ;
	/*private int courseId;
	private int totalSeats ;
	private int seatsUsed ;
	private int seatsRemaining  	 ;
	private String expirationDate ;
	*/
	  public int compareTo(SortedCourseEntitlementItem sortedCourseEntitlementItem) {
		    int result = this.getCourseName().compareToIgnoreCase(sortedCourseEntitlementItem.getCourseName());
		    return result ;//== 0 ? firstName.compareTo(((Person) person).firstName) : result;
		  }
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
	 * @return the entitlementNumber
	 */
	public int getEntitlementNumber() {
		return entitlementNumber;
	}
	/**
	 * @param entitlementNumber the entitlementNumber to set
	 */
	public void setEntitlementNumber(int entitlementNumber) {
		this.entitlementNumber = entitlementNumber;
	}
	/**
	 * @return the entitlementCourseNumber
	 */
	public int getEntitlementCourseNumber() {
		return entitlementCourseNumber;
	}
	/**
	 * @param entitlementCourseNumber the entitlementCourseNumber to set
	 */
	public void setEntitlementCourseNumber(int entitlementCourseNumber) {
		this.entitlementCourseNumber = entitlementCourseNumber;
	}
	public String getCourseBusinesskey() {
		return courseBusinesskey;
	}
	public void setCourseBusinesskey(String courseBusinesskey) {
		this.courseBusinesskey = courseBusinesskey;
	}
		
}