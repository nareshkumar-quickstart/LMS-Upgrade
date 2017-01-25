/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNERSELFSTUDYCOURSEACTIVITY")
@DiscriminatorValue("SELFSTUDY")
public class LearnerSelfStudyCourseActivity extends LearnerCourseActivity
		implements SearchableKey {
	
	@Column(name = "OVERRIDESCORE")
	private double overrideScore = 0.00;
	
	@OneToOne
    @JoinColumn(name="LEARNERENROLLMENT_ID")
	private LearnerEnrollment learnerEnrollment ;
	
	/**
	 * @return the overrideScore
	 */
	public double getOverrideScore() {
		return overrideScore;
	}
	/**
	 * @param overrideScore the overrideScore to set
	 */
	public void setOverrideScore(double overrideScore) {
		this.overrideScore = overrideScore;
	}
	/**
	 * @return the learnerEnrollment
	 */
	public LearnerEnrollment getLearnerEnrollment() {
		return this.learnerEnrollment;
	}
	/**
	 * @param learnerEnrollment the learnerEnrollment to set
	 */
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}
	

}
