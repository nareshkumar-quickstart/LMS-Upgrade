/**
 * 
 */
package com.softech.vu360.lms.web.controller.model.creditreportingfield;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Arijit
 *
 */
public class CreditReportingFieldValueChoice  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public CreditReportingFieldValueChoice(com.softech.vu360.lms.model.CreditReportingFieldValueChoice creditReportingFieldValueChoiceRef){
		super();
		this.creditReportingFieldValueChoiceRef=creditReportingFieldValueChoiceRef;
	}
	
	private com.softech.vu360.lms.model.CreditReportingFieldValueChoice creditReportingFieldValueChoiceRef;
	private boolean selected = false;

	/**
	 * @return the creditReportingFieldValueChoiceRef
	 */
	public com.softech.vu360.lms.model.CreditReportingFieldValueChoice getCreditReportingFieldValueChoiceRef() {
		return creditReportingFieldValueChoiceRef;
	}

	/**
	 * @param creditReportingFieldValueChoiceRef the creditReportingFieldValueChoiceRef to set
	 */
	public void setCreditReportingFieldValueChoiceRef(
			com.softech.vu360.lms.model.CreditReportingFieldValueChoice creditReportingFieldValueChoiceRef) {
		this.creditReportingFieldValueChoiceRef = creditReportingFieldValueChoiceRef;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
