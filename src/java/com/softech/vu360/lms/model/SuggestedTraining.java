/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SUGGESTEDTRAINING")
public class SuggestedTraining implements SearchableKey {

	private static final long serialVersionUID = 233755032900147743L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SuggestedTraining_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SUGGESTEDTRAINING", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SuggestedTraining_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="SURVEY_ID")
	private Survey survey = null;
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="SUGGESTEDTRAINING_COURSE", joinColumns = @JoinColumn(name="SUGGESTEDTRAINING_ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID"))
	private List<Course> courses = new ArrayList<Course>();
	
	@ManyToMany
    @JoinTable(name="SUGGESTEDTRAINING_SURVEYANSWERITEM", joinColumns = @JoinColumn(name="SUGGESTEDTRAINING_ID"),inverseJoinColumns = @JoinColumn(name="SURVEYANSWERITEM_ID"))
    private List<SurveyAnswerItem> responses = new ArrayList<SurveyAnswerItem>();
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


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


	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}


	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}


	/**
	 * @return the responses
	 */
	public List<SurveyAnswerItem> getResponses() {
		return responses;
	}


	/**
	 * @param responses the responses to set
	 */
	public void setResponses(List<SurveyAnswerItem> responses) {
		this.responses = responses;
	}

}
