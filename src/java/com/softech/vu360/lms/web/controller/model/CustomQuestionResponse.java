package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class CustomQuestionResponse implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private String label = "";
	private String type = "";
	private boolean isCharacter = false;
	private String answerChoice = "";
	private boolean isRequired = false;
	
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
	/**
	 * @return the isCharacter
	 */
	public boolean isCharacter() {
		return isCharacter;
	}
	/**
	 * @param isCharacter the isCharacter to set
	 */
	public void setCharacter(boolean isCharacter) {
		this.isCharacter = isCharacter;
	}
	/**
	 * @return the answerChoice
	 */
	public String getAnswerChoice() {
		return answerChoice;
	}
	/**
	 * @param answerChoice the answerChoice to set
	 */
	public void setAnswerChoice(String answerChoice) {
		this.answerChoice = answerChoice;
	}
	/**
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}
	/**
	 * @param isRequired the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	
	
}
