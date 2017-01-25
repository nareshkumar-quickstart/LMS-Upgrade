package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 * This test case included CRUD operation for TrainingPlan.
 * Moreover also includes test cases for following entities that are used in relation with TrainingPlan, namely :-
 * TrainingPlanCourse
 * TrainingPlanAssignment
 * 
 */
@Transactional
public class TrainingPlanTest extends TestBaseDAO<TrainingPlan> {



	@Before
	public void setRequiredService() {

	}

//	@Test
	public void TrainingPlan_should_save() throws Exception {

		TrainingPlan plan = new TrainingPlan();
		// Adding Training Plan
		plan.setName("Test_TrainingPlan_New_2");
		plan.setDescription("Test_TrainingPlan_New_2");
		plan.setStartdate(new Date());
		plan.setEnddate(new Date());
		
		Customer customer = (Customer)crudFindById(Customer.class, new Long(3));
		plan.setCustomer(customer);
		save(plan);

	}

	@Test
	public void TrainingPlan_should_update_with_new_TrainingPlanCourse() throws Exception {

		TrainingPlan record = (TrainingPlan)getById(new Long(10171), TrainingPlan.class);

		List<TrainingPlanCourse> planCoursesList = new ArrayList<TrainingPlanCourse>();
		TrainingPlanCourse planCourse = new TrainingPlanCourse();
		Course course = (Course)crudFindById(Course.class, new Long(118709));
		planCourse.setCourse(course);
		planCourse.setTrainingPlan(record);
		planCoursesList.add(planCourse);
		record.setCourses(planCoursesList);
		record.setName("TrainingPlan_Updated");
		update(record);

	}

//	@Test
	public void TrainingPlanAssignment_should_save() throws Exception {

		TrainingPlanAssignment planAssignment = new TrainingPlanAssignment();
		// Setting Training Plan
		TrainingPlan plan=(TrainingPlan)getById(new Long(10170), TrainingPlan.class);
		planAssignment.setTrainingPlan(plan);
		
		//Setting LearnerEnrollment
		List<LearnerEnrollment> learnerEnrollment=new ArrayList<LearnerEnrollment>();
		LearnerEnrollment enroll_1=(LearnerEnrollment)crudFindById(LearnerEnrollment.class, new Long(51));
		LearnerEnrollment enroll_2=(LearnerEnrollment)crudFindById(LearnerEnrollment.class, new Long(52));
		learnerEnrollment.add(enroll_1);
		learnerEnrollment.add(enroll_2);
		
		planAssignment.setLearnerEnrollments(learnerEnrollment);
		crudSave(TrainingPlanAssignment.class, planAssignment);

	}
	
//	@Test
	public void TrainingPlanAssignment_should_update() throws Exception {

		
		TrainingPlanAssignment record = (TrainingPlanAssignment)crudFindById(TrainingPlanAssignment.class, new Long(12269));
		record.getLearnerEnrollments().remove(1);
		crudSave(TrainingPlanAssignment.class, record);


	}
}
