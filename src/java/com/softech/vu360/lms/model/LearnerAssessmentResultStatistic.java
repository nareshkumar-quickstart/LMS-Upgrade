package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;


/**
 * 
 * @author haider.ali
 * 
 */
@Entity
@DiscriminatorValue("AssessmentResultStatistic")
public class LearnerAssessmentResultStatistic extends LearnerStatistic {
	
	public static final int ASSESSMENT_TYPE_PRE_TEST = 0;
	public static final int ASSESSMENT_TYPE_QUIZ = 1;
	public static final int ASSESSMENT_TYPE_PRACTICE_TEST = 2;
	public static final int ASSESSMENT_TYPE_POST_TEST = 3;
	
	// type of assessment this result is for
	@Column(name = "ASSESSMENTTYPE")
	private Integer assessmentType = ASSESSMENT_TYPE_PRE_TEST;
	
	// raw score as calculated by player
	@Column(name = "RAWSCORE")
	private Double rawScore = 0.0;
	
	// total number of questions presented to learner in assessment
	//@Column(name = "TOTALNUMBERQUESTIONSASKED")
	@Transient
	private Integer totalNumberQuestionsAsked = 0;
	
	// total number of answers provided by learner in assessment - this is usually
	// the same as the total number of questions asked
	//@Column(name = "TOTALNUMBERQUESTIONSANSWERED")
	@Transient
	private Integer totalNumberQuestionsAnswered = 0;
	
	// total number of questions answered correctly by learner in assessment
	//@Column(name = "TOTALNUMBERQUESTIONSCORRECT")
	@Transient
	private Integer totalNumberQuestionsCorrect = 0;
	
	// total number of questions answered incorrectly by learner in assessment
	//@Column(name = "TOTALNUMBERQUESTIONSINCORRECT")
	@Transient
	private Integer totalNumberQuestionsIncorrect = 0;
	
	
	// total time (in seconds) it took the learner to complete the assessment
	//@Column(name = "TIMEATTENDEDINSECONDS")
	@Transient
	private Integer timeToTakeAssessmentInSeconds = 0;
	
	// did the learner pass this assessment?
	//@Column(name = "PASSEDASSESSMENT")
	@Transient
	private  Boolean passedAssessment = false;
	// did the learner acheive mastery for this assessment?
	
	//@Column(name = "ACHEIVEDASSESSMENTMASTERY")
	@Transient
	private  Boolean acheivedAssessmentMastery = false;

	public Integer getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(Integer assessmentType) {
		this.assessmentType = assessmentType;
	}

	public double getRawScore() {
		return rawScore;
	}

	public void setRawScore(double rawScore) {
		this.rawScore = rawScore;
	}

	public Integer getTotalNumberQuestionsAsked() {
		return totalNumberQuestionsAsked;
	}

	public void setTotalNumberQuestionsAsked(Integer totalNumberQuestionsAsked) {
		this.totalNumberQuestionsAsked = totalNumberQuestionsAsked;
	}

	public Integer getTotalNumberQuestionsAnswered() {
		return totalNumberQuestionsAnswered;
	}

	public void setTotalNumberQuestionsAnswered(Integer totalNumberQuestionsAnswered) {
		this.totalNumberQuestionsAnswered = totalNumberQuestionsAnswered;
	}

	public Integer getTotalNumberQuestionsCorrect() {
		return totalNumberQuestionsCorrect;
	}

	public void setTotalNumberQuestionsCorrect(Integer totalNumberQuestionsCorrect) {
		this.totalNumberQuestionsCorrect = totalNumberQuestionsCorrect;
	}

	public Integer getTotalNumberQuestionsIncorrect() {
		return totalNumberQuestionsIncorrect;
	}

	public void setTotalNumberQuestionsIncorrect(Integer totalNumberQuestionsIncorrect) {
		this.totalNumberQuestionsIncorrect = totalNumberQuestionsIncorrect;
	}

	public Integer getTimeToTakeAssessmentInSeconds() {
		return timeToTakeAssessmentInSeconds;
	}

	public void setTimeToTakeAssessmentInSeconds(Integer timeToTakeAssessmentInSeconds) {
		this.timeToTakeAssessmentInSeconds = timeToTakeAssessmentInSeconds;
	}

	public  Boolean isPassedAssessment() {
		return passedAssessment;
	}

	public void setPassedAssessment(Boolean passedAssessment) {
		this.passedAssessment = passedAssessment;
	}

	public  Boolean isAcheivedAssessmentMastery() {
		return acheivedAssessmentMastery;
	}

	public void setAcheivedAssessmentMastery(Boolean acheivedAssessmentMastery) {
		this.acheivedAssessmentMastery = acheivedAssessmentMastery;
	}

}
