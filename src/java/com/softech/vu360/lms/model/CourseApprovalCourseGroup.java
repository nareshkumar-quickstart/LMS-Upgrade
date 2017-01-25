package com.softech.vu360.lms.model;



public class CourseApprovalCourseGroup implements SearchableKey {

	private static final long serialVersionUID = 1L;

	private Long id;
    private CourseApproval courseapproval=null;
    private CourseGroup coursegroup=null;
    
	
    @Override
	public String getKey() {
		return id.toString();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public CourseApproval getCourseapproval() {
		return courseapproval;
	}


	public void setCourseapproval(CourseApproval courseapproval) {
		this.courseapproval = courseapproval;
	}


	public CourseGroup getCoursegroup() {
		return coursegroup;
	}


	public void setCoursegroup(CourseGroup coursegroup) {
		this.coursegroup = coursegroup;
	}
    
	
}
