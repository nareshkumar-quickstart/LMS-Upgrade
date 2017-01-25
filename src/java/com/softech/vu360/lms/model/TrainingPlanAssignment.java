/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "TRAINIINGPLANASSIGNMENT")
@NamedEntityGraph(name="TrainingPlanAssignment.GraphLearnerEnrollmentWithTrainingPlan", 
attributeNodes={
	@NamedAttributeNode("id"),
	@NamedAttributeNode(value="learnerEnrollments", subgraph="learnerEnrollmentsSubGraph"),
	@NamedAttributeNode(value="trainingPlan", subgraph="trainingPlanSubGraph")
},
subgraphs={
		@NamedSubgraph(name="learnerEnrollmentsSubGraph", attributeNodes={
	    	    @NamedAttributeNode("id"),
	    	    @NamedAttributeNode(value="courseStatistics", subgraph="courseStatisticsSubGraph")
	    }),
	    @NamedSubgraph(name="trainingPlanSubGraph", attributeNodes={
	    	    @NamedAttributeNode("id"),
	    	    @NamedAttributeNode("name"),
	    	    @NamedAttributeNode("description")
	    })
	    }
)

public class TrainingPlanAssignment implements SearchableKey {
	
	private static final long serialVersionUID = 1084869108568183974L;

	@Id
    @javax.persistence.TableGenerator(name = "TRAINIINGPLANASSIGNMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "TRAINIINGPLANASSIGNMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TRAINIINGPLANASSIGNMENT_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="TRAINIINGPLAN_ID")
	private TrainingPlan trainingPlan = null;
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="TRAININGPLANASSIGNMENT_LEARNERENROLLMENT", joinColumns = @JoinColumn(name="TRAININGPLANASSIGNMENT_ID"),inverseJoinColumns = @JoinColumn(name="LEARNERENROLLMENT_ID"))
    private List<LearnerEnrollment> learnerEnrollments = null;
	

	/*
	 * (non-Javadoc)
	 *  
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
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
	 * @return the trainingPlan
	 */
	public TrainingPlan getTrainingPlan() {
		return trainingPlan;
	}

	/**
	 * @param trainingPlan
	 *            the trainingPlan to set
	 */
	public void setTrainingPlan(TrainingPlan trainingPlan) {
		this.trainingPlan = trainingPlan;
	}

	/**
	 * @return the learnerEnrollments
	 */
	public List<LearnerEnrollment> getLearnerEnrollments() {
		return learnerEnrollments;
	}

	/**
	 * @param learnerEnrollments the learnerEnrollments to set
	 */
	public void setLearnerEnrollments(List<LearnerEnrollment> learnerEnrollments) {
		this.learnerEnrollments = learnerEnrollments;
	}
	
	public void addLearnerEnrollment(LearnerEnrollment enrollment) {
		this.learnerEnrollments.add(enrollment);
	}

}
