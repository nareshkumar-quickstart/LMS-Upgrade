package com.softech.vu360.lms.rest.model;

public class LearnerProfileTimeZone {
	
	private String value = "0";
	private String text = "";
	private Boolean selected = false;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}
