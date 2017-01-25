package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;

public interface AggregateSurveyQuestionItemRepository extends CrudRepository<AggregateSurveyQuestionItem, Long>	{

	public List<AggregateSurveyQuestionItem> findByAggregateSurveyQuestionIdOrderByDisplayOrderAsc(Long Id);
	
	public void deleteByQuestionIdIn(List<Long> ids);
	
	public void deleteByIdIn(List<Long> ids);
	
}
