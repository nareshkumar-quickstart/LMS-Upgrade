package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegulatoryAnalyst implements Serializable {
	private static final long serialVersionUID = 6029188228657344907L;

	private Long id;
	private Boolean isForAllContentOwner = Boolean.TRUE;
	private List<ContentOwner> contentOwners = new ArrayList<ContentOwner>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isForAllContentOwner() {
		return isForAllContentOwner;
	}

	public void setForAllContentOwner(Boolean isForAllContentOwner) {
		this.isForAllContentOwner = isForAllContentOwner;
	}

	public List<ContentOwner> getContentOwners() {
		return contentOwners;
	}

	public void setContentOwners(List<ContentOwner> contentOwners) {
		this.contentOwners = contentOwners;
	}
	
}
