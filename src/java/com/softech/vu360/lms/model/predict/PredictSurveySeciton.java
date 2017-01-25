package com.softech.vu360.lms.model.predict;

import java.util.ArrayList;
import java.util.List;

public class PredictSurveySeciton {
	private Long id;
	private List<PredictSurveySeciton> children = new ArrayList<PredictSurveySeciton>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<PredictSurveySeciton> getChildren() {
		return children;
	}
	public void setChildren(List<PredictSurveySeciton> children) {
		this.children = children;
	}
}
