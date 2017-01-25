package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class CredentialForm implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	public static final String CUSTOMFIELD_STATIC = "Static Field";	
	
	private String informationLastVerifiedDate = null;
	private Credential credential;
	private List<SelectedRequirement> requirements = new ArrayList<SelectedRequirement>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> credentialRequirementCustomFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	
	private long cid = 0;  //for edit Credential Summary.
	private String totalNumberOfLicense=null;
	//for add requirement
	private CredentialCategoryRequirement requirement; 
	private String licenseRenewalDate = null;
	private String CERequirementDeadline = null;
	private String reportingPeriod = null;
	
	private long reqId = 0;  //for edit Requirement.
	
	private CustomField customField; //for add CustomField
	private CustomField credentialRequirementCustomField; //for add CustomField
	
	private List<ManageCustomField> manageCustomField = new ArrayList<ManageCustomField>(); //for manage CustomField
	private List<ManageCustomField> manageCredentialRequirementCustomField = new ArrayList<ManageCustomField>(); //for manage CustomField
	private List<CredentialTrainingCourses> credentialTrainingCourses = new ArrayList<CredentialTrainingCourses>();
	private List<CredentialProctors> credentialProctors = new ArrayList<CredentialProctors>();
	

	// For Add New CredentialCategory
	private CredentialCategory category;
	
	
	private String pageIndex="0";
	private String showAll = "false";
	private String pageCurrIndex;
	private String sortColumnIndex;
	private String trainingCourseName="";
	private String trainingCourseBusinessKey="";
	private String gridAction="0";
	private int sortDirection = 0;
	
	private String firstName="";
	private String lastName="";
	private String eMailAddress="";
	private String companyName="";
	private String startDate="";
	private String endDate="";
	
	
	 
        /** For Credential -> Category -> Requirements. */
        private String creditHours = null;
        
	/**
	 * @return the credential
	 */
	public Credential getCredential() {
		return credential;
	}
	
	/**
	 * @param credential the credential to set
	 */
	public void setCredential(Credential credential) {
		this.credential = credential;
	}
	
	/**
	 * @return the requirements
	 */
	public List<SelectedRequirement> getRequirements() {
		return requirements;
	}
	
	/**
	 * @param requirements the requirements to set
	 */
	public void setRequirements(List<SelectedRequirement> requirements) {
		this.requirements = requirements;
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
	public void setCustomFields(List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}

	/**
	 * @return the informationLastVerifiedDate
	 */
	public String getInformationLastVerifiedDate() {
		return informationLastVerifiedDate;
	}

	/**
	 * @param informationLastVerifiedDate the informationLastVerifiedDate to set
	 */
	public void setInformationLastVerifiedDate(String informationLastVerifiedDate) {
		this.informationLastVerifiedDate = informationLastVerifiedDate;
	}

	/**
	 * @return the cid
	 */
	public long getCid() {
		return cid;
	}

	/**
	 * @param cid the cid to set
	 */
	public void setCid(long cid) {
		this.cid = cid;
	}

	/**
	 * @return the requirement
	 */
	public CredentialCategoryRequirement getRequirement() {
		return requirement;
	}

	/**
	 * @param requirement the requirement to set
	 */
	public void setRequirement(CredentialCategoryRequirement requirement) {
		this.requirement = requirement;
	}

	/**
	 * @return the licenseRenewalDate
	 */
	public String getLicenseRenewalDate() {
		return licenseRenewalDate;
	}

	/**
	 * @param licenseRenewalDate the licenseRenewalDate to set
	 */
	public void setLicenseRenewalDate(String licenseRenewalDate) {
		this.licenseRenewalDate = licenseRenewalDate;
	}

	/**
	 * @return the cERequirementDeadline
	 */
	public String getCERequirementDeadline() {
		return CERequirementDeadline;
	}

	/**
	 * @param requirementDeadline the cERequirementDeadline to set
	 */
	public void setCERequirementDeadline(String requirementDeadline) {
		CERequirementDeadline = requirementDeadline;
	}

	/**
	 * @return the reportingPeriod
	 */
	public String getReportingPeriod() {
		return reportingPeriod;
	}

	/**
	 * @param reportingPeriod the reportingPeriod to set
	 */
	public void setReportingPeriod(String reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	/**
	 * @return the reqId
	 */
	public long getReqId() {
		return reqId;
	}

	/**
	 * @param reqId the reqId to set
	 */
	public void setReqId(long reqId) {
		this.reqId = reqId;
	}

	/**
	 * @return the customField
	 */
	public CustomField getCustomField() {
		return customField;
	}

	/**
	 * @param customField the customField to set
	 */
	public void setCustomField(CustomField customField) {
		this.customField = customField;
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

	/**
	 * @return the totalNumberOfLicense
	 */
	public String getTotalNumberOfLicense() {
		return totalNumberOfLicense;
	}

	/**
	 * @param totalNumberOfLicense the totalNumberOfLicense to set
	 */
	public void setTotalNumberOfLicense(String totalNumberOfLicense) {
		this.totalNumberOfLicense = totalNumberOfLicense;
	}

	public CustomField getCredentialRequirementCustomField() {
		return credentialRequirementCustomField;
	}

	public void setCredentialRequirementCustomField(
			CustomField credentialRequirementCustomField) {
		this.credentialRequirementCustomField = credentialRequirementCustomField;
	}

	public List<ManageCustomField> getManageCredentialRequirementCustomField() {
		return manageCredentialRequirementCustomField;
	}

	public void setManageCredentialRequirementCustomField(
			List<ManageCustomField> manageCredentialRequirementCustomField) {
		this.manageCredentialRequirementCustomField = manageCredentialRequirementCustomField;
	}

	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCredentialRequirementCustomFields() {
		return credentialRequirementCustomFields;
	}

	public void setCredentialRequirementCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> credentialRequirementCustomFields) {
		this.credentialRequirementCustomFields = credentialRequirementCustomFields;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(CredentialCategory category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public CredentialCategory getCategory() {
		return category;
	}
	
	
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}

    /**
     * @return the creditHours
     */
    public String getCreditHours() {
        return creditHours;
    }

    /**
     * @param creditHours the creditHours to set
     */
    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

	public List<CredentialTrainingCourses> getCredentialTrainingCourses() {
		return credentialTrainingCourses;
	}

	public void setCredentialTrainingCourses(
			List<CredentialTrainingCourses> credentialTrainingCourses) {
		this.credentialTrainingCourses = credentialTrainingCourses;
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

	public String getPageCurrIndex() {
		return pageCurrIndex;
	}

	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}

	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	public List<CredentialProctors> getCredentialProctors() {
		return credentialProctors;
	}

	public void setCredentialProctors(List<CredentialProctors> credentialProctors) {
		this.credentialProctors = credentialProctors;
	}

	public String getTrainingCourseName() {
		return trainingCourseName;
	}

	public void setTrainingCourseName(String trainingCourseName) {
		this.trainingCourseName = trainingCourseName;
	}

	public String getTrainingCourseBusinessKey() {
		return trainingCourseBusinessKey;
	}

	public void setTrainingCourseBusinessKey(String trainingCourseBusinessKey) {
		this.trainingCourseBusinessKey = trainingCourseBusinessKey;
	}

	public String getGridAction() {
		return gridAction;
	}

	public void setGridAction(String gridAction) {
		this.gridAction = gridAction;
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

	public String geteMailAddress() {
		return eMailAddress;
	}

	public void seteMailAddress(String eMailAddress) {
		this.eMailAddress = eMailAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	
}
