/**
 * 
 */
package com.softech.vu360.lms.model;

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
@Table(name = "LEARNERASSIGNMENTACTIVITY")
@DiscriminatorValue("ASSIGNMENT")
public class LearnerAssignmentActivity extends LearnerCourseActivity implements
		SearchableKey {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ASSIGNMENTATTEMPTEDTF")
	private Boolean assignmentAttempted = false;
	
	@Column(name = "ASSIGNMENTCOMPLETETF")
	private Boolean assignmentComplete = false;
	
	@Column(name = "PERCENTSCORE")
	private double percentScore = 0.00;
	
	@Column(name = "RAWSCORE")
	private double rawScore = 0.00;
	
	/**
	 * @return the assignmentAttempted
	 */
	public  Boolean isAssignmentAttempted() {
		return assignmentAttempted;
	}
	/**
	 * @param assignmentAttempted the assignmentAttempted to set
	 */
	public void setAssignmentAttempted(Boolean assignmentAttempted) {
		this.assignmentAttempted = assignmentAttempted;
	}
	/**
	 * @return the assignmentComplete
	 */
	public  Boolean isAssignmentComplete() {
		return assignmentComplete;
	}
	/**
	 * @param assignmentComplete the assignmentComplete to set
	 */
	public void setAssignmentComplete(Boolean assignmentComplete) {
		this.assignmentComplete = assignmentComplete;
	}
	/**
	 * @return the percentScore
	 */
	public double getPercentScore() {
		return percentScore;
	}
	/**
	 * @param percentScore the percentScore to set
	 */
	public void setPercentScore(double percentScore) {
		this.percentScore = percentScore;
	}
	/**
	 * @return the rawScore
	 */
	public double getRawScore() {
		return rawScore;
	}
	/**
	 * @param rawScore the rawScore to set
	 */
	public void setRawScore(double rawScore) {
		this.rawScore = rawScore;
	}
	

}
