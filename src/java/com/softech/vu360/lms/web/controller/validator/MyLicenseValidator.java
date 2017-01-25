package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseForm;

/**
 * 
 * @author haider.ali
 *
 */
public class MyLicenseValidator implements Validator{
	
	
	private static final Logger log = Logger.getLogger(MyLicenseValidator.class.getName());
	private LearnerLicenseService learnerLicenseService; 

	
	public boolean supports(@SuppressWarnings("rawtypes") Class arg0) {
	  return LearnerLicenseForm.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		LearnerLicenseForm form = (LearnerLicenseForm)target;
		//validatePage1Form(target, errors);
		validatePage2Form(form, errors);
		//validatePage3Form(target, errors);
	}

	
	//validate page 1, userName
		public void validatePage1Form(Object target, Errors errors) {
			//re validation required.
		}
	 
		//validate page 2, password
		public void validatePage2Form(LearnerLicenseForm form, Errors errors) {
			
			 errors.setNestedPath("");

			 if (form.getSelectedCredentialId() != null && form.getLicenseIndustryId() !=null)
            {
        		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            	List<LicenseOfLearner> licOfLearners = learnerLicenseService.getAllLicensesOfLearner(user.getLearner().getId());
            	if(licOfLearners!=null && licOfLearners.size()>0){
            		for (LicenseOfLearner licenseOfLearner : licOfLearners) {
            			IndustryCredential ic = licenseOfLearner.getIndustryCredential();
            			
            			if( ic.getId().intValue() == form.getSelectedCredentialId().intValue() ){
            				errors.rejectValue("selectedCredentialId","lms.mylicense.newLicense.error");
            				break;
            			}
					}
            	}
            }
		}
	 
		//validate page 3, remark
		public void validatePage3Form(Object target, Errors errors) {
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "remark", "required.remark", "Field name is required.");
		}

		public LearnerLicenseService getLearnerLicenseService() {
			return learnerLicenseService;
		}

		public void setLearnerLicenseService(LearnerLicenseService learnerLicenseService) {
			this.learnerLicenseService = learnerLicenseService;
		}

}

