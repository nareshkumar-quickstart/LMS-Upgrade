package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */

public class MySurveysForm implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	public MySurveysForm() {
	}
	
	private String show=null;
	List<Survey> surveyList = new ArrayList<Survey>();
	private List<MySurveyCourse> mySurveyCourseList = new ArrayList<MySurveyCourse>();
	
	/**
	 * @return the show
	 */
	public String getShow() {
		return show;
	}

	/**
	 * @param show the show to set
	 */
	public void setShow(String show) {
		this.show = show;
	}

	/**
	 * @return the surveyList
	 */
	public List<Survey> getSurveyList() {
		return surveyList;
	}

	/**
	 * @param surveyList the surveyList to set
	 */
	public void setSurveyList(List<Survey> surveyList) {
		this.surveyList = surveyList;
	}

	/**
	 * @return the mySurveyCourseList
	 */
	public List<MySurveyCourse> getMySurveyCourseList() {
		return mySurveyCourseList;
	}

	/**
	 * @param mySurveyCourseList the mySurveyCourseList to set
	 */
	public void setMySurveyCourseList(List<MySurveyCourse> mySurveyCourseList) {
		this.mySurveyCourseList = mySurveyCourseList;
	}

}
