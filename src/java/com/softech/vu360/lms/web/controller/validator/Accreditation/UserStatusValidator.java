package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageUserStatusForm;
import com.softech.vu360.util.UserStatusUpdateUtil;

public class UserStatusValidator implements Validator {
	
	private UserStatusUpdateUtil userStatusUpdateUtil=null;

	@Override
	public boolean supports(Class clazz) {
		return ManageUserStatusForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		
		ManageUserStatusForm form = (ManageUserStatusForm) obj;
		
		boolean validationResponse = userStatusUpdateUtil.statusesChangeAllowed(form.getUserstatuses(), form.getUpdate_courseStatus(), form.isReversalPermission());
		
		if(!validationResponse)	{
			errors.rejectValue("userstatuses", "lms.accraditation.manageUserStatus.update.error");
		}
		
	}

	public UserStatusUpdateUtil getUserStatusUpdateUtil() {
		return userStatusUpdateUtil;
	}

	public void setUserStatusUpdateUtil(UserStatusUpdateUtil userStatusUpdateUtil) {
		this.userStatusUpdateUtil = userStatusUpdateUtil;
	}

}
