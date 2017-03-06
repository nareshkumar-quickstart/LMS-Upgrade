package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Arijit
 *
 */
public class LearnerProfileForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>();
	private VU360User vu360User;
	private String eventSource = "" ;
	private String isCountrySelected="";
	private String saved = "false";
	private boolean notifyOnLicenseExpire;
	
	private List<ValidationQuestion> lstValidationQuestion = new ArrayList<>();
	private Map<Object,Object> mpValidationQuestion = new HashMap<>();
	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	

	//private List<CustomField> customFields = new ArrayList<CustomField>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	private CustomField customField; //for add CustomField
	private List<CustomFieldValue> customFieldValueList = new ArrayList<CustomFieldValue>(); //for add CustomField
	private List<TimeZone> timeZoneList = new ArrayList<TimeZone>(); //for add CustomField
	private long timeZoneId = 0;
	private List<LicenseOfLearner> learnerOfLicense=null;	 
	private LearnerValidationQASetDTO learnerValidationQASet;
	private boolean hasAnyInProgressEnrollmentOfStandardValidationQuestions = false;
	
	/**
	 * @return the creditReportingFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> getCreditReportingFields() {
		return creditReportingFields;
	}
	/**
	 * @param creditReportingFields the creditReportingFields to set
	 */
	public void setCreditReportingFields(
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields) {
		this.creditReportingFields = creditReportingFields;
	}
	/**
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return vu360User;
	}
	/**
	 * @param vu360User the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}
	/**
	 * @return the eventSource
	 */
	public String getEventSource() {
		return eventSource;
	}
	public String getIsCountrySelected() {
		return isCountrySelected;
	}
	public void setIsCountrySelected(String isCountrySelected) {
		this.isCountrySelected = isCountrySelected;
	}
	/**
	 * @param eventSource the eventSource to set
	 */
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	public String getSaved() {
		return saved;
	}
	public void setSaved(String saved) {
		this.saved = saved;
	}
	/**
	 * @return the customFields2
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}
	/**
	 * @param customFields2 the customFields2 to set
	 */
	public void setCustomFields(List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}
	/**
	 * @return the customFieldValueList
	 */
	public List<CustomFieldValue> getCustomFieldValueList() {
		return customFieldValueList;
	}
	/**
	 * @param customFieldValueList the customFieldValueList to set
	 */
	public void setCustomFieldValueList(List<CustomFieldValue> customFieldValueList) {
		this.customFieldValueList = customFieldValueList;
	}
	public void setTimeZoneList(List<TimeZone> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}
	public List<TimeZone> getTimeZoneList() {
		return timeZoneList;
	}
	/**
	 * @return the timeZoneId
	 */
	public long getTimeZoneId() {
		return timeZoneId;
	}
	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId( long timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public List<LicenseOfLearner> getLearnerOfLicense() {
		return learnerOfLicense;
	}
	public void setLearnerOfLicense(List<LicenseOfLearner> learnerOfLicense) {
		this.learnerOfLicense = learnerOfLicense;
	}
	

	public LearnerValidationQASetDTO getLearnerValidationQASet() {
		return learnerValidationQASet;
	}
	public void setLearnerValidationQuestions(
			LearnerValidationQASetDTO learnerValidationQASet) {
		this.learnerValidationQASet = learnerValidationQASet;
	}
	
	public boolean isNotifyOnLicenseExpire() {
		return notifyOnLicenseExpire;
	}
	
	public void setNotifyOnLicenseExpire(boolean notifyOnLicenseExpire) {
		this.notifyOnLicenseExpire = notifyOnLicenseExpire;
	}
	public List<ValidationQuestion> getLstValidationQuestion() {
		return lstValidationQuestion;
	}
	public void setLstValidationQuestion(List<ValidationQuestion> lstValidationQuestion) {
	    this.lstValidationQuestion = lstValidationQuestion;
	}
	public Map<Object, Object> getMpValidationQuestion() {
	    return mpValidationQuestion;
	}
	public void setMpValidationQuestion(Map<Object, Object> mpValidationQuestion) {
	    this.mpValidationQuestion = mpValidationQuestion;
	}
	public boolean isHasAnyInProgressEnrollmentOfStandardValidationQuestions() {
	    return hasAnyInProgressEnrollmentOfStandardValidationQuestions;
	}
	
	public void setHasAnyInProgressEnrollmentOfStandardValidationQuestions(boolean hasAnyInProgressEnrollmentOfStandardValidationQuestions) {
	   this.hasAnyInProgressEnrollmentOfStandardValidationQuestions = hasAnyInProgressEnrollmentOfStandardValidationQuestions;
	}
}