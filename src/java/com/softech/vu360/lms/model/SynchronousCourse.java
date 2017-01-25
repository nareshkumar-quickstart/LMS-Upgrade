package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Classroom Course")
public class SynchronousCourse extends Course {

    private static final long serialVersionUID = 1L;
	public static final String COURSE_TYPE="Classroom Course";

	public SynchronousCourse() {
		super();
        setCourseType(COURSE_TYPE);
	}
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
		

}
