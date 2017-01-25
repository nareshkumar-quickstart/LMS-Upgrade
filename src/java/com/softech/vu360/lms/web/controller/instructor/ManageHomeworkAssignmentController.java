package com.softech.vu360.lms.web.controller.instructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmissionAsset;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.helper.SortPagingAndSearch;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentLearner;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentSubmissionForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class ManageHomeworkAssignmentController extends VU360BaseMultiActionController {

    HttpSession session = null;

    private static final Logger log = Logger.getLogger(ManageHomeworkAssignmentController.class.getName());

    private String homeworkAssignmentLearnersList = null;

    private String editScore = null;
    private LMSProductPurchaseService lmsProductPurchaseService;
    private LearnerHomeworkAssignmentSubmissionService homeworkAssignmentService = null;
    private EnrollmentService enrollmentService = null;
    private EntitlementService entitlementService=null;
    private StatisticsService statisticsService = null;

    
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
    
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
    
    public EnrollmentService getEnrollmentService() {
	return enrollmentService;
    }

    public void setEnrollmentService(EnrollmentService enrollmentService) {
	this.enrollmentService = enrollmentService;
    }

    public LearnerHomeworkAssignmentSubmissionService getHomeworkAssignmentService() {
	return homeworkAssignmentService;
    }

    public void setHomeworkAssignmentService(LearnerHomeworkAssignmentSubmissionService homeworkAssignmentService) {
	this.homeworkAssignmentService = homeworkAssignmentService;
    }

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}

    
    public ManageHomeworkAssignmentController() {
	super();
    }

    protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
	log.debug("onBind");

    }

    protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
	log.debug("validate");
    }

    public ManageHomeworkAssignmentController(Object delegate) {
	super(delegate);
    }

    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	log.debug(" IN handleNoSuch Method ");

	return listLearners(request, response, null, null);
    }

    /**
     * Pulls the data to be displayed to help instructor edit/give score
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception
     */
    public ModelAndView editScore(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	setEditScoreForm(request, command);
	Map<Object, Object> context = new HashMap<Object, Object>();
	context.put("deleteError", false);
    	//submissionForm.setInstructionsFileName(assignment.getHwAssignmentInstruction().getFileName());

	return new ModelAndView(editScore,"context",context);
    }

    private void setEditScoreForm(HttpServletRequest request, Object command) {
    	String enrollmentId = request.getParameter("id");
    	Map<Object, Object> context = new HashMap<Object, Object>();
    	HomeworkAssignmentSubmissionForm submissionForm = (HomeworkAssignmentSubmissionForm) command;
    	if (!StringUtils.isBlank(enrollmentId)) {
    	    submissionForm.setLearnerEnrollment(enrollmentId);
    	}
    	LearnerEnrollment enrollment = enrollmentService.loadForUpdateLearnerEnrollment(new Long(submissionForm.getLearnerEnrollment()));
    	submissionForm.setAssignmentName(enrollment.getCourse().getCourseTitle());
    	submissionForm.setCourseName(enrollment.getCourse().getCourseTitle());
    	submissionForm.setFirstName(enrollment.getLearner().getVu360User().getFirstName());
    	submissionForm.setLastName(enrollment.getLearner().getVu360User().getLastName());
    	HomeworkAssignmentCourse assignment = (HomeworkAssignmentCourse) enrollment.getCourse();
    	LearnerHomeworkAssignmentSubmission s = null;
    	try {
    	    s = enrollment.getHwSubmission().get(0);
    	} catch (Exception e) {
    	    s = new LearnerHomeworkAssignmentSubmission();
    	}
    	try {
    	    submissionForm.setId(s.getId().toString());
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    submissionForm.setId(null);
    	}

    	if (LearnerHomeworkAssignmentSubmission.SCORE_METHOD_SIMPLE.equals(assignment.getGradingMethod())) {
    	    submissionForm.setScore(s.getPercentScore());
    	} else {
    	    if (LearnerHomeworkAssignmentSubmission.SCORE_INCOMPLETED.equals(s.getPercentScore())) {
    		submissionForm.setScore(s.getPercentScore());
    		submissionForm.setPercentScore("");
    	    } else {
    		submissionForm.setPercentScore(s.getPercentScore());
    		submissionForm.setScore("scorePercent");
    	    }

    	}

    	submissionForm.setScoringMethod(assignment.getGradingMethod());
    	submissionForm.setStatus(s.getStatus());
    	
    	context.put("deleteError", false);

	// Brander brander =
	// VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
    }

    /**
     * controlls the update of submitted homework assignment score/grade
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception
     */
    public ModelAndView updateScore(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

	Map<Object, Object> context = new HashMap<Object, Object>();
	HomeworkAssignmentCourse hwAssignmentCourse = null;
	HomeworkAssignmentSubmissionForm submissionForm = (HomeworkAssignmentSubmissionForm) command;
	LearnerHomeworkAssignmentSubmission submission = new LearnerHomeworkAssignmentSubmission();

	VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

	// if submission is not being made by the learner
	try {
	    submission.setId(new Long(submissionForm.getId()));
	} catch (Exception e) {
	    submission.setId(null);
	}

	submission.setGrader(loggedInUser.getInstructor());

	if(submissionForm.getScoringMethod().equals(LearnerHomeworkAssignmentSubmission.SCORE_METHOD_SCORE))
	{
		
		LearnerEnrollment learnerEnrollment = new LearnerEnrollment(new Long(submissionForm.getLearnerEnrollment()));
	    LearnerCourseStatistics learnerCourseStatistics = statisticsService.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment);
	    Course course = learnerCourseStatistics.getLearnerEnrollment().getCourse();
	    if(course instanceof HomeworkAssignmentCourse)
	    {
	     hwAssignmentCourse =  ((HomeworkAssignmentCourse)course);
	    }
	    if(!submissionForm.getScore().equals("Incomplete"))
	    {
			 submission.setPercentScore(submissionForm.getScore());
		    
		    
		    double marksattained = Double.parseDouble(submissionForm.getPercentScore());
		    int masteryscore = Double.compare(hwAssignmentCourse.getMasteryScore(), marksattained);
		    
		    if(masteryscore > 0)
		    {
			    //learnerCourseStatistics.setStatus("incomplete");
			    learnerCourseStatistics.setCompleted(Boolean.FALSE);
			    
		    }
		    if(masteryscore <= 0  )
		    {
			    learnerCourseStatistics.setStatus("completed");
			    learnerCourseStatistics.setCompleted(Boolean.TRUE);
			    learnerCourseStatistics.setCompletionDate(new Date());
		    }
		    
	    
	    }
	    
	    if(submissionForm.getScore().equals("Incomplete"))
	    {
		    learnerCourseStatistics.setStatus("incomplete");
		    learnerCourseStatistics.setCompleted(Boolean.FALSE);
		    
	    }
	    
	    LearnerCourseStatistics newStats = statisticsService.updateLearnerCourseStatistics(learnerCourseStatistics.getId().longValue(), learnerCourseStatistics);
	    
	}
	
	if (submissionForm.getScoringMethod().equals(LearnerHomeworkAssignmentSubmission.SCORE_METHOD_SIMPLE)) {
	    submission.setPercentScore(submissionForm.getScore());
	    //LearnerEnrollment learnerEnrollment = entitlementService.getLearnerEnrollmentById(userstatus);
	    LearnerEnrollment learnerEnrollment = new LearnerEnrollment(new Long(submissionForm.getLearnerEnrollment()));
	    LearnerCourseStatistics learnerCourseStatistics = statisticsService.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment);
	    if(submissionForm.getScore().equals("Pass"))
	    {
		    learnerCourseStatistics.setStatus("completed");
		    learnerCourseStatistics.setCompleted(Boolean.TRUE);
		    learnerCourseStatistics.setCompletionDate(new Date());
	    }
	    if(submissionForm.getScore().equals("Incomplete"))
	    {
		    learnerCourseStatistics.setStatus("incomplete");
		    learnerCourseStatistics.setCompleted(Boolean.FALSE);
		    
	    }
	    if(submissionForm.getScore().equals("Fail"))
	    {
		    //learnerCourseStatistics.setStatus("incomplete");
		    learnerCourseStatistics.setCompleted(Boolean.FALSE);
		    
	    }
	    
	    LearnerCourseStatistics newStats = statisticsService.updateLearnerCourseStatistics(learnerCourseStatistics.getId().longValue(), learnerCourseStatistics);
	} else {
	    if (submissionForm.getScore().equals(LearnerHomeworkAssignmentSubmission.SCORE_INCOMPLETED)) {
		submission.setPercentScore(submissionForm.getScore());
	    } else {
		submission.setPercentScore(submissionForm.getPercentScore());
	    }
	}

	// if learnerenrollment id null, make sure record does't get inserted
	// into
	// submission table because with learnerenrollment, submission can't
	// exist.
	try {
	    submission.setLearnerEnrollment(new LearnerEnrollment(new Long(submissionForm.getLearnerEnrollment())));
	} catch (Exception e) {
	    submission.setLearnerEnrollment(null);
	}

	submission = homeworkAssignmentService.updateScore(submission);

	
	
	setHWAssignmentSubmissionList(context, request);
	return new ModelAndView(homeworkAssignmentLearnersList, "context", context);

    }

    /**
     * downloads file
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception
     */
    public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

	String fileName = request.getParameter("fileName");
	String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
	String id = request.getParameter("submissionIds");
	String[] instructorfiles = null;
	String filePath = null;
	

	//String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
	int i = 0;
	//learners[0] = System.currentTimeMillis() + "_homeworkAssignmet.zip";
	LearnerEnrollment enrollment = null;
	enrollment = enrollmentService.loadForUpdateLearnerEnrollment(new Long(id));
	
	if(enrollment != null)
	{
		Course enrolledCourse = enrollment.getCourse();
		List<HomeWorkAssignmentAsset> lstHomeWorkAssignmentAsset = lmsProductPurchaseService.getHomeWorkAssignmentAsset(enrolledCourse.getId());
		if(lstHomeWorkAssignmentAsset != null)
		{
			instructorfiles = new String[lstHomeWorkAssignmentAsset.size() + 1];
			//instructorfiles[i++] = fileLocation + File.separator + enrolledCourse.getCourseTitle().replace(" ", "_")  + ".zip";
			instructorfiles[i++] = fileLocation + File.separator + enrollment.getCourse().getCourseTitle().replace(" ", "_") + ".zip";
			filePath = instructorfiles[0];
			
            //for (HomeWorkAssignmentAsset asset : lstHomeWorkAssignmentAsset) {
            	//instructorfiles[i++] = fileLocation + File.separator + asset.get;
			//}

            for(HomeWorkAssignmentAsset homeworkassignmentasset: lstHomeWorkAssignmentAsset)
            	{
            		if(homeworkassignmentasset.getAsset() instanceof Document)
            			{
            			instructorfiles[i++] = fileLocation + File.separator + ((Document)homeworkassignmentasset.getAsset()).getFileName();
            			}
            	}
            
			FileUploadUtils.downloadZipFile(instructorfiles);

		}
	}
	
	//String filePath = fileLocation + File.separator + fileName;
	//log.debug("filePath *" + filePath + "*");
	FileUploadUtils.downloadFile(filePath, response);

	return null;

    }

    /*
     * This method will be invoked by "Student's Work' and "Download Selected"
     * options of Homework Assignment Tool module
     */
    public ModelAndView downloadStudentSubmittedAssignment(HttpServletRequest request, HttpServletResponse response, Object command,
	    BindException errors) throws Exception {

	String pageName = request.getParameter("pageName");
	String ids = request.getParameter("submissionIds");
	//String ids = "251185";
	// return to list page without processiong for download
	if (StringUtils.isEmpty(ids)) {

	    if (!StringUtils.isBlank(pageName) && pageName.equals("0")) {
		Map<Object, Object> context = new HashMap<Object, Object>();
		setHWAssignmentSubmissionList(context, request);
		return new ModelAndView(homeworkAssignmentLearnersList, "context", context);
	    } else {
		setEditScoreForm(request, command);
		return new ModelAndView(editScore);
	    }

	}

	

	String[] submissions = ids.split(",");
	String[] singleLearnerSubmissions = null;
	String[] learners = new String[submissions.length + 1];
	String fileToDownload = null;
	LearnerHomeworkAssignmentSubmission hwSubmission;
	List<LearnerHomeworkAssignmentSubmissionAsset> submittedFiles = null;
	String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
	int i = 0;
	learners[0] = System.currentTimeMillis() + "_homeworkAssignmet.zip";
	
	try {
	    // Loop through the total learners selected whose assignments will
	    // be downloaded
	    for (int count = 0; count < submissions.length; count++) {
		log.debug(">>>>>>>>>>>>>>>>>> ID <<<<<<<<<<<<<<<<<<<<" + submissions[count]);
		//enrollment = enrollmentService.loadForUpdateLearnerEnrollment(new Long(submissions[count]));
		LearnerEnrollment enrollment = homeworkAssignmentService.getEnrollmentNoCache(new Long(submissions[count]));
		
		//List<Map<Object, Object>> obj = homeworkAssignmentService.gethomeWorkAssignmentAssetByEnrollId(enrollment.getId());
		// hwSubmission =
		// homeworkAssignmentService.getHomeworkAssignmentSubmissionById(submissions[count]);
		// no assignment submitted for this enrollment and hence
		// will not be processed to download the files

		try {

		    
				log.debug("enrollment.getHwSubmission().size() : " + enrollment.getHwSubmission().size());
			    hwSubmission = enrollment.getHwSubmission().get(enrollment.getHwSubmission().size()-1);
			    hwSubmission.setStatus("Viewed");
			    hwSubmission = homeworkAssignmentService.updateStatus(hwSubmission);
			    log.debug("hwSubmission : " + hwSubmission);
			    submittedFiles = hwSubmission.getSubmittedWork();
			    if (submittedFiles.size() < 1)
				throw new Exception("File not found");
			 
		} catch (Exception e) {
		    e.printStackTrace();
		    continue;
		}
		
		// Increment to 1 because 0 index of this array will hold
		// learner name
		singleLearnerSubmissions = new String[hwSubmission.getSubmittedWork().size() + 1];

		// it will store zip filename with complete path which will
		// constitute assignment submissions by a learner
		singleLearnerSubmissions[i++] = fileLocation + File.separator
			+ hwSubmission.getLearnerEnrollment().getLearner().getVu360User().getName().replace(" ", "_") + ".zip";
		fileToDownload = singleLearnerSubmissions[0];

		for (LearnerHomeworkAssignmentSubmissionAsset asset : submittedFiles) {
		    singleLearnerSubmissions[i++] = fileLocation + File.separator + asset.getDocument().getFileName();
		}

		FileUploadUtils.downloadZipFile(singleLearnerSubmissions);

		if (submissions.length > 1) {
		    // learners[count+1] is because index 0 already occupies zip
		    // file name
		    learners[count + 1] = singleLearnerSubmissions[0];
		}
		i = 0;
	    }

	    /*
	     * this section will be executed only if more then one learners are
	     * selected for downloading their submitted assignments. This will
	     * create archive of archives. For eg,
	     * 
	     * 2302348204889_homeworkAssignment.zip Joe Doe.zip Micheal
	     * Clark.zip
	     */
	    if (submissions.length > 2) {
		FileUploadUtils.downloadZipFile(learners);
		fileToDownload = learners[0];
	    }

	    log.debug("File to download *" + fileToDownload + "*");
	    FileUploadUtils.downloadFile(fileToDownload, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (!StringUtils.isBlank(pageName) && pageName.equals("0")) {
	    Map<Object, Object> context = new HashMap<Object, Object>();
	    setHWAssignmentSubmissionList(context, request);
	    return new ModelAndView(homeworkAssignmentLearnersList, "context", context);
	} else {
	    setEditScoreForm(request, command);
	    return new ModelAndView(editScore);
	}

    }

    /**
     * Display list of learners enrolled against the Homework Assignment course.
     * This interface helps instructor to give score against the submissted
     * assignment
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception
     */
    public ModelAndView listLearners(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	try {
	    log.debug("************ In listLearners ****************");

	    Map<Object, Object> context = new HashMap<Object, Object>();
	    setHWAssignmentSubmissionList(context, request);
	    return new ModelAndView(homeworkAssignmentLearnersList, "context", context);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ModelAndView(homeworkAssignmentLearnersList);
    }

    /**
     * Pull the search parameters from HttpServletRequest and set them into sps
     * object.
     * 
     * @param request
     * @param sps
     */
    private void setSearchParamValues(HttpServletRequest request, SortPagingAndSearch sps) {
	String firstName = "";
	String lastName = "";
	String courseName = "";
	String status = "";

	HttpSession session = request.getSession();

	if (sps.isDoSearchData()) {
	    firstName = (request.getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME) == null) ? "" : request
		    .getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME);
	    lastName = (request.getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME) == null) ? "" : request
		    .getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME);
	    courseName = (request.getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME) == null) ? "" : request
		    .getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME);
	    status = (request.getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS) == null) ? "All" : request
		    .getParameter(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS);

	    session.setAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME, firstName);
	    session.setAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME, lastName);
	    session.setAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME, courseName);
	    session.setAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS, status);

	} else {
	    firstName = (session.getAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME) == null) ? "" : session.getAttribute(
		    HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME).toString();
	    lastName = (session.getAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME) == null) ? "" : session.getAttribute(
		    HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME).toString();
	    courseName = (session.getAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME) == null) ? "" : session.getAttribute(
		    HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME).toString();
	    status = (session.getAttribute(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS) == null) ? "All" : session.getAttribute(
		    HomeworkAssignmentLearner.SEARCH_FIELD_STATUS).toString();
	}

	sps.setValue(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME, firstName);
	sps.setValue(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME, lastName);
	sps.setValue(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME, courseName);
	sps.setValue(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS, status);

    }// end of setSearchParamValues method

    /**
     * As list will be invoked more then once therefore putting the entire list
     * of homework assignment submission logic in a private helper method which
     * will be called by multiple methods from this same controller.
     * 
     * @param context
     * @param request
     * @throws Exception
     */
    private void setHWAssignmentSubmissionList(Map<Object, Object> context, HttpServletRequest request) throws Exception {
    	com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());

	// bind paging and sorting field values from view part to a bean
	SortPagingAndSearch sps = new SortPagingAndSearch();

	/*
	 * set page size. Pull the page size value from properties file against
	 * the key lms.resultSet.pageSize and if invalid page size is set, then
	 * set default page size 10
	 */
	try {
	    sps.setPageSize(Integer.parseInt(brander.getBrandElement("lms.resultSet.pageSize")));
	} catch (NumberFormatException nfe) {
	    log.warn("Invalid page size defined." + brander.getBrandElement("lms.resultSet.pageSize"));
	    sps.setPageSize(10);
	}
	bind(request, sps);

	// Bind search parameters
	setSearchParamValues(request, sps);
	// get all the learners enrolled for homework assignment course;
	// either submitted their work or not.
	List<HomeworkAssignmentLearner> learners = homeworkAssignmentService.getHomeworkAssignmentLearnersByInstructor(loggedInUser.getInstructor()
		.getId(), sps);

	context.put("learnerList", learners);
	context.put("status", fillStatusList());
	context.put("pageAttributes", sps);
	log.debug(sps.toString());
    }

    /**
     * 
     * @param statusList
     */
    private List<String> fillStatusList() {

	List<String> statusList = new ArrayList<String>();
	statusList.add("ALL");
	statusList.add(HomeworkAssignmentLearner.SEARCH_STATUS_SUBMITTED);
	statusList.add(HomeworkAssignmentLearner.SEARCH_STATUS_PENDING);
	statusList.add(HomeworkAssignmentLearner.SEARCH_STATUS_VIEWED);

	return statusList;

    }

    public String getHomeworkAssignmentLearnersList() {
	return homeworkAssignmentLearnersList;
    }

    public void setHomeworkAssignmentLearnersList(String homeworkAssignmentLearnersList) {
	this.homeworkAssignmentLearnersList = homeworkAssignmentLearnersList;
    }

    public String getEditScore() {
	return editScore;
    }

    public void setEditScore(String editScore) {
	this.editScore = editScore;
    }
}