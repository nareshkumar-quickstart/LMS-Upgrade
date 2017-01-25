package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class Learner implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private VU360User vu360User;
	private Customer customer ;
	private LearnerProfile learnerProfile;
	private LearnerPreferences preference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VU360User getVu360User() {
		return vu360User;
	}

	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LearnerProfile getLearnerProfile() {
		return learnerProfile;
	}

	public void setLearnerProfile(LearnerProfile learnerProfile) {
		this.learnerProfile = learnerProfile;
	}

	public LearnerPreferences getPreference() {
		return preference;
	}

	public void setPreference(LearnerPreferences preference) {
		this.preference = preference;
	}

	
}
