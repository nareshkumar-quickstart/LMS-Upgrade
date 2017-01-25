package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class GradeBookForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String FINAL_SCORE_COURSE = "Final Score Course";
	public static final String ASSIGNMENT_COURSE = "Assignment Course";
	public static final String SELF_STUDY_COURSE = "Self Study Course";
	public static final String LECTURE_COURSE = "Lecture Course";
	
	private List<ManageActivity> activityList = new ArrayList<ManageActivity>();
	private List<ManageLearnerResult> learnerList = new ArrayList<ManageLearnerResult>();
	private List<LearnerCourseActivity> learnerCourseActivities=null;
	private List<CourseActivity> courseActivities=new ArrayList<CourseActivity>();
	private long synClsId = 0;
	private long gradebookId = 0;
	private long caId = 0;
	private String synClassName = "";
	
	private String activityName = "";
	private List<ManageGrade> manageGradeList = new ArrayList<ManageGrade>();
	private String actType = "";
	
	
	
	/**
	 * @return the activityList
	 */
	public List<ManageActivity> getActivityList() {
		return activityList;
	}

	/**
	 * @param activityList the activityList to set
	 */
	public void setActivityList(List<ManageActivity> activityList) {
		this.activityList = activityList;
	}

	/**
	 * @return the learnerList
	 */
	public List<ManageLearnerResult> getLearnerList() {
		return learnerList;
	}

	/**
	 * @param learnerList the learnerList to set
	 */
	public void setLearnerList(List<ManageLearnerResult> learnerList) {
		this.learnerList = learnerList;
	}

	/**
	 * @return the synClsId
	 */
	public long getSynClsId() {
		return synClsId;
	}

	/**
	 * @param synClsId the synClsId to set
	 */
	public void setSynClsId(long synClsId) {
		this.synClsId = synClsId;
	}

	public List<LearnerCourseActivity> getLearnerCourseActivities() {
		return learnerCourseActivities;
	}

	public void setLearnerCourseActivities(
			List<LearnerCourseActivity> learnerCourseActivities) {
		this.learnerCourseActivities = learnerCourseActivities;
	}

	public long getGradebookId() {
		return gradebookId;
	}

	public void setGradebookId(long gradebookId) {
		this.gradebookId = gradebookId;
	}
	public List<CourseActivity> getCourseActivities() {
		return courseActivities;
	}
	public void setCourseActivities(List<CourseActivity> courseActivities) {
		this.courseActivities = courseActivities;
	}

	/**
	 * @return the caId
	 */
	public long getCaId() {
		return caId;
	}

	/**
	 * @param caId the caId to set
	 */
	public void setCaId(long caId) {
		this.caId = caId;
	}

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return the manageGradeList
	 */
	public List<ManageGrade> getManageGradeList() {
		return manageGradeList;
	}

	/**
	 * @param manageGradeList the manageGradeList to set
	 */
	public void setManageGradeList(List<ManageGrade> manageGradeList) {
		this.manageGradeList = manageGradeList;
	}

	/**
	 * @return the actType
	 */
	public String getActType() {
		return actType;
	}

	/**
	 * @param actType the actType to set
	 */
	public void setActType(String actType) {
		this.actType = actType;
	}

	/**
	 * @return the synClassName
	 */
	public String getSynClassName() {
		return synClassName;
	}

	/**
	 * @param synClassName the synClassName to set
	 */
	public void setSynClassName(String synClassName) {
		this.synClassName = synClassName;
	}

}
