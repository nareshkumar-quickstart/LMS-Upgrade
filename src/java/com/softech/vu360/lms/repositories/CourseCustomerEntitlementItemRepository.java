package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

/**
 * 
 * @author haider
 *
 */
public interface CourseCustomerEntitlementItemRepository extends CrudRepository<CourseCustomerEntitlementItem, Long>, CourseCustomerEntitlementItemRepositoryCustom{

	/*public List<CourseCustomerEntitlementItem> getItemsByCourseEntitlement(Long customerEntitlementId){
		ReadAllQuery query = new ReadAllQuery(CourseCustomerEntitlementItem.class);
		ExpressionBuilder builder = query.getExpressionBuilder();
		Expression finalExpr=(builder.get("customerEntitlement").get("id").equal(customerEntitlementId));
		query.setSelectionCriteria(finalExpr);
		return (List<CourseCustomerEntitlementItem>)getTopLinkTemplate().executeQuery(query);
	}*/
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlement(CustomerEntitlement customerEntitlement);
	public List<CourseCustomerEntitlementItem> findBycustomerEntitlementId(Long customerEntitlementId);
	@Query("select ccei from CourseCustomerEntitlementItem ccei where ccei.customerEntitlement.customer.id=:customerId\n"
			+ " and (ccei.customerEntitlement.endDate is null or ccei.customerEntitlement.endDate>=:endDate)\n"
			+ " and ccei.customerEntitlement.startDate<=:startDate\n"
			+ " and (ccei.customerEntitlement.allowUnlimitedEnrollments=:allowUnlimitedEnrollments\n"
			+ " or ccei.customerEntitlement.maxNumberSeats > ccei.customerEntitlement.numberSeatsUsed)\n"
			+ " and ccei.course.id=:courseId")
	public List<CourseCustomerEntitlementItem> getActiveCourseContractsFor(@Param("customerId") Long customerId,@Param("endDate") Date endDate ,@Param("startDate") Date startDate,@Param("allowUnlimitedEnrollments") boolean allowUnlimitedEnrollments,@Param("courseId") Long courseId);
	
	
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatus(@Param("custId") Long custId,@Param("courseStatus") String courseStatus);
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.courseTitle like %:title%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("title") String title);
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.bussinesskey like %:businessKey%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseBussinesskeyStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("businessKey") String businessKey);
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.keywords like %:keywords%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseKeywordsStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("keywords") String keywords);
	
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.courseTitle like %:title% and course.bussinesskey like %:businessKey%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("title") String title,@Param("businessKey") String businessKey);
	
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.courseTitle like %:title% and course.keywords like %:keywords%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseKeywordsStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("title") String title,@Param("keywords") String keywords);
	
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and course.courseStatus=:courseStatus and course.bussinesskey like %:businessKey% and course.keywords like %:keywords%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseBussinessKeyStartingWithAndCourseKeywordsStartingWith(@Param("custId") Long custId,@Param("courseStatus") String courseStatus,@Param("businessKey") String businessKey,@Param("keywords") String keywords);
	
	@Query("select a from CourseCustomerEntitlementItem a where a.customerEntitlement.customer.id=:custId and a.course.courseTitle like %:title% and a.course.keywords like %:keywords% and a.course.courseStatus=:status and a.course.bussinesskey like %:businessKey%")
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWithAndCourseKeywordsStartingWith(@Param("custId") Long custId,@Param("status") String status,@Param("title") String title , @Param("businessKey") String businessKey,@Param("keywords") String keywords);
	//public List<CourseCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWithAndCourseKeywordsStartingWith(Long custId,String courseStatus,String title,String businessKey,String keywords);
	
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementIdAndCourseIdIn(
			Long customerEntitlementIt, Long[] courseId);

	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementId(
			Long customerEntitlementId);

	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementIdAndCourseRetiredFalse(
			Long customerEntitlementId);
	
	public List<CourseCustomerEntitlementItem> findByCustomerEntitlementIdAndCourseId(
			Long customerEntitlementId, Long courseId);
	
	public CourseCustomerEntitlementItem findByCustomerEntitlementIdAndCourseGroupIdAndCourseId(
			Long customerEntitlementId, Long courseGroupId, Long courseId);

	//entitlementDAO.getCourseCustomerEntitlementItemByCourseId(customer, courseId);
	List<CourseCustomerEntitlementItem> findByCustomerEntitlement_CustomerAndCourseId(Customer customer,Long courseId);

}
