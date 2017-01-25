package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "VU360ReportExecutionSummary")
public class VU360ReportExecutionSummary implements SearchableKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1154761297314238469L;

	@Id
	@javax.persistence.TableGenerator(name = "VU360ReportExecutionSummary_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360ReportExecutionSummary", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360ReportExecutionSummary_ID")
	private Long id;
	
	@Column(name = "CSVLOCATION")
	private String csvLocation = null;
	
	@Column(name = "HTMLLOCATION")
	private String htmlLocation = null;
	
	@Column(name = "EXECUTIONDATE")
	private Date executionDate = new Date(System.currentTimeMillis());
	
	@OneToOne(optional=false)
    @JoinColumn(name = "VU360REPORT_ID")
	private VU360Report report = null;
	
	@OneToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="VU360USER_ID")
	private VU360User user=null;
	/**
	 * @return the csvLocation
	 */
	public String getCsvLocation() {
		return csvLocation;
	}

	/**
	 * @param csvLocation
	 *            the csvLocation to set
	 */
	public void setCsvLocation(String csvLocation) {
		this.csvLocation = csvLocation;
	}

	/**
	 * @return the htmlLocation
	 */
	public String getHtmlLocation() {
		return htmlLocation;
	}

	/**
	 * @param htmlLocation
	 *            the htmlLocation to set
	 */
	public void setHtmlLocation(String htmlLocation) {
		this.htmlLocation = htmlLocation;
	}

	/**
	 * @return the executionDate
	 */
	public Date getExecutionDate() {
		return executionDate;
	}

	/**
	 * @param executionDate
	 *            the executionDate to set
	 */
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
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

	public VU360User getUser() {
		return user;
	}

	public void setUser(VU360User user) {
		this.user = user;
	}
}
