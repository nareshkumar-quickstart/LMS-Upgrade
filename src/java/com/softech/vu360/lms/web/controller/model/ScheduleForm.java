package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ScheduleForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private long sid=0;	
	private String className=null;
	private String courseName=null;
	private String maximumClassSize=null;
	private String minimumClassSize=null;
	private java.util.Date enrollmentCloseDate=null;
	private boolean status = false;
	private boolean selected=false;
	private String onlineMeeting=null;
	private String meetingID = null;
	private String meetingPassCode = null;
	private String meetingURL = null;
	private String location=null;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getMaximumClassSize() {
		return maximumClassSize;
	}
	public void setMaximumClassSize(String maximumClassSize) {
		this.maximumClassSize = maximumClassSize;
	}
	public String getMinimumClassSize() {
		return minimumClassSize;
	}
	public void setMinimumClassSize(String minimumClassSize) {
		this.minimumClassSize = minimumClassSize;
	}
	public java.util.Date getEnrollmentCloseDate() {
		return enrollmentCloseDate;
	}
	public void setEnrollmentCloseDate(java.util.Date enrollmentCloseDate) {
		this.enrollmentCloseDate = enrollmentCloseDate;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getOnlineMeeting() {
		return onlineMeeting;
	}
	public void setOnlineMeeting(String onlineMeeting) {
		this.onlineMeeting = onlineMeeting;
	}
	public String getMeetingID() {
		return meetingID;
	}
	public void setMeetingID(String meetingID) {
		this.meetingID = meetingID;
	}
	public String getMeetingPassCode() {
		return meetingPassCode;
	}
	public void setMeetingPassCode(String meetingPassCode) {
		this.meetingPassCode = meetingPassCode;
	}
	public String getMeetingURL() {
		return meetingPassCode;
	}
	public void setMeetingURL(String meetingURL) {
		this.meetingURL = meetingURL;
	}	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	
}
