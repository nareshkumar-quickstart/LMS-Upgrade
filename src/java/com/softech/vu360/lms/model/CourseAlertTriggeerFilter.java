package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@DiscriminatorValue("CourseAlertTriggeerFilter")
public class CourseAlertTriggeerFilter extends AlertTriggerFilter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
    @JoinTable(name="ALERTTRIGGERFILTER_COURSE", joinColumns = @JoinColumn(name="ALERTTRIGGERFILTER_ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID"))
    private List<Course> course = new ArrayList<Course>();

	public List<Course> getCourses() {
		return course;
	}

	public void setCourses(List<Course> course) {
		this.course = course;
	}

	
}
