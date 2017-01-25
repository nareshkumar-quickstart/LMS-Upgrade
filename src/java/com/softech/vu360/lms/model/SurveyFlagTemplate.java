package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SURVEYFLAGTEMPLATE")
public class SurveyFlagTemplate implements SearchableKey {

	
	private static final long serialVersionUID = -1672610573894247143L;

	@Id
	@javax.persistence.TableGenerator(name = "SURVEYFLAGTEMPLATE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYFLAGTEMPLATE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYFLAGTEMPLATE_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "FLAGNAME")
	private String flagName = null;
	
	@Column(name = "SURVEYTO")
	private String to = null;
	
	@Column(name = "SUBJECT")
	private String subject = null;
	
	@Column(name = "MESSAGE")
	private String message = null;
	
	@Column(name = "SENDMETF")
	private  Boolean isSendMe = false;
	
	//private SurveyQuestion question ; 
	
	@ManyToMany
    @JoinTable(name="SURVEYFLAGTEMPLATE_SURVEYANSWERITEM", joinColumns = @JoinColumn(name="SERVEYFLAGTEMPLATE_ID"),inverseJoinColumns = @JoinColumn(name="SURVEYANSWERITEM_ID"))
    private List<SurveyAnswerItem> surveyAnswers = new ArrayList<SurveyAnswerItem>();
	
	@OneToOne
	@JoinColumn(name="SURVEY_ID")
	private Survey survey=null;
	
	

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
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the flagName
	 */
	public String getFlagName() {
		return flagName;
	}

	/**
	 * @param flagName the flagName to set
	 */
	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the isSendMe
	 */
	public  Boolean isSendMe() {
		return isSendMe;
	}

	/**
	 * @param isSendMe the isSendMe to set
	 */
	public void setSendMe(Boolean isSendMe) {
		this.isSendMe = isSendMe;
	}

	/**
	 * @return the question
	 */
	public SurveyQuestion getQuestion() {
		return surveyAnswers.get(0).getSurveyQuestion();
	}

	

	/**
	 * @return the surveyAnswers
	 */
	public List<SurveyAnswerItem> getSurveyAnswers() {
		return surveyAnswers;
	}

	/**
	 * @param surveyAnswers the surveyAnswers to set
	 */
	public void setSurveyAnswers(List<SurveyAnswerItem> surveyAnswers) {
		this.surveyAnswers = surveyAnswers;
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


}
