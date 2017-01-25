package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.web.controller.model.ManageLicenseForm;

public class ManageLicenseValidator implements Validator{

    private LearnerLicenseService learnerLicenseService =null;
    private LearnerLicenseAlertService learnerLicenseAlertService = null;
	


	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageLicenseForm form = (ManageLicenseForm)obj;
		validateFirstPage(form,errors);
	}
    public void validateFirstPage( ManageLicenseForm form, Errors errors ) {
		
    	errors.setNestedPath("");
    	
    	if(form.getLicenseOfLearnerId() != null && form.getLicenseOfLearner() != null)
    	{
    		long[] learnerlicenseid = ArrayUtils.toPrimitive(form.getLicenseOfLearnerId());
    		int lstlearnerlicensealert = learnerLicenseService.getLicenseAlert(learnerlicenseid);
    		if(lstlearnerlicensealert > 0)
    		{
	    		errors.rejectValue("licenseOfLearner", "lms.mylicense.associatedAlert.error");
	    	}
    		
    	}
    	
		
	}

	public LearnerLicenseService getLearnerLicenseService() {
		return learnerLicenseService;
	}

	public void setLearnerLicenseService(LearnerLicenseService learnerLicenseService) {
		this.learnerLicenseService = learnerLicenseService;
	}
	

	public LearnerLicenseAlertService getLearnerLicenseAlertService() {
		return learnerLicenseAlertService;
	}

	public void setLearnerLicenseAlertService(
			LearnerLicenseAlertService learnerLicenseAlertService) {
		this.learnerLicenseAlertService = learnerLicenseAlertService;
	}
	

}
