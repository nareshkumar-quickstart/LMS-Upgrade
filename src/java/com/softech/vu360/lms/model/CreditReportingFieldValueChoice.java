/**
 * 
 */
package com.softech.vu360.lms.model;

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
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "CREDITREPOTINGFIELDVALUECHOICE")
public class CreditReportingFieldValueChoice implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDITREPOTINGFIELDVALUECHOICE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDITREPORTINGFIELDVALUECHOICE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDITREPOTINGFIELDVALUECHOICE_ID")
	private Long id;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name = "LABEL")
	private String label = null;
	
	@Column(name = "VALUE")
	private String value = null;
	
	@OneToOne
    @JoinColumn(name="CREDITREPORTINGFIELD_ID")
	private CreditReportingField creditReportingField = null;
	
	
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
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
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the creditReportingField
	 */
	public CreditReportingField getCreditReportingField() {
		return creditReportingField;
	}
	/**
	 * @param creditReportingField the creditReportingField to set
	 */
	public void setCreditReportingField(CreditReportingField creditReportingField) {
		this.creditReportingField = creditReportingField;
	}
	
	
	

}
