package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class CreditReportingFieldForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String COURSE_APPROVAL = "CourseApproval";
	
	public static final String REPORTINGFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String REPORTINGFIELD_DATE = "Date";
	public static final String REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String REPORTINGFIELD_NUMBER = "Number";
	public static final String REPORTINGFIELD_RADIO_BUTTON = "Radio Button";
	public static final String REPORTINGFIELD_CHOOSE = "Choose Menu";
	public static final String REPORTINGFIELD_CHECK_BOX = "Check Box";
	public static final String REPORTINGFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	public static final String REPORTINGFIELD_STATIC_FIELD = "Static Field";//static field to represent any permanent field of the learner profile 
	public static final String REPORTINGFIELD_TELEPHONE_NUMBER_FIELD = "Telephone Number";
	
	private CreditReportingField creditReportingField; 
	private String fieldType;
	private String entity;
	private String option;
	private List<String> optionList = new ArrayList<String>();
	private boolean alignment = true;
	
	private CourseApproval courseApproval = null;
	
	/*Set of Course Approvals that contain this reporting field*/
	private Set <CourseApproval> containingCourseApprovalSet=new HashSet<CourseApproval>(); 
	//For edit CreditReportingField
	private long reportingFieldId = 0;
	private String fieldLabel = null;
	private boolean fieldEncrypted = false;
	private boolean fieldRequired = false;
	private String reportingFieldDescription = null;
	private List<CreditReportingFieldValueChoice> options = new ArrayList<CreditReportingFieldValueChoice>();
	private List<ManageCustomField> manageCreditReportingField = new ArrayList<ManageCustomField>();
	/**
	 * @return the creditReportingField
	 */
	public CreditReportingField getCreditReportingField() {
		return creditReportingField;
	}
	/**
	 * @param creditReportingField the creditReportingField to set
	 */
	public void setCreditReportingField(CreditReportingField creditReportingField) {
		this.creditReportingField = creditReportingField;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the optionList
	 */
	public List<String> getOptionList() {
		return optionList;
	}
	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}
	/**
	 * @return the alignment
	 */
	public boolean isAlignment() {
		return alignment;
	}
	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(boolean alignment) {
		this.alignment = alignment;
	}
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
	 * @return the reportingFieldId
	 */
	public long getReportingFieldId() {
		return reportingFieldId;
	}
	/**
	 * @param reportingFieldId the reportingFieldId to set
	 */
	public void setReportingFieldId(long reportingFieldId) {
		this.reportingFieldId = reportingFieldId;
	}
	/**
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
	/**
	 * @param fieldLabel the fieldLabel to set
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	/**
	 * @return the fieldEncrypted
	 */
	public boolean isFieldEncrypted() {
		return fieldEncrypted;
	}
	/**
	 * @param fieldEncrypted the fieldEncrypted to set
	 */
	public void setFieldEncrypted(boolean fieldEncrypted) {
		this.fieldEncrypted = fieldEncrypted;
	}
	/**
	 * @return the fieldRequired
	 */
	public boolean isFieldRequired() {
		return fieldRequired;
	}
	/**
	 * @param fieldRequired the fieldRequired to set
	 */
	public void setFieldRequired(boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
	}
	/**
	 * @return the reportingFieldDescription
	 */
	public String getReportingFieldDescription() {
		return reportingFieldDescription;
	}
	/**
	 * @param reportingFieldDescription the reportingFieldDescription to set
	 */
	public void setReportingFieldDescription(String reportingFieldDescription) {
		this.reportingFieldDescription = reportingFieldDescription;
	}
	/**
	 * @return the options
	 */
	public List<CreditReportingFieldValueChoice> getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(List<CreditReportingFieldValueChoice> options) {
		this.options = options;
	}
	
	public List<ManageCustomField> getManageCreditReportingField() {
		return manageCreditReportingField;
	}
	/**
	 * @param manageCreditReportingField the manageCreditReportingField to set
	 */
	public void setManageCreditReportingField(
			List<ManageCustomField> manageCreditReportingField) {
		this.manageCreditReportingField = manageCreditReportingField;
	}
	public Set<CourseApproval> getContainingCourseApprovalSet() {
		return containingCourseApprovalSet;
	}
	public void setContainingCourseApprovalSet(
			Set<CourseApproval> containingCourseApprovalSet) {
		this.containingCourseApprovalSet = containingCourseApprovalSet;
	}
	
	
	

}
