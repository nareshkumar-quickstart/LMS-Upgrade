/**
 * 
 */
package com.softech.vu360.lms.model;

/**
 * @author muhammad.taqveem
 *
 */
public enum LearnerProfileStaticField {
	/* These are the static fields in the Learner profile page*/	        
    FIRST_NAME("First Name"),                              
    MIDDLE_NAME("Middle Name"),
    LAST_NAME("Last Name"), 
    EMAIL("Email"),
    PHONE("Phone"),
    OFFICE_PHONE("Office Phone"),
    OFFICE_EXTN("Office Extn"),
    ADDRESS_1_LINE_1("Address Line 1"),
    ADDRESS_1_LINE_2("Address Line 2"),
    CITY("City"),
    STATE("State"),
    ZIP_CODE("Zip Code"),
    COUNTRY("Country"),
    ADDRESS_2_LINE_1("Address 2 Line 1"),
    ADDRESS_2_LINE_2("Address 2 Line 2"),
    CITY_2("City 2"),
    STATE_2("State 2"),
    ZIP_CODE_2("Zip Code 2"),
    COUNTRY_2("Country 2");
    
    private final String name;
	public String toString()  {
	        return name;
	  }
	
	public String[] getStringArray(){
		String[] array= new String[LearnerProfileStaticField.values().length];
		int index= 0;
		for(LearnerProfileStaticField field: LearnerProfileStaticField.values()){
			array[index++]=field.toString();
		}
		return array;
	}
	
	public static  Boolean contains(String name){
		for(LearnerProfileStaticField field: LearnerProfileStaticField.values()){
			if(field.toString().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	LearnerProfileStaticField( String name ){	            
	      this.name = name;
	 }
    
		
}
