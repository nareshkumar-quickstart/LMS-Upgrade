package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AdminLearnerEnrollmentSearch  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String firstName = null;
	private String lastName = null;
	private String eMail = null;
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param name the name to set
	 */
	public void setFirstName(String name) {
		this.firstName = name;
	}
	/**
	 * @return the eMail
	 */
	public String getEMail() {
		return eMail;
	}
	/**
	 * @param mail the eMail to set
	 */
	public void setEMail(String mail) {
		eMail = mail;
	}
	/**
	 * @return the status
	 */
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
