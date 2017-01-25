package com.softech.vu360.lms.model;

public class Recipient {

	private String recipGroupName="";
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRecipGroupName() {
		return recipGroupName;
	}
	public void setRecipGroupName(String recipGroupName) {
		this.recipGroupName = recipGroupName;
	}
	public String getRecipGroupType() {
		return recipGroupType;
	}
	public void setRecipGroupType(String recipGroupType) {
		this.recipGroupType = recipGroupType;
	}
	private String recipGroupType="";




}
