package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.List;

import javax.inject.Inject;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.repositories.CourseApprovalRepository;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.RequirementForm;

/**
 * 
 * @author Saptarshi
 *
 */

public class AddApprovalRequirementValidator implements Validator {
	@Inject
	private CourseApprovalRepository courseApprovalRepository;
	
	public boolean supports(Class clazz) {
		return RequirementForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RequirementForm form = (RequirementForm)obj;
		validateCredential(form, errors);
		validateRequirement(form, errors);
	}

	public void validateCredential(RequirementForm form, Errors errors) {

		boolean selected = false;
		List<ApprovalCredential> appCredentials = form.getApprovalCredential();
		for( ApprovalCredential appCred : appCredentials ) {
			if( appCred.isSelected() ) {
				selected = true;
				break;
			}
		}
		if( !selected ) {
			errors.rejectValue("approvalCredential", "error.editApproval.addCredential.notselected");
		}
	}
	
	public void validateRequirement(RequirementForm form, Errors errors) {
		
		if (form.getRequirements() == null || form.getRequirements().size() == 0) {
			errors.rejectValue("requirements", "error.editApproval.requirement.required");
		} else if (form.getRequirements().size() > 0) {
			boolean flag = false;
			CourseApproval ca= courseApprovalRepository.findOne(form.getCourseApproval().getId());
			List<CredentialCategoryRequirement> requirements = ca.getRequirements();
			//List<CredentialCategoryRequirement> requirements = form.getCourseApproval().getRequirements();
			if(requirements != null && requirements.size() > 0 ) {
				for (CredentialCategoryRequirement selectedReq : form.getRequirements()) {
					for (CredentialCategoryRequirement credReq : requirements) {
						if (selectedReq.getId().compareTo(credReq.getId()) == 0) {
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
				if (flag) {
					errors.rejectValue("requirements", "error.editApproval.requirement.exists");
				}
			}
		}
	}

}
