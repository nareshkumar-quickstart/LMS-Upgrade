package com.softech.vu360.lms.web.controller.instructor;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.ExternalCourse;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.instructor.CourseDetails;
import com.softech.vu360.lms.web.controller.validator.AddCourseValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class InstuctorEditCourseController extends VU360BaseMultiActionController {

    private static final Logger log = Logger.getLogger(InstuctorEditCourseController.class.getName());

    private static final String[] BUSINESS_UNITS = { "All", "Business Development", "EMEA", "Ethics and Compliance", "Financial Services",
	    "Healthcare", "Higher Education", "Occupational Training", "Public Safety", "Real Estate" };
    private static final String[] GRADING_METHODS = { "Simple", "Scored" };
    private static final String[] INSTRUCTOR_TYPES = { "Email Online", "Webinar"};
    private static final String[] LANGUAGES = { "English" };
    private static final String[] REGULATORY_REQUIREMENT = { "None", "Percentage", "Flat Dollar" };

    private String summaryTemplate = null;
    private String closeTemplate = null;
    private String courseOverViewTemplate = null;
    private String courseExamOverviewTemplate = null;
    private String courseAdditionalDetailsTemplate = null;
    private String scheduleTemplate = null;
    private CourseAndCourseGroupService courseAndCourseGroupService;
    private SynchronousClassService synchronousClassService = null;
    private LMSProductPurchaseService lmsProductPurchaseService;
    private AccreditationService accreditationService;
    private VelocityEngine velocityEngine;
    private EnrollmentService enrollmentService;

    @Override
    protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

		log.debug("onBind");
	
		if (methodName.equals("viewCourseSummary") || methodName.equals("viewCourseOverView") || methodName.equals("viewCourseExamOverView")
			|| methodName.equals("viewCourseAdditionalDetails")) {
			
			if (!StringUtils.isBlank(request.getParameter("start"))) {
				long cid = Long.parseLong(request.getParameter("id"));
		
				Course course = (Course) courseAndCourseGroupService.getCourseByIdWithNoCache(cid);
		
				com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				log.debug(user == null ? "User null" : " learnerId=" + user.getLearner());
				// Brander
				// brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				// Learner learner = user.getLearner();
		
				CourseDetails form = (CourseDetails) command;
		
				// form.setLearnerEnrollmentId(course.);
				Course dbcourse = getCourseType(course);
				form.setId(cid);
				form.setCourseType(dbcourse.getCourseType());
				form.setCourseName(dbcourse.getCourseTitle());
				form.setCourseID(dbcourse.getCourseId());
				form.setKeywords(dbcourse.getKeywords());
				form.setCreditHours(dbcourse.getCredithour());
				form.setVersion(dbcourse.getVersion());
				form.setBusinessUnit(dbcourse.getBusinessUnitName());
				Language lang = dbcourse.getLanguage();
		
				if (lang != null) {
				    form.setLanguage(lang.getId() + "");
				}
				if (course.getCourseType().equals(WebLinkCourse.COURSE_TYPE)) {
				    form.setLink(((WebLinkCourse) course).getLink());
				}
		
				if (course.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
					
					List<LearnerEnrollment> lstLearnerEnrollment= enrollmentService.getLearerEnrollmentsByCourse(cid);
					if(lstLearnerEnrollment != null)
					{
						if(lstLearnerEnrollment.size() > 0)
						{
							form.setActiveEnrollment(lstLearnerEnrollment.size());
						}
						else
						{
							form.setActiveEnrollment(0);
						}
					}
					/*
					List<HomeWorkAssignmentAsset> lsthomeworkassignmentasset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(cid);
					List<Document> lstDoc = new ArrayList<Document>();
					if(lsthomeworkassignmentasset != null)
					{
						if(lsthomeworkassignmentasset.size() > 0)
						{
							for(HomeWorkAssignmentAsset homeworkassignmentasset: lsthomeworkassignmentasset)
							{
								if(homeworkassignmentasset.getAsset() instanceof Document)
								{
									Document doc = new Document();
									doc.setFileName(((Document)homeworkassignmentasset.getAsset()).getFileName());
									doc.setId(homeworkassignmentasset.getAsset().getId());
									lstDoc.add(doc);
								}
							}
						}
					}
					*/
					
					List<HomeWorkAssignmentAsset> lsthomeworkassignmentasset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(cid);
					List<Document> lstDoc = new ArrayList<Document>();
					
					if(lsthomeworkassignmentasset != null)
					{
						if(lsthomeworkassignmentasset.size() > 0)
						{
						  form.setLstHomeWorkAssignmentAsset(lsthomeworkassignmentasset);
						  form.setBlankHomeAssignementfile(Boolean.FALSE);
						}
					}	
						
					/*
					if(lstDoc != null)
					{
						if(lstDoc.size() > 0)
						{
							form.setLstDocument(lstDoc);
						}
					}
					*/
				    HomeworkAssignmentCourse homeworkAssignment = (HomeworkAssignmentCourse) course;
				    log.debug("setAssignmentDueDate  : " + homeworkAssignment.getAssignmentDueDate());
				    if (homeworkAssignment.getAssignmentDueDate() != null) {
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
					form.setAssignmentDueDate(formatter.format(homeworkAssignment.getAssignmentDueDate()));
				    }
		
				    Double masterScore = homeworkAssignment.getMasteryScore();
				    if (masterScore != null)
					form.setMasterScore(masterScore.toString());
				    form.setGradingMethod(homeworkAssignment.getGradingMethod());
				    Document doc = homeworkAssignment.getHwAssignmentInstruction();
		
				    if(null!= doc )
				    	form.setHwAssignmentInstruction(doc.getFileName());
		
				}
				log.debug("course.getCourseType() = " + course.getCourseType());
				if (course.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE)) {
				    log.debug("course.getCourseType() = " + course.getCourseType());
				    InstructorConnectCourse instructorConnectCourse = (InstructorConnectCourse) course;
				    form.setInstructorType(instructorConnectCourse.getInstructorType());
				    form.setMeetingId(instructorConnectCourse.getMeetingId());
				    form.setMeetingPasscode(instructorConnectCourse.getMeetingPasscode());
				    form.setEmailAddress(instructorConnectCourse.getEmailAddress());
				}
		
				form.setDescription(dbcourse.getDescription());
		
				// Setting Up Course Over View
				form.setCourseGuide(course.getCourseGuide());
				form.setPreRequisites(course.getCoursePrereq());
		
				// Setting Up Course Additional Info View
				try{
					if(course.getDeliveryMethodId()!=null)
						form.setDeliveryMethodId(course.getDeliveryMethodId().toString());
					else
						form.setDeliveryMethodId("1");
				}catch(Exception sube){
					form.setDeliveryMethodId("1");
				}
				
				BigDecimal bigDMsrp = new BigDecimal(course.getMsrp(),MathContext.DECIMAL128);
				// form.setMsrp(bigDMsrp.toPlainString());
				form.setMsrp(bigDMsrp.setScale (2,
						BigDecimal.ROUND_HALF_UP).toPlainString());
				
				form.setCourseCode(course.getCode());
				form.setApprovedCourseHours(String.valueOf(course.getApprovedcoursehours()));
				
				//LMS-15747
				form.setDurationHours(String.valueOf(course.getCeus()));
				form.setApprovalNumber(course.getApprovalNumber());
				form.setCurrency(course.getCurrency());
				
				BigDecimal bigDProductPrice = new BigDecimal(course.getProductprice(),MathContext.DECIMAL128);
				// form.setProductPrice(bigDProductPrice.toPlainString());
				form.setProductPrice(bigDProductPrice.setScale (2,
						BigDecimal.ROUND_HALF_UP).toPlainString());
				if (course.getWholeSalePrice() != null) {
					BigDecimal bigDWholeSalePrice = new BigDecimal(course.getWholeSalePrice(),MathContext.DECIMAL128);
					
					form.setWholeSalePrice(bigDWholeSalePrice.setScale (2,
							BigDecimal.ROUND_HALF_UP).toPlainString());	
				}
				form.setRoyaltyPartner(course.getRoyaltyPartner());
				form.setRoyaltyType(course.getRoyaltyType());
		
				form.setRegulatoryRequirement(course.getStateRegistartionRequired());
				form.setTermsOfService(Integer.toString(course.getTos()));
		
				// Setting Up EXAM Over View
		
				form.setLearningObjectives(course.getLearningObjectives());
				form.setQuizInformation(course.getQuizInfo());
				form.setFinalExamInformation(course.getFinalExamInfo());
				form.setEndOfCourseInstructions(course.getEndofCourseInstructions());
			}
			
			if(methodName.equals("viewCourseAdditionalDetails")){
				long cid = 0;
				CourseDetails form = (CourseDetails) command;
				List<SynchronousClass> synchClassesList = null;
				System.out.println(form.getDeliveryMethod()+"|"+form.getDeliveryMethodId());
			
				if(request.getParameter("id")!=null){
					cid = Long.parseLong(request.getParameter("id"));
				}
				else if(form!=null && form.getCourseID()!=null){
					
					cid = form.getId();
				}
				
				if(cid>0){
					synchClassesList = synchronousClassService.getAllSynchClassesOfCourse(cid);
				}
				
			    if(synchClassesList!=null && synchClassesList.size()>0){
			    	String meetingType = ((SynchronousClass)synchClassesList.get(0)).getMeetingType();
					System.out.println("meetingType"+meetingType);
					if(meetingType!=null ){
						form.setDeliveryMethod("Webinar");
				    	form.setDeliveryMethodId("8");
					}
					else {
						form.setDeliveryMethod("Classroom");
				    	form.setDeliveryMethodId("7");
					}
			    }
			    else{
			    	form.setDeliveryMethod("");
			    	form.setDeliveryMethodId("");
			    }
			}
		}
    }

    @Override
    protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
	log.debug("inside validate .:::::::::");
	CourseDetails form = (CourseDetails) command;
	List<HomeWorkAssignmentAsset> lstHomeworkAssignmentAsset = null;
	lstHomeworkAssignmentAsset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(form.getId()) ;
	AddCourseValidator validator = (AddCourseValidator) this.getValidator();
	if (StringUtils.isBlank(request.getParameter("start")) && methodName!=null && methodName.equalsIgnoreCase("saveAllInfo")) {
	    if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
				Brander brander = VU360Branding.getInstance()
						.getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new com.softech.vu360.lms.vo.Language());
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		    Map files = multiRequest.getFileMap();
		    Iterator fileIterator=files.values().iterator();
		    MultipartFile file;
		    String fileName = null;
		    boolean fileError = false;
		    boolean hasNext = fileIterator.hasNext();
		    //if(hasNext && !form.isBlankHomeAssignementfile())
		    if(hasNext)
		    {
		    	form.setBlankHomeAssignementfile(false);
		    }
		    else if(lstHomeworkAssignmentAsset != null && lstHomeworkAssignmentAsset.size() == 0)
		    {
		       form.setBlankHomeAssignementfile(true);
		    }
			validator.validateEditHW(form, errors, brander);
			validator.validate(form, errors);
	    } else if (form.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE)) {
			validator.validatePage7(form, errors);
			validator.validate(form, errors);
	    } else {
	    	validator.validate(form, errors);
	    }
	    
	    /*if (form.getCourseType().equals(SynchronousCourse.COURSE_TYPE) && methodName.equals("saveAllInfo") ) {
	    	
	    	if(request.getParameter("tabName")!=null && request.getParameter("tabName").equalsIgnoreCase("additionalDetailsTab")){
		    	try{
			    	if(form.getProductPrice()==null || form.getProductPrice().equals("") || Double.parseDouble(form.getProductPrice()) < 1){
			    		errors.rejectValue("productPrice", "error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
			    	}
		    	} catch (Exception sube) {
						errors.rejectValue("productPrice", "error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
				}
	    	}
	    }*/
	}
    }

    @SuppressWarnings("unchecked")
    public ModelAndView downloadUploadedFile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	CourseDetails form = (CourseDetails) command;
	log.debug("INSIDE downloadUploadedFile()");
	Map model = new HashMap();
	model.put("businessUnits", BUSINESS_UNITS);
	model.put("languages", LANGUAGES);
	log.debug("form.getCourseType() " + form.getCourseType());
	if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
	    model.put("gradingMethods", GRADING_METHODS);
	    String fileName = form.getHwAssignmentInstruction();
	    log.debug("fileName " + fileName);
	    String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
	    String filePath = fileLocation + File.separator + fileName;
	    if (!FileUploadUtils.downloadFile(filePath, response)) {
		errors.rejectValue("file", "error.learner.launchcourse.file.notfound");
	    }

	}
	return new ModelAndView(summaryTemplate, "context", model);
    }

    @SuppressWarnings("unchecked")
    public ModelAndView viewCourseSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	CourseDetails form = (CourseDetails) command;
	log.debug("INSIDE viewCourseSummary()");
	Map model = new HashMap();
	model.put("businessUnits", BUSINESS_UNITS);
	model.put("languages", LANGUAGES);
	log.debug("form.getCourseType() = " + form.getCourseType());
	if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
	    model.put("gradingMethods", GRADING_METHODS);
	} else if (form.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE)) {
	    model.put("instructorTypes", INSTRUCTOR_TYPES);
	}

	return new ModelAndView(summaryTemplate, "context", model);
    }

    public ModelAndView saveUpdateCourseOverview(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	log.debug("inside saveUpdateCourseOverview method");
	CourseDetails form = (CourseDetails) command;
	Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));

	course.setCourseGuide(form.getCourseGuide());
	course.setCoursePrereq(form.getPreRequisites());

	courseAndCourseGroupService.saveCourse(course);

	return new ModelAndView(closeTemplate);
    }

    public ModelAndView viewCourseOverView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	log.debug("inside viewCourseOverView()");

	return new ModelAndView(courseOverViewTemplate);

    }

    public ModelAndView saveUpdateCourseExamOverView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	log.debug("inside saveUpdateCourseExamOverView method");
	CourseDetails form = (CourseDetails) command;
	Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));

	course.setLearningObjectives(form.getLearningObjectives());
	course.setQuizInfo(form.getQuizInformation());
	course.setFinalExamInfo(form.getFinalExamInformation());
	course.setEndofCourseInstructions(form.getEndOfCourseInstructions());

	courseAndCourseGroupService.saveCourse(course);

	return new ModelAndView(closeTemplate);
    }

    public ModelAndView viewCourseExamOverView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	log.debug("inside viewCourseExamOverView()");

	return new ModelAndView(courseExamOverviewTemplate);
    }

    public ModelAndView saveUpdateCourseAdditionalDetails(HttpServletRequest request, HttpServletResponse response, Object command,
	    BindException errors) throws Exception {

	if (errors.hasErrors()) {
	    Map model = new HashMap();
	    model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
	    return new ModelAndView(courseAdditionalDetailsTemplate, "context", model);
	}
	if (!errors.hasErrors()) {
	    log.debug("inside saveUpdateCourseExamOverView method");
	    CourseDetails form = (CourseDetails) command;
	    Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));

	    try{
	    	course.setDeliveryMethodId(Long.parseLong(form.getDeliveryMethodId()));
		}catch(Exception sube){
			course.setDeliveryMethodId(1l);
		}
		
	    if (form.getMsrp() != null && !form.getMsrp().equals("")) {
		course.setMsrp(Double.parseDouble(form.getMsrp()));
	    } else
		course.setMsrp(0.0);
	    course.setCode(form.getCourseCode());
	    if (form.getApprovedCourseHours() != null && !form.getApprovedCourseHours().equals("")) {
		course.setApprovedcoursehours(Double.parseDouble(form.getApprovedCourseHours()));
	    } else
		course.setApprovedcoursehours(0.0);
	    course.setApprovalNumber(form.getApprovalNumber());
	    course.setCurrency(form.getCurrency());
	    if (form.getProductPrice() != null && !form.getProductPrice().equals("")) {
		course.setProductprice(Double.parseDouble(form.getProductPrice()));
	    } else
		course.setProductprice(0.0);
	    if (form.getWholeSalePrice() != null && !form.getWholeSalePrice().equals("")) {
		course.setWholeSalePrice(Double.parseDouble(form.getWholeSalePrice()));
	    } else
		course.setWholeSalePrice(0.0);
	    course.setRoyaltyPartner(form.getRoyaltyPartner());
	    course.setRoyaltyType(form.getRoyaltyType());
	    course.setStateRegistartionRequired(form.getRegulatoryRequirement());
	    if (form.getTermsOfService() != null && !form.getTermsOfService().equals("")) {
		course.setTos(Integer.parseInt(form.getTermsOfService()));
	    } else
		course.setTos(0);

	    course.setLearningObjectives(form.getLearningObjectives());

	    courseAndCourseGroupService.saveCourse(course);

	    return new ModelAndView(closeTemplate);
	} else
	    return new ModelAndView(courseAdditionalDetailsTemplate);

    }

    public ModelAndView viewCourseAdditionalDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	log.debug("inside viewCourseAdditionalDetails()");
	Map model = new HashMap();

	model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
	return new ModelAndView(courseAdditionalDetailsTemplate, "context", model);
    }

    @SuppressWarnings("unchecked")
    public ModelAndView saveUpdateCourseSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	log.debug("inside saveUpdateCourseSummary .:::::::::");
	if (errors.hasErrors()) {
	    Map model = new HashMap();
	    model.put("businessUnits", BUSINESS_UNITS);
	    model.put("languages", LANGUAGES);
	    return new ModelAndView(summaryTemplate, "context", model);
	}
	CourseDetails form = (CourseDetails) command;
	log.debug("====================== Form CourseType : " + form.getCourseType());
	Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));

	log.debug("====================== DB CourseType : " + course.getCourseType());

	course.setCourseTitle(form.getCourseName());
	course.setCourseId(form.getCourseID());
	course.setKeywords(form.getKeywords());
	course.setCredithour(form.getCreditHours());
	course.setVersion(form.getVersion());// should i save it or not. as its
					     // already present in
					     // course.java
	course.setBussinesskey(course.getCourseId());
	course.setBusinessUnitName(form.getBusinessUnit());
	course.setBusinessUnitId(this.getBusinessUnitId(course.getBusinessUnitName()));
	course.setCourseStatus(Course.PUBLISHED);

	// [4/22/2010] VCS-264 :: Content Owner code was missing; required for
	// Discussion Forum during call to Web Service
	ContentOwner contentOwner = this.getContentOwner();
	course.setContentOwner(contentOwner);

	if (form.getCourseType().equals(WebLinkCourse.COURSE_TYPE)) {
	    ((WebLinkCourse) course).setLink(form.getLink());
	}
	if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    if (!StringUtils.isBlank(form.getAssignmentDueDate())) {
		((HomeworkAssignmentCourse) course).setAssignmentDueDate(formatter.parse(form.getAssignmentDueDate()));
	    }
	    if (!StringUtils.isBlank(form.getMasterScore())) {
		((HomeworkAssignmentCourse) course).setMasteryScore(Double.parseDouble(form.getMasterScore()));
	    }
	    ((HomeworkAssignmentCourse) course).setGradingMethod(form.getGradingMethod());

	    log.debug("saveUpdateCourseSummary HomeworkAssignment : " + ((HomeworkAssignmentCourse) course).getHwAssignmentInstruction());
	    /*
	    if (form.getFile() != null && !form.getFile().isEmpty()) {

		Document doc = uploadHomeWorkAssignment(form);
		accreditationService.saveDocument(doc);
		((HomeworkAssignmentCourse) course).setHwAssignmentInstruction(doc);
	    }
	    */
	    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		List<Document> lstdoc = new ArrayList<Document>();
		lstdoc = uploadHomeWorkAssignment(form,multiRequest);
		log.debug("Multiple Upload Document: " + lstdoc.size());
	    for(Document doc : lstdoc)
		    {
		    	accreditationService.saveDocument(doc);
		    	HomeWorkAssignmentAsset homeWorkAssignmentAsset = new HomeWorkAssignmentAsset();
		    	homeWorkAssignmentAsset.setAsset(doc);
		    	homeWorkAssignmentAsset.setHomeWorkAssignmentCourse(course);
		    	lmsProductPurchaseService.saveHomeWorkAssignmentAsset(homeWorkAssignmentAsset);
		    }  
	}
	
	log.debug("language:::: >>>> " + form.getLanguage());
	Language language = new Language();
	try {
	    language.setId(1l);
	    language.setLanguage("en");
	    language.setCountry("US");
	    language.setVariant("En-US");
	} catch (Exception pe) {
	    pe.printStackTrace();
	}
	course.setLanguage(language);// its hard coded... need to have a service
				     // class for accessing languages,
	course.setDescription(form.getDescription());

	log.debug("====================== Form CourseType : " + form.getCourseType());

	log.debug("====================== DB CourseType : " + course.getCourseType());

	// [4/22/2010] VCS-264 :: Error handling in case of failure
	Map<Object, Object> context = new HashMap<Object, Object>();
	context.put("errorMessage", this.saveCourse(course, errors));

	return new ModelAndView(closeTemplate, "context", context);
    }

    private ContentOwner getContentOwner() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Customer customer = null;
	/*
	 * if (auth.getDetails() != null && auth.getDetails() instanceof
	 * VU360UserAuthenticationDetails) { VU360UserAuthenticationDetails
	 * details = (VU360UserAuthenticationDetails) auth .getDetails(); if
	 * (details.getCurrentCustomer() != null) { customer =
	 * details.getCurrentCustomer(); } } if (customer == null) { VU360User
	 * loggedInUser = (VU360User) SecurityContextHolder
	 * .getContext().getAuthentication().getPrincipal(); customer =
	 * loggedInUser.getLearner().getCustomer(); } return
	 * courseAndCourseGroupService.getContentOwnerByCustomer(customer);
	 */
	if (auth.getDetails() != null && auth.getDetails() instanceof VU360UserAuthenticationDetails) {
	    VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
		customer = details.getCurrentCustomer();
	}
	if (customer == null) {
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
	    customer = loggedInUser.getLearner().getCustomer();
	}
	ContentOwner contentOwner = null;
	if (customer.isDistributorRepresentative()) {
	    contentOwner = customer.getDistributor().getContentOwner();
	}
	if (contentOwner == null)
	    contentOwner = customer.getContentOwner();

	return contentOwner;

    }

    // [4/22/2010] VCS-264 :: Wrapper method exclusively due to Discussion Forum
    // type courses along with other types
    private String saveCourse(Course course, BindException error) {

	String errorCode = "";
	if (course instanceof DiscussionForumCourse) {
	    Course retCourse = courseAndCourseGroupService.updateDiscussionForumCourse((DiscussionForumCourse) course);
	    if (retCourse == null) {
		errorCode = "error.instructor.editCourse.dfcCourse.save.failed";
	    }
	} else {
	    courseAndCourseGroupService.saveCourse(course);
	}
	return errorCode;
    }

    public ModelAndView viewDFCourseCourses(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	// DFC course, course tab functionality goes here........

	return new ModelAndView(courseAdditionalDetailsTemplate);
    }

    public ModelAndView saveUpdateDFCourseCourses(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	return new ModelAndView(closeTemplate);

    }

    public ModelAndView viewCourseSchedule(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	Long instructorId;
	Map<Object, Object> context = new HashMap<Object, Object>();

	com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	instructorId = user.getInstructor().getId();
	List<SynchronousClass> synchClassesList = synchronousClassService.getAllSynchronousClasses(instructorId);

	context.put("mySynchClassList", synchClassesList);

	return new ModelAndView(scheduleTemplate, "context", context);
    }

    public ModelAndView saveUpdateCourseSchedule(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	// DFC course, course tab functionality goes here........
	return new ModelAndView(closeTemplate);

    }

    @SuppressWarnings("unchecked")
    public ModelAndView deleteDocument(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	Map context = new HashMap();
	
	CourseDetails form = (CourseDetails) command;
	List<LearnerEnrollment> lstLearnerEnrollment= enrollmentService.getLearerEnrollmentsByCourse(new Long(request.getParameter("id")));
	//if(lstLearnerEnrollment.size() == 0)
	//{
		Long homeWorkAssignmentAsset = new Long(request.getParameter("documentid"));
	    
		lmsProductPurchaseService.deleteHomeWorkAssignmentAsset(homeWorkAssignmentAsset);
		
		List<HomeWorkAssignmentAsset> lstHomeWorkAssignmentAsset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(new Long(request.getParameter("id")));
		
		
		if(lstHomeWorkAssignmentAsset.size() == 0)
		{
			form.setLstHomeWorkAssignmentAsset(null);
		}
		else
		{
		
			List<HomeWorkAssignmentAsset> lsthomeworkassignmentasset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(new Long(request.getParameter("id")));
			List<Document> lstDoc = new ArrayList<Document>();
			
			if(lsthomeworkassignmentasset != null)
			{
				if(lsthomeworkassignmentasset.size() > 0)
				{
				  form.setLstHomeWorkAssignmentAsset(lsthomeworkassignmentasset);
				}
			}
		}
		log.debug("HomeWorkAssignmentAssetId" + homeWorkAssignmentAsset);
	//}
	
	return new ModelAndView(summaryTemplate, "context", context);
	//return new ModelAndView(closeTemplate, "context", context);
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView publishSFCourse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	Map context = new HashMap();
	Course course = null;
	Long id = new Long(request.getParameter("id"));

	course = courseAndCourseGroupService.getCourseByIdWithNoCache(id);
	String distributorCode = this.getResellerCode();
	
	if (!validateData(request, context, course)) {
		if (publishSynchronousCourse(course,distributorCode)) {
		course.setPublishedonstorefront(true);
		course.setCourseStatus(Course.PUBLISHED);
		courseAndCourseGroupService.saveCourse(course);
	    } else {
		context.put("publishingError", "error.publishSynchronousCourse.publishingError");
		context.put("course", course);
		return new ModelAndView(summaryTemplate, "context", context);
	    }
	} else {
	    context.put("course", course);
	    return new ModelAndView(summaryTemplate, "context", context);
	}
	context.put("totalRecord", 0);
	context.put("recordShowing", 0);
	context.put("pageNo", 0);
	context.put("sortDirection", 0);
	return new ModelAndView(closeTemplate, "context", context);
    }
    

    String getResellerCode(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Customer customer = null;
    	
    	if (auth.getDetails() != null && auth.getDetails() instanceof VU360UserAuthenticationDetails) {
    	    VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
    		customer = details.getCurrentCustomer();
    	}
    	if (customer == null) {
    		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
    	    customer = loggedInUser.getLearner().getCustomer();
    	}
    	return customer.getDistributor().getDistributorCode();
    }
    
    public ModelAndView cancelEditCourse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {
	return new ModelAndView(closeTemplate);
    }

    public Course getCourseType(Course course) {

	if (course instanceof DiscussionForumCourse) {
	    log.debug("course instanceof DiscussionForumCourse ::::: >" + (course instanceof DiscussionForumCourse));
	    DiscussionForumCourse dbcourse = (DiscussionForumCourse) course;
	    return dbcourse;
	}
	if (course instanceof ScormCourse) {
	    log.debug("course instanceof ScormCourse ::::: >" + (course instanceof ScormCourse));
	    ScormCourse dbcourse = (ScormCourse) course;
	    return dbcourse;
	}
	if (course instanceof WebLinkCourse) {
	    log.debug("course instanceof WebLinkCourse ::::: >" + (course instanceof WebLinkCourse));
	    WebLinkCourse dbcourse = (WebLinkCourse) course;
	    return dbcourse;
	}
	if (course instanceof SynchronousCourse) {
	    log.debug("course instanceof SynchronousCourse ::::: >" + (course instanceof SynchronousCourse));
	    SynchronousCourse dbcourse = (SynchronousCourse) course;
	    return dbcourse;
	}
	if (course instanceof WebinarCourse) {
	    log.debug("course instanceof WebinarCourse ::::: >" + (course instanceof WebinarCourse));
	    WebinarCourse dbcourse = (WebinarCourse) course;
	    return dbcourse;
	}
	if (course instanceof ExternalCourse) {
	    log.debug("course instanceof ExternalCourse ::::: >" + (course instanceof ExternalCourse));
	    ExternalCourse dbcourse = (ExternalCourse) course;
	    return dbcourse;
	}
	if (course instanceof LegacyCourse) {
	    log.debug("course instanceof LegacyCourse ::::: >" + (course instanceof LegacyCourse));
	    LegacyCourse dbcourse = (LegacyCourse) course;
	    return dbcourse;
	}
	if (course instanceof SelfPacedCourse) {
	    log.debug("course instanceof SelfPacedCourse ::::: >" + (course instanceof SelfPacedCourse));
	    SelfPacedCourse dbcourse = (SelfPacedCourse) course;
	    return dbcourse;
	}
	if (course instanceof HomeworkAssignmentCourse) {
	    log.debug("course instanceof HomeworkAssignment ::::: >" + (course instanceof HomeworkAssignmentCourse));
	    HomeworkAssignmentCourse dbcourse = (HomeworkAssignmentCourse) course;
	    return dbcourse;
	}
	if (course instanceof InstructorConnectCourse) {
	    log.debug("course instanceof InstructorConnectCourse ::::: >" + (course instanceof InstructorConnectCourse));
	    InstructorConnectCourse dbcourse = (InstructorConnectCourse) course;
	    return dbcourse;
	}
	return new Course();

    }

    private boolean validateData(HttpServletRequest request, Map context, Course course) {

	boolean check = false;

	if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("publishSFCourse")) { // [09/21/2010]
															    // LMS-7108
	    List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);
	    List lstError = new ArrayList();
	    if (courseGroups == null || courseGroups.size() <= 0) {
	    	//context.put("validateCoursePublishing", "error.publishSynchronousCourse.courseNotInCourseGroup");
	    	lstError.add("error.publishSynchronousCourse.courseNotInCourseGroup");
	    	check = true;
	    }
	    
	    if((course instanceof SynchronousCourse || course instanceof WebinarCourse) && course.getDeliveryMethodId()==null ){
	    	//context.put("validateCoursePublishing2", "error.publishSynchronousCourse.courseIsNotWebinarOrClassroomType");
	    	lstError.add("error.publishSynchronousCourse.courseIsNotWebinarOrClassroomType");
			check = true;
	    }else if((course instanceof SynchronousCourse || course instanceof WebinarCourse) && course.getDeliveryMethodId()!=7 && course.getDeliveryMethodId()!=8){
	    	//context.put("validateCoursePublishing2", "error.publishSynchronousCourse.courseIsNotWebinarOrClassroomType");
	    	lstError.add("error.publishSynchronousCourse.courseIsNotWebinarOrClassroomType");
			check = true;
	    }
	    
	    if(course instanceof SynchronousCourse || course instanceof WebinarCourse){//LMS-15751 For Synchronous course only
		    List<SynchronousClass> synchClassesList = synchronousClassService.getAllSynchClassesOfCourse(course.getId());
		    if(synchClassesList==null || (synchClassesList!=null && synchClassesList.isEmpty())){
		    	lstError.add("error.publishSynchronousCourse.courseScheduleNotExists");
				check = true;
		    }
		    
		    
		  /*  try{
		    	if(course.getProductprice() < 1.0){
		    		lstError.add("error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
		    		check = true;
		    	}
	    	} catch (Exception sube) {
	    			lstError.add("error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
					check = true;
			}*/
	    }
	    
	    
	    
	    
			if (StringUtils.isBlank(course.getCourseTitle())) {
				lstError.add("error.addCourse.courseName.required");
				check = true;
			}
			if (StringUtils.isBlank(course.getKeywords())) {
				lstError.add("error.addCourse.keyword.required");
				check = true;
			}
			
			
			if (StringUtils.isBlank(course.getVersion())) {
				lstError.add("error.addCourse.version.required");
				check = true;
			}
			
			if (StringUtils.isBlank(course.getDescription())) {
				lstError.add("error.addCourse.description.required");
				check = true;
			}
		
			if (StringUtils.isBlank(course.getApprovalNumber())) {
				lstError.add("error.addCourse.ApprovalNumber.required");
				check = true;
			}
			
			if (StringUtils.isBlank(course.getCurrency())) {
				lstError.add( "error.addCourse.currency.required");
				check = true;
			}
			
			try{
		    	if(course.getProductprice() < 1.0){
		    		lstError.add("error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
		    		check = true;
		    	}
	    	} catch (Exception sube) {
	    			lstError.add("error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
					check = true;
			}
		
	   
    	
	    if(check)
	    	context.put("lstErrors",lstError);
	}
	return check;
    }

    /*
     * Getter Setter Area
     */
    public String getSummaryTemplate() {
	return summaryTemplate;
    }

    public void setSummaryTemplate(String summaryTemplate) {
	this.summaryTemplate = summaryTemplate;
    }

    public String getCloseTemplate() {
	return closeTemplate;
    }

    public void setCloseTemplate(String closeTemplate) {
	this.closeTemplate = closeTemplate;
    }

    public CourseAndCourseGroupService getCourseAndCourseGroupService() {
	return courseAndCourseGroupService;
    }

    public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
	this.courseAndCourseGroupService = courseAndCourseGroupService;
    }

    public VelocityEngine getVelocityEngine() {
	return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
	this.velocityEngine = velocityEngine;
    }

    public String getCourseOverViewTemplate() {
	return courseOverViewTemplate;
    }

    public void setCourseOverViewTemplate(String courseOverViewTemplate) {
	this.courseOverViewTemplate = courseOverViewTemplate;
    }

    public String getCourseExamOverviewTemplate() {
	return courseExamOverviewTemplate;
    }

    public void setCourseExamOverviewTemplate(String courseExamOverviewTemplate) {
	this.courseExamOverviewTemplate = courseExamOverviewTemplate;
    }

    public String getCourseAdditionalDetailsTemplate() {
	return courseAdditionalDetailsTemplate;
    }

    public void setCourseAdditionalDetailsTemplate(String courseAdditionalDetailsTemplate) {
	this.courseAdditionalDetailsTemplate = courseAdditionalDetailsTemplate;
    }

    public String getScheduleTemplate() {
	return scheduleTemplate;
    }

    public void setScheduleTemplate(String scheduleTemplate) {
	this.scheduleTemplate = scheduleTemplate;
    }

    public SynchronousClassService getSynchronousClassService() {
	return synchronousClassService;
    }

    public void setSynchronousClassService(SynchronousClassService synchronousClassService) {
	this.synchronousClassService = synchronousClassService;
    }

    public AccreditationService getAccreditationService() {
	return accreditationService;
    }

    public void setAccreditationService(AccreditationService accreditationService) {
	this.accreditationService = accreditationService;
    }

    private boolean publishSynchronousCourse(Course course, String distributorCode) {
	List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);
	return new StorefrontClientWSImpl().publishCourseEvent(course, courseGroups, distributorCode);

    }

    public ModelAndView saveAllInfo(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

	log.debug("inside saveAllInfo method");
	CourseDetails form = (CourseDetails) command;
	Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));
	log.debug("====================== Form CourseType : " + form.getCourseType());
	log.debug("====================== DB CourseType : " + course.getCourseType());
	// Course Over View
	course.setCourseGuide(form.getCourseGuide());
	course.setCoursePrereq(form.getPreRequisites());

	// Course Exam Over View

	course.setLearningObjectives(form.getLearningObjectives());
	course.setQuizInfo(form.getQuizInformation());
	course.setFinalExamInfo(form.getFinalExamInformation());
	course.setEndofCourseInstructions(form.getEndOfCourseInstructions());

	// Additional Details
	if (errors.hasErrors()) {
	    Map model = new HashMap();
	    /*
	     * model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
	     * return new ModelAndView(courseAdditionalDetailsTemplate,
	     * "context", model);
	     */
	    model.put("businessUnits", BUSINESS_UNITS);
	    model.put("languages", LANGUAGES);
	    if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
		model.put("gradingMethods", GRADING_METHODS);
	    } else if (form.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE)) {
		model.put("instructorTypes", INSTRUCTOR_TYPES);
	    }

	    //return new ModelAndView(summaryTemplate, "context", model);
	    if(form.getTabName()!=null && form.getTabName().equalsIgnoreCase("summeryTab"))
	    	return new ModelAndView(summaryTemplate, "context", model); // by zulfiqar
	    else
	    	return new ModelAndView(courseAdditionalDetailsTemplate, "context", model); // by zulfiqar
	    
	}
	if (!errors.hasErrors()) {
	    log.debug("inside saveAllInfo method");

	   
	    try{
	    	 course.setDeliveryMethodId(Long.parseLong(form.getDeliveryMethodId()));
		}catch(Exception sube){
			course.setDeliveryMethodId(1l);
		}
		
	    if (form.getMsrp() != null && !form.getMsrp().equals("")) {
		course.setMsrp(Double.parseDouble(form.getMsrp()));
	    } else
		course.setMsrp(0.0);
	    course.setCode(form.getCourseCode());
	    if (form.getApprovedCourseHours() != null && !form.getApprovedCourseHours().equals("")) {
		course.setApprovedcoursehours(Double.parseDouble(form.getApprovedCourseHours()));
	    } else
		course.setApprovedcoursehours(0.0);
	    course.setApprovalNumber(form.getApprovalNumber());
	    course.setCurrency(form.getCurrency());
	    if (form.getProductPrice() != null && !form.getProductPrice().equals("")) {
		course.setProductprice(Double.parseDouble(form.getProductPrice()));
	    } else
		course.setProductprice(0.0);
	    if (form.getWholeSalePrice() != null && !form.getWholeSalePrice().equals("")) {
		course.setWholeSalePrice(Double.parseDouble(form.getWholeSalePrice()));
	    } else
		course.setWholeSalePrice(0.0);
	    course.setRoyaltyPartner(form.getRoyaltyPartner());
	    course.setRoyaltyType(form.getRoyaltyType());
	    course.setStateRegistartionRequired(form.getRegulatoryRequirement());
	    if (form.getTermsOfService() != null && !form.getTermsOfService().equals("")) {
		course.setTos(Integer.parseInt(form.getTermsOfService()));
	    } else
		course.setTos(0);

	    course.setLearningObjectives(form.getLearningObjectives());

	} else {
	    return new ModelAndView(courseAdditionalDetailsTemplate);
	}

	// Save Summary 
	
	//LMS-15747
	 if (form.getDurationHours() != null && !form.getDurationHours().equals("")) {
		 course.setCeus(Double.parseDouble(form.getDurationHours()));
     } else
    	course.setCeus(0.0);
	 
	course.setCourseTitle(form.getCourseName());
	course.setCourseId(form.getCourseID());
	course.setKeywords(form.getKeywords());
	course.setCredithour(form.getCreditHours());
	course.setVersion(form.getVersion());// should i save it or not. as its
					     // already present in
					     // course.java
	course.setBussinesskey(course.getCourseId());
	course.setBusinessUnitName(form.getBusinessUnit());
	course.setBusinessUnitId(this.getBusinessUnitId(course.getBusinessUnitName()));
	course.setCourseStatus(Course.PUBLISHED);

	ContentOwner contentOwner = this.getContentOwner();
	course.setContentOwner(contentOwner);

	if (form.getCourseType().equals(WebLinkCourse.COURSE_TYPE)) {
	    ((WebLinkCourse) course).setLink(form.getLink());
	}

	if (form.getCourseType().equals(HomeworkAssignmentCourse.COURSE_TYPE)) {
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    if (!StringUtils.isBlank(form.getAssignmentDueDate())) {
		((HomeworkAssignmentCourse) course).setAssignmentDueDate(formatter.parse(form.getAssignmentDueDate()));
	    }
	    if (!StringUtils.isBlank(form.getMasterScore())) {
		((HomeworkAssignmentCourse) course).setMasteryScore(Double.parseDouble(form.getMasterScore()));
	    }
	    ((HomeworkAssignmentCourse) course).setGradingMethod(form.getGradingMethod());
/*
	    if (form.getFile() != null && !form.getFile().isEmpty()) {
		log.debug("form.getFile() != null || !form.getFile().isEmpty()");
		Document doc = uploadHomeWorkAssignment(form);
		accreditationService.saveDocument(doc);

		((HomeworkAssignmentCourse) course).setHwAssignmentInstruction(doc);
	    }
*/
	    if(request instanceof MultipartHttpServletRequest)
	    {
	    
	    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		
	    List<Document> lstdoc = new ArrayList<Document>();
		lstdoc = uploadHomeWorkAssignment(form,multiRequest);
		log.debug("Multiple Upload Document: " + lstdoc.size());
	    for(Document doc : lstdoc)
		    {
		    	accreditationService.saveDocument(doc);
		    	HomeWorkAssignmentAsset homeWorkAssignmentAsset = new HomeWorkAssignmentAsset();
		    	homeWorkAssignmentAsset.setAsset(doc);
		    	homeWorkAssignmentAsset.setHomeWorkAssignmentCourse(course);
		    	lmsProductPurchaseService.saveHomeWorkAssignmentAsset(homeWorkAssignmentAsset);
		    }
	    }
	}

	if (form.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE)) {
	    ((InstructorConnectCourse) course).setInstructorType(form.getInstructorType());
	    ((InstructorConnectCourse) course).setMeetingId(form.getMeetingId());
	    ((InstructorConnectCourse) course).setMeetingPasscode(form.getMeetingPasscode());
	    ((InstructorConnectCourse) course).setEmailAddress(form.getEmailAddress());
	}

	log.debug("language:::: >>>> " + form.getLanguage());
	Language language = new Language();
	try {
	    language.setId(1l);
	    language.setLanguage("en");
	    language.setCountry("US");
	    language.setVariant("En-US");
	} catch (Exception pe) {
	    pe.printStackTrace();
	}
	course.setLanguage(language);// its hard coded... need to have a service
				     // class for accessing languages,
	course.setDescription(form.getDescription());
	log.debug("====================== Form CourseType : " + form.getCourseType());
	log.debug("====================== DB CourseType : " + course.getCourseType());
	// [4/22/2010] VCS-264 :: Error handling in case of failure
	Map<Object, Object> context = new HashMap<Object, Object>();
	context.put("errorMessage", this.saveCourse(course, errors));

	courseAndCourseGroupService.saveCourse(course);
	return new ModelAndView(closeTemplate, "context", context);
    }

    private List<Document> uploadHomeWorkAssignment(CourseDetails form, MultipartHttpServletRequest multiRequest) {
   	 
    	
   	 Map files = multiRequest.getFileMap();
   	 List<Document> lstDoc = new ArrayList<Document>();
   	 
   	 if (files == null) {
            logger.info("no file was sent");
            return lstDoc;
      }
      else{
   	   
   	   Iterator fileIterator=files.values().iterator();
          MultipartFile file;
          while(fileIterator.hasNext()) {
       	   file =(MultipartFile)fileIterator.next();
       	   if (file != null && !StringUtils.isBlank(file.getOriginalFilename()))
      	   {
       		   log.debug(file.getOriginalFilename());
       		   Document doc = FileUploadUtils.uploadFile(file, VU360Properties.getVU360Property("hwassignment.saveLocation"), getContentOwner());
       		   lstDoc.add(doc);
       	   }
          }
     }
      
 return lstDoc;
    }
    
 /*   
    private Document uploadHomeWorkAssignment(CourseDetails form) {

	if (form.getFile() != null && !StringUtils.isBlank(form.getFile().getOriginalFilename())) {
	    return FileUploadUtils.uploadFile(form.getFile(), VU360Properties.getVU360Property("hwassignment.saveLocation"), getContentOwner());
	}

	return null;
    }
*/
    private int getBusinessUnitId (String businessUnitName)
    {
    	int i = 0;
    	for(; i < BUSINESS_UNITS.length; i++)
    		if(BUSINESS_UNITS[i].equalsIgnoreCase(businessUnitName))
    			break;
    	return i + 1;
    }

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

}