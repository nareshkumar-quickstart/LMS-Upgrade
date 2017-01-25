package com.softech.vu360.lms.service;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanAssignmentSoapVO;

/**
 * TrainingPlanService defines the set of interfaces 
 * to control the interactions and business logic
 * of training plans within the LMS.
 * 
 * @author jason
 *
 */
public interface TrainingPlanService {

	public TrainingPlan addTrainingPlan(TrainingPlan trainingPlan);
	public List<TrainingPlanCourse> addTrainingPlanCourses(List<TrainingPlanCourse> trainingPlanCourses);
	public void addTrainingPlanAssignments(TrainingPlanAssignment trainingPlanAssignments);
	public List<TrainingPlan> findTrainingPlanByName(String name,Customer customer);	
	public List<TrainingPlan> findTrainingPlanByCustomerId(Customer customer);
	public TrainingPlan getTrainingPlanByID(long id);
	public List<TrainingPlanAssignment> getTraingPlanAssignmentsByTraingPlanId(long id);
	public List<TrainingPlanAssignmentSoapVO> getTraingPlanAssignmentsByTraingPlanIdAndDate(List<Long> standardId,Date startDate,Date endDate);
	public List<TrainingPlanCourse> getTrainingPlanCourses(TrainingPlan trainingPlan);
	public List<TrainingPlanCourse> getTrainingPlanCourses(TrainingPlan trainingPlan, String[] trainingPlanCourseIds);
	
	/**
	 * Deletes Training Plan(s) with given id(s) along with their course(s) and assignment(s).  
	 * @param trainingPlanIds	Id(s) of Training plan(s) to be deleted.
	 * @author muzammil.shaikh
	 */
	public void deleteTrainingPlan(String[] trainingPlanIds);
	
	/**
	 * Deletes Training Plan Course(s) with given id(s).  
	 * @param trainingPlanIds	Id(s) of Training plan course(s) to be deleted.
	 * @author muzammil.shaikh
	 */
	public void deleteTrainingPlanCourses(String[] courseIds);
	
	/**
	 * Deletes Training Plan Course(s) with given id(s).  
	 * @param trainingPlanIds	Id(s) of Training plan course(s) to be deleted.
	 * @author muzammil.shaikh
	 */
	public void deleteTrainingPlanCourses(List<String> courseIds);
	
	public TrainingPlan loadForUpdateTrainingPlan(long id);
	
	public List<Object[]> countLearnerByTrainingPlan(Long [] trainingPlanIds);
}
