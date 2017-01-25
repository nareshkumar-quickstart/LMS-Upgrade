package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEYSECTION")
public class SurveySection implements Comparable<SurveySection>, SearchableKey, Serializable {

	private static final long serialVersionUID = -2141723707982069542L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SURVEYSECTION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYSECTION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYSECTION_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToOne
	@JoinColumn(name="SURVEY_ID")
	private Survey survey ;
	
	
	@OneToOne
	@JoinColumn(name="PARENT_ID")
	private SurveySection parent ;
	
	@OneToMany(mappedBy="parent")
	private List<SurveySection> children = new ArrayList<SurveySection>();
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy="surveySection")
	private List<SurveySectionSurveyQuestionBank> surveySectionSurveyQuestionBanks = new ArrayList<SurveySectionSurveyQuestionBank>();
	
	@Override
	public String getKey() {
		return getId().toString();
	}

	@Override
	public int compareTo(SurveySection surveySection) {
		
		int comparisonInt = surveySection.getName().compareTo(this.getName());
		if(comparisonInt == 0)
			return comparisonInt;
		else if(comparisonInt > 0)
			return -1;
		else if(comparisonInt < 0)
			return 1;
		
		return 0;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public SurveySection getParent() {
		return (SurveySection) parent;
	}

	public void setParent(SurveySection parent) {
		this.parent = parent;
	}

	protected Survey getSurveyHolder() {
		return survey;
	}

	protected void setSurveyHolder(Survey surveyHolder) {
		this.survey = surveyHolder;
	}

	protected SurveySection getParentHolder() {
		return parent;
	}

	protected void setParentHolder(SurveySection parentHolder) {
		this.parent = parentHolder;
	}

	public List<SurveySection> getChildren() {
		return children;
	}

	public void setChildren(List<SurveySection> children) {
		this.children = children;
	}

	public List<SurveySectionSurveyQuestionBank> getSurveySectionSurveyQuestionBanks() {
		return surveySectionSurveyQuestionBanks;
	}

	public void setSurveySectionSurveyQuestionBanks(List<SurveySectionSurveyQuestionBank> surveySectionSurveyQuestionBanks) {
		this.surveySectionSurveyQuestionBanks = surveySectionSurveyQuestionBanks;
	}

}
