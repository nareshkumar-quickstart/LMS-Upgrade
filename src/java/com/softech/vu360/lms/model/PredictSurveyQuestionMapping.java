package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Junaid
 * 
 **/

@Entity
@Table(name = "PredictSurveyQuestionMapping")
public class PredictSurveyQuestionMapping implements Serializable {
	private static final long serialVersionUID = 5710808390066268631L;
	
	@Id
	@javax.persistence.TableGenerator(name = "PredictSurveyQuestionMapping_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "PredictSurveyQuestionMapping", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PredictSurveyQuestionMapping_ID")
	private Long id;
	@Column(name="lmsSectionId")
	private Long lmsSectionId;
	@Column(name="surveyId")
	private Long surveyId;
	@Column(name="surveySectionId")
	private String surveySectionId;
	@Column(name="frameworkId")
	private Long frameworkId;
	@Column(name="questionId")
	private Long questionId;
	@Column(name="lmsSurveyId")
	private Long lmsSurveyId;
	@Column(name="lmsQuestionId")
	private Long lmsQuestionId;
	@Column(name="lmsFrameworkId")
	private Long lmsFrameworkId;
	@Column(name="clones")
	private Integer clones;

	public Long getFrameworkId() {
		return this.frameworkId;
	}

	public Long getId() {
		return this.id;
	}

	public Long getLmsQuestionId() {
		return this.lmsQuestionId;
	}

	public Long getLmsSectionId() {
		return this.lmsSectionId;
	}

	public Long getLmsSurveyId() {
		return this.lmsSurveyId;
	}

	public Long getQuestionId() {
		return this.questionId;
	}

	public Long getSurveyId() {
		return this.surveyId;
	}

	public String getSurveySectionId() {
		return this.surveySectionId;
	}

	public void setFrameworkId(Long frameworkId) {
		this.frameworkId = frameworkId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLmsQuestionId(Long lmsQuestionId) {
		this.lmsQuestionId = lmsQuestionId;
	}

	public void setLmsSectionId(Long lmsSectionId) {
		this.lmsSectionId = lmsSectionId;
	}

	public void setLmsSurveyId(Long lmsSurveyId) {
		this.lmsSurveyId = lmsSurveyId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setSurveySectionId(String surveySectionId) {
		this.surveySectionId = surveySectionId;
	}

	public Long getLmsFrameworkId() {
		return lmsFrameworkId;
	}

	public void setLmsFrameworkId(Long lmsFrameworkId) {
		this.lmsFrameworkId = lmsFrameworkId;
	}

	public Integer getClones() {
		return clones;
	}

	public void setClones(Integer clones) {
		this.clones = clones;
	}

}
