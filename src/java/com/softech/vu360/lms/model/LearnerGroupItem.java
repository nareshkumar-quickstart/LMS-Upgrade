package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author raja.ali
 * Date: 2015/11/18
 *
 */

@Entity
@Table(name = "LEARNERGROUP_COURSE")
@IdClass(LearnerGroupItemPK.class)
public class LearnerGroupItem implements Serializable{
	
	private static final long serialVersionUID = -9199896390035560300L;
	public LearnerGroupItem() {
	}
	
	@Id
	@OneToOne
	@JoinColumn(name="LEARNERGROUP_ID" , referencedColumnName = "id")
	private LearnerGroup learnerGroup = null;
	
	@Id
	@OneToOne
	@JoinColumn(name="COURSE_ID" , referencedColumnName = "id")
	private Course course = null;
	
	@OneToOne
	@JoinColumn(name="COURSEGROUP_ID", referencedColumnName = "id")
	private CourseGroup courseGroup = null;
	

	
	public LearnerGroup getLearnerGroup() {
		return learnerGroup;
	}

	public void setLearnerGroup(LearnerGroup learnerGroup) {
		this.learnerGroup = learnerGroup;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public CourseGroup getCourseGroup() {
		return courseGroup;
	}

	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}

	@Override
	public String toString() {
		return "LearnerGroupItem";
		
	}
}
