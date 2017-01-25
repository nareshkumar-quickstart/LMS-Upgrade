package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.CourseApprovalVO;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.customfield.CustomField;

public class CreditReportingForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>();
	private VU360User vu360User;
	String learnerEnrollmentId = "";
	// merged from 4.7.2
	private List<CourseApprovalVO> courseApproval = new ArrayList<CourseApprovalVO>();
	private List<CustomField> missingCustomFields = new ArrayList<CustomField>();
	private List<com.softech.vu360.lms.model.CreditReportingField> missingCreditReprotingFields = new ArrayList<com.softech.vu360.lms.model.CreditReportingField>();
	private List<CustomFieldValue> customFieldValueList = new ArrayList<CustomFieldValue>(); // for
																								// add
																								// CustomField
	//LMS-15942 
	private String jurisdictionSuggestionYN=org.apache.commons.lang.StringUtils.EMPTY;
	private String showAllRegulators=org.apache.commons.lang.StringUtils.EMPTY;
	private String suggestedCourseApprovalFound=org.apache.commons.lang.StringUtils.EMPTY;
	private String noneSuggestedCourseApprovalFound=org.apache.commons.lang.StringUtils.EMPTY;

	public String getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}

	public void setLearnerEnrollmentId(String learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}
	
	HashMap<String, Integer> fileDetails = new HashMap<String, Integer>();
	
	String homeWorkAssignmentdocid = null;
	List<Document> lstHomeWorkAssignment = new ArrayList<Document>();
	String noOfHomeworkAssignmentuploaded;
	String assignmentDueDate;
	String fileName;
	String courseId = "";
	String source = "";
	//String externalLMSSessionID;
	String externallmssessionid;
	String externallmsurl;
	//String externalLMSUrl;
	String learningSessionId;
	String learnerEmailAddress;
	String instructorEmailAddresses;
	String learnerEmailSubject;
	String message;
	String courseName = "";
	
	int lmsProvider;
	String courseApprovalId;
	
	//LMS-15942 
	public String findMoreInfoLink=StringUtils.EMPTY;
	public String findMoreInfoTxt=StringUtils.EMPTY;
	
	/**
	 * @return the lmsProvider
	 */
	public int getLmsProvider() {
		return lmsProvider;
	}

	/**
	 * @param lmsProvider
	 *            the lmsProvider to set
	 */
	public void setLmsProvider(int lmsProvider) {
		this.lmsProvider = lmsProvider;
	}

	public List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> getCreditReportingFields() {
		return creditReportingFields;
	}

	public void setCreditReportingFields(
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields) {
		this.creditReportingFields = creditReportingFields;
	}

	public VU360User getVu360User() {
		return vu360User;
	}

	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/*
	public String getExternalLMSSessionID() {
		return externalLMSSessionID;
	}

	public void setExternalLMSSessionID(String externalLMSSessionID) {
		this.externalLMSSessionID = externalLMSSessionID;
	}

	public String getExternalLMSUrl() {
		return externalLMSUrl;
	}

	public void setExternalLMSUrl(String externalLMSUrl) {
		this.externalLMSUrl = externalLMSUrl;
	}
	*/


	public String getexternallmssessionid() {
		return externallmssessionid;
	}

	public void setexternallmssessionid(String externallmssessionid) {
		this.externallmssessionid = externallmssessionid;
	}

	public String getexternallmsurl() {
		return externallmsurl;
	}

	public void setexternallmsurl(String externallmsurl) {
		this.externallmsurl = externallmsurl;
	}

	public String getLearningSessionId() {
		return learningSessionId;
	}

	public void setLearningSessionId(String learningSessionId) {
		this.learningSessionId = learningSessionId;
	}

	public List<CustomField> getMissingCustomFields() {
		return missingCustomFields;
	}

	public void setMissingCustomFields(List<CustomField> missingCustomFields) {
		this.missingCustomFields = missingCustomFields;
	}

	public List<CustomFieldValue> getCustomFieldValueList() {
		return customFieldValueList;
	}

	public void setCustomFieldValueList(
			List<CustomFieldValue> customFieldValueList) {
		this.customFieldValueList = customFieldValueList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAssignmentDueDate() {
		return assignmentDueDate;
	}

	public void setAssignmentDueDate(String assignmentDueDate) {
		this.assignmentDueDate = assignmentDueDate;
	}

	public String getNoOfHomeworkAssignmentuploaded() {
		return noOfHomeworkAssignmentuploaded;
	}

	public void setNoOfHomeworkAssignmentuploaded(
			String noOfHomeworkAssignmentuploaded) {
		this.noOfHomeworkAssignmentuploaded = noOfHomeworkAssignmentuploaded;
	}

	/**
	 * @return the learnerEmailAddress
	 */
	public String getLearnerEmailAddress() {
		return learnerEmailAddress;
	}

	/**
	 * @param learnerEmailAddress the learnerEmailAddress to set
	 */
	public void setLearnerEmailAddress(String learnerEmailAddress) {
		this.learnerEmailAddress = learnerEmailAddress;
	}

	/**
	 * @return the learnerEmailSubject
	 */
	public String getLearnerEmailSubject() {
		return learnerEmailSubject;
	}

	/**
	 * @param learnerEmailSubject the learnerEmailSubject to set
	 */
	public void setLearnerEmailSubject(String learnerEmailSubject) {
		this.learnerEmailSubject = learnerEmailSubject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the instructorEmailAddresses
	 */
	public String getInstructorEmailAddresses() {
	    return instructorEmailAddresses;
	}

	/**
	 * @param instructorEmailAddresses the instructorEmailAddresses to set
	 */
	public void setInstructorEmailAddresses(String instructorEmailAddresses) {
	    this.instructorEmailAddresses = instructorEmailAddresses;
	}
	
	/**
	 * @return the List<CourseApprovalVO>
	 */
	public List<CourseApprovalVO> getCourseApproval() {
		return courseApproval;
	}

	/**
	 * @param courseApproval
	 */
	public void setCourseApproval(List<CourseApprovalVO> courseApproval) {
		this.courseApproval = courseApproval;
	}
	
	public String getCourseApprovalId() {
		return courseApprovalId;
	}

	public void setCourseApprovalId(String courseApprovalId) {
		this.courseApprovalId = courseApprovalId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<com.softech.vu360.lms.model.CreditReportingField> getMissingCreditReprotingFields() {
		return missingCreditReprotingFields;
	}

	public void setMissingCreditReprotingFields(
			List<com.softech.vu360.lms.model.CreditReportingField> missingCreditReprotingFields) {
		this.missingCreditReprotingFields = missingCreditReprotingFields;
	}

	public String getJurisdictionSuggestionYN() {
		return jurisdictionSuggestionYN;
	}

	public void setJurisdictionSuggestionYN(String jurisdictionSuggestionYN) {
		this.jurisdictionSuggestionYN = jurisdictionSuggestionYN;
	}

	public String getFindMoreInfoLink() {
		return findMoreInfoLink;
	}

	public void setFindMoreInfoLink(String findMoreInfoLink) {
		this.findMoreInfoLink = findMoreInfoLink;
	}

	public String getFindMoreInfoTxt() {
		return findMoreInfoTxt;
	}

	public void setFindMoreInfoTxt(String findMoreInfoTxt) {
		this.findMoreInfoTxt = findMoreInfoTxt;
	}

	public String getShowAllRegulators() {
		return showAllRegulators;
	}

	public void setShowAllRegulators(String showAllRegulators) {
		this.showAllRegulators = showAllRegulators;
	}

	public String getSuggestedCourseApprovalFound() {
		return suggestedCourseApprovalFound;
	}

	public void setSuggestedCourseApprovalFound(String suggestedCourseApprovalFound) {
		this.suggestedCourseApprovalFound = suggestedCourseApprovalFound;
	}

	public String getNoneSuggestedCourseApprovalFound() {
		return noneSuggestedCourseApprovalFound;
	}

	public void setNoneSuggestedCourseApprovalFound(
			String noneSuggestedCourseApprovalFound) {
		this.noneSuggestedCourseApprovalFound = noneSuggestedCourseApprovalFound;
	}

	public List<Document> getLstHomeWorkAssignment() {
				return lstHomeWorkAssignment;
			}
		
	public void setLstHomeWorkAssignment(
					List<Document> lstHomeWorkAssignment) {
				this.lstHomeWorkAssignment = lstHomeWorkAssignment;
			}
		
	public String getHomeWorkAssignmentdocid() {
				return homeWorkAssignmentdocid;
			}
		
	public void setHomeWorkAssignmentdocid(String homeWorkAssignmentdocid) {
				this.homeWorkAssignmentdocid = homeWorkAssignmentdocid;
			}

	public HashMap<String, Integer> getFileDetails() {
		return fileDetails;
	}

	public void setFileDetails(HashMap<String, Integer> fileDetails) {
		this.fileDetails = fileDetails;
	}

}
