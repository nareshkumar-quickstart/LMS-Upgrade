package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ActivityForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String FINAL_SCORE_COURSE = "Final Score Course";
	public static final String ASSIGNMENT_COURSE = "Assignment Course";
	public static final String SELF_STUDY_COURSE = "Self Study Course";
	public static final String LECTURE_COURSE = "Lecture Course";
	
	private List<ManageActivity> activityList = new ArrayList<ManageActivity>();
	private List<CourseActivity> courseActivities = new ArrayList<CourseActivity>();
	private long id = 0;
	private CourseActivity courseActivity = new CourseActivity();
	private ManageActivity manageActivity = new ManageActivity();
	
	public String activityName = "";
	public String description = "";
	public String type = "";
	
	private long grdBkId = 0;
	

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
	 * @return the courseActivity
	 */
	public CourseActivity getCourseActivity() {
		return courseActivity;
	}

	/**
	 * @param courseActivity the courseActivity to set
	 */
	public void setCourseActivity(CourseActivity courseActivity) {
		this.courseActivity = courseActivity;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the grdBkId
	 */
	public long getGrdBkId() {
		return grdBkId;
	}

	/**
	 * @param grdBkId the grdBkId to set
	 */
	public void setGrdBkId(long grdBkId) {
		this.grdBkId = grdBkId;
	}

	public List<CourseActivity> getCourseActivities() {
		return courseActivities;
	}

	public void setCourseActivities(List<CourseActivity> courseActivities) {
		this.courseActivities = courseActivities;
	}

	public ManageActivity getManageActivity() {
		return manageActivity;
	}

	public void setManageActivity(ManageActivity manageActivity) {
		this.manageActivity = manageActivity;
	}
	
	
}
