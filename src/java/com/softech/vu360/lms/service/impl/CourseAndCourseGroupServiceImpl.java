package com.softech.vu360.lms.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.repositories.ContentOwnerRepository;
import com.softech.vu360.lms.repositories.CourseGroupRepository;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.InstructorCourseRepository;
import com.softech.vu360.lms.repositories.LanguageRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.repositories.TrainingPlanAssignmentRepository;
import com.softech.vu360.lms.repositories.TrainingPlanRepository;
import com.softech.vu360.lms.repositories.WebLinkCourseRepository;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.vo.RecommendedCourseVO;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.CourseView;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.lms.webservice.client.impl.SuggestedCoursesClientWSImpl;
import com.softech.vu360.lms.webservice.client.impl.VcsDiscussionForumClientWSImpl;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.CourseInfo;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesList;
import com.softech.vu360.lms.webservice.message.lcms.ContentOwnerVO;
import com.softech.vu360.lms.webservice.message.lcms.CourseVO;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CourseGroupTree;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.TreeNode;

/**
 * @author jason
 *
 */
public class CourseAndCourseGroupServiceImpl implements CourseAndCourseGroupService {
	private static final Logger log = Logger.getLogger(CourseAndCourseGroupServiceImpl.class.getName());
	private EntitlementService entitlementService = null;
	private AuthorService authorService = null;
	private CustomerService customerService = null;
	private EnrollmentService enrollmentService = null;
	
	@Inject 
	private CourseRepository courseRepository = null;
	
	@Inject 
	private CourseGroupRepository courseGroupRepository = null;
	
	@Inject 
	private ContentOwnerRepository contentOwnerRepository = null;
	
	@Inject 
	private TrainingPlanRepository trainingPlanRepository = null;
	
	@Inject 
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRepository = null; 
		
	@Inject 
	private WebLinkCourseRepository webLinkCourseRepository = null; 
	
	@Inject 
	private LanguageRepository languageRepository = null;
	
	@Inject
	private InstructorCourseRepository instructorCourseRepository;

	public Course getCourseById(Long id) {
		try {
			log.debug("looking up course by id:"+id);
			//return courseAndCourseGroupDAO.getCourseById(id);
			return this.courseRepository.findOne(id);
		}
		catch(Exception ex) {
			log.error("Error during launch:"+ex.getMessage(), ex);
			return null;
		}
	}

	public Document getCourseDocumentByCourseId(Long id){
		
		HomeworkAssignmentCourse course = (HomeworkAssignmentCourse) this.courseRepository.findOne(id); 
		if(course != null){
			Document doc = course.getHwAssignmentInstruction();
			return doc;
		}else{
			return null;
		}
			
		
		//return courseAndCourseGroupDAO.getCourseDocumentByCourseId(id.toString());
		
	}

	public Course getCourseByCourseId(String courseId) {
		return courseRepository.findFirstByBussinesskeyOrderByIdDesc(courseId) ;
		//return courseAndCourseGroupDAO.getCourseByCourseId(courseId);
	}
	
	public Course getCourseByIdWithNoCache(Long courseId) {
		return courseRepository.findOne(courseId);
		//return courseAndCourseGroupDAO.getCourseByIdWithNoCache(courseId);
	}
	
	public List<CourseGroup> getCourseGroupsForCourse(Course course) {
		//return courseAndCourseGroupDAO.getCourseGroupsForCourse(course);
		return this.courseGroupRepository.findByCoursesId(course.getId());
		
		
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.CourseAndCourseGroupService#getCourseGroupById(java.lang.Long)
	 */
	public CourseGroup getCourseGroupById(Long id) {
		try {
			log.debug("looking up course by courseGroupId:"+id);
			return courseGroupRepository.findOne(id);
			//return courseAndCourseGroupDAO.getCourseGroupById(id);
		}
		catch(Exception ex) {
			log.error("Error during launch:"+ex.getMessage(), ex);
			return null;
		}
	}

	@Transactional
	public Course SaveCourse(WebLinkCourse course){
		log.debug("looking up WEBLINKCOURSE:");
		return webLinkCourseRepository.save(course);
		//return courseAndCourseGroupDAO.SaveCourse(course);
	}
	
	// [4/21/2010] VCS-263 :: Wrapper method for Discussion Forum type courses only
	@Transactional
	@Override
	public Course addDiscussionForumCourse(DiscussionForumCourse dfCourse){
		Course course = null;
		
		dfCourse.setCourseGUID(GUIDGeneratorUtil.generateGUID().replace("-",""));		
		if ( new VcsDiscussionForumClientWSImpl().createForumEvent(dfCourse) ) {
			//course = courseAndCourseGroupDAO.addCourse(dfCourse);
			course = this.courseRepository.save(dfCourse);
			//this.courseRepository.save(course);
		}
		
		return course;
	}

	@Transactional
	public Course addCourse(Course course){
		course.setCourseGUID(GUIDGeneratorUtil.generateGUIDForCourseAndCourseGroup());
		return this.courseRepository.save(course);
		//return  courseAndCourseGroupDAO.addCourse(course);
	}
	
	// [4/22/2010] VCS-264 :: Wrapper method for Discussion Forum type courses only
	@Transactional
	@Override
	public Course updateDiscussionForumCourse(DiscussionForumCourse dfCourse){
		Course course = null;
		
//		dfCourse.setCourseGUID(GUIDGeneratorUtil.generateGUID().replace("-",""));		
		if ( new VcsDiscussionForumClientWSImpl().updateForumEvent(dfCourse) ) {
			//course = courseAndCourseGroupDAO.saveCourse(dfCourse);
			course = this.courseRepository.save(dfCourse);
		}
		
		return course;
	}
	@Transactional
	public Course saveCourse(Course course) {
		log.debug("looking up SynchronousCourse:");
		//return  courseAndCourseGroupDAO.saveCourse(course);
		return this.courseRepository.save(course);
	}
	@Transactional
	public void deleteCourse(Course course){
		//courseAndCourseGroupDAO.deleteCourse(course);
		this.courseRepository.delete(course);
	}
	public void deleteCourse(long courseIdArray[]) {
		// change for deleting the related courseGroup_course entries, when deleting a course.Tahir Mehmood
		//courseAndCourseGroupDAO.deleteCourse(courseIdArray);
		this.courseRepository.deleteByIdIn(ArrayUtils.toObject(courseIdArray));
	}
	public  Course  loadForUpdateCourse(long id){
		return courseRepository.findOne(id);
		//return courseAndCourseGroupDAO.loadForUpdateCourse(id);
	}
	public  CourseGroup  loadForUpdateCourseGroup(long id){
			return courseGroupRepository.findOne(id);
		//return courseAndCourseGroupDAO.loadForUpdateCourseGroup(id);
	}

	@Transactional
	public void SaveWeblinkCourse(WebLinkCourse weblinkCourse,Customer customer, VU360User loggedInUser){
		//find me contentOwner
		//is there any contentOwner for my customer?
		ContentOwner contentOwner = customer.getContentOwner();
		//Am I really there? if yes set me with course or  create me
		
		if(contentOwner!= null){
			weblinkCourse.setContentOwner(contentOwner);

		}else{
			
			try{
				contentOwner = authorService.addContentOwnerIfNotExist(customer, loggedInUser.getId());
				weblinkCourse.setContentOwner(contentOwner);
				
				Customer newCustomer = this.getCustomerService().loadForUpdateCustomer(customer.getId());
				newCustomer.setContentOwner(contentOwner);
				this.getCustomerService().updateCustomer(newCustomer);
				
				
			}
			catch(Exception ex){
				log.error("Error in saving weblink course when creating contentowner for the customer : " + customer.getName());
			}
		}
		//Course course= courseAndCourseGroupDAO.SaveCourse(weblinkCourse);
		Course course= webLinkCourseRepository.save(weblinkCourse);
		
		//Now lets create an entitlement for this course

		CourseCustomerEntitlement customerEntitlement=new CourseCustomerEntitlement();
		customerEntitlement.setName(weblinkCourse.getCourseTitle());
		customerEntitlement.setAllowUnlimitedEnrollments(true);
		customerEntitlement.setAllowSelfEnrollment(true);
		//customerEntitlement.adddCustomerEntitlementItem(course, null);	//customerEntitlement.addCourse(course);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		entitlementService.addCourseCustomerEntitlementItemsIfNotExist(customerEntitlement, courses);
		customerEntitlement.setCustomer(customer);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Calendar now = Calendar.getInstance();
		now.setTime(new Date()); 
		String TodaysDate=formatter.format(new Date());
		Date startDate=null;
		try {
			startDate=formatter.parse(TodaysDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		customerEntitlement.setStartDate(startDate);

		// I am afraid to keep it blank for time now its 30 years. TO DO change later
		now.add(Calendar.YEAR , 30);
		customerEntitlement.setEndDate(now.getTime());
		entitlementService.saveCustomerEntitlement(customerEntitlement,null);
	}

	public List<WebLinkCourse> findCustomCourseByName(String name,Customer customer){
		//return courseAndCourseGroupDAO.findCustomCourseByName(name,customer);
		return  this.webLinkCourseRepository.findByCourseTitleIgnoreCaseLikeAndContentOwner_Customer("%" + name + "%", customer);
	}
	
	@Transactional
	public void deleteCustomCourse(long customCourseIdArray[]){
		
		List<WebLinkCourse> webLinkCourse = this.webLinkCourseRepository.findByIdIn(ArrayUtils.toObject(customCourseIdArray));
		this.webLinkCourseRepository.delete(webLinkCourse);
		//this.webLinkCourseRepository.deleteByIdIn(ArrayUtils.toObject(customCourseIdArray));
		//courseAndCourseGroupDAO.deleteCustomCourse(customCourseIdArray);
	}
	
	public WebLinkCourse getWebLinkCourseByID(long id){
		//return courseAndCourseGroupDAO.getWebLinkCourseByID(id);
		return  this.webLinkCourseRepository.findOne(id);
	}

//	public CourseConfiguration findCourseCompletionCriteriaForCourse(Learner learner, Course course) {
//		// in the future there may be more than one course completion criteria available for a course and/or
//		// for a learner taking the course.  Thankfully as it is close to UAT, we have only one!
//		return courseAndCourseGroupDAO.getCourseCompletionCriteriaByCourseId(course.getId());
//	}
	
	public TrainingPlan getTrainingPlanByCourse(Course course,Customer customer){
		
		
		List<TrainingPlan> trainingPlans=trainingPlanRepository.findByCoursesCourseAndCustomer(course,customer);
		//List<TrainingPlan> trainingPlans=courseAndCourseGroupDAO.getTrainingPlanByCourse(course,customer);
		if(trainingPlans!=null && trainingPlans.size()>0)
			return trainingPlans.get(0);
		else 
			return null;
	}

	public TrainingPlan getTrainingPlanByEnrollment(LearnerEnrollment enrollment){
		

		TrainingPlanAssignment trainingPlanAssignment = trainingPlanAssignmentRepository.findBylearnerEnrollments(enrollment);
		if (trainingPlanAssignment != null){
			TrainingPlan trainingPlan = trainingPlanAssignment.getTrainingPlan();
			return trainingPlan;
			
		}else{
			return null;
		}
		
//		if( courseAndCourseGroupDAO.getTrainingPlanByEnrollment(enrollment) != null )
//			return courseAndCourseGroupDAO.getTrainingPlanByEnrollment(enrollment);
//		else 
//			return null;
	}
	
	public List<TrainingPlanAssignment> getTrainingPlanByEnrollments(List<LearnerEnrollment> enrollments) {
		List<TrainingPlanAssignment> trainingPlanAssignments = this.trainingPlanAssignmentRepository.findDistinctByLearnerEnrollmentsIn(enrollments);
		//Modified By MariumSAud : Code added to reload TrainPlanAssignment with LearenrEnrollment and TrainingPlan Entity Graph in order to avoid Proxy Error 
		// due to no of queries execution while displaying Learner > MyCourses. Entity Graph can not be used with JPA IN clause (Hibernate Bug)
		//List<TrainingPlanAssignment> trainingPlanAssignmentsWithEntityGraph = new ArrayList<TrainingPlanAssignment>();

		//http://jira.360training.com/browse/LMS-22136
		//I am commenting out this code in the ticket above. Not needed.//Muhammad Sajjad
		/*for (TrainingPlanAssignment trainingPlanAssignment : trainingPlanAssignments) {
//			TrainingPlanAssignment tpa = this.trainingPlanAssignmentRepository.findById(trainingPlanAssignment.getId());
			trainingPlanAssignmentsWithEntityGraph.add(trainingPlanAssignment);
		}*/

		return trainingPlanAssignments;
	}
	
	public List<Map<Object, Object>> getCoursesForCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search){
		//This methods call View by using Stored procedure code. need to check
		//return courseAndCourseGroupDAO.getCoursesForCatalog(learnerId,customerEntitlements, search,0l);
		return null;
	}
	
	public List<Map<Object, Object>> getCoursesForCatalogOrderByPCG(long learnerId,List<CustomerEntitlement> customerEntitlements, String search){
		//return courseAndCourseGroupDAO.getCoursesForCatalogOrderByPCG(learnerId,customerEntitlements, search,0l);
		return this.courseRepository.findCatalogSPById(learnerId, null, search, "SP_COURSECATALOGWITHCOURSEGROUPS");
	}
	
	public List<Map<Object, Object>> getCoursesForCatalogByCourseGroupID(long learnerId,List<CustomerEntitlement> customerEntitlements, String search, long courseGroupId)
	{
		//This methods call View by using Stored procedure code. need to check
		//return courseAndCourseGroupDAO.getCoursesForCatalog(new Long(learnerId),customerEntitlements, search, courseGroupId);
		return null;
	}
	
	public List<Map<Object, Object>> getTrainingPlansForCourseCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search){
		//return trainingPlanRepository.getTrainingPlansForCourseCatalog(learnerId, search);  //courseAndCourseGroupDAO.getTrainingPlansForCourseCatalog(learnerId,customerEntitlements, search);
		return this.courseRepository.findCatalogSPById(new Long(learnerId), null, search, "SP_COURSECATALOGWITHTRAININGPLANS"); 
	}
	
	public List<Map<Object, Object>> getTrainingPlanCoursesForCatalogByTrainingPlanId(long learnerId,List<CustomerEntitlement> customerEntitlements, long trainingPlanId, String search){
		//return trainingPlanRepository.getTrainingPlansForCourseCatalog(learnerId, search);
		return this.courseRepository.findCatalogSPById(new Long(learnerId), null, search, "SP_COURSECATALOGWITHTRAININGPLANS");
	}


	public List<Map<Object, Object>> getMiscellaneousCoursesForCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search){
		//return courseAndCourseGroupDAO.getMiscellaneousCoursesForCatalog(learnerId,customerEntitlements, search);
		return this.courseRepository.findCatalogSPById(new Long(learnerId), null, search, "SP_COURSECATALOGWITHMISC");
	}
	
	/*
	 * Created By: Faisal Ahmed Siddiqui (faisal.ahmed@360training.com)
	 * Created On: 26th March 2009
	 * Description: This method is created for the integration of StoreFront & LMS Application
	 * 			 All courses are mapped through GUID b/w StoreFront & LMS Courses
	 * UnitTest Status: In-Progress
	 */
	public Course getCourseByGUID(String guid){

		return this.courseRepository.findFirstByCourseGUIDOrderByIdDesc(guid);
		//return courseAndCourseGroupDAO.getCourseByGUID(guid);
	}

	@Override
	public List<Course> getCoursesByGUIDs(String[] guids){
		//return courseAndCourseGroupDAO.getCoursesByGUIDs(guids);
		return this.courseRepository.findByCourseGUIDIn(guids);
	}
	
	/*@Override
	public List<CourseGroup> getCourseGroupsByGUIDs(String[] guids){
		return courseAndCourseGroupDAO.getCourseGroupsByGUIDs(guids);
	}
	*/
	public ContentOwner getContentOwnerByCustomer(Customer customer){
		//return courseAndCourseGroupDAO.getContentOwnerByCustomer(customer);
		return contentOwnerRepository.findByCustomer(customer);
	}

	/**
	 * // [1/27/2011] LMS-7183 :: Retired Course Functionality II (Refactored Code)
	 * Search Courses based on Search Criteria
	 */
	@Override
	public Map<String, Object> getCourses (Long contentOwnerId, String courseName, String courseId, String courseType, String courseStatus, String sortColumn, int sortDirection, int pageIndex, int pageSize) {
		//return this.courseAndCourseGroupDAO.getCourses(contentOwnerId, courseName, courseId, courseType, courseStatus, sortColumn, sortDirection, pageIndex, pageSize);
		int recordSize = -1;
			List<Course> resultList = new ArrayList<Course>();

			try {
				if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)) {
					courseType = SynchronousCourse.COURSE_TYPE;
				} else if (courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
					courseType = WebinarCourse.COURSE_TYPE;
				} else if (courseType.equalsIgnoreCase("SCORM Package") || courseType.equalsIgnoreCase(ScormCourse.COURSE_TYPE)) {
					courseType = ScormCourse.COURSE_TYPE;
				} else if (courseType.equalsIgnoreCase("Weblink Course")) {
					courseType = WebLinkCourse.COURSE_TYPE;
				} else if (courseType.equalsIgnoreCase("Discussion Forum")) {
					courseType = DiscussionForumCourse.COURSE_TYPE;
				} else if (courseType
						.equalsIgnoreCase(HomeworkAssignmentCourse.COURSE_TYPE)) {
					courseType = HomeworkAssignmentCourse.COURSE_TYPE;
				} else if (courseType
						.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
					courseType = InstructorConnectCourse.COURSE_TYPE;
				} 
				RepositorySpecificationsBuilder<Course> sb_Course=new RepositorySpecificationsBuilder<Course>();

				sb_Course.with("contentOwner_id", sb_Course.JOIN_EQUALS, contentOwnerId, "AND");
				sb_Course.with("courseTitle", sb_Course.LIKE_IGNORE_CASE, courseName, "AND");
				sb_Course.with("courseId", sb_Course.LIKE_IGNORE_CASE, courseId, "AND");

				if (courseType.equalsIgnoreCase("All")) {
					sb_Course.with("courseType", sb_Course.IN, new String[] { ScormCourse.COURSE_TYPE,
							WebLinkCourse.COURSE_TYPE,
							DiscussionForumCourse.COURSE_TYPE,
							SynchronousCourse.COURSE_TYPE,
							WebinarCourse.COURSE_TYPE,
							HomeworkAssignmentCourse.COURSE_TYPE,
							InstructorConnectCourse.COURSE_TYPE }, "AND");
				}
				else{
					sb_Course.with("courseType", sb_Course.EQUALS_TO, courseType, "AND");
				}

				if (!courseStatus.equalsIgnoreCase("All")) {
					sb_Course.with("retired", sb_Course.EQUALS_TO, courseStatus.equalsIgnoreCase("Active") ? false
							: true, "AND");
				}

				Sort sortSpec = null;
				sortSpec = orderBy(sortDirection, sortColumn);

				PageRequest pageRequest = null;

				if (pageSize != -1) {
					pageRequest = new PageRequest(pageIndex, pageSize,sortSpec);
					Page<Course> page = courseRepository.findAll(sb_Course.build(), pageRequest);
					recordSize = ((int) (long)page.getTotalElements());
					resultList = page.getContent();
				}
				else{
					List<Course> list = courseRepository.findAll(sb_Course.build());
					recordSize = list.size();
					for (Course course : list){
						resultList.add(course);
					}
				}
				log.debug("getCourses --------------------------------------->"
						+ resultList.size());
			} catch (Exception e) {
				log.debug(e);
			}

			Map<String, Object> serachResult = new HashMap<String, Object>();
			serachResult.put("recordSize", recordSize);
			serachResult.put("list", resultList);
			return (serachResult);
		}

	private Sort orderBy(int sortDirection, String sortBy) {
		if (sortDirection == 0) {
			return new Sort(Sort.Direction.ASC, sortBy);
		} else {
			return new Sort(Sort.Direction.DESC, sortBy);
		}

	}
	
	@Override
	public Map<String, Object> getCourses (Long contentOwnerId, String courseName, String courseId, String courseType,String[] courseTypes, String courseStatus, String sortColumn, int sortDirection, int pageIndex, int pageSize) {
		int recordSize = -1;
		List<Course> resultList = new ArrayList<Course>();

		try {
			RepositorySpecificationsBuilder<Course> sb_Course=new RepositorySpecificationsBuilder<Course>();
			
			sb_Course.with("contentOwner_id", sb_Course.JOIN_EQUALS, contentOwnerId, "AND");
			sb_Course.with("courseTitle", sb_Course.LIKE_IGNORE_CASE, courseName, "AND");
			sb_Course.with("courseId", sb_Course.LIKE_IGNORE_CASE, courseId, "AND");

			if (courseType.equalsIgnoreCase("All")) {
				sb_Course.with("courseType", sb_Course.IN, courseTypes, "AND");
			}

			if (!courseStatus.equalsIgnoreCase("All")) {
				sb_Course.with("retired", sb_Course.EQUALS_TO, courseStatus.equalsIgnoreCase("Active") ? false
						: true, "AND");
			}

			Sort sortSpec = null;
			sortSpec = orderBy(sortDirection, sortColumn);
			
			PageRequest pageRequest = null;

			if (pageSize != -1) {
				pageRequest = new PageRequest(pageIndex, pageSize,sortSpec);
				Page<Course> page = courseRepository.findAll(sb_Course.build(), pageRequest);
				recordSize = ((int) (long)page.getTotalElements());
				resultList = page.getContent();
			}
			else{
				List<Course> list = courseRepository.findAll(sb_Course.build());
				recordSize = list.size();
				for (Course course : list){
					resultList.add(course);
				}
			}
			
			log.debug("getCourses --------------------------------------->"
					+ resultList.size());
		} catch (Exception e) {
			log.debug(e);
		}

		Map<String, Object> serachResult = new HashMap<String, Object>();
		serachResult.put("recordSize", recordSize);
		serachResult.put("list", resultList);
		return (serachResult);
	}
	
	/**
	 * Search Courses based on contentOwnerId, courseName, bussinesskey
	 */
	@Override
	public List<Course> getCourses (Long contentOwnerId, String courseName, String bussinesskey, Long[] courseIds){
		
		// Comment out following filter - Need to work on Content Owner filters  
		//finalExpr = builder.get("contentOwner").get("id").equal(contentOwnerId);

		//finalExpr = finalExpr.and(builder.get("courseStatus").equalsIgnoreCase(Course.PUBLISHED));
		RepositorySpecificationsBuilder<Course> sb_Course=new RepositorySpecificationsBuilder<Course>();
		
		sb_Course.with("courseStatus", sb_Course.EQUALS_TO, Course.PUBLISHED, "AND");
//		finalExpr = builder.get("courseStatus").equalsIgnoreCase(Course.PUBLISHED);
		
		sb_Course.with("retired", sb_Course.EQUALS_TO, false, "AND");
		//finalExpr = finalExpr.and(builder.get("retired").equal(false));
		
		sb_Course.with("courseTitle", sb_Course.LIKE_IGNORE_CASE, courseName, "AND");
//		if (StringUtils.isNotBlank(courseName)) 
//			finalExpr = finalExpr.and(builder.get("courseTitle").likeIgnoreCase("%" + courseName + "%"));
		
		sb_Course.with("bussinesskey", sb_Course.LIKE_IGNORE_CASE, bussinesskey, "AND");
//		if (StringUtils.isNotBlank(bussinesskey)) 
//			finalExpr = finalExpr.and(builder.get("bussinesskey").likeIgnoreCase("%" + bussinesskey + "%"));
		
		sb_Course.with("id", sb_Course.NOT_IN, courseIds, "AND");
//		if (courseIds!=null && courseIds.length>0) 
//			finalExpr = finalExpr.and(builder.get("id").notIn(courseIds));
		List<Course> courseList=courseRepository.findAll(sb_Course.build());

		return 	courseList;
	}

	@Override
	public List<RecommendedCourseVO> getRecommendedCoursesFromSF (Brander brand, VU360User user){
		
		Long beforeALL = System.currentTimeMillis();
		log.debug("before ALL " + beforeALL);
		List<RecommendedCourseVO> lstOfObjCoursesVO  = new ArrayList<RecommendedCourseVO>();
	
		
		log.debug("totalMemory:"+Runtime.getRuntime().totalMemory());
		log.debug("1- displayMyCourses freeMemory:"+Runtime.getRuntime().freeMemory());
		
		
		List<String> recommendedCourses = new ArrayList<String>();
		
		//LearnerEnrollment learnerEnrollment = null;
		//Course course = null;
		//LearnerCourseStatistics courseStatistics = null;
		//CourseConfiguration courseCompletionCriteria = null;
		//List<Object> coursesdetail = new ArrayList<Object>();
        List<CourseInfo> coursesInfoDetail = new ArrayList<CourseInfo>();
		//String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");

       

		try {

            SuggestedCoursesList wsResponse = new SuggestedCoursesList();
			
	         
			 String resellerCode = user.getLearner().getCustomer().getDistributor().getDistributorCode();
			 SuggestedCoursesClientWSImpl wsClient= new SuggestedCoursesClientWSImpl();
			 
			 List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
				List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
				List sortedCourseGroups = new ArrayList();
				String search = "Search";
				Map map = enrollmentService.displayMyCourses(user, brand, courseGroups, filteredMyCourses, sortedCourseGroups, "recent", search, true);
				List myCourseItems = (List) map.get("myCourseItems");
			
				for (Object item : myCourseItems) {
					MyCoursesItem myCoursesItem = (MyCoursesItem) item;
					recommendedCourses.add(myCoursesItem.getCourseID()) ;
			    }
				
			
				
				wsResponse = wsClient.getSuggestedCourses_Marshal(recommendedCourses, "", resellerCode);
				if(wsResponse!=null && wsResponse.getCourse()!=null && wsResponse.getCourse().size()>0){
								coursesInfoDetail = wsResponse.getCourse();
							
							
							
						String[] courseGuids = new String[coursesInfoDetail.size()];
						 
						
						for (int i=0;i<coursesInfoDetail.size();i++) {
							courseGuids[i] = coursesInfoDetail.get(i).getCourseGuidTo();
						}
						//List<Course> lstObjLCMSCourses = courseAndCourseGroupDAO.getCoursesByGUIDs(courseGuids);
						List<Course> lstObjLCMSCourses = this.courseRepository.findByCourseGUIDIn(courseGuids);
						
						
						RecommendedCourseVO recommendedCourseVO=null;
						    
			            for (Object item : coursesInfoDetail) {
			                    CourseInfo mySuggestedCourses = (CourseInfo) item;
			                    
			                    for (Course lcmscourse : lstObjLCMSCourses) {
			                    	if(mySuggestedCourses.getCourseGuidTo().equalsIgnoreCase(lcmscourse.getCourseGUID())){
			                    
				                    	recommendedCourseVO = new RecommendedCourseVO();
				                    	recommendedCourseVO.setId(lcmscourse.getId());
				                    	recommendedCourseVO.setCourseGuidFrom(mySuggestedCourses.getCourseGuidFrom());
				                    	recommendedCourseVO.setCourseGuidTo(mySuggestedCourses.getCourseGuidTo());
				                    	recommendedCourseVO.setCourseName(lcmscourse.getCourseTitle());
				                    	recommendedCourseVO.setImageUrl(mySuggestedCourses.getImageUrl());
				                    	recommendedCourseVO.setOrderItemURL(mySuggestedCourses.getOrderItemURL());
				                    	lstOfObjCoursesVO.add(recommendedCourseVO);
			                    	}
			                    }
			                    
			            }
				}else
				{
					if(user!=null && user.getUsername()!=null)
						log.info("::::: Recommended Courses not received from SF when user tried with username ::::: " + user.getUsername());
				}
            return lstOfObjCoursesVO;
            
		} catch (Exception e) {
			log.debug("exception", e);
			return null;
		}
	}
	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public List<Course> getCoursesByIds(long[] courseIds)
	{
		final int max_limit = 1000;
		int buckets = courseIds.length % max_limit;
		int bucketSize = max_limit;
		List<Course> allCourses = new ArrayList<Course>(courseIds.length);
		List<Course> courses = null;
		boolean loop = buckets > 0;
		int index = 0;
		long[] idbucket = null;
		while (loop) {
			if (index + max_limit > courseIds.length) {
				bucketSize = courseIds.length - index;// supposed to be last
														// bucket with the
														// content less than
														// max_limit
			} else {
				bucketSize = max_limit;
			}
			idbucket = new long[bucketSize]; // [10/29/2010] LMS-7576 ::
												// Training Plan crashed when
												// adding more courses to it
			System.arraycopy(courseIds, index, idbucket, 0, bucketSize);
			try {

//				ReadAllQuery raq = new ReadAllQuery(Course.class);
//				ExpressionBuilder builder = raq.getExpressionBuilder();
//				Expression e = builder.get("id").in(courseIds);
//				raq.setSelectionCriteria(e);
//				courses = (List<Course>) getTopLinkTemplate().executeQuery(raq);
				
				courses = this.courseRepository.findByIdIn( ArrayUtils.toObject(courseIds));
				
				allCourses.addAll(courses);
			} catch (ObjectRetrievalFailureException e) {
				if (log.isDebugEnabled()) {
					log.debug("learners:" + courseIds);
				}
			}

			index += bucketSize;
			if (index >= courseIds.length)
				loop = false;
		}
		return allCourses;
		
//		long startTime=System.currentTimeMillis();
//		List<Course> courses=null;
//		if(courseIds!=null && courseIds.length>0)
//		{
//			courses=courseAndCourseGroupDAO.getCoursesByIds(courseIds);
//			log.debug("courses.size=>"+courses.size());
//		}
//		return courses;
	}
	
	public List<CourseGroup> getCourseGroupsByIds(long[] courseGroupIds)
	{
		List <CourseGroup> courseGroups = new ArrayList<CourseGroup>();
		int count= 0;
		int totalcount=-1;
		int SUBARRAYSIZE=courseGroupIds.length<2000?courseGroupIds.length:2000;
		long[] subArray= new long[SUBARRAYSIZE];
		for(int i=0;i<courseGroupIds.length;i++){
			subArray[count++]= courseGroupIds[i];
			totalcount++;
			if(count>1900){
				//list.addAll(getCourseGroupsByIdFromDB(subArray));
				courseGroups.addAll(this.courseGroupRepository.findByIdIn(ArrayUtils.toObject(subArray)));
				count=0;
				subArray=new long[SUBARRAYSIZE];
			}else if(totalcount==courseGroupIds.length-1){
				//list.addAll(getCourseGroupsByIdFromDB(subArray));
				courseGroups.addAll(this.courseGroupRepository.findByIdIn(ArrayUtils.toObject(subArray)));		
			}							
		}		
		return courseGroups;
		
		
//		List<CourseGroup> courseGroups=courseAndCourseGroupDAO.getCourseGroupsByIds(courseGroupIds);
	//	return courseGroups;
	}
	
	@Transactional
	public CourseGroup saveCourseGroup(CourseGroup courseGroup){
		courseGroup.setGuid(GUIDGeneratorUtil.generateGUIDForCourseAndCourseGroup());
		//courseGroup = courseAndCourseGroupDAO.saveCourseGroup(courseGroup);
		courseGroup =  this.courseGroupRepository.save(courseGroup);
		return courseGroup ;
	}
	@Transactional
	public CourseGroup  updateCourseGroup(CourseGroup cg) {
		
		CourseGroup courseGroup =  this.courseGroupRepository.findOne(cg.getId()); //courseAndCourseGroupDAO.loadForUpdateCourseGroup( cg.getId() );
		courseGroup.setChildrenCourseGroups(cg.getChildrenCourseGroups());
		courseGroup.setContentOwner(cg.getContentOwner());
		courseGroup.setCourses(cg.getCourses());
		courseGroup.setDescription(cg.getDescription());
		courseGroup.setKeywords(cg.getKeywords());
		courseGroup.setName(cg.getName());
		courseGroup.setParentCourseGroup(cg.getParentCourseGroup());
		courseGroup.setRootCourseGroup(cg.getRootCourseGroup());
		
		//courseGroup = courseAndCourseGroupDAO.updateCourseGroup(courseGroup);
		courseGroup = this.courseGroupRepository.save(courseGroup);
		return courseGroup ;
	}
	
	public List<CourseGroup> getAllCourseGroupsByContentOwnerId(Long instructorContentOwnerId){
		//List<CourseGroup> courseGroupList = courseAndCourseGroupDAO.getAllCourseGroupsByContentOwnerId(instructorContentOwnerId);
		List<CourseGroup> courseGroupList = this.courseGroupRepository.findByContentOwnerId(instructorContentOwnerId);
		return courseGroupList ;
	}
	
	public List<CourseGroup> getAllCourseGroupsByCourseId(Long courseId, String varCourseGroupName){
		//List<CourseGroup> courseGroupList = courseAndCourseGroupDAO.getAllCourseGroupsByCourseId(courseId, varCourseGroupName);
		
		if(courseId==null || courseId<=0)
			courseId= new Long(0);
		
		List<CourseGroup> courseGroupList =  this.courseGroupRepository.findByCoursesIdAndNameIgnoreCaseLike(courseId, "%" + varCourseGroupName + "%");
		return courseGroupList ;
	}
	
	public List<CourseGroup> getCourseGroupsForCourse(Course course, List<DistributorEntitlement> distributorEntitlement){
		
		List<CourseGroup> courseGroups=new ArrayList<CourseGroup>();
		List<CourseGroup> coursesCourseGroups=this.getCourseGroupsForCourse(course);
		if(coursesCourseGroups==null || coursesCourseGroups.size()==0)
			return courseGroups;
		
		
		for(DistributorEntitlement dE : distributorEntitlement){
			for(CourseGroup cG : dE.getCourseGroups()){
				if(coursesCourseGroups.contains(cG)){
					courseGroups.add(cG);
				}
			}
		}
		
		
		return courseGroups;
	}
	 
	@Transactional
	@Override
	public void deleteCourseGroup(CourseGroup cg) {
		//courseAndCourseGroupDAO.deleteCourseGroup(cg);
		this.courseGroupRepository.delete(cg);
		
	}

	@Override
	public List<Course> getAllCoursesByContentOwnerId(
			Long instructorContentOwnerId) {
		//return courseAndCourseGroupDAO.getAllCoursesByContentOwnerId(instructorContentOwnerId);
		return this.courseRepository.findByContentOwnerId(instructorContentOwnerId);
	}
	public CourseGroup getCourseGroupByguid(String guid){
		//return courseAndCourseGroupDAO.getCourseGroupByguid(guid);
		return this.courseGroupRepository.findByGuid(guid);
	}


	public List<Course> getCoursesByCourseGourp(CourseGroup courseGroup)
	{
		List<Course> courses = new ArrayList<Course>();
		//filter the courses, and select only the four courses for which we have add course support.   Tahir Mehmood(30/04)
		
		for (Course course : courseGroup.getCourses()) 
		{
			if( (course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) 
					|| course.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE) 
					|| course.getCourseType().equalsIgnoreCase(WebLinkCourse.COURSE_TYPE) 
					|| course.getCourseType().equalsIgnoreCase(DiscussionForumCourse.COURSE_TYPE) 
					|| course.getCourseType().equalsIgnoreCase(ScormCourse.COURSE_TYPE))
				&& (! course.isRetired() ) )  // [07/22/2010] LMS-6388 :: Hide retired courses from all queries and views				
			{
				courses.add(course);
			}
		}		
		
		return courses;
	}
	
	/**
	 * [5/18/2010] VCS-284 :: Get DFC Courses by Instructor id specially for HOME page
	 */
	@Override
	public List<DiscussionForumCourse> getDFCCourseByInstructorId(Long instructorId) {
		
		List<DiscussionForumCourse> dfcCourseList = new ArrayList<DiscussionForumCourse>();
		try {
			List<InstructorCourse> courseInstructorVector = instructorCourseRepository.findByInstructorIdEqualsAndCourseCourseMediaTypeEqualsIgnoreCaseAndCourseRetiredFalseOrderByCourseCourseTitleAsc(instructorId,"DFC");

			for (InstructorCourse courseInstructor : courseInstructorVector) {
				dfcCourseList.add((DiscussionForumCourse) courseInstructor
						.getCourse());
			}
		} catch (Exception e) {
			log.debug(e.getStackTrace());
		}

		return dfcCourseList;
	
	}
	/*
	 * This service is created to encapsulate the all business process needs to be executed at the time of course publish
	 * currently, its creating customer Entitlement of type course if content owner is associated with customer. And if content owner is associated
	 * with reseller then it will do nothing just refresh the cache 
	 * for more detail visit http://jira.360training.com/browse/LMS-5599
	 */
	public HashMap<String, Integer> publishCourses(List<ContentOwnerVO> contentOwners,boolean refreshCache) {
		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		int totalEntitlementsToCreateUpdate = 0;
		int entitlementCreatedUpdated = 0;
		
		if (CollectionUtils.isNotEmpty(contentOwners)) {
			for(ContentOwnerVO contentOwnerVO : contentOwners) {
				String contentOwnerGuid = contentOwnerVO.getContentOwnerGUID();
				ContentOwner contentOwner = authorService.readContentOwnerByGUID(contentOwnerGuid);
				List<CourseVO> courseVOs = contentOwnerVO.getCourseList();
				String[] courseGuids = new String[courseVOs.size()];
				for (int i=0;i<courseVOs.size();i++) {
					courseGuids[i] = courseVOs.get(i).getGuid();
					log.debug("Course GUID [" + courseGuids[i] + "]");
				}
				if(refreshCache)
					this.refreshCoursesCache(courseGuids);

				Customer customer = authorService.getCustomerByContentOwner(contentOwner);
				if (customer != null && CollectionUtils.isNotEmpty(courseVOs)) {				
					totalEntitlementsToCreateUpdate ++;
					//List<Course> courses = courseAndCourseGroupDAO.getCoursesByGUIDs(courseGuids);
					List<Course> courses =  this.courseRepository.findByCourseGUIDIn(courseGuids);
					
					// -- Start [LMS-14950]
					// Creating/updating Reseller contract if reseller is a type of 'Selfservice'  
					// Reseller has one or more customers. if Content Owner of current customer has PlanType_Id==2, its mean current Reseller and Customer are 'Selfservice' type objects.
					if(contentOwner.getPlanTypeId()!=null && contentOwner.getPlanTypeId()==2){
						entitlementService.getUnlimitedDistributorEntitlement(customer.getDistributor(), courses, true);
					}	
					// -- End   [LMS-14950]
					
					if (contentOwner != null && courses != null && !courses.isEmpty()) {
						log.debug("Customer Id [" + customer.getId() + "], Customer Name [" + customer.getName() + "]");
						CustomerEntitlement customerEntitlement = entitlementService.addUnlimitedCustomerEntitlementIfNotExist(customer, courses);
						if (customerEntitlement != null) {
							entitlementCreatedUpdated++;
						}else{
							log.error("WARNING: Unable to create customer contract...! ");
						}
					}
				}
			}
		}
		
		result.put(ENTITLEMENTS_TO_CREATE_UPDATE, totalEntitlementsToCreateUpdate);
		result.put(ENTITLEMENTS_CREATED_UPDATED, entitlementCreatedUpdated);
		
		return result;
	}	
	/*
	 * This method will refresh Course Related Objects in TopLink cache
	 */
	
	public List<Course> refreshCoursesCache(String[] courseGUIDs){
		//courseAndCourseGroupDAO.refreshAssoicatedCoursesGroupsInCache(courseGUIDs);
		this.courseGroupRepository.findByCoursesCourseGUIDIn(courseGUIDs);
		
		////////////////////////courseAndCourseGroupDAO.refreshCourseCompletionCriteria(courseGUIDs);
		//return courseAndCourseGroupDAO.refreshCoursesInCache(courseGUIDs);
		return this.courseRepository.findByCourseGUIDIn(courseGUIDs);
	}	

	@Transactional
    public ArrayList<Course> addCustomerEntitlementAndPublishEntitlementCourses(CustomerEntitlement customerEntitlement) {
    	ArrayList<Course> courses = new ArrayList<Course>();
    	
    	if (customerEntitlement != null) {    	
    		CourseCustomerEntitlement courseCustomerEntitlement = (CourseCustomerEntitlement)customerEntitlement; 
    		
			List<CourseCustomerEntitlementItem> entitlementItems = entitlementService.getItemsByEntitlement(courseCustomerEntitlement);
			
			for(CourseCustomerEntitlementItem entitlementItem : entitlementItems) {
				
				try {
    				Course course = entitlementItem.getCourse();
    				course = getCourseById(course.getId());
    				course.setCourseStatus(Course.PUBLISHED);
    				//course = saveCourse(course);
    				course = this.courseRepository.save(course);
    				if (course.getCourseStatus() != null && course.getCourseStatus().trim().equalsIgnoreCase(Course.PUBLISHED)) {
    					courses.add(course);
    				}
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
    	}
    	
    	return courses;
    }
    
	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	/**
	* [07/22/2010] LMS-6388 :: Implement Retire Course functionality instead of Delete Course
	* [01/18/2011] LMS-8662 :: ERR: Instructor Mode > Manage Courses > Edit Course > Course Retire status is not saving to DB
	*/
	@Transactional
	@Override
	public void retireCourse(String[] courseIdArray) {
		
		Long[] courseIds = FormUtil.getStringAsLong(courseIdArray);
		
		//this.courseAndCourseGroupDAO.retireCourse(courseIds);
		List<Course> courses = this.courseRepository.findByIdIn(courseIds);
		try {
			for(Course course:courses){
				course.setRetired(true);
				this.courseRepository.save(courses);
			}
			
			
//			for (Long courseId : courseIdArray) {
//				Course course = (Course) getTopLinkTemplate().readAndCopy(
//						Course.class, courseId);
//				course.setRetired(true);
//				getTopLinkTemplate().deepMerge(course);
//			}
		} catch (Exception e) {
			log.debug(e.getStackTrace());
		}
	}

	/**
	 * Convert give list of Course Groups to Tree with complete parent hierarchy
	 */
	@Override
	public List<List<TreeNode>> getCourseGroupTreeList(List<CourseGroup> courseGroupList, boolean enableNodes) {
		
		List<List<TreeNode>> treeNodeList = new ArrayList<List<TreeNode>>();
		
		if ( courseGroupList.size() > 0 ){
			CourseGroupTree cgTree = new CourseGroupTree(enableNodes);
			
			for ( CourseGroup courseGroup : courseGroupList ) {
				cgTree.addNode( courseGroup );
			}
			
			List<TreeNode> rootNodeList = cgTree.getRootNodes();
			for ( TreeNode rootNode : rootNodeList ) {
				treeNodeList.add( rootNode.bfs() );
			}
		}
		return treeNodeList;
	}
	
	
	/**
	 * Convert give list of Course Groups to Tree with complete parent hierarchy
	 */
	@Override
	public List<List<TreeNode>> getCourseGroupTreeList2(List<CourseGroup> courseGroupList, boolean enableNodes) {
		
		List<List<TreeNode>> treeNodeList = new ArrayList<List<TreeNode>>();
		
		if ( courseGroupList.size() > 0 ){
			CourseGroupTree cgTree = new CourseGroupTree(enableNodes);
			
			for ( CourseGroup courseGroup : courseGroupList ) {
				cgTree.addNode2( courseGroup );
			}
			
			List<TreeNode> rootNodeList = cgTree.getRootNodes();
			for ( TreeNode rootNode : rootNodeList ) {
				treeNodeList.add( rootNode.bfs() );
			}
		}
		return treeNodeList;
	}
	

	/**
	 * LMS-6504
	 */
	@Transactional
	@Override
	public boolean removeCourseCourseGroupRelationship(Long courseId, Long[] courseGroupIds) {
		
		try {
			//Course course = this.courseAndCourseGroupDAO.getCourseById( courseId );
			Course course =  this.courseRepository.findOne(courseId);
			for (Long courseGroupId : courseGroupIds) {
				CourseGroup courseGroup = this.courseGroupRepository.findOne(courseGroupId); //this.courseAndCourseGroupDAO.loadForUpdateCourseGroup( courseGroupId );
				if ( courseGroup.getCourses().contains(course) ) {
					courseGroup.getCourses().remove(course);
					//this.courseAndCourseGroupDAO.updateCourseGroup( courseGroup );
					this.courseGroupRepository.save(courseGroup);
				}
			}
			return true;
		}
		catch (Exception e) {
			log.debug(e);
		}
		return false;		
	}
	

	@Override
	public List<CourseGroup> getCourseGroupsByContentOwner(Long contentOwnerId,	String courseGroupName, String keyword) {
		
		List<CourseGroup> courseGroupList = new ArrayList<CourseGroup>();
		RepositorySpecificationsBuilder<CourseGroup> sb_courseGroup=new RepositorySpecificationsBuilder<CourseGroup>();
		try {
			sb_courseGroup.with("contentOwner_id", sb_courseGroup.JOIN_EQUALS, contentOwnerId, "AND");
			sb_courseGroup.with("name", sb_courseGroup.LIKE_IGNORE_CASE, courseGroupName, "AND");
			sb_courseGroup.with("keywords", sb_courseGroup.LIKE_IGNORE_CASE, keyword, "AND");

			courseGroupList = courseGroupRepository.findAll(sb_courseGroup.build());
		} catch (Exception e) {
			log.debug(e);
		}

		return courseGroupList;
	}

	@Transactional
	@Override
	public boolean addCourseCourseGroupRelationship(Long courseId, Long[] courseGroupIds) {
		
		try {
			//Course course = this.courseAndCourseGroupDAO.getCourseById( courseId );
			Course course = this.courseRepository.findOne(courseId);
			for (Long courseGroupId : courseGroupIds) {
				CourseGroup courseGroup = this.courseGroupRepository.findOne(courseGroupId); //this.courseAndCourseGroupDAO.loadForUpdateCourseGroup( courseGroupId );
				if (! courseGroup.getCourses().contains(course) ) {
					courseGroup.getCourses().add(course);
					//this.courseAndCourseGroupDAO.updateCourseGroup( courseGroup );
					this.courseGroupRepository.save(courseGroup);
				}
			}
			return true;
		}
		catch (Exception e) {
			log.debug(e);
		}
		return false;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

        @Override
        public List<CourseGroup> getCourseGroupsByBusinessKeys(String[] businessKeys) {
            //return courseAndCourseGroupDAO.getCourseGroupsByBusinessKeys(businessKeys);
        	return this.courseGroupRepository.findByCourseGroupIDIn(businessKeys);
        }
        
        public List<Course> getCoursesByCourseConfigurationTemplate(long ccTemplateId){
        	//return courseAndCourseGroupDAO.getCoursesByCourseConfigurationTemplate(ccTemplateId);
        	return this.courseRepository.findByCourseConfigTemplateId(new Long(ccTemplateId));
        }

		public EnrollmentService getEnrollmentService() {
			return enrollmentService;
		}

		public void setEnrollmentService(EnrollmentService enrollmentService) {
			this.enrollmentService = enrollmentService;
		}

	@Override
	public List<Language> getAllLanguages() {
		//return courseAndCourseGroupDAO.getAllLanguages();
		return (List<Language>) this.languageRepository.findAll();
		
	}
     
	public List<CourseView> getEntitlementsByCourseGroupId(Learner learner,	Long courseGroupId,boolean isTrainingPlan, boolean isMiscellaneous) {
		List<CourseView> lstCourseViews = new ArrayList<CourseView>();
		List<Map<Object, Object>> lstMap = null;
		
		if(!isTrainingPlan && !isMiscellaneous){
			List<Object[]> lstObjectArr = courseRepository.getEntitlementsByCourseGroupId(learner.getId(), courseGroupId); //courseAndCourseGroupDAO.getEntitlementsByCourseGroupId(learner,courseGroupId);
			if(lstObjectArr!=null && lstObjectArr.size()>0){
				lstObjectArr.stream().forEach((objArr) -> lstCourseViews.add(createCourseViewByCG(objArr)));
			}
			return lstCourseViews;
		}else if(isTrainingPlan){
			lstMap =  this.courseRepository.getEntitlementsByTrainingPlanId(learner.getId(), courseGroupId); //courseAndCourseGroupDAO.getEntitlementsByTrainingPlan(learner,courseGroupId);
		}else if (isMiscellaneous){
			lstMap = this.courseRepository.findCatalogSPById(learner.getId(), null, "", "SP_COURSECATALOGWITHMISC"); //courseAndCourseGroupDAO.getMiscEntitlements(learner,courseGroupId);
		}
		
		// @Marium-Saud : Replace for loop by Java 8 Stream for better performance result.
		lstMap.stream().forEach((record) -> lstCourseViews.add(new CourseView(record)));
	
		return lstCourseViews;
	}

	private CourseView createCourseViewByCG(Object[] objArr) {
		CourseView courseView=new CourseView();
		//manageUserStatus.setAffidavitType( nullConv( (String) objArr[26]) );
		courseView.setGroupName((String) objArr[4]);//map.get("COURSEGROUPNAME")
		courseView.setName((String) objArr[5]);//map.get("COURSENAME")
		courseView.setCode((String) objArr[6]);//map.get("COURSECODE")
		courseView.setType((String) objArr[7]);//map.get("COURSETYPE")
		courseView.setEnrollmentId( objArr[2]==null?null:Long.parseLong((String)objArr[2]));//map.get("ENROLLMENT_ID")
		courseView.setEntitlementId(((BigInteger) objArr[1]).longValueExact());//map.get("CUSTOMERENTITLEMENT_ID")
		courseView.setCourseId((Integer) objArr[9]);//map.get("COURSE_ID")
		courseView.setEnrollmentStatus((String) objArr[8]);//map.get("EnrollmentStatus")
		if(courseView.getEnrollmentStatus()==null){
			courseView.setEnrollmentStatus(LearnerCourseStatistics.NOT_STARTED);
		}
		//courseView.setCourseStatus((String) );// NOT FOUND in SP >> map.get("COURSE_STATUS")
		if(objArr[11]!=null)//map.get("APPROVEDCOURSEHOURS")
		{
			courseView.setCourseHours(((BigDecimal) objArr[11]).doubleValue());
		}
		
		if( objArr[12]!= null)//map.get("CEUS")
		{
			courseView.setDuration(((BigDecimal) objArr[12]).doubleValue());
		}
		return courseView;
	}
	
	public Map<Long,CourseGroupView> getAllParentCourseGroups(List<CourseGroupView> cgViewList, Map<Long, CourseGroupView> cgViewMap) {
		Map<Long,CourseGroupView> mapCourseGroupView = new HashMap<>();
		try {
			Set<Long> groupList = new HashSet<>();
			for (CourseGroupView cgView : cgViewList) {
				if (cgViewMap.get(cgView.getParentCourseGroupId()) == null && cgView.getParentCourseGroupId()!=null) {
					groupList.add(cgView.getParentCourseGroupId());
			    }
			}
			
			if(!groupList.isEmpty()){
				List<Object[]> rMap =  this.courseGroupRepository.getAllParentCourseGroups(groupList);
				
				for (Object[] objMap : rMap) {
					// [0]=CGID
					Long courseGroupid =  Long.valueOf((Integer)objMap[0]);
					// [1]=CGNAME
					String courseGroupName =  (String)objMap[2];
					Long parentCourseGroupId = null;
					// [1]=PARENTCGID
					if(objMap[1]!=null){
						parentCourseGroupId = Long.valueOf((Integer)objMap[1]);
					}
					CourseGroupView cgView = new CourseGroupView();
					cgView.setGroupName(courseGroupName);
					cgView.setId(courseGroupid);
					if(parentCourseGroupId!=null){
						cgView.setParentCourseGroupId(parentCourseGroupId);
					}
					mapCourseGroupView.put(courseGroupid, cgView);
				}
			}
		}
		catch(Exception ex) {
			log.error("Error during retrieve course groups:"+ex.getMessage(), ex);
		}
		return mapCourseGroupView;
	}

	/**
	 * @author 		noman.liaquat
	 * @param		id	CourseGroup Id
	 * @return		List of Courses
	 */
	public List<Course> getActiveCourses(Long id) {
		return this.courseRepository.getActiveCoursOfCourseGroup(id);
	}

	@Override
	public List<Course> getCourseByBusinessKey(String businessKey) {
		return this.courseRepository.findByBussinesskeyEquals(businessKey);
	}

	@Override
	public Course getCourseByGUIDRefreshCourse(String guid) {
		return this.courseRepository.findByCourseGUID(guid);
	}
}