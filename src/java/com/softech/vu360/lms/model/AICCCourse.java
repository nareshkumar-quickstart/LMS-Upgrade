package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("AICC Course")
public class AICCCourse extends Course {

    private static final long serialVersionUID = 1L;
	public static final String COURSE_TYPE = "AICC Course";

    public AICCCourse() {
        super();
        setCourseType(COURSE_TYPE);
    }

	/**
	 * @return the courseType
	 */
	public String getCourseType() {
		return COURSE_TYPE;
	}
	
	

}
