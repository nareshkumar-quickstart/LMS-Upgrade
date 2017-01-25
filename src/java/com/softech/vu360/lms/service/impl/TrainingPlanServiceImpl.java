/**
 * 
 */
package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.repositories.TrainingPlanAssignmentRepository;
import com.softech.vu360.lms.repositories.TrainingPlanCourseRepository;
import com.softech.vu360.lms.repositories.TrainingPlanRepository;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanAssignmentSoapVO;

/**
 * @author Ashis
 *
 */
public class TrainingPlanServiceImpl implements TrainingPlanService {
	
	
	@Inject
	private TrainingPlanRepository trainingPlanRepository = null;
	
	@Inject
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRepository = null;
	
	@Inject
	private TrainingPlanCourseRepository trainingPlanCourseRepository = null;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.TrainingPlanService#addTrainingPlan(com.softech.vu360.lms.model.TrainingPlan)
	 */
	@Override
	public TrainingPlan addTrainingPlan(TrainingPlan trainingPlan){
		return trainingPlanRepository.save(trainingPlan);
	}
	public List<TrainingPlanCourse> addTrainingPlanCourses(List<TrainingPlanCourse> trainingPlanCourses){
		return (List<TrainingPlanCourse>)trainingPlanCourseRepository.save(trainingPlanCourses);//trainingPlanDAO.addTrainingPlanCourses(trainingPlanCourses);
	}
	public void addTrainingPlanAssignments(TrainingPlanAssignment trainingPlanAssignments){
		trainingPlanAssignmentRepository.save(trainingPlanAssignments);
	}
	public List<TrainingPlan> findTrainingPlanByName(String name,Customer customer){
		return trainingPlanRepository.findByNameAndCustomerId(name, customer.getId());
	}
	public List<TrainingPlan> findTrainingPlanByCustomerId(Customer customer){
		return trainingPlanRepository.findByCustomerId(customer.getId());
	}
	public TrainingPlan getTrainingPlanByID(long id) {
		return trainingPlanRepository.findOne(id);
	}
	public List<TrainingPlanAssignment> getTraingPlanAssignmentsByTraingPlanId(long id){
		return trainingPlanAssignmentRepository.findByTrainingPlanId(id);
	}
	
	public List<TrainingPlanCourse> getTrainingPlanCourses(TrainingPlan trainingPlan){
		return trainingPlanCourseRepository.findByTrainingPlanId(trainingPlan.getId());
	}

	public List<TrainingPlanCourse> getTrainingPlanCourses(TrainingPlan trainingPlan, String[] trainingPlanCourseIds) {
		List<TrainingPlanCourse> trainingPlanCourses = new ArrayList<TrainingPlanCourse>();
		if (trainingPlanCourseIds != null && trainingPlanCourseIds.length > 0)
		{
			long[] tpCourseIds = new long[trainingPlanCourseIds.length];
			
			for (int i=0;i<tpCourseIds.length;i++) {
				tpCourseIds[i] = Long.valueOf(trainingPlanCourseIds[i]);
			}
			
			trainingPlanCourses = trainingPlanCourseRepository.findByIdNotInAndTrainingPlanId(tpCourseIds, trainingPlan.getId());//trainingPlanDAO.getTrainingPlanCourses(trainingPlan, tpCourseIds);
		}
		return trainingPlanCourses;
	}

	/**
	 * Deletes Training Plan(s) with given id(s) along with their course(s) and assignment(s).  
	 * @param trainingPlanIds	Id(s) of Training plan(s) to be deleted.
	 * @author muzammil.shaikh
	 */
	@Transactional
	public void deleteTrainingPlan(String[] trainingPlanIds) {
		
		if( trainingPlanIds != null && trainingPlanIds.length > 0) {
			long trainingPlanIdArray[] = new long[trainingPlanIds.length];
			for( int i=0; i<trainingPlanIds.length; i++ ) {
				trainingPlanIdArray[i]=Long.parseLong(trainingPlanIds[i]);
			}			
			List<TrainingPlanAssignment> trainingPlanAssignments = trainingPlanAssignmentRepository.findByTrainingPlanIdIn(trainingPlanIdArray);
			trainingPlanAssignmentRepository.delete(trainingPlanAssignments);
			List<TrainingPlanCourse> trainingPlanCourses = trainingPlanCourseRepository.findByTrainingPlanIdIn(trainingPlanIdArray);
			trainingPlanCourseRepository.delete(trainingPlanCourses);
			trainingPlanRepository.deleteByIdIn(trainingPlanIdArray);
			//trainingPlanDAO.deleteTrainingPlan(trainingPlanIdArray);
		}		
	}
	
	
	public void deleteTrainingPlanCourses(List<String> courseIds) {
		String[] ids = new String[courseIds.size()];
		ids = courseIds.toArray(ids);
		deleteTrainingPlanCourses(ids);
	}
	
	/**
	 * Deletes Training Plan Course(s) with given id(s).  
	 * @param trainingPlanIds	Id(s) of Training plan course(s) to be deleted.
	 * @author muzammil.shaikh
	 */
	public void deleteTrainingPlanCourses(String[] courseIds) {
		if (courseIds != null && courseIds.length > 0) {
			long trainingPlanCourseIdArray[] = new long[courseIds.length];			
			for( int i=0; i<courseIds.length; i++ ) {
				trainingPlanCourseIdArray[i]=Long.parseLong(courseIds[i]);
			}			
			
			List<TrainingPlanCourse> trainingPlanCourses = trainingPlanCourseRepository.findByIdIn(trainingPlanCourseIdArray);
			trainingPlanCourseRepository.delete(trainingPlanCourses);
		}
	}
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.TrainingPlanService#loadForUpdateTrainingPlan(long)
	 */
	@Override
	public TrainingPlan loadForUpdateTrainingPlan(long id) {
		return trainingPlanRepository.findOne(id);
	}
	
	@Override
	public List<TrainingPlanAssignmentSoapVO> getTraingPlanAssignmentsByTraingPlanIdAndDate(List<Long> trainingPlanIds,Date startDate,Date endDate){
		List<TrainingPlanAssignment> trainingPlanAssignments = new ArrayList<TrainingPlanAssignment>();
		List<TrainingPlanAssignmentSoapVO> trainingPlanAssignmentSoapVOs = new ArrayList<TrainingPlanAssignmentSoapVO>();
		for(Long trainingPlanId : trainingPlanIds){
			List<String> users = new ArrayList<String>();
			List<TrainingPlanAssignment> tpAssignments = trainingPlanAssignmentRepository.getTraingPlanAssignmentsByTraingPlanIdAndDate(trainingPlanId, startDate, endDate);//trainingPlanDAO.getTraingPlanAssignmentsByTraingPlanIdAndDate(trainingPlanId,startDate,endDate); 
			for(TrainingPlanAssignment trainingPlanAssignment:tpAssignments ){
				for(LearnerEnrollment enrollments :  trainingPlanAssignment.getLearnerEnrollments()){
					//We need only training plan assignment not courses assignment
					if(!users.contains(enrollments.getLearner().getVu360User().getUsername())){
						TrainingPlanAssignmentSoapVO trainingPlanAssignmentSoapVO = new TrainingPlanAssignmentSoapVO();
						trainingPlanAssignmentSoapVO.setTrainingPlanId(trainingPlanAssignment.getTrainingPlan().getId());
						trainingPlanAssignmentSoapVO.setTrainingPlanName(trainingPlanAssignment.getTrainingPlan().getName());
						trainingPlanAssignmentSoapVO.setUserName(enrollments.getLearner().getVu360User().getUsername());
						trainingPlanAssignmentSoapVO.setAssignmentDate(enrollments.getEnrollmentDate().toString());
						trainingPlanAssignmentSoapVOs.add(trainingPlanAssignmentSoapVO);
						users.add(enrollments.getLearner().getVu360User().getUsername());
					}	
				}
			}

			
		}
		return trainingPlanAssignmentSoapVOs;
	}
	
	public List<Object[]> countLearnerByTrainingPlan(Long [] trainingPlanIds){
		return trainingPlanRepository.countLearnerByTrainingPlan(trainingPlanIds);
	}
}
