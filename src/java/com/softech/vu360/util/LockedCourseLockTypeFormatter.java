/**
 * 
 */
package com.softech.vu360.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rehan.rana
 *
 */


public class LockedCourseLockTypeFormatter {
	
	public static Map<String, String> strLT = new HashMap<String, String>();
	
	public LockedCourseLockTypeFormatter(){
		System.out.println("--------------Entering Contructor LockedCourseLockTypeFormatter --------");
		strLT.put("FailedCompletionMustCompleteWithinSpecificAmountOfTimeMinute", 
						"Failed Completion Must Complete Within Specific Amount Of Time Minute");
		
		strLT.put("FailedCompletionMustCompleteWithinSpecificAmountOfTimeAfterRegistration", 
						"Failed Completion Must Complete Within Specific Amount Of Time After Registration");
		
		strLT.put("ProctorLoginFailed", "Proctor Login Failed");
		strLT.put("ProctorNotPartOfCredential", "Proctor Not Part Of Credential");
		strLT.put("MaxAttemptReachPostAssessment", "Max Attempt Reach Post Assessment");
		strLT.put("MaxAttemptReachLessonAssessment", "Max Attempt Reach Lesson Assessment");
		strLT.put("IdleUserTimeElapsed", "Idle User Time Elapsed");
		
		strLT.put("MustStartCourseWithinSpecificAmountOfTimeAfterRegistration", 
				"Must Start Course Within Specific Amount Of Time After Registration");
		
		strLT.put("MaxAttemptReach", "Max Attempt Reach");
		strLT.put("ValidationFailed", "Validation Failed");
		strLT.put("ProctorAccountNotActive", "Proctor Account Not Active");
		strLT.put("MaxAttemptReachPreAssessment", "Max Attempt Reach Pre Assessment");
		strLT.put("MaxAttemptReachPracticeExam", "Max Attempt Reach Practice Exam");
	}
	
	/**
	 * return the Formatted word with respect to the LockType provided
	 * @param strLockType
	 * @return
	 */
    public static String format(String strLockType) {
    	
    	if (strLockType == null ) 
            return null;
        
        else if (strLT.get(strLockType) != null ) 
            return strLT.get(strLockType);
        
        else  
            return strLockType;

    }
    
    public static String escapeSpecialCharacters(String varStr){
    	Pattern pattern = Pattern.compile("[/'`.,;:<>!~@#*$%^&()+=?()\"|!\\[#$-]");  
		Matcher matcher = pattern.matcher(varStr);
		
		return matcher.replaceAll("");
    }

}
