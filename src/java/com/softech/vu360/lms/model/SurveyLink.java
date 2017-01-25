/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEYLINKS")
public class SurveyLink implements SearchableKey{
	
	private static final long serialVersionUID = -8091666252873810064L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SURVEYLINKS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYLINKS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYLINKS_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "LINKNAME")
	private String urlName;
	
	@OneToOne
    @JoinColumn(name="SURVEY_ID")
	private Survey survey = null;
	
	@Transient
	private Integer displayOrder = 0;


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
	 * @return the urlName
	 */
	public String getUrlName() {
		return urlName;
	}
	/**
	 * @param urlName the urlName to set
	 */
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	/**
	 * @return the survey
	 */
	public Survey getSurvey() {
		return survey;
	}
	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
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


}
