package com.softech.vu360.lms.web.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddCustomCoursesForm;

public class AddCustomCoursesValidator implements Validator{

	private static final Logger log = Logger.getLogger(ManageSurveyValidator.class.getName());
	//private DistributorService  distributorService = null;
	public boolean supports(Class clazz) {
		return AddCustomCoursesForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddCustomCoursesForm addCustomCoursesForm = (AddCustomCoursesForm)obj;
		validatePage(addCustomCoursesForm,errors);
	}

	public void validatePage(AddCustomCoursesForm form, Errors errors) {

		if (StringUtils.isBlank(form.getCourseName())){
			errors.rejectValue("courseName", "error.manager.addCustomCourses.courcename");
		}

		if (StringUtils.isBlank(form.getLink())){
			errors.rejectValue("link", "error.manager.addCustomCourses.link");
		}else if(!this.validateLink(form.getLink(), errors)){
			errors.rejectValue("link", "error.manager.addCustomCourses.link.invalid");
		}
		/*if (StringUtils.isBlank(form.getDescription())){
			errors.rejectValue("description", "error.manager.addCustomCourses.description");
		}*/

		/*if (StringUtils.isBlank(form.getHours())){
			errors.rejectValue("hours", "error.manager.addCustomCourses.hours");
		}*/
		/*else if (!StringUtils.isNumeric(form.getHours())){
			errors.rejectValue("hours", "error.manager.addCustomCourses.hoursNumeric");
		}*/
	}

	public boolean validateLink(String link, Errors errors) {
		boolean isValid = false;
		String expression = "^(https?://(.*?)\\.(.*))$";
			/*+"([A-Za-z]{2,}+\\.)"
			+"([A-Za-z0-9]{3,}+\\.)"
			+"?([A-Za-z]{2,}+\\.)"
			+"?[A-Za-z]{2,4}$";*/
		CharSequence inputStr = link;
		{
			//Make the comparison case-insensitive. 
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if(matcher.matches()){
				isValid = true;
			}

			return isValid;
		}
	}

}
