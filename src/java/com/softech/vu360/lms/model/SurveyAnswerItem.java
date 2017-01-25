package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "SURVEYANSWER")
public class SurveyAnswerItem implements Comparable<SurveyAnswerItem>, SearchableKey, Serializable {

	private static final long serialVersionUID = -7429835753975472865L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SURVEYANSWER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYANSWER", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYANSWER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Nationalized
	@Column(name = "Label")
	private String label;
	
	@Column(name = "Value")
	private String value;
	
	@OneToOne
	@JoinColumn(name="SURVEYQUESTION_ID")
	private SurveyQuestion surveyQuestion;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;

	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the surveyQuestion
	 */
	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	/**
	 * @param surveyQuestion
	 *            the surveyQuestion to set
	 */
	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder
	 *            the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int compareTo(SurveyAnswerItem arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}
	
	public SurveyAnswerItem getClone(){
		
		SurveyAnswerItem answer = new SurveyAnswerItem();
		
		answer.setDisplayOrder(displayOrder);
		answer.setLabel(label);
		//answer.setSurveyQuestion(surveyQuestion);
		answer.setValue(value);
		
		return  answer;
	}
	
}
