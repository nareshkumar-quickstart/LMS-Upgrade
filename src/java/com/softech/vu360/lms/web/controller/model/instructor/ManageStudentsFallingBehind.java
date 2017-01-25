package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageStudentsFallingBehind implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private VU360User user;
	private String days;
	
	
	/**
	 * @return the user
	 */
	public VU360User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(VU360User user) {
		this.user = user;
	}
	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}

}
