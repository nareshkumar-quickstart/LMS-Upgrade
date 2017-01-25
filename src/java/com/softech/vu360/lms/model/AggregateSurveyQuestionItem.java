/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "AGGREGATESURVEYQUESTIONITEM")
public class AggregateSurveyQuestionItem implements Comparable<AggregateSurveyQuestionItem>, SearchableKey {

	private static final long serialVersionUID = -8929401819980270208L;
	
	/*@Id
	@javax.persistence.TableGenerator(name = "AGGREGATESURVEYQUESTIONITEM_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AGGREGATESURVEYQUESTIONITEM", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "AGGREGATESURVEYQUESTIONITEM_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;*/
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqAggregateSurveyQuestionItemId")
	@GenericGenerator(name = "seqAggregateSurveyQuestionItemId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	@Parameter(name = "table_name", value = "VU360_SEQ"),
	@Parameter(name = "value_column_name", value = "NEXT_ID"),
	@Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	@Parameter(name = "segment_value", value = "AGGREGATESURVEYQUESTIONITEM") })
	private Long id;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@OneToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="SURVEYQUESTION_ID")
	private SurveyQuestion question = null;
	
	@OneToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="AGGREGATESURVEYQUESTION_ID")
	private AggregateSurveyQuestion aggregateSurveyQuestion = null;
	
	
	@Override
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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * @return the question
	 */
	public SurveyQuestion getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	/**
	 * @return the aggregateSurveyQuestion
	 */
	public AggregateSurveyQuestion getAggregateSurveyQuestion() {
		return aggregateSurveyQuestion;
	}

	/**
	 * @param aggregateSurveyQuestion the aggregateSurveyQuestion to set
	 */
	public void setAggregateSurveyQuestion(
			AggregateSurveyQuestion aggregateSurveyQuestion) {
		this.aggregateSurveyQuestion = aggregateSurveyQuestion;
	}
	
	public int compareTo(AggregateSurveyQuestionItem arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}

	
	
	public AggregateSurveyQuestionItem getClone(){
		
		AggregateSurveyQuestionItem item = new AggregateSurveyQuestionItem();
		
		item.setAggregateSurveyQuestion(aggregateSurveyQuestion);
		item.setDisplayOrder(displayOrder);
		item.setQuestion(aggregateSurveyQuestion);
		
		
		
		
		return item;
	}
	
}
