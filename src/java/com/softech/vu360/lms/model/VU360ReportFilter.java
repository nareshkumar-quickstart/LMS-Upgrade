package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "VU360REPORTFILTER")
public class VU360ReportFilter implements SearchableKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// filter types
	// basic 'native' types
	public static final String STRING_TYPE = "string";
	public static final String DATE_TYPE = "date";
	public static final String NUMERIC_TYPE = "numeric";
	public static final String BOOLEAN_TYPE = " Boolean";
	// custom filters
	public static final String OBJECT_TYPE = "objectCollection";

	@Id
	@javax.persistence.TableGenerator(name = "VU360ReportFilter_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360ReportFilter", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360ReportFilter_ID")
	private Long id;
	
	@Column(name = "MANDAATORY")
	private Boolean manditoryFilter;// = false;
	
	@OneToOne (optional = false)
    @JoinColumn(name="VU360REPORTFILTEROPERAND_ID")
	private VU360ReportFilterOperand operand = null;
	
	@OneToOne (optional = false)
    @JoinColumn(name="VU360REPORTFIELD_ID")
	private VU360ReportField field = null;
	
	@OneToOne (optional = false)
    @JoinColumn(name="VU360REPORT_ID")
	private VU360Report report = null;
	
	@OneToOne(mappedBy = "filter",  cascade = { CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE },orphanRemoval=true)
	private VU360ReportFilterValue value = null;
	
	
	@Column(name = "REPORTFILTERTYPE")
	private String reportFilterType = null;

	/**
	 * @return the manditoryFilter
	 */
	public  Boolean getManditoryFilter() {
		if(manditoryFilter == null)
			manditoryFilter = Boolean.FALSE;
		return manditoryFilter;
	}

	/**
	 * @param manditoryFilter
	 *            the manditoryFilter to set
	 */
	public void setManditoryFilter(Boolean manditoryFilter) {
		this.manditoryFilter = manditoryFilter;
	}

	/**
	 * @return the operand
	 */
	public VU360ReportFilterOperand getOperand() {
		return operand;
	}

	/**
	 * @param operand
	 *            the operand to set
	 */
	public void setOperand(VU360ReportFilterOperand operand) {
		this.operand = operand;
	}

	/**
	 * @return the field
	 */
	public VU360ReportField getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(VU360ReportField field) {
		this.field = field;
	}

	/**
	 * @return the report
	 */
	public VU360Report getReport() {
		return report;
	}

	/**
	 * @param report
	 *            the report to set
	 */
	public void setReport(VU360Report report) {
		this.report = report;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public VU360ReportFilterValue getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(VU360ReportFilterValue value) {
		this.value = value;
	}

	/**
	 * @return the reportFilterType
	 */
	public String getReportFilterType() {
		return reportFilterType;
	}

	/**
	 * @param reportFilterType the reportFilterType to set
	 */
	public void setReportFilterType(String reportFilterType) {
		this.reportFilterType = reportFilterType;
	}
}
