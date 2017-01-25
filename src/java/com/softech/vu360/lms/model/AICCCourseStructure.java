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

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AICC_COURSESTRUCTURE")
public class AICCCourseStructure implements SearchableKey {
	
	private static final long serialVersionUID = -935010010062816629L;

	@Id
    @javax.persistence.TableGenerator(name = "AICC_COURSESTRUCTURE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AICC_COURSESTRUCTURE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AICC_COURSESTRUCTURE_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="COURSE_ID")
	private Course course;
	
	@Column(name = "SYSTEM_ID")
	private String systemId;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate;
	
	@Column(name = "CREATEDUSERID")
	private Integer createdUserId;
	
	@Column(name = "LASTUPDATEDDATE")
	private Date lastUpdatedDate;
	
	@Column(name = "LASTUPDATEDUSER")
	private Integer lastUpdatedUser;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	public String getKey() {
		return id.toString();
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the courseId
	 */
	public Course getCourseId() {
		return course;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Course course) {
		this.course = course;
	}
	/**
	 * @return the systemId
	 */
	public String getSystemId() {
		return systemId;
	}
	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	/**
	 * @return the createdDate
	 */
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
	 * @return the createdUserId
	 */
	public Integer getCreatedUserId() {
		return createdUserId;
	}
	/**
	 * @param createdUserId the createdUserId to set
	 */
	public void setCreatedUserId(Integer createdUserId) {
		this.createdUserId = createdUserId;
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
	 * @return the lastUpdatedUser
	 */
	public Integer getLastUpdatedUser() {
		return lastUpdatedUser;
	}
	/**
	 * @param lastUpdatedUser the lastUpdatedUser to set
	 */
	public void setLastUpdatedUser(Integer lastUpdatedUser) {
		this.lastUpdatedUser = lastUpdatedUser;
	}
	

}
