package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION")
public class LearnerHomeworkAssignmentSubmission implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	public final static String STATUS_SUBMITTED="Submitted";
	public final static String STATUS_PENDING="Pending";
	public final static String SCORE_INCOMPLETED="Incomplete";
	public final static String SCORE_METHOD_SIMPLE="Simple";
	public final static String SCORE_METHOD_SCORE="Scored";
	public static final String SEARCH_STATUS_VIEWED = "Viewed";
	
	@Id
    @javax.persistence.TableGenerator(name = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ID")
	private Long id = null;
	
	@OneToOne
    @JoinColumn(name="GRADERUSER_ID")
	private Instructor grader;
	
	@Column(name = "PERCENTSCORE")
	private String percentScore = null;
	
	@Column(name = "STATUS")
	private String status = null;
	
	@OneToMany(mappedBy="homeworkAssignmentSubmission" , cascade = { CascadeType.PERSIST, CascadeType.MERGE }) 
	private List<LearnerHomeworkAssignmentSubmissionAsset> submittedWork = null;
	
	@OneToOne
    @JoinColumn(name="LEARNERENROLLMENT_ID")
	private LearnerEnrollment learnerEnrollment = null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Instructor getGrader() {
		return grader;
	}
	public void setGrader(Instructor grader) {
		this.grader = grader;
	}
	public String getPercentScore() {
		return percentScore;
	}
	public void setPercentScore(String percentScore) {
		this.percentScore = percentScore;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<LearnerHomeworkAssignmentSubmissionAsset> getSubmittedWork() {
		return submittedWork;
	}
	public void setSubmittedWork(
			List<LearnerHomeworkAssignmentSubmissionAsset> submittedWork) {
		this.submittedWork = submittedWork;
	}
	
	public LearnerEnrollment getLearnerEnrollment() {
		return learnerEnrollment;
	}
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return id.toString();

	}


}
