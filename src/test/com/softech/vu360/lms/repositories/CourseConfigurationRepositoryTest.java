package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseConfigurationRepositoryTest {

	@Inject
	private CourseConfigurationRepository repoCourseConfigurationRepository;
	
	
	@Test
	public void courseConfigurationRepository_should_find_by_fieldLabel_fieldType_active() {
		
		Long templateId =1L;
		List<CourseConfiguration> lstContact = repoCourseConfigurationRepository.findByCourseConfigTemplateId(templateId);
		
		System.out.println("..........");
	}
	
	//@Test
	public void courseConfiguration_should_find_by_id() {
		
		Long id =1L;
		CourseConfiguration courseConfiguration = repoCourseConfigurationRepository.findOne(id);
		
		System.out.println("..........");
	}
	
	//@Test
	public void courseConfiguration_should_save() {
		
		Long id =1L;
		CourseConfiguration courseConfiguration = repoCourseConfigurationRepository.findOne(id);
		courseConfiguration.setId(null);
		
		CourseConfiguration courseConfiguration2 = repoCourseConfigurationRepository.save(courseConfiguration);
		
		System.out.println("..........");
	}
	
	
}
