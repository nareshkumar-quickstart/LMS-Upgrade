package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Kashif.Rasheed
 * 
 */
public class HomeworkAssignmentLearner  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    public static final String SEARCH_FIELD_FIRSTNAME = "firstName";

    @SuppressWarnings("unused")
    public static final String SEARCH_FIELD_LASTNAME = "lastName";

    @SuppressWarnings("unused")
    public static final String SEARCH_FIELD_COURSENAME = "courseName";

    @SuppressWarnings("unused")
    public static final String SEARCH_FIELD_STATUS = "status";

    public static final String SEARCH_STATUS_SUBMITTED = "Submitted";
    public static final String SEARCH_STATUS_PENDING = "Pending";
    public static final String SEARCH_STATUS_VIEWED = "Viewed";
    public static final String SEARCH_STATUS_ALL = "All";

    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_FIRSTNAME = "0";
    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_LASTNAME = "1";
    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_ASSIGNMENT = "2";
    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_STATUS = "3";
    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_SCORINGMETHOD = "4";
    @SuppressWarnings("unused")
    public static final String SORT_COLUMN_SCORE = "5";

    private Long id;
    private Long learnerEnrollmentId;

    public Long getLearnerEnrollmentId() {
	return learnerEnrollmentId;
    }

    public void setLearnerEnrollmentId(Long learnerEnrollmentId) {
	this.learnerEnrollmentId = learnerEnrollmentId;
    }

    private String firstName;
    private String lastName;
    private String assignmentName;
    private String grade;
    private String scoringMethod;
    private String status;
    private String coursestatus;

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getAssignmentName() {
	return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
	this.assignmentName = assignmentName;
    }

    public String getGrade() {
	return grade;
    }

    public void setGrade(String grade) {
	this.grade = grade;
    }

    public String getScoringMethod() {
	return scoringMethod;
    }

    public void setScoringMethod(String scoringMethod) {
	this.scoringMethod = scoringMethod;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

	public String getCoursestatus() {
		return coursestatus;
	}

	public void setCoursestatus(String coursestatus) {
		this.coursestatus = coursestatus;
	}

}
