package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AUDITEVENT")
public class AuditEvent  implements ILMSBaseInterface{

	public static final String EVENT_CREATED = "created";
	public static final String EVENT_MODIFIED = "modified";
	public static final String EVENT_DELETED = "deleted";

	@Id
    @javax.persistence.TableGenerator(name = "AUDITEVENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AUDITEVENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AUDITEVENT_ID")
	private Long id;
	
	@Transient
	private String userId;
	@Transient
	private String objectId;
	@Transient
	private Date eventDate;
	@Transient
	private String event = EVENT_CREATED;

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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate
	 *            the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}
}
