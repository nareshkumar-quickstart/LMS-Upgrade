package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
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
@Table(name = "VU360REPORTFIELD")
public class VU360ReportField implements SearchableKey,
		Comparable<VU360ReportField> {

	// data types supported
	public static final String DT_DATE = "date";
	public static final String DT_INTEGER = "int";
	public static final String DT_DOUBLE = "double";
	public static final String DT_STRING = "string";
	public static final String DT_LEARNER_GROUP = "learnerGroup";
	public static final String DT_ORG_GROUP = "orgGroup";
	public static final String DT_COURSE_STATUS = "courseStatus";
	public static final String DT_CHARACTER = "character";
	public static final String DT_BOOLEAN = " Boolean";

	// how are we going to format the field?
	public static final String FORMAT_DATE = "date";
	public static final String FORMAT_INTEGER = "integer";
	public static final String FORMAT_DOUBLE = "double";
	public static final String FORMAT_STRING = "string";
	public static final String FORMAT_BOOLEAN = "Boolean";
	public static final String FORMAT_TIME = "time";
	public static final String FORMAT_PERCENT = "percentage2";
	public static final String CONVERT_FORMAT_PERCENT = "percentage";
	
	public static final String FORMAT_DATETIME = "DateTime";
	
	public static final int SORT_ASCENDING = 0;
	public static final int SORT_DESSCENDING = 1;

	@Id
	@javax.persistence.TableGenerator(name = "VU360REPORTFIELD_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360ReportField", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360REPORTFIELD_ID")
	private Long id;
	
	@Column(name = "DATATYPE")
	private String dataType = DT_STRING;
	
	 @Column(name = "DBCOLUMNNAME")
	private String dbColumnName = null;
	
	 @Column(name = "DISPLAYNAME")
	private String displayName = null;
	
	  @Column(name = "COLUMNFORMAT")
	private String columnFormat = FORMAT_STRING;
	
	  @Column(name = "DEFAULTDISPLAYORDER")
	private Integer defaultDisplayOrder = 0;
	
	 @Column(name = "DEFAULTSORTORDER")
	private Integer defaultSortOrder = 0;
	
	 @Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name = "SORTORDER")
	private Integer sortOrder = 0;
	
	@Column(name = "SORTTYPE")
	private Integer sortType;// = SORT_ASCENDING;
	
	@Column(name = "FIELDORDER")
	private Integer fieldOrder = 0;
	
	@Column(name = "FILTERABLE")
	private Boolean filterable;
	
	@Column(name = "VISIBLE")
	private Boolean visible;
	
	@Column(name = "VISIBLEBYDEFAULT")
	private Boolean visibleByDefault;
	
	@Column(name = "ENABLEDTF")
	private Boolean enabled;
	
	@OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE} )
    @JoinColumn(name="VU360REPORT_ID")
	private VU360Report vu360report = null;

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
	 * @return the dbColumnName
	 */
	public String getDbColumnName() {
		return dbColumnName;
	}

	/**
	 * @param dbColumnName
	 *            the dbColumnName to set
	 */
	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the columnFormat
	 */
	public String getColumnFormat() {
		return columnFormat;
	}

	/**
	 * @param columnFormat
	 *            the columnFormat to set
	 */
	public void setColumnFormat(String columnFormat) {
		this.columnFormat = columnFormat;
	}

	/**
	 * @return the defaultDisplayOrder
	 */
	public Integer getDefaultDisplayOrder() {
		return defaultDisplayOrder;
	}

	/**
	 * @param defaultDisplayOrder
	 *            the defaultDisplayOrder to set
	 */
	public void setDefaultDisplayOrder(Integer defaultDisplayOrder) {
		this.defaultDisplayOrder = defaultDisplayOrder;
	}

	/**
	 * @return the defaultSortOrder
	 */
	public Integer getDefaultSortOrder() {
		return defaultSortOrder;
	}

	/**
	 * @param defaultSortOrder
	 *            the defaultSortOrder to set
	 */
	public void setDefaultSortOrder(Integer defaultSortOrder) {
		this.defaultSortOrder = defaultSortOrder;
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
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the filterable
	 */
	public  Boolean getFilterable() {
		if(filterable == null)
			filterable = Boolean.FALSE;
		return filterable;
	}

	/**
	 * @param filterable
	 *            the filterable to set
	 */
	public void setFilterable(Boolean filterable) {
		this.filterable = filterable;
	}

	/**
	 * @return the visible
	 */
	public  Boolean getVisible() {
		if(visible == null)
			visible = Boolean.FALSE;
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the visibleByDefault
	 */
	public  Boolean getVisibleByDefault() {
		if(visibleByDefault == null)
			visibleByDefault = Boolean.FALSE;
		return visibleByDefault;
	}

	/**
	 * @param visibleByDefault
	 *            the visibleByDefault to set
	 */
	public void setVisibleByDefault(Boolean visibleByDefault) {
		this.visibleByDefault = visibleByDefault;
	}

	/**
	 * @return the enabled
	 */
	public  Boolean getEnabled() {
		if(enabled == null)
			enabled = Boolean.FALSE;
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the fieldOrder
	 */
	public Integer getFieldOrder() {
		return fieldOrder;
	}

	/**
	 * @param fieldOrder
	 *            the fieldOrder to set
	 */
	public void setFieldOrder(Integer fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public int compareTo(VU360ReportField rp) {
		if (this.getFieldOrder() > rp.getFieldOrder()) {
			return 1;
		}
		if (this.getFieldOrder() < rp.getFieldOrder()) {
			return -1;
		}
		return 0;
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
	 * @return the vu360report
	 */
	public VU360Report getVu360report() {
		return vu360report;
	}

	/**
	 * @param vu360report
	 *            the vu360report to set
	 */
	public void setVu360report(VU360Report vu360report) {
		this.vu360report = vu360report;
	}

	/**
	 * @return the sortType
	 */
	public Integer getSortType() {
		if(sortType == null)
			sortType = Integer.valueOf(SORT_ASCENDING);
		return sortType;
	}

	/**
	 * @param sortType the sortType to set
	 */
	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}
}
