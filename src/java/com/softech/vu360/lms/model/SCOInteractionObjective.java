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
@Table(name = "SCOINTERACTIONOBJECTIVE")
public class SCOInteractionObjective implements SearchableKey {

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "SCOINTERACTIONOBJECTIVE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SCOINTERACTIONOBJECTIVE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SCOINTERACTIONOBJECTIVE_ID")
	private Long id;
	
	@Column(name="SCOINTERACTIONOBJECTIVEID")
	private String scoInteractionObjectiveId = null;

	@Column(name="SCOINTERACTION_ID")
	private String scoInteractionId = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScoInteractionObjectiveId() {
		return scoInteractionObjectiveId;
	}

	public void setScoInteractionObjectiveId(String scoInteractionObjectiveId) {
		this.scoInteractionObjectiveId = scoInteractionObjectiveId;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getScoInteractionId() {
		return scoInteractionId;
	}

	public void setScoInteractionId(String scoInteractionId) {
		this.scoInteractionId = scoInteractionId;
	}
	
	
}
