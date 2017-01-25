package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.model.AddOrganizationGroupForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddOrganizationGroupValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddOrganizationGroupValidator.class.getName());
	
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	
	public boolean supports(Class clazz) {
		return AddOrganizationGroupForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddOrganizationGroupForm form = (AddOrganizationGroupForm)obj;
		validateFirstPage(form, errors);
	}
	
	public void validateFirstPage(AddOrganizationGroupForm form, Errors errors) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();
		
		if( StringUtils.isBlank(form.getNewGroupName()) ) {
			errors.rejectValue("groups", "error.addOrgGroup.nameRequired");
		} else if( form.getGroups() == null || form.getGroups().length == 0 ) {
			errors.rejectValue("groups", "error.addOrgGroup.orgGroupRequired");
		}
		if(! StringUtils.isBlank(form.getNewGroupName())) {
			List<OrganizationalGroup> existingOrgGroups=orgGroupLearnerGroupService.getOrgGroupByNames(new String[]{form.getNewGroupName()}, customer);
			
			if(!existingOrgGroups.isEmpty()){
				errors.rejectValue("groups", "error.addOrgGroup.name.exists");
			}
			
		}
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	
}