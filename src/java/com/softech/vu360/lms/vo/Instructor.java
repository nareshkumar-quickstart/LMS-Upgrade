package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.List;

public class Instructor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	String areaOfExpertise = null;
	private String firstName = null;
	private String lastName = null;
	private Boolean active = Boolean.TRUE;
	private ContentOwner contentOwner;
	private List<CustomField> customfields;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CustomField> getCustomfields() {
		return customfields;
	}

	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}

	public String getAreaOfExpertise() {
		return areaOfExpertise;
	}

	public void setAreaOfExpertise(String areaOfExpertise) {
		this.areaOfExpertise = areaOfExpertise;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

}
