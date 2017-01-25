package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Survey;

public class SurveyPreviewVO implements Serializable {
	
	private static final long serialVersionUID = 2189131612902156233L;
	
	private Survey survey ;
	private List<SurveySectionVO> surveySectionVO = new ArrayList<SurveySectionVO>();
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public List<SurveySectionVO> getSurveySectionVO() {
		return surveySectionVO;
	}
	public void setSurveySectionVO(List<SurveySectionVO> surveySectionVO) {
		this.surveySectionVO = surveySectionVO;
	}

}
