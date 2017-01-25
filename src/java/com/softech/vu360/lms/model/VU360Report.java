package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "VU360REPORT")
public class VU360Report implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	public static final String LEARNER_MODE = "LEARNER";
	public static final String MANAGER_MODE = "MANAGER";
	public static final String ADMINISTRATOR_MODE = "ADMINISTRATOR";
	public static final String ACCREDITATION_MODE = "LEARNER";
	
    
    @Id
	@javax.persistence.TableGenerator(name = "VU360REPORT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "VU360REPORT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360REPORT_ID")
	private Long id;
    
    @Column(name = "CATEGORY")
	private String category = null;
    
    @Column(name = "DATASOURCE")
	private String dataSource = null;
    
    @Column(name = "REPORTNAME")
	private String title = null;
    
    @Column(name = "DESCRIPTION")
	private String description = null;
    
    @Column(name = "ISFAVORITE")
	private Boolean favorite = false;
    
    @Column(name = "MODE")
	private String mode = null;
    
    @Column(name = "SQLTEMPLATEURI")
	private String sqlTemplateUri = null;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
	private VU360User owner = null;
    
	@OneToOne
    @JoinColumn(name="derivedFrom")
	private VU360Report derivedFrom = null;
    
	@OneToOne
    @JoinColumn(name="ORIGINALSYSTEMREPORT_ID")
	private VU360Report originalSystemReport = null;
    
    @Column(name = "SYSTEMOWNEDREPORTTF")
	private Boolean systemOwned = false;
    
    @OneToMany(cascade = CascadeType.PERSIST  , mappedBy="report" )
	private List<VU360ReportExecutionSummary> executionSummaries = null;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy="vu360report" )
	private List<VU360ReportField> fields = new ArrayList();
	
	@OneToMany(mappedBy="report",cascade = {CascadeType.MERGE, CascadeType.REMOVE},orphanRemoval = true )
	private List<VU360ReportFilter> filters = new ArrayList();
	
    @Transient
	private List<VU360ReportParameter> Parameter = new ArrayList();
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the favorite
	 */
	public Boolean isFavorite() {
		return favorite;
	}

	/**
	 * @param favorite
	 *            the favorite to set
	 */
	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the owner
	 */
	public VU360User getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(VU360User owner) {
		this.owner = owner;
	}

	/**
	 * @return the derivedFrom
	 */
	public VU360Report getDerivedFrom() {
		return derivedFrom;
	}

	/**
	 * @param derivedFrom
	 *            the derivedFrom to set
	 */
	public void setDerivedFrom(VU360Report derivedFrom) {
		this.derivedFrom = derivedFrom;
	}

	/**
	 * @return the originalSystemReport
	 */
	public VU360Report getOriginalSystemReport() {
		return originalSystemReport;
	}

	/**
	 * @param originalSystemReport
	 *            the originalSystemReport to set
	 */
	public void setOriginalSystemReport(VU360Report originalSystemReport) {
		this.originalSystemReport = originalSystemReport;
	}

	/**
	 * @return the systemOwned
	 */
	public Boolean getSystemOwned() {
		return systemOwned;
	}

	/**
	 * @param systemOwned
	 *            the systemOwned to set
	 */
	public void setSystemOwned(Boolean systemOwned) {
		this.systemOwned = systemOwned;
	}

	/**
	 * @return the executionSummaries
	 */
	public List<VU360ReportExecutionSummary> getExecutionSummaries() {
		return executionSummaries;
	}

	/**
	 * @param executionSummaries
	 *            the executionSummaries to set
	 */
	public void setExecutionSummaries(
			List<VU360ReportExecutionSummary> executionSummaries) {
		this.executionSummaries = executionSummaries;
	}

	/**
	 * @return the fields
	 */
	public List<VU360ReportField> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<VU360ReportField> fields) {
		this.fields = fields;
	}

	/**
	 * @return the filters
	 */
	public List<VU360ReportFilter> getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 *            the filters to set
	 */
	public void setFilters(List<VU360ReportFilter> filters) {
		this.filters = filters;
	}

	/**
	 * @return the sqlTemplateUri
	 */
	public String getSqlTemplateUri() {
		return sqlTemplateUri;
	}

	/**
	 * @param sqlTemplateUri
	 *            the sqlTemplateUri to set
	 */
	public void setSqlTemplateUri(String sqlTemplateUri) {
		this.sqlTemplateUri = sqlTemplateUri;
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
	
	

	public List<VU360ReportParameter> getParameter() {
		return Parameter;
	}

	public void setParameter(List<VU360ReportParameter> parameter) {
		Parameter = parameter;
	}
	
}
