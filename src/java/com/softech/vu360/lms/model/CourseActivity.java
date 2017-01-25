/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COURSEACTIVITY")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ACTIVITYTYPE")
public class CourseActivity implements Comparable<CourseActivity>,SearchableKey {

	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COURSEACTIVITY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COURSEACTIVITY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSEACTIVITY_ID")
	private Long id;
	
	@Column(name = "ACTIVITYNAME")
	private String activityName = null;
	
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@OneToOne
    @JoinColumn(name="GRADEBOOK_ID")
	private Gradebook gradeBook ;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;  // Note : LMS:7970
	
	@Column(name = "ACTIVITYSCORE")
	private Integer activityScore = 0;
	
	public static final String ASSIGNMENT_ACTIVITY = "Assignment";
	public static final String ATTENDANCE_ACTIVITY = "Attendance";
	public static final String GENERAL_GRADED_ACTIVITY = "General Graded";
	public static final String FINAL_SCORE_COURSE_ACTIVITY = "Finalscorecourseactivity";
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
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
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	
	/**
	 * @return the gradeBook
	 */
	public Gradebook getGradeBook() {
		return gradeBook;
	}

	/**
	 * @param gradeBook the gradeBook to set
	 */
	public void setGradeBook(Gradebook gradeBook) {
		this.gradeBook = gradeBook;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public int compareTo(CourseActivity arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}

	public Integer getActivityScore() {
		return activityScore;
	}

	public void setActivityScore(Integer activityScore) {
		this.activityScore = activityScore;
	}

}
