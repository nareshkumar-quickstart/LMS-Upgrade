package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AUDIT_COURSESTATUSCHANGE")
public class AuditCourseStatus  implements ILMSBaseInterface {

	public static final String EVENT_CREATED = "created";
	public static final String EVENT_MODIFIED = "modified";
	public static final String EVENT_DELETED = "deleted";
	
	@Id
    @javax.persistence.TableGenerator(name = "AUDIT_COURSESTATUSCHANGE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AUDIT_COURSESTATUSCHANGE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AUDIT_COURSESTATUSCHANGE_ID")
	private Long id;
	
	@Column(name = "CREATEDVU360USER_ID")
	private Long userId;
	
	@Column(name = "LEARNERENROLLMENT_ID")
	private Long learnerEnrollmentId;
	
	@Column(name = "CREATEDATE")
	private Date createdDate;
	
	@Column(name = "PREVIOUS_STATUS")
	private String previousStatus;
	
	@Column(name = "NEW_STATUS")
	private String currentStatus;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}
	public void setLearnerEnrollmentId(Long learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
}
