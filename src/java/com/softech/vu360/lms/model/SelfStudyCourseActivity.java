/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("SELFSTUDY")
public class SelfStudyCourseActivity extends CourseActivity implements
		SearchableKey {

	@Transient
	private Course course ;
	
	/**
	 * 
	 */
	public SelfStudyCourseActivity() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	

}
