package com.softech.vu360.lms.web.controller.administrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.TreeNode;

/**
 * @author arijit dutta
 *
 */
public class UpdateEntitlementDetailsController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(UpdateEntitlementDetailsController.class.getName());

	private String updateEntitlementDetailsTemplate = null;
	private String searchEntitlementTemplate = null;
	private String viewCustomerEntitlementTemplate = null;
	private String searchAndAddCourseTemplate = null;
	private String addEntitlementDetailsTemplate = null;
	private String searchAndAddCourseGroupTemplate = null;
	private String removeCustomerEntitlementsTemplate = null;
	private String closeTemplate = null;
	private String failureTemplate = null;

	private static final String MANAGE_ENTITLEMENT_SAVE_ACTION = "Save";
	private static final String MANAGE_ENTITLEMENT_ADDCOURSE_ACTION = "addCourseInEntitlement";
	private static final String REMOVE_ENTITLEMENT_ACTION = "removeEntitlement";
	private static final String MOVE_ENROLLEMNT_ACTION = "moveEnrollments";
	private static final String DROP_ENROLLEMNT_ACTION = "dropEnrollments";
	
	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;
	private CustomerService customerService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private OrgGroupLearnerGroupService groupService = null;
	private EnrollmentService enrollmentService=null;
	HttpSession session;


	/**
	 * @author muzammil.shaikh
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView viewEntitlementsList(HttpServletRequest request, HttpServletResponse response) {
		
		try {
				//-- Verifing that Customer must be selected ----START----
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					if( details.getCurrentCustomer() == null || details.getCurrentSearchType() != AdminSearchType.CUSTOMER){
						return new ModelAndView(failureTemplate,"isRedirect","c");
					}
				} else {
					// admin has not selected any customer
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
				//-- Verifing that Customer must be selected ----END----
			
			String action=request.getParameter("action");
			if(StringUtils.isNotBlank(action) && action.equalsIgnoreCase(REMOVE_ENTITLEMENT_ACTION)) {
				
				String entitlementIds = request.getParameter("entitlementIds");
				if ( StringUtils.isNotBlank( entitlementIds ) ) {
					Long contractId = Long.parseLong(entitlementIds);
					
					Map<String, Object> context = new HashMap<String, Object>();
					
					//Get Contract Details for selected contractID
					CustomerEntitlement selectedContract = entitlementService.getCustomerEntitlementById(contractId);					
					if ( selectedContract != null && !selectedContract.isSystemManaged()) {
						
						List<CustomerEntitlement> similarContractList = new ArrayList<CustomerEntitlement>();
						
						//Get Enrollment Count for this contract
						Long totalEnrollments = entitlementService.getEnrollmentCountByCustomerEntitlementId(contractId);
						if ( totalEnrollments > 0 ) {
							
							// Find Contracts with same contract Items as this one
							similarContractList = this.entitlementService.getSimilarCustomerEntitlements(contractId, totalEnrollments);
							
							// Display Move/Drop Enrollment options
							context.put("contractId", contractId);
							context.put("selectedContract", selectedContract);
							context.put("similarContractList", similarContractList);
							
							return new ModelAndView(removeCustomerEntitlementsTemplate, "context", context);
						}
						
						// Delete Contract without asking user any other thing
						this.entitlementService.deleteCustomerEntitlement(selectedContract);
					}
				}
			}
			
			// Search Contracts to display as default action
			return searchEntitlement(request, response);
		}
		catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		return new ModelAndView("searchEntitlementTemplate");
	}

	public ModelAndView removeEntitlements(HttpServletRequest request, HttpServletResponse response) {

		try {
			String entitlementIds = request.getParameter("entitlementIds");
			String courseEntitlementForCourses = request.getParameter("courseEntitlementForCourses");
			String action = request.getParameter("removeContract");
			
			if (StringUtils.isNotBlank(entitlementIds) && StringUtils.isNotBlank(action)) {
				Long sourceContractId = Long.parseLong(entitlementIds);
				CustomerEntitlement sourceContract = this.entitlementService.getCustomerEntitlementById(sourceContractId);
				Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
				
				if (action.equalsIgnoreCase(MOVE_ENROLLEMNT_ACTION) && StringUtils.isNotBlank(courseEntitlementForCourses) ) {
					Long destinationContractId = Long.parseLong(courseEntitlementForCourses);
					
					// Move Enrollments to selected Contract 
					this.entitlementService.moveCustomerEnrollments(customer, sourceContractId, destinationContractId);
					
					// Delete Source Contract
					this.entitlementService.deleteCustomerEntitlement(sourceContract);
				}
				// For Drop Enrollments
				else if (action.equalsIgnoreCase(DROP_ENROLLEMNT_ACTION)) {
					this.entitlementService.removeEntitlementsWithEnrollments(customer, sourceContract);
				}
				
			}
			// Search Contracts to display as default action
			return searchEntitlement(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("searchEntitlementTemplate");
	}

	/**
	 * last update by Owais Majid Khan
	 * this method show customer Entitlement
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView searchEntitlement(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			session = request.getSession();		


			/***********OWS Code for Sorting****************/


			Map<Object, Object> context = new HashMap<Object, Object>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null ){
					customer = details.getCurrentCustomer();
					//List<CustomerEntitlement>  listCustomerEntitlements = entitlementService.getAllCustomerEntitlements(customer);
					//context.put("CustomerEntitlementsList", listCustomerEntitlements);
					context.put("selectedCustomer", customer);

					session = request.getSession();
					String sortDirection = request.getParameter("sortDirection");
//					if( sortDirection == null && session.getAttribute("sortDirection") != null )
//						sortDirection = session.getAttribute("sortDirection").toString();
					String sortColumnIndex = request.getParameter("sortColumnIndex");
//					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();

					String showAll= request.getParameter("showAll");
					if (showAll==null)showAll="false";


					//Map<Object, Object> context = new HashMap<Object, Object>();

					List<CustomerEntitlement> entitlements = entitlementService.getAllCustomerEntitlementsForGrid(
							((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().
									getDetails()).getCurrentCustomer());


					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
					context.put("showAll", showAll);
					String pageIndex = request.getParameter("pageCurrIndex");
					if( pageIndex == null ) pageIndex = "0";
					log.debug("pageIndex = " + pageIndex);
					Map PagerAttributeMap = new HashMap();
					PagerAttributeMap.put("pageCurrIndex",pageIndex);



					// manually sorting
					if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {

						request.setAttribute("PagerAttributeMap", PagerAttributeMap);
						// sorting against entitlement name
						request.setAttribute("PagerAttributeMap", PagerAttributeMap);

						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											CustomerEntitlement ce1 =  entitlements.get(i);
											CustomerEntitlement ce2 =  entitlements.get(j);
											if( StringUtils.isNotBlank(ce1.getName()) && StringUtils.isNotBlank(ce2.getName()) ) { 
												if(	ce1.getName().compareTo(ce2.getName()) > 0 ) {
													CustomerEntitlement tempMap = entitlements.get(i);
													entitlements.set(i, entitlements.get(j));
													entitlements.set(j, tempMap);
												}
											}
										}
									}
								}
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 0);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 0);
							} else {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											CustomerEntitlement ce1 =  entitlements.get(i);
											CustomerEntitlement ce2 =  entitlements.get(j);
											if( StringUtils.isNotBlank(ce1.getName())&& StringUtils.isNotBlank(ce2.getName()) ) {
												if( ce1.getName().compareTo(ce2.getName()) < 0 ) {
													CustomerEntitlement tempMap = entitlements.get(i);
													entitlements.set(i, entitlements.get(j));
													entitlements.set(j, tempMap);
												}
											}
										}
									}
								}
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 0);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 0);
							}

						}
						//Sorting against contract type
						else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											CustomerEntitlement ce1 =  entitlements.get(i);
											CustomerEntitlement ce2 =  entitlements.get(j);
											if( StringUtils.isNotBlank(ce1.getEnrollmentType()) && StringUtils.isNotBlank(ce2.getEnrollmentType()) ) { 
												if(	ce1.getEnrollmentType().compareTo(ce2.getEnrollmentType()) > 0 ) {
													CustomerEntitlement tempMap = entitlements.get(i);
													entitlements.set(i, entitlements.get(j));
													entitlements.set(j, tempMap);
												}
											}
										}
									}
								}
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 1);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 1);
							} else {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											CustomerEntitlement ce1 =  entitlements.get(i);
											CustomerEntitlement ce2 =  entitlements.get(j);
											if( StringUtils.isNotBlank(ce1.getEnrollmentType())&& StringUtils.isNotBlank(ce2.getEnrollmentType()) ) {
												if( ce1.getEnrollmentType().compareTo(ce2.getEnrollmentType()) < 0 ) {
													CustomerEntitlement tempMap = entitlements.get(i);
													entitlements.set(i, entitlements.get(j));
													entitlements.set(j, tempMap);
												}
											}
										}
									}
								}
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 1);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against number of max enrollments
						}


						else if( sortColumnIndex.equalsIgnoreCase("2") ) {

							// breaking the list into 2 sublists - having unlimited or limited enrollments...
							// if entitlement has unlimited enrollments -
							List<CustomerEntitlement> entsUnlimited = new ArrayList <CustomerEntitlement>();
							for( int i = 0 ; i < entitlements.size() ;) {
								if( entitlements.get(i).isAllowUnlimitedEnrollments() ) {
									CustomerEntitlement tempMap = entitlements.remove(i);
									entsUnlimited.add(tempMap);
								} else {
									i ++ ;
								}
							}
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getMaxNumberSeats();
											int count2 =  entitlements.get(j).getMaxNumberSeats();
											if( count1 > count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// merging two lists
								entitlements.addAll(0, entsUnlimited);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 2);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 2);
							} else {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getMaxNumberSeats();
											int count2 =  entitlements.get(j).getMaxNumberSeats();
											if( count1 < count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// merging two lists...
								entitlements.addAll(entsUnlimited);
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 2);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 2);
							}
							// sorting against number of seat used	
						} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getNumberSeatsUsed();
											int count2 =  entitlements.get(j).getNumberSeatsUsed();
											if( count1 > count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 3);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 3);
							} else {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getNumberSeatsUsed();
											int count2 =  entitlements.get(j).getNumberSeatsUsed();
											if( count1 < count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 3);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 3);
							}






						} //Sorting against Seats Remaining
						else if( sortColumnIndex.equalsIgnoreCase("4") ) {

							// breaking the list into 2 sublists - having unlimited or limited enrollments...
							// if entitlement has unlimited enrollments -
							List<CustomerEntitlement> entsUnlimited = new ArrayList <CustomerEntitlement>();
							for( int i = 0 ; i < entitlements.size() ;) {
								if( entitlements.get(i).isAllowUnlimitedEnrollments() ) {
									CustomerEntitlement tempMap = entitlements.remove(i);
									entsUnlimited.add(tempMap);
								} else {
									i ++ ;
								}
							}
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getRemainingSeats();
											int count2 =  entitlements.get(j).getRemainingSeats();
											if( count1 > count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// merging two lists
								entitlements.addAll(0, entsUnlimited);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 4);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 4);
							} else {
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  entitlements.get(i).getRemainingSeats();
											int count2 =  entitlements.get(j).getRemainingSeats();
											if( count1 < count2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// merging two lists...
								entitlements.addAll(entsUnlimited);
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 4);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 4);
							}
							// sorting against number of seat used	
						}



						// sorting against expiration date	
						else if( sortColumnIndex.equalsIgnoreCase("5") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								// breaking the list into 2 sublists - having end date & having terms of service...
								// if entitlement has end date -
								List<CustomerEntitlement> entsHavingEndDate = new ArrayList <CustomerEntitlement>();

								for( int i = 0 ; i < entitlements.size() ; ) {
									if( entitlements.get(i).getEndDate() != null ) {
										CustomerEntitlement tempMap = entitlements.remove(i);
										entsHavingEndDate.add(tempMap);
									} else {
										i ++ ;
									}
								}
								// sorting against terms of service
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int days1 =  entitlements.get(i).getDefaultTermOfServiceInDays();
											int days2 =  entitlements.get(j).getDefaultTermOfServiceInDays();
											if( days1 > days2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// sorting against end date
								for( int i = 0 ; i < entsHavingEndDate.size() ; i ++ ) {
									for( int j = 0 ; j < entsHavingEndDate.size() ; j ++ ) {
										if( i != j ) {
											Date date1 =  entsHavingEndDate.get(i).getEndDate();
											Date date2 =  entsHavingEndDate.get(j).getEndDate();
											if( date1.after(date2) ) {
												CustomerEntitlement tempMap = entsHavingEndDate.get(i);
												entsHavingEndDate.set(i, entsHavingEndDate.get(j));
												entsHavingEndDate.set(j, tempMap);
											}
										}
									}
								}
								// now merging 2 sublists...
								entitlements.addAll(entsHavingEndDate);

								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 5);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 5);
							} else {
								// if entitlement has end date -
								List<CustomerEntitlement> entsHavingEndDate = new ArrayList <CustomerEntitlement>();

								for( int i = 0 ; i < entitlements.size() ; ) {
									if( entitlements.get(i).getEndDate() != null ) {
										CustomerEntitlement tempMap = entitlements.remove(i);
										entsHavingEndDate.add(tempMap);
									} else {
										i ++ ;
									}
								}
								// sorting against terms of service
								for( int i = 0 ; i < entitlements.size() ; i ++ ) {
									for( int j = 0 ; j < entitlements.size() ; j ++ ) {
										if( i != j ) {
											int days1 =  entitlements.get(i).getDefaultTermOfServiceInDays();
											int days2 =  entitlements.get(j).getDefaultTermOfServiceInDays();
											if( days1 < days2 ) {
												CustomerEntitlement tempMap = entitlements.get(i);
												entitlements.set(i, entitlements.get(j));
												entitlements.set(j, tempMap);
											}
										}
									}
								}
								// sorting against end date
								for( int i = 0 ; i < entsHavingEndDate.size() ; i ++ ) {
									for( int j = 0 ; j < entsHavingEndDate.size() ; j ++ ) {
										if( i != j ) {
											Date date1 =  entsHavingEndDate.get(i).getEndDate();
											Date date2 =  entsHavingEndDate.get(j).getEndDate();
											if( date1.before(date2) ) {
												CustomerEntitlement tempMap = entsHavingEndDate.get(i);
												entsHavingEndDate.set(i, entsHavingEndDate.get(j));
												entsHavingEndDate.set(j, tempMap);
											}
										}
									}
								}
								// now merging 2 sublists...
								entitlements.addAll(entsHavingEndDate);

								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 5);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 5);
							}
						}
					}



					context.put("CustomerEntitlementsList", entitlements);

					return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
				}

				/*********End Sorting Code*********************/





			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchEntitlementTemplate);
	}

	private Map<DistributorEntitlement, List<Course>> filterCourses(List<DistributorEntitlement> distributorEntitlements,List<Course> courses){

		Map<DistributorEntitlement, List<Course>> mapdistributorEntitlement = new LinkedHashMap<DistributorEntitlement, List<Course>>();
		List<Course> filteredCourses= new ArrayList<Course>();
		for(DistributorEntitlement distributorEntitlement: distributorEntitlements){

			for (CourseGroup courseGroup1:distributorEntitlement.getCourseGroups()){
				//TODO will implement after new DAO written comment on 02/04/09.
				/*for (Course course:courses){
					for (CourseGroup courseGroup:course.getCourseGroups()){
						if (courseGroup.getId()== courseGroup1.getId()){
							filteredCourses.add(course);
							break;
						}
					}
				}*/
				//TODO will implement after new DAO written comment on 02/04/09.
			}
			mapdistributorEntitlement.put(distributorEntitlement, filteredCourses);
		}

		return mapdistributorEntitlement;

	}

	public ModelAndView addCourseAndCourseGroup(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			List<DistributorEntitlement> distributorEntitlements= new ArrayList<DistributorEntitlement>();//TODO will implement after new DAO written comment on 02/04/09.
			//TODO will implement after new DAO written comment on 02/04/09.
			session = request.getSession();
			List<Course> courseList = null;
			Map<Object, Object> context = new HashMap<Object, Object>();

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){

				if( details.getCurrentCustomer() == null ){
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			} else {
				// admin has not selected any customer
				return new ModelAndView(failureTemplate,"isRedirect","c");
			}

			String entitlementType = request.getParameter("entitlementType");
			String action = request.getParameter("action");
			if (entitlementType ==null){
				return new ModelAndView(closeTemplate);
			}
			if (action==null){
				if(entitlementType.equalsIgnoreCase("course")){
					int count=0;
					for (DistributorEntitlement distributorEntitlement:distributorEntitlements){
						count=count+ distributorEntitlement.getCourseGroups().size();
					}
					long[] courseGroupIdArray = new long[count];
					count=0;
					//TODO will implement after new DAO written comment on 02/04/09.
					/*for(int loop1=0;loop1<customer.getDistributor().getDistributorEntitlements().size();loop1++){
						DistributorEntitlement distributorEntitlement = customer.getDistributor().getDistributorEntitlements().get(loop1);
						for(int loop2=0;loop2<distributorEntitlement.getCourseGroups().size();loop2++,count++){
							courseGroupIdArray[count] = distributorEntitlement.getCourseGroups().get(loop2).getId().longValue();
							//count=count+1;
						}
					}*/
					courseList = entitlementService.findCoursesByDistributor(courseGroupIdArray, "", "", "", "");

					context.put("entitlementType",entitlementType);
					context.put("distributorEntitlements",filterCourses(distributorEntitlements,courseList));
					return new ModelAndView(searchAndAddCourseTemplate,"context", context);
				}else if(entitlementType.equalsIgnoreCase("courseGroup")){
					context.put("entitlementType",entitlementType);
					context.put("distributorEntitlements",distributorEntitlements);
					return new ModelAndView(searchAndAddCourseTemplate,"context", context);
				} else{
					return new ModelAndView(closeTemplate);
				}
			}
			else if( action.equalsIgnoreCase(MANAGE_ENTITLEMENT_ADDCOURSE_ACTION)){
				String[] selectedCoursesId = request.getParameterValues("selectedCourse");
				session.removeAttribute("listOfCourses");
				session.removeAttribute("listOfCourseGroups");

				if(selectedCoursesId != null){
					if(entitlementType.equalsIgnoreCase("course")){
						List<Course> selectedCourses= new ArrayList<Course>();
						for(int loop=0;loop<selectedCoursesId.length;loop++){
							/*Course selectedCourse= new Course();
							selectedCourse.setId(Long.valueOf(selectedCoursesId[loop]));*/
							Course selectedCourse= courseAndCourseGroupService.getCourseById(Long.valueOf(selectedCoursesId[loop]));
							selectedCourses.add(selectedCourse);
						}
						//TODO Why we keeping the list in session, refactor required, should not be in session over head in replication 
						session.setAttribute("listOfCourses",selectedCourses);
						return new ModelAndView(searchAndAddCourseTemplate,"context", context);
					}else if(entitlementType.equalsIgnoreCase("courseGroup")){
						List<CourseGroup> selectedCourseGroups= new ArrayList<CourseGroup>();
						for(int loop=0;loop<selectedCoursesId.length;loop++){
							CourseGroup selectedCourseGroup= new CourseGroup();
							selectedCourseGroup.setId(Long.valueOf(selectedCoursesId[loop]));
							selectedCourseGroups.add(selectedCourseGroup);
						}
						//TODO Why we keeping the list in session, refactor required, should not be in session over head in replication
						session.setAttribute("listOfCourseGroups",selectedCourseGroups);
						return new ModelAndView(searchAndAddCourseTemplate,"context", context);
					}

				}
				else{
					//need validation
				}
			}



			return new ModelAndView(searchAndAddCourseTemplate,"context", context);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchAndAddCourseTemplate);
	}


	public ModelAndView searchAndAddCourseGroups(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String searchType = request.getParameter("searchType");
			String courseAction = request.getParameter("courseAction");
			if(searchType == null){
				context.put("courseList", null);
				return new ModelAndView(searchAndAddCourseGroupTemplate);
			}else {
				searchType = request.getParameter("searchType");
				//Customer customer = user.getLearner().getCustomer();//can be changed when distributorentitlement is ready
				//TODO will implement after new DAO written comment on 02/04/09.
				/*long[] courseGroupIdArray = new long[customer.getDistributor().getDistributorEntitlements().get(0).getCourseGroups().size()];//hardcoded for this time
				int count = 0;
				for(int loop1=0;loop1<customer.getDistributor().getDistributorEntitlements().size();loop1++){
					DistributorEntitlement distributorEntitlement = customer.getDistributor().getDistributorEntitlements().get(loop1);
					for(int loop2=0;loop2<distributorEntitlement.getCourseGroups().size();loop2++,count++){
						courseGroupIdArray[count] = distributorEntitlement.getCourseGroups().get(loop2).getId().longValue();
					}
				}	
				if(searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION)){
					//if(((String)session.getAttribute("courseRadioSelected")).equalsIgnoreCase("courseGroup")){
					courseGroupList = entitlementService.findCourseGroupsByDistributor(courseGroupIdArray, request.getParameter("simpleSearchCriteria"));
					context.put("courseList", courseGroupList);
					context.put("typeOfSearch","simplesearch");
					//}
				}*/
			}
			List<Course> courseGroups = new ArrayList();
			if(courseAction != null && courseAction.equalsIgnoreCase(MANAGE_ENTITLEMENT_ADDCOURSE_ACTION)){
				String[] selectedCourses = request.getParameterValues("selectedCourse");
				if(selectedCourses != null){
					//if(((String)session.getAttribute("courseRadioSelected")).equalsIgnoreCase("courseGroup")){
					for(int loop=0;loop<selectedCourses.length;loop++){
						courseGroups.add(courseAndCourseGroupService.getCourseById(Long.valueOf(selectedCourses[loop])));
					}
					session.setAttribute("listOfCoursesOrCourseGroups",courseGroups);
					//}
				}
			}
			return new ModelAndView(searchAndAddCourseGroupTemplate,"context", context);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchAndAddCourseGroupTemplate);
	}


	private Long[] transformStringArrayToLong(String[] strEntitlementIds) {
		Long []longArray=new Long[strEntitlementIds.length];
		for(int i=0;i<longArray.length;i++){
			longArray[i]=Long.parseLong(strEntitlementIds[i]);
		}

		return longArray;
	}


	/**
	 * added by arijit
	 * this method update Entitlement Details
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView updateEntitlement(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				CustomerEntitlement customerEntitlement = entitlementService.loadForUpdateCustomerEntitlement(Long.valueOf((String)request.getParameter("customerEntitlementId")));
				if(action.equalsIgnoreCase(MANAGE_ENTITLEMENT_SAVE_ACTION)){
					if(!validate(request, context)){
						String[] selectedOrgGroupValues = request.getParameterValues("groups");
						List<OrgGroupEntitlement> orgGroupEntitlements =null; 
						List<OrgGroupEntitlement> modOrgGroupEntitlements =new ArrayList<OrgGroupEntitlement>(); 
						orgGroupEntitlements=entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
						int sumMaxEnrollments = 0;
						//outer loop traverses all organizational group 
						if( selectedOrgGroupValues != null ){
							for(int loop1=0;loop1<selectedOrgGroupValues.length;loop1++){
								int flagOldOrgGroupEntitlement = 0;
								String orgGroupID = selectedOrgGroupValues[loop1];
								OrganizationalGroup orgGroup = null;
								if( StringUtils.isNotBlank(orgGroupID) ) {
									orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
								}
								//inner loop traverses all organizational group entitlement
								for(int loop2=0;loop2<orgGroupEntitlements.size();loop2++){
									OrgGroupEntitlement oge=entitlementService.getOrgGroupEntitlementById(orgGroupEntitlements.get(loop2).getId());
									/*	oge.setAllowSelfEnrollment(orgGroupEntitlements.get(loop2).isAllowSelfEnrollment());
									oge.setAllowUnlimitedEnrollments(orgGroupEntitlements.get(loop2).isAllowUnlimitedEnrollments());
									oge.setCustomerEntitlement(orgGroupEntitlements.get(loop2).getCustomerEntitlement());
									oge.setDefaultTermOfServiceInDays(orgGroupEntitlements.get(loop2).getDefaultTermOfServiceInDays());
									oge.setEndDate(orgGroupEntitlements.get(loop2).getEndDate());
									oge.setId(oge.getId());

									oge.setNumberSeatsUsed(orgGroupEntitlements.get(loop2).getNumberSeatsUsed());
									oge.setOrganizationalGroup(orgGroupEntitlements.get(loop2).getOrganizationalGroup());
									oge.setStartDate(orgGroupEntitlements.get(loop2).getStartDate());*/
									if(orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == orgGroup.getId().longValue()){
										if(request.getParameter(orgGroup.getName()).isEmpty()){
											oge.setMaxNumberSeats(0);
											//orgGroupEntitlements.get(loop2).setMaxNumberSeats(0);
										}else{
											//orgGroupEntitlements.get(loop2).setMaxNumberSeats(Integer.parseInt(request.getParameter(orgGroup.getName())));
											oge.setMaxNumberSeats(Integer.parseInt(request.getParameter(orgGroup.getName())));
											sumMaxEnrollments = sumMaxEnrollments + orgGroupEntitlements.get(loop2).getMaxNumberSeats();
										}
										//orgGroupEntitlements.get(loop2).setCustomerEntitlement(customerEntitlement);
										flagOldOrgGroupEntitlement = 1;
										modOrgGroupEntitlements.add(oge);
										//	break;
									}
								}
								//for new OrganizationalGroupEntitlement 
								if(flagOldOrgGroupEntitlement == 0){
									OrgGroupEntitlement newOrgGroupEntitlement = new OrgGroupEntitlement();
									if(request.getParameter(orgGroup.getName()).isEmpty()){
										newOrgGroupEntitlement.setMaxNumberSeats(0);
									}else{
										newOrgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(request.getParameter(orgGroup.getName())));
										sumMaxEnrollments = sumMaxEnrollments + newOrgGroupEntitlement.getMaxNumberSeats();
									}
									newOrgGroupEntitlement.setOrganizationalGroup(orgGroup);
									if(customerEntitlement.isAllowSelfEnrollment())
										newOrgGroupEntitlement.setAllowSelfEnrollment(true);
									else
										newOrgGroupEntitlement.setAllowSelfEnrollment(false);
									newOrgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
									//orgGroupEntitlements.add(newOrgGroupEntitlement);
									modOrgGroupEntitlements.add(newOrgGroupEntitlement);

								}
							}
						}
						//editing or setting of customerEntitlement object
						customerEntitlement.setName(request.getParameter("entitlementName"));
						customerEntitlement.setAllowSelfEnrollment(new Boolean(request.getParameter("allowSelfEnrollment")));
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
						if (!request.getParameter("startDate").isEmpty()){
							Date myDate = formatter.parse(request.getParameter("startDate"));
							customerEntitlement.setStartDate(myDate);
						}else{
							customerEntitlement.setStartDate(null);
						}	
						if(request.getParameter("termsOfServicesRadio") != null){
							if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days")){
								customerEntitlement.setEndDate(null);
								customerEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(request.getParameter("termsOfServices")));
							}else if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("date")){
								if (!request.getParameter("endDate").isEmpty()){
									Date myDate = formatter.parse(request.getParameter("endDate"));
									customerEntitlement.setEndDate(myDate);
									customerEntitlement.setDefaultTermOfServiceInDays(0);
								}else{
									customerEntitlement.setEndDate(null);
									customerEntitlement.setDefaultTermOfServiceInDays(0);
								}
							}
						}
						if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited")){
							customerEntitlement.setAllowUnlimitedEnrollments(new Boolean("false"));
							customerEntitlement.setMaxNumberSeats(Integer.parseInt(request.getParameter("maximumEnrollments")));
						}else if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("unlimited")){
							customerEntitlement.setAllowUnlimitedEnrollments(new Boolean("true"));
							customerEntitlement.setMaxNumberSeats(0);
						}
						customerEntitlement.setCustomer(customerEntitlement.getCustomer());//it is not shown in the form
						if(session.getAttribute("listOfCoursesOrCourseGroups") != null){
							List obj = (List<Object>)session.getAttribute("listOfCoursesOrCourseGroups");
							if(obj.get(0) instanceof Course){
								//customerEntitlement.setCourses(obj);
							}else if(obj.get(0) instanceof CourseGroup){
								//customerEntitlement.setCourseGroups(obj);
							}
						}
						if(customerEntitlement.isAllowUnlimitedEnrollments()){
							entitlementService.saveCustomerEntitlement(customerEntitlement,modOrgGroupEntitlements);
							//	entitlementService.addOrgGroupEntitlementInCustomerEntitlement(customerEntitlement,customerEntitlement.getOrgGroupEntitlements());
						}else{
							if(customerEntitlement.getMaxNumberSeats() < sumMaxEnrollments){
								context.put("errorMsg", new Boolean(true));
							}else{
								context.put("errorMsg", new Boolean(false));
								entitlementService.saveCustomerEntitlement(customerEntitlement,modOrgGroupEntitlements);
								//	entitlementService.addOrgGroupEntitlementInCustomerEntitlement(customerEntitlement,customerEntitlement.getOrgGroupEntitlements());
							}
						}
					}
				}
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			Long customerId = details.getCurrentCustomerId();

			OrganizationalGroup rootOrgGroup =  groupService.getRootOrgGroupForCustomer(customerId);
			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			context.put("orgGroupTreeAsList", treeAsList);
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			String customerEntitlementId = request.getParameter("customerEntitlementId");
			CustomerEntitlement customerEntitlement= entitlementService.getCustomerEntitlementById(Long.valueOf(customerEntitlementId));
			List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
			for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
				OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
				map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
			}
			String startDateString = null;
			String endDateString = null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(customerEntitlement.getEndDate() != null) {
				endDateString = formatter.format(customerEntitlement.getEndDate());
			}
			if(customerEntitlement.getStartDate() != null){
				startDateString = formatter.format(customerEntitlement.getStartDate());
			}
			if(customerEntitlement instanceof CourseGroupCustomerEntitlement){
				context.put("courseGroup", new Boolean("True"));
				context.put("course", new Boolean("False"));
			}
			if(customerEntitlement instanceof CourseCustomerEntitlement){
				context.put("course", new Boolean("True"));
				context.put("courseGroup", new Boolean("False"));
			}
			context.put("customerEntitlement",customerEntitlement);
			context.put("endDate",endDateString);
			context.put("startDate",startDateString);
			context.put("countmap", map1);
			return new ModelAndView(updateEntitlementDetailsTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(updateEntitlementDetailsTemplate);
	}

	@SuppressWarnings("unchecked")
	private boolean validate(HttpServletRequest request,Map context){
		boolean check = false;
		if(StringUtils.isBlank(request.getParameter("entitlementName"))){
			context.put("validateName","error.customerentitlement.validateNameBlank");
			check = true;
		}
		if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited")) { 
			if( StringUtils.isBlank(request.getParameter("maximumEnrollments"))){
				context.put("validateMaximumEnrollments","error.customerentitlement.maximumEnrollment");
				check = true;
			}else {
				try {
					Integer.parseInt(request.getParameter("maximumEnrollments"));

				} catch (Exception e) {
					context.put("validateMaximumEnrollments","error.customerentitlement.maximumEnrollment");
					check = true;
				}
			}
		}
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days")) { 
			if( StringUtils.isBlank(request.getParameter("termsOfServices"))){
				context.put("validateTermsofServices","error.customerentitlement.enterdays");
				check = true;
			}else {
				try {
					Integer.parseInt(request.getParameter("termsOfServices"));

				} catch (Exception e) {
					context.put("validateTermsofServices","error.customerentitlement.enterdays");
					check = true;
				}
			}
		}
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("date") && StringUtils.isBlank(request.getParameter("endDate"))){
			context.put("validateTermsofServicesInDate","error.customerentitlement.enterdate");
			check = true;
		}
		/*if(FieldsValidation.isInValidName(request.getParameter("EntitlementName"))){
			context.put("validateName","Invalid Name");
			check = true;
		}*/
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days") && !request.getParameter("termsOfServices").isEmpty() 
				&& !FieldsValidation.isNumeric(request.getParameter("termsOfServices"))){
			context.put("validateTermsofServicesNumericField","error.customerentitlement.InvalidInput");
			check = true;
		}
		if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited") 
				&& !request.getParameter("maximumEnrollments").isEmpty() &&!FieldsValidation.isNumeric(request.getParameter("maximumEnrollments"))){
			context.put("validateMaximumEnrollmentsNumericField","error.customerentitlement.InvalidInput");
			check = true;
		}

		if(request.getParameter("endDate")!=null && !request.getParameter("endDate").isEmpty()){
			long diffInMilliseconds =(24 * 60 * 60 * 1000);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date newEnrollmentDate = null;
			try{
				newEnrollmentDate=formatter.parse(request.getParameter("endDate"));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}

			Date startDate = null;
			try{
				startDate=formatter.parse(request.getParameter("startDate"));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calender=Calendar.getInstance();
			Date date=calender.getTime();
			if( (  newEnrollmentDate.getTime()-startDate.getTime() )/ diffInMilliseconds < 0){
				context.put("fiexedEndDate", "error.customerentitlement.invalidEndDate");
				check = true;
			}
		}

		CustomerEntitlement customerEntitlement = null;
		String entitlementName = request.getParameter("entitlementName");
		if(request.getParameter("customerEntitlementId") != null){
			customerEntitlement = entitlementService.getCustomerEntitlementById(Long.valueOf((String)request.getParameter("customerEntitlementId")));
			if(!customerEntitlement.getName().equalsIgnoreCase(entitlementName)){
				if(entitlementService.isCustomerEntitlementExistByName(entitlementName)){
					context.put("validateDuplicateName","error.customerentitlement.DuplicateName");
					check = true;
				}
			}
		}else{
			if(entitlementService.isCustomerEntitlementExistByName(entitlementName)){
				context.put("validateDuplicateName","error.customerentitlement.DuplicateName");
				check = true;
			}
		}
		return check;
	}


	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup){

		if(orgGroup!=null){

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i));
			}

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}





	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public String getUpdateEntitlementDetailsTemplate() {
		return updateEntitlementDetailsTemplate;
	}

	public void setUpdateEntitlementDetailsTemplate(
			String updateEntitlementDetailsTemplate) {
		this.updateEntitlementDetailsTemplate = updateEntitlementDetailsTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public String getSearchEntitlementTemplate() {
		return searchEntitlementTemplate;
	}

	public void setSearchEntitlementTemplate(String searchEntitlementTemplate) {
		this.searchEntitlementTemplate = searchEntitlementTemplate;
	}



	public CustomerService getCustomerService() {
		return customerService;
	}



	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}



	public String getViewCustomerEntitlementTemplate() {
		return viewCustomerEntitlementTemplate;
	}



	public void setViewCustomerEntitlementTemplate(
			String viewCustomerEntitlementTemplate) {
		this.viewCustomerEntitlementTemplate = viewCustomerEntitlementTemplate;
	}


	public String getSearchAndAddCourseTemplate() {
		return searchAndAddCourseTemplate;
	}


	public void setSearchAndAddCourseTemplate(String searchAndAddCourseTemplate) {
		this.searchAndAddCourseTemplate = searchAndAddCourseTemplate;
	}


	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}


	public String getAddEntitlementDetailsTemplate() {
		return addEntitlementDetailsTemplate;
	}


	public void setAddEntitlementDetailsTemplate(
			String addEntitlementDetailsTemplate) {
		this.addEntitlementDetailsTemplate = addEntitlementDetailsTemplate;
	}


	public String getFailureTemplate() {
		return failureTemplate;
	}


	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}


	/**
	 * @return the searchAndAddCourseGroupTemplate
	 */
	public String getSearchAndAddCourseGroupTemplate() {
		return searchAndAddCourseGroupTemplate;
	}


	/**
	 * @param searchAndAddCourseGroupTemplate the searchAndAddCourseGroupTemplate to set
	 */
	public void setSearchAndAddCourseGroupTemplate(
			String searchAndAddCourseGroupTemplate) {
		this.searchAndAddCourseGroupTemplate = searchAndAddCourseGroupTemplate;
	}


	public String getCloseTemplate() {
		return closeTemplate;
	}


	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}


	/**
	 * @return the groupService
	 */
	public OrgGroupLearnerGroupService getGroupService() {
		return groupService;
	}


	/**
	 * @param groupService the groupService to set
	 */
	public void setGroupService(OrgGroupLearnerGroupService groupService) {
		this.groupService = groupService;
	}


	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}


	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public String getRemoveCustomerEntitlementsTemplate() {
		return removeCustomerEntitlementsTemplate;
	}

	public void setRemoveCustomerEntitlementsTemplate(
			String removeCustomerEntitlementsTemplate) {
		this.removeCustomerEntitlementsTemplate = removeCustomerEntitlementsTemplate;
	}	

	private String[] createEntitlementIdsArrayFromArrays(int arraySize, String[] array1, String[] array2) {
		String[] entilementsIdsArray = new String[arraySize];
		int i;
		for (i=0;i<array1.length;i++) {
			entilementsIdsArray[i] = array1[i];
		}
		System.out.println("Ent Ids length: " + arraySize);
		System.out.println("i: " + i);
		for (int j=0;j<array2.length;j++,i++) {
			entilementsIdsArray[i] = array2[j];
		}

		return entilementsIdsArray;
	}
	
}	