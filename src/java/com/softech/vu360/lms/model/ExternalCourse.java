package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("External Course")
public class ExternalCourse extends Course {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String COURSE_TYPE="External Course";
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}
