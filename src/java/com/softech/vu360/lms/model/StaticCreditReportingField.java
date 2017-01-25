/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("STATICCREDITREPORTINGFIELD")
public class StaticCreditReportingField extends CreditReportingField {
	public StaticCreditReportingField() {
		super();		
		
	}
			
	public void assignWeightToField(){
		if(this.getFieldLabel().equals(
				LearnerProfileStaticField.FIRST_NAME.toString())){
				this.setWeight(1);
		}else if(this.getFieldLabel().equals(
				LearnerProfileStaticField.MIDDLE_NAME.toString())){
				this.setWeight(2);
		}else if(this.getFieldLabel().equals(
				LearnerProfileStaticField.EMAIL.toString())){
				this.setWeight(3);
		}else if(this.getFieldLabel().equals(
				LearnerProfileStaticField.PHONE.toString())){
				this.setWeight(4);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.OFFICE_PHONE.toString())){
				this.setWeight(5);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.OFFICE_EXTN.toString())){
				this.setWeight(6);
		}else if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ADDRESS_1_LINE_1.toString())){
				this.setWeight(7);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ADDRESS_1_LINE_2.toString())){
				this.setWeight(8);
		}else   if(this.getFieldLabel().equals(
				LearnerProfileStaticField.CITY.toString())){
				this.setWeight(9);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.STATE.toString())){
				this.setWeight(10);
		}else   if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ZIP_CODE.toString())){
				this.setWeight(11);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.COUNTRY.toString())){
				this.setWeight(12);
		}else   if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ADDRESS_2_LINE_1.toString())){
			this.setWeight(13);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ADDRESS_2_LINE_2.toString())){
				this.setWeight(14);
		}else   if(this.getFieldLabel().equals(
				LearnerProfileStaticField.CITY_2.toString())){
				this.setWeight(15);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.STATE_2.toString())){
				this.setWeight(16);
		}else   if(this.getFieldLabel().equals(
				LearnerProfileStaticField.ZIP_CODE_2.toString())){
				this.setWeight(17);
		}else  if(this.getFieldLabel().equals(
				LearnerProfileStaticField.COUNTRY_2.toString())){
				this.setWeight(18);
		} 
		
	}
}
