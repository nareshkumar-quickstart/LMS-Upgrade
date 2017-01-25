/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
 
/**
 * @author Adeel Hussain
 *
 */
  public class ZipCodeValidator {
	//private static final Logger log = Logger.getLogger(AddLearnerController.class.getName());

/**
 * @param get the length of zip code of country provided
 */
	  public static int getZipCodeLength(String country , Brander brander){
		
		String zipCodeLength =  "0";
		if(brander != null){	
	        List<LabelBean>  countries = brander.getBrandMapElements("lms.manageUser.AddLearner.Country");
	        
	        for (LabelBean bean : countries ){
        
	        	if( country.equalsIgnoreCase(bean.getValue()) || country.equalsIgnoreCase(bean.getLabel())){
	        		zipCodeLength =  brander.getBrandElement("lms.manageUser.AddLearner.zip."+bean.getValue()+".length");
	        		break;
	        	}
	        } 	 
			
		}
		if (zipCodeLength==null || zipCodeLength.isEmpty()){
			zipCodeLength = "0";
		}
		int zipCodeCount = Integer.parseInt(zipCodeLength); 	
		return zipCodeCount;
		
	}

/**
 * @param get the type of zip code of country provided
 */

	  public static String getZipCodeType(String country , Brander brander){
		
		String zipCodeType =  "";
        List<LabelBean>  countries = brander.getBrandMapElements("lms.manageUser.AddLearner.Country");
        
        for (LabelBean bean : countries ){
        
        	if( country.equalsIgnoreCase(bean.getValue())){
        		zipCodeType =  brander.getBrandElement("lms.manageUser.AddLearner.zip."+bean.getValue()+".type");
        		break;
        	}	
        } 	

		return zipCodeType;	
	}
 
/**
 * @param get the Error of zip code of country provided
 */	
	public static String getCountryZipCodeError(String country , Brander brander){
		
		String zipCodeType =  "";
        List<LabelBean>  countries = brander.getBrandMapElements("lms.manageUser.AddLearner.Country");
        
        for (LabelBean bean : countries ){
        
        	if( country.equalsIgnoreCase(bean.getValue())){
        		zipCodeType =  "error.ZipCode."+bean.getValue() ;
        		break;
        	}	
        } 	

		return zipCodeType;	
	}

	/**
	 * @param get the regular expression of zip code of country provided
	 */
	public static String getCountryexpression(String country , Brander brander){
		
		String zipCodeExpression =  "";
        //String [] countries = brander.getBrandElements("lms.manageUser.AddLearner.Country");
        	
    	if( country.equalsIgnoreCase("GB") || country.equalsIgnoreCase("United Kingdom")){	
    		//^([A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]|[A-HK-Y][0-9]([0-9]|[ABEHMNPRV-Y]))|[0-9][A-HJKS-UW])\ [0-9][ABD-HJLNP-UW-Z]{2}|(GIR\ 0AA)|(SAN\ TA1)|(BFPO\ (C\/O\ )?[0-9]{1,4})|((ASCN|BBND|[BFS]IQQ|PCRN|STHL|TDCU|TKCA)\ 1ZZ))$
    		zipCodeExpression =  "^([A-Za-z0-9][A-Za-z0-9][A-Za-z0-9]?[A-Za-z0-9]? {1,2}[0-9][A-Za-z]{2}|GIR 0AA)$";
    		 
    	}
    	else if( country.equalsIgnoreCase("US") || country.equalsIgnoreCase("United States")){        	
    		zipCodeExpression =  "^(\\d{5})(-\\d{4})?$";
    		
    	}
       	else if( country.equalsIgnoreCase("CA") || country.equalsIgnoreCase("Canada")){   	
       		zipCodeExpression =  "^[A-Za-z]{1}\\d{1}[A-Za-z]{1} *\\d{1}[A-Za-z]{1}\\d{1}$";
    		//zipCodeExpression =  "^[A-Za-z]{1}\\d{1}[A-Za-z]{1} \\d{1}[A-Za-z]{1}\\d{1}$";
       	}
       	else if( country.equalsIgnoreCase("AU") || country.equalsIgnoreCase("Australia")){	
    		zipCodeExpression =  "^\\d{4}$";
       	}

        return zipCodeExpression;	
	}

	/**
	 * @author adeel.hussain
	 * @return boolean
	 * @description to centralize the most of Zip code validation 
	 */
	public static boolean isZipCodeValid(String country , String zipCode , Brander brander , Logger log ) {
		
		if(brander != null && StringUtils.isNotBlank(zipCode) )	{
			zipCode = zipCode.trim();

			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			int zipRequiredLength =  ZipCodeValidator.getZipCodeLength(country, brander);
			
			if ( zipRequiredLength > 0 && zipCode.length() > 0 ){
				String expressionString = ZipCodeValidator.getCountryexpression(country, brander );
	            Pattern pattern = Pattern.compile(expressionString);
	            //log.debug("reg exp for some Zip code of " + country+" " + zipCode);	
                Matcher matcher = pattern.matcher(zipCode);
               // log.debug( "   For Expression " + expressionString);
                return matcher.find();
			}
		}	
		return true ; // if Brand object is null
	}
	
}
