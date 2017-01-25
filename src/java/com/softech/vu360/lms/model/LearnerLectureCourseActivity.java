package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNERLECTURECOURSEACTIVITY")
@DiscriminatorValue("LECTURE")
public class LearnerLectureCourseActivity extends LearnerCourseActivity {
	
	
	@Column(name = "ATTENDEDTF")
	private Boolean attended = false;
	
	/**
	 * @return the attended
	 */
	public  Boolean isAttended() {
		return attended;
	}
	
	/**
	 * @param attended the attended to set
	 */
	public void setAttended(Boolean attended) {
		this.attended = attended;
	}
}