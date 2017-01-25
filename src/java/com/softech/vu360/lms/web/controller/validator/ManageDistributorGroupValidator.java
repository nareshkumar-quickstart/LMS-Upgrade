/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManagerDistributorGroupForm;

/**
 * @author Tapas Mondal
 *
 */
public class ManageDistributorGroupValidator  implements Validator{
	
private static final Logger log = Logger.getLogger(ManageSurveyValidator.class.getName());
	
	public boolean supports(Class clazz) {
		return ManagerDistributorGroupForm.class.isAssignableFrom(clazz);
	}
    
	public void validate(Object obj, Errors errors) {
		ManagerDistributorGroupForm managerDistributorGroupForm = (ManagerDistributorGroupForm)obj;
		validateFirstPage(managerDistributorGroupForm,errors);
		validateSecondPage(managerDistributorGroupForm,errors);
		
	}
	
	public void validateFirstPage(ManagerDistributorGroupForm form, Errors errors) {
		if (StringUtils.isBlank(form.getDistributorGroupName()))		{
			errors.rejectValue("distributorGroupName", "error.surveyName.required");
		}
			}

	public void validateSecondPage(ManagerDistributorGroupForm form, Errors errors) {
		if(form.getDistributors().size() == 0){
			errors.rejectValue("distributors", "error.surveyQuestions.required");
		}
	}

}
