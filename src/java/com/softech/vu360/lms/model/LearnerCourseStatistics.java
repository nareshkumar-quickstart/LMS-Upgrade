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

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNERCOURSESTATISTICS")
public class LearnerCourseStatistics implements SearchableKey {
	private static final long serialVersionUID = 2086437647415362820L;

	public static final String NOT_STARTED = "notstarted";
	public static final String IN_PROGRESS = "inprogress";
	public static final String IN_COMPLETE = "incomplete";
	public static final String EXPIRED = "Expired";
	public static final String LOCKED = "locked";
	public static final String COMPLETED = "completed";
	public static final String AFFIDAVIT_PENDING = "affidavitpending";
	public static final String AFFIDAVIT_RECEIVED = "affidavitreceived";
	public static final String AFFIDAVIT_DISPUTED = "affidavitdisputed";
	public static final String REPORTING_PENDING = "reportingpending";
	public static final String USER_DECLINED_AFFIDAVIT = "userdeclinedaffidavit";

	public static final String REPORTED = "reported";

	@Id
    @javax.persistence.TableGenerator(name = "LEARNERCOURSESTATISTICS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERCOURSESTATISTICS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERCOURSESTATISTICS_ID")
	private Long id;
	
	@Column(name = "STATUS")
	private String status;
	
	@OneToOne (fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="LEARNERENROLLMENT_ID")
	private LearnerEnrollment learnerEnrollment ;
	
	@Column(name = "AVERAGEPOSTTESTSCORE")
	private Double averagePostTestScore = 0.00;
	
	@Column(name = "AVERAGEQUIZSCORE")
	private Double averageQuizScore = 0.00;
	
	@Column(name = "COMPLETED")
	private Boolean completed;
	
	@Column(name = "COMPLETIONDATE")
	private Date completionDate = null;
	
	@Column(name = "FIRSTACCESSDATE")
	private Date firstAccessDate = null;
	
	@Column(name = "FIRSTPOSTTESTDATE")
	private Date firstPostTestDate = null;
	
	@Column(name = "FIRSTQUIZDATE")
	private Date firstQuizDate = null;
	
	@Column(name = "HIGHESTPOSTTESTSCORE")
	private Double highestPostTestScore = 0.00;
	
	@Column(name = "HIGHESTQUIZSCORE")
	private Double highestQuizScore = 0.00;
	
	@Column(name = "LASTACCESSDATE")
	private Date lastAccessDate = null;
	
	@Column(name = "LASTPOSTTESTDATE")
	private Date lastPostTestDate = null;
	
	@Column(name = "LASTQUIZDATE")
	private Date lastQuizDate = null;
	
	@Column(name = "LAUNCHESACCRUED")
	private Integer launchesAccrued = 0;
	
	@Column(name = "LOWESTPOSTTESTSCORE")
	private Double lowestPostTestScore = -1.0;
	
	@Column(name = "LOWESTQUIZSCORE")
	private Double lowestQuizScore = -1.0;
	
	@Column(name = "NUMBERPOSTTESTSTAKEN")
	private Integer numberPostTestsTaken = 0;
	
	@Column(name = "NUMBERQUIZESTAKEN")
	private Double numberQuizesTaken = 0.00;
	
	@Column(name = "PERCENTCOMPLETE")
	private Double percentComplete = 0.00;
	
	@Column(name = "PRETESTDATE")
	private Date preTestDate = null;
	
	@Column(name = "PRETESTSCORE")
	private Double pretestScore = -1.00;
	
	@Column(name = "TOTALTIMEINSECONDS")
	private Integer totalTimeInSeconds = 0;
	
	@Column(name = "certificateURL")
	private String certificateURL = null;
	
	@Column(name = "REPORTED")
	private Boolean reported = false;
	
	@Column(name = "REPORTDATE")
	private Date reportedDate = null;
	
	@Column(name = "DEBUGINFO")
	private String debugInfo = "";
	
	@Column(name = "CERTIFICATENUMBER")
	private String certificateNumber = null;
	
	@Column(name = "CERTIFICATEISSUEDDATE")
	private Date certificateIssueDate = null;
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
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
	public LearnerEnrollment getLearnerEnrollment() {
		return this.learnerEnrollment;
	}

	/**
	 * @param learnerEnrollment
	 *            the learnerEnrollment to set
	 */
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
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
	public  Boolean getCompleted() {
		if(completed==null){
			completed = Boolean.FALSE;
		}
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(Boolean completed) {
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
	public Integer getLaunchesAccrued() {
		return launchesAccrued;
	}

	/**
	 * @param launchesAccrued
	 *            the launchesAccrued to set
	 */
	public void setLaunchesAccrued(Integer launchesAccrued) {
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
	public Integer getNumberPostTestsTaken() {
		return numberPostTestsTaken;
	}

	/**
	 * @param numberPostTestsTaken
	 *            the numberPostTestsTaken to set
	 */
	public void setNumberPostTestsTaken(Integer numberPostTestsTaken) {
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
		if(status==null){
			status = NOT_STARTED;
		}
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
	public Integer getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}

	/**
	 * @param totalTimeInSeconds
	 *            the totalTimeInSeconds to set
	 */
	public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}

	public void addTimeSpentInSeconds(Integer totalTimeToAddInSeconds) {
		this.totalTimeInSeconds += totalTimeToAddInSeconds;
	}

	public LearnerCourseStatistics clone() {
		LearnerCourseStatistics clone = new LearnerCourseStatistics();
		clone.setId(this.getId());
		clone.setAveragePostTestScore(this.getAveragePostTestScore());
		clone.setAverageQuizScore(this.getAverageQuizScore());
		clone.setCompleted(this.getCompleted());
		clone.setCompletionDate(this.getCompletionDate());
		clone.setFirstAccessDate(this.getFirstAccessDate());
		clone.setFirstPostTestDate(this.getFirstPostTestDate());
		clone.setFirstQuizDate(this.getFirstQuizDate());
		clone.setHighestPostTestScore(this.getHighestPostTestScore());
		clone.setHighestQuizScore(this.getHighestQuizScore());
		clone.setLowestQuizScore(this.getLowestQuizScore());
		clone.setLastAccessDate(this.getLastAccessDate());
		clone.setLastPostTestDate(this.getLastPostTestDate());
		clone.setLastQuizDate(this.getLastQuizDate());
		clone.setLaunchesAccrued(this.getLaunchesAccrued());
		clone.setLearnerEnrollment(this.getLearnerEnrollment());
		clone.setLowestPostTestScore(this.getLowestPostTestScore());
		clone.setNumberQuizesTaken(this.getNumberQuizesTaken());
		clone.setNumberPostTestsTaken(this.getNumberPostTestsTaken());
		clone.setPercentComplete(this.getPercentComplete());
		clone.setPreTestDate(this.getPreTestDate());
		clone.setPretestScore(this.getPretestScore());
		clone.setStatus(this.getStatus());
		clone.setTotalTimeInSeconds(this.getTotalTimeInSeconds());
		clone.setReported(getReported());
		clone.setReportedDate(this.getReportedDate());
		clone.setCertificateNumber(this.getCertificateNumber());
		clone.setCertificateIssueDate(this.getCertificateIssueDate());
		clone.setCertificateURL(this.getCertificateURL());
		clone.setDebugInfo(this.getDebugInfo());
		// TODO: implement me as a true clone

		return clone;
	}

	/**
	 * @return the reported
	 */
	public  Boolean getReported() {
		return reported;
	}

	/**
	 * @param reported
	 *            the reported to set
	 */
	public void setReported(Boolean reported) {
		this.reported = reported;
	}

	/**
	 * @return the reportedDate
	 */
	public Date getReportedDate() {
		return reportedDate;
	}

	/**
	 * @param reportedDate
	 *            the reportedDate to set
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

	public static final int DEBUG_FIELD_SIZE = 8000;

	public void appendDebugInfo(String str) {

		debugInfo = (StringUtils.isBlank(debugInfo) ? "" : debugInfo) + str;
		if (debugInfo.length() > DEBUG_FIELD_SIZE) {
			debugInfo = debugInfo.substring(
					(debugInfo.length() - DEBUG_FIELD_SIZE) + 1,
					debugInfo.length());
		}
	}

	/**
	 * @param certificateNumber
	 *            the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	/**
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}

	/**
	 * // [10/12/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 * 
	 * @param certificateIssueDate
	 *            the certificateIssueDate to set
	 */
	public void setCertificateIssueDate(Date certificateIssueDate) {
		this.certificateIssueDate = certificateIssueDate;
	}

	/**
	 * // [10/12/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 * 
	 * @return the certificateIssueDate
	 */
	public Date getCertificateIssueDate() {
		return certificateIssueDate;
	}

	public Boolean isCourseCompleted() {
		return this.getCompleted()
				&& (this.getStatus().equals(COMPLETED) || this.getStatus().equals(REPORTED));
	}

	public Boolean isCompleted() {
		return this.getCompleted()
				&& (this.getStatus().equals(COMPLETED) || this.getStatus().equals(REPORTED));
	}
	
	public String getStatusDisplayText() {
		if (this.getStatus().equals(NOT_STARTED)) {
			return "Not Started";
		} else if (this.getStatus().equals(IN_PROGRESS)) {
			return "In Progress";
		} else if (this.getStatus().equals(AFFIDAVIT_PENDING)) {
			return "Affidavit Pending";
		} else if (this.getStatus().equals(AFFIDAVIT_RECEIVED)) {
			return "Affidavit Received";
		} else if (this.getStatus().equals(AFFIDAVIT_DISPUTED)) {
			return "Affidavit Disputed";
		} else if (this.getStatus().equals(REPORTING_PENDING)) {
			return "Reporting Pending";
		} else if (this.getStatus().equals(COMPLETED)) {
			return "Completed";
		} else if (this.getStatus().equals(REPORTED)) {
			return "Reported";
		} else if (this.getStatus().equals(LOCKED)) {
			return "Locked";
		} else if (this.getStatus().equals(USER_DECLINED_AFFIDAVIT)) {
			return "User Declined Affidavit";
		} else {
			return this.getStatus();
		}
	}
}
