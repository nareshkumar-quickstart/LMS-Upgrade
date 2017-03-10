package com.softech.vu360.lms.web.controller.learner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.LicenseIndustry;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseForm;

public class EditMyLicenseController extends VU360BaseMultiActionController {


	private static final Logger log = Logger.getLogger(EditMyLicenseController.class.getName());

    private String modifyLicenseTemplate = StringUtils.EMPTY;
    private String modifyLicenseTemplateRedirect = StringUtils.EMPTY;
	private LearnerLicenseService learnerLicenseServices;
	private AccreditationService accreditationService; 

	
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object,Object> context = new HashMap<Object,Object>();
        context.put("target", "displayt");

        return new ModelAndView(modifyLicenseTemplate, "context",context);	
		
	}
	
	public ModelAndView displayMyLicense(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		LearnerLicenseForm form = (LearnerLicenseForm) command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(user.getLearner().getId());
		List<LicenseIndustry> ls = learnerLicenseServices.getLicenseIndustries();

		LicenseOfLearner learnerLicenseOfLearner = learnerLicenseServices.getLicenseOfLearner(form.getId());
		if(learnerLicenseOfLearner!=null){
			
			
			form.setLicenseIndustryId(learnerLicenseOfLearner.getIndustryCredential().getLicenseindustry().getId());
			form.setLicenseOrCertificate(learnerLicenseOfLearner.getLicenseCertificate());
			form.setSelectedCredentialId(learnerLicenseOfLearner.getIndustryCredential().getId());
			form.setState(learnerLicenseOfLearner.getState());
			form.setSupportingInformation(learnerLicenseOfLearner.getSupportingInformation());
			form.setCredentials(convert(learnerLicenseOfLearner.getIndustryCredential().getLicenseindustry().getId(),  learnerLicenseOfLearner.getState()));

		}


		form.setLicenseIndustries(ls);
		
		Map<Object,Object> context = new HashMap<Object,Object>();
		context.put("licenseList", list);
		return new ModelAndView(modifyLicenseTemplate, "context", context);	

	}
	

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		LearnerLicenseForm form = (LearnerLicenseForm) command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		LicenseOfLearner licenseOfLearner = learnerLicenseServices.getLicenseOfLearner(form.getId());
		
		licenseOfLearner.setLicenseIndustry(learnerLicenseServices.getLicenseIndustry(form.getLicenseIndustryId()));
		licenseOfLearner.setLicenseCertificate(form.getLicenseOrCertificate());
		licenseOfLearner.setState(form.getState());
		//licenseOfLearner.setLearnerLicenseType(learnerLicenseServices.getLearnerRequestedLicenseTypesById(form.getSelectedLearnerLicenseTypesId()));
		licenseOfLearner.setIndustryCredential( learnerLicenseServices.getLicenseIndustryCredential(form.getSelectedCredentialId()) );
		
		licenseOfLearner.setSupportingInformation(form.getSupportingInformation());
		licenseOfLearner.setActive(Boolean.TRUE);
		VU360UserNew vu360UserModel = new VU360UserNew();
		vu360UserModel.setId(user.getId());
		licenseOfLearner.setUpdatedBy(vu360UserModel);
		licenseOfLearner.setUpdateOn( new Timestamp(System.currentTimeMillis()));
		com.softech.vu360.lms.model.Learner learnerModel = new com.softech.vu360.lms.model.Learner();
		learnerModel.setId(user.getLearner().getId());
		licenseOfLearner.setLearner(learnerModel);
		learnerLicenseServices.save(licenseOfLearner);
		
		//it is bug need to fixed.
/*		if( StringUtils.isNotEmpty(form.getRedirectTo()) &&  form.getRedirectTo().equals("profile")){
			return new ModelAndView(modifyLicenseTemplateRedirect);	
		}else{
			return new ModelAndView(modifyLicenseTemplateRedirect);
		}
*/		return new ModelAndView(modifyLicenseTemplateRedirect);

	}
	public String getModifyLicenseTemplate() {
		return modifyLicenseTemplate;
	}

	public void setModifyLicenseTemplate(String modifyLicenseTemplate) {
		this.modifyLicenseTemplate = modifyLicenseTemplate;
	}

	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}


	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		
		String Id = request.getParameter("Id");
		if(!StringUtils.isEmpty(Id)){
			LearnerLicenseForm form = (LearnerLicenseForm) command;
			form.setId(Long.parseLong(Id));
		}

		
	}
	
	
	private List<IndustryCredential> convert(Long industryId, String state){
		
		
		List<Map<Object, Object>> IndustryCredential = null;
		IndustryCredential = learnerLicenseServices.getLicenseIndusrtyCredentials(industryId, state);
		List<IndustryCredential> industryCredentials = new ArrayList<IndustryCredential>();
		
		for(Map lsSingleRow:IndustryCredential){

			IndustryCredential industryCredential2 = null;
			
            if(lsSingleRow.get("id") != null)
            	industryCredential2 = learnerLicenseServices.getLicenseIndustryCredential((Long)lsSingleRow.get("id"));

            industryCredentials.add(industryCredential2);
		}
		return industryCredentials;
	} 
	


	public String getModifyLicenseTemplateRedirect() {
		return modifyLicenseTemplateRedirect;
	}

	public void setModifyLicenseTemplateRedirect(
			String modifyLicenseTemplateRedirect) {
		this.modifyLicenseTemplateRedirect = modifyLicenseTemplateRedirect;
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	

}
