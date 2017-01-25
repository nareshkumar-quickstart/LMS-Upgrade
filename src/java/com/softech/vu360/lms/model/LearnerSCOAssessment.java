package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "LEARNERSCOASSESSMENT")
public class LearnerSCOAssessment implements SearchableKey {

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "LEARNERSCOASSESSMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERSCOASSESSMENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERSCOASSESSMENT_ID")
	private Long id;
	
	@Column(name="ATTEMPTDATE")
	private Date attemptDate = new Date();
	@Column(name="MASTERYACHIEVED")
	private  Boolean masteryAcheived = false;
	@Column(name="MAXSCORE")
	private double maxScore = -1;
	@Column(name="MINSCORE")
	private double minScore = -1;
	@Column(name="RAWSCORE")
	private double rawScore = -1;
	@Column(name="SCALEDSCORE")
	private double scaledScore = -1;
	
	@OneToMany(mappedBy = "learnerSCOAssessment" )
    private List<SCOInteraction> scoInteractions = new ArrayList<SCOInteraction>();

	@OneToOne
	@JoinColumn(name = "LEARNERSCOSTATISTIC_ID")
	private LearnerSCOStatistics learnerScoStatistic ;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getAttemptDate() {
		return attemptDate;
	}
	public void setAttemptDate(Date attemptDate) {
		this.attemptDate = attemptDate;
	}
	public  Boolean isMasteryAcheived() {
		return masteryAcheived;
	}
	public void setMasteryAcheived(Boolean masteryAcheived) {
		this.masteryAcheived = masteryAcheived;
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
	public List<SCOInteraction> getScoInteractions() {
		return scoInteractions;
	}
	public void setScoInteractions(List<SCOInteraction> scoInteractions) {
		this.scoInteractions = scoInteractions;
	}
	public LearnerSCOStatistics getLearnerScoStatistic() {
		return learnerScoStatistic;
	}
	public void setLearnerScoStatistic(LearnerSCOStatistics learnerScoStatistic) {
		this.learnerScoStatistic = learnerScoStatistic;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addScoInteraction(SCOInteraction scoInt) {
		this.scoInteractions.add(scoInt);
	}
	
	
}
