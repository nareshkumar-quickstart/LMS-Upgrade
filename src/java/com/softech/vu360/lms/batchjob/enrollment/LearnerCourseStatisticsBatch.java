package com.softech.vu360.lms.batchjob.enrollment;

import java.util.Date;

import com.softech.vu360.lms.model.LearnerCourseStatistics;

/**
 * this class is used with the new learner enrollment
 * service for creating new enrollments.  As such this
 * class is not used in the normal application and should 
 * never be read as an object - it is used to optimize writes
 * as the domain model is too complex
 * 
 * @author jason
 * 
 */
public class LearnerCourseStatisticsBatch {

	private Long id;
	private String status = LearnerCourseStatistics.NOT_STARTED;
	private LearnerEnrollmentBatch learnerEnrollment;
	private double averagePostTestScore = 0;
	private double averageQuizScore = 0;
	private boolean completed = false;
	private Date completionDate = null;
	private Date firstAccessDate = null;
	private Date firstPostTestDate = null;
	private Date firstQuizDate = null;
	private double highestPostTestScore = 0;
	private double highestQuizScore = 0;
	private Date lastAccessDate = null;
	private Date lastPostTestDate = null;
	private Date lastQuizDate = null;
	private int launchesAccrued = 0;
	private double lowestPostTestScore = -1;
	private double lowestQuizScore = -1;
	private int numberPostTestsTaken = 0;
	private double numberQuizesTaken = 0;
	private double percentComplete = 0;
	private Date preTestDate = null;
	private double pretestScore = -1;
	private int totalTimeInSeconds = 0;
	private String certificateURL=null;
	private boolean reported = false;
	private Date reportedDate = null;
	private String debugInfo="";

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the lastAccessDate
	 */
	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	/**
	 * @param lastAccessDate
	 *            the lastAccessDate to set
	 */
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	/**
	 * @return the learnerEnrollment
	 */
	public LearnerEnrollmentBatch getLearnerEnrollment() {
		return learnerEnrollment;
	}

	/**
	 * @param learnerEnrollment the learnerEnrollment to set
	 */
	public void setLearnerEnrollment(LearnerEnrollmentBatch learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}

	/**
	 * @return the averagePostTestScore
	 */
	public double getAveragePostTestScore() {
		return averagePostTestScore;
	}

	/**
	 * @param averagePostTestScore
	 *            the averagePostTestScore to set
	 */
	public void setAveragePostTestScore(double averagePostTestScore) {
		this.averagePostTestScore = averagePostTestScore;
	}

	/**
	 * @return the averageQuizScore
	 */
	public double getAverageQuizScore() {
		return averageQuizScore;
	}

	/**
	 * @param averageQuizScore
	 *            the averageQuizScore to set
	 */
	public void setAverageQuizScore(double averageQuizScore) {
		this.averageQuizScore = averageQuizScore;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}

	/**
	 * @param completionDate
	 *            the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	/**
	 * @return the firstAccessDate
	 */
	public Date getFirstAccessDate() {
		return firstAccessDate;
	}

	/**
	 * @param firstAccessDate
	 *            the firstAccessDate to set
	 */
	public void setFirstAccessDate(Date firstAccessDate) {
		this.firstAccessDate = firstAccessDate;
	}

	/**
	 * @return the firstPostTestDate
	 */
	public Date getFirstPostTestDate() {
		return firstPostTestDate;
	}

	/**
	 * @param firstPostTestDate
	 *            the firstPostTestDate to set
	 */
	public void setFirstPostTestDate(Date firstPostTestDate) {
		this.firstPostTestDate = firstPostTestDate;
	}

	/**
	 * @return the firstQuizDate
	 */
	public Date getFirstQuizDate() {
		return firstQuizDate;
	}

	/**
	 * @param firstQuizDate
	 *            the firstQuizDate to set
	 */
	public void setFirstQuizDate(Date firstQuizDate) {
		this.firstQuizDate = firstQuizDate;
	}

	/**
	 * @return the heighestPostTestScore
	 */
	public double getHighestPostTestScore() {
		return highestPostTestScore;
	}

	/**
	 * @param heighestPostTestScore
	 *            the heighestPostTestScore to set
	 */
	public void setHighestPostTestScore(double highestPostTestScore) {
		this.highestPostTestScore = highestPostTestScore;
	}

	/**
	 * @return the highestQuizScore
	 */
	public double getHighestQuizScore() {
		return highestQuizScore;
	}

	/**
	 * @param highestQuizScore
	 *            the highestQuizScore to set
	 */
	public void setHighestQuizScore(double highestQuizScore) {
		this.highestQuizScore = highestQuizScore;
	}

	/**
	 * @return the lastPostTestDate
	 */
	public Date getLastPostTestDate() {
		return lastPostTestDate;
	}

	/**
	 * @param lastPostTestDate
	 *            the lastPostTestDate to set
	 */
	public void setLastPostTestDate(Date lastPostTestDate) {
		this.lastPostTestDate = lastPostTestDate;
	}

	/**
	 * @return the lastQuizDate
	 */
	public Date getLastQuizDate() {
		return lastQuizDate;
	}

	/**
	 * @param lastQuizDate
	 *            the lastQuizDate to set
	 */
	public void setLastQuizDate(Date lastQuizDate) {
		this.lastQuizDate = lastQuizDate;
	}

	/**
	 * @return the launchesAccrued
	 */
	public int getLaunchesAccrued() {
		return launchesAccrued;
	}

	/**
	 * @param launchesAccrued
	 *            the launchesAccrued to set
	 */
	public void setLaunchesAccrued(int launchesAccrued) {
		this.launchesAccrued = launchesAccrued;
	}

	/**
	 * @return the lowestPostTestScore
	 */
	public double getLowestPostTestScore() {
		return lowestPostTestScore;
	}

	/**
	 * @param lowestPostTestScore
	 *            the lowestPostTestScore to set
	 */
	public void setLowestPostTestScore(double lowestPostTestScore) {
		this.lowestPostTestScore = lowestPostTestScore;
	}

	/**
	 * @return the lowestQuizScore
	 */
	public double getLowestQuizScore() {
		return lowestQuizScore;
	}

	/**
	 * @param lowestQuizScore
	 *            the lowestQuizScore to set
	 */
	public void setLowestQuizScore(double lowestQuizScore) {
		this.lowestQuizScore = lowestQuizScore;
	}

	/**
	 * @return the numberPostTestsTaken
	 */
	public int getNumberPostTestsTaken() {
		return numberPostTestsTaken;
	}

	/**
	 * @param numberPostTestsTaken
	 *            the numberPostTestsTaken to set
	 */
	public void setNumberPostTestsTaken(int numberPostTestsTaken) {
		this.numberPostTestsTaken = numberPostTestsTaken;
	}

	/**
	 * @return the numberQuizesTaken
	 */
	public double getNumberQuizesTaken() {
		return numberQuizesTaken;
	}

	/**
	 * @param numberQuizesTaken
	 *            the numberQuizesTaken to set
	 */
	public void setNumberQuizesTaken(double numberQuizesTaken) {
		this.numberQuizesTaken = numberQuizesTaken;
	}

	/**
	 * @return the percentComplete
	 */
	public double getPercentComplete() {
		return percentComplete;
	}

	/**
	 * @param percentComplete
	 *            the percentComplete to set
	 */
	public void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}

	/**
	 * @return the preTestDate
	 */
	public Date getPreTestDate() {
		return preTestDate;
	}

	/**
	 * @param preTestDate
	 *            the preTestDate to set
	 */
	public void setPreTestDate(Date preTestDate) {
		this.preTestDate = preTestDate;
	}

	/**
	 * @return the pretestScore
	 */
	public double getPretestScore() {
		return pretestScore;
	}

	/**
	 * @param pretestScore
	 *            the pretestScore to set
	 */
	public void setPretestScore(double pretestScore) {
		this.pretestScore = pretestScore;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the totalTimeInSeconds
	 */
	public int getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}

	/**
	 * @param totalTimeInSeconds
	 *            the totalTimeInSeconds to set
	 */
	public void setTotalTimeInSeconds(int totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}
	
	public void addTimeSpentInSeconds(int totalTimeToAddInSeconds) {
		this.totalTimeInSeconds += totalTimeToAddInSeconds;
	}
	
	
	/**
	 * @return the reported
	 */
	public boolean isReported() {
		return reported;
	}

	/**
	 * @param reported the reported to set
	 */
	public void setReported(boolean reported) {
		this.reported = reported;
	}

	/**
	 * @return the reportedDate
	 */
	public Date getReportedDate() {
		return reportedDate;
	}

	/**
	 * @param reportedDate the reportedDate to set
	 */
	public void setReportedDate(Date reportedDate) {
		this.reportedDate = reportedDate;
	}

	public String getCertificateURL() {
		return certificateURL;
	}

	public void setCertificateURL(String certificateURL) {
		this.certificateURL = certificateURL;
	}
	public String getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
}
