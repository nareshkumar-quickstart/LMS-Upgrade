package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@DiscriminatorValue("AttendanceStatistic")
public class LearnerAttendanceSummaryStatistic extends LearnerStatistic {
	
	@Column(name = "MAXPERCENTCOURSEATTENDED")
	private double maxPercentCourseAttended = 0;

	public double getMaxPercentCourseAttended() {
		return maxPercentCourseAttended;
	}

	public void setMaxPercentCourseAttended(double maxPercentCourseAttended) {
		this.maxPercentCourseAttended = maxPercentCourseAttended;
	}
}
