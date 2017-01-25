package com.softech.vu360.lms.web.controller.model;

import java.util.Date;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


/**
 * @author Noman
 *
 */

public class SessionForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	/** id used here as a classId, because when we start
	 * wizard then we need this id for checking if user
	 * try to create the duplicate sessions (startDateTime, endDateTime)
	 * */
	private Long id = 0l; 
	
	private String startDate = null;
	private String endDate = null;
	private String startTime = null;
	private String endTime = null;
	
	Date startDateTime = null;
	Date endDateTime = null;
	
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Date getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}