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
@DiscriminatorValue("Legacy Course")
public class LegacyCourse extends Course {
	public static final String COURSE_TYPE="Legacy Course";

	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}

