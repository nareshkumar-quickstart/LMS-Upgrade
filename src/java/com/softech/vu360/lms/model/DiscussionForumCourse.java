/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("DFC")
public class DiscussionForumCourse extends Course {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String COURSE_TYPE="DFC";
	public DiscussionForumCourse(){
		super();
		setCourseType(COURSE_TYPE);
	}
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}

