package com.softech.vu360.lms.web.controller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseGroupView implements Serializable{

	
	private static final long serialVersionUID = 2821335262973727898L;
	private String groupName;
	private String trainingPlanName;
	//initializing with reasonable large number as dynamic increasing size is expensive operation
	private List<CourseView> courses = new ArrayList<CourseView>(500); 
	private HashMap<String, CourseView> courseMap = new HashMap<String, CourseView>();
	private Long parentCourseGroupId = null;
	private long id;
	private boolean trainingPlanFlag;
	private boolean miscFlag;
	
	public void addCourse(CourseView courseView){
		CourseView cView = this.courseMap.get(courseView.getCourseId().toString());
		if(cView==null){
			courses.add(courseView);
			courseMap.put(courseView.getCourseId().toString(), cView);
		}
	}
	/**
	 * 
	 */
	public void addCourse(Map map) {
		// TODO need to add duplicate check here before adding course
		String courseID = map.get("COURSE_ID").toString();
		if (courseMap.get(courseID) == null){
			CourseView course=new CourseView(map);
			courses.add(course);
			courseMap.put(course.courseId.toString(), course);
		}
	}

	public Long getParentCourseGroupId() {
		return parentCourseGroupId;
	}

	public void setParentCourseGroupId(Long parentCourseGroupId) {
		this.parentCourseGroupId = parentCourseGroupId;
	}

	public List<CourseView> getCourses() {
		return courses;
	}
	
	public void removeCourse(CourseView course){
		CourseView _courseView=(CourseView)courseMap.get(course.getName());
		if(_courseView!=null)
			courses.remove(_courseView);
	}
	public void removeCourse(String courseName){
		CourseView _courseView=(CourseView)courseMap.get(courseName);
		if(_courseView!=null)
			courses.remove(_courseView);
	}
	public String getName(){
		if(this.isTrainingPlan()){
			return getTrainingPlanName();
		}else
			return getGroupName();
	}
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTrainingPlanName() {
		return trainingPlanName;
	}

	public void setTrainingPlanName(String trainingPlanName) {
		this.trainingPlanName = trainingPlanName;
	}

	public void setCourses(List<CourseView> courses) {
		this.courses = courses;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public boolean isMisc(){
		return miscFlag;
	}
	public void markMisc(boolean miscFlag){
		this.miscFlag = miscFlag;
	}
	public boolean isTrainingPlan() {
		return trainingPlanFlag;
	}
	public void markTrainingPlan(boolean trainingPlanFlag) {
		this.trainingPlanFlag = trainingPlanFlag;
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(id);builder.append(",");
		builder.append(getName());builder.append(",");
		builder.append("trainingPlan="+isTrainingPlan());builder.append(",");
		builder.append("misc="+isMisc());builder.append("}");
		return builder.toString();
	}
}
