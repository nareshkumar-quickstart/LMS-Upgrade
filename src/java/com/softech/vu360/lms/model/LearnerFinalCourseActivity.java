/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNERFINALCOURSEACTIVITY")
@DiscriminatorValue("FINALSCORE")
public class LearnerFinalCourseActivity extends LearnerCourseActivity {
	
	@Column(name = "COURSECOMPLETETF")
	private Boolean courseComplete = false;
	
	@Column(name = "FINALPERCENTSCORE")
	private double finalPercentScore = 0.00;
	
	@Column(name = "FINALRAWSCORE")
	private double finalRawScore = 0.00;
	
	@Column(name = "COURSECOMPLETEDATE")
	private Date courseCompleteDate = null;
	
	/**
	 * @return the courseComplete
	 */
	public  Boolean isCourseComplete() {
		return courseComplete;
	}
	/**
	 * @param courseComplete the courseComplete to set
	 */
	public void setCourseComplete(Boolean courseComplete) {
		this.courseComplete = courseComplete;
	}
	/**
	 * @return the finalPercentScore
	 */
	public double getFinalPercentScore() {
		return finalPercentScore;
	}
	/**
	 * @param finalPercentScore the finalPercentScore to set
	 */
	public void setFinalPercentScore(double finalPercentScore) {
		this.finalPercentScore = finalPercentScore;
	}
	/**
	 * @return the finalRawScore
	 */
	public double getFinalRawScore() {
		return finalRawScore;
	}
	/**
	 * @param finalRawScore the finalRawScore to set
	 */
	public void setFinalRawScore(double finalRawScore) {
		this.finalRawScore = finalRawScore;
	}
	/**
	 * @return the courseCompleteDate
	 */
	public Date getCourseCompleteDate() {
		return courseCompleteDate;
	}
	/**
	 * @param courseCompleteDate the courseCompleteDate to set
	 */
	public void setCourseCompleteDate(Date courseCompleteDate) {
		this.courseCompleteDate = courseCompleteDate;
	}
	
	
	
}
