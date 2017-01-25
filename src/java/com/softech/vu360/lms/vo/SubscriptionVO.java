package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class SubscriptionVO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String subscriptionId;
	private String subscriptionName;
	private String subscriptionCourseName;
	private String subscriptionCourseId;
	private String enrollmentId;
	private String courseType;
	
	
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public String getSubscriptionCourseName() {
		return subscriptionCourseName;
	}
	public void setSubscriptionCourseName(String subscriptionCourseName) {
		this.subscriptionCourseName = subscriptionCourseName;
	}
	public String getSubscriptionCourseId() {
		return subscriptionCourseId;
	}
	public void setSubscriptionCourseId(String subscriptionCourseId) {
		this.subscriptionCourseId = subscriptionCourseId;
	}
	public String getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	

}
