package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEYLEARNER")
@IdClass(SurveyLearnerPK.class)
public class SurveyLearner implements Serializable {
	
	private static final long serialVersionUID = -6984831542528932379L;


	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNER_ID")
	private Learner learner;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SURVEY_ID")
	private Survey survey;
	
	@Column(name = "STARTDATE")
	private Date startDate;
	
	@Column(name = "ENDDATE")
	private Date endDate;
	
	@Column(name = "OPENSURVEYTF")
	private  Boolean  surveyNeverExpired=false;
	
	@Column(name = "NOTIFYUPONLEARNERCOMPLETIONTF")
	private  Boolean notifyOnCompletion=false;
	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COMPLETIONNOTIFYVU360USER_ID")
	private VU360User userToNotify;
	
	public SurveyLearner(){
		this.learner = new Learner();
		this.survey = new Survey();
	}
	
	
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean isSurveyNeverExpired() {
		return surveyNeverExpired;
	}
	public void setSurveyNeverExpired(Boolean surveyNeverExpired) {
		this.surveyNeverExpired = surveyNeverExpired;
	}
	public Boolean isNotifyOnCompletion() {
		return notifyOnCompletion;
	}
	public void setNotifyOnCompletion(Boolean notifyOnCompletion) {
		this.notifyOnCompletion = notifyOnCompletion;
	}
	public VU360User getUserToNotify() {
		return userToNotify;
	}
	public void setUserToNotify(VU360User userToNotify) {
		this.userToNotify = userToNotify;
	}

	@Override
	public String toString() {
		return "SurveyLearner [startDate=" + startDate + ", endDate=" + endDate
				+ ", notifyOnCompletion=" + notifyOnCompletion + "]";
	}
}
