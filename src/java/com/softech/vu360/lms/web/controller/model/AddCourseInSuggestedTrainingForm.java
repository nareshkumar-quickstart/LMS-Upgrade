package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * Used in adding individual course to Suggested Training.
 *
 */
public class AddCourseInSuggestedTrainingForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String action = "";
	private List<CourseItem> courses = new ArrayList<CourseItem>();
	private String courseName = "";
	private String courseId = "";
	private String keywords = "";
	private List<Course> courseList = new ArrayList<Course>();
	private long sid = 0 ;
	private Survey survey = null;
	private com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = null;
	private SuggestedTraining suggTraining = null ;
	

	/**
	 * @return the survey
	 */
	public Survey getSurvey() {
		return survey;
	}
	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<CourseItem> getCourses() {
		return courses;
	}
	public void setCourses(List<CourseItem> courses) {
		this.courses = courses;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the courseList
	 */
	public List<Course> getCourseList() {
		return courseList;
	}
	/**
	 * @param courseList the courseList to set
	 */
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	/**
	 * @return the sid
	 */
	public long getSid() {
		return sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(long sid) {
		this.sid = sid;
	}
	/**
	 * @return the surveyView
	 */
	public com.softech.vu360.lms.web.controller.model.survey.Survey getSurveyView() {
		return surveyView;
	}
	/**
	 * @param surveyView the surveyView to set
	 */
	public void setSurveyView(
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView) {
		this.surveyView = surveyView;
	}
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the suggTraining
	 */
	public SuggestedTraining getSuggTraining() {
		return suggTraining;
	}
	/**
	 * @param suggTraining the suggTraining to set
	 */
	public void setSuggTraining(SuggestedTraining suggTraining) {
		this.suggTraining = suggTraining;
	}

}