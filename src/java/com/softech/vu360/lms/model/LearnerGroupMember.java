package com.softech.vu360.lms.model;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNER_LEARNERGROUP")
@IdClass(LearnerGroupMemberPK.class)
public class LearnerGroupMember implements Serializable{

	private static final long serialVersionUID = -2407636994915445584L;
	
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNER_ID" )
	private Learner learner ;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNERGROUP_ID" )
	private LearnerGroup learnerGroup ;
	
	public LearnerGroupMember(){
		this.learner = new Learner();
		this.learnerGroup = new LearnerGroup();
	}
	
	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}

	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	/**
	 * @return the learnerGroup
	 */
	public LearnerGroup getLearnerGroup() {
		return learnerGroup;
	}

	/**
	 * @param learnerGroup the learnerGroup to set
	 */
	public void setLearnerGroup(LearnerGroup learnerGroup) {
		this.learnerGroup = learnerGroup;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LearnerGroupMember [learner=" + learner + ", learnerGroup="
				+ learnerGroup + "]";
	}
	
}
