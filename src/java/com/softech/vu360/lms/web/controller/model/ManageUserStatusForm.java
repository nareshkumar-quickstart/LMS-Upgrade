package com.softech.vu360.lms.web.controller.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageUserStatusForm implements ILMSBaseInterface {

	public static long COURSE_TYPE_UNSELECTED = 0; 
	public static long COURSE_TYPE_AFFIDAVIT_WITH_REPORTING = 1;
	public static long COURSE_TYPE_AFFIDAVIT_WITHOUT_REPORTING = 2;
	public static long COURSE_TYPE_REPORTING_WITH_NO_AFFIDAVIT = 3;
	
	public static long COURSE_STATUS_AFFIDAVIT_UNSELECTED = 0; 
	public static long COURSE_STATUS_AFFIDAVIT_PENDING = 1;
	public static long COURSE_STATUS_AFFIDAVIT_RECEIVED = 2;
	public static long COURSE_STATUS_AFFIDAVIT_DISPUTED = 3;
	public static long COURSE_STATUS_AFFIDAVIT_COMPLETED = 4;
	public static long COURSE_STATUS_AFFIDAVIT_REPORTED = 5;
	
	public static long AFFIDAVIT_TYPE_UNSELECTED = 0; 
	public static long AFFIDAVIT_TYPE_FILE = 1;
	public static long AFFIDAVIT_TYPE_TEMPLATEID = 2;

	
	private static final long serialVersionUID = 1L;
	private Asset asset = null;
	private byte[] fileData;
	private MultipartFile affidavitFile;
	private String affidavitFileName; 
	private byte[] affidavitFileData;
	private Long learnerEnrollmentId;
	private boolean reversalPermission = false;
	
	public List<Map<Object, Object>> manageUserStatusList = null;

	public List<Map<Object, Object>> getManageUserStatusList() {
		return manageUserStatusList;
	}

	public Long getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}

	public void setLearnerEnrollmentId(Long learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}

	public String getAffidavitFileName() {
		return affidavitFileName;
	}

	public void setAffidavitFileName(String affidavitFileName) {
		this.affidavitFileName = affidavitFileName;
	}

	public byte[] getAffidavitFileData() {
		return affidavitFileData;
	}

	public void setAffidavitFileData(byte[] affidavitFileData) {
		this.affidavitFileData = affidavitFileData;
	}

	public void setManageUserStatusList(
			List<Map<Object, Object>> manageUserStatusList) {
		this.manageUserStatusList = manageUserStatusList;
	}
	
	public MultipartFile getAffidavitFile() {
		return affidavitFile;
	}

	public void setAffidavitFile(MultipartFile affidavitFile) {
		this.affidavitFile = affidavitFile;
	}

	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}


	/*
	 * Search Form Fields
	 */
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Long holdingRegulatorId;
	private Long regulatorCategoryId;
	private Long regulatorCategoryId2;
	private String courseId;
	private String courseType;
	private Long courseTypeId;
	private String courseStatus;
	//private Long affidavitTypeId;
	private String affidavitType;
	private String startDate;
	private String endDate;
	private ManageUserStatus manageUserStatus = null;

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

	public Long getHoldingRegulatorId() {
		return holdingRegulatorId;
	}

	public Long getRegulatorCategoryId() {
		return regulatorCategoryId;
	}
	
	public Long getRegulatorCategoryId2() {
		return regulatorCategoryId2;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public Long getCourseTypeId() {
		return courseTypeId;
	}

	public void setCourseTypeId(Long courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getAffidavitType() {
		return affidavitType;
	}

	public void setAffidavitType(String affidavitType) {
		this.affidavitType = affidavitType;
	}

	public void setHoldingRegulatorId(Long holdingRegulatorId) {
		this.holdingRegulatorId = holdingRegulatorId;
	}

	public void setRegulatorCategoryId(Long regulatorCategoryId) {
		this.regulatorCategoryId = regulatorCategoryId;
	}
	
	public void setRegulatorCategoryId2(Long regulatorCategoryId2) {
		this.regulatorCategoryId2 = regulatorCategoryId2;
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
	
	public ManageUserStatus getManageUserStatus() {
		return manageUserStatus;
	}
	public void setManageUserStatus(ManageUserStatus manageUserStatus) {
		this.manageUserStatus = manageUserStatus;
	}
	/*
	 * Update Form Fields
	 */
	private String update_courseStatus;	
	private List<Long> userstatuses;

	
	public String getUpdate_courseStatus() {
		return update_courseStatus;
	}

	public void setUpdate_courseStatus(String update_courseStatus) {
		this.update_courseStatus = update_courseStatus;
	}

	public List<Long> getUserstatuses() {
		return userstatuses;
	}

	public void setUserstatuses(List<Long> userstatuses) {
		this.userstatuses = userstatuses;
	}
	
	/*
	 * Update Form Fields
	 */

	public boolean isNotEmptyForm(){
		if( StringUtils.isEmpty(firstName)
			&& StringUtils.isEmpty(firstName)
			&& StringUtils.isEmpty(lastName)
			&& StringUtils.isEmpty(emailAddress)
			&& (holdingRegulatorId == null || holdingRegulatorId==0 )
			&& (regulatorCategoryId == null || regulatorCategoryId==0) 
			&& (regulatorCategoryId2 == null || regulatorCategoryId2==0) 
			&& StringUtils.isEmpty(courseId)
			&& StringUtils.isEmpty(courseType)
			&& (courseTypeId == null || courseTypeId==0)
			&& (StringUtils.isEmpty(courseStatus) || "0".equals(courseStatus)) 
			&& ( StringUtils.isEmpty(affidavitType) || "0".equals(affidavitType)) 
			&& StringUtils.isEmpty(startDate)
			&& StringUtils.isEmpty(endDate)
			)
			return false;
		else
			return true;
	}
	
	public void resetForm()	{
		firstName = "";
		lastName = "";
		emailAddress = "";
		holdingRegulatorId = (long) 0;
		regulatorCategoryId = (long) 0;
		courseId = "";
		courseType = "";
		courseTypeId = (long) 0;
		courseStatus = "";
		affidavitType = "";
		startDate = "";
		endDate = "";
	}

	public boolean isReversalPermission() {
		return reversalPermission;
	}

	public void setReversalPermission(boolean reversalPermission) {
		this.reversalPermission = reversalPermission;
	}
}
