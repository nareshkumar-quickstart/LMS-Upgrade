package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.Modality;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class RegulatorCategoryForm  implements ILMSBaseInterface{

    private static Logger log = Logger.getLogger(RegulatorCategoryForm.class.getName());
    public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
    public static final String CUSTOMFIELD_DATE = "Date";
    public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
    public static final String CUSTOMFIELD_NUMBER = "Number";
    public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
    public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
    public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
    public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
    public static final String CUSTOMFIELD_STATIC = "Static Field";
    private static final long serialVersionUID = 1L;
    // Use for Credit Reporting field -- LMS-13120
    // As this field from this class are used for check Reporting fields in 'ManageAndEditRegulatorCategoryController'
    // so we need to add Telephone number field here also
    public static final String CUSTOMFIELD_TELEPHONE_NUMBER = "Telephone Number";
    
    private long id;
    private Regulator regulator;
    private String categoryType;
    private RegulatorCategory category;
    private String displayName;
    private boolean validationRequired;
    private int validationFrequency;
    private Set<Modality> modalitiesAllowed = new HashSet<Modality>();
    private int maximumOnlineHours;
    private int minimumSeatTime;
    private String minimimumSeatTimeUnit;
    private boolean preAssessmentRequired;
    private boolean quizRequired;
    private boolean finalAssessmentRequired;
    private double preAssessmentPassingRatePercentage;
    private int preAssessmentNumberOfQuestions;
    private double quizPassingRatePercentage;
    private int quizNumberOfQuestions;
    private double finalAssessmentPassingRatePercentage;
    private int finalAssessmentNumberOfQuestions;
    private String certificate;
    private String reporting;
    private String reciprocityNotes;
    private boolean repeatable;
    private String repetabilityNotes;
    private boolean providerApprovalRequired;
    private int providerApprovalPeriod;
    private String providerApprovalPeriodUnit;
    private boolean courseApprovalRequired;
    private int courseApprovalPeriod;
    private String courseApprovalPeriodUnit;
    private boolean reportingRequired;
    private int reportingTimeframe;
    private boolean certificateOrCardRequired;
    private int  shippableItemProcessingTime;
    private String  shippableItemName;
    private List<CreditReportingField> reportingFields = new ArrayList<CreditReportingField>();
    private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
    private List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
    private List<ManageCustomField> manageCreditReportingField = new ArrayList<ManageCustomField>();
    private List<ManageCustomField> manageCustomFields = new ArrayList<ManageCustomField>();
    protected String onlineModality = null;
    protected String correspondenceModality = null;
    protected String classroomModality = null;
    
    protected String validationFrequencyStr = "0";
    protected String maximumOnlineHoursStr = "0";
    protected String minimumSeatTimeStr = "0";
    protected String preAssessmentPassingRatePercentageStr = "0";
    protected String preAssessmentNumberOfQuestionsStr = "0";
    protected String quizPassingRatePercentageStr = "0";
    protected String quizNumberOfQuestionsStr = "0";
    protected String finalAssessmentPassingRatePercentageStr = "0";
    protected String finalAssessmentNumberOfQuestionsStr = "0";
    protected String providerApprovalPeriodStr = "0";
    protected String courseApprovalPeriodStr = "0";    
    private String reportingTimeframeStr = "0";
    private String shippableTimeframeStr = "0";

    /**
     * Reinitializes the form instance properties with regulator category loaded
     * from the database.
     *
     * @param regulatorCategory
     */
    public void loadRegulatorCategory(RegulatorCategory regulatorCategory) {
        this.id = regulatorCategory.getId();
        setRegulator(regulatorCategory.getRegulator());
        this.category=regulatorCategory;
        this.categoryType = regulatorCategory.getCategoryType();
        this.displayName = regulatorCategory.getDisplayName();
        this.shippableItemName = regulatorCategory.getShippableItemName();
        this.validationRequired = regulatorCategory.getValidationRequired();
        this.validationFrequency =  (int) regulatorCategory.getValidationFrequency();
        //this.modalitiesAllowed.clear();
        this.modalitiesAllowed = regulatorCategory.getModalitiesAllowed();
        this.maximumOnlineHours = regulatorCategory.getMaximumOnlineHours();
        this.minimumSeatTime = regulatorCategory.getMinimumSeatTime();
        this.minimimumSeatTimeUnit = regulatorCategory.getMinimimumSeatTimeUnit();
        this.preAssessmentRequired = regulatorCategory.getPreAssessmentRequired();
        this.quizRequired = regulatorCategory.getQuizRequired();
        this.finalAssessmentRequired = regulatorCategory.getFinalAssessmentRequired();
        this.preAssessmentPassingRatePercentage = regulatorCategory.getPreAssessmentPassingRatePercentage();
        this.preAssessmentNumberOfQuestions = regulatorCategory.getPreAssessmentNumberOfQuestions();
        this.quizPassingRatePercentage = regulatorCategory.getQuizPassingRatePercentage();
        this.quizNumberOfQuestions = regulatorCategory.getQuizNumberOfQuestions();
        this.finalAssessmentPassingRatePercentage = regulatorCategory.getFinalAssessmentPassingRatePercentage();
        this.finalAssessmentNumberOfQuestions = regulatorCategory.getFinalAssessmentNumberOfQuestions();
        this.certificate = regulatorCategory.getCertificate();
        this.reporting = regulatorCategory.getReporting();
        this.reciprocityNotes = regulatorCategory.getReciprocityNotes();
        this.repeatable = regulatorCategory.getRepeatable();
        this.repetabilityNotes = regulatorCategory.getRepetabilityNotes();
        this.providerApprovalRequired = regulatorCategory.getProviderApprovalRequired();
        this.providerApprovalPeriod = regulatorCategory.getProviderApprovalPeriod();
        this.providerApprovalPeriodUnit = regulatorCategory.getProviderApprovalPeriodUnit();
        this.courseApprovalRequired = regulatorCategory.getCourseApprovalRequired();
        this.courseApprovalPeriod = regulatorCategory.getCourseApprovalPeriod();
        this.courseApprovalPeriodUnit = regulatorCategory.getCourseApprovalPeriodUnit();
        this.reportingRequired = regulatorCategory.getReportingRequired();
        this.reportingTimeframe = regulatorCategory.getReportingTimeframe();
        this.certificateOrCardRequired = regulatorCategory.getCertificateOrCardRequired();
        this.shippableItemProcessingTime = regulatorCategory.getShippableItemProcessingTime();
        this.shippableItemName = regulatorCategory.getShippableItemName();
        this.reportingFields.clear();
        this.reportingFields.addAll(regulatorCategory.getReportingFields());
        this.customFields.clear();        
        this.customFieldValues.clear();
        this.customFieldValues.addAll(regulatorCategory.getCustomFieldValues());
        
        this.validationFrequencyStr = String.valueOf((int) this.validationFrequency);
        this.maximumOnlineHoursStr = Integer.toString(this.maximumOnlineHours);
        this.minimumSeatTimeStr = Integer.toString(this.minimumSeatTime);
        this.preAssessmentPassingRatePercentageStr = Double.toString(this.preAssessmentPassingRatePercentage);
        this.quizPassingRatePercentageStr = Double.toString(this.quizPassingRatePercentage);
        this.finalAssessmentPassingRatePercentageStr = Double.toString(this.finalAssessmentPassingRatePercentage);
        this.providerApprovalPeriodStr = Integer.toString(this.providerApprovalPeriod);
        this.courseApprovalPeriodStr = Integer.toString(this.courseApprovalPeriod);
        this.reportingTimeframeStr = String.valueOf((int) this.reportingTimeframe);
        this.shippableTimeframeStr = String.valueOf((int) this.shippableItemProcessingTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isValidationRequired() {
        return validationRequired;
    }

    public void setValidationRequired(boolean validationRequired) {
        this.validationRequired = validationRequired;
    }

    public int getValidationFrequency() {
        return Integer.valueOf(validationFrequencyStr);
    }

    public void setValidationFrequency(int validationFrequency) {
        this.validationFrequency = validationFrequency;
    }

    public int getMaximumOnlineHours() {
        return Integer.valueOf(maximumOnlineHoursStr);
    }

    public void setMaximumOnlineHours(int maximumOnlineHours) {
        this.maximumOnlineHours = maximumOnlineHours;
    }

    public int getMinimumSeatTime() {
        return Integer.valueOf(minimumSeatTimeStr);
    }

    public void setMinimumSeatTime(int minimumSeatTime) {
        this.minimumSeatTime = minimumSeatTime;
    }

    public String getMinimimumSeatTimeUnit() {
        return minimimumSeatTimeUnit;
    }

    public void setMinimimumSeatTimeUnit(String minimimumSeatTimeUnit) {
        this.minimimumSeatTimeUnit = minimimumSeatTimeUnit;
    }

    public boolean isPreAssessmentRequired() {
        return preAssessmentRequired;
    }

    public void setPreAssessmentRequired(boolean preAssessmentRequired) {
        this.preAssessmentRequired = preAssessmentRequired;
    }

    public boolean isQuizRequired() {
        return quizRequired;
    }

    public void setQuizRequired(boolean quizRequired) {
        this.quizRequired = quizRequired;
    }

    public boolean isFinalAssessmentRequired() {
        return finalAssessmentRequired;
    }

    public void setFinalAssessmentRequired(boolean finalAssessmentRequired) {
        this.finalAssessmentRequired = finalAssessmentRequired;
    }

    public double getPreAssessmentPassingRatePercentage() {
        return Double.valueOf(preAssessmentPassingRatePercentageStr);
    }

    public void setPreAssessmentPassingRatePercentage(
            double preAssessmentPassingRatePercentage) {
        this.preAssessmentPassingRatePercentage = preAssessmentPassingRatePercentage;
    }

    public int getPreAssessmentNumberOfQuestions() {
        return Integer.valueOf(preAssessmentNumberOfQuestionsStr);
    }

    public void setPreAssessmentNumberOfQuestions(int preAssessmentNumberOfQuestions) {
        this.preAssessmentNumberOfQuestions = preAssessmentNumberOfQuestions;
    }

    public double getQuizPassingRatePercentage() {
        return Double.valueOf(quizPassingRatePercentageStr);
    }

    public void setQuizPassingRatePercentage(double quizPassingRatePercentage) {
        this.quizPassingRatePercentage = quizPassingRatePercentage;
    }

    public int getQuizNumberOfQuestions() {
        return Integer.valueOf(quizNumberOfQuestionsStr);
    }

    public void setQuizNumberOfQuestions(int quizNumberOfQuestions) {
        this.quizNumberOfQuestions = quizNumberOfQuestions;
    }

    public double getFinalAssessmentPassingRatePercentage() {
        return Double.valueOf(finalAssessmentPassingRatePercentageStr);
    }

    public void setFinalAssessmentPassingRatePercentage(
            double finalAssessmentPassingRatePercentage) {
        this.finalAssessmentPassingRatePercentage = finalAssessmentPassingRatePercentage;
    }

    public int getFinalAssessmentNumberOfQuestions() {
        return Integer.valueOf(finalAssessmentNumberOfQuestionsStr);
    }

    public void setFinalAssessmentNumberOfQuestions(
            int finalAssessmentNumberOfQuestions) {
        this.finalAssessmentNumberOfQuestions = finalAssessmentNumberOfQuestions;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getReporting() {
        return reporting;
    }

    public void setReporting(String reporting) {
        this.reporting = reporting;
    }

    public String getReciprocityNotes() {
        return reciprocityNotes;
    }

    public void setReciprocityNotes(String reciprocityNotes) {
        this.reciprocityNotes = reciprocityNotes;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getRepetabilityNotes() {
        return repetabilityNotes;
    }

    public void setRepetabilityNotes(String repetabilityNotes) {
        this.repetabilityNotes = repetabilityNotes;
    }

    public boolean isProviderApprovalRequired() {
        return providerApprovalRequired;
    }

    public void setProviderApprovalRequired(boolean providerApprovalRequired) {
        this.providerApprovalRequired = providerApprovalRequired;
    }

    public double getProviderApprovalPeriod() {
        return Double.valueOf(providerApprovalPeriodStr);
    }

    public void setProviderApprovalPeriod(int providerApprovalPeriod) {
        this.providerApprovalPeriod = providerApprovalPeriod;
    }

    public String getProviderApprovalPeriodUnit() {
        return providerApprovalPeriodUnit;
    }

    public void setProviderApprovalPeriodUnit(String providerApprovalPeriodUnit) {
        this.providerApprovalPeriodUnit = providerApprovalPeriodUnit;
    }

    public boolean isCourseApprovalRequired() {
        return courseApprovalRequired;
    }

    public void setCourseApprovalRequired(boolean courseApprovalRequired) {
        this.courseApprovalRequired = courseApprovalRequired;
    }

    public int getCourseApprovalPeriod() {
        return Integer.valueOf(courseApprovalPeriodStr);
    }

    public void setCourseApprovalPeriod(int courseApprovalPeriod) {
        this.courseApprovalPeriod = courseApprovalPeriod;
    }

    public String getCourseApprovalPeriodUnit() {
        return courseApprovalPeriodUnit;
    }

    public void setCourseApprovalPeriodUnit(String courseApprovalPeriodUnit) {
        this.courseApprovalPeriodUnit = courseApprovalPeriodUnit;
    }

    public List<CreditReportingField> getReportingFields() {
        return reportingFields;
    }

    public void setReportingFields(List<CreditReportingField> reportingFields) {
        this.reportingFields = reportingFields;
    }

    public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(
            List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
        this.customFields = customFields;
    }

    public List<CustomFieldValue> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public Set<Modality> getModalitiesAllowed() {
        return modalitiesAllowed;
    }

    public void setModalitiesAllowed(Set<Modality> modalitiesAllowed) {
        this.modalitiesAllowed = modalitiesAllowed;
    }

    public Regulator getRegulator() {
        return this.regulator;
    }

    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }

    public List<ManageCustomField> getManageCreditReportingField() {
        return manageCreditReportingField;
    }

    public void setManageCreditReportingField(
            List<ManageCustomField> manageCreditReportingField) {
        this.manageCreditReportingField = manageCreditReportingField;
    }

    /**
     * Transforms into {@link RegulatorCategory} from properties of the form.
     * @return {@link RegulatorCategory}
     */
    public RegulatorCategory toRegulatorCategory() {
        return toRegulatorCategory(new RegulatorCategory());
    }

    /**
     * Transforms into {@link RegulatorCategory} from properties of the form.
     * @param category often loaded from the database.
     * @return {@link RegulatorCategory}
     */
    public RegulatorCategory toRegulatorCategory(RegulatorCategory category) {
        if (null == category) {
            throw new IllegalArgumentException("Please provide valid instance of category.");
        }

        category.setRegulator(getRegulator());
        category.setCategoryType(categoryType);
        category.setDisplayName(displayName);
        category.setShippableItemName(shippableItemName);
        category.setValidationRequired(validationRequired);
        //category.getModalitiesAllowed().clear();
        category.setModalitiesAllowed(modalitiesAllowed);
        category.setMinimimumSeatTimeUnit(minimimumSeatTimeUnit);

        category.setPreAssessmentRequired(preAssessmentRequired);
        category.setQuizRequired(quizRequired);
        category.setFinalAssessmentRequired(finalAssessmentRequired);
        category.setPreAssessmentNumberOfQuestions(preAssessmentNumberOfQuestions);
        category.setQuizNumberOfQuestions(quizNumberOfQuestions);
        category.setFinalAssessmentNumberOfQuestions(finalAssessmentNumberOfQuestions);

        category.setCertificate(certificate);
        category.setReporting(reporting);
        category.setReciprocityNotes(reciprocityNotes);

        category.setRepeatable(repeatable);
        category.setRepetabilityNotes(repetabilityNotes);
        category.setProviderApprovalRequired(providerApprovalRequired);
        category.setProviderApprovalPeriodUnit(providerApprovalPeriodUnit);
        category.setCourseApprovalRequired(courseApprovalRequired);
        category.setCourseApprovalPeriodUnit(courseApprovalPeriodUnit);
        category.setReportingRequired(reportingRequired);
        category.setCertificateOrCardRequired(certificateOrCardRequired);
        category.setCustomFieldValues(this.getCustomFieldValues());
        //Please load customfields inside customfields method, use of it here is causing other fuctionality to crash when this crashes.
        //loadCustomFields(category);
             
        
        category.setValidationFrequency(Integer.valueOf(validationFrequencyStr));
        category.setMaximumOnlineHours(Integer.valueOf(maximumOnlineHoursStr));
        category.setMinimumSeatTime(Integer.valueOf(minimumSeatTimeStr));
        category.setPreAssessmentPassingRatePercentage(Double.valueOf(preAssessmentPassingRatePercentageStr));
        category.setQuizPassingRatePercentage(Double.valueOf(quizPassingRatePercentageStr));
        category.setFinalAssessmentPassingRatePercentage(Double.valueOf(finalAssessmentPassingRatePercentageStr));
        category.setProviderApprovalPeriod(Integer.valueOf
                (providerApprovalPeriodStr));
        category.setCourseApprovalPeriod(Integer.valueOf
                (courseApprovalPeriodStr));
        category.setReportingTimeframe(Integer.valueOf(reportingTimeframeStr));
        category.setCertificateOrCardRequired(certificateOrCardRequired);
        category.setShippableItemName(shippableItemName);
        category.setShippableItemProcessingTime(Integer.valueOf(shippableTimeframeStr));

        return category;
    }

    /**
     * Load custom fields from the form.
     * @param regulatorCategory 
     */
//    private void loadCustomFields(RegulatorCategory regulatorCategory) {
//        if (getCustomFields().size() > 0) {
//
//            List<CustomFieldValue> cfValues = new ArrayList<CustomFieldValue>();
//            for (CustomField customField : getCustomFields()) {
//
//                if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {
//
//                    MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField) customField.getCustomFieldRef();
//                    if (multiSelectCustomField.isCheckBox()) {
//
//                        List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
//                        for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()) {
//
//                            if (customFieldValueChoice.isSelected()) {
//                                CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
//                                customFieldValueChoices.add(customFieldValueChoiceRef);
//                            }
//
//                        }
//                        CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//                        Object value = customFieldValue.getValue();
//                        customFieldValue.setCustomField(customField.getCustomFieldRef());
//                        /*  @added by Dyutiman
//                         *  for Encryption...
//                         */
//                        if (customField.getCustomFieldRef().isFieldEncrypted()) {
//                            customFieldValue.setValue(value);
//                        }
//                        customFieldValue.setValueItems(customFieldValueChoices);
//                        cfValues.add(customFieldValue);
//
//                    } else {
//                        List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
//                        if (customField.getSelectedChoices() != null) {
//                            Map<Long, CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long, CustomFieldValueChoice>();
//                            for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()) {
//                                customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
//                            }
//
//                            for (String selectedChoiceIdString : customField.getSelectedChoices()) {
//                                if (customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))) {
//                                    CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
//                                    customFieldValueChoices.add(customFieldValueChoiceRef);
//                                }
//                            }
//                        }
//                        CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//                        Object value = customFieldValue.getValue();
//                        customFieldValue.setCustomField(customField.getCustomFieldRef());
//                        customFieldValue.setValueItems(customFieldValueChoices);
//                        /*
//                         *  for Encryption...
//                         */
//                        if (customField.getCustomFieldRef().isFieldEncrypted()) {
//                            customFieldValue.setValue(value);
//                        }
//                        cfValues.add(customFieldValue);
//                    }
//
//                } else {
//                    CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//                    Object value = customFieldValue.getValue();
//                    customFieldValue.setCustomField(customField.getCustomFieldRef());
//                    /*
//                     *  for Encryption...
//                     */
//                    if (customField.getCustomFieldRef().isFieldEncrypted()) {
//                        customFieldValue.setValue(value);
//                    }
//                    cfValues.add(customFieldValue);
//                }
//            }
//
//            log.debug("customFieldValues: " + cfValues.size());
//            if (cfValues.isEmpty() == false) {
//                regulatorCategory.setCustomFieldValues(cfValues);
//            }
//        }
//    }

    /**
     * Prepares a set of user selected modalities.
     * @return 
     */
    private Set<Modality> getModalities() {
        Set<Modality> modalities = new HashSet<Modality>();

        if (null != onlineModality && onlineModality.length() > 0) {
            modalities.add(createModality("Online"));
        }
        if (null != correspondenceModality && correspondenceModality.length() > 0) {
            modalities.add(createModality("Correspondence"));
        }
        if (null != classroomModality && classroomModality.length() > 0) {
            modalities.add(createModality("Classroom"));
        }

        return modalities;
    }

    /**
     * Creates a new modality with name.
     * @param name
     * @return 
     */
    private Modality createModality(String name) {
        Modality modality = new Modality();
        modality.setName(name);

        return modality;
    }

    /**
     * @return the onlineModality
     */
    public String getOnlineModality() {
        return onlineModality;
    }

    /**
     * @param onlineModality the onlineModality to set
     */
    public void setOnlineModality(String onlineModality) {
        this.onlineModality = onlineModality;
    }

    /**
     * @return the correspondenceModality
     */
    public String getCorrespondenceModality() {
        return correspondenceModality;
    }

    /**
     * @param correspondenceModality the correspondenceModality to set
     */
    public void setCorrespondenceModality(String correspondenceModality) {
        this.correspondenceModality = correspondenceModality;
    }

    /**
     * @return the classroomModality
     */
    public String getClassroomModality() {
        return classroomModality;
    }

    /**
     * @param classroomModality the classroomModality to set
     */
    public void setClassroomModality(String classroomModality) {
        this.classroomModality = classroomModality;
    }

    /**
     * @return the manageCustomFields
     */
    public List<ManageCustomField> getManageCustomFields() {
        return manageCustomFields;
    }

    /**
     * @param manageCustomFields the manageCustomFields to set
     */
    public void setManageCustomFields(List<ManageCustomField> manageCustomFields) {
        this.manageCustomFields = manageCustomFields;
    }

    /**
     * @return the validationFrequencyStr
     */
    public String getValidationFrequencyStr() {
        return validationFrequencyStr;
    }

    /**
     * @param validationFrequencyStr the validationFrequencyStr to set
     */
    public void setValidationFrequencyStr(String validationFrequencyStr) {
        this.validationFrequencyStr = validationFrequencyStr;
    }

    /**
     * @return the maximumOnlineHoursStr
     */
    public String getMaximumOnlineHoursStr() {
        return maximumOnlineHoursStr;
    }

    /**
     * @param maximumOnlineHoursStr the maximumOnlineHoursStr to set
     */
    public void setMaximumOnlineHoursStr(String maximumOnlineHoursStr) {
        this.maximumOnlineHoursStr = maximumOnlineHoursStr;
    }

    /**
     * @return the minimumSeatTimeStr
     */
    public String getMinimumSeatTimeStr() {
        return minimumSeatTimeStr;
    }

    /**
     * @param minimumSeatTimeStr the minimumSeatTimeStr to set
     */
    public void setMinimumSeatTimeStr(String minimumSeatTimeStr) {
        this.minimumSeatTimeStr = minimumSeatTimeStr;
    }

    /**
     * @return the preAssessmentPassingRatePercentageStr
     */
    public String getPreAssessmentPassingRatePercentageStr() {
        return preAssessmentPassingRatePercentageStr;
    }

    /**
     * @param preAssessmentPassingRatePercentageStr the preAssessmentPassingRatePercentageStr to set
     */
    public void setPreAssessmentPassingRatePercentageStr(String preAssessmentPassingRatePercentageStr) {
        this.preAssessmentPassingRatePercentageStr = preAssessmentPassingRatePercentageStr;
    }

    /**
     * @return the preAssessmentNumberOfQuestionsStr
     */
    public String getPreAssessmentNumberOfQuestionsStr() {
        return preAssessmentNumberOfQuestionsStr;
    }

    /**
     * @param preAssessmentNumberOfQuestionsStr the preAssessmentNumberOfQuestionsStr to set
     */
    public void setPreAssessmentNumberOfQuestionsStr(String preAssessmentNumberOfQuestionsStr) {
        this.preAssessmentNumberOfQuestionsStr = preAssessmentNumberOfQuestionsStr;
    }

    /**
     * @return the quizPassingRatePercentageStr
     */
    public String getQuizPassingRatePercentageStr() {
        return quizPassingRatePercentageStr;
    }

    /**
     * @param quizPassingRatePercentageStr the quizPassingRatePercentageStr to set
     */
    public void setQuizPassingRatePercentageStr(String quizPassingRatePercentageStr) {
        this.quizPassingRatePercentageStr = quizPassingRatePercentageStr;
    }

    /**
     * @return the quizNumberOfQuestionsStr
     */
    public String getQuizNumberOfQuestionsStr() {
        return quizNumberOfQuestionsStr;
    }

    /**
     * @param quizNumberOfQuestionsStr the quizNumberOfQuestionsStr to set
     */
    public void setQuizNumberOfQuestionsStr(String quizNumberOfQuestionsStr) {
        this.quizNumberOfQuestionsStr = quizNumberOfQuestionsStr;
    }

    /**
     * @return the finalAssessmentPassingRatePercentageStr
     */
    public String getFinalAssessmentPassingRatePercentageStr() {
        return finalAssessmentPassingRatePercentageStr;
    }

    /**
     * @param finalAssessmentPassingRatePercentageStr the finalAssessmentPassingRatePercentageStr to set
     */
    public void setFinalAssessmentPassingRatePercentageStr(String finalAssessmentPassingRatePercentageStr) {
        this.finalAssessmentPassingRatePercentageStr = finalAssessmentPassingRatePercentageStr;
    }

    /**
     * @return the finalAssessmentNumberOfQuestionsStr
     */
    public String getFinalAssessmentNumberOfQuestionsStr() {
        return finalAssessmentNumberOfQuestionsStr;
    }

    /**
     * @param finalAssessmentNumberOfQuestionsStr the finalAssessmentNumberOfQuestionsStr to set
     */
    public void setFinalAssessmentNumberOfQuestionsStr(String finalAssessmentNumberOfQuestionsStr) {
        this.finalAssessmentNumberOfQuestionsStr = finalAssessmentNumberOfQuestionsStr;
    }

    /**
     * @return the providerApprovalPeriodStr
     */
    public String getProviderApprovalPeriodStr() {
        return providerApprovalPeriodStr;
    }

    /**
     * @param providerApprovalPeriodStr the providerApprovalPeriodStr to set
     */
    public void setProviderApprovalPeriodStr(String providerApprovalPeriodStr) {
        this.providerApprovalPeriodStr = providerApprovalPeriodStr;
    }

    /**
     * @return the courseApprovalPeriodStr
     */
    public String getCourseApprovalPeriodStr() {
        return courseApprovalPeriodStr;
    }

    /**
     * @param courseApprovalPeriodStr the courseApprovalPeriodStr to set
     */
    public void setCourseApprovalPeriodStr(String courseApprovalPeriodStr) {
        this.courseApprovalPeriodStr = courseApprovalPeriodStr;
    }

	public RegulatorCategory getCategory() {
		return category;
	}

	public void setCategory(RegulatorCategory category) {
		this.category = category;
	}

	public boolean isReportingRequired() {
		return reportingRequired;
	}

	public void setReportingRequired(boolean reportingRequired) {
		this.reportingRequired = reportingRequired;
	}

	public double getReportingTimeframe() {
		return Double.valueOf(reportingTimeframeStr);
	}

	public void setReportingTimeframe(int reportingTimeframe) {
		this.reportingTimeframe = reportingTimeframe;
	}

	public String getReportingTimeframeStr() {
		return reportingTimeframeStr;
	}

	public void setReportingTimeframeStr(String reportingTimeframeStr) {
		this.reportingTimeframeStr = reportingTimeframeStr;
	}

	public boolean isCertificateOrCardRequired() {
		return certificateOrCardRequired;
	}

	public void setCertificateOrCardRequired(boolean certificateOrCardRequired) {
		this.certificateOrCardRequired = certificateOrCardRequired;
	}

	public int getShippableItemProcessingTime() {
		return shippableItemProcessingTime;
	}

	public void setShippableItemProcessingTime(int shippableItemProcessingTime) {
		this.shippableItemProcessingTime = shippableItemProcessingTime;
	}

	public String getShippableItemName() {
		return shippableItemName;
	}

	public void setShippableItemName(String shippableItemName) {
		this.shippableItemName = shippableItemName;
	}

	public String getShippableTimeframeStr() {
		return shippableTimeframeStr;
	}

	public void setShippableTimeframeStr(String shippableTimeframeStr) {
		this.shippableTimeframeStr = shippableTimeframeStr;
	}
    
}
