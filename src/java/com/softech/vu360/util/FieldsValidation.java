package com.softech.vu360.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FieldsValidation{

	private static final String ALPHA_NUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
	
 
 /**
 * This method  checks if the input email address is a valid email addrees.
 * Email format: A valid email address will have following format 
   [\\w\\.-]+ : Begins with word characters, (may include periods and hypens).
   @: It must have a '@' symbol after initial characters.
   ([\\w\\-]+\\.)+ : @ must follow by more alphanumeric characters (may include hypens.). This part must also have a "." to separate domain and subdomain names.
   [A-Z]{2,4}$: Must end with two to four alaphabets. (This will allow domain names with 2, 3 and 4 characters e.g pa, com, net, wxyz)
 * @param email String. Email adress to validate
 * @return boolean: true if email address is valid, false otherwise.
 */
 public static boolean isEmailValid(String email){
	 
  boolean isValid = false;
  String expression = "^[\'\\p{L}\\w\\.-]+@([\\p{L}\\w\\-]+\\.)+[A-Z]{2,}$";  
  CharSequence inputStr = email;

  //Make the comparison case-insensitive. 
  Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
  Matcher matcher = pattern.matcher(inputStr);
	 
  if(matcher.matches()){
	  isValid = true;
  }

 	return isValid;
 }
 
 /**
  * This method  checks if the input email address is a valid email addrees. followed by Name
  * as in pattern "Sender Name <sender@xyz.abc>"
  * @param email
  * @return
  */
 public static boolean isEmailWithNameValid(String email){
	  boolean isValid = false;
	  if(email.contains("<")){
		  if(email.contains(">")){
			  String fromName=email.substring(0, email.indexOf("<"));
			  String fromEmail=email.substring(email.indexOf("<")+1, email.indexOf(">"));
			  String junk=email.substring(email.indexOf(">")+1);
			  if(junk.trim().isEmpty()){
				  isValid=isEmailValid(fromEmail);
			  }else{
				  isValid=false;
			  }
			  
		  }else{
			  isValid=false;
		  }
	  }else{
		  isValid=isEmailValid(email);
	  }
	  return isValid;
 }
 
 /**
 * This method  checks if the input phone number is a valid phone number.
 * @param email String. Phone number to validate
 * @return boolean: true if phone number is valid, false otherwise.
 */
 public static boolean isPhoneNumberValid(String phoneNumber){
   boolean isValid = false;
   /* Phone Number format:
        ^\\(? : May start with an option "(" .
	(\\d{3}): Followed by 3 digits.
	\\)? : May have an optional ")"
	[- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
	(\\d{3}) : Followed by 3 digits.
	[- ]? : May have another optional "-" after numeric digits.
	(\\d{4})$: ends with four digits.
	Matches following:
	 (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
	
   */
   String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
   CharSequence inputStr = phoneNumber;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }
 
 
 /**
  * This method will validate the phone number in (123)456-7890, (123)-456-7890 format.
  * @param phoneNumber
  * @return true if phone number is valid, false otherwise.
  */
 	public static boolean isValidBracketFormattedPhoneNumber(String phoneNumber){
	   boolean isValid = false;
	   /* Phone Number format:
	        ^\\(? : May start with an option "(" .
		(\\d{3}): Followed by 3 digits.
		\\)? : May have an optional ")"
		[- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
		(\\d{3}) : Followed by 3 digits.
		[- ]? : May have another optional "-" after numeric digits.
		(\\d{4})$: ends with four digits.
		Matches following:
		 (123)456-7890, (123)-456-7890
		
	   */
	   String expression = "^\\((\\d{3})\\)[- ]?(\\d{3})[- ](\\d{4})$";
	   CharSequence inputStr = phoneNumber;
	   Pattern pattern = Pattern.compile(expression);
	   Matcher matcher = pattern.matcher(inputStr);
	   if(matcher.matches()){
	   isValid = true;
	   }
	   return isValid; 
	 }
 
 /**
 * This method  checks if the input social security number is valid.
 * @param email String. Social Security number to validate
 * @return boolean: true if social security number is valid, false otherwise.
 */
 public static boolean isSSNValid(String ssn){
   boolean isValid = false;
   //SSN format:
   /*
       ^\\d{3} : Starts with three numeric digits.
	[- ]?   : Followed by an optional - and space
	\\d{2}: Two numeric digits after the optional "-"
	[- ]? : May contains an optional second "-" character.
	\\d{4}:  ends with four numeric digits.
   */
   String expression = "^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$";
   CharSequence inputStr = ssn;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }
 
  /**
 * This method  checks if the input text contains all numeric characters.
 * @param email String. Number to validate
 * @return boolean: true if the input is all numeric, false otherwise.
 */
 public static boolean isNumeric(String number){
   boolean isValid = false;
   //Number:
   /*
       ^\\d{3} : Starts with three numeric digits.
	[- ]?   : Followed by an optional - and space
	\\d{2}: Two numeric digits after the optional "-"
	[- ]? : May contains an optional second "-" character.
	\\d{4}:  ends with four numeric digits.
   */
   String expression = "[-+]?[0-9]*\\.?[0-9]+$";
   //expression = "/^(\(\\d+\) ?)?(\d+[\- ])*\d+$/";
   //expression = "^\\d{3}$";
   CharSequence inputStr = number;
   Pattern pattern = Pattern.compile(expression);
   Matcher matcher = pattern.matcher(inputStr);
   if(matcher.matches()){
   isValid = true;
   }
   return isValid; 
 }

 public static boolean isProper(String number){
	 boolean isValid = true;

	 //String INVALID_CHARS = "~!@#$%*()|^-+=:.?=[];\\,<b>";
	 String INVALID_CHARS = "~!@#$%*()|^=:.?=[];\\,<b>";
		for(char INVALIDCHAR:INVALID_CHARS.toCharArray()){
			 if (number.indexOf(INVALIDCHAR) != -1){
				 isValid=false;
			 }
		}
	 return isValid; 
 }
 
 /* KS - 2009-12-08 - Refactored {LMS-3337} 
 public static boolean isPasswordLengthCorrect(String pass){
	 boolean isValid = false;

	 int VALID_LENGTH = 8;

	 if (pass.length()>=VALID_LENGTH){
		 isValid=true;
	 }
	 return isValid; 
	
 }
*/
 public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z\\u00C0-\\u017F])(?=.*[0-9])");
 public static final int 	 VALID_LENGTH = 8;
 
 public static boolean isPasswordCorrect(String pass){
	 
	 boolean isValid = false;

	 if ( pass != null && pass.length() >= VALID_LENGTH ){
		 
		 isValid = PASSWORD_PATTERN.matcher(pass).find();
	 }

	 return isValid; 
 }

 // will be used for First Name Last Name and Middle Name
 /*public static boolean isInValidName(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\'\\-]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
		return isValid;
	}
 */
 /*the difference between above isInValidName and isInValidEntitlementName is the space which is included 
    below(//s)*/
 /*public static boolean isInValidEntitlementName(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\'\\-\\s]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
		return isValid;
	}*/
//will be used for Address
 // System should "alphabets", "numbers", "dashes(-)", "hash(#)", "underscore( _ )", "@", "comma ( , )". 
 public static boolean isInValidAddress(String input){
	/*	Pattern p = Pattern.compile("[^A-Za-z0-9\\,\\-\\@\\_\\#]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();*/
	 
	    /*
	     * no such validation required
	     */
		return false;
	}
 
 public static boolean isInValidGlobalName(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\u00C0-\\u017F\\,\\-\\@\\_\\#\\'\\s\\,\\;\\.\\(\\)]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
	 
	   
		return isValid;
	}

 public static boolean isInValidUsername(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\u00C0-\\u017F\\-\\@\\_\\.\\(\\)]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
	 
	   
		return isValid;
	}

 public static boolean isInValidCustomerName(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\u00C0-\\u017F\\,\\-\\@\\_\\#\\'\\s\\,\\;\\.\\(\\)&]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
	 
	   
		return isValid;
	}

// System should allow "numbers", "alphabets", "dashes(-)" and "brackets ( )".

 public static boolean isInValidOffPhone(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\u00C0-\\u017F\\-\\(\\)]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
		return isValid;
	}
 //System should allow "Numbers" and "dashes( - )". 
 public static boolean isInValidMobPhone(String input){
		Pattern p = Pattern.compile("[^0-9\\'\\-]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
		return isValid;
	}
 public static boolean isValidDate(String date)
	{
	    // set date format, this can be changed to whatever format
	    // you want, MM-dd-yyyy, MM.dd.yyyy, dd.MM.yyyy etc.
	    // you can read more about it here:
	    // http://java.sun.com/j2se/1.4.2/docs/api/index.html
	     String[] dateChunks = date.split("/", 3); // break the date string into 3 leangth array
	    
	     if( dateChunks.length == 3) {  // if it is not of length 3 , then discard it 
	    	 for( int dateIndex = 0 ; dateIndex < dateChunks.length ; dateIndex++){
	    		 int dateInt = 0 ;
	    		 try{
	    			 if(dateIndex == 2 &&  dateChunks[dateIndex].length() == 4  )
	    				 dateInt = Integer.parseInt( dateChunks[dateIndex] ) ;
	    			 else if( dateChunks[dateIndex].length() > 0 &&  dateChunks[dateIndex].length() < 3  )   // if each length is 2 or greater than 0 , then fine 
	    				 dateInt = Integer.parseInt( dateChunks[dateIndex] ) ;
	    			 else 
	    				 return false ;
	    		 }catch(NumberFormatException e){
	    			 e.printStackTrace();
	    			 return false ;
	    		 }
	    	 }
	    	 
	    	 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    	 sdf.setLenient(false);
 
		    // we will now try to parse the string into date form
		    try
		    {
		    	sdf.parse(date);
		    }	    
		    catch (ParseException e)
		    {
		    	e.printStackTrace();
		        return false;
		    }
	    	 
	    	 
	     }
	     else {
	    	 return false ;
	     }
	    	 

	    return true;

	} // end isValidDate
 
 	//To Match Password fields for confirm password. (currently for selfregistration page only)
 	public static boolean isConfirmPasswordMatch(String pass, String confirmPass){
	 boolean isValid = false;

	 if(pass.equals(confirmPass))
		 isValid=true;
	 
	 return isValid; 
	
 	}
 
	public static boolean isInValidRegulatorContactPhone(String input){
		Pattern p = Pattern.compile("[^A-Za-z0-9\\-\\(\\)\\s]");
		Matcher m = p.matcher(input);
		//m.f
		boolean isValid = m.find();
		return isValid;
	}

	public static boolean isNumeric(Integer intNumber){
		boolean isValid = false;
		//Number:
		String number=intNumber.toString();
		String expression = "[-+]?[0-9]*\\.?[0-9]+$";
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches()){
			isValid = true;
		}
		return isValid; 
	}
	
	
	// Sana Majeed | 12/30/2009 | LMS-955
	public static boolean isValidWebURL(String url){
		Pattern pattern = Pattern.compile("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		Matcher matcher = pattern.matcher(url);		
		return matcher.matches();
	}
	
	/**
	 * Tests for alpha-numeric value and returns true if it is a alpha-numeric value. Else false.
	 * @param input	String value to test
	 * @return boolean
	 * @author muzammil.shaikh
	 */
	public static boolean isValidAlphaNumeric(String input){
		
		int inputLength = input.trim().length();
		input = input.trim();
		for(int i=0;i<inputLength;i++){
			if (ALPHA_NUMERIC_CHARACTERS.indexOf(String.valueOf(input.charAt(i))) == -1) {
				return false;
			}
		}
		
		return true;
	}

	
	
}