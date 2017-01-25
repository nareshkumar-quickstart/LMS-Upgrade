package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ViewLearnerEnrollmentItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private boolean ready = false;
	private LearnerEnrollment learnerEnrollment= null;
	private String newEnrollmentEndDate = null;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public LearnerEnrollment getLearnerEnrollment() {
		return learnerEnrollment;
	}
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}
	public String getNewEnrollmentEndDate() {
		return newEnrollmentEndDate;
	}
	public void setNewEnrollmentEndDate(String newEnrollmentEndDate) {
		this.newEnrollmentEndDate = newEnrollmentEndDate;
	}
	public boolean getReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
}