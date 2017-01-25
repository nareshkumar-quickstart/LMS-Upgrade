package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class HomeworkAssignmentSubmissionForm  implements ILMSBaseInterface{
	public String getFirstName() {
		return firstName;
	}
	private static final long serialVersionUID = 1L;
	public String getLastName() {
		return lastName;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getStatus() {
		return status;
	}

	public String getScoringMethod() {
		return scoringMethod;
	}

	public void setScoringMethod(String scoringMethod) {
		this.scoringMethod = scoringMethod;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFullName() {

		return this.firstName + " " + this.lastName;

	}

	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	public String getInstructionsFileName() {
		return instructionsFileName;
	}

	public void setInstructionsFileName(String instructionsFileName) {
		this.instructionsFileName = instructionsFileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLearnerEnrollment() {
		return learnerEnrollment;
	}

	public void setLearnerEnrollment(String learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}

	/**
	 * @return the percentScore
	 */
	public String getPercentScore() {
		return percentScore;
	}

	/**
	 * @param percentScore
	 *            the percentScore to set
	 */
	public void setPercentScore(String percentScore) {
		this.percentScore = percentScore;
	}

	private String id = "";
	private String learnerEnrollment = "";
	private String firstName = "";
	private String lastName = "";
	private String courseName = "";
	private String status = "";
	private String scoringMethod = "";
	private String assignmentName = "";
	private String score = "";
	private String completionStatus = "";
	private String instructionsFileName = "";
	private String percentScore = "";

}
