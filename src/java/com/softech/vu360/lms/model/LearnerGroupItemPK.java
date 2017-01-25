package com.softech.vu360.lms.model;

import java.io.Serializable;

public class LearnerGroupItemPK implements Serializable{

	private static final long serialVersionUID = -945676923410928791L;
	private LearnerGroup learnerGroup = null;
	private Course course = null;

	
	public LearnerGroupItemPK() {
		
	}

	public LearnerGroupItemPK(LearnerGroup learnerGroup, Course course) {
		this.learnerGroup = learnerGroup;
		this.course = course;
	}

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result
				+ ((learnerGroup == null) ? 0 : learnerGroup.hashCode());
		result = prime
				* result
				+ ((learnerGroup == null) ? 0 : learnerGroup
						.hashCode());
		return result;
	}
	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LearnerGroupItemPK other = (LearnerGroupItemPK) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (learnerGroup == null) {
			if (other.learnerGroup != null)
				return false;
		} else if (!learnerGroup.equals(other.learnerGroup))
			return false;
		
		return true;
	}
	
}
