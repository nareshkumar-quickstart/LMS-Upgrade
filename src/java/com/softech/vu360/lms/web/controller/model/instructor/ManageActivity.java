package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.SearchableKey;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageActivity implements Comparable<ManageActivity>,SearchableKey {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name = null;
	private String type = null;
	private Learner learner=null;
	private List<LearnerCourseActivity> learnerCourseActivities=new ArrayList<LearnerCourseActivity>();
	private int displayOrder = 0; 
	private List<String> types = new ArrayList<String>();
	private int activityScore = 0; 
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
	 * @return the name
	 */
	
	public String getKey(){
		// TODO Auto-generated method stub
		return id.toString();
	}

	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	
	
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public List<String> getTypes() {
		return types;
	}
	 public void addType(String type) {
		 types.add(type);
	    }
	public void setLearnerCourseActivities(List<LearnerCourseActivity> learnerCourseActivities) {
		this.learnerCourseActivities = learnerCourseActivities;
	}
	public List<LearnerCourseActivity> getLearnerCourseActivities() {
		return learnerCourseActivities;
	}
	public void addLearnerCourseActivity(LearnerCourseActivity learnerCourseActivity) {
		this.learnerCourseActivities.add(learnerCourseActivity) ;
	}
	public int compareTo(ManageActivity arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public int getActivityScore() {
		return activityScore;
	}
	public void setActivityScore(int activityScore) {
		this.activityScore = activityScore;
	}
	
}
