package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SurveyResult")
public class SurveyResult implements SearchableKey {

	private static final long serialVersionUID = -2117323377264073171L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SurveyResult_ID")
	@GenericGenerator(name = "SurveyResult_ID", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "SURVEYRESULT") })
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="Survey_ID")
	private Survey survey;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VU360User_ID")
	private VU360User surveyee;
	
	@OneToOne
	@JoinColumn(name="COURSE_ID")
	private Course course;
	
	@OneToOne
	@JoinColumn(name="LEARNINGSESSION_ID")
	private LearningSession learningSession;
	
	@Column(name = "RESPONSEDATE")
	private Date responseDate;

	@OneToMany(mappedBy = "surveyResult" , cascade = CascadeType.PERSIST)
	private List<SurveyResultAnswer> answers = new ArrayList<SurveyResultAnswer>();

	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public VU360User getSurveyee() {
		return surveyee;
	}

	public void setSurveyee(VU360User surveyee) {
		this.surveyee = surveyee;
	}

	public List<SurveyResultAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<SurveyResultAnswer> answers) {
		this.answers = answers;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setLearningSession(LearningSession learningSession) {
		this.learningSession = learningSession;
	}

	public LearningSession getLearningSession() {
		return learningSession;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

}
