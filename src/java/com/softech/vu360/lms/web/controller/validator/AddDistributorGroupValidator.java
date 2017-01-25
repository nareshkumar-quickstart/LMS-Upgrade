/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.web.controller.model.ManagerDistributorGroupForm;

/**
 * @author Tapas Mondal
 *
 */
public class AddDistributorGroupValidator  implements Validator{

	private static final Logger log = Logger.getLogger(ManageSurveyValidator.class.getName());
private DistributorService  distributorService = null;
	public boolean supports(Class clazz) {
		return ManagerDistributorGroupForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ManagerDistributorGroupForm managerDistributorGroupForm = (ManagerDistributorGroupForm)obj;
		validateFirstPage(managerDistributorGroupForm,errors);
		validateSecondPage(managerDistributorGroupForm,errors);

	}

	public void validateFirstPage(ManagerDistributorGroupForm form, Errors errors) {
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (StringUtils.isBlank(form.getDistributorGroupName()))		{
			log.debug("distributorGroupName validate");
			errors.rejectValue("distributorGroupName", "error.distributorGroupName.required");
		} else if (distributorService.findDistributorGroupsByName(form.getDistributorGroupName(), loggedInUser,true).size()>0){
			 errors.rejectValue("distributorGroupName", "error.distributorGroupName.existDistributorGroupName");
		}
		/*else if (FieldsValidation.isInValidName(form.getDistributorGroupName())){
			errors.rejectValue("distributorGroupName", "error.all.invalidText");
		}*/
	}

	public void validateSecondPage(ManagerDistributorGroupForm form, Errors errors) {
		if(form.getSelectedDistributors().size() == 0){
			errors.rejectValue("distributors", "error.distributorGrous.required");
		}
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

}
