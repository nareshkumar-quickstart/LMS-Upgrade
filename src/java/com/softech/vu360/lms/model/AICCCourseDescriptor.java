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
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AICC_COURSEDESCRIPTOR")
public class AICCCourseDescriptor implements SearchableKey {
	
	private static final long serialVersionUID = 6246235642432526183L;

	/*@Id
    @javax.persistence.TableGenerator(name = "AICC_COURSEDESCRIPTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AICC_COURSEDESCRIPTOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AICC_COURSEDESCRIPTOR_ID")
	private Long id;*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqAICCCourseDescriptorId")
	@GenericGenerator(name = "seqAICCCourseDescriptorId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	@Parameter(name = "table_name", value = "VU360_SEQ"),
	@Parameter(name = "value_column_name", value = "NEXT_ID"),
	@Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	@Parameter(name = "segment_value", value = "AICC_COURSEDESCRIPTOR") })
	private Long id;
	
	@OneToOne
    @JoinColumn(name="COURSE_ID")
	private Course course;
	
	@Column(name = "SYSTEM_ID")
	private String systemId;
	
	@Column(name = "DEVELOPER_ID")
	private String developerId;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
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
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String getKey() {
		return id.toString();
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
	 * @return the developerId
	 */
	public String getDeveloperId() {
		return developerId;
	}
	/**
	 * @param developerId the developerId to set
	 */
	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
