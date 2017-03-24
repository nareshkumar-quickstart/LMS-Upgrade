/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.LMSProductCourseType;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.SCO;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.instructor.LMSXPathParser;
import com.softech.vu360.lms.web.controller.model.instructor.CourseDetails;
import com.softech.vu360.lms.web.controller.validator.AddCourseValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Tahir Mehmood
 *
 */


public class ManagerAddCourseController extends AbstractWizardFormController{

	private static final String[] BUSINESS_UNITS = {"Career Training", "Business Skills", "Cosmetology Education", "Environmental Health and Safety", 
													"Financial Services Education", "Food and Beverage Programs", "Healthcare Education", "HR, Ethics & Compliance", 
													"Industrial Skills", "Insurance Education", "IT Certification", "Power and Utilities", "Quality Management Education", 
													"Real Estate Education", "Trades and Engineering"};
	private static final String[] LANGUAGES = {"English"};
	//private static final String DISCUSSION_FORUM = "discussionForum";
	private static final String SCORM_PACKAGE = "scormPackage";
	private static final String SYNCHRONOUS_COURSE = "synchronousCourse";
	private static final String WEBINAR_COURSE = "webinarCourse";
	private static final String WEBLINK_COURSE = "weblinkCourse";
	private static final String SCORM_UPLOAD_SERVLET = "mgr_uploadScormPackge.do;jsessionid=";

	private static final String[] REGULATORY_REQUIREMENT= {"None","Percentage","Flat Dollar"};
	private static boolean validateThirdPage = true;
	private static final Logger log = Logger.getLogger(ManagerAddCourseController.class.getName());	
	private CourseAndCourseGroupService courseAndCourseGroupService;	
	private AuthorService authorService;
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;	
	private VU360UserService vu360UserService = null;
	private EntitlementService entitlementService = null;
	private CustomerService customerService = null;
	private LMSProductPurchaseService lmsProductPurchaseService;
	public ManagerAddCourseController() {
		super();
		setCommandName("courseForm");
		setCommandClass(com.softech.vu360.lms.web.controller.model.instructor.CourseDetails.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/manageSynchronousCourse/addCourse/mgr_addCourseType",
				"manager/manageSynchronousCourse/addCourse/mgr_addCourse_DiscussionForum",
				"manager/manageSynchronousCourse/addCourse/mgr_addCourse_SCORMPackage_applet",
				"manager/manageSynchronousCourse/addCourse/mgr_addCourse_SCORMPackage_summary",
				"manager/manageSynchronousCourse/addCourse/mgr_addCourse_SynchronousCourse", 
				"manager/manageSynchronousCourse/addCourse/mgr_addCourse_WeblinkCourse"});
	}

	/**
	 * Step 1.
	 * We do not need to override this method now.
	 * This method basically lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * This is called first when a new request is made and then on
	 * every subsequent request. However in our case, 
	 * since the bindOnNewForm is true this 
	 * will NOT be called on subsequent requests...
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject in addSynchrounous course ");
		CourseDetails courseForm = (CourseDetails)super.formBackingObject(request);
		//courseForm.setCourseType("");
		return courseForm;
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// check for customer selection
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();

			if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
				if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
					response.sendRedirect("adm_manageLearners.do?error=customerSelection");
				}
			}
		}	
		// TODO Auto-generated method stub request, response, binder
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		log.debug("IN referenceData ,,, page::>"+page);
		log.debug("_page_page_page_page_page_page :: "+request.getParameter("_page"));

		Map model = new HashMap();

		switch(page) {
		
		case 0:
			log.debug("inside CASE # 0");
			// Here the condition to show course type will be checked and display accordingly
			Long customerId=null;
			boolean showAllCourse = false;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				customerId = details.getCurrentCustomerId();
				if(CustomerUtil.checkB2BCustomer(details.getCurrentCustomer())){
					showAllCourse = true;
				}
			}
			
			if(showAllCourse){
				//model.put("showDiscussionForum", 1);
				model.put("showSynchronous", 1);
				model.put("showSCORM", 1);
				model.put("showWeblinkCourse", 1);
				model.put("showHomeworkAssignment", 1);
			}
			else{
				int showSynchronous=0,showSCORM=0,showWeblinkCourse = 0, showHomeworkAssignment=0;
				List<LMSProductCourseType> allowedCourseTypes = lmsProductPurchaseService.getCustomerCourseTypes(customerId);
				if(allowedCourseTypes!=null && allowedCourseTypes.size()>0){
					for (LMSProductCourseType lmsProductCourseType : allowedCourseTypes) {
						/*if(lmsProductCourseType.getCourseType()!=null && lmsProductCourseType.getCourseType().equals("")){
							showDiscussionForum = 0;//not required in manager mode if user is B2C
						}*/
						if(lmsProductCourseType.getCourseType()!=null && (lmsProductCourseType.getCourseType().equals(SynchronousCourse.COURSE_TYPE)||lmsProductCourseType.getCourseType().equals(WebinarCourse.COURSE_TYPE))){
							showSynchronous = 0;//not required in manager mode if user is B2C
						}
						if(lmsProductCourseType.getCourseType()!=null && lmsProductCourseType.getCourseType().equals("Scorm Course")){
							showSCORM = 1;
						}
						if(lmsProductCourseType.getCourseType()!=null && lmsProductCourseType.getCourseType().equals("")){
							showWeblinkCourse = 0;//not required in manager mode if user is B2C
						}
						if(lmsProductCourseType.getCourseType()!=null && lmsProductCourseType.getCourseType().equals("Learner Assignment")){
							showHomeworkAssignment = 0;//not required in manager mode if user is B2C
						}
						
					}
					
					//model.put("showDiscussionForum", showDiscussionForum);
					model.put("showSynchronous", showSynchronous);
					model.put("showSCORM", showSCORM);
					model.put("showWeblinkCourse", showWeblinkCourse);
					model.put("showHomeworkAssignment", showHomeworkAssignment);
					
					
				}
			}
			return model;
		case 1:
			log.debug("inside CASE # 1");
			model.put("businessUnits", BUSINESS_UNITS);			
			model.put("languages", LANGUAGES);
			return model;
		case 2:
			log.debug("inside CASE # 2");
			String jSessionId = "";
			Cookie cookies[]= request.getCookies();
			if (cookies!=null)
			{
				for (int i=0; i<cookies.length; i++)
				{
					if(cookies[i].getName().equalsIgnoreCase("JSESSIONID"))
					{
						jSessionId = cookies[i].getValue();
						break;
					}
				}
			}
			model.put("jSessionId",jSessionId);
						
			
			String fileUploadURL = request.getRequestURL().toString();
			fileUploadURL = fileUploadURL.substring(0, fileUploadURL.indexOf( request.getRequestURI() ) ) + request.getContextPath() + "/";	
			fileUploadURL = fileUploadURL + SCORM_UPLOAD_SERVLET + jSessionId + "?userType=mgr";
			
			model.put("fileUploadURL",fileUploadURL);//setting dynamic upload servlet url for SCORM package upload. Tahir 06/11/201			
			
			return model;
		case 3:
			log.debug("inside CASE # 3");
			model.put("businessUnits", BUSINESS_UNITS);			
			model.put("languages", LANGUAGES);
			model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
			return model;
		case 5:
			log.debug("inside CASE # 5");
			model.put("businessUnits", BUSINESS_UNITS);			
			model.put("languages", LANGUAGES);
			return model;
		case 4:
			log.debug("inside CASE # 4");
			model.put("businessUnits", BUSINESS_UNITS);
			if(request.getParameter("courseType").equals(WEBINAR_COURSE)) {
				model.put("page_heading", "lms.instructor.addWebinarCourses.caption.pageHead");
			} else {
				model.put("page_heading", "lms.instructor.addSynchrounousCourses.caption.pageHead");
			}

			model.put("languages", LANGUAGES);
			return model;
		default:
			break;
		}

		return super.referenceData(request, page);
	}

	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */

	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage :: > "+page);

		// TODO Auto-generated method stub
		CourseDetails form = (CourseDetails)command;
		AddCourseValidator addCourseValidator = (AddCourseValidator) getValidator();

		switch (page) {
		
		case 0:
			if(form.getCourseType()==null || (form.getCourseType()!=null && form.getCourseType().isEmpty())){
				errors.rejectValue("courseType", "error.addCourse.courseType.required");
			}
			break;
		
		case 1:
			/*if(form.getCourseType().equals(DISCUSSION_FORUM))
			{
				addCourseValidator.validatePage1(form, errors);
			}		*/	
			break;
		case 2:
			if(form.getCourseType().equals(SCORM_PACKAGE))
			{
				/*
				 * Not required as it is being validated in methond: getTargetPage() of this class
				 */
				//addCourseValidator.validatePage2(form, errors); 
			}			
			break;
		case 3:
			if(form.getCourseType().equals(SCORM_PACKAGE))
			{
				if(validateThirdPage)
				{
					for (CourseDetails courseDetails : form.getScormCourses()) {
						addCourseValidator.validatePage1(courseDetails, errors);
						addCourseValidator.validateCourseAdditionalDetails(courseDetails, errors);
					}									
				}
				validateThirdPage=true;	
			}			
			break;
		case 4:
			if(form.getCourseType().equals(SYNCHRONOUS_COURSE) || form.getCourseType().equals(WEBINAR_COURSE))
			{
				addCourseValidator.validatePage4(form, errors);
			}			
			break;
		case 5:
			if(form.getCourseType().equals(WEBLINK_COURSE))
			{
				addCourseValidator.validatePage5(form, errors);
			}			
			break;
		default:
			break;
		}		
	}

	@Override
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		// TODO Auto-generated method stub
		log.debug(" INSIDE  .........:::::::::::::: postProcessPage");
		request.setAttribute("PARAM_FINISH", "_finish");
		super.isFinishRequest(request);
		log.debug(" INSIDE  .........:::::::::::::: postProcessPage >>>>> "+super.isFinishRequest(request));
		super.postProcessPage(request, command, errors, page);
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		// TODO Auto-generated method stub
		if(page==3 && getTargetPage(request, page)==2)
		{

			//dont validate now.......as its a previous request from the last page...
			//will validate when it will come here again
			validateThirdPage=false;			
		}
		if(page==5)
		{
			super.isFinishRequest(request);
			validateThirdPage=true;
		}

		super.onBindAndValidate(request, command, errors, page);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		CourseDetails courseForm = (CourseDetails)command;
		log.debug("in GET TARGET PAGE...."+currentPage);
		if(currentPage==0){

			/*if (courseForm.getCourseType().equals(DISCUSSION_FORUM))
				return 1;
			else*/ if (courseForm.getCourseType().equals(SCORM_PACKAGE))
				return 2;
			else if (courseForm.getCourseType().equals(SYNCHRONOUS_COURSE) || courseForm.getCourseType().equals(WEBINAR_COURSE))
				return 4;
			else if (courseForm.getCourseType().equals(WEBLINK_COURSE))
				return 5;		

		}
		else if(currentPage==2){
			try {
				if(!errors.hasErrors())
				{
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					Map files = multiRequest.getFileMap();
	    	    	
					Iterator fileIterator=files.values().iterator();
	    	        MultipartFile fileToUpload = null;
					while(fileIterator.hasNext()) {
						fileToUpload = (MultipartFile)fileIterator.next();
						if(fileToUpload!=null && fileToUpload.getSize()>0){
							courseForm.setFile(fileToUpload);
						}
					}
					//cleanUpFiles((CourseDetails) command);// canceling the
					courseForm.getScormPackages().removeAllElements();
					//List<Document> lstdoc = new ArrayList<Document>();
					//lstdoc = uploadHomeWorkAssignment(form,multiRequest);
					//log.debug("Multiple Upload Document: " + lstdoc.size());
					
					
					//there can be a single file uploaded from browse button...
					if(courseForm.getFile() != null && courseForm.getFile().getOriginalFilename() != null && !courseForm.getFile().getOriginalFilename().equals("")){
						MultipartFile originalFile = courseForm.getFile();
						String fileName = originalFile.getOriginalFilename();
						String fileLocation = VU360Properties.getVU360Property("scormPackage.tempSaveLocation");//for saving the file in temporary location

						String filePath = fileLocation + File.separator+System.currentTimeMillis()+"_"+fileName;
						File tempFile = new File(filePath);

						if (!tempFile.exists()) {
							try {
								courseForm.getScormPackages().add(FileUploadUtils.copyFile(originalFile, tempFile));//file saved...and added to my personal files							
							} 
							catch (IOException e) {
								log.error(e);									
								errors.rejectValue("file", "error.addCourse.SCORMPackage.invalidSaveLocation");
								return super.getTargetPage(request, command, errors, currentPage);
							}	
						}
						log.info("FILE UPLOADED............. . "+fileName);
					}else
					{
						if(courseForm.getScormPackages() == null || courseForm.getScormPackages().size()==0)
						{
							log.error("NO FILE WAS UPLOADED..... either single or multiple :: ");	
							errors.rejectValue("file", "error.addCourse.SCORMPackage.file.required");
							return super.getTargetPage(request, command, errors, currentPage);
						}
						//there is no file selected...

					}
					log.info("TOTAL UPLOADED FILES :: "+courseForm.getScormPackages().size());	

					/*
					 * need to iterate through each ZIP file and then extract the imanifest.xml file from each ZIP. 
					 * then process over that manifest and extract the required fields. We need to add the extracted information in
					 * the CourseForm Object. same for each zip file.... at the end need to display on VM by looping over the courseForm objects.
					 * VALIDATION : need to validate each courseForm object. there can be many of this type in SCORM case.
					 * 
					 * FileUploadUtils.tempFilNames ::> The ZIP files uploaded right now in this session.
					 */


					List<String> infoErrors = processFiles(courseForm.getScormPackages(), request, command);

					if(infoErrors.size() != 0)
					{
						//bindErrors.addAllErrors(infoErrors);
						for (String infoErr : infoErrors) {
							errors.reject(infoErr);
						}
						log.error("Errors while Processing Files....... >> "+errors.getErrorCount());
					}

				}

			} catch (Exception e) {				
				log.error(e,e);
				errors.reject("error.instructor.addCourse.scormCourse.save.fileError");
			}
		}

		return super.getTargetPage(request, command, errors, currentPage);
	}
	
	public List<String> processFiles(Vector<String> fileNames,HttpServletRequest request,Object command) throws Exception
	{
		log.debug("Inside ProcessFILe method...... processing all files one by one...");
		List<String> errors = new ArrayList<String>();
		CourseDetails form = ((CourseDetails)command);
		List<CourseDetails> courses = new ArrayList<CourseDetails>();
		for (String fileName : fileNames) {//loop over all files....ZIPs
			File currentXmlFile = FileUploadUtils.getXMLFileFromZIP(fileName);
			if(currentXmlFile == null){
				//this is ERROR... there should be atleast one XML file inside the ZIP...add up to the ERRORS
				log.error("There is no \"imsmanifest.xml\" file in the SCORM package :: "+fileName);
				errors.add("error.instructor.addCourse.scormCourse.save.noManifestFile");
			}
			else if(currentXmlFile != null)//is there  a file... ??
			{
				LMSXPathParser parser = new LMSXPathParser();
				Map courseDetails = parser.getCourseDetailsFromXML(currentXmlFile);
				//check if we have got the correct info.... ?? if not then show it as an error.
				if(courseDetails != null && courseDetails.size() >= 4 )
				{
					CourseDetails scormPkgDetails = new CourseDetails(); 
					scormPkgDetails.setCourseName(courseDetails.get("title").toString());
					scormPkgDetails.setVersion(courseDetails.get("schemaversion").toString());
					scormPkgDetails.setDescription(courseDetails.get("description").toString());
					
					//BEGIN: ENGSUP-31157
					String launchData = null;	
					
					if(courseDetails.get("datafromlms") !=null )
					{
						launchData = courseDetails.get("datafromlms").toString();
						
						if(launchData !=null && launchData.length() >0)
						{
							scormPkgDetails.setLaunchData(launchData);						
						}
					}
					else
					{
						scormPkgDetails.setLaunchData(launchData);						
					}
					
					String timeLimitAction = null;
					
					if(courseDetails.get("timelimitaction") != null)
					{
						timeLimitAction = courseDetails.get("timelimitaction").toString();
						
						if(timeLimitAction !=null && timeLimitAction.length() >0)
						{
							scormPkgDetails.setTimeLimitAction(timeLimitAction);				
						}
					}
					else
					{	
						scormPkgDetails.setTimeLimitAction(timeLimitAction);	
					}
					
					double completionThreshold = 0.0;
					String completionThresholdString = null;
					
					
					if(courseDetails.get("completionThreshold") != null)
					{
						completionThresholdString = courseDetails.get("completionThreshold").toString();
						
																
						if(completionThresholdString !=null && completionThresholdString.length() >0)
						{
							completionThreshold = Double.parseDouble(completionThresholdString);
							scormPkgDetails.setCompletionThreshold(completionThreshold);				
						}
						else
						{
							scormPkgDetails.setCompletionThreshold(completionThreshold);						
						}
					}
					else
					{
						scormPkgDetails.setCompletionThreshold(completionThreshold);	
					}
										

					double masteryScore = 0.0;
					String masteryScoreString = null;
					
					if(courseDetails.get("masteryscore") != null)
					{
						masteryScoreString = courseDetails.get("masteryscore").toString();
						
						/*if(masteryScoreString == null || (masteryScoreString !=null && masteryScoreString.length() <=0))
						{
							masteryScoreString = courseDetails.get(("minNormalizedMeasure").toLowerCase()).toString();					
						}*/
																
						if(masteryScoreString !=null && masteryScoreString.length() >0)
						{
							masteryScore = Double.parseDouble(masteryScoreString);
							scormPkgDetails.setMasteryScore(masteryScore);				
						}
						else
						{
							scormPkgDetails.setMasteryScore(masteryScore);						
						}
					}
					else
					{
						scormPkgDetails.setMasteryScore(masteryScore);						
					}
										
					
					String maxTimeAllowedString = null;
					long maxTimeAllowed =0;
					
					/*
					if(courseDetails.get(("maxtimeallowed").toLowerCase()) !=null)
					{
						maxTimeAllowedString = courseDetails.get(("maxtimeallowed").toLowerCase()).toString();
						
						if(maxTimeAllowedString == null || (maxTimeAllowedString !=null && maxTimeAllowedString.length() <=0))
						{
							if(courseDetails.get(("attemptAbsoluteDurationLimit").toLowerCase()) != null)
							{
								maxTimeAllowedString = courseDetails.get(("attemptAbsoluteDurationLimit").toLowerCase()).toString();
							}
						}
						
						if(maxTimeAllowedString !=null && maxTimeAllowedString.length() >0)
						{
							////need to evaluate maxTimeAllowedString
							
							scormPkgDetails.setMaxTimeAllowedSeconds(0);
						}
						else
						{
							scormPkgDetails.setMaxTimeAllowedSeconds(0);						
						}
					}
					else
					{
						scormPkgDetails.setMaxTimeAllowedSeconds(0);						
					}
					*/
					
					
					
					if(courseDetails.get(("maxtimeallowedValue")) !=null)
					{
						maxTimeAllowedString = courseDetails.get("maxtimeallowedValue").toString();
						
						if(maxTimeAllowedString !=null && maxTimeAllowedString.length() >0)
						{
							////need to evaluate maxTimeAllowedString
							maxTimeAllowed = Long.parseLong(maxTimeAllowedString);
							scormPkgDetails.setMaxTimeAllowedSeconds(maxTimeAllowed);
						}
						else
						{
							scormPkgDetails.setMaxTimeAllowedSeconds(0);						
						}
					}
					else
					{
						scormPkgDetails.setMaxTimeAllowedSeconds(0);						
					}
					
					
					
					//END: ENGSUP-31157
					
					log.debug("TITLE::::::::::"+courseDetails.get("title").toString());
					log.debug("schemaversion ::::::::: "+courseDetails.get("schemaversion").toString());
					log.debug("description ::::::::: "+courseDetails.get("description").toString());
					//set SCOs...
					List scos = ((ArrayList<String>)courseDetails.get("href"));
					if(scos.size()==0){
						errors.add("error.instructor.addCourse.scormCourse.save.cannotParseManifestFile");
						log.error("Cannot upload course.Unable to parse the \"imsmanifest.xml\" file for the attribute 'adlcp:scormtype'.");
					}
					String newScos[]=new String[scos.size()];
					int i=0;
					for (Object sco : scos) {
						newScos[i] = sco.toString();
						i++;					
					}
					scormPkgDetails.setScoURI(newScos);
					courses.add(scormPkgDetails);
				}
				else 
				{
					//some info is missing.........
					errors.add("error.instructor.addCourse.scormCourse.save.noMmissingInformation");
					log.error("Incorrect file format or some information is missing.");

				}
				form.setScormCourses(courses);
			}
		}
		return errors;
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		if(((CourseDetails)command).getCourseType().equalsIgnoreCase(SCORM_PACKAGE)){
			cleanUpFiles((CourseDetails)command);//canceling the wizard........... delete uploaded files...
		}
		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processFinish");
		Customer customer = getCustomer();
		Map<Object, Object> context = new HashMap<Object, Object>();
		CourseDetails form = (CourseDetails)command;
		Course dbCourse=null;

		if(form.getCourseType().equals(SCORM_PACKAGE))
		{	
			log.debug("Saving SCORM COURSE : "+form.getCourseType());	
			int j=0;
			for (CourseDetails scormCourse : form.getScormCourses()) {
				scormCourse.setCourseType(form.getCourseType());
				 dbCourse = fillCourse(request, response, scormCourse, error, customer);
				//save each course...
				String errorValue = this.addCourse(dbCourse,error);				
				ScormCourse courseToUpdate = (ScormCourse)(courseAndCourseGroupService.loadForUpdateCourse(dbCourse.getId()));
				courseToUpdate.setDescription(courseToUpdate.getDescription()+" ");
				long contentOwnerId = courseToUpdate.getContentOwner().getId(); ;
				long courseId = courseToUpdate.getId();
				if(courseToUpdate instanceof ScormCourse)//special code for scormcourse so that SCOs should also get saved....Tahir Mehmood
				{

					String[] scos = scormCourse.getScoURI();
					ArrayList<SCO> scosList = new ArrayList<SCO>(); 
					log.debug("........ADDING SCOs to the SCORM package.."+scos.length);
					int i=0;
					for (String scoUri : scos) {
						SCO scoObj = new SCO();
						
						// [03/21/2011] LMS-8856 :: Upload SCORM Courses in custom folder
						String scoLaunchURI = VU360Properties.getVU360Property("scormPackage.SCOPrefix")
						.concat(String.valueOf(contentOwnerId)).concat("/").concat(String.valueOf(courseId)).concat("/").concat(scoUri);
						scoObj.setLaunchURI( scoLaunchURI );
						
						//BEGIN: ENGSUP-31157
						scoObj.setLaunchData(scormCourse.getLaunchData());
						scoObj.setMaximumTimeAllowedSeconds(scormCourse.getMaxTimeAllowedSeconds());
						scoObj.setTimeLimitAction(scormCourse.getTimeLimitAction());
						scoObj.setCompletionThreshold(scormCourse.getCompletionThreshold());
						scoObj.setScaledPassingScore(new Double(scormCourse.getMasteryScore()));
						//END: ENGSUP-31157
						
						scoObj.setLastUpdatedUsername(customer.getName());
						scoObj.setCreatedUsername(customer.getName());
						scoObj.setCreatedDate(new Date());
						scoObj.setLastUpdatedDate(new Date());
						scoObj.setDefaultSequenceOrder(i);
						scoObj.setCourse(courseToUpdate);
						i++;				
						scosList.add(scoObj);
					}
					courseToUpdate.setScos(scosList);
					courseAndCourseGroupService.saveCourse(courseToUpdate);
										
					// [08/19/2009] LMS-6639 :: Code revamped > Extract content to "scormPackage.permanentSaveLocation" instead of "application.getRealPath("/")"					
					String permanentSaveLocation = VU360Properties.getVU360Property("scormPackage.permanentSaveLocation");//for saving the file in Permanent Location
					permanentSaveLocation = permanentSaveLocation + File.separator + contentOwnerId + File.separator + courseId + File.separator;
					log.debug("EXTRACTING Contents of the uploaded SCORM Package : " + form.getScormPackages().get(j) + " PATH = "+ permanentSaveLocation );
					FileUploadUtils.extractZIPFile(form.getScormPackages().get(j), permanentSaveLocation);

					j++; //loop counter or files counter.,
				}
				if(!errorValue.equals(""))
				{
					error.reject(errorValue);
					log.error("There is some error while saving the course. course save failed . "+error.getModel().size());
					return showPage(request, error, getCurrentPage(request));	
				}
			}
			
			//now remove the file names from the record... so that they should not be processed again...
			form.getScormPackages().removeAllElements();	
		}
		else//not a SCORM course.... save only one course.
		{
			 dbCourse = fillCourse(request, response, form, error, customer);

			String errorValue = this.addCourse(dbCourse,error);
			if(!errorValue.equals(""))
			{
				error.reject(errorValue);
				log.error("There is some error while saving the course. course save failed . "+error.getModel().size());
				return showPage(request, error, getCurrentPage(request));		

			}
		}
		
		//Now Create a Course Type Contract for this if it doesnt already exist and associate
		
		entitlementService.assignCourseIntoSystemManagedContract(dbCourse, customer);
		
		
		return new ModelAndView(closeTemplate, "context", context);
	}

	public void cleanUpFiles(CourseDetails courseForm)
	{
		if(courseForm.getScormPackages() != null){

			Vector<String> tempFileNames = courseForm.getScormPackages();
			if(tempFileNames.size()!=0)
			{
				for (String tempFileName : tempFileNames) {					
					File tempFile = new File(tempFileName);//file to be moved...
					tempFile.delete();	//delete all temp files...  that were uploaded now.		
				}
			}

			//now remove the file names from the static field... so that they should not be processed again...
			courseForm.getScormPackages().removeAllElements();
		}
	}
	protected Course fillCourse(HttpServletRequest request, HttpServletResponse response, CourseDetails command, BindException error, Customer customer) throws Exception {

		CourseDetails form = command;
		Course dbCourse = getCourseType(form.getCourseType());

		if(dbCourse instanceof WebLinkCourse)
		{
			((WebLinkCourse) dbCourse).setLink(form.getLink());
		}

		dbCourse.setCourseTitle(form.getCourseName());
		dbCourse.setCourseId(form.getCourseID());
		dbCourse.setKeywords(form.getKeywords());
		dbCourse.setCredithour(form.getCreditHours());
		dbCourse.setVersion(form.getVersion());//should i save it or not. as its already present in dbCourse.java
		dbCourse.setBussinesskey(form.getBusinessUnit());
		dbCourse.setCourseStatus(Course.PUBLISHED);//we need to save the course in published state....
		VU360User myUser = vu360UserService.getUserByGUID(customer.getCustomerGUID());// user created at the time of customer creation shares the same GUID. Reference#CustomerServiceImpl->addCustomer()
		ContentOwner contentOwner = customer.getDistributor().getContentOwner();
		if(contentOwner==null && customer.getContentOwner()==null){
			contentOwner = authorService.addContentOwnerIfNotExist(customer, myUser.getId());
		}
		else if(contentOwner==null && customer.getContentOwner()!=null){
			contentOwner = customer.getContentOwner();
		}
		else{
			contentOwner = authorService.addContentOwnerIfNotExist(customer, myUser.getId());
		}
		customer = this.getCustomerService().loadForUpdateCustomer(customer.getId());
		customer.setContentOwner(contentOwner);
		this.getCustomerService().updateCustomer(customer);
		
		
		dbCourse.setContentOwner(contentOwner);

		log.debug("language:::: >>>> "+form.getLanguage());
		Language language=new Language();
		try{
			language.setId(1l);
			language.setLanguage("en");
			language.setCountry("US");
			language.setVariant("En-US");			
		}catch(Exception pe)
		{
			pe.printStackTrace();
		}

		dbCourse.setLanguage(language);//its hard coded... need to have a service class for  accessing languages,
		dbCourse.setDescription(form.getDescription());

		dbCourse.setCourseGuide(form.getCourseGuide());
		dbCourse.setCoursePrereq(form.getPreRequisites());

		dbCourse.setLearningObjectives(form.getLearningObjectives());
		dbCourse.setQuizInfo(form.getQuizInformation());
		dbCourse.setFinalExamInfo(form.getFinalExamInformation());
		dbCourse.setEndofCourseInstructions(form.getEndOfCourseInstructions());

		dbCourse.setDeliveryMethod(form.getDeliveryMethod());
		if(form.getMsrp()!=null && !form.getMsrp().equals(""))
		{
			dbCourse.setMsrp(Double.parseDouble(form.getMsrp()));
		}
		dbCourse.setCode(form.getCourseCode());
		if(form.getApprovedCourseHours()!=null && !form.getApprovedCourseHours().equals(""))
		{
			dbCourse.setApprovedcoursehours(Double.parseDouble(form.getApprovedCourseHours()));
		}
		dbCourse.setApprovalNumber(form.getApprovalNumber());
		dbCourse.setCurrency(form.getCurrency());
		if(form.getProductPrice()!=null && !form.getProductPrice().equals(""))
		{
			dbCourse.setProductprice(Double.parseDouble(form.getProductPrice()));
		}
		if(form.getWholeSalePrice()!=null && !form.getWholeSalePrice().equals(""))
		{
			dbCourse.setWholeSalePrice(Double.parseDouble(form.getWholeSalePrice()));
		}
		dbCourse.setRoyaltyPartner(form.getRoyaltyPartner());
		dbCourse.setRoyaltyType(form.getRoyaltyType());
		dbCourse.setStateRegistartionRequired(form.getRegulatoryRequirement());
		if(form.getTermsOfService()!=null && !form.getTermsOfService().equals(""))
		{
			dbCourse.setTos(Integer.parseInt(form.getTermsOfService()));
		}


		return dbCourse;

	}
	// [4/21/2010] VCS-263 :: Wrapper method exclusively due to Discussion Forum type courses along with other types
	private String addCourse(Course course, BindException error){	

		String errorCode = "";
		if(course instanceof DiscussionForumCourse){
			Course retCourse = courseAndCourseGroupService.addDiscussionForumCourse((DiscussionForumCourse)course);
			if (retCourse == null){				
				errorCode = "error.instructor.addCourse.dfcCourse.save.failed";
			}
		}
		else{
			Course retOtherCourse = courseAndCourseGroupService.addCourse(course);
			if(retOtherCourse == null)
			{
				errorCode = "error.instructor.addCourse.dfcCourse.save.failed";
			}
		}
		return errorCode;
	}


	public Course getCourseType(String courseType)
	{
		Course dbcourse = null;
		/*if(courseType.equals(DISCUSSION_FORUM))
		{
			log.debug("courseType.equals(DISCUSSION_FORUM)::::: >"+(courseType.equals(DISCUSSION_FORUM)));
			dbcourse=new DiscussionForumCourse();
			return dbcourse;
		}*/
		if(courseType.equals(SCORM_PACKAGE))
		{
			log.debug("courseType.equals(SCORM_PACKAGE))::::: >"+(courseType.equals(SCORM_PACKAGE)));
			dbcourse=new ScormCourse();
			return dbcourse;
		}
		if(courseType.equals(SYNCHRONOUS_COURSE))
		{
			log.debug("courseType.equals(SYNCHRONOUS_COURSE)::::: >"+(courseType.equals(SYNCHRONOUS_COURSE)));
			dbcourse=new SynchronousCourse();
			return dbcourse;
		}
		if(courseType.equals(WEBINAR_COURSE))
		{
			log.debug("courseType.equals(WEBINAR_COURSE)::::: >"+(courseType.equals(WEBINAR_COURSE)));
			dbcourse=new WebinarCourse();
			return dbcourse;
		}
		if(courseType.equals(WEBLINK_COURSE))
		{
			log.debug("courseType.equals(WEBLINK_COURSE)::::: >"+(courseType.equals(WEBLINK_COURSE)));
			dbcourse=new WebLinkCourse();
			return dbcourse;
		}
		return new Course();

	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
	/*
	 * Author: Faisal Ahmed Siddiqui
	 * Description: This method encapsulates the logic of getting current customer
	 * In case of login as manager this method will return customer selected in Admin mode
	 * else it will return customer that logged-in user belongs to
	 */
	private Customer getCustomer(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			customer = details.getCurrentCustomer();
		}
		if(customer == null){
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			customer = loggedInUser.getLearner().getCustomer();
		}
		return customer;	
	}

	/**
	 * @return the authorService
	 */
	public AuthorService getAuthorService() {
		return authorService;
	}

	/**
	 * @param authorService the authorService to set
	 */
	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}
	

	
}
