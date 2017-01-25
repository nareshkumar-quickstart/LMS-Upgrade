package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.model.ReportFilterItem;

public interface ReportingConfigurationService {

	public List<VU360Report> getSystemReports(String mode);

	public List<VU360Report> getOwnedReports(VU360User user);
	public List<VU360Report> getOwnedReports(Long userID, String mode);
	public VU360Report getReportCopy(long id);
	
	public VU360ReportExecutionSummary lastExecutionSummary(VU360Report report,Long userID);
	public VU360Report changeReportFavouriteStatus(VU360Report report, boolean makeFavourite);

	public void editReportSummary(VU360Report report);

	public VU360Report cloneReport(VU360Report baseReport, String reportName, String reportCategory,
			String reportDescription, com.softech.vu360.lms.vo.VU360User owner);

	public VU360Report saveReportFieldVisibility(VU360Report baseReport, List<VU360ReportField> reportFields);

	public VU360Report saveReportFieldOrder(VU360Report baseReport, List<VU360ReportField> reportFields);

	public VU360Report saveReportFieldSortOrder(VU360Report baseReport, List<VU360ReportField> reportFields);

	public Map<String, List<VU360ReportFilterOperand>> getReportFilterOperandMap();

	public VU360Report saveReportFilters(VU360Report baseReport, List<ReportFilterItem> filterItems);

	public VU360ReportExecutionSummary addReportExeuctionSummary(VU360Report report,
			VU360ReportExecutionSummary executionSummary);

	public List<VU360ReportFilter> getVU360ReportFilter(VU360Report report);

	public List<VU360ReportField> getVU360ReportFieldsbyReportId(Long reportId);
}
