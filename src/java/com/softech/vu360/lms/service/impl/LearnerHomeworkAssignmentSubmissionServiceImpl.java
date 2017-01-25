package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmissionAsset;
import com.softech.vu360.lms.repositories.DocumentRepository;
import com.softech.vu360.lms.repositories.HomeWorkAssignmentAssetRepository;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.LearnerHomeworkAssignmentSubmissionAssetRepository;
import com.softech.vu360.lms.repositories.LearnerHomeworkAssignmentSubmissionRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService;
import com.softech.vu360.lms.web.controller.helper.SortPagingAndSearch;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentLearner;

/**
 * @author jason
 * 
 */
public class LearnerHomeworkAssignmentSubmissionServiceImpl implements LearnerHomeworkAssignmentSubmissionService {

	private static final Logger log = Logger.getLogger(LearnerHomeworkAssignmentSubmissionServiceImpl.class.getName());
    
	@Inject
	DocumentRepository documentRepository;
	
	@Inject
	private LearnerEnrollmentRepository leanrEnrollmentRepository;
	
	@Inject
	LearnerHomeworkAssignmentSubmissionRepository learnerHomeworkAssignmentSubmissionRepository;
	
	@Inject
	LearnerHomeworkAssignmentSubmissionAssetRepository learnerHomeworkAssignmentSubmissionAssetRepository;
	
	@Inject
	HomeWorkAssignmentAssetRepository homeWorkAssignmentAssetRepository;

	public List<HomeWorkAssignmentAsset> getUploadedHomeWorkDocuments(Long courseId)
    {
    	return homeWorkAssignmentAssetRepository.findByHomeWorkAssignmentCourseId(courseId);
    }
    
    @Override
    public LearnerHomeworkAssignmentSubmission getLearnerHomeworkAssignmentSubmission(LearnerEnrollment learnerEnrollment) {
	
    	List<LearnerHomeworkAssignmentSubmission> submission = learnerHomeworkAssignmentSubmissionRepository.findBylearnerEnrollmentIdOrderByIdDesc(learnerEnrollment.getId());
    	if(submission != null)
		{
			if(submission.size() > 0)
				return submission.get(0);
		}
    	
    	return null;
    }
    
    @Override
    public List<LearnerHomeworkAssignmentSubmission> getLearnerHomeworkAssignmentSubmissionMastery(LearnerEnrollment learnerEnrollment) {
	
    	List<LearnerHomeworkAssignmentSubmission> submission = learnerHomeworkAssignmentSubmissionRepository.findBylearnerEnrollmentIdOrderByIdDesc(learnerEnrollment.getId());
    	if(submission != null)
		{
			if(submission.size() > 0)
				return submission;
		}
    	
    	return null;
    }
    
    /* not in use
    public List<Map<Object, Object>> gethomeWorkAssignmentAssetByEnrollId(Long enrollId)
    {
    	return learnerHomeworkAssignmentSubmissionDAO.gethomeWorkAssignmentAssetByEnrollId(enrollId);
    }
    */
    public LearnerEnrollment getEnrollmentNoCache(Long enrollmentId)
    {
    	return leanrEnrollmentRepository.findOne(enrollmentId);
    }
    
    @Override
    public void addLearnerHomeworkAssignmentSubmissionAsset(List<LearnerHomeworkAssignmentSubmissionAsset> assignmentSubmissionAssets) {
	for (LearnerHomeworkAssignmentSubmissionAsset assignmentSubmissionAsset : assignmentSubmissionAssets) {
	    //accreditationDAO.saveDocument(assignmentSubmissionAsset.getDocument());
	    documentRepository.save(assignmentSubmissionAsset.getDocument());
	    learnerHomeworkAssignmentSubmissionAssetRepository.save(assignmentSubmissionAsset);

	}

    }

    @Transactional
    @Override
    public void deleteLearnerHomeworkAssignmentSubmissionAsset(List<LearnerHomeworkAssignmentSubmissionAsset> assignmentSubmissionAssets) {
		for (LearnerHomeworkAssignmentSubmissionAsset assignmentSubmissionAsset : assignmentSubmissionAssets) {
			learnerHomeworkAssignmentSubmissionAssetRepository.delete(assignmentSubmissionAsset);
		    documentRepository.delete(assignmentSubmissionAsset.getDocument());
		}
    }

    @Override
    public boolean saveLearnerHomeworkAssignmentSubmission(LearnerEnrollment learnerEnrollment, List<Document> submittedDoc) {
	boolean isSuccess = true;
	try {
	    LearnerHomeworkAssignmentSubmission learnerHomeworkAssignmentSubmission = null;
	    List<LearnerHomeworkAssignmentSubmission> submission = learnerHomeworkAssignmentSubmissionRepository.findBylearnerEnrollmentIdOrderByIdDesc(learnerEnrollment.getId());
    	if(submission != null)
			if(submission.size() > 0)
				learnerHomeworkAssignmentSubmission = submission.get(0);
		
    	
	    if (learnerHomeworkAssignmentSubmission != null) {
		learnerHomeworkAssignmentSubmission = learnerHomeworkAssignmentSubmissionRepository.findOne(learnerHomeworkAssignmentSubmission.getId());
		deleteLearnerHomeworkAssignmentSubmissionAsset(learnerHomeworkAssignmentSubmission.getSubmittedWork());
		learnerHomeworkAssignmentSubmission.setSubmittedWork(getNewHWSubmissionAsset(submittedDoc, learnerHomeworkAssignmentSubmission));
		learnerHomeworkAssignmentSubmission.setStatus(LearnerHomeworkAssignmentSubmission.STATUS_SUBMITTED);
	    } else {
		learnerHomeworkAssignmentSubmission = new LearnerHomeworkAssignmentSubmission();
		learnerHomeworkAssignmentSubmission.setLearnerEnrollment(learnerEnrollment);
		learnerHomeworkAssignmentSubmission.setSubmittedWork(getNewHWSubmissionAsset(submittedDoc, learnerHomeworkAssignmentSubmission));
		learnerHomeworkAssignmentSubmission.setStatus(LearnerHomeworkAssignmentSubmission.STATUS_SUBMITTED);
	    }

	    learnerHomeworkAssignmentSubmissionRepository.save(learnerHomeworkAssignmentSubmission);

	} catch (Exception e) {
	    log.error(e.toString());
	    isSuccess = false;
	}

	return isSuccess;
    }

    private List<LearnerHomeworkAssignmentSubmissionAsset> getNewHWSubmissionAsset(List<Document> submittedDoc,
	    LearnerHomeworkAssignmentSubmission learnerHomeworkAssignmentSubmission) {
	List<LearnerHomeworkAssignmentSubmissionAsset> assets = new ArrayList<LearnerHomeworkAssignmentSubmissionAsset>();
	Iterator<Document> iterator = submittedDoc.iterator();

		while (iterator.hasNext()) {
		    LearnerHomeworkAssignmentSubmissionAsset asset = new LearnerHomeworkAssignmentSubmissionAsset();
		    asset.setDocument(iterator.next());
		    asset.setHomeworkAssignmentSubmission(learnerHomeworkAssignmentSubmission);
		    assets.add(asset);
		}
		return assets;
    }

   
    private Sort orderBy(String sortDirection, String sortBy) {
		if (sortDirection.equalsIgnoreCase("desc")) {
			return new Sort(Sort.Direction.DESC, sortBy);
		} else {
			return new Sort(Sort.Direction.ASC, sortBy);
		}

	}
    
    /**
     * Calls DAO layer to pull the learners enrolled for homework assignment
     * course
     */
    public List<HomeworkAssignmentLearner> getHomeworkAssignmentLearnersByInstructor(Long instrutorId, SortPagingAndSearch sps) {
	String sortColumn = null;
	int sortColumnIndex = 0;

	/*
	 * Set first column as sort column if invalid sort column is set
	 */
	
	/*try {
	    sortColumnIndex = Integer.parseInt(sps.getSortColumnIndex());
	} catch (NumberFormatException nfe) {
	    sortColumnIndex = 0;
	}

	switch (sortColumnIndex) {
	case 0:
	    sortColumn = "learner.vu360User.firstName";
	case 1:
	    sortColumn = "learner.vu360User.lastName";
	default:
	    sortColumn = "learner.vu360User.firstName";
	}

	sps.setSortColumnName(sortColumn);
	 */
	List<HomeworkAssignmentLearner> learners = new ArrayList<HomeworkAssignmentLearner>();
	//List<LearnerEnrollment> enrollments = this.learnerHomeworkAssignmentSubmissionDAO.getHomeworkAssignmentLearnersByInstructor(instrutorId, sps);
	
	Sort sortSpec = null;

	RepositorySpecificationsBuilder<LearnerEnrollment> specBuilder = new RepositorySpecificationsBuilder<LearnerEnrollment>();
	specBuilder.with("course_courseType", "::IC", HomeworkAssignmentCourse.COURSE_TYPE, "AND");
	specBuilder.with("course_instructorCourses_instructor_id", "::",instrutorId, "AND");
	
	if (StringUtils.isNotBlank(sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME))) 
		specBuilder.with("learner_vu360User_firstName", "::IC",sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_FIRSTNAME), "AND");
	       
	if (StringUtils.isNotBlank(sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME))) 
		specBuilder.with("learner_vu360User_lastName", "::IC",sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_LASTNAME), "AND");
	
	if (StringUtils.isNotBlank(sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME))) 
		specBuilder.with("course_courseTitle", "::IC",sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_COURSENAME), "AND");
	  
	//TODO following check was not working in legacy LMS and still not wroking in Tech migration project
	if (sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS).equalsIgnoreCase(HomeworkAssignmentLearner.SEARCH_STATUS_PENDING)) 
		specBuilder.with("hwSubmission_status", "::IC",sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS), "AND");
	
	//TODO following check was not working in legacy LMS and still not wroking in Tech migration project
	if (sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS).equalsIgnoreCase(HomeworkAssignmentLearner.SEARCH_STATUS_SUBMITTED)) 
		specBuilder.with("hwSubmission_status", "::IC",sps.getValue(HomeworkAssignmentLearner.SEARCH_FIELD_STATUS), "AND");
	
	
	if (HomeworkAssignmentLearner.SORT_COLUMN_FIRSTNAME.equals(sps.getSortColumnIndex())) 
		sortSpec = orderBy(sps.getSortDirection(), "learner_vu360User_firstName");
	
	if (HomeworkAssignmentLearner.SORT_COLUMN_LASTNAME.equals(sps.getSortColumnIndex())) 
	    sortSpec = orderBy(sps.getSortDirection(), "learner_vu360User_lastName");
	
	if (HomeworkAssignmentLearner.SORT_COLUMN_ASSIGNMENT.equals(sps.getSortColumnIndex())) 
	    sortSpec = orderBy(sps.getSortDirection(), "course_courseTitle");
	
	if (HomeworkAssignmentLearner.SORT_COLUMN_STATUS.equals(sps.getSortColumnIndex())) 
	    sortSpec = orderBy(sps.getSortDirection(), "hwSubmission_status");
	
	
	//TODO following sorting check is not working in legacy LMS and was giving the following error 
	//com.microsoft.sqlserver.jdbc.SQLServerException: Invalid column name 'GRADINGMETHOD'.
	if (HomeworkAssignmentLearner.SORT_COLUMN_SCORINGMETHOD.equals(sps.getSortColumnIndex())) {
	   // ebSorting = query.getExpressionBuilder().get("course").postfixSQL("GRADINGMETHOD");
	}
	
	if (HomeworkAssignmentLearner.SORT_COLUMN_SCORE.equals(sps.getSortColumnIndex())) 
	    sortSpec = orderBy(sps.getSortDirection(), "hwSubmission_percentScore");
	
	
	Pageable pageRequest =  new PageRequest(sps.getPageIndex(), sps.getPageSize(), sortSpec);
	Page<LearnerEnrollment> lst = leanrEnrollmentRepository.findAll(specBuilder.build(), pageRequest);
	List<LearnerEnrollment> enrollments = lst.getContent();
		
	HomeworkAssignmentLearner homeworkAssignmentLearner = null;

	boolean isSubmissionFound = false;
	String assignmentName = null;
	String firstName = null;
	String lastName = null;
	String scoringMethod = null;
	Long learnerEnrollmentId = null;
	
	/*
	 * Iterate through all the enrollments for the learners how are enrolled
	 * for homework assignment
	 */
	for (LearnerEnrollment learnerEnrollment : enrollments) {
	    assignmentName = learnerEnrollment.getCourse().getCourseTitle();
	    firstName = learnerEnrollment.getLearner().getVu360User().getFirstName();
	    lastName = learnerEnrollment.getLearner().getVu360User().getLastName();
	    scoringMethod = (String) ((HomeworkAssignmentCourse) learnerEnrollment.getCourse()).getGradingMethod();
	    learnerEnrollmentId = learnerEnrollment.getId();

	    List<LearnerHomeworkAssignmentSubmission> submissions = learnerEnrollment.getHwSubmission();
	    
	    for (LearnerHomeworkAssignmentSubmission submission : submissions) {
		
	    isSubmissionFound = true;
		homeworkAssignmentLearner = new HomeworkAssignmentLearner();
		homeworkAssignmentLearner.setLearnerEnrollmentId(learnerEnrollmentId);
		homeworkAssignmentLearner.setAssignmentName(assignmentName);
		homeworkAssignmentLearner.setFirstName(firstName);
		homeworkAssignmentLearner.setLastName(lastName);
		homeworkAssignmentLearner.setScoringMethod(scoringMethod);

		
		homeworkAssignmentLearner.setId(submission.getId());
		homeworkAssignmentLearner.setStatus(submission.getStatus());
		homeworkAssignmentLearner.setGrade(submission.getPercentScore()); 
       
		if(scoringMethod.equals("Scored"))
        {
        	if(submission.getPercentScore() != null )
        	{
        		if(!submission.getPercentScore().equals("Incomplete") )	
        		{
					double marksattained = Double.parseDouble(submission.getPercentScore());
		 		    int masteryscore = Double.compare(((HomeworkAssignmentCourse) learnerEnrollment.getCourse()).getMasteryScore(), marksattained);
		 		    
		 		   if(masteryscore <= 0  )
				    {
		 			  homeworkAssignmentLearner.setCoursestatus("PASS");
				    }
        		}
        	}
        }
		else if(scoringMethod.equals("Simple"))
		{
        	if(submission.getPercentScore() != null )
        	{ 	
				if(submission.getPercentScore().equals("Pass"))
				{
					 homeworkAssignmentLearner.setCoursestatus("PASS");
				}
        	}
		}
		learners.add(homeworkAssignmentLearner);
	    }

	    if (!isSubmissionFound) {
		homeworkAssignmentLearner = new HomeworkAssignmentLearner();

		homeworkAssignmentLearner.setLearnerEnrollmentId(learnerEnrollmentId);
		homeworkAssignmentLearner.setAssignmentName(assignmentName);
		homeworkAssignmentLearner.setFirstName(firstName);
		homeworkAssignmentLearner.setLastName(lastName);
		homeworkAssignmentLearner.setScoringMethod(scoringMethod);
		homeworkAssignmentLearner.setStatus(HomeworkAssignmentLearner.SEARCH_STATUS_PENDING);

		learners.add(homeworkAssignmentLearner);
	    }
	    isSubmissionFound = false;
	}
	return learners;
    }

    /**
     * 
     */
    public LearnerHomeworkAssignmentSubmission getHomeworkAssignmentSubmissionById(String id) {
    	return learnerHomeworkAssignmentSubmissionRepository.findOne(Long.valueOf(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService
     * #updateScore
     * (com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission)
     */
    @Override
    public LearnerHomeworkAssignmentSubmission updateScore(LearnerHomeworkAssignmentSubmission submission) {

	LearnerHomeworkAssignmentSubmission s = new LearnerHomeworkAssignmentSubmission();
	// load for update if record present otherwise create new model class
	if (null != submission.getId()) {
	    s = learnerHomeworkAssignmentSubmissionRepository.findOne(submission.getId());
	} else {
	    LearnerEnrollment e = leanrEnrollmentRepository.findOne(submission.getLearnerEnrollment().getId());  //enrollmentDAO.loadForUpdateLearnerEnrollment(submission.getLearnerEnrollment().getId());
	    s.setLearnerEnrollment(e);
	    //s.setStatus(HomeworkAssignmentLearner.SEARCH_STATUS_PENDING);
	    s.setStatus(HomeworkAssignmentLearner.SEARCH_STATUS_SUBMITTED);
	}

	s.setGrader(submission.getGrader());
	s.setPercentScore(submission.getPercentScore());
	return learnerHomeworkAssignmentSubmissionRepository.save(s);

    }
    
    @Override
    public LearnerHomeworkAssignmentSubmission updateStatus(LearnerHomeworkAssignmentSubmission submission) {
		LearnerHomeworkAssignmentSubmission s = new LearnerHomeworkAssignmentSubmission();
		// load for update if record present otherwise create new model class
		if (null != submission.getId()) {
		    s = learnerHomeworkAssignmentSubmissionRepository.findOne(submission.getId());
		}
	
		s.setStatus(HomeworkAssignmentLearner.SEARCH_STATUS_VIEWED);
		return learnerHomeworkAssignmentSubmissionRepository.save(s);
    }

}