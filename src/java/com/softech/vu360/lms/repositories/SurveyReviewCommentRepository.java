package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveyReviewComment;

public interface SurveyReviewCommentRepository extends CrudRepository<SurveyReviewComment, Long> {
	
	public SurveyReviewComment findByAnswerId(Long answerId);
	
}
