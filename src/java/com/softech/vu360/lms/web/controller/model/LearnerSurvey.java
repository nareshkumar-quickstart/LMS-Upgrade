package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerSurvey  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private VU360User user = null;
	private int totalSurvey = 0;
	private int completedSurvey = 0;
	
	
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
	 * @return the totalSurvey
	 */
	public int getTotalSurvey() {
		return totalSurvey;
	}
	/**
	 * @param totalSurvey the totalSurvey to set
	 */
	public void setTotalSurvey(int totalSurvey) {
		this.totalSurvey = totalSurvey;
	}
	/**
	 * @return the completedSurvey
	 */
	public int getCompletedSurvey() {
		return completedSurvey;
	}
	/**
	 * @param completedSurvey the completedSurvey to set
	 */
	public void setCompletedSurvey(int completedSurvey) {
		this.completedSurvey = completedSurvey;
	}
	
}
