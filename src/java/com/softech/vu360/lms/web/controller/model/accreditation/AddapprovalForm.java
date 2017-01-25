package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

/**
 * @author Dyutiman
 * created on 6 july 2009
 *
 */
public class AddapprovalForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String method = "courseApproval";
	private CourseApproval courseApproval;
	private ProviderApproval providerApproval;
	private InstructorApproval instructorApproval;
	List<ApprovalRegulatorCategory> regulatorCategories = new ArrayList <ApprovalRegulatorCategory>();
	List<Course> courses = new ArrayList <Course>();
	private String selectedCourseId;
	 //following variable use to hold course id that user select before the current selected course Id. This variable help to in fixing of LMS-15916
	private String previouslySelectedCourseId="";
	
	List<ApprovalCredential> credentials = new ArrayList <ApprovalCredential>();
	List<CourseConfigurationTemplate> templates = new ArrayList <CourseConfigurationTemplate>();
	private String selectedTemplateId;
	List<Certificate> certificates = new ArrayList <Certificate>();
	List<Affidavit> affidavits = new ArrayList <Affidavit>();
	private String selectedCertificateId;
	List<Provider> providers = new ArrayList <Provider>();
	private String selectedProviderId;
	List<Instructor> instructors = new ArrayList <Instructor>();
	private String selectedInstructorId;
	private String action = "";
	private String selectedAffidavitId;
	private String selectedRegulatorCategoryId;
	// used for course search
	private String courseName = "";
	private String businessKey = "";
	
	private String templateName;
	private String templateLastUpdatedDate;
	
	// used for dates
	private String effectiveStartDate;
	private String effectiveEndDate;
	private String rescentSubmittedDate;
	private String originalApprovadDate;
	private String submissionReminderDate;
	
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	private String regulatorCategoryName="";
	private String regulatorCategoryType="";
	
	private String certificateNumberGeneratorNextNumberString = "";	
	// custom fields
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = 
		new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	
	//Course Groups Tree List
	private String courseGroupId="";
	private String certificateExpirationPeriod="";
	private String courseGroupName="";
	List<List<TreeNode>> courseGroupTreeList = new ArrayList<List<TreeNode>>();
	
	public String getSelectedCourseId() {
		return selectedCourseId;
	}
	public void setSelectedCourseId(String selectedCourseId) {
		this.selectedCourseId = selectedCourseId;
	}
	public List<ApprovalCredential> getCredentials() {
		return credentials;
	}
	public void setCredentials(List<ApprovalCredential> credentials) {
		this.credentials = credentials;
	}
	public String getSelectedTemplateId() {
		return selectedTemplateId;
	}
	public void setSelectedTemplateId(String selectedTemplateId) {
		this.selectedTemplateId = selectedTemplateId;
	}
	public String getSelectedCertificateId() {
		return selectedCertificateId;
	}
	public void setSelectedCertificateId(String selectedCertificateId) {
		this.selectedCertificateId = selectedCertificateId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public List<ApprovalRegulatorCategory> getRegulatorCategories() {
		return regulatorCategories;
	}
	public void setRegulatorCategories(
			List<ApprovalRegulatorCategory> regulatorCategories) {
		this.regulatorCategories = regulatorCategories;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public String getRescentSubmittedDate() {
		return rescentSubmittedDate;
	}
	public void setRescentSubmittedDate(String rescentSubmittedDate) {
		this.rescentSubmittedDate = rescentSubmittedDate;
	}
	public String getOriginalApprovadDate() {
		return originalApprovadDate;
	}
	public void setOriginalApprovadDate(String originalApprovadDate) {
		this.originalApprovadDate = originalApprovadDate;
	}
	public String getSubmissionReminderDate() {
		return submissionReminderDate;
	}
	public void setSubmissionReminderDate(String submissionReminderDate) {
		this.submissionReminderDate = submissionReminderDate;
	}
	public List<Provider> getProviders() {
		return providers;
	}
	public void setProviders(List<Provider> providers) {
		this.providers = providers;
	}
	public String getSelectedProviderId() {
		return selectedProviderId;
	}
	public void setSelectedProviderId(String selectedProviderId) {
		this.selectedProviderId = selectedProviderId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<Instructor> getInstructors() {
		return instructors;
	}
	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}
	public String getSelectedInstructorId() {
		return selectedInstructorId;
	}
	public void setSelectedInstructorId(String selectedInstructorId) {
		this.selectedInstructorId = selectedInstructorId;
	}
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}
	public ProviderApproval getProviderApproval() {
		return providerApproval;
	}
	public void setProviderApproval(ProviderApproval providerApproval) {
		this.providerApproval = providerApproval;
	}
	public InstructorApproval getInstructorApproval() {
		return instructorApproval;
	}
	public void setInstructorApproval(InstructorApproval instructorApproval) {
		this.instructorApproval = instructorApproval;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getShowAll() {
		return showAll;
	}
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}
	public List<CourseConfigurationTemplate> getTemplates() {
		return templates;
	}
	public void setTemplates(List<CourseConfigurationTemplate> templates) {
		this.templates = templates;
	}
//	public List<Certificate> getCertificates() {
//		return certificates;
//	}
//	public void setCertificates(List<Certificate> certificates) {
//		this.certificates = certificates;
//	}
	
	
	/**
	 * @return the customFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}
	public List<Certificate> getCertificates() {
		return certificates;
	}
	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}
	public List<Affidavit> getAffidavits() {
		return affidavits;
	}
	public void setAffidavits(List<Affidavit> affidavits) {
		this.affidavits = affidavits;
	}
	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getRegulatorCategoryName() {
		return regulatorCategoryName;
	}
	public void setRegulatorCategoryName(String regulatorCategoryName) {
		this.regulatorCategoryName = regulatorCategoryName;
	}
	public String getRegulatorCategoryType() {
		return regulatorCategoryType;
	}
	public void setRegulatorCategoryType(String regulatorCategoryType) {
		this.regulatorCategoryType = regulatorCategoryType;
	}
	public String getSelectedAffidavitId() {
		return selectedAffidavitId;
	}
	public void setSelectedAffidavitId(String selectedAffidavitId) {
		this.selectedAffidavitId = selectedAffidavitId;
	}
	public String getCertificateNumberGeneratorNextNumberString() {
		return certificateNumberGeneratorNextNumberString;
	}
	public void setCertificateNumberGeneratorNextNumberString(
			String certificateNumberGeneratorNextNumberString) {
		this.certificateNumberGeneratorNextNumberString = certificateNumberGeneratorNextNumberString;
	}
	public String getSelectedRegulatorCategoryId() {
		return selectedRegulatorCategoryId;
	}
	public void setSelectedRegulatorCategoryId(String selectedRegulatorCategoryId) {
		this.selectedRegulatorCategoryId = selectedRegulatorCategoryId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateLastUpdatedDate() {
		return templateLastUpdatedDate;
	}
	public void setTemplateLastUpdatedDate(String templateLastUpdatedDate) {
		this.templateLastUpdatedDate = templateLastUpdatedDate;
	}
	public String getCourseGroupId() {
		return courseGroupId;
	}
	public void setCourseGroupId(String courseGroupId) {
		this.courseGroupId = courseGroupId;
	}
	public String getCourseGroupName() {
		return courseGroupName;
	}
	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}
	
	public List<List<TreeNode>> getCourseGroupTreeList() {
		return courseGroupTreeList;
	}
	public void setCourseGroupTreeList(List<List<TreeNode>> courseGroupTreeList) {
		this.courseGroupTreeList = courseGroupTreeList;
	}
	public String getPreviouslySelectedCourseId() {
		return previouslySelectedCourseId;
	}
	public void setPreviouslySelectedCourseId(String previouslySelectedCourseId) {
		this.previouslySelectedCourseId = previouslySelectedCourseId;
	}
	public String getCertificateExpirationPeriod() {
		return certificateExpirationPeriod;
	}
	public void setCertificateExpirationPeriod(String certificateExpirationPeriod) {
		this.certificateExpirationPeriod = certificateExpirationPeriod;
	}
	
	
	
	
}