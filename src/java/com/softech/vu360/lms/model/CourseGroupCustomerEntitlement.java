package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("CourseGroup")
public class CourseGroupCustomerEntitlement extends CustomerEntitlement {

	private static final long serialVersionUID = 1L;
	private static final String ENROLLMENT_TYPE="CourseGroup";
	public String getEnrollmentType() {
		return ENROLLMENT_TYPE;
	}

}