package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItemPK;
import com.softech.vu360.lms.model.CustomerEntitlement;

/**
 * 
 * @author haider
 *
 */
public interface CourseGroupCustomerEntitlementItemRepository extends CrudRepository<CourseGroupCustomerEntitlementItem, CourseGroupCustomerEntitlementItemPK>,CourseGroupCustomerEntitlementItemRepositoryCustom{

	/*public List<CourseCustomerEntitlementItem> getItemsByCourseEntitlement(Long customerEntitlementId){
		ReadAllQuery query = new ReadAllQuery(CourseCustomerEntitlementItem.class);
		ExpressionBuilder builder = query.getExpressionBuilder();
		Expression finalExpr=(builder.get("customerEntitlement").get("id").equal(customerEntitlementId));
		query.setSelectionCriteria(finalExpr);
		return (List<CourseCustomerEntitlementItem>)getTopLinkTemplate().executeQuery(query);
	}*/
	public List<CourseGroupCustomerEntitlementItem> findByCustomerEntitlement(CustomerEntitlement customerEntitlement);
	public List<CourseGroupCustomerEntitlementItem> findByCustomerEntitlementId(Long customerEntitlementId);
	
	/*
	try {
	Expression expression = expressionBuilder.get("customerEntitlement").get("customer").get("id").equal( customer.getId() )
						.and( expressionBuilder.get("customerEntitlement").get("endDate").isNull()
						.or(expressionBuilder.get("customerEntitlement").get("endDate").greaterThanEqual(now)))
						.and(expressionBuilder.get("customerEntitlement").get("startDate").lessThanEqual(now))
						.and( expressionBuilder.get("customerEntitlement").get("allowUnlimitedEnrollments").equal(true) 
						.or(expressionBuilder.get("customerEntitlement").get("maxNumberSeats").greaterThan(expressionBuilder.get("customerEntitlement").get("numberSeatsUsed")) ));
		expression = expression.and(expressionBuilder.get("courseGroup").anyOf("courses").get("id").equal(courseId));
		courseGroupItems = (List<CourseGroupCustomerEntitlementItem>)getTopLinkTemplate().executeQuery(raq);
	}
	catch (Exception e) {
		log.debug("Error occurred while fetching Active Contracts for Customer:"+ customer.getId());
	}
	//entitlementDAO.getActiveCourseGroupContractsFor(customer, courseId);
	@Query(value ="SELECT DISTINCT cce.COURSEGROUP_ID, cce.CUSTOMERENTITLEMENT_ID "
			+ "FROM COURSE_COURSEGROUP ccg, COURSEGROUP cg, COURSE c, COURSEGROUP_CUSTOMERENTITLEMENT cce,"
			+ "CUSTOMERENTITLEMENT ce, CUSTOMER cus "
			+ "WHERE ((((((cus.ID = :customerId) AND ((ce.ENDDATE IS NULL) OR (ce.ENDDATE >= :curDate))) "
			+ "AND (ce.STARTDATE <= :curDate)) AND ((ce.ALLOWUNLIMITEDENROLLMENTS = 1) OR (ce.SEATS > ce.NUMBERSEATSUSED))) "
			+ "AND (c.ID = :courseId)) AND ((((ce.ID = cce.CUSTOMERENTITLEMENT_ID) AND (cus.ID = ce.CUSTOMER_ID)) "
			+ "AND (cg.ID = cce.COURSEGROUP_ID)) AND ((ccg.COURSEGROUP_ID = cg.ID) AND (c.ID = ccg.COURSE_ID))))",nativeQuery=true)
*/
	@Query("SELECT s FROM CourseGroupCustomerEntitlementItem s inner join s.courseGroup.courses c WHERE s.customerEntitlement.customer.id=:customerId"
			+ " AND (s.customerEntitlement.endDate is null OR s.customerEntitlement.endDate>= :curDate)"
			+ " AND s.customerEntitlement.startDate<= :curDate AND "
			+ "(s.customerEntitlement.allowUnlimitedEnrollments=1 OR s.customerEntitlement.maxNumberSeats > s.customerEntitlement.numberSeatsUsed)"
			+ " AND c.id=:courseId")
	List<CourseGroupCustomerEntitlementItem> getActiveCourseGroupContractsFor(@Param("customerId") Long customerId,@Param("curDate") Date curDate ,@Param("courseId") Long courseId);
	
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t5,\n"
			+ " COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2, CUSTOMERENTITLEMENT t1,\n"
			+ " CUSTOMER t0 WHERE (((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = UPPER(:status)))\n"
			+ " AND ((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID)) AND\n"
			+ " (t4.ID = t2.COURSEGROUP_ID)) AND ((t5.COURSEGROUP_ID = t4.ID) AND (t3.ID = t5.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatus(@Param("custId") Long custId,@Param("status") String status);
	
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t11,\n"
			+ " COURSE_COURSEGROUP t10, COURSE_COURSEGROUP t9, COURSE_COURSEGROUP t8, COURSE t7,\n"
			+ " COURSE t6, COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2,\n"
			+ " CUSTOMERENTITLEMENT t1, CUSTOMER t0 WHERE ((((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status))\n"
			+ " AND (UPPER(t5.NAME) LIKE %:title%)) AND (UPPER(t6.BUSSINESSKEY) LIKE %:businesskey%)) AND (UPPER(t7.KEYWORDS) LIKE %:keywords%))\n"
			+ " AND (((((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID)) AND (t4.ID = t2.COURSEGROUP_ID))\n"
			+ " AND ((t8.COURSEGROUP_ID = t4.ID) AND (t3.ID = t8.COURSE_ID))) AND ((t9.COURSEGROUP_ID = t4.ID)\n"
			+ " AND (t5.ID = t9.COURSE_ID))) AND ((t10.COURSEGROUP_ID = t4.ID) AND (t6.ID = t10.COURSE_ID)))\n"
			+ " AND ((t11.COURSEGROUP_ID = t4.ID) AND (t7.ID = t11.COURSE_ID))))",nativeQuery=true)
	
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKeyAndKeywords(@Param("custId") Long custId,@Param("status") String status,@Param("title") String title,@Param("businesskey") String businesskey,@Param("keywords") String keywords);
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t7, COURSE_COURSEGROUP t6,\n"
			+ " COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2, CUSTOMERENTITLEMENT t1, CUSTOMER t0\n"
			+ " WHERE ((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status)) AND (UPPER(t5.NAME) LIKE %:title%)) AND\n"
			+ " (((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID)) AND (t4.ID = t2.COURSEGROUP_ID)) AND\n"
			+ " ((t6.COURSEGROUP_ID = t4.ID) AND (t3.ID = t6.COURSE_ID))) AND ((t7.COURSEGROUP_ID = t4.ID) AND (t5.ID = t7.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseTitle(@Param("custId") Long custId,@Param("status") String status,@Param("title") String title);
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t7,\n"
			+ " COURSE_COURSEGROUP t6, COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2,\n"
			+ " CUSTOMERENTITLEMENT t1, CUSTOMER t0 WHERE ((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status))\n"
			+ " AND (UPPER(t5.BUSSINESSKEY) LIKE %:businesskey%)) AND (((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID))\n"
			+ " AND (t4.ID = t2.COURSEGROUP_ID)) AND ((t6.COURSEGROUP_ID = t4.ID) AND (t3.ID = t6.COURSE_ID)))\n"
			+ " AND ((t7.COURSEGROUP_ID = t4.ID) AND (t5.ID = t7.COURSE_ID))))",nativeQuery=true)
	
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseBussinessKey(@Param("custId") Long custId,@Param("status") String status,@Param("businesskey") String businesskey);
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t7, COURSE_COURSEGROUP t6,\n"
			+ " COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2, CUSTOMERENTITLEMENT t1,\n"
			+ " CUSTOMER t0 WHERE ((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status)) AND (UPPER(t5.KEYWORDS) LIKE %:keywords%))\n"
			+ " AND (((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID)) AND (t4.ID = t2.COURSEGROUP_ID))\n"
			+ " AND ((t6.COURSEGROUP_ID = t4.ID) AND (t3.ID = t6.COURSE_ID)))\n"
			+ " AND ((t7.COURSEGROUP_ID = t4.ID) AND (t5.ID = t7.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseKeywords(@Param("custId") Long custId,@Param("status") String status,@Param("keywords") String keywords);
	
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t9, COURSE_COURSEGROUP t8,\n"
			+ " COURSE_COURSEGROUP t7, COURSE t6, COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2,\n"
			+ " CUSTOMERENTITLEMENT t1, CUSTOMER t0 WHERE (((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status)) AND (UPPER(t5.NAME) LIKE %:title%))\n"
			+ " AND (UPPER(t6.BUSSINESSKEY) LIKE %:businesskey%)) AND ((((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID))\n"
			+ " AND (t4.ID = t2.COURSEGROUP_ID)) AND ((t7.COURSEGROUP_ID = t4.ID) AND (t3.ID = t7.COURSE_ID)))\n"
			+ " AND ((t8.COURSEGROUP_ID = t4.ID) AND (t5.ID = t8.COURSE_ID))) AND\n"
			+ " ((t9.COURSEGROUP_ID = t4.ID) AND (t6.ID = t9.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKey(@Param("custId") Long custId,@Param("status") String status,@Param("title") String title,@Param("businesskey") String businesskey);
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t9, COURSE_COURSEGROUP t8, COURSE_COURSEGROUP t7,\n"
			+ " COURSE t6, COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2, CUSTOMERENTITLEMENT t1, CUSTOMER t0 WHERE\n"
			+ " (((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status)) AND (UPPER(t5.NAME) LIKE %:title%)) AND (UPPER(t6.KEYWORDS) LIKE %:keywords%)) AND\n"
			+ " ((((((t1.ID = t2.CUSTOMERENTITLEMENT_ID) AND (t0.ID = t1.CUSTOMER_ID)) AND (t4.ID = t2.COURSEGROUP_ID)) AND\n"
			+ " ((t7.COURSEGROUP_ID = t4.ID) AND (t3.ID = t7.COURSE_ID))) AND ((t8.COURSEGROUP_ID = t4.ID) AND (t5.ID = t8.COURSE_ID)))\n"
			+ " AND ((t9.COURSEGROUP_ID = t4.ID) AND (t6.ID = t9.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseTitleAndCourseKeywords(@Param("custId") Long custId,@Param("status") String status,@Param("title") String title,@Param("keywords") String keywords);
	
	@Query(value="SELECT DISTINCT t2.* FROM COURSE_COURSEGROUP t9, COURSE_COURSEGROUP t8,\n"
			+ " COURSE_COURSEGROUP t7, COURSE t6, COURSE t5, COURSEGROUP t4, COURSE t3, COURSEGROUP_CUSTOMERENTITLEMENT t2,\n"
			+ " CUSTOMERENTITLEMENT t1, CUSTOMER t0 WHERE (((((t0.ID = :custId) AND (UPPER(t3.COURSESTATUS) = :status)) AND\n"
			+ " (UPPER(t5.BUSSINESSKEY) LIKE %:businesskey%)) AND (UPPER(t6.KEYWORDS) LIKE %:keywords%)) AND ((((((t1.ID = t2.CUSTOMERENTITLEMENT_ID)\n"
			+ " AND (t0.ID = t1.CUSTOMER_ID)) AND (t4.ID = t2.COURSEGROUP_ID)) AND ((t7.COURSEGROUP_ID = t4.ID) AND (t3.ID = t7.COURSE_ID)))\n"
			+ " AND ((t8.COURSEGROUP_ID = t4.ID) AND (t5.ID = t8.COURSE_ID))) AND ((t9.COURSEGROUP_ID = t4.ID)\n"
			+ " AND (t6.ID = t9.COURSE_ID))))",nativeQuery=true)
	public List<CourseGroupCustomerEntitlementItem> findByCustomerIdAndStatusAndCourseBussinessKeyAndCourseKeywords(@Param("custId") Long custId,@Param("status") String status,@Param("businesskey") String businesskey,@Param("keywords") String keywords);

	public List<CourseGroupCustomerEntitlementItem> findByCustomerEntitlementIdAndCourseGroupIdIn(
			long customerEntitlementId, long courseGroupIds[]);
	
	public List<CourseGroupCustomerEntitlementItem> findByCourseGroupCoursesIdAndCustomerEntitlementCustomerId(Long courseId, Long customerId);
	
	public List<CourseGroupCustomerEntitlementItem> findByCustomerEntitlementIdAndCourseGroupCoursesId(Long customerEntitlementId, Long courseId);
	
	public List<CourseGroupCustomerEntitlementItem> findByCustomerEntitlementCustomerIdAndCourseGroupCoursesCourseStatus(Long customerId, String courseStatus);
	
	
	
}
