/**
 * 
 */
package com.softech.vu360.lms.util;

/**
 * @author Zeeshan Hanif
 *
 * This class will used as utility to change or create format of data provided  
 */
public class TextFormatter {

	
	
	/**
	 * @param telephoneNumber
	 * @return String
	 * 
	 * This method will convert telephone number with of formate of (xxx) xxx-xxxx into xxxxxxxxxx
	 * Object is to replace hyphen, space and round brackets from telephonen number
	 * 
	 */
	public static String convertTelephoneToNumber(String telephoneNumber){
		
		return telephoneNumber.replaceAll("[(]", "").replace(")","").replace(" ", "").replace("-", "");
		
	}
	
}
