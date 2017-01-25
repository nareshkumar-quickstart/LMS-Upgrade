package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageGrade  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private VU360User user;
	
	//for LearnerSelfStudyCourseActivity
	private String score;
	private String override;
	private LearnerCourseActivity learnerCourseActivity;
	
	//for LearnerLectureCourseActivity
	private boolean attended = false;
	
	//for LearnerAssignmentActivity
	private boolean attempted = false;
	private boolean completed = false;
	private String percentScore ;
	private String rawScore ;
	
	//for LearnerFinalCourseActivity
	private boolean courseComplete = false;
	private String finalPercentScore;
	private String finalRawScore;
	private String courseCompleteDate ;
	
	
	/**
	 * @return the user
	 */
	public VU360User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(VU360User user) {
		this.user = user;
	}
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the override
	 */
	public String getOverride() {
		return override;
	}
	/**
	 * @param override the override to set
	 */
	public void setOverride(String override) {
		this.override = override;
	}
	/**
	 * @return the learnerCourseActivity
	 */
	public LearnerCourseActivity getLearnerCourseActivity() {
		return learnerCourseActivity;
	}
	/**
	 * @param learnerCourseActivity the learnerCourseActivity to set
	 */
	public void setLearnerCourseActivity(LearnerCourseActivity learnerCourseActivity) {
		this.learnerCourseActivity = learnerCourseActivity;
	}
	/**
	 * @return the attended
	 */
	public boolean isAttended() {
		return attended;
	}
	/**
	 * @param attended the attended to set
	 */
	public void setAttended(boolean attended) {
		this.attended = attended;
	}
	/**
	 * @return the attempted
	 */
	public boolean isAttempted() {
		return attempted;
	}
	/**
	 * @param attempted the attempted to set
	 */
	public void setAttempted(boolean attempted) {
		this.attempted = attempted;
	}
	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	/**
	 * @return the percentScore
	 */
	public String getPercentScore() {
		return percentScore;
	}
	/**
	 * @param percentScore the percentScore to set
	 */
	public void setPercentScore(String percentScore) {
		this.percentScore = percentScore;
	}
	/**
	 * @return the rawScore
	 */
	public String getRawScore() {
		return rawScore;
	}
	/**
	 * @param rawScore the rawScore to set
	 */
	public void setRawScore(String rawScore) {
		this.rawScore = rawScore;
	}
	/**
	 * @return the courseComplete
	 */
	public boolean isCourseComplete() {
		return courseComplete;
	}
	/**
	 * @param courseComplete the courseComplete to set
	 */
	public void setCourseComplete(boolean courseComplete) {
		this.courseComplete = courseComplete;
	}
	/**
	 * @return the finalPercentScore
	 */
	public String getFinalPercentScore() {
		return finalPercentScore;
	}
	/**
	 * @param finalPercentScore the finalPercentScore to set
	 */
	public void setFinalPercentScore(String finalPercentScore) {
		this.finalPercentScore = finalPercentScore;
	}
	/**
	 * @return the finalRawScore
	 */
	public String getFinalRawScore() {
		return finalRawScore;
	}
	/**
	 * @param finalRawScore the finalRawScore to set
	 */
	public void setFinalRawScore(String finalRawScore) {
		this.finalRawScore = finalRawScore;
	}
	/**
	 * @return the courseCompleteDate
	 */
	public String getCourseCompleteDate() {
		return courseCompleteDate;
	}
	/**
	 * @param courseCompleteDate the courseCompleteDate to set
	 */
	public void setCourseCompleteDate(String courseCompleteDate) {
		this.courseCompleteDate = courseCompleteDate;
	}
	
}
