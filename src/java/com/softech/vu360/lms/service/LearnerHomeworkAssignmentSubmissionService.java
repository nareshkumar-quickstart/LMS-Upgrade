package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmissionAsset;
import com.softech.vu360.lms.web.controller.helper.SortPagingAndSearch;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentLearner;

/**
 * CourseAndCourseGroupService defines a set of interfaces to controll the
 * business logic for interactions with the Course and Course Groups within LMS.
 * 
 * @author jason
 * 
 */
public interface LearnerHomeworkAssignmentSubmissionService {

    public List<HomeworkAssignmentLearner> getHomeworkAssignmentLearnersByInstructor(Long instrutorId, SortPagingAndSearch sps);

    public LearnerHomeworkAssignmentSubmission getLearnerHomeworkAssignmentSubmission(LearnerEnrollment learnerEnrollment);
    public List<LearnerHomeworkAssignmentSubmission> getLearnerHomeworkAssignmentSubmissionMastery(LearnerEnrollment learnerEnrollment);
    
    public LearnerEnrollment getEnrollmentNoCache(Long enrollmentId);
    
    public LearnerHomeworkAssignmentSubmission getHomeworkAssignmentSubmissionById(String id);

    public void addLearnerHomeworkAssignmentSubmissionAsset(List<LearnerHomeworkAssignmentSubmissionAsset> assignmentSubmissionAssets);

    public LearnerHomeworkAssignmentSubmission updateScore(LearnerHomeworkAssignmentSubmission learnerHomeworkAssignmentSubmission);

    public void deleteLearnerHomeworkAssignmentSubmissionAsset(List<LearnerHomeworkAssignmentSubmissionAsset> assignmentSubmissionAssets);

    public boolean saveLearnerHomeworkAssignmentSubmission(LearnerEnrollment learnerEnrollment, List<Document> submittedWork);
    
    public List<HomeWorkAssignmentAsset> getUploadedHomeWorkDocuments(Long courseId); 
    
    public LearnerHomeworkAssignmentSubmission updateStatus(LearnerHomeworkAssignmentSubmission submission);

}