package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@DiscriminatorValue("LearnerGroupAlertFilter")
public class LearnerGroupAlertFilter extends AlertTriggerFilter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
    @JoinTable(name="ALERTTRIGGERFILTER_LEARNERGROUP", joinColumns = @JoinColumn(name="ALERTTRIGGERFILTER_ID"),inverseJoinColumns = @JoinColumn(name="LEARNERGROUP_ID"))
    private List<LearnerGroup> learnergroup = new ArrayList<LearnerGroup>();

	public List<LearnerGroup> getLearnerGroup() {
		return learnergroup;
	}

	public void setLearnerGroup(List<LearnerGroup> learnergroup) {
		this.learnergroup = learnergroup;
	}

	
}
