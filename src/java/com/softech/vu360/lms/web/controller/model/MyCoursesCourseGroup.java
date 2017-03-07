package com.softech.vu360.lms.web.controller.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.ArrangeCourseGroupTree;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.TreeNode;
/**
 * 
 * 
 * @author jason
 *
 */
@SuppressWarnings("unchecked")
public class MyCoursesCourseGroup extends HashMap<String, Object>  implements ILMSBaseInterface, Comparable<MyCoursesCourseGroup>  {
	
	public static final int FILTER_IN_PROGRESS   = 0;
	public static final int FILTER_COMPLETED     = 1;
	public static final int FILTER_RECENT        = 2;
	public static final int FILTER_AVAILABLE     = 3;
	public static final int FILTER_ACTIVE        = 4;
	public static final int FILTER_EXPIRED       = 5;
	public static final int FILTER_ALL		     = 6;
	public static final int FILTER_ENROLLED      = 7;
	public static final int FILTER_COURSECATALOG = 8;
	public static final int FILTER_ABOUT_TO_EXPIRE = 9;
	
	private String courseGroupName = StringUtils.EMPTY;
	
	private static final Logger log = Logger.getLogger(MyCoursesCourseGroup.class.getName());	
	// this will be serialized in the session...
	private static final long serialVersionUID = -2449128335007281620L;

	private Map internalMap = new HashMap();
	private Set<MyCoursesItem> currentMyCoursesItems = new HashSet<MyCoursesItem>();
	private Set<MyCoursesItem> allMyCoursesItems = new HashSet<MyCoursesItem>();

	public String getCourseGroupName() {
		return courseGroupName;
	}

	public MyCoursesCourseGroup(CourseGroup courseGroup) {
		internalMap = new HashMap();
		allMyCoursesItems = new HashSet<MyCoursesItem>();
		courseGroupName = courseGroup.getName();
		internalMap.put("courseGroupName", courseGroupName);
		internalMap.put("courseGroupDescription", courseGroup.getDescription());
		internalMap.put("breadCrumb", createBreadCrumb(courseGroup));
	}

	
	
	public String getName() {
		return (String)get("courseGroupName");
	}

	public void addCourse(MyCoursesItem item) {
		allMyCoursesItems.add(item);
	}

	public Set<MyCoursesItem> getAllMyCoursesItems() {
		return allMyCoursesItems;
	}

	/**
	 * @return the currentMyCoursesItems
	 */
	public List<MyCoursesItem> getCurrentMyCoursesItems(String viewType) {
		List list = new ArrayList(currentMyCoursesItems); 
		Collections.sort(list);
		return list;
	}

	public Set<MyCoursesItem> getCurrentMyCoursesItemsSet() {
		return currentMyCoursesItems;
	}

	/**
	 * @param currentMyCoursesItems the currentMyCoursesItems to set
	 */
	public void setCurrentMyCoursesItems(Set<MyCoursesItem> currentMyCoursesItems) {
		this.currentMyCoursesItems = currentMyCoursesItems;
	}

	public Object put(String key, Object obj) {
		return internalMap.put(key, obj);
	}

	public Object get(String key) {
		return internalMap.get(key);
	}

	@Override
	public int compareTo(MyCoursesCourseGroup arg0) {
		if(arg0.getName().contains("Training Plan:"))
		{
			int result=-99999+(this.getName().compareToIgnoreCase(arg0.getName()));

			return result; 
		}
		int result=getName().compareToIgnoreCase(arg0.getName());
		return result;
	}


	public static List<MyCoursesItem> getAllCurrentCourses(Set<MyCoursesCourseGroup> courseGroups) {
		List<MyCoursesItem> results = new ArrayList<MyCoursesItem>();
		if( courseGroups.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : courseGroups) {
				for (MyCoursesItem myCoursesItem : myCoursesCourseGroup.getCurrentMyCoursesItemsSet()) {
					if ( !results.contains(myCoursesItem) ) {
						if(myCoursesItem.get("enrollmentStatus").toString().equalsIgnoreCase(LearnerEnrollment.ACTIVE))
						results.add(myCoursesItem);
					}
				}
			}
		}
		return results;
	}

	public static List<CourseGroupView> generateCourseCatalogView(Long learnerId,List<CustomerEntitlement> customerEntitlements,
			CourseAndCourseGroupService courseAndCourseGroupService, String search) {
		
		HashMap<String,CourseGroupView> resultMap=new HashMap<>();
		String courseGroupName;
		final String MISC = "Miscellaneous";
		CourseGroupView groupView;
		long trainingPlanId = 0;
		
		List<CourseGroupView> megaList = new ArrayList<CourseGroupView>();	
		List<Map<Object, Object>> trainingPlancourses= courseAndCourseGroupService.getTrainingPlansForCourseCatalog(learnerId, customerEntitlements, search);
		List<CourseGroupView> trainingPlans = new ArrayList<CourseGroupView>();
		for(Map map:trainingPlancourses)
		{
			courseGroupName=(String)map.get("TRAININGPLANNAME");// consider training plan as course group for presentation only not really
			if(map.get("TRAININGPLAN_ID") != null)
			{
				trainingPlanId = BigInteger.valueOf(Long.valueOf(map.get("TRAININGPLAN_ID").toString())).longValue();
			}
			groupView=resultMap.get(courseGroupName);// Training Plan name mustn't be null 
			if(groupView==null){
				groupView=new CourseGroupView();
				groupView.setGroupName("TrainingPlan: "+courseGroupName);
				groupView.setTrainingPlanName(courseGroupName);
				groupView.markTrainingPlan(Boolean.TRUE);
				groupView.setId(trainingPlanId);
				resultMap.put(courseGroupName, groupView);
				trainingPlans.add(groupView);
			}
			//groupView.addCourse(map);
		}
		
		List<Map<Object, Object>> courses =courseAndCourseGroupService.getCoursesForCatalogOrderByPCG(learnerId,customerEntitlements,search);
		List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
		Long parentCourseGroupId = null;
		String prevCourseGroupName = "";
		Integer prevCGroupId = -1;
		Long prevParentCGroupId = -1L;
		Integer courseGroupId ;
		for(Map map:courses)
		{
			courseGroupName=map.get("COURSEGROUPNAME").toString();
			if(map.get("PARENTCOURSEGROUP_ID")!=null){
				parentCourseGroupId = BigInteger.valueOf(Long.valueOf(map.get("PARENTCOURSEGROUP_ID").toString())).longValue();
			}
			if(StringUtils.isNotBlank(courseGroupName)){
				groupView = resultMap.get(courseGroupName);
				courseGroupId = BigInteger.valueOf(Long.valueOf(map.get("COURSEGROUP_ID").toString())).intValue();
			}else{
				groupView = resultMap.get(MISC);
				courseGroupName = MISC;
				courseGroupId = Integer.valueOf(-1);
			}
			if(groupView == null || ((parentCourseGroupId!=null && prevParentCGroupId.compareTo(parentCourseGroupId)!=0) 
										|| !prevCourseGroupName.equals(courseGroupName) || prevCGroupId.intValue() != courseGroupId.intValue())){
				groupView = new CourseGroupView();
				groupView.setGroupName(courseGroupName);
				groupView.setId(courseGroupId.longValue());
				if(parentCourseGroupId!=null){
					groupView.setParentCourseGroupId(parentCourseGroupId);
					prevParentCGroupId = parentCourseGroupId;
				}
				
				if(courseGroupName!=null)
					prevCourseGroupName = courseGroupName;
				
				if(courseGroupId!=null)
					prevCGroupId = courseGroupId;
					
								
				resultMap.put(courseGroupName, groupView);
				courseGroups.add(groupView);
			}
			//groupView.addCourse(map);
		}


		//Now we need to retrieve all the course in the Miscellaneous category fmk dec,3rd 2009
		List<Map<Object, Object>> miscCourses 	= courseAndCourseGroupService.getMiscellaneousCoursesForCatalog(learnerId, customerEntitlements, search);
		groupView = new CourseGroupView();
		groupView.setGroupName(MISC);
		groupView.setId(1);
		groupView.markMisc(Boolean.TRUE);
		for(Map map:miscCourses){
			groupView.addCourse(map);
		}
		
		//Collections.sort(courseGroups, comparator);
		megaList.addAll(courseGroups);
		//Now add the Misc Courses to the mega List
		if(miscCourses!=null && !miscCourses.isEmpty())
			megaList.add(groupView);
		//Collections.sort(trainingPlans, comparator);
		megaList.addAll(trainingPlans);
		
		return megaList;
	}
	
	public static Map<Object,Object> generateCourseCatalogView(Long learnerId,List<CustomerEntitlement> customerEntitlements, CourseAndCourseGroupService courseAndCourseGroupService, String search, String courseType, long id) {

		Map<Object,Object> result = new HashMap<Object,Object>();
		Set<CourseGroup> courseGroupsList = new HashSet<CourseGroup>();
		Set<CourseGroup> courseGroupsForEntitlement = new HashSet<CourseGroup>();
		for (CustomerEntitlement customerEntitlement : customerEntitlements) {
			courseGroupsForEntitlement = customerEntitlement.getListOfCourseGroups();
			courseGroupsList.addAll(courseGroupsForEntitlement);
			for (CourseGroup courseGroup : courseGroupsForEntitlement) {
				CourseGroup cg = courseGroup;
				while(courseGroup.getParentCourseGroup() != null)
				{
					log.debug("courseGroup.getParentCourseGroup():::"+courseGroup.getParentCourseGroup().getName()+"\n courseGroup.getName():::"+courseGroup.getName());
					courseGroup = courseGroup.getParentCourseGroup();
				}
				courseGroupsList.add(courseGroup);
				courseGroup=cg;
			}		
		}
		/*for (CustomerEntitlement customerEntitlement : customerEntitlements) {
			courseGroupsList.addAll(customerEntitlement.getListOfCourseGroups());
		}*/
		log.debug("courseGroupsList.size():::"+courseGroupsList.size());
		Map<String,Map<String,Set<CourseGroup>>> levels = new HashMap<String, Map<String,Set<CourseGroup>>>();
		Map<String,Set<CourseGroup>> courseGroups = new HashMap<String, Set<CourseGroup>>();
		List<TreeNode> treeNodesList = getCourseGroupsTree(courseGroupsList);		
		levels = traverseAllTreeNodes(treeNodesList, levels);
		Set<CourseGroup> cgs = new HashSet<CourseGroup>();
		StringBuffer subMenus = new StringBuffer();
		StringBuffer jsFunctions = new StringBuffer();
		Set<String> keySet = levels.keySet();
		TreeSet<String> treeSet = new TreeSet<String>(keySet);
		
		HashMap<String,CourseGroupView> resultMap=new HashMap<String,CourseGroupView>();
		String courseGroupName=null;
		long trainingPlanId, courseGroupId;
		final String  MISC="Miscellaneous";
		CourseGroupView groupView=null;
		List<CourseGroupView> megaList = new ArrayList<CourseGroupView>();	
		List<CourseGroupView> tPlans = new ArrayList<CourseGroupView>();
		List<CourseGroupView> miscs = new ArrayList<CourseGroupView>();
		List<CourseGroupView> cgCourses = new ArrayList<CourseGroupView>();

		if(courseType.equalsIgnoreCase("TP"))
		{
			trainingPlanId = id;
			List<Map<Object, Object>> trainingPlancourses = courseAndCourseGroupService.getTrainingPlanCoursesForCatalogByTrainingPlanId(learnerId, customerEntitlements, trainingPlanId, search);			
			groupView=new CourseGroupView();
			for (Map tp : trainingPlancourses) {
				courseGroupName=(String)tp.get("TRAININGPLANNAME");
				groupView.setGroupName(courseGroupName);
				groupView.setId(((Long)tp.get("TRAININGPLAN_ID")).longValue());
				groupView.setTrainingPlanName(courseGroupName);	
				groupView.addCourse(tp);
				tPlans.add(groupView);
			}
			result.put("courses", tPlans);
		}
		else if(courseType.equalsIgnoreCase("CG"))
		{
			courseGroupId = id;
			List<Map<Object, Object>> courses = courseAndCourseGroupService.getCoursesForCatalogByCourseGroupID(learnerId, customerEntitlements, search, courseGroupId);
			groupView=new CourseGroupView();
			for (Map cg : courses) {
				courseGroupName=(String)cg.get("COURSEGROUPNAME");
				
				groupView.setGroupName(courseGroupName);
				groupView.setId(((Integer)cg.get("COURSEGROUP_ID")).longValue());
				groupView.setTrainingPlanName(courseGroupName);
				groupView.addCourse(cg);
				cgCourses.add(groupView);
			}
			result.put("courses", cgCourses);
		}
		List<Map<Object, Object>> tpc = courseAndCourseGroupService.getTrainingPlansForCourseCatalog(learnerId, customerEntitlements, search);
		
		//List<Map<Object, Object>> courses = courseAndCourseGroupService.getCoursesForCatalog(learnerId,customerEntitlements,search);
		List<Map<Object, Object>> miscCourses = courseAndCourseGroupService.getMiscellaneousCoursesForCatalog(learnerId, customerEntitlements, search);
		
		
		groupView = new CourseGroupView();
		for(Map map:miscCourses)
		{
			
			courseGroupName=(String)map.get("COURSEGROUPNAME");
			groupView=new CourseGroupView();
			groupView.setGroupName(courseGroupName);
			miscs.add(groupView);				
		}
		log.debug("miscellanious courses size :: "+miscs.size());
		result.put("miscCourses", miscs);			 
		megaList.addAll(tPlans);				

		for (String key: treeSet) {
			courseGroups = levels.get(key);
			subMenus.append("<div id=\"level"+key+"\">");
			for (String cgKey: courseGroups.keySet()) {
				cgs = courseGroups.get(cgKey);
				jsFunctions.append("flyoutmenu_l"+key+"(\""+cgKey+"\", \""+key+"_"+cgKey+"\");");
				if(key.equalsIgnoreCase(treeSet.last()))
					jsFunctions.append("flyoutmenuhide_l1(\""+key+"_"+cgKey+"\");");
				subMenus.append("<div class=\"flyout-menu\" id=\""+key+"_"+cgKey+"\" style=\"height: 431px; top: 87px; left: 203px; display: none;\">");
				String parentCgName = cgs.iterator().next().getParentCourseGroup().getName();
				subMenus.append("<div id=\"sidebar-heading-l2\" title=\""+parentCgName+"\">"+(parentCgName.length()>16 ? parentCgName.substring(0,16) : parentCgName)+"</div>");
				subMenus.append(" <div class=\"sc\" style=\"height: 403px;\">");
				subMenus.append("<ul>");
				for (CourseGroup courseGroup : cgs) {
					if(courseGroupsList.contains(courseGroup))
						courseGroupsList.remove(courseGroup);
					subMenus.append("<li id=\""+courseGroup.getId()+"\" title=\""+courseGroup.getName()+"\" onclick=\"javascript:getCoursegroupCourses('"+courseGroup.getId()+"');\">"+(courseGroup.getName().length()>23 ? courseGroup.getName().substring(0,23) : courseGroup.getName())+"</li>  ");
				}
				subMenus.append("</ul></div>  <div class=\"custom-scrollbar\"><div class=\"scroller\"></div></div></div>");
				if(key.equalsIgnoreCase("1"))
				{
					jsFunctions.append("flyoutmenu_l1(\"tp_1\", \"trainingPlans\");");
					subMenus.append("<div class=\"flyout-menu\" id=\"trainingPlans\" style=\"height: 431px; top: 87px; left: 203px; display: none;\">");
					subMenus.append("<div id=\"sidebar-heading-l2\" title=\"Training Plans\">Training Plans</div>");
					subMenus.append(" <div class=\"sc\" style=\"height: 403px;\">");
					subMenus.append("<ul>");
					Set<Long> ids = new HashSet<Long>();
					Long tpID= 0l;
					for (Map tp : tpc) {
						tpID = (Long)tp.get("TRAININGPLAN_ID");
						if(!ids.contains(tpID))
						{
							ids.add(tpID);
							courseGroupName=(String)tp.get("TRAININGPLANNAME");
							subMenus.append("<li id=\""+courseGroupName+"\" title=\""+courseGroupName+"\" onClick=\"javascript:getTrainingPlanCourses('"+tpID.longValue()+"');\" >"+(courseGroupName.length()>23 ? courseGroupName.substring(0,23) : courseGroupName)+"</li>  ");
						}
						
						
					}		
					
					/*for (CourseGroupView trainingPlan : megaList) {
						subMenus.append("<li id=\""+trainingPlan.getGroupName()+"\" title=\""+trainingPlan.getGroupName()+"\" onClick=\"javascript:getTrainingPlanCourses('"+trainingPlan.getId()+"');\" >"+(trainingPlan.getGroupName().length()>23 ? trainingPlan.getGroupName().substring(0,23) : trainingPlan.getGroupName())+"</li>  ");
					}
					*/
					subMenus.append("</ul></div>  <div class=\"custom-scrollbar\"><div class=\"scroller\"></div></div></div>");
				}								
			}
			subMenus.append("</div>");			
		}

		//log.debug("subMenus"+subMenus.toString());
		result.put("subMenus", subMenus.toString());
		result.put("jsFunctions", jsFunctions);
		log.debug("courseGroupsList.size()"+courseGroupsList.size());
		result.put("courseGroupsList", courseGroupsList);
		result.put("trainingPlans", megaList);

		return result;		

	}

	/**
	 * part of our rich domain model where the domain objects understands the business logic
	 * for filtering of MyCoursesItem
	 * 
	 * @param filterType
	 * @param itemsToFilter
	 * @return
	 */
	public static Set<MyCoursesCourseGroup> filterCoursesForMyCourses(int filterType, List<MyCoursesCourseGroup> itemsToFilter) {
		
		log.debug("filterCoursesForMyCourses::START"); 
		Set<MyCoursesCourseGroup> filteredList = null;

		switch (filterType) {

		case FILTER_IN_PROGRESS : filteredList = getInProgressCourses(itemsToFilter);break;

		case FILTER_COMPLETED : filteredList = getCompletedCourses(itemsToFilter);break;

		case FILTER_AVAILABLE : filteredList = getAvailableCourses(itemsToFilter);break;

		case FILTER_ALL : filteredList = getAllCourses(itemsToFilter);break;

		case FILTER_ACTIVE : filteredList = getActiveCourses(itemsToFilter);break;

		case FILTER_EXPIRED : filteredList = getExpiredCourses(itemsToFilter);break;

		case FILTER_ENROLLED : filteredList = getEnrolledCourses(itemsToFilter);break;

		case FILTER_COURSECATALOG : filteredList = getAvailableCourses(itemsToFilter);break;

		case FILTER_ABOUT_TO_EXPIRE : filteredList = getCoursesAboutToExpire(itemsToFilter);break;

		default	:  filteredList = getRecentCourses(itemsToFilter);

		}

		log.debug("filterCoursesForMyCourses::END");
		return filteredList;
	}
	// IN PROGRESS FILTER
	private static Set<MyCoursesCourseGroup> getInProgressCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					// if it meets criteria add to the list
					if ( (StringUtils.isNotBlank(courseStatus)) && ( 
							(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.IN_PROGRESS))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_PENDING))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTING_PENDING))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_DISPUTED))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_RECEIVED))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTED))
							||(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT))
							) ) {
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("active") ) ) {
							itemsList.add(item);
						}
					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}

	private static Set<MyCoursesCourseGroup> getEnrolledCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		log.debug("getEnrolledCourses::START");
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
//		String courseStatus = null;
//		String enrollmentStatus = null;
		log.debug("items to filter:"+itemsToFilter.size());
		LearnerEnrollment le = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					le = (LearnerEnrollment) item.get("enrollment");
					// this is bad but for now since we know when we ask for the enrollments and recent view
					// we only pull enrollments we are okay.
					//LMS-1792: Course not swapped in learner mode. Both swapped and new courses are displayed
					//[LMS-6537] need all enrollments i.e Active and Expired
					log.debug( item.get("courseStatus").toString());
					if(item.get("enrollmentStatus").toString().equalsIgnoreCase(LearnerEnrollment.ACTIVE)) {/* || item.get("enrollmentStatus").toString().equalsIgnoreCase(LearnerEnrollment.EXPIRED)*/
					   if(item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.REPORTED)
							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.COMPLETED)
							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_PENDING)
							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.REPORTING_PENDING)
   							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)
   							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)
   							   || item.get("courseStatus").toString().equalsIgnoreCase(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT)
							   || (le.getEnrollmentEndDate()!=null && !le.getEnrollmentEndDate().before(new Date()))
							   )
						itemsList.add(item);
					}
				}
				
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		log.debug("getEnrolledCourses::END");
		return filteredList;
	}

	// COMPLETED FILTER
	private static Set<MyCoursesCourseGroup> getCompletedCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					// if it meets criteria add to the list
					if ( (StringUtils.isNotBlank(courseStatus)) && 
							( (courseStatus != null && (courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.COMPLETED) || courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTED)) ) ) ) {
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("active") ) ) {
							itemsList.add(item);
						}
					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}

	// AVAILABLE FILTER
	private static Set<MyCoursesCourseGroup> getAvailableCourses(List<MyCoursesCourseGroup> itemsToFilter) {

		long startTime=System.currentTimeMillis();
		log.debug("getAvailableCourses::START");
		Collections.sort(itemsToFilter);
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					// this is bad but for now since we know when we ask for the enrollments and recent view
					// we only pull enrollments we are okay.
					//LMS-1792: Course not swapped in learner mode. Both swapped and new courses are displayed
					log.debug("enrollmentStatus="+item.get("enrollmentStatus"));
					if(item.get("enrollmentStatus")==null || item.get("enrollmentStatus").toString().equals(LearnerEnrollment.EXPIRED))
						
						itemsList.add(item);
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		long endTime=System.currentTimeMillis();
		log.debug("total time taken MyCoursesCourseGroup.getAvailableCourses:"+(endTime-startTime));
		log.debug("getAvailableCourses::END");
		return filteredList;
	}



	// ALL FILTER
	private static Set<MyCoursesCourseGroup> getAllCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				myCoursesCourseGroup.setCurrentMyCoursesItems(myCoursesCourseGroup.getAllMyCoursesItems());
				filteredList.add(myCoursesCourseGroup);
			}
		}
		return filteredList;
	}

	// ACTIVE FILTER
	private static Set<MyCoursesCourseGroup> getActiveCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					// if it meets criteria add to the list
					if ( (StringUtils.isNotBlank(courseStatus)) && 
							(courseStatus.trim().equalsIgnoreCase("inprogress")||courseStatus.trim().equalsIgnoreCase("notstarted") )) {
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("active") ) ) {
							itemsList.add(item);
						}
					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}

	// EXPIRED FILTER
	private static Set<MyCoursesCourseGroup> getExpiredCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					// if it meets criteria add to the list
					if ( (StringUtils.isNotBlank(courseStatus) && !courseStatus.trim().equalsIgnoreCase("completed")
							&& !courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTED)) )
						{
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("expired") ) ||
								(StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("active") ) ) {
							itemsList.add(item);
						}
					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}
	
	// ABOUT TO EXPIRE FILTER
	private static Set<MyCoursesCourseGroup> getCoursesAboutToExpire(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					// if it meets criteria add to the list
					if ( (StringUtils.isNotBlank(courseStatus) && !courseStatus.trim().equalsIgnoreCase("completed")) 
							&& !courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTED) )
						{
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && (enrollmentStatus.trim().equalsIgnoreCase("active") ) ) {
							itemsList.add(item);
						}
					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}

	// RECENT FILTER
	private static Set<MyCoursesCourseGroup> getRecentCourses(List<MyCoursesCourseGroup> itemsToFilter) {
		Set<MyCoursesCourseGroup> filteredList = new HashSet<MyCoursesCourseGroup>();
		Set<MyCoursesItem> itemsList = null;
		Date lastAccessDate = null;
		String courseStatus = null;
		String enrollmentStatus = null;
		if( itemsToFilter.size() > 0 ) {
			for (MyCoursesCourseGroup myCoursesCourseGroup : itemsToFilter) {
				itemsList = new HashSet<MyCoursesItem>();
				for (MyCoursesItem item : myCoursesCourseGroup.getAllMyCoursesItems()) {
					courseStatus = (String)item.get("courseStatus");
					enrollmentStatus = (String)item.get("enrollmentStatus");
					lastAccessDate = (Date)item.get("lastAccessDate");
					if ( (lastAccessDate != null)  && 
							(courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.IN_PROGRESS) 
							|| courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_PENDING) 
							|| courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.REPORTING_PENDING)
							|| courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)
							|| courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)
							|| courseStatus.trim().equalsIgnoreCase(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT))
						){
						if ( (StringUtils.isNotBlank(enrollmentStatus)) && 
								(enrollmentStatus.trim().equalsIgnoreCase("active") ) ){
							itemsList.add(item);	
						}

					}
				}
				// now set the filteredList to the currentList on this courseGroup
				if ( itemsList.size() > 0 ) {
					myCoursesCourseGroup.setCurrentMyCoursesItems(itemsList);
					filteredList.add(myCoursesCourseGroup);
				}
			}
		}
		return filteredList;
	}

	private String createBreadCrumb(CourseGroup courseGroup) {
		Stack<CourseGroup> stack = new Stack<CourseGroup>();
		if(courseGroup.getParentCourseGroup()!=null){
			stack.add(courseGroup.getParentCourseGroup());
		}else{
			stack.add(courseGroup);
		}
		CourseGroup cg;
		List<CourseGroup> breadCrumbs = new ArrayList<CourseGroup>();
		while ( !stack.isEmpty() ) {
			cg = stack.pop();
			breadCrumbs.add(cg);
			if ( cg.getParentCourseGroup() != null ) { 
				stack.push(cg.getParentCourseGroup());
			}
		}

		// now generate the breadCrumb
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (CourseGroup cg2 : breadCrumbs) {
			if ( first ) {
				first = false;
			}
			else {
				sb.append(" : ");
			}
			sb.append(cg2.getName());
		}
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if ( obj instanceof MyCoursesItem ) {
			MyCoursesCourseGroup item = (MyCoursesCourseGroup)obj;
			String objCoursId = (String)item.get("courseGroupName");
			String thisCourseId = (String)this.get("courseGroupName");
			return thisCourseId.equalsIgnoreCase(objCoursId);
		}
		return false;
	}

	public int hashCode() {
		String courseId = (String)this.get("courseGroupName");
		return courseId.hashCode();
	} 

	/*
	 * author Faisal A. Siddiqui
	 */
	public static List<MyCoursesCourseGroup> arrangeCoursesInCourseGroups(List<CourseGroup> groups)
	{
		long startTime=System.currentTimeMillis();
		String  cgName;
		HashMap mcgMap=new HashMap();
		log.debug("groups.size=>"+groups.size());
		List<MyCoursesCourseGroup> results = new ArrayList<MyCoursesCourseGroup>();

		for(CourseGroup cg:groups)
		{
			if(cg.getCourses()!=null)
			{
				MyCoursesCourseGroup mcg=(MyCoursesCourseGroup)mcgMap.get(cg.getName());
				if(mcg==null)
				{
					mcg=new MyCoursesCourseGroup(cg);
					mcgMap.put(cg.getName(), mcg);
					results.add(mcg);
				}
				else
				{
					log.debug("duplicate course group="+cg.getName());
				}
				mcg.addCourses(cg.getCourses());
			}
		}
		log.debug("arrangeCoursesInCourseGroups.totalTime="+(System.currentTimeMillis()-startTime));
		log.debug("results.size="+results.size());
		return results;
	}
	/*
	 * author Faisal A. Siddiqui
	 */

	public void addCourses(List<Course> courses)
	{
		long startTime=System.currentTimeMillis();
		for(Course c:courses)
		{
			if(c.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED))
				this.addCourse(new MyCoursesItem(c));
		}
		log.debug("addCourses.totalTime="+(System.currentTimeMillis()-startTime)+" totalCourses="+courses.size());
	}

	public static List<TreeNode> getCourseGroupsTree(Set<CourseGroup> courseGroupsList)
	{
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
		TreeNode courseGroupCourseTreeNode = null;

		for (CourseGroup courseGroup : courseGroupsList) {
			boolean courseGroupAdded = false;
			for (TreeNode rootTreeNode : rootNodesReferences) {
				List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
				if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
					courseGroupAdded = true;
					break;
				}
				CourseGroup currentCourseGroup = courseGroup;
				while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
					CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
					childCourseGroups.add(currentCourseGroup);
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}
					currentCourseGroup = parentCourseGroup;
				}
				if (courseGroupAdded)
					break;
			}
			if (!courseGroupAdded) {
				courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null);
				rootNodesReferences.add(courseGroupCourseTreeNode);
			}						
		}

		for (TreeNode rootTreeNode : rootNodesReferences) {
			treeNodesList.addAll(rootTreeNode.bfs());
		}		
		return treeNodesList;			
	}

	public static Map<String , Map<String,Set<CourseGroup>>> traverse( TreeNode treenode, Map<String , Map<String,Set<CourseGroup>>> levels) {

		log.debug("P >>> "+treenode.getDepth()+"="+((CourseGroup)treenode.getValue()).getName());
		if(treenode.getChildCount()>0){

			for(TreeNode node: treenode.getChildren()) {
				CourseGroup cg =(CourseGroup)node.getValue();
				if(levels.get(node.getDepth()+"") == null )//there is no level map for this level.
				{
					Map<String,Set<CourseGroup>> cgList =  new HashMap<String,Set<CourseGroup>>();
					Set<CourseGroup> cgs = new HashSet<CourseGroup>();
					cgs.add(cg);
					cgList.put(cg.getParentCourseGroup().getId().toString(), cgs);
					levels.put(node.getDepth()+"", cgList);
				}
				else//there is a map for this level
				{
					Map<String,Set<CourseGroup>> cgList =  levels.get(node.getDepth()+"");
					if(cgList.get(cg.getParentCourseGroup().getId().toString()) == null)
					{
						Set<CourseGroup> cgs = new HashSet<CourseGroup>();
						cgs.add(cg);
						cgList.put(cg.getParentCourseGroup().getId().toString(), cgs);
					}
					else
					{
						Set<CourseGroup> cgs = cgList.get(cg.getParentCourseGroup().getId().toString());
						cgs.add(cg);
						cgList.put(cg.getParentCourseGroup().getId().toString(), cgs);
					}

					levels.put(node.getDepth()+"", cgList);

				}
				log.debug("C >>> >>>"+node.getDepth()+"c="+node.getChildCount()+"="+((CourseGroup)node.getValue()).getName());
				if(node.getChildCount()>0)
					return traverse(node,levels);
			}
		}		
		return levels;

	}
	public static Map<String , Map<String,Set<CourseGroup>>>  traverseAllTreeNodes( List<TreeNode> treenodeList,Map<String , Map<String,Set<CourseGroup>>> levels)  
	{		
		for (TreeNode treeNode : treenodeList) {			
			if(treeNode.getChildCount() > 0)
			{
				levels = traverse(treeNode, levels);				
			}

		}
		return levels;
	}



}
