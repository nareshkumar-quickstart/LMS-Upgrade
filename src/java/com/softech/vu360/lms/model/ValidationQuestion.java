package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "VALIDATIONQUESTION")
public class ValidationQuestion implements SearchableKey{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqValidationQuestionId")
	@GenericGenerator(name = "seqValidationQuestionId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "VALIDATIONQUESTION") })
	private Long id;
	
	@OneToOne
    @JoinColumn(name="COURSECONFIGURATION_ID")
	private CourseConfiguration courseConfiguration =  new CourseConfiguration();
	
	@OneToOne
    @JoinColumn(name="LANGUAGE_ID")
	private Language language = new Language();
	
	@Column(name="QUESTIONSTEM")
	private String question = null;
	
	@Column(name="QUESTIONTYPE")
	private String questionType = null;
	
	@Column(name="ANSWERQUERY")
	private String answerQuery = null;
	
	@Column(name="IS_ACTIVE")
	private Boolean isActive;
	
	@OneToOne
    @JoinColumn(name="CREATED_BY")
	private Author createdBy=null;
	
	@OneToOne
    @JoinColumn(name="MODIFIED_BY")
	private Author modifiedBy=null;
	
	@Column(name="CREATED_DATE")
	private Date createdDate=null;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate=null;
	
	@Override
	public String getKey() {
		
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CourseConfiguration getCourseConfiguration() {
		return courseConfiguration;
	}

	public void setCourseConfiguration(CourseConfiguration courseConfiguration) {
		this.courseConfiguration = courseConfiguration;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getAnswerQuery() {
		return answerQuery;
	}

	public void setAnswerQuery(String answerQuery) {
		this.answerQuery = answerQuery;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Author getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Author createdBy) {
		this.createdBy = createdBy;
	}

	public Author getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Author modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
