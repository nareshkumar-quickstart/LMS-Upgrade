package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Parameter;


/**
 * 
 * @updated haider.ali
 *
 */
@Entity
@Table(name = "LEARNERVALIDATIONANSWERS")
public class LearnerValidationAnswers {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqLearnerValidationAnswerId")
	@GenericGenerator(name = "seqLearnerValidationAnswerId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "LEARNERVALIDATIONANSWERS") })
    @Column(name = "ID", unique = true, nullable = false)
    private long id;
    
	@OneToOne
	@JoinColumn(name="LEARNER_ID")
	private Learner learner;
    
    @Column(name="QUESTION_ID")
	private long questionId;
    @Column(name="SET_ID")
	private long setId;
    
    @Nationalized
    @Column(name="ANSWER")
	private String answer = null;
	
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getSetId() {
		return setId;
	}
	public void setSetId(long setId) {
		this.setId = setId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	
	
	
	
}
