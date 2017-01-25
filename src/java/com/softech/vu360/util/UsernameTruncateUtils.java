/**
 * 
 */
package com.softech.vu360.util;

/**
 * @author syed.mahmood
 *
 */


public class UsernameTruncateUtils {

	/**
	 * return the truncated word with respect to the maxLength provided
	 * @param truncateMe
	 * @param maxLength
	 * @param suffix
	 * @param truncateAtWord
	 * @return
	 */
    public static String truncate(String truncateMe, int maxLength, String suffix, boolean truncateAtWord) {
        if (truncateMe == null || maxLength <= 0) {
            return null;
        }

        if (truncateMe.length() <= maxLength) {
            return truncateMe;
        }
        if (suffix == null || maxLength - suffix.length() <= 0) {
            // either no need or no room for suffix
            return truncateMe.substring(0, maxLength);
        }
        if (truncateAtWord) {
            // find the latest space within maxLength
            int lastSpace = truncateMe.substring(0, maxLength + 1).lastIndexOf(" ");
            if (lastSpace > suffix.length()) {
                return truncateMe.substring(0, lastSpace) + suffix;
            }
        }
        // truncate to exact character and append suffix
        return truncateMe.substring(0, maxLength) + suffix;

    }

}
