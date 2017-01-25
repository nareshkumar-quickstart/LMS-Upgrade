package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ASSET")
public class LearnerHomeworkAssignmentSubmissionAsset implements SearchableKey {
	
	@Id
    @javax.persistence.TableGenerator(name = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ASSET_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ASSET", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNER_HOMEWORKASSIGNMENTSUBMISSION_ASSET_ID")
	private Long id = null;

	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="HOMEWORKASSIGNMENTSUBMISSION_ID")
	private LearnerHomeworkAssignmentSubmission homeworkAssignmentSubmission = null;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ASSET_ID")
	private Document document = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LearnerHomeworkAssignmentSubmission getHomeworkAssignmentSubmission() {
		return homeworkAssignmentSubmission;
	}

	public void setHomeworkAssignmentSubmission(
			LearnerHomeworkAssignmentSubmission homeworkAssignmentSubmission) {
		this.homeworkAssignmentSubmission = homeworkAssignmentSubmission;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getKey() {
		return id.toString();

	}

}
