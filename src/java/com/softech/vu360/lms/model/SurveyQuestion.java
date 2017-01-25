
package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SURVEYQUESTION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="QUESTION_TYPE")
public class SurveyQuestion implements Comparable<SurveyQuestion>, SearchableKey, Serializable {

	private static final long serialVersionUID = -7111481397022469519L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqSurveyQuestionId")
	@GenericGenerator(name = "seqSurveyQuestionId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "SURVEYQUESTION") })
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
	
    @Nationalized
	@Column(name = "TEXT")
	private String text;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name = "required")
	private Boolean  required = Boolean.FALSE;
	
	@Transient
	private Integer ansFlag = 0;
	
	@Column(name = "NOTES")
	private String notes;
	
	private transient String surveyAnswerLines;
	
	@Column(name = "canHaveFile")
	private Boolean canHaveFile;
	
	@Column(name = "fileRequired")
	private Boolean fileRequired;

	@OneToOne
	@JoinColumn(name="SURVEY_ID")
	private Survey survey;
	
	@OneToOne
	@JoinColumn(name="questionBankId")
	private SurveyQuestionBank surveyQuestionBank;
	
	@OneToMany(mappedBy = "surveyQuestion" , fetch = FetchType.LAZY,cascade = { CascadeType.ALL })
	private List<SurveyAnswerItem> surveyAnswers = new ArrayList<SurveyAnswerItem>();
	
	public Integer getAnsFlag() {
		return ansFlag;
	}

	public void setAnsFlag(Integer ansFlag) {
		this.ansFlag = ansFlag;
	}

	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public List<SurveyAnswerItem> getSurveyAnswers() {
		return surveyAnswers;
	}

	public void setSurveyAnswers(List<SurveyAnswerItem> surveyAnswers) {
		this.surveyAnswers = surveyAnswers;
	}

	public void addSurveyAnswer(SurveyAnswerItem answer) {
		if (this.surveyAnswers == null) {
			this.surveyAnswers = new ArrayList<SurveyAnswerItem>();
		}
		this.surveyAnswers.add(answer);
	}

	public String getSurveyAnswerLines() {
		return surveyAnswerLines;
	}

	public void setSurveyAnswerLines(String surveyAnswerLines) {
		this.surveyAnswerLines = surveyAnswerLines;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public int compareTo(SurveyQuestion arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getRequired() {
		return required;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean isCanHaveFile() {
		return canHaveFile;
	}

	public void setCanHaveFile(Boolean canHaveFile) {
		this.canHaveFile = canHaveFile;
	}

	public Boolean isFileRequired() {
		return fileRequired;
	}

	public void setFileRequired(Boolean fileRequired) {
		this.fileRequired = fileRequired;
	}
}
