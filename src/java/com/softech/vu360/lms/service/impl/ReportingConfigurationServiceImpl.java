package com.softech.vu360.lms.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.model.VU360ReportFilterValue;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.VU360ReportExecutionSummaryRepository;
import com.softech.vu360.lms.repositories.VU360ReportFilterOperandRepository;
import com.softech.vu360.lms.repositories.VU360ReportFilterRepository;
import com.softech.vu360.lms.repositories.VU360ReportRepository;
import com.softech.vu360.lms.service.ReportingConfigurationService;
import com.softech.vu360.lms.web.controller.model.ReportFilterItem;

public class ReportingConfigurationServiceImpl implements ReportingConfigurationService {

	@Autowired
	private VU360ReportRepository reportRepository = null;
	
	@Autowired
	private VU360ReportExecutionSummaryRepository reportExeSumRepository = null;
	
	@Autowired
	private VU360ReportFilterRepository reportFilterRepository = null;
	
	@Autowired
	private VU360ReportFilterOperandRepository reportFilterOpeRepository = null;
	
	
	public ReportingConfigurationServiceImpl() {}

	public List<VU360Report> getSystemReports(String mode) {
		List<VU360Report> reportList = reportRepository.findByModeAndSystemOwnedOrderByTitleAsc(mode,true);
		if(reportList == null)
			reportList = new ArrayList<VU360Report>();
		return reportList;
	}

	public List<VU360Report> getOwnedReports(VU360User user) {
		List<VU360Report> reportList = reportRepository.findByOwnerId(user.getId());
		if(reportList == null)
			reportList = new ArrayList<VU360Report>();
		return reportList;
	}
	
	public List<VU360Report> getOwnedReports(Long userId, String mode) {
		List<VU360Report> reportList = new ArrayList<VU360Report>();
		List<VU360Report> systemReportList = reportRepository.findByModeAndSystemOwnedOrderByTitleAsc(mode, true);
		List<VU360Report> ownedReportList = reportRepository.findByOwnerIdAndMode(userId, mode);
		
		if(systemReportList != null && systemReportList.size()>0)
			reportList.addAll(systemReportList);
		if(ownedReportList != null && ownedReportList.size()>0)
			reportList.addAll(ownedReportList);
		
		return reportList;
	}

	public VU360Report getReportCopy(long id) {
		return this.reportRepository.findOne(id);
	}

	public VU360ReportExecutionSummary lastExecutionSummary(VU360Report report,Long userID) {
		//get the list of VU360ReportExecutionSummary for this report sorted by the execution date in descending order
		List<VU360ReportExecutionSummary> reportExecutionSummaries = reportExeSumRepository.findByReportIdAndUserIdOrderByExecutionDateDesc(report.getId(),userID);
		if(reportExecutionSummaries!=null && reportExecutionSummaries.size()>0)
			return reportExecutionSummaries.get(0);
		return null;
	}

	
	public VU360Report changeReportFavouriteStatus(VU360Report report, boolean makeFavourite) {
		VU360Report clonereport = null;
		try {
			clonereport = reportRepository.findOne(report.getId());
			clonereport.setFavorite(makeFavourite);
			clonereport=reportRepository.save(clonereport);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return clonereport;
		
	}

	
	public void editReportSummary(VU360Report report) {
		try {
			VU360Report clonereport = reportRepository.findOne(report.getId());
			clonereport.setCategory(report.getCategory());
			clonereport.setTitle(report.getTitle());
			clonereport.setDescription(report.getDescription());
			clonereport.setSystemOwned(report.getSystemOwned());
			reportRepository.save(clonereport);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public VU360Report cloneReport(VU360Report baseReport, String reportName, String reportCategory, String reportDescription, com.softech.vu360.lms.vo.VU360User owner){
		
		VU360User vu360UserModel = new VU360User();
		vu360UserModel.setId(owner.getId());
		
		//create a clone report object and set it up with the basic summary
		baseReport = reportRepository.findOne(baseReport.getId());
		VU360Report cloneReport = new VU360Report();
		//cloneReport.setId(null);
		cloneReport.setTitle(reportName);
		cloneReport.setDescription(reportDescription);
		cloneReport.setMode(baseReport.getMode());
		cloneReport.setCategory(reportCategory);
		cloneReport.setDerivedFrom(baseReport);
		cloneReport.setSqlTemplateUri(baseReport.getSqlTemplateUri());
		cloneReport.setSystemOwned(new Boolean(false));
		cloneReport.setOwner(vu360UserModel);
		//cloneReport.setFields(baseReport.getFields());
		
		//setup the list of associated fields to this clone report
		List<VU360ReportField> cloneFields = baseReport.getFields();
		VU360ReportField field = new VU360ReportField();
		//cloneReport.setFields(null);
		for(VU360ReportField cloneField: cloneFields){
			field.setId(null);
			//field.setVu360report(cloneReport);
			field.setColumnFormat(cloneField.getColumnFormat());
			field.setDataType(cloneField.getDataType());
			field.setDbColumnName(cloneField.getDbColumnName());
			field.setDefaultDisplayOrder(cloneField.getDefaultDisplayOrder());
			field.setDefaultSortOrder(cloneField.getDefaultSortOrder());
			field.setDisplayName(cloneField.getDisplayName());
			field.setDisplayOrder(cloneField.getDisplayOrder());
			field.setEnabled(cloneField.getEnabled());
			field.setFieldOrder(cloneField.getFieldOrder());
			field.setFilterable(cloneField.getFilterable());
			field.setSortOrder(cloneField.getSortOrder());
			field.setSortType(cloneField.getSortType());
			field.setVisible(cloneField.getVisible());
			field.setVisibleByDefault(cloneField.getVisibleByDefault());
			field.setVu360report(cloneReport);
			cloneReport.getFields().add(field);
			field = new VU360ReportField();
			
		}
		
		//persist it with references
		return reportRepository.save(cloneReport);
	}
	
	public VU360Report saveReportFieldVisibility(VU360Report baseReport, List<VU360ReportField> reportFields) {
		VU360Report cloneReport = reportRepository.findOne(baseReport.getId());
		List<VU360ReportField> cloneReportFields = cloneReport.getFields();
		for(int i = 0; i<cloneReportFields.size(); i++){
			VU360ReportField cloneField = cloneReportFields.get(i);
			for(int j = 0; j<reportFields.size(); j++){
				VU360ReportField editedField = reportFields.get(j);
				if(editedField.getId().longValue() == cloneField.getId().longValue()){
					cloneField.setVisible(editedField.getVisible());
					break;
				}
			}
		}
		//persist it with references
		return reportRepository.save(cloneReport);
	}

	public VU360Report saveReportFieldOrder(VU360Report baseReport, List<VU360ReportField> reportFields) {
		VU360Report cloneReport = reportRepository.findOne(baseReport.getId());
		List<VU360ReportField> cloneReportFields = cloneReport.getFields();
		for(int i = 0; i<cloneReportFields.size(); i++){
			VU360ReportField cloneField = cloneReportFields.get(i);
			for(int j = 0; j<reportFields.size(); j++){
				VU360ReportField editedField = reportFields.get(j);
				if(editedField.getId().longValue() == cloneField.getId().longValue()){
					cloneField.setDisplayOrder(editedField.getDisplayOrder());
					break;
				}
			}
		}
		//persist it with references
		return reportRepository.save(cloneReport);
	}

	public VU360Report saveReportFieldSortOrder(VU360Report baseReport, List<VU360ReportField> reportFields) {
		VU360Report cloneReport = reportRepository.findOne(baseReport.getId());
		List<VU360ReportField> cloneReportFields = cloneReport.getFields();
		for(int i = 0; i<cloneReportFields.size(); i++){
			VU360ReportField cloneField = cloneReportFields.get(i);
			for(int j = 0; j<reportFields.size(); j++){
				VU360ReportField editedField = reportFields.get(j);
				if(editedField.getId().longValue() == cloneField.getId().longValue()){
					cloneField.setSortOrder(editedField.getSortOrder());
					cloneField.setSortType(editedField.getSortType());
					break;
				}
			}
		}
		//persist it with references
		return reportRepository.save(cloneReport);
	}

	public Map<String, List<VU360ReportFilterOperand>> getReportFilterOperandMap() {
		List<VU360ReportFilterOperand> operands = reportFilterOpeRepository.findAllByOrderByDisplayOrderAsc();
		Map<String,List<VU360ReportFilterOperand>> operandMap = new LinkedHashMap<String,List<VU360ReportFilterOperand>>();
		for(VU360ReportFilterOperand op : operands){
			String keyDataType = op.getDataType().toLowerCase();
			if(operandMap.containsKey(keyDataType)){
				List<VU360ReportFilterOperand> opList = operandMap.get(keyDataType);
				opList.add(op);
			}else{
				List<VU360ReportFilterOperand> opList = new ArrayList<VU360ReportFilterOperand>();
				opList.add(op);
				operandMap.put(keyDataType, opList);
			}
		}
		return operandMap;
	}

	

	public List<VU360ReportFilter> getVU360ReportFields(VU360Report report){
		return reportFilterRepository.findByReportId(report.getId());
	}
	
	public VU360Report saveReportFilters(VU360Report baseReport, List<ReportFilterItem> filterItems) {
		
		VU360Report reportCopy = reportRepository.findOne(baseReport.getId());
		
		Map<String, List<VU360ReportFilterOperand>> operandMap = this.getReportFilterOperandMap();
		
		//List<VU360ReportFilter> copiedFilters = reportCopy.getFilters();
		List<VU360ReportFilter> addedFilters = new ArrayList<VU360ReportFilter>();
		
		//loop over the form filter items
		for(int i=0; i<filterItems.size(); i++){
			ReportFilterItem item = filterItems.get(i);
			
			if(item.isMarkedForDeletion()){//removing the deleted filters
				for(int j=reportCopy.getFilters().size()-1; j>=0; j--){
					VU360ReportFilter existingFilter = reportCopy.getFilters().get(j); 
					if(item.getId()!=null && existingFilter.getId().longValue()==item.getId().longValue()){
						//removedFilters.add(existingFilter);
						//copiedFilters.remove(j);
						reportCopy.getFilters().remove(j);
						break;
					}
				}
			}else{
				VU360ReportFilter filter = null;
				if(item.getId()!=null){	//existing filter update
					for(int j=reportCopy.getFilters().size()-1; j>=0; j--){
						VU360ReportFilter existingFilter = reportCopy.getFilters().get(j); 
						if(existingFilter.getId().longValue()==item.getId().longValue()){
							filter = existingFilter;
							filter.setReportFilterType(item.getFilterType());

							break;
						}
					}
				}else{//new addition
					filter = new VU360ReportFilter();
					filter.setReport(reportCopy);
					filter.setReportFilterType(item.getFilterType());

					//create a new filter value for this filter
					VU360ReportFilterValue value = new VU360ReportFilterValue();
					value.setFilter(filter);
					filter.setValue(value);
					
					addedFilters.add(filter);
				}
				
				//get the field
				Long fieldId = item.getReportFieldId();
				for(int j=0; j<reportCopy.getFields().size(); j++){
					if(fieldId.longValue() == reportCopy.getFields().get(j).getId().longValue()){
						filter.setField(reportCopy.getFields().get(j));
						break;
					}
				}
				
				//set the value of the filtervalue using the field datatype
				String fieldDataType = filter.getField().getDataType();
				//long filterValue = Long.valueOf(item.getFilterIntegerValue());
				if(fieldDataType.equalsIgnoreCase(VU360ReportField.DT_STRING))
					filter.getValue().setStringValue(item.getFilterStringValue());
				else if(fieldDataType.equalsIgnoreCase(VU360ReportField.DT_INTEGER))
					filter.getValue().setNumericValue(BigInteger.valueOf(Long.valueOf(item.getFilterIntegerValue())));
				else if(fieldDataType.equalsIgnoreCase(VU360ReportField.DT_DOUBLE))
					filter.getValue().setNumericValue(BigInteger.valueOf(Long.valueOf(item.getFilterIntegerValue())));
				else if(fieldDataType.equalsIgnoreCase(VU360ReportField.DT_DATE))
					filter.getValue().setDateValue(item.getFilterDateValue());
				else if(fieldDataType.equalsIgnoreCase(VU360ReportField.DT_BOOLEAN))
					filter.getValue().setNumericValue(item.getFilterBooleanValue()?new BigInteger("1"):new BigInteger("0"));
				else
					;//others to come..

				//get the operand
				List<VU360ReportFilterOperand> possibleOps = operandMap.get(filter.getField().getDataType().toLowerCase());
				for(int k=0; k<possibleOps.size(); k++){
					VU360ReportFilterOperand op = possibleOps.get(k);
					if(op.getId().longValue() == item.getFilterOperandId().longValue()){
						filter.setOperand(op);
						break;
					}
				}
				
				//editedFilters.add(filter);
			}
		}
		if(addedFilters.size()>0)
			reportCopy.getFilters().addAll(addedFilters);
		reportFilterRepository.save(reportCopy.getFilters());
//		reportCopy = reportRepository.save(reportCopy);
		return reportCopy;
	}

	
	public VU360ReportExecutionSummary addReportExeuctionSummary(
			VU360Report report, VU360ReportExecutionSummary executionSummary) {
		// TODO refactor me to be correct after UAT!
		//VU360Report dbReport = reportDAO.loadReport(report.getId());
		executionSummary.setReport(report);
		return reportExeSumRepository.save(executionSummary);
	}

	/**
	 * @param reportDAO the reportDAO to set
	 */
	/*public void setReportDAO(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}
	 */
	@Override
	public List<VU360ReportFilter> getVU360ReportFilter(VU360Report report) {
		return reportFilterRepository.findByReportId(report.getId());
	}
	
	@Override
	public List<VU360ReportField> getVU360ReportFieldsbyReportId(Long reportId){
		List<VU360ReportField> fields =  reportRepository.findOne(reportId).getFields();
		return fields;
	}
}
