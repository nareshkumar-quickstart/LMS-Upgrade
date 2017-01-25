package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;

public interface LearnerHomeworkAssignmentSubmissionRepository extends CrudRepository<LearnerHomeworkAssignmentSubmission, Long>{

	//getLearnerHomeworkAssignmentSubmission
    //public List<LearnerHomeworkAssignmentSubmission> getLearnerHomeworkAssignmentSubmissionMastery(LearnerEnrollment learnerEnrollment);
	List<LearnerHomeworkAssignmentSubmission> findBylearnerEnrollmentIdOrderByIdDesc(Long leId);
	
	// bellow function is not in use
	@Query(value = "SELECT * from LEARNER_HOMEWORKASSIGNMENTSUBMISSION where LEARNERENROLLMENT_ID = :Id", nativeQuery = true)
	List<Object[]> getHomeWorkAssignmentAssetByEnrollId( @Param("Id") Long Id);
}
