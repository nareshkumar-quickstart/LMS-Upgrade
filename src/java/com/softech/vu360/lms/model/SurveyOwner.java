package com.softech.vu360.lms.model;

import java.io.Serializable;

public interface SurveyOwner extends Serializable {
	
	public Long getId();
	public void setId(Long id);

	public String getOwnerType();
	public void setOwnerType(String ownerType);

}
