package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementsForm;

public class OrganisationGroupEntitlementValidator implements Validator{

	private EntitlementService entitlementService = null;


	public OrganisationGroupEntitlementValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean supports(Class clazz) {
		return OrganisationalGroupEntitlementsForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object command, Errors errors) {
		OrganisationalGroupEntitlementsForm form = (OrganisationalGroupEntitlementsForm)command;
		int flag = 0;
		for(int loop = 0,maxEnrollmentsOfRoot=0;loop<form.getOrganisationalGroupEntitlementItems().size();loop++){
			OrganisationalGroupEntitlementItem organisationalGroupEntitlementItem = (OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop);
			if (!(StringUtils.isNumeric(organisationalGroupEntitlementItem.getMaxEnrollments()))){
				flag = 1;
				break;
			}
		}
		if(flag == 1){
			errors.rejectValue("organisationalGroupEntitlementItems", "error.maxEnrollments.required");
		}
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}


}
