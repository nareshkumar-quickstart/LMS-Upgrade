package com.softech.vu360.lms.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "VU360REPORTFILTERVALUE")
public class VU360ReportFilterValue implements SearchableKey {

	@Id
	@javax.persistence.TableGenerator(name = "VU360REPORTFILTERVALUE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360REPORTFILTERVALUE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360REPORTFILTERVALUE_ID")
	private Long id;
	
	@Column(name = "STRINGVALUE")
	private String stringValue;
	
	@Column(name = "DATEVALUE")
	private Date dateValue;
	
	@Transient
	private List<Object> collectionsValue;
	
	@Column(name = "NUMERICVALUE")
	private BigInteger numericValue;
	
	@OneToOne
	@JoinColumn(name="VU360REPORTFILTER_ID")
	private VU360ReportFilter filter = null;


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
	 * @return the filter
	 */
	public VU360ReportFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(VU360ReportFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the dateValue
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * @param dateValue the dateValue to set
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	/**
	 * @return the collectionsValue
	 */
	public List<Object> getCollectionsValue() {
		return collectionsValue;
	}

	/**
	 * @param collectionsValue the collectionsValue to set
	 */
	public void setCollectionsValue(List<Object> collectionsValue) {
		this.collectionsValue = collectionsValue;
	}

	/**
	 * @return the numericValue
	 */
	public BigInteger getNumericValue() {
		return numericValue;
	}

	/**
	 * @param numericValue the numericValue to set
	 */
	public void setNumericValue(BigInteger numericValue) {
		this.numericValue = numericValue;
	}
}
