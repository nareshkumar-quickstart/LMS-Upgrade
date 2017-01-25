package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "LEARNERSCOOBJECTIVESTATISTICS")
public class LearnerSCOObjectiveStatistics implements SearchableKey {
	
	private static final long serialVersionUID = 3926810211795937549L;
	
	public static final String SUCCCESS_STATUS_UNKNOWN = "unknown";
	public static final String SUCCCESS_STATUS_PASSED = "passed";
	public static final String SUCCCESS_STATUS_FAILED = "failed";
	
	@Id
	@javax.persistence.TableGenerator(name = "LEARNERSCOOBJECTIVESTATISTICS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERSCOOBJECTIVESTATISTICS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERSCOOBJECTIVESTATISTICS_ID")
	private Long id;
	
	@Column(name = "COMPLETIONSTATUS")
	private String completionStatus = LearnerSCOStatistics.COMPLETION_STATUS_UNKOWN;
	@Column(name = "maxscore")
	private double maxScore;
	@Column(name = "minScore")
	private double minScore;
	@Column(name = "progressMeasure")
	private double progressMeasure;
	@Column(name = "rawScore")
	private double rawScore;
	@Column(name = "scaledScore")
	private double scaledScore;
	@Column(name = "successStatus")
	private String successStatus = SUCCCESS_STATUS_UNKNOWN;
	@Column(name = "LEARNERSCOSTATISTIC_ID")
	private long lrnSCOStatisticID = 0;

	
	@OneToOne
	@JoinColumn(name = "SCOOBJECTIVE_ID")
	private SCOObjective scoObjective ;

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

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public double getMinScore() {
		return minScore;
	}

	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}

	public double getProgressMeasure() {
		return progressMeasure;
	}

	public void setProgressMeasure(double progressMeasure) {
		this.progressMeasure = progressMeasure;
	}

	public double getRawScore() {
		return rawScore;
	}

	public void setRawScore(double rawScore) {
		this.rawScore = rawScore;
	}

	public double getScaledScore() {
		return scaledScore;
	}

	public void setScaledScore(double scaledScore) {
		this.scaledScore = scaledScore;
	}

	public String getSuccessStatus() {
		return successStatus;
	}

	public void setSuccessStatus(String successStatus) {
		this.successStatus = successStatus;
	}

	public SCOObjective getScoObjective() {
		return scoObjective;
	}

	public void setScoObjective(SCOObjective scoObjective) {
		this.scoObjective = scoObjective;
	}

	public long getLrnSCOStatisticID() {
		return lrnSCOStatisticID;
	}

	public void setLrnSCOStatisticID(long lrnSCOStatisticID) {
		this.lrnSCOStatisticID = lrnSCOStatisticID;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
