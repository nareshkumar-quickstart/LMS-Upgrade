package com.softech.vu360.lms.model;

import java.util.ArrayList;
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
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "SCOINTERACTION")
public class SCOInteraction implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SCOINTERACTION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SCOINTERACTION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SCOINTERACTION_ID")
	private Long id;
	@Column(name="DESCRIPTION")
	private String description = null;
	@Column(name="INTERACTIONID")
	private String interactionId = null;
	@Column(name="LATENCY")
	private String latency = null;
	@Column(name="LEARNERRESPONSE")
	private String learnerResponse = null;
	@Column(name="RESULT")
	private String result = null;
	@Column(name="TIME")
	private String time = null;
	@Column(name="TYPE")
	private String type = null;
	@Column(name="WEIGHTING")
	private double weighting;

	@OneToOne
	@JoinColumn(name = "LEARNERSCOASSESSMENT_ID")
	private LearnerSCOAssessment learnerSCOAssessment;
	
	@OneToMany(mappedBy = "scoInteractionId" )
    private List<SCOInteractionResponse> interactionResponses = new ArrayList<SCOInteractionResponse>();

	@OneToMany(mappedBy = "scoInteractionId" )
    private List<SCOInteractionObjective> interactionObjectives = new ArrayList<SCOInteractionObjective>();
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInteractionId() {
		return interactionId;
	}
	public void setInteractionId(String interactionId) {
		this.interactionId = interactionId;
	}
	public String getLatency() {
		return latency;
	}
	public void setLatency(String latency) {
		this.latency = latency;
	}
	public String getLearnerResponse() {
		return learnerResponse;
	}
	public void setLearnerResponse(String learnerResponse) {
		this.learnerResponse = learnerResponse;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getWeighting() {
		return weighting;
	}
	public void setWeighting(double weighting) {
		this.weighting = weighting;
	}
	public LearnerSCOAssessment getLearnerSCOAssessment() {
		return learnerSCOAssessment;
	}
	public void setLearnerSCOAssessment(LearnerSCOAssessment learnerSCOAssessment) {
		this.learnerSCOAssessment = learnerSCOAssessment;
	}
	public List<SCOInteractionResponse> getInteractionResponses() {
		return interactionResponses;
	}
	public void setInteractionResponses(
			List<SCOInteractionResponse> interactionResponses) {
		this.interactionResponses = interactionResponses;
	}
	public List<SCOInteractionObjective> getInteractionObjectives() {
		return interactionObjectives;
	}
	public void setInteractionObjectives(
			List<SCOInteractionObjective> interactionObjectives) {
		this.interactionObjectives = interactionObjectives;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		return "SCOInteraction [id=" + id + ", description=" + description
				+ ", interactionId=" + interactionId + ", latency=" + latency
				+ ", learnerResponse=" + learnerResponse + ", result=" + result
				+ ", time=" + time + ", type=" + type + ", weighting="
				+ weighting + ", learnerSCOAssessment=" + learnerSCOAssessment
				+ ", interactionResponses=" + interactionResponses
				+ ", interactionObjectives=" + interactionObjectives + "]";
	}
	
}
