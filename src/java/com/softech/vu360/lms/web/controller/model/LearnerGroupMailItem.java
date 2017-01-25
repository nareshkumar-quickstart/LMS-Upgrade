package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerGroupMailItem implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private boolean isSelected;
	private String learnerGroupName;
	private Long learnerGroupId;
	
	public LearnerGroupMailItem() {
		super();
	}
	
	public Long getLearnerGroupId() {
		return learnerGroupId;
	}
	public void setLearnerGroupId(Long learnerGroupId) {
		this.learnerGroupId = learnerGroupId;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getLearnerGroupName() {
		return learnerGroupName;
	}
	public void setLearnerGroupName(String learnerGroupName) {
		this.learnerGroupName = learnerGroupName;
	}
	
}