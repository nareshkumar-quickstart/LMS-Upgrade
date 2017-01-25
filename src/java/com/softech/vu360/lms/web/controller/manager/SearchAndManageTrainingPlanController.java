package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class SearchAndManageTrainingPlanController extends MultiActionController implements
InitializingBean {

	private static final Logger log = Logger.getLogger(SearchAndManageTrainingPlanController.class.getName());
	private String searchTrainingPlanTemplate = null;

	private static final String MANAGE_TRAININGPLAN_SEARCH_ACTION = "Search";
	private static final String MANAGE_TRAININGPLAN_DELETE_ACTION = "Delete";
	private static final String MANAGE_TRAININGPLAN_SHOWALLPLANS_ACTION = "allsearch";

	private LearnerService learnerService = null;
	private TrainingPlanService trainingPlanService;
	private CustomerService customerService = null;
	private EntitlementService entitlementService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	HttpSession session = null;

	public ModelAndView displaySearchTrainingPlan(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			session = request.getSession();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				customer = details.getCurrentCustomer();
			}

			Map<Object, Object> context = new HashMap<Object, Object>();
			String action = request.getParameter("action");
			String sortDirection = request.getParameter("sortDirection");
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			context.put("showAll", showAll);
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
			PagerAttributeMap.put("pageIndex",pageIndex);
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			
			if( action != null ){
				
				if( action.equalsIgnoreCase(MANAGE_TRAININGPLAN_DELETE_ACTION) ) {
					String selectedTrainingPlanValues = request.getParameter("trainingPlanIdsParamHidden");
					if( selectedTrainingPlanValues != null ) {
						trainingPlanService.deleteTrainingPlan(selectedTrainingPlanValues.split(","));
						searchTrainingPlans(request, context, customer, sortColumnIndex, sortDirection);
					}
					return new ModelAndView(searchTrainingPlanTemplate, "context", context);
					
				} else if( action.equalsIgnoreCase(MANAGE_TRAININGPLAN_SHOWALLPLANS_ACTION) ) {
					String searchCriteria = null;
					if( request.getParameter("simpleSearchCriteria") != null ) {
						session.setAttribute("simpleSearchCriteria",request.getParameter("simpleSearchCriteria"));
						searchCriteria = request.getParameter("simpleSearchCriteria");
					} else {
						searchCriteria = (String)session.getAttribute("simpleSearchCriteria");
					}
					if( customer != null ){
						List<TrainingPlan> listOfTrainingPlans = trainingPlanService.findTrainingPlanByName(searchCriteria, customer);
						List<Map<String,Object>> trainingPlanMapList = new ArrayList <Map<String,Object>>();
						
						Long [] arrTrainingPlanIds = new Long[listOfTrainingPlans.size()];
						for(int i=0;i<listOfTrainingPlans.size(); i++){
							arrTrainingPlanIds[i] = listOfTrainingPlans.get(i).getId();
						}
						
						List<Object[]> arrLearnerCoubnt= trainingPlanService.countLearnerByTrainingPlan(arrTrainingPlanIds);
						
						for( TrainingPlan trainingPlan:listOfTrainingPlans) {
							//List<Learner> learners = new ArrayList <Learner>();

							// calculating the number of learners present in each training plan.
							trainingPlan.setEntitledCourseCount(entitlementService.getEntitledCoursesFromTrainingPlanCourses(trainingPlan.getCourses(), 9999).size());
//							for( TrainingPlanAssignment assign : trainingPlanService.getTraingPlanAssignmentsByTraingPlanId(trainingPlan.getId())) {
//								for( LearnerEnrollment enr : assign.getLearnerEnrollments() ) {
//									Learner l = enr.getLearner();
//									boolean unique = true;
//									for(Learner presentLearner : learners) {
//										if( l.getId().compareTo(presentLearner.getId()) == 0 )
//											unique = false;
//									}
//									if(unique)
//										learners.add(l);
//								}
//							}
							
							String learnerCount = "0";
							int indexTRAINIINGPLAN_ID = 0;
							int indexLEARNERCOUNT = 1;
							if(arrLearnerCoubnt!=null && !arrLearnerCoubnt.isEmpty() ){
								for (Object[] objLearnerCoubnt : arrLearnerCoubnt) {
									Long TRAINIINGPLAN_ID = Long.valueOf(objLearnerCoubnt[indexTRAINIINGPLAN_ID].toString());
									if(TRAINIINGPLAN_ID.longValue() == trainingPlan.getId().longValue()){
										learnerCount = objLearnerCoubnt[indexLEARNERCOUNT].toString();
									}
								}	
							}
							Map<String,Object> trPlanMap = new HashMap <String,Object>();
							trPlanMap.put("tPlan", trainingPlan);
							trPlanMap.put("learnerNumber", learnerCount);
							trainingPlanMapList.add(trPlanMap);
							
						}
						
						
						/*************/
						// manually sorting
						if( sortDirection != null && sortColumnIndex != null ) {
							// sorting against training plan name
							if( sortColumnIndex.equalsIgnoreCase("0") ) {
								if( sortDirection.equalsIgnoreCase("0") ) {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
												TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
												if( tp1.getName().compareTo(tp2.getName()) > 0 ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 0);
									context.put("sortColumnIndex", 0);
								} else {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
												TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
												if( tp1.getName().compareTo(tp2.getName()) < 0 ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 1);
									context.put("sortColumnIndex", 0);
								}
							// sorting against number of courses	
							} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
								if( sortDirection.equalsIgnoreCase("0") ) {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
												TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
												if( tp1.getCourses().size() > tp2.getCourses().size() ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 0);
									context.put("sortColumnIndex", 1);
								} else {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
												TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
												if( tp1.getCourses().size() < tp2.getCourses().size() ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 1);
									context.put("sortColumnIndex", 1);
								}
							// sorting against number of learners	
							} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
								if( sortDirection.equalsIgnoreCase("0") ) {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												int ln1 =  (Integer) trainingPlanMapList.get(i).get("learnerNumber");
												int ln2 =  (Integer) trainingPlanMapList.get(j).get("learnerNumber");
												if( ln1 > ln2 ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 0);
									context.put("sortColumnIndex", 2);
								} else {
									for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
										for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
											if( i != j ) {
												int ln1 =  (Integer) trainingPlanMapList.get(i).get("learnerNumber");
												int ln2 =  (Integer) trainingPlanMapList.get(j).get("learnerNumber");
												if( ln1 < ln2 ) {
													Map<String,Object> tempMap = trainingPlanMapList.get(i);
													trainingPlanMapList.set(i, trainingPlanMapList.get(j));
													trainingPlanMapList.set(j, tempMap);
												}
											}
										}
									}
									context.put("sortDirection", 1);
									context.put("sortColumnIndex", 2);
								}
							}
						}
						/*************/
						
						context.put("listOfTrainingPlans", trainingPlanMapList);
					}
					context.put("simpleSearchCriteria", searchCriteria);
					
				} else if( action.equalsIgnoreCase(MANAGE_TRAININGPLAN_SEARCH_ACTION) ) {	
					log.info("Search for training plans");
					searchTrainingPlans(request, context, customer, sortColumnIndex, sortDirection);
				}
			}	else{
				request.setAttribute("newPage","true");
			}
			return new ModelAndView(searchTrainingPlanTemplate,"context",context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchTrainingPlanTemplate);
	}

	private void searchTrainingPlans(HttpServletRequest request, Map<Object, Object> context, Customer customer, 
			String sortColumnIndex, String sortDirection){
		String searchStr = null;
		if( request.getParameter("simpleSearchCriteria") != null ) {
			session.setAttribute("simpleSearchCriteria",request.getParameter("simpleSearchCriteria"));
			searchStr = request.getParameter("simpleSearchCriteria");
		} else {
			searchStr = (String)session.getAttribute("simpleSearchCriteria");
		}
		context.put("simpleSearchcriteria", request.getParameter("simpleSearchCriteria"));
		
		if( customer != null ) {
			List<Map<String,Object>> trainingPlanMapList = new ArrayList <Map<String,Object>>();
			List<TrainingPlan> listOfTrainingPlans = trainingPlanService.findTrainingPlanByName(searchStr, customer);
			
			Long [] arrTrainingPlanIds = new Long[listOfTrainingPlans.size()];
			for(int i=0;i<listOfTrainingPlans.size(); i++){
				arrTrainingPlanIds[i] = listOfTrainingPlans.get(i).getId();
			}
			
			List<Object[]> arrLearnerCount = null;
			if(arrTrainingPlanIds.length>0){
				arrLearnerCount= trainingPlanService.countLearnerByTrainingPlan(arrTrainingPlanIds);
			}
			
			for(TrainingPlan trainingPlan:listOfTrainingPlans) {
				
				trainingPlan.setEntitledCourseCount(entitlementService.getEntitledCoursesFromTrainingPlanCourses(trainingPlan.getCourses(), 9999).size());
				String LearnerCount = "0";
				int indexTRAINIINGPLAN_ID = 0;
				int indexLEARNERCOUNT = 1;
				if(arrLearnerCount!=null && !arrLearnerCount.isEmpty() ){
					for (Object[] objLearnerCoubnt : arrLearnerCount) {
						Long TRAINIINGPLAN_ID = Long.valueOf(objLearnerCoubnt[indexTRAINIINGPLAN_ID].toString());
						if(TRAINIINGPLAN_ID.longValue() == trainingPlan.getId().longValue()){
							LearnerCount = objLearnerCoubnt[indexLEARNERCOUNT].toString();
						}
					}	
				}
				
				Map<String,Object> trPlanMap = new HashMap <String,Object>();
				trPlanMap.put("tPlan", trainingPlan);
				trPlanMap.put("learnerNumber", LearnerCount);
				trainingPlanMapList.add(trPlanMap);
			}
			
			
			if(sortColumnIndex==null)
				sortColumnIndex="0";
			if(sortDirection==null)
				sortDirection="0";
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			
			// manually sorting
			if( StringUtils.isNotBlank(sortDirection)&& StringUtils.isNotBlank(sortColumnIndex) ) {
				// sorting against training plan name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
									TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
									if( tp1.getName().compareTo(tp2.getName()) > 0 ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
									TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
									if( tp1.getName().compareTo(tp2.getName()) < 0 ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
				// sorting against number of courses	
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
									TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
									if( tp1.getCourses().size() > tp2.getCourses().size() ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									TrainingPlan tp1 =  (TrainingPlan) trainingPlanMapList.get(i).get("tPlan");
									TrainingPlan tp2 =  (TrainingPlan) trainingPlanMapList.get(j).get("tPlan");
									if( tp1.getCourses().size() < tp2.getCourses().size() ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
				// sorting against number of learners	
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									int ln1 =  (Integer) trainingPlanMapList.get(i).get("learnerNumber");
									int ln2 =  (Integer) trainingPlanMapList.get(j).get("learnerNumber");
									if( ln1 > ln2 ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 2);
					} else {
						for( int i = 0 ; i < trainingPlanMapList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanMapList.size() ; j ++ ) {
								if( i != j ) {
									int ln1 =  (Integer) trainingPlanMapList.get(i).get("learnerNumber");
									int ln2 =  (Integer) trainingPlanMapList.get(j).get("learnerNumber");
									if( ln1 < ln2 ) {
										Map<String,Object> tempMap = trainingPlanMapList.get(i);
										trainingPlanMapList.set(i, trainingPlanMapList.get(j));
										trainingPlanMapList.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 2);
					}
				}
			}
			context.put("listOfTrainingPlans", trainingPlanMapList);			
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 *  getters & setters
	 */
	public String getSearchTrainingPlanTemplate() {
		return searchTrainingPlanTemplate;
	}

	public void setSearchTrainingPlanTemplate(String searchTrainingPlanTemplate) {
		this.searchTrainingPlanTemplate = searchTrainingPlanTemplate;
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

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}