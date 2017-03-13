package com.softech.vu360.lms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.TrainingPlanCourseView;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.TreeNode;

/**
 * EntitlementService defines the set of interfaces 
 * to control the interactions and business logic
 * of all entitlements and learner enrollments within
 * the LMS.
 * 
 * @author jason
 * @modifed Faisal A. Siddiqui
 */
public interface EntitlementService {
	
	public LearnerEnrollment getLearnerEnrollmentById(Long id);
	
	public OrgGroupEntitlement getOrgGroupEntitlementById(long id);
	
	public OrgGroupEntitlement saveOrgGroupEntitlement(OrgGroupEntitlement orgGroupEntitlement);
	public CustomerEntitlement addCustomerEntitlement(Customer customer, CustomerEntitlement customerEntitlement);
	public CustomerEntitlement addCustomerEntitlement(CustomerEntitlement customerEntitlement);
	public CustomerEntitlement createUnlimitedCustomerEntitlement(Customer customer, List<Course> courses);
	public List<LearnerEnrollment> getLearnerEnrollmentsForLearner(Learner l);
	public List<LearnerEnrollment> getFreshLearnerEnrollmentsForLearner(Learner l);
    public List<LearnerEnrollment> getActiveLearnerEnrollmentsByLearner(Learner l, String search);
    public List<LearnerEnrollment> getLearnerEnrollmentsByLearner(Learner l, String search);
    /*LMS-13793 - getLearnerEnrollmentsForReportingfields */
    public List<LearnerEnrollment> getLearnerEnrollmentsForReportingfields(Learner l);
    public List<LearnerEnrollment> getExpiredLearnerEnrollmentsByLearner(Learner l, String search);
	public LearnerEnrollment getLearnerEnrollmentsForLearner(Learner learner,Course course);
	public List<LearnerEnrollment> getLearnerEnrollmentsAgainstCustomerEntitlement(CustomerEntitlement cE);
	public List<CustomerEntitlement> getActiveCustomerEntitlementsForCustomer(Customer customer);
	public CustomerEntitlement customerHasActiveEntitlementFor(Customer customer, Course course);
	
	public List<CustomerEntitlement> getCustomerEntitlementsForLearner(Learner l);
	
	public List<OrgGroupEntitlement> getOrgGroupEntitlementsForLearner(Learner l);
	
	public CustomerEntitlement getCustomerEntitlementById(Long id);	
	public DistributorEntitlement getDistributorEntitlementById(Long id);

	public void addOrgGroupEntitlementInCustomerEntitlement(
			CustomerEntitlement customerEntitlement,List<OrgGroupEntitlement> orgGroupEntitlements); 
		
	public List<CustomerEntitlement> getAllCustomerEntitlements(Customer customer);	
	public List<CustomerEntitlement> getAllCustomerEntitlementsExcept(Long[] entitlmentIdsNotToGet);
	public List<DistributorEntitlement> getAllDistributorEntitlements(Distributor distributor);
	
	public List<Course> getAllCoursesByEntitlement(Long customerID);
	public List<Course> getCoursesByEntitlement(Long customerID, String searchCriteria);
	public List<Course> getCoursesByEntitlement(Customer customer, String courseName,String courseGUID,String keywords );
	
	public List<Course> getAllCoursesByEntitlement(Long customerId,String courseName, String courseType);
	
	public List<Course> getCoursesByNameAndCourseType(Customer customer, String courseName, String courseType);
	
	public List<CustomerEntitlement> getCustomerEntitlementsByCourse(Customer customer, String searchCriteria);
	public CustomerEntitlement saveCustomerEntitlement(CustomerEntitlement ce,List<OrgGroupEntitlement> orgGroupEntitlements);
	public DistributorEntitlement saveDistributorEntitlement(DistributorEntitlement distributorEntitlement);

	public boolean isCustomerEntitlementExistByName(String customerEntitlementName);

	public List<DistributorEntitlement> findAllDistributorEntitlementsByDistributor(long distributorId);
	public List<Course> findCoursesByDistributor(long courseGroupIdArray[] , String courseName, String courseGUID, String keywords,String searchCriteria);
	public List<CourseGroup> findCourseGroupsByDistributor(long courseGroupIdArray[] , String courseGroupName);
	public List<CourseGroup> findCourseGroups( String courseGroupName);
	public DistributorEntitlement getDistributorEntitlementByName(String name,long distributorId);
	public List<OrgGroupEntitlement> getOrgGroupsEntilementsForCustomerEntitlement(CustomerEntitlement customerEntitlement);
	public OrgGroupEntitlement getOrgGroupEntitlementByOrgGroupId(Long id);
	public OrgGroupEntitlement getMaxAvaiableOrgGroupEntitlementByLearner(Learner learner,long entitlementId);
	public long[] getCourseIDArrayForDistributor(Distributor distributor);
	public void deleteOrgGroupEntitlementInCustomerEntitlement(List<OrgGroupEntitlement> orgGroups);
	public List<Course> findCoursesByCustomer(long courseIdArray[] , String courseName, String courseGUID, String keywords,String searchCriteria);
	 //commenting following function call because findCoursesByDistributor() is in used for older forms and which are not in use
	//public HashMap<Long, Course> findCoursesByDistributor(Distributor distributor , String courseName, String courseGUID, String keywords,String searchCriteria);
	
	public void deleteCustomerEntitlement( CustomerEntitlement objCustomerEntitlement);
	public List<CustomerEntitlement> getCustomerEntitlementsByCourseId(Long courseId);
		
	@Deprecated
	public List<CustomerEntitlement> searchCoursesForEnrollment(Customer customer, String courseName,String courseId , String entitlementName , String date, int resultSetLimit ) ;
	public CustomerEntitlement readCustomerEntitlementById(Long id);
	public List<EnrollmentCourseView> getCoursesForEnrollmentByCustomer(Customer customer , String courseName,String courseId , String entitlementName , Date date, Long[] customerEntitlementIds, int resultSetLimit) ;
	public List<TrainingPlanCourseView> getEntitledCoursesFromTrainingPlanCourses(List<TrainingPlanCourse> trainingPlanCourseList, int resultSetLimit);
	public LearnerEnrollment getLearnerEnrollmentsForLearner(Learner learner,long courseId);
	public List<CustomerEntitlement> getCustomerEntitlementsByCourseId(Customer customer, Long courseId);
    public List<CustomerEntitlement> getActiveCustomerContractByCourseId(Customer customer, Long courseId);
    public List<EnrollmentCourseView> getCoursesForTrainingPlanByCustomer(Customer customer,String courseName, String courseId, String entitlementName,Date date, Long[] customerEntitlementIds, int resultSetLimit);
	public CourseCustomerEntitlement addCourseCustomerEntitlementItemsIfNotExist(CourseCustomerEntitlement customerEntitlement, 
    		List<Course> courses);
	public CustomerEntitlement addUnlimitedCustomerEntitlementIfNotExist(Customer customer, List<Course> courses);
	public DistributorEntitlement getUnlimitedDistributorEntitlement(Distributor distributor, List<Course> courses, boolean createIfNotExists);
	public DistributorEntitlement createUnlimitedDistributorEntitlement(Distributor distributor, List<Course> courses);
	public DistributorEntitlement addDistributorEntitlementCourseGroupForCoursesIfNotExist(DistributorEntitlement distributorEntitlement, 
    		List<Course> courses);
	public  CustomerEntitlement  loadForUpdateCustomerEntitlement(long id);
	public  DistributorEntitlement  loadForUpdateDistributorEntitlement(long id);
	public OrgGroupEntitlement   loadForUpdateOrgGroupEntitlement(long id);
	public List<EnrollmentCourseView> getEnrollmentCourseViewsByCustomerAndCourseIds(List<CustomerEntitlement> customerEntitlementList, Long[] courseIds);
	public List<EnrollmentCourseView> getEnrollmentCourseViewsByCustomerCourseAndCourseGroupIds(List<CustomerEntitlement> customerEntitlementList, Long courseId, Long courseGroupId);

	// [08/03/2010] LMS-5544 :: Remove Contract
	public List<CustomerEntitlement> getSimilarCustomerEntitlements (Long customerEntitlementId, Long totalEnrollments );
	public void moveCustomerEnrollments(Customer customer, Long sourceContractId, Long destinationContractId);
	public void removeEntitlementsWithEnrollments(Customer customer, CustomerEntitlement customerEntitlement);
	
	// [09/06/2010] LMS-6997 :: Org. Group Entitlement: update seatsUsed after selfEnrollment 
	public void updateSeatsUsed(CustomerEntitlement cE, OrgGroupEntitlement orgGroupEntitlement, int seatCountToAdd);

	public List<CourseCustomerEntitlementItem> getItemsByEntitlement(CourseCustomerEntitlement cce);
	//LMS-15124 - Created overload function getItemsByEntitlement() that only pull active courses if pass it to 'true'
	public List<CourseCustomerEntitlementItem> getItemsByEntitlement(CourseCustomerEntitlement cce, boolean removeRetiredCourse);
	public List<CourseGroupCustomerEntitlementItem> getItemsByEntitlement(CourseGroupCustomerEntitlement cgce);
	public Set<Course> getCoursesByCourseCustomerEntitlement(CourseCustomerEntitlement cce);
	public CourseCustomerEntitlementItem findEntitlementItem(CourseCustomerEntitlement cce, Long courseGroupId,Long courseId);
	public List<CourseCustomerEntitlementItem> findEntitlementItemsByCourse(CourseCustomerEntitlement cce, Long courseId);
	public CourseCustomerEntitlementItem addEntitlementItem(CourseCustomerEntitlement ce, Course course, CourseGroup courseGroup);
	public List<CourseGroupCustomerEntitlementItem> addEntitlementItems(CourseGroupCustomerEntitlement cgce, List<CourseGroup> courseGroups);
	public Set<Course> getUniqueCourses(CustomerEntitlement ce);
	public CustomerEntitlement getSystemManagedContract(Customer customer);
	public Long getEnrollmentCountByCustomerEntitlementId (Long customerEntitlementId);
	public List<TreeNode> getEntitlementCourseGroupTreeNode(Object object,String title, String id, String keywords, String contractType, Map context, Customer customer);
	public List<TreeNode> getEntitlementItemsTreeStack(CustomerEntitlement cE,Map context);
	public List<TreeNode> getTreeForContract(CustomerEntitlement entitlement, Set<CourseGroup> contractCourseGroups, String searchBy, String keyword);
    public List<TreeNode> getTreeForContract(CustomerEntitlement entitlement, Set<CourseGroup> contractCourseGroups);
	
	public HashMap<CourseGroup, List<Course>> findCourseInCustomerEntitlementsBySearchCriteria(Customer customer, String courseName, String courseGUID, String keywords);

	public void deleteCourseEntitlementItems(List<CourseCustomerEntitlementItem> entitlementItems);
        public void deleteEntitlementItems(CourseGroupCustomerEntitlement cgce, long[] courseGroupIds);

	//[8/31/2010] LMS-6931 :: Code re-factored to get Course Group for Enrolled Courses
	public List<CourseGroupCustomerEntitlementItem> findCourseGroupEntitlementItems(CourseGroupCustomerEntitlement cgce, Long courseId);
	
	public CustomerEntitlement assignCourseIntoSystemManagedContract(Course dbCourse, Customer customer);
	public List<CourseCustomerEntitlementItem> addCourseEntitlementItems(List<CourseCustomerEntitlementItem> courseGroupCustomerEntitlementItems);
 	public List<CourseGroupCustomerEntitlementItem> addCourseGroupEntitlementItem(List<CourseGroupCustomerEntitlementItem> items);
 	public List<CustomerEntitlement> getAllCustomerEntitlementsForGrid(Customer customer);
 	// [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses irrespective of contract and enrollments availability
	public Map<Object, Object> getCoursesForEnrollment (Long customerId, String courseName, String courseCode, String contractName, int pageIndex, int pageSize);
	
//	 issue LMS 8256
	@Deprecated
	public EnrollmentCourseView getEnrollmentCourseViewByCourseId(long courseId);
	public HashMap<CourseGroup, List<Course>> findCoursesAndCourseGroupsByDistributor(Distributor distributor,String courseName, String entityId, String keywords, String contractType);
	public HashMap<CourseGroup, List<Course>> findAvailableCourses(Distributor distributor,String courseName, String entityId, String keywords);
	public HashMap<CourseGroup, List<Course>> findAvailableCourseGroups(Distributor distributor,String courseName, String entityId, String keywords);
	
	public HashMap<CourseGroup, List<Course>> findAvailableCourses(Distributor distributor, List<Long> courseIdList);
	public HashMap<CourseGroup, List<Course>> findAvailableCourseGroups(Distributor distributor, List<Long> courseGroudIdList);

        /**
         * Removes the customer entitlement from the distributor.
         * @param distributorEntitlement 
         */
        public void removeDistributorEntitlement(DistributorEntitlement distributorEntitlement);

        /**
         * Prepares hierarchy of course groups for the given distributor.
         * @param title
         * @param entityId
         * @param keywords
         * @return 
         */
        public List<TreeNode> searchCourseGroups(String title, 
                String entityId, String keywords);

    public List<TreeNode> searchCourseGroupsByDistributor(Distributor distributor, Set<CourseGroup> courseGroupSet, String title,
                                    String entityId, String keywords, String contractType);
        
        /**
         * Searches the Course Groups based on the search criteria or if the
         * search criteria is found to be empty returns all course groups.
         * 
         * @param courseGroupName 
         * @param entityId 
         * @param keywords 
         * @return 
         */
        public  Map<CourseGroup, List<Course>> findCourseGroups(String courseGroupName, String entityId, String keywords);

        /**
         * Arranges the course groups associated with distributor entitlement in a tree.
         * @param distributorEntitlement 
         * @param distributorEntitlement 
         * @return 
         */
        public List<TreeNode> getCourseGroupTreeForDistributorEntitlement(DistributorEntitlement distributorEntitlement, 
                Set<CourseGroup> contractCourseGroups);
        
        public Date getMaxDistributorEntitlementEndDate(Distributor distributor);

        public Long[] getCourseGroupIDArrayForDistributor(Distributor distributor);
        public String getCoursePathToCourseGroup(int nearestCourseGroupId) throws Exception;
        public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByOrgGrpIds(List<Long> orgGrpIds);
        public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByLearnerIds(Long learnerIds[]);
        public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByLearnerGroupIds(List<Long> learnerGrpIds);
        public boolean isEnforceOrgGroupEnrollmentRestrictionEnable(Customer customer);
}
