package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@DiscriminatorValue("PSQ")
public class PersonalInformationSurveyQuestion extends SurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = 1314344670071839876L;
	
	@ManyToMany (cascade = {CascadeType.ALL})
    @JoinTable(name="SURVEYQUESTION_AVAILABLEPERSONALINFORMATION", joinColumns = @JoinColumn(name="SURVEYQUESTION_ID"),inverseJoinColumns = @JoinColumn(name="AVAILABLEPERSONALINFORMATION_ID"))
    private List<AvailablePersonalInformationfield> personalInformationfields = new ArrayList<AvailablePersonalInformationfield>();

	public List<AvailablePersonalInformationfield> getPersonalInformationfields() {
		return personalInformationfields;
	}

	public void setPersonalInformationfields(List<AvailablePersonalInformationfield> personalInformationfields) {
		this.personalInformationfields = personalInformationfields;
	}
	
	public SurveyQuestion getClone(){	
		SurveyQuestion question = new PersonalInformationSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		((PersonalInformationSurveyQuestion)question).setPersonalInformationfields(personalInformationfields);		
		return question;
	}

}