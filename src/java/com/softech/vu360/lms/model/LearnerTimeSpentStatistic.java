package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@DiscriminatorValue("TimeSpentStatistic")
public class LearnerTimeSpentStatistic extends LearnerStatistic {

	//@Column(name = "TIMEATTENDEDINSECONDS")
	@Transient
	private Integer timeAttendedInSeconds = 0;

	public Integer getTimeAttendedInSeconds() {
		return timeAttendedInSeconds;
	}

	public void setTimeAttendedInSeconds(Integer timeAttendedInSeconds) {
		this.timeAttendedInSeconds = timeAttendedInSeconds;
	}
}
