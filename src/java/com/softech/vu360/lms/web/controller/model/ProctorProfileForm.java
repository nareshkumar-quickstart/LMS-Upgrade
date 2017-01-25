/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author syed.mahmood
 *
 */
public class ProctorProfileForm  implements ILMSBaseInterface{
	
	private static final long serialVersionUID = 1L;
	private long id;
	private String proctorId;
	private String username;
	private String currentPassword = null;
	private String newPassword = null;
	private String confirmPassword = null;
	private String saved = "false";
	
	private Proctor proctor = null;
	
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the proctorId
	 */
	public String getProctorId() {
		return proctorId;
	}
	/**
	 * @param proctorId the proctorId to set
	 */
	public void setProctorId(String proctorId) {
		this.proctorId = proctorId;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @param currentPassword the currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	/**
	 * @return the currentPassword
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}
	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	/**
	 * @return the saved
	 */
	public String getSaved() {
		return saved;
	}
	/**
	 * @param saved the saved to set
	 */
	public void setSaved(String saved) {
		this.saved = saved;
	}
	/**
	 * @return the proctor
	 */
	public Proctor getProctor() {
		return proctor;
	}
	/**
	 * @param proctor the proctor to set
	 */
	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}
	
	

}
