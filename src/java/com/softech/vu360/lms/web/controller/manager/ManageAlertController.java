package com.softech.vu360.lms.web.controller.manager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserNewService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageAlertForm;
import com.softech.vu360.lms.web.controller.model.MngAlert;
import com.softech.vu360.lms.web.controller.validator.ManageAlertValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.AlertSort;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;
public class ManageAlertController extends VU360BaseMultiActionController{
	private Logger log = Logger.getLogger(ManageAlertController.class.getName());
	private String redirectTemplate;
	private String displayTemplate;
	private String displayEditAlertTemplate;
	private String editAlertTemplate;
	private SurveyService surveyService;
	private LearnerService learnerService;
	@Inject
	private VU360UserNewService vu360UserNewService;
	
	public String getEditAlertTemplate() {
		return editAlertTemplate;
	}

	public void setEditAlertTemplate(String editAlertTemplate) {
		this.editAlertTemplate = editAlertTemplate;
	}
	private HttpSession session = null;

	public ManageAlertController() {
		super();
	}

	public ManageAlertController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/**
		 * LMS-14035 Manager Alert left menu highlighted when selected
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
		
		if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			request.getSession(true).setAttribute("feature", "LMS-MGR-0017");
		}
		

		Map<Object,Object> context = new HashMap<Object,Object>();
		context.put("target", "displayMagAlert");

		return new ModelAndView(redirectTemplate,"context",context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		
			
		// TODO Auto-generated method stub
	}

	public ModelAndView displayMagAlert( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		
		ManageAlertForm form = (ManageAlertForm)command;
		//TODO
		return new ModelAndView(displayTemplate);
	}


	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {


		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		session = request.getSession(true);
		session.setAttribute("feature", "LMS-ADM-0005");
		VU360UserNew vu360UserModel = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
		ManageAlertForm form = (ManageAlertForm)command;
		List<Alert> alerts = new ArrayList<Alert>();
		if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			com.softech.vu360.lms.vo.Customer cust = (com.softech.vu360.lms.vo.Customer) session.getAttribute("adminSelectedCustomer");
			com.softech.vu360.lms.vo.Distributor distributorvo = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");

			if(cust != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(cust.getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId);
			} else if(distributorvo != null) {
				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorvo.getMyCustomer().getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId);
			} else {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();
			}
		} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();
		}
		if(vu360UserModel!=null)
			alerts = surveyService.findAlert(vu360UserModel.getId() , form.getAlertName());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(Alert alert : alerts){			
			MngAlert mngAlt =new MngAlert();
			
			if(alert.getIsDelete()==false){
				mngAlt.setAlert(alert);
				mngAlerts.add(mngAlt);
			}
			//mngAlt.setRecipents(alert.getRecipents());
			
		}

		//============================For Sorting============================
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("recipents");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("lastName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
		}
		
		
		form.setMngAlerts(mngAlerts);
		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		//context.put("alerts", alerts);
		// TODO
		return new ModelAndView(displayTemplate , "context" , context);
	}
	
	public ModelAndView showEditAlertPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		
		ManageAlertForm form = (ManageAlertForm)command;
		String alertId=request.getParameter("alertId");
		Alert alert=null; 
		if (alertId !=null);
		alert=surveyService.loadAlertForUpdate(Long.parseLong(alertId));
		form.setAlert(alert);
		if( StringUtils.isBlank(form.getAlert().getAlertMessageBody() ) )
			form.getAlert().setAlertMessageBody("");
		form.getAlert().setAlertMessageBody(form.getAlert().getAlertMessageBody().replaceAll("\"","'"));

		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r\n", "") );
		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\n", "<br>") );
		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r", "<br>") );
		form.getAlert().setFromName(form.getAlert().getFromName());
		form.getAlert().setAlertSubject(form.getAlert().getAlertSubject());
		form.setDefaultMessage(alert.getIsDefault());
		// surveyService.gets
		return new ModelAndView(editAlertTemplate);
	}
	public ModelAndView updateAlertDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		
		ManageAlertForm form=(ManageAlertForm)command;
		VU360UserNew logInUser = VU360UserAuthenticationDetails.getCurrentSimpleUser();
		form.getAlert().setCreatedBy(logInUser);
		ManageAlertValidator validator = (ManageAlertValidator)this.getValidator();
		
		com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
		lang.setLanguage(Language.DEFAULT_LANG);
		Brander brand = VU360Branding.getInstance().getBrander(VU360Branding.DEFAULT_BRAND, lang);
		
		
		if(!request.getParameter("chkValue").equals("")) 
			form.setDefaultMessage(Boolean.valueOf(request.getParameter("chkValue")));
		else
			form.setDefaultMessage(false);
		
		
		if(form.isDefaultMessage()==true)
		{
			if(brand.getBrandElement("lms.manager.manageAlert.addAlert.caption.detaultMessage") != null) {
				form.getAlert().setAlertMessageBody(brand.getBrandElement("lms.manager.manageAlert.addAlert.caption.detaultMessage"));
			} else {
				form.getAlert().setAlertMessageBody("");
			}
			if(VU360Properties.getVU360Property("lms.email.alert.default.subject") != null) {
				form.getAlert().setAlertSubject(VU360Properties.getVU360Property("lms.email.alert.default.subject"));
			} else {
				form.getAlert().setAlertSubject(form.getAlert().getAlertName());
			}
			if(VU360Properties.getVU360Property("lms.email.alert.default.fromName") != null) {
				form.getAlert().setFromName(VU360Properties.getVU360Property("lms.email.alert.default.fromName"));
			} else {
				form.getAlert().setFromName("default@default.com");
			}
		}
		
		form.getAlert().setIsDefault(form.isDefaultMessage());
		form.getAlert().setAlertMessageBody(form.getAlert().getAlertMessageBody().substring(0, Math.min(form.getAlert().getAlertMessageBody().length(), 2000)));
		
		/**
		 * LMS-14200 | Rehan Rana | 12 Feb, 2013
		 * validate in last of method because if user selected isDefaultMessage, need to validate only alert name
		 */
		validator.validateFirstPage(form, errors);
		
		
		if( errors.hasErrors() ) 
			return new ModelAndView(editAlertTemplate);
		
		surveyService.addAlert(form.getAlert());
					
		return new ModelAndView(redirectTemplate);
	}



	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getDisplayTemplate() {
		return displayTemplate;
	}

	public void setDisplayTemplate(String displayTemplate) {
		this.displayTemplate = displayTemplate;
	}	
	
	public String getDisplayEditAlertTemplate() {
		return displayEditAlertTemplate;
	}

	public void setDisplayEditAlertTemplate(String displayEditAlertTemplate) {
		this.displayEditAlertTemplate = displayEditAlertTemplate;
	}

	public ModelAndView deleteAlert( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		
		String[] selectedAlerts= request.getParameterValues("rowone") ;
		if (selectedAlerts !=null && selectedAlerts.length>0)
		{
			log.debug("====== deleteFlags ==========> >>>>>>  " + selectedAlerts.length);
			long[] selectedFlagIds = new long [selectedAlerts.length];
			int count=0;
			for(String selectedFlag : selectedAlerts){
				selectedFlagIds[count]= Long.parseLong(selectedFlag);
				count++;
			}
			surveyService.deleteAlerts(selectedFlagIds);
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "showManageFlags");
		
		
		
		return displayAfterDelete (  request,  response,  command,  errors );
	}
	
	public ModelAndView displayAfterDelete ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		session = request.getSession(true);
		session.setAttribute("feature", "LMS-ADM-0005");
		
		ManageAlertForm form = (ManageAlertForm)command;
		List<Alert> alerts = new ArrayList<Alert>();
		VU360UserNew vu360UserModel = null;
		
		//LMS-15637 - Start
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();

		if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			com.softech.vu360.lms.vo.Customer cust = (com.softech.vu360.lms.vo.Customer) session.getAttribute("adminSelectedCustomer");
			com.softech.vu360.lms.vo.Distributor distributorvo = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");

			if(cust != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(cust.getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId);
			} else if(distributorvo != null) {
				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorvo.getMyCustomer().getId());
				vu360UserModel = vu360UserNewService.getVU360UserByLearnerId(learnerId);
			} else {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();
			}
		} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			vu360UserModel = VU360UserAuthenticationDetails.getCurrentSimpleUser();
		}
		//LMS-15637 - End
		if(vu360UserModel != null)
			alerts = surveyService.findAlert(vu360UserModel.getId() , form.getAlertName());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(Alert alert : alerts){
			MngAlert mngAlt =new MngAlert();
			
			if(alert.getIsDelete()==false){
				mngAlt.setAlert(alert);
				mngAlerts.add(mngAlt);
			}
			//mngAlt.setRecipents(alert.getRecipents());
			
		}

		//============================For Sorting============================
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("recipents");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("lastName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
		}
		
		
		form.setMngAlerts(mngAlerts);
		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		//context.put("alerts", alerts);
		// TODO
		return new ModelAndView(displayTemplate , "context" , context);
		
		
		
	}
	public ModelAndView editSaveAlert(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0005");
		
		return new ModelAndView(editAlertTemplate);
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}