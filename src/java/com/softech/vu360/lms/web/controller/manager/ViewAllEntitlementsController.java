package com.softech.vu360.lms.web.controller.manager;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * The controller view all entitlements.
 *
 * @author Bishwajit Maitra
 * @date 13-01-09
 *
 */
public class ViewAllEntitlementsController implements Controller {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;

	private String viewAllEntitlementsTemplate = null;

	HttpSession session = null;

	/**
	 *  modified by Dyutiman -
	 *  added pagination & sorting 
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String showAll= request.getParameter("showAll");
			if (showAll==null)showAll="false";

			Map<Object, Object> context = new HashMap<Object, Object>();

			List<CustomerEntitlement> entitlements = entitlementService.getAllCustomerEntitlementsForGrid(
					((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().
							getDetails()).getCurrentCustomer());

			context.put("showAll", showAll);
			context.put("sortDirection", 0);
			context.put("sortColumnIndex", 0);
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			log.debug("pageIndex = " + pageIndex);
			Map PagerAttributeMap = new HashMap();
			PagerAttributeMap.put("pageIndex",pageIndex);

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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 3);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 3);
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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 4);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 4);
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
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 5);
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
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 5);
					}
				}
			}			
			context.put("entitlement", entitlements);

			return new ModelAndView(viewAllEntitlementsTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewAllEntitlementsTemplate);
	}


	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getViewAllEntitlementsTemplate() {
		return viewAllEntitlementsTemplate;
	}

	public void setViewAllEntitlementsTemplate(String viewAllEntitlementsTemplate) {
		this.viewAllEntitlementsTemplate = viewAllEntitlementsTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}