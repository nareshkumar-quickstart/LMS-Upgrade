package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 * 
 * @author muhammad.rehan
 *
 */

public class OrganizationalGroupMemberPK implements Serializable{

	private static final long serialVersionUID = -7391669718530720244L;
	private Learner learner;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((learner == null) ? 0 : learner.hashCode());
		result = prime * result + ((organizationalGroup == null) ? 0 : organizationalGroup.hashCode());
		return result;
	}

	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganizationalGroupMemberPK other = (OrganizationalGroupMemberPK) obj;
		if (learner == null) {
			if (other.learner != null)
				return false;
		} else if (!learner.equals(other.learner))
			return false;
		if (organizationalGroup == null) {
			if (other.organizationalGroup != null)
				return false;
		} else if (!organizationalGroup.equals(other.organizationalGroup))
			return false;
		return true;
	}
}
