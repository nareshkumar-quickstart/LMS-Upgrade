package com.softech.vu360.lms.web.controller.model.survey;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class AggregateSurveyQuestionItem implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private com.softech.vu360.lms.model.AggregateSurveyQuestionItem aggregateSurveyQuestionItemRef;

	public AggregateSurveyQuestionItem(com.softech.vu360.lms.model.AggregateSurveyQuestionItem aggregateSurveyQuestionItem) {
		super();
		this.aggregateSurveyQuestionItemRef = aggregateSurveyQuestionItem;
	}

	private boolean selected = false;

	/**
	 * @return the aggregateSurveyQuestionItemRef
	 */
	public com.softech.vu360.lms.model.AggregateSurveyQuestionItem getAggregateSurveyQuestionItemRef() {
		return aggregateSurveyQuestionItemRef;
	}

	/**
	 * @param aggregateSurveyQuestionItemRef the aggregateSurveyQuestionItemRef to set
	 */
	public void setAggregateSurveyQuestionItemRef(
			com.softech.vu360.lms.model.AggregateSurveyQuestionItem aggregateSurveyQuestionItemRef) {
		this.aggregateSurveyQuestionItemRef = aggregateSurveyQuestionItemRef;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
