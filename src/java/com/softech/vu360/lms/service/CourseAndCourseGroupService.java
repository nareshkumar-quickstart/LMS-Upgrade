package com.softech.vu360.lms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
//import com.softech.vu360.lms.model.CourseCompletionCriteria;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.vo.RecommendedCourseVO;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.CourseView;
import com.softech.vu360.lms.webservice.message.lcms.ContentOwnerVO;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.TreeNode;

/**
 * CourseAndCourseGroupService defines a set of interfaces
 * to controll the business logic for interactions with the
 * Course and Course Groups within LMS.
 * 
 * @author jason
 *
 */
public interface CourseAndCourseGroupService {

	public static final String ENTITLEMENTS_CREATED_UPDATED = "EntitlementsCreatedUpdated";
	public static final String ENTITLEMENTS_TO_CREATE_UPDATE = "EntitlementsToCreateUpdate";
	public Course getCourseById(Long id);
	
	public Document getCourseDocumentByCourseId(Long id);
	
	public Course SaveCourse(WebLinkCourse course);

	public Course addCourse(Course course);
	
	public Course saveCourse(Course course);
	
	public void retireCourse(String courseIdArray[]); // [07/22/2010] LMS-6388 :: Implement Retire Course functionality instead of Delete Course
	
	public void deleteCourse(long courseIdArray[]);
	public void deleteCourse(Course course);
	
	public CourseGroup getCourseGroupById(Long id);
	public List<WebLinkCourse> findCustomCourseByName(String name,Customer customer);
	public void deleteCustomCourse(long customCourseIdArray[]);
	public WebLinkCourse getWebLinkCourseByID(long id);
	public Course getCourseByGUID(String guid);
	public List<Course> getCoursesByGUIDs(String[] guids);
	public Course getCourseByCourseId(String courseId);
	public Course getCourseByIdWithNoCache(Long id);
	public ContentOwner getContentOwnerByCustomer(Customer customer);
	public List<CourseGroup> getCourseGroupsForCourse(Course course);
//	public CourseConfiguration findCourseCompletionCriteriaForCourse(Learner learner, Course course);
	public void SaveWeblinkCourse(WebLinkCourse weblinkCourse,Customer customer, VU360User loggedInUser);
	public TrainingPlan getTrainingPlanByCourse(Course course,Customer customer);
	public TrainingPlan getTrainingPlanByEnrollment(LearnerEnrollment enrollment);
	public List<TrainingPlanAssignment> getTrainingPlanByEnrollments(List<LearnerEnrollment> enrollments);	
	public List<Map<Object, Object>> getTrainingPlansForCourseCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search);
	public List<Map<Object, Object>> getTrainingPlanCoursesForCatalogByTrainingPlanId(long learnerId,List<CustomerEntitlement> customerEntitlements, long trainingPlanId, String search);
	public List<Map<Object, Object>> getCoursesForCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search);
	public List<Map<Object, Object>> getCoursesForCatalogOrderByPCG(long learnerId,List<CustomerEntitlement> customerEntitlements, String search);
	public List<Map<Object, Object>> getCoursesForCatalogByCourseGroupID(long learnerId,List<CustomerEntitlement> customerEntitlements, String search, long courseGroupId);
	public List<Map<Object, Object>> getMiscellaneousCoursesForCatalog(long learnerId,List<CustomerEntitlement> customerEntitlements, String search);
	
	// [1/27/2011] LMS-7183 :: Retired Course Functionality II (Refactored Code)
	public Map<String, Object> getCourses (Long contentOwnerId, String courseName, String courseId, String courseType, String courseStatus, String sortColumn, int sortDirection, int pageIndex, int pageSize);
	public Map<String, Object> getCourses (Long contentOwnerId, String courseName, String courseId, String courseType,String[] courseTypes, String courseStatus, String sortColumn, int sortDirection, int pageIndex, int pageSize);
	public List<CourseGroup> getCourseGroupsByIds(long[] courseGroupIds);
	public List<Course> getCoursesByIds(long[] courseIds);
	public CourseGroup saveCourseGroup(CourseGroup courseGroup);

	public List<CourseGroup> getAllCourseGroupsByContentOwnerId(Long instructorContentOwnerId);
	public List<CourseGroup> getAllCourseGroupsByCourseId( Long courseId, String varCourseGroupName);
	public List<Course> getAllCoursesByContentOwnerId(Long instructorContentOwnerId);
	public void deleteCourseGroup(CourseGroup cg) ;
	public CourseGroup  updateCourseGroup(CourseGroup cg) ;
	public List<CourseGroup> getCourseGroupsForCourse(Course course, List<DistributorEntitlement> distributorEntitlement);
	public CourseGroup getCourseGroupByguid(String guid);
	//public List<CourseGroup> getCourseGroupsByGUIDs(String[] guids);
	// For DiscussionForum Type Courses only.
	public Course addDiscussionForumCourse(DiscussionForumCourse dfCourse);
	public Course updateDiscussionForumCourse(DiscussionForumCourse dfCourse);
	public List<Course> getCoursesByCourseGourp(CourseGroup courseGroup);
	public List<DiscussionForumCourse> getDFCCourseByInstructorId (Long instructorId);
	public HashMap<String, Integer> publishCourses(List<ContentOwnerVO> contentOwners,boolean refreshCache);
	public  Course  loadForUpdateCourse(long id);
	public  CourseGroup  loadForUpdateCourseGroup(long id);	
	
	public List<List<TreeNode>> getCourseGroupTreeList (List<CourseGroup> courseGroupList, boolean enableNodes);
	
	// LMS-6504
	public boolean removeCourseCourseGroupRelationship ( Long courseId, Long[] courseGroupIds );
	public List<CourseGroup> getCourseGroupsByContentOwner (Long contentOwnerId, String courseGroupName, String keyword);
	public boolean addCourseCourseGroupRelationship ( Long courseId, Long[] courseGroupIds );
        
	public List<CourseGroup> getCourseGroupsByBusinessKeys(String[] businessKeys);
	public List<Course> getCoursesByCourseConfigurationTemplate(long ccTemplateId);
	
	public List<Course> getCourses (Long contentOwnerId, String courseName, String bussinesskey, Long[] courseIds);
	public List<RecommendedCourseVO> getRecommendedCoursesFromSF (Brander brand, VU360User user);
	public List<Language> getAllLanguages();
    public List<Course> refreshCoursesCache(String[] courseGUIDs);
    public List<CourseView> getEntitlementsByCourseGroupId(Learner learner,Long courseGroupId, boolean isTrainingPlan, boolean isMiscellaneous);
    
   // public List<TreeNode> searchCourseGroupsByDistributor(Distributor distributor, Set<CourseGroup> courseGroupSet, String title,
    //        String entityId, String keywords, String contractType);
   // public Long[] getCourseGroupIDArrayForDistributor(Distributor distributor);

	List<List<TreeNode>> getCourseGroupTreeList2(
			List<CourseGroup> courseGroupList, boolean enableNodes);
	public Map<Long,CourseGroupView> getAllParentCourseGroups(List<CourseGroupView> cgViewList, Map<Long, CourseGroupView> cgViewMap);
	
	/**
	 * @author 		noman.liaquat
	 * @param		id	CourseGroup Id
	 * @return		List of Courses
	 */
	public List<Course> getActiveCourses(Long id);
	
	public List<Course> getCourseByBusinessKey(String businessKey);
}