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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Parameter;

/**
 * @author Haider.ali
 * @date: 22-09-2015
 */


@Entity
@Table(name = "AICC_COURSEASIGNABLEUNIT")
public class AICCAssignableUnit implements SearchableKey {
	
	private static final long serialVersionUID = 1L;

	/*@Id
	@javax.persistence.TableGenerator(name = "AICC_COURSEASIGNABLEUNIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AICC_COURSEASIGNABLEUNIT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "AICC_COURSEASIGNABLEUNIT_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqAICCAssignableUnitId")
	@GenericGenerator(name = "seqAICCAssignableUnitId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	@Parameter(name = "table_name", value = "VU360_SEQ"),
	@Parameter(name = "value_column_name", value = "NEXT_ID"),
	@Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	@Parameter(name = "segment_value", value = "AICC_COURSEASIGNABLEUNIT") })
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURSE_ID")
	private Course course;
	
	@Column(name="SYSTEM_ID")
	private String systemID;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="COMMANDLINE")
	private String commandLine;
	
	@Nationalized
	@Column(name="FILENAME")
	private String fileName;
	
	@Column(name="MAXSCORE")
	private Double maxScore;
	
	@Column(name="MASTERYSCORE")
	private Double masteryScore;
	
	@Column(name="MAXTIMEALLOWED_MIN")
	private Integer maxTimeAllowedMin;
	
	@Column(name="TIMELIMITACTION")
	private String timeLimitAction;
	
	@Column(name="SYSTEMVENDOR")
	private String systemVendor;
	
	@Nationalized
	@Column(name="COREVENDOR")
	private String coreVendor;
	
	@Nationalized
	@Column(name="WEBLAUNCH")
	private String webLaunch;
	
	@Column(name="AUPASSWORD")
	private String auPassword;
	
	@Column(name="CREATEDDATE")
	private Date createdDate;
	
	@Column(name="CREATEDUSERID")
	private Integer createdUserId;
	
	@Column(name="LASTUPDATEDDATE")
	private Date lastUpdateDate;
	
	@Column(name="LASTUPDATEDUSER")
	private Integer lastUpdatedUser;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}


	public String getSystemID() {
		return systemID;
	}


	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCommandLine() {
		return commandLine;
	}


	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Double getMaxScore() {
		return maxScore;
	}


	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}


	public Double getMasteryScore() {
		return masteryScore;
	}


	public void setMasteryScore(Double masteryScore) {
		this.masteryScore = masteryScore;
	}


	public Integer getMaxTimeAllowedMin() {
		return maxTimeAllowedMin;
	}


	public void setMaxTimeAllowedMin(Integer maxTimeAllowedMin) {
		this.maxTimeAllowedMin = maxTimeAllowedMin;
	}


	public String getTimeLimitAction() {
		return timeLimitAction;
	}


	public void setTimeLimitAction(String timeLimitAction) {
		this.timeLimitAction = timeLimitAction;
	}


	public String getSystemVendor() {
		return systemVendor;
	}


	public void setSystemVendor(String systemVendor) {
		this.systemVendor = systemVendor;
	}


	public String getCoreVendor() {
		return coreVendor;
	}


	public void setCoreVendor(String coreVendor) {
		this.coreVendor = coreVendor;
	}


	public String getWebLaunch() {
		return webLaunch;
	}


	public void setWebLaunch(String webLaunch) {
		this.webLaunch = webLaunch;
	}


	public String getAuPassword() {
		return auPassword;
	}


	public void setAuPassword(String auPassword) {
		this.auPassword = auPassword;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getCreatedUserId() {
		return createdUserId;
	}


	public void setCreatedUserId(Integer createdUserId) {
		this.createdUserId = createdUserId;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public Integer getLastUpdatedUser() {
		return lastUpdatedUser;
	}


	public void setLastUpdatedUser(Integer lastUpdatedUser) {
		this.lastUpdatedUser = lastUpdatedUser;
	}


	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	

}
