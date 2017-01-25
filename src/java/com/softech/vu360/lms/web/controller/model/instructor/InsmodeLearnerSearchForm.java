package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField;
import com.softech.vu360.lms.web.controller.model.customfield.CustomField;

public class InsmodeLearnerSearchForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String searchFirstName = "";
	private String searchLastName = "";
	private String searchEmailAddress = "";
	private List<CreditReportingField> creditReportingFields = new ArrayList<CreditReportingField>();
	private List<CustomField> customFields = new ArrayList<CustomField>();
	List<CourseCourseStatisticsPair> results = new ArrayList<CourseCourseStatisticsPair>();
	
	List<InsSearchMember> insSearchLernerList = null;
	private VU360User user = null;
	private long learnerId = 0;

	public String getSearchFirstName() {
		return searchFirstName;
	}
	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}
	public String getSearchLastName() {
		return searchLastName;
	}
	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}
	public String getSearchEmailAddress() {
		return searchEmailAddress;
	}
	public void setSearchEmailAddress(String searchEmailAddress) {
		this.searchEmailAddress = searchEmailAddress;
	}
	public List<InsSearchMember> getInsSearchLernerList() {
		return insSearchLernerList;
	}
	public void setInsSearchLernerList(List<InsSearchMember> insSearchLernerList) {
		this.insSearchLernerList = insSearchLernerList;
	}
	public VU360User getUser() {
		return user;
	}
	public void setUser(VU360User user) {
		this.user = user;
	}
	public List<CreditReportingField> getCreditReportingFields() {
		return creditReportingFields;
	}
	public void setCreditReportingFields(
			List<CreditReportingField> creditReportingFields) {
		this.creditReportingFields = creditReportingFields;
	}
	public List<CustomField> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}
	public List<CourseCourseStatisticsPair> getResults() {
		return results;
	}
	public void setResults(List<CourseCourseStatisticsPair> results) {
		this.results = results;
	}
	/**
	 * @return the learnerId
	 */
	public long getLearnerId() {
		return learnerId;
	}
	/**
	 * @param learnerId the learnerId to set
	 */
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}
}