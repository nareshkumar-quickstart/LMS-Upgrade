package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */

@Entity
@Table(name = "AICCLEARNERSTATISTICS")
public class AICCLearnerStatistics implements SearchableKey {
	
	@Id
    @javax.persistence.TableGenerator(name = "AICCLEARNERSTATISTICS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AICCLEARNERSTATISTICS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AICCLEARNERSTATISTICS_ID")
	private Long id;
	
	@Column(name = "Learner_ID")
	private Long learnerId;
	
	@Column(name = "LearnerEnrollment_ID")
	private Long learnerEnrollmentId;
	
	@Column(name = "Credit")
	private String credit;
	
	@Column(name = "Lesson_Location")
	private String lessonLocation;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "TotalTimeInSecond")
	private Integer totalTimeInSeconds;
	
	@Column(name = "CreateDate")
	private Date createDate;
	
	@Column(name = "LastUpdateDate")
	private Date lastUpdateDate;

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
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
	 * @return the learnerId
	 */
	public long getLearnerId() {
		return learnerId;
	}

	/**
	 * @param learnerId the learnerId to set
	 */
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}

	/**
	 * @return the learnerEnrollmentId
	 */
	public long getLearnerEnrollmentId() {
		return learnerEnrollmentId;
	}

	/**
	 * @param learnerEnrollmentId the learnerEnrollmentId to set
	 */
	public void setLearnerEnrollmentId(long learnerEnrollmentId) {
		this.learnerEnrollmentId = learnerEnrollmentId;
	}

	/**
	 * @return the credit
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * @param credit the credit to set
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}

	/**
	 * @return the lessonLocation
	 */
	public String getLessonLocation() {
		return lessonLocation;
	}

	/**
	 * @param lessonLocation the lessonLocation to set
	 */
	public void setLessonLocation(String lessonLocation) {
		this.lessonLocation = lessonLocation;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the totalTimeInSeconds
	 */
	public Integer getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}

	/**
	 * @param totalTimeInSeconds the totalTimeInSeconds to set
	 */
	public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
}
