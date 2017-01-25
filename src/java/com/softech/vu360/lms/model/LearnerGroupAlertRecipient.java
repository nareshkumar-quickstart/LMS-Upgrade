package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("LearnerGroupAlertRecipient")
public class LearnerGroupAlertRecipient extends AlertRecipient{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
	@JoinTable(name="ALERTRECIPIENT_LEARNERGROUP", joinColumns = @JoinColumn(name="ALERTRECIPIENT_ID"),inverseJoinColumns = @JoinColumn(name="LEARNERGROUP_ID"))
	private List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();

	public List<LearnerGroup> getLearnerGroups() {
		return learnerGroups;
	}

	public void setLearnerGroups(List<LearnerGroup> learnerGroups) {
		this.learnerGroups = learnerGroups;
	}
	

	

}
