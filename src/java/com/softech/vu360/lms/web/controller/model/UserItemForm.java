package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class UserItemForm implements Comparable<UserItemForm>, ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;
	private boolean authorAssigned = false;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	private VU360User user;

	public VU360User getUser() {
		return user;
	}
	public void setUser(VU360User user) {
		this.user = user;
	}

	public int compareTo(UserItemForm other) {
		return (this.user.getId()).compareTo(other.getUser().getId());
	}
	
	public boolean isAuthorAssigned() {
		return authorAssigned;
	}
	public void setAuthorAssigned(boolean authorAssigned) {
		this.authorAssigned = authorAssigned;
	}
	
	
	
}