package com.softech.vu360.lms.model;



/**
 * 
 * @author haider.ali
 *
 */
public class ManageUserStatus implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	public static String COURSE_TYPE_AFFIDAVIT_WITH_REPORTING = "Affidavit with Reporting";
	public static String COURSE_TYPE_AFFIDAVIT_WITHOUT_REPORTING = "Affidavit without Reporting";
	public static String COURSE_TYPE_REPORTING_WITH_NO_AFFIDAVIT = "Reporting with no Affidavit";
	
	public static String COURSE_STATUS_AFFIDAVIT_PENDING = "Affidavit Pending";
	public static String COURSE_STATUS_AFFIDAVIT_RECEIVED = "Affidavit Received";
	public static String COURSE_STATUS_AFFIDAVIT_DISPUTED = "Affidavit Disputed";
	public static String COURSE_STATUS_AFFIDAVIT_DECLINED = "User Declined Affidavit";
	public static String COURSE_STATUS_AFFIDAVIT_COMPLETED = "Completed";
	public static String COURSE_STATUS_AFFIDAVIT_REPORTED = "Reported";
	
	public static String AFFIDAVIT_TYPE_FILE = "File";
	public static String AFFIDAVIT_TYPE_TEMPLATEID = "Template ID";
	
	public String firstName;
	public String lastName;
	public String emailAddress;
	public String phoneNumber;
	public String courseName;
	public String courseId;
	public String affidavitLink;
	public String courseStatus;
	//public Long courseStatusId;
	public String courseType;
	public Long courseTypeId;
	public Long assetId;
	public String completeDate;
	public String enrollmentDate;
	public String firstAccessDate;
	public String holdingRegulatorName;
	public String regulatoryCategory;
	public Long holdingRegulatorId;
	public Long regulatoryCategoryId;
	public Long affidavitTypeId;
	public String affidavitType;
	public Long	learnerEnrollmentId;
	public Long	courseSatisticsId;
	public Long	courseApprovalId;
	public String lastUserStatusChange;
	public String lastUserStatusChangeDate;
	public String lastUserAffidavitUpload;
	public String lastUserAffidavitUploadDate;
	public String address1;
	public String city;
	public String state;
	public String zipCode;
	private String startDate;
	private String endDate;
    private long learnerId;
	
	
	public Long getCourseApprovalId() {
		return courseApprovalId;
	}
	public void setCourseApprovalId(Long courseApprovalId) {
		this.courseApprovalId = courseApprovalId;
	}
	public Long getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}
	public void setLearnerEnrollmentId(Long learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Long getAffidavitTypeId() {
		return affidavitTypeId;
	}
	public void setAffidavitTypeId(Long affidavitTypeId) {
		this.affidavitTypeId = affidavitTypeId;
	}
	public Long getCourseTypeId() {
		return courseTypeId;
	}
	public void setCourseTypeId(Long courseTypeId) {
		this.courseTypeId = courseTypeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getAffidavitLink() {
		return affidavitLink;
	}
	public void setAffidavitLink(String affidavitLink) {
		this.affidavitLink = affidavitLink;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public String getHoldingRegulatorName() {
		return holdingRegulatorName;
	}
	public void setHoldingRegulatorName(String holdingRegulatorName) {
		this.holdingRegulatorName = holdingRegulatorName;
	}
	public String getRegulatoryCategory() {
		return regulatoryCategory;
	}
	public void setRegulatoryCategory(String regulatoryCategory) {
		this.regulatoryCategory = regulatoryCategory;
	}
	public Long getHoldingRegulatorId() {
		return holdingRegulatorId;
	}
	public void setHoldingRegulatorId(Long holdingRegulatorId) {
		this.holdingRegulatorId = holdingRegulatorId;
	}
	public Long getRegulatoryCategoryId() {
		return regulatoryCategoryId;
	}
	public void setRegulatoryCategoryId(Long regulatoryCategoryId) {
		this.regulatoryCategoryId = regulatoryCategoryId;
	}
	public String getAffidavitType() {
		return affidavitType;
	}
	public void setAffidavitType(String affidavitType) {
		this.affidavitType = affidavitType;
	}
	public Long getCourseSatisticsId() {
		return courseSatisticsId;
	}
	public void setCourseSatisticsId(Long courseSatisticsId) {
		this.courseSatisticsId = courseSatisticsId;
	}
	public String getLastUserStatusChange() {
		return lastUserStatusChange;
	}
	public void setLastUserStatusChange(String lastUserStatusChange) {
		this.lastUserStatusChange = lastUserStatusChange;
	}
	public String getLastUserStatusChangeDate() {
		return lastUserStatusChangeDate;
	}
	public void setLastUserStatusChangeDate(String lastUserStatusChangeDate) {
		this.lastUserStatusChangeDate = lastUserStatusChangeDate;
	}
	public String getLastUserAffidavitUpload() {
		return lastUserAffidavitUpload;
	}
	public void setLastUserAffidavitUpload(String lastUserAffidavitUpload) {
		this.lastUserAffidavitUpload = lastUserAffidavitUpload;
	}
	public String getLastUserAffidavitUploadDate() {
		return lastUserAffidavitUploadDate;
	}
	public void setLastUserAffidavitUploadDate(String lastUserAffidavitUploadDate) {
		this.lastUserAffidavitUploadDate = lastUserAffidavitUploadDate;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	public String getFirstAccessDate() {
		return firstAccessDate;
	}
	public void setFirstAccessDate(String firstAccessDate) {
		this.firstAccessDate = firstAccessDate;
	}

    public long getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(long learnerId) {
        this.learnerId = learnerId;
    }

}
