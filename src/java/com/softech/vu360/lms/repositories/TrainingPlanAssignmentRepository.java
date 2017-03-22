package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;

public interface TrainingPlanAssignmentRepository  extends CrudRepository<TrainingPlanAssignment, Long> {

	//public TrainingPlan getTrainingPlanByEnrollment(LearnerEnrollment enrollment)

	TrainingPlanAssignment findBylearnerEnrollments(LearnerEnrollment enrollment);

	@Query("SELECT TPA FROM TrainingPlanAssignment TPA " +
			"LEFT JOIN FETCH TPA.trainingPlan " +
			"LEFT JOIN FETCH TPA.learnerEnrollments LE WHERE LE IN :enrollments")
	List<TrainingPlanAssignment> findDistinctByLearnerEnrollmentsIn(@Param("enrollments") List<LearnerEnrollment> enrollments);
	
	@EntityGraph(value = "TrainingPlanAssignment.GraphLearnerEnrollmentWithTrainingPlan" , type = EntityGraphType.FETCH)
	List<TrainingPlanAssignment> findByTrainingPlanId(long trainingPlanId);
	
	public List<TrainingPlanAssignment> findByTrainingPlanIdIn(long[] trainingPlanIds);
	@Query(value="select Distinct t.ID,t.TRAINIINGPLAN_ID from  TRAINIINGPLANASSIGNMENT t,LEARNERENROLLMENT l,TRAININGPLANASSIGNMENT_LEARNERENROLLMENT tl where t.TRAINIINGPLAN_ID=? and ((tl.TRAININGPLANASSIGNMENT_ID = t.ID) and (tl.LEARNERENROLLMENT_ID = l.ID) and (l.ENROLLMENTDATE >= ? and l.ENROLLMENTDATE <= ?))",nativeQuery=true)
	public List<TrainingPlanAssignment> getTraingPlanAssignmentsByTraingPlanIdAndDate(Long trainingPlaId,Date startDate,Date endDate);
	
	public List<TrainingPlanAssignment> findByLearnerEnrollmentsIdIn(Collection<Long> learnerEnrollmentsIds);
	
	@EntityGraph(value = "TrainingPlanAssignment.GraphLearnerEnrollmentWithTrainingPlan" , type = EntityGraphType.FETCH)
	TrainingPlanAssignment findById(long trainingPlanAssignmentId);
}
