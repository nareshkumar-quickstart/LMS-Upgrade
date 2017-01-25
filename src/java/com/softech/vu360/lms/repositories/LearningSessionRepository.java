package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LearningSession;

public interface LearningSessionRepository extends CrudRepository<LearningSession, Long>, LearningSessionRepositoryCustom {
	
	List<LearningSession> findByEnrollmentId(Long enrollmentId);
	List<LearningSession> findByEnrollmentIdAndCourseApprovalIdGreaterThan(Long enrollmentId, Long courseAppId);
	List<LearningSession> findByEnrollmentIdAndCourseApprovalIdGreaterThanAndLearnerIdAndCourseId(Long enrollmentId, Long courseAppId, Long learnerId, String courseId);
	List<LearningSession> findByLearnerIdAndCourseApprovalIdGreaterThanAndCourseIdIn(Long learnerId, Long courseAppId, List<String> courseId);
	LearningSession findTop1ByLearningSessionID(String learningSessionID);
	
	@Query(value = "SELECT top 1 * FROM LEARNINGSESSION WHERE SOURCE=:Source AND externalLMSSessionID=:externalLMSSessionID", nativeQuery=true)
	LearningSession findFirstByExternalLMSSessionIDAndSource(@Param("externalLMSSessionID") String externalLMSSessionID, @Param("Source") String Source);
	List<LearningSession> findByCourseApprovalIdInAndCourseApprovalIdGreaterThan(List<Long> courseAppId, Long greaterThanValue);
	LearningSession findFirstByEnrollmentIdAndLearnerIdAndCourseIdOrderByIdDesc(Long enrollmentId, Long learnerId, String courseId);
	LearningSession findFirstByEnrollmentIdAndCourseApprovalIdNotNullAndCourseApprovalIdGreaterThanOrderByIdDesc(Long enrollmentId, Long greaterThanValue);
	LearningSession findFirstByExternalLMSSessionIDAndSourceAndCourseId(String ExternalLMSSessionID,String Source,String courseId);
	
}
