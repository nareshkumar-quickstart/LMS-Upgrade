package com.softech.vu360.lms.model;

import javax.persistence.Embeddable;

@Embeddable
public class Owner implements SurveyOwner {

	private static final long serialVersionUID = -978772640016398591L;
	
	private Long id;
	private String ownerType;
	
	@Override
	public Long getId() {
		return this.id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getOwnerType() {
		return this.ownerType;
	}
	@Override
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
}