package com.softech.vu360.lms.rest.model;

import java.util.List;

public class LearnerProfile {
	
	private PersonalInformation personalInformation;
	private List<ValidationQuestionSet> validationQuestions;
	private List<LearnerCreditReportingField> customFields;
	private List<LearnerCreditReportingField> reportingFields;

	public PersonalInformation getPersonalInformation() {
		return personalInformation;
	}
	public void setPersonalInformation(PersonalInformation personalInformation) {
		this.personalInformation = personalInformation;
	}
	public List<ValidationQuestionSet> getValidationQuestions() {
		return validationQuestions;
	}
	public void setValidationQuestions(
			List<ValidationQuestionSet> validationQuestions) {
		this.validationQuestions = validationQuestions;
	}
	public List<LearnerCreditReportingField> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(List<LearnerCreditReportingField> customFields) {
		this.customFields = customFields;
	}
	public List<LearnerCreditReportingField> getReportingFields() {
		return reportingFields;
	}
	public void setReportingFields(List<LearnerCreditReportingField> reportingFields) {
		this.reportingFields = reportingFields;
	}
}
