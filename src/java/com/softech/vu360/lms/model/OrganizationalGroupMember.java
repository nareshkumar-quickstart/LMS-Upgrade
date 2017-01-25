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
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LEARNER_ORGANIZATIONALGROUP")
@IdClass(OrganizationalGroupMemberPK.class)
public class OrganizationalGroupMember implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNER_ID" )
	private Learner learner ;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORGANIZATIONALGROUP_ID" )
	private OrganizationalGroup organizationalGroup ;

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public OrganizationalGroup getOrganizationalGroup() {
		return organizationalGroup;
	}

	public void setOrganizationalGroup(OrganizationalGroup organizationalGroup) {
		this.organizationalGroup = organizationalGroup;
	}
}
