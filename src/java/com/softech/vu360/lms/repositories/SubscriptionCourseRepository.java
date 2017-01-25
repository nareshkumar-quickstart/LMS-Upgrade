package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.SubscriptionCourse;

/**
 * @author ramiz.uddin
 */

public interface SubscriptionCourseRepository extends
		CrudRepository<SubscriptionCourse, Long>, SubscriptionCourseRepositoryCustom {

	public List<SubscriptionCourse> findBySubscriptionSubscriptionCode(
			String subscriptionCode);

	public List<SubscriptionCourse> findBySubscriptionId(Long subscriptionId);

	public List<SubscriptionCourse> findBySubscriptionCustomerEntitlementId(
			Long id);

	@Query(value = "SELECT S.SUBSCRIPTION_NAME, S.ID, SC.COURSE_ID , "
			+ "C.NAME AS 'COURSENAME' , C.COURSETYPE , "
			+ "(SELECT TOP 1 LE.ID FROM LEARNERENROLLMENT LE WHERE LE.COURSE_ID = SC.COURSE_ID AND LE.LEARNER_ID = :learnerId) AS 'ENROLLMENTID'"
			+ " FROM SUBSCRIPTION S, SUBSCRIPTION_COURSE SC, SUBSCRIPTION_USER SU, COURSE C"
			+ " WHERE S.ID = SC.SUBSCRIPTION_ID AND S.ID = SU.SUBSCRIPTION_ID AND SC.SUBSCRIPTION_ID = SU.SUBSCRIPTION_ID AND SC.COURSE_ID = C.ID AND C.NAME LIKE %:coursesearch% AND S.ID = :subscriptionId AND SU.VU360USER_ID = :userId", nativeQuery = true)
	public List<Object[]> getUserSubscriptionCourses(
			@Param("learnerId") long learnerId, @Param("userId") long userId,
			@Param("subscriptionId") long subscriptionId,
			@Param("coursesearch") String coursesearch);

	@Query(value = "SELECT S.SUBSCRIPTION_NAME, S.ID, SC.COURSE_ID , "
			+ "C.NAME AS 'COURSENAME' , C.COURSETYPE , "
			+ "(SELECT TOP 1 LE.ID FROM LEARNERENROLLMENT LE WHERE LE.COURSE_ID = SC.COURSE_ID AND LE.LEARNER_ID = :learnerId) AS 'ENROLLMENTID'"
			+ " FROM SUBSCRIPTION S, SUBSCRIPTION_COURSE SC, SUBSCRIPTION_USER SU, COURSE C"
			+ " WHERE S.ID = SC.SUBSCRIPTION_ID AND S.ID = SU.SUBSCRIPTION_ID AND SC.SUBSCRIPTION_ID = SU.SUBSCRIPTION_ID AND SC.COURSE_ID = C.ID AND S.ID = :subscriptionId AND SU.VU360USER_ID = :userId", nativeQuery = true)
	public Map<Object, Object> getUserSubscriptionCourses(
			@Param("learnerId") long learnerId, @Param("userId") long userId,
			@Param("subscriptionId") long subscriptionId);

	@Query(value = "SELECT S.ID, S.SUBSCRIPTION_NAME FROM SUBSCRIPTION_USER SU, SUBSCRIPTION S WHERE UPPER(S.SUBSCRIPTION_STATUS)  IN ('ACTIVE', 'ONHOLD') AND S.ID = SU.SUBSCRIPTION_ID AND SU.VU360USER_ID = :userId ORDER BY S.SUBSCRIPTION_NAME ASC", nativeQuery = true)
	public Map<Object, Object> getUserSubscriptions(@Param("userId") long userId);

}
