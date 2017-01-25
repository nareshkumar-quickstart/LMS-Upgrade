package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class TrainingPlanAssignmentRepositoryTest {

	@Inject
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRepository; 
	
	@Inject 
	private LearnerEnrollmentRepository learnerEnrollmentRepository;
	
	//@Test
		public void TrainingPlanAssignment_Should_find() {
		
		try{
				TrainingPlanAssignment tpa = this.trainingPlanAssignmentRepository.findBylearnerEnrollments(learnerEnrollmentRepository.findOne(3127L));

				System.out.println(tpa.getTrainingPlan().getName());
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			
		
		}
	//@Test
	public void TrainingPlanAssignment_Should_find_By_Enrollments() {
		
	try{
		Long[] enrollmentIds= new Long[2];
		enrollmentIds[0] = 2915L;
		enrollmentIds[1] = 3127L;
				
		List<LearnerEnrollment>  learnerEnrollment = this.learnerEnrollmentRepository.findByIdIn(enrollmentIds);	
		List<TrainingPlanAssignment> tpa = this.trainingPlanAssignmentRepository.findDistinctByLearnerEnrollmentsIn(learnerEnrollment);
			for(TrainingPlanAssignment trainingPlanAssignment: tpa)
			System.out.println(trainingPlanAssignment.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void findByTrainingPlanId(){
		
		List<TrainingPlanAssignment> objs = trainingPlanAssignmentRepository.findByTrainingPlanId(252L);
		for(TrainingPlanAssignment obj : objs){
			System.out.println("******************** Id ="+obj.getId());
		}
		
	}
	
	//@Test
	public void findByTrainingPlanIdInAndDeleteByIdIn(){
		long[] trainingPlanIds = new long[2];
		trainingPlanIds[0]=10223;
		trainingPlanIds[1]=10224;
		
		List<TrainingPlanAssignment> trainingPlanCourses = trainingPlanAssignmentRepository.findByTrainingPlanIdIn(trainingPlanIds);
		List<Long> ids = new ArrayList();
		for(TrainingPlanAssignment obj : trainingPlanCourses){
			ids.add(obj.getId());
			System.out.println("******************* Id = "+obj.getId());
		//	trainingPlanCourseRepository.delete(obj);
		}
		
		trainingPlanAssignmentRepository.delete(trainingPlanCourses);
		
	}
	
	@Test
	public void getTraingPlanAssignmentsByTraingPlanIdAndDate(){
		
		List<TrainingPlanAssignment> lst = trainingPlanAssignmentRepository.getTraingPlanAssignmentsByTraingPlanIdAndDate(1552L,new Date(),new Date());
		for(TrainingPlanAssignment obj : lst){
			System.out.println("================="+obj.getId());
		}
	}
}
