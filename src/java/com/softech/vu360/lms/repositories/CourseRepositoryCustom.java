package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;

public interface CourseRepositoryCustom  {
	public List<Course> findCoursesByContentOwner(Collection<Long> coids, String courseTitle, String courseStatus, Boolean retired, String courseId);
	public List<Map<Object, Object>> findCatalogSPById(Long learner_Id,Long Customer_id , String search,String sp_Name);
	public List<Course> findBycourseTitle(List<Long> courseIdArray, String courseName, String courseGUID, String keywords,String searchCriteria);
	public List<Map<Object, Object>> findByCustomerIdBycourseNameByCourseIdByEntitlementIdByExpiryDate(Long customerId, String courseName,String courseId, String entitlementName, Date date, Long[] customerEntitlementIds);
	public List<Map<Object, Object>> findByCustomerEntitlementsByCourseIds(List<CustomerEntitlement> customerEntitlements,	Long[] trainingPlanCourseIds);
	public List<Map<Object, Object>> getByCourseAndCourseGroupId(List<CustomerEntitlement> customerEntitlements, 
			Long trainingPlanCourseId, Long courseGroupId);
	
	public Long[] getPublishedEntitledCourses(Long customerID);
	public Long[] getPublishedEntitledCourseIdArray(Long distributorId);
	public List<Map<Object, Object>> findAvailableCourses(Distributor distributor,String courseName, String entityId, String keywords);
	public List<Map<Object, Object>> findAvailableCourses(Long distributorId, List<Long> courseIdList);
	List<Course> findCoursesByCustomer(Long courseIdArray[], String courseTitle, String courseGUID, String keywords,String searchCriteria);
	List<Course> findCoursesByCustomer(Long courseIdArray[], String courseTitle, String courseType);
	public List<Map<Object, Object>> getCoursesForEnrollmentByCourseIdsAndCustomer(Long customerId, List<Long> trainingPlanCourseIds);
	public List<Course> getCourseUnderAlertTriggerFilter(Long alertTriggerId, String courseName, String courseType);
	
	public List<Object[]> getEntitlementsByCourseGroupId(Long learner_Id, Long courseGroupId);
	public List<Map<Object, Object>> getEntitlementsByTrainingPlanId(Long learner_Id, Long trainingPlanId);
}