package com.softech.vu360.util;

import java.util.UUID;


/**
 * @author jason
 * 
 */
public class GUIDGeneratorUtil {
	
	public static String generateGUID() {
		
		return UUID.randomUUID().toString();
		
	}
	
	public static String generateGUIDForCourseAndCourseGroup() {
		
		return UUID.randomUUID().toString().replace("-", "");
		
	}

	 public static String generatePassword(){
		 
		  {
		    final int PASSWORD_LENGTH = 8;
		    StringBuffer sb = new StringBuffer();
		    for (int x = 0; x < PASSWORD_LENGTH-1; x++)
		    {
		      sb.append((char)((int)(Math.random()*26)+97));
		      
		    }
		    sb.append(1);
		    return (sb.toString());
		  }
	 }
	 
	 public static String generateProctorPassword(){
		 
		  {
		    final int PASSWORD_LENGTH = 8;
		    StringBuffer sb = new StringBuffer();
//		    LMS-14806
//		    for (int x = 0; x < PASSWORD_LENGTH-1; x++)
		    for (int x = 0; x <= PASSWORD_LENGTH-1; x++)
		    {
		      sb.append((char)((int)(Math.random()*26)+97));
		      
		    }
//		    sb.append(1);
//		    return (sb.toString());
		    return (sb.toString().toUpperCase());
		  }
	 }
	

}
