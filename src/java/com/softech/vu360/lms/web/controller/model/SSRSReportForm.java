package com.softech.vu360.lms.web.controller.model;

import java.util.List;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SSRSReportForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public SSRSReportForm() {

	}
	private String userMode="";
	private List<VU360Report> ownedReports = null;
	private VU360Report currentReport;
	private String startDate = "";
	private String endDate = "";
	private String courseId = "";
	private String startTime = "";
	private String endTime = "";
	
	public String getUserMode() {
		return userMode;
	}
	public void setUserMode(String userMode) {
		this.userMode = userMode;
	}
	public List<VU360Report> getOwnedReports() {
		return ownedReports;
	}
	public void setOwnedReports(List<VU360Report> ownedReports) {
		this.ownedReports = ownedReports;
	}
	public VU360Report getCurrentReport() {
		return currentReport;
	}
	public void setCurrentReport(VU360Report currentReport) {
		this.currentReport = currentReport;
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
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
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
}
