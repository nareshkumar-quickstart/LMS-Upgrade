package com.softech.vu360.lms.web.controller.model;

import java.util.Calendar;
import java.util.Date;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * 
 * @author noman ali
 * 
 *
 */

public class MySessionDateTime  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Calendar startDateTime;
	private Calendar endDateTime;
	
	public Calendar getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Calendar getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Calendar endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public Date getStartDate() {
		return startDateTime.getTime();
	}
	
	public Date getEndDate() {
		return endDateTime.getTime();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}