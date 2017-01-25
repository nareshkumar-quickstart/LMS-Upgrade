package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseConfigurationTemplateRepositoryTest {

	@Inject
	private CourseConfigurationTemplateRepository courseConfigurationTemplateRepository;
	
	
	//@Test
	public void find_CourseConfigurationTemplate_by_name_And_lastUpdatedDate(){
		String name="Test";
		
		List<CourseConfigurationTemplate> listCourseConfigurationTemplate =  courseConfigurationTemplateRepository.findByNameAndLastUpdateDate(name, null);
		
		System.out.println(".................");
	}
	
	//@Test
	public void find_CourseConfigurationTemplate_by_name_And_lastUpdatedDateAndContentOwnerId(){
		String name="Test";
		Long contentOwnerId = 66073L;
		
		List<CourseConfigurationTemplate> listCourseConfigurationTemplate =  courseConfigurationTemplateRepository.findByNameAndLastUpdateDateAndContentOwnerId(name, null, contentOwnerId);
		
		System.out.println(".................");
	}
	
	@Test
	public void find_viewAssessmentResults(){
		Boolean flag =  courseConfigurationTemplateRepository.findviewAssessmentResults(265686L);
		
		System.out.println(flag);
	}
}
