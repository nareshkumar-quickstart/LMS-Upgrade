package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageLearnerResult  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private List<Integer> score = new ArrayList<Integer>();
	
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the score
	 */
	public List<Integer> getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(List<Integer> score) {
		this.score = score;
	}
	

}
