package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.SurveySection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SurveySectionRepositoryTest {
	@Inject
	private SurveySectionRepository surveySectionRepository;
	@Inject
	private SurveyRepository surveyRepository;
	
	

	//@Test
	public void saveSurveySection() {
		SurveySection objSurveySection = new SurveySection();
		objSurveySection.setDescription("description-12-19-2015");
		objSurveySection.setName("name 12-19-2015");
		objSurveySection.setSurvey(surveyRepository.findOne(19490L));
		objSurveySection.setParent(surveySectionRepository.findOne(2766L));
		
		try{
			
			objSurveySection = surveySectionRepository.save(objSurveySection);
			System.out.println("objSurveySection.id::"+objSurveySection.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//@Test
	public void getSurveySectionsBySurveyId() {
		List<SurveySection> surveySections = surveySectionRepository.findBySurveyId(19490L);
		System.out.println(surveySections);
	}
	
	@Test
	public void deleteSurveySectionsBySurveyId() {
		try{
			List<SurveySection> surveySections = surveySectionRepository.findBySurveyId(33458L);
			surveySectionRepository.delete(surveySections);
			System.out.println(surveySections);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//@Test
	public void getSurveySectionByID() {
		SurveySection surveySection = surveySectionRepository.findOne(8658L);
		System.out.println(surveySection.getName());
	}
	
	@Test
	public void SurveySection_should_findBySurveyIdAndParentIsNull() {
		surveySectionRepository.findBySurveyIdAndParentIsNull(1L);
	}
}
