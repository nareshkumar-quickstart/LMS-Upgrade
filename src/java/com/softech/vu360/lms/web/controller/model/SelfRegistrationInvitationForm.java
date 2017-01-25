/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.List;

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

/**
 * @author Tathya
 *
 */
public class SelfRegistrationInvitationForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public SelfRegistrationInvitationForm() {
		// TODO Auto-generated constructor stub
	}
	private String[] groups;
	private String invitationName;
	private String passCode;
	private boolean isRegistrationUnlimited = true;
	private String maximumLimitedRegistration;
	private String[] availableLearnerGroups;
	private String[] selectedLearnerGroups;
	private String message;
	
	//for edit registration invitation.
	private long id = 0;  
	private List<TreeNode> treeAsList = null;
	private List<LearnerGroup> availableLernerGroupList = null;
	private List<LearnerGroup> selectedLearnersGroupList = null;

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String getInvitationName() {
		return invitationName;
	}

	public void setInvitationName(String invitationName) {
		this.invitationName = invitationName;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public boolean isRegistrationUnlimited() {
		return isRegistrationUnlimited;
	}

	public void setRegistrationUnlimited(boolean isRegistrationUnlimited) {
		this.isRegistrationUnlimited = isRegistrationUnlimited;
	}

	public String getMaximumLimitedRegistration() {
		return maximumLimitedRegistration;
	}

	public void setMaximumLimitedRegistration(String maximumLimitedRegistration) {
		this.maximumLimitedRegistration = maximumLimitedRegistration;
	}

	public String[] getAvailableLearnerGroups() {
		return availableLearnerGroups;
	}

	public void setAvailableLearnerGroups(String[] availableLearnerGroups) {
		this.availableLearnerGroups = availableLearnerGroups;
	}

	public String[] getSelectedLearnerGroups() {
		return selectedLearnerGroups;
	}

	public void setSelectedLearnerGroups(String[] selectedLearnerGroups) {
		this.selectedLearnerGroups = selectedLearnerGroups;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

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
	 * @return the treeAsList
	 */
	public List<TreeNode> getTreeAsList() {
		return treeAsList;
	}

	/**
	 * @param treeAsList the treeAsList to set
	 */
	public void setTreeAsList(List<TreeNode> treeAsList) {
		this.treeAsList = treeAsList;
	}

	/**
	 * @return the availableLernerGroupList
	 */
	public List<LearnerGroup> getAvailableLernerGroupList() {
		return availableLernerGroupList;
	}

	/**
	 * @param availableLernerGroupList the availableLernerGroupList to set
	 */
	public void setAvailableLernerGroupList(
			List<LearnerGroup> availableLernerGroupList) {
		this.availableLernerGroupList = availableLernerGroupList;
	}

	/**
	 * @return the selectedLearnersGroupList
	 */
	public List<LearnerGroup> getSelectedLearnersGroupList() {
		return selectedLearnersGroupList;
	}

	/**
	 * @param selectedLearnersGroupList the selectedLearnersGroupList to set
	 */
	public void setSelectedLearnersGroupList(
			List<LearnerGroup> selectedLearnersGroupList) {
		this.selectedLearnersGroupList = selectedLearnersGroupList;
	}

}
