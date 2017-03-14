package com.softech.vu360.lms.web.controller.manager;

//import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LicenseIndustry;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseForm;
import com.softech.vu360.lms.web.controller.validator.MyLicenseValidator;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;

/**
 * 
 * @author haider.ali
 *
 */
public class AddMyLicenseController extends AbstractWizardFormController{

	
	private static final Logger log = Logger.getLogger(AddMyLicenseController.class.getName());

	private VU360UserService vu360UserService;
	private LearnerLicenseService learnerLicenseServices; 

	public AddMyLicenseController() {
		super();
		setCommandName("learnerLicenseForm");
		setCommandClass(LearnerLicenseForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				  "manager/lrnLicense/step1"
				, "manager/lrnLicense/step2"
				, "manager/lrnLicense/step3"
				, "manager/lrnLicense/step4"
				});
	}

	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
		// Auto-generated method stub
		
		//ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,				CONTEXT, context);
		return super.showForm(request, response, errors);
	}
	
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		LearnerLicenseForm form = (LearnerLicenseForm) command;
		if(page==0){
			List<LicenseIndustry> ls = learnerLicenseServices.getLicenseIndustries();
			form.setLicenseIndustries(ls);
		}if(page==1){
			form.setCredentials(convert(form.getLicenseIndustryId(), form.getState()));
			if(form.getLicenseIndustryId()!=null){
				LicenseIndustry li = learnerLicenseServices.getLicenseIndustry(form.getLicenseIndustryId());	
				form.setLicenseIndustryName(li.getName());
			}
		}
		
		if(page==3){
			/*
			if(form.getSelectedLearnerLicenseTypesId()!=null){
				LearnerLicenseType learnerLicenseType = learnerLicenseServices.getLearnerRequestedLicenseTypesById(form.getSelectedLearnerLicenseTypesId()); //1037444L
				form.setLearnerRequestedLicenseTypeName(learnerLicenseType.getLicenseType().getLicenseType());
			}
			*/
		}
		if(page==4){
			
		}

		return super.referenceData(request, page);

	}
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LicenseOfLearner licenseOfLearner = new LicenseOfLearner();
		LearnerLicenseForm form = (LearnerLicenseForm) command;
		licenseOfLearner.setSupportingInformation(form.getSupportingInformation());
		licenseOfLearner.setLicenseIndustry(learnerLicenseServices.getLicenseIndustry(form.getLicenseIndustryId()));
		licenseOfLearner.setLicenseCertificate(form.getLicenseOrCertificate());
		licenseOfLearner.setState(form.getState());
		licenseOfLearner.setIndustryCredential(learnerLicenseServices.getLicenseIndustryCredential(form.getSelectedCredentialId()));
		licenseOfLearner.setActive(Boolean.TRUE);
		VU360UserNew vU360User= new VU360UserNew();
		vU360User.setId(loggedInUserVO.getId());
		licenseOfLearner.setUpdatedBy(vU360User);
		licenseOfLearner.setUpdateOn( new Timestamp(System.currentTimeMillis()));
		Learner learner= new Learner();
		learner.setId(loggedInUserVO.getLearner().getId());
		licenseOfLearner.setLearner(learner);
		
		learnerLicenseServices.save(licenseOfLearner);
		
		return new ModelAndView("redirect:/lrn_manageLicense.do?method=displayManageLicense");	
	}
	
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		MyLicenseValidator validator = (MyLicenseValidator)this.getValidator();
		LearnerLicenseForm form = (LearnerLicenseForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validatePage1Form(form, errors);
			break;
		case 1:
			validator.validatePage2Form(form, errors);
			break;
		case 2:
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		return new ModelAndView("redirect:/lrn_manageLicense.do?method=displayManageLicense");
		
	}
	
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page){
		
		
		LearnerLicenseForm form = (LearnerLicenseForm) command;
		if(page==1){
			
			if(StringUtils.isNotEmpty(form.getSendEmailParm()) && "true".equalsIgnoreCase(form.getSendEmailParm())){ 
				
				com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Brander brander = VU360Branding.getInstance().getBranderByUser(null, loggedInUserVO);
				
				String toAddress =  brander.getBrandElement("lms.email.learnerLicense.support.fromAddress");  //mashood
				if(!StringUtils.isEmpty(toAddress)){
					
					String fromAddress = brander.getBrandElement("lms.email.learnerLicense.fromAddress");  //support
					String friendlyName = brander.getBrandElement("lms.email.learnerLicense.fromCommonName");
					String subject =  brander.getBrandElement("lms.email.learnerLicense.subject");
					String templateText =  brander.getBrandElement("lms.email.learnerLicense.support.body");;
					
					templateText= templateText.replaceAll("&lt;firstname&gt;", loggedInUserVO.getFirstName());
					templateText= templateText.replaceAll("&lt;lastname&gt;", loggedInUserVO.getLastName());
					templateText= templateText.replaceAll("&lt;username&gt;", loggedInUserVO.getUsername());
					
					String iName = learnerLicenseServices.getLicenseIndustry(form.getLicenseIndustryId()).getName();
					String ctype = form.getLicenseOrCertificate();
							
					templateText= templateText.replaceAll("&lt;industry&gt;", iName);
					templateText= templateText.replaceAll("&lt;credentialType&gt;", ctype);
					templateText= templateText.replaceAll("&lt;messagetxt&gt;", form.getEmailtextarea());
					templateText= templateText.replaceAll("&lt;support&gt;", friendlyName);
					
					
					List<LabelBean> states = brander.getBrandMapElements("lms.manageUser.AddLearner.State");
					String stateName="Not Match state";
					for( LabelBean bean : states ) {
						if( form.getState().equalsIgnoreCase(bean.getValue())) {
							stateName = bean.getLabel();
							break;
						}
					}
					templateText= templateText.replaceAll("&lt;state&gt;", stateName );
					SendMailService.sendSMTPMessage(toAddress, fromAddress, friendlyName, subject, templateText);
					
					//send request to learner.
					String friendlyEngSupportName = brander.getBrandElement("lms.email.learnerLicense.engSupport.fromCommonName");
					String learnerAddress =  loggedInUserVO.getEmailAddress();
					String fromSupport = brander.getBrandElement("lms.email.learnerLicense.fromAddress");
					String templateLearnerText =  brander.getBrandElement("lms.email.learnerLicense.learner.body");
					templateLearnerText= templateLearnerText.replaceAll("&lt;lastname&gt;", loggedInUserVO.getLastName());
					templateLearnerText= templateLearnerText.replaceAll("&lt;support&gt;", friendlyName);
					SendMailService.sendSMTPMessage(learnerAddress, fromSupport, friendlyEngSupportName, subject, templateLearnerText);
					
				}else{
					log.error("No User Email defined in Profile ");
				}
				
			}
		
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


	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}	
	
	
}
