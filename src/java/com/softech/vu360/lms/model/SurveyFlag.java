/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.Date;

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
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEYFLAG")
public class SurveyFlag implements SearchableKey {

	public static final String REVIEWED = "Reviewed";
	public static final String SENTBACKTOLEARNER = "Send Back";
	public static final String AWAITINGRESPONSE = "Awaiting Response";

	@Id
	@javax.persistence.TableGenerator(name = "SURVEYFLAG_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYFLAG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYFLAG_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="SURVEYFLAGTEMPLATE_ID")
	private SurveyFlagTemplate flagTemplate = null;
	
	@Column(name="REVIEWED")
	private Boolean reviewed = false;
	
	@Column(name="SENTBACKTOLEARNER")
	private Boolean sentBackToLearner = false;
	
	@Column(name="TRIGGERDATE")
	private Date triggerDate = null;
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RESPONSEPROVIDER")
	private VU360User responseProvider = null;
	
	@Column(name="REVIEWEDBY")
	private String  reviewedBy = null;
	
	@OneToOne
	@JoinColumn(name="SURVEYRESULTANSWER")
	private SurveyResultAnswer answerline = null;
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
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
	 * @return the flagTemplate
	 */
	public SurveyFlagTemplate getFlagTemplate() {
		return flagTemplate;
	}


	/**
	 * @param flagTemplate the flagTemplate to set
	 */
	public void setFlagTemplate(SurveyFlagTemplate flagTemplate) {
		this.flagTemplate = flagTemplate;
	}


	/**
	 * @return the reviewed
	 */
	public Boolean isReviewed() {
		return reviewed;
	}


	/**
	 * @param reviewed the reviewed to set
	 */
	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}


	/**
	 * @return the sentBackToLearner
	 */
	public Boolean isSentBackToLearner() {
		return sentBackToLearner;
	}


	/**
	 * @param sentBackToLearner the sentBackToLearner to set
	 */
	public void setSentBackToLearner(Boolean sentBackToLearner) {
		this.sentBackToLearner = sentBackToLearner;
	}


	/**
	 * @return the triggerDate
	 */
	public Date getTriggerDate() {
		return triggerDate;
	}


	/**
	 * @param triggerDate the triggerDate to set
	 */
	public void setTriggerDate(Date triggerDate) {
		this.triggerDate = triggerDate;
	}


	/**
	 * @return the responseProvider
	 */
	public VU360User getResponseProvider() {
		return responseProvider;
	}


	/**
	 * @param responseProvider the responseProvider to set
	 */
	public void setResponseProvider(VU360User responseProvider) {
		this.responseProvider = responseProvider;
	}


	/**
	 * @return the reviewedBy
	 */
	public String getReviewedBy() {
		return reviewedBy;
	}


	/**
	 * @param reviewedBy the reviewedBy to set
	 */
	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}


	/**
	 * @return the answerline
	 */
	public SurveyResultAnswer getAnswerline() {
		return answerline;
	}


	/**
	 * @param answerline the answerline to set
	 */
	public void setAnswerline(SurveyResultAnswer answerline) {
		this.answerline = answerline;
	}

}
