package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class TrainingPlanCourseRepositoryTest {

	
	@Inject
	private TrainingPlanRepository trainingPlanRepository;
	
	@Inject
	private TrainingPlanCourseRepository trainingPlanCourseRepository;
	
	
	//@Test
	public void findByTrainingPlanIdInAndDeleteByIdIn(){
		long[] trainingPlanIds = new long[2];
		trainingPlanIds[0]=10223;
		trainingPlanIds[1]=10224;
		
		List<TrainingPlanCourse> trainingPlanCourses = trainingPlanCourseRepository.findByTrainingPlanIdIn(trainingPlanIds);
		List<Long> ids = new ArrayList();
		for(TrainingPlanCourse obj : trainingPlanCourses){
			ids.add(obj.getId());
			System.out.println("******************* Id = "+obj.getId());
		//	trainingPlanCourseRepository.delete(obj);
		}
		
		trainingPlanCourseRepository.delete(trainingPlanCourses);
		
	}
	
	//@Test
	public void findByCourseIdInAndDeleteByIdIn(){
		long[] trainingPlanCourseIds = new long[2];
		trainingPlanCourseIds[0]=2;
		trainingPlanCourseIds[1]=3;
		
		List<TrainingPlanCourse> trainingPlanCourses = trainingPlanCourseRepository.findByIdIn(trainingPlanCourseIds);
		
		trainingPlanCourseRepository.delete(trainingPlanCourses);
		
	}
	
//	@Test
	public void findByTrainingPlanId(){
		List<TrainingPlanCourse> objs = trainingPlanCourseRepository.findByTrainingPlanId(106L);
		
		for(TrainingPlanCourse obj : objs ){
			System.out.println("***************** ID = "+obj.getId());
		}
	}
	
	//@Test
	public void findByIdNotInAndTrainingPlanId(){
		long[] arr = new long[3];
		arr[0]=61;
		arr[1]=62;
		arr[2]=63;	
		List<TrainingPlanCourse> objs = trainingPlanCourseRepository.findByIdNotInAndTrainingPlanId(arr,106L);
		
		for(TrainingPlanCourse obj : objs ){
			System.out.println("***************** ID = "+obj.getId());
		}
		
	}
	
	@Test
	public void saveIds(){
		
		List<TrainingPlanCourse> trainingPlanCourses = new ArrayList();
		TrainingPlanCourse obj = new TrainingPlanCourse();
		obj.setId(2L);
		TrainingPlan train = trainingPlanRepository.findOne(10223L);
		obj.setTrainingPlan(train);
		
		TrainingPlanCourse obj1 = new TrainingPlanCourse();
		obj1.setId(3L);
		TrainingPlan train1 = trainingPlanRepository.findOne(10224L);
		obj1.setTrainingPlan(train1);
		
		trainingPlanCourses.add(obj1);
		trainingPlanCourses.add(obj);
		
		
		List<TrainingPlanCourse> lst = (List<TrainingPlanCourse>)trainingPlanCourseRepository.save(trainingPlanCourses);
		System.out.println("********************** "+lst.size());
	}
}
