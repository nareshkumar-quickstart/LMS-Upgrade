package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ChangeUsernamePasswordForm implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;

	private String email = "";
	private String firstName = "";
	private String lastName = "";
	
	// User can select 'Username' or 'Email Address' option type to Retrieve his forgot Password
	private String forgetPasswordSelectionType = ""; 
	private String username = "";
	private String forgetTypeSelection = "";
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getForgetTypeSelection() {
		return forgetTypeSelection;
	}

	public void setForgetTypeSelection(String forgetTypeSelection) {
		this.forgetTypeSelection = forgetTypeSelection;
	}

	public String getForgetPasswordSelectionType() {
		return forgetPasswordSelectionType;
	}

	public void setForgetPasswordSelectionType(String forgetPasswordSelectionType) {
		this.forgetPasswordSelectionType = forgetPasswordSelectionType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
