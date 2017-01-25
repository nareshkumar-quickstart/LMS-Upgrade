package com.softech.vu360.lms.web.controller.manager;

//Author Amit

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.model.EmailAddressAlertRecipient;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertRecipient;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertRecipient;
import com.softech.vu360.lms.model.OrgGroupAlertRecipient;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageRecipientForm;
import com.softech.vu360.lms.web.controller.model.MngAlert;
import com.softech.vu360.lms.web.controller.validator.ManageRecipientValidator;
import com.softech.vu360.util.RecipientSort;


public class ManageRecipientController extends VU360BaseMultiActionController{
	
	private static transient Logger log = Logger.getLogger(ManageRecipientController.class.getName());
	
	private String redirectTemplate;
	private String searcheditAlertTemplate;
	private String displayTemplate;
	private SurveyService surveyService;
	private LearnerService learnerService;

	private String editAlertTemplate;

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	private HttpSession session = null;



	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

		if( command instanceof ManageRecipientForm ){
			ManageRecipientForm form = (ManageRecipientForm)command;
			if( methodName.equals("SearchEditRecipientPageLearners")
					|| methodName.equals("SearchEditRecipientPageLearnerGroups")
					|| methodName.equals("SearchEditRecipientPageOrganisationGroups")
					|| methodName.equals("SearchEditRecipientPageEmailAddress")){

				long recipientId=0;
				long alertId=0;
				recipientId=Long.parseLong((request.getParameter("recipientId")).trim());
				if (request.getParameter("recipientId") != null)
					form.setId(recipientId);
				if (request.getParameter("alertId") != null){
					form.setAlertId(alertId);
				}
				
			}
		}

	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object,Object> context = new HashMap<Object,Object>();
		request.setAttribute("newPage","true");
		context.put("target", "displayMngRecipient");

		return new ModelAndView(redirectTemplate,"context",context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ModelAndView displayMngRecipient( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		/*ManageRecipientForm form = (ManageRecipientForm)command;
		
		form.setMngAlerts(null);
		form.setLearnerListFromDB(null);
		form.setLearnerGroupListFromDB(null);
		form.setOrganizationalGroupListFromDB(null);
		form.setEmailAddressFromDB(null);
		long alertId = 0;
		if(form.getAlertId() != 0) {
			alertId = form.getAlertId();
		}*/
		
		search(request, response, command, errors);
		/*if(request.getParameter("alertId")!=null)
			form.setAlertId(alertId);*/
		
		//TODO
		return new ModelAndView(displayTemplate);
	}


	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		request.setAttribute("search", "doSearch");
		ManageRecipientForm form = (ManageRecipientForm)command;
		HttpSession session = request.getSession();
		long alertId=0;
		if((request.getParameter("alertId"))!=null){
			alertId=Long.parseLong((request.getParameter("alertId")).trim());
			form.setAlertId(alertId);
		}
		else if(form.getAlertId() != 0){
			alertId=form.getAlertId();
			form.setAlertId(alertId);
		}
		String recipientGroupName ="";
		if(request.getParameter("searchRecipientGroupName")!= null){
			recipientGroupName = request.getParameter("searchRecipientGroupName");
		}
		form.setAlertRecipientGroupName(recipientGroupName);
		List<AlertRecipient> recips = new ArrayList<AlertRecipient>();
		String paging = request.getParameter("paging");
		if((form.getAlertRecipientGroupType() != null) && (form.getAlertRecipientGroupType()).equals("LearnerAlertRecipient"))
			recips = surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "LearnerAlertRecipient",alertId);
		else if((form.getAlertRecipientGroupType() != null) && (form.getAlertRecipientGroupType()).equals("LearnerGroupAlertRecipient"))	
			recips=surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "LearnerGroupAlertRecipient",alertId);
		else if((form.getAlertRecipientGroupType() != null) && (form.getAlertRecipientGroupType()).equals("OrgGroupAlertRecipient"))
			recips=surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "OrgGroupAlertRecipient",alertId);
		else if((form.getAlertRecipientGroupType() != null) && (form.getAlertRecipientGroupType()).equals("EmailAddressAlertRecipient"))
			recips=surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "EmailAddressAlertRecipient",alertId);
		else
			recips=surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "",alertId);

		//recips=surveyService.findAlertRecipient(form.getRecipient().getRecipGroupName(),form.getRecipient().getRecipGroupType());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		recipientGroupName = (recipientGroupName == null)? "" : recipientGroupName.trim();
		context.put("searchRecipientGroupName", recipientGroupName);
		context.put("showAll", showAll);

		for(AlertRecipient recip : recips){
			MngAlert mngAlt =new MngAlert();
			if(recip.isDelete()==false){
				mngAlt.setRecipient(recip);
				if(recip instanceof LearnerAlertRecipient)
				mngAlt.setAlertRecipientGroupType("User");
				else if(recip instanceof LearnerGroupAlertRecipient)
					mngAlt.setAlertRecipientGroupType("User Group");
				else if(recip instanceof OrgGroupAlertRecipient)
					mngAlt.setAlertRecipientGroupType("Organization Group");
				else if(recip instanceof EmailAddressAlertRecipient)
					mngAlt.setAlertRecipientGroupType("Email Address");
				mngAlerts.add(mngAlt);	
			}
		}


		if (StringUtils.isNotBlank(paging) && paging.equalsIgnoreCase("paging")) {
			recipientGroupName = (String)session.getAttribute("recipientGroupName");
			session.setAttribute("recipientGroupName", recipientGroupName);
			context.put("recipientGroupName", recipientGroupName);
		} else {
			if( request.getParameter("searchRecipientGroupName") != null &&
					!request.getParameter("searchRecipientGroupName").isEmpty() ) {
				recipientGroupName = request.getParameter("searchRecipientGroupName");
			} 
		}	
		//============================For Sorting============================
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
		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);
		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipientGroupName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipientGroupName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipGroupType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipGroupType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			} 
		}                                                   

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate,"context" , context);
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

	public ModelAndView updateRecipientsDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		ManageRecipientValidator validator=(ManageRecipientValidator)this.getValidator();
		validator.validateFirstPage(form, errors);
		if( errors.hasErrors() ) {

			return new ModelAndView(editAlertTemplate);
		}
		surveyService.addAlertRecipient(form.getRecipient());
		return new ModelAndView(displayTemplate);
	}



	public ModelAndView showEditRecipientPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		String recipientId=request.getParameter("recipientId");
    long alertId=0;
		if((request.getParameter("alertId"))!=null){
			alertId=(Long.parseLong(request.getParameter("alertId")));
			form.setAlertId(alertId);
		}
		else if(form.getAlertId()!= 0){
			alertId=form.getAlertId();
			form.setAlertId(alertId);
		}
		AlertRecipient alertRecipient=null;
		if (recipientId !=null);
		alertRecipient=surveyService.loadAlertRecipientForUpdate(Long.parseLong(recipientId));
		form.setRecipient(alertRecipient);
		form.getRecipient().setAlertRecipientGroupName(form.getRecipient().getAlertRecipientGroupName());
		if (alertRecipient instanceof LearnerAlertRecipient){
			form.setRecipientType("users");
		}
		if (alertRecipient instanceof LearnerGroupAlertRecipient){
			form.setRecipientType("usergroups");
		}
		if (alertRecipient instanceof OrgGroupAlertRecipient){
			form.setRecipientType("organizationgroups");
		}
		if (alertRecipient instanceof EmailAddressAlertRecipient){
			form.setRecipientType("emailaddress");
		}
		// surveyService.gets
		return new ModelAndView(editAlertTemplate);
	}
	
	public ModelAndView deleteRecipient( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		String[] selectedRecipients= request.getParameterValues("rowone") ;
		if (selectedRecipients !=null && selectedRecipients.length>0)
		{
			log.debug("====== deleteFlags ==========> >>>>>>  " + selectedRecipients.length);
			long[] selectedRecipientIds = new long [selectedRecipients.length];
			int count=0;
			for(String selectedRecipient : selectedRecipients){
				selectedRecipientIds[count]= Long.parseLong(selectedRecipient);
				count++;
			}
			surveyService.deleteAlertRecipients(selectedRecipientIds);
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "displayAfterDelete");
		
		
		
		return displayAfterDelete (  request,  response,  command,  errors );
	}


	public ModelAndView SearchEditRecipientPageLearners(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		request.setAttribute("search", "doSearch");
		
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String emailAddress=request.getParameter("emailAddress");
		/*VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<VU360User> users = learnerService.findLearner("", "", "", loggedInUser);
		form.setLearnerListFromDB(users);*/
		if(firstName== null){
			firstName="";
		}
		if(lastName== null){
			lastName="";
		}
		if(emailAddress== null){
			emailAddress="";
		}

		List<Learner> learners = surveyService.getLearnersUnderAlertRecipient(form.getId(), firstName, lastName, emailAddress);
		form.setLearnerListFromDB(learners);
		return new ModelAndView(searcheditAlertTemplate);

	}


	public	ModelAndView deleteLearner( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )

	throws Exception {

		int j=0;

		ManageRecipientForm form = (ManageRecipientForm)command;

		LearnerAlertRecipient learnerAlertRecipient=(LearnerAlertRecipient)surveyService.loadAlertRecipientForUpdate(form.getId());

		String[] diSelectedLearner =null;

		String[] selectedLearners= request.getParameterValues("rowone") ;

		form.setSelectedLearnersId(selectedLearners);

		if (selectedLearners !=null && selectedLearners.length>0)

		{

			log.debug("====== deleteFlags ==========> >>>>>> " + selectedLearners.length);

			long[] selectedLearnerIds = new long [selectedLearners.length];

			int count=0;

			for(String selectedLearner : selectedLearners){

				selectedLearnerIds[count]= Long.parseLong(selectedLearner);

				count++;

			}

			for(long Id:selectedLearnerIds){

				for(j=0;j<learnerAlertRecipient.getLearners().size();j++){

					if(learnerAlertRecipient.getLearners().get(j).getId()==Id){

						learnerAlertRecipient.getLearners().remove(j);

					}

				}

			}

			surveyService.addAlertRecipient(learnerAlertRecipient);

			/*for(long Id:selectedLearnerIds){

				for(int s=0;s<form.getMngAlerts().size();s++){

					if(form.getMngAlerts().get(s).getLearner().getId().compareTo(Id) == 0){

						form.getMngAlerts().remove(s);

					}

				}

			}

			List<Learner> learners =new ArrayList<Learner>();

			for(MngAlert mngAlert :form.getMngAlerts()){

				learners.add(mngAlert.getLearner());

			}*/
			
			for(long Id:selectedLearnerIds){

				for(int s=0;s<form.getLearnerListFromDB().size();s++){

					if(form.getLearnerListFromDB().get(s).getId().compareTo(Id) == 0){

						form.getLearnerListFromDB().remove(s);

					}

				}

			}

		}

		Map<String, String> context =new HashMap <String, String>();

		context.put("target", "displayAfterDeleteLearners");

		return displayAfterDeleteLearners ( request, response, command, errors );

	}

	public ModelAndView displayAfterDeleteLearners ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){

		ManageRecipientForm form = (ManageRecipientForm)command;
		for(int i=0;i<form.getLearnerListFromDB().size();i++){
			for(int j=0;j<form.getSelectedLearnersId().length;j++){
				if(form.getLearnerListFromDB().get(i).getId()==Long.parseLong((form.getSelectedLearnersId())[j])){
					form.getLearnerListFromDB().remove(i);
				}
			}
		}

		return new ModelAndView(searcheditAlertTemplate);

	}
	
	public	ModelAndView deleteRecipientLearnerGroups( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	
	throws Exception {
		
		int j=0;
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		
		LearnerGroupAlertRecipient learnerGroupAlertRecipient=(LearnerGroupAlertRecipient)surveyService.loadAlertRecipientForUpdate(form.getId());
		
		String[] diSelectedLearner =null;
		
		String[] selectedLearners= request.getParameterValues("rowone") ;
		
		form.setSelectedLearnersId(selectedLearners);
		
		if (selectedLearners !=null && selectedLearners.length>0)
			
		{
			
			log.debug("====== deleteFlags ==========> >>>>>> " + selectedLearners.length);
			
			long[] selectedLearnerIds = new long [selectedLearners.length];
			
			int count=0;
			
			for(String selectedLearner : selectedLearners){
				
				selectedLearnerIds[count]= Long.parseLong(selectedLearner.trim());
				
				count++;
				
			}
			
			for(long Id:selectedLearnerIds){
				
				for(j=0;j<learnerGroupAlertRecipient.getLearnerGroups().size();j++){
					
					if(learnerGroupAlertRecipient.getLearnerGroups().get(j).getId()==Id){
						
						learnerGroupAlertRecipient.getLearnerGroups().remove(j);
						
					}
					
				}
				
			}
			
			surveyService.addAlertRecipient(learnerGroupAlertRecipient);
			
			for(long Id:selectedLearnerIds){

				for(int s=0;s<form.getLearnerGroupListFromDB().size();s++){

					if(form.getLearnerGroupListFromDB().get(s).getId().compareTo(Id) == 0){

						form.getLearnerGroupListFromDB().remove(s);

					}

				}

			}
			
			//form.setLearnerGroupListFromDB(learnerGroup);
			
		}
		
		Map<String, String> context =new HashMap <String, String>();
		
		context.put("target", "displayAfterDeleteLearnersGroup");
		
		return displayAfterDeleteLearnerGroup ( request, response, command, errors );
		
	}
	
	public ModelAndView displayAfterDeleteLearnerGroup ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		for(int i=0;i<form.getLearnerGroupListFromDB().size();i++){
			for(int j=0;j<form.getSelectedLearnersId().length;j++){
				if(form.getLearnerGroupListFromDB().get(i).getId()==Long.parseLong((form.getSelectedLearnersId())[j])){
					form.getLearnerGroupListFromDB().remove(i);
				}
			}
		}
		
		return new ModelAndView(searcheditAlertTemplate);
		
	}

	
	public	ModelAndView deleteRecipientOrganisationGroups( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	
	throws Exception {
		
		int j=0;
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		
		OrgGroupAlertRecipient orgGroupAlertRecipient=(OrgGroupAlertRecipient)surveyService.loadAlertRecipientForUpdate(form.getId());
		
		String[] diSelectedLearner =null;
		
		String[] selectedLearners= request.getParameterValues("rowone") ;
		
		form.setSelectedLearnersId(selectedLearners);
		
		if (selectedLearners !=null && selectedLearners.length>0)
			
		{
			
			log.debug("====== deleteFlags ==========> >>>>>> " + selectedLearners.length);
			
			long[] selectedLearnerIds = new long [selectedLearners.length];
			
			int count=0;
			
			for(String selectedLearner : selectedLearners){
				
				selectedLearnerIds[count]= Long.parseLong(selectedLearner);
				
				count++;
				
			}
			
			for(long Id:selectedLearnerIds){
				
				for(j=0;j< orgGroupAlertRecipient.getOrganizationalGroups().size();j++){
					
					if(orgGroupAlertRecipient.getOrganizationalGroups().get(j).getId()==Id){
						
						orgGroupAlertRecipient.getOrganizationalGroups().remove(j);
						
					}
					
				}
				
			}
			
			surveyService.addAlertRecipient(orgGroupAlertRecipient);
			
			for(long Id:selectedLearnerIds){

				for(int s=0;s<form.getOrganizationalGroupListFromDB().size();s++){

					if(form.getOrganizationalGroupListFromDB().get(s).getId().compareTo(Id) == 0){

						form.getOrganizationalGroupListFromDB().remove(s);

					}

				}

			}
		}
		Map<String, String> context =new HashMap <String, String>();
		
		context.put("target", "displayAfterDeleteLearnersGroup");
		
		return displayAfterDeleteLearnersGroup ( request, response, command, errors );
		
	}
	
	public ModelAndView displayAfterDeleteLearnersGroup ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		for(int i=0;i<form.getOrganizationalGroupListFromDB().size();i++){
			for(int j=0;j<form.getSelectedLearnersId().length;j++){
				if(form.getOrganizationalGroupListFromDB().get(i).getId()==Long.parseLong((form.getSelectedLearnersId())[j])){
					form.getOrganizationalGroupListFromDB().remove(i);
				}
			}
		}
		
		return new ModelAndView(searcheditAlertTemplate);
		
	}


	
	public	ModelAndView deleteRecipientEmailAddress( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	
	throws Exception {
		
		int j=0;
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		
		EmailAddressAlertRecipient emailAddressAlertRecipient=(EmailAddressAlertRecipient)surveyService.loadAlertRecipientForUpdate(form.getId());
		
		String[] diSelectedLearner =null;
		
		String[] selectedLearners= request.getParameterValues("rowone") ;
		
		form.setSelectedLearnersId(selectedLearners);
		
		if (selectedLearners !=null && selectedLearners.length>0)
			
		{
			
			log.debug("====== deleteFlags ==========> >>>>>> " + selectedLearners.length);
			
			long[] selectedLearnerIds = new long [selectedLearners.length];
			
			int count=0;
			
			for(String selectedLearner : selectedLearners){
				
				selectedLearnerIds[count]= Long.parseLong(selectedLearner);
				
				count++;
				
			}
			
			for(long Id:selectedLearnerIds){
				
				for(j=0;j< emailAddressAlertRecipient.getEmailAddress().size();j++){
					
					if(emailAddressAlertRecipient.getEmailAddress().get(j).getId()==Id){
						
						emailAddressAlertRecipient.getEmailAddress().remove(j);
						
					}
					
				}
				
			}
			
			surveyService.addAlertRecipient(emailAddressAlertRecipient);
			
			for(long Id:selectedLearnerIds){

				for(int s=0;s<form.getEmailAddressFromDB().size();s++){

					if(form.getEmailAddressFromDB().get(s).getId().compareTo(Id) == 0){

						form.getEmailAddressFromDB().remove(s);

					}

				}

			}
		}
		Map<String, String> context =new HashMap <String, String>();
		
		context.put("target", "displayAfterDeleteLearnersGroup");
		
		return displayAfterDeleteEmailAddress ( request, response, command, errors );
		
	}
	
	public ModelAndView displayAfterDeleteEmailAddress ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){
		
		ManageRecipientForm form = (ManageRecipientForm)command;
		for(int i=0;i<form.getEmailAddressFromDB().size();i++){
			for(int j=0;j<form.getSelectedLearnersId().length;j++){
				if(form.getEmailAddressFromDB().get(i).getId()==Long.parseLong((form.getSelectedLearnersId())[j])){
					form.getEmailAddressFromDB().remove(i);
				}
			}
		}
		
		return new ModelAndView(searcheditAlertTemplate);
		
	}



	public ModelAndView SearchEditRecipientPageLearnerGroups(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		request.setAttribute("search", "doSearch");
		String learnergroupName=request.getParameter("learnergroupName");

		if(learnergroupName== null){
			learnergroupName="";
		}


		List<LearnerGroup> learnergroup = surveyService.getLearnerGroupsUnderAlertRecipient(form.getId(), learnergroupName);
		
		form.setLearnerGroupListFromDB(learnergroup);
		
		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView SearchEditRecipientPageOrganisationGroups(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		request.setAttribute("search", "doSearch");
		String organizationlgroupName=request.getParameter("organizationlgroupName");
		if (organizationlgroupName != null) {

			/*VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<VU360User> users = learnerService.findLearner("", "", "", loggedInUser);
		form.setLearnerListFromDB(users);*/
			if(organizationlgroupName== null){
				organizationlgroupName="";
			}


			List<OrganizationalGroup> organizationlgroup = surveyService.getOrganisationGroupsUnderAlertRecipient(form.getId(), organizationlgroupName);
			form.setOrganizationalGroupListFromDB(organizationlgroup);
		}
 		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView SearchEditRecipientPageEmailAddress(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		
		String emailaddress=request.getParameter("emailaddress");
		if (emailaddress != null) {

		/*VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<VU360User> users = learnerService.findLearner("", "", "", loggedInUser);
		form.setLearnerListFromDB(users);*/
		
		if(emailaddress== null){
			emailaddress="";
		}


		List<EmailAddress> emailAddress = surveyService.getEmailAddressUnderAlertRecipient(form.getId(), emailaddress);
		form.setEmailAddressFromDB(emailAddress);
		}
		return new ModelAndView(searcheditAlertTemplate);

	}
	public ModelAndView SearchEditRecipientLearners(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		ManageRecipientForm form = (ManageRecipientForm) command;
		if(request.getParameter("recipientId") != null || StringUtils.isEmpty(request.getParameter("recipientId"))) {
			form.setId(Long.parseLong(request.getParameter("recipientId")));
		}
		
		List<Learner> learners = surveyService.getLearnersUnderAlertRecipient(form.getId(), "", "", "");
		form.setLearnerListFromDB(learners);
		
		return new ModelAndView(searcheditAlertTemplate);
	}
	/*public void searchList(String firstName,String lastName,String emailAddress,Object command){
		ManageRecipientForm form = (ManageRecipientForm)command;
		int i=0, j=0;
		VU360User item0;
		VU360User selecteditem0;
		List<VU360User> selectedLearners = new ArrayList<VU360User>();

		if(                 )
		for(;i<form.getLearnerListFromDB().size();){
			item0 = form.getLearnerListFromDB().get(i);
			for(;j<form.getSelectedLearners().size();){
				selecteditem0 = form.getSelectedLearners().get(j);

				if(item0.getId()==selecteditem0.getId()){
					//addRecipientForm.getSelectedLearnerGroupList().add(item);
					selectedLearners.add(item0.getFirstName());



					break;
				}
				j++;
		}
		i++;
		}
		form.setSelectedLearners(selectedLearners);



	}*/
	/*public  SearchRecipientPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(searcheditAlertTemplate);
	}
	 */
	public ModelAndView deleteRecipientLearners( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		ManageRecipientForm form = (ManageRecipientForm)command;
		String[] selectedRecipients= request.getParameterValues("rowone") ;
		if (selectedRecipients !=null && selectedRecipients.length>0)
		{
			log.debug("====== deleteFlags ==========> >>>>>>  " + selectedRecipients.length);
			long[] selectedRecipientIds = new long [selectedRecipients.length];
			int count=0;
			for(String selectedRecipient : selectedRecipients){
				selectedRecipientIds[count]= Long.parseLong(selectedRecipient);
			}
			surveyService.deleteAlertRecipients(selectedRecipientIds);
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "displayAfterDelete");



		return displayAfterDelete(  request,  response,  command,  errors );
	}
	public ModelAndView displayAfterDelete( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){

		ManageRecipientForm form = (ManageRecipientForm)command;
		List<AlertRecipient> recips = new ArrayList<AlertRecipient>();
		Long alertId=Long.parseLong(request.getParameter("alertId"));
		recips=surveyService.findAlertRecipient(form.getAlertRecipientGroupName(), "",alertId);
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(AlertRecipient recip : recips){
			MngAlert mngAlt =new MngAlert();
			if(recip.isDelete()==false){
				mngAlt.setRecipient(recip);
				if(recip instanceof LearnerAlertRecipient)
				mngAlt.setAlertRecipientGroupType("User");
				else if(recip instanceof LearnerGroupAlertRecipient)
					mngAlt.setAlertRecipientGroupType("User Group");
				else if(recip instanceof OrgGroupAlertRecipient)
					mngAlt.setAlertRecipientGroupType("Organization Group");
				else if(recip instanceof EmailAddressAlertRecipient)
					mngAlt.setAlertRecipientGroupType("Email Address");
				mngAlerts.add(mngAlt);	
			}
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
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipientGroupName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipientGroupName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipGroupType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					RecipientSort sort = new RecipientSort();
					sort.setSortBy("recipGroupType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			} 
		}

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate);


	}

	public String getEditAlertTemplate() {
		return editAlertTemplate;
	}

	public void setEditAlertTemplate(String editAlertTemplate) {
		this.editAlertTemplate = editAlertTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getSearcheditAlertTemplate() {
		return searcheditAlertTemplate;
	}

	public void setSearcheditAlertTemplate(String searcheditAlertTemplate) {
		this.searcheditAlertTemplate = searcheditAlertTemplate;
	}

}