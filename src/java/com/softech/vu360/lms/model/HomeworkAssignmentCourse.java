/**
 *
 */
package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Learner Assignment")
public class HomeworkAssignmentCourse extends Course {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//public static final String COURSE_TYPE="Homework Assignment";
	public static final String COURSE_TYPE="Learner Assignment";
	
	@Transient
	private Document hwAssignmentInstruction = null;
//	private ValueHolderInterface hwAssignmentInstruction ;
	
	@Column(name = "HOMEWORKASSIGNMENTDUEDATE")
	private Date assignmentDueDate = null;
	
	@Column(name = "HOMEWORKASSIGNMENTGRADINGMETHOD")
	private String gradingMethod = null;
	
	@Column(name = "HOMEWORKASSIGNMENTMASTERYSCORE")
	private Double masteryScore = null;

	public final static String SCORE_METHOD_SIMPLE ="Simple";
	public final static String SCORE_METHOD_SCORE ="Score";
	
	
	public HomeworkAssignmentCourse() {
		super();
        setCourseType(COURSE_TYPE);
	}

	/**
     * @return the hwAssignmentInstruction
     */
    public Document getHwAssignmentInstruction() {
        return this.hwAssignmentInstruction;
    }

    /**
     * @param hwAssignmentInstruction the hwAssignmentInstruction to set
     */
    public void setHwAssignmentInstruction(Document instruction) {
        this.hwAssignmentInstruction = instruction;
    }


	public String getGradingMethod() {
		return gradingMethod;
	}



	public void setGradingMethod(String gradingMethod) {
		this.gradingMethod = gradingMethod;
	}




	@Override
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}



	public Date getAssignmentDueDate() {
		return assignmentDueDate;
	}



	public void setAssignmentDueDate(Date assignmentDueDate) {
		this.assignmentDueDate = assignmentDueDate;
	}










	public Double getMasteryScore() {
		return masteryScore;
	}










	public void setMasteryScore(Double masteryScore) {
		this.masteryScore = masteryScore;
	}






}
