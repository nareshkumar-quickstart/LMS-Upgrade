package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class MySurveyCourse  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Course course = null;
	private List<Survey> surveys = new ArrayList<Survey>();
	
	
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
	/**
	 * @return the surveys
	 */
	public List<Survey> getSurveys() {
		return surveys;
	}
	/**
	 * @param surveys the surveys to set
	 */
	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	
	
}
