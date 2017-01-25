package com.softech.vu360.lms.model;

import java.io.Serializable;
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
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "SCO")
public class SCO implements Serializable {
	
	private static final long serialVersionUID = 4907548599336178454L;
	public static final String TIME_LIMIT_ACTION_EXIT_AND_MESSAGE = "exit,message";
	public static final String TIME_LIMIT_ACTION_EXIT_AND_NO_MESSAGE = "exit,no message";
	public static final String TIME_LIMIT_ACTION_CONTINUE_AND_MESSAGE = "continue,message";
	public static final String TIME_LIMIT_ACTION_CONTINUE_AND_NO_MESSAGE = "continue,no message";
	
	@Id
	@javax.persistence.TableGenerator(name = "SCO_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SCO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SCO_ID")
	private Long id;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate = new Date();
	
	@Column(name = "LASTUPDATEDDATE")
	private Date lastUpdatedDate = new Date();
	
	@Column(name = "CREATEDUSERNAME")
	private String createdUsername = null;
	
	@Column(name = "LASTUPDATEDUSERNAME")
	private String lastUpdatedUsername = null;
	
	@Column(name = "AVAILABLEMODES")
	private String availableModes = null;
	
	@Column(name = "COMPLETIONTHRESHOLD")
	private Double completionThreshold;
	
	@Column(name = "DEFAULTSEQUENCEORDER")
	private Integer defaultSequenceOrder = 0;
	
	@Column(name = "LAUNCHDATA")
	private String launchData = null;
	
	@Column(name = "LAUNCHURI")
	private String launchURI = null;
	
	@Column(name = "MAXIMUMTIMEALLOWEDSECONDS")
	private Long maximumTimeAllowedSeconds;
	
	@Column(name = "TIMELIMITACTION")
	private String timeLimitAction = TIME_LIMIT_ACTION_CONTINUE_AND_NO_MESSAGE;
	
	@Column(name = "SCALEDPASSINGSCORE")
	private Double scaledPassingScore;
	
	@OneToMany(mappedBy = "sco")
    private List<SCOObjective> scoObjectives = new ArrayList<SCOObjective>();
	
	@OneToOne
	@JoinColumn(name = "COURSE_ID")
	private Course course ;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/**
	 * @return the createdUsername
	 */
	public String getCreatedUsername() {
		return createdUsername;
	}
	/**
	 * @param createdUsername the createdUsername to set
	 */
	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}
	/**
	 * @return the lastUpdatedUsername
	 */
	public String getLastUpdatedUsername() {
		return lastUpdatedUsername;
	}
	/**
	 * @param lastUpdatedUsername the lastUpdatedUsername to set
	 */
	public void setLastUpdatedUsername(String lastUpdatedUsername) {
		this.lastUpdatedUsername = lastUpdatedUsername;
	}
	/**
	 * @return the availableModes
	 */
	public String getAvailableModes() {
		return availableModes;
	}
	/**
	 * @param availableModes the availableModes to set
	 */
	public void setAvailableModes(String availableModes) {
		this.availableModes = availableModes;
	}
	/**
	 * @return the completionThreshold
	 */
	public Double getCompletionThreshold() {
		return completionThreshold;
	}
	/**
	 * @param completionThreshold the completionThreshold to set
	 */
	public void setCompletionThreshold(Double completionThreshold) {
		if(completionThreshold==null){
			this.completionThreshold = 0.00;
		}else{
			this.completionThreshold = completionThreshold;
		}
	}
	/**
	 * @return the defaultSequenceOrder
	 */
	public Integer getDefaultSequenceOrder() {
		return defaultSequenceOrder;
	}
	/**
	 * @param defaultSequenceOrder the defaultSequenceOrder to set
	 */
	public void setDefaultSequenceOrder(Integer defaultSequenceOrder) {
		this.defaultSequenceOrder = defaultSequenceOrder;
	}
	/**
	 * @return the launchDate
	 */
	public String getLaunchData() {
		return launchData;
	}
	/**
	 * @param launchDate the launchDate to set
	 */
	public void setLaunchData(String launchData) {
		this.launchData = launchData;
	}
	/**
	 * @return the launchURI
	 */
	public String getLaunchURI() {
		return launchURI;
	}
	/**
	 * @param launchURI the launchURI to set
	 */
	public void setLaunchURI(String launchURI) {
		this.launchURI = launchURI;
	}
	/**
	 * @return the maximumTimeAllowedSeconds
	 */
	public Long getMaximumTimeAllowedSeconds() {
		return maximumTimeAllowedSeconds;
	}
	/**
	 * @param maximumTimeAllowedSeconds the maximumTimeAllowedSeconds to set
	 */
	public void setMaximumTimeAllowedSeconds(Long maximumTimeAllowedSeconds) {
		this.maximumTimeAllowedSeconds = maximumTimeAllowedSeconds;
	}
	/**
	 * @return the timeLimitAction
	 */
	public String getTimeLimitAction() {
		return timeLimitAction;
	}
	/**
	 * @param timeLimitAction the timeLimitAction to set
	 */
	public void setTimeLimitAction(String timeLimitAction) {
		this.timeLimitAction = timeLimitAction;
	}

	public Double getScaledPassingScore() {
		if(scaledPassingScore==null){
			scaledPassingScore=0.00;
		}
		return scaledPassingScore;
	}

	public void setScaledPassingScore(Double scaledPassingScore) {
		if(scaledPassingScore==null){
			this.scaledPassingScore=0.00;
		}
		else{
			this.scaledPassingScore = scaledPassingScore;
		}
	}
	/**
	 * @return the scoObjectives
	 */
	public List<SCOObjective> getScoObjectives() {
		return scoObjectives;
	}
	/**
	 * @param scoObjectives the scoObjectives to set
	 */
	public void setScoObjectives(ArrayList<SCOObjective> scoObjectives) {
		this.scoObjectives = scoObjectives;
	}
	public ScormCourse getCourse() {
		return (ScormCourse)course;
	}
	public void setCourse(ScormCourse course) {
		this.course = course;
	}
}
