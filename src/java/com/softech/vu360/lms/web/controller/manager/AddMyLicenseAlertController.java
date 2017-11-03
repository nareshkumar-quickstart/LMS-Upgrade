package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseAlertForm;
import com.softech.vu360.lms.web.controller.validator.MyLicenseAlertValidator;


/**
 * 
 * @author haider.ali
 *
 */
public class AddMyLicenseAlertController extends AbstractWizardFormController {
	
	private static final Logger log = Logger.getLogger(AddMyLicenseAlertController.class.getName());

	private VU360UserService vu360UserService;
	private LearnerLicenseService learnerLicenseService; 
	private LearnerLicenseAlertService learnerLicenseAlertService;

	public AddMyLicenseAlertController() {
		super();
		setCommandName("learnerLicenseAlertForm");
		setCommandClass(LearnerLicenseAlertForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				  "manager/lrnLicense/step1Alert"
				, "manager/lrnLicense/step2Alert"
				, "manager/lrnLicense/step3Alert"
				, "manager/lrnLicense/step4Alert"
				});
	}

	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
		// Auto-generated method stub
		return super.showForm(request, response, errors);
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LearnerLicenseAlertForm frm = (LearnerLicenseAlertForm)command;
		LearnerLicenseAlert learnerlicensealert = new LearnerLicenseAlert();
		LicenseOfLearner licenseoflearner = learnerLicenseService.getLicenseOfLearner(frm.getLicenseoflearnerId());

		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		learnerlicensealert.setLearnerlicense(licenseoflearner);
		if(frm.isAfter()){
			if(frm.getDays() != null){
			  learnerlicensealert.setDaysAfterEvent(frm.getDays());
			}
		}else if(frm.isBefore()){
			if(frm.getDays() != null){
			  learnerlicensealert.setDaysBeforeEvent(frm.getDays());
			}
		}
		
		Learner learner4Alert= new Learner();
		learner4Alert.setId(loggedInUserVO.getLearner().getId());
		learnerlicensealert.setLearner(learner4Alert);
		learnerlicensealert.setCreatedDate(new Date());
		learnerlicensealert.setTriggerSingleDate(frm.getTriggerSingleDate());
		learnerlicensealert.setAlertName(frm.getLicensealertname());
		learnerLicenseAlertService.addLearnerLicenseAlert(learnerlicensealert);
		
		return new ModelAndView("redirect:/lrn_manageLicense.do?method=displayManageLicenseAlert");
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		return new ModelAndView("redirect:/lrn_manageLicense.do?method=displayManageLicenseAlert");
	}
	
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		LearnerLicenseAlertForm form = (LearnerLicenseAlertForm) command;
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LicenseOfLearner LOL = null;
		if(page==0){
			List<LicenseOfLearner> list = learnerLicenseService.getAllLicensesOfLearner(loggedInUserVO.getLearner().getId()); 
			form.setLicenseoflearner(list);
		}else if(page==1){
			LOL = learnerLicenseService.getLicenseOfLearner(form.getLicenseoflearnerId());
			form.setSelectedLicenseoflearner(LOL);
			LOL = learnerLicenseService.getLicenseOfLearner(form.getLicenseoflearnerId());
	        form.setLicensealertname(LOL.getIndustryCredential().getCredential().getOfficialLicenseName());
	        form.setExpirationDate(LOL.getSupportingInformation());
		}else if(page==2){
	        LicenseOfLearner licenseoflearner = learnerLicenseService.getLicenseOfLearner(form.getLicenseoflearnerId());
	        String licensesupportinginfo = licenseoflearner.getSupportingInformation();
			DateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
		    //handling ParseExceptions is an exercise left to the reader!
			Calendar c = Calendar.getInstance();    
			if(form.isAfter()){
				if(form.getDays() != null){
					form.setDaysAfterEvent(form.getDays());
				  if( licensesupportinginfo != null){
						c.setTime(dateFormat.parse(licenseoflearner.getSupportingInformation()));
						c.add(Calendar.DATE, form.getDays());
						form.setTriggerSingleDate(c.getTime());
						form.setExpirationDate(new SimpleDateFormat("MM/dd/yyyy").format(form.getTriggerSingleDate()).toString());
					}
				 
				}
			}else if(form.isBefore()){
				if(form.getDays() != null){
				  if( licensesupportinginfo != null){
						c.setTime(dateFormat.parse(licenseoflearner.getSupportingInformation()));
						c.add(Calendar.DATE, -form.getDays());
						form.setTriggerSingleDate(c.getTime());
						form.setExpirationDate(new SimpleDateFormat("MM/dd/yyyy").format(form.getTriggerSingleDate()).toString());
					}
				}
			}
	        log.debug("Page Number: " + page);
		}
		return super.referenceData(request, page);
	}
	
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		MyLicenseAlertValidator validator = (MyLicenseAlertValidator)this.getValidator();
		LearnerLicenseAlertForm form = (LearnerLicenseAlertForm)command;
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

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page){
		/*LearnerLicenseForm learnerLicenseForm = (LearnerLicenseForm) command;
		if(page==1){
			if(StringUtils.isNotEmpty(learnerLicenseForm.getSendEmailParm()) && "true".equalsIgnoreCase(learnerLicenseForm.getSendEmailParm())){ 
				
				SendMailService
				.sendSMTPMessage(
						new String[] { VU360Properties.getVU360Property("lms.email.globalException.toAddress") },
						null,"lmsalerts@domain.com",
						VU360Properties.getVU360Property("lms.email.globalException.title"),
						VU360Properties.getVU360Property("lms.email.globalException.subject"),
						"<br> Request for Lernere License Registration. ");
			}
		}*/
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
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

	public void setLearnerLicenseAlertService(LearnerLicenseAlertService learnerLicenseAlertService) {
		this.learnerLicenseAlertService = learnerLicenseAlertService;
	}
}
