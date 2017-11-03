package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CourseDetails implements ILMSBaseInterface {

	private long id = 0l;
	
	private String courseType = "scormPackage";
	private static final long serialVersionUID = 1L;
	private String courseName = null;
	private String courseID = null;
	private String description = null;
	private String keywords = null;
	private String creditHours = null;
	private String durationHours = null;
	private String version = null;
	private String businessUnit = null;
	private String hwAssignmentInstruction = null;
	private String assignmentDueDate = null;
	private String gradingMethod = null;
	private String masterScore = null;
	private String language = null;
	private String link = null;
	// SCORM Package properties remains to be added.
	private String courseGuide = null;
	private String preRequisites = null;
	private String courseOutline = null;
	private String learningObjectives = null;
	private String quizInformation = null;
	private String finalExamInformation = null;
	private String endOfCourseInstructions = null;
	private String deliveryMethod = null;
	private String deliveryMethodId = null;
	private String msrp = null;
	private String courseCode = null;
	private String approvedCourseHours = null;
	private String approvalNumber = null;
	private String currency = null;
	private String productPrice = null;
	private String wholeSalePrice = null;
	private String royaltyPartner = null;
	private String royaltyType = null;
	private String regulatoryRequirement = null;
	private String termsOfService = null;
	private MultipartFile file;
	private String scoURI[] = null;
	private String tempFileName = null;
	private String fileUploadURL = null;
	List<CourseDetails> scormCourses = new ArrayList<CourseDetails>();
	List<Document> lstDocument = new ArrayList<Document>();
	List<HomeWorkAssignmentAsset> lstHomeWorkAssignmentAsset = new ArrayList<HomeWorkAssignmentAsset>();
	private Vector<String> scormPackages = new Vector<String>();
	private String instructorType;
	private String meetingId;
	private String meetingPasscode;
	private String emailAddress;
    private boolean blankHomeAssignementfile; 
	// SCORM Package properties 
	private String launchData;
	private long maxTimeAllowedSeconds;
	private long activeEnrollment;
	private String timeLimitAction;
	private double completionThreshold;
	private double masteryScore;
	
	//In Edit course screen, there are multiple tabs. Use this property to track the user selected tab.  
	private String tabName="";
	
	private List<CommonsMultipartFile> fileData;
	

	
	public String getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(String durationHours) {
		this.durationHours = durationHours;
	}

	public Vector<String> getScormPackages() {
		return scormPackages;
	}

	public void setScormPackages(Vector<String> scormPackages) {
		this.scormPackages = scormPackages;
	}

	public String getFileUploadURL() {
		return fileUploadURL;
	}

	public void setFileUploadURL(String fileUploadURL) {
		this.fileUploadURL = fileUploadURL;
	}

	public List<CourseDetails> getScormCourses() {
		return scormCourses;
	}

	public void setScormCourses(List<CourseDetails> scormCourses) {
		this.scormCourses = scormCourses;
	}

	public String getTempFileName() {
		return tempFileName;
	}

	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}

	public String[] getScoURI() {
		return scoURI;
	}

	public void setScoURI(String[] scoURI) {
		this.scoURI = scoURI;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourseGuide() {
		return courseGuide;
	}

	public void setCourseGuide(String courseGuide) {
		this.courseGuide = courseGuide;
	}

	public String getPreRequisites() {
		return preRequisites;
	}

	public void setPreRequisites(String preRequisites) {
		this.preRequisites = preRequisites;
	}

	public String getLearningObjectives() {
		return learningObjectives;
	}

	public void setLearningObjectives(String learningObjectives) {
		this.learningObjectives = learningObjectives;
	}

	public String getQuizInformation() {
		return quizInformation;
	}

	public void setQuizInformation(String quizInformation) {
		this.quizInformation = quizInformation;
	}

	public String getFinalExamInformation() {
		return finalExamInformation;
	}

	public void setFinalExamInformation(String finalExamInformation) {
		this.finalExamInformation = finalExamInformation;
	}

	public String getEndOfCourseInstructions() {
		return endOfCourseInstructions;
	}

	public void setEndOfCourseInstructions(String endOfCourseInstructions) {
		this.endOfCourseInstructions = endOfCourseInstructions;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getMsrp() {
		return msrp;
	}

	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getApprovedCourseHours() {
		return approvedCourseHours;
	}

	public void setApprovedCourseHours(String approvedCourseHours) {
		this.approvedCourseHours = approvedCourseHours;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getWholeSalePrice() {
		return wholeSalePrice;
	}

	public void setWholeSalePrice(String wholeSalePrice) {
		this.wholeSalePrice = wholeSalePrice;
	}

	public String getRoyaltyPartner() {
		return royaltyPartner;
	}

	public void setRoyaltyPartner(String royaltyPartner) {
		this.royaltyPartner = royaltyPartner;
	}

	public String getRoyaltyType() {
		return royaltyType;
	}

	public void setRoyaltyType(String royaltyType) {
		this.royaltyType = royaltyType;
	}

	public String getRegulatoryRequirement() {
		return regulatoryRequirement;
	}

	public void setRegulatoryRequirement(String regulatoryRequirement) {
		this.regulatoryRequirement = regulatoryRequirement;
	}

	public String getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(String termsOfService) {
		this.termsOfService = termsOfService;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCreditHours() {
		return creditHours;
	}

	public void setCreditHours(String creditHours) {
		this.creditHours = creditHours;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CourseDetails() {
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getHwAssignmentInstruction() {
		return hwAssignmentInstruction;
	}

	public void setHwAssignmentInstruction(String hwAssignmentInstruction) {
		this.hwAssignmentInstruction = hwAssignmentInstruction;
	}

	public String getAssignmentDueDate() {
		return assignmentDueDate;
	}

	public void setAssignmentDueDate(String assignmentDueDate) {
		this.assignmentDueDate = assignmentDueDate;
	}

	public String getGradingMethod() {
		return gradingMethod;
	}

	public void setGradingMethod(String gradingMethod) {
		this.gradingMethod = gradingMethod;
	}

	public String getMasterScore() {
		return masterScore;
	}

	public void setMasterScore(String masterScore) {
		this.masterScore = masterScore;
	}

	public String getInstructorType() {
		return instructorType;
	}

	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingPasscode() {
		return meetingPasscode;
	}

	public void setMeetingPasscode(String meetingPasscode) {
		this.meetingPasscode = meetingPasscode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(String deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	
	
	
	
	// SCORM Package properties 
	public String getLaunchData() {
		return launchData;
	}

	public void setLaunchData(String launchData) {
		this.launchData = launchData;
	}
	
	public long getMaxTimeAllowedSeconds() {
		return maxTimeAllowedSeconds;
	}

	public void setMaxTimeAllowedSeconds(long maxTimeAllowedSeconds) {
		this.maxTimeAllowedSeconds = maxTimeAllowedSeconds;
	}
	
	
	public String getTimeLimitAction() {
		return timeLimitAction;
	}

	public void setTimeLimitAction(String timeLimitAction) {
		this.timeLimitAction = timeLimitAction;
	}
	
	
	public double getCompletionThreshold() {
		return completionThreshold;
	}

	public void setCompletionThreshold(double completionThreshold) {
		this.completionThreshold = completionThreshold;
	}
	
	public double getMasteryScore() {
		return masteryScore;
	}

	public void setMasteryScore(double masteryScore) {
		this.masteryScore = masteryScore;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<CommonsMultipartFile> getFileData() {
		return fileData;
	}

	public void setFileData(List<CommonsMultipartFile> fileData) {
		this.fileData = fileData;
	}

	public List<Document> getLstDocument() {
		return lstDocument;
	}

	public void setLstDocument(List<Document> lstDocument) {
		this.lstDocument = lstDocument;
	}
	
	public List<HomeWorkAssignmentAsset> getLstHomeWorkAssignmentAsset() {
		return lstHomeWorkAssignmentAsset;
	}

	public void setLstHomeWorkAssignmentAsset(List<HomeWorkAssignmentAsset> lstHomeWorkAssignmentAsset) {
		this.lstHomeWorkAssignmentAsset = lstHomeWorkAssignmentAsset;
	}

	public long getActiveEnrollment() {
		return activeEnrollment;
	}

	public void setActiveEnrollment(long activeEnrollment) {
		this.activeEnrollment = activeEnrollment;
	}

	public boolean isBlankHomeAssignementfile() {
		return blankHomeAssignementfile;
	}

	public void setBlankHomeAssignementfile(boolean blankHomeAssignementfile) {
		this.blankHomeAssignementfile = blankHomeAssignementfile;
	}

	public String getCourseOutline() {
		return courseOutline;
	}

	public void setCourseOutline(String courseOutline) {
		this.courseOutline = courseOutline;
	}


}