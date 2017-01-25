/**
 * 
 */
package com.softech.vu360.lms.web.controller.model.creditreportingfield;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.LearnerProfileStaticField;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultipleLineTextCreditReportingfield;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SingleLineTextCreditReportingFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Arijit
 *
 */
public class CreditReportingFieldBuilder  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static final String SINGLE_LINE_TEXT_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleLineTextCreditReportingField.vm";
	private static final String STATE_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/stateCreditReportingField.vm";	
	private static final String COUNTRY_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/countryCreditReportingField.vm";
	private static final String SINGLE_LINE_NUMERIC_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleLineNumericCreditReportingField.vm";
	private static final String SINGLE_LINE_SSN_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleLineSSNCreditReportingField.vm";
	private static final String MULTIPLE_LINE_TEXT_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/multipleLineTextCreditReportingField.vm";
	private static final String DATE_TIME_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/dateTimeCreditReportingField.vm";
	private static final String SINGLE_SELECT_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleSelectCreditReportingField.vm";
	private static final String MULTI_SELECT_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/multiSelectCreditReportingField.vm";
	private static final String MULTI_SELECT_COMBO_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/multiSelectComboCreditReportingField.vm";
	private static final String STATIC_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleLineTextCreditReportingField.vm";
	private static final String TELEPHONE_NUMBER_CREDIT_REPORTING_FIELD_TEMPLATE="vm/accreditation/CreditReportingFieldTemplate/singleLineTelephoneNumberCreditReportingField.vm";
	private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFieldList = new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>();

	/**
	 * @return the creditReportingFieldList
	 */
	public List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> getCreditReportingFieldList() {
		return creditReportingFieldList;
	}

	/**
	 * @param creditReportingFieldList the creditReportingFieldList to set
	 */
	public void setCreditReportingFieldList(
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFieldList) {
		this.creditReportingFieldList = creditReportingFieldList;
	}
	
	public void buildCreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingField,
			int fieldStatus,List<CreditReportingFieldValue> creditReportingFieldValues){
		this.buildCreditReportingField(creditReportingField,fieldStatus, creditReportingFieldValues, null);
	}
	
	@SuppressWarnings("static-access")
	public void buildCreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingField,
			int fieldStatus,List<CreditReportingFieldValue> creditReportingFieldValues,
			List<CreditReportingFieldValueChoice> creditReportingFieldValueChoices){
		
		com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field=null;
		
		CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, creditReportingFieldValues);
		/*At this point, write code to pull data from learner 
		 * */
		if(creditReportingField instanceof SingleLineTextCreditReportingFiled){
			
			SingleLineTextCreditReportingFiled singleLineTextCreditReportingFiled = (SingleLineTextCreditReportingFiled)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(singleLineTextCreditReportingFiled,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.SINGLE_LINE_TEXT_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}else if(creditReportingField instanceof NumericCreditReportingField){
			
			NumericCreditReportingField numericCreditReportingField = (NumericCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(numericCreditReportingField,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.SINGLE_LINE_NUMERIC_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}else if(creditReportingField instanceof SSNCreditReportingFiled){
			
			SSNCreditReportingFiled sSNCreditReportingFiled = (SSNCreditReportingFiled)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(sSNCreditReportingFiled,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.SINGLE_LINE_SSN_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}else if(creditReportingField instanceof DateTimeCreditReportingField){
			
			DateTimeCreditReportingField dateTimeCreditReportingField = (DateTimeCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(dateTimeCreditReportingField,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.DATE_TIME_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}else if(creditReportingField instanceof MultipleLineTextCreditReportingfield){
			
			MultipleLineTextCreditReportingfield multipleLineTextCreditReportingfield = (MultipleLineTextCreditReportingfield)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(multipleLineTextCreditReportingfield,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.MULTIPLE_LINE_TEXT_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}else if(creditReportingField instanceof SingleSelectCreditReportingField){
			
			SingleSelectCreditReportingField singleSelectCreditReportingField = (SingleSelectCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(singleSelectCreditReportingField,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.SINGLE_SELECT_CREDIT_REPORTING_FIELD_TEMPLATE);
			
			if (creditReportingFieldValueChoices!=null){
				for (CreditReportingFieldValueChoice creditReportingFieldValueChoice: creditReportingFieldValueChoices){
					com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice choice = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice(creditReportingFieldValueChoice);
					field.addCreditReportingFieldValueChoice(choice);
				}
			}
			
		}else if(creditReportingField instanceof MultiSelectCreditReportingField){
			
			MultiSelectCreditReportingField multiSelectCreditReportingField = (MultiSelectCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(multiSelectCreditReportingField,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			
			if(multiSelectCreditReportingField.isCheckBox()){
				field.setTemplatePath(this.MULTI_SELECT_CREDIT_REPORTING_FIELD_TEMPLATE);
			}else{
				field.setTemplatePath(this.MULTI_SELECT_COMBO_CREDIT_REPORTING_FIELD_TEMPLATE);
			}
			
			if (creditReportingFieldValueChoices!=null){
				for (CreditReportingFieldValueChoice creditReportingFieldValueChoice: creditReportingFieldValueChoices){
					com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice choice = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice(creditReportingFieldValueChoice);
					field.addCreditReportingFieldValueChoice(choice);
				}
			}
			
		}else if(creditReportingField instanceof StaticCreditReportingField){
			
			StaticCreditReportingField staticCreditReportingFiled = (StaticCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(staticCreditReportingFiled,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			if(field.getCreditReportingFieldRef().getFieldLabel().equals(LearnerProfileStaticField.STATE.toString()) ||
					field.getCreditReportingFieldRef().getFieldLabel().equals(LearnerProfileStaticField.STATE_2.toString())){
				field.setTemplatePath(this.STATE_CREDIT_REPORTING_FIELD_TEMPLATE);
			}else if(field.getCreditReportingFieldRef().getFieldLabel().equals(LearnerProfileStaticField.COUNTRY.toString()) ||
					field.getCreditReportingFieldRef().getFieldLabel().equals(LearnerProfileStaticField.COUNTRY_2.toString())){
				    field.setTemplatePath(this.COUNTRY_CREDIT_REPORTING_FIELD_TEMPLATE);
			}else{
			  field.setTemplatePath(this.SINGLE_LINE_TEXT_CREDIT_REPORTING_FIELD_TEMPLATE);
			}
			
		}
		else if(creditReportingField instanceof TelephoneNumberCreditReportingField){
			
			TelephoneNumberCreditReportingField telephoneNumberCreditReportingField = (TelephoneNumberCreditReportingField)creditReportingField;
			field = new com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField(telephoneNumberCreditReportingField,creditReportingFieldValue);
			field.setStatus(fieldStatus);
			field.setTemplatePath(this.TELEPHONE_NUMBER_CREDIT_REPORTING_FIELD_TEMPLATE);
			
		}
		
		if(field!=null){
			this.creditReportingFieldList.add(field);
		}
		
	}
	
	private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingField,
			List<CreditReportingFieldValue> creditReportingFieldValues){
		if (creditReportingFieldValues != null){
			for (CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues){
				if (creditReportingFieldValue.getReportingCustomField()!=null){
					if (creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId())==0){
						creditReportingFieldValue.setReportingCustomField(creditReportingField);
						return creditReportingFieldValue;
					}
				}
			}
		}
		CreditReportingFieldValue newCreditReportingField= new CreditReportingFieldValue();
		newCreditReportingField.setReportingCustomField(creditReportingField );
		return newCreditReportingField;
	}
	
}
