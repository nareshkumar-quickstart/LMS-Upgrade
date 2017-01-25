package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 *
 *@author kaunain.wajeeh
 * 
 */

@Entity
@Table(name = "MESSAGETEMPLATE")
public class MessageTemplate {
	
	
	
	//@Kaunain - Discussed with Saleem, I could not find methods which perform insert in the same table, hence sequence needs to be inserted manually
	/*This table is probably used for dynamic fetching from the table, though records are manually inserted*/
	//@javax.persistence.TableGenerator(name = "MESSAGETEMPLATE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "MESSAGETEMPLATE", allocationSize = 1)
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "MESSAGETEMPLATE_ID")
	@Id
	@Column(name="ID")
	private long id;
	
	@Column(name = "BODY")
	private String body;
	
	@Column(name = "SUBJECTLINE")
	private String subjectLine;
	
	@Column(name = "EVENTTYPE")
	private String eventType;
	
	@Column(name = "TRIGGERTYPE")
	private String triggerType;
	
	
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getSubjectLine() {
		return subjectLine;
	}
	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
