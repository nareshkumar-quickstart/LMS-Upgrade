package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class TrainingAdministrator implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean managesAllOrganizationalGroups = Boolean.TRUE;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return id.toString();
	}

	public  Boolean isManagesAllOrganizationalGroups() {
		return managesAllOrganizationalGroups;
	}

	public void setManagesAllOrganizationalGroups(
			 Boolean managesAllOrganizationalGroups) {
		this.managesAllOrganizationalGroups = managesAllOrganizationalGroups;
	}

}
