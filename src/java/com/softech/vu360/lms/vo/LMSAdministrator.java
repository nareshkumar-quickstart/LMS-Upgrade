package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class LMSAdministrator implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean globalAdministrator = Boolean.FALSE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isGlobalAdministrator() {
		return globalAdministrator;
	}

	public void setGlobalAdministrator(Boolean globalAdministrator) {
		this.globalAdministrator = globalAdministrator;
	}

}