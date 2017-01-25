package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

/**
 * @author Dyutiman
 */
public class InstructorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Instructor instructor = new Instructor();
	private Learner learner;
	private LearnerProfile profile;
	private String expirationDate;
	private String confirnPassword;
	private Brander brander = null;
	
	//for approval instructor
	private List<Instructor> instructors = new ArrayList<Instructor>();
	private String instId = null; 
	private String firstName = null; 
	private String lastName = null; 
	private String emailAdd = null; 
	private InstructorApproval instructorApproval = null;
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	
	//for validation
	private String eventSource = "false";
	
	//for add Custom Field
	private long instructorId = 0;
	private List<ManageCustomField> manageCustomField = new ArrayList<ManageCustomField>();
	private String pageIndex="0";
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	
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
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public LearnerProfile getProfile() {
		return profile;
	}
	public void setProfile(LearnerProfile profile) {
		this.profile = profile;
	}
	public String getConfirnPassword() {
		return confirnPassword;
	}
	public void setConfirnPassword(String confirnPassword) {
		this.confirnPassword = confirnPassword;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	/**
	 * @return the instId
	 */
	public String getInstId() {
		return instId;
	}
	/**
	 * @param instId the instId to set
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}
	/**
	 * @return the instructors
	 */
	public List<Instructor> getInstructors() {
		return instructors;
	}
	/**
	 * @param instructors the instructors to set
	 */
	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}
	/**
	 * @return the instructorApproval
	 */
	public InstructorApproval getInstructorApproval() {
		return instructorApproval;
	}
	/**
	 * @param instructorApproval the instructorApproval to set
	 */
	public void setInstructorApproval(InstructorApproval instructorApproval) {
		this.instructorApproval = instructorApproval;
	}
	/**
	 * @return the emailAdd
	 */
	public String getEmailAdd() {
		return emailAdd;
	}
	/**
	 * @param emailAdd the emailAdd to set
	 */
	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}
	/**
	 * @return the customFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}
	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}
	/**
	 * @return the instructorId
	 */
	public long getInstructorId() {
		return instructorId;
	}
	/**
	 * @param instructorId the instructorId to set
	 */
	public void setInstructorId(long instructorId) {
		this.instructorId = instructorId;
	}
	/**
	 * @return the manageCustomField
	 */
	public List<ManageCustomField> getManageCustomField() {
		return manageCustomField;
	}
	/**
	 * @param manageCustomField the manageCustomField to set
	 */
	public void setManageCustomField(List<ManageCustomField> manageCustomField) {
		this.manageCustomField = manageCustomField;
	}
	public Brander getBrander() {
		return brander;
	}
	public void setBrander(Brander brander) {
		this.brander = brander;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}