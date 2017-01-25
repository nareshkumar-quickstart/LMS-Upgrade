package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseActivity;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerCourseActivityRepositoryTest {
	
	@Inject
	private LearnerCourseActivityRepository learnerCourseActivityRepository;
	
	
	
	//@Test
	public void findByCourseActivityGradeBookSynchronousClassId(){
		
		
		
		
		System.out.print("***********************"+learnerCourseActivityRepository.findByCourseActivityGradeBookSynchronousClassId(new Long(5902)).get(0).getCourseActivity().getGradeBook().getSynchronousClass().getSectionName());
		
	}
	
	
	@Test
	public void findByCourseActivityId(){
		
		CourseActivity courseActivity = new CourseActivity();
		courseActivity.setId(new Long(1451));
		
		
		System.out.print("***********************"+learnerCourseActivityRepository.findByCourseActivityId(new Long(1451)).get(0).getCourseActivity().getActivityName());
		
	}

}
