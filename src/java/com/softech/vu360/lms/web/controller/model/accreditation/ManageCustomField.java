package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageCustomField  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private long id;
	private String fieldName;
	private String fieldType;
	private boolean selected;
	
	/*Only used in case this is a reporting Field.*/
	//private int containingCourseApprovalCount;
	
	/*Only used in case this is a reporting Field.*/
	private int containingRegulatorCategoryCount;

	//This should be true if atleast one learner has entered data against a reporting field.
	private boolean inUseByLearners;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
	
	/*Only used in case this is a reporting Field.*/
	/*public int getContainingCourseApprovalCount() {
		return containingCourseApprovalCount;
	}*/
	/*Only used in case this is a reporting Field.*/
	/*public void setContainingCourseApprovalCount(
			int containingCourseApprovalCount) {
		this.containingCourseApprovalCount = containingCourseApprovalCount;
	}*/
	
	
	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	public void copy (CustomField customField) {
		
		this.id = customField.getId();
		this.fieldName = customField.getFieldLabel();		
		
		if (customField instanceof SingleLineTextCustomFiled) {
			this.fieldType = CredentialForm.CUSTOMFIELD_SINGLE_LINE_OF_TEXT;
		} else if (customField instanceof DateTimeCustomField) {
			this.fieldType = CredentialForm.CUSTOMFIELD_DATE;
		} else if (customField instanceof MultipleLineTextCustomfield) {
			this.fieldType = CredentialForm.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT;
		} else if (customField instanceof NumericCusomField) {
			this.fieldType = CredentialForm.CUSTOMFIELD_NUMBER;
		} else if (customField instanceof SSNCustomFiled) {
			this.fieldType = CredentialForm.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER;
		} else if (customField instanceof SingleSelectCustomField) {
			this.fieldType = CredentialForm.CUSTOMFIELD_RADIO_BUTTON;
		} else if (customField instanceof MultiSelectCustomField) {
			if (((MultiSelectCustomField) customField).getCheckBox())
				this.fieldType = CredentialForm.CUSTOMFIELD_CHECK_BOX;
			else 
				this.fieldType = CredentialForm.CUSTOMFIELD_CHOOSE;
		}
	}
	public int getContainingRegulatorCategoryCount() {
		return containingRegulatorCategoryCount;
	}
	public void setContainingRegulatorCategoryCount(
			int containingRegulatorCategoryCount) {
		this.containingRegulatorCategoryCount = containingRegulatorCategoryCount;
	}
	public boolean isInUseByLearners() {
		return inUseByLearners;
	}
	public void setInUseByLearners(boolean inUseByLearners) {
		this.inUseByLearners = inUseByLearners;
	}
	
	
}
