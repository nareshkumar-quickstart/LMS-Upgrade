package com.softech.vu360.lms.web.controller.model;

import java.util.Date;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ReportFilterItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long reportFieldId;
	private String reportFieldDatatype;
	
	private Long filterOperandId;
	
	private String filterType;
	
	private String filterValue;//generic for now
	private String filterStringValue;
	private Integer filterIntegerValue;
	private Double filterDoubleValue;
	private Date filterDateValue;
	private Boolean filterBooleanValue = false;
	
	private boolean markedForDeletion = false;


	/**
	 * @return the filterType
	 */
	public String getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	/**
	 * @return the filterOperandId
	 */
	public Long getFilterOperandId() {
		return filterOperandId;
	}

	/**
	 * @param filterOperandId the filterOperandId to set
	 */
	public void setFilterOperandId(Long filterOperandId) {
		this.filterOperandId = filterOperandId;
	}

	/**
	 * @return the filterValue
	 */
	public String getFilterValue() {
		return filterValue;
	}

	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the reportFieldId
	 */
	public Long getReportFieldId() {
		return reportFieldId;
	}

	/**
	 * @param reportFieldId the reportFieldId to set
	 */
	public void setReportFieldId(Long reportFieldId) {
		this.reportFieldId = reportFieldId;
	}

	/**
	 * @return the markedForDeletion
	 */
	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}

	/**
	 * @param markedForDeletion the markedForDeletion to set
	 */
	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}

	/**
	 * @return the filterDateValue
	 */
	public Date getFilterDateValue() {
		return filterDateValue;
	}

	/**
	 * @param filterDateValue the filterDateValue to set
	 */
	public void setFilterDateValue(Date filterDateValue) {
		this.filterDateValue = filterDateValue;
	}

	/**
	 * @return the filterIntegerValue
	 */
	public Integer getFilterIntegerValue() {
		return filterIntegerValue;
	}

	/**
	 * @param filterIntegerValue the filterIntegerValue to set
	 */
	public void setFilterIntegerValue(Integer filterIntegerValue) {
		this.filterIntegerValue = filterIntegerValue;
	}

	/**
	 * @return the filterStringValue
	 */
	public String getFilterStringValue() {
		return filterStringValue;
	}

	/**
	 * @param filterStringValue the filterStringValue to set
	 */
	public void setFilterStringValue(String filterStringValue) {
		this.filterStringValue = filterStringValue;
	}

	/**
	 * @return the filterDoubleValue
	 */
	public Double getFilterDoubleValue() {
		return filterDoubleValue;
	}

	/**
	 * @param filterDoubleValue the filterDoubleValue to set
	 */
	public void setFilterDoubleValue(Double filterDoubleValue) {
		this.filterDoubleValue = filterDoubleValue;
	}

	/**
	 * @return the reportFieldDatatype
	 */
	public String getReportFieldDatatype() {
		return reportFieldDatatype;
	}

	/**
	 * @param reportFieldDatatype the reportFieldDatatype to set
	 */
	public void setReportFieldDatatype(String reportFieldDatatype) {
		this.reportFieldDatatype = reportFieldDatatype;
	}

	/**
	 * @return the filterBooleanValue
	 */
	public Boolean getFilterBooleanValue() {
		return filterBooleanValue;
	}

	/**
	 * @param filterBooleanValue the filterBooleanValue to set
	 */
	public void setFilterBooleanValue(Boolean filterBooleanValue) {
		this.filterBooleanValue = filterBooleanValue;
	}

	
}
