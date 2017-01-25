package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class InsSearchMember  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String firstName = null;
	private String lastName = null;
	private String eMail = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEMail() {
		return eMail;
	}
	public void setEMail(String mail) {
		eMail = mail;
	}
}