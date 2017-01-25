package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@DiscriminatorValue("LearnerAlertFilter")
public class LearnerAlertFilter extends AlertTriggerFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
    @JoinTable(name="ALERTTRIGGERFILTER_LEARNER", joinColumns = @JoinColumn(name="ALERTTRIGGERFILTER_ID"),inverseJoinColumns = @JoinColumn(name="LEARNER_ID"))
    private List<Learner> learner = new ArrayList<Learner>();

	public List<Learner> getLearners() {
		return learner;
	}

	public void setLearners(List<Learner> learner) {
		this.learner = learner;
	}

}
