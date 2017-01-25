package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SurveySectionSurveyQuestionBank")
@IdClass(SurveySectionSurveyQuestionBankPK.class)
public class SurveySectionSurveyQuestionBank implements Serializable {
	private static final long serialVersionUID = 4185468565361839264L;

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="surveySectionId")
	private SurveySection surveySection ;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SurveyQuestionBankId")
	private SurveyQuestionBank surveyQuestionBank ;

	public SurveySectionSurveyQuestionBank() {
		this.surveySection = new SurveySection();
		this.surveyQuestionBank= new SurveyQuestionBank();
	}
	
	public SurveyQuestionBank getSurveyQuestionBank() {
		return (SurveyQuestionBank) this.surveyQuestionBank;
	}

	protected SurveyQuestionBank getSurveyQuestionBankHolder() {
		return this.surveyQuestionBank;
	}

	public SurveySection getSurveySection() {
		return this.surveySection;
	}

	protected SurveySection getSurveySectionHolder() {
		return this.surveySection;
	}

	public void setSurveyQuestionBank(SurveyQuestionBank surveyQuestionBank) {
		this.surveyQuestionBank = surveyQuestionBank;
	}

	protected void setSurveyQuestionBankHolder(
			SurveyQuestionBank surveyQuestionBank) {
		this.surveyQuestionBank = surveyQuestionBank;
	}

	public void setSurveySection(SurveySection surveySection) {
		this.surveySection = surveySection;
	}

	protected void setSurveySectionHolder(SurveySection surveySection) {
		this.surveySection = surveySection;
	}

	@Override
	public String toString() {
		return "SurveySectionSurveyQuestionBank toString()";
	}
}
