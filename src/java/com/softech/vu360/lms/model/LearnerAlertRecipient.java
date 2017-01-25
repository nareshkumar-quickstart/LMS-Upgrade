package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
@DiscriminatorValue("LearnerAlertRecipient")
public class LearnerAlertRecipient extends AlertRecipient {
	
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
    @JoinTable(name="ALERTRECIPIENT_LEARNER", joinColumns = @JoinColumn(name="ALERTRECIPIENT_ID"),inverseJoinColumns = @JoinColumn(name="LEARNER_ID"))
	private List<Learner> learners = new ArrayList<Learner>();

	public List<Learner> getLearners() {
		return learners;
	}

	public void setLearners(List<Learner> learners) {
		this.learners = learners;
	}

	

}
