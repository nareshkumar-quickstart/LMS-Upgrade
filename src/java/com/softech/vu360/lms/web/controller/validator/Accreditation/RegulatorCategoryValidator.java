package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCategoryForm;

/**
 * This {@link Validator} implementation will validate
 * an instance of {@link RegulatorCategoryForm}.
 */
public class RegulatorCategoryValidator implements Validator {

    private static final Logger log = Logger.getLogger(RegulatorCategoryValidator.class.getName());

    @Override
    public boolean supports(Class clazz) {
        return RegulatorCategoryForm.class.isAssignableFrom(clazz);
    }

    /**
     * Validate an instance of {@link RegulatorCategoryForm}
     * @param regulatorCategoryForm 
     * @param regulatorCategoryForm
     */
    @Override
    public void validate(Object regulatorCategoryForm, Errors errors) {
        RegulatorCategoryForm categoryForm = (RegulatorCategoryForm) regulatorCategoryForm;

        if ("Select Category Type".equalsIgnoreCase(categoryForm.getCategoryType())) {
            errors.rejectValue("categoryType", "error.editRegulatorCategory.categoryType", "");
        }
        if (StringUtils.isEmpty(categoryForm.getDisplayName())) {
            errors.rejectValue("displayName", "error.editRegulatorCategory.displayName", "");
        }

        if ((categoryForm.getReportingTimeframeStr().length() > 0 &&
                !NumberUtils.isDigits(categoryForm.getReportingTimeframeStr())) ||
        		categoryForm.getReportingTimeframeStr().length() <= 0

                ) {
        	errors.rejectValue("reportingTimeframeStr", "error.editRegulatorCategory.reportingTimeframe");
        }
        else if(Integer.parseInt(categoryForm.getReportingTimeframeStr()) < 0)
        {
        	errors.rejectValue("reportingTimeframeStr", "error.editRegulatorCategory.negative.reportingTimeframe");
        }
        
        if(categoryForm.isCertificateOrCardRequired()){
        	    if(StringUtils.isBlank(categoryForm.getShippableItemName())){
        	         		errors.rejectValue("shippableItemName", "error.editRegulatorCategory.blank.shippableItemName");
        	    }
        	    if ((categoryForm.getShippableTimeframeStr().length() > 0 &&
        	                     !NumberUtils.isDigits(categoryForm.getShippableTimeframeStr())) ||
        	             		categoryForm.getShippableTimeframeStr().length() <= 0){
        	         		errors.rejectValue("shippableTimeframeStr", "error.editRegulatorCategory.blank.shippableTimeframeStr");
        	    }
        }
        
        if ((categoryForm.getValidationFrequencyStr().length() > 0 &&
                !NumberUtils.isDigits(categoryForm.getValidationFrequencyStr())) ||
        		categoryForm.getValidationFrequencyStr().length() <= 0) {
        	errors.rejectValue("validationFrequencyStr", "error.editRegulatorCategory.validationFrequency");
        }
        else if(Double.parseDouble(categoryForm.getValidationFrequencyStr()) < 0)	{
        	errors.rejectValue("validationFrequencyStr", "error.editRegulatorCategory.negative.validationFrequency");
        }
        
        if ((categoryForm.getMaximumOnlineHoursStr().length() > 0 &&
                !NumberUtils.isDigits(categoryForm.getMaximumOnlineHoursStr())) ||
        		categoryForm.getMaximumOnlineHoursStr().length() <= 0) {
        	errors.rejectValue("maximumOnlineHoursStr", "error.editRegulatorCategory.maximumOnlineHours");
        }
        else if(Integer.parseInt(categoryForm.getMaximumOnlineHoursStr()) < 0)	{
        	errors.rejectValue("maximumOnlineHoursStr", "error.editRegulatorCategory.negative.maximumOnlineHours");
        }
        
        if ((categoryForm.getMinimumSeatTimeStr().length() > 0 &&
                !NumberUtils.isDigits(categoryForm.getMinimumSeatTimeStr())) ||
        		categoryForm.getMinimumSeatTimeStr().length() <= 0) {
        	errors.rejectValue("minimumSeatTimeStr", "error.editRegulatorCategory.minimumSeatTime");
        }
        else if(Integer.parseInt(categoryForm.getMinimumSeatTimeStr()) < 0)	{
        	errors.rejectValue("minimumSeatTimeStr", "error.editRegulatorCategory.negative.minimumSeatTime");
        }
        
        if ((categoryForm.getPreAssessmentPassingRatePercentageStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getPreAssessmentPassingRatePercentageStr())) ||
        		categoryForm.getPreAssessmentPassingRatePercentageStr().length() <= 0) {
        	errors.rejectValue("preAssessmentPassingRatePercentageStr", "error.editRegulatorCategory.preAssessmentPassingRatePercentage");
        }
        else if(Double.parseDouble(categoryForm.getPreAssessmentPassingRatePercentageStr()) < 0)	{
        	errors.rejectValue("preAssessmentPassingRatePercentageStr", "error.editRegulatorCategory.negative.preAssessmentPassingRatePercentage");
        }
        
        if ((categoryForm.getPreAssessmentNumberOfQuestionsStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getPreAssessmentNumberOfQuestionsStr())) ||
        		categoryForm.getPreAssessmentNumberOfQuestionsStr().length() <= 0) {
        	errors.rejectValue("preAssessmentNumberOfQuestionsStr", "error.editRegulatorCategory.preAssessmentNumberOfQuestions");
        }
        else if(Double.parseDouble(categoryForm.getPreAssessmentNumberOfQuestionsStr()) < 0)	{
        	errors.rejectValue("preAssessmentNumberOfQuestionsStr", "error.editRegulatorCategory.negative.preAssessmentNumberOfQuestions");
        }
        
        if ((categoryForm.getQuizPassingRatePercentageStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getQuizPassingRatePercentageStr())) ||
        		categoryForm.getQuizPassingRatePercentageStr().length() <= 0) {
        	errors.rejectValue("quizPassingRatePercentageStr", "error.editRegulatorCategory.quizPassingRatePercentage");
        }
        else if(Double.parseDouble(categoryForm.getQuizPassingRatePercentageStr()) < 0)	{
        	errors.rejectValue("quizPassingRatePercentageStr", "error.editRegulatorCategory.negative.quizPassingRatePercentage");
        }
        
        if ((categoryForm.getQuizNumberOfQuestionsStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getQuizNumberOfQuestionsStr())) ||
        		categoryForm.getQuizNumberOfQuestionsStr().length() <= 0) {
        	errors.rejectValue("quizNumberOfQuestionsStr", "error.editRegulatorCategory.quizNumberOfQuestions");
        }
        else if(Double.parseDouble(categoryForm.getQuizNumberOfQuestionsStr()) < 0)	{
        	errors.rejectValue("quizNumberOfQuestionsStr", "error.editRegulatorCategory.negative.quizNumberOfQuestions");
        }
        
        if ((categoryForm.getFinalAssessmentPassingRatePercentageStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getFinalAssessmentPassingRatePercentageStr())) ||
        		categoryForm.getFinalAssessmentPassingRatePercentageStr().length() <= 0) {
        	errors.rejectValue("finalAssessmentPassingRatePercentageStr", "error.editRegulatorCategory.finalAssessmentPassingRatePercentage");
        }
        else if(Double.parseDouble(categoryForm.getFinalAssessmentPassingRatePercentageStr()) < 0)	{
        	errors.rejectValue("finalAssessmentPassingRatePercentageStr", "error.editRegulatorCategory.negative.finalAssessmentPassingRatePercentage");
        }
        
        if ((categoryForm.getFinalAssessmentNumberOfQuestionsStr().length() > 0 && !NumberUtils.isNumber(categoryForm.getFinalAssessmentNumberOfQuestionsStr())) ||
        		categoryForm.getFinalAssessmentNumberOfQuestionsStr().length() <= 0) {
        	errors.rejectValue("finalAssessmentNumberOfQuestionsStr", "error.editRegulatorCategory.finalAssessmentNumberOfQuestions");
        }
        else if(Double.parseDouble(categoryForm.getFinalAssessmentNumberOfQuestionsStr()) < 0)	{
        	errors.rejectValue("finalAssessmentNumberOfQuestionsStr", "error.editRegulatorCategory.negative.finalAssessmentNumberOfQuestions");
        }
        
        if ((categoryForm.getProviderApprovalPeriodStr().length() > 0 && !NumberUtils.isDigits(categoryForm.getProviderApprovalPeriodStr())) ||
        		categoryForm.getProviderApprovalPeriodStr().length() <= 0) {
        	errors.rejectValue("providerApprovalPeriodStr", "error.editRegulatorCategory.providerApprovalPeriod");
        }
        else if(Integer.parseInt(categoryForm.getProviderApprovalPeriodStr()) < 0)	{
        	errors.rejectValue("providerApprovalPeriodStr", "error.editRegulatorCategory.negative.providerApprovalPeriod");
        }
        
        if ((categoryForm.getCourseApprovalPeriodStr().length() > 0 &&
                !NumberUtils.isDigits(categoryForm.getCourseApprovalPeriodStr()))
                ||
        		categoryForm.getCourseApprovalPeriodStr().length() <= 0) {
        	errors.rejectValue("courseApprovalPeriodStr", "error.editRegulatorCategory.courseApprovalPeriod");
        }
        else if(Integer.parseInt(categoryForm.getCourseApprovalPeriodStr()) < 0)	{
        	errors.rejectValue("courseApprovalPeriodStr", "error.editRegulatorCategory.negative.courseApprovalPeriod");
        }
        
    }
}
