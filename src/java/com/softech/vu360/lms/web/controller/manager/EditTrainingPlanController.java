package com.softech.vu360.lms.web.controller.manager;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.TrainingPlanCourseView;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.TrainingPlanCourseViewSort;
import com.softech.vu360.util.VU360Branding;

public class EditTrainingPlanController extends MultiActionController implements
InitializingBean {

	private static final Logger log = Logger.getLogger(EditTrainingPlanController.class.getName());
	private String editTrainingPlanNameAndDescriptionTemplate = null;
	private String editTrainingPlanCourseTemplate = null;
	private String redirectTemplate = null;

	private static final String EDIT_TRAININGPLAN_SAVENAME_ACTION = "SavePlanNameAndDescription";
	private static final String EDIT_TRAININGPLAN_UPDATECOURSES_ACTION = "UpdateCourses";
	private static final String MANAGE_TRAININGPLANCOURSE_DELETE_ACTION = "Delete";
	private static final String MANAGE_TRAININGPLAN_SHOWALLPLANS_ACTION = "allsearch";
	private static final String EDIT_TRAININGPLAN_COURSE_UPDATE_ACTION="update"; 
	private static final String ADD_NEW_COURSES = "addNewCourses";
	
	private static final int SORTBY_COURSENAME = 0;
	private static final int SORTBY_COURSEID = 1;
	private static final int SORTBY_COURSEEXPIRATIONDATE = 2;

	private TrainingPlanService trainingPlanService;
	private LearnerService learnerService = null;
	private CustomerService customerService = null;
	private EntitlementService entitlementService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	HttpSession session = null;

	public ModelAndView editTrainingPlanNameAndDescription(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String trainingPlanId = request.getParameter("Id");
			TrainingPlan trainingPlan = null;
			if(StringUtils.isNotBlank(trainingPlanId)){
				trainingPlan = trainingPlanService.loadForUpdateTrainingPlan(Long.parseLong(trainingPlanId));
			}
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(EDIT_TRAININGPLAN_SAVENAME_ACTION)){
					if(trainingPlan != null) {
					//trainingPlan = (TrainingPlan)session.getAttribute("trainingPlanObject");
						if(!validate(request,context)) 
						{
							trainingPlan.setName(request.getParameter("trainingPlanName").trim());
							trainingPlan.setDescription(request.getParameter("trainingPlanDescription").trim());
							trainingPlan = trainingPlanService.addTrainingPlan(trainingPlan);
							context.put("action", "Search");
							return new ModelAndView(redirectTemplate,"context",context);
						}
						else {
							trainingPlan.setName(request.getParameter("trainingPlanName").trim());
							trainingPlan.setDescription(request.getParameter("trainingPlanDescription").trim());
						}
					}
				}	
			}	
			context.put("trainingPlan", trainingPlan);
			return new ModelAndView(editTrainingPlanNameAndDescriptionTemplate,"context",context);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editTrainingPlanNameAndDescriptionTemplate);
	}


	private boolean validate(HttpServletRequest request,Map context){
		boolean check = false;
		if(StringUtils.isBlank(request.getParameter("trainingPlanName"))){
			context.put("validateName","error.editTrainingPlan.validateTrainingPlanNameBlank");
			check = true;
		}
		return check;
	}




	public ModelAndView editTrainingPlanCourse(HttpServletRequest request,
			HttpServletResponse response) {


		try{
			TrainingPlan trainingPlan = null;
			Map<Object, Object> context = new HashMap<Object, Object>();
			String trainingPlanId = request.getParameter("Id");
			String action = request.getParameter("action");			
			
			if(StringUtils.isNotBlank(trainingPlanId)) {
				
				trainingPlan = trainingPlanService.loadForUpdateTrainingPlan(Long.parseLong(trainingPlanId));
				context.put("trainingPlan", trainingPlan);
				 
				if (trainingPlan != null) {
					List<TrainingPlanCourse> newCoursesToAdd = new ArrayList<TrainingPlanCourse>();
					String newCoursesId = request.getParameter("newCoursesId");					
					
					if (!StringUtils.isBlank(action)
							&& action.trim().equals(EDIT_TRAININGPLAN_UPDATECOURSES_ACTION)) {
						updateTrainingPlan(trainingPlan, newCoursesId, request.getParameter("trainingPlanCourseIdsToDelete"));
						return new ModelAndView(redirectTemplate);
					}
					else {
						List<String> existingTpCourseIdsToRemove = new ArrayList<String>();
						if (!StringUtils.isBlank(request.getParameter("trainingPlanCourseIdsToDelete")))
							context.put("trainingPlanCourseIdsToDelete", request.getParameter("trainingPlanCourseIdsToDelete").trim());
						
						if (!StringUtils.isBlank(action)) {
							
							if( action.trim().equals(MANAGE_TRAININGPLANCOURSE_DELETE_ACTION)) {
								String[] tpcourseIds = request.getParameterValues("trainingPlanCourseIds"); //Selected Courses to Delete
								StringBuilder tpCourseIdsBuilder = new StringBuilder();
								for (String tpcourseId :tpcourseIds) {
									tpCourseIdsBuilder.append(tpcourseId + ",");									
								}
								
								String ids = StringUtils.removeEnd(tpCourseIdsBuilder.toString(), ",");
								context.put("trainingPlanCourseIdsToDelete", ids);
								
								//Check if any from new courses to delete.
								ArrayList<String> remainingNewCoursesIds = new ArrayList<String>();
								if (!StringUtils.isBlank(newCoursesId)) {
									String[] newCoursesIdStr = newCoursesId.split(",");									
									
									for (int j=0;j<newCoursesIdStr.length;j++) {
										boolean toKeepNewCourses = true;
										String newCourseId = newCoursesIdStr[j];
										for (int i=0;i<tpcourseIds.length;i++) {
											String tpcourseId = tpcourseIds[i];
											if (tpcourseId.startsWith("0")) {
												String courseId = tpcourseId.split(":")[1];
												if (courseId.trim().equals(newCourseId)) {
													toKeepNewCourses = false;
													break;
												}
											}
											else {
												String tpcId = tpcourseId.split(":")[0];
												existingTpCourseIdsToRemove.add(tpcId);
											}
										}
										if (toKeepNewCourses)
											remainingNewCoursesIds.add(newCourseId);
									}
									
									if (!remainingNewCoursesIds.isEmpty()) {
										newCoursesId = "";
										for(String id : remainingNewCoursesIds) {
											newCoursesId += id + ",";
										}
										newCoursesId = StringUtils.removeEnd(newCoursesId.toString(), ",");
									}
									else {
										newCoursesId = "";
									}
								}
								else {
									context.put("newCoursesId", newCoursesId);
										
									for (String tpcourseId : tpcourseIds) {
										String tpcId = tpcourseId.split(":")[0];
										existingTpCourseIdsToRemove.add(tpcId);
									}
								}
								//trainingPlanService.deleteTrainingPlanCourses(courseIds);
							}
							
							if (action.trim().equals(ADD_NEW_COURSES) 
										|| !StringUtils.isBlank(newCoursesId)) {
								
								if (!StringUtils.isBlank(newCoursesId)) {
									String[] newCoursesIdStr = newCoursesId.split(",");
									
									long[] newCoursesIds = new long[newCoursesIdStr.length];
									for (int i=0;i<newCoursesIds.length;i++) {
										newCoursesIds[i] = Long.valueOf(newCoursesIdStr[i]);
									}
									
									List<Course> selectedCourses = courseAndCourseGroupService.getCoursesByIds(newCoursesIds);
									
									for( Course c : selectedCourses) {			
										TrainingPlanCourse tpcourse = new TrainingPlanCourse();
										tpcourse.setId(0L);
										tpcourse.setCourse(c);
										tpcourse.setTrainingPlan(trainingPlan);
										newCoursesToAdd.add(tpcourse);
									}
									context.put("newCoursesId", newCoursesId);
								}
							}
						}
						
						//Sorting logic below
						String sortDirection = request.getParameter("sortDirection");
						if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
							sortDirection = session.getAttribute("sortDirection").toString();
						String sortColumnIndex = request.getParameter("sortColumnIndex");
						if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
							sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
	
						String pageIndex = request.getParameter("pageCurrIndex");
						if( pageIndex == null ) pageIndex = "0";
						Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
						PagerAttributeMap.put("pageIndex",pageIndex);
						request.setAttribute("PagerAttributeMap", PagerAttributeMap);
						
						List<TrainingPlanCourse> trainingPlanCourses = new ArrayList<TrainingPlanCourse>();
						List<TrainingPlanCourseView> trainingPlanCourseViewList = new ArrayList<TrainingPlanCourseView>();
						if(existingTpCourseIdsToRemove.isEmpty()) {
							trainingPlanCourses = trainingPlanService.getTrainingPlanCourses(trainingPlan);
						}
						else {
							String[] existingCourseIdsToRemoveStrArray = new String[existingTpCourseIdsToRemove.size()];
							existingCourseIdsToRemoveStrArray = existingTpCourseIdsToRemove.toArray(existingCourseIdsToRemoveStrArray);
							trainingPlanService.deleteTrainingPlanCourses(existingCourseIdsToRemoveStrArray);
							trainingPlanCourses = trainingPlanService.getTrainingPlanCourses(trainingPlan, existingCourseIdsToRemoveStrArray);
						}
						
						if (!newCoursesToAdd.isEmpty())
							trainingPlanCourses.addAll(newCoursesToAdd);
						
						if (!trainingPlanCourses.isEmpty()) {
							int intLimit = 100;
							Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
							String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");
							
							if(StringUtils.isNumeric(limit.trim()))
								intLimit  = Integer.parseInt(limit);
							
							trainingPlanCourseViewList = entitlementService.getEntitledCoursesFromTrainingPlanCourses(trainingPlanCourses, intLimit);
							
							for (TrainingPlanCourseView enrollmentCourseView : trainingPlanCourseViewList) {
								for (TrainingPlanCourse tpCourse : trainingPlanCourses) {
									if (enrollmentCourseView.getCourseId().equals("" + tpCourse.getCourse().getId())) {
										String courseId = enrollmentCourseView.getCourseId();
										Long tpCourseId = tpCourse.getId();
										String idForView = "" + tpCourseId + ":" + courseId;
										enrollmentCourseView.setCourseId(idForView);
									}
								}
							}
						}
						
						if(sortColumnIndex==null)
							sortColumnIndex="0";
						if(sortDirection==null)
							sortDirection="0";
						context.put("sortDirection", sortDirection);
						context.put("sortColumnIndex", sortColumnIndex);
						
						if( StringUtils.isNotBlank(sortDirection)&& StringUtils.isNotBlank(sortColumnIndex) ) {
							TrainingPlanCourseViewSort sorter = new TrainingPlanCourseViewSort();
							Integer direction = Integer.valueOf(sortDirection.trim());
							Integer columnIndex = 0;
							sorter.setSortDirection(direction);
							//Sort by Course Name 
							if( sortColumnIndex.equals("0") ) {
								sorter.setSortBy(TrainingPlanCourseViewSort.SORTBY_COURSENAME);
							}//Sort by Course Id 'Business Key'
							else if (sortColumnIndex.equals("1")) {
								sorter.setSortBy(TrainingPlanCourseViewSort.SORTBY_ID);
								columnIndex = Integer.valueOf(1);
							}//Sort by Course Id 'Expiration Date'
							else if (sortColumnIndex.equals("2")){
								sorter.setSortBy(TrainingPlanCourseViewSort.SORTBY_EXPIRATIONDATE);
								columnIndex = Integer.valueOf(2);							
							}else if (sortColumnIndex.equals("3")){
								sorter.setSortBy(TrainingPlanCourseViewSort.SORTBY_ENTITLEMENTNAME);
								columnIndex = Integer.valueOf(3);							
							}

							Collections.sort(trainingPlanCourseViewList, sorter);
							context.put("sortDirection", direction);
							context.put("sortColumnIndex", columnIndex);
							session.setAttribute("sortDirection", direction);
							session.setAttribute("sortColumnIndex", columnIndex);	
						}
						context.put("trainingPlanCourses", trainingPlanCourseViewList);
					}
				}
			}
			return new ModelAndView(editTrainingPlanCourseTemplate, "context", context);

		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editTrainingPlanCourseTemplate);
	}
	
	
	private void updateTrainingPlan(TrainingPlan trainingPlan, String newCoursesId, String trainingPlanCourseIdsToDelete) {
		if (!StringUtils.isBlank(trainingPlanCourseIdsToDelete)) {
			ArrayList<String> trainingPlanCourseIds = new ArrayList<String>(); 
			String[] courseIdsToDelete = trainingPlanCourseIdsToDelete.split(",");
			for(String courseIdToDelete : courseIdsToDelete) {
				String tpId = courseIdToDelete.split(":")[0].trim();
				if (!tpId.equals("0")) {
					trainingPlanCourseIds.add(tpId);
				}
			}
			if (!trainingPlanCourseIds.isEmpty()){
				trainingPlanService.deleteTrainingPlanCourses(trainingPlanCourseIds);
				
				//Also delete from other table
				
				List<TrainingPlanCourse> trainingPlanCoursesToDelete=new ArrayList<TrainingPlanCourse>();
				for(String strCourseId : trainingPlanCourseIds){
					
				
					for(TrainingPlanCourse tpCourse : trainingPlan.getCourses()){
						if(tpCourse.getId().equals(Long.parseLong(strCourseId))){
							trainingPlanCoursesToDelete.add(tpCourse);
						}
					}
					
					
					
				}
				
				trainingPlan.getCourses().removeAll(trainingPlanCoursesToDelete);
				trainingPlanService.addTrainingPlan(trainingPlan);
				
			}
		}
		
		if (!StringUtils.isBlank(newCoursesId)) {
			TrainingPlan updatedTrainingPlan = trainingPlanService.loadForUpdateTrainingPlan(trainingPlan.getId());
//			list was not updated for courses
			updatedTrainingPlan.setCourses(trainingPlanService.getTrainingPlanCourses(updatedTrainingPlan));
			
			List<TrainingPlanCourse> newTrainingPlanCourses = new ArrayList<TrainingPlanCourse>();
			String[] ids = newCoursesId.split(",");
			long[] courseIds = new long[ids.length];
			for (int i=0;i<courseIds.length;i++) {
				courseIds[i] = Long.valueOf(ids[i]);
			}
			
			List<Course> courses = courseAndCourseGroupService.getCoursesByIds(courseIds);
			for (Course course : courses) {
				TrainingPlanCourse newTpCourse = new TrainingPlanCourse();
				newTpCourse.setCourse(course);
				newTpCourse.setTrainingPlan(updatedTrainingPlan);
				newTrainingPlanCourses.add(newTpCourse);
			}
			
			if (!newTrainingPlanCourses.isEmpty()) {
				List<TrainingPlanCourse> trainingPlanCoursesAdded = trainingPlanService.addTrainingPlanCourses(newTrainingPlanCourses);
				if (!trainingPlanCoursesAdded.isEmpty()) {
					updatedTrainingPlan.getCourses().addAll(trainingPlanCoursesAdded);
					trainingPlanService.addTrainingPlan(updatedTrainingPlan);
				}
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}



	public String getEditTrainingPlanNameAndDescriptionTemplate() {
		return editTrainingPlanNameAndDescriptionTemplate;
	}



	public void setEditTrainingPlanNameAndDescriptionTemplate(
			String editTrainingPlanNameAndDescriptionTemplate) {
		this.editTrainingPlanNameAndDescriptionTemplate = editTrainingPlanNameAndDescriptionTemplate;
	}



	public LearnerService getLearnerService() {
		return learnerService;
	}



	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}



	public CustomerService getCustomerService() {
		return customerService;
	}



	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}



	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}



	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}



	public String getEditTrainingPlanCourseTemplate() {
		return editTrainingPlanCourseTemplate;
	}



	public void setEditTrainingPlanCourseTemplate(
			String editTrainingPlanCourseTemplate) {
		this.editTrainingPlanCourseTemplate = editTrainingPlanCourseTemplate;
	}


	public EntitlementService getEntitlementService() {
		return entitlementService;
	}


	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}


	public String getRedirectTemplate() {
		return redirectTemplate;
	}


	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}


	/**
	 * @return the trainingPlanService
	 */
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}


	/**
	 * @param trainingPlanService the trainingPlanService to set
	 */
	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

}
