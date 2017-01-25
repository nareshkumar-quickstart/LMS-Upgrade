package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;

public interface LearnerCourseStatisticsRepository extends CrudRepository<LearnerCourseStatistics, Long> {
	
	List<LearnerCourseStatistics> findByLearnerEnrollmentLearnerIdAndLearnerEnrollmentEnrollmentStatusNotIn(Long learnerId,List<String> status);
	
	List<LearnerCourseStatistics> findByLearnerEnrollmentCourseIdInAndCompletedTrueAndCompletionDateBetweenAndLearnerEnrollmentLearnerCustomerDistributorSelfReportingFalse(List<Long> courseIds, Date startDate, Date endDate);
	
	@Query(value = "select lc.* from LEARNERCOURSESTATISTICS lc left outer join LEARNERENROLLMENT le on lc.LEARNERENROLLMENT_ID=le.id where le.id=:learnerEnrollmentId AND (lc.STATUS=:statusNotStarted OR lc.STATUS=:statusInProgress)" , nativeQuery=true)
	List<LearnerCourseStatistics> findByLearnerEnrollmentIdEqualsAndStatusEqualsOrStatusEquals(@Param("learnerEnrollmentId") Long learnerEnrollmentId, @Param("statusNotStarted") String statusNotStarted, @Param("statusInProgress") String statusInProgress);
	
	LearnerCourseStatistics findFirstByLearnerEnrollmentLearnerIdAndLearnerEnrollmentCourseId(Long learnerId, Long courseId);
	
	LearnerCourseStatistics findFirstByLearnerEnrollmentId(Long learnerEnrollmentId);
	
	List<LearnerCourseStatistics> findByLearnerEnrollmentIdIn(Collection<Long> learnerEnrollmentsIds);
	
	LearnerCourseStatistics findByLearnerEnrollment(LearnerEnrollment learnerEnrollment);

}
