package com.softech.vu360.lms.web.controller.validator;

/**
 * Contains reusable validation utility methods to be used
 * accross different validators.
 * 
 * @author abdul.aziz
 */
public final class ValidationUtil {

    /**
     * Tests if the decimals in the hour are less than 2.
     * 
     * @param strHour
     * @return true if validations are passed.
     */
    public static Boolean isValidHourWithValidDecimals(String strHour) {
        //TODO: Think regex.
        int decimalLocation = strHour.indexOf(".");
        int decimals = -1;
        if (decimalLocation > 0) {
            decimals = strHour.substring(strHour.indexOf(".") + 1).length();
        }
        
        //TODO: Do we need to externalize valid number of decimals?
        if( decimals < 3){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Tests if the string is valid decimal.
     * 
     * @param strHour
     * @return true if validations are passed.
     */
    public static Boolean isValidDecimal(String str) {
        return str.matches("^\\d*\\.?\\d*$");
    }
}
