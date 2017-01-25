package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "SCOINTERACTIONRESPONSE")
public class SCOInteractionResponse implements SearchableKey {

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "SCOINTERACTIONRESPONSE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SCOINTERACTIONRESPONSE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SCOINTERACTIONRESPONSE_ID")
	private Long id;
	
	@Column(name="SCOINTERACTIONRESPONSEID")
	private String scoInteractionResponseId = null;

	@Column(name="SCOINTERACTION_ID")
	private String scoInteractionId = null;

	@Column(name="PATTERN")
	private String pattern = null;
	
	@Column(name="TYPE")
	private String type = null;

	public String getScoInteractionId() {
		return scoInteractionId;
	}

	public void setScoInteractionId(String scoInteractionId) {
		this.scoInteractionId = scoInteractionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScoInteractionResponseId() {
		return scoInteractionResponseId;
	}

	public void setScoInteractionResponseId(String scoInteractionResponseId) {
		this.scoInteractionResponseId = scoInteractionResponseId;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
