package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.LearnerLicenseAlertForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class ManageAndEditMyLicenseAlertController  extends VU360BaseMultiActionController{

	private VU360UserService vu360UserService;
	private LearnerLicenseService learnerLicenseService; 
	private LearnerLicenseAlertService learnerLicenseAlertService;
	private static String DATE_FORMAT = "MM/dd/yyyy";
	
	
	public ManageAndEditMyLicenseAlertController() {
		super();
	}

	public ManageAndEditMyLicenseAlertController(Object delegate) {
		super(delegate);
	}
	
protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
	if( command instanceof LearnerLicenseAlertForm ){
		LearnerLicenseAlertForm form = (LearnerLicenseAlertForm)command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);

		if( methodName.equals("displayMyLicenseAlert"))
		{
			List<LicenseOfLearner> list = learnerLicenseService.getAllLicensesOfLearner(user.getLearner().getId()); 
			form.setLicenseoflearner(list);
			
			LearnerLicenseAlert learnerlicensealert = null;
			learnerlicensealert = learnerLicenseAlertService.loadForUpdateLearnerLicenseAlert(Long.parseLong(request.getParameter("Id")));
			form.setLicensealertId(learnerlicensealert.getId());
			form.setLicenseoflearnerId(learnerlicensealert.getLearnerlicense().getId());
			if(learnerlicensealert.getDaysAfterEvent() != null)
			{
				form.setAfter(true);
				form.setDays(learnerlicensealert.getDaysAfterEvent());
			}
			if(learnerlicensealert.getDaysBeforeEvent() != null)
			{
				form.setBefore(true);
				form.setDays(learnerlicensealert.getDaysBeforeEvent());				
			}
			//form.getTriggerSingleDate(learnerlicensealert.getTriggerSingleDate().toString());
			//form.getTriggerSingleDate((Date)df.parse(learnerlicensealert.getTriggerSingleDate().toString()));
			form.setTriggerSingleDate(learnerlicensealert.getTriggerSingleDate());
			
			if(form.getTriggerSingleDate()==null){
				Date currentDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				form.setExpirationDate(sdf.format(currentDate));
			}
			else{
				//form.setExpirationDate(new SimpleDateFormat(DATE_FORMAT).format(learnerlicensealert.getLearnerlicense().getSupportingInformation()).toString());
				form.setExpirationDate(learnerlicensealert.getLearnerlicense().getSupportingInformation());
			}
		
		}
	}
}

public ModelAndView saveAlert( HttpServletRequest request, HttpServletResponse response,
		Object command, BindException errors ) throws Exception {
	
	DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	LearnerLicenseAlertForm frm = (LearnerLicenseAlertForm)command;
	LearnerLicenseAlert learnerlicensealert = null;
	learnerlicensealert = learnerLicenseAlertService.loadForUpdateLearnerLicenseAlert(frm.getLicensealertId());
	DateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
	Calendar c = Calendar.getInstance(); 
    /*
	if(frm.getLicenseoflearner() != null)
    {
    	learnerlicensealert.setLearnerlicense(frm.getLicenseoflearner());
    }
	*/
	com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Learner learner = vu360UserService.getLearnerByVU360UserId(loggedInUserVO.getId());
	
	
	if(frm.isAfter())
	{
		if(frm.getDays() != null)
		{
		  learnerlicensealert.setDaysAfterEvent(frm.getDays());
		  if(learnerlicensealert.getLearnerlicense().getSupportingInformation().toString() != null)
		  {
			    c.setTime(dateFormat.parse(learnerlicensealert.getLearnerlicense().getSupportingInformation()));
				c.add(Calendar.DATE, frm.getDays());
				frm.setTriggerSingleDate(c.getTime());
				learnerlicensealert.setTriggerSingleDate(frm.getTriggerSingleDate());
		  }
		}
	}
	else if(frm.isBefore())	
	{
		if(frm.getDays() != null)
		 {
		   learnerlicensealert.setDaysBeforeEvent(frm.getDays());
		   
		   if(learnerlicensealert.getLearnerlicense().getSupportingInformation().toString() != null)
			  {
				    c.setTime(dateFormat.parse(learnerlicensealert.getLearnerlicense().getSupportingInformation()));
					c.add(Calendar.DATE, -frm.getDays());
					frm.setTriggerSingleDate(c.getTime());
					learnerlicensealert.setTriggerSingleDate(frm.getTriggerSingleDate());
			  }
		 }
	}
	learnerlicensealert.setLearner(learner);
	learnerlicensealert.setCreatedDate(new Date());
	learnerlicensealert.setLearnerlicense(learnerLicenseService.getLicenseOfLearner(frm.getLicenseoflearnerId()));
	learnerlicensealert.setAlertName(learnerLicenseService.getLicenseOfLearner(frm.getLicenseoflearnerId()).getIndustryCredential().getCredential().getOfficialLicenseName());
	
	
	learnerLicenseAlertService.saveLearnerLicenseAlert(learnerlicensealert);
	
	return new ModelAndView("redirect:/lrn_manageLicense.do?method=displayManageLicenseAlert");
}

protected void validate( HttpServletRequest request, Object command,
		BindException errors, String methodName ) throws Exception {

    }

public ModelAndView displayMyLicenseAlert( HttpServletRequest request, HttpServletResponse response,
		Object command, BindException errors ) throws Exception {

	return new ModelAndView("manager/lrnLicense/edit_licensealert");
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
