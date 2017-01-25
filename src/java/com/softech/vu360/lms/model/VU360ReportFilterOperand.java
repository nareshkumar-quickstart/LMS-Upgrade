package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "VU360ReportFilterOperand")
public class VU360ReportFilterOperand implements SearchableKey {

	public static final String STRING_EQUALS_OP = "equals";
	public static final String EQUALS_OP = "equals";
	public static final String CONTAINS_OP = "contains";
	public static final String STARTSWITH_OP = "starts with";
	public static final String ENDSWITH_OP = "ends with";
	public static final String BEFORE_OP = "before";
	public static final String AFTER_OP = "after";
	public static final String IN_OP = "in";
	public static final String GREATER_THAN_OP = "greater than";
	public static final String GREATER_THAN_OR_EQUAL_OP = "greater than or equal";
	public static final String LESS_THAN_OR_EQUAL_OP = "less than";
	public static final String LESS_THAN_OP = "less than or equal";
	public static final String ONE_OF_OP = "one of";

	@Id
	@javax.persistence.TableGenerator(name = "VU360ReportFilterOperand_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360ReportFilterOperand", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360ReportFilterOperand_ID")
	private Long id;
	
	@Column(name = "DATATYPE")
	private String dataType = VU360ReportField.DT_STRING;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name = "LABEL")
	private String label = null;
	
	@Column(name = "VALUE")
	private String value = null;
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder
	 *            the displayOrder to set
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
	 * @param label
	 *            the label to set
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
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
}
