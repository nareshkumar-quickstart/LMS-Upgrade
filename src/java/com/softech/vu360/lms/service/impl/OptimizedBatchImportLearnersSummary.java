package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.util.Brander;

public class OptimizedBatchImportLearnersSummary {

	private int totalRecordCount;
	private int totalImportedUsers;
	private int totalUpdatedUsers;
	private int recordNumber;
	private List<Learner> addedLearners = new ArrayList<Learner>();
	private boolean nullFile;
	private List<String> passwords = new ArrayList<String>();
	List<OptimizedBatchImportLearnersErrors> batchImportErrorsList = new ArrayList<OptimizedBatchImportLearnersErrors>();
	private boolean updateExistingUser;
	private Brander brander;
	
	public int getTotalRecordCount() {
		return totalRecordCount;
	}
	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	public int getTotalImportedUsers() {
		return totalImportedUsers;
	}
	public void setTotalImportedUsers(int totalImportedUsers) {
		this.totalImportedUsers = totalImportedUsers;
	}
	public int getTotalUpdatedUsers() {
		return totalUpdatedUsers;
	}
	public void setTotalUpdatedUsers(int totalUpdatedUsers) {
		this.totalUpdatedUsers = totalUpdatedUsers;
	}
	public int getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public List<Learner> getAddedLearners() {
		return addedLearners;
	}
	public void setAddedLearners(List<Learner> addedLearners) {
		this.addedLearners = addedLearners;
	}
	public boolean isNullFile() {
		return nullFile;
	}
	public void setNullFile(boolean nullFile) {
		this.nullFile = nullFile;
	}
	public List<String> getPasswords() {
		return passwords;
	}
	public void setPasswords(List<String> passwords) {
		this.passwords = passwords;
	}
	public List<OptimizedBatchImportLearnersErrors> getBatchImportErrorsList() {
		return batchImportErrorsList;
	}
	public void setBatchImportErrorsList(
			List<OptimizedBatchImportLearnersErrors> batchImportErrorsList) {
		this.batchImportErrorsList = batchImportErrorsList;
	}	
	public boolean isUpdateExistingUser() {
		return updateExistingUser;
	}
	public void setUpdateExistingUser(boolean updateExistingUser) {
		this.updateExistingUser = updateExistingUser;
	}
	public Brander getBrander() {
		return brander;
	}
	public void setBrander(Brander brander) {
		this.brander = brander;
	}
	
	
}
