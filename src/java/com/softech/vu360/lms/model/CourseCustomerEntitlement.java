package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Course")
public class CourseCustomerEntitlement extends CustomerEntitlement {

	private static final long serialVersionUID = 1L;
	private static final String ENROLLMENT_TYPE="Course";
	public String getEnrollmentType() {
		return ENROLLMENT_TYPE;
	}
}