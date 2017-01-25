package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ReportForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public ReportForm() {

	}
	private String userMode="";
	/**
	 * @return the userMode
	 */
	public String getUserMode() {
		return userMode;
	}

	/**
	 * @param userMode the userMode to set
	 */
	public void setUserMode(String userMode) {
		this.userMode = userMode;
	}

	private List<VU360Report> ownedReports = null;

	public List<VU360Report> getOwnedReports() {
		return ownedReports;
	}

	public void setOwnedReports(List<VU360Report> ownedReports) {
		this.ownedReports = ownedReports;
	}
	
	public void addOwnedReport(VU360Report report){
		if(ownedReports==null)
			ownedReports = new ArrayList<VU360Report>();
		ownedReports.add(report);
	}
	
	/**
	 * Helper method to get the reports organised by category 
	 * and Favorite reports to be used in left menu.
	 */
	public Map<String, ArrayList<VU360Report>> getReportsByCategory(){
		Map<String, ArrayList<VU360Report>> reportMap = new LinkedHashMap<String,ArrayList<VU360Report>>();
		if(ownedReports!=null){
			for(VU360Report report:ownedReports){
				String category = report.getCategory();
				if(reportMap.containsKey(category)){
					ArrayList<VU360Report> categoryReports = reportMap.get(category);
					categoryReports.add(report);
				}else{
					ArrayList<VU360Report> categoryReports = new ArrayList<VU360Report>();
					categoryReports.add(report);
					reportMap.put(category, categoryReports);
				}
			}
		}
		return reportMap;
	}
	
	public ArrayList<VU360Report> getFavouriteReports(){
		ArrayList<VU360Report> favourites = new ArrayList<VU360Report>();
		if(ownedReports!=null){
			for(VU360Report report:ownedReports){
				if(report.isFavorite())
					favourites.add(report);
			}
		}
		return favourites;
	}
	
	private VU360Report currentReport;

	public VU360Report getCurrentReport() {
		return currentReport;
	}

	public void setCurrentReport(VU360Report currentReport) {
		this.currentReport = currentReport;
	}
	
	private VU360ReportExecutionSummary currentReportLastExecutionSummary;

	public VU360ReportExecutionSummary getCurrentReportLastExecutionSummary() {
		return currentReportLastExecutionSummary;
	}

	public void setCurrentReportLastExecutionSummary(
			VU360ReportExecutionSummary currentReportLastExecutionSummary) {
		this.currentReportLastExecutionSummary = currentReportLastExecutionSummary;
	}
	
	private VU360Report reportSummaryEdit;

	public VU360Report getReportSummaryEdit() {
		return reportSummaryEdit;
	}

	public void setReportSummaryEdit(VU360Report reportSummaryEdit) {
		this.reportSummaryEdit = reportSummaryEdit;
	}
	
	private List<VU360ReportField> reportFieldsEdit;

	public List<VU360ReportField> getReportFieldsEdit() {
		return reportFieldsEdit;
	}

	public void setReportFieldsEdit(List<VU360ReportField> reportFieldsEdit) {
		this.reportFieldsEdit = reportFieldsEdit;
	}
	
	private Map<String, List<VU360ReportFilterOperand>> operandMap;

	public Map<String, List<VU360ReportFilterOperand>> getOperandMap() {
		return operandMap;
	}

	public void setOperandMap(Map<String, List<VU360ReportFilterOperand>> operandMap) {
		this.operandMap = operandMap;
	}
	
	private List<ReportFilterItem> reportFilterItems = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(ReportFilterItem.class));

	public List<ReportFilterItem> getReportFilterItems() {
		return reportFilterItems;
	}

	public void setReportFilterItems(List<ReportFilterItem> reportFilterItems) {
		this.reportFilterItems = reportFilterItems;
	}

	
	private String startDate = "";
	private String endDate = "";
	private String courseId = "";
	private String startTime = "";
	private String endTime = "";
	
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
