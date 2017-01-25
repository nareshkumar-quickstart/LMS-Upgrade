package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.EnrollmentSubscriptionDetailsForm;
import com.softech.vu360.lms.web.controller.model.SubscriptionItemForm;

public class AddSubscriptionEnrollmentValidator  implements Validator {

	
		
	@Override
	public boolean supports(Class clazz) {
		// TODO Auto-generated method stub
		return EnrollmentSubscriptionDetailsForm.class.isAssignableFrom(clazz);
		
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		EnrollmentSubscriptionDetailsForm enrollmentSubscriptionDetailsForm = (EnrollmentSubscriptionDetailsForm)obj;
		validateSecondPage(enrollmentSubscriptionDetailsForm,errors);
		validateThirdPage(enrollmentSubscriptionDetailsForm,errors);
		
	}

	public void validateSecondPage(EnrollmentSubscriptionDetailsForm form, Errors errors) {
		if( form.getSelectedLearners() == null || form.getSelectedLearners().size() <= 0 ) 
			errors.rejectValue("learners", "error.addEnrollment.LearnerRequired");
		
	}
	
	public void validateThirdPage(EnrollmentSubscriptionDetailsForm form, Errors errors) {
		if( form.getSelectedSubscriptions() == null || form.getSelectedSubscriptions().size() <= 0 ) 
			errors.rejectValue("subscriptionList", "error.assignEnrollmentBySubscription.subscriptionRequired");
		
		for(SubscriptionItemForm selectedSubscription : form.getSelectedSubscriptions())
		{
			if(!selectedSubscription.isSeatavailable())
				errors.rejectValue("subscriptionList", "error.assignEnrollmentBySubscription.seatsnotavailable");
		}
	}
	
	
}
