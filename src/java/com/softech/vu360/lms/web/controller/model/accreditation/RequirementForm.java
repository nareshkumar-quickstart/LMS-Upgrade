package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class RequirementForm implements ILMSBaseInterface {
	
	public static final String COURSE_APPROVAL = "Course";
	private static final long serialVersionUID = 1L;
	
	private CourseApproval courseApproval;
	
	private String entity ;
	private String credentialName ;
	private String credentialType ;
	private String credentialValidation ;
	private String pageIndex="0";
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	private List<ApprovalCredential> approvalCredential = new ArrayList<ApprovalCredential>();
	private List<CredentialCategoryRequirement> requirements = new ArrayList<CredentialCategoryRequirement>();
	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}
	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the credentialName
	 */
	public String getCredentialName() {
		return credentialName;
	}
	/**
	 * @param credentialName the credentialName to set
	 */
	public void setCredentialName(String credentialName) {
		this.credentialName = credentialName;
	}
	/**
	 * @return the credentialType
	 */
	public String getCredentialType() {
		return credentialType;
	}
	/**
	 * @param credentialType the credentialType to set
	 */
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
	/**
	 * @return the credentialValidation
	 */
	public String getCredentialValidation() {
		return credentialValidation;
	}
	/**
	 * @param credentialValidation the credentialValidation to set
	 */
	public void setCredentialValidation(String credentialValidation) {
		this.credentialValidation = credentialValidation;
	}
	/**
	 * @return the approvalCredential
	 */
	public List<ApprovalCredential> getApprovalCredential() {
		return approvalCredential;
	}
	/**
	 * @param approvalCredential the approvalCredential to set
	 */
	public void setApprovalCredential(List<ApprovalCredential> approvalCredential) {
		this.approvalCredential = approvalCredential;
	}
	/**
	 * @return the requirements
	 */
	public List<CredentialCategoryRequirement> getRequirements() {
		return requirements;
	}
	/**
	 * @param requirements the requirements to set
	 */
	public void setRequirements(List<CredentialCategoryRequirement> requirements) {
		this.requirements = requirements;
	}
	/**
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	/**
	 * @return the showAll
	 */
	public String getShowAll() {
		return showAll;
	}
	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}
	/**
	 * @return the pageCurrIndex
	 */
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}
	/**
	 * @param pageCurrIndex the pageCurrIndex to set
	 */
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}
	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}
	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	
}
