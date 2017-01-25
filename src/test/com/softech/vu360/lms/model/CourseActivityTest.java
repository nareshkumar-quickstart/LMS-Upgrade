package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 * This test case is for CourseActivity which is a base class and is extended by following entitiies:
 * AssignmentCourseActivity
 * AttendanceCourseActivity
 * FinalScoreCourseActivity
 * GeneralGradedCourseActivity
 * LectureCourseActivity
 * SelfStudyCourseActivity
 * This test case also included CRUD for Gradebook
 * 
 */
@Transactional
public class CourseActivityTest extends TestBaseDAO<CourseActivity> {

	
//	@Test
	public void CourseActivity_should_save() throws Exception{
		
		CourseActivity activity=null;
		//activity=new AssignmentCourseActivity();
//		activity.setActivityName("Test_Assignment_COurseActivity");
//		activity.setDescription("Test_Desc_Assignment_CourseActivity");
//		activity=new AttendanceCourseActivity();
//		activity.setActivityName("Test_Attendance_COurseActivity");
//		activity.setDescription("Test_Desc_Attendance_CourseActivity");
//		activity=new FinalScoreCourseActivity();
//		activity.setActivityName("Test_FinalScore_COurseActivity");
//		activity.setDescription("Test_Desc_FinalScore_CourseActivity");
//		activity=new GeneralGradedCourseActivity();
//		activity.setActivityName("Test_GeneralGraded_COurseActivity");
//		activity.setDescription("Test_Desc_GeneralGraded_CourseActivity");
		activity=new SelfStudyCourseActivity();
		activity.setActivityName("Test_SelfStudy_COurseActivity");
		activity.setDescription("Test_Desc_SelfStudy_CourseActivity");
		activity.setActivityScore(8);
		activity.setDisplayOrder(2);
		//Setting Gradebook
		Gradebook gradebook=(Gradebook)crudFindById(Gradebook.class, new Long(1));
		activity.setGradeBook(gradebook);
		save(activity);
		
	}
	
//	@Test
	public void CourseActivity_should_update() throws Exception{
		
		SelfStudyCourseActivity updateActivity=(SelfStudyCourseActivity)crudFindById(SelfStudyCourseActivity.class, new Long(13609));
		updateActivity.setActivityName("Test_SelfStudy_CourseActivity_Updated");
		updateActivity.setDescription("Test_Desc_SelfStudy_CourseActivity_Updated");
		crudSave(SelfStudyCourseActivity.class, updateActivity);
	}
	
	@Test
	public void Gradebook_should_save(){
		
		Gradebook gradeBook=new Gradebook();
		gradeBook.setDisplayOrder(1);
		gradeBook.setName("Test_GradeBook");
		SynchronousClass syncClass=(SynchronousClass)crudFindById(SynchronousClass.class, new Long(4));
		gradeBook.setSynchronousClass(syncClass);
		
		try{
			gradeBook=(Gradebook)crudSave(Gradebook.class, gradeBook);
		}
		catch(Exception ex){
			System.out.println(gradeBook.getId());
		}
	}
	}
