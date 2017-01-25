package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SurveyQuestionBank")
public class SurveyQuestionBank implements Serializable {

	private static final long serialVersionUID = 7442343484861096027L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SurveyQuestionBank_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SurveyQuestionBank", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SurveyQuestionBank_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToMany(mappedBy = "surveyQuestionBank" )
	private List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<SurveyQuestion> getQuestions() {
		return questions;
	}
	public void setQuestions(List<SurveyQuestion> questions) {
		this.questions = questions;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
