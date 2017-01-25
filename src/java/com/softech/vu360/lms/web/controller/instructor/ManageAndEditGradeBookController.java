package com.softech.vu360.lms.web.controller.instructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

import com.softech.vu360.lms.model.AssignmentCourseActivity;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.FinalScoreCourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.LearnerAssignmentActivity;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerFinalCourseActivity;
import com.softech.vu360.lms.model.LearnerLectureCourseActivity;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.model.LectureCourseActivity;
import com.softech.vu360.lms.model.SelfStudyCourseActivity;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.GradeBookForm;
import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;
import com.softech.vu360.lms.web.controller.model.instructor.ManageEnrolledLearner;
import com.softech.vu360.lms.web.controller.model.instructor.ManageGrade;
import com.softech.vu360.util.EditGradebookSort;
import com.softech.vu360.util.GradeBookSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.PdfCreator;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Saptarshi
 *
 */
public class ManageAndEditGradeBookController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditGradeBookController.class.getName());

	private ResourceService resourceService;
	private SynchronousClassService synchronousClassService;
	private StatisticsService statisticsService;
    private LearnersToBeMailedService learnersToBeMailedService;

	private String manageGradeBookTemplate = null;
	private String editGradebookTemplate = null;
	private String editLectureGradeTemplate = null;
	private String editSelfStudyCourseActivityTemplate = null;
	private String editAssignmentActivityTemplate = null;
	private String editFinalCourseActivityTemplate = null;
	private String redirectTemplate = null;
	
	private HttpSession session = null;

	public ManageAndEditGradeBookController() {
		super();
	}

	public ManageAndEditGradeBookController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		return searchGradeBook(request,response,null,null);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof GradeBookForm ){
			GradeBookForm form = (GradeBookForm)command;
			if( methodName.equals("editGradebookCourseActivityView")){
				if (form.getGradebookId() != 0) {
					Gradebook gradeBook = resourceService.getGradeBookById(form.getGradebookId());
					form.setSynClassName(gradeBook.getSynchronousClass().getSectionName());
					form.setActivityList(null);
				}
				form.setCourseActivities(null);
			} else if(methodName.equals("editGrade")) {
				form.setManageGradeList(null);
			}
		}
	}

	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {

	}

	public ModelAndView searchGradeBook( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {
		try {
			request.getSession(true).setAttribute("feature", "LMS-INS-0005");
			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			//Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String className = "";
			String startDate = "";
			String endDate = "";
			if( request.getParameter("showAll") !=null && request.getParameter("showAll").equalsIgnoreCase("true")  )
			{
				className=(session.getAttribute("className") == null) ? "" : session.getAttribute("className").toString() ;
				startDate=(session.getAttribute("startDate") == null) ? "" : session.getAttribute("startDate").toString() ;
				endDate=(session.getAttribute("endDate") == null) ? "" : session.getAttribute("endDate").toString() ;
			} else {
				className = (request.getParameter("className") == null) ? "" : request.getParameter("className");
				startDate = (request.getParameter("startDate") == null) ? "" : request.getParameter("startDate");
				endDate = (request.getParameter("endDate") == null) ? "" : request.getParameter("endDate");
				session.setAttribute("className", className);
				session.setAttribute("startDate", startDate);
				session.setAttribute("endDate", endDate);
			}
			className = HtmlEncoder.escapeHtmlFull(className).toString();

			context.put("activityName", className);
			List<Gradebook> gradeBookList = resourceService.findGradeBooks(loggedInUser.getInstructor().getId(), className, startDate, endDate);
			//List<Gradebook> gradeBookList = new ArrayList<Gradebook>();
			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
			if( sortDirection == null && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null &&  session.getAttribute("pageCurrIndex") != null){
				pageIndex = session.getAttribute("pageCurrIndex").toString();
			}
			if( sortColumnIndex !=null && sortDirection !=null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("className");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("className");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("startDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 1);
					} else {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("startDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 1);
					}
				}else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("endDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 2);
					} else {
						GradeBookSort sort = new GradeBookSort();
						sort.setSortBy("endDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(gradeBookList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 2);
					}
				} 
			}	

			context.put("gradeBookList", gradeBookList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			return new ModelAndView(manageGradeBookTemplate,"context",context);

		}catch( Exception e ){
			e.printStackTrace();
		}
		
		return new ModelAndView(manageGradeBookTemplate);
	}

	
	public ModelAndView createPdf( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		GradeBookForm form = (GradeBookForm)command;
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("courseActivities", form.getCourseActivities());
		context.put("activities", form.getActivityList());
		context.put("synClassName", form.getSynClassName());
		PdfCreator creator  = new PdfCreator();
		com.lowagie.text.pdf.PdfWriter w = null;
		creator.buildPdfDocument(context, null, w, request, response);
		//return this.searchGradeBook(request, response, command, errors);
		// code taken from LMS - 4.7.2 branch
		String documentPath = VU360Properties.getVU360Property("document.gradebook.pdfLocationRelative");
		return new ModelAndView(redirectTemplate,"documentPath",documentPath);

	}
	
	public ModelAndView editGradebookCourseActivityView( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {

		GradeBookForm form = (GradeBookForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		try {

			if (form.getSynClsId() != 0 && form.getGradebookId() !=0) {

				List<CourseActivity> courseActivities = resourceService.findCourseActivitiesByGradeBook(form.getGradebookId());
				Collections.sort(courseActivities);
				if (courseActivities != null && courseActivities.size()>0)
				{
					log.debug("courseActivities.get(0).getActivityName() " + courseActivities.get(0).getActivityName())	;
					form.setCourseActivities(courseActivities);
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

					List<ManageActivity> manageLearnerActivities = resourceService.getActivityListByGradeBookByInstructor(form.getSynClsId(),loggedInUser.getInstructor().getId(), courseActivities);
					
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					String sortColumnIndex = request.getParameter("sortColumnIndex");
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = request.getParameter("sortDirection");
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = request.getParameter("pageCurrIndex");

					if(StringUtils.isEmpty(pageIndex))
						pageIndex = (String)session.getAttribute("pageCurrIndex");
					
					if(StringUtils.isEmpty(pageIndex))
						pageIndex="0";
					
					if( sortColumnIndex != null && sortDirection != null ) {

						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								EditGradebookSort sort = new EditGradebookSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(manageLearnerActivities,sort);
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								EditGradebookSort sort = new EditGradebookSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(manageLearnerActivities,sort);
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								EditGradebookSort sort = new EditGradebookSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(manageLearnerActivities,sort);
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								EditGradebookSort sort = new EditGradebookSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(manageLearnerActivities,sort);
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
						}
					}	
					/*Collections.sort(manageLearnerActivities);*/
					form.setActivityList(manageLearnerActivities);
					context.put("sortDirection", sortDirection);
					context.put("sortColumnIndex", sortColumnIndex);
					context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
					session.setAttribute("pageCurrIndex", pageIndex);
					pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}

			}
		} catch (Exception e) {
			log.debug(e);
		}
		return new ModelAndView(editGradebookTemplate,"context",context);
	}

	public ModelAndView editGrade( HttpServletRequest request, HttpServletResponse response,Object command, BindException errors ) throws Exception {
		GradeBookForm form = (GradeBookForm)command;
		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long instructorId = 0l;
			instructorId = loggedInUser.getInstructor().getId();
			HashSet<ManageEnrolledLearner> studentList = resourceService.findSynchronousClassesLearnersByInstructor("", "", "", instructorId, form.getSynClsId());
			CourseActivity courseActivity = resourceService.getCourseActivityById(form.getCaId());
			form.setActivityName(courseActivity.getActivityName());
			List<LearnerCourseActivity> learnerCourseActivities = resourceService.getLearnerCourseActivitiesByCourseActivity(courseActivity);
			List<ManageGrade> manageGradeList = new ArrayList<ManageGrade>();
			if(courseActivity != null) {
				if (courseActivity instanceof LectureCourseActivity) {
					form.setActType(GradeBookForm.LECTURE_COURSE);
					if (learnerCourseActivities.size()==0 ) {
						Iterator<ManageEnrolledLearner> itr = studentList.iterator();
						while(itr.hasNext()) {
							boolean flag = false;
							ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
							for (int i =0;i<manageGradeList.size();i++){
								if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
									flag = true;
								}
							}
							if(!flag){
								ManageGrade manageGrade = new ManageGrade();
								manageGrade.setUser(manageEnrolledLearner.getUser());
								LearnerLectureCourseActivity learnerLectureCourseActivity = new LearnerLectureCourseActivity();
								learnerLectureCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
								manageGrade.setLearnerCourseActivity(learnerLectureCourseActivity);
								manageGradeList.add(manageGrade);
							}
						}
						form.setManageGradeList(manageGradeList);
					}
				} else if (courseActivity instanceof AssignmentCourseActivity) {
					form.setActType(GradeBookForm.ASSIGNMENT_COURSE);
					if (learnerCourseActivities.size()==0 ) {
						Iterator<ManageEnrolledLearner> itr = studentList.iterator();
						while(itr.hasNext()) {
							boolean flag = false;
							ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
							for (int i =0;i<manageGradeList.size();i++){
								if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
									flag = true;
								}
							}
							if(!flag){
								ManageGrade manageGrade = new ManageGrade();
								manageGrade.setUser(manageEnrolledLearner.getUser());
								LearnerAssignmentActivity learnerAssignmentActivity = new LearnerAssignmentActivity();
								learnerAssignmentActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
								manageGrade.setLearnerCourseActivity(learnerAssignmentActivity);
								manageGradeList.add(manageGrade);
							}
						}
						form.setManageGradeList(manageGradeList);
					}
				} else if (courseActivity instanceof FinalScoreCourseActivity) {
					form.setActType(GradeBookForm.FINAL_SCORE_COURSE);
					if (learnerCourseActivities.size()==0 ) {
						Iterator<ManageEnrolledLearner> itr = studentList.iterator();
						while(itr.hasNext()) {
							boolean flag = false;
							ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
							for (int i =0;i<manageGradeList.size();i++){
								if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
									flag = true;
								}
							}
							if(!flag){
								ManageGrade manageGrade = new ManageGrade();
								manageGrade.setUser(manageEnrolledLearner.getUser());
								LearnerFinalCourseActivity learnerFinalCourseActivity = new LearnerFinalCourseActivity();
								learnerFinalCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
								manageGrade.setLearnerCourseActivity(learnerFinalCourseActivity);
								manageGradeList.add(manageGrade);
							}
						}
						form.setManageGradeList(manageGradeList);
					}
				} else if (courseActivity instanceof SelfStudyCourseActivity) {
					form.setActType(GradeBookForm.SELF_STUDY_COURSE);
					Iterator<ManageEnrolledLearner> itr = studentList.iterator();
					if (learnerCourseActivities.size()==0 ) {
						while(itr.hasNext()) {
							boolean flag = false;
							ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
							for (int i =0;i<manageGradeList.size();i++){
								if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
									flag = true;
								}
							}
							if(!flag){
								ManageGrade manageGrade = new ManageGrade();
								manageGrade.setUser(manageEnrolledLearner.getUser());
								if (manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics() != null) {
									manageGrade.setScore(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics().getHighestPostTestScore()+"");
								}
								LearnerSelfStudyCourseActivity learnerSelfStudyCourseActivity = new LearnerSelfStudyCourseActivity();
								learnerSelfStudyCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
								learnerSelfStudyCourseActivity.setLearnerEnrollment(manageEnrolledLearner.getLearnerEnrollment());
								manageGrade.setLearnerCourseActivity(learnerSelfStudyCourseActivity);
								manageGradeList.add(manageGrade);
							}
						}
						form.setManageGradeList(manageGradeList);
					}
				}
			}

			if (learnerCourseActivities != null && learnerCourseActivities.size() != 0) {

				if (learnerCourseActivities.get(0) instanceof LearnerLectureCourseActivity) {
					for(LearnerCourseActivity learnerCourseActivity : learnerCourseActivities) {
						LearnerLectureCourseActivity lectureCourseActivity = (LearnerLectureCourseActivity)learnerCourseActivity;
						ManageGrade manageGrade = new ManageGrade();
						manageGrade.setUser(lectureCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner().getVu360User());
						manageGrade.setAttended(lectureCourseActivity.isAttended());
						manageGrade.setLearnerCourseActivity(learnerCourseActivity);
						manageGradeList.add(manageGrade);
					}
					Iterator<ManageEnrolledLearner> itr = studentList.iterator();
					while(itr.hasNext()) {
						boolean flag = false;
						ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
						for (int i =0;i<manageGradeList.size();i++){
							if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
								flag = true;
							}
						}
						if(!flag){
							ManageGrade manageGrade = new ManageGrade();
							manageGrade.setUser(manageEnrolledLearner.getUser());
							LearnerLectureCourseActivity learnerLectureCourseActivity = new LearnerLectureCourseActivity();
							learnerLectureCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
							manageGrade.setLearnerCourseActivity(learnerLectureCourseActivity);
							manageGradeList.add(manageGrade);
						}
					}
				} else if (learnerCourseActivities.get(0) instanceof LearnerAssignmentActivity) {
					for(LearnerCourseActivity learnerCourseActivity : learnerCourseActivities) {
						LearnerAssignmentActivity assignmentActivity = (LearnerAssignmentActivity)learnerCourseActivity;
						ManageGrade manageGrade = new ManageGrade();
						manageGrade.setUser(assignmentActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner().getVu360User());
						manageGrade.setAttempted(assignmentActivity.isAssignmentAttempted());
						manageGrade.setCompleted(assignmentActivity.isAssignmentComplete());
						manageGrade.setPercentScore(assignmentActivity.getPercentScore()+"");
						manageGrade.setRawScore(assignmentActivity.getRawScore()+"");
						manageGrade.setLearnerCourseActivity(learnerCourseActivity);
						manageGradeList.add(manageGrade);
					}
					Iterator<ManageEnrolledLearner> itr = studentList.iterator();
					while(itr.hasNext()) {
						boolean flag = false;
						ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
						for (int i =0;i<manageGradeList.size();i++){
							if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
								flag = true;
							}
						}
						if(!flag){
							ManageGrade manageGrade = new ManageGrade();
							manageGrade.setUser(manageEnrolledLearner.getUser());
							LearnerAssignmentActivity learnerAssignmentActivity = new LearnerAssignmentActivity();
							learnerAssignmentActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
							manageGrade.setLearnerCourseActivity(learnerAssignmentActivity);
							manageGradeList.add(manageGrade);
						}
					}

				} else if (learnerCourseActivities.get(0) instanceof LearnerFinalCourseActivity) {
					for(LearnerCourseActivity learnerCourseActivity : learnerCourseActivities) {
						LearnerFinalCourseActivity finalCourseActivity = (LearnerFinalCourseActivity)learnerCourseActivity;
						ManageGrade manageGrade = new ManageGrade();
						manageGrade.setUser(finalCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner().getVu360User());
						manageGrade.setCourseComplete(finalCourseActivity.isCourseComplete());
						SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						if(finalCourseActivity.getCourseCompleteDate()!=null)
							manageGrade.setCourseCompleteDate(format.format(finalCourseActivity.getCourseCompleteDate()));
						manageGrade.setFinalPercentScore(finalCourseActivity.getFinalPercentScore()+"");
						manageGrade.setFinalRawScore(finalCourseActivity.getFinalRawScore()+"");
						manageGrade.setLearnerCourseActivity(learnerCourseActivity);
						manageGradeList.add(manageGrade);
					}
					Iterator<ManageEnrolledLearner> itr = studentList.iterator();
					while(itr.hasNext()) {
						boolean flag = false;
						ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
						for (int i =0;i<manageGradeList.size();i++){
							if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
								flag = true;
							}
						}
						if(!flag){
							ManageGrade manageGrade = new ManageGrade();
							manageGrade.setUser(manageEnrolledLearner.getUser());
							LearnerFinalCourseActivity learnerFinalCourseActivity = new LearnerFinalCourseActivity();
							learnerFinalCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
							manageGrade.setLearnerCourseActivity(learnerFinalCourseActivity);
							manageGradeList.add(manageGrade);
						}
					}
				} else if (learnerCourseActivities.get(0) instanceof LearnerSelfStudyCourseActivity) {
					for(LearnerCourseActivity learnerCourseActivity : learnerCourseActivities) {
						LearnerSelfStudyCourseActivity learnerSelfStudyCourseActivity = (LearnerSelfStudyCourseActivity)learnerCourseActivity;
						ManageGrade manageGrade = new ManageGrade();
						manageGrade.setUser(learnerSelfStudyCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner().getVu360User());
						manageGrade.setScore(learnerSelfStudyCourseActivity.getLearnerCourseStatistics().getHighestPostTestScore()+"");
						manageGrade.setOverride(learnerSelfStudyCourseActivity.getOverrideScore()+"");
						manageGrade.setLearnerCourseActivity(learnerCourseActivity);
						manageGradeList.add(manageGrade);
					}
					Iterator<ManageEnrolledLearner> itr = studentList.iterator();
					while(itr.hasNext()) {
						boolean flag = false;
						ManageEnrolledLearner manageEnrolledLearner = (ManageEnrolledLearner)itr.next();
						for (int i =0;i<manageGradeList.size();i++){
							if(manageGradeList.get(i).getUser().getId() == manageEnrolledLearner.getUser().getId()){
								flag = true;
							}
						}
						if(!flag){
							ManageGrade manageGrade = new ManageGrade();
							manageGrade.setUser(manageEnrolledLearner.getUser());
							if (manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics() != null) {
								manageGrade.setScore(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics().getHighestPostTestScore()+"");
							}
							LearnerSelfStudyCourseActivity learnerSelfStudyCourseActivity = new LearnerSelfStudyCourseActivity();
							learnerSelfStudyCourseActivity.setLearnerCourseStatistics(manageEnrolledLearner.getLearnerEnrollment().getCourseStatistics());
							learnerSelfStudyCourseActivity.setLearnerEnrollment(manageEnrolledLearner.getLearnerEnrollment());
							manageGrade.setLearnerCourseActivity(learnerSelfStudyCourseActivity);
							manageGradeList.add(manageGrade);
						}
					}
				}
				form.setManageGradeList(manageGradeList);
			}
		} catch(Exception e) {
			log.debug(e);
		}

		if (form.getActType().equalsIgnoreCase(GradeBookForm.LECTURE_COURSE) ){
			return new ModelAndView(editLectureGradeTemplate);
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.SELF_STUDY_COURSE)){
			return new ModelAndView(editSelfStudyCourseActivityTemplate);
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.FINAL_SCORE_COURSE)){
			return new ModelAndView(editFinalCourseActivityTemplate);
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.ASSIGNMENT_COURSE)){
			return new ModelAndView(editAssignmentActivityTemplate);
		}

		return null;
	}
	
	public ModelAndView printGrade( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		GradeBookForm form = (GradeBookForm)command;
		PdfCreator creator  = new PdfCreator();
		creator.buildGradeBookPdfDocument(form);
		return new ModelAndView(redirectTemplate);
	}

	public ModelAndView saveGrade( HttpServletRequest request, HttpServletResponse response,Object command, BindException errors ) throws Exception {
		GradeBookForm form = (GradeBookForm)command;

		if (form.getActType().equalsIgnoreCase(GradeBookForm.LECTURE_COURSE) ){
			List<ManageGrade> manageGradeList = form.getManageGradeList();
			CourseActivity courseActivity = resourceService.getCourseActivityById(form.getCaId());
			for(ManageGrade mngGrade : manageGradeList) {
				LearnerCourseActivity learnerCourseActivity=mngGrade.getLearnerCourseActivity();
				LearnerLectureCourseActivity learnerLectureCourseActivity = (LearnerLectureCourseActivity)learnerCourseActivity;
				learnerLectureCourseActivity.setAttended(mngGrade.isAttended());
				learnerLectureCourseActivity.setCourseActivity(courseActivity);
				resourceService.saveLearnerCourseActivity(learnerLectureCourseActivity);
				//resourceService.saveLearnerCourseActivity(learnerLectureCourseActivity);
			}
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.ASSIGNMENT_COURSE)){
			List<ManageGrade> manageGradeList = form.getManageGradeList();
			CourseActivity courseActivity = resourceService.getCourseActivityById(form.getCaId());
			for(ManageGrade mngGrade : manageGradeList) {
				LearnerCourseActivity learnerCourseActivity=mngGrade.getLearnerCourseActivity();
				LearnerAssignmentActivity learnerAssignmentActivity = (LearnerAssignmentActivity)learnerCourseActivity;
				learnerAssignmentActivity.setAssignmentAttempted(mngGrade.isAttempted());
				learnerAssignmentActivity.setAssignmentComplete(mngGrade.isCompleted());
				
				learnerAssignmentActivity.setCourseActivity(courseActivity);
				resourceService.saveLearnerCourseActivity(learnerAssignmentActivity);
			}
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.FINAL_SCORE_COURSE)){
			List<ManageGrade> manageGradeList = form.getManageGradeList();
			CourseActivity courseActivity = resourceService.getCourseActivityById(form.getCaId());
			for(ManageGrade mngGrade : manageGradeList) {
				//SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				LearnerCourseActivity learnerCourseActivity=mngGrade.getLearnerCourseActivity();
				LearnerFinalCourseActivity learnerFinalCourseActivity = (LearnerFinalCourseActivity)learnerCourseActivity;
				learnerFinalCourseActivity.setCourseActivity(courseActivity);
				learnerFinalCourseActivity.setCourseComplete(mngGrade.isCourseComplete());
				resourceService.saveLearnerCourseActivity(learnerFinalCourseActivity);

                if(mngGrade.isCourseComplete()) {
                    LearnerCourseStatistics learnerCourseStatistics =
                    learnerFinalCourseActivity.getLearnerCourseStatistics();

                    learnerCourseStatistics.setCompleted(true);
                    learnerCourseStatistics.setStatus(LearnerCourseStatistics.COMPLETED);
                    learnerCourseStatistics.setCompletionDate(new Date());
                    statisticsService.saveLearnerCourseStatistics
                            (learnerCourseStatistics);

                    learnersToBeMailedService.emailCourseCompletionCertificate(learnerCourseStatistics.getLearnerEnrollment().getId());


                }

			}
		} else if(form.getActType().equalsIgnoreCase(GradeBookForm.SELF_STUDY_COURSE)){
			List<ManageGrade> manageGradeList = form.getManageGradeList();
			CourseActivity courseActivity = resourceService.getCourseActivityById(form.getCaId());
			for(ManageGrade mngGrade : manageGradeList) {
				if (!mngGrade.getOverride().isEmpty()) {
					if (Double.parseDouble(mngGrade.getOverride()) > 0) {
						//LearnerSelfStudyCourseActivity learnerSelfStudyCourseActivity = new LearnerSelfStudyCourseActivity();
						LearnerCourseActivity learnerCourseActivity=mngGrade.getLearnerCourseActivity();
						LearnerSelfStudyCourseActivity	learnerSelfStudyCourseActivity=(LearnerSelfStudyCourseActivity)learnerCourseActivity;
						learnerSelfStudyCourseActivity.setOverrideScore(Double.parseDouble(mngGrade.getOverride()));
						((LearnerSelfStudyCourseActivity)mngGrade.getLearnerCourseActivity()).setOverrideScore(Double.parseDouble(mngGrade.getOverride()));
						mngGrade.getLearnerCourseActivity().setCourseActivity(courseActivity);
						//resourceService.saveCourseActivity(courseActivity);
						resourceService.saveLearnerCourseActivity(mngGrade.getLearnerCourseActivity());
					}
				}
			}
		}

		return new ModelAndView(editGradebookTemplate);
	}

	/**
	 * @return the resourceService
	 */
	public ResourceService getResourceService() {
		return resourceService;
	}

	/**
	 * @param resourceService the resourceService to set
	 */
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * @return the manageGradeBookTemplate
	 */
	public String getManageGradeBookTemplate() {
		return manageGradeBookTemplate;
	}

	/**
	 * @param manageGradeBookTemplate the manageGradeBookTemplate to set
	 */
	public void setManageGradeBookTemplate(String manageGradeBookTemplate) {
		this.manageGradeBookTemplate = manageGradeBookTemplate;
	}

	/**
	 * @return the editGradebookTemplate
	 */
	public String getEditGradebookTemplate() {
		return editGradebookTemplate;
	}

	/**
	 * @param editGradebookTemplate the editGradebookTemplate to set
	 */
	public void setEditGradebookTemplate(String editGradebookTemplate) {
		this.editGradebookTemplate = editGradebookTemplate;
	}

	/**
	 * @return the synchronousClassService
	 */
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	/**
	 * @param synchronousClassService the synchronousClassService to set
	 */
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	/**
	 * @return the statisticsService
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the editLectureGradeTemplate
	 */
	public String getEditLectureGradeTemplate() {
		return editLectureGradeTemplate;
	}

	/**
	 * @param editLectureGradeTemplate the editLectureGradeTemplate to set
	 */
	public void setEditLectureGradeTemplate(String editLectureGradeTemplate) {
		this.editLectureGradeTemplate = editLectureGradeTemplate;
	}

	/**
	 * @return the editSelfStudyCourseActivityTemplate
	 */
	public String getEditSelfStudyCourseActivityTemplate() {
		return editSelfStudyCourseActivityTemplate;
	}

	/**
	 * @param editSelfStudyCourseActivityTemplate the editSelfStudyCourseActivityTemplate to set
	 */
	public void setEditSelfStudyCourseActivityTemplate(
			String editSelfStudyCourseActivityTemplate) {
		this.editSelfStudyCourseActivityTemplate = editSelfStudyCourseActivityTemplate;
	}

	/**
	 * @return the editAssignmentActivityTemplate
	 */
	public String getEditAssignmentActivityTemplate() {
		return editAssignmentActivityTemplate;
	}

	/**
	 * @param editAssignmentActivityTemplate the editAssignmentActivityTemplate to set
	 */
	public void setEditAssignmentActivityTemplate(
			String editAssignmentActivityTemplate) {
		this.editAssignmentActivityTemplate = editAssignmentActivityTemplate;
	}

	/**
	 * @return the editFinalCourseActivityTemplate
	 */
	public String getEditFinalCourseActivityTemplate() {
		return editFinalCourseActivityTemplate;
	}

	/**
	 * @param editFinalCourseActivityTemplate the editFinalCourseActivityTemplate to set
	 */
	public void setEditFinalCourseActivityTemplate(
			String editFinalCourseActivityTemplate) {
		this.editFinalCourseActivityTemplate = editFinalCourseActivityTemplate;
	}
	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

    public LearnersToBeMailedService getLearnersToBeMailedService() {
        return learnersToBeMailedService;
    }

    public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
        this.learnersToBeMailedService = learnersToBeMailedService;
    }

}