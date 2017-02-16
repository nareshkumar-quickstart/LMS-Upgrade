package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.accreditation.CourseConfigForm;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;

/**
 * @author PG
 * @created on 5-july-2009
 *
 */
public class AddCourseConfigValidator implements Validator {

	public boolean supports(Class clazz) {
		return CourseConfigForm.class.isAssignableFrom(clazz);
	}
	
	public void validate( Object obj, Errors errors ) {
		CourseConfigForm form = (CourseConfigForm)obj;
		validateApprovalCourseConfig(form,errors);
	}
	
	public void validateStep1(CourseConfigForm form, Errors errors) {		
		if(StringUtils.isBlank(form.getCourseConfiguration().getCourseConfigTemplate().getName())){
			errors.rejectValue("courseConfiguration.courseConfigTemplate.name", "error.courseConfigTemplate.name");
		}else if(FieldsValidation.isInValidGlobalName(form.getCourseConfiguration().getCourseConfigTemplate().getName())){
			errors.rejectValue("courseConfiguration.courseConfigTemplate.name", "error.courseConfigTemplate.name.invalid");
		}
	}
	
	public void validateStep2(CourseConfigForm form, Errors errors) {
            validateTimeAmount(form, errors);
            //LMS-12864 Start
            if(form.isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified())
            {
            	validateMustCompleteTime(form,errors);
            }
            //LMS-12864 End
	}
        
	private void validateMustCompleteTime(CourseConfigForm form, Errors errors) {
		 int mustCompleteTime=form.getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration();
         if( mustCompleteTime < 0 || mustCompleteTime > 1000){
             errors.rejectValue("courseConfiguration.mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration", 
                 "error.courseConfiguration.coursepolicy.mustStartCourseWithinTimeAfterRegistration");
         }
     }
	
        private final void validateTimeAmount(CourseConfigForm form, Errors errors){
            if(! FieldsValidation.isNumeric(form.getIdleTimeAmount())) {
                    errors.rejectValue("courseConfiguration.idleTimeAmount", "error.courseConfiguration.idleTimeAmount");
            }else{
                long idleTimeAmount=FormUtil.parseNumber(form.getIdleTimeAmount());
                if( idleTimeAmount < 301 || idleTimeAmount > 1800){
                    errors.rejectValue("courseConfiguration.idleTimeAmount", 
                        "error.courseConfiguration.idleTimeAmount.rangeViolation");
                }
            }
        }
        
	public void validateStep3(CourseConfigForm form, Errors errors) {
            validateNumberOfCourseLaunhces(form, errors);
            validateDaysAfterRegistration(form, errors);
            validateAfterFirstCourseLaunch(form, errors);
	}
        
        private void validateDaysAfterRegistration(CourseConfigForm form, Errors errors) {
            String strDaysOfRegistraion = form.getDaysOfRegistraion();
            if( NumberUtils.isNumber(strDaysOfRegistraion) == false){
                errors.rejectValue("courseConfiguration.daysOfRegistraion", 
                    "error.courseConfiguration.timeDaysofRegistration");
                return;
            }
            long daysOfRegistration = NumberUtils.toLong(strDaysOfRegistraion);
            if( daysOfRegistration < 0 || daysOfRegistration > 1000){
                errors.rejectValue("courseConfiguration.minutesAfterFirstCourseLaunch", 
                    "error.courseConfiguration.timeDaysofRegistration.rangeViolation");
            }
        }
        
        private void validateAfterFirstCourseLaunch(CourseConfigForm form, Errors errors) {
            String minutesAfterFirstCourseLaunch = form.getMinutesAfterFirstCourseLaunch();
            if( NumberUtils.isNumber(minutesAfterFirstCourseLaunch) == false){
                errors.rejectValue("courseConfiguration.numberOfCoursesLaunch", 
                    "error.courseConfiguration.timeAfterFirstCourseLaunch");
                return;
            }
            long afterFirstCourseLaunch = NumberUtils.toLong(minutesAfterFirstCourseLaunch, -1);
            if( afterFirstCourseLaunch < 0 || afterFirstCourseLaunch > 1000){
                errors.rejectValue("courseConfiguration.numberOfCoursesLaunch", 
                    "error.courseConfiguration.timeAfterFirstCourseLaunch.rangeViolation");
            }
        }
        
        private void validateNumberOfCourseLaunhces(CourseConfigForm form, Errors errors) {
            String numberOfCoursesLaunch = form.getNumberOfCoursesLaunch();
            if( NumberUtils.isNumber(numberOfCoursesLaunch) == false){
                errors.rejectValue("courseConfiguration.numberOfCoursesLaunch", 
                    "error.courseConfiguration.numberOfCourseLaunches");
                return;
            }
            long courseLaunches = NumberUtils.toLong(numberOfCoursesLaunch, -1);
            if( courseLaunches < 1 || courseLaunches > 1000){
                errors.rejectValue("courseConfiguration.numberOfCoursesLaunch", 
                    "error.courseConfiguration.numberOfCourseLaunches.rangeViolation");
            }
        }
        
	public void validateStep4(CourseConfigForm form, Errors errors) {
            validateNumberOfQuestions("preassessmentNoquestion", form.getPreassessmentNoquestion(), errors);
            validateMasteryScore("preassessmentMasteryscore", form.getPreassessmentMasteryscore(), errors);
            validateAutomaticGradeAssessment("preassessmentEnforcemaximumtimelimit", form.getPreassessmentEnforcemaximumtimelimit(), errors);
            validateAssessmentAttemptNo("preassessmentMaximumnoattempt", form.getPreassessmentMaximumnoattempt(), errors);
	}
        
        public void validateStep5(CourseConfigForm form, Errors errors) {
            validateNumberOfQuestions("quizNoquestion", form.getQuizAssessmentNoquestion(), errors);
            validateMasteryScore("quizMasteryscore", form.getQuizAssessmentMasteryscore(), errors);
            validateAutomaticGradeAssessment("quizEnforcemaximumtimelimit", 
                form.getQuizAssessmentEnforcemaximumtimelimit(), errors);
            validateAssessmentAttemptNo("quizMaximumnoattempt", form.getQuizAssessmentMaximumnoattempt(), errors);
	}
        
	public void validateStep6(CourseConfigForm form, Errors errors) {
            validateNumberOfQuestions("postassessmentNoquestion", form.getPostassessmentNoquestion(), errors);
            validateMasteryScore("postassessmentMasteryscore", form.getPostassessmentMasteryscore(), errors);
            validateAutomaticGradeAssessment("postassessmentEnforcemaximumtimelimit", 
                form.getPostassessmentEnforcemaximumtimelimit(), errors);
            validateAssessmentAttemptNo("postassessmentMaximumnoattempt", form.getPostassessmentMaximumnoattempt(), errors);
	}
        
        private void validateAssessmentAttemptNo(String formProperty, String strAssessmentAttemptNo, Errors errors) {
            if( NumberUtils.isNumber(strAssessmentAttemptNo) == false){
                errors.rejectValue("courseConfiguration."  + formProperty, 
                    "error.courseConfiguration.assessmentAttemptNo");
                return;
            }
            
            int assessmentAttemptNo = NumberUtils.toInt(strAssessmentAttemptNo, -1);
            if( assessmentAttemptNo < 0 || assessmentAttemptNo > 9999999){
                errors.rejectValue("courseConfiguration."  + formProperty, 
                    "error.courseConfiguration.assessmentAttemptNo.rangeViolation");
            }
        }
        
        private void validateAutomaticGradeAssessment(String formProperty, String autoGradeAssessmentAfter, Errors errors) {
            if( NumberUtils.isNumber(autoGradeAssessmentAfter) == false){
//                errors.rejectValue("courseConfiguration."  + formProperty, 
//                    "error.courseConfiguration.autoGradeAssessment");
                
                errors.rejectValue("courseConfiguration.preassessmentMaximumnoattempt", 
                    "error.courseConfiguration.autoGradeAssessment");
                return;
            }
            
            int automaticGradeAssesmentAfter = NumberUtils.toInt(autoGradeAssessmentAfter, -1);
            if( automaticGradeAssesmentAfter < 0 || automaticGradeAssesmentAfter > 9999999){
//                errors.rejectValue("courseConfiguration."  + formProperty, 
//                    "error.courseConfiguration.autoGradeAssessment.rangeViolation");
                errors.rejectValue("courseConfiguration.preassessmentMaximumnoattempt", 
                    "error.courseConfiguration.autoGradeAssessment.rangeViolation");
            }
        }
        
        private void validateMasteryScore(String formProperty, String strMasteryScore, Errors errors) {
            if( NumberUtils.isNumber(strMasteryScore) == false){
                errors.rejectValue("courseConfiguration."  + formProperty, 
                    "error.courseConfiguration.masteryScore");
                return;
            }
            
            int masteryScore = NumberUtils.toInt(strMasteryScore, -1);
            if( masteryScore < 0 || masteryScore > 100){
                errors.rejectValue("courseConfiguration."  + formProperty, 
                    "error.courseConfiguration.masteryScore.rangeViolation");
            }
        }
        
        private void validateNumberOfQuestions(String formProperty, String strAssessmentNoquestion, Errors errors) {
            if( NumberUtils.isNumber(strAssessmentNoquestion) == false){
                errors.rejectValue("courseConfiguration." + formProperty, 
                    "error.courseConfiguration.noOfQuestions.rangeViolation");
                return;
            }
            int assessmentNoquestion = NumberUtils.toInt(strAssessmentNoquestion, -1);
            if( assessmentNoquestion < 0 || assessmentNoquestion > 9999999){
                errors.rejectValue("courseConfiguration." + formProperty, 
                    "error.courseConfiguration.noOfQuestions.rangeViolation");
            }
        }
        
	public void validateStep7(CourseConfigForm form, Errors errors) {
            validateTimeBetweenQuestion(form, errors);
            validateSecondsToAnswerEachQuestion(form, errors);
            validateNumberOfMissedAllowed(form, errors);
	}
        
        public void validateStep9(CourseConfigForm form, Errors errors) {
            validateMaximumSeatTimeEnforcement(form, errors);
        }
        
        private void validateMaximumSeatTimeEnforcement(CourseConfigForm form, Errors errors) {
            validateTimeFormat(form, errors);
            
            if( errors.hasFieldErrors("courseConfiguration.seattimeinhour") == false 
                && errors.hasFieldErrors("courseConfiguration.seattimeinmin") == false){
                int seattimeinhour = Integer.valueOf(form.getSeattimeinhour());
                int seatTimeinMinutes = Integer.valueOf(form.getSeattimeinmin());

                validateSeatTimeInHour(seattimeinhour, seatTimeinMinutes, errors);
                validateSeatTimeInMinutes(seatTimeinMinutes, errors);

                int totalHours = seattimeinhour + 
                    Double.valueOf(Math.ceil( 
                        Integer.valueOf(seatTimeinMinutes).doubleValue() / 59d)).intValue();

                if( totalHours < 0 || totalHours > 24){
                    errors.rejectValue("courseConfiguration.seattimeinhour", 
                        "error.courseConfiguration.maximumSeatTimeEnforcement.rangeViolation");
                }
            }
        }
        
        private void validateTimeFormat(CourseConfigForm form, Errors errors){
            if(NumberUtils.isNumber(form.getSeattimeinhour()) == false){
                errors.rejectValue("courseConfiguration.seattimeinhour", 
                    "error.courseConfiguration.seatTimeInHours.rangeViolation");
            }
            if(NumberUtils.isNumber(form.getSeattimeinmin()) == false){
                errors.rejectValue("courseConfiguration.seattimeinmin", 
                    "error.courseConfiguration.seatTimeInMinutes.rangeViolation");
            }
        }
        
        private void validateSeatTimeInHour(int seattimeinhour, int seatTimeinMinutes, Errors errors) {
            if( (seatTimeinMinutes > 0 && seattimeinhour > 23) || (seattimeinhour < 0 || seattimeinhour > 24)){
                errors.rejectValue("courseConfiguration.seattimeinhour", 
                    "error.courseConfiguration.seatTimeInHours.rangeViolation");
            }
        }

        private void validateSeatTimeInMinutes(int seattimeInMinutes, Errors errors) {
            if( seattimeInMinutes < 0 || seattimeInMinutes > 59){
                errors.rejectValue("courseConfiguration.seattimeinmin", 
                    "error.courseConfiguration.seatTimeInMinutes.rangeViolation");
            }
        }
        
        public void validateStep10(CourseConfigForm form, Errors errors) {
            validatePostMinimumSeatTimeBeforeAssessmentStart(form, errors);
        }
        
        private void validatePostMinimumSeatTimeBeforeAssessmentStart(CourseConfigForm form, Errors errors) {
            String strPostMinimumSeatTimeBeforeAssessmentStart = form.getPostMinimumSeatTimeBeforeAssessmentStart();
            if( NumberUtils.isNumber(strPostMinimumSeatTimeBeforeAssessmentStart) == false){
                errors.rejectValue("courseConfiguration.postMinimumSeatTimeBeforeAssessmentStart", 
                    "error.courseConfiguration.minimumSeatTimeBeforeAssessmentUnlocks");
                return;
            }
            int validationTimeBetweenQuestion = NumberUtils.toInt(strPostMinimumSeatTimeBeforeAssessmentStart, -1);
            if( validationTimeBetweenQuestion < 0 || validationTimeBetweenQuestion > 100){
                errors.rejectValue("courseConfiguration.postMinimumSeatTimeBeforeAssessmentStart", 
                    "error.courseConfiguration.minimumSeatTimeBeforeAssessmentUnlocks.rangeViolation");
            }
        }
        
        private void validateTimeBetweenQuestion(CourseConfigForm form, Errors errors) {
            String strValidationTimeBetweenQuestion = form.getValidationTimeBetweenQuestion();
            if( NumberUtils.isNumber(strValidationTimeBetweenQuestion) == false){
                errors.rejectValue("courseConfiguration.validationTimeBetweenQuestion", 
                    "error.courseConfiguration.secondsBetweenEachQuestion");
                return;
            }
            //            /* LMS-21692 */
            //            int validationTimeBetweenQuestion = NumberUtils.toInt(strValidationTimeBetweenQuestion, -1);
            //            if( validationTimeBetweenQuestion < 0 || validationTimeBetweenQuestion > 7200){
            //                errors.rejectValue("courseConfiguration.validationTimeBetweenQuestion", 
            //                    "error.courseConfiguration.secondsBetweenEachQuestion.rangeViolation");
            //            }
        }
        
        private void validateNumberOfMissedAllowed(CourseConfigForm form, Errors errors) {
            String strValidationNoMissedQuestionsAllowed = form.getValidationNoMissedQuestionsAllowed();
            if( NumberUtils.isNumber(strValidationNoMissedQuestionsAllowed) == false){
                errors.rejectValue("courseConfiguration.validationNoMissedQuestionsAllowed", 
                    "error.courseConfiguration.noOfMissedQuestionsAllowed");
                return;
            }
            int validationTimeBetweenQuestion = NumberUtils.toInt(strValidationNoMissedQuestionsAllowed, -1);
            if( validationTimeBetweenQuestion < 0 || validationTimeBetweenQuestion > 1000){
                errors.rejectValue("courseConfiguration.validationNoMissedQuestionsAllowed", 
                    "error.courseConfiguration.noOfMissedQuestionsAllowed.rangeViolation");
            }
        }
        
        
        private void validateSecondsToAnswerEachQuestion(CourseConfigForm form, Errors errors) {
            String strAllowSecondsToAnswerEachQuestion = form.getAllowSecondsToAnswerEachQuestion();
            if( NumberUtils.isNumber(strAllowSecondsToAnswerEachQuestion) == false){
                errors.rejectValue("courseConfiguration.validationTimeBetweenQuestion", 
                    "error.courseConfiguration.allowSecondsToAnswerEachQuestion");
                return;
            }
            int allowSecondsToAnswerEachQuestion = NumberUtils.toInt(strAllowSecondsToAnswerEachQuestion, -1);
            if( allowSecondsToAnswerEachQuestion < 0 || allowSecondsToAnswerEachQuestion > 1000){
                errors.rejectValue("courseConfiguration.validationTimeBetweenQuestion", 
                    "error.courseConfiguration.allowSecondsToAnswerEachQuestion.rangeViolation");
            }
        }
        
	public void validateApprovalCourseConfig(CourseConfigForm form, Errors errors) {
		if (StringUtils.isBlank(form.getSelectedCourseConfigID())) {
			errors.rejectValue("selectedCourseConfigID", "error.editApproval.CourseConfig.required");
		}
	}

}