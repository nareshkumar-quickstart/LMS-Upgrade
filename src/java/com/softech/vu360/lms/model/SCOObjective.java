package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "SCOOBJECTIVE")
public class SCOObjective implements SearchableKey {
	
	@Id
	@javax.persistence.TableGenerator(name = "SCOOBJECTIVE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SCOOBJECTIVE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SCOOBJECTIVE_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "SCO_ID")
	private SCO sco ;
	
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@Column(name = "SCOOBJECTIVEID")
	private String scoObjectiveId = null;
	
		
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
	/**
	 * @return the searchableKey
	 */
	public String getKey() {
		return id.toString();
	}
	/**
	 * @return the sco
	 */
	public SCO getSco() {
		return this.sco;
	}
	/**
	 * @param sco the sco to set
	 */
	public void setSco(SCO sco) {
		this.sco = sco;
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
	 * @return the scoObjectiveId
	 */
	public String getScoObjectiveId() {
		return scoObjectiveId;
	}
	/**
	 * @param scoObjectiveId the scoObjectiveId to set
	 */
	public void setScoObjectiveId(String scoObjectiveId) {
		this.scoObjectiveId = scoObjectiveId;
	}
}
