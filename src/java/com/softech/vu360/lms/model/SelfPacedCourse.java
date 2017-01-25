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
@DiscriminatorValue("Self Paced Course")
public class SelfPacedCourse extends Course {
	public static final String COURSE_TYPE="Self Paced Course";

	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}

