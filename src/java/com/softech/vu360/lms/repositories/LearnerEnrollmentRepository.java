package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.LearnerEnrollment;

public interface LearnerEnrollmentRepository extends CrudRepository<LearnerEnrollment, Long>, LearnerEnrollmentRepositoryCustom, JpaSpecificationExecutor<LearnerEnrollment>  {
	
	public List<LearnerEnrollment> findByLearnerIdAndCourseIdAndEnrollmentStatus(Long learnerId,Long courseId,String enrollmentStatus);
	public List<LearnerEnrollment> findByCourseIdAndEnrollmentStatus(long courseid,String enrollmentStatus);
	public List<LearnerEnrollment> findByCourseIdInAndEnrollmentStatus(List<Long> courseids,String enrollmentStatus);
	public List<LearnerEnrollment> findByIdInAndEnrollmentStatus(List<Long> EnrollmentIds,String enrollmentStatus);
	public List<LearnerEnrollment> findByIdIn(Long[] EnrollmentIds);
	public List<LearnerEnrollment> findBySynchronousClassIdInAndEnrollmentDateBetween(List<Long> synchronousIds,Date fromDate,Date toDate);
	public List<LearnerEnrollment> findBySynchronousClassIdInAndCourseStatisticsCompletionDateBetween(List<Long> synchronousIds,Date fromDate,Date toDate);
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.synchronousClass.id in (:synchIds) and  le.learner.vu360User.firstName LIKE %:firstName% and le.learner.vu360User.lastName LIKE %:lastName% and le.learner.vu360User.emailAddress LIKE %:emailAddress%")
	public List<LearnerEnrollment> findSynchronousClassIdsInandfirstNameandLastNameandEmailAddress(@Param("synchIds") List<Long> synchIds,@Param("firstName") String firstName,@Param("lastName") String lastName,@Param("emailAddress") String emailAddress);
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.synchronousClass.id = :synchId and  le.learner.vu360User.firstName LIKE %:firstName% and le.learner.vu360User.lastName LIKE %:lastName% and le.learner.vu360User.emailAddress LIKE %:emailAddress%")
	public List<LearnerEnrollment> findSynchronousClassIdInandfirstNameandLastNameandEmailAddress(@Param("synchId") Long synchId,@Param("firstName") String firstName,@Param("lastName") String lastName,@Param("emailAddress") String emailAddress);
	public List<LearnerEnrollment> findByLearnerId(Long learnerId);
	public List<LearnerEnrollment> findByCustomerEntitlementId(Long customerEntitlementId);
	public List<LearnerEnrollment> findByLearnerIdAndCourseId(Long learnerId,Long courseId);
	
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and le.enrollmentStatus in (:enrollmentStatus) and le.enrollmentEndDate > :enrollmentEndDate and (le.course.courseTitle LIKE %:search% or le.course.description LIKE %:search% or le.course.keywords LIKE %:search%)")
	public List<LearnerEnrollment> findbyLearnerIdandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(@Param("learnerId") Long learnerId,@Param("enrollmentStatus") List<String> enrollmentStatus,@Param("enrollmentEndDate") Date enrollmentEndDate,@Param("search") String search);

	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and le.courseStatistics.status != (:statsStatus) and (le.enrollmentStatus = :enrollmentStatusExpired or le.enrollmentStatus = :enrollmentStatusActive) and le.enrollmentEndDate < :enrollmentEndDate and (le.course.courseTitle LIKE %:search% or le.course.description LIKE %:search% or le.course.keywords LIKE %:search%)")
	public List<LearnerEnrollment> findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(@Param("learnerId") Long learnerId,@Param("statsStatus") String statsStatus,@Param("enrollmentStatusExpired") String enrollmentStatusExpired, @Param("enrollmentStatusActive") String enrollmentStatusActive,@Param("enrollmentEndDate") Date enrollmentEndDate,@Param("search") String search);

	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and le.enrollmentStatus in (:enrollmentStatus) and le.enrollmentEndDate > :enrollmentEndDate")
	public List<LearnerEnrollment> findbyLearnerIdandEnrollmentStatusandEnrollmentEndDate(@Param("learnerId") Long learnerId,@Param("enrollmentStatus") List<String> enrollmentStatus,@Param("enrollmentEndDate") Date enrollmentEndDate);
	
	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and le.courseStatistics.status != (:statsStatus) and (le.enrollmentStatus = :enrollmentStatusExpired or le.enrollmentStatus = :enrollmentStatusActive) and le.enrollmentEndDate < :enrollmentEndDate")
	public List<LearnerEnrollment> findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDate(@Param("learnerId") Long learnerId,@Param("statsStatus") String statsStatus,@Param("enrollmentStatusExpired") String enrollmentStatusExpired, @Param("enrollmentStatusActive") String enrollmentStatusActive,@Param("enrollmentEndDate") Date enrollmentEndDate);

	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and (le.enrollmentStatus = :enrollmentStatusExpired or le.enrollmentStatus = :enrollmentStatusActive) and (le.course.courseTitle LIKE %:search% or le.course.description LIKE %:search% or le.course.keywords LIKE %:search%)")
	public List<LearnerEnrollment> findbyLearnerIdandEnrollmentStatusandCourseTitleorDescorKeywords(@Param("learnerId") Long learnerId,@Param("enrollmentStatusExpired") String enrollmentStatusExpired, @Param("enrollmentStatusActive") String enrollmentStatusActive,@Param("search") String search);

	
	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId and (le.enrollmentStatus = :expiredEnrollmentStatus or le.enrollmentStatus = :activeEnrollmentStatus)")
	public List<LearnerEnrollment> findbyLearnerIdandEnrollmentStatus(@Param("learnerId") Long learnerId,@Param("expiredEnrollmentStatus") String expiredEnrollmentStatus,@Param("activeEnrollmentStatus") String activeEnrollmentStatus);

	public List<LearnerEnrollment> findByCustomerEntitlement(CustomerEntitlement customerEntitlement);
	
	public List<LearnerEnrollment> findBySynchronousClassId(Long syncId);
	
	public List<LearnerEnrollment> findByCourseId(Long courseId);
	
	public List<LearnerEnrollment> findByLearnerIdAndCourseIdAndEnrollmentEndDateLessThan(Long learnerId,Long courseId, Date enrollmentEndDate);
	
	public List<LearnerEnrollment> findBySubscriptionId(Long subscriptionId);
	
	public LearnerEnrollment findByIdAndEnrollmentStatus(@Param("Id") Long Id,@Param("enrollmentStatus") String enrollmentStatus);
	
	public Long countByCustomerEntitlementId(Long customerEntitlementId);

	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId AND le.course.courseType IS NOT NULL AND ( le.course.courseType='Classroom Course' OR le.course.courseType='Webinar Course') AND le.synchronousClass.id IS NOT NULL")
	List<LearnerEnrollment> findByLearnerIdAndCourseType(@Param("learnerId") Long learnerId);
	
	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId AND (le.enrollmentStatus = 'EXPIRED' OR  le.enrollmentStatus = 'ACTIVE') AND le.course.courseStatus='Published' AND le.course.courseStatus != '' AND (upper(le.course.courseTitle) like %:courseTitle% or upper(le.course.description) like %:description% or upper(le.course.keywords) like %:keywords%)")
	List<LearnerEnrollment> findByLearnerIdAndCourseStatusWithTitleDescKeywords(@Param("learnerId") Long learnerId, @Param("courseTitle") String courseTitle, @Param("description") String description, @Param("keywords") String keywords);
	
	@EntityGraph(value = "LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus" , type = EntityGraphType.FETCH)
	@Query("SELECT le FROM  LearnerEnrollment le WHERE le.learner.id = :learnerId AND (le.enrollmentStatus = 'EXPIRED' OR  le.enrollmentStatus = 'ACTIVE') AND le.course.courseStatus='Published' AND le.course.courseStatus != ''")
	List<LearnerEnrollment> findByLearnerIdAndCourseStatus(@Param("learnerId") Long learnerId);
	
	//Added By Marium Saud : Method created in replacement of findByLearnerIdAndCourseIdAndEnrollmentStatus() inorder to avoid ProxyError while enrollment processes through OrgGroup Enrollment Method.
	List<LearnerEnrollment> findByLearnerIdAndCourseIdAndEnrollmentStatusAndEnrollmentEndDateNotNullAndEnrollmentEndDateAfter(Long learnerId,Long courseId,String enrollmentStatus,Date newEnrollmentDate);
}
