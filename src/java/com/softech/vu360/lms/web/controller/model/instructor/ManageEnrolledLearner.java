package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageEnrolledLearner implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private VU360User user;
	private LearnerEnrollment learnerEnrollment;
	
	
	/**
	 * @return the learnerEnrollment
	 */
	public LearnerEnrollment getLearnerEnrollment() {
		return learnerEnrollment;
	}
	/**
	 * @param learnerEnrollment the learnerEnrollment to set
	 */
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}
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
	
	

}
