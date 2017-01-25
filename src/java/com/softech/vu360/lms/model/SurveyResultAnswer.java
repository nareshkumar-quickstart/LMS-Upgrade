/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "SurveyResultAnswer")
public class SurveyResultAnswer implements SearchableKey, Serializable {

	private static final long serialVersionUID = -1735610988132556422L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqSurveyResultAnswerId")
	@GenericGenerator(name = "seqSurveyResultAnswerId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "SURVEYRESULTANSWER") })
	private Long id;
	
	@OneToOne
	@JoinColumn(name="SURVEYQUESTION_ID")
	private SurveyQuestion question;
	
	@ManyToMany (cascade = CascadeType.MERGE)
    @JoinTable(name="SURVEYANSWER_SURVEYRESULTANSWER", joinColumns = @JoinColumn(name="SURVEYRESULTANSWER_ID"),inverseJoinColumns = @JoinColumn(name="SURVEYANSWER_ID"))
    private List<SurveyAnswerItem> surveyAnswerItems = new ArrayList<SurveyAnswerItem>();
	
	@OneToOne
	@JoinColumn(name="SurveyResult_ID")
	private SurveyResult surveyResult;
	
	@Column(name="SURVEYANSWERTEXT")
	private String surveyAnswerText = null;
	
	@OneToMany(mappedBy = "answer" )
	private List<SurveyReviewComment> comments = new ArrayList<SurveyReviewComment>();
	
	@OneToOne
	@JoinColumn(name="surveySectionId")
	private SurveySection surveySection ;
	
	
	@OneToOne
	@JoinColumn(name="surveyQuestionBankId")
	private SurveyQuestionBank surveyQuestionBank ;
	
	@OneToMany(mappedBy = "surveyResultAnswer" )
	private List<SurveyResultAnswerFile> surveyResultAnswerFiles = new ArrayList<SurveyResultAnswerFile>();

	public String getKey() {
		return Long.toString(id.longValue());
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
	 * @return the surveyAnswerItems
	 */
	public List<SurveyAnswerItem> getSurveyAnswerItems() {
		return surveyAnswerItems;
	}

	/**
	 * @param surveyAnswerItems
	 *            the surveyAnswerItems to set
	 */
	public void setSurveyAnswerItems(List<SurveyAnswerItem> surveyAnswerItems) {
		this.surveyAnswerItems = surveyAnswerItems;
	}

	/**
	 * @return the surveyResult
	 */
	public SurveyResult getSurveyResult() {
		return surveyResult;
	}

	/**
	 * @param surveyResult
	 *            the surveyResult to set
	 */
	public void setSurveyResult(SurveyResult surveyResult) {
		this.surveyResult = surveyResult;
	}

	/**
	 * @return the question
	 */
	public SurveyQuestion getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	/**
	 * @return the surveyAnswerText
	 */
	public String getSurveyAnswerText() {
		return surveyAnswerText;
	}

	/**
	 * @param surveyAnswerText
	 *            the surveyAnswerText to set
	 */
	public void setSurveyAnswerText(String surveyAnswerText) {
		this.surveyAnswerText = surveyAnswerText;
	}

	/**
	 * @return the comments
	 */
	public List<SurveyReviewComment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<SurveyReviewComment> comments) {
		this.comments = comments;
	}

	public SurveySection getSurveySection() {
		return (SurveySection) surveySection;
	}

	public void setSurveySection(SurveySection surveySection) {
		this.surveySection = surveySection;
	}

	public SurveyQuestionBank getSurveyQuestionBank() {
		return (SurveyQuestionBank) surveyQuestionBank;
	}

	public void setSurveyQuestionBank(SurveyQuestionBank surveyQuestionBank) {
		this.surveyQuestionBank = surveyQuestionBank;
	}

	protected SurveySection getSurveySectionHolder() {
		return surveySection;
	}

	protected void setSurveySectionHolder(SurveySection surveySectionHolder) {
		this.surveySection = surveySectionHolder;
	}

	protected SurveyQuestionBank getSurveyQuestionBankHolder() {
		return surveyQuestionBank;
	}

	protected void setSurveyQuestionBankHolder(SurveyQuestionBank surveyQuestionBankHolder) {
		this.surveyQuestionBank = surveyQuestionBankHolder;
	}

	public List<SurveyResultAnswerFile> getSurveyResultAnswerFiles() {
		return surveyResultAnswerFiles;
	}

	public void setSurveyResultAnswerFiles(List<SurveyResultAnswerFile> surveyResultAnswerFiles) {
		this.surveyResultAnswerFiles = surveyResultAnswerFiles;
	}

}
