package com.softech.vu360.lms.model;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Before;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class LearnerCourseActivityTest extends TestBaseDAO<LearnerCourseActivity> {

	@Before
	public void setRequiredService() {

	}

	//@Test
	public void LearnerCourseActivity_should_save() throws Exception {
		
		//===============LearnerCourseActivity of type Assignment
//		LearnerAssignmentActivity learnerAssignmentCourseActivity = new LearnerAssignmentActivity();
//		//Setting courseActivity
//		CourseActivity courseActivity = instructorDAO.getCourseActivityById(new Long(1451));
//		learnerAssignmentCourseActivity.setCourseActivity(courseActivity);
//		//Setting learnercoursestatistics
//		LearnerCourseStatistics courseStatistics=new LearnerCourseStatistics();
//		courseStatistics=aiccLearnerStatsDAO.getLearnerCourseStatisticsById(new Long(169412));
//		learnerAssignmentCourseActivity.setLearnerCourseStatistics(courseStatistics);
//		
//		learnerAssignmentCourseActivity.setAssignmentAttempted(true);
//		learnerAssignmentCourseActivity.setAssignmentComplete(true);
		
		//===============LearnerCourseActivity of type Final Score
		LearnerFinalCourseActivity learnerFinalCourseActivity = new LearnerFinalCourseActivity();
		//Setting courseActivity
		CourseActivity courseActivity = (CourseActivity) crudFindById(CourseActivity.class, new Long(1501));
		learnerFinalCourseActivity.setCourseActivity(courseActivity);
		//Setting learnercoursestatistics
		LearnerCourseStatistics courseStatistics=new LearnerCourseStatistics();
		courseStatistics=(LearnerCourseStatistics)crudFindById(LearnerCourseStatistics.class, new Long(169412));
		learnerFinalCourseActivity.setLearnerCourseStatistics(courseStatistics);
		
		learnerFinalCourseActivity.setCourseComplete(true);
		learnerFinalCourseActivity.setCourseCompleteDate(new Date());
		
		crudSave(LearnerFinalCourseActivity.class, learnerFinalCourseActivity);

	}

	//@Test
	public void LearnerCourseActivity_should_update() throws Exception {

		LearnerCourseActivity activity=(LearnerCourseActivity)crudFindById(LearnerCourseActivity.class, new Long(1001));
		activity.setLearnerCourseStatistics(null);
		crudSave(LearnerCourseActivity.class, activity);

	}
	}
