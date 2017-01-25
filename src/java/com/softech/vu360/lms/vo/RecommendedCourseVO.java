package com.softech.vu360.lms.vo;

public class RecommendedCourseVO {

	public Long id;
	public String courseName;
	public String courseGuidFrom;
	public String courseGuidTo;
	public String orderItemURL;
	public String imageUrl;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseGuidFrom() {
		return courseGuidFrom;
	}
	public void setCourseGuidFrom(String courseGuidFrom) {
		this.courseGuidFrom = courseGuidFrom;
	}
	public String getCourseGuidTo() {
		return courseGuidTo;
	}
	public void setCourseGuidTo(String courseGuidTo) {
		this.courseGuidTo = courseGuidTo;
	}
	public String getOrderItemURL() {
		return orderItemURL;
	}
	public void setOrderItemURL(String orderItemURL) {
		this.orderItemURL = orderItemURL;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	
}
