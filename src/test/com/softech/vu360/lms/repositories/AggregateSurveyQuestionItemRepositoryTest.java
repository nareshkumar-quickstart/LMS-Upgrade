package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AggregateSurveyQuestionItemRepositoryTest {

	@Inject
	private AggregateSurveyQuestionItemRepository aggregateSurveyQuestionItemRepository;
	
	@Test
	public void AggreateSurveyQuestionItem_should_deleteByQuestionIdIn() {
		List<Long> questionsIds = new ArrayList<Long>();
		questionsIds.add(11343L);
		questionsIds.add(11388L);
		aggregateSurveyQuestionItemRepository.deleteByQuestionIdIn(questionsIds);
	}
	
	@Test
	public void AggreateSurveyQuestionItem_should_deleteByQuestionId() {
		//aggregateSurveyQuestionItemRepository.deleteByQuestionId(1L);
	}
	
	@Test
	public void AggregateSurveyQuestionItem_findByAggregateSurveyQuestionIdOrderByDisplayOrderAsc(){
		aggregateSurveyQuestionItemRepository.findByAggregateSurveyQuestionIdOrderByDisplayOrderAsc(37431L);
	}
	
}
