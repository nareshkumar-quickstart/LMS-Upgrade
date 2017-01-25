package com.softech.vu360.lms.web.controller.model.survey;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 * 05 August, 2010
 *
 */
public class AvailablePersonalInformationfield  implements ILMSBaseInterface, Comparable<AvailablePersonalInformationfield>{
	private static final long serialVersionUID = 1L;
	private com.softech.vu360.lms.model.AvailablePersonalInformationfield availablePersonalInformationfieldRef;
	private String answerText = "" ;
	private int displayOrder=0;
	
	public AvailablePersonalInformationfield(com.softech.vu360.lms.model.AvailablePersonalInformationfield availablePersonalInformationfieldRef) {
		super();
		this.availablePersonalInformationfieldRef = availablePersonalInformationfieldRef;
	}

	/**
	 * @return the availablePersonalInformationfieldRef
	 */
	public com.softech.vu360.lms.model.AvailablePersonalInformationfield getAvailablePersonalInformationfieldRef() {
		return availablePersonalInformationfieldRef;
	}

	/**
	 * @param availablePersonalInformationfieldRef the availablePersonalInformationfieldRef to set
	 */
	public void setAvailablePersonalInformationfieldRef(
			com.softech.vu360.lms.model.AvailablePersonalInformationfield availablePersonalInformationfieldRef) {
		this.availablePersonalInformationfieldRef = availablePersonalInformationfieldRef;
	}

	/**
	 * @return the answerText
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * @param answerText the answerText to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	
	public int compareTo(AvailablePersonalInformationfield ob1){
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  ob1.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
}
