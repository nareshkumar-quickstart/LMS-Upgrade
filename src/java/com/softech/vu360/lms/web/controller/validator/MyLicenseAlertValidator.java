package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseAlertForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * 
 * @author haider.ali
 *
 */
public class MyLicenseAlertValidator implements Validator{
	
	private static final Logger log = Logger.getLogger(MyLicenseAlertValidator.class.getName());
	private LearnerLicenseAlertService learnerLicenseAlertService;
	private LearnerLicenseService learnerLicenseService; 
	

	private VU360UserService vu360UserService;
	
	

	

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		 return LearnerLicenseAlertForm.class.isAssignableFrom(clazz);
		
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		LearnerLicenseAlertForm form = (LearnerLicenseAlertForm)target;
		validatePage1Form(form,errors);
		validatePage2Form(form,errors);
	}

	
	//validate page 1, userName
		public void validatePage1Form(LearnerLicenseAlertForm form, Errors errors) {
			
			errors.setNestedPath("");
			int count;
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			count = learnerLicenseAlertService.getLearnerLicenseAlert(user.getLearner().getId(), form.getLicenseoflearnerId());
			if(count > 0)
			{
				errors.rejectValue("licenseoflearnerId","error.LearnerLicenseAlert.licenseoflearnerId","");
			}
		}
	 
		//validate page 2, password
		public void validatePage2Form(LearnerLicenseAlertForm form, Errors errors) {
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.password", "Field name is required.");
			
            errors.setNestedPath("");
            if (form.getDays() != null)
            {
				if(!StringUtils.isNumeric(form.getDays().toString()))
				{
					errors.rejectValue("days","error.LearnerLicenseAlert.days","");
				}
				else if(FieldsValidation.isInValidGlobalName(form.getDays().toString())){
					errors.rejectValue("days", "error.LearnerLicenseAlert.days.invalidText","");
				}
            }
            else
            {
            	errors.rejectValue("days","error.LearnerLicenseAlert.days","");
            }
            if(form.isAfter() != true && form.isBefore() != true)
            {
            	errors.rejectValue("before","error.LearnerLicenseAlert.BeforeorAfter","");
            }
		}
	 
		//validate page 3, remark
		public void validatePage3Form(Object target, Errors errors) {
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "remark", "required.remark", "Field name is required.");
		}
		
		public LearnerLicenseAlertService getLearnerLicenseAlertService() {
			return learnerLicenseAlertService;
		}

		public void setLearnerLicenseAlertService(
				LearnerLicenseAlertService learnerLicenseAlertService) {
			this.learnerLicenseAlertService = learnerLicenseAlertService;
		}
		
		public VU360UserService getVu360UserService() {
			return vu360UserService;
		}

		public void setVu360UserService(VU360UserService vu360UserService) {
			this.vu360UserService = vu360UserService;
		}
		
		public LearnerLicenseService getLearnerLicenseService() {
			return learnerLicenseService;
		}

		public void setLearnerLicenseService(LearnerLicenseService learnerLicenseService) {
			this.learnerLicenseService = learnerLicenseService;
		}
}
