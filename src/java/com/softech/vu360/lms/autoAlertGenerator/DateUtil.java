package com.softech.vu360.lms.autoAlertGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
	static String[] monthArr = {"January", "February" , "March" , "April" , "May" , "June" , "July", "August", "September", "October", "Novemeber", "December"};
	
	public static boolean isTodayAWeekDay(){
		Calendar calendar = Calendar.getInstance();
		Calendar xmas = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		int dayOfWeek = xmas.get(Calendar.DAY_OF_WEEK);    // 6=Friday
		
		if(dayOfWeek >1 && dayOfWeek<7){
			return true;
		}else{
			return false;
		}
//[NOTE: 0=sunday , 7=saturday]
		
	}
	
	public static boolean isTodayAWeekEnd(){
		Calendar calendar = Calendar.getInstance();
		Calendar xmas = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		int dayOfWeek = xmas.get(Calendar.DAY_OF_WEEK);    // 6=Friday
		
		if(dayOfWeek == 0 || dayOfWeek == 7){
			return true;
		}else{
			return false;
		}
//[NOTE: 0=sunday , 7=saturday]
		
	}
	
	public static boolean isTodayTheExactWeekDay(int weekday){
		Calendar calendar = Calendar.getInstance();
		Calendar xmas = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		int dayOfWeek = xmas.get(Calendar.DAY_OF_WEEK);    // 6=Friday
		
		if(dayOfWeek == weekday){
			return true;
		}else{
			return false;
		}
//[NOTE: 0=sunday , 7=saturday]
		
	}
	
	
	public static long getTodaysDayDifferenceFromAParticularDate( Date particularDate){
		
		Calendar calendar = Calendar.getInstance();
		Calendar calToday = Calendar.getInstance();
        Calendar calParticular = Calendar.getInstance();
         
        // Set the date for both of the calendar instance
        calToday.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calParticular.set( (particularDate.getYear()+1900), particularDate.getMonth(), particularDate.getDay());
        
        // Get the represented date in milliseconds
        long milis1 = calToday.getTimeInMillis();
        long milis2 = calParticular.getTimeInMillis();
        
        // Calculate difference in milliseconds
	    long diff = milis2 - milis1;
	    
	    // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);
        
        return diffDays;
	}
	
	
	public static long getDateDifference(Date firstDate, Date secondDate){
		  Calendar cal1 = Calendar.getInstance();cal1.setTime(firstDate);  
		  Calendar cal2 = Calendar.getInstance();cal2.setTime(secondDate);  
		 
		  return (firstDate.getTime() - secondDate.getTime()) / (24 * 60 * 60 * 1000); 
	}

	public static boolean isEndDateGreaterThanStartDate(Date startDate , Date endDate){
		
		Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
         
        // Set the date for both of the calendar instance
        calStart.set((startDate.getYear()+1900), startDate.getMonth(), startDate.getDay());
        calEnd.set( (endDate.getYear()+1900), endDate.getMonth(), endDate.getDay());
        
        // Get the represented date in milliseconds
        long milisStart = calStart.getTimeInMillis();
        long milisEnd = calEnd.getTimeInMillis();
        
        // Calculate difference in milliseconds
	    //long diff = milisEnd - milisStart;
        
        if(milisEnd > milisStart){
        	return true;
        }
        return false;
	}
	
	public static int getMonthIndexFromMonthArray(String month){
		
		for(int i=0;i<monthArr.length;i++){
			if(monthArr[i].equalsIgnoreCase(month)){
				return i;
			}
		}
		return -1;
		
	}
	
	
	
	public int getMonthOfYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH);
	}
	
	
}
