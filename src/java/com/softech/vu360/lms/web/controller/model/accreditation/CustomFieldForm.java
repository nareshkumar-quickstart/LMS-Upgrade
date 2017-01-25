package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class CustomFieldForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String CREDENTIAL = "Credential";
	public static final String CREDENTIAL_REQUIREMENT = "CredentialRequirement";
	public static final String REGULATOR = "Regulator";
	public static final String REGULATOR_CATEGORY = "RegulatorCategory";
	public static final String PROVIDER_APPROVAL = "ProviderApproval";
	public static final String INSTRUCTOR_APPROVAL = "InstructorApproval";
	public static final String COURSE_APPROVAL = "CourseApproval";
	public static final String PROVIDER = "Providers";
	public static final String INSTRUCTOR = "Instructors";
	public static final String CREDENTIAL_CATEGORY = "Credential Category";
	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	private Credential credential; 
	private CredentialCategoryRequirement credentialCategoryRequirement;
	private Regulator regulator ;
	private CustomField customField; 
	private String fieldType;
	private String entity;
	private String option;
	private List<String> optionList = new ArrayList<String>();
	private boolean alignment = true;
	private CredentialCategory credentialCategory;
        protected RegulatorCategory regulatorCategory = null;
	
	//FOR EDIT CUSTOMFIELD
	private long customFieldId = 0;  
	private boolean fieldEncrypted = false;
	private String fieldLabel = null;
	private boolean fieldRequired = false;
	private String customFieldDescription = null;
	private List<CustomFieldValueChoice> options = new ArrayList<CustomFieldValueChoice>();
	
	//for approval
	private ProviderApproval providerApproval = null;
	private InstructorApproval instructorApproval = null;
	private CourseApproval courseApproval = null;
	
	//for Provider
	private Provider provider = null;
	
	//for Instructor
	private Instructor instructor = null;
	
	
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
	
	public CredentialCategoryRequirement getCredentialRequirement() {
		return credentialCategoryRequirement;
	}
	public void setCredentialRequirement(CredentialCategoryRequirement credentialCategoryRequirement) {
		this.credentialCategoryRequirement = credentialCategoryRequirement;
	}
	/**
	 * @return the customFieldId
	 */
	public long getCustomFieldId() {
		return customFieldId;
	}
	/**
	 * @param customFieldId the customFieldId to set
	 */
	public void setCustomFieldId(long customFieldId) {
		this.customFieldId = customFieldId;
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
	 * @return the customFieldDescription
	 */
	public String getCustomFieldDescription() {
		return customFieldDescription;
	}
	/**
	 * @param customFieldDescription the customFieldDescription to set
	 */
	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}
	/**
	 * @return the options
	 */
	public List<CustomFieldValueChoice> getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(List<CustomFieldValueChoice> options) {
		this.options = options;
	}
	/**
	 * @return the regulator
	 */
	public Regulator getRegulator() {
		return regulator;
	}
	/**
	 * @param regulator the regulator to set
	 */
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}
	/**
	 * @return the providerApproval
	 */
	public ProviderApproval getProviderApproval() {
		return providerApproval;
	}
	/**
	 * @param providerApproval the providerApproval to set
	 */
	public void setProviderApproval(ProviderApproval providerApproval) {
		this.providerApproval = providerApproval;
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
	 * @return the provider
	 */
	public Provider getProvider() {
		return provider;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	/**
	 * @return the instructor
	 */
	public Instructor getInstructor() {
		return instructor;
	}
	/**
	 * @param instructor the instructor to set
	 */
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	/**
	 * [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	 * @param credentialCategory the credentialCategory to set
	 */
	public void setCredentialCategory(CredentialCategory credentialCategory) {
		this.credentialCategory = credentialCategory;
	}
	/**
	 * [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	 * @return the credentialCategory
	 */
	public CredentialCategory getCredentialCategory() {
		return credentialCategory;
	}

        /**
         * @return the regulatorCategory
         */
        public RegulatorCategory getRegulatorCategory() {
            return regulatorCategory;
        }

        /**
         * @param regulatorCategory the regulatorCategory to set
         */
        public void setRegulatorCategory(RegulatorCategory regulatorCategory) {
            this.regulatorCategory = regulatorCategory;
        }	
}
