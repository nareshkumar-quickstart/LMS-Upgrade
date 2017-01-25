package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;

/**
 * @author Dyutiman
 */
public class CreditReportForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<CourseItem> courses = new ArrayList <CourseItem>();
	private List<LearnerItemForm> learners = new ArrayList <LearnerItemForm>();
	private String action = "";
	// for course search
	private String courseName = "";
	private String businessKey = "";
	// for time frame
	private String fromDate = "";
	private String toDate = "";
	private String fromDateWithTime = "";
	private String toDateWithTime = "";
	private String fromTimeHour = "";
	private String toTimeHour = "";
	private String fromTimeMinute = "";
	private String toTimeMinute = "";
	private String fromTimePhase = "AM";
	private String toTimePhase = "AM";
	// selection
	private String selectAllLearner = "true";
	private int selectedLearners = 0;
	// option
	private String exportCsv = "";
	private String markComplete = "";
	private String genCertificate = "";
	private String csvDel = "";
	private String firstRowHeader = "yes";
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	// for creating csv & certificates
	private String last = "false";
	
	
	public String getExportCsv() {
		return exportCsv;
	}
	public void setExportCsv(String exportCsv) {
		this.exportCsv = exportCsv;
	}
	public String getMarkComplete() {
		return markComplete;
	}
	public void setMarkComplete(String markComplete) {
		this.markComplete = markComplete;
	}
	public String getGenCertificate() {
		return genCertificate;
	}
	public void setGenCertificate(String genCertificate) {
		this.genCertificate = genCertificate;
	}
	public List<CourseItem> getCourses() {
		return courses;
	}
	public void setCourses(List<CourseItem> courses) {
		this.courses = courses;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromTimeMinute() {
		return fromTimeMinute;
	}
	public void setFromTimeMinute(String fromTimeMinute) {
		this.fromTimeMinute = fromTimeMinute;
	}
	public String getToTimeMinute() {
		return toTimeMinute;
	}
	public void setToTimeMinute(String toTimeMinute) {
		this.toTimeMinute = toTimeMinute;
	}
	public String getSelectAllLearner() {
		return selectAllLearner;
	}
	public void setSelectAllLearner(String selectAllLearner) {
		this.selectAllLearner = selectAllLearner;
	}
	public List<LearnerItemForm> getLearners() {
		return learners;
	}
	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getShowAll() {
		return showAll;
	}
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getFromTimeHour() {
		return fromTimeHour;
	}
	public void setFromTimeHour(String fromTimeHour) {
		this.fromTimeHour = fromTimeHour;
	}
	public String getToTimeHour() {
		return toTimeHour;
	}
	public void setToTimeHour(String toTimeHour) {
		this.toTimeHour = toTimeHour;
	}
	public int getSelectedLearners() {
		return selectedLearners;
	}
	public void setSelectedLearners(int selectedLearners) {
		this.selectedLearners = selectedLearners;
	}
	public String getCsvDel() {
		return csvDel;
	}
	public void setCsvDel(String csvDel) {
		this.csvDel = csvDel;
	}
	public String getFirstRowHeader() {
		return firstRowHeader;
	}
	public void setFirstRowHeader(String firstRowHeader) {
		this.firstRowHeader = firstRowHeader;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getFromTimePhase() {
		return fromTimePhase;
	}
	public void setFromTimePhase(String fromTimePhase) {
		this.fromTimePhase = fromTimePhase;
	}
	public String getToTimePhase() {
		return toTimePhase;
	}
	public void setToTimePhase(String toTimePhase) {
		this.toTimePhase = toTimePhase;
	}
	/**
	 * @return the toDateWithTime
	 */
	public String getToDateWithTime() {
		return toDateWithTime;
	}
	/**
	 * @param toDateWithTime the toDateWithTime to set
	 */
	public void setToDateWithTime(String toDateWithTime) {
		this.toDateWithTime = toDateWithTime;
	}
	/**
	 * @return the fromDateWithTime
	 */
	public String getFromDateWithTime() {
		return fromDateWithTime;
	}
	/**
	 * @param fromDateWithTime the fromDateWithTime to set
	 */
	public void setFromDateWithTime(String fromDateWithTime) {
		this.fromDateWithTime = fromDateWithTime;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
}