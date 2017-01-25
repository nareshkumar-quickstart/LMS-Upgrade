package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "LEARNERSCOSTATISTIC")
public class LearnerSCOStatistics implements SearchableKey {

	private static final long serialVersionUID = -5063338646342881425L;
	public static final String COMPLETION_STATUS_UNKOWN = "unknown";
	public static final String COMPLETION_STATUS_NOT_ATTEMPTED = "not attempted";
	public static final String COMPLETION_STATUS_COMPLETED = "completed";
	public static final String COMPLETION_STATUS_PASSED = "passed";
	public static final String COMPLETION_STATUS_FAILED = "failed";
	public static final String COMPLETION_STATUS_INCOMPLETE = "incomplete";
	public static final String FOR_CREDIT = "credit";
	public static final String NOT_FOR_CREDIT = "no-credit";
	
	
	@Id
	@javax.persistence.TableGenerator(name = "LEARNERSCOSTATISTIC_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERSCOSTATISTIC", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERSCOSTATISTIC_ID")
	private Long id;
	
	@Column(name = "COMPLETIONSTATUS")
	private String completionStatus = COMPLETION_STATUS_UNKOWN;
	
	@Column(name = "CREATEDATE")
	private Date createdDate = new Date();
	
	@Column(name = "LASTUPDATEDDATE")
	private Date lastUpdatedDate = new Date();
	
	@Column(name = "CREDIT")
	private String credit = FOR_CREDIT;
	@Column(name = "SCOENTRY")
	private String entry = null;
	@Column(name = "SCOEXIT")
	private String exit = null;
	@Column(name = "POSTASSESSMENTTAKE")
	private  Boolean postAssessmentTaken = false;
	@Column(name = "PROGRESSMEASURE")
	private double progressMeasure;
	@Column(name = "SCOLOCATION")
	private String scoLocation = null;
	@Column(name = "SUSPENDDATA")
	private String supsendData = null;
	@Column(name = "SESSIONTIMESECONDS")
	private long sessionTimeInSeconds;
	@Column(name = "TOTALTIMESECONDS")
	private long totalTimeInSeconds;
	
	@Transient
	private List<LearnerSCOObjectiveStatistics> learnerScoObjectiveStatistics = new ArrayList<LearnerSCOObjectiveStatistics>();
	@Transient
	private List<ADLStore> adlStores = new ArrayList<ADLStore>();
	
	@OneToOne
	@JoinColumn(name = "SCO_ID")
	private SCO sco ;
	
	@Column(name = "LEARNERENROLLMENT_ID")
	private Long learnerEnrollmentId;
	
	@Column(name = "LEARNER_ID")
	private Long learnerId;
	
	@OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.MERGE })
	@JoinColumn(name = "LEARNERSCOSTATISTIC_ID")
	private List<LearnerSCOAssessment> assessments = new ArrayList<LearnerSCOAssessment>();
	
	@Transient
	private transient LearnerSCOAssessment currentAssessmentAttempt = null;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompletionStatus() {
		return completionStatus;
	}
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public String getExit() {
		return exit;
	}
	public void setExit(String exit) {
		this.exit = exit;
	}
	public  Boolean isPostAssessmentTaken() {
		return postAssessmentTaken;
	}
	public void setPostAssessmentTaken(Boolean postAssessmentTaken) {
		this.postAssessmentTaken = postAssessmentTaken;
	}
	public double getProgressMeasure() {
		return progressMeasure;
	}
	public void setProgressMeasure(double progressMeasure) {
		this.progressMeasure = progressMeasure;
	}
	public String getScoLocation() {
		return scoLocation;
	}
	public void setScoLocation(String scoLocation) {
		this.scoLocation = scoLocation;
	}
	public String getSupsendData() {
		return supsendData;
	}
	public void setSupsendData(String supsendData) {
		this.supsendData = supsendData;
	}
	public long getSessionTimeInSeconds() {
		return sessionTimeInSeconds;
	}
	public void setSessionTimeInSeconds(long sessionTimeInSeconds) {
		this.sessionTimeInSeconds = sessionTimeInSeconds;
	}
	public long getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}
	public void setTotalTimeInSeconds(long totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}
	public List<LearnerSCOObjectiveStatistics> getLearnerScoObjectiveStatistics() {
		return learnerScoObjectiveStatistics;
	}
	public void setLearnerScoObjectiveStatistics(
			List<LearnerSCOObjectiveStatistics> learnerScoObjectiveStatistics) {
		this.learnerScoObjectiveStatistics = learnerScoObjectiveStatistics;
	}
	public List<ADLStore> getAdlStores() {
		return adlStores;
	}
	public void setAdlStores(List<ADLStore> adlStores) {
		this.adlStores = adlStores;
	}
	public long getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}
	public void setLearnerEnrollmentId(long learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}
	public long getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}
	public List<LearnerSCOAssessment> getAssessments() {
		return assessments;
	}
	public void setAssessments(List<LearnerSCOAssessment> assessments) {
		this.assessments = assessments;
	}
	public LearnerSCOAssessment getCurrentAssessmentAttempt() {
		return currentAssessmentAttempt;
	}
	public void setCurrentAssessmentAttempt(
			LearnerSCOAssessment currentAssessmentAttempt) {
		this.currentAssessmentAttempt = currentAssessmentAttempt;
	}
	public SCO getSco() {
		return sco;
	}
	public void setSco(SCO sco) {
		this.sco = sco;
	}
	@Override
	public String getKey() {
		return null;
	}

	public void addAdlStore(ADLStore adlStore) {
		adlStores.add(adlStore);
	}
	
	public void createNewLearnerSCOAssessmentAttempt() {
		LearnerSCOAssessment scoAssessment = new LearnerSCOAssessment();
		this.currentAssessmentAttempt=scoAssessment;
	}
	
	public void addLearnerSCOAssessment(LearnerSCOAssessment scoAssessment) {
		this.assessments.add(scoAssessment);
	}
	
	public void addLearnerScoObjectiveStatistics(LearnerSCOObjectiveStatistics scoObjStat) {
		this.learnerScoObjectiveStatistics.add(scoObjStat);
	}
	
}
