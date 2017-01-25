package com.softech.vu360.lms.web.controller.validator.Instructor;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentSubmissionForm;

public class HomeworkAssignmentSubmissionValidator implements Validator{

	
	private static final Logger log = Logger.getLogger(HomeworkAssignmentSubmissionValidator.class.getName());
	
	public boolean supports(Class clazz)
	{
		return HomeworkAssignmentSubmissionValidator.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj , Errors errors)
	{
		HomeworkAssignmentSubmissionForm form = (HomeworkAssignmentSubmissionForm) obj;
		if( HomeworkAssignmentCourse.SCORE_METHOD_SCORE.equalsIgnoreCase(form.getScoringMethod()) )
		{
			long score = 0;
			try{
				score = Long.parseLong(form.getScore());
				if(score < 0 || score > 100 )
					errors.rejectValue("scoreNotNumber", "error.homework.editScore.score.outofrange","");
			}catch(NumberFormatException nfe){
				errors.rejectValue("scoreNotNumber", "error.homework.editScore.score.invalid","");	
			}
		}
	}
}
