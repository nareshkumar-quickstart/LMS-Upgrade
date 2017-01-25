package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author haider.ali
 * 
 */
@Entity
@Table(name = "LEARNERSTATISTICS")
@DiscriminatorColumn(name = "LEARNERSTATISTICTYPE")
public abstract class LearnerStatistic {

    @Id
	@javax.persistence.TableGenerator(name = "LEARNERSTATISTICS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERSTATISTICS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERSTATISTICS_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

    @Column(name = "statisticDate")
	private Date statisticDate = null;
	
	@OneToOne (cascade = { CascadeType.ALL })
    @JoinColumn(name="LEARNINGSESSION_ID")
	private LearningSession learningSession = null;

	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}

	public LearningSession getLearningSession() {
		return learningSession;
	}

	public void setLearningSession(LearningSession learningSession) {
		this.learningSession = learningSession;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
