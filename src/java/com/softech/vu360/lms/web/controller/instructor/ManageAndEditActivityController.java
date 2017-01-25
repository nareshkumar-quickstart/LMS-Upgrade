package com.softech.vu360.lms.web.controller.instructor;

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

import com.softech.vu360.lms.model.AssignmentCourseActivity;
import com.softech.vu360.lms.model.AttendanceCourseActivity;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.FinalScoreCourseActivity;
import com.softech.vu360.lms.model.GeneralGradedCourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.LectureCourseActivity;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.ActivityForm;
import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;
import com.softech.vu360.lms.web.controller.validator.Instructor.ActivityValidator;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ManageActivitySort;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageAndEditActivityController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(ManageAndEditActivityController.class.getName());

	public static final String FINAL_SCORE_COURSE = "Final Score";
	public static final String ASSIGNMENT_COURSE = "Assignment";
	public static final String GENERAL_GRADED = "General Graded";
	public static final String ATTENDANCE = "Attendance";
	//public static final String SELF_STUDY_COURSE = "Self Study Course";
	//public static final String LECTURE_COURSE = "Lecture Course";

	private ResourceService resourceService;

	private String manageActivityTemplate = null;
	private String editActivityTemplate = null;
	private String closeTemplate = null;
	
	private HttpSession session = null;

	public ManageAndEditActivityController() {
		super();
	}

	public ManageAndEditActivityController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		session = request.getSession();
		String grdBkId =  request.getParameter("grdBkId");
		if (!StringUtils.isBlank(grdBkId)) {
			Gradebook gradeBook = resourceService.getGradeBookById(Long.parseLong(grdBkId));
			String className = gradeBook.getSynchronousClass().getSectionName();
			context.put("className", className);
			session.setAttribute("className", className);
		} else {
			grdBkId = (String)session.getAttribute("grdBkId");
		}
		context.put("grdBkId", grdBkId);
		session.setAttribute("grdBkId", grdBkId);
		return new ModelAndView(manageActivityTemplate,"context",context);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof ActivityForm ){
			ActivityForm form = (ActivityForm)command;
			if( methodName.equals("editActivity")){
				CourseActivity courseActivity = new CourseActivity();
				courseActivity = resourceService.loadForUpdateCourseActivity(form.getId());
				form.setCourseActivity(courseActivity);
			}
		}
	}

	public ModelAndView searchActivity( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {

		try {
			ActivityForm form = (ActivityForm)command;
			session= request.getSession();
			String grdBkId =  request.getParameter("grdBkId");
			if(StringUtils.isNotEmpty(grdBkId)){
			session.setAttribute("grdBkId", grdBkId);
			}
			Map<Object, Object> context = new HashMap<Object, Object>();			
			List<ManageActivity> manageActivities = new ArrayList<ManageActivity>();


			String activityName = "";
			String showAll = "true";//request.getParameter("showAll");
			
			if( showAll != null && showAll.equalsIgnoreCase("true")  )
			{
				activityName = (session.getAttribute("activityName") == null) ? "" : session.getAttribute("activityName").toString() ;
			} else {
				activityName = (showAll == null) ? "" : request.getParameter("activityName");
				session.setAttribute("activityName", activityName);
			}
			//appName = HtmlEncoder.escapeHtmlFull(resourceName).toString();
			activityName = HtmlEncoder.escapeHtmlFull(activityName).toString();
			context.put("activityName", activityName);
			
			String className = session.getAttribute("className").toString();
			context.put("className", className);
			session.setAttribute("className", className);
			List<CourseActivity> activityList = resourceService.findCourseActivitiesByGradeBookAndName(activityName,Long.parseLong(session.getAttribute("grdBkId").toString()));
			
			Collections.sort(activityList);
			form.setCourseActivities(activityList);
			
			
			//List<String> rTypes = new ArrayList<String>();
			if ( activityList != null && activityList.size() > 0) {
				for( CourseActivity activity : activityList ) {
					ManageActivity mAct = new ManageActivity();					
					if (activity instanceof FinalScoreCourseActivity) {
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.FINAL_SCORE_COURSE);
						mAct.setActivityScore(activity.getActivityScore());
						form.setManageActivity(mAct);
						
						//manageActivities.add(mAct); LMS:7973 (NOTE: There should be only one FINAL_SCORE_COURSE activity which will be created automatically during adding schedule)
					} 
					else if (activity instanceof AssignmentCourseActivity){
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.ASSIGNMENT_COURSE);
						mAct.setActivityScore(activity.getActivityScore());
						manageActivities.add(mAct);
					} 
					/*else if (activity instanceof SelfStudyCourseActivity){
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.SELF_STUDY_COURSE);
						manageActivities.add(mAct);
					} 
					else if (activity instanceof LectureCourseActivity){
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.LECTURE_COURSE);
						manageActivities.add(mAct);
					}*/ 
					else if (activity instanceof AttendanceCourseActivity){
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.ATTENDANCE);
						mAct.setActivityScore(activity.getActivityScore());
						manageActivities.add(mAct);
					} 
					else if (activity instanceof GeneralGradedCourseActivity){
						mAct.setId(activity.getId());
						mAct.setName(activity.getActivityName());
						mAct.setType(ManageAndEditActivityController.GENERAL_GRADED);
						mAct.setActivityScore(activity.getActivityScore());
						manageActivities.add(mAct);
					}
					
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
			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageActivitySort sort = new ManageActivitySort();
						sort.setSortBy("activityName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageActivities,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageActivitySort sort = new ManageActivitySort();
						sort.setSortBy("activityName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageActivities,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageActivitySort sort = new ManageActivitySort();
						sort.setSortBy("activityType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageActivities,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageActivitySort sort = new ManageActivitySort();
						sort.setSortBy("activityType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageActivities,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 1);
					}
				}
			}	
			form.setActivityList(manageActivities);	
			context.put("grdBkId", session.getAttribute("grdBkId"));
			session.setAttribute("grdBkId", session.getAttribute("grdBkId"));
			context.put("activityList", manageActivities);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			return new ModelAndView(manageActivityTemplate,"context",context);

		}catch( Exception e ){
			// do nothing
			e.printStackTrace();
		}
		return new ModelAndView(manageActivityTemplate);
	}
	
	public ModelAndView saveFinalScoreActivity( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {
		
		try {
			ActivityForm form = (ActivityForm)command;
			
			String activityScores[] = request.getParameterValues("gridActivityScore");
			String activityIds[] = request.getParameterValues("gridActivityId");
			
			if(activityScores!=null && activityIds!=null && activityScores.length == activityIds.length){
				for (CourseActivity courseActivity : form.getCourseActivities()) {
					int activityScore = this.findActivityScore(courseActivity.getId(), activityIds, activityScores);
					CourseActivity activityUpdate = resourceService.loadForUpdateCourseActivity(courseActivity.getId());
					activityUpdate.setActivityScore(activityScore);
					resourceService.saveCourseActivity(activityUpdate);
				}
			}
			
			/*
			 * Update FinalScoreCourseActivity
			 */
		}
		catch(Exception e){
			
		}
		
		return searchActivity(request, response, command, errors);
	}

	public ModelAndView editActivity( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {

		ActivityForm form = (ActivityForm)command;
		try {
			if (form.getCourseActivity() != null) {
				form.setActivityName(form.getCourseActivity().getActivityName());
				form.setDescription(form.getCourseActivity().getDescription());
				
				if (form.getCourseActivity() instanceof FinalScoreCourseActivity) {
					form.setType(ManageAndEditActivityController.FINAL_SCORE_COURSE);
				} 
				else if (form.getCourseActivity() instanceof AssignmentCourseActivity){
					form.setType(ManageAndEditActivityController.ASSIGNMENT_COURSE);
				} 
				/*else if (form.getCourseActivity() instanceof SelfStudyCourseActivity){
					form.setType(ManageAndEditActivityController.SELF_STUDY_COURSE);
				} 
				else if (form.getCourseActivity() instanceof LectureCourseActivity){
					form.setType(ManageAndEditActivityController.LECTURE_COURSE);
				}*/
				else if (form.getCourseActivity() instanceof AttendanceCourseActivity){
					form.setType(ManageAndEditActivityController.ATTENDANCE);
				}
				else if (form.getCourseActivity() instanceof GeneralGradedCourseActivity){
					form.setType(ManageAndEditActivityController.GENERAL_GRADED);
				}
			}
		} catch (Exception e) {
			log.debug(e);
		}
		return new ModelAndView(editActivityTemplate);
	}

	public ModelAndView saveActivity( HttpServletRequest request, HttpServletResponse response,	Object command, BindException errors ) throws Exception {

		ActivityForm form = (ActivityForm)command;
		if( errors.hasErrors() ) {
			return new ModelAndView(editActivityTemplate);
		}
		try {
			if (form.getCourseActivity() instanceof FinalScoreCourseActivity) {
				FinalScoreCourseActivity activity = (FinalScoreCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			} else if (form.getCourseActivity() instanceof AssignmentCourseActivity){
				AssignmentCourseActivity activity = (AssignmentCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			} 
			/*else if (form.getCourseActivity() instanceof SelfStudyCourseActivity){
				SelfStudyCourseActivity activity = (SelfStudyCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			} 
			else if (form.getCourseActivity() instanceof LectureCourseActivity){
				LectureCourseActivity activity = (LectureCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			}*/
			else if (form.getCourseActivity() instanceof GeneralGradedCourseActivity){
				GeneralGradedCourseActivity activity = (GeneralGradedCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			}
			else if (form.getCourseActivity() instanceof AttendanceCourseActivity){
				AttendanceCourseActivity activity = (AttendanceCourseActivity)form.getCourseActivity();
				activity.setActivityName(form.getActivityName());
				activity.setDescription(form.getDescription());
				resourceService.saveCourseActivity(activity);
			}
		} catch (Exception e) {
			log.debug(e);
		}
		Map context = new HashMap();
		context.put("target", "searchActivity");
		return new ModelAndView(closeTemplate, "context", context);
	}

	
	
	public ModelAndView moveActivity(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		long grdBkId = Long.parseLong(request.getParameter("grdBkId")!=null ? request.getParameter("grdBkId") : "0");
		ActivityForm form = (ActivityForm)command;
		int aid = Integer.parseInt(request.getParameter("aid"));
		String param = request.getParameter("moveParam");
		String pageIndex = request.getParameter("pageCurrIndex");
		/*List<CourseActivity> actvtyList = resourceService.findCourseActivitiesByGradeBookAndName("",
				Long.parseLong(session.getAttribute("grdBkId").toString()));*/
		
		List<CourseActivity> actvtyList = resourceService.findCourseActivitiesByGradeBook(grdBkId);
		
		Collections.sort(actvtyList);
		if ( actvtyList != null && actvtyList.size() > 0) {
	         for(int i=0;i< actvtyList.size(); i++ ) {
	        if (actvtyList.get(i) instanceof FinalScoreCourseActivity){
	            actvtyList.remove(i);
	            
	        }
	    }
	   }
		form.setCourseActivities(actvtyList);
		CourseActivity activity = form.getCourseActivities().get(aid);
		//log.debug(" INSIDE moveQuestion :: aid :: "+aid+" param :: "+param+" questions ::"+form.getDeleteableQuestions().size()+" pageIndex :: "+pageIndex+" :>question:>"+question.getId());
		
		if(param.equals("top"))
		{

			CourseActivity topActivity = form.getCourseActivities().get(Integer.parseInt(pageIndex));
			CourseActivity topActivityNew=resourceService.loadForUpdateCourseActivity(topActivity.getId());
			topActivityNew.setDisplayOrder(aid);
			
			CourseActivity activityNew = resourceService.loadForUpdateCourseActivity(activity.getId());
			activityNew.setDisplayOrder(topActivity.getDisplayOrder());
			//topActivity.setDisplayOrder(activity.getDisplayOrder());
			resourceService.saveCourseActivity(topActivityNew);
			resourceService.saveCourseActivity(activityNew);
			//surveyService.add(topActivity);
			//surveyService.addSurveyQuestion(question);			
		}
		if(param.equals("bottom"))
		{
			//int bottomIndex = Integer.parseInt(pageIndex)+9;
			//if(bottomIndex>form.getCourseActivities().size())
			int bottomIndex=form.getCourseActivities().size()-1;
			CourseActivity bottomActivity = form.getCourseActivities().get(bottomIndex);
			CourseActivity bottomActivityNew=resourceService.loadForUpdateCourseActivity(bottomActivity.getId());
			bottomActivityNew.setDisplayOrder(aid);
			
			CourseActivity activityNew = resourceService.loadForUpdateCourseActivity(activity.getId());
			activityNew.setDisplayOrder(bottomActivity.getDisplayOrder());
			/*int bottomOrder = bottomActivity.getDisplayOrder();
			bottomActivity.setDisplayOrder(activity.getDisplayOrder());
			activity.setDisplayOrder(bottomOrder);*/
			resourceService.saveCourseActivity(bottomActivityNew);
			resourceService.saveCourseActivity(activityNew);		
		}
		if(param.equals("up"))
		{

			CourseActivity upActivity = form.getCourseActivities().get(aid-1);
			int upOrder = upActivity.getDisplayOrder();
			CourseActivity upActivityNew=resourceService.loadForUpdateCourseActivity(upActivity.getId());
			upActivityNew.setDisplayOrder(aid);
			CourseActivity activityNew = resourceService.loadForUpdateCourseActivity(activity.getId());
			activityNew.setDisplayOrder(upOrder);
			resourceService.saveCourseActivity(upActivityNew);
			resourceService.saveCourseActivity(activityNew);			
		}
		if(param.equals("down"))
		{

			if(form.getCourseActivities().size()>1){
				CourseActivity downActivity = form.getCourseActivities().get(aid+1);
				int downOrder = downActivity.getDisplayOrder();
				CourseActivity downActivityNew=resourceService.loadForUpdateCourseActivity(downActivity.getId());
				downActivityNew.setDisplayOrder(aid);
				CourseActivity activityNew = resourceService.loadForUpdateCourseActivity(activity.getId());
				activityNew.setDisplayOrder(downOrder);
				resourceService.saveCourseActivity(downActivityNew);
				resourceService.saveCourseActivity(activityNew);
			}
		}

		Map context = new HashMap();
		List<ManageActivity> manageActivities = new ArrayList<ManageActivity>();
		List<CourseActivity> activityList = resourceService.findCourseActivitiesByGradeBookAndName("",
				Long.parseLong(session.getAttribute("grdBkId").toString()));
		Collections.sort(activityList);
		if ( activityList != null && activityList.size() > 0) {
			for( CourseActivity actvty : activityList ) {
				ManageActivity mAct = new ManageActivity();
				
				if (actvty instanceof FinalScoreCourseActivity) {				
					mAct.setId(activity.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.FINAL_SCORE_COURSE);
					mAct.setActivityScore(actvty.getActivityScore());
					form.setManageActivity(mAct);
				} else if (actvty instanceof AssignmentCourseActivity){
					mAct.setId(actvty.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.ASSIGNMENT_COURSE);
					mAct.setActivityScore(actvty.getActivityScore());
					manageActivities.add(mAct);
				} 
				/*else if (actvty instanceof SelfStudyCourseActivity){
					mAct.setId(actvty.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.SELF_STUDY_COURSE);
					manageActivities.add(mAct);
				} 
				else if (actvty instanceof LectureCourseActivity){
					mAct.setId(actvty.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.LECTURE_COURSE);
					manageActivities.add(mAct);
				}*/
				else if (actvty instanceof AttendanceCourseActivity){
					mAct.setId(actvty.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.ATTENDANCE);
					mAct.setActivityScore(actvty.getActivityScore());
					manageActivities.add(mAct);
				}
				else if (actvty instanceof LectureCourseActivity){
					mAct.setId(actvty.getId());
					mAct.setName(actvty.getActivityName());
					mAct.setType(ManageAndEditActivityController.GENERAL_GRADED);
					mAct.setActivityScore(actvty.getActivityScore());
					manageActivities.add(mAct);
				}
				
			}
		}
		/*Collections.sort(manageActivities);
		context.put("activityList", manageActivities);
		context.put("grdBkId", grdBkId);
		Collections.sort(activityList);
		form.setCourseActivities(activityList);*/
		
		//return new ModelAndView(manageActivityTemplate,"context",context);
		return searchActivity(request, response, command, errors);
		
	}
	public ModelAndView deleteActivity(HttpServletRequest request,HttpServletResponse response ) throws Exception{
		String[] selectedActivityIds = request.getParameterValues("selectedActivity");

		if (selectedActivityIds != null) {
			long activityIdArray[] = new long[selectedActivityIds.length];
			for (int loop = 0; loop < selectedActivityIds.length; loop++) {
				String delActivityId = selectedActivityIds[loop];
				if (delActivityId != null)
					activityIdArray[loop] = Long.parseLong(delActivityId);
			}
			/*
			 * this will Inactive Resource
			 */
			resourceService.deleteCourseActivity(activityIdArray);

		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "searchActivity");
		return new ModelAndView(closeTemplate, "context", context);
	}


	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {

		ActivityValidator validator = (ActivityValidator)this.getValidator();
		ActivityForm form = (ActivityForm)command;
		if( methodName.equals("saveActivity")) {
			validator.validateCourseActivity(form, errors);
		}
	}

	private int findActivityScore(Long courseActivityId,String[] inpActivities ,String[] inpScores){
		int resultScore = 0;
		try{
			for (int i = 0; i < inpActivities.length; i++) {
				if(new Long(inpActivities[i]).longValue() == courseActivityId.longValue()){
					resultScore = Integer.parseInt(inpScores[i]);
					break;
				}
			}
		}
		catch(Exception e){
			
		}
		return resultScore;
		
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
	 * @return the manageActivityTemplate
	 */
	public String getManageActivityTemplate() {
		return manageActivityTemplate;
	}

	/**
	 * @param manageActivityTemplate the manageActivityTemplate to set
	 */
	public void setManageActivityTemplate(String manageActivityTemplate) {
		this.manageActivityTemplate = manageActivityTemplate;
	}

	/**
	 * @return the editActivityTemplate
	 */
	public String getEditActivityTemplate() {
		return editActivityTemplate;
	}

	/**
	 * @param editActivityTemplate the editActivityTemplate to set
	 */
	public void setEditActivityTemplate(String editActivityTemplate) {
		this.editActivityTemplate = editActivityTemplate;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	

}